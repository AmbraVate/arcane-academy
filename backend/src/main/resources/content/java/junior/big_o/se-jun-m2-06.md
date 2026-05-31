---
id: se-jun-m2-06
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m2
moduleTitle: "Module 2: Collections & Algorithms"
moduleGlyph: "📊"
moduleSortOrder: 2
topicSlug: big_o
topicTitle: "Big O Basics"
topicSortOrder: 6
lesson: big_o_basics
title: "Big O Basics"
sortOrder: 6
difficulty: 2
estimatedMinutes: 25
xpReward: 80
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m2-05]
integrationDomains: [sorting, searching]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly classifies O(1), O(n), O(n²), and O(log n) algorithms by their loop structure"
    - "Explains that Big O describes growth rate, not exact runtime"
    - "Identifies why O(n²) becomes impractical for large input sizes"
    - "Gives a real example of each of the four complexity classes"
    - "Explains why constants and lower-order terms are dropped in Big O notation"
  keywords: [Big O, O(1), O(n), O(n²), O(log n), time complexity, growth rate, algorithm efficiency, constant, linear, quadratic, logarithmic]
  modelAnswer: |
    // O(1) - constant: single array access
    int first = arr[0];

    // O(n) - linear: single loop
    for (int x : arr) sum += x;

    // O(n²) - quadratic: nested loops
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            process(i, j);

    // O(log n) - logarithmic: halving each step
    // Binary search: while (low <= high) { mid = ...; }

    // Big O drops constants: 3n + 5 → O(n), 2n² + 100n → O(n²)
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: REFLECTION
    instruction: "For each code snippet, determine its Big O time complexity and explain your reasoning: (a) `return arr[0]`, (b) `for (int x : arr) sum += x`, (c) two nested loops both over arr, (d) binary search halving loop."
    inputConfig:
      language: java
      starterCode: |
        // (a) - what complexity?
        int first = arr[0];

        // (b) - what complexity?
        int sum = 0;
        for (int x : arr) sum += x;

        // (c) - what complexity?
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                process(arr[i], arr[j]);
            }
        }

        // (d) - what complexity?
        int low = 0, high = arr.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] == target) return mid;
            else if (arr[mid] < target) low = mid + 1;
            else high = mid - 1;
        }
    markingRule: "Identifies (a) O(1) - single access no loop, (b) O(n) - one loop proportional to n, (c) O(n²) - two nested loops each O(n), (d) O(log n) - search space halved each iteration"
    hint: "Count the loops. One loop = O(n). Nested loops = O(n²). Halving loop = O(log n). No loop = O(1)."
    reflectionPrompt: "Why do we drop constants like 2n or 3n and just write O(n)? What does Big O actually describe?"
  - id: step-2
    sortOrder: 2
    inputType: REFLECTION
    instruction: "Calculate: how many steps does O(n), O(n²), and O(log n) take for n=10, n=100, n=1000, and n=1,000,000? Fill in the table and describe the pattern you see."
    inputConfig:
      language: java
      starterCode: |
        // Fill in approximate steps for each n:
        // n=10:       O(n)=?, O(n²)=?, O(log n)=?
        // n=100:      O(n)=?, O(n²)=?, O(log n)=?
        // n=1000:     O(n)=?, O(n²)=?, O(log n)=?
        // n=1000000:  O(n)=?, O(n²)=?, O(log n)=?
    markingRule: "Correct values: n=10 [10, 100, ~3], n=100 [100, 10000, ~7], n=1000 [1000, 1M, ~10], n=1M [1M, 10^12, ~20]; observes that O(n²) grows explosively while O(log n) barely grows"
    hint: "O(log n) uses log base 2. log₂(1000) ≈ 10, log₂(1000000) ≈ 20."
    reflectionPrompt: "An O(n²) algorithm runs fine on n=1000. At what point does it become unacceptably slow for a production API?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A function has the expression 5n² + 100n + 42 operations. What is its Big O complexity?"
    options:
      - "O(5n²) — the coefficient matters"
      - "O(n² + n) — keep the two highest terms"
      - "O(n²) — drop constants and lower-order terms"
      - "O(142) — add up all the constants"
    correctIndex: 2
    feedback: "Big O drops multiplicative constants and lower-order terms because we care about growth rate as n → infinity. 5n² + 100n + 42 grows like n², so it is O(n²). The 5, the 100n, and the 42 become negligible when n is very large."
  - type: MULTIPLE_CHOICE
    question: "Which operation on a HashMap has O(1) average time complexity?"
    options:
      - "Iterating over all entries"
      - "Sorting all keys"
      - "get(key) and put(key, value)"
      - "Finding the entry with the maximum value"
    correctIndex: 2
    feedback: "HashMap get() and put() compute the key's hash code and go directly to the relevant bucket — a fixed number of operations regardless of map size. Iteration is O(n), sorting keys is O(n log n), and finding the max value requires scanning all entries: O(n)."
retrieval:
  recall: "List the four Big O complexities covered in this lesson from fastest to slowest growth."
  explain: "Explain why 3n² + 7n + 100 simplifies to O(n²) in Big O notation. What is Big O actually measuring?"
  mistakeId:
    code: |
      // "My method is O(n) because it has one loop"
      public int sumPairs(int[] arr) {
          int total = 0;
          for (int i = 0; i < arr.length; i++) {       // O(n)
              for (int j = i + 1; j < arr.length; j++) { // O(n)
                  total += arr[i] + arr[j];
              }
          }
          return total;
      }
    answer: "The method is O(n²), not O(n). Although the inner loop starts at i+1 (not 0), it is still a nested loop that runs O(n) times on average for each outer iteration. The total number of operations is n*(n-1)/2 ≈ n²/2, which is O(n²)."
---

# Hook

Why does one algorithm process a million records in half a second while another takes two days? The answer is not hardware — it is growth rate. Big O notation is the language engineers use to compare algorithms by how their runtime scales with input size. Once you can read O(1), O(n), O(n²), and O(log n), you can look at any algorithm and predict whether it will survive contact with production data.

# Lore Introduction

Two apprentice archivists were given the same task: find all pairs of spells in the archive that share a mana cost. The first wrote two nested loops and submitted it. For a hundred spells, it finished instantly. For a hundred thousand spells, it was still running the next morning. The second archivist said "that is O(n²) — I need an O(n) approach" and used a HashMap to group spells by cost in a single pass. The task that took a day for one took milliseconds for the other. The only difference was understanding how their algorithms grew.

# Core Learning

## Concept Introduction

**What Big O means:**
Big O notation describes how an algorithm's runtime (or space usage) grows as the input size n increases. It expresses the *worst case* growth rate, dropping constants and lower-order terms. It is not about exact nanoseconds — it is about the shape of the curve.

**The four essential complexities:**

**O(1) — Constant time:**
- Runtime does not depend on input size
- Examples: array index access `arr[5]`, HashMap `get()`, `put()`
- For any n, the number of operations is the same

**O(n) — Linear time:**
- Runtime grows proportionally with input size
- Examples: single loop over a list, linear search, reading every element
- Double n → roughly double the work

**O(log n) — Logarithmic time:**
- Runtime grows by 1 each time n doubles
- Examples: binary search, balanced tree lookup
- 1,000,000 elements → only ~20 steps

**O(n²) — Quadratic time:**
- Runtime grows as the square of input size
- Examples: two nested loops both over the same input, bubble sort
- Double n → four times the work
- Impractical for large n

**Dropping constants:**
Big O drops multiplicative constants and lower-order terms:
- `5n + 100` → O(n)
- `3n² + 7n + 42` → O(n²)
- `2 log n + 50` → O(log n)
This is valid because at large n, lower-order terms become negligible.

## Why It Matters

A production API that processes 10,000 requests per second cannot afford O(n²) algorithms on large datasets. The difference between O(n) and O(n²) at n=100,000 is 100,000 operations versus 10,000,000,000. That is 100 billion operations. No amount of hardware compensates for the wrong algorithm. Understanding Big O lets you predict whether your code will scale before you deploy it.

## Worked Examples

**Example 1 — O(1): HashMap lookup**

```java
Map<String, Integer> spellPower = new HashMap<>();
spellPower.put("Fireball", 80);
spellPower.put("Ice Shard", 45);

// O(1) — does not matter if map has 10 or 10,000,000 entries
int power = spellPower.get("Fireball"); // always ~same time
```

**Example 2 — O(n): single loop**

```java
public int sumAll(List<Integer> numbers) {
    int sum = 0;
    for (int n : numbers) { // visits each element once
        sum += n;
    }
    return sum; // O(n): 1000 elements → ~1000 operations
}
```

**Example 3 — O(n²): nested loops**

```java
// Find all duplicate pairs in a list (naive approach)
public List<int[]> findDuplicatePairs(int[] arr) {
    List<int[]> pairs = new ArrayList<>();
    for (int i = 0; i < arr.length; i++) {        // O(n)
        for (int j = i + 1; j < arr.length; j++) { // O(n)
            if (arr[i] == arr[j]) {
                pairs.add(new int[]{i, j});
            }
        }
    }
    return pairs; // O(n²): 1000 elements → ~500,000 operations
}
```

**Example 4 — O(log n): binary search**

```java
// Binary search halves the search space each step
public int binarySearch(int[] sortedArr, int target) {
    int low = 0, high = sortedArr.length - 1;
    while (low <= high) {          // runs at most log₂(n) times
        int mid = low + (high - low) / 2;
        if (sortedArr[mid] == target) return mid;
        else if (sortedArr[mid] < target) low = mid + 1;
        else high = mid - 1;
    }
    return -1; // 1,000,000 elements → at most 20 iterations
}
```

## Common Mistakes

- **Assuming one loop always means O(n).** A loop inside another loop is O(n²). A loop that halves its range each iteration is O(log n).
- **Confusing O(n) and O(n²) when the inner loop starts at i+1.** Even starting at i+1, nested loops are O(n²) — the number of iterations is n*(n-1)/2 which simplifies to O(n²).
- **Thinking constants matter for Big O.** O(100n) is still O(n). Big O is about growth rate, not exact operation count.
- **Ignoring space complexity.** An algorithm can be O(n) time but O(n²) space (e.g., building a large 2D array). Both matter.
- **Applying Big O to trivially small inputs.** Big O describes large-n behaviour. For n=5, even O(n³) may be fine. Big O matters when n grows large.

## Mental Model

Imagine n as the number of items in a magical archive. O(1) is reaching into a known drawer — same effort regardless of archive size. O(n) is reading every scroll once. O(n²) is comparing every pair of scrolls — each new scroll forces you to compare it against every existing one. O(log n) is using the index: each check lets you discard half the archive. As the archive grows from 100 to 1,000,000 scrolls, the O(log n) approach barely changes effort; the O(n²) approach becomes impossible.

## Mini Summary

- O(1): constant — runtime independent of input size; HashMap get/put, array index access.
- O(n): linear — one loop over n elements; single pass, linear search.
- O(n²): quadratic — nested loops over n; bubble sort, naive pair-finding.
- O(log n): logarithmic — halve each step; binary search, balanced tree lookup.
- Drop constants and lower-order terms: 5n² + 3n → O(n²).
- At n=1,000,000: O(log n) ≈ 20 steps; O(n) = 1M steps; O(n²) = 10¹² steps.

# Guided Practice Quest

Complete the two reflection steps: classify four code snippets by their Big O complexity with reasoning, then calculate and compare step counts for O(n), O(n²), and O(log n) at four different input sizes.

# Solo Practice Quest

Review the following four method signatures and their implementations (described below). For each: state the Big O time complexity, explain your reasoning, and suggest a more efficient approach if the complexity is worse than O(n log n). Methods: (1) find the max in a List<Integer> with a loop, (2) check if any two elements in an array sum to a target using nested loops, (3) look up a User by ID in a sorted List<User> using a for-each loop, (4) count the frequency of each word in a List<String> using a Map. Write your analysis as a code comment block with your justification.

# Integration

Big O is the universal language for the algorithms you have already studied. In **Sorting Algorithms**, bubble sort is O(n²) and merge sort is O(n log n) — now you know exactly why that gap matters at scale. In **Searching Algorithms**, linear search is O(n) and binary search is O(log n) — and at a million records the 50,000x difference is the difference between working software and a timeout. In **Maps**, you learned that HashMap `get()` is O(1) because of hashing. In **Databases**, indexes reduce a table scan from O(n) to O(log n). Big O is not abstract theory — it is the reason every performance decision in the curriculum makes sense.

**Integration question:** You have a method that calls `list.contains(value)` inside a loop of n iterations. The `list` is an `ArrayList`. What is the overall time complexity? What data structure would reduce it to O(n)?

# Lore Conclusion

The archivist who switched from O(n²) to O(n) did not get faster hardware — they changed the shape of the work. Now when a new task arrives involving large data, the first question is always "what is the Big O?" and the second is "can we do better?" That instinct — checking complexity before writing code — separates engineers who write systems that scale from those who discover the problem at midnight when production traffic arrives.
