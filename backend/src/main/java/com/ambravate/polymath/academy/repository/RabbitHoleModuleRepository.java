package com.ambravate.polymath.academy.repository;

import com.ambravate.polymath.academy.model.RabbitHoleModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RabbitHoleModuleRepository extends JpaRepository<RabbitHoleModule, String> {
    List<RabbitHoleModule> findByChunkIdOrderBySortOrderAsc(String chunkId);
    void deleteByChunkId(String chunkId);
}
