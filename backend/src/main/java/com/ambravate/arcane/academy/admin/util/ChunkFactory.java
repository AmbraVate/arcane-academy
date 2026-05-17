// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.admin.util;

import com.ambravate.arcane.academy.admin.dto.AdminChunkDto;
import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ChunkFactory {

  private final ObjectMapper objectMapper;

  public ChunkFactory(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }


  public Chunk buildChunk(AdminChunkDto req) {
    return Chunk.builder()
        .id(req.getId()).title(req.getTitle()).glyph(req.getGlyph())
        .sortOrder(req.getSortOrder())
        .tier(req.getTier() != null ? LearnerPath.valueOf(req.getTier()) : LearnerPath.FOUNDATION)
        .topicId(req.getTopicId() != null ? req.getTopicId() : "java")
        .prerequisiteIds(toJson(req.getPrerequisiteIds()))
        .build();
  }

  private String toJson(List<String> list) {
    if (list == null || list.isEmpty()) return "[]";
    try { return objectMapper.writeValueAsString(list); } catch (Exception e) { return "[]"; }
  }

}
