package com.ambravate.arcane.academy.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.List;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminLessonDto {
    @NotBlank private String id;
    @NotBlank private String moduleId;
    @NotBlank private String title;
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

    private List<String> learningObjectives;
    private Map<String, Object> challenge;
    private String miniProject;
    private List<String> commonMistakes;
    private List<String> assessmentCriteria;
    private List<Map<String, Object>> downloadables;
}
