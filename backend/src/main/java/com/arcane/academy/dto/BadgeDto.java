package com.arcane.academy.dto;

import lombok.*;
import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BadgeDto {
    private String id;
    private String displayName;
    private String description;
    private String glyph;
    private String category;
    private boolean earned;
    private Instant earnedAt;
}
