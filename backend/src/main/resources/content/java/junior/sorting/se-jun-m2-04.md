---
id: se-jun-m2-04
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m2
moduleTitle: "Module 2: Collections & Algorithms"
moduleGlyph: "📊"
moduleSortOrder: 2
topicSlug: sorting
topicTitle: "Sorting Algorithms"
topicSortOrder: 4
lesson: sorting_algorithms
title: "Sorting Algorithms"
sortOrder: 4
difficulty: 3
estimatedMinutes: 30
xpReward: 80
practiceType: JAVA
questType: INVESTIGATION
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m2-03]
integrationDomains: [big_o, lists_advanced]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Implements bubble sort correctly with nested loops and a swap"
    - "Uses a Comparator with Collections.sort() or Arrays.sort() to sort custom objects"
    - "Explains the difference between O(n²) bubble sort and O(n log n) merge sort"
    - "Sorts a list of strings in reverse order using Comparator.reverseOrder()"
    - "Describes when to use the built-in sort versus a hand-rolled algorithm"
  keywords: [bubble sort, merge sort, Comparator, Arrays.sort, Collections.sort, O(n²), O(n log n), swap, partition, natural order]
  modelAnswer: |
    import java.util.*;

    // Bubble sort
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    // Sort strings by length using Comparator
    List<String> spells = new ArrayList<>(List.of("Fireball", "Ice", "Thunder Wave", "Hex"));
    spells.sort(Comparator.comparingInt(String::length));
    // [Ice, Hex, Fireball, Thunder Wave]

    // Reverse order
    spells.sort(Comparator.reverseOrder());
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Implement bubble sort for an int array. The outer loop controls the number of passes; the inner loop compares and swaps adjacent elements."
    inputConfig:
      language: java
      starterCode: |
        public static void bubbleSort(int[] arr) {
            int n = arr.length;
            // outer loop: n-1 passes
            // inner loop: compare arr[j] and arr[j+1], swap if out of order
        }
    markingRule: "Outer loop runs n-1 times, inner loop runs n-i-1 times, swap uses a temp variable, largest element bubbles to end each pass"
    hint: "int temp = arr[j]; arr[j] = arr[j+1]; arr[j+1] = temp; — always use a temp variable when swapping."
    reflectionPrompt: "Why does the inner loop upper bound decrease by i each pass? What has already been sorted?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Sort a List<String> of spell names first by length (shortest first), then alphabetically for equal lengths. Use Comparator.comparingInt().thenComparing()."
    inputConfig:
      language: java
      starterCode: |
        import java.util.*;
        List<String> spells = new ArrayList<>(List.of("Fireball", "Ice", "Thunder", "Hex", "Bolt"));
        // Sort by length, then alphabetically
        spells.sort(/* your Comparator here */);
        System.out.println(spells);
    markingRule: "Uses Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()), output is [Hex, Ice, Bolt, Fireball, Thunder]"
    hint: "Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()) chains two sorting criteria."
    reflectionPrompt: "What is the difference between sort() on a List and Arrays.sort() on an array? Do they use the same algorithm internally?"
  - id: step-3
    sortOrder: 3
    inputType: CODE
    instruction: "Sort an array of integers using Arrays.sort() with a custom order: even numbers first (ascending), then odd numbers (ascending). Use a Comparator."
    inputConfig:
      language: java
      starterCode: |
        import java.util.*;
        Integer[] numbers = {5, 2, 8, 1, 4, 9, 6, 3};
        // sort: evens first (ascending), then odds (ascending)
        Arrays.sort(numbers, /* your Comparator */);
        System.out.println(Arrays.toString(numbers));
    markingRule: "Comparator checks parity, even numbers sort before odd, within each group numbers are ascending, output is [2, 4, 6, 8, 1, 3, 5, 9]"
    hint: "Compare (a % 2) values first. If both have the same parity, compare Integer.compare(a, b)."
    reflectionPrompt: "Arrays.sort() uses a dual-pivot quicksort for primitives. Why does it use a different algorithm for objects?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the time complexity of bubble sort in the worst case?"
    options:
      - "O(n log n) — it divides the array in half each pass"
      - "O(n²) — each element is compared against every other element"
      - "O(n) — it only makes one pass through the array"
      - "O(log n) — it uses binary comparison"
    correctIndex: 1
    feedback: "Bubble sort has two nested loops each running O(n) times, resulting in O(n²) comparisons. This makes it impractical for large datasets. Merge sort and quicksort achieve O(n log n) by dividing the problem."
  - type: MULTIPLE_CHOICE
    question: "Which method should you use in production code to sort a List<Employee> by salary?"
    options:
      - "Implement bubble sort manually for full control"
      - "list.sort(Comparator.comparingInt(Employee::getSalary))"
      - "Write a recursive merge sort implementation from scratch"
      - "Convert to array, sort, convert back"
    correctIndex: 1
    feedback: "Collections.sort() and List.sort() use TimSort — an O(n log n) hybrid algorithm optimised for real-world data. Always use the standard library's sort with a Comparator. Only write your own sorting algorithm for educational purposes or specialised performance requirements."
retrieval:
  recall: "Name two sorting algorithms and state the Big O time complexity of each."
  explain: "Explain why you would use Collections.sort() with a Comparator rather than implementing your own sort in production code."
  mistakeId:
    code: |
      int[] arr = {5, 2, 8, 1};
      for (int i = 0; i < arr.length; i++) {
          for (int j = 0; j < arr.length; j++) {
              if (arr[j] > arr[j + 1]) {  // ArrayIndexOutOfBoundsException!
                  int temp = arr[j];
                  arr[j] = arr[j + 1];
                  arr[j + 1] = temp;
              }
          }
      }
    answer: "The inner loop runs to arr.length, so arr[j+1] accesses index arr.length which is out of bounds. Fix: inner loop should run to `arr.length - 1` (or `arr.length - i - 1` to skip already-sorted tail): `for (int j = 0; j < arr.length - 1; j++)`."
---

# Hook

Every collection you've built will eventually need ordering. Search engines rank results. Leaderboards rank players. Product pages rank items by price. Sorting is one of the most studied problems in computer science — not because it is hard, but because the difference between a naive sort and a good one can turn a 10-hour runtime into 10 seconds. This lesson covers bubble sort to understand the mechanics, merge sort to understand the concept, and the standard library sort to handle everything in production.

# Lore Introduction

The Academy rankings committee must sort ten thousand competitor scores every tournament. The apprentice archivist tried writing a bubble sort. After running it overnight and still not finishing, they sought the senior archivist's help. The senior produced three lines: `scores.sort(Comparator.reverseOrder())`. "The library already solved this," she said. "Your job is to understand why it's faster." The apprentice did — and never wrote a bubble sort in production again. But they remembered the lesson: knowing how sorting works makes you a better programmer even when you use the built-in.

# Core Learning

## Concept Introduction

**Bubble Sort — the simple but slow algorithm:**
- Repeatedly compare adjacent elements and swap if out of order
- Each full pass "bubbles" the largest unsorted element to its correct position
- Time complexity: O(n²) — two nested loops, each O(n)
- Space complexity: O(1) — sorts in place

**Merge Sort — divide and conquer:**
- Split the array in half recursively until single elements remain
- Merge sorted halves back together, comparing front elements
- Time complexity: O(n log n) — log n levels of splitting, O(n) merge per level
- Space complexity: O(n) — needs extra space for merging
- Stable: equal elements keep their original relative order

**Java's built-in sort:**
- `Arrays.sort(array)` — uses dual-pivot quicksort for primitives (O(n log n))
- `Collections.sort(list)` and `list.sort(comparator)` — use TimSort (hybrid merge + insertion, O(n log n), stable)
- `Comparator` interface: defines custom ordering rules

**Comparator patterns:**
```java
// Natural order
Comparator.naturalOrder()

// By field
Comparator.comparingInt(Person::getAge)

// Reversed
Comparator.comparingInt(Person::getAge).reversed()

// Chained
Comparator.comparingInt(Person::getAge).thenComparing(Person::getName)
```

## Why It Matters

Sorting is foundational. Binary search requires sorted input. Efficient duplicate detection is easier on sorted data. Database indexes are sorted structures. Understanding that O(n²) sort on 10,000 items performs 100 million operations — while O(n log n) performs only 130,000 — explains why production systems use TimSort, not bubble sort. When you use `list.sort()`, you are using decades of computer science optimisation. But knowing why it is better than bubble sort is what separates engineers who understand their tools from those who merely use them.

## Worked Examples

**Example 1 — Bubble sort implementation**

```java
public static void bubbleSort(int[] arr) {
    int n = arr.length;
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                // swap
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}

int[] scores = {64, 34, 25, 12, 22, 11, 90};
bubbleSort(scores);
System.out.println(Arrays.toString(scores)); // [11, 12, 22, 25, 34, 64, 90]
```

**Example 2 — Collections.sort with Comparator**

```java
import java.util.*;

record Wizard(String name, int power) {}

List<Wizard> wizards = new ArrayList<>(List.of(
    new Wizard("Aldric", 75),
    new Wizard("Seraphine", 92),
    new Wizard("Brynn", 60)
));

// Sort by power descending
wizards.sort(Comparator.comparingInt(Wizard::power).reversed());
// [Seraphine=92, Aldric=75, Brynn=60]

// Sort by name alphabetically
wizards.sort(Comparator.comparing(Wizard::name));
// [Aldric, Brynn, Seraphine]
```

**Example 3 — Arrays.sort with lambda Comparator**

```java
String[] spells = {"Thunder Wave", "Ice", "Fireball", "Hex"};

// Sort by string length
Arrays.sort(spells, (a, b) -> Integer.compare(a.length(), b.length()));
System.out.println(Arrays.toString(spells)); // [Ice, Hex, Fireball, Thunder Wave]
```

## Common Mistakes

- **Inner loop runs to `arr.length` not `arr.length - 1`.** Accessing `arr[j + 1]` when `j == arr.length - 1` causes `ArrayIndexOutOfBoundsException`.
- **Forgetting the temp variable when swapping.** Writing `arr[j] = arr[j+1]; arr[j+1] = arr[j];` loses the original `arr[j]` value before it is saved.
- **Using bubble sort for large datasets.** 10,000 items = 50 million comparisons. Use `Collections.sort()` or `Arrays.sort()` in production.
- **Assuming `Collections.sort()` is unstable.** Java's TimSort is stable — equal elements maintain their relative order, which matters when sorting by secondary fields.
- **Using a `Comparator` that can return inconsistent results.** A comparator must be transitive and consistent with equals, or sort results will be undefined.

## Mental Model

Think of bubble sort as sorting playing cards by picking up two adjacent cards, swapping them if out of order, and repeating across the entire hand. After each pass, the highest unsorted card is in its final position. It is simple but slow because you must re-examine most cards every pass.

Merge sort is like splitting a shuffled deck in half, sorting each half, then merging by always taking the smaller of the two face-up cards. The "divide and conquer" means each split halves the problem, giving log n levels. At each level you do O(n) merging work. Total: O(n log n).

## Mini Summary

- Bubble sort: O(n²), simple to implement, only for learning or tiny arrays.
- Merge sort: O(n log n), divide and conquer, stable, requires O(n) extra space.
- `Arrays.sort()`: dual-pivot quicksort for primitives, TimSort for objects — use this in production.
- `Collections.sort()` and `list.sort()`: TimSort, stable, O(n log n).
- `Comparator.comparingInt/comparing()` builds readable sort criteria; chain with `.thenComparing()`.
- Always use the standard library for production; understand the algorithms to reason about performance.

# Guided Practice Quest

Complete the three steps: implement bubble sort with correct loop bounds and a swap, sort a list of strings by length then alphabetically using a chained Comparator, and sort an array with a custom parity ordering using Arrays.sort().

# Solo Practice Quest

Create a `TournamentRanker` class. It receives a `List<Competitor>` where `Competitor` has `name: String`, `score: int`, and `timeSeconds: int`. Implement `getRankings()` which returns the list sorted: highest score first, and for equal scores, lowest timeSeconds first (faster is better). Also implement `getTop3()` which returns only the top three. Use `Comparator` chaining — no custom sorting algorithm.

# Integration

Sorting is inseparable from **Searching Algorithms** (the next lesson): binary search requires a sorted collection. In the **Big O Basics** lesson, you will formalise why O(n log n) makes such a practical difference. When you reach **Databases**, you will learn that SQL's `ORDER BY` relies on sorting, and that indexes are pre-sorted structures that make queries fast. In **APIs**, sorted response data means clients do not need to sort on the frontend. Every time you return a collection from a service, ask yourself: should this be sorted, and who should bear that responsibility?

**Integration question:** Your REST API returns a list of 100,000 products. The client needs them sorted by price. Should the sorting happen in Java before returning the response, in the database via ORDER BY, or on the client? What are the trade-offs?

# Lore Conclusion

The rankings committee now processes ten thousand scores in milliseconds. The apprentice archivist, who once let bubble sort run through the night, understands exactly why: not because they were told the answer, but because they implemented bubble sort and measured it, then saw O(n log n) in action. The senior archivist's three-line solution is no longer magic — it is the accumulated work of computer scientists who solved sorting once, correctly, so every programmer since can stand on that foundation.
