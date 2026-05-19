package com.ambravate.arcane.academy.gamification.repository;

import com.ambravate.arcane.academy.common.domain.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, String> {
    List<UserBadge> findByUserId(String userId);
    boolean existsByUserIdAndBadgeId(String userId, String badgeId);
}
