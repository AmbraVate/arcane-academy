---
id: fe-app-m5-08
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
lesson: arrays
title: "Arrays"
sortOrder: 3
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m5-07]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Creates an array with at least three items"
    - "Accesses an item by index correctly (zero-based)"
    - "Uses push() to add an item and pop() to remove one"
    - "Loops over the array using for...of or a for loop with .length"
    - "Explains why arrays use zero-based indexing"
  keywords: [array, index, push, pop, length, iterate, zero-based, forEach]
  modelAnswer: |
    An array is an ordered list of values. Items are accessed by zero-based index
    (first item is [0]). push() adds to the end; pop() removes the last item.
    Arrays have a .length property. You iterate with for...of, forEach, or a
    standard for loop using index up to array.length - 1.
guidedSteps:
  - id: js-arr-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Given `const colours = ["red", "green", "blue"]`, what is `colours[1]`?
    inputConfig:
      options:
        - '"red"'
        - '"green"'
        - '"blue"'
        - "undefined"
    markingRule:
      matchMode: NORMALIZED
      accepted: ['"green"', "green"]
      rejectedFeedback: "Arrays use zero-based indexing: [0] is 'red', [1] is 'green', [2] is 'blue'. Index 1 is the second item."
    hint: "Counting in arrays starts at 0, not 1. The second item is at index ___."
    reflectionPrompt: "Correct. Zero-based indexing is one of the most important things to memorise in programming. The first item is always [0]."

  - id: js-arr-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Which method adds an item to the **end** of an array?
    inputConfig:
      placeholder: "method name"
    markingRule:
      matchMode: NORMALIZED
      accepted: [push, "push()", ".push()"]
      rejectedFeedback: "push() adds to the end of an array. pop() removes from the end. unshift() adds to the start. shift() removes from the start."
    hint: "Think of pushing something onto a stack — you push it on top (the end)."
    reflectionPrompt: "Correct. push(value) is the most common way to grow an array. Its counterpart, pop(), removes and returns the last item."

  - id: js-arr-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Write a for...of loop that logs each item in `const fruits = ["apple", "banana", "cherry"]`.
    inputConfig:
      minWords: 5
    markingRule:
      matchMode: CONTAINS
      accepted: ["for", "of", "fruits", "console.log"]
      rejectedFeedback: "The for...of syntax is: for (const item of array) { console.log(item); }"
    hint: "for (const ___ of fruits) { console.log(___); }"
    reflectionPrompt: "for...of is the cleanest way to iterate over array items when you don't need the index. Use a standard for loop with i when you need the index too."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `arr.length` return for `const arr = [10, 20, 30]`?"
    options:
      - "2"
      - "3"
      - "30"
      - "undefined"
    correctIndex: 1
    feedback: "arr.length returns the number of items in the array — 3 items means length is 3. The last valid index is always length - 1 (index 2 here)."
  - type: MULTIPLE_CHOICE
    question: "What does `arr.pop()` do?"
    options:
      - "Adds an item to the start of the array"
      - "Removes and returns the first item"
      - "Removes and returns the last item"
      - "Empties the entire array"
    correctIndex: 2
    feedback: "pop() removes the last item and returns it. push() adds to the end. shift() removes from the start. unshift() adds to the start."

retrieval:
  recall: "List four array methods and what each one does."
  explain: "Explain zero-based indexing and why the last element is at index length - 1."
  mistakeId:
    code: "const items = ['a','b','c']; console.log(items[3]);"
    answer: "The array has three items at indices 0, 1, 2. Index 3 is out of bounds — JavaScript returns undefined rather than throwing an error. Access the last item with items[items.length - 1] or items[2]."
---

# Hook

A single variable holds one value. But web pages rarely deal with one of anything.

A product listing has dozens of products. A navigation menu has several links. A photo gallery has many images. Programming needs a structure that holds an ordered collection of values — and that structure is an array.

Arrays are the most common data structure in JavaScript front-end development. Nearly everything you build will use them.

> Think of any list on a webpage you visit regularly. What kind of data is in that list?

# Lore Introduction

Master Aelindra produces a long case divided into numbered compartments.

*"A single vessel for a single ingredient — that is fine for simple work,"* she says. *"But when your enchantment requires a dozen components in sequence, you need a case. Ordered. Numbered. Accessible by position."*

She opens the case to reveal twenty compartments, each labelled — starting from zero.

*"Zero,"* she says, tapping the first compartment. *"Not one. Zero. This is the convention of the craft, and it does not bend for sentiment."*

# Core Learning

## Concept Introduction

An **array** is an ordered, zero-indexed list of values.

```js
const colours = ["red", "green", "blue"];
//  index:          0       1       2
```

| Operation | Syntax | Result |
|-----------|--------|--------|
| Create | `const arr = [1, 2, 3]` | Array with 3 items |
| Access by index | `arr[0]` | First item |
| Get length | `arr.length` | Number of items |
| Add to end | `arr.push(4)` | Array now has 4 items |
| Remove from end | `arr.pop()` | Removes and returns last item |
| Add to start | `arr.unshift(0)` | Adds 0 at index 0 |
| Remove from start | `arr.shift()` | Removes and returns first item |

## Why It Matters

Arrays are the backbone of dynamic web content. When you render a list of search results, display a menu, or show a gallery, you are iterating over an array. Understanding arrays — how to create, access, modify, and iterate them — is essential for any JavaScript developer.

## Worked Examples

**Example 1 — Creating and accessing**

```js
const scores = [88, 92, 74, 100, 65];
console.log(scores[0]);            // 88 — first item
console.log(scores[4]);            // 65 — last item
console.log(scores[scores.length - 1]); // 65 — last item dynamically
```

**Example 2 — Adding and removing**

```js
const cart = ["apple", "bread"];
cart.push("milk");          // ["apple", "bread", "milk"]
const last = cart.pop();    // "milk" removed and returned
console.log(cart);          // ["apple", "bread"]
```

**Example 3 — Iterating with for...of**

```js
const names = ["Elara", "Veylan", "Aelindra"];
for (const name of names) {
  console.log("Hello, " + name + "!");
}
```

**Example 4 — Iterating with index**

```js
const items = ["sword", "shield", "potion"];
for (let i = 0; i < items.length; i++) {
  console.log(i + ": " + items[i]);
}
// 0: sword
// 1: shield
// 2: potion
```

## Common Mistakes

- Using index 1 for the first item — arrays start at 0, always
- Accessing `arr[arr.length]` — that index is always `undefined`; use `arr[arr.length - 1]` for the last item
- Mutating an array declared with `const` and being surprised it works — `const` prevents reassignment, not mutation
- Forgetting that `pop()` and `shift()` *modify* the array, not just read it

## Mental Model

An array is a numbered row of letterboxes.

The house number is the array name. Each box has a number starting from 0. You can reach in and retrieve any box's contents by number, add a new box at the end, or remove the last box.

The total number of boxes is `.length`. The last box is always at position `.length - 1`.

## Mini Summary

- Arrays hold ordered lists of values, indexed from 0
- Access items with `array[index]`; the last item is at `array[array.length - 1]`
- Common methods: `push()`, `pop()`, `unshift()`, `shift()`, `length`
- Iterate with `for...of` (clean) or `for` with index (when you need the position)
- `const` arrays can still be mutated — `const` only prevents reassignment of the variable

# Guided Practice Quest

In this quest you will access items by index, add and remove items, and write a `for...of` loop over a small array.

These three steps build the array fluency you need to work with dynamic content in DOM manipulation.

# Solo Practice Quest

Create an array of five of your favourite films. Then:
1. Log the first and last film using index notation
2. Add a sixth film with `push()`
3. Remove the last film with `pop()` and log what was removed
4. Use a `for...of` loop to log each film with its number (1: ..., 2: ..., etc.)

# Integration

**Connecting to Mathematics — Sequences and Indexing**

In mathematics, a sequence is an ordered list of values: a₁, a₂, a₃, ... Mathematicians conventionally use 1-based indexing. Computer scientists use 0-based indexing — a deliberate choice rooted in how memory addresses work.

When an array starts at memory address M, the item at position i is at address M + i × (size of item). If i starts at 0, the first item is at M + 0 = M — elegant and efficient. If i started at 1, every address calculation would need a correction. Zero-based indexing is not arbitrary tradition; it is a direct consequence of how computers address memory.

# Lore Conclusion

The apprentice carefully places a scroll into compartment 0 of the case, then another into compartment 1.

*"You understand the container now,"* Master Aelindra says. *"Not just one ingredient — a sequence. In the next lesson, we add shape to that sequence: objects that describe not just a list, but a thing with properties."*

The numbered case closes with a soft click.

---
