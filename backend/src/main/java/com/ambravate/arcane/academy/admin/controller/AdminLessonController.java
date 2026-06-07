package com.ambravate.arcane.academy.admin.controller;

import com.ambravate.arcane.academy.admin.dto.AdminLessonDto;
import com.ambravate.arcane.academy.admin.util.AdminLessonAssembler;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.content.repository.QuestionRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/lessons")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminLessonController {

    private final LessonRepository lessonRepository;
    private final QuestionRepository questionRepository;
    private final AdminLessonAssembler lessonAssembler;
    private final UserChunkProgressRepository progressRepository;

    @GetMapping
    public ResponseEntity<List<AdminLessonDto>> list(@RequestParam String moduleId) {
        return ResponseEntity.ok(
                lessonRepository.findByModuleIdOrderBySortOrderAsc(moduleId).stream()
                    .map(l -> lessonAssembler.toDto(l, questionRepository.findByLessonId(l.getId()).size()))
                    .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminLessonDto> get(@PathVariable String id) {
        return lessonRepository.findById(id)
                .map(l -> ResponseEntity.ok(lessonAssembler.toDto(l, questionRepository.findByLessonId(l.getId()).size())))
                .orElseThrow(() -> new NoSuchElementException("Lesson not found: " + id));
    }

    @PostMapping
    public ResponseEntity<AdminLessonDto> create(@Valid @RequestBody AdminLessonDto req) {
        Lesson l = lessonAssembler.fromDto(req);
        return ResponseEntity.ok(lessonAssembler.toDto(lessonRepository.save(l), questionRepository.findByLessonId(l.getId()).size()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminLessonDto> update(@PathVariable String id, @Valid @RequestBody AdminLessonDto req) {
        lessonRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Lesson not found: " + id));
        Lesson l = lessonAssembler.fromDto(req);
        l.setId(id);
        return ResponseEntity.ok(lessonAssembler.toDto(lessonRepository.save(l), questionRepository.findByLessonId(l.getId()).size()));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (progressRepository.existsByLessonId(id)) {
            return ResponseEntity.status(409).build();
        }
        questionRepository.deleteAll(questionRepository.findByLessonId(id));
        lessonRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
