// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.admin.util;

import com.ambravate.arcane.academy.admin.dto.AdminChunkDto;
import com.ambravate.arcane.academy.common.domain.Chunk;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ChunkAssembler {

  private final ObjectMapper objectMapper;

  public ChunkAssembler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public AdminChunkDto toDto(Chunk c, long subCount) {
    List<String> prereqs = List.of();
    if (c.getPrerequisiteIds() != null && !c.getPrerequisiteIds().isBlank()) {
      try { prereqs = objectMapper.readValue(c.getPrerequisiteIds(), List.class); } catch (Exception ignored) {}
    }
    return AdminChunkDto.builder()
        .id(c.getId()).title(c.getTitle()).glyph(c.getGlyph())
        .sortOrder(c.getSortOrder()).tier(c.getTier().name())
        .topicId(c.getTopicId()).prerequisiteIds(prereqs)
        .subChunkCount(subCount)
        .build();
  }

}
