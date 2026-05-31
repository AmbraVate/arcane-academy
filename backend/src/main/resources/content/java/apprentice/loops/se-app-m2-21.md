---
id: se-app-m2-21
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
topicSlug: loops
topicTitle: "Loops"
topicSortOrder: 4
lesson: while_loops
title: "While Loops"
sortOrder: 21
difficulty: 1
estimatedMinutes: 22
xpReward: 50
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-20]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly writes `while (condition) { body }` syntax"
    - "Explains that the condition is checked before each iteration"
    - "Explains that the loop ends when the condition becomes false"
    - "Describes a scenario where the iteration count is unknown in advance (user input, sensor, etc.)"
    - "Identifies at least one guard mechanism to prevent infinite loops"
  keywords: [while, condition, iteration, infinite, guard, update, unknown, before]
  modelAnswer: |
    A `while` loop has the syntax `while (condition) { body }`. Before each iteration, the condition is evaluated. If it is true, the body runs. After the body finishes, the condition is evaluated again. This continues until the condition is false, at which point the loop ends and execution continues after the closing brace.

    The condition is always checked before the body runs. This means if the condition is false from the start, the body never executes — the loop runs zero times.

    While loops are best for situations where the iteration count is not known in advance: reading user input until valid data is entered, processing items from a stream until it is empty, or waiting for a sensor reading to change.

    To prevent infinite loops, the body must contain something that eventually makes the condition false — usually updating a variable that the condition depends on. If a while loop's body never changes the condition, the loop runs forever.
guidedSteps:
  - id: se-app-m2-21-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      ```java
      int count = 0;
      while (count < 3) {
          System.out.println("Count: " + count);
          count++;
      }
      ```
      How many lines does this print?
    inputConfig:
      options:
        - "0 lines"
        - "2 lines"
        - "3 lines"
        - "4 lines"
    markingRule:
      matchMode: EXACT
      accepted: ["3 lines"]
      rejectedFeedback: "count starts at 0. Iteration 1: 0 < 3 is true → prints 'Count: 0' → count becomes 1. Iteration 2: 1 < 3 is true → prints 'Count: 1' → count becomes 2. Iteration 3: 2 < 3 is true → prints 'Count: 2' → count becomes 3. Iteration 4: 3 < 3 is false → loop ends. Three lines are printed."
    hint: "Trace through: what is count before each iteration check?"
    reflectionPrompt: "Tracing a loop by hand — writing the variable values before each iteration — is the most reliable way to understand what a loop does. Practice this until it becomes automatic."

  - id: se-app-m2-21-step2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Write a while loop that prints the numbers 1 to 5. Use a variable `n` starting at 1.
    inputConfig:
      language: java
      starterCode: |
        int n = 1;
        // Write your while loop below
    markingRule:
      matchMode: CONTAINS
      accepted: ["while", "n", "<=", "5", "println"]
      rejectedFeedback: |
        ```java
        int n = 1;
        while (n <= 5) {
            System.out.println(n);
            n++;
        }
        ```
        The condition `n <= 5` keeps the loop running while n is 1 through 5. `n++` increments n each iteration, ensuring the loop eventually ends.
    hint: "The condition should keep the loop running while n is 5 or less. Remember to increment n inside the loop."
    reflectionPrompt: "Every while loop needs three things: a variable the condition depends on, a condition that starts true, and a change inside the loop that eventually makes the condition false."

  - id: se-app-m2-21-step3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Why is the `while` loop well-suited for situations where you do not know in advance how many times to loop? Give a real-world programming example.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["unknown", "condition", "input", "user", "until", "changes", "data"]
      rejectedFeedback: "While loops are driven by a condition, not a count. This is ideal when the number of iterations depends on something that changes at runtime — user input (keep asking until valid), a network stream (keep reading until the stream closes), or a game loop (keep running until the player quits)."
    hint: "Think about a program that reads user input and must keep reading until the user enters the right thing."
    reflectionPrompt: "While loops express 'keep going as long as this is true' rather than 'do this N times'. That makes them natural for condition-driven repetition where N is unknown."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "When is the condition of a `while` loop evaluated?"
    options:
      - "Once, before the loop starts"
      - "After each iteration (at the end of the body)"
      - "Before each iteration (including the first)"
      - "Only when the loop variable changes"
    correctIndex: 2
    feedback: "The condition is evaluated before each iteration, including the very first one. If the condition is false from the start, the body never runs. This distinguishes `while` from `do-while` (which always runs the body at least once, checking the condition after)."

  - type: MULTIPLE_CHOICE
    question: "What must happen inside a while loop body to prevent an infinite loop?"
    options:
      - "The loop must print something each iteration"
      - "The loop body must contain a return statement"
      - "Something must change that will eventually make the condition false"
      - "The loop must call System.exit(0) at some point"
    correctIndex: 2
    feedback: "A while loop continues as long as its condition is true. For the loop to end, the condition must eventually become false. This requires something inside the body to change — usually updating the variable the condition depends on. If nothing changes, the condition stays true forever: infinite loop."

retrieval:
  recall: "Write a while loop that counts down from 10 to 1, printing each number."
  explain: "Explain why a while loop is better than a for loop when reading user input until the user types 'quit'."
  mistakeId:
    code: |
      int x = 1;
      while (x <= 5) {
          System.out.println(x);
      }
    answer: "The loop body never changes `x`. The condition `x <= 5` is always true because x stays at 1 forever. This is an infinite loop — it prints '1' without stopping. The fix is to add `x++` inside the loop body so x eventually exceeds 5 and the condition becomes false."
---

# Hook

Your program needs to keep asking a user for a password until they type the correct one. How many times will the user try? You do not know. It could be once. It could be twenty times. You cannot write twenty `if` statements and hope for the best. You need a loop that runs *for as long as a condition is true*, stopping the moment the condition becomes false. That is the `while` loop — the condition-driven repetition tool.

# Lore Introduction

"The `for` rune counts," Archmage Veylan says, holding two different scrolls side by side, "but the `while` rune persists." He unrolls the second scroll. "While the ward is down, fortify. While the potion is not yet ready, stir. While the student has not answered correctly, keep asking." He traces the symbol. "The while rune does not ask 'how many times?' It asks only one question: 'is it still time to act?' As long as the answer is yes, it acts. The moment the answer is no, it stops."

# Core Learning

## Concept Introduction

A **while loop** repeats its body as long as a condition is `true`:

```java
while (condition) {
    // body — executes repeatedly while condition is true
}
```

**Execution flow:**
1. Evaluate the condition.
2. If `true` → run the body → go back to step 1.
3. If `false` → skip the body, continue after the loop.

**Three required elements:**
| Element | Purpose |
|---------|---------|
| **Condition** | Boolean expression evaluated before each iteration |
| **Body** | Code to execute each iteration |
| **Progress** | Something in the body that eventually makes the condition false |

## Why It Matters

`while` loops are used when you do not know the iteration count in advance. They drive game loops, user-input validation, data stream processing, and event-driven systems. Without them, you cannot write programs that wait for or respond to runtime conditions.

## Worked Examples

**Example 1 — Countdown:**
```java
int countdown = 5;
while (countdown > 0) {
    System.out.println(countdown);
    countdown--;
}
System.out.println("Blast off!");
// Prints: 5, 4, 3, 2, 1, Blast off!
```

**Example 2 — Sum until threshold:**
```java
int total = 0;
int addend = 1;
while (total < 100) {
    total += addend;
    addend++;
}
System.out.println("Total: " + total);
// Keeps adding until total reaches or exceeds 100
```

**Example 3 — Simulated user-input loop:**
```java
// Simulating user input (in real code, use Scanner)
String[] attempts = {"wrong", "wrong", "correct"};
int index = 0;
String input = attempts[index];

while (!input.equals("correct")) {
    System.out.println("Wrong password, try again.");
    index++;
    input = attempts[index];
}
System.out.println("Access granted.");
// Runs twice (two wrong attempts), then exits
```

## Common Mistakes

- **Forgetting to update the condition variable:** If the variable the condition depends on never changes, the loop runs forever.
- **Setting up the condition backwards:** `while (count > 0)` with `count` starting at 0 runs zero times. Make sure the condition starts true.
- **Off-by-one in the condition:** `while (n < 5)` stops at 4; `while (n <= 5)` stops at 5. Know which boundary you want.
- **Updating the variable in the wrong direction:** Incrementing when you should decrement (or vice versa) causes an infinite loop.
- **Not initialising the loop variable before the while:** The condition variable must exist and have a value before the loop starts.

## Mental Model

A `while` loop is like a **guard at a gate**. The guard checks the condition: "Is it safe to proceed?" If yes, they let the action happen and then check again. If no, they stop everyone. Every iteration, the guard checks. As soon as the answer changes to "no", the gate closes. The guard does not count — they simply keep checking the condition until it is false.

## Mini Summary

- `while (condition) { body }` runs the body repeatedly while the condition is true.
- The condition is checked before each iteration, including the first.
- If the condition starts false, the body never runs.
- Something in the body must eventually make the condition false.
- Use `while` when the iteration count is not known in advance.
- The three elements of every safe while loop: condition, body, and progress toward termination.

# Guided Practice Quest

*"The Academy's water clock must drain a vessel one unit at a time until it is empty," Archmage Veylan explains. "Write the while loop that simulates this: start with 10 units of water, subtract 1 per iteration, and print the current level each time. Stop when the vessel is empty."*

# Solo Practice Quest

**The Guess Game**

Write a Java program (you may simulate user input with an array) that:

1. Sets a secret number to 7.
2. Uses a `while` loop that continues until the guess equals the secret number.
3. In each iteration, prints "Too low" if the guess is below 7, "Too high" if above, and stops when equal.
4. Simulates guesses: 3, 9, 6, 7 (use an array and index).

Trace through your loop on paper: write the guess and message for each iteration.

# Integration

**Mathematics connection:** In mathematics, many sequences and series are defined iteratively: a₁ = 1, aₙ = aₙ₋₁ + 2 (odd numbers). Computing the 1,000th term requires performing the recurrence 999 times — a while loop. The Euclidean algorithm for computing the greatest common divisor of two numbers is a classic while loop: `while (b != 0) { temp = b; b = a % b; a = temp; }`. Many fundamental mathematical algorithms are, at their core, while loops.

**Psychology connection:** Psychologists studying habit formation describe behaviour in terms of cue→routine→reward loops: a cue triggers a routine that is repeated until a reward is achieved. This maps directly to a while loop: the cue is the initial condition being true, the routine is the body, and the reward (and termination) is when the condition becomes false. The parallel suggests that human behaviour and computer iteration share a common structural pattern — repeated action until a goal state is reached.

*Free question: Java has a `do-while` loop as well as a plain `while` loop. The difference is that `do-while` checks the condition after the body, not before. Can you think of a situation where `do-while` would be more natural than `while`?*

# Lore Conclusion

The water clock drains smoothly, one unit at a time, until the vessel is empty. The while rune deactivates as the last drop falls. "Perfect termination," Archmage Veylan says, nodding. "The rune asked its question before every drop: 'Is there still water?' And when the answer was no, it stopped — without you having to tell it how many drops the vessel held." He turns to the next lesson. "Now learn the counting rune — the `for` loop. Where `while` persists until a condition changes, `for` counts with mathematical precision. Both are tools; knowing when to use each is the craft."
