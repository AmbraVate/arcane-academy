package com.ambravate.polymath.academy.dto.admin;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminChunkDto {
    private String id;
    private String title;
    private String glyph;
    private int sortOrder;
    private String tier;
    private String topicId;
    private List<String> prerequisiteIds;
    private long subChunkCount;
}
