---
id: se-app-m4-07
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m4
moduleTitle: "Module 4: Data Structures Foundations"
moduleGlyph: "📦"
moduleSortOrder: 4
topicSlug: lists
topicTitle: "Lists"
topicSortOrder: 2
lesson: adding_items
title: "Adding Items"
sortOrder: 7
difficulty: 1
estimatedMinutes: 20
xpReward: 50
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m4-06]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly writes ArrayList declaration with type parameter and `new ArrayList<>()`"
    - "Uses `list.add(item)` to append items"
    - "Uses `list.size()` to get the number of elements"
    - "Explains autoboxing — why `list.add(42)` works even though the list holds Integer"
    - "Demonstrates understanding that add() appends to the end by default"
  keywords: [ArrayList, add, size, type parameter, autoboxing, Integer, append, index]
  modelAnswer: |
    To create an ArrayList: `ArrayList<String> names = new ArrayList<>();` — the type parameter (e.g., `<String>`) specifies the item type; `new ArrayList<>()` creates an empty list.

    `list.add(item)` appends item to the end of the list. After each add, `list.size()` increases by 1.

    `list.size()` returns the current number of items (not the capacity). An empty list returns 0.

    Autoboxing is the automatic conversion Java performs between primitive types and their wrapper classes. `ArrayList<Integer>` holds `Integer` objects, but you can write `list.add(42)` — Java automatically converts the `int` literal 42 to an `Integer` object behind the scenes. Similarly, retrieving an element and storing it in an `int` variable works via auto-unboxing.

    `add(item)` always appends to the end. There is also `add(index, item)` to insert at a specific position, but by default add goes to the end.
guidedSteps:
  - id: se-app-m4-07-step1
    sortOrder: 1
    inputType: CODE
    instruction: |
      Create an ArrayList of Strings called `fruits`, add three fruits to it, and print its size.
    inputConfig:
      language: java
      starterCode: |
        import java.util.ArrayList;
        // Write your code below
    markingRule:
      matchMode: CONTAINS
      accepted: ["ArrayList", "String", "add", "size", "println"]
      rejectedFeedback: |
        ```java
        import java.util.ArrayList;
        ArrayList<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Cherry");
        System.out.println(fruits.size()); // 3
        ```
        `add()` appends each item. `size()` returns the number of items currently in the list.
    hint: "Declare `ArrayList<String> fruits = new ArrayList<>();` then call `fruits.add(\"Apple\")` etc."
    reflectionPrompt: "Every call to `add()` increases size by 1. The list starts at 0 and grows as you add. `size()` always reflects the current count of added items."

  - id: se-app-m4-07-step2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      ```java
      ArrayList<Integer> numbers = new ArrayList<>();
      numbers.add(10);
      numbers.add(20);
      numbers.add(30);
      System.out.println(numbers.size());
      ```
      What is printed?
    inputConfig:
      options:
        - "0"
        - "2"
        - "3"
        - "30"
    markingRule:
      matchMode: EXACT
      accepted: ["3"]
      rejectedFeedback: "Three items are added (10, 20, 30). `size()` returns the count of items, which is 3. Note: `ArrayList<Integer>` holds Integer objects, but `numbers.add(10)` works due to autoboxing — Java converts the int 10 to an Integer automatically."
    hint: "Count how many times `add()` is called. `size()` equals the number of items."
    reflectionPrompt: "autoboxing makes working with ArrayList<Integer> feel almost identical to int arrays. Java handles the int→Integer conversion transparently."

  - id: se-app-m4-07-step3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain what "autoboxing" is in Java. Why is it needed when working with `ArrayList<Integer>`?
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: ["primitive", "wrapper", "Integer", "automatic", "convert", "int", "object"]
      rejectedFeedback: "Autoboxing is Java's automatic conversion of a primitive type (like `int`) to its corresponding wrapper class object (like `Integer`). It is needed because Java generics (like `ArrayList<T>`) only work with objects, not primitives. So `ArrayList<int>` is invalid, but `ArrayList<Integer>` is fine. When you write `list.add(5)`, Java automatically wraps 5 in an `Integer` object — that wrapping is autoboxing."
    hint: "What type does ArrayList<Integer> hold? Can it hold primitive ints directly?"
    reflectionPrompt: "Autoboxing is a convenience feature. Without it, you would have to write `list.add(Integer.valueOf(5))` every time. Java does this for you automatically."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `ArrayList<String>` mean?"
    options:
      - "An array of exactly String.length() elements"
      - "A dynamic list that can only hold String objects"
      - "A list of characters (since String is made of chars)"
      - "A list where each element is the String type itself"
    correctIndex: 1
    feedback: "The `<String>` is a type parameter telling the ArrayList what type of objects it holds. `ArrayList<String>` is a dynamic list that can only hold String objects. If you try to add an Integer, the compiler will give an error. The type parameter ensures type safety."

  - type: MULTIPLE_CHOICE
    question: "After `list.add(\"a\"); list.add(\"b\"); list.add(\"c\");`, what is `list.size()`?"
    options:
      - "0"
      - "2"
      - "3"
      - "Depends on the ArrayList's initial capacity"
    correctIndex: 2
    feedback: "`size()` returns the number of elements currently in the list. Three elements have been added, so `size()` returns 3. This is independent of the ArrayList's internal capacity (which is an implementation detail)."

retrieval:
  recall: "Write the code to create an ArrayList of Integers, add the numbers 1 through 5, and print the size."
  explain: "Why can you not use `ArrayList<int>` in Java? What must you use instead?"
  mistakeId:
    code: |
      ArrayList numbers = new ArrayList();
      numbers.add(10);
      numbers.add("hello");
      numbers.add(3.14);
    answer: "This creates a raw ArrayList (no type parameter). While it compiles, it is unsafe — the list holds items of mixed types (int, String, double), and any retrieval requires casting, which can throw ClassCastException at runtime. The fix is to declare with an appropriate type parameter: `ArrayList<Integer> numbers = new ArrayList<>();` (then only add integers). Type parameters enforce consistency and catch type errors at compile time."
---

# Hook

An empty container is the starting point of everything. A shopping cart before the first item. A playlist before the first song. A leaderboard before the first score. The ArrayList starts empty and grows one item at a time with a single method call: `add()`. In this lesson you learn the mechanics of building up a dynamic list — declaring it, adding to it, and measuring it.

# Lore Introduction

"A blank dynamic scroll," Archmage Veylan says, presenting a shimmering, empty surface. "No runes inscribed yet. Size: zero." He draws a symbol on the scroll. "One rune added. Size: one." He adds another. "Two." He continues until the scroll holds several inscriptions, each one appearing and shifting to accommodate the next. "The scroll manages its own space. You need only speak the `add` word and provide the rune. The scroll does the rest." He sets it down. "This is the ArrayList in action. Simple interface. Powerful internals."

# Core Learning

## Concept Introduction

**Declaring an ArrayList:**
```java
import java.util.ArrayList;

ArrayList<String> names = new ArrayList<>();
//         ↑ type parameter   ↑ creates empty list
```

The type parameter `<String>` restricts the list to hold only `String` objects.

**Adding items — `add(item)`:**
```java
names.add("Alice");   // list: ["Alice"]
names.add("Bob");     // list: ["Alice", "Bob"]
names.add("Carol");   // list: ["Alice", "Bob", "Carol"]
```

`add(item)` appends to the **end** of the list.

**Checking size — `size()`:**
```java
System.out.println(names.size()); // 3
```

**Insert at a specific index — `add(index, item)`:**
```java
names.add(1, "Zara"); // inserts at index 1, shifting others right
// list: ["Alice", "Zara", "Bob", "Carol"]
```

**Autoboxing with `ArrayList<Integer>`:**
```java
ArrayList<Integer> scores = new ArrayList<>();
scores.add(85);  // Java autoboxes int 85 → Integer(85)
scores.add(92);
```

## Why It Matters

Adding items is the fundamental write operation of any list. Understanding how `add()` works — appending to the end, adjusting size — and how `size()` reflects the true count are the building blocks for all list operations. Autoboxing matters because most numeric lists use `ArrayList<Integer>` and you need to know why you cannot write `ArrayList<int>`.

## Worked Examples

**Example 1 — Building a shopping cart:**
```java
ArrayList<String> cart = new ArrayList<>();
cart.add("Laptop");
cart.add("Mouse");
cart.add("Keyboard");
System.out.println("Items in cart: " + cart.size()); // 3
```

**Example 2 — Iterating and printing a list:**
```java
ArrayList<String> colours = new ArrayList<>();
colours.add("Red");
colours.add("Green");
colours.add("Blue");

for (int i = 0; i < colours.size(); i++) {
    System.out.println(colours.get(i));
}
// Red, Green, Blue
```

**Example 3 — Autoboxing with integers:**
```java
ArrayList<Integer> temps = new ArrayList<>();
temps.add(-5);    // autoboxed: int → Integer
temps.add(0);
temps.add(18);
temps.add(37);
System.out.println("Recorded " + temps.size() + " temperatures.");
```

## Common Mistakes

- **`ArrayList<int>` instead of `ArrayList<Integer>`:** Java generics require object types, not primitives. Use wrapper classes.
- **Forgetting the import:** `ArrayList` is in `java.util`; it must be imported.
- **Using `length` instead of `size()`:** Arrays use `.length`; ArrayList uses `.size()`. Mixing them is a compile error.
- **Assuming `add(index, item)` replaces the item at that index:** It inserts, shifting existing items right. To replace, use `set(index, item)`.
- **Calling `get()` on an empty list:** `list.get(0)` on an empty list throws `IndexOutOfBoundsException`. Check `size() > 0` first.

## Mental Model

ArrayList is like a **queue at a ticket booth**. Each call to `add()` is a new person joining at the back of the queue. The queue grows with each arrival. `size()` tells you how many people are currently waiting. `get(i)` lets you look at the person at position i. The queue manages its own length — you never need to declare "this queue can hold at most N people."

## Mini Summary

- Declare: `ArrayList<Type> list = new ArrayList<>();`
- Add to end: `list.add(item)`
- Insert at position: `list.add(index, item)`
- Get count: `list.size()`
- ArrayList holds object types — use `Integer`, not `int`
- Autoboxing converts `int` literals to `Integer` objects automatically

# Guided Practice Quest

*"The Academy's student registry begins empty at the start of each semester," Archmage Veylan explains. "Create an ArrayList of Strings representing student names. Add five names. Print the list's size after each addition to show how it grows. Finally, use a for loop and `get(i)` to print all names."*

# Solo Practice Quest

**The Score Tracker**

Write a Java program that:

1. Creates an `ArrayList<Integer>` called `scores`.
2. Adds the following test scores: 88, 74, 95, 61, 82.
3. Prints the total number of scores using `size()`.
4. Uses a for loop to print each score on its own line.
5. Computes and prints the sum of all scores using a for loop and `get(i)`.

After writing, trace through the loop that computes the sum: write the value of `i`, `scores.get(i)`, and the running sum for each iteration.

# Integration

**Mathematics connection:** A dynamic list in computer science corresponds to a finite sequence in mathematics. Just as a sequence a₁, a₂, ..., aₙ can be extended by appending aₙ₊₁, an ArrayList can always be extended by one more element. The `size()` method corresponds to the length n of the sequence. Building a list by repeated `add()` is equivalent to constructing a sequence element by element — a process central to combinatorics, probability theory (where you build sample spaces), and algorithm analysis (where you build intermediate result sequences).

**Psychology connection:** The concept of "chunking" in cognitive psychology describes how humans group individual items into larger meaningful units to reduce memory load. An ArrayList is a programming implementation of chunking: instead of tracking each data item as a separate variable (`score1`, `score2`, `score3`...), you group them into one named collection. Research shows that chunking dramatically improves working memory efficiency. In code, a well-named ArrayList (`highScores`, `pendingRequests`) is a cognitive chunk — a single mental object that represents a meaningful collection.

*Free question: If you call `list.add(1, "Zara")` on a list that already has 3 items, what happens to the items at indices 1 and 2? How does this affect performance for large lists?*

# Lore Conclusion

The scroll holds five names now, each added seamlessly, the size counter incrementing with each inscription. Archmage Veylan examines it. "You have learned to grow the scroll. The `add` word is the most-used word in dynamic collection magic." He sets the scroll down. "But a collection that can only grow becomes a hoard. Next: the art of removal — making the scroll smaller, selectively and safely."
