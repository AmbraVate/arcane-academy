package com.ambravate.arcane.academy.auth.repository;

import com.ambravate.arcane.academy.common.domain.UserTrackProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTrackProfileRepository extends JpaRepository<UserTrackProfile, String> {
    Optional<UserTrackProfile> findByUserIdAndTrackId(String userId, String trackId);
}
