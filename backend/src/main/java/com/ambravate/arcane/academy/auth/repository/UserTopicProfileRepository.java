package com.ambravate.arcane.academy.auth.repository;

import com.ambravate.arcane.academy.common.domain.UserTopicProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTopicProfileRepository extends JpaRepository<UserTopicProfile, String> {
    Optional<UserTopicProfile> findByUserIdAndTopicId(String userId, String topicId);
}
