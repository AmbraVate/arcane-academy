package com.ambravate.polymath.academy.repository;

import com.ambravate.polymath.academy.model.UserTopicProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTopicProfileRepository extends JpaRepository<UserTopicProfile, String> {
    Optional<UserTopicProfile> findByUserIdAndTopicId(String userId, String topicId);
}
