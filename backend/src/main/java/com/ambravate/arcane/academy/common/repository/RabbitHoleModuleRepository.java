package com.ambravate.arcane.academy.common.repository;

import com.ambravate.arcane.academy.common.domain.RabbitHoleModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RabbitHoleModuleRepository extends JpaRepository<RabbitHoleModule, String> {
    List<RabbitHoleModule> findByChunkIdOrderBySortOrderAsc(String chunkId);
    void deleteByChunkId(String chunkId);
}
