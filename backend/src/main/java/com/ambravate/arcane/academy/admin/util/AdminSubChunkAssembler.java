// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.admin.util;

import com.ambravate.arcane.academy.admin.dto.AdminSubChunkDto;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkPracticeType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

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
        .soloPracticeHtml(sc.getSoloPracticeHtml())
        .feynmanPrompt(sc.getFeynmanPrompt())
        .questionCount(qCount)
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
        .soloPracticeHtml(dto.getSoloPracticeHtml())
        .feynmanPrompt(dto.getFeynmanPrompt())
        .build();
  }

  private List<Map<String, Object>> parseJsonList(String json) {
    if (json == null || json.isBlank()) return List.of();
    try { return objectMapper.readValue(json, new TypeReference<>() {}); } catch (Exception e) { return List.of(); }
  }

  private String toJson(List<?> list) {
    if (list == null || list.isEmpty()) return null;
    try { return objectMapper.writeValueAsString(list); } catch (Exception e) { return null; }
  }

}
