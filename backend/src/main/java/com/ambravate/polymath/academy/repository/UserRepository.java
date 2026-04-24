package com.ambravate.polymath.academy.repository;

import com.ambravate.polymath.academy.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
           "(:search IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<User> findBySearchTerm(@Param("search") String search, Pageable pageable);
}
