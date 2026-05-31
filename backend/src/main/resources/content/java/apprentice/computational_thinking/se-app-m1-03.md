---
id: se-app-m1-03
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m1
moduleTitle: "Module 1: Foundations of Computation"
moduleGlyph: "🧠"
moduleSortOrder: 1
topicSlug: computational_thinking
topicTitle: "Computational Thinking"
topicSortOrder: 1
lesson: decomposition
title: "Decomposition"
sortOrder: 3
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [algorithms_in_daily_life]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies a complex task and breaks it into sub-tasks"
    - "Each sub-task is smaller and more manageable than the original"
    - "Sub-tasks are independent or have clear dependencies"
    - "The decomposition could be handed to separate people/modules"
    - "No sub-task is itself too large (further decomposition applied if needed)"
  keywords: [decompose, sub-task, divide, independent, module, component, break down]
  modelAnswer: |
    Complex task: Build a website
    Decomposition:
    - Design (layout, colours, fonts) — independent
    - Content (text, images) — independent
    - Front-end code (HTML/CSS/JavaScript) — depends on design
    - Back-end code (server, database) — independent
    - Testing (check it works) — depends on front-end + back-end
    - Deployment (put it online) — depends on testing
guidedSteps:
  - id: decomp-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Why is decomposition useful when solving large problems?"
    inputConfig:
      options:
        - "It makes problems disappear"
        - "It breaks complex problems into manageable parts that can be solved independently"
        - "It always makes programs run faster"
        - "It removes the need to understand the full problem"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["It breaks complex problems into manageable parts that can be solved independently"]
      rejectedFeedback: "Decomposition doesn't eliminate complexity — it makes it *manageable*. By breaking a large problem into smaller ones, you can solve each part separately and combine the solutions."
    hint: "Think about building a house: you don't do everything at once. You plan, dig foundations, build walls, add roof, install utilities — separately, in order."
    reflectionPrompt: "Correct. Decomposition is how humans and teams tackle problems too large to hold in one mind at once. It's the basis of functions, classes, and microservices in software."

  - id: decomp-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Break down the task "Build a simple calculator" into at least 4 sub-tasks.
      Write them as a numbered list.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [input, display, calculate, button, result, number, operation, output]
      rejectedFeedback: "A calculator needs: a way to receive numbers (input), a display to show results, operations (+, -, ×, ÷), and logic to compute. Each of these is a decomposed sub-task."
    hint: "Think about what a calculator needs to do: show numbers, accept buttons, perform maths, show results. Each of these is a sub-task."
    reflectionPrompt: "Good decomposition. Notice how each sub-task can be designed and built independently. This is how real software projects are divided among teams."

  - id: decomp-step-3
    sortOrder: 3
    inputType: FILL_BLANK
    instruction: |
      In software, a function or method is a form of decomposition — it ___ a single sub-task
      with a clear name, hiding its implementation details from the caller.
    inputConfig:
      placeholder: "one word"
    markingRule:
      matchMode: NORMALIZED
      accepted: [encapsulates, handles, performs, solves, contains, wraps, implements]
      rejectedFeedback: "A function encapsulates (or handles/performs) a single sub-task. The caller only needs to know the name and what it returns — not how it works internally. This is decomposition in code."
    hint: "Think about what a function does: it groups related instructions under a single name, hiding the details."
    reflectionPrompt: "Exactly. Functions ARE decomposition in code. When you write `calculateTax(income)`, you've decomposed tax calculation into a named sub-task that any other part of the program can use without understanding the internals."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does 'decomposition' mean in computational thinking?"
    options:
      - "Making code run faster"
      - "Breaking a complex problem into smaller, manageable sub-problems"
      - "Deleting unnecessary code"
      - "Compressing data to use less memory"
    correctIndex: 1
    feedback: "Decomposition = breaking complex problems into smaller parts. The word comes from 'de-compose' — to un-combine. In software this becomes functions, classes, modules, and services."
  - type: MULTIPLE_CHOICE
    question: "Which of these is the best example of decomposition?"
    options:
      - "Writing all the code in one 500-line function"
      - "Splitting 'send an email' into: compose, validate address, connect to server, transmit, confirm"
      - "Making the code shorter"
      - "Asking someone else to do it"
    correctIndex: 1
    feedback: "'Send an email' broken into compose/validate/connect/transmit/confirm is textbook decomposition — complex task split into independent, named steps."

retrieval:
  recall: "What is decomposition, and why do programmers use it?"
  explain: "Explain decomposition to a child using the example of cleaning their bedroom."
  mistakeId:
    code: "Decomposition: writing a 1000-line function that does everything"
    answer: "A 1000-line function that does everything is the *opposite* of decomposition — it's a monolith. Decomposition means breaking it into small, named sub-functions each doing one thing."
---

# Hook

How do you eat an elephant? One bite at a time.

The oldest joke in engineering wisdom is also its most important lesson. No one programs a complex system in one step. Every great program — from a text editor to an operating system — was built by taking large, overwhelming problems and breaking them into pieces small enough to solve.

That skill is called decomposition, and it's the reason functions, classes, and modules exist.

> What is the largest task you have broken into smaller steps recently? How did you decide where to divide it?

# Lore Introduction

The Academy's senior architects never attempt to cast a Grand Incantation whole. Instead, they divide it into sub-spells: one handles the power source, another shapes the effect, a third controls duration. Each sub-spell can be tested in isolation, improved, and reused in other incantations.

*"A builder who tries to build a tower all at once will find themselves paralysed,"* Archmage Veylan observes. *"But a builder who first lays one stone, then another, then another — that one will finish."*

# Core Learning

## Concept Introduction

**Decomposition** is the process of breaking a complex problem or system into smaller sub-problems that are easier to understand, develop, and solve.

The sub-problems should be:
- **Smaller** than the original
- **Independently solvable** (or have clear dependencies)
- **Named** — giving each piece a clear identity
- **Recombine-able** — solutions can be combined to solve the original problem

## Why It Matters

Humans can hold roughly 4–7 items in working memory at once. A complex program has thousands. Decomposition makes any problem tractable by reducing what you need to think about at any one moment. In code, this becomes: functions, classes, packages, services.

## Worked Examples

**Example 1 — Building a social media app**

Original: "Build a social media app"
Decomposed:
1. User accounts (register, login, profile)
2. Posts (create, edit, delete, view)
3. Comments (add, delete, reply)
4. Feed (show posts from followed users)
5. Notifications (when someone likes/comments)

**Example 2 — A sorting algorithm**

Original: "Sort a list"
Decomposed:
1. Compare two adjacent elements
2. Swap if out of order
3. Repeat for all pairs
4. Repeat entire pass until no swaps needed

**Example 3 — In Java (preview)**

```java
// Instead of one giant method:
void doEverything() { ... }

// Decomposed into named sub-tasks:
void readInput() { ... }
void processData() { ... }
void displayResults() { ... }
```

## Common Mistakes

- **Decomposing too little** — sub-tasks are still enormous. Keep breaking them down until each does one clear thing.
- **Decomposing too much** — trivial operations broken into their own sub-tasks unnecessarily. Aim for meaningful chunks.
- **Ignoring dependencies** — some sub-tasks must complete before others can start. Map these out.
- **Naming sub-tasks poorly** — vague names like `doStuff()` defeat the purpose. Names should describe *what* is done.

## Mental Model

Decomposition is like building with **LEGO**.

You don't build a castle by pressing one giant button. You build individual bricks, then rooms, then towers, then the complete structure. Each piece can be inspected and tested independently. If one room collapses, it doesn't bring down the whole castle.

## Mini Summary

- ✔ Decomposition breaks complex problems into smaller, manageable sub-problems
- ✔ Good sub-tasks are independently solvable and clearly named
- ✔ In code: functions, classes, and modules are decomposition in practice
- ✔ Decomposition is how teams divide work and how large systems are built
- ✔ Good names for sub-tasks make the overall solution self-documenting

# Guided Practice Quest

**The Guild Project**

The Guild has asked you to plan the development of a simple library management system. You don't need to code it — just decompose it into sub-tasks that different apprentices could work on independently.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Choose one of the following complex tasks and decompose it into sub-tasks:
1. Building a simple online shop
2. Creating a quiz game
3. Writing a program that manages a school timetable

For your chosen task:
- List **at least 6 sub-tasks**
- For each, write one sentence describing what it does
- Identify which sub-tasks are **independent** and which **depend on others**
- Draw a simple dependency diagram (list "X depends on Y" relationships)

# Integration

**Connecting to Mathematics — Divide and Conquer**

Decomposition maps directly to a class of algorithms in computer science called **divide and conquer**. These algorithms (like merge sort and binary search) work by splitting the problem in half, solving each half independently, and combining the solutions.

Mathematically, divide and conquer transforms an O(n²) problem into an O(n log n) one — a dramatic speedup. The reason: when sub-problems are independent, they can be solved in parallel, and the combination step is cheap.

This is why decomposition isn't just an organisational technique — it's a *performance* technique too. How might the same principle apply to how teams of engineers organise their work?

# Lore Conclusion

The apprentice's plan is spread across three scrolls — each piece standing alone, each clearly named.

*"Now you understand what the Masters mean by 'divide the work,'"* says Archmage Veylan. *"Not to reduce effort — but to reduce confusion. A clear sub-task, given to a careful apprentice, will be done well. A vague grand task will be done badly or not at all."*

The library management system will be built. One scroll at a time.
---
