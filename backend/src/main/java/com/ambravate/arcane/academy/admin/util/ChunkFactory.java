// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.admin.util;

import com.ambravate.arcane.academy.admin.dto.AdminChunkDto;
import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.content.repository.ChunkRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ChunkFactory {

  private final ChunkRepository chunkRepository;

  public ChunkFactory(ChunkRepository chunkRepository) {
    this.chunkRepository = chunkRepository;
  }

  public Chunk buildChunk(AdminChunkDto req) {
    List<Chunk> prereqChunks = req.getPrerequisiteIds() == null ? List.of() :
        req.getPrerequisiteIds().stream()
            .map(pId -> chunkRepository.findById(pId)
                .orElseThrow(() -> new IllegalArgumentException("Prerequisite chunk not found: " + pId)))
            .toList();
    return Chunk.builder()
        .id(req.getId()).title(req.getTitle()).glyph(req.getGlyph())
        .sortOrder(req.getSortOrder())
        .tier(req.getTier() != null ? LearnerPath.valueOf(req.getTier()) : LearnerPath.FOUNDATION)
        .topicId(req.getTopicId() != null ? req.getTopicId() : "java")
        .prerequisites(prereqChunks)
        .build();
  }

}
