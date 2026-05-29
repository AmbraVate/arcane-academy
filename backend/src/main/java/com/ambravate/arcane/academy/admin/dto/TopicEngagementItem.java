package com.ambravate.arcane.academy.admin.dto;

public record TopicEngagementItem(
    String topicId,
    String topicName,
    String glyph,
    long totalSubChunks,
    long totalCompletions,
    long uniqueLearners
) {}
