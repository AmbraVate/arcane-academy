---
id: se-jun-m2-05
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m2
moduleTitle: "Module 2: Collections & Algorithms"
moduleGlyph: "📊"
moduleSortOrder: 2
topicSlug: searching
topicTitle: "Searching Algorithms"
topicSortOrder: 5
lesson: searching_algorithms
title: "Searching Algorithms"
sortOrder: 5
difficulty: 3
estimatedMinutes: 25
xpReward: 80
practiceType: JAVA
questType: INVESTIGATION
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m2-04]
integrationDomains: [big_o, sorting]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Implements linear search that returns the index or -1 if not found"
    - "Implements binary search using low/high/mid pointers on a sorted array"
    - "Uses Collections.binarySearch() correctly and handles the return value"
    - "Explains that binary search requires the collection to be sorted first"
    - "Identifies which algorithm to use given an unsorted vs sorted dataset"
  keywords: [linear search, binary search, O(n), O(log n), sorted, Collections.binarySearch, low, high, mid, index]
  modelAnswer: |
    // Linear search
    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) return i;
        }
        return -1;
    }

    // Binary search
    public static int binarySearch(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] == target) return mid;
            else if (arr[mid] < target) low = mid + 1;
            else high = mid - 1;
        }
        return -1;
    }

    // Built-in
    List<Integer> sorted = List.of(1, 3, 5, 7, 9);
    int idx = Collections.binarySearch(sorted, 5); // 2
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Implement linearSearch(String[] arr, String target) that returns the index of the target, or -1 if not found. Test with a small array."
    inputConfig:
      language: java
      starterCode: |
        public static int linearSearch(String[] arr, String target) {
            // iterate through arr, return index when found
            return -1; // not found
        }
    markingRule: "Iterates from index 0 to arr.length-1, uses .equals() for String comparison, returns index when found, returns -1 if loop completes without finding target"
    hint: "Use .equals() not == for String comparison: if (arr[i].equals(target)) return i;"
    reflectionPrompt: "What is the worst case for linear search? When does it occur?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Implement binarySearch(int[] arr, int target) on a sorted array using low/high/mid pointers. Return the index or -1."
    inputConfig:
      language: java
      starterCode: |
        public static int binarySearch(int[] arr, int target) {
            int low = 0;
            int high = arr.length - 1;
            while (low <= high) {
                int mid = low + (high - low) / 2; // avoids overflow
                // check arr[mid], adjust low or high
            }
            return -1;
        }
    markingRule: "Uses while(low<=high), calculates mid correctly, moves low=mid+1 when target>mid, moves high=mid-1 when target<mid, returns mid when found, returns -1 on exit"
    hint: "If arr[mid] < target, the answer is to the right: low = mid + 1. If arr[mid] > target, it is to the left: high = mid - 1."
    reflectionPrompt: "Why use mid = low + (high - low) / 2 instead of (low + high) / 2? What problem does it avoid?"
  - id: step-3
    sortOrder: 3
    inputType: CODE
    instruction: "Use Collections.binarySearch() on a sorted List<String>. Handle both the found (>=0) and not-found (<0) return values. Also demonstrate sorting the list first with Collections.sort()."
    inputConfig:
      language: java
      starterCode: |
        import java.util.*;
        List<String> spells = new ArrayList<>(List.of("Thunder", "Fireball", "Ice", "Hex", "Barrier"));
        // 1. Sort the list first
        // 2. Search for "Ice" using Collections.binarySearch()
        // 3. Print the index if found, or "not found"
    markingRule: "Sorts list before searching, calls Collections.binarySearch(list, key), checks result >= 0 for found, prints index or not-found message"
    hint: "int result = Collections.binarySearch(list, key); if (result >= 0) { found at result } else { not found }"
    reflectionPrompt: "What does a negative return value from Collections.binarySearch() mean, and how can you use it to find the insertion point?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the time complexity of binary search?"
    options:
      - "O(n) — it may have to inspect every element"
      - "O(n²) — it uses nested loops"
      - "O(log n) — it halves the search space each step"
      - "O(1) — it always finds the answer in one step"
    correctIndex: 2
    feedback: "Binary search halves the remaining search space at each step. For 1,000,000 elements it takes at most 20 steps (log₂ 1,000,000 ≈ 20). This logarithmic growth is why binary search is dramatically faster than linear search on large sorted datasets."
  - type: MULTIPLE_CHOICE
    question: "You have an unsorted List<Integer> and need to find a specific value. Which approach is correct?"
    options:
      - "Use Collections.binarySearch() directly — it sorts internally first"
      - "Use linear search or stream().filter(), since the list is unsorted"
      - "Use Arrays.binarySearch() which works on unsorted arrays"
      - "Binary search works fine on unsorted data; the result may just be unpredictable"
    correctIndex: 1
    feedback: "Binary search requires sorted input. Calling Collections.binarySearch() on an unsorted list produces undefined results — the contract explicitly requires the list to be sorted. For unsorted data, use linear search (loop or stream) or sort first then binary search."
retrieval:
  recall: "What are the time complexities of linear search and binary search? What precondition does binary search require?"
  explain: "Explain in your own words why binary search is O(log n). How many steps does it take to search 1,000,000 elements?"
  mistakeId:
    code: |
      List<String> spells = new ArrayList<>(List.of("Thunder", "Ice", "Fireball"));
      int index = Collections.binarySearch(spells, "Ice");
      System.out.println("Found at: " + index); // prints wrong index
    answer: "Collections.binarySearch() requires the list to be sorted first. Searching an unsorted list produces undefined (incorrect) results. Fix: sort before searching — `Collections.sort(spells); int index = Collections.binarySearch(spells, \"Ice\");`"
---

# Hook

Finding an item in a collection is something every application does constantly. Log in and the system searches for your account. Query a product by ID and the database searches its index. The difference between searching every element (O(n)) and halving the space each step (O(log n)) is the difference between 1,000,000 operations and 20. This lesson covers both algorithms, when to use each, and the built-in Java methods that handle this for you.

# Lore Introduction

The Academy's spell library holds a million scrolls. An apprentice archivist, tasked with finding a specific scroll, starts at the first shelf and checks every scroll in order. By the third day they have not finished. A senior archivist walks in, opens to the middle, checks the title, then discards the entire left or right half of the library — and finds the scroll in twenty seconds. "That is binary search," the senior says. "It only works because the shelves are in order. Keep your shelves sorted." The apprentice never checked shelves out of order again.

# Core Learning

## Concept Introduction

**Linear Search:**
- Check each element in sequence until found or end reached
- Works on any collection, sorted or unsorted
- Time complexity: O(n) — in the worst case checks every element
- Space complexity: O(1)
- When to use: unsorted data, small collections, or when you need the first match

**Binary Search:**
- Requires sorted input
- Examine the middle element: if target, return; if too small, search right half; if too large, search left half
- Each step eliminates half the remaining elements
- Time complexity: O(log n) — halves the search space each step
- Space complexity: O(1) iterative, O(log n) recursive
- When to use: sorted data, large collections, repeated lookups

**Java built-in search:**
- `Collections.binarySearch(list, key)` — requires sorted list, returns index if found, negative value if not
- `Arrays.binarySearch(array, key)` — same for arrays
- Negative return: `-(insertion point) - 1` — tells you where the element would be inserted

## Why It Matters

Most real search problems do not require implementing a search algorithm from scratch. But understanding them matters because: you need to know that `Collections.binarySearch` requires sorted input (calling it on an unsorted list is a silent bug), database indexes are sorted structures that enable O(log n) lookups, and choosing between linear and binary search is a design decision with real performance implications. At 1 million records, binary search takes 20 steps. Linear search takes up to 1,000,000. That difference is visible in latency.

## Worked Examples

**Example 1 — Linear search**

```java
public static int linearSearch(int[] arr, int target) {
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == target) {
            return i; // found: return index
        }
    }
    return -1; // not found
}

int[] levels = {3, 7, 1, 9, 4, 6};
System.out.println(linearSearch(levels, 9)); // 3
System.out.println(linearSearch(levels, 5)); // -1
```

**Example 2 — Binary search implementation**

```java
public static int binarySearch(int[] arr, int target) {
    int low = 0;
    int high = arr.length - 1;

    while (low <= high) {
        int mid = low + (high - low) / 2; // avoids integer overflow
        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            low = mid + 1;  // target is in right half
        } else {
            high = mid - 1; // target is in left half
        }
    }
    return -1; // not found
}

int[] sorted = {1, 3, 5, 7, 9, 11, 13};
System.out.println(binarySearch(sorted, 7));  // 3
System.out.println(binarySearch(sorted, 6));  // -1
```

**Example 3 — Collections.binarySearch**

```java
import java.util.*;

List<String> spells = new ArrayList<>(List.of("Barrier", "Fireball", "Hex", "Ice", "Thunder"));
// List must be sorted!
int index = Collections.binarySearch(spells, "Hex");
System.out.println("Found at index: " + index); // 2

int missing = Collections.binarySearch(spells, "Lightning");
System.out.println(missing); // negative value (e.g., -4), not found
// Insertion point = -(missing) - 1 = 3
```

## Common Mistakes

- **Calling `Collections.binarySearch()` on an unsorted list.** The contract requires sorted input. Results on unsorted lists are undefined — no exception is thrown, but the index will be wrong.
- **Using `==` for String comparison in linear search.** Use `.equals()` for object comparison; `==` compares references, not content.
- **Off-by-one in binary search.** The while condition must be `low <= high` (not `<`). Using `<` misses the case where the target is the last remaining element.
- **Integer overflow in midpoint calculation.** `(low + high) / 2` overflows when both are large ints. Use `low + (high - low) / 2`.
- **Forgetting to sort before binary search.** Sort once upfront; multiple binary searches then run in O(log n) each.

## Mental Model

Linear search is like checking every page of a book for a word. Binary search is like using the index: open to the middle, see if your word would come before or after, discard the irrelevant half, repeat. Each step you discard half the remaining pages. A 1,000-page book takes at most 10 binary-search steps. The catch: the index must be in alphabetical order first.

## Mini Summary

- Linear search: O(n), works on unsorted data, check each element in sequence.
- Binary search: O(log n), requires sorted data, halves search space each step.
- 1 million elements: linear search up to 1,000,000 steps; binary search at most 20 steps.
- `Collections.binarySearch(list, key)` requires sorted list; negative return means not found.
- `Arrays.binarySearch(array, key)` for primitive and object arrays.
- Choose linear for unsorted/small data; binary for sorted/large data with repeated lookups.

# Guided Practice Quest

Complete the three steps: implement linear search with `.equals()`, implement binary search with correct midpoint and pointer updates, and use `Collections.binarySearch()` on a sorted list with proper sort-first setup.

# Solo Practice Quest

Build a `SpellFinder` class with a `List<String> spells` field. Implement `addSpell(String spell)`, `linearFind(String spell)` returning index or -1, `binaryFind(String spell)` returning index or -1 (maintain sorted order using `Collections.sort()` before each binary search), and `findAll(String prefix)` returning all spells that start with the given prefix (linear is fine here). Write a `main` method demonstrating all methods with at least five spells.

# Integration

Searching is the partner of sorting. In the **Sorting Algorithms** lesson you learned to sort data; now you understand why — sorted data enables O(log n) search. In the **Big O Basics** lesson you will formalise the mathematical notation you have been using here. In **Databases**, every indexed column is a sorted structure enabling binary-search-style lookups. The `LIKE 'prefix%'` SQL query uses an index only when searching from the start of a string — the same reason binary search works only on sorted data. In Spring Data JPA, `findById()` uses a primary key index: essentially a binary search on disk.

**Integration question:** Your application stores 500,000 user records in a List. Users look up each other by username frequently. Would you use linear search, binary search, or a HashMap? Justify your answer with Big O reasoning.

# Lore Conclusion

The Academy's spell library is now searchable in twenty lookups regardless of size. The apprentice archivist who spent three days on linear search understands why the shelves must stay sorted — and why the senior archivist's first question when given a search problem is always: "Is it sorted?" The answer to that question determines whether you need twenty steps or a million. Now the apprentice knows to ask it too.
