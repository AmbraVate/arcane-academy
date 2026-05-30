package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * A concept-cluster that groups related Lessons within a Module.
 *
 * <p>Canonical hierarchy:
 * <pre>
 *   School → Domain → Tier → Module → Topic → Lesson
 * </pre>
 *
 * Every Topic belongs to exactly one Module. A Module will typically have
 * several Topics (e.g. "Variables & State", "Control Flow"). Lessons are
 * grouped under their Topic in the module map UI.
 */
@Entity
@Table(name = "topics")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Topic {

    @Id
    private String id;

    /** The Module this Topic belongs to. */
    @Column(name = "module_id", nullable = false)
    private String moduleId;

    @Column(nullable = false)
    private String title;

    /** Optional prose describing what capability this topic cluster creates. */
    @Column(name = "purpose_html", columnDefinition = "TEXT")
    private String purposeHtml;

    /** JSON array of plain-text learning-outcome strings. */
    @Column(name = "learning_outcomes_json", columnDefinition = "TEXT")
    private String learningOutcomesJson;

    @Column(name = "sort_order")
    @Builder.Default
    private int sortOrder = 0;
}
