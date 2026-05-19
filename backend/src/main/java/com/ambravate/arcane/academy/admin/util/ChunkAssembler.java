// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.admin.util;

import com.ambravate.arcane.academy.admin.dto.AdminChunkDto;
import com.ambravate.arcane.academy.common.domain.Chunk;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ChunkAssembler {

  public AdminChunkDto toDto(Chunk c, long subCount) {
    List<String> prereqs = c.getPrerequisites().stream()
        .map(Chunk::getId)
        .toList();
    return AdminChunkDto.builder()
        .id(c.getId()).title(c.getTitle()).glyph(c.getGlyph())
        .sortOrder(c.getSortOrder()).tier(c.getTier().name())
        .topicId(c.getTopicId()).prerequisiteIds(prereqs)
        .subChunkCount(subCount)
        .build();
  }

}
