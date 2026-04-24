package com.ambravate.polymath.academy.controller.admin;

import com.ambravate.polymath.academy.dto.admin.AdminChunkDto;
import com.ambravate.polymath.academy.model.Chunk;
import com.ambravate.polymath.academy.model.LearnerPath;
import com.ambravate.polymath.academy.repository.ChunkRepository;
import com.ambravate.polymath.academy.repository.SubChunkRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/chunks")
@RequiredArgsConstructor
public class AdminChunkController {

    private final ChunkRepository chunkRepository;
    private final SubChunkRepository subChunkRepository;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<AdminChunkDto>> list(
            @RequestParam(required = false) String topicId) {
        List<Chunk> chunks = (topicId != null)
                ? chunkRepository.findByTopicIdOrderBySortOrderAsc(topicId)
                : chunkRepository.findAllByOrderBySortOrderAsc();
        return ResponseEntity.ok(chunks.stream().map(this::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminChunkDto> get(@PathVariable String id) {
        return chunkRepository.findById(id)
                .map(c -> ResponseEntity.ok(toDto(c)))
                .orElseThrow(() -> new NoSuchElementException("Chunk not found: " + id));
    }

    @PostMapping
    public ResponseEntity<AdminChunkDto> create(@RequestBody AdminChunkDto req) {
        if (chunkRepository.existsById(req.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Chunk chunk = buildChunk(req);
        return ResponseEntity.ok(toDto(chunkRepository.save(chunk)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminChunkDto> update(@PathVariable String id, @RequestBody AdminChunkDto req) {
        Chunk chunk = chunkRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Chunk not found: " + id));
        chunk.setTitle(req.getTitle());
        chunk.setGlyph(req.getGlyph());
        chunk.setSortOrder(req.getSortOrder());
        chunk.setTier(LearnerPath.valueOf(req.getTier()));
        chunk.setTopicId(req.getTopicId());
        chunk.setPrerequisiteIds(toJson(req.getPrerequisiteIds()));
        return ResponseEntity.ok(toDto(chunkRepository.save(chunk)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        long subCount = subChunkRepository.findByChunkIdOrderBySortOrderAsc(id).size();
        if (subCount > 0) {
            return ResponseEntity.status(409).build(); // Conflict — has subchunks
        }
        chunkRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private AdminChunkDto toDto(Chunk c) {
        List<String> prereqs = List.of();
        if (c.getPrerequisiteIds() != null && !c.getPrerequisiteIds().isBlank()) {
            try { prereqs = objectMapper.readValue(c.getPrerequisiteIds(), List.class); } catch (Exception ignored) {}
        }
        long subCount = subChunkRepository.findByChunkIdOrderBySortOrderAsc(c.getId()).size();
        return AdminChunkDto.builder()
                .id(c.getId()).title(c.getTitle()).glyph(c.getGlyph())
                .sortOrder(c.getSortOrder()).tier(c.getTier().name())
                .topicId(c.getTopicId()).prerequisiteIds(prereqs)
                .subChunkCount(subCount)
                .build();
    }

    private Chunk buildChunk(AdminChunkDto req) {
        return Chunk.builder()
                .id(req.getId()).title(req.getTitle()).glyph(req.getGlyph())
                .sortOrder(req.getSortOrder())
                .tier(req.getTier() != null ? LearnerPath.valueOf(req.getTier()) : LearnerPath.FOUNDATION)
                .topicId(req.getTopicId() != null ? req.getTopicId() : "java")
                .prerequisiteIds(toJson(req.getPrerequisiteIds()))
                .build();
    }

    private String toJson(List<String> list) {
        if (list == null || list.isEmpty()) return "[]";
        try { return objectMapper.writeValueAsString(list); } catch (Exception e) { return "[]"; }
    }
}
