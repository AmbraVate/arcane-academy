---
id: se-app-m4-09
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m4
moduleTitle: "Module 4: Data Structures"
moduleGlyph: "📦"
moduleSortOrder: 4
topicSlug: lists
topicTitle: "Lists"
topicSortOrder: 1
lesson: searching
title: "Searching"
sortOrder: 9
difficulty: 2
estimatedMinutes: 20
xpReward: 45
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [removing_items]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the difference between contains() and indexOf() clearly"
    - "Describes a scenario where a manual loop search is preferable to contains()"
    - "Correctly explains what O(n) means in plain terms"
    - "Identifies at least one real-world analogy for linear search"
    - "Reflects on when searching a list versus using a different data structure is appropriate"
  keywords: [linear, search, contains, indexOf, loop, O(n), ArrayList, sequential]
  modelAnswer: |
    // Using contains() to check membership
    List<String> spells = new ArrayList<>();
    spells.add("Fireball");
    spells.add("Ice Bolt");
    spells.add("Thunder Strike");

    boolean hasFireball = spells.contains("Fireball"); // true

    // Using indexOf() to find position
    int pos = spells.indexOf("Ice Bolt"); // 1

    // Manual loop search (useful when checking a condition, not just equality)
    String target = "Thunder Strike";
    int foundAt = -1;
    for (int i = 0; i < spells.size(); i++) {
        if (spells.get(i).equals(target)) {
            foundAt = i;
            break;
        }
    }
    System.out.println("Found at index: " + foundAt); // Found at index: 2
guidedSteps:
  - id: search-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which method would you use to check if an ArrayList contains the value "Dragon"?
      ```java
      List<String> enemies = new ArrayList<>();
      enemies.add("Goblin");
      enemies.add("Dragon");
      enemies.add("Troll");
      ```
    inputConfig:
      options:
        - "enemies.find(\"Dragon\")"
        - "enemies.contains(\"Dragon\")"
        - "enemies.search(\"Dragon\")"
        - "enemies.has(\"Dragon\")"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["enemies.contains(\"Dragon\")"]
      rejectedFeedback: "ArrayList uses the contains() method inherited from the Collection interface. It returns true if the element is present, false otherwise."
    hint: "Think about which Collection method checks for membership."
    reflectionPrompt: "contains() is a clean, readable way to check membership — it hides the loop from you."

  - id: search-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the code so it finds the position of "Ice Bolt" in the list:
      ```java
      List<String> spells = new ArrayList<>();
      spells.add("Fireball");
      spells.add("Ice Bolt");
      spells.add("Thunder Strike");

      int position = spells.___(\"Ice Bolt\");
      ```
      What method name fills the blank?
    inputConfig:
      placeholder: "method name only"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["indexOf"]
      rejectedFeedback: "indexOf() returns the zero-based position of the first occurrence of the element, or -1 if not found."
    hint: "This method returns an integer index, not a boolean."
    reflectionPrompt: "indexOf() returning -1 when not found is a convention you'll see across Java APIs — check for -1 before using the result."

  - id: search-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In plain English, explain what "O(n)" means for a linear search through a list of n items. Why does the list size matter?
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [linear, grows, size, every, proportional]
      rejectedFeedback: "O(n) means the time to search grows proportionally with the number of items. If you have 1000 items, you may need to check all 1000. The more items, the longer it potentially takes."
    hint: "Think about the worst case — what happens if the item is at the very end or not there at all?"
    reflectionPrompt: "Understanding O(n) is your first step into algorithmic thinking — a superpower for writing efficient code."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does indexOf() return when the element is NOT found in the list?"
    options:
      - "0"
      - "null"
      - "-1"
      - "It throws an exception"
    correctIndex: 2
    feedback: "indexOf() returns -1 when the element is not present. Always check for -1 before using the returned index to avoid accessing invalid positions."
  - type: MULTIPLE_CHOICE
    question: "You need to find all items in a list that start with the letter 'A'. Which approach is best?"
    options:
      - "Use contains() with 'A'"
      - "Use indexOf() and check the character"
      - "Write a manual loop with a condition"
      - "Sort the list first, then use indexOf()"
    correctIndex: 2
    feedback: "When your search condition is more complex than exact equality, a manual loop gives you full control over the matching logic. contains() and indexOf() only support exact equality matching."
retrieval:
  recall: "Name two built-in ArrayList methods used for searching, and describe what each returns."
  explain: "A classmate says 'just use contains() for everything'. When would a manual loop search be a better choice?"
  mistakeId:
    code: |
      List<String> potions = new ArrayList<>();
      potions.add("Health");
      potions.add("Mana");
      potions.add("Stamina");

      int idx = potions.indexOf("Mana");
      String found = potions.get(idx + 1);
      System.out.println(found);
    answer: "The code adds 1 to the index of 'Mana' (which is 1), so it accesses index 2 ('Stamina') — this may be intentional but is fragile. More critically, if indexOf() returned -1 (item not found), calling get(-1 + 1) = get(0) would return the wrong element silently. Always check idx != -1 before using the result."
---

# Hook

Imagine you are a librarian in a vast magical archive with thousands of scrolls. A wizard rushes in and asks for the Scroll of Eternal Flame. You have no catalogue, so you start at the first shelf and work your way along, one scroll at a time, until you find it — or reach the end. This is exactly what a computer does when it searches through a list: it checks each element in sequence until a match is found.

This simple idea — scanning from start to finish — is called **linear search**, and it is the foundation of every search operation you will perform on an unsorted list. Understanding how it works, and what it costs, will help you write code that is both correct and appropriately efficient.

> When you search for something in your own memory, do you always start from the beginning, or do you have shortcuts? What does that tell you about the difference between organised and unorganised information?

# Lore Introduction

In the Grand Archive of the Arcane Academy, the Scroll Keepers maintain thousands of enchanted manuscripts in long oak shelves. When a young apprentice needs a specific scroll, they must walk the shelves from left to right, reading the title of each until they find their quarry. The more scrolls that have been added, the longer the search might take.

Archmage Veylan once remarked: "A mage who knows the cost of their search is a mage who knows when to reorganise their archive." In code, this wisdom translates to choosing the right search strategy for the right situation — and understanding exactly why your choice matters.

# Core Learning

## Concept Introduction

**Searching** a list means locating an element (or its position) within that list. Java's `ArrayList` provides two key methods:

- `contains(Object o)` — returns `true` if the element is in the list, `false` otherwise
- `indexOf(Object o)` — returns the **zero-based index** of the first occurrence, or **-1** if not found

Both methods perform a **linear search** internally — they iterate from index 0 to the end, comparing each element with the target.

```java
List<String> runes = new ArrayList<>();
runes.add("Fire");
runes.add("Water");
runes.add("Earth");

boolean hasWater = runes.contains("Water"); // true
int earthPos  = runes.indexOf("Earth");     // 2
int airPos    = runes.indexOf("Air");       // -1 (not found)
```

## Why It Matters

Most real programs need to find things — a user by name, a product by ID, a setting by key. Understanding how searching works at the list level helps you recognise when `ArrayList` is the right tool and when you need something faster. It also teaches you to handle the "not found" case gracefully, which prevents subtle bugs like treating -1 as a valid index.

## Worked Examples

**Example 1 — Checking membership before acting**
```java
List<String> unlockedSpells = new ArrayList<>();
unlockedSpells.add("Fireball");
unlockedSpells.add("Shield");

String attempt = "Fireball";
if (unlockedSpells.contains(attempt)) {
    System.out.println("Casting " + attempt);
} else {
    System.out.println("Spell not unlocked yet!");
}
// Output: Casting Fireball
```

**Example 2 — Using indexOf() safely**
```java
List<String> queue = new ArrayList<>();
queue.add("Alice");
queue.add("Bob");
queue.add("Charlie");

int pos = queue.indexOf("Bob");
if (pos != -1) {
    System.out.println("Bob is at position " + pos); // Bob is at position 1
} else {
    System.out.println("Bob is not in the queue.");
}
```

**Example 3 — Manual loop search for complex conditions**
```java
List<Integer> scores = new ArrayList<>();
scores.add(45);
scores.add(78);
scores.add(92);
scores.add(61);

// Find first score above 75
int highScore = -1;
for (int score : scores) {
    if (score > 75) {
        highScore = score;
        break;
    }
}
System.out.println("First high score: " + highScore); // 78
```

## Common Mistakes

- **Ignoring the -1 return value** — calling `list.get(indexOf(...))` without checking for -1 will throw an `IndexOutOfBoundsException` if the element is absent.
- **Using == instead of .equals()** — comparing strings with `==` checks reference equality; `contains()` and `indexOf()` correctly use `.equals()`, but beware in manual loops.
- **Assuming contains() is O(1)** — it is O(n) on an ArrayList. For fast lookups, consider a `HashSet` or `HashMap`.
- **Searching a null list** — always initialise your list before calling search methods to avoid `NullPointerException`.
- **Off-by-one after indexOf()** — remember indices start at 0; do not accidentally add 1 thinking lists start at 1.

## Mental Model

Think of a list search as a security guard checking IDs at a long queue. They start at the front and work down the line, comparing each ID to the target face. In the best case the target is first; in the worst case they reach the end. The longer the queue, the longer the check — that is O(n) in action.

## Mini Summary

✔ `contains()` returns a boolean — use it when you only need to know *if* something is present.
✔ `indexOf()` returns an integer index — use it when you need to know *where* something is.
✔ Both methods return -1 (or false) when the element is absent; always handle the not-found case.
✔ Both perform a linear O(n) scan — performance degrades as the list grows.
✔ For complex matching conditions, write a manual loop; for exact equality, prefer the built-in methods.

# Guided Practice Quest

**The Scroll Keeper's Search**
The Arcane Academy's archive has received a new batch of scrolls. You must search the collection efficiently using both built-in methods and manual loops.
Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Write a small Java method called `findFirstLongSpell` that takes a `List<String>` and an `int minLength`, and returns the first spell whose name has at least `minLength` characters. Return an empty string `""` if none is found. Do not use any external library — only a loop and `.length()`. Write the method signature and body, then write two example calls with their expected output. Reflect in 2-3 sentences: why can't `contains()` or `indexOf()` solve this problem directly?

# Integration

**Connecting to Mathematics — Algorithmic Complexity**
Linear search introduces the concept of **algorithmic complexity** — a mathematical way of describing how performance scales with input size. When a list has *n* elements, a linear search examines at most *n* elements: one comparison per element in the worst case. Mathematicians write this as O(n) — "order n" — meaning the work grows proportionally with input size.

This is your first encounter with **Big O notation**, a tool borrowed from mathematics that software engineers use every day to evaluate and compare algorithms. A sorted list could be searched in O(log n) with binary search, which is dramatically faster for large collections. Understanding O(n) vs O(log n) vs O(1) is a fundamental mathematical literacy skill for any engineer.

> If your list doubles in size, how does O(n) search time change? What about O(1)? What does this tell you about choosing the right data structure?

# Lore Conclusion

The apprentice returns to the archive and successfully locates the Scroll of Eternal Flame — after checking forty-seven other scrolls first. Exhausted but wiser, they understand now why the senior Scroll Keepers spend so much effort maintaining catalogued indices. Brute-force searching works, but it does not scale.

In the next lesson, you will discover a structure that abandons sequential search entirely: the **Map**, where any value can be retrieved in an instant by its unique key. The archive is about to get a lot more organised.

---
