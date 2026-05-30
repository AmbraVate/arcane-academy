---
moduleId: java-app-6
moduleTitle: "Module 6: Arrays"
moduleGlyph: "📦"
moduleSortOrder: 6
domainId: java
tier: APPRENTICE
topicSlug: arrays
topicTitle: "Arrays"
topicSortOrder: 6
id: java-app-6a
title: "1D Arrays: Declaration, Access & Iteration"
sortOrder: 1
xpReward: 70
practiceType: JAVA
questType: KNOWLEDGE
feynmanPrompt: "Explain what an array is and why you'd use one instead of five separate variables, using only non-technical everyday language."
learningObjectives:
  - Declare and initialise a 1D array using both syntactic forms
  - Access individual elements by index and explain zero-based indexing
  - Iterate over an array using a for loop and an enhanced for-each loop
  - Handle the common ArrayIndexOutOfBoundsException by checking bounds
integrationDomains:
  - mathematics
  - psychology
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Declares an array with at least 4 elements
    - Accesses at least one element by index (e.g. arr[0])
    - Iterates over the entire array using a loop
    - Prints each element or processes all elements inside the loop
    - Uses arr.length correctly (not a hard-coded number) in the loop condition
  keywords:
    - array
    - index
    - length
    - loop
    - iterate
    - element
    - zero
  modelAnswer: |
    Here is one valid solution — printing all spell names from an array:

    ```java
    String[] spells = {"Fireball", "Ice Lance", "Thunder Strike", "Arcane Bolt"};

    for (int i = 0; i < spells.length; i++) {
        System.out.println("Spell " + i + ": " + spells[i]);
    }
    ```

    Output:
    ```
    Spell 0: Fireball
    Spell 1: Ice Lance
    Spell 2: Thunder Strike
    Spell 3: Arcane Bolt
    ```

    Key checks:
    - `String[] spells` declares an array of Strings.
    - Indices run from `0` to `spells.length - 1`.
    - Using `spells.length` in the loop condition means the loop adapts automatically if elements are added.

guidedSteps:
  - id: arr-step-1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      An array of five `int` values is declared and initialised below.

      What is the **index** of the value `42`?

      ```java
      int[] powers = {10, 25, 42, 7, 99};
      ```

      Fill in the blank: `powers[___]` is `42`.
    inputConfig:
      placeholder: "index"
    markingRule:
      matchMode: NORMALIZED
      accepted:
        - "2"
        - 2
      rejectedFeedback: "Arrays are **zero-indexed** — the first element is at index `0`. Count along: `powers[0]=10`, `powers[1]=25`, `powers[2]=42`. The value `42` is at index `2`."
    hint: "Start counting from **0**. `powers[0]` is `10`, `powers[1]` is `25`, and `powers[2]` is...?"
    reflectionPrompt: "Correct! Index `2` gives you `42`. Zero-based indexing is the universal convention in Java (and most languages). An array of length `n` has indices `0` through `n-1`. This is why going to index `n` causes an `ArrayIndexOutOfBoundsException`."

  - id: arr-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Given:

      ```java
      int[] scores = {80, 95, 72, 88, 61};
      ```

      Write a **for loop** that prints the sum of all elements. You don't need to run it — just write the loop.
    inputConfig:
      minWords: 5
    markingRule:
      matchMode: CONTAINS
      accepted:
        - scores.length
        - "scores[i]"
        - sum
        - for
      rejectedFeedback: "Use a loop variable `i` from `0` to `scores.length - 1`. Inside the loop, add `scores[i]` to a running `sum`. Example: `int sum = 0; for (int i = 0; i < scores.length; i++) { sum += scores[i]; }`"
    hint: "Start with `int sum = 0;` before the loop. Inside the loop, write `sum += scores[i];` to accumulate the total."
    reflectionPrompt: "Well done! Using `scores.length` rather than hard-coding `5` is important — if you later add or remove elements from the array, the loop automatically adjusts. Hard-coded lengths are a common source of bugs."

  - id: arr-step-3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which line causes an `ArrayIndexOutOfBoundsException`?

      ```java
      int[] runes = {3, 6, 9};
      System.out.println(runes[0]);  // line A
      System.out.println(runes[2]);  // line B
      System.out.println(runes[3]);  // line C
      System.out.println(runes[1]);  // line D
      ```
    inputConfig:
      options:
        - "Line A (runes[0])"
        - "Line B (runes[2])"
        - "Line C (runes[3])"
        - "Line D (runes[1])"
    markingRule:
      matchMode: NORMALIZED
      accepted:
        - "Line C (runes[3])"
        - line c
        - runes[3]
        - "c"
        - C
      rejectedFeedback: "The array has 3 elements at indices 0, 1, and 2. Index `3` is out of bounds — it would be a 4th element that does not exist. Line C (`runes[3]`) throws the exception."
    hint: "An array of length 3 has valid indices 0, 1, and 2. Index 3 would be the *fourth* element — which doesn't exist."
    reflectionPrompt: "Correct! `runes[3]` throws `ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 3`. The last valid index is always `array.length - 1`. Guard against this with bounds checks or by using the enhanced for-each loop when you don't need the index."
---

# Hook

Five variables. Ten variables. A hundred variables — all holding the same kind of thing.

There is a better way. When you need to store many values of the same type — scores, names, spell powers — you reach for an **array**: a single container with numbered slots, accessible in any order, iterable in a loop.

One name. Many values.

# Lore Introduction

Deep in the Academy's vault lies a cabinet with a hundred identical drawers. Each drawer is numbered from zero. Each holds exactly one item. The vault-keeper, Scribe Aldren, demonstrates:

> *"To find the seventh spell-stone, you do not rummage through all the drawers. You go directly to drawer six. All drawers are the same size; all are the same distance from the front. That is the power of the indexed container."*

In Java, that cabinet is an **array**.

# Core Learning

## Concept Introduction

An **array** is a fixed-size, ordered collection of elements of the **same type**.

**Declaring and initialising:**

```java
// Form 1 — initialiser list (size inferred from number of elements)
int[] powers = {10, 25, 42, 7, 99};

// Form 2 — new keyword (creates array of fixed size; elements default to 0/false/null)
int[] scores = new int[5];
```

**Accessing elements by index** (zero-based):

```java
System.out.println(powers[0]);   // 10  ← first element
System.out.println(powers[4]);   // 99  ← last element (length - 1)
powers[2] = 100;                 // replace the element at index 2
```

**Array length:**

```java
System.out.println(powers.length);  // 5 (not a method — it's a field)
```

## Why It Matters

Arrays eliminate the need for a separate variable for each item (`score1`, `score2`, `score3`…). They make it possible to process all elements with a loop — which is essential for tasks like finding a maximum, sorting, averaging, or searching.

They are also the foundation for more advanced data structures (ArrayList, etc.) that you will encounter soon. Understanding arrays is prerequisite to understanding them all.

## Worked Examples

**Example 1 — for loop (with index)**

```java
String[] spells = {"Fireball", "Ice Lance", "Arcane Bolt"};
for (int i = 0; i < spells.length; i++) {
    System.out.println(i + ": " + spells[i]);
}
// 0: Fireball
// 1: Ice Lance
// 2: Arcane Bolt
```

**Example 2 — enhanced for-each loop (when index not needed)**

```java
int[] manaPool = {30, 55, 20, 80, 10};
int total = 0;
for (int mana : manaPool) {
    total += mana;
}
System.out.println("Total mana: " + total);  // Total mana: 195
```

**Example 3 — finding the maximum**

```java
int[] rolls = {4, 9, 2, 7, 11, 3};
int max = rolls[0];
for (int i = 1; i < rolls.length; i++) {
    if (rolls[i] > max) {
        max = rolls[i];
    }
}
System.out.println("Highest roll: " + max);  // Highest roll: 11
```

## Common Mistakes

- Using index `array.length` instead of `array.length - 1` — the last valid index is always one less than the length.
- Hard-coding the length in a loop (`i < 5`) instead of using `array.length` — breaks silently when the array size changes.
- Forgetting that arrays are zero-indexed — element 1 is at index `0`, not index `1`.
- Trying to resize an array after creation — arrays are **fixed-size**; use `ArrayList` when you need dynamic resizing.
- Using `==` to compare array contents — `arr1 == arr2` checks identity (same object), not equality. Use `Arrays.equals(arr1, arr2)` instead.

## Mental Model

Imagine a **numbered car park** with a fixed number of spaces.

Each space has a number painted on the tarmac — starting at 0. Every space is the same size (same type). You can drive directly to space 7 without checking 0 through 6. If you try to go to space 100 in a 50-space car park, you hit a barrier — that is `ArrayIndexOutOfBoundsException`.

A for loop is a security guard who checks every space in order: "Space 0? Yes. Space 1? Yes. Space 2? Yes…" until they reach the last one.

## Mini Summary

- Arrays store multiple values of the **same type** in **fixed-size**, **indexed** slots.
- Indices start at **0**; the last valid index is `array.length - 1`.
- Use `array.length` (not a hard-coded number) in loops for safety.
- **For loop** — when you need the index. **For-each loop** — when you only need the value.
- Arrays are **fixed-size** after creation; `ArrayList` is the alternative for dynamic sizing.

# Guided Practice Quest

Print a spell list with index numbers.

Declare an array of at least four `String` values (spell names). Use a `for` loop with an index to print each spell preceded by its index number.

**Expected output** (your spell names will differ):
```
Spell 0: Fireball
Spell 1: Ice Lance
Spell 2: Thunder Strike
Spell 3: Arcane Bolt
```

Then compute and print the total number of characters across all spell names using a for-each loop.

# Solo Practice Quest

Write a program that works with an array of five dungeon room danger ratings (`int[]`).

1. Declare the array with ratings of your choice (e.g. `{3, 7, 1, 9, 4}`).
2. Print all ratings using a for-each loop.
3. Find and print the **maximum** rating.
4. Find and print the **average** rating (use `double` for the average).

Do not use any library methods — write the logic yourself.

# Integration

**Connecting to Mathematics — Sequences and Indexed Sets**

A Java array is a concrete implementation of an *indexed sequence* — a foundational concept in mathematics. A sequence is a function from natural numbers (the indices) to values (the elements): `a(0) = 10, a(1) = 25, …`. Arrays formalise this: element `i` is accessed in O(1) time because the physical memory address can be computed directly as `baseAddress + i × elementSize`. This direct-address property makes arrays the fastest possible data structure for random access — a fact that underpins sorting algorithms, matrix operations, and virtually every high-performance algorithm you will encounter.

**Connecting to Psychology — Cognitive Load and Chunking**

Novice programmers tend to reach for separate variables (`score1`, `score2`, `score3`) because the mind resists abstraction under high cognitive load. Recognising that a set of same-typed values can be *chunked* into a single named container — and iterated — is an important threshold in programming cognition. Research on expert–novice differences in programming shows that experts perceive code in higher-level patterns (a loop over an array, a map-reduce operation) while novices see individual lines. Arrays are one of the first structures that reward this pattern-level thinking.

# Lore Conclusion

Scribe Aldren closed the vault door and handed the apprentice a key.

*"One cabinet. One name. One loop to walk every drawer. The student who mastered a hundred separate scrolls is no faster than you — and their scrolls are a hundred times harder to maintain."*

The apprentice's Grimoire now held a new symbol: a row of numbered boxes, a single arrow pointing to each in turn.

Arrays. The first step toward thinking at scale.
