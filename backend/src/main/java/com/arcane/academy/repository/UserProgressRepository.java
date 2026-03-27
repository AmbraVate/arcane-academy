package com.arcane.academy.repository;

import com.arcane.academy.model.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserProgressRepository extends JpaRepository<UserProgress, String> {
    List<UserProgress> findByUserId(String userId);
    Optional<UserProgress> findByUserIdAndItemIdAndItemType(
            String userId, String itemId, UserProgress.ItemType itemType);
    boolean existsByUserIdAndItemIdAndItemType(
            String userId, String itemId, UserProgress.ItemType itemType);
}
