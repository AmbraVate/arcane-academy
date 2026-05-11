// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.profile.domain;

import java.time.Instant;

public record EarnedBadge(
    String id,
    String displayName,
    String glyph,
    String category,
    Instant earnedAt
) {

}
