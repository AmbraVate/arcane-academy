package com.ambravate.arcane.academy.gamification.api;

import com.ambravate.arcane.academy.common.dto.BadgeDto;
import java.util.List;

/**
 * Public API surface of the gamification module.
 * Other modules must only depend on this interface, never on gamification.service.* directly.
 */
public interface GamificationFacade {

    /** Evaluate all badge conditions for the user and award any newly earned badges. */
    List<BadgeDto> evaluateAndAwardBadges(String userId);

    /** Returns true if the user has not yet engaged today but had a streak yesterday (at risk of losing it). */
    boolean isStreakAtRisk(String userId);
}
