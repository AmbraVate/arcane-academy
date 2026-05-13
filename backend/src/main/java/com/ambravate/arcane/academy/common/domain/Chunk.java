package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chunks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Chunk {

    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    private String glyph;

    private int sortOrder;

    @Column(columnDefinition = "TEXT")
    private String prerequisiteIds;

    /** FOUNDATION | PRACTITIONER | EXPERT */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private LearnerPath tier = LearnerPath.FOUNDATION;

    /** Topic this chunk belongs to — e.g. "java", "tailwind" */
    @Column(nullable = false)
    @Builder.Default
    private String topicId = "java";
}
