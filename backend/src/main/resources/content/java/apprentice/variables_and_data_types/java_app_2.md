---
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
domainId: java
tier: APPRENTICE
topicSlug: variables_and_state
topicTitle: "Variables & State"
topicSortOrder: 1
id: java-app-2a
title: "Primitives, Wrapper Classes & Type Safety"
sortOrder: 1
xpReward: 60
practiceType: JAVA
questType: KNOWLEDGE
feynmanPrompt: "Explain the difference between a primitive type and a wrapper class to someone who only knows basic arithmetic — no jargon allowed."
learningObjectives:
  - Name all eight Java primitive types and describe what each stores
  - Declare and initialise variables of at least three primitive types
  - Explain why Java provides Wrapper classes alongside primitives
  - Recognise and avoid common type-mismatch mistakes
integrationDomains:
  - mathematics
  - psychology
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Declares at least one int variable with a meaningful name and prints it
    - Declares at least one double variable and prints it
    - Declares at least one boolean variable and prints it
    - Declares at least one String variable and prints it
    - Uses correct Java syntax for each declaration (type name = value;)
  keywords:
    - int
    - double
    - boolean
    - String
    - declare
    - primitive
    - type
  modelAnswer: |
    Here is one valid solution — your variable names and values may differ:

    ```java
    int spellLevel = 5;
    double manaCostPerSecond = 2.75;
    boolean isChannelling = true;
    String spellName = "Arcane Missile";

    System.out.println(spellLevel);
    System.out.println(manaCostPerSecond);
    System.out.println(isChannelling);
    System.out.println(spellName);
    ```

    Key checks:
    - `int` stores whole numbers; `double` stores decimals.
    - `boolean` stores exactly `true` or `false`.
    - `String` is a class (capital S), not a primitive — text goes in double quotes.
    - Each declaration follows: `type name = value;`

guidedSteps:
  - id: prim-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Java has **eight primitive types**. Which of the following is **not** a Java primitive?

    inputConfig:
      options:
        - "int"
        - "double"
        - "String"
        - "boolean"
    markingRule:
      matchMode: NORMALIZED
      accepted:
        - "String"
        - "string"
      rejectedFeedback: "Remember — `String` starts with a capital S and is a *class*, not a primitive. The eight primitives are: byte, short, int, long, float, double, char, boolean."
    hint: "Seven of the eight primitives are lower-case. One option in this list starts with a capital letter — that's your answer."
    reflectionPrompt: "Correct! `String` is a *class* (part of `java.lang`), not a primitive. That's why it gets an Integer-style Wrapper treatment. The eight primitives — byte, short, int, long, float, double, char, boolean — are all lower-case in Java source."

  - id: prim-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A dungeon game tracks a monster's remaining health as a percentage: `87.5`.

      Complete the declaration with the **most appropriate type**:

      ```java
      ___ monsterHealth = 87.5;
      ```
    inputConfig:
      placeholder: "type name"
    markingRule:
      matchMode: NORMALIZED
      accepted:
        - double
        - float
      rejectedFeedback: "The value `87.5` has a decimal point — so it needs a floating-point type. `int` only holds whole numbers. Try `double` (the most common choice) or `float`."
    hint: "Values with decimal points need a floating-point type. Java defaults to `double` for decimal literals."
    reflectionPrompt: "Well done! `double` is the go-to floating-point type for most situations — it gives you 15 significant digits of precision. `float` works too (append `f` to the literal: `87.5f`) but is less common."

  - id: prim-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In one or two sentences, explain **why Java provides `Integer` (the Wrapper class)
      alongside `int` (the primitive)**.

      Think about what you know about Lists — can you put `int` values directly into an `ArrayList`?
    inputConfig:
      minWords: 8
    markingRule:
      matchMode: CONTAINS
      accepted:
        - object
        - collection
        - list
        - arraylist
        - null
        - generic
      rejectedFeedback: "Java's Wrapper classes exist because primitives are not objects. Many Java features (such as ArrayList and generics) can only work with objects — so `Integer`, `Double`, etc. act as object wrappers around the raw primitive values."
    hint: "`ArrayList<int>` is a compile error — you need `ArrayList<Integer>`. Why? Because generics require *objects*, not primitives."
    reflectionPrompt: "Exactly! Primitives live on the stack and are pure values; they have no methods and cannot be used where an Object is expected. Wrappers bridge that gap — and Java autoboxes between them automatically: `Integer x = 42;` works because the compiler inserts the conversion for you."
---

# Hook

Eight vessels of power. Eight ways to name what you know.

Before you can cast a single spell in the Arcane Academy, you must understand the *nature* of the things you store. A number of mana. A fraction of health. A name. A truth-or-falsehood. Each demands a different vessel — and choosing the wrong vessel will shatter the casting.

# Lore Introduction

In the Hall of Inscription, newly arrived apprentices face a wall of glowing crystals — each a different colour. Archmage Veylan gestures at them:

> *"Eight crystals. Eight natures. A whole number fits only in the amber. A fraction — in the sapphire. A single character — in the jade. A truth — in the obsidian. Choose wrong and the binding breaks."*

The crystals are Java's **primitive types**. Learning them is not optional. They are the bedrock on which every spell, every object, every system is built.

# Core Learning

## Concept Introduction

Java has **eight primitive types** — the most fundamental categories of data the language knows how to store:

| Type      | What it stores           | Size     | Example value  |
|-----------|--------------------------|----------|----------------|
| `byte`    | tiny whole number        | 8-bit    | `127`          |
| `short`   | small whole number       | 16-bit   | `32_000`       |
| `int`     | standard whole number    | 32-bit   | `1_000_000`    |
| `long`    | large whole number       | 64-bit   | `9_000_000_000L` |
| `float`   | decimal (less precise)   | 32-bit   | `3.14f`        |
| `double`  | decimal (more precise)   | 64-bit   | `3.14159265`   |
| `char`    | single Unicode character | 16-bit   | `'A'`          |
| `boolean` | true or false            | 1-bit    | `true`         |

In practice, you will use `int`, `double`, `boolean`, and `char` for the vast majority of work. The others exist for performance or storage-size reasons.

Beyond primitives, Java provides **Wrapper classes** (`Integer`, `Double`, `Boolean`, `Character`, etc.) — objects that wrap a primitive value so it can be used wherever an `Object` is required (such as in Collections, generics, or when you need `null`).

## Why It Matters

Choosing the wrong type causes bugs that are sometimes invisible. An `int` silently discards the decimal part of a division (`5 / 2 = 2`, not `2.5`). An `int` overflows at ~2.1 billion — and wraps around to a large negative number, producing bizarre results. A `char` holds one character; attempting to put a word in it is a compile error.

Type discipline is one of Java's greatest strengths: the compiler catches category errors before the program ever runs. Use it.

## Worked Examples

**Example 1 — Choosing types for a game character**

```java
String name    = "Elara";       // text — String (not a primitive)
int    level   = 12;            // whole number — int
double health  = 94.5;          // decimal — double
boolean alive  = true;          // flag — boolean
char   initial = 'E';           // single character — char
```

**Example 2 — Integer division trap**

```java
int totalXP  = 7;
int sessions = 2;
int average  = totalXP / sessions;   // result: 3, NOT 3.5
System.out.println(average);         // prints: 3
```

To preserve the decimal, at least one operand must be a `double`:

```java
double average = (double) totalXP / sessions;   // result: 3.5
```

**Example 3 — Autoboxing**

```java
// Primitive → Wrapper (autoboxing — compiler inserts conversion)
Integer boxed = 42;

// Wrapper → Primitive (unboxing)
int raw = boxed;

// Useful in collections
java.util.List<Integer> scores = new java.util.ArrayList<>();
scores.add(95);   // Java boxes 95 into Integer automatically
```

## Common Mistakes

- Using `int` when the value can be a decimal (leads to silent truncation in division).
- Forgetting the `L` suffix on large `long` literals — `9_000_000_000` without `L` is a compile error.
- Forgetting the `f` suffix on `float` literals — `float x = 3.14;` fails because `3.14` is a `double`.
- Comparing Wrapper objects with `==` instead of `.equals()` — `Integer a = 200; Integer b = 200; a == b` may return `false` because large integers aren't cached.
- Using `char` with double quotes — `char c = "A"` is a compile error; `char` literals use single quotes.

## Mental Model

Think of primitives as **pre-cut filing slots** in a physical cabinet.

The `int` slot is exactly the right width for a whole number — nothing bigger fits; nothing smaller wastes space. The `double` slot is wider and has extra depth for a decimal point. The `boolean` slot is the smallest possible — just a two-position switch labelled `true`/`false`.

Wrapper classes are like **envelopes** you slide the filing card into. The card itself hasn't changed, but now it can travel through a postal system (Java's object infrastructure) that only accepts envelopes.

## Mini Summary

- Java has **8 primitives**: byte, short, int, long, float, double, char, boolean.
- Use `int` for whole numbers, `double` for decimals, `boolean` for flags, `char` for single characters.
- **Wrapper classes** (`Integer`, `Double`, etc.) wrap primitives in objects; Java **autoboxes** between them automatically.
- Integer division **truncates** — cast to `double` first if you need a decimal result.
- The compiler enforces type correctness at compile time — trust it.

# Guided Practice Quest

Practise declaring and printing variables of four different types.

In your code editor, write four variable declarations:

1. An `int` to store a spell's power level (choose any whole number).
2. A `double` to store a mana cost per second (choose a decimal, e.g. `2.75`).
3. A `boolean` to track whether the spell is currently channelling (`true` or `false`).
4. A `String` to store the spell's name (any text in double quotes).

Print each variable on its own line with `System.out.println(...)`.

**Expected output** (values will differ):
```
5
2.75
true
Arcane Missile
```

# Solo Practice Quest

Without looking back at the examples, model a **dungeon encounter** using primitive types.

Declare variables to store:
- The dungeon level (a whole number)
- The boss's health as a percentage (a decimal)
- Whether the boss has a shield active (a flag)
- The boss's name (text)

Print each value on a separate line. Add meaningful variable names — code that reads like prose is code worth keeping.

# Integration

**Connecting to Mathematics — Number Sets**

Mathematicians classify numbers into sets: natural numbers (ℕ), integers (ℤ), rationals (ℚ), reals (ℝ). Java's type system maps onto this directly. `int` corresponds to a finite subset of ℤ (whole numbers with a fixed range). `double` approximates a subset of ℝ (the reals), though with finite precision — it cannot represent every real number exactly.

This is why `0.1 + 0.2 == 0.3` evaluates to `false` in Java (and almost every other language): `double` stores values in binary, and `0.1` has no exact binary representation, just as `1/3` has no exact decimal representation. When decimal precision is critical (financial calculations, for example), use `java.math.BigDecimal` rather than `double`.

**Connecting to Psychology — Working Memory**

Choosing the right type is a form of **chunking** — the cognitive strategy of compressing information into manageable units. Declaring `int level = 12` encodes both the fact (level is 12) and the constraint (level is always a whole number) in a single unit. When you return to this code months later, you instantly know the value cannot be `12.7`. Good types are compressed documentation that your future self reads for free.

# Lore Conclusion

Archmage Veylan nods as the final crystal glows.

*"Eight natures. You have named them all and chosen wisely. The careless scribe pours water into a fire-crystal and wonders why it cracks. The wise scribe chooses the vessel before the power."*

The Hall of Inscription is quiet now. Your Grimoire holds four new runes — amber, sapphire, obsidian, and the white light of text. The next chapter begins: giving your power direction.
