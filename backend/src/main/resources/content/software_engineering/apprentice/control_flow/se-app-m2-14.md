---
id: se-app-m2-14
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
lesson: why_programs_need_decisions
title: "Why Programs Need Decisions"
sortOrder: 14
difficulty: 1
estimatedMinutes: 18
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-10]
integrationDomains: [philosophy, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains that without decisions, programs execute the same steps for every input"
    - "Describes branching as the ability to choose different paths based on conditions"
    - "Gives a concrete real-world example of decision logic in software"
    - "Explains how comparison operators produce the boolean values that drive decisions"
    - "Articulates why decision-making is what separates useful software from simple scripts"
  keywords: [branching, conditional, decision, path, control flow, boolean, if, program]
  modelAnswer: |
    Without decisions, a program is just a list of steps that always executes in exactly the same way, producing the same output regardless of input. This is useful for fixed transformations (like converting a temperature from Celsius to Fahrenheit), but useless for any situation that requires different behaviour based on different conditions.

    "Branching" refers to the program's ability to choose between two or more paths of execution based on a condition. A login page must decide: is the password correct? A game must decide: is the player's health above zero? A navigation app must decide: has the driver missed a turn? Each of these situations requires evaluating a boolean condition and executing different code depending on the result.

    Comparison operators produce the boolean values (true/false) that conditions are built from. When a condition evaluates to true, one branch of code runs; when it evaluates to false, another branch (or no branch) runs. This is "control flow" — the order in which statements execute is controlled by the program's decisions.

    Real-world examples include: an ATM checking whether your PIN is correct before granting access; a shopping cart checking whether an item is in stock; a weather app checking whether temperature is below freezing to show a frost warning.
guidedSteps:
  - id: se-app-m2-14-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A program always executes lines 1, 2, 3, 4, 5 in that order, with no variations. What is the main limitation of this approach?
    inputConfig:
      options:
        - "It is too slow to be useful in practice"
        - "It behaves identically regardless of the input — it cannot respond to different situations"
        - "It uses too much memory"
        - "It only works for mathematical calculations"
    markingRule:
      matchMode: EXACT
      accepted: ["It behaves identically regardless of the input — it cannot respond to different situations"]
      rejectedFeedback: "A program without decisions always runs the same sequence. It cannot say 'do this if the score is high, do that if it's low.' It cannot handle login success vs. failure. It cannot adapt to any input. Decisions are what make programs respond to context."
    hint: "Think about what a program that always does the same thing can and cannot do."
    reflectionPrompt: "Programs without decisions are rigid. They are useful for pure transformations (e.g., convert this file format) but useless for any situation requiring adaptation to different inputs or states."

  - id: se-app-m2-14-step2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Name THREE real-world software situations where a program must make a decision (choose different behaviour based on a condition). Be specific about what the condition is.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["if", "when", "condition", "check", "whether", "true", "false", "depends"]
      rejectedFeedback: "Good examples: (1) A login system checks 'is the password correct?' — if yes, grant access; if no, show an error. (2) An ATM checks 'does the account have sufficient balance?' — if yes, dispense cash; if no, decline. (3) A traffic light system checks 'has the timer expired?' — if yes, change state."
    hint: "Think of apps you use daily — what conditions do they check before deciding what to show or do?"
    reflectionPrompt: "Decisions are everywhere in software. Every conditional message, every personalised response, every error check — all are implementations of the branching concept you will learn to code."

  - id: se-app-m2-14-step3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      What value type provides the 'answer' to a conditional check that a program uses to decide which path to take?
    inputConfig:
      options:
        - "int — 1 for true, 0 for false"
        - "String — 'yes' or 'no'"
        - "boolean — true or false"
        - "double — values above 0.5 mean true"
    markingRule:
      matchMode: EXACT
      accepted: ["boolean — true or false"]
      rejectedFeedback: "In Java, conditions produce `boolean` values: `true` or `false`. The `if` statement needs a boolean to decide which path to take. This is why comparison operators (which return booleans) pair so naturally with `if` statements."
    hint: "Which type holds exactly two values: true or false?"
    reflectionPrompt: "Booleans are the bridge between comparison operators and control flow. A comparison like `health > 0` returns a boolean, and that boolean tells the if-statement which branch to execute."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is 'control flow' in a program?"
    options:
      - "The speed at which a program executes"
      - "The order in which statements are executed, which can vary based on decisions"
      - "How data flows between functions"
      - "The number of variables a program uses"
    correctIndex: 1
    feedback: "Control flow refers to the order of execution of statements. Without decisions, control flow is always linear (top to bottom). With decisions, the program can take different paths, execute different statements, or skip sections entirely."

  - type: MULTIPLE_CHOICE
    question: "Which best describes 'branching' in programming?"
    options:
      - "Creating multiple copies of a program"
      - "Using version control to create a code branch"
      - "The program choosing one of multiple possible execution paths based on a condition"
      - "A loop that repeats code multiple times"
    correctIndex: 2
    feedback: "Branching means the program can go one way or another depending on a condition. If the condition is true, take path A. If false, take path B. This choice is the fundamental mechanism behind all decision-making in code."

retrieval:
  recall: "What is the difference between a program with no decisions and one that uses branching?"
  explain: "Explain why boolean values (true/false) are the fundamental data type that enables decision-making in programs."
  mistakeId:
    code: |
      // Programmer's description of their program's logic:
      // "My program checks if the user is logged in and shows different content."
      // But their actual code just prints "Welcome!" every time, with no check.
    answer: "The code does not implement the described logic. Describing a decision in comments or in your head does not make it happen in the code — you must explicitly write a conditional statement (like `if (isLoggedIn)`) that evaluates the boolean condition and executes different code for each case. Intent does not automatically become behaviour."
---

# Hook

Every interesting thing software does involves a decision. Your email app decides whether a message is spam. Your phone's autocorrect decides whether to suggest a correction. A navigation app decides whether you missed your turn. A game decides whether you won or lost. Strip away all the decisions from a program, and you are left with a rigid script that always does the same thing — useful for very little. The moment you add decisions, you give the program the ability to *react*. What separates a programme that reacts from one that merely recites?

# Lore Introduction

"A spell that casts itself identically on every target," Archmage Veylan says, "is a weak spell." He waves his hand at a simple enchantment that fires regardless of whether the target is friend or foe. "True magic responds. It perceives the conditions before it and chooses the appropriate action." In Arcane Academy, the binding runes — the conditional constructs — are what separate a novice's rigid incantations from a master's adaptive spellwork. Without binding runes, every spell runs to completion in the same way for every target. With them, spells think.

# Core Learning

## Concept Introduction

**Control flow** is the order in which statements are executed in a program.

**Without decisions:** statements always execute in sequence, top to bottom:
```
Statement 1 → Statement 2 → Statement 3 → ... (always the same path)
```

**With decisions (branching):** the program evaluates a condition and chooses one path:
```
Statement 1
   ↓
Condition true? → Path A
Condition false? → Path B
   ↓
Statement 2
```

The fundamental decision structure has three components:

| Component | Role |
|-----------|------|
| **Condition** | A boolean expression (true or false) |
| **True branch** | Code that runs if the condition is true |
| **False branch** | Code that runs if the condition is false (optional) |

In Java, this is written with an `if` statement — which you will study in the next lesson.

## Why It Matters

Programs that cannot make decisions cannot handle different situations. A login system that does not check passwords is not a login system. A shopping app that does not check stock is not useful. A game that does not track whether the player is alive is not a game. Every feature of useful software exists because some programmer wrote a condition that responds to reality and routes execution accordingly.

## Worked Examples

**Example 1 — Conceptual: thermostat logic:**
```
IF the temperature is below 18°C:
    Turn heating on
ELSE:
    Turn heating off
```
The same code running on two different days with different temperatures will *do different things* — because of branching.

**Example 2 — Conceptual: game over check:**
```
IF player health is 0 or less:
    Show "Game Over" screen
    Stop the game loop
ELSE:
    Continue the game
```

**Example 3 — What decision-free code looks like (and why it fails):**
```
// No decisions:
print "Welcome"
print "Your account balance is updated"
print "Have a nice day"
```
This prints the same thing whether the user just deposited money, withdrew money, or made an error. It is useless for a banking app.

## Common Mistakes

- **Thinking describing a decision in comments makes it real:** You must actually write the code for the decision.
- **Confusing sequence with branching:** Code that always runs in the same order is sequential, not branching.
- **Thinking decisions require complex code:** The simplest decision is a single `if` with one condition. Start there.
- **Forgetting that conditions must produce booleans:** An `if` statement needs a true/false value to decide.
- **Confusing a single-path decision with a branch:** A true branch without a false branch is still a valid decision — it just does nothing (or continues) when the condition is false.

## Mental Model

Think of a program's execution as a **river flowing downhill**. Without decisions, the river has one channel — it always flows the same way. Decisions are like **forks in the river**: based on the current conditions (rainfall, terrain), the water takes one channel or another. The same river can take very different routes to the sea depending on which forks it encounters. A program with decisions can reach very different outcomes from the same starting point.

## Mini Summary

- Without decisions, programs execute the same sequence of steps every time.
- "Branching" means the program chooses between different execution paths.
- A condition is a boolean expression (true or false) that determines which branch runs.
- Control flow refers to the order in which statements execute.
- Decisions make programs respond to context — the fundamental property of useful software.
- Booleans are the bridge between comparison operators and decision-making constructs.

# Guided Practice Quest

*"Before you can cast a binding rune," Archmage Veylan says, "you must understand what it is for." He stands before a simple enchantment and a complex one. "The simple enchantment fires identically every time. The complex one reads the situation first." Complete the exercises to demonstrate your understanding of why decisions matter.*

# Solo Practice Quest

**The Decision Audit**

Think about an app or game you use regularly. Write a short essay (4-6 sentences) that:

1. Names the app or game.
2. Identifies at least THREE decisions the software must make to function correctly.
3. For each decision, describes the condition being checked (in plain English, not code).
4. Explains what would go wrong if the software could NOT make decisions.

Use the terms *condition*, *branching*, and *boolean* at least once each.

# Integration

**Philosophy connection:** The 17th-century philosopher René Descartes proposed a mechanistic view of the universe — that the natural world, including animals, operates like a complex machine following fixed rules. But he noted that machines cannot adapt to novel situations, only to those they were designed for. Descartes saw this inflexibility as the key difference between machines and minds. Decision-making in programs is an attempt to bridge this gap: by encoding conditions that cover many situations, programmers create software that *appears* to adapt. But every decision must still be anticipated and coded by a human — a profound limitation that lies at the heart of artificial intelligence research.

**Psychology connection:** Cognitive psychologists study how humans make decisions under uncertainty — and find that the same person makes different choices depending on how a decision is framed. In programming, the framing of a decision is determined by the *condition*: what exactly you check, and what you check it against. Choosing the right condition is not just a technical act — it requires understanding what the program is trying to respond to. This is why requirements analysis (understanding what decisions a program needs to make) is as important as coding.

*Free question: Can a program's control flow change based on randomness? For example, can a game decide to spawn a rare enemy with a 5% probability? How do you think that might work in code?*

# Lore Conclusion

Archmage Veylan extinguishes the rigid enchantment with a wave of his hand. "Now you understand *why* the binding runes exist," he says. "They are not syntax to memorise — they are the principle of responsiveness." He turns to the teaching board and writes a single keyword: `if`. "And this is the simplest binding rune. In the next lesson, you will inscribe it for the first time and experience the moment when your code first *chooses*."
