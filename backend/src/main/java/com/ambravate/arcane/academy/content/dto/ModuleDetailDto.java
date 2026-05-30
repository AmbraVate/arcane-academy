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
    /** Ordered topic clusters for this module — used by the UI to group lessons. */
    private List<TopicDto> topics;
    /** Flat lesson list with topicId/topicTitle set per lesson for grouping. */
    private List<LessonSummaryDto> lessons;
}
