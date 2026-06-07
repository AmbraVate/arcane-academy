---
id: se-app-m4-08
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m4
moduleTitle: "Module 4: Data Structures"
moduleGlyph: "📦"
moduleSortOrder: 4
topicSlug: lists
topicTitle: "Lists"
topicSortOrder: 2
lesson: removing_items
title: "Removing Items"
sortOrder: 8
difficulty: 2
estimatedMinutes: 22
xpReward: 50
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m4-07]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains `remove(index)` removes the item at a given position and shifts remaining items"
    - "Explains `remove(Object)` removes the first occurrence of that object by value"
    - "Explains `clear()` removes all items"
    - "Explains the index-shift danger when removing in a forward loop"
    - "Describes the correct approach to remove while iterating (iterate backwards or use iterator)"
  keywords: [remove, index, object, clear, shift, size, loop, backwards, iterator]
  modelAnswer: |
    ArrayList provides three removal methods:

    1. `list.remove(int index)` removes the item at the specified index. All items after it shift left by one position. The list's size decreases by 1.

    2. `list.remove(Object o)` removes the first occurrence of the given object by value. For a list of Strings, `list.remove("Alice")` removes the first "Alice". Returns true if found and removed.

    3. `list.clear()` removes all items, leaving an empty list. `size()` returns 0 afterwards.

    A critical caution: when removing items in a for loop iterating forward with an index, removing shifts remaining items left, causing the index to point to the wrong item next iteration. The safest approaches are: (1) iterate backwards from size-1 to 0; (2) use an Iterator or Java's removeIf method.

    Example of the forward-loop bug: removing index 1 shifts what was at index 2 to index 1 — if you then increment to index 2, you skip what is now at index 1.
guidedSteps:
  - id: se-app-m4-08-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      ```java
      ArrayList<String> list = new ArrayList<>();
      list.add("A");
      list.add("B");
      list.add("C");
      list.remove(1);
      System.out.println(list.size() + " " + list.get(0) + " " + list.get(1));
      ```
      What is printed?
    inputConfig:
      options:
        - "3 A C"
        - "2 A C"
        - "2 A B"
        - "2 B C"
    markingRule:
      matchMode: EXACT
      accepted: ["2 A C"]
      rejectedFeedback: "Removing index 1 removes 'B'. The list becomes ['A', 'C']. Size is now 2. `get(0)` is 'A', `get(1)` is 'C'. Note: 'C' shifted from index 2 to index 1 after 'B' was removed."
    hint: "After removing index 1 ('B'), what fills index 1? What is the new size?"
    reflectionPrompt: "When an item is removed, all subsequent items shift left. This means indices change after removal. A previously valid index may now refer to a different item or no item at all."

  - id: se-app-m4-08-step2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Given an ArrayList containing `["cat", "dog", "bird", "dog"]`, write code to remove the first occurrence of "dog" by value, then print the remaining items.
    inputConfig:
      language: java
      starterCode: |
        import java.util.ArrayList;
        ArrayList<String> animals = new ArrayList<>();
        animals.add("cat");
        animals.add("dog");
        animals.add("bird");
        animals.add("dog");
        // Remove first "dog" by value, then print all items
    markingRule:
      matchMode: CONTAINS
      accepted: ["remove", "dog", "for", "get", "println"]
      rejectedFeedback: |
        ```java
        animals.remove("dog"); // removes first "dog" (index 1)
        for (int i = 0; i < animals.size(); i++) {
            System.out.println(animals.get(i));
        }
        // Prints: cat, bird, dog
        ```
        `remove("dog")` removes the first occurrence by value. The second "dog" (now at index 2) remains.
    hint: "Use `animals.remove(\"dog\")` to remove by value (not by index). Note the type: String, not int."
    reflectionPrompt: "`remove(Object)` removes the first occurrence by value. `remove(int)` removes by index. For a list of Integers, `list.remove(Integer.valueOf(5))` removes the value 5 (not index 5). This distinction matters."

  - id: se-app-m4-08-step3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the index-shifting bug that occurs when you remove items from an ArrayList while iterating forward. How do you avoid it?
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: ["shift", "index", "skip", "backwards", "iterator", "forward"]
      rejectedFeedback: "When you remove item at index i in a forward loop, items at i+1, i+2, etc. shift left by 1. If you then increment i to i+1, you skip what is now at index i (which was at i+1 before the removal). Solution: iterate backwards (from size-1 to 0), or use `list.removeIf(condition)`, or use an Iterator."
    hint: "If you remove index 2 and then increment to index 3, what item is now at index 2?"
    reflectionPrompt: "This is one of the most common ArrayList bugs. Iterating backwards avoids it because removing from the end does not affect the indices of items you have not yet processed."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `list.clear()` do?"
    options:
      - "Removes the last item in the list"
      - "Removes all items, leaving an empty list"
      - "Creates a new empty copy of the list"
      - "Removes duplicate items"
    correctIndex: 1
    feedback: "`clear()` removes every item from the list, resulting in an empty list with `size()` of 0. The list object still exists — it is just empty. This is different from setting the variable to `null` (which would discard the entire object)."

  - type: MULTIPLE_CHOICE
    question: "For `ArrayList<Integer> nums`, what is the difference between `nums.remove(0)` and `nums.remove(Integer.valueOf(0))`?"
    options:
      - "They are identical — both remove the integer value 0"
      - "`nums.remove(0)` removes the item at index 0; `nums.remove(Integer.valueOf(0))` removes the first occurrence of the value 0"
      - "`nums.remove(0)` is invalid for Integer lists"
      - "`nums.remove(Integer.valueOf(0))` throws an exception if 0 is not found"
    correctIndex: 1
    feedback: "In an `ArrayList<Integer>`, `remove(0)` is ambiguous — Java resolves it as `remove(int index)`, removing the item at index 0. To remove the VALUE 0, you must use `remove(Integer.valueOf(0))` or cast: `remove((Integer) 0)`. This is a well-known Java gotcha with Integer lists."

retrieval:
  recall: "Write code to remove all items from an ArrayList. Then write code to remove items one by one in a backwards loop."
  explain: "Why is iterating forward over a list while removing items dangerous? Give a concrete example of the bug this causes."
  mistakeId:
    code: |
      ArrayList<String> names = new ArrayList<>();
      names.add("Alice");
      names.add("Bob");
      names.add("Carol");
      for (int i = 0; i < names.size(); i++) {
          names.remove(i);
      }
      System.out.println(names.size());
    answer: "This forward-loop removal skips items. Iteration 1: removes index 0 ('Alice'), list becomes ['Bob','Carol'], i becomes 1. Iteration 2: 1 < 2 (size), removes index 1 ('Carol'), list becomes ['Bob'], i becomes 2. Iteration 3: 2 < 1 is false — loop ends. 'Bob' was never removed; `size()` prints 1. The fix: iterate backwards (`for (int i = names.size()-1; i >= 0; i--)`) or use `names.clear()`."
---

# Hook

Every list eventually needs to shrink. A completed task removed from a to-do list. A sold item removed from inventory. A player who dropped out removed from the leaderboard. Removing items from an ArrayList sounds simple — and the method calls are simple. But there is a subtle trap when you remove items inside a loop: the indices shift, and your loop loses its footing. This lesson covers three removal methods and one critical caution.

# Lore Introduction

"You have learned to inscribe runes on the dynamic scroll," Archmage Veylan says, studying the list of names. "Now learn to erase them." He demonstrates three techniques: removing a rune by its position, removing it by its symbol value, and wiping the scroll entirely. "Each technique has its place." He sets the eraser down. "But beware — when you erase a rune while the scroll is being read, the positions shift. A rune you meant to erase may be skipped. A rune you meant to keep may be erased by mistake." He holds up the scroll. "Order matters. Timing matters. And there is a safe method for erasure while reading."

# Core Learning

## Concept Introduction

**Three removal methods:**

| Method | Effect |
|--------|--------|
| `list.remove(int index)` | Removes item at that index; others shift left |
| `list.remove(Object o)` | Removes first occurrence of that value |
| `list.clear()` | Removes all items |

**After removal:** `size()` decreases by 1 (or to 0 for `clear()`).

**Index-shift caution:** When removing while iterating forward:
```java
// BUG: forward removal skips items
for (int i = 0; i < list.size(); i++) {
    list.remove(i); // shifts remaining items, skips the next one
}

// SAFE: iterate backwards
for (int i = list.size() - 1; i >= 0; i--) {
    list.remove(i); // removal doesn't affect earlier (already-processed) indices
}
```

## Why It Matters

Removal is as common as addition in any real application. Shopping carts, task lists, player rosters, message queues — all need items removed. The index-shift bug is one of the most common ArrayList mistakes and can produce silent wrong behaviour (missing removals, wrong items removed).

## Worked Examples

**Example 1 — Remove by index:**
```java
ArrayList<String> items = new ArrayList<>();
items.add("Alpha");
items.add("Beta");
items.add("Gamma");
items.remove(1); // removes "Beta"
System.out.println(items); // [Alpha, Gamma]
System.out.println(items.size()); // 2
```

**Example 2 — Remove by value:**
```java
ArrayList<String> tags = new ArrayList<>();
tags.add("java");
tags.add("python");
tags.add("java");
tags.remove("java"); // removes first occurrence only
System.out.println(tags); // [python, java]
```

**Example 3 — Clear all items:**
```java
ArrayList<String> sessionData = new ArrayList<>();
sessionData.add("token123");
sessionData.add("userData");
sessionData.clear();
System.out.println(sessionData.size()); // 0
```

## Common Mistakes

- **Removing in a forward loop by index:** Skip-over bug. Always iterate backwards, or use `removeIf`.
- **Confusing `remove(0)` and `remove(Integer.valueOf(0))` for Integer lists:** `remove(0)` removes by index; `remove(Integer.valueOf(0))` removes by value.
- **Expecting `remove(value)` to remove all occurrences:** It removes only the first. Loop to remove all.
- **Not checking if the item exists before removing:** `remove(Object)` returns `false` silently if not found; no exception. This can mask bugs if you expected the item to be there.
- **Using `list = null` instead of `list.clear()`:** Setting to null loses the reference; `clear()` empties the list while keeping it usable.

## Mental Model

Think of ArrayList as a **queue at a cinema**. Removing the person at position 3 means everyone from position 4 onwards moves one step forward (shifts left). If you are checking positions in order and remove position 3, the person who was at position 4 is now at position 3 — and if you go to position 4 next, you skip them. Iterating backwards means you process the end of the queue first, and removals from the end do not affect positions of people ahead of you.

## Mini Summary

- `remove(index)` removes by position; items after it shift left.
- `remove(Object)` removes the first occurrence by value.
- `clear()` empties the list entirely.
- Removing while iterating forward causes index-skipping bugs.
- Safe removal while iterating: go backwards or use `removeIf`.
- `size()` decreases by 1 after each `remove()`.

# Guided Practice Quest

*"The Academy must remove withdrawn students from the enrollment list," Archmage Veylan explains. "Start with a list: 'Alice', 'Bob', 'Carol', 'Dave', 'Eve'. Remove 'Bob' by value. Remove the item at index 2. Then print the remaining list and size. Finally, clear the list and confirm it is empty."*

# Solo Practice Quest

**The Pruner**

Write a Java program that:

1. Creates an `ArrayList<Integer>` with the values: 5, 12, 3, 19, 7, 14, 2, 8.
2. Removes all values less than 6 using a backwards for loop.
3. Prints the final list contents using `get(i)` and the final size.

Trace the backwards loop on paper: for each iteration, write the index, the value at that index, whether it is removed, and the list after the iteration.

# Integration

**Mathematics connection:** The removal of an element from a list is a concrete implementation of mathematical **set difference** (for unique collections) and **sequence deletion** (for ordered lists with potential duplicates). When you remove the first occurrence of a value, you are computing the sequence with that value deleted. This is analogous to the mathematical operation of removing a term from a finite sequence. The "index shift" after removal is equivalent to re-indexing the remaining terms — a standard operation in mathematical notation for sequences.

**Psychology connection:** The "Zeigarnik effect" in psychology describes how incomplete tasks are remembered more vividly than completed ones. In productivity systems, the act of removing a completed task from a list is not just a data operation — it provides psychological closure. This is why "done" states in task managers explicitly remove or cross out items rather than just marking them. The ArrayList `remove()` operation models this: the item is gone, not just marked. In software for task management, the choice between "mark as done" (keep in list, flag) vs "remove when done" (remove from list) reflects this psychological dimension.

*Free question: Java's `removeIf` method removes all elements matching a condition in one call: `list.removeIf(n -> n < 6)`. Can you guess what the `n -> n < 6` syntax is called and what it means? (Hint: it is called a lambda expression.)*

# Lore Conclusion

The scroll shrinks gracefully as withdrawn students are erased — each remaining rune shifting to close the gap, the scroll contracting to fit exactly what remains. "The scroll does not leave blank spaces," Archmage Veylan observes. "Removal is clean." He folds it. "You can now add and remove. What you cannot yet do is find — searching the scroll for a specific rune without reading every one." He opens the next scroll. "That is the next skill: searching a list, both with built-in tools and with your own iteration."
