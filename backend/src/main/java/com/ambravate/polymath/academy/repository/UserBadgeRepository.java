package com.ambravate.polymath.academy.repository;

import com.ambravate.polymath.academy.model.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadge, String> {
    List<UserBadge> findByUserId(String userId);
    boolean existsByUserIdAndBadgeId(String userId, String badgeId);
}
