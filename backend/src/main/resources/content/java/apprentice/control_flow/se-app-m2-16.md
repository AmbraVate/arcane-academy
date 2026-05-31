---
id: se-app-m2-16
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
topicSlug: control_flow
topicTitle: "Control Flow"
topicSortOrder: 3
lesson: else_and_else_if
title: "Else & Else If"
sortOrder: 16
difficulty: 1
estimatedMinutes: 22
xpReward: 50
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-15]
integrationDomains: [philosophy, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly explains `else` as the fallback when the `if` condition is false"
    - "Correctly explains `else if` for chaining multiple mutually exclusive conditions"
    - "Explains that only the first matching branch in an else-if chain executes"
    - "Demonstrates understanding that ordering of conditions matters in an else-if chain"
    - "Describes a scenario where else-if is more appropriate than separate if statements"
  keywords: [else, else if, fallback, chain, branch, mutually exclusive, condition, order]
  modelAnswer: |
    The `else` clause provides a fallback block of code that runs when the preceding `if` condition is false. This creates a two-branch decision: one branch for true, one for false. Exactly one of the two branches always runs.

    When you need more than two branches, `else if` lets you chain additional conditions. The program evaluates each condition in order and executes the body of the first condition that evaluates to true. Once a matching branch is found, all remaining `else if` and `else` clauses are skipped. This means the branches are mutually exclusive — only one can run per evaluation.

    Ordering matters because conditions are checked top to bottom. If you place a broader condition before a more specific one, the broader one will match first and the specific case will never be reached. For example, checking `score >= 60` before `score >= 90` means you will never reach the 90 branch for a score of 95 — it would be caught by the 60 check first.

    A scenario where else-if is better than separate ifs: assigning a letter grade. Once you know a student scored an A, there is no reason to also check if they scored a B or C. Using separate `if` statements would check all conditions regardless, which is less efficient and more confusing.
guidedSteps:
  - id: se-app-m2-16-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      ```java
      int score = 55;
      if (score >= 90) {
          System.out.println("A");
      } else if (score >= 70) {
          System.out.println("B");
      } else if (score >= 60) {
          System.out.println("C");
      } else {
          System.out.println("F");
      }
      ```
      What is printed?
    inputConfig:
      options:
        - "A"
        - "B"
        - "C"
        - "F"
    markingRule:
      matchMode: EXACT
      accepted: ["F"]
      rejectedFeedback: "55 is not >= 90, not >= 70, and not >= 60. All three `else if` conditions are false. So the final `else` (the fallback) runs and prints 'F'."
    hint: "Check each condition in order: is 55 >= 90? Is 55 >= 70? Is 55 >= 60?"
    reflectionPrompt: "When none of the `if` and `else if` conditions match, the `else` block is the safety net. It guarantees that something happens even when no specific condition is true."

  - id: se-app-m2-16-step2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Write an if/else statement for a variable `isOnline`. If `isOnline` is true, print "Connected". Otherwise, print "Offline". Assume `isOnline` is already declared as a boolean.
    inputConfig:
      language: java
      starterCode: |
        boolean isOnline = false;
        // Write your if/else below
    markingRule:
      matchMode: CONTAINS
      accepted: ["if", "isOnline", "else", "Connected", "Offline"]
      rejectedFeedback: |
        ```java
        if (isOnline) {
            System.out.println("Connected");
        } else {
            System.out.println("Offline");
        }
        ```
        The `else` block runs when `isOnline` is false. Exactly one of the two branches always executes.
    hint: "Use `if (isOnline)` for the true case, and `else` for the false case."
    reflectionPrompt: "With `if/else`, exactly one branch always runs. This guarantees the program always produces an output, regardless of the condition's value."

  - id: se-app-m2-16-step3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain why ordering conditions matters in an `else if` chain. Give an example where wrong ordering causes a bug.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["first", "order", "match", "never", "reach", "skipped", "caught"]
      rejectedFeedback: "If you write `if (x > 0)` before `if (x > 100)`, then any value over 100 will match the first condition and the second will never be checked. The first matching branch wins. Broader conditions must come after narrower ones, not before."
    hint: "Think about what happens when the first condition matches — are the remaining conditions checked?"
    reflectionPrompt: "Conditions are evaluated in order and the first true one wins. Put your most specific conditions first (or most restrictive) so broader conditions don't swallow them."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In an if/else if/else chain, how many branches can execute for a single evaluation?"
    options:
      - "All branches that have a true condition"
      - "None — the program skips all of them"
      - "Exactly one — the first branch with a true condition, or else if no condition matches"
      - "The last branch always executes"
    correctIndex: 2
    feedback: "Exactly one branch executes in an if/else if/else chain. The program checks conditions in order and runs the body of the first one that is true, then skips all remaining branches. If none are true, the `else` block runs (if present)."

  - type: MULTIPLE_CHOICE
    question: "What is the difference between using three separate `if` statements versus an `if/else if/else` chain?"
    options:
      - "There is no difference — they work identically"
      - "Separate `if` statements each check independently; `else if` skips remaining checks once one matches"
      - "`else if` is faster because it uses less memory"
      - "Separate `if` statements cannot use boolean conditions"
    correctIndex: 1
    feedback: "With three separate `if` statements, all three conditions are evaluated every time, regardless of which ones match. With `else if`, once a match is found the rest are skipped. For mutually exclusive categories (like grade bands), `else if` is more correct because it prevents multiple matches."

retrieval:
  recall: "Write a Java if/else if/else chain that prints 'High' for a score above 80, 'Medium' for above 50, and 'Low' otherwise."
  explain: "Why is it a mistake to put a broad condition (like score >= 50) before a narrow one (like score >= 90) in an else-if chain?"
  mistakeId:
    code: |
      int score = 95;
      if (score >= 50) {
          System.out.println("Pass");
      } else if (score >= 90) {
          System.out.println("Distinction");
      }
    answer: "The condition `score >= 50` matches first for a score of 95, printing 'Pass' and skipping the `else if`. The 'Distinction' branch is unreachable for any score because any score >= 90 is also >= 50. The fix is to put the more specific/restrictive condition first: check `score >= 90` before `score >= 50`."
---

# Hook

You have learned to ask one question: "Is this true?" But most real decisions have more than two outcomes. A traffic light is not just "green or not green" — it is green, amber, or red. A game score is not just "high or not high" — it is S-tier, A-tier, B-tier, or failing. Real software must navigate a landscape of multiple possibilities, each mapped to a specific response. The `else` and `else if` clauses give your `if` statement the power to handle not just one condition, but an entire decision tree.

# Lore Introduction

"A binding rune that only casts when conditions are right is useful," Archmage Veylan concedes, "but what about when conditions are *wrong*? Do you simply do nothing?" He draws a second rune alongside the first, connected by a glowing thread. "The fallback rune — `else` — activates precisely when the first does not. Together, they form a complete decision: cast one spell or the other, never both, never neither." He adds a third rune to the chain. "And when you need more than two outcomes? You link them. That is the `else if` chain — a sequence of choices where only the first matching rune fires."

# Core Learning

## Concept Introduction

**`else` — the fallback:**
```java
if (condition) {
    // runs when condition is true
} else {
    // runs when condition is false
}
```

Exactly one of the two branches always runs.

**`else if` — chaining conditions:**
```java
if (condition1) {
    // runs when condition1 is true
} else if (condition2) {
    // runs when condition1 is false AND condition2 is true
} else {
    // runs when all conditions above are false
}
```

Key rules:
- Conditions are checked **top to bottom**.
- **Only the first matching branch runs.** All others are skipped.
- The `else` at the end is optional but acts as a catch-all.

## Why It Matters

Most decisions in software have more than two outcomes. `else if` chains let you map a range of possible states to a range of responses in a single, readable structure. Without them, you would need multiple separate `if` statements, which creates bugs when conditions overlap — a score of 95 would print "Distinction" and "Pass" and "Adequate" separately, all three.

## Worked Examples

**Example 1 — Two branches with if/else:**
```java
int balance = -10;
if (balance >= 0) {
    System.out.println("Account is in credit.");
} else {
    System.out.println("Account is overdrawn.");
}
// Prints: Account is overdrawn.
```

**Example 2 — Grade classification with else if:**
```java
int score = 78;
if (score >= 90) {
    System.out.println("A");
} else if (score >= 80) {
    System.out.println("B");
} else if (score >= 70) {
    System.out.println("C");
} else {
    System.out.println("F");
}
// Prints: C
```

**Example 3 — Ordering error (bug):**
```java
int score = 95;
// BUG: broad condition before narrow
if (score >= 60) {
    System.out.println("Pass");          // This matches first!
} else if (score >= 90) {
    System.out.println("Distinction");   // Never reached for any valid score
}
// Fix: put the narrow condition first
```

## Common Mistakes

- **Broad conditions before narrow ones:** `if (x > 0)` before `if (x > 100)` means the second is unreachable for positive values.
- **Forgetting that else is optional:** You only need `else` if you want a fallback. If doing nothing on mismatch is correct, omit it.
- **Using separate `if` statements when you want mutual exclusion:** Three separate `if` statements can all fire; an `else if` chain fires only once.
- **Putting the most common case last:** For performance and readability, the most likely condition should be first.
- **Accidentally missing a closing brace:** Each branch needs its own `{ }`. Mismatched braces cause compile errors or logic bugs.

## Mental Model

Imagine a **waterfall of buckets**. Water (your program's value) falls from above. The first bucket tries to catch it — if its condition matches, it catches the water and the rest of the buckets are empty. If the first bucket's condition does not match, the water flows past to the next bucket. The water always lands in exactly one bucket (or falls to the floor if there is no `else` and no conditions match). The `else` is the floor — a guaranteed catch.

## Mini Summary

- `else` provides a fallback block that runs when the `if` condition is false.
- `else if` adds additional conditions, checked in order after the first `if`.
- Only the first matching branch executes; all others are skipped.
- Put more specific (narrower) conditions before broader ones.
- `else` is optional — omit it when no fallback action is needed.
- Use `else if` chains when branches are mutually exclusive (only one should ever fire).

# Guided Practice Quest

*Archmage Veylan presents a magical crystal that glows different colours based on energy levels. "It glows red above 80 energy, yellow between 40 and 80, green below 40, and goes dark at 0." He looks at you. "Write the binding rune chain that maps energy to colour. Remember — only one colour at a time."*

# Solo Practice Quest

**The Sorting Hat Problem**

Imagine a sorting hat that assigns students to one of four houses based on their personality score (0–100):

- Score 90–100: Arcane (intellect)
- Score 70–89: Ember (courage)
- Score 50–69: Tide (wisdom)
- Score 0–49: Gale (adaptability)

Write the `else if` chain in Java. Then answer: what would go wrong if you wrote the conditions in reverse order (Gale first, Arcane last)? Explain in 2-3 sentences.

# Integration

**Mathematics connection:** In mathematics, a **piecewise function** defines a function using different expressions for different intervals of the input. For example: f(x) = x² if x ≥ 0, or -x if x < 0. This is identical in structure to an `else if` chain: each condition defines a domain, and the associated branch defines the output for that domain. Writing an `else if` chain is, in essence, implementing a piecewise function in code.

**Philosophy connection:** The philosopher Aristotle described the "law of excluded middle" — for any proposition P, either P is true or P is not true; there is no third option. An `if/else` structure embodies this: either the condition is true (first branch) or it is not (else branch). The `else if` chain extends this by breaking the "not true" case into sub-cases. This is analogous to a philosophical decision tree, where each node branches into mutually exclusive possibilities until a conclusion is reached.

*Free question: Can you have an `else if` without a final `else`? What happens if none of the conditions match? Is there a situation where that is correct behaviour?*

# Lore Conclusion

The chain of runes glows in sequence as the crystal's energy is measured — red, yellow, green. Only one rune fires. "Notice," Archmage Veylan says, "that the crystal never displays two colours simultaneously. That is because each rune yields to the next only when it cannot activate." He steps back and observes the chain. "You have now mastered the two-rune and three-rune chain. In the next lesson, you will encounter the `switch` — a different structure suited for a different kind of choice. The binding rune chain and the switch are both valid tools; a master knows which to reach for."
