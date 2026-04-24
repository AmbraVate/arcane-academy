package com.ambravate.polymath.academy.dto.admin;

import lombok.*;
import java.util.List;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminSubChunkDto {
    private String id;
    private String chunkId;
    private String title;
    private int sortOrder;
    private int xpReward;
    private String practiceType;
    private String filename;

    private String hookHtml;
    private String explanationHtml;
    private List<Map<String, Object>> storyBeats;
    private String guidedPracticeHtml;
    private String guidedPracticeStarterCode;
    private List<Map<String, Object>> guidedPracticeTests;
    private String soloPracticeHtml;
    private String feynmanPrompt;

    private long questionCount;
}
