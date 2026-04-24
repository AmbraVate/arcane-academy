package com.ambravate.polymath.academy.controller.admin;

import com.ambravate.polymath.academy.dto.admin.AdminQuestionDto;
import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.QuestionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/questions")
@RequiredArgsConstructor
public class AdminQuestionController {

    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<AdminQuestionDto>> list(@RequestParam String subChunkId) {
        return ResponseEntity.ok(
                questionRepository.findBySubChunkId(subChunkId)
                        .stream().map(this::toDto).toList());
    }

    @PostMapping
    public ResponseEntity<AdminQuestionDto> create(@RequestBody AdminQuestionDto req) {
        Question q = fromDto(req);
        q.setId(null); // let UUID generate
        return ResponseEntity.ok(toDto(questionRepository.save(q)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminQuestionDto> update(@PathVariable String id,
                                                    @RequestBody AdminQuestionDto req) {
        Question existing = questionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Question not found: " + id));
        Question updated = fromDto(req);
        updated.setId(existing.getId());
        return ResponseEntity.ok(toDto(questionRepository.save(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        questionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private AdminQuestionDto toDto(Question q) {
        List<String> options = null;
        if (q.getOptionsJson() != null) {
            try { options = objectMapper.readValue(q.getOptionsJson(), List.class); } catch (Exception ignored) {}
        }
        return AdminQuestionDto.builder()
                .id(q.getId()).subChunkId(q.getSubChunkId())
                .type(q.getType().name()).tier(q.getTier().name())
                .questionHtml(q.getQuestionHtml()).codeSnippet(q.getCodeSnippet())
                .options(options).correctAnswer(q.getCorrectAnswer())
                .explanationHtml(q.getExplanationHtml())
                .build();
    }

    private Question fromDto(AdminQuestionDto dto) {
        String optionsJson = null;
        if (dto.getOptions() != null && !dto.getOptions().isEmpty()) {
            try { optionsJson = objectMapper.writeValueAsString(dto.getOptions()); } catch (Exception ignored) {}
        }
        // TRUE_FALSE always gets canonical options
        if ("TRUE_FALSE".equals(dto.getType())) {
            optionsJson = "[\"True\",\"False\"]";
        }

        QuestionTier tier = dto.getTier() != null
                ? QuestionTier.valueOf(dto.getTier()) : QuestionTier.RECALL;
        LearnerPath minPath = tier == QuestionTier.DISCRIMINATION
                ? LearnerPath.PRACTITIONER : LearnerPath.FOUNDATION;

        return Question.builder()
                .subChunkId(dto.getSubChunkId())
                .type(QuestionType.valueOf(dto.getType()))
                .tier(tier).minPath(minPath)
                .questionHtml(dto.getQuestionHtml())
                .codeSnippet(dto.getCodeSnippet())
                .optionsJson(optionsJson)
                .correctAnswer(dto.getCorrectAnswer())
                .explanationHtml(dto.getExplanationHtml())
                .build();
    }
}
