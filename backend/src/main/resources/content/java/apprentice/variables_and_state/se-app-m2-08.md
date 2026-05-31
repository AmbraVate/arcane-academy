---
id: se-app-m2-08
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
topicSlug: variables_and_state
topicTitle: "Variables & State"
topicSortOrder: 1
lesson: constants
title: "Constants"
sortOrder: 8
difficulty: 2
estimatedMinutes: 20
xpReward: 40
practiceType: JAVA
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-05]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Declares constants using the `final` keyword with correct SCREAMING_SNAKE_CASE naming"
    - "Explains that `final` prevents reassignment after declaration"
    - "Gives at least two examples of values that should be constants"
    - "Explains why replacing magic numbers with named constants improves readability"
    - "Code compiles and demonstrates that reassigning a final variable produces an error"
  keywords: [final, constant, SCREAMING_SNAKE_CASE, immutable, magic number, readability, named constant]
  modelAnswer: |
    ```java
    public class GameConstants {
        public static void main(String[] args) {
            // Constants using final keyword and SCREAMING_SNAKE_CASE
            final int MAX_HEALTH = 100;
            final double TAX_RATE = 0.15;
            final String GAME_TITLE = "Arcane Academy";
            final int MAX_LEVEL = 50;

            int playerHealth = MAX_HEALTH;
            double itemPrice = 80.0;
            double totalCost = itemPrice + (itemPrice * TAX_RATE);

            System.out.println("Game: " + GAME_TITLE);
            System.out.println("Starting health: " + playerHealth);
            System.out.println("Max level: " + MAX_LEVEL);
            System.out.println("Total cost: " + totalCost);

            // This would cause a compile error:
            // MAX_HEALTH = 150; // ERROR: cannot assign a value to final variable
        }
    }
    ```
guidedSteps:
  - id: se-app-m2-08-step1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      Complete the declaration to create a constant for the maximum number of players in a game:

      ```java
      ___ int MAX_PLAYERS = 4;
      ```
    inputConfig:
      placeholder: "keyword"
    markingRule:
      matchMode: EXACT
      accepted: ["final"]
      rejectedFeedback: "The `final` keyword makes a variable a constant — once assigned, it cannot be changed. The full declaration is `final int MAX_PLAYERS = 4;`"
    hint: "There is one keyword in Java that marks a variable as unchangeable after assignment."
    reflectionPrompt: "`final` means 'this value is final — it will not change.' Java will refuse to compile any code that tries to assign a new value to a `final` variable."

  - id: se-app-m2-08-step2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the correct name for the value `3.14159` if you were to make it a constant?

      ```java
      final double ___ = 3.14159;
      ```
    inputConfig:
      options:
        - "piValue"
        - "PI_VALUE"
        - "PiValue"
        - "pi_value"
    markingRule:
      matchMode: EXACT
      accepted: ["PI_VALUE"]
      rejectedFeedback: "Constants in Java use SCREAMING_SNAKE_CASE: all uppercase letters, with underscores between words. `PI_VALUE` is correct. `piValue` is camelCase (for variables), `PiValue` is PascalCase (for classes)."
    hint: "Constants use all caps with underscores between words. Think LOUD_NAME."
    reflectionPrompt: "SCREAMING_SNAKE_CASE makes constants visually distinct from regular variables. When you see `MAX_PLAYERS` in code, you instantly know it is a constant that should never change."

  - id: se-app-m2-08-step3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of the following is the best reason to use a named constant instead of writing the number directly in code?
    inputConfig:
      options:
        - "Constants are faster to compute than regular numbers"
        - "Java requires you to use constants for all numbers"
        - "Named constants explain what the value means and make it easy to update in one place"
        - "Constants use less memory than variables"
    markingRule:
      matchMode: EXACT
      accepted: ["Named constants explain what the value means and make it easy to update in one place"]
      rejectedFeedback: "The key benefits of named constants are readability (the name explains the meaning) and maintainability (if the value ever changes, you update it in one place). `if (level >= 50)` is a 'magic number'. `if (level >= MAX_LEVEL)` is self-documenting."
    hint: "Think about what happens when you need to change the value. And think about a reader who sees the number for the first time."
    reflectionPrompt: "Magic numbers are numbers with no explanation. `if (score >= 1000)` — what is 1000? `if (score >= WINNING_SCORE)` makes it obvious. And if the winning score changes, you update one line."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What happens if you try to reassign a `final` variable in Java?"
    options:
      - "The variable silently keeps its original value"
      - "Java prints a warning but continues running"
      - "A compile-time error occurs — `final` variables cannot be reassigned"
      - "The program runs but produces wrong output"
    correctIndex: 2
    feedback: "Attempting to reassign a `final` variable causes a compile-time error. Java prevents the program from even being built if any code tries to change a `final` variable after its initial assignment."

  - type: MULTIPLE_CHOICE
    question: "Which of the following follows the correct Java convention for a constant representing the maximum inventory size?"
    options:
      - "maxInventorySize"
      - "MaxInventorySize"
      - "MAX_INVENTORY_SIZE"
      - "max-inventory-size"
    correctIndex: 2
    feedback: "`MAX_INVENTORY_SIZE` is correct SCREAMING_SNAKE_CASE — all uppercase, underscores between words. This convention is specifically used for constants in Java to make them visually distinct from variables."

retrieval:
  recall: "What keyword makes a variable a constant in Java, and what naming convention should constants use?"
  explain: "Explain what a 'magic number' is and why replacing magic numbers with named constants is considered good practice."
  mistakeId:
    code: |
      final int maxHealth = 100;
      maxHealth = 150; // player bought an upgrade
    answer: "Two issues: (1) `final` variables cannot be reassigned — the second line causes a compile error. If health upgrades are possible, `maxHealth` should be a regular variable, not a `final`. (2) The name `maxHealth` should be `MAX_HEALTH` in SCREAMING_SNAKE_CASE if it is truly meant to be a constant."
---

# Hook

Deep in every ancient codex, certain truths are carved rather than written — permanent, immovable, sealed against revision. The value of pi does not change. The speed of light does not change. The maximum size of a chessboard does not change. Programs have the same category of knowledge: values that are fixed by design and should never, under any circumstances, be altered at runtime. When these values are clearly labelled as permanent, code becomes safer, clearer, and easier to maintain. What values in a program should be set in stone?

# Lore Introduction

"Not all rune vessels are meant to be refilled," Archmage Veylan says, moving to a section of the vault where certain vessels glow with a steady, sealed light. "These are bound with the seal of permanence. Their charge was set at inscription and may never be changed — not by accident, not by carelessness, and not by deliberate act." He gestures to one labelled `MAX_SPELL_CHARGES`. "This vessel holds the law of the Academy. Any mage who attempts to alter it will find their hand burned." In Arcane Academy, sealed vessels are called *constants*, and they hold the invariants of the system — the truths that must never change.

# Core Learning

## Concept Introduction

A **constant** is a variable whose value cannot be changed after it is initially set. In Java, constants are created using the `final` keyword.

```java
final type NAME = value;
```

**Naming convention:** Constants use **SCREAMING_SNAKE_CASE** — all uppercase letters, underscores between words.

| Regular variable | Constant |
|-----------------|---------|
| `int maxHealth = 100;` | `final int MAX_HEALTH = 100;` |
| `double pi = 3.14159;` | `final double PI = 3.14159;` |
| `int boardSize = 8;` | `final int BOARD_SIZE = 8;` |

**Examples of good constant candidates:**
```java
final int MAX_PLAYERS = 4;
final double TAX_RATE = 0.18;
final int STARTING_LIVES = 3;
final String VERSION = "1.0.0";
```

Attempting to reassign a `final` variable causes a **compile-time error**:
```java
final int MAX_HEALTH = 100;
MAX_HEALTH = 150; // ERROR: cannot assign a value to final variable MAX_HEALTH
```

## Why It Matters

Constants serve two purposes: **safety** and **readability**. Safety: a constant can never be accidentally changed — the compiler will reject any attempt. Readability: a number like `5000` in the middle of code is a "magic number" — it has no explanation. Replace it with `final int MAX_XP_PER_LEVEL = 5000;` and the code becomes self-documenting. Maintainability: if a constant's value ever changes (say, the max level increases from 50 to 100), you update one declaration rather than hunting through the code for every occurrence of `50`.

## Worked Examples

**Example 1 — Declaring and using constants:**
```java
final int MAX_LEVEL = 50;
final int STARTING_GOLD = 100;
final double DAMAGE_MULTIPLIER = 1.5;

int playerLevel = 1;
int currentGold = STARTING_GOLD;  // starts at 100

if (playerLevel >= MAX_LEVEL) {
    System.out.println("You have reached the maximum level!");
}
```

**Example 2 — Magic numbers vs named constants:**
```java
// With magic numbers (unclear):
if (score >= 10000) {
    rank = 3;
}

// With named constants (self-documenting):
final int HIGH_SCORE_THRESHOLD = 10000;
final int RANK_GOLD = 3;

if (score >= HIGH_SCORE_THRESHOLD) {
    rank = RANK_GOLD;
}
```

**Example 3 — Constants for configuration:**
```java
final int GRID_WIDTH = 10;
final int GRID_HEIGHT = 10;
final int MINE_COUNT = 15;

System.out.println("Board: " + GRID_WIDTH + "x" + GRID_HEIGHT);
System.out.println("Mines: " + MINE_COUNT);
```
If the game design changes, you update three lines. Without constants, you might have `10`, `10`, and `15` scattered across hundreds of lines of code.

## Common Mistakes

- **Using camelCase for constants:** `maxHealth` looks like a regular variable. Use `MAX_HEALTH` to signal that it is a constant.
- **Declaring a constant without assigning it immediately:** `final int MAX;` and then `MAX = 10;` later — this is only allowed in certain contexts. Best practice: assign at declaration.
- **Making values constants that should change:** Player health starts at 100 but can change — it should not be `final`. Only values that are *designed to never change* should be constants.
- **Forgetting to use the constant after declaring it:** Declaring `final int MAX_LEVEL = 50;` but then writing `if (level >= 50)` elsewhere defeats the purpose.
- **Lowercase constant names:** Constants should shout their permanence with SCREAMING_SNAKE_CASE. `pi` looks like a variable; `PI` looks like a constant.

## Mental Model

Think of constants like **physical laws engraved in stone**. The number of sides on a hexagon is 6 — this does not change based on user input or game state. The speed limit on a specific road is fixed (for the purposes of your program). The maximum hand size in a card game is defined by the rules. These values are *definitional* — they define what the system is. Engraving them in stone (using `final`) tells every reader: "this value is part of the definition of this system, not something that varies during execution."

## Mini Summary

- `final` makes a variable a constant — it cannot be reassigned after initial assignment.
- Constants use SCREAMING_SNAKE_CASE: `MAX_HEALTH`, `STARTING_LIVES`, `TAX_RATE`.
- Attempting to change a `final` variable causes a compile-time error.
- Named constants replace "magic numbers" with self-documenting names.
- Constants improve readability (names explain meaning) and maintainability (one place to update).
- Only use `final` for values that are truly fixed by design — never for values that legitimately change.

# Guided Practice Quest

*Archmage Veylan hands you a seal — a golden ring that, once pressed onto a vessel, can never be removed. "Use this wisely," he says. "Seal only what must never change. An incorrectly sealed vessel is worse than no seal at all." Complete the exercises to practise using the seal of permanence.*

# Solo Practice Quest

**The Game Configuration File**

You are building a simple game and need to define its core configuration. Declare at least six constants that define fixed aspects of the game:

- Maximum player health
- Starting gold amount
- Maximum level
- Number of lives at start
- Game title (a String)
- At least one double constant of your choice (e.g., experience multiplier, damage modifier)

Write the constants using proper `final` keyword and SCREAMING_SNAKE_CASE. Then write a short block of `System.out.println` statements that prints each constant with a descriptive label.

Finally, try writing a line that reassigns one of your constants. Add it as a comment with `// This would cause: ` and describe the error Java would produce.

# Integration

**Mathematics connection:** The concept of mathematical constants — `π` (pi), `e` (Euler's number), `c` (speed of light) — is the direct inspiration for programming constants. In Java, `Math.PI` is a built-in `final double` with the value `3.141592653589793`. Using named mathematical constants in code is both mathematically precise and communicates intent: any reader who sees `Math.PI` immediately understands the geometric or trigonometric context of the calculation, just as a physicist reading an equation with `c` knows they are dealing with relativity.

**Philosophy connection:** The philosopher Plato distinguished between *Forms* (eternal, unchanging ideals) and *instances* (particular, changing examples). Constants in programming reflect a Platonic structure: `MAX_LEVEL = 50` is the *Form* of maximum level — the ideal, defining value — while a player's current `level` is the changing instance. When we make something `final`, we are saying "this is a definition, not a measurement" — separating the timeless rules of the system from its mutable state.

*Free question: Java also has an `enum` type for defining a set of related constants (like `NORTH`, `SOUTH`, `EAST`, `WEST`). When do you think using an `enum` would be better than several separate `final` constants?*

# Lore Conclusion

Archmage Veylan steps back from the sealed vessels and nods. "Some truths," he says, "should never bend." The sealed amber vessel labelled `MAX_SPELL_CHARGES` glows with an unchanging light — clear, permanent, impossible to corrupt by accident or intent. You have now mastered all eight foundations of rune vessel management: why memory exists, how to declare vessels, how to name them, which substances they hold, how to bind essence, how to update them, where they are visible, and how to seal them forever. The first chapter of the Academy's teaching is complete. In the next chapter, you will learn the great operators — the tools that act upon these vessels to produce new knowledge.
