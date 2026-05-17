package com.ambravate.arcane.academy.admin.service;

import com.ambravate.arcane.academy.admin.dto.StuckReportDto;
import com.ambravate.arcane.academy.common.domain.StuckReport;
import com.ambravate.arcane.academy.common.domain.StuckReportStatus;
import com.ambravate.arcane.academy.common.repository.StuckReportRepository;
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
                .topicId(r.getTopicId())
                .subChunkId(r.getSubChunkId())
                .currentPhase(r.getCurrentPhase())
                .currentUrl(r.getCurrentUrl())
                .userMessage(r.getUserMessage())
                .userAgent(r.getUserAgent())
                .status(r.getStatus().name())
                .adminNotes(r.getAdminNotes())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
