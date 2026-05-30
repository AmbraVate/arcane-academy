package com.ambravate.arcane.academy.content.seeder;

import com.ambravate.arcane.academy.common.dto.ChunkContentDto;
import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.Topic;
import com.ambravate.arcane.academy.content.repository.TopicRepository;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.common.domain.QuestionTier;
import com.ambravate.arcane.academy.common.domain.QuestionType;
import com.ambravate.arcane.academy.content.domain.RabbitHoleModule;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonPracticeType;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.QuestionRepository;
import com.ambravate.arcane.academy.content.repository.RabbitHoleModuleRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsonContentSeeder {

    private final LearningModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final QuestionRepository questionRepository;
    private final RabbitHoleModuleRepository rabbitHoleRepository;
    private final UserChunkProgressRepository userChunkProgressRepository;
    private final TopicRepository topicRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationContext applicationContext;
    private final PlatformTransactionManager transactionManager;
    private final MarkdownConverter markdown;

    @Transactional
    public void upsertChunk(ChunkContentDto dto) throws Exception {
        replaceGeneratedChildren(dto);
        seedModule(dto);
        wirePrerequisites(dto);
    }

    public int seed() throws Exception {
        List<LoadedChunk> loaded = loadAll();
        List<String> jsonModuleIds = loaded.stream().map(lc -> lc.dto().id).toList();

        TransactionTemplate tx = new TransactionTemplate(transactionManager);

        if (allModulesPresent(jsonModuleIds)) {
            log.info("[JsonContentSeeder] All {} module(s) already present â€” skipping content seed.",
                    jsonModuleIds.size());
        } else {
            Map<String, Set<String>> jsonIdsByDomainTier = new HashMap<>();
            for (LoadedChunk lc : loaded) {
                ChunkContentDto dto = lc.dto();
                log.info("Loading module content: {}", lc.resource().getFilename());
                resolveFileRefs(dto, lc.resource());
                tx.executeWithoutResult(status -> {
                    replaceGeneratedChildren(dto);
                    seedModule(dto);
                });
                jsonIdsByDomainTier
                        .computeIfAbsent(dto.domainId + "|" + dto.tier, k -> new HashSet<>())
                        .add(dto.id);
            }
            tx.executeWithoutResult(status -> pruneStaleModules(jsonIdsByDomainTier));
        }

        tx.executeWithoutResult(status -> loaded.forEach(lc -> wirePrerequisites(lc.dto())));

        return jsonModuleIds.size();
    }

    private List<LoadedChunk> loadAll() throws IOException {
        Resource[] resources = applicationContext.getResources("classpath:content/**/*.json");
        Arrays.sort(resources, (a, b) -> naturalOrder(
                a.getFilename() == null ? "" : a.getFilename(),
                b.getFilename() == null ? "" : b.getFilename()));

        List<LoadedChunk> loaded = new ArrayList<>(resources.length);
        for (Resource resource : resources) {
            ChunkContentDto dto = objectMapper.readValue(resource.getInputStream(), ChunkContentDto.class);
            loaded.add(new LoadedChunk(resource, dto));
        }
        return loaded;
    }

    private boolean allModulesPresent(List<String> jsonModuleIds) {
        if (jsonModuleIds.isEmpty()) return true;
        return moduleRepository.findAllById(jsonModuleIds).size() == jsonModuleIds.size();
    }

    private record LoadedChunk(Resource resource, ChunkContentDto dto) {}

    private void pruneStaleModules(Map<String, Set<String>> jsonIdsByDomainTier) {
        for (Map.Entry<String, Set<String>> entry : jsonIdsByDomainTier.entrySet()) {
            String[] parts = entry.getKey().split("\\|", 2);
            String domainId = parts[0];
            String tier     = parts[1];
            Set<String> validIds = entry.getValue();

            LearnerPath tierEnum;
            try { tierEnum = LearnerPath.valueOf(tier); }
            catch (IllegalArgumentException e) { continue; }

            List<LearningModule> stale = moduleRepository.findByTrackIdOrderBySortOrderAsc(domainId).stream()
                    .filter(m -> m.getTier() == tierEnum && !validIds.contains(m.getId()))
                    .toList();

            if (stale.isEmpty()) continue;

            List<String> staleModuleIds = stale.stream().map(LearningModule::getId).toList();
            log.info("[JsonContentSeeder] Pruning {} stale module(s) for domain='{}' tier='{}': {}",
                    stale.size(), domainId, tier, staleModuleIds);

            List<Lesson> staleLessons = lessonRepository.findByModuleIdIn(staleModuleIds);
            if (!staleLessons.isEmpty()) {
                List<String> staleLessonIds = staleLessons.stream().map(Lesson::getId).toList();
                questionRepository.deleteByLessonIdIn(staleLessonIds);
                userChunkProgressRepository.deleteByLessonIdIn(staleLessonIds);
                lessonRepository.deleteAll(staleLessons);
            }
            stale.forEach(m -> rabbitHoleRepository.deleteByModuleId(m.getId()));
            moduleRepository.deleteAll(stale);
        }
    }

    // â”€â”€ @file: reference resolution â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void resolveFileRefs(ChunkContentDto dto, Resource jsonResource) throws IOException {
        if (dto.subChunks != null) {
            for (ChunkContentDto.SubChunkDto sc : dto.subChunks) {
                sc.guidedPracticeStarterCode =
                        resolveRef(sc.guidedPracticeStarterCode, jsonResource, sc.id);
                if (sc.challenge != null) {
                    sc.challenge.starterCode =
                            resolveRef(sc.challenge.starterCode, jsonResource, sc.id + ".challenge");
                }
            }
        }
        if (dto.rabbitHoles != null) {
            for (ChunkContentDto.RabbitHoleDto rh : dto.rabbitHoles) {
                rh.starterCode = resolveRef(rh.starterCode, jsonResource, rh.id);
            }
        }
    }

    private String resolveRef(String value, Resource base, String ownerId) throws IOException {
        if (value == null || !value.startsWith("@file:")) return value;
        String relativePath = value.substring("@file:".length()).strip();
        Resource ref = base.createRelative(relativePath);
        if (!ref.exists()) {
            throw new IllegalArgumentException(
                    "[@file: resolution failed] '" + relativePath + "' referenced in " +
                    ownerId + " not found (looked next to " + base.getFilename() + ")");
        }
        String content = new String(ref.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        log.debug("Resolved @file:{} for {}", relativePath, ownerId);
        return content;
    }

    private void replaceGeneratedChildren(ChunkContentDto dto) {
        List<String> lessonIds = dto.subChunks == null
                ? List.of()
                : dto.subChunks.stream().map(sc -> sc.id).toList();

        if (!lessonIds.isEmpty()) {
            questionRepository.deleteByLessonIdIn(lessonIds);
        }

        rabbitHoleRepository.deleteByModuleId(dto.id);
    }

    // â”€â”€ Module â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void seedModule(ChunkContentDto dto) {
        LearningModule module = LearningModule.builder()
                .id(dto.id)
                .title(dto.title)
                .glyph(dto.glyph)
                .sortOrder(dto.sortOrder)
                .tier(LearnerPath.valueOf(dto.tier))
                .trackId(dto.domainId)
                .build();
        moduleRepository.save(module);

        // Ensure the default Topic for this module exists (upsert by stable ID convention)
        String defaultTopicId = dto.id + "-default-topic";
        topicRepository.save(Topic.builder()
                .id(defaultTopicId)
                .moduleId(dto.id)
                .title(dto.title)
                .sortOrder(0)
                .build());

        if (dto.subChunks != null) {
            for (ChunkContentDto.SubChunkDto sc : dto.subChunks) {
                seedLesson(dto.id, sc);
            }
        }

        if (dto.rabbitHoles != null) {
            for (ChunkContentDto.RabbitHoleDto rh : dto.rabbitHoles) {
                seedRabbitHole(dto.id, rh);
            }
        }
    }

    // â”€â”€ Lesson â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void seedLesson(String moduleId, ChunkContentDto.SubChunkDto sc) {
        String storyJson  = toJson(normaliseBeats(sc.storyBeats));
        String testsJson  = toJson(sc.guidedPracticeTests);

        LessonPracticeType practiceType = sc.practiceType != null
                ? LessonPracticeType.valueOf(sc.practiceType)
                : LessonPracticeType.JAVA;

        String rhTermsJson = toJson(sc.rabbitHoleTerms);

        String learningObjectivesJson = toJson(sc.learningObjectives);
        String challengeTestsJson     = sc.challenge != null ? toJson(sc.challenge.tests) : null;
        String commonMistakesJson     = toJson(sc.commonMistakes);
        String assessmentCriteriaJson = toJson(sc.assessmentCriteria);
        String downloadablesJson      = toJson(sc.downloadables);

        com.ambravate.arcane.academy.common.domain.QuestType questType = null;
        if (sc.questType != null && !sc.questType.isBlank()) {
            try { questType = com.ambravate.arcane.academy.common.domain.QuestType.valueOf(sc.questType.toUpperCase()); }
            catch (IllegalArgumentException ignored) {}
        }

        lessonRepository.save(Lesson.builder()
                .id(sc.id)
                .moduleId(moduleId)
                .topicId(moduleId + "-default-topic")
                .title(sc.title)
                .sortOrder(sc.sortOrder)
                .xpReward(sc.xpReward > 0 ? sc.xpReward : 50)
                .filename(sc.filename)
                .hookHtml(markdown.toHtml(sc.hookHtml))
                .explanationHtml(markdown.toHtml(sc.explanationHtml))
                .storyJson(storyJson)
                .guidedPracticeHtml(markdown.toHtml(sc.guidedPracticeHtml))
                .guidedPracticeStarterCode(sc.guidedPracticeStarterCode)
                .guidedPracticeTestsJson(testsJson)
                .soloPracticeHtml(markdown.toHtml(sc.soloPracticeHtml))
                .feynmanPrompt(markdown.toHtml(sc.feynmanPrompt))
                .practiceType(practiceType)
                .rabbitHoleTermsJson(rhTermsJson)
                .modelAnswer(markdown.toHtml(sc.modelAnswer))
                .guidedPracticeModelAnswer(markdown.toHtml(sc.guidedPracticeModelAnswer))
                .learningObjectivesJson(learningObjectivesJson)
                .challengeHtml(markdown.toHtml(sc.challenge != null ? sc.challenge.html : null))
                .challengeStarterCode(sc.challenge != null ? sc.challenge.starterCode : null)
                .challengeTestsJson(challengeTestsJson)
                .miniProjectHtml(markdown.toHtml(sc.miniProject))
                .commonMistakesJson(commonMistakesJson)
                .assessmentCriteriaJson(assessmentCriteriaJson)
                .downloadablesJson(downloadablesJson)
                .integrationPrompt(markdown.toHtml(sc.integrationPrompt))
                .questType(questType)
                .build());

        if (sc.questions != null) {
            for (ChunkContentDto.QuestionDto q : sc.questions) {
                seedQuestion(sc.id, q);
            }
        }
    }

    // â”€â”€ Question â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void seedQuestion(String lessonId, ChunkContentDto.QuestionDto q) {
        String optionsJson;
        if ("TRUE_FALSE".equals(q.type)) {
            optionsJson = "[\"True\",\"False\"]";
        } else if (q.options != null && !q.options.isEmpty()) {
            optionsJson = writeJson(q.options);
        } else {
            optionsJson = null;
        }

        String crossChunkJson = (q.crossChunkIds != null && !q.crossChunkIds.isEmpty())
                ? writeJson(q.crossChunkIds)
                : null;

        String correctAnswer = q.correctAnswer;
        if (correctAnswer == null && q.correctIndex != null
                && q.options != null && q.correctIndex < q.options.size()) {
            correctAnswer = q.options.get(q.correctIndex);
        }
        if ("TRUE_FALSE".equals(q.type) && correctAnswer != null) {
            if ("true".equalsIgnoreCase(correctAnswer) || Boolean.TRUE.toString().equalsIgnoreCase(correctAnswer)) {
                correctAnswer = "True";
            } else if ("false".equalsIgnoreCase(correctAnswer) || Boolean.FALSE.toString().equalsIgnoreCase(correctAnswer)) {
                correctAnswer = "False";
            }
        }

        QuestionTier questionTier = q.tier != null ? QuestionTier.valueOf(q.tier) : QuestionTier.RECALL;
        LearnerPath minPath = questionTier == QuestionTier.DISCRIMINATION
                ? LearnerPath.JUNIOR
                : LearnerPath.APPRENTICE;

        questionRepository.save(Question.builder()
                .lessonId(lessonId)
                .tier(questionTier)
                .type(QuestionType.valueOf(q.type))
                .questionHtml(q.questionHtml)
                .codeSnippet(q.codeSnippet)
                .optionsJson(optionsJson)
                .correctAnswer(correctAnswer)
                .explanationHtml(q.explanationHtml)
                .crossChunkIds(crossChunkJson)
                .minPath(minPath)
                .build());
    }

    // â”€â”€ Rabbit hole â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void seedRabbitHole(String moduleId, ChunkContentDto.RabbitHoleDto rh) {
        rabbitHoleRepository.save(RabbitHoleModule.builder()
                .id(rh.id)
                .moduleId(moduleId)
                .title(rh.title)
                .sortOrder(rh.sortOrder)
                .contentHtml(markdown.toHtml(rh.contentHtml))
                .storyJson(toJson(normaliseBeats(rh.storyBeats)))
                .starterCode(rh.starterCode)
                .testCasesJson(toJson(rh.tests))
                .filename(rh.filename)
                .build());
    }

    // â”€â”€ Prerequisites â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void wirePrerequisites(ChunkContentDto dto) {
        if (dto.prerequisites == null || dto.prerequisites.isEmpty()) return;
        List<LearningModule> prereqModules = new ArrayList<>(dto.prerequisites.stream()
                .map(pId -> moduleRepository.findById(pId)
                        .orElseThrow(() -> new IllegalStateException("Prereq module not found: " + pId)))
                .toList());
        LearningModule module = moduleRepository.findById(dto.id).orElseThrow();
        module.setPrerequisites(prereqModules);
        moduleRepository.save(module);
    }

    // â”€â”€ Helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private String toJson(List<?> list) {
        return (list == null || list.isEmpty()) ? null : writeJson(list);
    }

    private String writeJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialise seed content to JSON", e);
        }
    }

    private static int naturalOrder(String a, String b) {
        int i = 0, j = 0;
        while (i < a.length() && j < b.length()) {
            if (Character.isDigit(a.charAt(i)) && Character.isDigit(b.charAt(j))) {
                int si = i, sj = j;
                while (i < a.length() && Character.isDigit(a.charAt(i))) i++;
                while (j < b.length() && Character.isDigit(b.charAt(j))) j++;
                int diff = Integer.compare(Integer.parseInt(a.substring(si, i)),
                                           Integer.parseInt(b.substring(sj, j)));
                if (diff != 0) return diff;
            } else {
                int diff = Character.compare(a.charAt(i++), b.charAt(j++));
                if (diff != 0) return diff;
            }
        }
        return Integer.compare(a.length() - i, b.length() - j);
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> normaliseBeats(List<Map<String, Object>> beats) {
        if (beats == null) return null;
        return beats.stream().map(raw -> {
            java.util.LinkedHashMap<String, Object> beat = new java.util.LinkedHashMap<>(raw);

            Object type = beat.get("type");
            if (type instanceof String s) {
                beat.put("type", s.toLowerCase());
            }

            if (!beat.containsKey("speaker") && beat.containsKey("label")) {
                beat.put("speaker", beat.remove("label"));
            }

            if (!beat.containsKey("text") && beat.containsKey("code")) {
                beat.put("text", beat.remove("code"));
            }

            if (!beat.containsKey("text") && beat.containsKey("content")) {
                beat.put("text", beat.remove("content"));
            }

            if (!beat.containsKey("av") && beat.containsKey("avatarEmoji")) {
                beat.put("av", beat.remove("avatarEmoji"));
            }
            if (!beat.containsKey("cls") && beat.containsKey("cssClass")) {
                beat.put("cls", beat.remove("cssClass"));
            }

            return (Map<String, Object>) beat;
        }).toList();
    }
}
