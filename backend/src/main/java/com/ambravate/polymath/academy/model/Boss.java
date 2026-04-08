package com.ambravate.polymath.academy.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "bosses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Boss {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    private String glyph;
    private int chapterNumber;
    private int xpReward;

    @Column(columnDefinition = "TEXT")
    private String intro;

    // JSON array of question objects
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String questionsJson;
}
