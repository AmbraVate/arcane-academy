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
import com.ambravate.arcane.academy.content.repository.ChunkRepository;
import com.ambravate.arcane.academy.content.repository.QuestionRepository;
import com.ambravate.arcane.academy.content.repository.RabbitHoleModuleRepository;
import com.ambravate.arcane.academy.content.repository.SubChunkRepository;
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
    private final PlatformTransactionManager transactionManager;

    /**
     * Upserts a single chunk from a DTO — used by the admin import endpoint.
     * JPA's {@code save} performs insert-or-update by entity ID, so re-importing
     * an existing chunk safely overwrites it.
     */
    @Transactional
    public void upsertChunk(ChunkContentDto dto) throws Exception {
        replaceGeneratedChildren(dto);
        seedChunk(dto);
        wirePrerequisites(dto);
    }

    /**
     * Seeds chunk content from the JSON files under {@code classpath:content/}.
     *
     * <p>Resilient by design, and safe to run on every startup:
     * <ul>
     *   <li><b>Per-file transactions</b> — each chunk file commits in its own
     *       transaction, so a dropped connection mid-seed loses at most one file
     *       and a re-run resumes cleanly; no transaction is held open across the
     *       whole load.</li>
     *   <li><b>Idempotent</b> — chunks and sub-chunks upsert by ID; questions and
     *       rabbit holes are replaced, so re-running converges on the JSON content.</li>
     *   <li><b>Gated</b> — the destructive child-replacement pass is skipped when
     *       every JSON chunk already exists, so rows keyed off generated content
     *       are not churned on routine restarts.</li>
     * </ul>
     *
     * @return the number of chunk files found
     */
    public int seed() throws Exception {
        List<LoadedChunk> loaded = loadAll();
        List<String> jsonChunkIds = loaded.stream().map(lc -> lc.dto().id).toList();

        TransactionTemplate tx = new TransactionTemplate(transactionManager);

        if (allChunksPresent(jsonChunkIds)) {
            log.info("[JsonContentSeeder] All {} chunk(s) already present — skipping content seed.",
                    jsonChunkIds.size());
        } else {
            // Track topic+tier -> set of chunk IDs loaded from JSON, for stale-chunk pruning.
            Map<String, Set<String>> jsonIdsByTopicTier = new HashMap<>();
            for (LoadedChunk lc : loaded) {
                ChunkContentDto dto = lc.dto();
                log.info("Loading chunk content: {}", lc.resource().getFilename());
                resolveFileRefs(dto, lc.resource());     // file I/O — kept outside the transaction
                tx.executeWithoutResult(status -> {      // one transaction per chunk file
                    replaceGeneratedChildren(dto);
                    seedChunk(dto);                      // saves the chunk without prerequisites
                });
                jsonIdsByTopicTier
                        .computeIfAbsent(dto.topicId + "|" + dto.tier, k -> new HashSet<>())
                        .add(dto.id);
            }
            tx.executeWithoutResult(status -> pruneStaleChunks(jsonIdsByTopicTier));
        }

        // Prerequisite wiring always runs: it is cheap, idempotent and non-destructive,
        // and running it unconditionally self-heals a prior run that saved chunks but
        // died before wiring finished. A separate pass keeps seeding order-independent —
        // cross-tier and cross-topic prerequisite lookups always resolve.
        tx.executeWithoutResult(status -> loaded.forEach(lc -> wirePrerequisites(lc.dto())));

        return jsonChunkIds.size();
    }

    /** Reads and parses every JSON content file. Performs no database access. */
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

    /** True when every supplied chunk ID already exists in the database. */
    private boolean allChunksPresent(List<String> jsonChunkIds) {
        if (jsonChunkIds.isEmpty()) {
            return true;
        }
        return chunkRepository.findAllById(jsonChunkIds).size() == jsonChunkIds.size();
    }

    /** A content file paired with its parsed DTO. */
    private record LoadedChunk(Resource resource, ChunkContentDto dto) {}

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

    /** Saves a chunk and its sub-chunks/rabbit-holes. Prerequisites are NOT set here —
     *  call {@link #wirePrerequisites} in a second pass after all chunks are saved. */
    private void seedChunk(ChunkContentDto dto) {
        Chunk chunk = Chunk.builder()
                .id(dto.id)
                .title(dto.title)
                .glyph(dto.glyph)
                .sortOrder(dto.sortOrder)
                .tier(LearnerPath.valueOf(dto.tier))
                .topicId(dto.topicId)
                .build();  // prerequisites defaults to empty ArrayList via @Builder.Default
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

    private void seedSubChunk(String chunkId, ChunkContentDto.SubChunkDto sc) {
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

    private void seedQuestion(String subChunkId, ChunkContentDto.QuestionDto q) {
        // TRUE_FALSE always gets the canonical two options
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

    private void seedRabbitHole(String chunkId, ChunkContentDto.RabbitHoleDto rh) {
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

    private String toJson(List<?> list) {
        return (list == null || list.isEmpty()) ? null : writeJson(list);
    }

    /** Serialises a value to JSON, wrapping Jackson's checked exception so the
     *  transaction lambdas in {@link #seed()} stay free of checked exceptions. */
    private String writeJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialise seed content to JSON", e);
        }
    }

    /** Sets prerequisites on an already-saved chunk. Must be called after all chunks in the
     *  batch have been saved, so cross-tier and cross-topic lookups always succeed. */
    private void wirePrerequisites(ChunkContentDto dto) {
        if (dto.prerequisites == null || dto.prerequisites.isEmpty()) return;
        // A mutable list is required: Hibernate manages this as the chunk's @ManyToMany
        // collection and calls clear() on it during the merge inside save() — an immutable
        // Stream.toList() result would throw UnsupportedOperationException.
        List<Chunk> prereqChunks = new ArrayList<>(dto.prerequisites.stream()
                .map(pId -> chunkRepository.findById(pId)
                        .orElseThrow(() -> new IllegalStateException("Prereq chunk not found: " + pId)))
                .toList());
        Chunk chunk = chunkRepository.findById(dto.id).orElseThrow();
        chunk.setPrerequisites(prereqChunks);
        chunkRepository.save(chunk);
    }

    /** Compares filenames treating embedded digit runs as integers: "prt-9" &lt; "prt-10". */
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
