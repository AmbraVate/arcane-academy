---
id: se-app-m3-08
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m3
moduleTitle: "Module 3: Functions and Reusability"
moduleGlyph: "⚗️"
moduleSortOrder: 3
topicSlug: problem_solving
topicTitle: "Problem-Solving"
topicSortOrder: 2
lesson: pseudocode
title: "Pseudocode"
sortOrder: 8
difficulty: 1
estimatedMinutes: 20
xpReward: 40
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m3-07]
integrationDomains: [psychology, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Writes pseudocode in plain English without Java syntax"
    - "Uses keywords like IF, ELSE, FOR, WHILE clearly"
    - "Converts the pseudocode logically into Java"
    - "Explains why pseudocode is language-independent"
    - "Demonstrates a concrete problem solved with pseudocode first"
  keywords: [pseudocode, IF, ELSE, FOR, WHILE, plain, language, convert, plan]
  modelAnswer: |
    // Pseudocode:
    // SET sum TO 0
    // FOR each number from 1 to 5
    //   ADD number TO sum
    // PRINT sum

    // Java translation:
    int sum = 0;
    for (int i = 1; i <= 5; i++) {
        sum += i;
    }
    System.out.println(sum);
guidedSteps:
  - id: gs-m3-08-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of the following is the best example of pseudocode for "add two numbers and print the result"?
    inputConfig:
      options:
        - "int a = 3; int b = 4; System.out.println(a + b);"
        - "SET a TO 3, SET b TO 4, PRINT a + b"
        - "a plus b equals result show result"
        - "ADD NUMBERS"
    markingRule:
      matchMode: EXACT
      accepted: ["SET a TO 3, SET b TO 4, PRINT a + b"]
      rejectedFeedback: "Pseudocode uses plain language with clear keywords like SET, PRINT, IF — not Java syntax, but more precise than casual English."
    hint: "Pseudocode sits between plain English and real code — structured but not bound to one language's syntax."
    reflectionPrompt: "Good pseudocode is precise enough to be converted to any language, but readable without knowing any language."
  - id: gs-m3-08-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Write pseudocode (no Java) for: "If a score is 50 or more, print 'Pass', otherwise print 'Fail'."
    inputConfig:
      minWords: 8
    markingRule:
      matchMode: CONTAINS
      accepted: ["IF", "50", "Pass", "Fail", "ELSE", "otherwise", "print", "PRINT"]
      rejectedFeedback: "Use IF/ELSE structure. Example: IF score >= 50 THEN PRINT 'Pass' ELSE PRINT 'Fail'"
    hint: "Use IF...THEN...ELSE to express the condition."
    reflectionPrompt: "The IF/ELSE structure in pseudocode maps directly to if/else in Java — pseudocode makes the translation obvious."
  - id: gs-m3-08-3
    sortOrder: 3
    inputType: CODE
    instruction: |
      Convert this pseudocode to Java:
      SET total TO 0
      FOR i FROM 1 TO 3
        ADD i TO total
      PRINT total
    inputConfig:
      placeholder: |
        // write the Java translation here
    markingRule:
      matchMode: CONTAINS
      accepted: ["int total", "for", "total +=", "System.out.println"]
      rejectedFeedback: "Translate each pseudocode line: int total = 0; for (int i = 1; i <= 3; i++) { total += i; } System.out.println(total);"
    hint: "Each pseudocode line maps to one or two Java lines. Start with int total = 0;"
    reflectionPrompt: "Converting pseudocode to Java is mechanical — the thinking was already done in the pseudocode phase."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Pseudocode is valuable because:"
    options:
      - "It runs faster than real code"
      - "It lets you plan logic without worrying about syntax"
      - "Java compilers can read pseudocode directly"
      - "It replaces the need to write real code"
    correctIndex: 1
    feedback: "Pseudocode focuses on logic and structure, not syntax. This separates two hard problems: 'what to do' and 'how to say it in Java'."
  - type: MULTIPLE_CHOICE
    question: "What does WHILE mean in pseudocode?"
    options:
      - "A special Java import"
      - "Repeat a block of steps as long as a condition is true"
      - "Wait for user input"
      - "Print a value to the screen"
    correctIndex: 1
    feedback: "WHILE in pseudocode represents a loop that continues as long as its condition holds — just like Java's while loop."
retrieval:
  recall: "Write pseudocode for: 'Count from 1 to 10 and print each number'."
  explain: "Explain why pseudocode is 'language-independent'. What does that mean and why is it useful?"
  mistakeId:
    code: |
      // Student's 'pseudocode':
      for (int i = 0; i < 5; i++) {
          System.out.println(i);
      }
    answer: "This is actual Java code, not pseudocode. Pseudocode should not contain Java syntax like for(...), System.out.println, or semicolons. Correct pseudocode: FOR i FROM 0 TO 4: PRINT i"
---

# Hook

You have a plan written in plain English. But how do you get from that plan to working Java? There is a useful middle step: pseudocode. Pseudocode is structured like code — it uses keywords, conditionals, and loops — but it uses plain English words instead of Java syntax. It is fast to write, easy to change, and can be converted to any programming language. Master pseudocode and you will never stare at a blank editor again.

# Lore Introduction

In the Academy's library, there existed a special kind of notation — not the formal rune language of Java, but a looser script that captured the *intent* of an incantation without binding it to any specific casting language. Senior mages called it the "draft tongue". "Write your spell in the draft tongue first," Veylan told his apprentices. "Every error you find there saves you from re-carving a completed stone." The draft tongue had its own conventions: SET for assignment, IF for conditions, FOR for repetition. Close enough to read; free enough to write quickly.

# Core Learning

## Concept Introduction

**Pseudocode** is an informal, structured notation for describing algorithms. It uses plain language with coding-style keywords like `IF`, `ELSE`, `FOR`, `WHILE`, `SET`, `PRINT`, and `RETURN`. The key rules:

- **No exact syntax required** — you are not writing Java.
- **Be specific enough** to translate to code without ambiguity.
- **Be readable** to someone who does not know Java.

Common pseudocode keywords:

| Pseudocode | Java equivalent |
|---|---|
| `SET x TO 5` | `int x = 5;` |
| `PRINT x` | `System.out.println(x);` |
| `IF x > 0 THEN ... ELSE ...` | `if (x > 0) { ... } else { ... }` |
| `FOR i FROM 1 TO 5` | `for (int i = 1; i <= 5; i++)` |
| `WHILE condition DO ...` | `while (condition) { ... }` |
| `RETURN result` | `return result;` |

## Why It Matters

Pseudocode separates two hard problems: **what to do** (logic) and **how to say it** (Java syntax). Solving them simultaneously is difficult. Pseudocode lets you solve the logic problem first, then translate to Java in a second, mechanical step. It is also language-independent — the same pseudocode works as a plan for Java, Python, or any other language.

## Worked Examples

**Example 1 — Simple conditional**

```
Pseudocode:
IF age >= 18 THEN
    PRINT "Adult"
ELSE
    PRINT "Minor"

Java:
if (age >= 18) {
    System.out.println("Adult");
} else {
    System.out.println("Minor");
}
```

**Example 2 — Loop with accumulator**

```
Pseudocode:
SET total TO 0
FOR i FROM 1 TO 5
    ADD i TO total
PRINT total

Java:
int total = 0;
for (int i = 1; i <= 5; i++) {
    total += i;
}
System.out.println(total);
```

**Example 3 — Method with return**

```
Pseudocode:
FUNCTION max(a, b)
    IF a > b THEN RETURN a
    ELSE RETURN b

Java:
public static int max(int a, int b) {
    if (a > b) return a;
    else return b;
}
```

## Common Mistakes

- **Writing actual Java in your pseudocode.** If you write `for (int i = 0; i < 5; i++)`, you are writing Java, not pseudocode.
- **Being too vague.** "Handle the data" is not a pseudocode step. "ADD value TO sum" is.
- **Skipping pseudocode for "simple" problems.** Simple problems rarely stay simple. The habit of pseudocode is more valuable than the time it saves on any single problem.
- **Using pseudocode keywords inconsistently.** Decide on your conventions and stick to them (`SET`, `PRINT`, `IF`, etc.) within one plan.
- **Thinking pseudocode must be "official".** There is no universal standard. As long as it communicates the logic clearly, it is valid.

## Mental Model

Pseudocode is like a **film storyboard**. A storyboard describes every scene of a film in rough sketches — not the final film, but close enough that the director, actors, and crew all understand what is needed. Pseudocode is your program's storyboard: rough enough to write quickly, detailed enough to guide the final production.

## Mini Summary

- Pseudocode uses plain-English keywords (IF, FOR, WHILE, SET, PRINT) to describe logic.
- It is not bound to any programming language's exact syntax.
- Pseudocode separates solving the logic problem from solving the syntax problem.
- Keywords like IF, FOR, WHILE map directly to their Java counterparts.
- Converting pseudocode to Java is a mechanical translation, not a creative challenge.
- Pseudocode is most useful for planning loops, conditions, and multi-step algorithms.

# Guided Practice Quest

Work through each step in order.

**Step 1.** Which option is the best example of pseudocode for "add two numbers and print the result"?

**Step 2.** Write pseudocode (no Java syntax) for: "If a score is 50 or more, print 'Pass', otherwise print 'Fail'."

**Step 3.** Convert the given pseudocode (SET total TO 0, FOR i FROM 1 TO 3, ADD i TO total, PRINT total) to Java.

# Solo Practice Quest

Write pseudocode for the following problem, then convert it to Java:

"Given a list of five test scores, calculate the total and print whether the student passed (average 50 or more) or failed."

Your answer must show:
1. The pseudocode plan (use SET, FOR, IF, PRINT).
2. The Java translation.
3. A brief comment explaining how the pseudocode helped you plan.

# Integration

**Psychology connection — Dual-process thinking**

Psychologist Daniel Kahneman describes two types of thinking: System 1 (fast, intuitive) and System 2 (slow, deliberate). Jumping straight to code is System 1 thinking — it feels productive but skips important analysis. Writing pseudocode engages System 2: deliberate, structured, and analytical. The extra effort at the planning stage — pseudocode — consistently produces better outcomes than the impulsive "just start coding" approach. Pseudocode is a practical tool for forcing System 2 thinking to happen before System 1 takes over the keyboard.

**Philosophy connection — Medium of thought**

Philosophers of language have argued that the words we have available shape the thoughts we can think. This is why pseudocode is valuable: it gives you a richer vocabulary than plain English ("IF", "FOR", "WHILE" are precise concepts) without the cognitive burden of full Java syntax. Pseudocode is an intermediate language that expands your thinking vocabulary just enough to express computational logic, without the strictness that slows early planning down.

**Free question:** Pseudocode has no single official standard. Different books and developers use different keywords and conventions. Is that a weakness or a strength? Argue your position.

# Lore Conclusion

Veylan unrolled a scroll covered in the Academy's draft tongue. "Every great incantation in this building," he said, gesturing around the tower, "began as a scroll like this one." He pointed to a line: `IF mana > threshold THEN invoke primary ward ELSE invoke fallback`. "The runes came second. The draft tongue came first. It freed us to think clearly before we committed ourselves to stone." The apprentice rolled up her own scroll, satisfied. The pseudocode was done. Now — only now — it was time to write the spell.
