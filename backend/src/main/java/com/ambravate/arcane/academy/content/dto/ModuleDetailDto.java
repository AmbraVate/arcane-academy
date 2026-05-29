package com.ambravate.arcane.academy.content.dto;

import com.ambravate.arcane.academy.practice.dto.LessonSummaryDto;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ModuleDetailDto {
    private String id;
    private String domainId;
    private String title;
    private String glyph;
    private String status;
    private List<LessonSummaryDto> lessons;
}
