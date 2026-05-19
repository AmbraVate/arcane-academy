package com.ambravate.arcane.academy.auth.repository;

import com.ambravate.arcane.academy.common.domain.UserLearnerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLearnerProfileRepository extends JpaRepository<UserLearnerProfile, String> {
    Optional<UserLearnerProfile> findByUserId(String userId);
}
