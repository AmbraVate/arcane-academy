---
id: se-app-m3-06
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m3
moduleTitle: "Module 3: Functions and Reusability"
moduleGlyph: "⚗️"
moduleSortOrder: 3
topicSlug: methods
topicTitle: "Methods"
topicSortOrder: 1
lesson: refactoring_repetition
title: "Refactoring Repetition"
sortOrder: 6
difficulty: 2
estimatedMinutes: 25
xpReward: 60
practiceType: JAVA
questType: PRACTICE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m3-05]
integrationDomains: [philosophy, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies duplicate code blocks in the before version"
    - "Extracts the repeated logic into a correctly declared method"
    - "Replaces all duplicate occurrences with method calls"
    - "Shows that the before and after produce identical output"
    - "Explains why the refactored version is easier to maintain"
  keywords: [refactor, extract, duplicate, method, DRY, maintain, replace, call]
  modelAnswer: |
    // Before: repeated banner printing
    System.out.println("===");
    System.out.println("Score: " + s1);
    System.out.println("===");
    System.out.println("Score: " + s2);
    System.out.println("===");

    // After: extracted method
    public static void printBanner() {
        System.out.println("===");
    }
    printBanner(); System.out.println("Score: " + s1);
    printBanner(); System.out.println("Score: " + s2);
    printBanner();
guidedSteps:
  - id: gs-m3-06-1
    sortOrder: 1
    inputType: CODE
    instruction: |
      The code below repeats the same two lines three times. Extract those two lines
      into a method called `printSeparator`. Write only the method — do not change the calls yet.
      Repeated block:
        System.out.println("----------");
        System.out.println();
    inputConfig:
      placeholder: |
        public static void printSeparator() {
            // your code here
        }
    markingRule:
      matchMode: CONTAINS
      accepted: ["System.out.println(\"----------\")", "System.out.println()"]
      rejectedFeedback: "Copy the two repeated lines into the method body: System.out.println(\"----------\"); then System.out.println();"
    hint: "Copy the repeated block exactly into the method body."
    reflectionPrompt: "The method body contains exactly what was repeated. The method name describes what that block does."
  - id: gs-m3-06-2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Now replace all three occurrences of the repeated two lines with calls to `printSeparator()`.
      Write the three call lines.
    inputConfig:
      placeholder: |
        // call printSeparator three times
    markingRule:
      matchMode: REGEX
      accepted: ["printSeparator\\s*\\(\\s*\\)\\s*;"]
      rejectedFeedback: "Each call is simply: printSeparator(); — written three times."
    hint: "One call per original duplicate block."
    reflectionPrompt: "Three copies of two lines (6 lines) become three one-line calls. Shorter, clearer, and easier to update."
  - id: gs-m3-06-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In plain English, describe what 'refactoring' means and why it does not change what the program does.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: ["same", "behaviour", "structure", "reorganise", "reorganize", "improve", "change", "output"]
      rejectedFeedback: "Refactoring improves the internal structure of code without changing what it does. The output before and after refactoring is identical."
    hint: "Think about whether the program's output changes after you extract a method."
    reflectionPrompt: "Refactoring is safe because it preserves behaviour. Tests that passed before should still pass after."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is refactoring?"
    options:
      - "Adding new features to a program"
      - "Fixing a bug in the program"
      - "Restructuring code without changing its external behaviour"
      - "Translating code from one language to another"
    correctIndex: 2
    feedback: "Refactoring improves code structure while keeping the program's behaviour identical."
  - type: MULTIPLE_CHOICE
    question: "After you extract repeated code into a method and replace the copies with calls, what changes?"
    options:
      - "The program produces different output"
      - "The program runs slower"
      - "Only the internal structure changes — output stays the same"
      - "The method name must match the variable names"
    correctIndex: 2
    feedback: "Refactoring preserves behaviour. The output is identical; only the structure of the code improves."
retrieval:
  recall: "List the three steps of the 'extract method' refactoring: identify, extract, replace."
  explain: "Why is it important to run your program before and after a refactoring? What are you checking for?"
  mistakeId:
    code: |
      // After 'refactoring', one occurrence was left unchanged:
      public static void printLine() {
          System.out.println("----");
      }

      printLine();
      System.out.println("----"); // forgotten — not replaced
      printLine();
    answer: "The second occurrence was not replaced. The middle line still duplicates the method body. It should be printLine(); — otherwise the refactoring is incomplete and the DRY benefit is lost."
---

# Hook

You have five copies of the same three lines scattered through a program. You find a bug in them. How many places do you fix? Five — and you have to find every single one. Refactoring is the practice of restructuring existing code without changing what it does, so that it is cleaner and easier to maintain. The most common refactoring is extracting duplicated code into a named method. After this lesson, you will see duplicate code and instinctively want to extract it.

# Lore Introduction

An Academy librarian once maintained fifty copies of the same ward inscription across fifty walls. When the Archmage adjusted the ward, the librarian spent a week making the same small change fifty times — and still missed two. Veylan watched this with visible discomfort. "The solution is ancient," he said. "Name the ward. Store it once. Let the walls *reference* the name." The process of consolidating those fifty copies into one named incantation is called refactoring — and it is one of the most valued skills at the Academy.

# Core Learning

## Concept Introduction

**Refactoring** means improving the internal structure of code without changing what the program does. The most important refactoring at this level is **Extract Method**: take a repeated block of code, move it into a named method, and replace every copy with a call to that method.

**The three steps:**

1. **Identify** — find the block that is repeated (or should be named).
2. **Extract** — move the block into a new method with a descriptive name.
3. **Replace** — replace every occurrence of the original block with a call to the new method.

## Why It Matters

Code is read far more often than it is written. Refactoring improves readability, reduces bugs (one place to fix), and makes future changes safer. The before version of code often works — but the after version *communicates* better. Clean code is not just functionally correct; it is clear enough that a new developer (or your future self) can understand it in seconds.

## Worked Examples

**Example 1 — Before and after**

```java
// BEFORE: repeated header block
System.out.println("=== RESULTS ===");
System.out.println("Player 1: " + score1);

System.out.println("=== RESULTS ===");
System.out.println("Player 2: " + score2);

System.out.println("=== RESULTS ===");
System.out.println("Player 3: " + score3);
```

```java
// AFTER: extracted method
public static void printHeader() {
    System.out.println("=== RESULTS ===");
}

printHeader();
System.out.println("Player 1: " + score1);

printHeader();
System.out.println("Player 2: " + score2);

printHeader();
System.out.println("Player 3: " + score3);
```

The output is identical. The structure is cleaner. To change the header, you update one line.

**Example 2 — Parameterised extraction**

Sometimes the repeated block has a slight variation. Extract it with a parameter:

```java
// BEFORE: slight variation in each copy
System.out.println("Player 1 score: " + score1);
System.out.println("Player 2 score: " + score2);
System.out.println("Player 3 score: " + score3);

// AFTER: one parameterised method
public static void printScore(int playerNum, int score) {
    System.out.println("Player " + playerNum + " score: " + score);
}

printScore(1, score1);
printScore(2, score2);
printScore(3, score3);
```

**Example 3 — A before/after that prevents a real bug**

```java
// BEFORE: typo only in the third copy
System.out.println("Welcome to the Academy!");
System.out.println("Welcome to the Academy!");
System.out.println("Welcome to the Acadamy!"); // typo

// AFTER: impossible to have this bug
public static void welcome() {
    System.out.println("Welcome to the Academy!");
}
welcome();
welcome();
welcome();
```

## Common Mistakes

- **Refactoring but forgetting to replace all copies.** If you extract a method but leave one original copy unchanged, you still have duplication.
- **Changing behaviour while refactoring.** Refactoring should be behaviour-preserving. If you change a string while extracting, you have combined two changes — risky.
- **Extracting code that is not actually repeated.** Not every block needs to be a method. Extract when there is genuine repetition or when naming improves clarity.
- **Choosing a bad name for the extracted method.** `doStuff()` defeats the purpose. The name must say what the block does.
- **Not running the program after refactoring.** Always verify the output is unchanged.

## Mental Model

Refactoring is like **updating a dictionary**. Instead of writing "a large, shaggy, four-legged mammal" fifty times in a document, you add "yak" to the dictionary and write "yak" each time. The meaning is preserved. The text is shorter. And if the definition ever needs updating, you change the dictionary once.

## Mini Summary

- Refactoring improves code structure without changing what the program does.
- Extract Method: identify repeated code, move it into a method, replace copies with calls.
- Always run the program before and after to confirm behaviour is unchanged.
- Parameterised methods can absorb repeated-but-slightly-varying code.
- One fix in the extracted method now applies everywhere it is called.
- Good method names are essential — they explain what the block does.

# Guided Practice Quest

Work through each step in order.

**Step 1.** The repeated block is `System.out.println("----------");` and `System.out.println();`. Extract these two lines into a method called `printSeparator`.

**Step 2.** Write the three calls to `printSeparator()` that replace the original three copies of the block.

**Step 3.** In plain English, explain what refactoring means and confirm that the output does not change.

# Solo Practice Quest

Below is the "before" code. Refactor it by extracting the repeated block into a method. Show both the extracted method and the updated code using calls. Your answer must:
1. Give the method a descriptive name.
2. Replace every occurrence.
3. Include a before/after comment.
4. Explain in 2-3 sentences why this change makes the code easier to maintain.

```java
// Before:
System.out.println("[ LEVEL 1 ]");
System.out.println("Difficulty: Easy");
System.out.println();

System.out.println("[ LEVEL 2 ]");
System.out.println("Difficulty: Easy");
System.out.println();

System.out.println("[ LEVEL 3 ]");
System.out.println("Difficulty: Easy");
System.out.println();
```

# Integration

**Philosophy connection — Essence and accident**

Aristotle distinguished between a thing's *essential* properties (what it must be to be what it is) and its *accidental* properties (incidental details that could change). Refactoring reveals the essential structure of code by stripping away accidental repetition. When you extract a method called `printResultHeader()`, you are identifying the essential operation — printing a result header — and separating it from the incidental detail of where it happens to be called. This is a deeply philosophical act: finding the underlying form in the apparent chaos.

**Psychology connection — Recognition over recall**

Cognitive psychology distinguishes between *recognition* (seeing something and knowing it) and *recall* (producing something from memory). Refactored code leverages recognition: when you see `printHeader()`, you instantly recognise what it does from the name. Non-refactored code forces recall: you must re-read all five lines and reconstruct their meaning. Code that favours recognition over recall is faster to read, faster to review, and less likely to introduce errors during maintenance.

**Free question:** A teammate says "I will refactor the code later when the feature is done". What are the risks of delaying refactoring? What might make it harder to refactor once more code is built on top?

# Lore Conclusion

The librarian returned to the fifty inscribed walls, this time with a chisel and a plan. One by one, the hand-written wards were erased and replaced with a small glyph — a reference to the single master ward in the Grand Tome. When Veylan later adjusted the ward, the librarian smiled: one change, fifty walls updated instantly. "That," said Veylan, clapping the librarian on the shoulder, "is the refactored Academy." Outside, not a single wall looked different. Inside, everything had changed.
