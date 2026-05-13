package com.ambravate.arcane.academy.common.repository;

import com.ambravate.arcane.academy.common.domain.AuthProvider;
import com.ambravate.arcane.academy.common.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByAuthProviderAndProviderId(AuthProvider provider, String providerId);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    /** Used by leaderboards — only opted-in users appear in rankings. */
    List<User> findByPublicProfileEnabledTrue();

    // Admin queries
    long countByLastLoginAtAfter(Instant after);
    List<User> findTop10ByOrderByCreatedAtDesc();

    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.email)    LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<User> findBySearchTerm(@Param("search") String search, Pageable pageable);

    /** Backfill: set role = USER for any legacy rows that have NULL in the role column. */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.role = com.ambravate.arcane.academy.common.domain.UserRole.USER WHERE u.role IS NULL")
    int backfillNullRoles();
}
