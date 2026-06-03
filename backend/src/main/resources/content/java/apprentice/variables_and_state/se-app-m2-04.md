---
id: se-app-m2-04
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
topicSlug: variables_and_state
topicTitle: "Variables and State"
topicSortOrder: 1
lesson: data_types
title: "Data Types"
sortOrder: 4
difficulty: 2
estimatedMinutes: 22
xpReward: 50
practiceType: JAVA
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-02, se-app-m2-03]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Declares variables of at least four different types (int, double, boolean, String)"
    - "Explains what each type is used for with a concrete example"
    - "Demonstrates understanding of when to use int vs double"
    - "Shows correct syntax for String literals (double quotes) and char literals (single quotes)"
    - "Code compiles without type mismatch errors"
  keywords: [int, double, boolean, String, char, type, primitive, literal, mismatch]
  modelAnswer: |
    ```java
    public class DataTypesDemo {
        public static void main(String[] args) {
            // int — whole numbers
            int level = 7;
            int enemiesDefeated = 42;

            // double — decimal numbers
            double healthPercentage = 87.5;
            double distanceToPortal = 3.14;

            // boolean — true or false
            boolean hasKey = true;
            boolean isGameOver = false;

            // String — text
            String spellName = "Fireball";
            String welcomeMessage = "Welcome to Arcane Academy";

            // char — single character
            char grade = 'A';
            char firstLetter = 'Z';

            System.out.println("Level: " + level);
            System.out.println("Health: " + healthPercentage + "%");
            System.out.println("Has key: " + hasKey);
            System.out.println("Spell: " + spellName);
            System.out.println("Grade: " + grade);
        }
    }
    ```
guidedSteps:
  - id: se-app-m2-04-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A game needs to store a player's score (always a whole number), their username (text), and whether they are currently online (yes/no). Which set of types is correct?
    inputConfig:
      options:
        - "double, char, int"
        - "int, String, boolean"
        - "String, String, boolean"
        - "int, String, int"
    markingRule:
      matchMode: EXACT
      accepted: ["int, String, boolean"]
      rejectedFeedback: "Score is a whole number → `int`. Username is text → `String`. Online status is yes/no → `boolean`. So `int, String, boolean` is correct."
    hint: "Match each value to its nature: whole number, text, true/false."
    reflectionPrompt: "Choosing the right type is not arbitrary — it determines what operations you can perform and what values are valid. A `boolean` can only be `true` or `false`; a `String` can hold any text."

  - id: se-app-m2-04-step2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the key difference between `int` and `double` in Java?
    inputConfig:
      options:
        - "`int` can hold larger numbers than `double`"
        - "`int` holds whole numbers only; `double` holds decimal (fractional) numbers"
        - "`double` is for negative numbers; `int` is for positive numbers"
        - "They are identical — just different names for the same type"
    markingRule:
      matchMode: EXACT
      accepted: ["`int` holds whole numbers only; `double` holds decimal (fractional) numbers"]
      rejectedFeedback: "`int` stores only whole numbers like `5`, `100`, `-3`. `double` stores numbers with decimal points like `3.14`, `99.9`, `-0.5`. Use `int` for counting things; use `double` for measurements."
    hint: "Think about what each type's name suggests. 'double' refers to double-precision floating point."
    reflectionPrompt: "Use `int` when you count things (players, levels, coins). Use `double` when you measure things (distance, percentages, averages)."

  - id: se-app-m2-04-step3
    sortOrder: 3
    inputType: FILL_BLANK
    instruction: |
      Complete the type to correctly declare a variable holding a single character, like the letter 'A':

      ```java
      ___ grade = 'A';
      ```
    inputConfig:
      placeholder: "type"
    markingRule:
      matchMode: EXACT
      accepted: ["char"]
      rejectedFeedback: "`char` is the type for a single character in Java. Notice that char literals use *single* quotes: `'A'`, not `\"A\"`. Double quotes are for Strings."
    hint: "This type holds a single character and is written with single quotes around the value."
    reflectionPrompt: "`char` holds exactly one character. Single quotes: `'A'`. If you need text with more than one character, use `String` with double quotes: `\"Hello\"`."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which type would you use to store the result of dividing 7 by 2 (which is 3.5)?"
    options:
      - "int"
      - "boolean"
      - "double"
      - "char"
    correctIndex: 2
    feedback: "`double` is correct because 3.5 has a decimal component. An `int` would truncate to 3, losing the .5. Use `double` whenever the result might not be a whole number."

  - type: MULTIPLE_CHOICE
    question: "What is wrong with this declaration: `int playerName = \"Zara\";`"
    options:
      - "Nothing — it is perfectly valid Java"
      - "The variable name is too long"
      - "Type mismatch: `int` cannot hold text. Should be `String playerName = \"Zara\";`"
      - "Variable names cannot contain the word 'player'"
    correctIndex: 2
    feedback: "`int` is a numeric type — it cannot store text. `\"Zara\"` is a String literal. The correct declaration is `String playerName = \"Zara\";`. Java will refuse to compile the mismatched version."

retrieval:
  recall: "Name the five main primitive/common types covered in this lesson and give a one-word description of what each holds."
  explain: "Explain why Java requires you to specify a type when declaring a variable. What would go wrong if types did not exist?"
  mistakeId:
    code: |
      boolean score = 99;
      int isAlive = true;
      double name = "Elara";
    answer: "All three lines have type mismatches. `boolean` can only hold `true` or `false`, not `99` — use `int score = 99;`. `int` holds whole numbers, not booleans — use `boolean isAlive = true;`. `double` holds decimal numbers, not text — use `String name = \"Elara\";`."
---

# Hook

Imagine a library where every book is stored in the same kind of container — encyclopaedias in paperback sleeves, maps folded into tiny envelopes, audio recordings jammed into bookshelves. Chaos. Libraries work because different types of content get different types of storage. Programs work the same way: a player's name is not the same kind of thing as their health, and treating them identically would cause catastrophic confusion. Java's type system is its filing system — and understanding it unlocks everything that follows.

# Lore Introduction

"Not all rune vessels are equal," Archmage Veylan says, moving along a wall of very different containers. "This amber vessel holds whole numbers — integers, counts, quantities. This silver vessel holds quantities with fractions. This pale vessel holds only truth or falsehood. And this golden vessel holds words." In Arcane Academy, the type of a vessel determines what essence it can contain and what operations may be performed upon it. You cannot add a word to a truth-rune, and you cannot ask whether a number is spelled correctly. Each type has its place, and confusing them causes the walls to crack.

# Core Learning

## Concept Introduction

A **data type** tells Java what kind of value a variable can hold and what operations are valid on it.

**The five most common types at the Apprentice level:**

| Type | Stores | Example | Notes |
|------|--------|---------|-------|
| `int` | Whole numbers | `42`, `-7`, `0` | Counts, levels, ages |
| `double` | Decimal numbers | `3.14`, `99.9` | Measurements, percentages |
| `boolean` | True or false | `true`, `false` | Flags, conditions |
| `String` | Text (any length) | `"Hello"` | Names, messages — note capital S |
| `char` | A single character | `'A'`, `'!'` | Single quotes, exactly one character |

**Key syntax details:**
```java
int level = 5;              // integer — no quotes
double price = 9.99;        // decimal — no quotes
boolean isReady = true;     // boolean — no quotes, lowercase true/false
String spell = "Fireball";  // String — double quotes, capital S
char initial = 'K';         // char — single quotes, one character only
```

## Why It Matters

Types exist for two reasons: **correctness** and **efficiency**. Correctness: Java will refuse to store text in a number variable, catching a whole class of bugs at compile time before they ever run. Efficiency: each type uses a fixed amount of memory suited to its purpose — an `int` uses 4 bytes, a `boolean` uses 1 bit. Choosing the right type is not pedantic — it is how you communicate intent and let Java help you avoid mistakes.

## Worked Examples

**Example 1 — Choosing types for a game character:**
```java
String characterName = "Veylan";   // text → String
int currentLevel = 12;             // whole number → int
double healthPercentage = 73.5;    // decimal → double
boolean hasMagicShield = true;     // yes/no flag → boolean
char difficultyRating = 'A';       // single letter → char
```

**Example 2 — Type mismatch errors (what NOT to do):**
```java
int score = "one hundred";  // ERROR: int cannot hold text
boolean level = 5;          // ERROR: boolean cannot hold a number
String count = 42;          // ERROR: String needs quotes, and the type is wrong
```
Java will refuse to compile any of these — the type and the value must match.

**Example 3 — When to use int vs double:**
```java
int numberOfEnemies = 15;    // always whole — use int
int playerLevel = 7;         // always whole — use int
double attackSpeed = 1.75;   // has decimal component — use double
double winRate = 0.623;      // proportion/percentage — use double
```

## Common Mistakes

- **Using `int` when the result could be decimal:** `int average = 7 / 2;` gives `3`, not `3.5`. Use `double` when division might produce fractions.
- **Confusing `String` and `char`:** `String` uses double quotes and holds any length of text. `char` uses single quotes and holds *exactly one* character.
- **Lowercase `string`:** Java's text type is `String` (capital S). Writing `string name = "Bob";` causes a compile error.
- **Storing a number as a String:** `String score = "42";` stores text, not a number. You cannot do arithmetic on it.
- **Using `True`/`False` (capitalised) for boolean:** Java's boolean literals are lowercase: `true`, `false`.

## Mental Model

Think of data types like **different kinds of measuring tools**. A ruler measures length — you would not use it to measure temperature. A thermometer measures temperature — you would not use it to measure weight. Each tool is designed for its specific kind of measurement, and using the wrong one gives nonsense results or simply breaks. Java's types are your toolkit: pick the right instrument for the kind of value you are working with.

## Mini Summary

- `int` — whole numbers (counts, levels, scores).
- `double` — decimal numbers (measurements, ratios, prices).
- `boolean` — `true` or `false` only (flags, on/off states).
- `String` — text of any length, double quotes, capital S.
- `char` — exactly one character, single quotes.
- Mixing a type with the wrong kind of value causes a compile-time error.

# Guided Practice Quest

*Archmage Veylan leads you to the Academy's Substance Chamber — five walls, each lined with vessels of a single type. "The apprentice who confuses amber with silver will have a very bad afternoon," he warns. Complete the questions above to prove you can match each essence to its proper vessel.*

# Solo Practice Quest

**The Adventurer's Profile Card**

A game's player profile needs to store the following information. For each item, declare a correctly-typed Java variable with a meaningful camelCase name and a sensible starting value:

1. The player's username
2. The player's current level (start at 1)
3. The player's win rate as a percentage (start at 0.0)
4. Whether the player has completed the tutorial (start as false)
5. The grade rating of the player's account ('B' as a starting grade)

After declaring all five variables, print all five values in a single block of `System.out.println` statements, clearly labelling each one.

# Integration

**Mathematics connection:** Java's distinction between `int` and `double` mirrors a fundamental mathematical distinction between the **integers** (the set {..., -2, -1, 0, 1, 2, ...}) and the **real numbers** (which include all decimal fractions). Integer arithmetic is exact; floating-point arithmetic (double) introduces rounding. This is why financial software often avoids `double` for currency — the rounding errors in floating-point arithmetic can accumulate. Mathematicians and computer scientists have studied this problem extensively, leading to special types like `BigDecimal` for cases where exact decimal arithmetic is required.

**Philosophy connection:** The philosopher Bertrand Russell argued that mathematics could be reduced to pure logic — and Java's type system is a practical expression of this idea. Types enforce logical categories: a `boolean` can only be `true` or `false` because logical propositions only have two states. An `int` encodes the concept of discrete counting numbers. By forcing programmers to declare what category of thing each variable represents, Java makes logical structure explicit and enforceable — turning philosophical categories into compile-time constraints.

*Free question: Java also has `long` (larger integers), `float` (less precise decimals), and `byte` (very small integers). Why do you think Java offers so many numeric types instead of just one? What trade-off is being managed?*

# Lore Conclusion

Archmage Veylan seals each vessel type with its corresponding rune and steps back. "You now know the five great substances," he says. "Each has its purpose; none can be substituted for another without consequence." The amber vessels of integers line up neatly beside the silver vessels of decimals, the pale vessels of truth, the golden vessels of words, and the tiny vessels of single marks. You have mastered the *what* — what each type holds. In the next lesson, you will master the *how* — how values are assigned into these vessels using the most important symbol in all of programming: the equals sign.
