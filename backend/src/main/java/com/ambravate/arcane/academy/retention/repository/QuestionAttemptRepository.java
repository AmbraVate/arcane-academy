package com.ambravate.arcane.academy.retention.repository;

import com.ambravate.arcane.academy.retention.domain.QuestionAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionAttemptRepository extends JpaRepository<QuestionAttempt, String> {
    List<QuestionAttempt> findByUserIdAndLessonIdOrderByAttemptedAtAsc(String userId, String lessonId);
}
