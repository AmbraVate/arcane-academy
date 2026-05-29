package com.ambravate.arcane.academy.content.repository;

import com.ambravate.arcane.academy.content.domain.CuriosityQueueItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuriosityQueueItemRepository extends JpaRepository<CuriosityQueueItem, String> {
    List<CuriosityQueueItem> findByUserIdOrderBySavedAtDesc(String userId);
    void deleteByUserIdAndLessonId(String userId, String lessonId);
}
