package com.ambravate.arcane.academy.admin.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Validated request body for {@code POST /api/stuck-reports}.
 * Size limits prevent DoS via oversized payloads (especially screenshotData).
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StuckReportRequest {

    @Size(max = 100)  private String domainId;
    @Size(max = 100)  private String lessonId;
    @Size(max = 50)   private String currentPhase;
    @Size(max = 500)  private String currentUrl;
    @Size(max = 2000) private String userMessage;

    /**
     * Base64-encoded PNG screenshot. Capped at ~375 KB decoded —
     * sufficient for a compressed screenshot; prevents storage exhaustion.
     */
    @Size(max = 500_000) private String screenshotData;
}
