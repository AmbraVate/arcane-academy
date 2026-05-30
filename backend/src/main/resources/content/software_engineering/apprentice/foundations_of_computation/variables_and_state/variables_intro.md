---
moduleId: sw-eng-app-foundations
moduleTitle: "Module 1: Foundations of Computation"
moduleGlyph: "⚡"
moduleSortOrder: 1
domainId: software_engineering
tier: APPRENTICE
topicSlug: variables_and_state
topicTitle: "Variables and State"
topicSortOrder: 1
id: sw-eng-app-variables-intro
title: "What is a Variable?"
sortOrder: 1
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
feynmanPrompt: "Explain what a variable is and why programs need them, as if you're talking to someone who has never written code before."
learningObjectives:
  - Explain what a variable is in plain English
  - Identify the three components of a variable declaration (name, type, value)
  - Recognise why programs need to store and retrieve information
integrationDomains:
  - psychology
  - mathematics
guidedSteps:
  - id: var-step-1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A variable has three parts. Complete the declaration below by filling in the **name**:

      ```java
      int ___ = 25;
      ```

      What would you call this variable to make its purpose clear?
    inputConfig:
      placeholder: "variable name"
    markingRule:
      matchMode: NORMALIZED
      accepted:
        - age
        - personage
        - userage
        - myage
      rejectedFeedback: "Choose a name that describes what 25 represents. What real-world quantity is 25 a good value for?"
    hint: "The number 25 could represent many things — a score, a temperature, or a person's **age**. Pick the name that best describes the value."
    reflectionPrompt: "Well chosen! `age` tells anyone reading the code exactly what's stored. A good variable name makes code self-documenting."

  - id: var-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      In your own words, describe the **three parts** of a variable declaration.

      Use the example `int score = 0;` to guide your answer.
    inputConfig:
      minWords: 6
    markingRule:
      matchMode: CONTAINS
      accepted:
        - type
        - name
        - value
      rejectedFeedback: "Every variable declaration has three parts. Think about: what *kind* of data it stores, what you *call* it, and what *data* it holds right now."
    hint: "In `int score = 0;` — `int` is the **type**, `score` is the **name**, and `0` is the **value**."
    reflectionPrompt: "Exactly! **Type → Name → Value** — that's the template for every variable declaration in Java. You'll recognise this pattern everywhere from here on."

  - id: var-step-3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      A program stores a user's score and later updates it. Which of the following best describes why a **variable** is the right tool here?
    inputConfig:
      options:
        - "Variables are faster than constants"
        - "Variables can hold a value that changes over time"
        - "Variables are required by the Java compiler"
        - "Variables use less memory than hard-coded numbers"
    markingRule:
      matchMode: NORMALIZED
      accepted:
        - "Variables can hold a value that changes over time"
        - "variables can hold a value that changes over time"
      rejectedFeedback: "Think about the word *variable* itself — it comes from *vary*. What quality makes a variable different from a fixed number in your code?"
    hint: "The clue is in the name: **vari**-able. The defining feature is that its value can *change*."
    reflectionPrompt: "Correct! A variable's defining feature is mutability — the ability to hold a changing value. That's precisely why a store's running total, a player's score, or a countdown timer all need variables."
---

# Hook

Every spell in the arcane arts begins with a *vessel* — a place to hold power.

Before an Arcane Scribe can cast their first enchantment, they must learn to bind energy into a container, give it a name, and recall it at will. In the language of machines, that vessel is called a **variable**.

You are about to learn how computers remember things.

# Lore Introduction

In the floating archives of Aetheria, every apprentice is handed a blank Grimoire on their first day. Its pages are empty — awaiting the first inscribed runes. The Grand Scribe explains:

> *"A rune without a vessel is noise. Bind your power to a name, and it becomes wisdom you can call upon."*

A variable is your first rune. It is the first act of making a machine remember something on your behalf.

# Core Learning

## Concept Introduction

A **variable** is a named storage location in your program's memory.

Think of it in three parts:

| Part  | What it means              | Example |
|-------|---------------------------|---------|
| **Name**  | What you call it          | `age`   |
| **Type**  | What kind of data it holds | `int`   |
| **Value** | The data itself            | `25`    |

In Java, you declare a variable like this:

```java
int age = 25;
```

This single line tells the computer: *"Reserve a slot in memory, label it `age`, fill it with the number `25`, and know that it can only hold whole numbers (`int`)."*

Variables can also change over time — that is why they are called **vari**-ables:

```java
age = 26;  // one year passes
```

## Why It Matters

Every program that does anything useful stores information. A game remembers your score. A banking app tracks your balance. A weather service knows today's temperature.

Without variables, a program has no memory. It could only perform a calculation and immediately forget the result — like doing mental arithmetic and never being able to write anything down.

Variables are the mechanism by which programs *think* across time.

## Worked Examples

**Example 1 — Storing a player's name**

```java
String playerName = "Elara";
System.out.println("Welcome, " + playerName + "!");
// Output: Welcome, Elara!
```

The `String` type holds text. The `+` operator joins strings together.

**Example 2 — Tracking a score**

```java
int score = 0;
score = score + 10;  // player earns points
score = score + 5;
System.out.println("Score: " + score);
// Output: Score: 15
```

Notice that `score = score + 10` reads the current value, adds 10, then stores the result back into the same variable.

**Example 3 — A boolean flag**

```java
boolean isLoggedIn = false;
isLoggedIn = true;  // user authenticates
```

`boolean` variables hold only two possible values: `true` or `false`. They are used as on/off switches throughout programming.

## Common Mistakes

- Declaring a variable twice in the same scope (Java will refuse to compile: *"variable already defined"*)
- Using a variable before assigning it a value (`int x; System.out.println(x);` — compile error)
- Choosing a type that is too small for the value (storing `3000000000` in an `int`, which only holds up to ~2.1 billion)
- Forgetting that `=` in code is *assignment*, not mathematical equality

## Mental Model

Imagine your computer's memory as a row of labelled post-it notes on a desk.

Each note has:
- A **label** (the variable name) written on the tab
- A **type** stamped in the corner (so you know what kind of thing can go on the note)
- A **value** written on the body of the note

When you write `int age = 25;` you are sticking a new note on the desk, labelling it `age`, stamping it `int`, and writing `25` on it.

When you later write `age = 26;` you are *erasing* the `25` and writing `26` — the label and type stay the same, only the value changes.

## Mini Summary

- A **variable** is a named storage location that holds a value of a specific type.
- Every variable has three things: a **name**, a **type**, and a **value**.
- Variables can change over time — that is their purpose.
- Java requires you to declare the type when you first introduce a variable.
- Common primitive types: `int` (whole number), `double` (decimal), `boolean` (true/false), `String` (text).

# Guided Practice Quest

Your first task as an apprentice scribe is to bind three values into the Grimoire.

Declare the following variables in your program:

1. A `String` called `spellName` holding the value `"Fireball"`
2. An `int` called `manaCost` holding the value `30`
3. A `boolean` called `isUnlocked` holding the value `false`

Then print each one on its own line using `System.out.println(...)`.

**Expected output:**
```
Fireball
30
false
```

*Tip: write one variable at a time and test as you go.*

# Solo Practice Quest

The Grand Scribe has asked you to record the vital statistics of a new apprentice — without looking back at the guided practice.

Declare variables to store:

- The apprentice's name (use your own name)
- Their age (use your real age)
- Whether they have completed their first lesson (`true` or `false`)

Print each value on a separate line.

# Integration

**Connecting to Psychology — Working Memory**

In cognitive psychology, *working memory* is the brain's temporary storage system — it holds a small amount of information actively in mind while you use it (Miller's Law: ~7 ± 2 items).

Variables in a computer are a *formalised* version of this same idea. Where human working memory is limited, fuzzy, and prone to decay, computer variables are precise, persistent until explicitly changed, and virtually unlimited in number.

When you write `int score = 15;`, you are offloading a fact from your own working memory into the machine's. The program can now remember `score` perfectly for as long as it runs — freeing your mental capacity for higher-level reasoning.

**Connecting to Mathematics — Algebra**

You have already encountered variables in algebra: `x = 5`, `y = 2x + 1`.

The difference is that in mathematics a variable is often *unknown* (something to solve for), whereas in programming a variable is *known* — you assigned it. Programming variables are closer to the idea of *substitution* in algebra: wherever you write `age`, the computer substitutes `25`.

# Lore Conclusion

The apprentice scratched the final rune onto the parchment, and the Grimoire glowed softly.

*"You have bound three truths to names,"* said the Grand Scribe. *"A spell without memory is a spark in the wind. A spell with memory is a fire that endures."*

The first page of the Grimoire was no longer blank.

Three variables. Three vessels. Three steps on the long road to mastery.

*The path ahead holds more runes to learn — types that carry decimals, types that carry whole sentences, types that carry other types entire. But all of them begin here: with a name, a type, and a value.*
