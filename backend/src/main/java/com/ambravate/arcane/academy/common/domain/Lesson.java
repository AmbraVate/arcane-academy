package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lessons")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Lesson {

    @Id
    private String id;

    @Column(name = "module_id", nullable = false)
    private String moduleId;

    @Column(nullable = false)
    private String title;

    private int sortOrder;

    @Column(columnDefinition = "TEXT")
    private String hookHtml;

    @Column(columnDefinition = "TEXT")
    private String explanationHtml;

    @Column(columnDefinition = "TEXT")
    private String storyJson;

    @Column(columnDefinition = "TEXT")
    private String guidedPracticeHtml;

    @Column(columnDefinition = "TEXT")
    private String guidedPracticeStarterCode;

    @Column(columnDefinition = "TEXT")
    private String guidedPracticeTestsJson;

    private String filename;

    @Column(columnDefinition = "TEXT")
    private String soloPracticeHtml;

    @Column(columnDefinition = "TEXT")
    private String feynmanPrompt;

    @Builder.Default
    private int xpReward = 50;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LessonPracticeType practiceType = LessonPracticeType.JAVA;

    @Column(columnDefinition = "TEXT")
    private String rabbitHoleTermsJson;

    @Column(columnDefinition = "TEXT")
    private String modelAnswer;

    @Column(columnDefinition = "TEXT")
    private String guidedPracticeModelAnswer;

    @Column(columnDefinition = "TEXT")
    private String learningObjectivesJson;

    @Column(columnDefinition = "TEXT")
    private String challengeHtml;

    @Column(columnDefinition = "TEXT")
    private String challengeStarterCode;

    @Column(columnDefinition = "TEXT")
    private String challengeTestsJson;

    @Column(columnDefinition = "TEXT")
    private String miniProjectHtml;

    @Column(columnDefinition = "TEXT")
    private String commonMistakesJson;

    @Column(columnDefinition = "TEXT")
    private String assessmentCriteriaJson;

    @Column(columnDefinition = "TEXT")
    private String downloadablesJson;

    @Column(columnDefinition = "TEXT")
    private String integrationPrompt;

    @Enumerated(EnumType.STRING)
    private QuestType questType;
}
