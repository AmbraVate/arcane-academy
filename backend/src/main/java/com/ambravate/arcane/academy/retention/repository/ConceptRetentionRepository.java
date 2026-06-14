package com.ambravate.arcane.academy.retention.repository;

import com.ambravate.arcane.academy.retention.domain.ConceptRetention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConceptRetentionRepository extends JpaRepository<ConceptRetention, String> {

    Optional<ConceptRetention> findByUserIdAndLessonId(String userId, String lessonId);

    @Query("""
        SELECT cr FROM ConceptRetention cr
        WHERE cr.userId = :userId
          AND cr.nextReviewDate IS NOT NULL
          AND cr.nextReviewDate <= :today
        ORDER BY cr.retentionScore ASC, cr.nextReviewDate ASC
        """)
    List<ConceptRetention> findDueForReview(@Param("userId") String userId,
                                            @Param("today") LocalDate today);
}
