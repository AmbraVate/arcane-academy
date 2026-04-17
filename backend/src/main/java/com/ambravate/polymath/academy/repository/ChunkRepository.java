package com.ambravate.polymath.academy.repository;

import com.ambravate.polymath.academy.model.Chunk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChunkRepository extends JpaRepository<Chunk, String> {
    List<Chunk> findAllByOrderBySortOrderAsc();
    List<Chunk> findByTopicIdOrderBySortOrderAsc(String topicId);
}
