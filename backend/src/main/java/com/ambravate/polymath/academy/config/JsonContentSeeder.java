package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Loads chunk content from JSON files under src/main/resources/content/
 * and seeds the database.
 *
 * File layout:
 *   resources/content/tailwind/tw-a.json
 *   resources/content/tailwind/tw-b.json
 *   resources/java/ch1.json
 *   ...
 *
 * Files within a directory are loaded in alphabetical order, so the naming
 * convention (tw-a, tw-b, tw-c) naturally enforces seeding order.
 *
 * Called from DataSeeder alongside the legacy Java seeders. As chunks are
 * migrated to JSON the corresponding Java seeder can be removed.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JsonContentSeeder {

    private final ChunkRepository chunkRepository;
    private final SubChunkRepository subChunkRepository;
    private final QuestionRepository questionRepository;
    private final RabbitHoleModuleRepository rabbitHoleRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationContext applicationContext;

    /**
     * Seeds all chunks found in classpath:content/**&#47;*.json.
     *
     * @return the number of chunk files loaded.
     */
    public int seed() throws Exception {
        Resource[] resources = applicationContext.getResources("classpath:content/**/*.json");
        Arrays.sort(resources, Comparator.comparing(r -> r.getFilename() == null ? "" : r.getFilename()));

        int count = 0;
        for (Resource resource : resources) {
            log.info("Loading chunk content: {}", resource.getFilename());
            ChunkContentDto dto = objectMapper.readValue(resource.getInputStream(), ChunkContentDto.class);
            seedChunk(dto);
            count++;
        }
        return count;
    }

    // ── Chunk ─────────────────────────────────────────────────────────────────

    private void seedChunk(ChunkContentDto dto) throws Exception {
        String prereqJson = (dto.prerequisites == null || dto.prerequisites.isEmpty())
                ? "[]"
                : objectMapper.writeValueAsString(dto.prerequisites);

        Chunk chunk = Chunk.builder()
                .id(dto.id)
                .title(dto.title)
                .glyph(dto.glyph)
                .sortOrder(dto.sortOrder)
                .tier(LearnerPath.valueOf(dto.tier))
                .topicId(dto.topicId)
                .prerequisiteIds(prereqJson)
                .build();
        chunkRepository.save(chunk);

        if (dto.subChunks != null) {
            for (ChunkContentDto.SubChunkDto sc : dto.subChunks) {
                seedSubChunk(dto.id, sc);
            }
        }

        if (dto.rabbitHoles != null) {
            for (ChunkContentDto.RabbitHoleDto rh : dto.rabbitHoles) {
                seedRabbitHole(dto.id, rh);
            }
        }
    }

    // ── SubChunk ──────────────────────────────────────────────────────────────

    private void seedSubChunk(String chunkId, ChunkContentDto.SubChunkDto sc) throws Exception {
        String storyJson  = toJson(normaliseBeats(sc.storyBeats));
        String testsJson  = toJson(sc.guidedPracticeTests);

        SubChunkPracticeType practiceType = sc.practiceType != null
                ? SubChunkPracticeType.valueOf(sc.practiceType)
                : SubChunkPracticeType.JAVA;

        subChunkRepository.save(SubChunk.builder()
                .id(sc.id)
                .chunkId(chunkId)
                .title(sc.title)
                .sortOrder(sc.sortOrder)
                .xpReward(sc.xpReward > 0 ? sc.xpReward : 50)
                .filename(sc.filename)
                .hookHtml(sc.hookHtml)
                .explanationHtml(sc.explanationHtml)
                .storyJson(storyJson)
                .guidedPracticeHtml(sc.guidedPracticeHtml)
                .guidedPracticeStarterCode(sc.guidedPracticeStarterCode)
                .guidedPracticeTestsJson(testsJson)
                .soloPracticeHtml(sc.soloPracticeHtml)
                .feynmanPrompt(sc.feynmanPrompt)
                .practiceType(practiceType)
                .build());

        if (sc.questions != null) {
            for (ChunkContentDto.QuestionDto q : sc.questions) {
                seedQuestion(sc.id, q);
            }
        }
    }

    // ── Question ──────────────────────────────────────────────────────────────

    private void seedQuestion(String subChunkId, ChunkContentDto.QuestionDto q) throws Exception {
        // TRUE_FALSE always gets the canonical two options
        String optionsJson;
        if ("TRUE_FALSE".equals(q.type)) {
            optionsJson = "[\"True\",\"False\"]";
        } else if (q.options != null && !q.options.isEmpty()) {
            optionsJson = objectMapper.writeValueAsString(q.options);
        } else {
            optionsJson = null;
        }

        String crossChunkJson = (q.crossChunkIds != null && !q.crossChunkIds.isEmpty())
                ? objectMapper.writeValueAsString(q.crossChunkIds)
                : null;

        // DISCRIMINATION questions are practitioner-level minimum
        QuestionTier questionTier = q.tier != null ? QuestionTier.valueOf(q.tier) : QuestionTier.RECALL;
        LearnerPath minPath = questionTier == QuestionTier.DISCRIMINATION
                ? LearnerPath.PRACTITIONER
                : LearnerPath.FOUNDATION;

        questionRepository.save(Question.builder()
                .subChunkId(subChunkId)
                .tier(questionTier)
                .type(QuestionType.valueOf(q.type))
                .questionHtml(q.questionHtml)
                .codeSnippet(q.codeSnippet)
                .optionsJson(optionsJson)
                .correctAnswer(q.correctAnswer)
                .explanationHtml(q.explanationHtml)
                .crossChunkIds(crossChunkJson)
                .minPath(minPath)
                .build());
    }

    // ── Rabbit hole ───────────────────────────────────────────────────────────

    private void seedRabbitHole(String chunkId, ChunkContentDto.RabbitHoleDto rh) throws Exception {
        rabbitHoleRepository.save(RabbitHoleModule.builder()
                .id(rh.id)
                .chunkId(chunkId)
                .title(rh.title)
                .sortOrder(rh.sortOrder)
                .contentHtml(rh.contentHtml)
                .storyJson(toJson(normaliseBeats(rh.storyBeats)))
                .starterCode(rh.starterCode)
                .testCasesJson(toJson(rh.tests))
                .filename(rh.filename)
                .build());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String toJson(List<?> list) throws Exception {
        return (list == null || list.isEmpty()) ? null : objectMapper.writeValueAsString(list);
    }

    /**
     * Normalises story beat maps from the legacy Java-chunk format to the
     * canonical format consumed by the frontend's {@code StoryPanel} component.
     *
     * <p>Legacy Java chunks use:
     * <ul>
     *   <li>{@code "type": "NARRATION" | "DIALOGUE" | "EXAMPLE"} (uppercase)</li>
     *   <li>{@code "label"} instead of {@code "speaker"} for example blocks</li>
     *   <li>{@code "code"}  instead of {@code "text"}    for example content</li>
     * </ul>
     *
     * <p>Tailwind / new chunks already use lowercase types and the correct keys, so
     * this method is idempotent when called on already-normalised beats.
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> normaliseBeats(List<Map<String, Object>> beats) {
        if (beats == null) return null;
        return beats.stream().map(raw -> {
            java.util.LinkedHashMap<String, Object> beat = new java.util.LinkedHashMap<>(raw);

            // 1. Lower-case the type
            Object type = beat.get("type");
            if (type instanceof String s) {
                beat.put("type", s.toLowerCase());
            }

            // 2. Map "label" → "speaker" (used by EXAMPLE beats)
            if (!beat.containsKey("speaker") && beat.containsKey("label")) {
                beat.put("speaker", beat.remove("label"));
            }

            // 3. Map "code" → "text" (used by EXAMPLE beats)
            if (!beat.containsKey("text") && beat.containsKey("code")) {
                beat.put("text", beat.remove("code"));
            }

            return (Map<String, Object>) beat;
        }).toList();
    }
}
