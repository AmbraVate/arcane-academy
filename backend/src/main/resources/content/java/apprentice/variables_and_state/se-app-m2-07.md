---
id: se-app-m2-07
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
lesson: scope_basics
title: "Scope Basics"
sortOrder: 7
difficulty: 2
estimatedMinutes: 22
xpReward: 40
practiceType: JAVA
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-05]
integrationDomains: [philosophy, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly identifies that a variable declared inside a block is not accessible outside it"
    - "Explains that curly braces define a scope block"
    - "Demonstrates understanding of why scope prevents naming conflicts"
    - "Identifies a compile error that would occur from accessing an out-of-scope variable"
    - "Explains a practical benefit of keeping variable scope as narrow as possible"
  keywords: [scope, block, curly braces, visibility, local, accessible, declaration, lifetime]
  modelAnswer: |
    Scope in Java defines where a variable is visible and can be used. A variable's scope is determined by the *block* it is declared in — a block being any section of code surrounded by curly braces `{}`. A variable declared inside a block only exists within that block; once the block ends (the closing `}`), the variable is gone.

    For example, if you declare `int x = 5;` inside an `if` block, you cannot access `x` outside of that block. Trying to do so causes a compile-time error: "cannot find symbol."

    Scope prevents naming conflicts because two different blocks can each have their own variable named `count` without interfering with each other. Each `count` lives in its own scope. This is similar to how two different classrooms can each have a student named "Alex" without confusion — they belong to separate contexts.

    Keeping scope narrow is good practice: a variable should exist only as long as it is needed. This reduces the chance of accidentally modifying the wrong variable and makes code easier to reason about.
guidedSteps:
  - id: se-app-m2-07-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Look at this code. Will it compile successfully?

      ```java
      public static void main(String[] args) {
          if (true) {
              int treasure = 50;
          }
          System.out.println(treasure); // line 5
      }
      ```
    inputConfig:
      options:
        - "Yes — `treasure` is declared in the program so it is accessible everywhere"
        - "No — `treasure` is declared inside the if block and cannot be accessed outside it"
        - "Yes — `true` means the if block always runs, so `treasure` is always created"
        - "No — you cannot declare variables inside an if block"
    markingRule:
      matchMode: EXACT
      accepted: ["No — `treasure` is declared inside the if block and cannot be accessed outside it"]
      rejectedFeedback: "This will NOT compile. `treasure` is declared inside the `if` block (between its `{}`). Its scope ends at the closing `}` of that block. Line 5, which is outside the block, cannot see `treasure`. The compile error is: 'cannot find symbol: treasure'."
    hint: "What are the curly braces of the if block? Where do they open and close?"
    reflectionPrompt: "A variable only exists within the block it is declared in. The closing `}` ends both the block and the variable's lifetime."

  - id: se-app-m2-07-step2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      How would you fix the code from Step 1 so that `treasure` can be printed after the if block?
    inputConfig:
      options:
        - "Move `int treasure = 50;` to before the if block (outside its curly braces)"
        - "Delete the if block entirely"
        - "Change `int` to `global int`"
        - "Declare it twice — once inside and once outside"
    markingRule:
      matchMode: EXACT
      accepted: ["Move `int treasure = 50;` to before the if block (outside its curly braces)"]
      rejectedFeedback: "The fix is to declare `treasure` in the outer scope — before the if block. Then it exists throughout the outer block (the method body), and both the if block and the code after it can access it."
    hint: "The scope that can see the println is the method body. Declare the variable in that scope."
    reflectionPrompt: "Declare variables in the smallest scope they need to be visible in — but if multiple parts of a block need to see them, declare them at the top of that shared enclosing block."

  - id: se-app-m2-07-step3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In one or two sentences, explain WHY Java has scope rules. What problem does limiting a variable's visibility solve?
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: ["conflict", "naming", "accidental", "visibility", "isolate", "prevent", "separate", "control"]
      rejectedFeedback: "Scope prevents naming conflicts and accidental modifications. Two separate blocks can each use a variable named `count` without interfering. It also limits where a bug can hide — if a variable is only visible in 10 lines, any bug involving it can only be in those 10 lines."
    hint: "Think about what could go wrong if every variable in a program was visible from everywhere."
    reflectionPrompt: "Scope is a safety feature. It limits both what code can accidentally change a variable and what names conflict with each other. Narrower scope = safer, clearer code."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What defines a scope block in Java?"
    options:
      - "Indentation — code that is indented belongs to the same scope"
      - "Curly braces {} — code inside a pair of braces forms a block with its own scope"
      - "Semicolons — each statement has its own scope"
      - "Methods — variables can only be declared inside methods"
    correctIndex: 1
    feedback: "In Java, scope is defined by curly braces `{}`. Everything declared inside a pair of braces is scoped to that block. Indentation in Java is cosmetic — it does NOT define scope."

  - type: MULTIPLE_CHOICE
    question: "Two variables in different blocks have the same name `count`. What happens?"
    options:
      - "A compile error — you cannot have two variables with the same name anywhere in a program"
      - "The second one overwrites the first one permanently"
      - "This is fine — each `count` lives in its own scope and they do not conflict"
      - "Java combines them into a single shared variable"
    correctIndex: 2
    feedback: "Scope allows the same name to be reused in different blocks without conflict. Each `count` is an independent variable that exists only in its own block. This is one of the key benefits of scope."

retrieval:
  recall: "What is the 'scope' of a variable in Java, and what syntax element creates a new scope?"
  explain: "Explain what error you would get if you tried to use a variable outside the block it was declared in."
  mistakeId:
    code: |
      public static void main(String[] args) {
          int playerLevel = 5;
          {
              int bonus = 10;
          }
          int total = playerLevel + bonus; // Is this valid?
      }
    answer: "This code will NOT compile. `bonus` is declared inside the inner block `{}` and its scope ends at that block's closing brace. The line `int total = playerLevel + bonus;` is outside that block and cannot see `bonus`. Fix: declare `bonus` before the inner block, or declare `total` inside the inner block."
---

# Hook

Imagine a word exists in every language at once — the same word meaning a hundred different things simultaneously. Every time anyone in the world said it, all hundred meanings activated at once. Chaos. Real languages prevent this through context: the same word can mean different things in different situations, because meaning is *scoped* to a context. Programming languages solve the same problem with scope rules. Without scope, every variable in a large program would have to have a globally unique name — and programs would be impossible to write.

# Lore Introduction

"Every rune vessel exists within a chamber," Archmage Veylan explains, gesturing at a series of nested rooms in the Academy's vault. "A vessel inscribed in the inner chamber can be seen only by those inside it. Step outside, and the vessel vanishes from perception — though it still exists within its chamber walls." This is the Law of Visible Chambers: a vessel's visibility does not extend beyond the chamber of its creation. The outer chamber may know nothing of what is inscribed within, and this is by design. It prevents chaos, prevents conflict, and gives every chamber its own private vocabulary.

# Core Learning

## Concept Introduction

**Scope** defines where a variable is visible and accessible in code. In Java, scope is determined by **curly braces `{}`**.

- Every pair of `{}` creates a new block.
- A variable declared inside a block exists only within that block.
- When the block ends (closing `}`), the variable goes out of scope and ceases to exist.

```java
public static void main(String[] args) {      // outer block opens
    int outerVar = 10;                         // visible throughout method

    if (true) {                                // inner block opens
        int innerVar = 20;                     // only visible in this block
        System.out.println(outerVar);          // OK: can see outer scope
        System.out.println(innerVar);          // OK: in same block
    }                                          // inner block closes — innerVar gone

    System.out.println(outerVar);             // OK: still in outer block
    System.out.println(innerVar);             // COMPILE ERROR: innerVar out of scope
}                                             // outer block closes
```

**Key rule:** Variables can see everything in their own block and all *enclosing* (outer) blocks — but not into inner (nested) blocks.

## Why It Matters

Scope keeps programs manageable. Without scope, a large program with thousands of variables would require every variable to have a globally unique name, and a bug in one part could accidentally change a variable used by a completely unrelated part. Scope isolates variables, prevents naming conflicts, and limits the "blast radius" of bugs — if a variable only exists in 20 lines of code, any bug involving it can only be in those 20 lines.

## Worked Examples

**Example 1 — Basic block scope:**
```java
public static void main(String[] args) {
    int level = 5;           // declared in method scope

    if (level > 3) {
        int bonus = 100;     // only exists inside this if-block
        System.out.println("Bonus: " + bonus);
    }
    // bonus is gone here — compile error if accessed
}
```

**Example 2 — Fixing an out-of-scope error:**
```java
// Before fix (broken):
if (playerIsAlive) {
    int score = 0;
}
System.out.println(score); // ERROR: score out of scope

// After fix:
int score = 0;             // declare in outer scope
if (playerIsAlive) {
    score += 100;          // update it (still visible here)
}
System.out.println(score); // OK: score is in this scope
```

**Example 3 — Same name in different scopes (no conflict):**
```java
{
    int count = 10;  // this count lives here
}
{
    int count = 20;  // different block, different count — no conflict
}
```

## Common Mistakes

- **Declaring a variable inside a block and trying to use it outside:** The most common scope error. Fix: move the declaration to the outer block.
- **Thinking indentation defines scope:** In Java, only `{}` matter. Indentation is cosmetic.
- **Declaring the same variable twice in the same scope:** `int x = 5; int x = 10;` in the same block is a compile error.
- **Assuming an inner block can't see the outer block:** Inner blocks *can* see outer variables — but not the other way around.
- **Not initialising a variable in the outer scope before updating it in the inner scope:** `int score; if (x) { score += 10; }` — `score` must have an initial value before use.

## Mental Model

Think of scope like **rooms within rooms**. A person in a small inner room can see and use everything in the outer hall, but someone standing in the outer hall cannot see anything inside the closed inner room. Variables declared in a block are in their own room. Outer rooms are always visible from inner rooms; inner rooms are private to those inside them. The more rooms you have, the more you can reuse the same furniture names without anyone getting confused about which chair belongs to which room.

## Mini Summary

- Scope defines where a variable is visible and usable.
- Java scope is defined by curly braces `{}` — each pair creates a block.
- A variable exists from its declaration to the closing `}` of its block.
- Inner blocks can see outer block variables; outer blocks cannot see inner block variables.
- Scope prevents naming conflicts and limits where bugs can hide.
- Declaring a variable in the narrowest scope it needs to be in is good practice.

# Guided Practice Quest

*Archmage Veylan walks you through the nested chambers of the vault. "You must always know which chamber your vessel was inscribed in," he says, "and you must never attempt to invoke a vessel from outside its chamber walls." Complete the exercises to prove you understand the boundaries.*

# Solo Practice Quest

**The Scope Audit**

Read the following code carefully and answer the questions below it:

```java
public static void main(String[] args) {
    int totalGold = 0;

    if (totalGold == 0) {
        int startingBonus = 50;
        totalGold += startingBonus;
    }

    {
        int questReward = 100;
        totalGold += questReward;
    }

    System.out.println("Total gold: " + totalGold);
}
```

1. What is the final value of `totalGold`?
2. Which variables are accessible on the last line (the `println`)?
3. Which variables are NOT accessible on the last line, and why?
4. What would you need to change to make `questReward` accessible after the anonymous block?

# Integration

**Philosophy connection:** The concept of scope in programming parallels the philosophical concept of *context-dependence* in semantics. Philosophers of language like Gottlob Frege and later David Kaplan argued that the meaning of expressions like "here," "now," or "I" depends on the context of utterance. Similarly, a variable name like `count` means different things in different scopes. Scope formalises the intuition that meaning is always *relative to a context* — a fundamental insight in both philosophy and computer science.

**Mathematics connection:** Scope corresponds to the concept of *bound variables* in formal mathematics and logic. In the expression `∑(i=1 to 10) i²`, the variable `i` is *bound* to the summation — it does not exist outside it. Similarly, a loop variable in Java is bound to its loop block. This parallel between mathematical quantification and programming scope is not coincidental — programming languages drew heavily from formal logic in their design.

*Free question: What would happen in a program if all variables were in "global" scope — visible everywhere? Give two specific problems this would cause in a large program.*

# Lore Conclusion

Archmage Veylan seals the inner chamber behind you with a measured click. "What is inscribed here," he says, "stays here — unless you choose to carry it outward." He holds up a glowing vessel, clearly visible only as long as you stand within the chamber. The moment you step through the threshold, it winks out of sight. Scope, you now understand, is not a restriction — it is a gift. It gives every part of a spell its own private language. In the next lesson, you will learn about constants — rune vessels that, once inscribed, may never be changed.
