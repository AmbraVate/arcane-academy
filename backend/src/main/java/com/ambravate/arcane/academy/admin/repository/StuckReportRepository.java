package com.ambravate.arcane.academy.admin.repository;

import com.ambravate.arcane.academy.common.domain.StuckReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StuckReportRepository extends JpaRepository<StuckReport, String> {
    Page<StuckReport> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<StuckReport> findByUserIdOrderByCreatedAtDesc(String userId);
}
