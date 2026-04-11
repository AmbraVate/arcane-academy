package com.ambravate.polymath.academy.repository;

import com.ambravate.polymath.academy.model.CuriosityQueueItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuriosityQueueItemRepository extends JpaRepository<CuriosityQueueItem, String> {
    List<CuriosityQueueItem> findByUserIdOrderBySavedAtDesc(String userId);
    void deleteByUserIdAndSubChunkId(String userId, String subChunkId);
}
