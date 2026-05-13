package com.ambravate.arcane.academy.common.repository;

import com.ambravate.arcane.academy.common.domain.CuriosityQueueItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuriosityQueueItemRepository extends JpaRepository<CuriosityQueueItem, String> {
    List<CuriosityQueueItem> findByUserIdOrderBySavedAtDesc(String userId);
    void deleteByUserIdAndSubChunkId(String userId, String subChunkId);
}
