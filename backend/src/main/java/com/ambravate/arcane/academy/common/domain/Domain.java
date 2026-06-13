package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "domains")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Domain {

    /** Slug — also used as domainId on LearningModule. E.g. "java", "psychology" */
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    private String glyph;

    @Column(columnDefinition = "TEXT")
    private String tagline;

    /** Hex colour used for UI accents on this domain */
    private String accentColor;

    private int sortOrder;

    @Builder.Default
    private boolean active = true;

    /** Guild name for fantasy narrative overlay (e.g. "Guild of Systems Architects") */
    private String guildName;

    /** FK to schools.id — the school this domain belongs to */
    private String schoolId;

    /** JSON object mapping tier keys to display labels, e.g. {"APPRENTICE":"Intern","JUNIOR":"Associate",...} */
    @Column(columnDefinition = "TEXT")
    private String tierLabels;
}
