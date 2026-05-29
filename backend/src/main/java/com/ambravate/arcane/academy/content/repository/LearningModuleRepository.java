package com.ambravate.arcane.academy.content.repository;

import com.ambravate.arcane.academy.common.domain.LearningModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningModuleRepository extends JpaRepository<LearningModule, String> {
    List<LearningModule> findAllByOrderBySortOrderAsc();
    List<LearningModule> findByTrackIdOrderBySortOrderAsc(String trackId);
}
