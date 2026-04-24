package com.ambravate.polymath.academy.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "topics")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Topic {

    /** Slug — also used as topicId on Chunk. E.g. "java", "tailwind" */
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    private String glyph;

    @Column(columnDefinition = "TEXT")
    private String tagline;

    /** Hex colour used for UI accents on this topic */
    private String accentColor;

    private int sortOrder;

    @Builder.Default
    private boolean active = true;
}
