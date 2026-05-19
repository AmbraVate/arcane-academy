package com.ambravate.arcane.academy.admin.controller;

import com.ambravate.arcane.academy.admin.dto.AdminChunkDto;
import com.ambravate.arcane.academy.admin.util.ChunkAssembler;
import com.ambravate.arcane.academy.admin.util.ChunkFactory;
import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.content.repository.ChunkRepository;
import com.ambravate.arcane.academy.content.repository.SubChunkRepository;
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
    private final ChunkFactory chunkFactory;
    private final ChunkAssembler chunkAssembler;

    @GetMapping
    public ResponseEntity<List<AdminChunkDto>> list(
            @RequestParam(required = false) String topicId) {
        List<Chunk> chunks = (topicId != null)
                ? chunkRepository.findByTopicIdOrderBySortOrderAsc(topicId)
                : chunkRepository.findAllByOrderBySortOrderAsc();
        return ResponseEntity.ok(chunks
            .stream()
            .map(chunk -> chunkAssembler.toDto(chunk, subChunkRepository.findByChunkIdOrderBySortOrderAsc(chunk.getId()).size() ))
            .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminChunkDto> get(@PathVariable String id) {
        return chunkRepository.findById(id)
                .map(c -> ResponseEntity.ok(chunkAssembler.toDto(c, subChunkRepository.findByChunkIdOrderBySortOrderAsc(c.getId()).size())))
                .orElseThrow(() -> new NoSuchElementException("Chunk not found: " + id));
    }

    @PostMapping
    public ResponseEntity<AdminChunkDto> create(@RequestBody AdminChunkDto req) {
        if (chunkRepository.existsById(req.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Chunk chunk = chunkFactory.buildChunk(req);
        return ResponseEntity.ok(chunkAssembler.toDto(chunkRepository.save(chunk), subChunkRepository.findByChunkIdOrderBySortOrderAsc(chunk.getId()).size()));
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
        List<Chunk> prereqChunks = req.getPrerequisiteIds() == null ? List.of() :
                req.getPrerequisiteIds().stream()
                        .map(pId -> chunkRepository.findById(pId)
                                .orElseThrow(() -> new IllegalArgumentException("Prerequisite not found: " + pId)))
                        .toList();
        chunk.setPrerequisites(prereqChunks);
        return ResponseEntity.ok(chunkAssembler.toDto(chunkRepository.save(chunk), subChunkRepository.findByChunkIdOrderBySortOrderAsc(chunk.getId()).size()));
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

}
