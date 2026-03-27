// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.arcane.academy.dto;
import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuestDetailDto {
    private String id;
    private String title;
    private String eyebrow;
    private String topic;
    private int xpReward;
    private String filename;
    private String problemHtml;
    private String hint;
    private String starterCode;
    private String winStory;
    private Object story;
    private Object testCaseLabels;
    private boolean completed;
}
