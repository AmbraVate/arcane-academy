package com.ambravate.arcane.academy.common.events;

/**
 * Published when a user completes a sub-chunk (reaches COMPLETE status).
 * Subscribers can react without the practice module knowing about them.
 */
public record SubChunkCompletedEvent(
        String userId,
        String subChunkId,
        String chunkId,
        String topicId,
        int xpEarned
) {}
