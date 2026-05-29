package com.ambravate.arcane.academy.common.domain;

public record ModuleHealth(
    String moduleId,
    String title,
    String glyph,
    String status,
    double memoryStrength,
    String healthColor,
    int totalLessons,
    int completedLessons,
    String tier
) { }
