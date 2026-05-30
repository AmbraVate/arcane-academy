package com.ambravate.arcane.academy.content.seeder;

import com.ambravate.arcane.academy.common.domain.GuidedStep;
import com.ambravate.arcane.academy.common.domain.GuidedStepInputType;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.LessonPracticeType;
import com.ambravate.arcane.academy.common.domain.QuestType;
import com.ambravate.arcane.academy.common.domain.Topic;
import com.ambravate.arcane.academy.content.repository.GuidedStepRepository;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.content.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Seeds lessons authored in Markdown format.
 *
 * <p>Scans {@code classpath:content/**"/*.md} (files starting with {@code _}
 * are skipped — reserved for module-level index files), parses each with
 * {@link MarkdownLessonParser}, and upserts the Module → Topic → Lesson graph.
 *
 * <p>Runs <em>after</em> {@link JsonContentSeeder} in {@link DataSeeder} so
 * Markdown lessons can reference modules that already exist from JSON content.
 * It can also create new modules from frontmatter data, so it works standalone.
 *
 * <p>Idempotent: if every lesson ID found in the .md files is already present
 * in the database, the seeder skips the upsert pass (fast-path on app restart).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MarkdownContentSeeder {

    private final LearningModuleRepository moduleRepository;
    private final LessonRepository         lessonRepository;
    private final TopicRepository          topicRepository;
    private final GuidedStepRepository     guidedStepRepository;
    private final MarkdownLessonParser     parser;
    private final PlatformTransactionManager transactionManager;
    private final ApplicationContext       applicationContext;

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Discovers, parses, and seeds all {@code .md} lesson files.
     *
     * @return the number of lesson files discovered (including any that failed to parse)
     */
    public int seed() throws IOException {
        Resource[] resources = applicationContext.getResources("classpath:content/**/*.md");
        if (resources.length == 0) {
            log.info("[MarkdownContentSeeder] No .md lesson files found — skipping.");
            return 0;
        }

        List<MarkdownLessonDto> parsed = new ArrayList<>();
        List<String> lessonIds = new ArrayList<>();

        for (Resource resource : resources) {
            String filename = resource.getFilename();
            // Skip module-level index files (convention: start with _)
            if (filename != null && filename.startsWith("_")) continue;

            try {
                String content = new String(
                        resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                MarkdownLessonDto dto = parser.parse(content);
                parsed.add(dto);
                lessonIds.add(dto.getId());
                log.debug("[MarkdownContentSeeder] Parsed: {}", filename);
            } catch (Exception e) {
                log.error("[MarkdownContentSeeder] Failed to parse {} — skipping: {}",
                        filename, e.getMessage());
            }
        }

        if (parsed.isEmpty()) {
            log.info("[MarkdownContentSeeder] No valid .md lessons parsed.");
            return resources.length;
        }

        if (allLessonsPresent(lessonIds)) {
            log.info("[MarkdownContentSeeder] All {} Markdown lesson(s) already present — skipping seed.",
                    lessonIds.size());
            return parsed.size();
        }

        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        for (MarkdownLessonDto dto : parsed) {
            tx.executeWithoutResult(status -> upsertLesson(dto));
        }

        log.info("[MarkdownContentSeeder] Seeded {} Markdown lesson(s).", parsed.size());
        return parsed.size();
    }

    // ── Internal ──────────────────────────────────────────────────────────────

    private boolean allLessonsPresent(List<String> ids) {
        if (ids.isEmpty()) return true;
        return lessonRepository.findAllById(ids).size() == ids.size();
    }

    private void upsertLesson(MarkdownLessonDto dto) {
        // 1. Upsert module — create only if absent (don't overwrite JSON-seeded modules)
        moduleRepository.findById(dto.getModuleId()).orElseGet(() -> {
            LearningModule m = LearningModule.builder()
                    .id(dto.getModuleId())
                    .title(dto.getModuleTitle())
                    .glyph(dto.getModuleGlyph())
                    .sortOrder(dto.getModuleSortOrder())
                    .tier(parseTier(dto.getTier(), dto.getModuleId()))
                    .trackId(dto.getDomainId())
                    .build();
            return moduleRepository.save(m);
        });

        // 2. Resolve / upsert topic
        String topicId = resolveTopicId(dto);

        // 3. Upsert lesson
        lessonRepository.save(Lesson.builder()
                .id(dto.getId())
                .moduleId(dto.getModuleId())
                .topicId(topicId)
                .title(dto.getTitle())
                .sortOrder(dto.getSortOrder())
                .xpReward(dto.getXpReward() > 0 ? dto.getXpReward() : 50)
                .practiceType(parsePracticeType(dto.getPracticeType()))
                .questType(parseQuestType(dto.getQuestType()))
                .feynmanPrompt(dto.getFeynmanPrompt())
                .learningObjectivesJson(dto.getLearningObjectivesJson())
                .integrationDomainsJson(dto.getIntegrationDomainsJson())
                // Phase 2 section fields
                .hookHtml(dto.getHookHtml())
                .loreIntroHtml(dto.getLoreIntroHtml())
                .explanationHtml(dto.getExplanationHtml())
                .whyItMattersHtml(dto.getWhyItMattersHtml())
                .workedExamplesHtml(dto.getWorkedExamplesHtml())
                .commonMistakesJson(dto.getCommonMistakesJson())
                .mentalModelHtml(dto.getMentalModelHtml())
                .miniSummaryHtml(dto.getMiniSummaryHtml())
                .guidedPracticeHtml(dto.getGuidedPracticeHtml())
                .soloPracticeHtml(dto.getSoloPracticeHtml())
                .integrationPrompt(dto.getIntegrationPrompt())
                .loreConclusionHtml(dto.getLoreConclusionHtml())
                .build());

        // 4. Phase 3 — upsert guided steps (replace all for this lesson)
        if (dto.getGuidedSteps() != null && !dto.getGuidedSteps().isEmpty()) {
            guidedStepRepository.deleteByLessonId(dto.getId());
            for (MarkdownLessonDto.GuidedStepConfig sc : dto.getGuidedSteps()) {
                GuidedStepInputType inputType = GuidedStepInputType.SHORT_TEXT;
                try { inputType = GuidedStepInputType.valueOf(sc.inputType().toUpperCase()); }
                catch (IllegalArgumentException ignored) {}

                guidedStepRepository.save(GuidedStep.builder()
                        .id(sc.id())
                        .lessonId(dto.getId())
                        .sortOrder(sc.sortOrder())
                        .instructionHtml(sc.instructionHtml())
                        .inputType(inputType)
                        .inputConfigJson(sc.inputConfigJson())
                        .markingRuleJson(sc.markingRuleJson())
                        .hintHtml(sc.hintHtml())
                        .reflectionPromptHtml(sc.reflectionPromptHtml())
                        .build());
            }
            log.info("[MarkdownContentSeeder] Seeded {} guided step(s) for lesson '{}'",
                    dto.getGuidedSteps().size(), dto.getId());
        }
    }

    /**
     * Returns the topic ID to assign to this lesson.
     * If {@code topicSlug} is provided, the full ID is {@code moduleId-topicSlug}
     * and the topic is upserted if absent.
     * Otherwise the default topic ({@code moduleId-default-topic}) is used and
     * ensured to exist.
     */
    private String resolveTopicId(MarkdownLessonDto dto) {
        boolean hasSlug = dto.getTopicSlug() != null && !dto.getTopicSlug().isBlank();
        String topicId = hasSlug
                ? dto.getModuleId() + "-" + dto.getTopicSlug()
                : dto.getModuleId() + "-default-topic";

        String titleFallback = hasSlug
                ? (dto.getTopicTitle() != null ? dto.getTopicTitle() : dto.getTopicSlug())
                : dto.getModuleTitle();

        topicRepository.findById(topicId).orElseGet(() -> topicRepository.save(
                Topic.builder()
                        .id(topicId)
                        .moduleId(dto.getModuleId())
                        .title(titleFallback)
                        .sortOrder(dto.getTopicSortOrder())
                        .build()));

        return topicId;
    }

    // ── Enum helpers ──────────────────────────────────────────────────────────

    private LearnerPath parseTier(String tier, String contextId) {
        try {
            return LearnerPath.valueOf(tier.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("[MarkdownContentSeeder] Unknown tier '{}' for '{}' — defaulting to APPRENTICE",
                    tier, contextId);
            return LearnerPath.APPRENTICE;
        }
    }

    private LessonPracticeType parsePracticeType(String value) {
        if (value == null || value.isBlank()) return LessonPracticeType.NONE;
        try {
            return LessonPracticeType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return LessonPracticeType.NONE;
        }
    }

    private QuestType parseQuestType(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return QuestType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
