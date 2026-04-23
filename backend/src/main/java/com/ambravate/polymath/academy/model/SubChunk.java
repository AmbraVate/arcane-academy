package com.ambravate.polymath.academy.model;

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
}
