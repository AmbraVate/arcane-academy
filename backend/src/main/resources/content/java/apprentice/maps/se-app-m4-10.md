---
id: se-app-m4-10
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m4
moduleTitle: "Module 4: Collections and Data"
moduleGlyph: "📦"
moduleSortOrder: 4
topicSlug: maps
topicTitle: "Maps"
topicSortOrder: 2
lesson: key_value_thinking
title: "Key Value Thinking"
sortOrder: 10
difficulty: 2
estimatedMinutes: 22
xpReward: 45
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [searching]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the key-value concept in their own words with a real-world analogy"
    - "Demonstrates correct use of put() to add entries to a HashMap"
    - "Demonstrates correct use of get() to retrieve a value by key"
    - "Correctly uses containsKey() to check before retrieving"
    - "Reflects on why Map lookup is faster than List search"
  keywords: [HashMap, key, value, put, get, containsKey, map, lookup]
  modelAnswer: |
    // A Map stores unique keys, each mapped to a value
    Map<String, Integer> spellPower = new HashMap<>();
    spellPower.put("Fireball", 80);
    spellPower.put("Ice Bolt", 55);
    spellPower.put("Thunder Strike", 95);

    // Retrieve by key
    int power = spellPower.get("Fireball"); // 80

    // Safe retrieval with containsKey check
    String spell = "Ice Bolt";
    if (spellPower.containsKey(spell)) {
        System.out.println(spell + " has power: " + spellPower.get(spell));
    }
    // Output: Ice Bolt has power: 55
guidedSteps:
  - id: kv-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which statement correctly adds a key-value pair to a HashMap?
      ```java
      Map<String, Integer> scores = new HashMap<>();
      ```
    inputConfig:
      options:
        - "scores.add(\"Alice\", 100);"
        - "scores.put(\"Alice\", 100);"
        - "scores.insert(\"Alice\", 100);"
        - "scores.set(\"Alice\", 100);"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["scores.put(\"Alice\", 100);"]
      rejectedFeedback: "Maps use put(key, value) to store entries. The List method add() does not exist on Map. HashMap follows the Map interface which defines put() as the insertion method."
    hint: "Map has its own insertion method — different from List."
    reflectionPrompt: "put() is consistent across all Map implementations — HashMap, TreeMap, LinkedHashMap all use it."

  - id: kv-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the code to retrieve the value associated with the key "Dragon":
      ```java
      Map<String, String> weaknesses = new HashMap<>();
      weaknesses.put("Dragon", "Ice");
      weaknesses.put("Troll", "Fire");

      String dragonWeakness = weaknesses.___("Dragon");
      ```
      What method name fills the blank?
    inputConfig:
      placeholder: "method name only"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["get"]
      rejectedFeedback: "get(key) retrieves the value associated with the given key. It returns null if the key does not exist in the map."
    hint: "This method takes a key and returns the corresponding value."
    reflectionPrompt: "get() returning null for missing keys is important to handle — use containsKey() first or getOrDefault() to provide a fallback."

  - id: kv-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In your own words, describe a real-world system (not a phone book) that works like a key-value map. What are the keys? What are the values? Why is instant lookup by key useful?
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [key, value, lookup, unique, retrieve]
      rejectedFeedback: "A good example: a supermarket product catalogue where the barcode (key) maps to product details (value). The barcode is unique, and scanning it instantly retrieves the product — no sequential search needed."
    hint: "Think about any system where you look something up using a unique identifier."
    reflectionPrompt: "Key-value thinking is everywhere: DNS (domain → IP), user sessions (token → user), configuration files (setting name → value)."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does map.get(\"missing\") return when the key does not exist?"
    options:
      - "An empty string"
      - "0"
      - "null"
      - "It throws a KeyNotFoundException"
    correctIndex: 2
    feedback: "get() returns null when the key is absent. This is why containsKey() checks or getOrDefault() are important — to avoid NullPointerException when using the result."
  - type: MULTIPLE_CHOICE
    question: "You call put() with a key that already exists in the map. What happens?"
    options:
      - "A second entry is added with the same key"
      - "An exception is thrown"
      - "The existing value is replaced with the new value"
      - "Nothing — put() is ignored for duplicate keys"
    correctIndex: 2
    feedback: "Keys in a HashMap are unique. Calling put() with an existing key replaces the old value with the new one and returns the old value."
retrieval:
  recall: "Name three HashMap methods and describe what each does."
  explain: "Why is key-value lookup faster than searching a list? What mental model helps you understand why?"
  mistakeId:
    code: |
      Map<String, Integer> health = new HashMap<>();
      health.put("Warrior", 200);
      health.put("Mage", 120);

      int mageHealth = health.get("mage");
      System.out.println("Mage health: " + mageHealth);
    answer: "The key 'mage' (lowercase) does not exist — the map stores 'Mage' (uppercase M). HashMap keys are case-sensitive. get('mage') returns null, and auto-unboxing null to int throws a NullPointerException. Fix by using the correct key case: health.get(\"Mage\"), or by normalising keys to lowercase consistently."
---

# Hook

Think about how your phone's contacts app works. You type a name — "Mum" — and instantly see the phone number. You do not scroll through thousands of contacts one by one. There is a direct path from name to number, and the app retrieves it in a fraction of a second regardless of whether you have 50 contacts or 5,000.

This is the power of the key-value structure. Instead of searching sequentially through everything, you use a unique **key** to go directly to the **value** you want. It is one of the most important and widely-used ideas in all of software engineering — and once you understand it, you will see it everywhere.

> Can you think of three places in your daily life where you look things up by a unique identifier rather than by browsing through everything? What makes those identifiers unique?

# Lore Introduction

Deep in the Arcane Academy's Vault of Correspondence, the senior wizards maintain a great enchanted tome: the Rune Index. Each spell has a unique rune symbol — its key — and looking up that rune in the tome instantly opens to the page describing the spell's effects, cost, and counter-charms. There is no need to read the tome from cover to cover; the rune symbol is the direct path.

This magical shortcut is the essence of a **Map**: a structure that pairs unique keys with their corresponding values. Archmage Veylan insists all senior mages memorise the Rune Index — not its contents, but its *structure*. "Understanding how information is organised," he says, "is the beginning of wisdom."

# Core Learning

## Concept Introduction

A **Map** in Java stores data as **key-value pairs**. Each key is unique; each key maps to exactly one value. The most common implementation is `HashMap`.

```java
Map<KeyType, ValueType> name = new HashMap<>();
```

Core methods:
- `put(key, value)` — adds or updates an entry
- `get(key)` — retrieves the value for a key (returns `null` if absent)
- `containsKey(key)` — returns `true` if the key exists
- `size()` — returns the number of entries
- `remove(key)` — deletes an entry

```java
Map<String, Integer> spellPower = new HashMap<>();
spellPower.put("Fireball", 80);      // add
spellPower.put("Ice Bolt", 55);      // add
spellPower.put("Fireball", 90);      // update (replaces 80)

System.out.println(spellPower.get("Fireball"));      // 90
System.out.println(spellPower.containsKey("Wind"));  // false
System.out.println(spellPower.size());               // 2
```

## Why It Matters

Lists force you to search item by item — O(n). A HashMap uses a **hash function** to compute a direct address from the key, so lookup is essentially O(1) — constant time regardless of how many entries exist. This makes HashMap ideal for any situation where you need fast lookup by a known identifier: user accounts, configuration settings, word frequency counts, caches, and far more.

## Worked Examples

**Example 1 — Simple phonebook**
```java
Map<String, String> phonebook = new HashMap<>();
phonebook.put("Alice", "555-0101");
phonebook.put("Bob",   "555-0102");
phonebook.put("Carol", "555-0103");

String number = phonebook.get("Bob");
System.out.println("Bob's number: " + number); // 555-0102
```

**Example 2 — Safe retrieval with containsKey**
```java
Map<String, Integer> inventory = new HashMap<>();
inventory.put("Sword", 3);
inventory.put("Shield", 1);

String item = "Potion";
if (inventory.containsKey(item)) {
    System.out.println("Stock: " + inventory.get(item));
} else {
    System.out.println(item + " not in inventory.");
}
// Output: Potion not in inventory.
```

**Example 3 — Using getOrDefault for safe fallback**
```java
Map<String, String> config = new HashMap<>();
config.put("theme", "dark");
config.put("language", "en");

// Returns "en" if key exists, "en" if not — safely
String lang = config.getOrDefault("language", "en");
String timeout = config.getOrDefault("timeout", "30s"); // fallback used
System.out.println(lang);    // en
System.out.println(timeout); // 30s
```

## Common Mistakes

- **Case-sensitive keys** — `"Dragon"` and `"dragon"` are different keys; always be consistent with key casing.
- **Not checking for null from get()** — if the key is absent, `get()` returns `null`; using that null without a check causes `NullPointerException`.
- **Using mutable objects as keys** — if a key object changes after being inserted, the hash changes and the entry becomes unretrievable.
- **Confusing Map with List** — Map has no `add()` or index-based access; use `put()` and `get()`.
- **Assuming HashMap preserves insertion order** — it does not; use `LinkedHashMap` if order matters.

## Mental Model

A HashMap is like a set of labelled post-boxes in a wall of pigeonholes. The label (key) is unique to one box, and anyone who knows the label can go directly to that box and retrieve its contents — without opening any other box. The hash function is what tells you which pigeonhole to go to from just the label.

## Mini Summary

✔ A Map stores unique key-value pairs; each key maps to exactly one value.
✔ `put(key, value)` adds or updates; `get(key)` retrieves; `containsKey(key)` checks existence.
✔ HashMap lookup is O(1) — far faster than linear search through a list.
✔ `get()` returns null for missing keys — always check or use `getOrDefault()`.
✔ Keys are case-sensitive strings; be consistent in how you store and look up keys.

# Guided Practice Quest

**The Rune Index**
You are building a simple spell lookup system for the Academy's enchanted compendium. Use a HashMap to store and retrieve spell data.
Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Create a Java program with a `HashMap<String, String>` that stores five countries as keys and their capital cities as values. Then write code that: (1) prints the capital of any two countries using `get()`; (2) checks whether a country that does not exist is in the map using `containsKey()`; (3) updates one capital to a new value and prints the updated value. Write out all the code, then reflect in 2-3 sentences: why is a Map better than two parallel ArrayLists (one for countries, one for capitals) for this use case?

# Integration

**Connecting to Psychology — Associative Memory**
The key-value structure mirrors how human associative memory works. Psychologists have long observed that humans do not store memories as sequential lists — we store them as networks of associations. When you smell coffee, your brain does not scan every memory sequentially; it jumps directly to the associated experiences: mornings, work, a particular café. This is **associative recall** — the psychological equivalent of a hash map.

When you design a HashMap in code, you are building the same kind of structure your brain already uses. This is why the concept feels intuitive once you grasp it: it reflects a deep pattern in how intelligent systems — biological or digital — organise information for fast retrieval.

> Can you think of a time when a smell, sound, or word immediately triggered a specific memory? What was the "key" and what was the "value" your brain retrieved?

# Lore Conclusion

The apprentice gazes at the Rune Index with new eyes. Each glowing symbol — each key — is a direct gateway to knowledge, bypassing the endless shelves of sequential scrolls. With a single gesture, they invoke the rune for "Thunderclap" and the entry appears instantly before them.

In the next lesson, you will learn to walk the full Rune Index — iterating over every key and value, building lookup tables, and using Maps to solve real counting problems. The archive's power is only beginning to reveal itself.

---
