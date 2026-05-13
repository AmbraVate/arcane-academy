package com.ambravate.arcane.academy.content.service;

import com.ambravate.arcane.academy.content.domain.CuriosityQueueItem;
import com.ambravate.arcane.academy.content.repository.CuriosityQueueItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuriosityQueueService {

    private final CuriosityQueueItemRepository repository;

    public List<CuriosityQueueItem> getQueue(String userId) {
        return repository.findByUserIdOrderBySavedAtDesc(userId);
    }

    @Transactional
    public void saveForLater(String userId, String subChunkId) {
        if (repository.findByUserIdOrderBySavedAtDesc(userId).stream()
                .noneMatch(i -> i.getSubChunkId().equals(subChunkId))) {
            repository.save(CuriosityQueueItem.builder()
                    .userId(userId).subChunkId(subChunkId).build());
        }
    }

    @Transactional
    public void removeFromQueue(String userId, String subChunkId) {
        repository.deleteByUserIdAndSubChunkId(userId, subChunkId);
    }
}
