---
id: se-jun-m2-02
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m2
moduleTitle: "Module 2: Collections & Algorithms"
moduleGlyph: "📊"
moduleSortOrder: 2
topicSlug: sets
topicTitle: "Sets"
topicSortOrder: 2
lesson: sets
title: "Sets"
sortOrder: 2
difficulty: 2
estimatedMinutes: 27
xpReward: 60
practiceType: JAVA
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m2-01]
integrationDomains: [maps_advanced, big_o]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Uses HashSet to deduplicate a list of values"
    - "Uses TreeSet to maintain sorted order"
    - "Demonstrates set intersection, union, or difference operations"
    - "Explains why HashSet is O(1) for contains() while TreeSet is O(log n)"
    - "Identifies a scenario where Set is preferable to List"
  keywords: [HashSet, TreeSet, LinkedHashSet, unique, contains, union, intersection, duplicate, order, hash]
  modelAnswer: |
    import java.util.*;

    List<String> withDuplicates = List.of("apple", "banana", "apple", "cherry", "banana");

    // Deduplicate preserving insertion order
    Set<String> unique = new LinkedHashSet<>(withDuplicates);
    System.out.println(unique); // [apple, banana, cherry]

    // Set operations
    Set<String> setA = new HashSet<>(Set.of("a", "b", "c", "d"));
    Set<String> setB = new HashSet<>(Set.of("c", "d", "e", "f"));

    // Intersection
    Set<String> intersection = new HashSet<>(setA);
    intersection.retainAll(setB);

    // Union
    Set<String> union = new HashSet<>(setA);
    union.addAll(setB);

    // Difference (A minus B)
    Set<String> difference = new HashSet<>(setA);
    difference.removeAll(setB);
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Given a list with duplicates, use a HashSet to produce a unique collection. Then use a TreeSet to get the same unique values in sorted order."
    inputConfig:
      language: java
      starterCode: |
        import java.util.*;
        List<Integer> scores = List.of(85, 92, 78, 92, 85, 100, 78);
        // Create HashSet and TreeSet versions
    markingRule: "HashSet created from list (removing duplicates), TreeSet created from list (sorted unique), both printed"
    hint: "Pass the list to the Set constructor: new HashSet<>(scores). This copies all elements, automatically discarding duplicates."
    reflectionPrompt: "How does HashSet decide two elements are duplicates? What methods on the element class does it use?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Write a method `commonElements(Set<String> a, Set<String> b)` that returns a new Set containing only elements present in BOTH sets (intersection)."
    inputConfig:
      language: java
      starterCode: |
        public static Set<String> commonElements(Set<String> a, Set<String> b) {
            // return intersection
        }
    markingRule: "Creates a copy of set a, calls retainAll(b) to keep only elements in b, returns the result"
    hint: "Set has a retainAll(Collection) method that keeps only elements also in the other collection. Work on a copy so you don't mutate the original."
    reflectionPrompt: "What is the time complexity of retainAll() on a HashSet? (Hint: it calls contains() for each element in b.)"
  - id: step-3
    sortOrder: 3
    inputType: REFLECTION
    instruction: "Name three situations in real software where you would use a Set instead of a List. For each, explain what property of Set makes it the better choice."
    inputConfig:
      minWords: 50
    markingRule: "At least two valid examples: deduplication, membership testing (contains), tracking visited nodes, unique tags/categories; explains uniqueness guarantee and O(1) contains"
    hint: "Think about: checking if something has already been seen, storing unique tags on a blog post, tracking visited pages in a web crawler."
    reflectionPrompt: "When is the ordering of LinkedHashSet important vs when is plain HashSet sufficient?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What happens when you add a duplicate element to a HashSet?"
    options:
      - "A DuplicateElementException is thrown"
      - "The duplicate is silently ignored and add() returns false"
      - "The old element is replaced by the new one"
      - "Both the old and new element are stored"
    correctIndex: 1
    feedback: "Sets guarantee uniqueness. add() returns a boolean — true if the element was added, false if it was already present. The duplicate is silently discarded. This is useful: `if (seen.add(item))` is a common pattern to process each item exactly once."
  - type: MULTIPLE_CHOICE
    question: "Why is HashSet.contains() O(1) while TreeSet.contains() is O(log n)?"
    options:
      - "HashSet is faster because it uses less memory"
      - "HashSet computes a hash to jump directly to the bucket; TreeSet traverses a balanced tree"
      - "TreeSet checks every element sequentially"
      - "HashSet contains() always returns instantly without checking the element"
    correctIndex: 1
    feedback: "HashSet uses a hash function to compute which bucket an element belongs to, then checks only that bucket — O(1). TreeSet stores elements in a Red-Black tree ordered by compareTo(), requiring O(log n) traversal to find an element."
retrieval:
  recall: "Name the three Set implementations in Java. What does each guarantee about element order?"
  explain: "For a custom class to work correctly in a HashSet, which two methods must be consistently overridden, and why must they be consistent with each other?"
  mistakeId:
    code: |
      Set<String> tags = new HashSet<>();
      tags.add("java");
      tags.add("programming");
      tags.add("java"); // duplicate
      System.out.println(tags.size()); // prints 2, but order is not guaranteed
      // Now I need to iterate in insertion order...
    answer: "Use LinkedHashSet instead of HashSet. LinkedHashSet maintains insertion order while still guaranteeing uniqueness and O(1) contains. HashSet has no order guarantee."
---

# Hook

A List can hold duplicates. A Set cannot — by definition. This constraint is not a limitation; it's the feature. When you need to track which users have visited a page, deduplicate items in a collection, or perform mathematical set operations (union, intersection, difference), a Set is the right tool and using a List with manual duplicate checking is the wrong one. This lesson covers three Set implementations and when to use each.

# Lore Introduction

The Academy's tournament registry must list each wizard only once. Using a scroll (List), the registrar had to manually check for duplicate entries — slow and error-prone. Switching to an enchanted crystal (Set) meant the registry simply rejected duplicates automatically. The crystal also maintained entries in sorted order (TreeSet), so the tournament bracket could be printed directly without re-sorting. The right data structure replaced a process with a property.

# Core Learning

## Concept Introduction

`Set<T>` — the Java interface — guarantees **uniqueness**: no two equal elements. Three main implementations:

**HashSet**
- No guaranteed order
- `add()`, `contains()`, `remove()`: O(1) average
- Backed by a `HashMap`
- Uses `hashCode()` and `equals()` to determine equality
- Best for: membership testing, deduplication when order doesn't matter

**TreeSet**
- Elements sorted in natural order (or by Comparator)
- `add()`, `contains()`, `remove()`: O(log n)
- Backed by a Red-Black tree
- Elements must be `Comparable` (or provide a Comparator)
- Best for: sorted unique elements, range queries

**LinkedHashSet**
- Insertion order preserved
- `add()`, `contains()`, `remove()`: O(1) average
- Backed by a LinkedHashMap
- Best for: deduplication while preserving insertion order

**Set operations:**
- `addAll(other)` → union
- `retainAll(other)` → intersection
- `removeAll(other)` → difference

## Why It Matters

Using a List when you need uniqueness means writing defensive checks everywhere: `if (!list.contains(item)) list.add(item)`. This is O(n) per check and error-prone — a forgotten check and duplicates slip in. A Set enforces uniqueness as a structural property. Additionally, `HashSet.contains()` is O(1) versus `List.contains()` which is O(n) — for large datasets, membership testing in a List is dramatically slower than in a HashSet.

## Worked Examples

**Example 1 — Deduplication**

```java
List<String> rawTags = List.of("java", "oop", "java", "patterns", "oop", "java");

// Deduplicate (unordered):
Set<String> uniqueTags = new HashSet<>(rawTags);
System.out.println(uniqueTags.size()); // 3

// Deduplicate (insertion order preserved):
Set<String> orderedTags = new LinkedHashSet<>(rawTags);
System.out.println(orderedTags); // [java, oop, patterns]

// Deduplicate (sorted):
Set<String> sortedTags = new TreeSet<>(rawTags);
System.out.println(sortedTags); // [java, oop, patterns] — alphabetical
```

**Example 2 — Membership testing (O(1) vs O(n))**

```java
// Checking if a username is taken:
Set<String> registeredUsers = new HashSet<>(loadAllUsernames()); // O(1) lookups

public boolean isUsernameTaken(String username) {
    return registeredUsers.contains(username); // O(1)
}

// With a List, this would be O(n) — slow for large user bases:
// return userList.contains(username); // O(n) per check!
```

**Example 3 — Set operations**

```java
Set<String> premiumUsers = Set.of("alice", "bob", "charlie");
Set<String> activeUsers = Set.of("bob", "charlie", "diana", "eve");

// Union (all users who are premium OR active):
Set<String> union = new HashSet<>(premiumUsers);
union.addAll(activeUsers);
// {alice, bob, charlie, diana, eve}

// Intersection (premium AND active):
Set<String> intersection = new HashSet<>(premiumUsers);
intersection.retainAll(activeUsers);
// {bob, charlie}

// Difference (premium but NOT active):
Set<String> difference = new HashSet<>(premiumUsers);
difference.removeAll(activeUsers);
// {alice}
```

## Common Mistakes

- **Using a Set for a class without overriding `hashCode()` and `equals()`.** Custom objects use object identity by default — two objects with the same field values are not equal. Override both methods consistently.
- **Expecting a HashSet to maintain any order.** The iteration order of HashSet is implementation-defined and can change between JVM versions. Use LinkedHashSet for insertion order or TreeSet for sorted order.
- **Mutating a Set's elements after insertion.** If a mutable object changes its `hashCode()` after being added to a HashSet, it may become "lost" — its new hash points to a different bucket than where it was stored. Prefer immutable objects in Sets.
- **Calling `retainAll()` on the original Set.** Always work on a copy: `new HashSet<>(setA).retainAll(setB)` to avoid modifying the source.
- **Choosing TreeSet when you don't need sorted order.** TreeSet is O(log n) for everything. If you just need uniqueness and O(1) access, use HashSet.

## Mental Model

A Set is like a guest list with a doorman who checks IDs. When a guest arrives, the doorman checks if they're already on the list — if yes, they're turned away. HashSet's doorman has a super-fast badge scanner (hash function) that instantly finds the right section of the list. TreeSet's doorman has a phone book sorted alphabetically and does a binary search. LinkedHashSet's doorman stamps guests as they arrive and keeps them in arrival order.

## Mini Summary

- Set guarantees uniqueness — add() silently ignores duplicates and returns false.
- HashSet: O(1) operations, no order — best default for deduplication and membership tests.
- TreeSet: O(log n), sorted order — use when you need sorted unique elements.
- LinkedHashSet: O(1), insertion order — use when you need uniqueness with stable order.
- Set operations: addAll (union), retainAll (intersection), removeAll (difference).
- Custom objects in Sets must correctly override `hashCode()` and `equals()`.

# Guided Practice Quest

Complete the guided steps: deduplicate using HashSet and TreeSet, implement a set intersection method using `retainAll()`, and describe three real scenarios where a Set is preferable to a List.

# Solo Practice Quest

Build a `TagManager` class that stores unique tags (strings) and supports: `addTag(String)`, `removeTag(String)`, `hasTag(String)`, `getTagsSorted()` (returns a sorted list), and `commonTags(TagManager other)` (returns tags present in both). Use the appropriate Set implementations internally to get optimal performance for each operation.

# Integration

Sets are used throughout the Java ecosystem. Java's `Set` is backed by `HashMap` (for `HashSet`) — you'll understand this deeply in the Maps lesson. In database terms, a UNIQUE constraint on a column is the relational equivalent of a Set. When you write JPA entities with unique fields and Spring Data JPA methods like `findByEmail()`, the uniqueness guarantee is maintained both at the Set level in Java and at the database constraint level.

**Integration question:** A web crawler tracks visited URLs to avoid revisiting them. You have millions of URLs. Why is `Set<String> visited = new HashSet<>()` vastly better than `List<String> visited = new ArrayList<>()` for the `if (!visited.contains(url))` check?

# Lore Conclusion

The tournament registry crystal now enforces uniqueness automatically — no duplicate wizards, no manual checks, no errors. The TreeSet version lets the registrar print the sorted bracket in one call. The operations of intersection and union now handle alliance tracking: which guilds overlap, which wizards belong to multiple schools. The right structure doesn't just store data — it enforces the rules that make the data meaningful.
