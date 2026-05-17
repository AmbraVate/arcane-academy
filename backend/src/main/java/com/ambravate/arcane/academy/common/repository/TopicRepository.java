package com.ambravate.arcane.academy.common.repository;

import com.ambravate.arcane.academy.common.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, String> {
    List<Topic> findAllByOrderBySortOrderAsc();
    List<Topic> findByActiveTrue();
}
