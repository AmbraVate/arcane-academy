---
id: se-app-m4-02
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
lesson: creating_arrays
title: "Creating Arrays"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 50
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m4-01]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Declares an array using `new` keyword with a specified size"
    - "Declares an array using initialiser syntax with values"
    - "Correctly states the default value of an uninitialised int array element"
    - "Explains that Java arrays have a fixed size after creation"
    - "Accesses the array's length with `.length`"
  keywords: [declare, array, new, initialiser, length, fixed, type, default]
  modelAnswer: |
    // Declaration with new:
    int[] scores = new int[5];  // 5 slots, all default to 0

    // Declaration with initialiser:
    int[] scores = {90, 85, 70, 95, 60};

    // Length:
    System.out.println(scores.length);  // 5
guidedSteps:
  - id: gs-m4-02-1
    sortOrder: 1
    inputType: CODE
    instruction: |
      Declare an int array called `temperatures` with space for 7 values using the `new` keyword.
    inputConfig:
      placeholder: "// declare temperatures array here"
    markingRule:
      matchMode: REGEX
      accepted: ["int\\[\\]\\s+temperatures\\s*=\\s*new\\s+int\\[7\\]\\s*;"]
      rejectedFeedback: "Use: int[] temperatures = new int[7]; — type[], name, new type[size]"
    hint: "Pattern: int[] name = new int[size];"
    reflectionPrompt: "This creates 7 slots, all initialised to 0 by default. The size is fixed — it cannot grow."
  - id: gs-m4-02-2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Declare a String array called `spells` using initialiser syntax with these three values:
      "Fireball", "Icebolt", "Heal"
    inputConfig:
      placeholder: "// declare spells array here"
    markingRule:
      matchMode: CONTAINS
      accepted: ["String[]", "spells", "Fireball", "Icebolt", "Heal"]
      rejectedFeedback: "Use: String[] spells = {\"Fireball\", \"Icebolt\", \"Heal\"};"
    hint: "Initialiser syntax uses curly braces with comma-separated values."
    reflectionPrompt: "Initialiser syntax is shorter than new + individual assignments. Use it when you know the values upfront."
  - id: gs-m4-02-3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the value of `scores[2]` after: `int[] scores = new int[5];`?
    inputConfig:
      options:
        - "null"
        - "undefined"
        - "0"
        - "5"
    markingRule:
      matchMode: EXACT
      accepted: ["0"]
      rejectedFeedback: "Java initialises int array elements to 0 by default. String arrays default to null."
    hint: "Java always initialises numeric array elements to their default value — what is the default int?"
    reflectionPrompt: "Default values by type: int → 0, double → 0.0, boolean → false, String/Object → null."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which declaration creates an int array of 4 elements all set to zero?"
    options:
      - "int[] arr = {4};"
      - "int arr = new int[4];"
      - "int[] arr = new int[4];"
      - "int[] arr = new int(4);"
    correctIndex: 2
    feedback: "int[] arr = new int[4]; — square brackets after the type, square brackets with size after new."
  - type: MULTIPLE_CHOICE
    question: "How do you find out how many elements are in an array called `names`?"
    options:
      - "names.size()"
      - "names.count()"
      - "names.length"
      - "length(names)"
    correctIndex: 2
    feedback: "Arrays use .length (a property, not a method — no parentheses). ArrayLists use .size() — don't mix them up."
retrieval:
  recall: "Write two ways to declare an int array of three elements: one using `new`, one using initialiser syntax."
  explain: "What does 'fixed size' mean for a Java array? Why is this a limitation?"
  mistakeId:
    code: |
      int[] scores = new int(5);
      System.out.println(scores.length);
    answer: "The array declaration uses () instead of []. It should be new int[5] with square brackets. Parentheses are for constructors of objects, not for array sizes."
---

# Hook

You understand why collections exist. Now: how do you actually create a Java array? There are two ways to declare one — with `new` when you know the size but not the values, and with initialiser syntax when you know the values upfront. Get the syntax right and you have a rune ledger ready to fill. Get it wrong and the compiler will complain. This lesson covers both patterns, the `.length` property, and the all-important constraint: arrays in Java have a fixed size.

# Lore Introduction

Veylan placed two rune ledgers on the workbench. "The first," he said, opening an empty one, "was created with space for seven entries — nothing written yet, each slot holding a zero." He placed a second beside it, already filled in. "The second was created with its contents already inscribed at the moment of binding." He looked at his apprentices. "Both are valid. The first is useful when you know how many entries you will need but not yet what they are. The second is useful when you have the entries ready at the moment of creation."

# Core Learning

## Concept Introduction

There are two ways to create an array in Java:

**Method 1 — Using `new` (specify size, values default to zero/null)**

```java
int[] scores = new int[5];
// Creates: [0, 0, 0, 0, 0]
// 5 slots, all initialised to 0
```

**Method 2 — Using initialiser syntax (specify values directly)**

```java
int[] scores = {90, 85, 70, 95, 60};
// Creates: [90, 85, 70, 95, 60]
// Size is determined by the number of values
```

**The type goes before `[]`:**
- `int[]` — array of integers
- `String[]` — array of Strings
- `double[]` — array of doubles
- `boolean[]` — array of booleans

**Finding the size:**
```java
int[] scores = {90, 85, 70};
System.out.println(scores.length); // 3
```

`.length` is a property (not a method — no parentheses).

**Fixed size:** once created, a Java array's size cannot change. You cannot add or remove elements. If you need a resizable collection, use `ArrayList` (covered later).

## Why It Matters

Choosing between `new int[n]` and `{v1, v2, ...}` depends on whether you know the values at creation time. Understanding `.length` is essential for writing loops that process arrays safely. Understanding fixed size is essential for knowing when to use arrays versus `ArrayList`.

## Worked Examples

**Example 1 — `new` then fill**

```java
String[] spells = new String[3];
spells[0] = "Fireball";
spells[1] = "Icebolt";
spells[2] = "Heal";
// spells = ["Fireball", "Icebolt", "Heal"]
```

**Example 2 — Initialiser syntax**

```java
String[] spells = {"Fireball", "Icebolt", "Heal"};
// Equivalent result, shorter code
```

**Example 3 — Using `.length`**

```java
int[] numbers = {10, 20, 30, 40, 50};
System.out.println("Length: " + numbers.length); // Length: 5
System.out.println("Last element: " + numbers[numbers.length - 1]); // 50
```

`numbers.length - 1` is always the index of the last element.

## Common Mistakes

- **Using `()` instead of `[]` for size.** `new int(5)` is wrong. `new int[5]` is correct.
- **Using `.size()` instead of `.length`.** Arrays use `.length`; `ArrayList` uses `.size()`.
- **Expecting arrays to grow.** Adding a value to a full array is impossible — you need `ArrayList` for that.
- **Mixing up size and last index.** An array of size 5 has indices 0, 1, 2, 3, 4. Index 5 does not exist.
- **Forgetting default values.** `new int[5]` creates five zeros, not five undefined values.

## Mental Model

Think of `new int[5]` as **booking 5 seats in a row at a cinema**. The seats exist, they are numbered 0-4, but they are empty. You fill them one by one. `{90, 85, 70}` is like booking seats and immediately assigning people to each one at the moment of booking.

## Mini Summary

- `int[] arr = new int[5];` creates a 5-element int array, all zeros.
- `int[] arr = {1, 2, 3};` creates a 3-element int array with given values.
- `arr.length` (no parentheses) gives the number of elements.
- The last element is always at index `arr.length - 1`.
- Java arrays have a fixed size — they cannot grow or shrink.
- Default values: `int` → 0, `double` → 0.0, `boolean` → false, `String` → null.

# Guided Practice Quest

Work through each step in order.

**Step 1.** Declare an `int` array called `temperatures` with space for 7 values using the `new` keyword.

**Step 2.** Declare a `String` array called `spells` using initialiser syntax with the values `"Fireball"`, `"Icebolt"`, `"Heal"`.

**Step 3.** After `int[] scores = new int[5];`, what is the value of `scores[2]`?

# Solo Practice Quest

Write a short Java program that:
1. Declares a `double` array called `prices` using initialiser syntax with five values of your choice.
2. Prints the array's length.
3. Prints the first and last elements.
4. Declares a second `String` array called `items` using `new String[5]`, then assigns a value to each index.

Include comments explaining each declaration style and when you would choose one over the other.

# Integration

**Mathematics connection — Vectors**

In linear algebra, a vector is an ordered list of numbers: (a₀, a₁, a₂, ..., aₙ₋₁). A Java `int[]` or `double[]` array is the direct computational representation of a vector. The `.length` property corresponds to the vector's dimension. Many numerical algorithms — dot products, matrix multiplication, statistical calculations — are implemented in Java using arrays. Understanding arrays deeply is the foundation of scientific computing in Java.

**Philosophy connection — Potential and actual**

Aristotle distinguished between *potential* (what something could become) and *actual* (what it currently is). A `new int[5]` array is potential: five slots exist, but their contents are defaults, not yet meaningful values. When you fill the slots, the potential becomes actual. Initialiser syntax skips the potential phase — it creates the array in its actual, filled state. This distinction maps onto a deeper question in software design: when should you create objects in their final state, and when is it useful to create them in an intermediate state?

**Free question:** Java arrays have a fixed size. What are the tradeoffs of this design? When is fixed size an advantage, and when is it a significant limitation?

# Lore Conclusion

The first apprentice created her rune ledger with seven empty slots — she would fill them as the week progressed. The second created his with three entries already inscribed at the moment of binding. Both were correct approaches for their situations. Veylan walked between them. "Neither is better. Both serve their purpose." He paused at the first ledger, noting its blank entries. "Potential awaits the filling. The second is already actual." He looked at both apprentices. "Know which approach your incantation requires. That judgement is the beginning of wisdom."
