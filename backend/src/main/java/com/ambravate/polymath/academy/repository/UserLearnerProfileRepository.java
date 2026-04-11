package com.ambravate.polymath.academy.repository;

import com.ambravate.polymath.academy.model.UserLearnerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLearnerProfileRepository extends JpaRepository<UserLearnerProfile, String> {
    Optional<UserLearnerProfile> findByUserId(String userId);
}
