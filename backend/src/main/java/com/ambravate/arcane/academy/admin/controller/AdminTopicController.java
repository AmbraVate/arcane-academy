package com.ambravate.arcane.academy.admin.controller;

import com.ambravate.arcane.academy.admin.dto.AdminTopicDto;
import com.ambravate.arcane.academy.common.domain.Topic;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.content.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Admin CRUD for the Topic entity (Module → Topic → Lesson cluster level).
 *
 * <p>Topics group related Lessons within a Module. Every Module automatically
 * receives a default Topic via the V29 migration and content seeder.
 * Admins can create additional Topics and reassign Lessons via this API.
 */
@RestController
@RequestMapping("/api/admin/topics")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTopicController {

    private final TopicRepository topicRepository;
    private final LessonRepository lessonRepository;

    @GetMapping
    public ResponseEntity<List<AdminTopicDto>> list(
            @RequestParam(required = false) String moduleId) {
        List<Topic> topics = (moduleId != null)
                ? topicRepository.findByModuleIdOrderBySortOrderAsc(moduleId)
                : topicRepository.findAll();
        return ResponseEntity.ok(topics.stream().map(this::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminTopicDto> get(@PathVariable String id) {
        return topicRepository.findById(id)
                .map(t -> ResponseEntity.ok(toDto(t)))
                .orElseThrow(() -> new NoSuchElementException("Topic not found: " + id));
    }

    @PostMapping
    public ResponseEntity<AdminTopicDto> create(@RequestBody AdminTopicDto req) {
        if (topicRepository.existsById(req.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(toDto(topicRepository.save(fromDto(req))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminTopicDto> update(@PathVariable String id, @RequestBody AdminTopicDto req) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Topic not found: " + id));
        topic.setTitle(req.getTitle());
        topic.setPurposeHtml(req.getPurposeHtml());
        topic.setLearningOutcomesJson(req.getLearningOutcomesJson());
        topic.setSortOrder(req.getSortOrder());
        return ResponseEntity.ok(toDto(topicRepository.save(topic)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        long lessonCount = lessonRepository.countByTopicId(id);
        if (lessonCount > 0) {
            return ResponseEntity.status(409).build();
        }
        topicRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private AdminTopicDto toDto(Topic t) {
        return AdminTopicDto.builder()
                .id(t.getId()).moduleId(t.getModuleId()).title(t.getTitle())
                .purposeHtml(t.getPurposeHtml())
                .learningOutcomesJson(t.getLearningOutcomesJson())
                .sortOrder(t.getSortOrder())
                .build();
    }

    private Topic fromDto(AdminTopicDto req) {
        return Topic.builder()
                .id(req.getId()).moduleId(req.getModuleId()).title(req.getTitle())
                .purposeHtml(req.getPurposeHtml())
                .learningOutcomesJson(req.getLearningOutcomesJson())
                .sortOrder(req.getSortOrder())
                .build();
    }
}
