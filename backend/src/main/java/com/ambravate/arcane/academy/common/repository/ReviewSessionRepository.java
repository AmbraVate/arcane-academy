package com.ambravate.arcane.academy.common.repository;

import com.ambravate.arcane.academy.common.domain.ReviewSession;
import com.ambravate.arcane.academy.common.domain.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewSessionRepository extends JpaRepository<ReviewSession, String> {
    List<ReviewSession> findByUserIdOrderByStartedAtDesc(String userId);
    List<ReviewSession> findByUserIdAndSessionType(String userId, SessionType type);
    long countByUserIdAndCompletedAtIsNotNull(String userId);
}
