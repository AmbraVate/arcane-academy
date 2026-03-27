// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.arcane.academy.dto;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuestSummaryDto {
    private String id;
    private String title;
    private String eyebrow;
    private String topic;
    private int chapterNumber;
    private int orderInChapter;
    private int xpReward;
    private boolean completed;
    private boolean locked;
}
