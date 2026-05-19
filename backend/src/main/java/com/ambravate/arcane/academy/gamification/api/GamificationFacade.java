package com.ambravate.arcane.academy.gamification.api;

import com.ambravate.arcane.academy.common.domain.ReviewSession;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.dto.BadgeDto;

import java.util.List;

/**
 * Public API surface of the gamification module.
 * Other modules must only depend on this interface, never on gamification.service.* directly.
 *
 * <p>Badge evaluation accepts pre-fetched {@link UserChunkProgress} and {@link ReviewSession}
 * lists so that the gamification module does not need to query practice repositories directly
 * (avoiding a module-level cycle between practice and gamification).
 */
public interface GamificationFacade {

    /**
     * Evaluate all badge conditions for the user and award any newly earned badges.
     *
     * @param userId       the user to evaluate
     * @param allProgress  all UserChunkProgress records for this user (fetched by the caller)
     * @param sessions     all ReviewSession records for this user (fetched by the caller)
     */
    List<BadgeDto> evaluateAndAwardBadges(String userId,
                                          List<UserChunkProgress> allProgress,
                                          List<ReviewSession> sessions);

    /** Returns true if the user has not yet engaged today but had a streak yesterday (at risk of losing it). */
    boolean isStreakAtRisk(String userId);

    /** Returns the number of badges the user has earned. Used by leaderboard. */
    int getBadgeCount(String userId);

    /** Returns all badges earned by the user, with full display metadata. Used by public profile. */
    List<BadgeDto> getEarnedBadges(String userId);
}
