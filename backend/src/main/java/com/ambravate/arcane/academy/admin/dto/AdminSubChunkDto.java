package com.ambravate.arcane.academy.admin.dto;

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
    private String guidedPracticeModelAnswer;
    private String soloPracticeHtml;
    private String modelAnswer;
    private String feynmanPrompt;
    private List<Map<String, Object>> rabbitHoleTerms;

    private long questionCount;

    // Sprint 1 — structured lesson metadata
    private List<String> learningObjectives;
    private Map<String, Object> challenge;  // { html, starterCode, tests }
    private String miniProject;
    private List<String> commonMistakes;
    private List<String> assessmentCriteria;

    // Sprint 7 — downloadable resources
    /** Each entry: { title, type, url } */
    private List<Map<String, Object>> downloadables;
}
