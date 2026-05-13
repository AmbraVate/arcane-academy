package com.ambravate.arcane.academy.common.dto;

import lombok.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(
    setterPrefix = "with",
    builderMethodName = "aBadgeDto"
)
public class BadgeDto {
    private String id;
    private String displayName;
    private String description;
    private String glyph;
    private String category;
    private boolean earned;
    private Instant earnedAt;
}
