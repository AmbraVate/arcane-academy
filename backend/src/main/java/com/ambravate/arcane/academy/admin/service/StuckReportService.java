package com.ambravate.arcane.academy.admin.service;

import com.ambravate.arcane.academy.admin.dto.StuckReportDto;
import com.ambravate.arcane.academy.common.domain.StuckReport;
import com.ambravate.arcane.academy.common.domain.StuckReportStatus;
import com.ambravate.arcane.academy.admin.repository.StuckReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class StuckReportService {

    private final StuckReportRepository repository;

    @Transactional
    public StuckReport submit(StuckReport report) {
        log.info("[StuckReport] Filed by user={} url={}", report.getUserId(), report.getCurrentUrl());
        return repository.save(report);
    }

    public Page<StuckReportDto> listAll(int page, int size) {
        return repository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size))
                .map(this::toDto);
    }

    /** Learner-facing: returns all reports for a given user, without screenshotData. */
    @Transactional(readOnly = true)
    public java.util.List<StuckReportDto> listForUser(String userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(r -> StuckReportDto.builder()
                        .id(r.getId())
                        .userId(r.getUserId())
                        .domainId(r.getDomainId())
                        .lessonId(r.getLessonId())
                        .currentPhase(r.getCurrentPhase())
                        .currentUrl(r.getCurrentUrl())
                        .userMessage(r.getUserMessage())
                        .status(r.getStatus().name())
                        .adminNotes(r.getAdminNotes())
                        // screenshotData deliberately omitted â€” admin only
                        .createdAt(r.getCreatedAt())
                        .updatedAt(r.getUpdatedAt())
                        .build())
                .toList();
    }

    @Transactional
    public StuckReportDto updateStatus(String id, StuckReportStatus status, String adminNotes) {
        StuckReport report = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Stuck report not found: " + id));
        report.setStatus(status);
        if (adminNotes != null) report.setAdminNotes(adminNotes);
        report.setUpdatedAt(Instant.now());
        return toDto(repository.save(report));
    }

    private StuckReportDto toDto(StuckReport r) {
        return StuckReportDto.builder()
                .id(r.getId())
                .userId(r.getUserId())
                .username(r.getUsername())
                .email(r.getEmail())
                .domainId(r.getDomainId())
                .lessonId(r.getLessonId())
                .currentPhase(r.getCurrentPhase())
                .currentUrl(r.getCurrentUrl())
                .userMessage(r.getUserMessage())
                .userAgent(r.getUserAgent())
                .status(r.getStatus().name())
                .adminNotes(r.getAdminNotes())
                .screenshotData(r.getScreenshotData())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
