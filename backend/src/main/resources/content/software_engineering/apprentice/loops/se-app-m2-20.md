---
id: se-app-m2-20
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
topicSlug: loops
topicTitle: "Loops"
topicSortOrder: 4
lesson: repetition_in_computation
title: "Repetition in Computation"
sortOrder: 20
difficulty: 1
estimatedMinutes: 18
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-19]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains why loops exist — to avoid writing the same code multiple times (DRY)"
    - "Describes the concept of iteration (one pass through a repeated action)"
    - "Gives two concrete real-world examples of repetition in computing"
    - "Explains that loops make programs work for any count, not just a fixed number"
    - "Distinguishes between a fixed number of repetitions and an unknown number"
  keywords: [loop, iteration, repetition, DRY, count, repeat, automate, sequence]
  modelAnswer: |
    Loops exist to allow a program to execute the same block of code multiple times without copying and pasting it. This is the DRY principle (Don't Repeat Yourself) applied to execution: instead of writing ten `println` calls for ten items, you write one `println` inside a loop that runs ten times.

    "Iteration" means one pass through the loop body — a single execution of the repeated block. A loop that runs ten times performs ten iterations.

    Real-world examples: (1) A printer iterating through each page in a document and printing it. (2) An email client iterating through each unread message and marking it as seen. Both involve doing the same action repeatedly to each member of a collection.

    The key power of loops is that they work for any count. If you hard-code ten `println` calls, your code only works for exactly ten items. With a loop, the same code works for one item, one hundred items, or one million items — you change the data, not the code.
guidedSteps:
  - id: se-app-m2-20-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A programmer wants to print the numbers 1 to 100. Without loops, they write:
      ```
      print(1)
      print(2)
      print(3)
      ...
      print(100)
      ```
      What is the primary problem with this approach?
    inputConfig:
      options:
        - "It is too slow to execute"
        - "It only works for 100 — changing to 1000 requires rewriting the code"
        - "Print statements cannot print numbers"
        - "The numbers must be stored in memory first"
    markingRule:
      matchMode: EXACT
      accepted: ["It only works for 100 — changing to 1000 requires rewriting the code"]
      rejectedFeedback: "The program is hard-coded for exactly 100 items. If the requirement changes to 1,000 or 1,000,000, the programmer must manually write thousands more lines. A loop solves this: change one number and the loop adapts automatically."
    hint: "What would you have to change if someone asked you to print 1 to 1,000 instead?"
    reflectionPrompt: "Hard-coding repeated operations makes code brittle and labour-intensive to change. Loops make repetition data-driven — the number of repetitions comes from a variable or collection, not from how many lines you wrote."

  - id: se-app-m2-20-step2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Give TWO real-world examples where a computer program must perform the same action many times. Describe what is being repeated and what it is repeated for (what is the collection or count).
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["each", "every", "repeat", "list", "all", "items", "collection", "for each"]
      rejectedFeedback: "Good examples: (1) A search engine crawling each webpage in its index and checking if a search term appears. (2) A bank statement generator looping through each transaction in the month and adding it to the total. (3) An image editor applying a filter to every pixel in a photo."
    hint: "Think of apps that process a list of things — messages, files, products, scores."
    reflectionPrompt: "Almost every interesting program involves processing a collection — a list of records, a set of files, a range of numbers. Loops are the mechanism for that processing."

  - id: se-app-m2-20-step3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does the term "iteration" mean in the context of loops?
    inputConfig:
      options:
        - "The total number of lines inside the loop body"
        - "One complete execution of the loop's body"
        - "The variable that counts how many times the loop has run"
        - "The condition that stops the loop"
    markingRule:
      matchMode: EXACT
      accepted: ["One complete execution of the loop's body"]
      rejectedFeedback: "One 'iteration' is one pass through the loop — one complete execution of everything inside the loop's body. If the loop runs ten times, it performs ten iterations. The word comes from the Latin 'iterare' (to repeat)."
    hint: "Think of iteration as one lap around a track."
    reflectionPrompt: "Iteration is a precise term: one loop cycle = one iteration. A loop that executes its body 50 times performs 50 iterations. Knowing the terminology helps you communicate precisely about loops."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which programming principle does the use of loops directly support?"
    options:
      - "YAGNI (You Aren't Gonna Need It)"
      - "SOLID (Single Responsibility)"
      - "DRY (Don't Repeat Yourself)"
      - "KISS (Keep It Simple, Stupid)"
    correctIndex: 2
    feedback: "Loops directly support DRY — Don't Repeat Yourself. Instead of duplicating the same statement ten or a thousand times, you write it once inside a loop. The repetition is expressed structurally (how many iterations) rather than literally (how many copies of the code)."

  - type: MULTIPLE_CHOICE
    question: "Why is a loop more flexible than copying the same code 10 times?"
    options:
      - "Loops execute faster than repeated statements"
      - "Loops work for any number of iterations determined at runtime; copied code is fixed"
      - "Loops allow the body to change on each iteration"
      - "Copied code is invalid Java syntax"
    correctIndex: 1
    feedback: "The key advantage of loops over copied code is flexibility. A loop can run 10, 100, or 1,000,000 times depending on data available at runtime (a list length, a user input, a file size). Copied code runs a fixed number of times determined at the time you wrote it."

retrieval:
  recall: "Explain in one sentence why loops are an application of the DRY principle."
  explain: "What is 'iteration'? How does the number of iterations relate to the number of times the loop body executes?"
  mistakeId:
    code: |
      // A student wrote this to greet 5 users:
      System.out.println("Hello, user 1");
      System.out.println("Hello, user 2");
      System.out.println("Hello, user 3");
      System.out.println("Hello, user 4");
      System.out.println("Hello, user 5");
    answer: "This is repeated code — the DRY principle violation. The same structure is copied five times with only the number changing. If the greeting format changes (e.g., 'Welcome' instead of 'Hello'), all five lines must be updated. A loop would repeat the action once, using a counter variable for the number: `for (int i = 1; i <= 5; i++) { System.out.println(\"Hello, user \" + i); }`"
---

# Hook

Imagine you need to check every email in your inbox — one by one. Or process every pixel in a photograph. Or validate every entry in a spreadsheet with ten thousand rows. Doing this manually, statement by statement, is not just tedious — it is impossible to scale. The computer's greatest advantage over human beings is not speed alone; it is the ability to repeat an operation as many times as needed without fatigue, boredom, or error. That ability has one name in code: a loop.

# Lore Introduction

"A spell cast once is limited," Archmage Veylan says, gesturing to a row of identical candles that need to be lit. "But a spell cast in rhythm — once, again, again, until the task is complete — that is the power of the repetition rune." He lights the first candle with a single incantation. "Imagine having to write that incantation one hundred times, once for each candle. Absurd." He draws a single loop symbol on the board. "Or: write it once, bind it in a repetition rune, and let the rune do the counting." In Arcane Academy, loops are the engine that transforms a single spell into an unstoppable sequence.

# Core Learning

## Concept Introduction

A **loop** is a programming construct that executes a block of code repeatedly.

**Why loops exist:** Without loops, any repeated action must be written out manually for every repetition. With loops, you write the action once and specify how many times (or under what conditions) to repeat it.

**Iteration:** One execution of the loop's body is called an *iteration*. A loop that runs 100 times performs 100 iterations.

**DRY applied to repetition:**
```
// Without loops — violates DRY:
print item 1
print item 2
print item 3
...
print item 1000

// With a loop — DRY:
repeat 1000 times:
    print next item
```

**Two categories of loops:**
| Category | Description | Example |
|----------|-------------|---------|
| **Fixed count** | Repeat exactly N times | Print numbers 1 to 10 |
| **Unknown count** | Repeat until a condition becomes false | Read input until valid |

## Why It Matters

Nearly every real-world program involves processing collections of data. A web server handles many requests. An analytics system processes many events. A game updates many objects. Without loops, none of this is possible at scale. Loops are the mechanism that makes software work for real-world data sizes.

## Worked Examples

**Example 1 — Manual repetition (the problem):**
```
// Imagine needing to print every number from 1 to 1,000,000
System.out.println(1);
System.out.println(2);
// ... 999,998 more lines
System.out.println(1000000);
// Completely impractical
```

**Example 2 — Pseudocode loop (the solution concept):**
```
for each number from 1 to 1,000,000:
    print that number
// One instruction, one million executions
```

**Example 3 — Real-world loop scenarios:**
```
// Email client:
for each email in inbox:
    if email is unread:
        mark as unread in counter

// Image processor:
for each pixel in image:
    apply brightness filter

// Bank statement:
for each transaction in month:
    add amount to running total
```

## Common Mistakes

- **Thinking repetition means copying code:** Copy-pasting is the wrong kind of repetition. Loops express repetition structurally.
- **Confusing "loop runs once" with "no loop needed":** Even a single-iteration loop is valid if the count comes from data. Tomorrow there might be more items.
- **Not seeing loops in everyday programs:** Almost any program that processes a list, a file, or a collection is using a loop. They are everywhere.
- **Thinking loops are only for counting:** Loops repeat until a condition changes — not just for a fixed count. A loop can run until a user types "quit".

## Mental Model

Think of a loop as a **factory assembly line**. The assembly line does not change — the same steps are performed in the same order. What changes is the product being processed: car 1, car 2, car 3. The assembly line (the loop body) is written once. The number of cars (the iteration count) comes from how many cars arrive on the line. The same factory can produce one car or ten thousand cars — the instructions do not change, only the input.

## Mini Summary

- Loops allow a block of code to execute multiple times without being written multiple times.
- Loops apply the DRY principle to execution, not just code structure.
- One pass through the loop body is called one iteration.
- Loops can run a fixed number of times or until a condition becomes false.
- Almost all programs that process collections, files, or streams use loops.
- Writing the same statement multiple times instead of using a loop is a DRY violation.

# Guided Practice Quest

*"Before you cast the repetition rune, you must understand why it exists," Archmage Veylan says. "Describe in writing: what problem does the repetition rune solve that a single-cast spell cannot? Give two examples from the real world of a task that requires repetition." Complete the written exercises before proceeding to loop syntax.*

# Solo Practice Quest

**The Repetition Inventory**

Think of a software application you use regularly (a game, a social media app, a music player, a navigation app). Identify THREE separate features within that application that must use loops internally to work correctly.

For each feature:
1. Name the feature.
2. Describe what is being iterated over (what is the collection or repeating sequence?).
3. Explain what the program does in each iteration.

Write your answer in clear prose (2-3 sentences per feature).

# Integration

**Mathematics connection:** In mathematics, **summation notation** (∑) expresses the idea of adding many values: ∑ from i=1 to n of f(i). This is precisely what a loop implements. The loop variable `i` is the index, the range defines the loop bounds, and the body computes f(i). The entire concept of summation, product (∏), and many other mathematical operations are fundamentally iterative — they define what to do once and specify how many times to do it.

**Philosophy connection:** The ancient Greek philosopher Aristotle distinguished between *acts* (discrete, single events) and *processes* (ongoing, repeated activities). A loop is a formal mechanism for expressing a process: it is not a single act but a repeated act performed until a termination condition is met. Interestingly, many of philosophy's hardest problems (Zeno's paradoxes, for example) involve infinite iteration — what happens when something is repeated without end. In programming, infinite loops are usually bugs, but the philosophical question of "when does a process terminate?" directly maps to the loop termination problem.

*Free question: Can a loop run zero times? What would that mean, and is that useful?*

# Lore Conclusion

Archmage Veylan extinguishes the last of the hundred candles with a single word and replaces them all with the single repetition rune. "One inscription. One hundred fires." He steps back and surveys the room. "You now understand *why* repetition exists in code. In the lessons ahead, you will learn the two great forms of the repetition rune: the `while` loop, which repeats for as long as a condition holds, and the `for` loop, which counts with precision." He gestures to the door. "The age of copying code is over. The age of iteration begins."
