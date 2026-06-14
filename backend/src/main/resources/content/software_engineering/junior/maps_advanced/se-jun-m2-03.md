---
id: se-jun-m2-03
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m2
moduleTitle: "Module 2: Collections & Algorithms"
moduleGlyph: "📊"
moduleSortOrder: 2
topicSlug: maps_advanced
topicTitle: "Maps"
topicSortOrder: 3
lesson: maps_in_depth
title: "Maps in Depth"
sortOrder: 3
difficulty: 3
estimatedMinutes: 30
xpReward: 70
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m2-02]
integrationDomains: [sorting, big_o]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Builds a frequency count map using getOrDefault or merge"
    - "Iterates over entrySet() to process both keys and values"
    - "Finds the entry with the maximum value"
    - "Groups items into a Map<String, List<T>> using computeIfAbsent"
    - "Explains the difference between HashMap and TreeMap for key ordering"
  keywords: [HashMap, TreeMap, entrySet, getOrDefault, merge, computeIfAbsent, frequency, group, key, value]
  modelAnswer: |
    import java.util.*;

    // Frequency count
    List<String> words = List.of("apple", "banana", "apple", "cherry", "banana", "apple");
    Map<String, Integer> freq = new HashMap<>();
    for (String w : words) {
        freq.merge(w, 1, Integer::sum);
    }

    // Find most frequent
    String mostFrequent = freq.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse(null);

    // Grouping by first letter
    Map<Character, List<String>> grouped = new HashMap<>();
    for (String w : words) {
        grouped.computeIfAbsent(w.charAt(0), k -> new ArrayList<>()).add(w);
    }
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Given a list of words, build a frequency map (word → count). Use getOrDefault() to handle the first-occurrence case."
    inputConfig:
      language: java
      starterCode: |
        import java.util.*;
        List<String> words = List.of("wizard", "knight", "wizard", "mage", "knight", "wizard");
        Map<String, Integer> frequency = new HashMap<>();
        // build frequency map using getOrDefault
    markingRule: "HashMap used, getOrDefault(key, 0) used to handle missing keys, + 1 applied, result is correct frequency count"
    hint: "freq.put(word, freq.getOrDefault(word, 0) + 1) — if the word isn't in the map yet, default is 0."
    reflectionPrompt: "What does merge() do, and how does it simplify the getOrDefault + put pattern?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Iterate over the frequency map using entrySet() and print each word and its count. Then find and print the word that appears most often."
    inputConfig:
      language: java
      starterCode: |
        // Iterate with entrySet() and find max
    markingRule: "Uses entrySet() for iteration, accesses getKey() and getValue(), finds maximum value entry"
    hint: "for (Map.Entry<String, Integer> entry : map.entrySet()) gives you both key and value. Track maxEntry as you iterate."
    reflectionPrompt: "Why is entrySet() preferred over separate keySet() + get() calls for iterating with both key and value?"
  - id: step-3
    sortOrder: 3
    inputType: CODE
    instruction: "Group a list of spells by their first character using computeIfAbsent(). Result: Map<Character, List<String>>."
    inputConfig:
      language: java
      starterCode: |
        List<String> spells = List.of("Fireball", "Frostbolt", "Lightning", "Life Drain", "Barrier");
        Map<Character, List<String>> grouped = new HashMap<>();
        // group by first character using computeIfAbsent
    markingRule: "computeIfAbsent used with key = first char, value factory = new ArrayList<>(), each spell added to correct list"
    hint: "grouped.computeIfAbsent(spell.charAt(0), k -> new ArrayList<>()).add(spell)"
    reflectionPrompt: "What would happen if you used putIfAbsent instead of computeIfAbsent for this pattern?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does map.getOrDefault(key, defaultValue) return?"
    options:
      - "The value for the key if present, otherwise puts defaultValue into the map and returns it"
      - "The value for the key if present, otherwise returns defaultValue without modifying the map"
      - "Always returns defaultValue if the key has not been accessed before"
      - "Returns null if the key is absent, ignoring defaultValue"
    correctIndex: 1
    feedback: "getOrDefault returns the mapped value if the key exists, or the specified default value otherwise. It does NOT modify the map. To put-and-return, use computeIfAbsent."
  - type: MULTIPLE_CHOICE
    question: "When iterating a Map, why is entrySet() preferred over keySet() + get(key)?"
    options:
      - "entrySet() returns entries in sorted order while keySet() does not"
      - "keySet() + get() performs two map lookups per iteration; entrySet() provides both key and value in one lookup"
      - "get() throws an exception if the key is absent"
      - "entrySet() is only available on HashMap, not TreeMap"
    correctIndex: 1
    feedback: "Calling get(key) inside a keySet() loop performs a second hash lookup for every entry. entrySet() provides Map.Entry objects with direct key and value access — only one traversal needed."
retrieval:
  recall: "Name three Map implementations in Java and describe one distinguishing property of each."
  explain: "Explain how HashMap handles the case where two different keys produce the same hash code (a hash collision)."
  mistakeId:
    code: |
      Map<String, Integer> scores = new HashMap<>();
      scores.put("Alice", 85);
      for (String key : scores.keySet()) {
          System.out.println(key + ": " + scores.get(key)); // two lookups!
      }
    answer: "Use entrySet() instead: `for (Map.Entry<String, Integer> entry : scores.entrySet()) { System.out.println(entry.getKey() + \": \" + entry.getValue()); }` — this performs one lookup per entry instead of two."
---

# Hook

Maps are the most versatile collection in your toolkit. Frequency counts, grouping, caching, lookup tables, index structures — all maps. You've used `get()` and `put()` before. This lesson goes deeper: the methods that make complex patterns concise (`getOrDefault`, `merge`, `computeIfAbsent`), how to iterate efficiently, and what's actually happening inside a HashMap when two keys share a hash value. Maps done well are O(1) lookups. Maps done naively are O(n) confusion.

# Lore Introduction

The Academy's spell registry maps spell names to their mana costs. But the deeper archive does more: it groups spells by school, counts how often each was cast in the last tournament, and provides O(1) lookup for any query. The archivist who learned `computeIfAbsent` reduced a hundred-line grouping loop to five lines. The one who learned `merge` eliminated the entire "check if key exists, then update" pattern. The right Map method makes the intent obvious and the code half the length.

# Core Learning

## Concept Introduction

**HashMap** — the default Map:
- Keys and values: any object (keys use `hashCode()` + `equals()`)
- `put()`, `get()`, `containsKey()`: O(1) average
- No ordering guarantee on keys

**TreeMap** — sorted by key:
- Keys sorted by natural order or Comparator
- `put()`, `get()`: O(log n)
- Use when you need sorted key iteration or range queries (`subMap()`, `headMap()`, `tailMap()`)

**LinkedHashMap** — insertion-order keys:
- O(1) operations, insertion or access-order iteration
- Useful for caches (LRU), stable output

**Key methods beyond get/put:**
- `getOrDefault(key, default)` — get or return fallback without modifying map
- `putIfAbsent(key, value)` — only put if key not present
- `merge(key, value, BiFunction)` — combine existing and new value
- `computeIfAbsent(key, Function)` — compute and put value if key absent, return it
- `forEach((k, v) -> ...)` — iterate without explicit entrySet loop

## Why It Matters

Frequency counting, grouping, and caching are core programming patterns. Writing them naively (check `containsKey`, then `get`, then compute, then `put`) is verbose and error-prone. `merge` and `computeIfAbsent` express these patterns in one atomic call. `entrySet()` iteration avoids the hidden double-lookup cost of `keySet()` + `get()`. These aren't advanced features — they're the standard tools that make Map code clean.

## Worked Examples

**Example 1 — Frequency counting with merge**

```java
List<String> classes = List.of("Warrior", "Mage", "Warrior", "Rogue", "Mage", "Warrior");

Map<String, Integer> classCount = new HashMap<>();
for (String cls : classes) {
    classCount.merge(cls, 1, Integer::sum);
    // First occurrence: puts (cls, 1)
    // Subsequent: sums existing value with 1
}
// {Warrior=3, Mage=2, Rogue=1}
```

**Example 2 — entrySet iteration and finding max**

```java
Map<String, Integer> scores = Map.of("Alice", 92, "Bob", 87, "Charlie", 95);

// Print all entries:
for (Map.Entry<String, Integer> entry : scores.entrySet()) {
    System.out.println(entry.getKey() + " scored " + entry.getValue());
}

// Find the top scorer:
Map.Entry<String, Integer> top = null;
for (Map.Entry<String, Integer> entry : scores.entrySet()) {
    if (top == null || entry.getValue() > top.getValue()) {
        top = entry;
    }
}
System.out.println("Top: " + top.getKey()); // Charlie
```

**Example 3 — Grouping with computeIfAbsent**

```java
List<String> items = List.of("Axe", "Arrow", "Bow", "Blade", "Bomb", "Crystal");

Map<Character, List<String>> byLetter = new HashMap<>();
for (String item : items) {
    byLetter.computeIfAbsent(item.charAt(0), k -> new ArrayList<>()).add(item);
}
// {A=[Axe, Arrow], B=[Bow, Blade, Bomb], C=[Crystal]}
```

**Example 4 — TreeMap for sorted key output**

```java
Map<String, Integer> scores = new TreeMap<>();
scores.put("Charlie", 95);
scores.put("Alice", 92);
scores.put("Bob", 87);

// Iterates in alphabetical key order:
scores.forEach((name, score) ->
    System.out.println(name + ": " + score));
// Alice: 92
// Bob: 87
// Charlie: 95
```

## Common Mistakes

- **Using `==` instead of `.equals()` on String keys.** HashMap calls `equals()` for key comparison — String equality works correctly. But if you override `hashCode()` without `equals()` on custom key types, keys become "lost."
- **Iterating with `keySet()` and calling `get(key)` inside the loop.** This doubles the lookup cost. Use `entrySet()`.
- **Assuming HashMap iteration order.** HashMap order is undefined. If order matters, use TreeMap or LinkedHashMap.
- **Using mutable objects as HashMap keys.** If an object's `hashCode()` changes after insertion, it can no longer be found in the map. Use immutable objects (String, Integer, record) as keys.
- **Using `put()` for frequency counting.** `map.put(k, map.get(k) + 1)` throws NullPointerException if the key is absent. Always use `getOrDefault` or `merge`.

## Mental Model

A HashMap is like a warehouse with numbered bays. When you store an item, the warehouse manager runs the item's serial number through a formula (hash) to get a bay number, then stores it there. To retrieve it, they run the same formula again and go straight to that bay — O(1). If two items hash to the same bay (collision), they're stored in a short list within that bay (a "bucket"), and the manager checks the serial numbers. As long as collisions are rare, performance stays near O(1).

## Mini Summary

- HashMap: O(1) operations, no order — the default Map for most use cases.
- TreeMap: O(log n), sorted keys — use when iteration in key order matters.
- LinkedHashMap: O(1), insertion or access order — useful for LRU caches.
- `merge()` for counting/accumulating; `computeIfAbsent()` for grouping.
- `entrySet()` for iterating with both key and value — never `keySet()` + `get()`.
- Mutable objects as Map keys cause hard-to-find bugs — prefer immutable key types.

# Guided Practice Quest

Complete the three steps: build a frequency count using `getOrDefault`, iterate with `entrySet()` and find the maximum, then group a list by first character using `computeIfAbsent`.

# Solo Practice Quest

Build a `SpellAnalyser` class with methods: `countBySchool(List<Spell> spells)` → `Map<String, Long>` (count spells per school), `groupByManaCost(List<Spell> spells)` → `Map<Integer, List<Spell>>` (group spells by mana cost), and `mostCastSpell(List<String> castLog)` → `String` (find the spell name that appears most in the log). Implement using the Map techniques from this lesson.

# Integration

Maps are the backbone of almost every non-trivial algorithm. In the **Searching Algorithms** lesson, you'll see that hash-based lookup is the mechanism that makes O(1) search possible. In the **Databases** module, you'll learn that indexes are essentially sorted maps. In Spring, the `@Cacheable` annotation uses a Map under the hood. The `groupingBy` collector in streams is a functional version of the `computeIfAbsent` grouping pattern you practiced here.

**Integration question:** You have a List of 1 million orders and need to look up orders by ID repeatedly. Would you store them in an `ArrayList<Order>` and call `list.stream().filter(o -> o.getId() == id).findFirst()`, or load them into a `Map<Long, Order>`? What is the time complexity of each approach per lookup?

# Lore Conclusion

The spell archive is now a masterwork of indexing. The frequency map shows which spells are cast most in tournaments (crucial for balance patches). The grouping map lets wizards browse by school without filtering through thousands of entries. The TreeMap ensures the guild directory is always alphabetically current. The archivist who once spent two hours on a grouping algorithm now does it in five lines. The right Map method makes intent obvious and waste impossible.
