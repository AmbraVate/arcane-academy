package com.ambravate.arcane.academy.admin.controller;

import com.ambravate.arcane.academy.admin.dto.AdminSubChunkDto;
import com.ambravate.arcane.academy.admin.util.AdminSubChunkAssembler;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.content.repository.QuestionRepository;
import com.ambravate.arcane.academy.content.repository.SubChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/subchunks")
@RequiredArgsConstructor
public class AdminSubChunkController {

    private final SubChunkRepository subChunkRepository;
    private final QuestionRepository questionRepository;
    private final AdminSubChunkAssembler subChunkAssembler;

    @GetMapping
    public ResponseEntity<List<AdminSubChunkDto>> list(@RequestParam String chunkId) {
        return ResponseEntity.ok(
                subChunkRepository.findByChunkIdOrderBySortOrderAsc(chunkId)
                        .stream()
                    .map(subchunk -> subChunkAssembler.toDto(subchunk, questionRepository.findBySubChunkId(subchunk.getId()).size()))
                    .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminSubChunkDto> get(@PathVariable String id) {
        return subChunkRepository.findById(id)
                .map(sc -> ResponseEntity.ok(subChunkAssembler.toDto(sc, questionRepository.findBySubChunkId(sc.getId()).size())))
                .orElseThrow(() -> new NoSuchElementException("SubChunk not found: " + id));
    }

    @PostMapping
    public ResponseEntity<AdminSubChunkDto> create(@RequestBody AdminSubChunkDto req) {
        SubChunk sc = subChunkAssembler.fromDto(req);
        return ResponseEntity.ok(subChunkAssembler.toDto(subChunkRepository.save(sc), questionRepository.findBySubChunkId(sc.getId()).size()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminSubChunkDto> update(@PathVariable String id,
                                                    @RequestBody AdminSubChunkDto req) {
        subChunkRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("SubChunk not found: " + id));
        SubChunk sc = subChunkAssembler.fromDto(req);
        sc.setId(id); // ensure ID matches path
        return ResponseEntity.ok(subChunkAssembler.toDto(subChunkRepository.save(sc), questionRepository.findBySubChunkId(sc.getId()).size()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        questionRepository.deleteAll(questionRepository.findBySubChunkId(id));
        subChunkRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
