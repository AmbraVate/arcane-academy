---
id: se-app-m3-07
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m3
moduleTitle: "Module 3: Functions and Reusability"
moduleGlyph: "⚗️"
moduleSortOrder: 3
topicSlug: problem_solving
topicTitle: "Problem-Solving"
topicSortOrder: 2
lesson: thinking_step_by_step
title: "Thinking Step-by-Step"
sortOrder: 7
difficulty: 1
estimatedMinutes: 18
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Describes the problem in plain language before writing any code"
    - "Breaks the problem into at least 3 numbered steps"
    - "Uses non-code language (pseudocode or plain English) for the plan"
    - "Explains why planning before coding reduces errors"
    - "Identifies what information (inputs) and what result (outputs) are needed"
  keywords: [plan, step, input, output, pseudocode, problem, decompose, think]
  modelAnswer: |
    Problem: Print the numbers 1 to 10.
    Steps:
    1. Start at 1.
    2. Print the current number.
    3. Add 1 to the current number.
    4. If the number is 10 or less, go to step 2.
    5. Stop.
    // Thinking through steps first reveals the loop structure before writing any code.
guidedSteps:
  - id: gs-m3-07-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Why do professional developers write a plan BEFORE writing code?
    inputConfig:
      options:
        - "To make the code run faster"
        - "Because Java requires a plan file before compilation"
        - "To catch logical problems early, before they become code bugs"
        - "To avoid having to write comments"
    markingRule:
      matchMode: EXACT
      accepted: ["To catch logical problems early, before they become code bugs"]
      rejectedFeedback: "Planning lets you spot logical errors before they become embedded in code, where they are harder to find and fix."
    hint: "Think about when it is cheapest to fix a mistake — on paper or in running code?"
    reflectionPrompt: "Mistakes caught on paper cost nothing. Mistakes caught in production code cost time, reputation, and sometimes money."
  - id: gs-m3-07-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Break this problem into steps using plain English (no Java yet):
      "Print the name of every student in a list of five names."
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: ["each", "every", "loop", "list", "print", "name", "repeat", "go through"]
      rejectedFeedback: "Think about what you need to do for each name. You need to go through the list and print each name one by one."
    hint: "How do you handle five names? Do you do the same thing for each one?"
    reflectionPrompt: "When you describe a repetitive task in steps, you naturally discover the need for a loop."
  - id: gs-m3-07-3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does it mean to 'decompose' a problem?
    inputConfig:
      options:
        - "Delete parts of the problem that seem too hard"
        - "Break the problem into smaller, manageable sub-problems"
        - "Convert the problem into mathematical equations"
        - "Ask someone else to solve the problem"
    markingRule:
      matchMode: EXACT
      accepted: ["Break the problem into smaller, manageable sub-problems"]
      rejectedFeedback: "Decomposition means breaking a large problem into smaller pieces, each of which is easier to solve independently."
    hint: "Decom-pose: break something down into its component parts."
    reflectionPrompt: "Every complex program is a collection of small, solved sub-problems composed together."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the first thing you should do when faced with a programming problem?"
    options:
      - "Open your IDE and start typing"
      - "Search for similar code online"
      - "Understand the problem and plan steps before writing code"
      - "Ask a senior developer to solve it"
    correctIndex: 2
    feedback: "Understanding the problem and planning steps first prevents you from solving the wrong problem or writing code that needs to be thrown away."
  - type: MULTIPLE_CHOICE
    question: "You have a complex problem that seems overwhelming. The best approach is to:"
    options:
      - "Solve the whole thing at once from memory"
      - "Break it into smaller steps and solve each step"
      - "Look for copy-paste solutions online"
      - "Skip the hard parts and come back to them"
    correctIndex: 1
    feedback: "Decomposing a complex problem into smaller steps makes each part approachable. This is the core skill of computational thinking."
retrieval:
  recall: "List three questions you should answer BEFORE writing any code for a new problem."
  explain: "Explain why diving straight into code without a plan often leads to more total time spent, not less."
  mistakeId:
    code: |
      // Developer starts coding without a plan:
      public static void process() {
          // Not sure what this should do yet...
          int x = 5;
          // Maybe add something here?
      }
    answer: "The developer has no clear goal. The method has no defined purpose, inputs, or outputs. Before writing any code, they should answer: What does this method do? What does it take as input? What does it return or produce? Only then should code be written."
---

# Hook

You sit down to write a program. You open your editor. Your fingers hover over the keyboard. Then nothing. You do not know where to start. Or worse — you start typing and twenty minutes later realise the code is solving the wrong problem. Every experienced developer has been here. The solution is not to type faster — it is to think first. Step-by-step thinking is the skill that separates programmers who get stuck from those who always know their next move.

# Lore Introduction

Archmage Veylan never cast a spell without first consulting his planning slate. On it, he wrote not runes but words — plain descriptions of what the incantation must do, what it must accept, and what it must produce. "The slate costs nothing," he told his apprentices. "Errors on slate can be erased with a finger. Errors in cast stone take weeks to undo." The Academy's fastest problem-solvers, he insisted, were not those who wrote incantations first — they were those who planned longest before picking up the quill.

# Core Learning

## Concept Introduction

Step-by-step thinking is the process of planning a solution before writing code. It has three phases:

**1. Understand the problem**
- What are the inputs? (What information does the program need?)
- What is the output? (What should the program produce?)
- What are the edge cases? (What could go wrong or be unusual?)

**2. Decompose into steps**
Break the problem into a numbered sequence of small actions. Use plain English — no code yet.

**3. Verify the plan**
Trace through your steps manually with a simple example. Does the plan produce the right output?

```
Problem: Calculate the average of three numbers.

Steps:
1. Accept three numbers as input.
2. Add the three numbers together.
3. Divide the sum by 3.
4. Return (or display) the result.

Test: inputs = 4, 8, 6. Sum = 18. Average = 18/3 = 6. Correct.
```

Only then does writing Java begin.

## Why It Matters

When you write code before thinking, you often solve the wrong problem, miss edge cases, or build a structure that needs to be torn down and rebuilt. Planning is an investment: ten minutes of thinking can save hours of confused coding. Professional developers spend significant time in design and planning — the writing of actual code is often the shortest phase of software development.

## Worked Examples

**Example 1 — Print numbers 1 to 5**

```
Problem: Print the numbers 1 to 5.
Inputs: none (hardcoded range)
Outputs: five lines, each with a number

Steps:
1. Set current number to 1.
2. Print the current number.
3. Increase the current number by 1.
4. If current number is 5 or less, go back to step 2.
5. Done.
```

This plan reveals that a loop is needed — before any Java is written.

**Example 2 — Check if a number is even**

```
Problem: Determine if a number is even or odd.
Inputs: one integer
Outputs: "even" or "odd"

Steps:
1. Receive the number.
2. Divide the number by 2 and check the remainder.
3. If the remainder is 0, it is even.
4. Otherwise, it is odd.
5. Print the result.
```

**Example 3 — Find the largest of three numbers**

```
Problem: Given three numbers, return the largest.
Inputs: three integers
Outputs: one integer (the largest)

Steps:
1. Start by assuming the first number is the largest.
2. Compare with the second number. If the second is larger, it becomes the new "largest".
3. Compare the current "largest" with the third number. If the third is larger, update.
4. Return the current "largest".
```

## Common Mistakes

- **Starting to code immediately.** Fast fingers do not make fast programs. A few minutes of planning almost always saves time overall.
- **Planning in Java.** Your plan should be in plain language. If you are writing `for (int i = 0; ...)` in your plan, you are in the wrong phase.
- **Making the plan too vague.** "Handle the data" is not a step. "Add the values together" is.
- **Skipping edge case consideration.** What if the input is zero? What if the list is empty? Plans should note these cases.
- **Treating the plan as fixed.** Plans should be revised as you learn more. A plan is a starting point, not a contract.

## Mental Model

Think of solving a problem like navigating an unfamiliar city. You would not start driving randomly and hope you arrive. You would look at a map first, identify your route, and then drive. The map is your plan. The drive is writing code. People who skip the map often end up lost, backtrack, and take far longer than those who planned.

## Mini Summary

- Understand the problem fully before writing any code.
- Identify inputs (what goes in) and outputs (what comes out).
- Decompose the problem into a numbered sequence of plain-English steps.
- Trace through the plan manually with a simple test case.
- Plans are cheap to change; code is expensive to change.
- Complex problems are solved by breaking them into simpler sub-problems.

# Guided Practice Quest

Work through each step in order.

**Step 1.** Why do professional developers write a plan before coding? Select the best reason.

**Step 2.** Break this problem into plain-English steps: "Print the name of every student in a list of five names."

**Step 3.** What does it mean to "decompose" a problem?

# Solo Practice Quest

Write a step-by-step plan (no Java code) for the following problem:

"A program should ask the user for two numbers. If the first number is larger, print 'First wins'. If the second is larger, print 'Second wins'. If they are equal, print 'Tie'."

Your plan must:
1. State the inputs and outputs.
2. List numbered steps in plain English.
3. Include at least one edge case.
4. Be testable by tracing through with example numbers.

# Integration

**Psychology connection — Cognitive load and working memory**

Research in cognitive psychology shows that the human brain's working memory can hold approximately seven items simultaneously. When you dive into coding without a plan, you are asking your working memory to track the problem, the solution approach, the syntax, the variable names, and the debugging all at once. By planning first, you off-load the problem structure onto paper (or a document), freeing your working memory to focus only on writing correct Java. This is why experienced developers who plan feel less mentally exhausted than those who code impulsively.

**Philosophy connection — Means and ends**

Aristotle distinguished between the *end* (the goal) and the *means* (the path to reach it). A common error in programming is focusing entirely on the means (writing code) without first clearly defining the end (what the program should do). Step-by-step thinking forces you to specify the end precisely before choosing the means. This is why the question "What exactly should the output be?" is more fundamental than "How do I write this in Java?"

**Free question:** A fellow apprentice says "I code better when I just freestyle — planning kills my creativity." How would you respond? Are there situations where their approach might work? Where might it fail?

# Lore Conclusion

Veylan put down his quill and held up his planning slate — covered in arrows, numbered steps, and crossed-out lines. "The plan is not pretty," he admitted, "but it is honest. Every crossed-out line represents an error I caught before it became an incantation. Every arrow is a connection I discovered by thinking, not by casting." He set the slate aside and picked up his quill. "Now," he said, "I will write the spell. Because now I know exactly what it must do." The apprentices understood: the slate was not a distraction from the work. The slate *was* the work.
