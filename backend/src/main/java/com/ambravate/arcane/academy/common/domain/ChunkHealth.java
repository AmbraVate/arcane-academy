// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.common.domain;

public record ChunkHealth(
    String chunkId,
    String title,
    String glyph,
    String status,
    double memoryStrength,
    String healthColor,
    int totalSubChunks,
    int completedSubChunks,
    String tier
) { }
