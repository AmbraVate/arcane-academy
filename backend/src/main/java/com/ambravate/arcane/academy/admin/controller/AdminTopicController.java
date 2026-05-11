package com.ambravate.arcane.academy.admin.controller;

import com.ambravate.arcane.academy.common.domain.Topic;
import com.ambravate.arcane.academy.common.repository.ChunkRepository;
import com.ambravate.arcane.academy.common.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/topics")
@RequiredArgsConstructor
public class AdminTopicController {

    private final TopicRepository topicRepository;
    private final ChunkRepository chunkRepository;

    @GetMapping
    public ResponseEntity<List<Topic>> list() {
        return ResponseEntity.ok(topicRepository.findAllByOrderBySortOrderAsc());
    }

    @PostMapping
    public ResponseEntity<Topic> create(@RequestBody Topic req) {
        if (topicRepository.existsById(req.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(topicRepository.save(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Topic> update(@PathVariable String id, @RequestBody Topic req) {
        Topic existing = topicRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Topic not found: " + id));
        existing.setName(req.getName());
        existing.setGlyph(req.getGlyph());
        existing.setTagline(req.getTagline());
        existing.setAccentColor(req.getAccentColor());
        existing.setSortOrder(req.getSortOrder());
        existing.setActive(req.isActive());
        return ResponseEntity.ok(topicRepository.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        long chunkCount = chunkRepository.findByTopicIdOrderBySortOrderAsc(id).size();
        if (chunkCount > 0) {
            return ResponseEntity.status(409).build();
        }
        topicRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
