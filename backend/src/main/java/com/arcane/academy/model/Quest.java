// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.arcane.academy.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "quests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Quest {

    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    private String eyebrow;
    private String topic;
    private int chapterNumber;
    private int orderInChapter;
    private int xpReward;
    private String filename;

    @Column(columnDefinition = "TEXT")
    private String problemHtml;

    @Column(columnDefinition = "TEXT")
    private String hint;

    @Column(columnDefinition = "TEXT")
    private String starterCode;

    @Column(columnDefinition = "TEXT")
    private String winStory;

    // JSON arrays stored as text
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String storyJson;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String testCasesJson;
}
