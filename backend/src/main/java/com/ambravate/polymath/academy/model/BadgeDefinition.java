package com.ambravate.polymath.academy.model;

import lombok.Getter;

@Getter
public enum BadgeDefinition {

    // Learning milestones
    FIRST_CONCEPT("First Spell Cast", "Complete your first sub-chunk", "\u2728", Category.LEARNING),
    CHUNK_A_MASTERED("Rune of Variables", "Master Chunk A: Variables & Data Types", "\uD83D\uDD2E", Category.LEARNING),
    CHUNK_B_MASTERED("Rune of Control", "Master Chunk B: Operators & Control Flow", "\uD83D\uDCDC", Category.LEARNING),
    CHUNK_C_MASTERED("Rune of Loops", "Master Chunk C: Loops", "\uD83D\uDD04", Category.LEARNING),
    CHUNK_D_MASTERED("Rune of Methods", "Master Chunk D: Methods", "\uD83D\uDDDD", Category.LEARNING),
    CHUNK_E_MASTERED("Rune of Collections", "Master Chunk E: Arrays & Collections", "\uD83D\uDCDA", Category.LEARNING),
    CHUNK_F_MASTERED("Rune of Objects", "Master Chunk F: Classes & Objects", "\uD83D\uDCD6", Category.LEARNING),
    CHUNK_G_MASTERED("Rune of Encapsulation", "Master Chunk G: Encapsulation", "\uD83D\uDD12", Category.LEARNING),
    CHUNK_H_MASTERED("Rune of Inheritance", "Master Chunk H: Inheritance", "\uD83C\uDF33", Category.LEARNING),
    CHUNK_I_MASTERED("Rune of Polymorphism", "Master Chunk I: Polymorphism & Abstraction", "\uD83C\uDF00", Category.LEARNING),
    CHUNK_J_MASTERED("Rune of Exceptions", "Master Chunk J: Exception Handling", "\u26A0", Category.LEARNING),
    CHUNK_K_MASTERED("Rune of APIs", "Master Chunk K: Common APIs & Utils", "\uD83D\uDEE0", Category.LEARNING),
    ALL_CHUNKS_COMPLETE("Grand Archmage", "Complete all chunks A\u2013K", "\uD83D\uDC51", Category.LEARNING),
    JAVA_CAPSTONE_COMPLETE("Grand Champion", "Conquer the Grand Tournament \u2014 Java capstone project", "\uD83C\uDFC6", Category.LEARNING),
    TAILWIND_CAPSTONE_COMPLETE("Agency Archmage", "Deliver The Agency Commission \u2014 Tailwind capstone project", "\uD83C\uDF10", Category.LEARNING),

    // SQL track milestones \u2014 one per tier, plus a track-master capstone
    SQL_QUERY_INITIATE("Query Initiate", "Master sql-a: Tables & SELECT", "\uD83E\uDE84", Category.LEARNING),
    SQL_JOIN_WEAVER("Join Weaver", "Master sql-d: JOINs across tables", "\uD83D\uDD17", Category.LEARNING),
    SQL_QUERY_OPTIMISER("Query Optimiser", "Master sql-g: Indexes & EXPLAIN \u2014 read the engine's plan", "\u2699\uFE0F", Category.LEARNING),
    SQL_TRACK_MASTER("Cipher's Heir", "Complete every chunk in the SQL track \u2014 Foundation through Expert", "\uD83D\uDDC3\uFE0F", Category.LEARNING),

    // Review & mastery
    PERFECT_REVIEW("Flawless Recall", "Score 100% on a review session", "\uD83C\uDFAF", Category.MASTERY),
    MEMORY_MASTER("Eternal Memory", "All chunks at green memory health", "\uD83E\uDDE0", Category.MASTERY),
    DIAGNOSTIC_ACE("Prodigy", "Score >80% on entry diagnostic", "\uD83E\uDDD0", Category.MASTERY),

    // Feynman
    FEYNMAN_FIRST("First Teaching", "Complete your first Feynman explanation", "\uD83C\uDF93", Category.FEYNMAN),
    FEYNMAN_MASTER("Master Teacher", "Score >80% on 10 Feynman explanations", "\uD83D\uDCDD", Category.FEYNMAN),

    // Path advancement
    PATH_PRACTITIONER("Practitioner Ascension", "Advance to Practitioner path", "\u2B50", Category.PATH),
    PATH_EXPERT("Expert Ascension", "Advance to Expert path", "\uD83C\uDF1F", Category.PATH),

    // Rabbit holes
    RABBIT_HOLE_FIRST("Curious Mind", "Complete your first deep-dive module", "\uD83D\uDC07", Category.EXPLORATION),

    // XP thresholds
    XP_100("Spark of Magic", "Earn 100 XP", "\u26A1", Category.XP),
    XP_500("Rising Flame", "Earn 500 XP", "\uD83D\uDD25", Category.XP),
    XP_1000("Arcane Adept", "Earn 1,000 XP", "\uD83D\uDC8E", Category.XP),
    XP_2500("Master of the Arts", "Earn 2,500 XP", "\uD83C\uDF1F", Category.XP),
    XP_5000("Legendary Wizard", "Earn 5,000 XP", "\uD83C\uDFC6", Category.XP),

    // Streak milestones
    STREAK_3("Consistent Apprentice", "Maintain a 3-day streak", "\uD83D\uDCC5", Category.STREAK),
    STREAK_7("Week of Dedication", "Maintain a 7-day streak", "\uD83D\uDDD3", Category.STREAK),
    STREAK_30("Unyielding Will", "Maintain a 30-day streak", "\uD83D\uDCAA", Category.STREAK);

    private final String displayName;
    private final String description;
    private final String glyph;
    private final Category category;

    BadgeDefinition(String displayName, String description, String glyph, Category category) {
        this.displayName = displayName;
        this.description = description;
        this.glyph = glyph;
        this.category = category;
    }

    public enum Category {
        LEARNING, MASTERY, FEYNMAN, PATH, EXPLORATION, XP, STREAK
    }
}
