package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sub_chunks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SubChunk {

    @Id
    private String id;

    @Column(name = "chunk_id", nullable = false)
    private String chunkId;

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

    /** Which editor + pipeline the GUIDED_PRACTICE phase uses. */
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SubChunkPracticeType practiceType = SubChunkPracticeType.JAVA;

    /** JSON array of { term, description } objects highlighted in story beats as rabbit-hole terms. */
    @Column(columnDefinition = "TEXT")
    private String rabbitHoleTermsJson;

    /**
     * Optional model answer shown to the learner after solo practice is complete.
     * Intended for written-response (NONE practice type) sub-chunks where self-assessment
     * against an exemplar is pedagogically more useful than a pass/fail score alone.
     */
    @Column(columnDefinition = "TEXT")
    private String modelAnswer;
}
