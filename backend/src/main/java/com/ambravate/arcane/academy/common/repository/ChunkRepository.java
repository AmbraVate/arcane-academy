package com.ambravate.arcane.academy.common.repository;

import com.ambravate.arcane.academy.common.domain.Chunk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ChunkRepository extends JpaRepository<Chunk, String> {
    List<Chunk> findAllByOrderBySortOrderAsc();
    List<Chunk> findByTopicIdOrderBySortOrderAsc(String topicId);
}
