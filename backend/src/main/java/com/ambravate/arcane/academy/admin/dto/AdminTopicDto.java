package com.ambravate.arcane.academy.admin.dto;

import lombok.*;

/**
 * Admin DTO for the Topic entity (Module → Topic → Lesson cluster level).
 * Not to be confused with the old "Topic" which was renamed to Domain.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminTopicDto {
    private String id;
    private String moduleId;
    private String title;
    private String purposeHtml;
    private String learningOutcomesJson;
    private int sortOrder;
}
