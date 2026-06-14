---
id: se-app-m1-01
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m1
moduleTitle: "Module 1: Foundations of Computation"
moduleGlyph: "🧠"
moduleSortOrder: 1
topicSlug: computational_thinking
topicTitle: "Computational Thinking"
topicSortOrder: 1
lesson: what_is_computation
title: "What is Computation?"
sortOrder: 1
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines computation in your own words (not copied)"
    - "Gives at least one real-world example of computation"
    - "Explains the difference between data and instructions"
    - "Describes what a computer does step-by-step"
    - "Uses accurate terminology (input, process, output)"
  keywords: [computation, instruction, input, output, process, data, algorithm]
  modelAnswer: |
    Computation is the process of following a precise set of instructions (an algorithm)
    to transform inputs into outputs. A computer is a machine that performs this process
    automatically and reliably.

    Example: A calculator computing 5 + 3 receives the inputs 5 and 3, applies the
    addition instruction, and produces the output 8. Every program you write is a
    sequence of such instructions.
guidedSteps:
  - id: comp-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of the following best describes what a computer **does**?
    inputConfig:
      options:
        - "Stores things permanently forever"
        - "Follows precise instructions to transform inputs into outputs"
        - "Creates new information from nothing"
        - "Replaces human thinking entirely"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Follows precise instructions to transform inputs into outputs"]
      rejectedFeedback: "A computer is fundamentally an instruction-follower. It takes inputs, applies instructions precisely, and produces outputs. It does not create information or replace thinking — it automates following a recipe."
    hint: "Think about a calculator: you press buttons (input), it applies an operation (instruction), and shows a result (output)."
    reflectionPrompt: "Exactly. The power of a computer lies in this: it follows instructions *perfectly* and *tirelessly*. A recipe that a human might mess up after 100 repetitions, a computer executes identically every single time."

  - id: comp-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence:

      "A computer receives ___ , processes them according to ___ , and produces ___."
    inputConfig:
      placeholder: "inputs / instructions / outputs"
    markingRule:
      matchMode: CONTAINS
      accepted: [input, instruction, output]
      rejectedFeedback: "The classic model: **inputs** → **processing** (instructions/algorithm) → **outputs**. This IPO model describes every computation, from a pocket calculator to a global search engine."
    hint: "Think about the three stages: what goes IN, what happens DURING, what comes OUT."
    reflectionPrompt: "The IPO model (Input → Process → Output) is the foundation of all computing. Every program you will ever write follows this pattern, even if it's hidden behind layers of complexity."

  - id: comp-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2-3 sentences, describe an everyday activity that works like a computation.
      What are the inputs, the instructions, and the outputs?
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [input, instruction, output, step, process, result]
      rejectedFeedback: "Try describing a recipe, a navigation app, or even sorting a deck of cards. Each has: things you start with (inputs), steps to follow (instructions), and a result (output)."
    hint: "A GPS navigation app is a great example: your location and destination are inputs; the routing algorithm is the instructions; the turn-by-turn directions are the output."
    reflectionPrompt: "Great. You've just described an algorithm in the real world. The transition from 'things we do daily' to 'things a computer can do' is exactly what programming makes possible."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the correct order of the IPO (Input-Process-Output) model?"
    options:
      - "Process → Input → Output"
      - "Input → Process → Output"
      - "Output → Process → Input"
      - "Input → Output → Process"
    correctIndex: 1
    feedback: "Input comes first (data enters), then processing (instructions applied), then output (result produced). This sequence is the foundation of every computation."
  - type: MULTIPLE_CHOICE
    question: "A spell-checker reads your essay and highlights misspelled words. What is the 'output' in this computation?"
    options:
      - "Your essay text"
      - "The dictionary used to check spellings"
      - "The list of highlighted misspelled words"
      - "The keyboard you typed on"
    correctIndex: 2
    feedback: "The essay text is input, the dictionary lookup rules are the process/instructions, and the highlighted list of misspellings is the output — the result produced."

retrieval:
  recall: "In one sentence, define computation without using the word 'computer'."
  explain: "Explain what a computer does to someone who has never used one, using only everyday analogies."
  mistakeId:
    code: "Computation = storing data permanently"
    answer: "Computation is the *process* of transforming inputs into outputs by following instructions — not storage. Storage (memory) is just one component that supports computation."
---

# Hook

You give a machine a problem. It gives you an answer. Instantly. Without fatigue. Without error.

How?

The answer is simple — and profound: the machine follows instructions. Not intelligent instructions. Not creative instructions. Just precise, unambiguous steps, repeated exactly.

That's computation. And understanding it changes how you see everything from calculators to search engines to the code you're about to write.

> Why do you think computers need explicit, step-by-step instructions rather than being able to figure things out on their own?

# Lore Introduction

In the first hall of the Arcane Academy, apprentices are shown a room containing only a crystal orb and a scroll. The scroll holds a single incantation: precise, complete, unambiguous. The orb does exactly what the scroll says — nothing more, nothing less.

Archmage Veylan speaks quietly: *"The orb has no wisdom. It has no curiosity. It has perfect obedience. That is its power — and its limitation. Your first task as a Systems Architect is to understand what 'perfect obedience' truly means."*

# Core Learning

## Concept Introduction

**Computation** is the process of following a defined set of instructions to transform inputs into outputs.

| Component | Meaning | Example |
|---|---|---|
| **Input** | Data fed into the process | Numbers to add, text to search |
| **Instructions** (algorithm) | The precise steps to follow | `+` for addition, pattern matching |
| **Output** | The result produced | Sum, search results |

The machine doing the computation is just an extremely reliable instruction-follower. It has no understanding of what the instructions *mean* — it simply executes them with perfect fidelity.

## Why It Matters

Every program you ever write is a set of instructions for a computation. If you cannot think clearly about inputs, the steps required, and the expected outputs, you cannot write working code. Understanding computation at its simplest level — before any syntax — gives you a mental foundation that no amount of language-specific knowledge can replace.

## Worked Examples

**Example 1 — A calculation**
- Input: two numbers (5, 3)
- Instructions: add them
- Output: 8

**Example 2 — A search**
- Input: a list of names + a name to find
- Instructions: compare each name in the list to the search term
- Output: position of the match (or "not found")

**Example 3 — A sorting program**
- Input: an unsorted list `[4, 1, 3, 2]`
- Instructions: compare adjacent elements, swap if out of order, repeat until sorted
- Output: `[1, 2, 3, 4]`

Each follows the same model: **Input → Process → Output**.

## Common Mistakes

- **Thinking computers are intelligent.** They are not. They follow rules. The "intelligence" is in the rules you write.
- **Confusing storage with computation.** Storing a number in memory is not computation; computing with it is.
- **Assuming the computer understands intent.** It doesn't. If your instructions are wrong, you get wrong output — guaranteed.
- **Skipping the output.** Every computation produces something. If you can't describe the output, your instructions are incomplete.

## Mental Model

Think of a **vending machine**. You insert coins and press a button (input). The machine's internal mechanism applies a fixed procedure (instructions). Out comes your item (output). The machine doesn't know why you want the item, doesn't care what you plan to do with it, and cannot deviate from its mechanism. That is computation: precise, predictable, and entirely dependent on the quality of its instructions.

## Mini Summary

- ✔ Computation = transforming inputs into outputs by following precise instructions
- ✔ Every computation has three components: Input, Process, Output (IPO)
- ✔ Computers follow instructions perfectly — they do not think, interpret, or improvise
- ✔ Programs *are* the instructions; programming is designing the instructions
- ✔ If the instructions are wrong, the output is wrong — always

# Guided Practice Quest

**The Instruction Scroll**

The Academy's sorting guild has asked you to think like a machine. For three everyday scenarios, describe the inputs, instructions, and outputs — without writing any code. This is pure computational thinking.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Choose **one** of the following scenarios and write a clear description of the computation it performs:

1. A thermostat that turns on heating when the temperature drops below 18°C
2. A vending machine dispensing a product after money is inserted
3. A simple alarm clock triggering at a set time

Your description must include:
- The inputs (what data does it receive?)
- The instructions (what steps does it follow?)
- The output (what result does it produce?)
- One thing that could go wrong if the instructions were slightly wrong

# Integration

**Connecting to Mathematics — Algorithms and Procedures**

Mathematicians have been describing algorithms for millennia — long before computers existed. Euclid's algorithm for finding the greatest common divisor (around 300 BC) is one of the oldest documented algorithms. It takes two numbers as input, applies a series of division and remainder steps, and outputs their GCD.

What computation and mathematics share is the demand for precision. A mathematical proof, like a computer program, must be exact — one ambiguous step invalidates the whole argument. When you learn to write programs, you are inheriting a tradition of rigorous, stepwise reasoning that goes back to ancient Greece.

What does this suggest about the kind of thinking that makes both a good mathematician and a good programmer?

# Lore Conclusion

The apprentice sets down the scroll. The crystal orb is dark and waiting.

*"Good,"* says Archmage Veylan. *"You understand what the orb is. Now you must learn what instructions it can follow — and how to write them with precision. The next lesson will show you the building blocks: the inputs, the types, and the simplest forms of transformation."*

The orb flickers faintly. The first rune of computation has been inscribed.
---
