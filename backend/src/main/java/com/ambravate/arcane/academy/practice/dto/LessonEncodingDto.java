package com.ambravate.arcane.academy.practice.dto;

import com.ambravate.arcane.academy.common.dto.QuestionDto;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LessonEncodingDto {
    private String lessonId;
    private String moduleId;
    private String domainId;
    private String title;
    private String phase;
    private String status;

    private String hookHtml;
    private String explanationHtml;
    private Object storyBeats;
    private String guidedPracticeHtml;
    private String soloPracticeHtml;
    private String starterCode;
    private Object testCaseLabels;
    private String filename;
    private List<QuestionDto> retrievalQuestions;
    private String feynmanPrompt;
    private int xpReward;
    private String practiceType;

    private Object rabbitHoleTerms;
    private String modelAnswer;
    private String guidedPracticeModelAnswer;

    private List<String> learningObjectives;
    private Object challenge;
    private String miniProject;
    private List<String> commonMistakes;
    private List<String> assessmentCriteria;
    private Object downloadables;

    private String integrationPrompt;
    private String questType;

    // Phase 2 — Markdown section fields (null for JSON-seeded lessons)
    private String loreIntroHtml;
    private String whyItMattersHtml;
    private String workedExamplesHtml;
    private String mentalModelHtml;
    private String miniSummaryHtml;
    private String loreConclusionHtml;

    // Phase 3 — guided step engine
    /** True when this lesson has at least one guided step; the frontend switches to GuidedStepper. */
    private boolean hasGuidedSteps;
}
