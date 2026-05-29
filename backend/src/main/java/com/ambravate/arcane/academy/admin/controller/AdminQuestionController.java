package com.ambravate.arcane.academy.admin.controller;

import com.ambravate.arcane.academy.admin.dto.AdminQuestionDto;
import com.ambravate.arcane.academy.admin.util.AdminQuestionAssembler;
import com.ambravate.arcane.academy.common.domain.Question;

import com.ambravate.arcane.academy.content.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/questions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminQuestionController {

    private final QuestionRepository questionRepository;
    private final AdminQuestionAssembler adminQuestionAssembler;

    @GetMapping
    public ResponseEntity<List<AdminQuestionDto>> list(@RequestParam String subChunkId) {
        return ResponseEntity.ok(
                questionRepository.findBySubChunkId(subChunkId)
                        .stream()
                    .map(adminQuestionAssembler::toDto)
                    .toList());
    }

    @PostMapping
    public ResponseEntity<AdminQuestionDto> create(@RequestBody AdminQuestionDto req) {
        Question q = adminQuestionAssembler.fromDto(req);
        q.setId(null); // let UUID generate
        return ResponseEntity.ok(adminQuestionAssembler.toDto(questionRepository.save(q)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminQuestionDto> update(@PathVariable String id,
                                                    @RequestBody AdminQuestionDto req) {
        Question existing = questionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Question not found: " + id));
        Question updated = adminQuestionAssembler.fromDto(req);
        updated.setId(existing.getId());
        return ResponseEntity.ok(adminQuestionAssembler.toDto(questionRepository.save(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        questionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
