package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "stuck_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StuckReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    private String username;
    private String email;

    private String domainId;
    private String lessonId;
    private String currentPhase;

    @Column(length = 2048)
    private String currentUrl;

    @Column(length = 2048)
    private String userMessage;

    @Column(length = 512)
    private String userAgent;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StuckReportStatus status = StuckReportStatus.NEW;

    @Column(length = 2048)
    private String adminNotes;

    /** Base64-encoded PNG screenshot captured at the moment the stuck report was submitted. */
    @Column(columnDefinition = "TEXT")
    private String screenshotData;

    @Builder.Default
    private Instant createdAt = Instant.now();

    private Instant updatedAt;
}
