---
id: se-app-m3-09
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
lesson: flowcharts
title: "Flowcharts"
sortOrder: 9
difficulty: 1
estimatedMinutes: 20
xpReward: 40
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m3-08]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Names and describes at least three flowchart symbols"
    - "Draws (describes in text) a flowchart for a simple algorithm"
    - "Correctly uses a diamond shape for decisions"
    - "Shows Yes/No branches from a decision"
    - "Identifies where a flowchart helps reveal logical errors"
  keywords: [flowchart, oval, rectangle, diamond, arrow, decision, process, start, end]
  modelAnswer: |
    // Flowchart for "print Pass or Fail based on score":
    // [START (oval)]
    //   |
    // [Get score (rectangle)]
    //   |
    // <score >= 50?> (diamond) -- YES --> [Print "Pass" (rectangle)]
    //                         -- NO  --> [Print "Fail" (rectangle)]
    //                                         |
    //                                       [END (oval)]
guidedSteps:
  - id: gs-m3-09-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which flowchart shape represents a DECISION (a yes/no question)?
    inputConfig:
      options:
        - "Oval (rounded rectangle)"
        - "Rectangle"
        - "Diamond"
        - "Arrow"
    markingRule:
      matchMode: EXACT
      accepted: ["Diamond"]
      rejectedFeedback: "A diamond represents a decision point with two possible paths (Yes/No). Ovals are start/end; rectangles are processes."
    hint: "Decisions branch into two directions — their shape reflects that branching quality."
    reflectionPrompt: "Diamonds force you to make branch conditions explicit. Every decision in a flowchart must be a Yes/No question."
  - id: gs-m3-09-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Describe in plain text the flowchart for: "If health is 0, print 'Game Over', otherwise print 'Alive'."
      Include: start, decision, two process boxes, end.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["start", "health", "0", "Game Over", "Alive", "end", "decision", "diamond"]
      rejectedFeedback: "Describe: START → decision diamond (health == 0?) → YES path to 'Game Over' box → END; NO path to 'Alive' box → END."
    hint: "Draw it mentally. Start oval, then a diamond for the condition, then two rectangles for the two outcomes, then end."
    reflectionPrompt: "A flowchart makes branches visible. Reading it, you can instantly see both paths through the logic."
  - id: gs-m3-09-3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      A loop in a flowchart is formed by:
    inputConfig:
      options:
        - "Two diamonds pointing at each other"
        - "An arrow that points back to an earlier step, creating a cycle"
        - "A rectangle with double borders"
        - "A special LOOP symbol not used for any other purpose"
    markingRule:
      matchMode: EXACT
      accepted: ["An arrow that points back to an earlier step, creating a cycle"]
      rejectedFeedback: "Loops are shown by an arrow from a later step pointing back to an earlier step, creating a cycle in the flow."
    hint: "How would you show 'repeat' in a diagram that uses arrows for flow?"
    reflectionPrompt: "When you see a backward arrow in a flowchart, you know there is a loop. The decision diamond with a backward-pointing No branch is a classic while loop."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What shape is used for the START and END of a flowchart?"
    options: ["Rectangle", "Diamond", "Arrow", "Oval"]
    correctIndex: 3
    feedback: "Ovals (also called terminals or rounded rectangles) mark the start and end points of a flowchart."
  - type: MULTIPLE_CHOICE
    question: "A decision diamond in a flowchart always has how many exit arrows?"
    options: ["1", "2", "3", "It varies"]
    correctIndex: 1
    feedback: "A decision diamond has exactly two exits: one for Yes (true) and one for No (false)."
retrieval:
  recall: "Name the four main flowchart shapes and what each represents."
  explain: "How does a flowchart reveal logic that might be missed in pseudocode or plain English? Give a specific example."
  mistakeId:
    code: |
      // Student describes this flowchart:
      // START -> Check score -> Pass or Fail -> END
    answer: "The decision point is missing. 'Check score' is vague — it does not show the condition being evaluated or the Yes/No branches. The diamond should appear between 'Check score' and the outcomes, with an explicit question like 'score >= 50?' and YES/NO arrows."
---

# Hook

Some people think in words; others think in pictures. Flowcharts are a visual tool for planning algorithms — they map out every step, every decision, and every path through your logic before you write a single line of code. When a program has branches and loops, a flowchart reveals the structure at a glance. After this lesson, you will be able to read any flowchart and draw one for any simple algorithm.

# Lore Introduction

In the Academy's cartography hall, there hung a vast diagram — lines, shapes, and arrows mapping every path through the Grand Tome's most complex incantation. Veylan called it the "flow map". "Before this map existed," he explained, "twelve apprentices attempted the incantation and failed at the same decision point — they could not see the fork." The map made the fork visible: a glowing diamond shape with two arrows, one labelled YES and one NO. "Visual thinking," Veylan said, "catches what written thinking misses."

# Core Learning

## Concept Introduction

A **flowchart** is a diagram that shows the steps of an algorithm using standardised shapes connected by arrows.

**Core shapes:**

| Shape | Name | Meaning |
|---|---|---|
| Oval | Terminal | Start or End of the program |
| Rectangle | Process | An action or computation |
| Diamond | Decision | A yes/no condition — branches into two paths |
| Arrow | Flow line | Direction of execution |

**Reading a flowchart:** follow the arrows from Start. When you reach a diamond, take the YES or NO path based on the condition. When you reach End, the algorithm is done.

**Loops** appear as a backward arrow: the NO path of a decision loops back to an earlier process step.

## Why It Matters

Flowcharts make branching logic visual. When an algorithm has multiple conditions and loops, a flowchart reveals the structure at a glance. They also make it easy to spot missing cases — a decision diamond with only one exit arrow, or a path that never reaches End, is an obvious error. Flowcharts are used in software engineering, business process design, and system architecture.

## Worked Examples

**Example 1 — Simple if/else flowchart**

```
(START)
   |
[Get score]
   |
<score >= 50?>
   |         \
  YES         NO
   |           |
[Print "Pass"]  [Print "Fail"]
   |           |
(END)       (END)
```

**Example 2 — Loop (count from 1 to 3)**

```
(START)
   |
[SET i = 1]
   |
<i <= 3?>
   |    \
  YES    NO
   |      |
[Print i] (END)
   |
[i = i + 1]
   |
(back to diamond)
```

The NO path exits; the YES path continues the loop. The backward arrow from `[i = i + 1]` back to the diamond is what makes this a loop.

**Example 3 — Reading a flowchart to write pseudocode**

From the loop flowchart above, you can directly read off:
```
SET i TO 1
WHILE i <= 3
    PRINT i
    ADD 1 TO i
```

Flowcharts and pseudocode are interchangeable planning tools. Use whichever suits your thinking style.

## Common Mistakes

- **Using a rectangle for a decision.** Decisions must be diamonds so that two exits are natural.
- **Decision questions that are not yes/no.** "What is the score?" is not a valid decision. "Is score >= 50?" is.
- **Missing an End.** Every path through the flowchart should reach an End symbol.
- **Arrows without direction.** Every line must have an arrowhead showing which way the flow goes.
- **Loops with no exit condition.** A loop in a flowchart must have a decision diamond that can eventually take the exit path.

## Mental Model

Think of a flowchart as a **road map**. Rectangles are towns you pass through. Diamonds are intersections where you must choose a direction. Arrows are roads. Ovals are your start point (home) and destination. A loop is a roundabout — you keep circling until the condition lets you take the exit road.

## Mini Summary

- Flowcharts use shapes: oval (start/end), rectangle (process), diamond (decision), arrow (flow).
- Decision diamonds always have exactly two exits: YES and NO.
- Loops are shown by an arrow pointing back to an earlier step.
- Flowcharts make branching and looping logic visible at a glance.
- Every path through a flowchart should reach an End.
- Flowcharts and pseudocode serve the same planning purpose — choose your preferred style.

# Guided Practice Quest

Work through each step in order.

**Step 1.** Which flowchart shape represents a decision (yes/no question)?

**Step 2.** Describe in plain text the flowchart for: "If health is 0, print 'Game Over', otherwise print 'Alive'." Include start, decision, two process boxes, and end.

**Step 3.** How is a loop represented in a flowchart?

# Solo Practice Quest

Draw (describe in text) a flowchart for the following algorithm:

"Keep asking the user to guess a number. If they guess correctly, print 'You win!' and stop. If they guess wrong, print 'Try again' and repeat."

Your description must:
1. Name every shape used (oval, rectangle, diamond).
2. Label each YES and NO arrow.
3. Show where the loop back-arrow goes.
4. Explain how the flowchart reveals the loop in a way that a sentence description might not.

# Integration

**Mathematics connection — Graph theory**

A flowchart is a type of directed graph — a set of nodes (shapes) connected by directed edges (arrows). Graph theory is a branch of mathematics that studies exactly these structures. The concept of "reachability" in graph theory directly translates to: "Can this path in the flowchart ever reach the End symbol?" Checking for unreachable nodes in a flowchart is the same as checking for dead code in a program. Understanding the mathematical structure of flowcharts deepens your intuition about program flow and control structures.

**Psychology connection — Visual thinking**

Research in cognitive psychology shows that spatial and visual representations of information are processed by different brain systems than verbal descriptions. Flowcharts exploit this by encoding algorithm structure spatially: the layout on the page shows you which parts come before others, which paths exist, and where loops occur. For people who have stronger spatial reasoning than verbal reasoning, flowcharts can reveal structure that pseudocode obscures. Offering both representations (flowchart and pseudocode) covers both cognitive styles.

**Free question:** When would you choose a flowchart over pseudocode, and when would you choose pseudocode over a flowchart? Are there problem types that strongly favour one over the other?

# Lore Conclusion

The apprentice pinned her completed flow map to the cartography wall beside Veylan's masterwork. It was smaller — just five shapes and eight arrows — but it was correct. "I found the bug before I started," she announced. "The NO path never reached End." Veylan nodded. "Every missed End is a program that never finishes. Every missing branch is behaviour that was never considered." He studied her map. "You could not have seen this in words. The map showed you." She picked up her quill. Now, at last, it was time to cast the spell.
