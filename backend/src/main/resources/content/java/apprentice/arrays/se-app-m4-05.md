---
id: se-app-m4-05
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m4
moduleTitle: "Module 4: Data Structures"
moduleGlyph: "📦"
moduleSortOrder: 4
topicSlug: arrays
topicTitle: "Arrays"
topicSortOrder: 1
lesson: common_array_mistakes
title: "Common Array Mistakes"
sortOrder: 5
difficulty: 2
estimatedMinutes: 20
xpReward: 50
practiceType: JAVA
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m4-04]
integrationDomains: [psychology, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies the off-by-one error in loop boundary conditions"
    - "Explains why == does not compare array contents"
    - "Uses Arrays.equals() to compare two arrays correctly"
    - "Explains the danger of hardcoding the array size"
    - "Describes the difference between arr.length and the last valid index"
  keywords: [off-by-one, equals, Arrays.equals, hardcode, length, bounds, compare, mistake]
  modelAnswer: |
    int[] a = {1, 2, 3};
    int[] b = {1, 2, 3};

    // Wrong: compares references, not contents
    System.out.println(a == b);              // false

    // Correct: compares contents
    System.out.println(Arrays.equals(a, b)); // true

    // Off-by-one: i <= a.length causes out-of-bounds
    // Correct: i < a.length
guidedSteps:
  - id: gs-m4-05-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does `arr1 == arr2` check when `arr1` and `arr2` are arrays?
    inputConfig:
      options:
        - "Whether the arrays contain the same values"
        - "Whether the arrays have the same length"
        - "Whether arr1 and arr2 refer to the same array object in memory"
        - "Whether all elements in arr1 are less than all elements in arr2"
    markingRule:
      matchMode: EXACT
      accepted: ["Whether arr1 and arr2 refer to the same array object in memory"]
      rejectedFeedback: "== on objects (including arrays) checks reference equality — whether both variables point to the same object in memory, not whether they contain the same values."
    hint: "Arrays are objects in Java. == on objects checks identity, not equality of contents."
    reflectionPrompt: "Use Arrays.equals(a, b) to compare contents. Use == only when you want to check if two variables point to the exact same array."
  - id: gs-m4-05-2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Fix this loop so it does not cause an ArrayIndexOutOfBoundsException:
      ```java
      int[] nums = {1, 2, 3, 4, 5};
      for (int i = 0; i <= nums.length; i++) {
          System.out.println(nums[i]);
      }
      ```
    inputConfig:
      placeholder: |
        int[] nums = {1, 2, 3, 4, 5};
        // fixed loop here
    markingRule:
      matchMode: CONTAINS
      accepted: ["i < nums.length"]
      rejectedFeedback: "Change <= to <. The condition should be i < nums.length, not i <= nums.length."
    hint: "The last valid index is nums.length - 1. The loop should stop when i equals nums.length."
    reflectionPrompt: "i <= nums.length iterates one too many times. The last iteration accesses nums[5] on a 5-element array — out of bounds."
  - id: gs-m4-05-3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      A developer writes `for (int i = 0; i < 5; i++)` to iterate over an array.
      The array size later changes to 8. What is the problem?
    inputConfig:
      options:
        - "The loop will throw an exception immediately"
        - "The loop will only process the first 5 elements, silently ignoring the last 3"
        - "The loop will process all 8 elements correctly"
        - "The code will not compile"
    markingRule:
      matchMode: EXACT
      accepted: ["The loop will only process the first 5 elements, silently ignoring the last 3"]
      rejectedFeedback: "Hardcoded size (5) means the loop stops early when the array grows. Use arr.length to make the loop adapt automatically."
    hint: "What happens when the loop condition is i < 5 but the array has 8 elements?"
    reflectionPrompt: "Always use arr.length in loop conditions. Hardcoding a size creates a maintenance bug that silently appears when the array changes."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which method correctly compares the contents of two int arrays?"
    options: ["arr1.equals(arr2)", "arr1 == arr2", "Arrays.equals(arr1, arr2)", "arr1.compareTo(arr2)"]
    correctIndex: 2
    feedback: "Arrays.equals(arr1, arr2) from java.util.Arrays compares element-by-element. The other options do not compare contents."
  - type: MULTIPLE_CHOICE
    question: "An array has length 10. What is the last VALID index?"
    options: ["10", "9", "11", "It depends on the element type"]
    correctIndex: 1
    feedback: "Valid indices are 0 through length-1. For length 10, the last valid index is 9."
retrieval:
  recall: "List four common array mistakes covered in this lesson."
  explain: "Explain why using == to compare two arrays usually gives the wrong answer. What should you use instead?"
  mistakeId:
    code: |
      int[] data = {10, 20, 30};
      int[] copy = {10, 20, 30};
      if (data == copy) {
          System.out.println("Same!");
      } else {
          System.out.println("Different!");
      }
    answer: "This always prints 'Different!' because == compares references (memory addresses), not contents. data and copy are two separate objects. Use Arrays.equals(data, copy) to compare their contents — this would return true."
---

# Hook

Arrays are powerful but come with sharp edges. Off-by-one errors in loop boundaries. Comparing two arrays with `==` and getting the wrong answer. Hardcoding the array size and watching the loop break when the size changes. These are the most common array mistakes new developers make — and they are all invisible at first glance. This lesson covers each one explicitly so you can recognise and avoid them before they cause bugs.

# Lore Introduction

Veylan collected a small box of cracked rune ledgers from the repair workshop. "Each of these," he said, setting them on the bench, "was damaged by the same handful of mistakes." He lifted one. "This one was read one slot past its boundary — the off-by-one error." He lifted another. "This one was compared using identity magic instead of content magic — the comparison was always wrong." A third. "This one was looped with a fixed number rather than a living boundary — when the ledger grew, the loop stopped short." He set them all down. "Learn these mistakes by name. That is how you stop making them."

# Core Learning

## Concept Introduction

**Mistake 1 — Off-by-one error in loop boundary**

```java
int[] arr = {1, 2, 3, 4, 5};

// WRONG: i <= arr.length — accesses index 5 on a length-5 array
for (int i = 0; i <= arr.length; i++) {
    System.out.println(arr[i]); // Exception on last iteration
}

// CORRECT: i < arr.length
for (int i = 0; i < arr.length; i++) {
    System.out.println(arr[i]);
}
```

**Mistake 2 — Using `arr.length` as the last index**

The last valid index is `arr.length - 1`, not `arr.length`.

```java
int[] arr = {10, 20, 30};
// WRONG:
System.out.println(arr[arr.length]);    // Exception: index 3 out of bounds
// CORRECT:
System.out.println(arr[arr.length - 1]); // 30
```

**Mistake 3 — Using `==` to compare array contents**

```java
int[] a = {1, 2, 3};
int[] b = {1, 2, 3};

System.out.println(a == b);              // false (compares references)
System.out.println(Arrays.equals(a, b)); // true (compares contents)
```

`==` checks if `a` and `b` are the *same object*. `Arrays.equals()` checks if they *contain the same values*.

**Mistake 4 — Hardcoding array size in a loop**

```java
int[] scores = {10, 20, 30, 40, 50};

// FRAGILE: hardcoded 5
for (int i = 0; i < 5; i++) {
    System.out.println(scores[i]);
}
// If scores grows to 8 elements, the loop misses the last 3 — no error, just silent data loss.

// ROBUST: use scores.length
for (int i = 0; i < scores.length; i++) {
    System.out.println(scores[i]);
}
```

## Why It Matters

These mistakes are so common that most Java developers can recall encountering every one. Off-by-one errors and wrong comparison operators are classic interview questions precisely because they are subtle and easy to introduce. Recognising each mistake by name — and knowing the correct pattern — turns a source of bugs into a checklist of things to verify.

## Worked Examples

**Example 1 — Off-by-one spotted and fixed**

```java
// Bug: prints 5 values then throws exception
for (int i = 0; i <= arr.length; i++) { ... }

// Fix: < instead of <=
for (int i = 0; i < arr.length; i++) { ... }
```

**Example 2 — Correct array comparison**

```java
import java.util.Arrays;

int[] expected = {1, 2, 3};
int[] actual   = {1, 2, 3};

if (Arrays.equals(expected, actual)) {
    System.out.println("Arrays match!");
}
```

**Example 3 — Dynamic length**

```java
int[] data = {5, 10, 15};
// Tomorrow data might have 10 elements — this loop always handles it:
for (int i = 0; i < data.length; i++) {
    System.out.println(data[i]);
}
```

## Common Mistakes Summary

- **`i <= arr.length`** — should be `i < arr.length` (off-by-one).
- **`arr[arr.length]`** — should be `arr[arr.length - 1]` (last element).
- **`arr1 == arr2`** — should be `Arrays.equals(arr1, arr2)` (content comparison).
- **Hardcoding array size** — always use `arr.length` in loop conditions.
- **Assuming sorted arrays**: arrays are not automatically sorted — do not assume element order.

## Mental Model

Think of each mistake as a **known road hazard** on a familiar route. Once you know the potholes, you swerve around them automatically. Before writing any array loop, run through the checklist: `<` not `<=`, `length - 1` for last element, `Arrays.equals` for comparison, `arr.length` not a hardcoded number. Four checks, a few seconds, zero common bugs.

## Mini Summary

- Loop condition: `i < arr.length` (not `<=`).
- Last element: `arr[arr.length - 1]` (not `arr[arr.length]`).
- Content comparison: `Arrays.equals(a, b)` (not `a == b`).
- Loop condition: `arr.length` (not a hardcoded number).
- `==` on arrays tests reference identity, not value equality.
- Know the mistakes by name — recognition is the first step to prevention.

# Guided Practice Quest

Work through each step in order.

**Step 1.** What does `arr1 == arr2` check when both are arrays?

**Step 2.** Fix the given loop: `for (int i = 0; i <= nums.length; i++)`.

**Step 3.** A loop uses `i < 5` but the array grows to 8 elements. What happens?

# Solo Practice Quest

Write a complete Java program that demonstrates all four array mistakes:
1. Show the buggy version of each mistake (commented out or with a comment saying "BUG").
2. Show the corrected version of each mistake.
3. Include a brief explanation (comment) for each fix.

Use the array `int[] values = {4, 8, 15, 16, 23, 42};`.

# Integration

**Psychology connection — The illusion of correctness**

Research in software engineering shows that off-by-one errors and comparison mistakes persist because the buggy code looks correct at a glance. The brain sees `i <= arr.length` and reads "up to the length" — which sounds right. Recognising that "up to" in programming means `<` (strictly less than), not `<=`, requires overriding an intuitive read. This is why naming mistakes explicitly is so valuable: the name "off-by-one error" creates a mental hook that triggers a deliberate check, overriding the superficial impression of correctness.

**Philosophy connection — Identity vs equality**

The philosophical distinction between *identity* (being the same thing) and *equality* (being equivalent in some respect) maps directly onto `==` vs `Arrays.equals()`. Two arrays can be *equal* (same contents) without being *identical* (the same object). Philosophy distinguishes "numerical identity" (A is the same thing as B) from "qualitative identity" (A has all the same properties as B). Java's `==` tests numerical identity; `Arrays.equals()` tests qualitative identity on contents. This distinction appears in every object-oriented language.

**Free question:** The `Arrays` class also provides `Arrays.sort()` and `Arrays.toString()`. Why might having these utilities in a separate class (rather than on the array object itself) reflect a design decision? What are the tradeoffs?

# Lore Conclusion

Veylan placed each repaired rune ledger back in the apprentices' hands. "You have now seen every crack," he said. "The off-by-one. The identity comparison. The frozen boundary. These are not exotic failures — they are the everyday hazards of rune ledger work." He paused. "The mages who never make these mistakes are not the ones who were born careful. They are the ones who made these mistakes early, learned their names, and built the habit of checking." He pointed at each apprentice in turn. "Check your boundaries. Compare by content. Use living lengths. That is all."
