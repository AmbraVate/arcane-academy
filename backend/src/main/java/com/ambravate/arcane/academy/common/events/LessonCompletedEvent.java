package com.ambravate.arcane.academy.common.events;

/**
 * Published when a user completes a lesson (reaches COMPLETE status).
 * Subscribers can react without the practice module knowing about them.
 */
public record LessonCompletedEvent(
        String userId,
        String lessonId,
        String moduleId,
        String domainId,
        int xpEarned
) {}
