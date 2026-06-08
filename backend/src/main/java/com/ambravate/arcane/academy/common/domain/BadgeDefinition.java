package com.ambravate.arcane.academy.common.domain;

import lombok.Getter;

/**
 * All badge definitions for the Arcane Academy platform.
 *
 * <p>Domains supported: Software Engineering, Frontend Engineering, Data Engineering.
 *
 * <p>Badge categories:
 * <ul>
 *   <li>LEARNING   — milestone completion (lessons, tiers)</li>
 *   <li>PATH       — domain pathway graduation (full tier + capstone)</li>
 *   <li>MASTERY    — review/retrieval performance</li>
 *   <li>FEYNMAN    — Feynman explanation practice</li>
 *   <li>EXPLORATION — rabbit holes and optional deep-dives</li>
 *   <li>XP         — XP threshold milestones</li>
 *   <li>STREAK     — daily activity streaks</li>
 * </ul>
 */
@Getter
public enum BadgeDefinition {

    // ── Onboarding ────────────────────────────────────────────────────────────
    ARCANE_INITIATE("Arcane Initiate", "Complete the academy orientation", "🌟", Category.LEARNING),

    // ── First steps ──────────────────────────────────────────────────────────
    FIRST_CONCEPT("First Spell Cast", "Complete your first lesson", "✨", Category.LEARNING),

    // ── Software Engineering tier badges ─────────────────────────────────────
    SE_APPRENTICE_COMPLETE("Code Initiate", "Complete all Software Engineering Apprentice modules", "⚙️", Category.LEARNING),
    SE_JUNIOR_COMPLETE("Software Craftsman", "Complete all Software Engineering Junior modules", "🔨", Category.LEARNING),
    SE_SENIOR_COMPLETE("Systems Thinker", "Complete all Software Engineering Senior modules", "🏗️", Category.LEARNING),
    SE_LEAD_COMPLETE("Engineering Lead", "Complete all Software Engineering Lead modules", "👑", Category.PATH),
    SE_APPRENTICE_CAPSTONE("First Build", "Submit your Software Engineering Apprentice capstone", "🏛️", Category.PATH),
    SE_JUNIOR_CAPSTONE("Production Ready", "Submit your Software Engineering Junior capstone", "🚀", Category.PATH),
    SE_SENIOR_CAPSTONE("Systems Architect", "Submit your Software Engineering Senior capstone", "🌐", Category.PATH),
    SE_LEAD_CAPSTONE("Engineering Champion", "Submit your Software Engineering Lead capstone", "🏆", Category.PATH),

    // ── Frontend Engineering tier badges ──────────────────────────────────────
    FE_APPRENTICE_COMPLETE("Interface Initiate", "Complete all Frontend Engineering Apprentice modules", "🖥️", Category.LEARNING),
    FE_JUNIOR_COMPLETE("Component Weaver", "Complete all Frontend Engineering Junior modules", "🧩", Category.LEARNING),
    FE_SENIOR_COMPLETE("UI Architect", "Complete all Frontend Engineering Senior modules", "✨", Category.LEARNING),
    FE_LEAD_COMPLETE("Frontend Guild Master", "Complete all Frontend Engineering Lead modules", "🌟", Category.PATH),
    FE_APPRENTICE_CAPSTONE("First Interface", "Submit your Frontend Engineering Apprentice capstone", "🖼️", Category.PATH),
    FE_JUNIOR_CAPSTONE("App Launched", "Submit your Frontend Engineering Junior capstone", "📱", Category.PATH),
    FE_SENIOR_CAPSTONE("Design Systems Author", "Submit your Frontend Engineering Senior capstone", "💎", Category.PATH),
    FE_LEAD_CAPSTONE("Frontend Archmage", "Submit your Frontend Engineering Lead capstone", "🏆", Category.PATH),

    // ── Data Engineering tier badges ──────────────────────────────────────────
    DE_APPRENTICE_COMPLETE("Data Apprentice", "Complete all Data Engineering Apprentice modules", "🗄️", Category.LEARNING),
    DE_JUNIOR_COMPLETE("Query Weaver", "Complete all Data Engineering Junior modules", "🔗", Category.LEARNING),
    DE_SENIOR_COMPLETE("Pipeline Architect", "Complete all Data Engineering Senior modules", "⚡", Category.LEARNING),
    DE_LEAD_COMPLETE("Data Guild Master", "Complete all Data Engineering Lead modules", "💠", Category.PATH),
    DE_APPRENTICE_CAPSTONE("First Pipeline", "Submit your Data Engineering Apprentice capstone", "📊", Category.PATH),
    DE_JUNIOR_CAPSTONE("Query Champion", "Submit your Data Engineering Junior capstone", "🔍", Category.PATH),
    DE_SENIOR_CAPSTONE("Data Architect", "Submit your Data Engineering Senior capstone", "🏛️", Category.PATH),
    DE_LEAD_CAPSTONE("Data Archmage", "Submit your Data Engineering Lead capstone", "🏆", Category.PATH),

    // ── Cross-domain tier completion ──────────────────────────────────────────
    // Awarded when any single domain's tier is fully completed.
    APPRENTICE_COMPLETE("Apprentice Graduate", "Graduate from any Apprentice tier", "🌱", Category.PATH),
    JUNIOR_COMPLETE("Junior Engineer", "Graduate from any Junior tier", "🔧", Category.PATH),
    SENIOR_COMPLETE("Senior Engineer", "Graduate from any Senior tier", "⚡", Category.PATH),
    LEAD_COMPLETE("Lead Engineer", "Graduate from any Lead tier", "👑", Category.PATH),

    // ── Note-taking ───────────────────────────────────────────────────────────
    FIRST_NOTE("First Inscription", "Save your first lesson note", "📝", Category.LEARNING),
    AVID_SCHOLAR("Avid Scholar", "Save 50 lesson notes", "📚", Category.LEARNING),

    // ── Review & mastery ──────────────────────────────────────────────────────
    PERFECT_REVIEW("Flawless Recall", "Score 100% on a review session", "🎯", Category.MASTERY),
    MEMORY_MASTER("Eternal Memory", "All lessons at green memory health", "🧠", Category.MASTERY),
    DIAGNOSTIC_ACE("Prodigy", "Score >80% on the entry diagnostic", "🤔", Category.MASTERY),

    // ── Feynman method ────────────────────────────────────────────────────────
    FEYNMAN_FIRST("First Teaching", "Complete your first Feynman explanation", "🎓", Category.FEYNMAN),
    FEYNMAN_MASTER("Master Teacher", "Score >80% on 10 Feynman explanations", "📝", Category.FEYNMAN),

    // ── Exploration ───────────────────────────────────────────────────────────
    RABBIT_HOLE_FIRST("Curious Mind", "Complete your first deep-dive module", "🐇", Category.EXPLORATION),

    // ── XP thresholds ─────────────────────────────────────────────────────────
    XP_100("Spark of Magic", "Earn 100 XP", "⚡", Category.XP),
    XP_500("Rising Flame", "Earn 500 XP", "🔥", Category.XP),
    XP_1000("Arcane Adept", "Earn 1,000 XP", "💎", Category.XP),
    XP_2500("Master of the Arts", "Earn 2,500 XP", "🌟", Category.XP),
    XP_5000("Legendary Wizard", "Earn 5,000 XP", "🏆", Category.XP),

    // ── Streak milestones ─────────────────────────────────────────────────────
    STREAK_3("Consistent Apprentice", "Maintain a 3-day streak", "📅", Category.STREAK),
    STREAK_7("Week of Dedication", "Maintain a 7-day streak", "🗓️", Category.STREAK),
    STREAK_30("Unyielding Will", "Maintain a 30-day streak", "💪", Category.STREAK);

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
        LEARNING, PATH, MASTERY, FEYNMAN, EXPLORATION, XP, STREAK
    }
}
