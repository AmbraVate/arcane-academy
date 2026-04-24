package com.ambravate.polymath.academy.controller.admin;

import com.ambravate.polymath.academy.dto.admin.AdminSubChunkDto;
import com.ambravate.polymath.academy.model.SubChunk;
import com.ambravate.polymath.academy.model.SubChunkPracticeType;
import com.ambravate.polymath.academy.repository.QuestionRepository;
import com.ambravate.polymath.academy.repository.SubChunkRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/subchunks")
@RequiredArgsConstructor
public class AdminSubChunkController {

    private final SubChunkRepository subChunkRepository;
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<AdminSubChunkDto>> list(@RequestParam String chunkId) {
        return ResponseEntity.ok(
                subChunkRepository.findByChunkIdOrderBySortOrderAsc(chunkId)
                        .stream().map(this::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminSubChunkDto> get(@PathVariable String id) {
        return subChunkRepository.findById(id)
                .map(sc -> ResponseEntity.ok(toDto(sc)))
                .orElseThrow(() -> new NoSuchElementException("SubChunk not found: " + id));
    }

    @PostMapping
    public ResponseEntity<AdminSubChunkDto> create(@RequestBody AdminSubChunkDto req) {
        SubChunk sc = fromDto(req);
        return ResponseEntity.ok(toDto(subChunkRepository.save(sc)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminSubChunkDto> update(@PathVariable String id,
                                                    @RequestBody AdminSubChunkDto req) {
        subChunkRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("SubChunk not found: " + id));
        SubChunk sc = fromDto(req);
        sc.setId(id); // ensure ID matches path
        return ResponseEntity.ok(toDto(subChunkRepository.save(sc)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        questionRepository.deleteAll(questionRepository.findBySubChunkId(id));
        subChunkRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private AdminSubChunkDto toDto(SubChunk sc) {
        long qCount = questionRepository.findBySubChunkId(sc.getId()).size();
        return AdminSubChunkDto.builder()
                .id(sc.getId()).chunkId(sc.getChunkId()).title(sc.getTitle())
                .sortOrder(sc.getSortOrder()).xpReward(sc.getXpReward())
                .practiceType(sc.getPracticeType() != null ? sc.getPracticeType().name() : "JAVA")
                .filename(sc.getFilename())
                .hookHtml(sc.getHookHtml()).explanationHtml(sc.getExplanationHtml())
                .storyBeats(parseJsonList(sc.getStoryJson()))
                .guidedPracticeHtml(sc.getGuidedPracticeHtml())
                .guidedPracticeStarterCode(sc.getGuidedPracticeStarterCode())
                .guidedPracticeTests(parseJsonList(sc.getGuidedPracticeTestsJson()))
                .soloPracticeHtml(sc.getSoloPracticeHtml())
                .feynmanPrompt(sc.getFeynmanPrompt())
                .questionCount(qCount)
                .build();
    }

    private SubChunk fromDto(AdminSubChunkDto dto) {
        SubChunkPracticeType pt = SubChunkPracticeType.JAVA;
        if (dto.getPracticeType() != null) {
            try { pt = SubChunkPracticeType.valueOf(dto.getPracticeType()); } catch (Exception ignored) {}
        }
        return SubChunk.builder()
                .id(dto.getId()).chunkId(dto.getChunkId()).title(dto.getTitle())
                .sortOrder(dto.getSortOrder())
                .xpReward(dto.getXpReward() > 0 ? dto.getXpReward() : 50)
                .practiceType(pt).filename(dto.getFilename())
                .hookHtml(dto.getHookHtml()).explanationHtml(dto.getExplanationHtml())
                .storyJson(toJson(dto.getStoryBeats()))
                .guidedPracticeHtml(dto.getGuidedPracticeHtml())
                .guidedPracticeStarterCode(dto.getGuidedPracticeStarterCode())
                .guidedPracticeTestsJson(toJson(dto.getGuidedPracticeTests()))
                .soloPracticeHtml(dto.getSoloPracticeHtml())
                .feynmanPrompt(dto.getFeynmanPrompt())
                .build();
    }

    private List<Map<String, Object>> parseJsonList(String json) {
        if (json == null || json.isBlank()) return List.of();
        try { return objectMapper.readValue(json, new TypeReference<>() {}); } catch (Exception e) { return List.of(); }
    }

    private String toJson(List<?> list) {
        if (list == null || list.isEmpty()) return null;
        try { return objectMapper.writeValueAsString(list); } catch (Exception e) { return null; }
    }
}
