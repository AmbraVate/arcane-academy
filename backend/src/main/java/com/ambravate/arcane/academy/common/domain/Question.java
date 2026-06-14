package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "questions",
    indexes = {
        @Index(name = "idx_question_lesson_tier", columnList = "lesson_id, tier"),
    })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "lesson_id", nullable = false)
    private String lessonId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionTier tier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String questionHtml;

    @Column(columnDefinition = "TEXT")
    private String codeSnippet;

    @Column(columnDefinition = "TEXT")
    private String optionsJson;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String correctAnswer;

    @Column(columnDefinition = "TEXT")
    private String explanationHtml;

    @Column(columnDefinition = "TEXT")
    private String crossChunkIds;

    @Builder.Default
    private int difficultyLevel = 1;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LearnerPath minPath = LearnerPath.APPRENTICE;
}
