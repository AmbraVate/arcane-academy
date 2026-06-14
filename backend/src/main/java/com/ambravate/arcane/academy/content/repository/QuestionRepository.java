package com.ambravate.arcane.academy.content.repository;

import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.common.domain.QuestionTier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
    List<Question> findByLessonId(String lessonId);
    List<Question> findByLessonIdAndTier(String lessonId, QuestionTier tier);
    List<Question> findByLessonIdIn(List<String> lessonIds);
    List<Question> findByLessonIdInAndMinPathLessThanEqual(List<String> lessonIds, LearnerPath path);
    void deleteByLessonIdIn(List<String> lessonIds);
}
