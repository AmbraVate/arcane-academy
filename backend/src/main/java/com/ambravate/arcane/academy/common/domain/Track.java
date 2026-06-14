package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tracks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Track {

    /** Slug — also used as trackId on LearningModule. E.g. "java", "python", "tailwind" */
    @Id
    private String id;

    @Column(name = "domain_id", nullable = false)
    private String domainId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int sortOrder;

}
