package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * @deprecated Replaced by the {@link #prerequisites} junction-table relation.
     * Retained for backward compatibility during the migration; will be dropped once
     * all application code reads from {@code prerequisites}.
     */
    @Deprecated
    @Column(columnDefinition = "TEXT")
    private String prerequisiteIds;

    /**
     * Chunks that must be fully completed before this chunk can be started.
     * Backed by the {@code chunk_prerequisites} junction table (V6 migration).
     */
    @ManyToMany
    @JoinTable(
        name = "chunk_prerequisites",
        joinColumns = @JoinColumn(name = "chunk_id"),
        inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    @Builder.Default
    private List<Chunk> prerequisites = new ArrayList<>();

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
