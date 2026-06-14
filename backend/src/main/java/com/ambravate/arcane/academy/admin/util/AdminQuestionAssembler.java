// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.admin.util;

import com.ambravate.arcane.academy.admin.dto.AdminQuestionDto;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.common.domain.QuestionTier;
import com.ambravate.arcane.academy.common.domain.QuestionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AdminQuestionAssembler {

  private final ObjectMapper objectMapper;

  public AdminQuestionAssembler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public AdminQuestionDto toDto(Question q) {
    List<String> options = null;
    if (q.getOptionsJson() != null) {
      try { options = objectMapper.readValue(q.getOptionsJson(), List.class); } catch (Exception ignored) {}
    }
    return AdminQuestionDto.builder()
        .id(q.getId()).lessonId(q.getLessonId())
        .type(q.getType().name()).tier(q.getTier().name())
        .questionHtml(q.getQuestionHtml()).codeSnippet(q.getCodeSnippet())
        .options(options).correctAnswer(q.getCorrectAnswer())
        .explanationHtml(q.getExplanationHtml())
        .build();
  }

  public Question fromDto(AdminQuestionDto dto) {
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
        ? LearnerPath.JUNIOR : LearnerPath.APPRENTICE;

    return Question.builder()
        .lessonId(dto.getLessonId())
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
