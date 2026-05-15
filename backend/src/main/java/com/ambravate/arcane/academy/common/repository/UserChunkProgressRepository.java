package com.ambravate.arcane.academy.common.repository;

import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserChunkProgressRepository extends JpaRepository<UserChunkProgress, String> {
    List<UserChunkProgress> findByUserId(String userId);
    Optional<UserChunkProgress> findByUserIdAndSubChunkId(String userId, String subChunkId);
    boolean existsByUserIdAndSubChunkId(String userId, String subChunkId);
    List<UserChunkProgress> findByUserIdAndNextReviewAtBefore(String userId, Instant now);
    List<UserChunkProgress> findByUserIdAndStatusIn(String userId, List<SubChunkStatus> statuses);
    List<UserChunkProgress> findByUserIdAndSubChunkIdIn(String userId, List<String> subChunkIds);
    long countByUserIdAndStatus(String userId, SubChunkStatus status);
    void deleteBySubChunkIdIn(List<String> subChunkIds);
}
