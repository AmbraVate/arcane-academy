package com.ambravate.arcane.academy.common.events;

/**
 * Published when a user completes any engagement action that should trigger a streak update.
 * Decouples practice completion from the gamification streak logic.
 */
public record UserEngagedEvent(String userId) {}
