package com.ambravate.arcane.academy.admin.controller;

import com.ambravate.arcane.academy.common.dto.ChunkContentDto;
import com.ambravate.arcane.academy.content.seeder.JsonContentSeeder;
import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.content.domain.RabbitHoleModule;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.content.repository.ChunkRepository;
import com.ambravate.arcane.academy.content.repository.QuestionRepository;
import com.ambravate.arcane.academy.content.repository.RabbitHoleModuleRepository;
import com.ambravate.arcane.academy.content.repository.SubChunkRepository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/content")
@RequiredArgsConstructor
public class AdminImportExportController {

    private final ChunkRepository chunkRepository;
    private final SubChunkRepository subChunkRepository;
    private final QuestionRepository questionRepository;
    private final RabbitHoleModuleRepository rabbitHoleRepository;
    private final JsonContentSeeder jsonContentSeeder;
    private final ObjectMapper objectMapper;

    /**
     * Export a single chunk (with its subchunks, questions, rabbit holes)
     * as a JSON file matching the ChunkContentDto schema.
     */
    @GetMapping("/export/chunk/{chunkId}")
    public ResponseEntity<byte[]> exportChunk(@PathVariable String chunkId) throws Exception {
        Chunk chunk = chunkRepository.findById(chunkId)
                .orElseThrow(() -> new NoSuchElementException("Chunk not found: " + chunkId));

        ChunkContentDto dto = buildExportDto(chunk);
        byte[] json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(dto);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + chunkId + ".json\"")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    /**
     * Import a JSON file (ChunkContentDto format) — upserts the chunk and all its content.
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importChunk(@RequestParam("file") MultipartFile file)
            throws Exception {
        ChunkContentDto dto = objectMapper.readValue(file.getInputStream(), ChunkContentDto.class);
        jsonContentSeeder.upsertChunk(dto);
        return ResponseEntity.ok(Map.of(
                "status", "imported",
                "chunkId", dto.id,
                "subChunks", dto.subChunks != null ? dto.subChunks.size() : 0
        ));
    }

    // ── Export helper ──────────────────────────────────────────────────────────

    private ChunkContentDto buildExportDto(Chunk chunk) throws Exception {
        ChunkContentDto dto = new ChunkContentDto();
        dto.id = chunk.getId();
        dto.title = chunk.getTitle();
        dto.glyph = chunk.getGlyph();
        dto.sortOrder = chunk.getSortOrder();
        dto.tier = chunk.getTier().name();
        dto.topicId = chunk.getTopicId();
        dto.prerequisites = chunk.getPrerequisites().stream().map(Chunk::getId).toList();

        List<SubChunk> subChunks = subChunkRepository.findByChunkIdOrderBySortOrderAsc(chunk.getId());
        dto.subChunks = subChunks.stream().map(sc -> {
            try { return buildSubChunkDto(sc); } catch (Exception e) { throw new RuntimeException(e); }
        }).toList();

        List<RabbitHoleModule> rhs = rabbitHoleRepository.findByChunkIdOrderBySortOrderAsc(chunk.getId());
        dto.rabbitHoles = rhs.stream().map(rh -> {
            try { return buildRabbitHoleDto(rh); } catch (Exception e) { throw new RuntimeException(e); }
        }).toList();

        return dto;
    }

    @SuppressWarnings("unchecked")
    private ChunkContentDto.SubChunkDto buildSubChunkDto(SubChunk sc) throws Exception {
        ChunkContentDto.SubChunkDto dto = new ChunkContentDto.SubChunkDto();
        dto.id = sc.getId();
        dto.title = sc.getTitle();
        dto.sortOrder = sc.getSortOrder();
        dto.xpReward = sc.getXpReward();
        dto.filename = sc.getFilename();
        dto.practiceType = sc.getPracticeType() != null ? sc.getPracticeType().name() : "JAVA";
        dto.hookHtml = sc.getHookHtml();
        dto.explanationHtml = sc.getExplanationHtml();
        dto.soloPracticeHtml = sc.getSoloPracticeHtml();
        dto.feynmanPrompt = sc.getFeynmanPrompt();
        dto.guidedPracticeHtml = sc.getGuidedPracticeHtml();
        dto.guidedPracticeStarterCode = sc.getGuidedPracticeStarterCode();
        if (sc.getStoryJson() != null) {
            dto.storyBeats = objectMapper.readValue(sc.getStoryJson(),
                new TypeReference<>() {
                });
        }
        if (sc.getGuidedPracticeTestsJson() != null) {
            dto.guidedPracticeTests = objectMapper.readValue(sc.getGuidedPracticeTestsJson(),
                new TypeReference<>() {
                });
        }
        dto.questions = questionRepository.findBySubChunkId(sc.getId()).stream()
                .map(q -> {
                    ChunkContentDto.QuestionDto qd = new ChunkContentDto.QuestionDto();
                    qd.type = q.getType().name();
                    qd.tier = q.getTier().name();
                    qd.questionHtml = q.getQuestionHtml();
                    qd.codeSnippet = q.getCodeSnippet();
                    qd.correctAnswer = q.getCorrectAnswer();
                    qd.explanationHtml = q.getExplanationHtml();
                    if (q.getOptionsJson() != null) {
                        try { qd.options = objectMapper.readValue(q.getOptionsJson(), List.class); }
                        catch (Exception ignored) {}
                    }
                    return qd;
                }).toList();
        return dto;
    }

    @SuppressWarnings("unchecked")
    private ChunkContentDto.RabbitHoleDto buildRabbitHoleDto(RabbitHoleModule rh) throws Exception {
        ChunkContentDto.RabbitHoleDto dto = new ChunkContentDto.RabbitHoleDto();
        dto.id = rh.getId();
        dto.title = rh.getTitle();
        dto.sortOrder = rh.getSortOrder();
        dto.filename = rh.getFilename();
        dto.contentHtml = rh.getContentHtml();
        dto.starterCode = rh.getStarterCode();
        if (rh.getStoryJson() != null) {
            dto.storyBeats = objectMapper.readValue(rh.getStoryJson(),
                new TypeReference<>() {});
        }
        if (rh.getTestCasesJson() != null) {
            dto.tests = objectMapper.readValue(rh.getTestCasesJson(),
                new TypeReference<>() {});
        }
        return dto;
    }
}
