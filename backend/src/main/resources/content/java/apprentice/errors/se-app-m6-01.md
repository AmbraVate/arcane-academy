---
id: se-app-m6-01
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m6
moduleTitle: "Module 6: Debugging & Engineering Habits"
moduleGlyph: "🔬"
moduleSortOrder: 6
topicSlug: errors
topicTitle: "Errors"
topicSortOrder: 1
lesson: syntax_errors
title: "Syntax Errors"
sortOrder: 1
difficulty: 1
estimatedMinutes: 20
xpReward: 40
practiceType: JAVA
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly defines a syntax error as a violation of Java's grammar rules"
    - "States that syntax errors are caught by the compiler before the program runs"
    - "Identifies at least three common examples of syntax errors"
    - "Explains how to read a compiler error message to locate and fix the error"
    - "Distinguishes syntax errors from runtime and logical errors"
  keywords: [syntax, compiler, grammar, semicolon, bracket, compile-time, error message, line number]
  modelAnswer: |
    A syntax error is a violation of Java's grammar rules — missing semicolons,
    mismatched braces, or misspelled keywords. The compiler catches syntax errors
    before the program runs and reports the file name, line number, and a description.
    To fix one: read the message, go to the reported line, look for the grammar violation,
    and correct it. Syntax errors are the easiest type to fix because the compiler
    tells you exactly where the problem is.

guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "This code has one syntax error. Find and fix it."
    inputConfig:
      language: java
      starterCode: "public class Hello {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\")\n    }\n}\n"
      expectedPattern: "System\\.out\\.println\\(\"Hello, World!\"\\);"
    markingRule: REGEX_MATCH
    hint: "Every Java statement must end with a semicolon."
    reflectionPrompt: "Why does Java require a semicolon at the end of each statement?"

  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "This code has a mismatched brace. Fix it."
    inputConfig:
      language: java
      starterCode: "public class Greet {\n    public static void main(String[] args) {\n        System.out.println(\"Hi!\");\n    \n}\n"
      expectedPattern: "\\}\\s*\\}"
    markingRule: REGEX_MATCH
    hint: "Count the opening { and closing } braces. They must match."
    reflectionPrompt: "How does indentation help you spot mismatched braces?"

  - id: step-3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: "A compiler error says: 'error: ';' expected — Line 7'. What should you do first?"
    inputConfig:
      options:
        - "Delete line 7 and try again"
        - "Restart the IDE"
        - "Go to line 7 in the file and look for a missing semicolon"
        - "Add a semicolon at the very end of the file"
      correctIndex: 2
    markingRule: EXACT_MATCH
    hint: "The compiler tells you the line number — that is the first place to look."
    reflectionPrompt: "Is the error always exactly on the line the compiler reports, or could it be on the line above?"

microCheckpoint:
  - question: "When does the Java compiler catch syntax errors?"
    options:
      - "While the program is running"
      - "After the program finishes"
      - "Before the program runs, during compilation"
      - "Only when you press a debug button"
    correctIndex: 2
    feedback: "Correct — syntax errors are compile-time errors. The program never starts running because the compiler refuses to produce executable code."

  - question: "Which of these is a syntax error?"
    options:
      - "Dividing a number by zero"
      - "A variable that holds the wrong value"
      - "Missing a closing curly brace }"
      - "A loop that runs too many times"
    correctIndex: 2
    feedback: "Yes — a missing closing brace is a grammar violation that the compiler catches before running."

retrieval:
  recall: "At what point in the development process are syntax errors caught, and why?"
  explain: "Describe how you would use a compiler error message to locate and fix a missing semicolon."
  mistakeId:
    code: |
      public class Counter {
          public static void main(String[] args) {
              int count = 0
              count = count + 1;
              System.out.println(count)
          }
      }
    answer: "Two syntax errors: missing semicolons after 'int count = 0' (line 3) and 'System.out.println(count)' (line 5). Add a semicolon at the end of each statement."
---

# Hook

You write what you think is a perfect spell. You cast it. Nothing happens — instead, a wall of red text appears. The compiler has found a rune corruption. Your first instinct is panic. Your second instinct — once you have been through it a few times — is relief. Syntax errors are the *friendliest* kind of bug. The compiler found them before any damage was done, and it is telling you almost exactly where to look. Every developer sees hundreds of them. The skill is learning to read the message and fix the problem in seconds.

# Lore Introduction

In the Academy's Inscription Hall, every Blueprint must pass the Verification Reader before it can be used to summon constructs. The Verification Reader is merciless but fair: it scans each rune against the Codex of Valid Grammar and halts at the first violation. A missing closing ward, a forgotten rune-terminator, a misread keyword — the Reader will not proceed. It reports the position of the violation and waits. The apprentice who learns to read these reports quickly wastes no time; the one who panics spends an hour on a one-character fix.

# Core Learning

## Concept Introduction

A **syntax error** is a violation of Java's grammar rules. Java, like any language, has precise rules about how code must be written. When you break one of those rules, the compiler refuses to translate your code into a runnable program.

Syntax errors are **compile-time errors** — they are caught *before* the program runs. This is good news: the program never launches in a broken state.

**Common syntax errors:**
- Missing semicolon at the end of a statement
- Mismatched curly braces `{}`
- Mismatched parentheses `()`
- Misspelled keyword (e.g., `Sytem.out.println` instead of `System.out.println`)
- Missing closing quotation mark
- Wrong capitalisation of a keyword (`Class` instead of `class`)

## Why It Matters

Syntax errors are the first obstacle every beginner hits, and the first skill to master is not avoiding them entirely — it is fixing them quickly. The compiler message contains a line number and a description. Reading that message instead of guessing is always faster.

## Worked Examples

**Error: missing semicolon**
```java
// BROKEN
int x = 5
System.out.println(x);

// Compiler says: error: ';' expected at line 1
// FIX:
int x = 5;
System.out.println(x);
```

**Error: mismatched braces**
```java
// BROKEN
public class Example {
    public static void main(String[] args) {
        System.out.println("Hello");
    
// Compiler says: error: reached end of file while parsing
// FIX: add the missing closing brace }
    }
}
```

**Error: misspelled method**
```java
// BROKEN
Sytem.out.println("Hello");

// Compiler says: error: cannot find symbol — Sytem
// FIX:
System.out.println("Hello");
```

**Reading compiler error messages — the anatomy:**
```
Main.java:7: error: ';' expected
        int count = 0
                      ^
```
- `Main.java` — which file
- `7` — which line
- `error: ';' expected` — what the problem is
- `^` — points to the approximate position

## Common Mistakes

- **Ignoring the line number**: Most developers instinctively scan the whole file looking for the error. Just go to the line number first.
- **Trusting the exact position**: Sometimes the compiler reports one line past the actual error — if the reported line looks fine, check the line above it.
- **Fixing random things**: Do not start randomly changing code. Read the message, go to the line, fix only the grammar violation described.

## Mental Model

Think of the compiler as a **very strict grammar checker** for a foreign language exam. If you write a sentence without a full stop, it marks the entire answer wrong and points at the missing punctuation. It does not guess what you meant — it just tells you what rule you broke and where. Your job is to trust the red pen and fix exactly what it is pointing at.

## Mini Summary

- ✔ Syntax errors break Java's grammar rules and are caught at compile time.
- ✔ They are the *easiest* errors to fix because the compiler tells you where they are.
- ✔ Common causes: missing semicolons, mismatched braces/parentheses, misspelled symbols.
- ✔ Read the compiler message: file, line number, error description, position marker.
- ✔ Go to the reported line first — do not guess.

# Guided Practice Quest

Work through the sidebar steps to find and fix a missing semicolon, a mismatched brace, and then practise using a compiler error message to locate a problem.

# Solo Practice Quest

**Spell: Read the Error Runes**

Here is a broken piece of Java code. Without running it, identify ALL the syntax errors:

```java
public class BrokenSpell {
    public static void main(String[] args) {
        int power = 100
        String name = "Arcane"
        System.out.println(name + " power: " + power;
        if (power > 50) {
            System.out.println("Powerful spell!");
        
    }
}
```

For each error:
1. State the line number (or approximate position).
2. Name the type of syntax error.
3. Write the corrected version of the affected line.

# Integration

**Mathematics connection — formal grammar**

In formal language theory, a grammar is a set of rules that defines which strings of symbols are valid in a language. Mathematical proofs must follow grammatical rules too — a proof that violates structural rules (e.g., missing a quantifier, using undefined notation) is rejected regardless of whether the underlying idea is correct. Java's syntax rules are exactly this: a formal grammar that every program must satisfy before meaning can be extracted from it. The compiler is the grammar checker.

**Philosophy connection — the analytic/synthetic distinction**

Philosopher Immanuel Kant distinguished *analytic* truths (true by definition, like "a bachelor is unmarried") from *synthetic* truths (true by observation, like "the sky is blue"). Syntax errors are analytic — they are wrong by definition, without needing to run the program to find out. Logic errors are synthetic — they require running the program and observing the wrong result. Understanding which type of error you are dealing with tells you immediately whether the compiler will find it or whether you must run the program yourself.

**Question:** Explain in your own words why a syntax error is considered a "compile-time" error rather than a "runtime" error, and what the practical benefit of catching errors at compile time is compared to catching them at runtime.

# Lore Conclusion

The Verification Reader is your ally, not your enemy. Its red reports are a gift: clear, precise, and fixable in moments if you learn to read them. In the next lesson you will meet a more dangerous category of rune corruption — one that slips past the Verification Reader entirely and strikes only when the spell is cast in anger: the Runtime Error.
