package com.ambravate.polymath.academy.dto;
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
