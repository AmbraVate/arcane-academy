package com.ambravate.arcane.academy.content.repository;

import com.ambravate.arcane.academy.common.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, String> {

    List<Topic> findByModuleIdOrderBySortOrderAsc(String moduleId);
}
