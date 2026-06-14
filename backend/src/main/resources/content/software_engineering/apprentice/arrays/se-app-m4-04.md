---
id: se-app-m4-04
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m4
moduleTitle: "Module 4: Data Structures"
moduleGlyph: "📦"
moduleSortOrder: 4
topicSlug: arrays
topicTitle: "Arrays"
topicSortOrder: 1
lesson: iteration
title: "Iteration"
sortOrder: 4
difficulty: 2
estimatedMinutes: 25
xpReward: 60
practiceType: JAVA
questType: PRACTICE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m4-03]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Writes a for loop that iterates over all array elements using index"
    - "Writes a for-each loop that iterates over all elements"
    - "Computes the sum of all elements in an array"
    - "Finds the maximum value in an array using a loop"
    - "Explains when to use for vs for-each"
  keywords: [loop, for, foreach, index, iterate, sum, max, accumulate, element]
  modelAnswer: |
    int[] scores = {80, 95, 70, 88, 60};

    // Sum with for loop:
    int sum = 0;
    for (int i = 0; i < scores.length; i++) {
        sum += scores[i];
    }

    // Max with for loop:
    int max = scores[0];
    for (int i = 1; i < scores.length; i++) {
        if (scores[i] > max) max = scores[i];
    }

    // For-each (read-only):
    for (int score : scores) {
        System.out.println(score);
    }
guidedSteps:
  - id: gs-m4-04-1
    sortOrder: 1
    inputType: CODE
    instruction: |
      Write a for loop that prints every element of: `int[] numbers = {3, 6, 9, 12};`
    inputConfig:
      placeholder: |
        int[] numbers = {3, 6, 9, 12};
        // write the for loop here
    markingRule:
      matchMode: CONTAINS
      accepted: ["for", "numbers.length", "numbers[i]", "System.out.println"]
      rejectedFeedback: "for (int i = 0; i < numbers.length; i++) { System.out.println(numbers[i]); }"
    hint: "Loop from i = 0 to i < numbers.length, and print numbers[i] each iteration."
    reflectionPrompt: "i < numbers.length ensures you never access an out-of-bounds index — the loop stops at the last valid index."
  - id: gs-m4-04-2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Rewrite the same loop using a for-each loop (enhanced for loop).
    inputConfig:
      placeholder: |
        int[] numbers = {3, 6, 9, 12};
        // write the for-each loop here
    markingRule:
      matchMode: CONTAINS
      accepted: ["for", "int", "numbers", "System.out.println"]
      rejectedFeedback: "for (int n : numbers) { System.out.println(n); }"
    hint: "For-each syntax: for (type variable : array) { ... }"
    reflectionPrompt: "For-each is cleaner for read-only iteration. Use the indexed for loop when you need the index value itself."
  - id: gs-m4-04-3
    sortOrder: 3
    inputType: CODE
    instruction: |
      Write code to compute the sum of `int[] values = {5, 10, 15, 20};` and print the result.
    inputConfig:
      placeholder: |
        int[] values = {5, 10, 15, 20};
        // compute and print sum
    markingRule:
      matchMode: CONTAINS
      accepted: ["int sum", "values.length", "sum +=", "System.out.println"]
      rejectedFeedback: "int sum = 0; for (int i = 0; i < values.length; i++) { sum += values[i]; } System.out.println(sum);"
    hint: "Start with int sum = 0, then add each element to sum inside the loop."
    reflectionPrompt: "An accumulator variable (sum = 0 before the loop, sum += element inside) is the standard pattern for summing."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which loop type gives you the INDEX of the current element during iteration?"
    options:
      - "for-each loop"
      - "while loop with no counter"
      - "Standard for loop with int i"
      - "Both loops provide the index"
    correctIndex: 2
    feedback: "The standard for loop (for int i = 0; i < arr.length; i++) gives you i as the index. For-each only gives you the element value."
  - type: MULTIPLE_CHOICE
    question: "To find the maximum value in an array, you should initialise your `max` variable to:"
    options:
      - "0"
      - "Integer.MAX_VALUE"
      - "The first element of the array (arr[0])"
      - "-1"
    correctIndex: 2
    feedback: "Initialise max = arr[0] so you start with a real value from the array. Starting at 0 could fail if all elements are negative."
retrieval:
  recall: "Write a for loop that computes the sum of an int array named `data`."
  explain: "When would you choose a for-each loop over a standard indexed for loop? Give a concrete reason for each choice."
  mistakeId:
    code: |
      int[] nums = {1, 2, 3, 4, 5};
      int max = 0;
      for (int n : nums) {
          if (n > max) max = n;
      }
      System.out.println(max);
    answer: "Initialising max to 0 works here (all elements are positive) but would fail if the array contained only negative numbers — max would remain 0, which is not in the array. Correct initialisation: max = nums[0]; This guarantees max starts as an actual array element."
---

# Hook

You have an array of scores. How do you print them all? How do you add them up? How do you find the highest? The answer to all three is iteration — looping over every element in the array. Java gives you two loop styles for arrays: the standard `for` loop (which gives you the index) and the `for-each` loop (which gives you the element directly). Mastering both is essential, and this lesson shows you when to use each.

# Lore Introduction

The apprentice stared at a rune ledger with a hundred entries. "I need the total power of all these components," she said. "Do I read each slot individually?" Veylan shook his head. "You iterate. You pass through every slot in order, accumulating the sum as you go." He traced a pattern in the air — a repeating loop, slot by slot, entry by entry. "The standard loop gives you the slot number so you can reach in and read. The simplified loop gives you each entry in turn, without the slot number." He smiled. "Both traverse the ledger. The choice depends on whether you need to know the slot."

# Core Learning

## Concept Introduction

**Standard for loop (indexed):**
```java
int[] scores = {80, 95, 70, 88};
for (int i = 0; i < scores.length; i++) {
    System.out.println(scores[i]);
}
```
Use this when you need the index `i` (e.g. to update elements, access two arrays at once, or know the position).

**For-each loop (enhanced for):**
```java
for (int score : scores) {
    System.out.println(score);
}
```
Use this for **read-only** iteration when you only need the element value, not its index.

**Computing sum:**
```java
int sum = 0;
for (int score : scores) {
    sum += score;
}
System.out.println("Sum: " + sum);
```

**Finding maximum:**
```java
int max = scores[0];
for (int i = 1; i < scores.length; i++) {
    if (scores[i] > max) {
        max = scores[i];
    }
}
System.out.println("Max: " + max);
```

## Why It Matters

Iterating over collections is one of the most fundamental operations in programming. Virtually every real program processes lists of data — computing totals, finding extremes, filtering, transforming. Being fluent with both loop styles means you can choose the right tool for each situation quickly and write code that other developers can read instantly.

## Worked Examples

**Example 1 — Print all with for-each**

```java
String[] spells = {"Fireball", "Icebolt", "Heal"};
for (String spell : spells) {
    System.out.println(spell);
}
// Fireball
// Icebolt
// Heal
```

**Example 2 — Sum with accumulator**

```java
int[] prices = {10, 25, 8, 40, 15};
int total = 0;
for (int price : prices) {
    total += price;
}
System.out.println("Total: " + total); // 98
```

**Example 3 — Find minimum**

```java
int[] temps = {22, 18, 30, 15, 27};
int min = temps[0];
for (int i = 1; i < temps.length; i++) {
    if (temps[i] < min) {
        min = temps[i];
    }
}
System.out.println("Min: " + min); // 15
```

## Common Mistakes

- **Using `<=` instead of `<` in the for condition.** `i <= scores.length` accesses one index past the end.
- **Initialising max/min to 0.** This fails for all-negative arrays. Always initialise to `arr[0]`.
- **Modifying array elements with a for-each loop.** `for (int x : arr) { x = 0; }` does NOT modify the array — `x` is a copy.
- **Starting the max/min search at index 0 again.** If you initialise `max = arr[0]`, start the loop at index 1 to avoid re-comparing the first element.
- **Forgetting that sum starts at 0.** `int sum;` without `= 0` will cause a compile error when used in `sum +=`.

## Mental Model

Think of iteration as a **mail carrier** walking a row of post-boxes. The standard for loop is a carrier who announces the box number (`"Box 3: letter inside"`). The for-each loop is a carrier who just hands you each letter without mentioning the box number. Choose the for-each carrier when you don't care about the box number; choose the indexed carrier when you need it.

## Mini Summary

- `for (int i = 0; i < arr.length; i++)` — indexed loop, gives access to the index.
- `for (int x : arr)` — for-each, cleaner for read-only iteration.
- Sum pattern: start accumulator at 0, add each element inside the loop.
- Max/min pattern: initialise to `arr[0]`, loop from index 1, update on each comparison.
- For-each copies each element — modifying the copy does not affect the array.
- Never use `i <= arr.length` — use `i < arr.length`.

# Guided Practice Quest

Work through each step in order.

**Step 1.** Write a for loop that prints every element of `int[] numbers = {3, 6, 9, 12}`.

**Step 2.** Rewrite the same loop using a for-each loop.

**Step 3.** Write code to compute and print the sum of `int[] values = {5, 10, 15, 20}`.

# Solo Practice Quest

Write a complete Java method called `analyseScores` that takes an `int[]` parameter. The method should:
1. Print all scores using a for-each loop.
2. Compute and print the total.
3. Compute and print the maximum score.
4. Compute and print the minimum score.

Then call it with `{45, 90, 73, 88, 61}`. Include a comment on each loop explaining why you chose that loop style (indexed vs for-each).

# Integration

**Mathematics connection — Sigma notation and accumulators**

Mathematical sigma notation Σᵢ₌₀ⁿ⁻¹ aᵢ is the direct mathematical counterpart of the sum accumulator loop. The index variable `i` maps to the loop variable, the upper bound `n-1` maps to `arr.length - 1`, and the accumulator variable maps to the partial sum. Understanding this correspondence means you can translate mathematical formulas into loops (and vice versa) mechanically. This skill is essential in numerical computing, statistics, and algorithm analysis.

**Philosophy connection — The whole and its parts**

Philosophers distinguish between a collection as a whole and its individual parts. When you iterate over an array, you are temporarily treating the collection as a sequence of parts. When you compute the sum, you synthesise those parts back into a single whole (the total). This movement from whole to parts (analysis) and back to whole (synthesis) is a fundamental pattern in both philosophy and algorithm design. Recognising it helps you see loops not as mere repetition but as a structured way of relating parts to wholes.

**Free question:** The for-each loop cannot modify array elements because it works on a copy. Why might this be considered a safety feature rather than a limitation? When would you want to guarantee that iteration cannot accidentally modify the data?

# Lore Conclusion

The apprentice completed her pass through the rune ledger, slot by slot, accumulating the total power of all one hundred components. The final number glowed in her summation vessel. "With a loop," she said slowly, "I didn't have to read each slot separately. The iteration read them all." Veylan nodded. "Iteration is how incantations process collections. Without it, you would write one hundred reads. With it, you write one loop." He pointed at the result. "The ledger is traversed. The total is known. That is the power of iteration."
