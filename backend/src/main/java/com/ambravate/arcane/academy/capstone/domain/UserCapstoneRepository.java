package com.ambravate.arcane.academy.capstone.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCapstoneRepository extends JpaRepository<UserCapstone, String> {

    List<UserCapstone> findByUserIdOrderByCreatedAtDesc(String userId);

    Optional<UserCapstone> findByIdAndUserId(String id, String userId);

    /** For admin: all capstones with optional filter by chunkId. */
    Page<UserCapstone> findByChunkIdContaining(String chunkId, Pageable pageable);
}
