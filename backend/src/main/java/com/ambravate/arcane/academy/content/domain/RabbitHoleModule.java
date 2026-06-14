package com.ambravate.arcane.academy.content.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rabbit_hole_modules")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RabbitHoleModule {

    @Id
    private String id;

    @Column(name = "module_id", nullable = false)
    private String moduleId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String contentHtml;

    @Column(columnDefinition = "TEXT")
    private String storyJson;

    @Column(columnDefinition = "TEXT")
    private String starterCode;

    @Column(columnDefinition = "TEXT")
    private String testCasesJson;

    private String filename;

    private int sortOrder;
}
