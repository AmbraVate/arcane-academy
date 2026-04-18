package com.ambravate.polymath.academy.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChunkDetailDto {
    private String id;
    private String topicId;
    private String title;
    private String glyph;
    private String status;
    private List<SubChunkSummaryDto> subChunks;
}
