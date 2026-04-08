package com.ambravate.polymath.academy.model;

import lombok.Getter;

@Getter
public enum BadgeDefinition {

    // Quest milestones
    FIRST_SPELL("First Spell Cast", "Complete your first quest", "\u2728", Category.QUEST),
    CHAPTER_I_COMPLETE("Rune Initiate", "Complete all Chapter I quests", "\uD83D\uDD2E", Category.QUEST),
    CHAPTER_II_COMPLETE("Tome Scholar", "Complete all Chapter II quests", "\uD83D\uDCDC", Category.QUEST),
    CHAPTER_III_COMPLETE("Structure Weaver", "Complete all Chapter III quests", "\uD83D\uDDDD", Category.QUEST),
    CHAPTER_IV_COMPLETE("Grimoire Keeper", "Complete all Chapter IV quests", "\uD83D\uDCD6", Category.QUEST),
    QUEST_MASTER("Quest Master", "Complete 25 quests", "\uD83C\uDFC5", Category.QUEST),

    // Boss milestones
    FIRST_BOSS("Dragon Slayer", "Defeat your first boss", "\uD83D\uDC09", Category.BOSS),
    ALL_BOSSES("Conqueror of Shadows", "Defeat all bosses", "\uD83D\uDC51", Category.BOSS),

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
        QUEST, BOSS, XP, STREAK
    }
}
