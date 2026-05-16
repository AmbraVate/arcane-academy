package com.ambravate.arcane.academy.profile.dto;

import lombok.*;

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
