// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.gamification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(
    setterPrefix = "with",
    builderMethodName = "aChunkHealthDto",
    builderClassName = "ChunkHealthDtoBuilder"
)
public class ChunkHealthDto {
  private String chunkId;
  private String title;
  private String glyph;
  private String status;
  private double memoryStrength;
  private String healthColor;
  private int totalSubChunks;
  private int completedSubChunks;
  private String tier;
}
