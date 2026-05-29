package com.ambravate.arcane.academy.content.repository;

import com.ambravate.arcane.academy.common.domain.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DomainRepository extends JpaRepository<Domain, String> {
    List<Domain> findAllByOrderBySortOrderAsc();
    List<Domain> findByActiveTrue();
}
