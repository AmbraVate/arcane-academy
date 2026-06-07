---
id: se-app-m2-19
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
topicSlug: control_flow
topicTitle: "Control Flow"
topicSortOrder: 3
lesson: common_conditional_mistakes
title: "Common Conditional Mistakes"
sortOrder: 19
difficulty: 2
estimatedMinutes: 20
xpReward: 40
practiceType: JAVA
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-18]
integrationDomains: [psychology, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly explains = vs == and why = in an if condition is a bug"
    - "Explains the missing braces pitfall and gives an example"
    - "Explains off-by-one errors with > vs >= and gives an example"
    - "Explains why `if (x == true)` is redundant and how to write it correctly"
    - "Explains what makes an else branch unreachable and gives an example"
  keywords: [assignment, equality, braces, off-by-one, redundant, unreachable, boolean, condition]
  modelAnswer: |
    Five common conditional mistakes in Java:

    1. **`=` vs `==`:** A single `=` is assignment; it sets a value. A double `==` is equality comparison; it checks if two values are equal. Writing `if (x = 5)` assigns 5 to x — in Java this produces a compile error for primitives because an int assignment is not a boolean.

    2. **Missing braces:** Without curly braces, only the immediately next statement is part of the if body. `if (x > 0) doA(); doB();` — `doB()` always runs regardless of the condition. Adding braces makes the intended body explicit.

    3. **Off-by-one (`>` vs `>=`):** Using `>` when `>=` is needed (or vice versa) means the boundary value is handled incorrectly. For example, `if (age > 18)` would reject an 18-year-old; `if (age >= 18)` correctly includes them.

    4. **`if (x == true)` redundancy:** If `x` is already a boolean, `x == true` is the same as just `x`. Write `if (x)` or `if (!x)` for readability.

    5. **Unreachable else:** An else branch after a condition that is always true (or after a return) can never execute. For example, `if (true) { return; } else { doSomething(); }` — the else never runs.
guidedSteps:
  - id: se-app-m2-19-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of the following would cause a compile error in Java?
      ```java
      int x = 5;
      if (x = 10) {
          System.out.println("Ten");
      }
      ```
    inputConfig:
      options:
        - "No error — it works fine and prints 'Ten'"
        - "No error — it silently assigns 10 to x and evaluates to false"
        - "Compile error — the condition `x = 10` is an int assignment, not a boolean"
        - "Runtime error — the program crashes at this line"
    markingRule:
      matchMode: EXACT
      accepted: ["Compile error — the condition `x = 10` is an int assignment, not a boolean"]
      rejectedFeedback: "In Java, `if` requires a boolean expression. `x = 10` assigns 10 to x and produces an int result — not a boolean. The Java compiler detects this and refuses to compile. (In C/C++, this would silently work and be a common bug — Java's stricter type system protects you.)"
    hint: "What type does `x = 10` evaluate to? What type does `if(...)` require?"
    reflectionPrompt: "Java's type system saves you from the = vs == mistake that plagues C/C++ programmers. The compiler will catch it — but you must understand why it is wrong so you can recognise it in code reviews."

  - id: se-app-m2-19-step2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      ```java
      int count = 5;
      if (count > 0)
          System.out.println("Positive");
          System.out.println("Done");
      ```
      What is printed when `count = -1`?
    inputConfig:
      options:
        - "Nothing is printed"
        - "Positive"
        - "Done"
        - "Positive Done"
    markingRule:
      matchMode: EXACT
      accepted: ["Done"]
      rejectedFeedback: "Without curly braces, only the immediately following statement (`System.out.println(\"Positive\")`) is part of the if body. `System.out.println(\"Done\")` is NOT inside the if — it always runs. When count is -1, the condition is false so 'Positive' is skipped, but 'Done' always prints."
    hint: "Without braces, how many lines are inside the if block?"
    reflectionPrompt: "Always use braces, even for single-statement if bodies. It prevents bugs when someone later adds a second line and assumes it is inside the if."

  - id: se-app-m2-19-step3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the difference between `if (age > 18)` and `if (age >= 18)` for a voting system that allows voting at exactly age 18. Which is correct and why?
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [">=", "18", "include", "correct", "exactly", "boundary"]
      rejectedFeedback: "`age > 18` is false when age equals exactly 18 — an 18-year-old would be incorrectly denied. `age >= 18` is true when age is 18 or above — correct. Off-by-one errors at boundary conditions are among the most common logic bugs in programming."
    hint: "What does > return when age is exactly 18? What does >= return?"
    reflectionPrompt: "Boundary values (the exact edge case) are where off-by-one errors live. Always ask: 'Should the boundary value be included or excluded?' and pick > or >= accordingly."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "If `isReady` is a boolean variable, which is the preferred way to check if it is true?"
    options:
      - "`if (isReady == true)`"
      - "`if (isReady == 1)`"
      - "`if (isReady)`"
      - "`if (isReady != false)`"
    correctIndex: 2
    feedback: "Since `isReady` is already a boolean, `if (isReady)` is the cleanest form. It reads almost like English: 'if ready'. The forms `isReady == true` and `isReady != false` are redundant — they compare a boolean to a boolean literal unnecessarily."

  - type: MULTIPLE_CHOICE
    question: "What is 'unreachable code' in the context of an else branch?"
    options:
      - "Code that runs only on weekdays"
      - "Code inside an else that can never execute because the if condition is always true"
      - "Code that compiles but is never tested"
      - "Code inside a nested if"
    correctIndex: 1
    feedback: "An else branch is unreachable if the corresponding if condition is always true — the else can never be reached. For example, `if (x >= 0 || x < 0)` is always true (every number is either non-negative or negative), so any else after it is dead code."

retrieval:
  recall: "List four common conditional mistakes in Java and the correct form for each."
  explain: "Why does Java produce a compile error for `if (x = 5)` when some other languages allow it?"
  mistakeId:
    code: |
      boolean isValid = true;
      if (isValid == true) {
          System.out.println("Valid");
      } else if (isValid == false) {
          System.out.println("Invalid");
      }
    answer: "Both conditions are redundant. Since `isValid` is a boolean, `isValid == true` is identical to `isValid`, and `isValid == false` is identical to `!isValid`. The preferred form is `if (isValid) { ... } else { ... }`. The `== true` and `== false` comparisons add visual noise without adding meaning."
---

# Hook

You have learned if, else, else-if, switch, and nested logic. Now you are ready to write real conditional code — which means you are also ready to make the most common mistakes. The good news: these mistakes are well-known, well-documented, and entirely avoidable once you know what to look for. This lesson is a diagnostic toolkit: five specific errors that trip up beginners (and occasionally experienced developers), each with a clear fix.

# Lore Introduction

"Every apprentice, at some point, inscribes a rune with a flaw they cannot see," Archmage Veylan says, spreading five smudged scrolls on the teaching table. "A wrong operator here. A missing boundary there. A condition that always fires." He taps each scroll in turn. "These are not random errors. They are systematic — the same five mistakes made by ten thousand apprentices before you." He looks up. "Study them now. Know them by sight. Then you will not make them."

# Core Learning

## Concept Introduction

**The five most common conditional mistakes:**

| # | Mistake | Quick Fix |
|---|---------|-----------|
| 1 | `=` instead of `==` | Use `==` for comparison in conditions |
| 2 | Missing braces | Always add `{ }` around if bodies |
| 3 | Off-by-one (`>` vs `>=`) | Ask: should the boundary be included? |
| 4 | `if (x == true)` redundancy | Use `if (x)` for boolean variables |
| 5 | Unreachable else | Remove or fix conditions that are always true |

## Why It Matters

These mistakes share a property: they do not always cause obvious crashes. Some cause silent wrong behaviour (off-by-one), some cause misleading code (redundant booleans), some cause compile errors (= vs ==). Knowing them means you can spot them in code review and avoid them in your own code.

## Worked Examples

**Example 1 — `=` vs `==`:**
```java
int score = 85;
// BUG: assignment, not comparison
// if (score = 100) { ... }  // Compile error in Java

// CORRECT: equality comparison
if (score == 100) {
    System.out.println("Perfect!");
}
```

**Example 2 — Missing braces:**
```java
int x = -1;
// BUG: only first line is in the if body
if (x > 0)
    System.out.println("Positive");
    System.out.println("Always prints!");  // NOT in the if block!

// CORRECT:
if (x > 0) {
    System.out.println("Positive");
    System.out.println("Also positive.");  // Now inside the block
}
```

**Example 3 — Off-by-one:**
```java
int age = 18;
// BUG: excludes the boundary value 18
if (age > 18) {
    System.out.println("Can vote.");
}
// CORRECT: includes 18
if (age >= 18) {
    System.out.println("Can vote.");
}
```

**Example 4 — Redundant boolean comparison:**
```java
boolean hasTicket = true;
// Redundant (but compiles):
if (hasTicket == true) { ... }

// Preferred (cleaner):
if (hasTicket) { ... }
if (!hasTicket) { ... }  // for the false case
```

**Example 5 — Unreachable else:**
```java
int x = 5;
// BUG: condition x >= 0 || x < 0 is ALWAYS true
if (x >= 0 || x < 0) {
    System.out.println("This always runs.");
} else {
    System.out.println("Unreachable — never runs.");  // Dead code
}
```

## Common Mistakes

- **Assuming indentation controls scope:** Java does not care about indentation. Only `{ }` braces define blocks.
- **Using `>` when the spec says "at least":** "at least 18" means `>= 18`, not `> 18`.
- **Comparing boolean variables to literals:** `if (flag == true)` and `if (flag != false)` are both redundant.
- **Putting `=` in a condition for String comparisons:** `if (name = "Alice")` does not even compile. Use `name.equals("Alice")`.
- **Not testing the boundary value:** Off-by-one bugs are caught by testing exactly the boundary (e.g., age = 18).

## Mental Model

Think of these five mistakes as **known hazards on a road**. Once you have driven the road before, you know exactly where the potholes are: "sharp turn at mile 2, pothole at mile 5, slippery bridge at mile 7." You do not need to rediscover them by driving into them. Knowing the hazards in advance is what transforms a cautious beginner into a confident coder.

## Mini Summary

- `=` assigns; `==` compares. Java will give a compile error if you use `=` in an if condition with primitives.
- Always use `{ }` braces — even for single-statement if bodies.
- Off-by-one: ask whether the boundary should be included (`>=`) or excluded (`>`).
- Boolean variables should be used directly: `if (ready)`, not `if (ready == true)`.
- Unreachable else branches are dead code — a sign the condition logic is wrong.

# Guided Practice Quest

*Archmage Veylan presents five broken runes on the teaching board. "Each contains exactly one mistake from the five we studied," he says. "Identify the mistake in each, explain what is wrong, and write the corrected version." Examine each rune carefully — the errors are subtle.*

# Solo Practice Quest

**The Code Review**

Read the following code carefully and identify ALL mistakes:

```java
boolean isAdmin = true;
int level = 10;

if (isAdmin == true) {
    if (level > 10)
        System.out.println("Senior admin");
        System.out.println("Admin logged in");
}
```

For each mistake:
1. Name the type of mistake.
2. Explain what goes wrong.
3. Write the corrected version.

# Integration

**Psychology connection:** Psychologists who study human error distinguish between "slips" (errors of action — you knew what to do but did it wrong) and "mistakes" (errors of intention — you had the wrong plan). Most of the five conditional mistakes in this lesson are slips: the programmer knows they want a comparison but writes an assignment; they know they want a block but forget the braces. Understanding the difference helps: slips are reduced by checklists and habits (always use braces); mistakes are reduced by deeper conceptual understanding (knowing what == means vs =).

**Philosophy connection:** The philosopher Karl Popper argued that scientific progress comes not from confirming theories but from attempting to *falsify* them — testing the cases most likely to prove them wrong. Applied to code: do not only test cases where you expect correct output. Test the boundary cases (age = 18, score = 80) where off-by-one errors live. Test the false case of every boolean condition. Popper's falsification principle is the philosophical basis for good unit testing.

*Free question: Why do you think `if (score = 100)` compiles and runs correctly in C but produces a compile error in Java? What design decision did Java's creators make, and was it a good one?*

# Lore Conclusion

Archmage Veylan gathers the five corrected scrolls and locks them in a glass cabinet labeled "Codex of Common Errors." "These five," he says, "are the hazards every apprentice maps in their first month. You have now mapped them before you needed to." He turns to a new section of the board. "Control flow is complete. You can make decisions, chain them, nest them, and you know their failure modes." He writes a single word: *loops*. "Now you will learn to repeat. Not once, not twice, but as many times as necessary — and how to make that repetition stop."
