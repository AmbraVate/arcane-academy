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

    /**
     * Optional exemplar answer revealed after guided practice is passed (NONE practice type).
     * Gives learners a benchmark to compare their guided attempt against before moving to solo.
     */
    @Column(columnDefinition = "TEXT")
    private String guidedPracticeModelAnswer;

    // ── Structured lesson metadata (added V15) ─────────────────────────────────

    /** JSON array of strings — learning objectives shown before story beats. */
    @Column(columnDefinition = "TEXT")
    private String learningObjectivesJson;

    /** HTML description for the challenge exercise (harder than solo practice). */
    @Column(columnDefinition = "TEXT")
    private String challengeHtml;

    /** Starter code for the challenge exercise. */
    @Column(columnDefinition = "TEXT")
    private String challengeStarterCode;

    /** JSON array of test objects for the challenge exercise (same format as guidedPracticeTestsJson). */
    @Column(columnDefinition = "TEXT")
    private String challengeTestsJson;

    /** HTML description of the mini project (larger open-ended task, no sandbox). */
    @Column(columnDefinition = "TEXT")
    private String miniProjectHtml;

    /** JSON array of strings — common mistakes shown at end of sub-chunk. */
    @Column(columnDefinition = "TEXT")
    private String commonMistakesJson;

    /** JSON array of strings — assessment criteria ("Can X", "Writes Y correctly"). */
    @Column(columnDefinition = "TEXT")
    private String assessmentCriteriaJson;

    /**
     * JSON array of downloadable resources shown as chips at the start of the EXPLANATION phase.
     * Each entry: { "title": "...", "type": "pdf", "url": "/downloads/filename.pdf" }
     */
    @Column(columnDefinition = "TEXT")
    private String downloadablesJson;
}
