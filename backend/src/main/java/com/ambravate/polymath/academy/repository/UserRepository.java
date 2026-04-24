package com.ambravate.polymath.academy.repository;

import com.ambravate.polymath.academy.model.User;
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
    Optional<User> findByAuthProviderAndProviderId(User.AuthProvider provider, String providerId);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

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
    @Query("UPDATE User u SET u.role = com.ambravate.polymath.academy.model.User.UserRole.USER WHERE u.role IS NULL")
    int backfillNullRoles();
}
