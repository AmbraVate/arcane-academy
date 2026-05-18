package com.ambravate.arcane.academy.content.seeder;

import com.ambravate.arcane.academy.common.dto.ChunkContentDto;
import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.common.domain.QuestionTier;
import com.ambravate.arcane.academy.common.domain.QuestionType;
import com.ambravate.arcane.academy.content.domain.RabbitHoleModule;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkPracticeType;
import com.ambravate.arcane.academy.common.repository.ChunkRepository;
import com.ambravate.arcane.academy.common.repository.QuestionRepository;
import com.ambravate.arcane.academy.content.repository.RabbitHoleModuleRepository;
import com.ambravate.arcane.academy.common.repository.SubChunkRepository;
import com.ambravate.arcane.academy.common.repository.UserChunkProgressRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private final UserChunkProgressRepository userChunkProgressRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationContext applicationContext;

    /**
     * Upserts a single chunk from a DTO — used by the admin import endpoint.
     * JPA's {@code save} performs insert-or-update by entity ID, so re-importing
     * an existing chunk safely overwrites it.
     */
    @Transactional
    public void upsertChunk(ChunkContentDto dto) throws Exception {
        replaceGeneratedChildren(dto);
        seedChunk(dto);
    }

    /**
     * Seeds all chunks found in classpath:content/**&#47;*.json.
     *
     * @return the number of chunk files loaded.
     */
    @Transactional
    public int seed() throws Exception {
        Resource[] resources = applicationContext.getResources("classpath:content/**/*.json");
        Arrays.sort(resources, Comparator.comparing(r -> r.getFilename() == null ? "" : r.getFilename()));

        // Track topic+tier → set of chunk IDs loaded from JSON, for stale-chunk pruning
        Map<String, Set<String>> jsonIdsByTopicTier = new HashMap<>();

        int count = 0;
        for (Resource resource : resources) {
            log.info("Loading chunk content: {}", resource.getFilename());
            ChunkContentDto dto = objectMapper.readValue(resource.getInputStream(), ChunkContentDto.class);
            resolveFileRefs(dto, resource);
            replaceGeneratedChildren(dto);
            seedChunk(dto);
            String key = dto.topicId + "|" + dto.tier;
            jsonIdsByTopicTier.computeIfAbsent(key, k -> new HashSet<>()).add(dto.id);
            count++;
        }

        // Prune stale DB chunks: for each (topicId, tier) that has JSON content, remove any
        // DB chunk in that same (topicId, tier) whose ID wasn't in the JSON files.
        // This cleans up old seeder-based chunks superseded by JSON content without touching
        // tiers that have no JSON replacement yet (e.g. Practitioner/Expert if not yet migrated).
        pruneStaleChunks(jsonIdsByTopicTier);

        return count;
    }

    private void pruneStaleChunks(Map<String, Set<String>> jsonIdsByTopicTier) {
        for (Map.Entry<String, Set<String>> entry : jsonIdsByTopicTier.entrySet()) {
            String[] parts = entry.getKey().split("\\|", 2);
            String topicId = parts[0];
            String tier    = parts[1];
            Set<String> validIds = entry.getValue();

            LearnerPath tierEnum;
            try { tierEnum = LearnerPath.valueOf(tier); }
            catch (IllegalArgumentException e) { continue; }

            List<Chunk> stale = chunkRepository.findByTopicIdOrderBySortOrderAsc(topicId).stream()
                    .filter(c -> c.getTier() == tierEnum && !validIds.contains(c.getId()))
                    .toList();

            if (stale.isEmpty()) continue;

            List<String> staleChunkIds = stale.stream().map(Chunk::getId).toList();
            log.info("[JsonContentSeeder] Pruning {} stale chunk(s) for topic='{}' tier='{}': {}",
                    stale.size(), topicId, tier, staleChunkIds);

            List<SubChunk> staleSubs = subChunkRepository.findByChunkIdIn(staleChunkIds);
            if (!staleSubs.isEmpty()) {
                List<String> staleSubIds = staleSubs.stream().map(SubChunk::getId).toList();
                questionRepository.deleteBySubChunkIdIn(staleSubIds);
                userChunkProgressRepository.deleteBySubChunkIdIn(staleSubIds);
                subChunkRepository.deleteAll(staleSubs);
            }
            stale.forEach(chunk -> rabbitHoleRepository.deleteByChunkId(chunk.getId()));
            chunkRepository.deleteAll(stale);
        }
    }

    // ── @file: reference resolution ───────────────────────────────────────────

    /**
     * Resolves {@code @file:relative/path} references in code fields, expanding
     * them to the contents of the referenced file.
     *
     * <p>The path is resolved relative to the JSON file that contains the reference.
     * For example, in {@code content/java/java-fnd-1.json}:
     * <pre>
     *   "guidedPracticeStarterCode": "@file:java-fnd-1a-starter.java"
     * </pre>
     * will load {@code content/java/java-fnd-1a-starter.java}.
     *
     * <p>Any code field that does <em>not</em> start with {@code @file:} is left
     * unchanged, so existing inline strings continue to work without modification.
     */
    private void resolveFileRefs(ChunkContentDto dto, Resource jsonResource) throws IOException {
        if (dto.subChunks != null) {
            for (ChunkContentDto.SubChunkDto sc : dto.subChunks) {
                sc.guidedPracticeStarterCode =
                        resolveRef(sc.guidedPracticeStarterCode, jsonResource, sc.id);
            }
        }
        if (dto.rabbitHoles != null) {
            for (ChunkContentDto.RabbitHoleDto rh : dto.rabbitHoles) {
                rh.starterCode = resolveRef(rh.starterCode, jsonResource, rh.id);
            }
        }
    }

    /**
     * If {@code value} starts with {@code @file:}, loads and returns the
     * contents of the referenced classpath resource.  Otherwise returns
     * {@code value} unchanged.
     *
     * @param value       the field value to check
     * @param base        the JSON resource being loaded (used to resolve sibling paths)
     * @param ownerId     ID of the owning sub-chunk / rabbit-hole (for error messages)
     */
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
        List<String> subChunkIds = dto.subChunks == null
                ? List.of()
                : dto.subChunks.stream().map(sc -> sc.id).toList();

        if (!subChunkIds.isEmpty()) {
            questionRepository.deleteBySubChunkIdIn(subChunkIds);
        }

        rabbitHoleRepository.deleteByChunkId(dto.id);
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

        String rhTermsJson = toJson(sc.rabbitHoleTerms);

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
                .rabbitHoleTermsJson(rhTermsJson)
                .modelAnswer(sc.modelAnswer)
                .guidedPracticeModelAnswer(sc.guidedPracticeModelAnswer)
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

        // Resolve correctAnswer: prefer explicit text, fall back to options[correctIndex]
        String correctAnswer = q.correctAnswer;
        if (correctAnswer == null && q.correctIndex != null
                && q.options != null && q.correctIndex < q.options.size()) {
            correctAnswer = q.options.get(q.correctIndex);
        }
        // TRUE_FALSE: map boolean-style answers to canonical "True"/"False"
        if ("TRUE_FALSE".equals(q.type) && correctAnswer != null) {
            if ("true".equalsIgnoreCase(correctAnswer) || Boolean.TRUE.toString().equalsIgnoreCase(correctAnswer)) {
                correctAnswer = "True";
            } else if ("false".equalsIgnoreCase(correctAnswer) || Boolean.FALSE.toString().equalsIgnoreCase(correctAnswer)) {
                correctAnswer = "False";
            }
        }

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
                .correctAnswer(correctAnswer)
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

            // 4. Map "content" → "text" (alternate key used in some authored chunks)
            if (!beat.containsKey("text") && beat.containsKey("content")) {
                beat.put("text", beat.remove("content"));
            }

            // 5. Map "avatarEmoji" → "av" and "cssClass" → "cls" (frontend expects short keys)
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
