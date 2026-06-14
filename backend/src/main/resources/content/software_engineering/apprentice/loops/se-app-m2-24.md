---
id: se-app-m2-24
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
lesson: infinite_loops
title: "Infinite Loops"
sortOrder: 24
difficulty: 2
estimatedMinutes: 20
xpReward: 40
practiceType: JAVA
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-23]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what causes an infinite loop — condition that never becomes false"
    - "Identifies at least two common causes of accidental infinite loops"
    - "Explains `while(true)` with `break` as a legitimate pattern"
    - "Describes how to recognise an infinite loop at runtime (program hangs, CPU usage)"
    - "Explains how to terminate a running program (Ctrl+C in terminal, stop in IDE)"
  keywords: [infinite, condition, break, while, true, hang, terminate, accidental, intentional]
  modelAnswer: |
    An infinite loop occurs when a loop's condition never becomes false. The body keeps executing indefinitely because nothing makes the condition change.

    Common causes: (1) Forgetting to update the loop variable (e.g., `while (x < 10)` with no `x++`). (2) Updating the variable in the wrong direction (decrementing when incrementing is needed). (3) Using the wrong condition that can never be false (e.g., `while (x != -1)` when x only ever increases from 0).

    `while (true)` with a `break` inside is a legitimate and common pattern for event loops, game loops, and menu-driven programs: the loop runs forever until an explicit `break` exits it. This is intentional — the exit condition is handled by `break` logic, not by the while condition itself.

    Signs of an infinite loop at runtime: the program stops producing output but does not terminate, CPU usage spikes to 100% for the Java process, the terminal appears frozen.

    To stop it: press Ctrl+C in the terminal, or click the stop button in your IDE.
guidedSteps:
  - id: se-app-m2-24-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of the following loops is infinite?
      ```java
      // A
      int x = 0;
      while (x < 10) { x += 2; }

      // B
      int y = 10;
      while (y > 0) { y--; }

      // C
      int z = 1;
      while (z != 0) { z++; }
      ```
    inputConfig:
      options:
        - "Only A"
        - "Only B"
        - "Only C"
        - "Both A and C"
    markingRule:
      matchMode: EXACT
      accepted: ["Only C"]
      rejectedFeedback: "A: z starts at 0, increments by 2 → reaches 10 → condition false. Not infinite. B: y starts at 10, decrements → reaches 0 → condition false. Not infinite. C: z starts at 1, increments → z is 1, 2, 3, ... and will never equal 0 again. The condition `z != 0` is never false. Infinite loop."
    hint: "Will z ever equal 0 again if it starts at 1 and keeps incrementing?"
    reflectionPrompt: "Infinite loops often hide in conditions that can never be satisfied. `z != 0` after z starts at 1 and only increments is a classic trap — the condition is never false because the variable moves away from the target value."

  - id: se-app-m2-24-step2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Explain one legitimate use case for `while (true)` with a `break` statement inside. Why is this pattern sometimes better than putting the exit condition directly in the while's condition?
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["menu", "game", "input", "event", "break", "exit", "until"]
      rejectedFeedback: "A game loop (`while (true) { update(); render(); if (quit) break; }`) is a classic example. The exit condition (user pressing quit) is checked inside the loop after doing work. Using `while (!quit)` would require `quit` to be checked before doing any work — sometimes you want to do work first, then decide whether to continue. `while(true)` with `break` makes the exit point explicit and flexible."
    hint: "Think about a game that runs continuously until the player presses Quit — or a menu that keeps showing until the user selects Exit."
    reflectionPrompt: "`while(true)` is not inherently bad. It is intentional — the programmer explicitly decides where the exit point is. Accidental infinite loops (no exit possible) are bugs; intentional `while(true)` with clear `break` logic is design."

  - id: se-app-m2-24-step3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      Your Java program runs but produces no output and the terminal is frozen. You suspect an infinite loop. What should you do first?
    inputConfig:
      options:
        - "Reboot your computer"
        - "Wait — it will probably finish eventually"
        - "Press Ctrl+C in the terminal (or the stop button in your IDE) to terminate the program"
        - "Add more print statements and rerun"
    markingRule:
      matchMode: EXACT
      accepted: ["Press Ctrl+C in the terminal (or the stop button in your IDE) to terminate the program"]
      rejectedFeedback: "Ctrl+C sends an interrupt signal to the running process, stopping it immediately. In most IDEs, the red stop button achieves the same result. Rebooting is extreme. Waiting is pointless — an infinite loop will never finish. Adding print statements requires stopping the program first anyway."
    hint: "You need to stop the running process. What key combination terminates a process in most terminals?"
    reflectionPrompt: "Recognising an infinite loop and knowing how to stop it are two essential debugging skills. After stopping, look for the loop whose condition never changes."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the MOST common cause of an accidental infinite loop?"
    options:
      - "Using a for loop instead of a while loop"
      - "The loop body never changes the variable the condition depends on"
      - "The loop starts with a false condition"
      - "Using `int` instead of `long` for the counter"
    correctIndex: 1
    feedback: "An accidental infinite loop almost always occurs because the body never changes the condition variable. For example, `while (count < 10) { System.out.println(count); }` — count is never incremented, so the condition stays true forever. Always ensure the loop body makes progress toward the termination condition."

  - type: MULTIPLE_CHOICE
    question: "Is `while (true) { if (done) break; doWork(); }` considered bad practice?"
    options:
      - "Yes — `while(true)` should never be used"
      - "No — it is a legitimate pattern when the exit condition is complex or checked mid-loop"
      - "Yes — `break` is only for switch statements"
      - "No — but only if the loop body is empty"
    correctIndex: 1
    feedback: "`while(true)` with an explicit `break` is a well-established pattern, particularly for event loops, game loops, and menu systems. It is not bad practice — it is intentional. The key is that the `break` is always reachable, ensuring the loop will eventually terminate."

retrieval:
  recall: "Write a while loop that would run infinitely. Then write the fixed version."
  explain: "What is the difference between an accidental infinite loop and an intentional `while(true)` loop? How can you tell them apart?"
  mistakeId:
    code: |
      int count = 10;
      while (count > 0) {
          System.out.println("Count: " + count);
          count++;
      }
    answer: "`count` starts at 10 and increments with `count++`. The condition is `count > 0`. Since count starts positive and only grows, the condition is always true — infinite loop. The fix depends on intent: if counting down, use `count--` instead of `count++`. If counting up to a limit, change the condition to `count < someLimit`."
---

# Hook

A loop that never ends. Your program freezes. The cursor blinks. CPU fans spin up. Nothing happens. This is the infinite loop — the most immediately obvious bug in programming. Sometimes it is an accident (you forgot to update the counter). Sometimes it is intentional (`while(true)` is a real design pattern). Knowing the difference, knowing the causes, and knowing how to stop a runaway program are essential survival skills for every programmer.

# Lore Introduction

"The repetition rune," Archmage Veylan says solemnly, "has a failure mode unlike any other." He gestures to a candle that flickers endlessly, its flame never going out, never consuming the wax. "A spell that runs without end. The caster grows exhausted. The wick never shortens. The room fills with smoke." He snuffs it forcefully. "This is what happens when your repetition rune has no termination — or when the termination was written incorrectly." He holds up the extinguisher. "Today you will learn to recognise the endless rune, avoid writing it by accident, and use it intentionally when that is the actual design."

# Core Learning

## Concept Introduction

An **infinite loop** is a loop whose condition never becomes false, causing it to run forever.

**What causes infinite loops:**
1. Forgetting to update the condition variable.
2. Updating the variable in the wrong direction.
3. A condition that can never be false for the given values.
4. Modifying the wrong variable (typo in variable name).

**Recognising an infinite loop at runtime:**
- Program does not terminate
- No output (or same output repeating forever)
- CPU usage spikes for the Java process
- Terminal appears frozen

**How to stop:** Ctrl+C in the terminal, or the red stop button in your IDE.

**Intentional infinite loops — `while(true)` with `break`:**
```java
while (true) {
    // do work
    if (exitCondition) {
        break; // exits the loop
    }
}
```
This is a valid and common pattern for event loops, game loops, and interactive menus.

## Why It Matters

Infinite loops are among the most common beginner bugs. More importantly, recognising them and understanding `while(true)` as an intentional pattern (not just a bug) builds your mental model of how loops work at a deeper level.

## Worked Examples

**Example 1 — Accidental infinite loop (missing update):**
```java
int count = 0;
while (count < 5) {
    System.out.println(count);
    // BUG: count never changes — loop runs forever
}
// Fix: add count++; inside the loop body
```

**Example 2 — Accidental infinite loop (wrong direction):**
```java
int x = 1;
while (x < 100) {
    System.out.println(x);
    x--;  // BUG: decrementing moves x away from 100, toward 0 and below
    // x goes 1, 0, -1, -2, ... never reaches 100
}
// Fix: change x-- to x++
```

**Example 3 — Intentional `while(true)` for a simple menu:**
```java
boolean running = true;
int iteration = 0;
while (true) {
    iteration++;
    System.out.println("Iteration: " + iteration);
    if (iteration >= 3) {
        System.out.println("Exiting.");
        break; // intentional exit
    }
}
// Prints iteration 1, 2, 3 then exits
```

## Common Mistakes

- **Not testing the termination condition before running:** Trace your loop on paper first. Will it ever terminate?
- **Using `while(true)` without any reachable `break`:** An intentional `while(true)` is only valid if `break` is definitely reachable.
- **Confusing the wrong variable:** Updating `count` when the condition checks `total` keeps the condition unchanged.
- **Thinking a large number means not infinite:** `while (x < 1000000)` with no update is just as infinite as `while (x < 10)`.
- **Panicking instead of pressing Ctrl+C:** A frozen program is always stoppable. Stop it, then debug calmly.

## Mental Model

An infinite loop is like a **broken treadmill that cannot be turned off**. The person on it keeps walking (the loop body runs) but the termination button is broken (condition never changes). The solution is either to fix the button (correct the condition), or to install an emergency stop (a `break` statement) that can be triggered from inside. `while(true)` is a treadmill where the off button is explicitly the emergency stop — and that is fine, as long as the emergency stop is definitely reachable.

## Mini Summary

- An infinite loop occurs when the condition is never false — the loop runs forever.
- Most accidental infinite loops are caused by failing to update the condition variable.
- To stop a running infinite loop: Ctrl+C in terminal or the IDE stop button.
- `while (true)` with a `break` is a legitimate pattern — not all infinite-looking loops are bugs.
- Always trace your loop before running it: will the condition eventually become false?
- An intentional `while(true)` must have a reachable `break` statement.

# Guided Practice Quest

*"Here are three loops written by junior apprentices," Archmage Veylan says, presenting three scrolls. "Two are broken — infinite. One is intentional. Your task: identify which are accidental, explain why each causes an infinite loop, and propose the fix. For the intentional one, explain why it is valid."*

# Solo Practice Quest

**The Loop Diagnosis Lab**

Examine each loop below and classify it as: (a) finite and correct, (b) accidental infinite loop, or (c) intentional `while(true)` pattern.

```java
// Loop 1
int n = 100;
while (n != 1) {
    if (n % 2 == 0) n = n / 2;
    else n = n * 3 + 1;
}

// Loop 2
int i = 5;
while (i > 0) {
    System.out.println(i);
}

// Loop 3
while (true) {
    String command = "quit"; // simulated input
    if (command.equals("quit")) break;
}
```

For Loop 1: Is it finite? (This is the Collatz conjecture — no one has proven it terminates for all inputs, but it does for 100.) For Loop 2: What is wrong? For Loop 3: Is the `break` reachable?

# Integration

**Mathematics connection:** The **halting problem** — proved unsolvable by Alan Turing in 1936 — asks: can a program determine, for any arbitrary program and input, whether that program will eventually halt? Turing proved this is impossible in general. Infinite loops are a concrete manifestation of the halting problem. For the specific loops you write, you can usually determine termination by tracing. But in general, there is no algorithm that can tell whether any program halts.

**Philosophy connection:** In Zeno's paradox of Achilles and the Tortoise, Achilles must always cover half the remaining distance, producing an infinite sequence of steps — yet the race finishes in finite time. This is analogous to a loop that runs a very large number of times: the condition makes it appear it should run forever, but careful analysis shows it terminates. The philosophical lesson: apparent infinity does not always mean actual infinity. Verify termination analytically, not by intuition.

*Free question: What is a "do-while" loop, and how does its termination differ from a regular while loop? Write a pseudocode example where do-while is more natural than while.*

# Lore Conclusion

The endless candle is extinguished, and the smoke clears. "Three tools," Archmage Veylan says. "Recognise the endless rune. Fix the accidental one. Use the intentional one wisely." He gestures to the next scroll. "You have learned loops from concept to infinite — now learn the two precise control words that change a loop's flow in the moment: `break` and `continue`. One exits. One skips. Together they give you fine-grained control over iteration itself."
