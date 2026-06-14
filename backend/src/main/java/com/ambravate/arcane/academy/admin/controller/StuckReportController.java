package com.ambravate.arcane.academy.admin.controller;

import com.ambravate.arcane.academy.admin.dto.StuckReportDto;
import com.ambravate.arcane.academy.admin.dto.StuckReportRequest;
import com.ambravate.arcane.academy.admin.service.StuckReportService;
import com.ambravate.arcane.academy.common.domain.StuckReport;
import com.ambravate.arcane.academy.common.domain.StuckReportStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import jakarta.validation.Valid;
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
     * Body is validated via {@link StuckReportRequest} — screenshotData is capped at ~375 KB
     * to prevent storage exhaustion / DoS.
     */
    @PostMapping("/api/stuck-reports")
    public ResponseEntity<Map<String, String>> submit(
            @Valid @RequestBody StuckReportRequest body,
            @RequestHeader(value = "User-Agent", defaultValue = "") String userAgent,
            @AuthenticationPrincipal UserPrincipal principal) {

        User user = userRepository.findById(principal.getId()).orElseThrow();

        StuckReport report = StuckReport.builder()
                .userId(principal.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .domainId(body.getDomainId())
                .lessonId(body.getLessonId())
                .currentPhase(body.getCurrentPhase())
                .currentUrl(body.getCurrentUrl())
                .userMessage(body.getUserMessage())
                .userAgent(userAgent)
                .screenshotData(body.getScreenshotData())
                .createdAt(Instant.now())
                .build();

        stuckReportService.submit(report);
        return ResponseEntity.ok(Map.of("message", "Report received — we'll look into it."));
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
