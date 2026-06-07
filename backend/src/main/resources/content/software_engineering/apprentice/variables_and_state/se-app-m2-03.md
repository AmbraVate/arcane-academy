---
id: se-app-m2-03
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
topicSlug: variables_and_state
topicTitle: "Variables and State"
topicSortOrder: 1
lesson: naming_variables
title: "Naming Variables"
sortOrder: 3
difficulty: 1
estimatedMinutes: 18
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-02]
integrationDomains: [psychology, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Demonstrates understanding of camelCase convention with a correct example"
    - "Lists at least two rules for valid Java variable names (no spaces, no leading digit)"
    - "Explains why meaningful names improve code readability"
    - "Identifies at least one example of a poor variable name and explains why it is poor"
    - "Explains what camelCase looks like and why it is used instead of spaces"
  keywords: [camelCase, identifier, naming, convention, readability, meaningful, descriptive]
  modelAnswer: |
    Good variable naming is one of the most impactful habits a programmer can build. In Java, variable names must follow certain rules: they cannot contain spaces, cannot start with a digit, and cannot be reserved keywords like `int` or `class`. Beyond these rules, Java uses camelCase convention: the first word is lowercase, and each subsequent word starts with an uppercase letter — for example, `playerHealth`, `totalGoldCollected`, or `isQuestComplete`.

    Meaningful names are crucial because code is read far more often than it is written. A variable called `x` forces every reader to figure out what it represents from context. A variable called `remainingHealthPoints` is self-documenting — anyone reading it instantly understands its purpose. Studies in software engineering consistently find that poor naming is one of the leading causes of bugs and maintenance difficulty, because misunderstanding what a variable represents leads to using it incorrectly.

    Poor names to avoid: single letters (except for short loops like `i`), abbreviations that are unclear (`tp` instead of `totalPoints`), and names that lie about the content (`isAlive` storing a score number).
guidedSteps:
  - id: se-app-m2-03-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of the following is a valid Java variable name written in correct camelCase?
    inputConfig:
      options:
        - "player health"
        - "PlayerHealth"
        - "playerHealth"
        - "player_health"
    markingRule:
      matchMode: EXACT
      accepted: ["playerHealth"]
      rejectedFeedback: "`playerHealth` is correct camelCase for Java variables: first word lowercase, subsequent words capitalised, no spaces or underscores. `PlayerHealth` is PascalCase (used for class names, not variables). `player health` has a space, which is illegal. `player_health` is snake_case, used in languages like Python but not Java."
    hint: "camelCase: first word lowercase, each new word starts with a capital. No spaces."
    reflectionPrompt: "camelCase is named after a camel's humps — the uppercase letters in the middle create 'humps' in the name. It is Java's standard for variable and method names."

  - id: se-app-m2-03-step2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      A programmer writes `int x = 42;` to store a player's score. What is the main problem with this name?
    inputConfig:
      options:
        - "It uses the wrong type — score should be a String"
        - "The name x gives no information about what the value represents"
        - "Variable names cannot be a single letter in Java"
        - "The value 42 is not a valid score"
    markingRule:
      matchMode: EXACT
      accepted: ["The name x gives no information about what the value represents"]
      rejectedFeedback: "Single letters *are* allowed in Java, but they are poor names because they are meaningless. `x` tells you nothing. `playerScore` tells you exactly what the variable holds. Note: single-letter names are acceptable for short loop counters like `i`, but not for meaningful program state."
    hint: "Think about what someone reading this code weeks later would understand about `x`."
    reflectionPrompt: "Code is read many more times than it is written. A name that takes 2 extra seconds to type can save 2 minutes of confusion every time someone reads it."

  - id: se-app-m2-03-step3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Rewrite the following poor variable names as good camelCase names that clearly describe what each variable holds:
      - `a` (stores a player's age)
      - `n` (stores a character's name)
      - `hp` (stores current hit points)
    inputConfig:
      minWords: 6
    markingRule:
      matchMode: CONTAINS
      accepted: ["playerAge", "characterName", "currentHitPoints", "hitPoints", "age", "name"]
      rejectedFeedback: "Good alternatives: `playerAge`, `characterName`, `currentHitPoints` (or `hitPoints`). Each name should describe the value's meaning without requiring the reader to guess."
    hint: "Write out what each variable actually stores, then convert to camelCase."
    reflectionPrompt: "Any reader who sees `playerAge`, `characterName`, and `currentHitPoints` instantly understands the code. That clarity is worth far more than the few extra keystrokes."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which Java naming rule is being violated here: `int 2fast = 90;`"
    options:
      - "Variable names cannot be longer than 5 characters"
      - "Variable names cannot start with a digit"
      - "Variable names must be written in uppercase"
      - "The value 90 must be stored as a double"
    correctIndex: 1
    feedback: "Java variable names cannot start with a digit. `2fast` is illegal because it begins with `2`. A valid alternative would be `twoFast` or better yet, a meaningful name that explains what 90 represents."

  - type: MULTIPLE_CHOICE
    question: "What is the camelCase version of the phrase 'maximum damage dealt'?"
    options:
      - "MaximumDamageDealt"
      - "maximum_damage_dealt"
      - "maximumdamagedealt"
      - "maximumDamageDealt"
    correctIndex: 3
    feedback: "`maximumDamageDealt` is correct camelCase: first word all lowercase, each subsequent word starts with a capital letter. `MaximumDamageDealt` is PascalCase (for class names). Underscores and all-lowercase are not Java variable conventions."

retrieval:
  recall: "List three rules that Java variable names must follow (technical rules, not just style conventions)."
  explain: "Explain in your own words why a variable named `numberOfCompletedQuestsThisSession` is better than one named `n`, even though `n` is much shorter to type."
  mistakeId:
    code: |
      int Total Score = 0;
      int 1stPlace = 1;
      String player name = "Alex";
    answer: "Three errors: (1) `Total Score` has a space — variable names cannot contain spaces, use `totalScore`. (2) `1stPlace` starts with a digit — names cannot begin with a number, use `firstPlace`. (3) `player name` again has a space — use `playerName`. All three also violate camelCase convention."
---

# Hook

Imagine receiving a treasure map where every landmark is labelled with a single letter: `A`, `B`, `C`. You have no idea what `A` means. Is it the cave? The cliff? The creek? Now imagine the same map with labels like `HiddenCave`, `NorthCliff`, and `MountainCreek`. You can navigate it without any other context. Code is exactly like this map — and the programmer who wrote it last month is a stranger to the programmer reading it today. How do you leave instructions clear enough for a stranger to follow?

# Lore Introduction

"Names carry power," Archmage Veylan says, running a finger along a shelf of rune vessels. Some glow with clear, bright labels. Others are scratched with single cryptic marks. "The vessel marked `g` — do you know what it holds? Gold? Glory? Grief? But *this* one" — he lifts a vessel etched with `currentGoldBalance` — "speaks for itself." The Academy's Law of Inscription states that every vessel must be named so that any mage who encounters it understands its purpose without interrogation. This is not a rule of syntax — it is a rule of respect for those who come after you.

# Core Learning

## Concept Introduction

Java variable names are called **identifiers**. They must follow certain rules and conventions.

**Hard rules (violating these causes a compile error):**

| Rule | Valid | Invalid |
|------|-------|---------|
| No spaces | `playerScore` | `player score` |
| No leading digit | `level2Boss` | `2boss` |
| No special characters (except `_` and `$`) | `maxHealth` | `max-health` |
| Not a reserved keyword | `isReady` | `int`, `class` |

**Convention (violating these causes confusion, not errors):**

| Convention | Correct | Incorrect |
|------------|---------|-----------|
| camelCase for variables | `heroName` | `HeroName`, `hero_name` |
| Meaningful names | `totalScore` | `ts`, `x` |
| No abbreviations | `playerHealth` | `plyrHlth` |

**camelCase** means: first word all lowercase, each subsequent word starts with a capital letter.
```
total gold collected → totalGoldCollected
is quest complete    → isQuestComplete
max damage per turn  → maxDamagePerTurn
```

## Why It Matters

Code is read far more often than it is written. Studies in software engineering show that developers spend roughly 70% of their coding time *reading* code — both their own and others'. A well-named variable is self-documenting: it tells the reader exactly what it holds without needing a comment. Poor names lead to misunderstandings, which lead to bugs. Naming is not a cosmetic concern; it is a correctness concern.

## Worked Examples

**Example 1 — Poor names vs good names:**
```java
// Poor names
int x = 100;
int y = 85;
boolean z = true;

// Good names — same program, instantly understandable
int maxHealth = 100;
int currentHealth = 85;
boolean isPlayerAlive = true;
```

**Example 2 — camelCase in action:**
```java
String playerFirstName = "Elara";
int numberOfCollectedGems = 17;
boolean hasCompletedTutorial = false;
double distanceToDestination = 42.7;
```
Each name reads almost like English. No abbreviations, no mystery.

**Example 3 — Names that are technically valid but violate conventions:**
```java
int GOLD = 100;         // ALL_CAPS is for constants, not variables
String PlayerName = "Zara"; // PascalCase is for class names, not variables
int totalscorepoints = 0;  // No camelCase humps — hard to read
```
These will compile but will confuse experienced Java developers.

## Common Mistakes

- **Using single letters:** `a`, `b`, `c` mean nothing. Reserve single letters for simple loop counters (`i`, `j`).
- **Abbreviating too aggressively:** `cHp`, `ttlScr`, `plr` — unreadable after a few days.
- **Starting with uppercase:** `PlayerScore` looks like a class name — reserved by convention for classes.
- **Using underscores between words:** `player_score` is Python/SQL style. Java uses `playerScore`.
- **Names that lie:** `isAlive = 42;` — a boolean-sounding name storing a number is deeply confusing.

## Mental Model

Think of variable names like **street addresses**. "House 7" is technically an address — it follows the rules — but "7 Oak Lane, Springfield" tells a delivery driver exactly where to go. Your variable names are the addresses inside your code. Anyone — including your future self — must be able to "deliver" their understanding to the right place instantly. A good name is an address that needs no further instructions.

## Mini Summary

- Variable names must follow hard rules: no spaces, no leading digits, no reserved words.
- Java uses **camelCase**: first word lowercase, each new word capitalised.
- Names should be **meaningful and descriptive** — say what the value represents.
- `playerHealth` is far better than `ph` or `x`.
- PascalCase (all words capitalised) is for class names, not variables.
- Good naming is an act of communication — to teammates, and to your future self.

# Guided Practice Quest

*Archmage Veylan gestures at a row of unlabelled rune vessels. "Before I can teach you to fill them," he says, "you must learn to inscribe them correctly. A mislabelled vessel is worse than an empty one — it deceives." Complete the naming exercises above.*

# Solo Practice Quest

**The Naming Audit**

You receive the following code from a fellow apprentice who needs help:

```java
int a = 250;
String b = "Torren";
boolean c = false;
int d = 3;
double e = 18.5;
```

This code tracks a hero's gold amount, their name, whether they have completed the main quest, their current level, and the distance in kilometres to the next town.

1. Rewrite all five variable declarations with descriptive camelCase names that reflect what each variable stores.
2. Write 2-3 sentences explaining why the original names are a problem, using the term *readability* at least once.

# Integration

**Psychology connection:** Research on cognitive load — the mental effort required to process information — shows that meaningful names dramatically reduce the effort needed to understand code. When a variable is named `x`, the reader's working memory must hold both the current value and a separately-remembered meaning ("x is the score"). A variable named `currentScore` fuses the two, freeing working memory for more complex reasoning. This is why experienced programmers treat naming as a core cognitive tool, not just a style preference.

**Philosophy connection:** The philosopher Gottlob Frege distinguished between *sense* (the meaning of a name) and *reference* (the thing a name points to). In programming, a variable name is the *sense* — it gives meaning to the raw memory address (the *reference*) where the value is stored. When programmers debate naming, they are engaging in a practical version of Frege's philosophical problem: how does a symbol carry meaning, and what makes it a good or poor representation of the thing it refers to?

*Free question: Should variable names ever include the type of value they hold, like `intScore` or `stringName`? What are the arguments for and against this practice?*

# Lore Conclusion

Archmage Veylan steps back and surveys the row of rune vessels, each now bearing a clear, descriptive inscription. "These," he says with approval, "will still make sense to any mage who studies them a century from now." He pauses at one vessel — `currentGoldBalance` — and lets its amber glow wash over his face. The lesson is learned: clarity is a form of power, and power can be given with nothing more than a well-chosen name. In the next lesson, you will learn the different *substances* a rune vessel can hold — the Academy's fundamental catalogue of data types.
