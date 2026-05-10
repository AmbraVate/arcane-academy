package com.ambravate.polymath.academy.repository;

import com.ambravate.polymath.academy.model.LearnerPath;
import com.ambravate.polymath.academy.model.Question;
import com.ambravate.polymath.academy.model.QuestionTier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, String> {
    List<Question> findBySubChunkId(String subChunkId);
    List<Question> findBySubChunkIdAndTier(String subChunkId, QuestionTier tier);
    List<Question> findBySubChunkIdIn(List<String> subChunkIds);
    List<Question> findBySubChunkIdInAndMinPathLessThanEqual(List<String> subChunkIds, LearnerPath path);
    void deleteBySubChunkIdIn(List<String> subChunkIds);
}
