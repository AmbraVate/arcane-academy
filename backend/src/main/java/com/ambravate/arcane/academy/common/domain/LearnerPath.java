package com.ambravate.arcane.academy.common.domain;

public enum LearnerPath {
    // ── New universal four-tier structure (pathway-agnostic) ───────────────────
    /** First tier — learn programming fundamentals. */
    APPRENTICE,
    /** Second tier — become a production engineer. */
    JUNIOR,
    /** Third tier — own systems and technical delivery. */
    SENIOR,
    /** Fourth tier — operate as a Lead/Staff engineer. */
    LEAD,

    // ── Legacy values retained until all content JSON files are migrated ───────
    // These are removed once V16 Flyway migration has run and all JSON is reseeded.
    /** @deprecated Use APPRENTICE */
    FOUNDATION,
    /** @deprecated Removed — content merged into JUNIOR */
    ADVANCED,
    /** @deprecated Use JUNIOR */
    PRACTITIONER,
    /** @deprecated Use SENIOR */
    EXPERT,
    /** @deprecated Replaced by per-tier capstone topics */
    CAPSTONE
}
