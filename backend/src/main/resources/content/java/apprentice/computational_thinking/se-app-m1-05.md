---
id: se-app-m1-05
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
lesson: pattern_recognition
title: "Pattern Recognition"
sortOrder: 5
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [abstraction]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies a repeating pattern in a given problem"
    - "Names the pattern clearly"
    - "Explains how recognising the pattern helps solve the problem"
    - "Gives at least one programming example where this pattern appears"
    - "Distinguishes the pattern from one-off logic"
  keywords: [pattern, repeat, generalise, loop, template, reuse, common, recurring]
  modelAnswer: |
    Pattern: Reading each item in a list and performing an action on it.
    This pattern appears in: printing a shopping list, finding the largest number,
    filtering items by a condition, totalling scores.
    In code it becomes: a for/while loop + an action on each element.
    Recognising this pattern means you reach for a loop rather than writing
    repeated code.
guidedSteps:
  - id: pat-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You notice that your programs often need to:
      1. Check a condition
      2. If true, do something
      3. Otherwise, do something else

      This is a pattern. What is it called in programming?
    inputConfig:
      options:
        - "A loop pattern"
        - "A conditional (if/else) pattern"
        - "A function pattern"
        - "A data pattern"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A conditional (if/else) pattern"]
      rejectedFeedback: "Check → do this OR that is the **conditional** (if/else) pattern. Recognising it means you automatically know the syntax structure to reach for."
    hint: "The pattern is: evaluate something, then take one of two paths based on the result."
    reflectionPrompt: "Correct. Every time you see 'check something and act differently based on the result', you're recognising the conditional pattern. In Java this becomes `if (condition) { ... } else { ... }`."
  - id: pat-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Look at these three tasks:
      - "Print each item in a shopping list"
      - "Calculate the total of all scores"
      - "Find the largest number in a list"

      What pattern do they all share?
    inputConfig:
      minWords: 8
    markingRule:
      matchMode: CONTAINS
      accepted: [each, every, iterate, loop, list, through, visit, all, repeat]
      rejectedFeedback: "All three visit every item in a collection and do something with it. This is the **iteration** pattern — 'do this for each element'. In code it becomes a loop."
    hint: "What do they all do to 'each item' in the collection?"
    reflectionPrompt: "The iteration pattern. Whenever you need to process every element in a collection, you're applying this pattern. In Java it becomes a `for` loop or for-each loop."
  - id: pat-step-3
    sortOrder: 3
    inputType: FILL_BLANK
    instruction: |
      When you recognise a pattern in a problem, you can apply a known ___ — a reusable
      solution template that solves that class of problem.
    inputConfig:
      placeholder: "one word"
    markingRule:
      matchMode: NORMALIZED
      accepted: [solution, template, pattern, approach, algorithm, strategy]
      rejectedFeedback: "Recognising a pattern lets you apply a known **solution template** (sometimes called a design pattern or algorithm pattern). You don't solve from scratch — you apply the appropriate template."
    hint: "What do you reach for when you recognise a familiar problem type?"
    reflectionPrompt: "Patterns → templates. This is the basis of design patterns in software engineering. Experts don't invent new solutions for every problem — they recognise the pattern and apply the appropriate template."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why is pattern recognition important in programming?"
    options:
      - "It makes programs run faster"
      - "It lets you reuse known solutions instead of solving every problem from scratch"
      - "It removes the need for debugging"
      - "It makes code shorter automatically"
    correctIndex: 1
    feedback: "Pattern recognition = applying known solutions. When you see 'iterate over a list', you reach for a loop. When you see 'check a condition', you reach for if/else. Patterns map problems to solutions."
  - type: MULTIPLE_CHOICE
    question: "Which of these represents a recognisable pattern in programming?"
    options:
      - "This specific variable name"
      - "Accumulating a running total by visiting each element in a list"
      - "The exact code for this one program"
      - "A typo in line 7"
    correctIndex: 1
    feedback: "Accumulate a running total by iterating = a general, repeating pattern that appears in summing, averaging, concatenating strings, and many other tasks. That generality is what makes it a pattern."

retrieval:
  recall: "What is pattern recognition, and why do experienced programmers use it?"
  explain: "Explain pattern recognition to a classmate using an example from maths or everyday life."
  mistakeId:
    code: "Pattern recognition: memorising every possible code snippet"
    answer: "Pattern recognition is about identifying *types* of problems, not memorising code. You recognise 'iterate over a list' as a category, then apply the appropriate solution — which can be adapted for any list and any action."
---

# Hook

Why do experienced programmers solve new problems faster than beginners?

It's not raw intelligence. It's pattern recognition. They've seen enough problems to know: "this looks like a filter-and-transform problem" or "this is a classic divide-and-conquer." They apply a known solution template rather than inventing one from scratch.

Learning to recognise patterns is learning to think like an expert.

> Think of a problem you solved before that later appeared in disguise. How did you recognise it was the same type of problem?

# Lore Introduction

Senior mages at the Academy spend less time on each new incantation — not because they work faster, but because they recognise the *form*. A new protection spell follows the same structure as a dozen they have already cast. They apply the pattern; they fill in the specifics.

*"The novice sees a new problem,"* Archmage Veylan says. *"The expert sees a familiar shape wearing new clothes."*

# Core Learning

## Concept Introduction

**Pattern recognition** is identifying similarities, trends, or recurring structures across problems — and using those patterns to apply known solutions.

Common programming patterns include:
| Pattern | Description | Code structure |
|---|---|---|
| **Iteration** | Do something for each element | `for` / `while` loop |
| **Conditional** | Do different things based on a condition | `if/else` |
| **Accumulation** | Build up a result by visiting elements | Loop + running variable |
| **Search** | Find an element matching a condition | Loop + comparison |

## Why It Matters

Every new programming concept you learn is a pattern — a reusable solution template. The more patterns you internalise, the faster you can solve new problems by recognising which pattern applies.

## Worked Examples

**Example 1 — Print each name in a list**
Pattern: iterate over a collection, do something to each element.
Template: for-each loop.

**Example 2 — Find the largest score**
Pattern: iterate + track running maximum.
Template: for loop + comparison + update variable.

**Example 3 — Validate a password**
Pattern: check multiple conditions; all must be true.
Template: `if` chain with `&&` (AND) operators.

## Common Mistakes

- **Forcing a pattern where it doesn't fit.** Not every problem is a loop; not every condition needs a switch.
- **Ignoring the problem's specific details.** Patterns provide the structure; you still need to fill in the specifics correctly.
- **Recognising too early.** Make sure you understand the problem before jumping to a pattern.

## Mental Model

Pattern recognition is like a **key-and-lock library**.

You accumulate keys (patterns). When you encounter a new lock (problem), you try your keys first. Most problems have been solved before — the key probably exists. Only when no key fits do you need to forge a new one.

## Mini Summary

- ✔ Pattern recognition = identifying familiar problem types and applying known solutions
- ✔ Common patterns: iteration, conditionals, accumulation, search
- ✔ Experts solve problems faster because they recognise patterns, not because they think faster
- ✔ Each programming construct (loop, if/else, function) IS a pattern applied to a problem
- ✔ Learning patterns makes you a better problem-solver across all programming languages

# Guided Practice Quest

**The Pattern Library**

The Guild keeps a library of solution patterns. Your quest: classify three given problems by their pattern type and describe the solution template each requires.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Look at the following list of tasks. Group them by the programming pattern they share, and for each group name the pattern and explain what code structure you would use:

1. Sum all numbers in a list
2. Print only even numbers from a list
3. Find the first student with a score above 90
4. Check if a password meets minimum length requirements
5. Greet each user in a waiting list
6. Count how many items in a basket cost more than £10

Identify **at least 3 distinct patterns** in this list.

# Integration

**Connecting to Psychology — Expert vs Novice Problem Solving**

Research in cognitive psychology (notably by de Groot, 1965, studying chess players) found that expert chess players don't calculate more moves than novices — they *recognise* familiar positions and know the strong responses from memory. Novices see a board of 32 pieces; experts see meaningful patterns.

This is called **chunking** — grouping information into meaningful units. Expert programmers chunk code into patterns: "a guard clause", "a factory method", "a null check". These chunks free up working memory for higher-level reasoning.

How might you deliberately practise pattern recognition to develop expert-level chunking in programming?

# Lore Conclusion

The apprentice picks up a new scroll — a problem never seen before. But the shape is familiar: iterate, compare, accumulate.

*"You see it,"* Archmage Veylan says quietly. *"Good. That recognition is the beginning of mastery. Not certainty — but the right question: 'Have I seen this shape before?'"*

The pattern library grows by one more entry.
---
