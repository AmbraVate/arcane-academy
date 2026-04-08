package com.ambravate.polymath.academy.dto;
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
    private boolean sideQuest;
}
