package com.ambravate.arcane.academy.admin.controller;

import com.ambravate.arcane.academy.admin.dto.StuckReportDto;
import com.ambravate.arcane.academy.admin.service.StuckReportService;
import com.ambravate.arcane.academy.common.domain.StuckReport;
import com.ambravate.arcane.academy.common.domain.StuckReportStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class StuckReportController {

    private final StuckReportService stuckReportService;
    private final UserRepository userRepository;

    /**
     * Any authenticated learner can file a stuck report.
     * Captures the current URL, phase, and an optional free-text message.
     */
    @PostMapping("/api/stuck-reports")
    public ResponseEntity<Map<String, String>> submit(
            @RequestBody Map<String, String> body,
            @RequestHeader(value = "User-Agent", defaultValue = "") String userAgent,
            @AuthenticationPrincipal UserPrincipal principal) {

        User user = userRepository.findById(principal.getId()).orElseThrow();

        StuckReport report = StuckReport.builder()
                .userId(principal.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .domainId(body.get("domainId"))
                .lessonId(body.get("lessonId"))
                .currentPhase(body.get("currentPhase"))
                .currentUrl(body.get("currentUrl"))
                .userMessage(body.get("userMessage"))
                .userAgent(userAgent)
                .screenshotData(body.get("screenshotData"))
                .createdAt(Instant.now())
                .build();

        stuckReportService.submit(report);
        return ResponseEntity.ok(Map.of("message", "Report received â€” we'll look into it."));
    }

    /** Learner: list their own stuck reports (no screenshot). */
    @GetMapping("/api/stuck-reports/mine")
    public ResponseEntity<java.util.List<StuckReportDto>> listMine(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(stuckReportService.listForUser(principal.getId()));
    }

    /** Admin: list all stuck reports, paginated, newest first. */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/stuck-reports")
    public ResponseEntity<Page<StuckReportDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return ResponseEntity.ok(stuckReportService.listAll(page, size));
    }

    /** Admin: update status and/or add notes to a report. */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/api/admin/stuck-reports/{id}")
    public ResponseEntity<StuckReportDto> updateStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        StuckReportStatus status = StuckReportStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(stuckReportService.updateStatus(id, status, body.get("adminNotes")));
    }
}
