package com.ambravate.arcane.academy.practice.repository;

import com.ambravate.arcane.academy.common.domain.LessonStatus;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChunkProgressRepository extends JpaRepository<UserChunkProgress, String> {
    List<UserChunkProgress> findByUserId(String userId);
    Optional<UserChunkProgress> findByUserIdAndLessonId(String userId, String lessonId);
    boolean existsByUserIdAndLessonId(String userId, String lessonId);
    List<UserChunkProgress> findByUserIdAndNextReviewAtBefore(String userId, Instant now);
    List<UserChunkProgress> findByUserIdAndStatusIn(String userId, List<LessonStatus> statuses);
    List<UserChunkProgress> findByUserIdAndLessonIdIn(String userId, List<String> lessonIds);
    long countByUserIdAndStatus(String userId, LessonStatus status);
    boolean existsByLessonId(String lessonId);
    void deleteByLessonIdIn(List<String> lessonIds);
}
