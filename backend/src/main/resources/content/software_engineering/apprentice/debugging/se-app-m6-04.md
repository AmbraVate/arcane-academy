---
id: se-app-m6-04
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m6
moduleTitle: "Module 6: Debugging and Engineering Habits"
moduleGlyph: "🔬"
moduleSortOrder: 6
topicSlug: debugging
topicTitle: "Debugging"
topicSortOrder: 2
lesson: reading_error_messages
title: "Reading Error Messages"
sortOrder: 4
difficulty: 2
estimatedMinutes: 22
xpReward: 50
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m6-01, se-app-m6-02]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies the exception type from the first line of the error message"
    - "Extracts the human-readable message describing the specific cause"
    - "Finds the file name and line number from the stack trace"
    - "Locates the correct entry point in the stack trace (first line referencing own code)"
    - "Proposes a fix based solely on reading the error message"
  keywords: [exception type, message, stack trace, line number, file, at, caused by, first line]
  modelAnswer: |
    Error message anatomy:
    Line 1: Exception type and message (e.g. java.lang.NullPointerException: name is null)
    Following lines: the stack trace — each 'at' line shows class, method, file, and line number.
    The top 'at' line is the most immediate location; find the first line referencing your own
    code to pinpoint the bug. Use the exception type + message to understand *what* went wrong,
    and the line number to find *where* to fix it.

guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_ANSWER
    instruction: "Read this error. State: (1) exception type, (2) message, (3) file and line to investigate.\n\nException in thread 'main' java.lang.ArrayIndexOutOfBoundsException: Index 4 out of bounds for length 3\n\tat SpellList.getCast(SpellList.java:15)\n\tat Main.main(Main.java:6)"
    inputConfig:
      placeholder: "Exception type: ...\nMessage: ...\nFile/line: ..."
    markingRule: KEYWORD_MATCH
    hint: "Exception type is before the colon on the first line. Message is after the colon. The 'at' lines give file and line number."
    reflectionPrompt: "Why would you look at SpellList.java:15 rather than Main.java:6 first?"

  - id: step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "A stack trace shows 10 lines of 'at' entries. Most reference java.util and java.lang libraries. One line says 'at MyShop.calculateTotal(MyShop.java:22)'. Which line should you investigate first?"
    inputConfig:
      options:
        - "The very first 'at' line, regardless of which class it is"
        - "The very last 'at' line"
        - "MyShop.java:22 — the first entry that references your own code"
        - "All of them equally"
      correctIndex: 2
    markingRule: EXACT_MATCH
    hint: "Library code (java.util, java.lang) rarely has bugs. Look for your own class names first."
    reflectionPrompt: "Why does library code appear in stack traces at all?"

  - id: step-3
    sortOrder: 3
    inputType: SHORT_ANSWER
    instruction: "Based on this error message alone, describe exactly what you would check on the affected line:\n\njava.lang.NullPointerException: Cannot invoke 'String.toUpperCase()' because 'playerName' is null"
    inputConfig:
      placeholder: "I would check..."
    markingRule: KEYWORD_MATCH
    hint: "The message tells you exactly which variable is null and what you tried to call on it."
    reflectionPrompt: "How is this message more helpful than just 'NullPointerException'?"

microCheckpoint:
  - question: "In a Java error message, where do you find the exception type?"
    options:
      - "At the very bottom of the stack trace"
      - "In the middle of the error output"
      - "On the first line, before the colon"
      - "Only in the IDE — not in console output"
    correctIndex: 2
    feedback: "Correct — the exception type (e.g., java.lang.NullPointerException) is on the first line, before the colon and the message."

  - question: "What does each 'at' line in a stack trace tell you?"
    options:
      - "The value of every variable at that point"
      - "The class name, method name, file name, and line number of one step in the call chain"
      - "A list of all methods in the class"
      - "The time the error occurred"
    correctIndex: 1
    feedback: "Yes — each 'at' line shows one frame: which class and method was executing, in which file, on which line."

retrieval:
  recall: "List the four key pieces of information you extract from a Java error message."
  explain: "Explain the strategy for finding your own code in a long stack trace that includes many library class entries."
  mistakeId:
    code: |
      // Student sees this error and starts randomly changing code:
      // java.lang.NumberFormatException: For input string: "abc"
      //     at Integer.parseInt(Integer.java:668)
      //     at OrderProcessor.processOrder(OrderProcessor.java:14)
    answer: "The student should stop guessing. The message says exactly what happened: parseInt received 'abc' which is not a valid integer. The fix is at OrderProcessor.java:14 — check where the 'abc' string came from and ensure only numeric strings are passed to parseInt."
---

# Hook

A locked-room mystery. Your program crashed. Red text fills the screen. A lesser detective waves their hands and starts changing code at random — sometimes fixing the bug, more often breaking something else. A skilled detective does something different first: they *read*. Error messages are not random noise — they are a confession. The exception tells you what went wrong. The message tells you the specific circumstances. The stack trace gives you the location. All the information you need is already there. You just have to learn to read it.

# Lore Introduction

The Academy's Forensic Analysis Guild teaches a single rule above all others: before touching a damaged construct, read its Incident Report. Every Runtime Corruption generates an Incident Report automatically — the exception type identifies the category of failure; the message describes the specific instance; the invocation cascade (stack trace) traces the path through every active spell that led to the moment of collapse. The Guild's master forensics officer, Tova Rashenn, famously spent thirty seconds reading an Incident Report and fixed a three-day-old mystery bug in four minutes. Read first. Touch second.

# Core Learning

## Concept Introduction

A Java error message has four key parts:

```
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "String.length()" because "name" is null
    at UserService.getNameLength(UserService.java:18)
    at Main.main(Main.java:5)
```

| Part | Location | Content |
|------|----------|---------|
| Thread | Start of first line | Which thread crashed (usually "main") |
| Exception type | First line, before `:` | What kind of error (e.g. NullPointerException) |
| Message | First line, after `:` | What specifically went wrong |
| Stack trace | `at` lines | Chain of calls with file names and line numbers |

## Why It Matters

Developers who read error messages carefully solve bugs in minutes. Developers who ignore them and start guessing can spend hours. The error message is a direct description of the problem — there is no interpretation needed beyond going to the correct line of code.

## Worked Examples

**Full error analysis:**
```
Exception in thread "main" java.lang.ArithmeticException: / by zero
    at Calculator.divide(Calculator.java:12)
    at OrderSystem.applyDiscount(OrderSystem.java:44)
    at Main.main(Main.java:7)
```

Step-by-step reading:
1. **Exception type**: `ArithmeticException` — an illegal arithmetic operation.
2. **Message**: `/ by zero` — specifically, a division where the divisor is 0.
3. **Stack trace top**: `Calculator.divide` at line 12 of `Calculator.java` — this is where the crash happened.
4. **Caller**: `OrderSystem.applyDiscount` at line 44 — this called `divide`.
5. **Entry point**: `Main.main` at line 7 — this is where execution started.

**Action**: Open `Calculator.java`, go to line 12. Find the division. Check what variable is the divisor. Trace back to why it is zero. Fix begins at `Calculator.java:12` — but the root cause (why the divisor is 0) may be found in `OrderSystem.java:44` where the wrong value was passed.

## Common Mistakes

- **Not reading the message at all**: The message after the colon is often a precise description of the problem. Read it.
- **Starting at the bottom of the stack**: The bottom is the entry point (`main`); the top is the actual crash location.
- **Trying to fix library code**: Lines like `at java.util.ArrayList.get(ArrayList.java:442)` are not your code. Keep scrolling until you see your class name.

## Mental Model

Think of the stack trace as a **series of breadcrumbs** leading from where execution started all the way to where it crashed. You want the crumb closest to the crash — the top of the trace. If that crumb is in library code, follow the crumbs backward (downward in the trace) until you reach one with *your* code's name. That is where the investigation starts.

## Mini Summary

- ✔ Error message = exception type + message + stack trace.
- ✔ Exception type tells you the category of failure.
- ✔ Message gives specific details about what went wrong.
- ✔ Stack trace `at` lines give file names and line numbers.
- ✔ Find your own code in the trace — start there.

# Guided Practice Quest

Work through the sidebar steps to extract information from a complete error message, identify your own code in a long stack trace, and translate an error message into a concrete debugging action.

# Solo Practice Quest

**Spell: Decode the Incident Report**

Read this error and write a complete analysis:

```
Exception in thread "main" java.lang.StringIndexOutOfBoundsException: Range [0, 5) out of bounds for length 3
    at java.base/java.lang.String.checkBoundsBeginEnd(String.java:3726)
    at java.base/java.lang.String.substring(String.java:1873)
    at SpellParser.extractCode(SpellParser.java:27)
    at AcademyApp.loadSpell(AcademyApp.java:11)
    at AcademyApp.main(AcademyApp.java:5)
```

Answer:
1. What exception type occurred?
2. What does the message tell you specifically?
3. Which file and line in *your* code should you investigate?
4. What kind of bug is this likely to be — syntax, runtime, or logical?
5. What would you check at the identified line to fix it?

# Integration

**Mathematics connection — reading formal notation**

Mathematicians read formal expressions by parsing components: `∑ᵢ₌₁ⁿ f(i)` is parsed as sum, lower bound, upper bound, summand. Reading it takes practice, but once you know the structure, it is fast. Error messages have the same structured syntax: type, message, stack. Once you internalise the structure, parsing a 20-line stack trace takes seconds. The skill is not knowledge — it is pattern recognition built through repeated exposure.

**Psychology connection — expertise and chunking**

Research on expertise shows that expert chess players do not evaluate pieces individually — they see the board in familiar chunks. Expert programmers read stack traces the same way: they immediately chunk "NullPointerException at my service class, line 23" into a single pattern and know exactly what to look for. As a beginner, you build this expertise by deliberately practising the parsing strategy: type, message, line — every single time, without skipping steps.

**Question:** Describe the full process you would follow, step by step, upon seeing a runtime error message in the console — from the moment you see the error to the moment you begin editing code to fix it.

# Lore Conclusion

You have learned to read the Incident Report. The cascade of invocation records is no longer a wall of red noise — it is an organised document with a structure you can parse. From this point on, every crash is a clue and every error message is a map. In the next lesson you will add the most practical tool in any developer's kit: the print statement. Used with precision, it lets you see inside a running program and confirm exactly what is happening at every step.
