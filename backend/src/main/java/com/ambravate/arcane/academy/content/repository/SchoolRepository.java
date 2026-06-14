package com.ambravate.arcane.academy.content.repository;

import com.ambravate.arcane.academy.common.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolRepository extends JpaRepository<School, String> {
    List<School> findAllByOrderBySortOrderAsc();
}
