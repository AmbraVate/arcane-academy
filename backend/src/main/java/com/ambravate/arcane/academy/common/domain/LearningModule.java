package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "modules")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LearningModule {

    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    private String glyph;

    private int sortOrder;

    /**
     * @deprecated Replaced by the {@link #prerequisites} junction-table relation.
     */
    @Deprecated
    @Column(columnDefinition = "TEXT")
    private String prerequisiteIds;

    /**
     * Modules that must be fully completed before this module can be started.
     * Backed by the {@code module_prerequisites} junction table.
     */
    @ManyToMany
    @JoinTable(
        name = "module_prerequisites",
        joinColumns = @JoinColumn(name = "module_id"),
        inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    @Builder.Default
    private List<LearningModule> prerequisites = new ArrayList<>();

    /** APPRENTICE | JUNIOR | SENIOR | LEAD */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private LearnerPath tier = LearnerPath.APPRENTICE;

    /** Track this module belongs to — e.g. "java", "python", "psychology" */
    @Column(name = "track_id", nullable = false)
    @Builder.Default
    private String trackId = "java";
}
