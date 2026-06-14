---
id: se-jun-m2-01
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m2
moduleTitle: "Module 2: Collections & Algorithms"
moduleGlyph: "📊"
moduleSortOrder: 2
topicSlug: lists_advanced
topicTitle: "Lists"
topicSortOrder: 1
lesson: lists_in_depth
title: "Lists in Depth"
sortOrder: 1
difficulty: 2
estimatedMinutes: 28
xpReward: 60
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [sorting, big_o]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Creates and populates an ArrayList and a LinkedList"
    - "Sorts the ArrayList using Collections.sort() with a Comparator"
    - "Uses an Iterator to safely remove elements during iteration"
    - "Explains the performance difference between ArrayList and LinkedList for random access"
    - "Uses correct generics syntax throughout"
  keywords: [ArrayList, LinkedList, Comparator, sort, Iterator, remove, generic, index, access, performance]
  modelAnswer: |
    import java.util.*;

    List<String> arrayList = new ArrayList<>(List.of("banana", "apple", "cherry"));
    List<String> linkedList = new LinkedList<>(arrayList);

    // Sort alphabetically
    Collections.sort(arrayList);

    // Sort by length using Comparator
    arrayList.sort(Comparator.comparingInt(String::length));

    // Safe removal with Iterator
    Iterator<String> it = arrayList.iterator();
    while (it.hasNext()) {
        if (it.next().startsWith("a")) {
            it.remove(); // safe removal during iteration
        }
    }
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Create an ArrayList of five Strings and sort them alphabetically using Collections.sort(). Then sort them by string length using a Comparator lambda."
    inputConfig:
      language: java
      starterCode: |
        import java.util.*;
        List<String> words = new ArrayList<>(List.of("zebra", "ant", "elephant", "cat", "lion"));
        // sort alphabetically, then by length
    markingRule: "Creates ArrayList, calls Collections.sort(), then sort() with Comparator.comparingInt(String::length) or lambda equivalent"
    hint: "For length-based sorting: list.sort(Comparator.comparingInt(String::length)) or list.sort((a, b) -> a.length() - b.length())"
    reflectionPrompt: "What does Comparator.comparingInt return? How does it differ from Collections.sort() with no comparator?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Use an Iterator to remove all strings shorter than 4 characters from the list. Explain why you cannot use a for-each loop with list.remove() here."
    inputConfig:
      language: java
      starterCode: |
        List<String> words = new ArrayList<>(List.of("ant", "bear", "ox", "lion", "eagle"));
        // Use Iterator to remove short words
    markingRule: "Uses iterator(), hasNext(), next(), it.remove() correctly; removes words with length < 4"
    hint: "Calling list.remove() inside a for-each loop throws ConcurrentModificationException. Use Iterator.remove() instead."
    reflectionPrompt: "Why does Java throw ConcurrentModificationException when you modify a list during enhanced for-loop iteration?"
  - id: step-3
    sortOrder: 3
    inputType: REFLECTION
    instruction: "Given that ArrayList uses an array internally and LinkedList uses a doubly-linked list of nodes: describe the time complexity of get(index) and add(index, element) for each. Which would you choose for a list you mostly read from the middle?"
    inputConfig:
      minWords: 40
    markingRule: "ArrayList get(i) is O(1), add(i,e) is O(n). LinkedList get(i) is O(n), add(first/last) is O(1). Chooses ArrayList for random access."
    hint: "ArrayList can jump straight to index i using pointer arithmetic. LinkedList must walk the chain from the head or tail."
    reflectionPrompt: "Is there a real-world scenario where LinkedList is actually preferable to ArrayList?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What exception is thrown when you call list.remove() inside a for-each loop?"
    options:
      - "IndexOutOfBoundsException"
      - "ConcurrentModificationException"
      - "IllegalStateException"
      - "UnsupportedOperationException"
    correctIndex: 1
    feedback: "For-each loops use an internal Iterator that detects structural modifications to the list while iterating. Use Iterator.remove() to safely remove during iteration, or collect items to remove first and remove them afterwards."
  - type: MULTIPLE_CHOICE
    question: "ArrayList's get(index) is O(1) because:"
    options:
      - "It caches the most recently accessed element"
      - "It uses a hash to compute the location"
      - "It stores elements contiguously in an array, so index arithmetic gives the address directly"
      - "It maintains a sorted order for binary lookup"
    correctIndex: 2
    feedback: "Arrays in Java (and C, and most languages) store elements in contiguous memory. The address of element i is: baseAddress + (i * elementSize). This arithmetic is constant time regardless of list size."
retrieval:
  recall: "What are the two most important performance differences between ArrayList and LinkedList? Which is better for random access and which for frequent head/tail insertions?"
  explain: "Explain how Comparator works with Collections.sort(). What does the int return value from a Comparator mean (negative, zero, positive)?"
  mistakeId:
    code: |
      List<String> list = new ArrayList<>(List.of("a", "b", "c"));
      for (String s : list) {
          if (s.equals("b")) list.remove(s);
      }
    answer: "This throws ConcurrentModificationException. Use Iterator: `Iterator<String> it = list.iterator(); while (it.hasNext()) { if (it.next().equals(\"b\")) it.remove(); }` Or collect then remove: `list.removeIf(s -> s.equals(\"b\"))`."
---

# Hook

You've used `ArrayList` before. Now let's actually understand it — and its cousin `LinkedList` — well enough to choose between them, sort contents correctly, and avoid the notorious `ConcurrentModificationException` that bites every developer at least once. At this tier, knowing which collection to reach for, and why, is the mark of intentional design rather than habit.

# Lore Introduction

The Academy's item registry started as a simple scroll — add items to the end, read from the beginning. Simple enough. But when the cataloguing guild needed to sort by rarity, remove duplicates during inventory audits, and insert new items mid-list without disrupting the order, the choice of list structure became critical. The archivist who chose the right structure for each operation saved hours; the one who chose wrong caused scroll-length re-indexing delays at peak hour.

# Core Learning

## Concept Introduction

Java's `List` interface has two primary implementations:

**ArrayList**
- Backed by a resizable array
- `get(i)`: O(1) — direct array indexing
- `add(i, element)`: O(n) — shifts all subsequent elements
- `add(end)`: O(1) amortised — occasionally resizes the backing array
- Best for: random access, read-heavy use, iterating

**LinkedList**
- Doubly-linked list of Node objects
- `get(i)`: O(n) — must walk from head or tail
- `addFirst() / addLast()`: O(1) — update head/tail pointer
- `add(i, element)`: O(n) to find position, O(1) to insert
- Best for: frequent head/tail insertions, queue/deque use cases
- Also implements `Deque` — can be used as a queue or stack

**Sorting lists:**
- `Collections.sort(list)` — natural order (requires `Comparable`)
- `list.sort(comparator)` — custom order via `Comparator`
- `Comparator.comparing(...)`, `Comparator.comparingInt(...)` — lambda-friendly factory methods

## Why It Matters

Choosing the wrong list implementation doesn't break code — it just makes it slow. An `ArrayList` with frequent insertions at position 0 copies thousands of elements on every operation. A `LinkedList` used for random access traverses the entire chain for each `get()`. As datasets grow, these O(n) vs O(1) differences become wall-clock seconds. Making intentional choices is the difference between code that scales and code that gets emergency-optimised in production.

## Worked Examples

**Example 1 — Sorting with Comparator**

```java
import java.util.*;

List<String> spells = new ArrayList<>(List.of("Fireball", "Ice Shard", "Lightning", "Mend"));

// Natural alphabetical order:
Collections.sort(spells);
System.out.println(spells); // [Fireball, Ice Shard, Lightning, Mend]

// By length (shortest first):
spells.sort(Comparator.comparingInt(String::length));
System.out.println(spells); // [Mend, Ice Shard, Fireball, Lightning]

// Reversed:
spells.sort(Comparator.comparingInt(String::length).reversed());
System.out.println(spells); // [Lightning, Ice Shard, Fireball, Mend]
```

**Example 2 — Comparator with custom objects**

```java
public record Wizard(String name, int level) {}

List<Wizard> wizards = new ArrayList<>(List.of(
    new Wizard("Gandalf", 20),
    new Wizard("Merlin", 30),
    new Wizard("Dumbledore", 25)
));

// Sort by level ascending:
wizards.sort(Comparator.comparingInt(Wizard::level));

// Sort by name, then by level:
wizards.sort(Comparator.comparing(Wizard::name)
             .thenComparingInt(Wizard::level));
```

**Example 3 — Safe removal with Iterator**

```java
List<String> inventory = new ArrayList<>(List.of("sword", "axe", "stick", "shield", "arrow"));

// Remove items shorter than 5 characters:
Iterator<String> it = inventory.iterator();
while (it.hasNext()) {
    String item = it.next();
    if (item.length() < 5) {
        it.remove();  // safe — no ConcurrentModificationException
    }
}
System.out.println(inventory); // [sword, shield, shield, arrow]

// Modern equivalent using removeIf:
inventory.removeIf(item -> item.length() < 5);
```

## Common Mistakes

- **Using LinkedList for random access.** If you mostly call `get(i)`, LinkedList is O(n) per call. Use ArrayList.
- **Removing inside a for-each loop.** Always use `Iterator.remove()` or `removeIf()` instead.
- **Sorting a `List.of(...)` list.** `List.of()` returns an unmodifiable list. Copy it into an `ArrayList` first.
- **Ignoring the Comparator return value contract.** The comparator must return negative if a < b, zero if a == b, positive if a > b. Returning 0 or 1 only is wrong.
- **Using LinkedList as a general-purpose list.** In practice, ArrayList outperforms LinkedList in most real applications because CPU cache locality favours contiguous arrays. Reach for LinkedList only when you have a measured, specific need for O(1) head/tail operations.

## Mental Model

ArrayList is a shelf with numbered slots — to find item 42, you just go to slot 42. To insert at slot 5, everything from slot 5 onwards shuffles right one slot. LinkedList is a chain of lockers, each holding a key to the next — to find item 42, you follow 42 keys. But adding a locker at the front just means pointing the new locker to the old front — the rest of the chain doesn't move.

## Mini Summary

- ArrayList: O(1) random access, O(n) mid-list insertion — default choice for most use cases.
- LinkedList: O(1) head/tail insertion, O(n) random access — use for queues/deques.
- `Collections.sort()` for natural order; `list.sort(Comparator)` for custom order.
- `Comparator.comparing()` and `Comparator.comparingInt()` create clean, readable sort orders.
- Never call `list.remove()` inside a for-each loop — use `Iterator.remove()` or `removeIf()`.
- Prefer ArrayList in nearly all real-world scenarios unless you have a specific measured reason for LinkedList.

# Guided Practice Quest

Complete the three steps: sort a list alphabetically and by length, safely remove elements using an Iterator, and reason about the performance trade-offs between ArrayList and LinkedList for different operations.

# Solo Practice Quest

Create a `SpellBook` class that wraps a `List<String>` of spell names. Implement: `addSpell(String)`, `removeSpellsStartingWith(String prefix)` (safe removal), `getSpellsSortedByLength()` (returns a sorted copy, doesn't modify the original), and `getMostPowerful(int n)` (returns the n longest spell names). Write a main method demonstrating all four operations.

# Integration

The `List` interface underpins almost every collection operation you'll write. In the **Sorting Algorithms** lesson you'll see how Java's built-in sort (TimSort) works and why it's used under the hood by `Collections.sort()`. When you work with Spring Data JPA, query methods return `List<T>` by default — sorting via Comparator in memory versus in the database query is a performance decision you'll face regularly. The `Comparator` API you've learned here is also the same API used in streams: `stream.sorted(Comparator.comparing(...))`.

**Integration question:** `list.sort(comparator)` modifies the original list. If you want a sorted view without modifying the original, what two approaches could you use?

# Lore Conclusion

The cataloguing guild now runs their inventory audits in seconds. The chief archivist chose ArrayList for the main registry (random access by item number is O(1)), and a LinkedList-backed queue for the incoming-items buffer (head removal is O(1)). The right structure for the right task — the guild's new motto, and not without cause.
