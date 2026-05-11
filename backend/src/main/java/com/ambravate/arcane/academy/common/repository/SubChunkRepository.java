package com.ambravate.arcane.academy.common.repository;

import com.ambravate.arcane.academy.common.domain.SubChunk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubChunkRepository extends JpaRepository<SubChunk, String> {
    List<SubChunk> findByChunkIdOrderBySortOrderAsc(String chunkId);
    List<SubChunk> findByChunkIdIn(List<String> chunkIds);
}
