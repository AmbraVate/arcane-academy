package com.ambravate.arcane.academy.content.repository;

import com.ambravate.arcane.academy.common.domain.GuidedStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuidedStepRepository extends JpaRepository<GuidedStep, String> {

    List<GuidedStep> findByLessonIdOrderBySortOrderAsc(String lessonId);

    long countByLessonId(String lessonId);

    void deleteByLessonId(String lessonId);
}
