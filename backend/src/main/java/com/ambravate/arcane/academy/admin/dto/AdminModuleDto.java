package com.ambravate.arcane.academy.admin.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminModuleDto {
    private String id;
    private String title;
    private String glyph;
    private int sortOrder;
    private String tier;
    private String domainId;
    private List<String> prerequisiteIds;
    private long lessonCount;
}
