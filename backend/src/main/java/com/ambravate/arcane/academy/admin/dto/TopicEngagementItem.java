package com.ambravate.arcane.academy.admin.dto;

public record TopicEngagementItem(
    String domainId,
    String topicName,
    String glyph,
    long totalSubChunks,
    long totalCompletions,
    long uniqueLearners
) {}
