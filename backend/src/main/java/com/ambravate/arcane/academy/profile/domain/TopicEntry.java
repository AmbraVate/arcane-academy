// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.profile.domain;

public record TopicEntry(
    String domainId,
    String name,
    String glyph,
    String accentColor,
    int xpEarned,
    int subChunksCompleted
) {

}
