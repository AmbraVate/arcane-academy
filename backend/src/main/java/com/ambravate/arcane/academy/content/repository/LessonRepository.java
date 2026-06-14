package com.ambravate.arcane.academy.content.repository;

import com.ambravate.arcane.academy.common.domain.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, String> {
    List<Lesson> findByModuleIdOrderBySortOrderAsc(String moduleId);
    List<Lesson> findByModuleIdIn(List<String> moduleIds);
    long countByTopicId(String topicId);
}
