---
id: se-app-m4-03
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m4
moduleTitle: "Module 4: Data Structures Foundations"
moduleGlyph: "📦"
moduleSortOrder: 4
topicSlug: arrays
topicTitle: "Arrays"
topicSortOrder: 1
lesson: accessing_elements
title: "Accessing Elements"
sortOrder: 3
difficulty: 2
estimatedMinutes: 22
xpReward: 50
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m4-02]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Reads an element by index using arr[i] syntax"
    - "Modifies an element by assigning to arr[i]"
    - "States that indices run from 0 to arr.length - 1"
    - "Explains what ArrayIndexOutOfBoundsException means"
    - "Uses arr.length to access the last element safely"
  keywords: [index, access, read, modify, assign, bounds, exception, zero, last]
  modelAnswer: |
    int[] scores = {10, 20, 30, 40, 50};
    System.out.println(scores[0]);  // 10 — read first element
    scores[2] = 99;                 // modify element at index 2
    System.out.println(scores[scores.length - 1]);  // 50 — last element
    // scores[5] would throw ArrayIndexOutOfBoundsException (valid: 0-4)
guidedSteps:
  - id: gs-m4-03-1
    sortOrder: 1
    inputType: CODE
    instruction: |
      Given: `String[] spells = {"Fireball", "Icebolt", "Heal"};`
      Write a line that prints the SECOND element.
    inputConfig:
      placeholder: "// print the second element of spells"
    markingRule:
      matchMode: CONTAINS
      accepted: ["spells[1]", "System.out.println"]
      rejectedFeedback: "The second element is at index 1 (zero-based). Use: System.out.println(spells[1]);"
    hint: "Second element = index 1 (first is index 0)."
    reflectionPrompt: "Zero-based indexing means: first = 0, second = 1, third = 2. Always subtract 1 from the human-readable position."
  - id: gs-m4-03-2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Given: `int[] scores = {5, 10, 15, 20};`
      Write a line that changes the value at index 2 to 99.
    inputConfig:
      placeholder: "// change element at index 2 to 99"
    markingRule:
      matchMode: REGEX
      accepted: ["scores\\s*\\[\\s*2\\s*\\]\\s*=\\s*99\\s*;"]
      rejectedFeedback: "Assign to the element: scores[2] = 99;"
    hint: "Assigning to an array element: arr[index] = newValue;"
    reflectionPrompt: "Reading: int x = arr[i]; — Writing: arr[i] = value; — The same index syntax works for both."
  - id: gs-m4-03-3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      An array has 6 elements. Which index causes an ArrayIndexOutOfBoundsException?
    inputConfig:
      options:
        - "arr[0]"
        - "arr[5]"
        - "arr[6]"
        - "arr[arr.length - 1]"
    markingRule:
      matchMode: EXACT
      accepted: ["arr[6]"]
      rejectedFeedback: "Valid indices for a 6-element array are 0 through 5. Index 6 is out of bounds."
    hint: "A 6-element array has indices 0, 1, 2, 3, 4, 5. The number 6 is the length, not a valid index."
    reflectionPrompt: "The last valid index is always arr.length - 1. Index equal to length is always out of bounds."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `arr[arr.length - 1]` access?"
    options: ["The first element", "The second-to-last element", "The last element", "The length of the array"]
    correctIndex: 2
    feedback: "arr.length - 1 is the index of the last element. For a 5-element array, that is index 4."
  - type: MULTIPLE_CHOICE
    question: "Which exception is thrown when you access an index outside the valid range?"
    options:
      - "NullPointerException"
      - "IndexOutOfRangeException"
      - "ArrayIndexOutOfBoundsException"
      - "InvalidIndexException"
    correctIndex: 2
    feedback: "Java throws ArrayIndexOutOfBoundsException when you try to access a negative index or an index >= the array's length."
retrieval:
  recall: "For an array of length 8, what are the valid indices? What is the index of the last element?"
  explain: "Explain what happens when you try to access index 5 of an array with only 5 elements."
  mistakeId:
    code: |
      String[] names = {"Alice", "Bob", "Carol"};
      System.out.println(names[3]); // trying to print fourth name
    answer: "Index 3 is out of bounds — the array has 3 elements at indices 0, 1, and 2. Accessing names[3] throws ArrayIndexOutOfBoundsException. The last valid index is names.length - 1 = 2."
---

# Hook

Your array exists. Now how do you get things out of it — or put things in? Array access uses a simple bracket syntax: `arr[index]`. That bracket notation is how you read an element, how you write an element, and how you update one. But there is a sharp edge: access an index that does not exist and Java throws an `ArrayIndexOutOfBoundsException` — one of the most common runtime errors for new developers. This lesson teaches you to access arrays confidently and safely.

# Lore Introduction

The rune ledger sat open before the apprentice. "To read entry three," Veylan said, pointing, "you simply open to slot three." He demonstrated: `ledger[3]`. "To inscribe a new value into slot three, you point at the slot and write." He demonstrated again: `ledger[3] = "Phoenix Feather"`. "But beware." His tone sharpened. "The ledger has five slots: zero through four. Point at slot five and the binding collapses." He held up his hand. "This is the `ArrayIndexOutOfBoundsException`. Respect the boundary."

# Core Learning

## Concept Introduction

**Reading** an element:
```java
int[] scores = {10, 20, 30, 40, 50};
int first = scores[0];  // 10
int third = scores[2];  // 30
```

**Writing** (modifying) an element:
```java
scores[2] = 99;
// Array is now: [10, 20, 99, 40, 50]
```

**Accessing the last element safely:**
```java
int last = scores[scores.length - 1];  // 50
```

Never hardcode the last index. Use `arr.length - 1` so your code still works if the array size changes.

**ArrayIndexOutOfBoundsException:**
- Thrown when you access a **negative** index or an index **>= arr.length**.
- For an array of length 5, valid indices are **0, 1, 2, 3, 4**.
- Accessing `scores[5]` throws the exception.

## Why It Matters

Array access is used in nearly every program that handles groups of data. Understanding the 0-based index system and the bounds rule prevents one of the most common runtime errors in Java. Using `arr.length - 1` for the last element — rather than hardcoding a number — is a habit that prevents bugs whenever the array size changes.

## Worked Examples

**Example 1 — Read and print elements**

```java
String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri"};
System.out.println(days[0]);   // Mon
System.out.println(days[4]);   // Fri
System.out.println(days[days.length - 1]); // Fri (safer)
```

**Example 2 — Modify elements**

```java
int[] scores = {5, 10, 15};
scores[1] = 99;
System.out.println(scores[1]); // 99
// Array is now: [5, 99, 15]
```

**Example 3 — What triggers the exception**

```java
int[] arr = new int[3]; // indices: 0, 1, 2
System.out.println(arr[3]); // Exception: Index 3 out of bounds for length 3
```

The error message tells you the bad index (3) and the length (3). The fix: use `arr[2]` for the last element, or `arr[arr.length - 1]`.

## Common Mistakes

- **Off-by-one: using `arr.length` as an index.** The last index is `arr.length - 1`, not `arr.length`.
- **Hardcoding the last index.** `arr[4]` works today but breaks if the array grows to 6 elements. Use `arr.length - 1`.
- **Negative indices.** `arr[-1]` is not valid in Java (unlike Python). It causes the same exception.
- **Confusing read and write syntax.** `int x = arr[1]` reads. `arr[1] = 5` writes. The index notation is the same; position in the statement differs.
- **Forgetting that modifying an element changes the array permanently.** `arr[i] = value` overwrites — the old value is gone.

## Mental Model

Think of array access as using a **locker number** in a gym changing room. Lockers are numbered 0 to 49 (for a 50-locker row). To open locker 12, you enter `locker[12]`. To put something inside, you enter `locker[12] = "bag"`. Trying to open locker 50 (`locker[50]`) is impossible — the locker doesn't exist. The gym attendant (Java runtime) will stop you and report the error.

## Mini Summary

- Read: `int x = arr[i];` — copies the value at index i.
- Write: `arr[i] = value;` — replaces the value at index i.
- Valid indices: 0 to `arr.length - 1`.
- Access `arr.length` or beyond throws `ArrayIndexOutOfBoundsException`.
- Use `arr[arr.length - 1]` to safely access the last element.
- Modifying an element permanently changes the array.

# Guided Practice Quest

Work through each step in order.

**Step 1.** Given `String[] spells = {"Fireball", "Icebolt", "Heal"}`, write a line that prints the second element.

**Step 2.** Given `int[] scores = {5, 10, 15, 20}`, write a line that changes the value at index 2 to 99.

**Step 3.** An array has 6 elements. Which index causes an `ArrayIndexOutOfBoundsException`?

# Solo Practice Quest

Write a Java program that:
1. Declares an array `int[] temps = {18, 22, 19, 25, 30, 21, 17};`
2. Prints the first temperature.
3. Prints the last temperature using `temps.length - 1`.
4. Updates the temperature at index 3 to 28.
5. Prints the updated temperature at index 3.
6. Attempts to describe (in a comment) what would happen if you tried `temps[7]`.

# Integration

**Mathematics connection — Zero-indexed sequences**

In mathematics, sequences are typically 1-indexed: a₁, a₂, a₃. In computing, arrays are typically 0-indexed: a[0], a[1], a[2]. This distinction matters when translating mathematical formulas to code. A sum formula Σᵢ₌₁ⁿ aᵢ translates to a Java loop from `i = 0` to `i < n`, not `i = 1` to `i <= n`. Getting this translation wrong is a classic off-by-one error. Understanding both conventions — and converting between them — is an important skill in scientific and algorithmic programming.

**Psychology connection — The fence-post problem**

The "fence-post problem" in psychology and computer science describes a cognitive tendency to confuse counts with intervals. Ten fence posts form nine intervals; five array elements occupy indices 0-4, not 1-5. This confusion is so common it has a name. Knowing about this cognitive bias helps you double-check boundary conditions: whenever you work with array indices, explicitly count the slots and indices to verify your mental model matches reality.

**Free question:** Why does Java choose to throw an exception when you access an out-of-bounds index, rather than silently returning a default value? What are the tradeoffs of each approach?

# Lore Conclusion

The apprentice touched slot two of the ledger and read its inscription aloud — "Phoenix Feather". She then inscribed a new name in slot four: "Moonwater Crystal". The ledger accepted the change. Then, out of curiosity, she reached for slot seven. The ledger snapped shut with a sharp crack. "As I said," Veylan murmured. "Five slots. Zero through four. The boundary is not a suggestion." She pulled her hand back, chastened. From that day forward, she always checked the length before reaching into a ledger.
