---
id: se-app-m1-02
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
lesson: algorithms_in_daily_life
title: "Algorithms in Daily Life"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [what_is_computation]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Writes a step-by-step algorithm for a chosen everyday task"
    - "Each step is clear and unambiguous (a machine could follow it)"
    - "Steps are in the correct order"
    - "Handles at least one 'what if' condition (edge case)"
    - "Identifies the inputs and output of the algorithm"
  keywords: [algorithm, step, instruction, order, unambiguous, input, output, condition]
  modelAnswer: |
    Algorithm: Make a cup of tea

    Inputs: kettle, water, teabag, mug, milk (optional), sugar (optional)
    Output: a cup of tea

    Steps:
    1. Fill kettle with water
    2. Turn kettle on
    3. While water is not boiling, wait
    4. Place teabag in mug
    5. Pour boiling water into mug
    6. Wait 3 minutes
    7. Remove teabag
    8. If adding milk, pour milk in
    9. If adding sugar, stir in sugar
    10. Serve

    Edge case: If no water in kettle, refill before step 2.
guidedSteps:
  - id: alg-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of the following is a valid algorithm?
    inputConfig:
      options:
        - "Do stuff until it works"
        - "1. Boil water. 2. Add teabag. 3. Wait 3 minutes. 4. Remove teabag."
        - "Make tea somehow"
        - "Tea-making process (complex)"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["1. Boil water. 2. Add teabag. 3. Wait 3 minutes. 4. Remove teabag."]
      rejectedFeedback: "An algorithm must be **unambiguous** (no vague words like 'somehow'), **ordered** (numbered steps), and **finite** (has an end). Only option B satisfies all three."
    hint: "Look for the option that is unambiguous — a machine could follow it without guessing."
    reflectionPrompt: "Correct. The key word is *unambiguous*. 'Do stuff' is not an algorithm — a computer cannot execute vagueness. Every step must be precise enough that it can be followed without interpretation."

  - id: alg-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Write a 3-5 step algorithm for crossing a road safely.
      Be precise enough that a robot could follow it.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [look, wait, cross, safe, clear, step, check]
      rejectedFeedback: "A crossing-road algorithm needs to: check for traffic, wait until clear, then cross. The steps need to be ordered and precise — 'be careful' is not a step a robot can execute."
    hint: "Think about what you physically do: which way do you look? When do you go? What do you do if cars are coming?"
    reflectionPrompt: "You just wrote a simple algorithm. Notice how hard it is to make it precise enough for a machine — humans rely on constant judgement; machines need explicit rules. This tension is at the heart of programming."

  - id: alg-step-3
    sortOrder: 3
    inputType: FILL_BLANK
    instruction: |
      An algorithm must be all of the following **except** one. Which property is NOT required?

      A) Finite (has a definite end)
      B) Creative (introduces new ideas)
      C) Unambiguous (no vague steps)
      D) Ordered (steps have a sequence)

      Answer: ___
    inputConfig:
      placeholder: "B, C, or D?"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["B", "Creative", "creative"]
      rejectedFeedback: "Algorithms must be finite (they terminate), unambiguous (no guessing), and ordered (sequence matters). They do NOT need to be creative — most are deliberately repetitive and mechanical."
    hint: "Think about whether a machine needs creativity to follow instructions."
    reflectionPrompt: "Exactly. Creativity is a human quality, not an algorithmic one. Algorithms are valued for their *reliability* and *precision*, not their novelty."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What makes an algorithm different from a rough idea?"
    options:
      - "An algorithm uses code"
      - "An algorithm is precise, ordered, and unambiguous"
      - "An algorithm requires a computer to run it"
      - "An algorithm must be short"
    correctIndex: 1
    feedback: "An algorithm is a precise, ordered, unambiguous set of steps. It does not require code or a computer — recipes and musical scores are algorithms. What it cannot contain is vagueness."
  - type: MULTIPLE_CHOICE
    question: "You tell someone to 'go to the shop and get milk'. Is this an algorithm?"
    options:
      - "Yes — it has a start and an end"
      - "No — it is too vague to be followed without interpretation"
      - "Yes — it is simple enough"
      - "No — it doesn't involve a computer"
    correctIndex: 1
    feedback: "'Go to the shop' is ambiguous — which route? How far? What if the shop is closed? An algorithm leaves nothing to interpretation. 'Go to the shop' is an instruction, not a complete algorithm."

retrieval:
  recall: "Name three properties that every algorithm must have."
  explain: "Explain what an algorithm is using only the example of a recipe — no technical jargon."
  mistakeId:
    code: "Algorithm: 'Sort the list until it looks right'"
    answer: "'Looks right' is vague — a computer cannot execute subjective judgements. An algorithm step must be precise: 'Compare adjacent elements; swap if the left is greater than the right.'"
---

# Hook

Every morning you follow algorithms without knowing it.

Wake up. Check the time. If late, skip breakfast. Otherwise make toast.

That sequence — those conditions — that's an algorithm. You've been executing algorithms your entire life. The only difference between that and programming is: when you program, you describe the algorithm in a language a computer can follow precisely.

> Think of the last decision you made automatically today. Could you write it as precise steps?

# Lore Introduction

The Guild of Systems Architects keeps a vast library of scrolls — not spells, but *procedures*. Each scroll describes exactly how to accomplish a task: step one, step two, step three. No ambiguity. No interpretation. The guild's most valued skill is not creativity; it is the ability to capture a process so precisely that even a novice could follow it without error.

*"An algorithm,"* says Archmage Veylan, *"is a recipe written for an obedient machine — one that follows instructions exactly but cannot guess."*

# Core Learning

## Concept Introduction

An **algorithm** is a finite, ordered, unambiguous set of instructions that solves a problem.

| Property | Meaning | Why it matters |
|---|---|---|
| **Finite** | Has a defined end | A loop that never stops is not an algorithm |
| **Ordered** | Steps have a sequence | Wrong order = wrong result |
| **Unambiguous** | Every step has one interpretation | Computers cannot guess |

## Why It Matters

Before writing a single line of code, you must know the algorithm. A program is just an algorithm expressed in a language a computer understands. If your algorithm is wrong, your program will be wrong — perfectly, consistently wrong. Getting the algorithm right first is the most important skill in software engineering.

## Worked Examples

**Example 1 — Finding the largest number in a list**
1. Start with the first number as the "current maximum"
2. For each remaining number in the list:
   - If this number is greater than the current maximum, replace the current maximum with it
3. When all numbers are checked, the current maximum is the answer

**Example 2 — Log-in verification**
1. Receive username and password from user
2. Look up the stored password for that username
3. If the passwords match, grant access
4. Otherwise, deny access and show an error

**Example 3 — Recipe (everyday algorithm)**
1. Boil water
2. Add pasta
3. Wait 10 minutes
4. Test if cooked; if not, wait 2 more minutes and test again
5. Drain, serve

## Common Mistakes

- **Skipping steps that seem obvious.** What seems obvious to a human is invisible to a machine. Write every step.
- **Using vague language.** "Process the data" is not a step. "Compare each element to the previous; if smaller, swap them" is a step.
- **Ignoring edge cases.** What happens if the list is empty? What if the user enters nothing? Good algorithms handle these.
- **Confusing algorithm with code.** An algorithm is language-independent. You can write an algorithm in English before writing code.

## Mental Model

Think of an algorithm as a **flight checklist** for a pilot.

The checklist doesn't assume experience or common sense — it lists every single step explicitly. Before takeoff: check fuel. Check instruments. Check doors. The pilot follows it in order, every time, without deviation. That's what makes aviation safe. That's what makes algorithms reliable.

## Mini Summary

- ✔ An algorithm is finite, ordered, and unambiguous
- ✔ Algorithms exist outside code — recipes, assembly instructions, and checklists are algorithms
- ✔ Programs are algorithms written in a language computers can execute
- ✔ Missing a step or using vague language breaks the algorithm
- ✔ Good algorithms handle edge cases (unexpected inputs)

# Guided Practice Quest

**The Instruction Scroll**

The Guild's mechanical golem needs instructions to make breakfast. It cannot guess, assume, or improvise. Write precise steps for a simple task it can follow.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Write a complete algorithm for **one** of the following:
- Making a sandwich
- Searching for a specific word in a book
- Deciding whether to take an umbrella today

Your algorithm must:
1. State the **inputs** (what information does it start with?)
2. List at least **5 numbered steps** — precise enough for a robot to follow
3. Include at least **one conditional step** (IF something, THEN do this)
4. State the **output** (what is the final result?)

# Integration

**Connecting to Psychology — Heuristics vs Algorithms**

Psychologists distinguish between two types of thinking: **algorithms** (guaranteed to find the correct answer if followed completely) and **heuristics** (mental shortcuts that work most of the time but can fail). When you look for your keys, you probably use a heuristic — checking likely places first — rather than an exhaustive algorithm that checks every possible location.

Algorithms and heuristics both have costs. An algorithm is reliable but can be slow (checking every single location). A heuristic is fast but can fail (the keys aren't where you expect them).

This tradeoff appears constantly in software: do you want a slow-but-correct algorithm or a fast-but-approximate one? What scenarios might call for each?

# Lore Conclusion

The apprentice folds the scroll and hands it to the golem. It reads the steps. It executes them. Breakfast appears.

*"Precision,"* says Archmage Veylan quietly. *"That is the first gift of the algorithm. Not speed, not intelligence — precision. A thing done precisely can be improved. A thing done vaguely can only be guessed at."*

The golem awaits its next instruction.
---
