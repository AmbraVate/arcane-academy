---
id: se-app-m4-11
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m4
moduleTitle: "Module 4: Data Structures"
moduleGlyph: "📦"
moduleSortOrder: 4
topicSlug: maps
topicTitle: "Maps"
topicSortOrder: 2
lesson: lookup_systems
title: "Lookup Systems"
sortOrder: 11
difficulty: 2
estimatedMinutes: 22
xpReward: 45
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [key_value_thinking]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly iterates over a Map using entrySet() with a for-each loop"
    - "Explains when to choose a Map over a List"
    - "Implements a frequency counting pattern using HashMap"
    - "Demonstrates use of keySet() or values() to access parts of a Map"
    - "Reflects on a real-world use case for a lookup table"
  keywords: [entrySet, keySet, iterate, frequency, count, lookup, table, Map]
  modelAnswer: |
    // Iterating over all entries
    Map<String, Integer> scores = new HashMap<>();
    scores.put("Alice", 95);
    scores.put("Bob", 87);
    scores.put("Carol", 92);

    for (Map.Entry<String, Integer> entry : scores.entrySet()) {
        System.out.println(entry.getKey() + ": " + entry.getValue());
    }

    // Frequency counting pattern
    String[] words = {"apple", "banana", "apple", "cherry", "banana", "apple"};
    Map<String, Integer> freq = new HashMap<>();
    for (String word : words) {
        freq.put(word, freq.getOrDefault(word, 0) + 1);
    }
    System.out.println(freq); // {apple=3, banana=2, cherry=1}
guidedSteps:
  - id: lookup-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does `map.entrySet()` return?
      ```java
      Map<String, Integer> map = new HashMap<>();
      map.put("a", 1);
      map.put("b", 2);
      ```
    inputConfig:
      options:
        - "A List of all keys"
        - "A Set of Map.Entry objects, each holding a key and value"
        - "A List of all values"
        - "An array of key-value pairs"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A Set of Map.Entry objects, each holding a key and value"]
      rejectedFeedback: "entrySet() returns a Set<Map.Entry<K,V>>. Each Map.Entry gives you both the key (via getKey()) and the value (via getValue()), making it the standard way to iterate over all entries."
    hint: "The return type name is a clue — it is a 'Set of Entries'."
    reflectionPrompt: "entrySet() is the most powerful iteration method — you get key and value together. keySet() gives only keys; values() gives only values."

  - id: lookup-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the frequency count — fill in the method that safely gets the current count (or 0 if absent):
      ```java
      Map<String, Integer> freq = new HashMap<>();
      String[] words = {"cat", "dog", "cat", "bird", "dog", "cat"};

      for (String word : words) {
          freq.put(word, freq.___(word, 0) + 1);
      }
      ```
      What method fills the blank?
    inputConfig:
      placeholder: "method name only"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["getOrDefault"]
      rejectedFeedback: "getOrDefault(key, defaultValue) returns the mapped value if the key exists, or the defaultValue if it does not. This avoids a NullPointerException when incrementing a count that has not been initialised."
    hint: "This method takes two arguments: the key and a fallback value."
    reflectionPrompt: "The pattern freq.put(word, freq.getOrDefault(word, 0) + 1) is one of the most common HashMap patterns you will write — memorise it."

  - id: lookup-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe a scenario where using a Map is clearly better than using a List. What specific property of a Map makes it the better choice in your scenario?
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [lookup, key, fast, unique, direct]
      rejectedFeedback: "A good example: storing user account details keyed by username. If you need to look up a specific user instantly, a Map gives O(1) lookup by key, while a List would require O(n) sequential search."
    hint: "Think about when you always know the 'name' or 'identifier' of the thing you want."
    reflectionPrompt: "Any time you find yourself writing a loop to search a List for a specific item, ask yourself: should this be a Map?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which method gives you only the keys of a Map, without the values?"
    options:
      - "entrySet()"
      - "keySet()"
      - "values()"
      - "keys()"
    correctIndex: 1
    feedback: "keySet() returns a Set<K> containing all the keys. Use it when you only need to iterate over keys, or check which keys are present. values() gives you the values; entrySet() gives you both."
  - type: MULTIPLE_CHOICE
    question: "You are counting word frequencies in a document. Which data structure is most appropriate?"
    options:
      - "ArrayList<String>"
      - "ArrayList<Integer>"
      - "HashMap<String, Integer>"
      - "HashMap<Integer, String>"
    correctIndex: 2
    feedback: "HashMap<String, Integer> maps each word (key) to its count (value). This is the canonical frequency-counting pattern — you can increment each word's count in O(1)."
retrieval:
  recall: "Explain three different ways to iterate over a HashMap's contents."
  explain: "A colleague builds two parallel ArrayLists — one for product names, one for prices — and zips them by index. Explain why a HashMap<String, Double> would be a better design."
  mistakeId:
    code: |
      Map<String, Integer> freq = new HashMap<>();
      String[] tokens = {"red", "blue", "red", "green"};

      for (String token : tokens) {
          freq.put(token, freq.get(token) + 1);
      }
      System.out.println(freq);
    answer: "The first time a new word (e.g. 'red') is encountered, freq.get('red') returns null because the key does not yet exist. Adding 1 to null causes a NullPointerException. Fix: use freq.getOrDefault(token, 0) + 1, or check containsKey first and initialise to 0."
---

# Hook

Every time you use an app that shows you personalised content — recommended songs, suggested friends, recently viewed items — there is a lookup table working behind the scenes. The app is not searching through millions of records one at a time; it is using keys to jump directly to precomputed results. Lookup systems are what make large-scale software feel instant.

Once you understand how to build and iterate over lookup tables, you will have a tool that shows up in almost every non-trivial program you will ever write: counting frequencies, caching expensive results, indexing data, routing messages. The humble HashMap is one of the hardest-working data structures in existence.

> Have you ever noticed that some websites load certain data almost instantly while others seem to search for it in real time? What might explain that difference in speed?

# Lore Introduction

The Academy's Grand Registry uses a Rune Catalogue — not just for looking up individual spells, but for tracking how often each spell has been cast across the realm. Every time a mage invokes a spell, the Registry's enchanted quill marks a tally next to that spell's rune. At the end of each moon cycle, the Archmage reviews which spells are most popular.

This tallying system — mapping spell names to their cast counts — is exactly the kind of lookup table you will build today. And just as the Registry quill can walk the entire catalogue from first rune to last, you will learn to iterate over every entry in a Map and extract exactly the information you need.

# Core Learning

## Concept Introduction

**Iterating over a Map** means visiting every key-value pair. Java provides three views:

| Method | Returns | Use when you need |
|---|---|---|
| `entrySet()` | `Set<Map.Entry<K,V>>` | Both key and value |
| `keySet()` | `Set<K>` | Keys only |
| `values()` | `Collection<V>` | Values only |

```java
Map<String, Integer> spellUses = new HashMap<>();
spellUses.put("Fireball", 42);
spellUses.put("Ice Bolt", 17);
spellUses.put("Heal", 88);

// Iterate with entrySet (most common)
for (Map.Entry<String, Integer> entry : spellUses.entrySet()) {
    System.out.println(entry.getKey() + " used " + entry.getValue() + " times");
}
```

**Frequency counting** is a canonical Map pattern:
```java
// Count how many times each element appears
freq.put(key, freq.getOrDefault(key, 0) + 1);
```

## Why It Matters

Lookup systems replace slow sequential searches with instant key-based access. Frequency tables are the foundation of text analysis, recommendation engines, and performance profiling. Knowing when to reach for a Map — and how to iterate over one — is an essential skill for writing efficient, readable code.

## Worked Examples

**Example 1 — Printing all map entries**
```java
Map<String, String> capitals = new HashMap<>();
capitals.put("France", "Paris");
capitals.put("Japan", "Tokyo");
capitals.put("Brazil", "Brasília");

for (Map.Entry<String, String> entry : capitals.entrySet()) {
    System.out.println(entry.getKey() + " → " + entry.getValue());
}
// France → Paris  (order not guaranteed with HashMap)
// Japan → Tokyo
// Brazil → Brasília
```

**Example 2 — Frequency counting**
```java
String[] spells = {"Fireball", "Ice Bolt", "Fireball", "Heal", "Fireball", "Ice Bolt"};
Map<String, Integer> freq = new HashMap<>();

for (String spell : spells) {
    freq.put(spell, freq.getOrDefault(spell, 0) + 1);
}

System.out.println(freq);
// {Fireball=3, Ice Bolt=2, Heal=1}
```

**Example 3 — Choosing Map vs List**
```java
// List approach — slow lookup O(n)
List<String> itemNames  = new ArrayList<>(Arrays.asList("Sword", "Shield", "Bow"));
List<Integer> itemCosts = new ArrayList<>(Arrays.asList(150, 80, 120));
// To find "Shield" price: loop through itemNames, find index, get from itemCosts

// Map approach — fast lookup O(1)
Map<String, Integer> shop = new HashMap<>();
shop.put("Sword", 150);
shop.put("Shield", 80);
shop.put("Bow", 120);
int shieldCost = shop.get("Shield"); // instant
```

## Common Mistakes

- **Modifying a Map while iterating** — adding or removing entries during a `for-each` loop causes `ConcurrentModificationException`; collect changes and apply after the loop.
- **Not handling null from get()** — if a key is absent, `get()` returns null; auto-unboxing null to an `int` throws `NullPointerException`.
- **Relying on HashMap ordering** — HashMap makes no ordering guarantees; use `LinkedHashMap` for insertion order or `TreeMap` for sorted keys.
- **Using the wrong iteration method** — using `keySet()` then calling `get()` inside the loop is less efficient than `entrySet()` which gives you both in one step.
- **Parallel list antipattern** — two lists indexed in sync are fragile; a Map is almost always cleaner and safer.

## Mental Model

Think of a Map as a spreadsheet with two columns: the left column is the key (unique per row), the right column is the value. `put()` adds or updates a row; `get()` looks up a row by its left-column value instantly; `entrySet()` gives you every row to loop through. The spreadsheet never has two rows with the same left-column value.

## Mini Summary

✔ `entrySet()` gives key+value pairs; `keySet()` gives only keys; `values()` gives only values.
✔ The frequency count pattern `freq.put(k, freq.getOrDefault(k, 0) + 1)` is universally useful.
✔ Choose Map over List when you need fast lookup by a known identifier.
✔ HashMap does not preserve insertion order — use `LinkedHashMap` if order matters.
✔ Never modify a Map while iterating it — collect changes and apply them after the loop.

# Guided Practice Quest

**The Spell Usage Registry**
The Academy's Registry needs a report of how many times each spell has been cast this week. Build the frequency table and iterate over the results.
Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Write a Java method `topWord(String[] words)` that returns the word that appears most frequently in the array (assume no ties). Use a `HashMap<String, Integer>` for frequency counting. Show the complete method body. Then write two example calls with different arrays and their expected outputs. Reflect in 3 sentences on what you would do differently if the array could contain null values.

# Integration

**Connecting to Mathematics — Set Theory and Functions**
A Map is a mathematical **function** in the strict sense: it maps each element of a domain (the set of keys) to exactly one element of a codomain (the set of values). In set theory, a function f: A → B assigns to every element of A exactly one element of B — precisely what a Map enforces with its unique-key constraint.

This connection to mathematics is not superficial. The performance guarantee of O(1) lookup comes from **hash functions** — mathematical functions that convert a key into a fixed-size integer (the hash code), which determines the storage location. Understanding that a Map is fundamentally a mathematical function helps you reason about its properties: if two keys are equal, they must hash to the same location; if they are not equal, they ideally hash to different locations.

> A mathematical function must assign exactly one output to each input. What does this mean for what happens when you call put() with an existing key? How does this mathematical property make Maps reliable?

# Lore Conclusion

The Registry quill completes its census and presents the Archmage with a neatly ordered count of every spell cast across the realm this moon cycle. "Fireball: three hundred and forty-two casts. Heal: four hundred and one. Shadow Step: eighty-seven." Archmage Veylan nods, satisfied. The lookup table has done its work in moments — work that would have taken a team of clerks days to compile manually.

You have now mastered the Map: how to populate it, how to retrieve from it, and how to walk its entire contents. In the coming modules, you will discover how these collection skills — lists, maps, and the searching and iteration patterns between them — combine to solve real engineering problems. The foundations are set; the spellwork begins in earnest.

---
