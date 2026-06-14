---
id: se-app-m2-23
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
topicSlug: loops
topicTitle: "Loops"
topicSortOrder: 4
lesson: nested_loops
title: "Nested Loops"
sortOrder: 23
difficulty: 2
estimatedMinutes: 24
xpReward: 50
practiceType: JAVA
questType: PRACTICE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-22]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly explains that the inner loop completes all its iterations for each one iteration of the outer loop"
    - "Calculates the total iterations for nested loops: outer × inner"
    - "Gives a concrete example of nested loops (grid, multiplication table, 2D structure)"
    - "Explains the O(n²) performance concern for large inputs"
    - "Describes how to keep nested loops manageable (clear variable names, not more than 2-3 levels)"
  keywords: [nested, inner, outer, grid, iterations, O(n^2), performance, row, column]
  modelAnswer: |
    A nested loop is a loop inside another loop. The outer loop runs N times. For each of those N iterations, the inner loop runs M times. The total number of iterations is N × M.

    This is essential for processing two-dimensional structures: a grid of rows and columns, a multiplication table, or a matrix. The outer loop handles rows, the inner loop handles columns.

    When both N and M grow with the input size (e.g., both equal n), the total work is n², described as O(n²) complexity. For small n this is fine; for n = 10,000, it is 100 million iterations, which can be slow.

    Example:
    ```java
    for (int row = 1; row <= 3; row++) {
        for (int col = 1; col <= 3; col++) {
            System.out.print(row + "," + col + " ");
        }
        System.out.println();
    }
    ```
    Output: visits all 9 cells of a 3×3 grid.
guidedSteps:
  - id: se-app-m2-23-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      ```java
      for (int i = 1; i <= 3; i++) {
          for (int j = 1; j <= 2; j++) {
              System.out.println(i + "-" + j);
          }
      }
      ```
      How many lines does this print?
    inputConfig:
      options:
        - "3"
        - "5"
        - "6"
        - "9"
    markingRule:
      matchMode: EXACT
      accepted: ["6"]
      rejectedFeedback: "The outer loop runs 3 times (i = 1, 2, 3). For each outer iteration, the inner loop runs 2 times (j = 1, 2). Total: 3 × 2 = 6 lines. They print: 1-1, 1-2, 2-1, 2-2, 3-1, 3-2."
    hint: "Multiply the outer loop's iteration count by the inner loop's iteration count."
    reflectionPrompt: "The total iterations in a nested loop is always outer × inner. This multiplicative relationship is what makes nested loops powerful for 2D structures — and what makes them slow for large inputs."

  - id: se-app-m2-23-step2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Write nested for loops that print a 4×4 multiplication table. Each cell should print `row * col` separated by a tab. Print a newline after each row.
    inputConfig:
      language: java
      starterCode: |
        // Write nested for loops for a 4x4 multiplication table
    markingRule:
      matchMode: CONTAINS
      accepted: ["for", "for", "4", "*", "println"]
      rejectedFeedback: |
        ```java
        for (int row = 1; row <= 4; row++) {
            for (int col = 1; col <= 4; col++) {
                System.out.print(row * col + "\t");
            }
            System.out.println();
        }
        ```
        The outer loop controls rows, the inner loop controls columns. `System.out.print` (without ln) keeps values on the same line; `System.out.println()` after the inner loop moves to the next row.
    hint: "Use `System.out.print()` for values on the same row and `System.out.println()` after the inner loop ends."
    reflectionPrompt: "The outer loop controls which row you are on; the inner loop fills all columns for that row. This outer=row, inner=column pattern applies to most 2D traversal problems."

  - id: se-app-m2-23-step3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      If an outer loop runs 1,000 times and the inner loop runs 1,000 times, how many total iterations occur? Why might this be a concern for performance?
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: ["1,000,000", "1000000", "million", "slow", "performance", "O(n^2)", "quadratic"]
      rejectedFeedback: "1,000 × 1,000 = 1,000,000 iterations. At even 100 million simple operations per second, that is 10 milliseconds — potentially noticeable. For larger inputs (10,000 × 10,000 = 100 million), this becomes a genuine performance concern. This quadratic growth is called O(n²) complexity."
    hint: "Multiply the two counts. How does that number grow as inputs get larger?"
    reflectionPrompt: "Nested loops are a performance hotspot. Before writing them for large data, ask: is there a way to solve this without iterating every combination?"

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What happens to the inner loop when the outer loop completes one iteration?"
    options:
      - "The inner loop pauses until the outer loop finishes all iterations"
      - "The inner loop resets to its start and runs all its iterations again for the next outer iteration"
      - "The inner loop runs once in total, shared across all outer iterations"
      - "The inner loop variable is shared with the outer loop variable"
    correctIndex: 1
    feedback: "For every single iteration of the outer loop, the inner loop runs completely — from its initialisation to its termination. Then the outer loop advances one step, and the inner loop runs completely again. This is why total iterations = outer × inner."

  - type: MULTIPLE_CHOICE
    question: "What does O(n²) mean in the context of nested loops?"
    options:
      - "The loop uses n² megabytes of memory"
      - "The total number of iterations grows proportionally to n squared as input size n increases"
      - "The loop must run exactly n² times"
      - "The loop is n times slower than a single loop"
    correctIndex: 1
    feedback: "O(n²) means the work grows proportionally to the square of the input size. If n doubles from 10 to 20, the work does not double — it quadruples (100 to 400). This is because both loops scale with n, and n × n = n²."

retrieval:
  recall: "Write nested for loops that print all pairs (i, j) where i goes from 1 to 3 and j goes from 1 to 3."
  explain: "Explain why nested loops produce O(n²) iterations when both loops depend on n."
  mistakeId:
    code: |
      for (int i = 0; i < 3; i++) {
          for (int i = 0; i < 3; i++) {
              System.out.println(i);
          }
      }
    answer: "Both loops use the same variable name `i`. The inner `i` shadows (hides) the outer `i` within the inner loop's scope. This compiles but produces confusing behaviour — the outer `i` never appears to change from the inner loop's perspective. The fix is to use different variable names: outer `i`, inner `j` (or any distinct names)."
---

# Hook

You know how to loop through a list of items. But what about a grid? A chessboard has 8 rows and 8 columns — 64 cells. A multiplication table has 10 rows and 10 columns — 100 products. Traversing any two-dimensional structure requires repeating for both dimensions: for each row, for each column in that row. That is a loop inside a loop: a nested loop. Powerful, precise, and — with large inputs — potentially expensive.

# Lore Introduction

"A single repetition rune traverses a line," Archmage Veylan says, drawing a row of glowing dots. "But what if the academy's enchanted grid spans both length and width?" He adds a second dimension and the dots multiply into a full matrix. "You need the inner rune to sweep each row while the outer rune advances each column." He draws a loop nested within a loop. "Two runes, each counting, each driving the other. For every beat of the outer rune, the inner rune plays its full song." He pauses. "Beautiful — and expensive. Use it wisely."

# Core Learning

## Concept Introduction

A **nested loop** is a loop placed inside the body of another loop:

```java
for (int outer = 0; outer < n; outer++) {
    for (int inner = 0; inner < m; inner++) {
        // body: runs n × m times total
    }
}
```

**How it works:**
- The outer loop advances one iteration.
- The inner loop runs **all its iterations** completely.
- The outer loop advances again.
- The inner loop runs all its iterations again.
- Repeat until the outer loop finishes.

**Total iterations:** outer count × inner count = N × M.

**Common pattern — 2D grid traversal:**
```
Outer loop = rows
Inner loop = columns
Body = process cell at (row, column)
```

## Why It Matters

Nested loops are essential for processing any two-dimensional structure: images (pixels as rows × columns), spreadsheets, game boards, matrices, and multi-level hierarchies. They are also used for algorithms like comparing every pair of items in a list.

## Worked Examples

**Example 1 — Print coordinates of a 3×3 grid:**
```java
for (int row = 1; row <= 3; row++) {
    for (int col = 1; col <= 3; col++) {
        System.out.print("(" + row + "," + col + ") ");
    }
    System.out.println(); // new line after each row
}
// (1,1) (1,2) (1,3)
// (2,1) (2,2) (2,3)
// (3,1) (3,2) (3,3)
```

**Example 2 — Multiplication table:**
```java
for (int i = 1; i <= 5; i++) {
    for (int j = 1; j <= 5; j++) {
        System.out.printf("%4d", i * j);
    }
    System.out.println();
}
//    1   2   3   4   5
//    2   4   6   8  10
//    3   6   9  12  15
//    4   8  12  16  20
//    5  10  15  20  25
```

**Example 3 — Find duplicates in an array (O(n²) algorithm):**
```java
int[] nums = {3, 1, 4, 1, 5};
for (int i = 0; i < nums.length; i++) {
    for (int j = i + 1; j < nums.length; j++) {
        if (nums[i] == nums[j]) {
            System.out.println("Duplicate: " + nums[i]);
        }
    }
}
// Prints: Duplicate: 1
```

## Common Mistakes

- **Reusing the same loop variable name:** `for (int i ...) { for (int i ...) }` — the inner `i` shadows the outer. Use `i` and `j` (or descriptive names).
- **Underestimating the iteration count:** 100 outer × 100 inner = 10,000 iterations. 1,000 × 1,000 = 1,000,000. Scale awareness matters.
- **Putting post-row logic inside the inner loop:** `System.out.println()` for a new line should be after the inner loop closes, not inside it.
- **More than 2-3 levels of nesting:** Three nested loops (O(n³)) is usually a sign to look for a smarter algorithm.
- **Forgetting that each loop variable is independent:** Changing `j` in the inner loop does not affect `i` in the outer loop.

## Mental Model

Think of nested loops as a **printer scanning a page**. The printer head starts at the top-left. The inner loop sweeps across the full width of one line (left to right). Then the outer loop advances to the next line. The inner loop sweeps the width again. This continues until the outer loop has covered all lines. Every cell of the page is visited exactly once.

## Mini Summary

- A nested loop is a loop inside another loop.
- For each outer iteration, the inner loop runs completely (all its iterations).
- Total iterations = outer count × inner count.
- Use nested loops for 2D structures: grids, tables, matrices.
- When both loops scale with input size n, total work is O(n²) — quadratic.
- Keep nesting to 2 levels maximum; more than that usually signals a design problem.

# Guided Practice Quest

*"The Academy's enchanted chessboard needs every square labelled with its row and column," Archmage Veylan explains. "Write nested for loops that print all 64 coordinates of an 8×8 board: (1,1) through (8,8). Print each row on one line." Trace the first two rows by hand before coding.*

# Solo Practice Quest

**The Star Triangle**

Write nested for loops that print this triangle:

```
*
**
***
****
*****
```

Where row `i` contains `i` stars (rows go from 1 to 5).

Then extend your solution to print an inverted triangle (5 stars on the first row, 1 on the last). Explain what you changed in the loop.

# Integration

**Mathematics connection:** Nested loops directly implement **double summation** from mathematics: ∑_{i=1}^{n} ∑_{j=1}^{m} f(i,j). The outer sum iterates i; for each i, the inner sum iterates j. This is the same structure as two nested for loops. Matrix multiplication is computed as a triple nested loop (n³ complexity), and many geometric algorithms use nested loops to compare pairs of objects. The connection between mathematical notation and loop structure is direct and powerful.

**Psychology connection:** Cognitive psychologists studying problem-solving have found that humans are poor at intuiting exponential and polynomial growth. When someone says "this nested loop is slow for large inputs", non-programmers often underestimate by orders of magnitude. A useful practice is to always estimate: "outer is N, inner is N, so total is N²." For N = 1,000, that is 1,000,000 — a concrete number that makes the cost tangible. This habit of explicit estimation is a hallmark of experienced engineers.

*Free question: Is there a way to print all pairs (i, j) where i ≤ j (i.e., avoiding the symmetric pair) using nested loops? How many pairs would that produce for n = 5? How does that relate to combinatorics (choosing 2 items from n)?*

# Lore Conclusion

The enchanted grid glows with 64 labelled squares, each inscribed by the nested rune. "Every cell visited, once and exactly once," Archmage Veylan says, stepping back to admire the completed grid. "This is the power of nested iteration." Then he dims the glow slightly. "But imagine a grid not of 64 squares, but of 10,000. Or 1,000,000." He lets the implication settle. "Power must be tempered with awareness of cost. Two loops multiplied is not twice the work — it is squared." He turns to the next scroll. "Now: what happens when a loop never ends? And what tools exist to cut it short when needed?"
