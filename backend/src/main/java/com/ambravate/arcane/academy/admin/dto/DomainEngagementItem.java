package com.ambravate.arcane.academy.admin.dto;

public record DomainEngagementItem(
    String domainId,
    String domainName,
    String glyph,
    long totalSubChunks,
    long totalCompletions,
    long uniqueLearners
) {}
