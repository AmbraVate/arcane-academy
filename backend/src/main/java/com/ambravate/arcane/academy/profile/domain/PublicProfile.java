// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.profile.domain;

import java.time.Instant;
import java.util.List;

public record PublicProfile(
    String username,
    Instant memberSince,
    String rank,
    int totalXp,
    int streakDays,
    List<DomainEntry> domains,
    List<EarnedBadge> badges
) {

}
