package com.ambravate.polymath.academy.repository;

import com.ambravate.polymath.academy.model.ReviewSession;
import com.ambravate.polymath.academy.model.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewSessionRepository extends JpaRepository<ReviewSession, String> {
    List<ReviewSession> findByUserIdOrderByStartedAtDesc(String userId);
    List<ReviewSession> findByUserIdAndSessionType(String userId, SessionType type);
    long countByUserIdAndCompletedAtIsNotNull(String userId);
}
