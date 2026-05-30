---
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
domainId: java
tier: APPRENTICE
topicSlug: control_flow
topicTitle: "Control Flow"
topicSortOrder: 3
id: java-app-4a
title: "Conditionals: if/else and switch-expressions"
sortOrder: 1
xpReward: 60
practiceType: JAVA
questType: KNOWLEDGE
feynmanPrompt: "Explain what 'control flow' means and how an if/else statement works, using only everyday non-coding language — pretend you're explaining it to a ten-year-old."
learningObjectives:
  - Write correct if / else-if / else chains in Java
  - Use switch-expressions (the modern arrow syntax) to replace multi-branch if chains
  - Identify when to use each construct and explain the tradeoffs
  - Avoid common conditional pitfalls (assignment vs comparison, missing default)
integrationDomains:
  - mathematics
  - psychology
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Uses an if / else-if / else chain with at least three branches
    - All conditions use a comparison operator (==, <, >, <=, >=, !=) or logical operator
    - The else branch handles the default / fallback case
    - The code compiles and produces the correct output for at least two test values
    - Variable names and branch labels are meaningful and readable
  keywords:
    - if
    - else
    - condition
    - comparison
    - boolean
    - branch
    - switch
  modelAnswer: |
    Here is one valid solution using if/else-if/else to assign a letter grade:

    ```java
    int score = 72;

    if (score >= 90) {
        System.out.println("Grade: A");
    } else if (score >= 80) {
        System.out.println("Grade: B");
    } else if (score >= 70) {
        System.out.println("Grade: C");
    } else if (score >= 60) {
        System.out.println("Grade: D");
    } else {
        System.out.println("Grade: F");
    }
    // Output: Grade: C
    ```

    Key checks:
    - Conditions are evaluated top-to-bottom; the first `true` branch runs.
    - `>=` not `>` avoids off-by-one errors at boundaries.
    - The final `else` with no condition is the default fallback.

guidedSteps:
  - id: cf-step-1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A spell costs 30 mana. If the caster has enough mana, they cast it; otherwise they fail.

      Complete the condition inside the if:

      ```java
      int mana = 45;
      if (___) {
          System.out.println("Spell cast!");
      } else {
          System.out.println("Not enough mana.");
      }
      ```

      What condition makes `"Spell cast!"` print when `mana = 45` but `"Not enough mana."` print when `mana = 20`?
    inputConfig:
      placeholder: "condition"
    markingRule:
      matchMode: NORMALIZED
      accepted:
        - mana >= 30
        - mana > 29
        - mana >= 30
        - "mana>=30"
        - "mana>29"
      rejectedFeedback: "The spell costs 30 mana. The caster succeeds if their mana is *at least* 30. Try `mana >= 30`."
    hint: "The spell costs 30. The caster needs *at least* 30 mana — so use `>=` (greater than or equal)."
    reflectionPrompt: "Correct! `mana >= 30` returns `true` whenever mana is 30 or above. With `mana = 45` the condition is true and the spell fires. With `mana = 20` it's false and the else branch runs."

  - id: cf-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is printed by the following code?

      ```java
      int level = 5;
      if (level > 10) {
          System.out.println("Senior mage");
      } else if (level > 5) {
          System.out.println("Junior mage");
      } else {
          System.out.println("Apprentice");
      }
      ```
    inputConfig:
      options:
        - "Senior mage"
        - "Junior mage"
        - "Apprentice"
        - "Nothing is printed"
    markingRule:
      matchMode: NORMALIZED
      accepted:
        - Apprentice
        - apprentice
      rejectedFeedback: "`level` is `5`. Is `5 > 10`? No. Is `5 > 5`? No — `>` is strict. So both conditions are false and the `else` branch runs, printing `Apprentice`."
    hint: "Trace carefully: `5 > 10` is false. `5 > 5` is also false (it is NOT greater — it is equal). What runs when every condition is false?"
    reflectionPrompt: "Exactly right! `5 > 5` is `false` because `>` is *strictly greater than*. If the intent was 'level 5 and above is Junior', the condition should be `level >= 5`. This is a very common off-by-one error — always check your boundary operators."

  - id: cf-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      You have a four-branch if/else-if chain that checks a spell tier (`"common"`, `"rare"`, `"epic"`, `"legendary"`).

      In one sentence, explain why a **switch-expression** might be a better choice here than if/else-if.
    inputConfig:
      minWords: 6
    markingRule:
      matchMode: CONTAINS
      accepted:
        - readable
        - cleaner
        - clearer
        - equality
        - equal
        - one value
        - same variable
        - exhaustive
        - default
        - switch
      rejectedFeedback: "Switch-expressions shine when you test the *same variable against multiple fixed values* (equality checks). An if/else chain with four equality conditions on the same variable is more verbose than a switch on that variable."
    hint: "Both work — but a switch makes it obvious that you're testing one variable against a fixed list of possible values, and the compiler can enforce completeness with a `default`."
    reflectionPrompt: "Right. Switch-expressions (arrow syntax, Java 14+) are more readable when branching on a single variable's exact value: `switch (tier) { case \"common\" -> ...; case \"rare\" -> ...; default -> ...; }`. The compiler flags missing cases, giving you safety for free."
---

# Hook

Every path splits. Every choice narrows the future.

A program that runs the same code no matter what is not a program — it is a recipe. The moment you need it to *decide*, to *branch*, to do one thing for a hero and another for a villain, you need **control flow**.

This is where programs come alive.

# Lore Introduction

The Arcane Academy's Grand Sorting Chamber contains a fork in the path. New apprentices approach it one by one, and a hovering Crystal of Judgement flares a different colour for each — sending them left, right, or centre.

Archmage Veylan explains:

> *"The Crystal asks a question. The answer is always true or false. Based on that truth, it chooses a path. That is all. That is everything. Every incantation of complexity you will ever write reduces to this."*

In Java, that crystal is the **conditional statement**.

# Core Learning

## Concept Introduction

A **conditional** evaluates a boolean expression and runs different code depending on whether it is `true` or `false`.

The basic form:

```java
if (condition) {
    // runs when condition is true
} else {
    // runs when condition is false
}
```

You can chain multiple conditions with `else if`:

```java
if (score >= 90) {
    System.out.println("A");
} else if (score >= 80) {
    System.out.println("B");
} else if (score >= 70) {
    System.out.println("C");
} else {
    System.out.println("F");
}
```

Conditions are evaluated **top to bottom**; the **first `true` branch** wins — no others run.

For testing a single variable against a list of fixed values, a **switch-expression** is often cleaner:

```java
String tier = "rare";
String label = switch (tier) {
    case "common"    -> "Grey";
    case "rare"      -> "Blue";
    case "epic"      -> "Purple";
    case "legendary" -> "Gold";
    default          -> "Unknown";
};
System.out.println(label); // Blue
```

The arrow (`->`) syntax (Java 14+) does not fall-through and is the preferred modern form.

## Why It Matters

Without conditionals, a program cannot respond to input, handle errors, enforce rules, or implement any form of logic. Every game, every API, every form validator — all of them are at their core a collection of `if` statements deciding what to do next.

Conditionals are also the entry point to thinking about **correctness**: does your condition cover all cases? What happens at the boundary?

## Worked Examples

**Example 1 — Damage calculation**

```java
int armour  = 10;
int rawDmg  = 25;
int damage;

if (armour >= rawDmg) {
    damage = 0;                   // armour absorbs all damage
} else {
    damage = rawDmg - armour;     // partial reduction
}
System.out.println("Damage taken: " + damage);  // Damage taken: 15
```

**Example 2 — Multi-branch tier check**

```java
int level = 7;
if (level >= 20) {
    System.out.println("Archmage");
} else if (level >= 10) {
    System.out.println("Senior Mage");
} else if (level >= 5) {
    System.out.println("Junior Mage");
} else {
    System.out.println("Apprentice");
}
// Output: Junior Mage (7 >= 5 is true; 7 >= 10 is false)
```

**Example 3 — Switch-expression (modern)**

```java
int day = 3;
String name = switch (day) {
    case 1 -> "Monday";
    case 2 -> "Tuesday";
    case 3 -> "Wednesday";
    case 4 -> "Thursday";
    case 5 -> "Friday";
    default -> "Weekend";
};
System.out.println(name); // Wednesday
```

## Common Mistakes

- Writing `=` instead of `==` in a condition (`if (x = 5)` assigns 5, then evaluates 5 as `true` — a logic bug, not a compile error for `boolean` conditions involving numeric results).
- Off-by-one errors: using `>` when you meant `>=` (or vice versa) at a boundary.
- Forgetting the `default` branch in a switch — if no case matches and there's no default, nothing runs.
- Relying on fall-through in old-style `switch` statements without `break` — the modern arrow syntax eliminates this entirely.
- Nesting conditions too deeply (more than 2-3 levels is a design smell — extract methods instead).

## Mental Model

Think of a conditional as a **railway junction**.

The train (program execution) arrives and a signal controller (your condition) checks the current state of the track. If the signal is green (true), the train goes left. If red (false), it goes right. Multiple else-if branches are like multiple forks in sequence — the train takes the first open route.

A switch-expression is a **multi-platform station**: the train checks its destination label and goes directly to the matching platform. No need to pass through each platform to check — it jumps straight.

## Mini Summary

- `if / else if / else` evaluates boolean conditions from top to bottom; the first `true` branch runs.
- `switch` (arrow syntax) branches on a single variable's exact value — cleaner for equality checks.
- Always include an `else` (or `default`) to handle the unexpected.
- Use `==` for comparison; `=` is assignment.
- Check boundary conditions carefully — `>` and `>=` differ by exactly 1.

# Guided Practice Quest

Write a program that assigns a letter grade to a score.

Given `int score = 72`, print the correct grade:
- 90+ → `"A"`
- 80–89 → `"B"`
- 70–79 → `"C"`
- 60–69 → `"D"`
- Below 60 → `"F"`

Use an `if / else-if / else` chain. Test with at least two values.

# Solo Practice Quest

The Crystal of Judgement must classify dungeon monsters by level.

Write a program that assigns a tier to a `int monsterLevel` variable:
- Level 1–5 → `"Minion"`
- Level 6–15 → `"Elite"`
- Level 16–30 → `"Champion"`
- Level 31+ → `"Boss"`

Print the tier. Then rewrite the same logic using a **switch-expression** where the value is the tier-string itself (hint: you may need to derive a category string first to switch on).

# Integration

**Connecting to Mathematics — Boolean Algebra**

Every condition in Java reduces to a boolean value (`true` or `false`). The logical operators `&&` (AND), `||` (OR), and `!` (NOT) are the programming counterparts of Boolean algebra — the branch of mathematics developed by George Boole in the 1840s. De Morgan's Laws apply: `!(A && B)` is equivalent to `(!A || !B)`. Understanding Boolean algebra makes complex conditions easier to simplify and reason about.

**Connecting to Psychology — Heuristics and Decision Making**

The `if/else` structure mirrors the heuristics humans use when making snap decisions: fast pattern-matching against a ranked list of rules. The top condition is checked first — like the System 1 (fast, automatic) thinking described by Kahneman. Unlike human heuristics, Java conditionals are completely deterministic: given the same input, they always produce the same output. This reliability is a strength — but it also means you must design your conditions to cover every case, because a program has no intuition to fall back on.

# Lore Conclusion

The Crystal of Judgement fades as the last apprentice takes their path.

*"You have learned to ask the right question,"* says Archmage Veylan. *"Every great incantation is, at its heart, a series of true-or-false questions, answered in order. Master the question, and you master the path."*

Your Grimoire adds a new glyph: the fork. It will appear in every spell you write from this moment on.
