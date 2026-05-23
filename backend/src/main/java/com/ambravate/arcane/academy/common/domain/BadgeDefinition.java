package com.ambravate.arcane.academy.common.domain;

import lombok.Getter;

@Getter
public enum BadgeDefinition {

    // Learning milestones
    FIRST_CONCEPT("First Spell Cast", "Complete your first sub-chunk", "\u2728", Category.LEARNING),

    // Java Foundation milestones \u2014 one badge per chunk (java-fnd-1 through java-fnd-8)
    JAVA_FND_1_COMPLETE("Rune of Syntax", "Master java-fnd-1: Syntax Basics \u2014 variables, primitives, operators", "\uD83D\uDCDD", Category.LEARNING),
    JAVA_FND_2_COMPLETE("Rune of Control", "Master java-fnd-2: Control Flow \u2014 conditionals, loops, switch-expressions", "\uD83D\uDD04", Category.LEARNING),
    JAVA_FND_3_COMPLETE("Rune of Objects", "Master java-fnd-3: Object-Oriented Programming \u2014 classes, inheritance, polymorphism", "\uD83D\uDCD6", Category.LEARNING),
    JAVA_FND_4_COMPLETE("Rune of Memory", "Master java-fnd-4: Memory Management \u2014 stack vs heap, garbage collection", "\uD83E\uDDE0", Category.LEARNING),
    JAVA_FND_5_COMPLETE("Rune of Collections", "Master java-fnd-5: Java Collections Framework \u2014 List, Set, Map", "\uD83D\uDCDA", Category.LEARNING),
    JAVA_FND_6_COMPLETE("Rune of Exceptions", "Master java-fnd-6: Exception Handling \u2014 try/catch, custom exceptions", "\u26A0\uFE0F", Category.LEARNING),
    JAVA_FND_7_COMPLETE("Rune of Streams", "Master java-fnd-7: Input/Output \u2014 console I/O, file reading and writing", "\uD83D\uDCBE", Category.LEARNING),
    JAVA_FND_8_COMPLETE("Rune of Generics", "Master java-fnd-8: Generics Basics \u2014 type parameters, bounded types", "\uD83D\uDD37", Category.LEARNING),
    JAVA_FOUNDATION_COMPLETE("Foundation Archmage", "Complete all 8 Java Foundation chunks", "\uD83C\uDF1F", Category.LEARNING),

    JAVA_CAPSTONE_COMPLETE("Grand Champion", "Conquer the Grand Tournament \u2014 Java capstone project", "\uD83C\uDFC6", Category.LEARNING),
    TAILWIND_CAPSTONE_COMPLETE("Agency Archmage", "Deliver The Agency Commission \u2014 Tailwind capstone project", "\uD83C\uDF10", Category.LEARNING),

    // SQL track milestones \u2014 one per tier, plus a track-master capstone
    SQL_QUERY_INITIATE("Query Initiate", "Master sql-a: Tables & SELECT", "\uD83E\uDE84", Category.LEARNING),
    SQL_JOIN_WEAVER("Join Weaver", "Master sql-d: JOINs across tables", "\uD83D\uDD17", Category.LEARNING),
    SQL_QUERY_OPTIMISER("Query Optimiser", "Master sql-g: Indexes & EXPLAIN \u2014 read the engine's plan", "\u2699\uFE0F", Category.LEARNING),
    SQL_TRACK_MASTER("Cipher's Heir", "Complete every chunk in the SQL track \u2014 Foundation through Expert", "\uD83D\uDDC3\uFE0F", Category.LEARNING),

    // React track milestones \u2014 mirrors the Tailwind minimalism (Foundation/Practitioner + capstone)
    REACT_HOOK_INITIATE("Hook Initiate", "Master rx-a: Components, props, and your first hook", "\u269B\uFE0F", Category.LEARNING),
    REACT_STATE_WEAVER("State Weaver", "Master rx-b: useEffect, custom hooks, and lifting state", "\uD83E\uDDF6", Category.LEARNING),
    REACT_CAPSTONE_COMPLETE("Guild Architect", "Deliver The Guild Portal \u2014 React capstone project", "\uD83C\uDFDB\uFE0F", Category.LEARNING),

    // Non-technical track Foundation milestones \u2014 one Foundation badge per new track
    PSY_FOUNDATION_COMPLETE("Mind-Walker", "Master the Foundation tier of Psychology \u2014 psy-a, psy-b, psy-c", "\uD83E\uDDE0", Category.LEARNING),
    GEN_FOUNDATION_COMPLETE("Lineage Scholar", "Master the Foundation tier of Genealogy \u2014 gen-a, gen-b, gen-c", "\uD83C\uDF33", Category.LEARNING),
    SCI_FOUNDATION_COMPLETE("Natural Philosopher", "Master the Foundation tier of Natural Sciences \u2014 sci-a, sci-b, sci-c", "\uD83D\uDD2C", Category.LEARNING),

    // \u2500\u2500 Note-taking \u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500
    FIRST_NOTE("First Inscription", "Save your first lesson note", "\uD83D\uDCDD", Category.LEARNING),
    AVID_SCHOLAR("Avid Scholar", "Save 50 lesson notes", "\uD83D\uDCDA", Category.LEARNING),

    // Review & mastery
    PERFECT_REVIEW("Flawless Recall", "Score 100% on a review session", "\uD83C\uDFAF", Category.MASTERY),
    MEMORY_MASTER("Eternal Memory", "All chunks at green memory health", "\uD83E\uDDE0", Category.MASTERY),
    DIAGNOSTIC_ACE("Prodigy", "Score >80% on entry diagnostic", "\uD83E\uDDD0", Category.MASTERY),

    // Feynman
    FEYNMAN_FIRST("First Teaching", "Complete your first Feynman explanation", "\uD83C\uDF93", Category.FEYNMAN),
    FEYNMAN_MASTER("Master Teacher", "Score >80% on 10 Feynman explanations", "\uD83D\uDCDD", Category.FEYNMAN),

    // \u2500\u2500 Tier completion (new four-tier structure) \u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500
    APPRENTICE_COMPLETE("Apprentice Graduate", "Complete all topics in the Apprentice tier", "\uD83C\uDF31", Category.PATH),
    JUNIOR_COMPLETE("Junior Engineer", "Complete all topics in the Junior tier", "\uD83D\uDD28", Category.PATH),
    SENIOR_COMPLETE("Senior Engineer", "Complete all topics in the Senior tier", "\u26A1", Category.PATH),
    LEAD_COMPLETE("Lead Engineer", "Complete all topics in the Lead tier", "\uD83D\uDC51", Category.PATH),

    // \u2500\u2500 Capstone completion (one per tier) \u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500
    APPRENTICE_CAPSTONE("First Creation", "Submit your Apprentice capstone project", "\uD83C\uDFD7\uFE0F", Category.PATH),
    JUNIOR_CAPSTONE("Production Ready", "Submit your Junior capstone project", "\uD83D\uDE80", Category.PATH),
    SENIOR_CAPSTONE("Systems Architect", "Submit your Senior capstone project", "\uD83C\uDF10", Category.PATH),
    LEAD_CAPSTONE("Engineering Leader", "Submit your Lead capstone project", "\uD83C\uDFC6", Category.PATH),

    // \u2500\u2500 Legacy path badges (kept for historical awarded badges) \u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500
    /** @deprecated Use APPRENTICE_COMPLETE / JUNIOR_COMPLETE */
    PATH_PRACTITIONER("Practitioner Ascension", "Advance to Practitioner path", "\u2B50", Category.PATH),
    /** @deprecated Use SENIOR_COMPLETE */
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

}
