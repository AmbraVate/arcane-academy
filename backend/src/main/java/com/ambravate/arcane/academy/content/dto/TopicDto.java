package com.ambravate.arcane.academy.content.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TopicDto {
    private String id;
    private String title;
    private String purposeHtml;
    private int sortOrder;
}
