package com.ambravate.arcane.academy.common.domain;

import lombok.Getter;

@Getter
public enum BadgeDefinition {

    // Onboarding
    ARCANE_INITIATE("Arcane Initiate", "Complete the academy orientation", "\ud83c\udf1f", Category.LEARNING),

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

    // React track milestones \u2014 legacy (kept so existing awarded badges are not lost)
    /** @deprecated Use REACT_APPRENTICE_COMPLETE */
    REACT_HOOK_INITIATE("Hook Initiate", "Master rx-a: Components, props, and your first hook", "\u269B\uFE0F", Category.LEARNING),
    /** @deprecated Use REACT_JUNIOR_COMPLETE */
    REACT_STATE_WEAVER("State Weaver", "Master rx-b: useEffect, custom hooks, and lifting state", "\uD83E\uDDF6", Category.LEARNING),
    /** @deprecated Use REACT_LEAD_CAPSTONE */
    REACT_CAPSTONE_COMPLETE("Guild Architect", "Deliver The Guild Portal \u2014 React capstone project", "\uD83C\uDFDB\uFE0F", Category.LEARNING),

    // \u2500\u2500 React tier badges \u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500
    REACT_APPRENTICE_COMPLETE("Interface Apprentice", "Complete all Apprentice React topics", "\u269B\uFE0F", Category.LEARNING),
    REACT_JUNIOR_COMPLETE("Hook Weaver", "Complete all Junior React topics", "\uD83E\uDDF9", Category.LEARNING),
    REACT_SENIOR_COMPLETE("State Archmage", "Complete all Senior React topics", "\uD83D\uDD2E", Category.LEARNING),
    REACT_LEAD_COMPLETE("Guild Architect", "Complete all Lead React topics", "\uD83C\uDFDB\uFE0F", Category.PATH),
    REACT_APPRENTICE_CAPSTONE("First Component", "Submit your React Apprentice capstone", "\u2728", Category.PATH),
    REACT_JUNIOR_CAPSTONE("App Deployed", "Submit your React Junior capstone", "\uD83D\uDE80", Category.PATH),
    REACT_SENIOR_CAPSTONE("State Master", "Submit your React Senior capstone", "\uD83D\uDC8E", Category.PATH),
    REACT_LEAD_CAPSTONE("Portal Architect", "Submit your React Lead capstone", "\uD83C\uDFC6", Category.PATH),

    // \u2500\u2500 Tailwind tier badges \u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500
    TW_APPRENTICE_COMPLETE("Utility Apprentice", "Complete all Apprentice Tailwind topics", "\uD83C\uDFA8", Category.LEARNING),
    TW_JUNIOR_COMPLETE("Layout Weaver", "Complete all Junior Tailwind topics", "\u270F\uFE0F", Category.LEARNING),
    TW_SENIOR_COMPLETE("Design Systems Architect", "Complete all Senior Tailwind topics", "\uD83D\uDDA5\uFE0F", Category.LEARNING),
    TW_LEAD_COMPLETE("Agency Archmage", "Complete all Lead Tailwind topics", "\uD83C\uDF10", Category.PATH),
    TW_APPRENTICE_CAPSTONE("First Interface", "Submit your Tailwind Apprentice capstone", "\uD83C\uDFD7\uFE0F", Category.PATH),
    TW_JUNIOR_CAPSTONE("Layout Mastered", "Submit your Tailwind Junior capstone", "\uD83D\uDCCF", Category.PATH),
    TW_SENIOR_CAPSTONE("Systems Designer", "Submit your Tailwind Senior capstone", "\uD83C\uDFAF", Category.PATH),
    TW_LEAD_CAPSTONE("Commission Complete", "Submit your Tailwind Lead capstone", "\uD83C\uDFC6", Category.PATH),

    // Non-technical track Foundation milestones (legacy \u2014 superseded by tier badges below)
    PSY_FOUNDATION_COMPLETE("Mind-Walker", "Complete the Apprentice tier of Psychology", "\uD83E\uDDE0", Category.LEARNING),
    GEN_FOUNDATION_COMPLETE("Lineage Scholar", "Complete the Apprentice tier of Genealogy", "\uD83C\uDF33", Category.LEARNING),
    SCI_FOUNDATION_COMPLETE("Natural Philosopher", "Complete the Apprentice tier of Natural Sciences", "\uD83D\uDD2C", Category.LEARNING),

    // \u2500\u2500 Psychology tier badges \u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500
    PSY_APPRENTICE_COMPLETE("Psychology Foundations", "Complete all Apprentice Psychology topics", "\uD83E\uDDE0", Category.LEARNING),
    PSY_JUNIOR_COMPLETE("Applied Psychologist", "Complete all Junior Psychology topics", "\uD83D\uDD2C", Category.LEARNING),
    PSY_SENIOR_COMPLETE("Advanced Psychologist", "Complete all Senior Psychology topics", "\uD83D\uDCCA", Category.LEARNING),
    PSY_LEAD_COMPLETE("Critical Psychologist", "Complete all Lead Psychology topics", "\uD83C\uDF93", Category.PATH),
    PSY_APPRENTICE_CAPSTONE("First Case Study", "Submit your Psychology Apprentice capstone", "\uD83D\uDCCB", Category.PATH),
    PSY_JUNIOR_CAPSTONE("Applied Report", "Submit your Psychology Junior capstone", "\uD83D\uDCDD", Category.PATH),
    PSY_SENIOR_CAPSTONE("Research Proposal", "Submit your Psychology Senior capstone", "\uD83D\uDD2D", Category.PATH),
    PSY_LEAD_CAPSTONE("Dissertation Scholar", "Submit your Psychology Lead capstone", "\uD83C\uDFC6", Category.PATH),

    // \u2500\u2500 Genealogy tier badges \u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500
    GEN_APPRENTICE_COMPLETE("Record Keeper", "Complete all Apprentice Genealogy topics", "\uD83D\uDCDC", Category.LEARNING),
    GEN_JUNIOR_COMPLETE("Archive Researcher", "Complete all Junior Genealogy topics", "\uD83D\uDDC2\uFE0F", Category.LEARNING),
    GEN_SENIOR_COMPLETE("Evidence Analyst", "Complete all Senior Genealogy topics", "\uD83D\uDD0D", Category.LEARNING),
    GEN_LEAD_COMPLETE("Professional Genealogist", "Complete all Lead Genealogy topics", "\uD83C\uDF33", Category.PATH),
    GEN_APPRENTICE_CAPSTONE("Pedigree Builder", "Submit your Genealogy Apprentice capstone", "\uD83C\uDF3F", Category.PATH),
    GEN_JUNIOR_CAPSTONE("Case Researcher", "Submit your Genealogy Junior capstone", "\uD83D\uDCC1", Category.PATH),
    GEN_SENIOR_CAPSTONE("Proof Argument", "Submit your Genealogy Senior capstone", "\u2696\uFE0F", Category.PATH),
    GEN_LEAD_CAPSTONE("Research Dissertation", "Submit your Genealogy Lead capstone", "\uD83C\uDFC6", Category.PATH),

    // \u2500\u2500 Natural Sciences tier badges \u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500
    SCI_APPRENTICE_COMPLETE("Science Curious", "Complete all Apprentice Natural Sciences topics", "\uD83C\uDF31", Category.LEARNING),
    SCI_JUNIOR_COMPLETE("Experimental Scientist", "Complete all Junior Natural Sciences topics", "\u2697\uFE0F", Category.LEARNING),
    SCI_SENIOR_COMPLETE("Research Scientist", "Complete all Senior Natural Sciences topics", "\uD83E\uDDEC", Category.LEARNING),
    SCI_LEAD_COMPLETE("Science Leader", "Complete all Lead Natural Sciences topics", "\uD83D\uDD2D", Category.PATH),
    SCI_APPRENTICE_CAPSTONE("First Inquiry", "Submit your Natural Sciences Apprentice capstone", "\uD83C\uDF0D", Category.PATH),
    SCI_JUNIOR_CAPSTONE("Scientific Report", "Submit your Natural Sciences Junior capstone", "\uD83D\uDCCA", Category.PATH),
    SCI_SENIOR_CAPSTONE("Research Design", "Submit your Natural Sciences Senior capstone", "\uD83D\uDD2C", Category.PATH),
    SCI_LEAD_CAPSTONE("Research Programme", "Submit your Natural Sciences Lead capstone", "\uD83C\uDFC6", Category.PATH),

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
