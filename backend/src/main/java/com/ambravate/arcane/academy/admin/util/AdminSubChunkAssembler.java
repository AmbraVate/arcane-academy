// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.admin.util;

import com.ambravate.arcane.academy.admin.dto.AdminSubChunkDto;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkPracticeType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import org.springframework.stereotype.Component;

@Component
public class AdminSubChunkAssembler {

  private final ObjectMapper objectMapper;

  public AdminSubChunkAssembler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public AdminSubChunkDto toDto(SubChunk sc, long qCount) {
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
        .guidedPracticeModelAnswer(sc.getGuidedPracticeModelAnswer())
        .soloPracticeHtml(sc.getSoloPracticeHtml())
        .modelAnswer(sc.getModelAnswer())
        .feynmanPrompt(sc.getFeynmanPrompt())
        .rabbitHoleTerms(parseJsonList(sc.getRabbitHoleTermsJson()))
        .questionCount(qCount)
        .learningObjectives(parseStringList(sc.getLearningObjectivesJson()))
        .challenge(parseJsonMap(sc.getChallengeHtml(), sc.getChallengeStarterCode(), sc.getChallengeTestsJson()))
        .miniProject(sc.getMiniProjectHtml())
        .commonMistakes(parseStringList(sc.getCommonMistakesJson()))
        .assessmentCriteria(parseStringList(sc.getAssessmentCriteriaJson()))
        .downloadables(parseJsonList(sc.getDownloadablesJson()))
        .build();
  }

  public SubChunk fromDto(AdminSubChunkDto dto) {
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
        .guidedPracticeModelAnswer(dto.getGuidedPracticeModelAnswer())
        .soloPracticeHtml(dto.getSoloPracticeHtml())
        .modelAnswer(dto.getModelAnswer())
        .feynmanPrompt(dto.getFeynmanPrompt())
        .rabbitHoleTermsJson(toJson(dto.getRabbitHoleTerms()))
        .build();
  }

  private List<Map<String, Object>> parseJsonList(String json) {
    if (json == null || json.isBlank()) return List.of();
    try { return objectMapper.readValue(json, new TypeReference<>() {}); } catch (Exception e) { return List.of(); }
  }

  private List<String> parseStringList(String json) {
    if (json == null || json.isBlank()) return Collections.emptyList();
    try { return objectMapper.readValue(json, new TypeReference<>() {}); } catch (Exception e) { return Collections.emptyList(); }
  }

  /** Build the challenge map { html, starterCode, tests } only when challengeHtml is set. */
  private Map<String, Object> parseJsonMap(String challengeHtml, String starterCode, String testsJson) {
    if (challengeHtml == null || challengeHtml.isBlank()) return null;
    List<Map<String, Object>> tests = List.of();
    if (testsJson != null && !testsJson.isBlank()) {
      try { tests = objectMapper.readValue(testsJson, new TypeReference<>() {}); } catch (Exception ignored) {}
    }
    return Map.of("html", challengeHtml, "starterCode", starterCode != null ? starterCode : "", "tests", tests);
  }

  private String toJson(List<?> list) {
    if (list == null || list.isEmpty()) return null;
    try { return objectMapper.writeValueAsString(list); } catch (Exception e) { return null; }
  }

}
