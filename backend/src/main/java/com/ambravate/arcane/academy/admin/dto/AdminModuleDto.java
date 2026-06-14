package com.ambravate.arcane.academy.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminModuleDto {
    @NotBlank private String id;
    @NotBlank private String title;
    private String glyph;
    private int sortOrder;
    @NotBlank private String tier;
    @NotBlank private String domainId;
    private List<String> prerequisiteIds;
    private long lessonCount;
}
