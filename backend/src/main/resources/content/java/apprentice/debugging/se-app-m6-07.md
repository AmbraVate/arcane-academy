---
id: se-app-m6-07
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m6
moduleTitle: "Module 6: Debugging and Engineering Habits"
moduleGlyph: "🔧"
moduleSortOrder: 6
topicSlug: debugging
topicTitle: "Debugging"
topicSortOrder: 1
lesson: systematic_troubleshooting
title: "Systematic Troubleshooting"
sortOrder: 7
difficulty: 2
estimatedMinutes: 22
xpReward: 45
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [ide_debuggers]
integrationDomains: [psychology, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly names all four steps of the reproduce-isolate-hypothesise-test cycle"
    - "Explains what a stack trace tells you and how to read it"
    - "Describes rubber duck debugging and explains why it works"
    - "Identifies the most common cause of NullPointerException"
    - "Reflects on how systematic troubleshooting reduces debugging time"
  keywords: [reproduce, isolate, hypothesis, NullPointerException, stack trace, rubber duck, systematic]
  modelAnswer: |
    // Reading a NullPointerException stack trace:
    // Exception in thread "main" java.lang.NullPointerException
    //     at UserService.getEmail(UserService.java:45)
    //     at ProfileBuilder.build(ProfileBuilder.java:23)
    //     at Main.main(Main.java:10)

    // Step 1 - Reproduce: run the code and confirm the error occurs
    // Step 2 - Isolate: the top of the stack trace → UserService.java line 45
    //   String email = user.getEmail(); // 'user' is null here
    // Step 3 - Hypothesise: user was never assigned a non-null value
    // Step 4 - Test: add a null check or trace where user is set
    if (user == null) {
        throw new IllegalArgumentException("User must not be null");
    }
guidedSteps:
  - id: trouble-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You encounter a bug. Putting the steps in order, what is the correct sequence for systematic troubleshooting?
    inputConfig:
      options:
        - "Hypothesise → Reproduce → Test → Isolate"
        - "Reproduce → Isolate → Hypothesise → Test"
        - "Isolate → Reproduce → Test → Hypothesise"
        - "Test → Hypothesise → Isolate → Reproduce"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Reproduce → Isolate → Hypothesise → Test"]
      rejectedFeedback: "The correct order is: Reproduce (confirm you can reliably trigger the bug), Isolate (narrow down where it occurs), Hypothesise (form a specific theory about the cause), Test (verify or disprove your hypothesis). This mirrors the scientific method."
    hint: "You cannot fix a bug you cannot consistently trigger — that comes first."
    reflectionPrompt: "This four-step cycle is not just for coding. Engineers, scientists, and doctors all use this same structure to diagnose problems."

  - id: trouble-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Given this stack trace, which file and line number should you investigate first?
      ```
      Exception in thread "main" java.lang.NullPointerException
          at SpellCaster.cast(SpellCaster.java:34)
          at BattleManager.startBattle(BattleManager.java:12)
          at Main.main(Main.java:5)
      ```
    inputConfig:
      options:
        - "Main.java line 5"
        - "BattleManager.java line 12"
        - "SpellCaster.java line 34"
        - "Any of the three — they are all equally suspect"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["SpellCaster.java line 34"]
      rejectedFeedback: "Read the stack trace from top to bottom. The topmost entry (SpellCaster.java line 34) is where the exception was actually thrown — this is your first investigation point. The other entries show how execution arrived there."
    hint: "The topmost line in the stack trace is where the crash actually happened."
    reflectionPrompt: "Stack traces read top-to-bottom: the top is where the crash happened; the bottom is where execution started. Learn to read them quickly and you will halve your debugging time."

  - id: trouble-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain what "rubber duck debugging" is and why explaining your code out loud (or in writing) to someone — even an inanimate object — helps you find bugs.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [explain, aloud, assumption, forces, discover]
      rejectedFeedback: "Rubber duck debugging is the practice of explaining your code step-by-step to an inanimate object (like a rubber duck). The act of articulating your reasoning forces you to make your assumptions explicit — and often you discover the flaw in your own logic before finishing the explanation."
    hint: "Think about what changes when you have to explain something rather than just think it."
    reflectionPrompt: "The rubber duck is just a tool for forcing you to slow down and state your assumptions explicitly. Many bugs hide in unchecked assumptions."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the most common cause of a NullPointerException in Java?"
    options:
      - "Using the wrong data type"
      - "Attempting to call a method or access a field on a variable that is null"
      - "An array index that is out of bounds"
      - "Integer overflow"
    correctIndex: 1
    feedback: "A NullPointerException occurs when you try to invoke a method or access a field on a reference variable that holds null instead of an object. Always verify that a variable has been properly initialised before using it."
  - type: MULTIPLE_CHOICE
    question: "Which troubleshooting step involves forming a specific theory about *why* the bug exists?"
    options:
      - "Reproduce"
      - "Isolate"
      - "Hypothesise"
      - "Test"
    correctIndex: 2
    feedback: "Hypothesise is where you move from 'I know where it breaks' to 'I think I know why'. A good hypothesis is specific and testable — e.g. 'the user object is null because the database query returns no results when the email is empty'."
retrieval:
  recall: "List the four steps of systematic troubleshooting and give a one-sentence description of each."
  explain: "Why is 'reproducing the bug' the first step — and what risk arises if you skip it and go straight to fixing?"
  mistakeId:
    code: |
      public class OrderProcessor {
          private List<Order> orders;

          public void addOrder(Order o) {
              orders.add(o);
          }
      }
    answer: "The 'orders' field is declared but never initialised — it is null. Calling orders.add(o) will throw a NullPointerException. Fix: initialise the field: private List<Order> orders = new ArrayList<>();"
---

# Hook

A seasoned engineer can look at a crashing program and within minutes have a precise theory about the cause. A beginner stares at the same crash for an hour, making random changes, hoping something works. The difference is not intelligence — it is **method**. The experienced engineer has internalised a systematic process: reproduce the bug, isolate the location, form a hypothesis, test it. Repeat until solved.

This four-step loop is essentially the scientific method applied to code, and it transforms debugging from an anxious scramble into a calm, structured investigation. Add stack-trace reading and rubber-duck technique, and you have a toolkit that will serve you for your entire career.

> Can you recall a time — in any domain, not just coding — when you solved a problem by systematically narrowing down the cause rather than guessing? What made that approach effective?

# Lore Introduction

The Arcane Academy's Diagnostics Guild teaches a sacred method passed down from the first Enchanters: the **Four Rites of Fault Isolation**. When a spell misfires, a Guild member does not panic and start randomly swapping components. They follow the Rites: first, cast the spell again to confirm the misfire. Second, trace which rune in the sequence flared with the wrong colour. Third, form a theory about the flaw in that rune's inscription. Fourth, test the theory by correcting only that one rune and casting again.

Centuries of Guild practice have refined this method because it works. The Four Rites are your debugging process in disguise.

# Core Learning

## Concept Introduction

**Systematic troubleshooting** follows four steps:

1. **Reproduce** — Can you make the bug happen reliably? If not, you cannot verify your fix.
2. **Isolate** — Where exactly does the bug occur? Use the stack trace, binary search, or divide-and-conquer to narrow the location.
3. **Hypothesise** — Form a *specific, testable* theory. Not "something is wrong with the database" but "the query returns null when the email field is empty".
4. **Test** — Make the minimal change to verify or disprove your hypothesis. If disproved, form a new hypothesis.

### Reading a Stack Trace

```
Exception in thread "main" java.lang.NullPointerException
    at UserService.getEmail(UserService.java:45)   ← where it crashed
    at ProfileBuilder.build(ProfileBuilder.java:23) ← called UserService
    at Main.main(Main.java:10)                      ← called ProfileBuilder
```

Read from **top to bottom**. The top line is where the exception was thrown — start your investigation there.

### Rubber Duck Debugging

Explain your code out loud, line by line, to an imaginary listener (traditionally a rubber duck). The act of articulation forces you to make your assumptions explicit — and the flaw often reveals itself mid-explanation.

## Why It Matters

Random changes waste time and can introduce new bugs. The systematic approach ensures each action either solves the problem or gives you new information. Over a career, this compounds enormously: developers who debug systematically are measurably faster and produce fewer regressions.

## Worked Examples

**Example 1 — NullPointerException investigation**
```java
// Stack trace says: NullPointerException at UserService.java:45
// Line 45:
String email = user.getEmail(); // 'user' must be null

// Hypothesis: user is null because findById() returns null for unknown IDs
// Test: add a check
User user = userRepository.findById(id);
if (user == null) {
    throw new IllegalArgumentException("User not found for id: " + id);
}
String email = user.getEmail(); // safe now
```

**Example 2 — Binary search isolation**
```java
// A 20-step process produces wrong output
// Instead of checking each step, split in half:
// Step 10 — print intermediate state
// If correct at step 10 → bug is in steps 11-20
// If wrong at step 10 → bug is in steps 1-10
// Repeat: cuts search space in half each time
```

**Example 3 — Rubber duck debugging in action**
```java
// Developer says aloud:
// "I create a User... then I call setName()... wait,
// I'm calling setName on 'newUser' but I never assigned newUser —
// I just declared it as null. THAT'S the bug."
User newUser = null;         // declared but not initialised
newUser.setName("Alice");    // NPE here — caught by rubber duck
// Fix:
User newUser = new User();
newUser.setName("Alice");
```

## Common Mistakes

- **Fixing without reproducing** — if you cannot reliably trigger the bug, you cannot verify your fix worked.
- **Reading the stack trace from the bottom** — always start at the top, where the exception actually occurred.
- **Changing multiple things at once** — if you change three things and the bug disappears, you do not know which change fixed it (or if they are masking each other).
- **Skipping the hypothesis step** — jumping from "I found the location" to "I'll change things randomly" is not systematic; form a clear theory first.
- **Assuming the error message is the bug** — the message tells you the *symptom*; the real cause is often several steps earlier in the stack trace.

## Mental Model

Think of debugging as a funnel. At the top you have the entire program; at each step you pour away everything that is not the problem, narrowing toward the one defective line. The four-step process is the structure of the funnel — each step reduces the search space by eliminating possibilities rather than adding guesses.

## Mini Summary

✔ The four debugging steps are: Reproduce → Isolate → Hypothesise → Test.
✔ Stack traces read top-to-bottom: the topmost entry is where the exception was thrown.
✔ NullPointerException means you called a method or accessed a field on a null reference.
✔ Rubber duck debugging externalises your assumptions — often revealing flaws mid-explanation.
✔ Change one thing at a time so you can know what actually fixed (or broke) the problem.

# Guided Practice Quest

**The Four Rites of Fault Isolation**
A spell registry is crashing with a NullPointerException. Apply the systematic troubleshooting method to diagnose and describe the fix.
Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Find (or write) a small Java program with at least one deliberate bug — for example: an uninitialised list, a null reference, or an off-by-one error. Write a debugging narrative in 200-250 words that walks through all four steps (Reproduce, Isolate, Hypothesise, Test) for finding and fixing your chosen bug. Include the buggy code snippet and the fixed version. Reflect: at which step do most developers waste the most time, and why?

# Integration

**Connecting to Psychology — Cognitive Bias in Debugging**
Systematic troubleshooting directly counteracts some of the most well-documented cognitive biases. **Confirmation bias** — the tendency to seek evidence that confirms what we already believe — leads developers to jump to a hypothesis before properly isolating the problem, causing them to "confirm" a wrong theory while ignoring contradictory evidence.

**Anchoring bias** leads developers to fixate on the first line of a stack trace that looks familiar, ignoring the true cause further up the chain. The systematic method's insistence on reproducing and isolating before hypothesising is a procedural guardrail against these biases. By forcing you to gather evidence first, it keeps your hypothesis grounded in observation rather than intuition.

> Can you recall a situation — in life or in coding — where you were so convinced of a particular explanation that you ignored evidence pointing elsewhere? What would a more systematic approach have looked like?

# Lore Conclusion

The apprentice completes the fourth Rite — the test — and the misfiring spell blazes correctly for the first time. The flaw was a single misdrawn loop in the third rune, which caused every downstream symbol to misinterpret the energy flowing through it. Without the systematic isolation process, they might have redrawn every rune in the sequence, wasting hours and potentially introducing new errors.

The Guild master nods approvingly. "You did not fix the noise; you found the source. That is the difference between a dabbler and an engineer." In the next lessons, you will discover that the same disciplined thinking extends beyond debugging — into how you name, format, and structure your code from the very beginning.

---
