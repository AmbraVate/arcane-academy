---
id: fe-app-m5-09
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m5
moduleTitle: "Module 5: JavaScript Foundations"
moduleGlyph: "⚡"
moduleSortOrder: 5
topicSlug: decisions_and_loops
topicTitle: "Decisions and Loops"
topicSortOrder: 2
lesson: objects
title: "Objects"
sortOrder: 4
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m5-08]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Creates an object literal with at least three key-value pairs"
    - "Accesses a property using dot notation and bracket notation"
    - "Adds a new property to an existing object"
    - "Explains the difference between an object and an array"
    - "Uses an object to group related data about a single thing"
  keywords: [object, property, key, value, dot notation, bracket notation, literal, method]
  modelAnswer: |
    An object groups related key-value pairs. Properties are accessed with dot notation
    (obj.name) or bracket notation (obj['name']). Unlike arrays (ordered lists), objects
    describe a single thing's attributes. New properties can be added at any time.
    Objects are ideal when data has named attributes rather than a positional order.
guidedSteps:
  - id: js-obj-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Given `const user = { name: "Elara", level: 5 }`, which correctly accesses the name?
    inputConfig:
      options:
        - "user[0]"
        - "user.name"
        - "user('name')"
        - "user->name"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["user.name"]
      rejectedFeedback: "Object properties are accessed with dot notation (user.name) or bracket notation (user['name']). Index notation [0] is for arrays."
    hint: "Dot notation: objectName.propertyName"
    reflectionPrompt: "Correct. Dot notation (object.property) is the standard way to access object properties when the key is a valid identifier."

  - id: js-obj-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Add a new property `score` with value `100` to this object:

      ```js
      const player = { name: "Veylan" };
      player.___ = 100;
      ```
    inputConfig:
      placeholder: "property name"
    markingRule:
      matchMode: NORMALIZED
      accepted: [score]
      rejectedFeedback: "You add a new property by assigning to it: player.score = 100. JavaScript objects are open — you can always add properties after creation."
    hint: "Use dot notation to assign a new property: player.propertyName = value"
    reflectionPrompt: "Correct. Objects in JavaScript are dynamic — you can add or remove properties at any time. This is different from languages with fixed class structures."

  - id: js-obj-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in your own words: when would you use an **object** instead of an **array**?
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [name, key, property, attribute, describe, thing, related, label]
      rejectedFeedback: "Use an object when data has named attributes describing one thing (a user, a product). Use an array for an ordered list of similar items."
    hint: "An array is a list of items. An object is a description of one thing with named attributes."
    reflectionPrompt: "Well put. Objects describe a single thing's properties. Arrays hold multiple things. In practice, you often have an array of objects — a list of users, each described as an object."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which notation must you use when the property key contains a hyphen, like 'first-name'?"
    options:
      - "Dot notation: obj.first-name"
      - "Bracket notation: obj['first-name']"
      - "Either works equally"
      - "Hyphens are not allowed in property keys"
    correctIndex: 1
    feedback: "Dot notation only works with valid identifiers (no hyphens, spaces, or starting with digits). For keys that break these rules, use bracket notation with a string."
  - type: MULTIPLE_CHOICE
    question: "What does `Object.keys(obj)` return?"
    options:
      - "The values of all properties"
      - "An array of all property names"
      - "The number of properties"
      - "A copy of the object"
    correctIndex: 1
    feedback: "Object.keys(obj) returns an array of the object's property names (keys). Object.values(obj) returns an array of its values. Object.entries(obj) returns both as pairs."

retrieval:
  recall: "Write an object literal representing a book with title, author, and year."
  explain: "Explain when bracket notation is necessary instead of dot notation."
  mistakeId:
    code: "const product = {}; console.log(product.price.toFixed(2));"
    answer: "product.price is undefined because price was never set. Calling .toFixed(2) on undefined throws a TypeError. Always check if a property exists before accessing nested properties."
---

# Hook

An array can tell you *how many* things you have and what each one is.

But it cannot easily tell you *what those things are* beyond their position. To describe a user with a name, age, email, and profile picture — you need something more structured.

Objects let you group related data under named properties, describing a single thing with as many attributes as it needs. They are the fundamental data structure of JSON, APIs, and nearly every modern web application.

> If you were building a user profile for a web app, what named attributes would you need to store?

# Lore Introduction

Master Aelindra sets a complex artefact on the workbench — a carved wooden box with many labelled compartments, each with a small plaque on the front.

*"An array is a numbered row. Useful for lists. But a person is not a list — they have a name, a rank, a lineage, a set of skills. These attributes are named, not numbered. That is an object."*

She opens one compartment labelled `name` and places a small scroll inside.

*"Key. Value. The key is the name of the compartment. The value is what you put in it. Together, they describe something real."*

# Core Learning

## Concept Introduction

An **object** is a collection of named key-value pairs (called **properties**) that describes a single thing.

```js
const user = {
  name: "Elara",
  level: 5,
  isLoggedIn: true
};
```

| Concept | Example |
|---------|---------|
| Object literal | `{ key: value, key2: value2 }` |
| Dot notation access | `user.name` → `"Elara"` |
| Bracket notation access | `user["level"]` → `5` |
| Add a property | `user.email = "elara@academy.com"` |
| Delete a property | `delete user.isLoggedIn` |

## Why It Matters

Almost all data from APIs and databases arrives as objects (in JSON format). Understanding how to create, read, and update objects is essential for working with real-world data in any JavaScript application.

## Worked Examples

**Example 1 — Describing a product**

```js
const product = {
  title: "Arcane Codex",
  price: 29.99,
  inStock: true
};

console.log(product.title);    // Arcane Codex
console.log(product["price"]); // 29.99
```

**Example 2 — Adding and updating properties**

```js
const config = { theme: "dark" };
config.language = "en";     // add new property
config.theme = "light";     // update existing property
console.log(config);
// { theme: "light", language: "en" }
```

**Example 3 — An array of objects**

```js
const students = [
  { name: "Elara", grade: 92 },
  { name: "Veylan", grade: 78 },
  { name: "Mira", grade: 85 }
];

for (const student of students) {
  console.log(student.name + ": " + student.grade);
}
```

This is the most common pattern in web development: an array of objects, each representing one item in a list.

**Example 4 — Bracket notation for dynamic keys**

```js
const key = "name";
console.log(user[key]);  // equivalent to user.name
```

Bracket notation is essential when the property name is stored in a variable.

## Common Mistakes

- Confusing objects with arrays — objects use named keys, arrays use numeric indexes
- Using dot notation with property names that are not valid identifiers (contain hyphens, spaces, or start with digits)
- Accessing a property that doesn't exist — returns `undefined`, not an error
- Trying to iterate an object with `for...of` — use `Object.keys()`, `Object.values()`, or `Object.entries()`

## Mental Model

An object is a **passport**.

A passport is one thing (one person), but it has many named fields: name, nationality, date of birth, passport number. You look up a field by its name — not by saying "give me the third field".

An array is a **queue** — items in order, accessed by position. An object is a **form** — fields identified by label.

## Mini Summary

- Objects group related data as named key-value pairs
- Access properties with dot notation (`obj.key`) or bracket notation (`obj["key"]`)
- Use bracket notation for dynamic keys or keys with special characters
- Objects are dynamic — properties can be added or removed after creation
- Arrays of objects are the most common data pattern in web development

# Guided Practice Quest

In this quest you will access object properties, add a new property, and explain when to use objects versus arrays.

These steps prepare you for working with JSON data from APIs — which is almost always an object or an array of objects.

# Solo Practice Quest

Create an object that describes yourself as an apprentice at Arcane Academy. Include at least five properties: name, age, favouriteLanguage, lessonsCompleted (a number), and one more of your choice.

Then:
1. Log each property individually using dot notation
2. Add a new property `goal` using bracket notation
3. Use `Object.keys()` to log all your property names

# Integration

**Connecting to Psychology — Schema Theory**

In cognitive psychology, a *schema* is a mental framework for organising and interpreting information. When you see a "user profile", your brain immediately structures it with fields like name, photo, and preferences — because your existing schema for "user profile" has those slots.

JavaScript objects are a computational schema. The keys define the expected structure; the values fill the slots. Well-designed object schemas — choosing clear, consistent property names — make code easier to reason about because they align with the mental model a developer already has.

When you name a property `firstName` instead of `fn`, you are aligning the code schema with the human schema. That alignment reduces cognitive load and prevents mistakes.

# Lore Conclusion

The apprentice closes the labelled box, satisfied with the arrangement.

*"A user is not a list,"* Master Aelindra says. *"A product is not a list. A message is not a list. Each is a thing described by its properties. Now you can represent things — not just sequences."*

The carved box joins the numbered case on the shelf: two tools, two kinds of structure, ready for whatever the enchantment demands.

---
