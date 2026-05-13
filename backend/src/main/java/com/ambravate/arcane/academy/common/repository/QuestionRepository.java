package com.ambravate.arcane.academy.common.repository;

import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.common.domain.QuestionTier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, String> {
    List<Question> findBySubChunkId(String subChunkId);
    List<Question> findBySubChunkIdAndTier(String subChunkId, QuestionTier tier);
    List<Question> findBySubChunkIdIn(List<String> subChunkIds);
    List<Question> findBySubChunkIdInAndMinPathLessThanEqual(List<String> subChunkIds, LearnerPath path);
    void deleteBySubChunkIdIn(List<String> subChunkIds);
}
