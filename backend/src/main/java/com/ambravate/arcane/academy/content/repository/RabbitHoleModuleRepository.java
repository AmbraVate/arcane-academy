package com.ambravate.arcane.academy.content.repository;

import com.ambravate.arcane.academy.content.domain.RabbitHoleModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RabbitHoleModuleRepository extends JpaRepository<RabbitHoleModule, String> {
    List<RabbitHoleModule> findByModuleIdOrderBySortOrderAsc(String moduleId);
    void deleteByModuleId(String moduleId);
}
