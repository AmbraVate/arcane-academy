package com.ambravate.polymath.academy.repository;

import com.ambravate.polymath.academy.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, String> {
    List<Topic> findAllByOrderBySortOrderAsc();
    List<Topic> findByActiveTrue();
}
