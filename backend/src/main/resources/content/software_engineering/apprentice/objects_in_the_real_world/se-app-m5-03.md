---
id: se-app-m5-03
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m5
moduleTitle: "Module 5: Object Thinking Foundations"
moduleGlyph: "🔷"
moduleSortOrder: 5
topicSlug: objects_in_the_real_world
topicTitle: "Objects in the Real World"
topicSortOrder: 1
lesson: real_world_modelling
title: "Real World Modelling"
sortOrder: 3
difficulty: 2
estimatedMinutes: 22
xpReward: 40
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m5-01, se-app-m5-02]
integrationDomains: [philosophy, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies a real-world system and lists at least three objects within it"
    - "For each object, lists relevant fields (state) with types"
    - "For each object, lists relevant methods (behaviour)"
    - "Explains the criteria used to decide which attributes are relevant"
    - "Keeps the model appropriately simple — does not over-engineer"
  keywords: [model, object, relevant, attribute, behaviour, system, identify, simplify]
  modelAnswer: |
    Library system: Book (title:String, isAvailable:boolean | borrow(), returnBook()),
    Member (name:String, borrowedCount:int | borrowBook(), returnBook()),
    Librarian (staffId:String | checkIn(Book), checkOut(Book)).
    Relevant attributes are chosen based on what the system needs to track —
    a Book's colour is irrelevant to a library system even though real books have colours.

guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_ANSWER
    instruction: "Consider a simple online shop. List three objects you would identify in this system. Just names, one per line."
    inputConfig:
      placeholder: "e.g. Product\nCustomer\n..."
    markingRule: KEYWORD_MATCH
    hint: "Think about the things involved in buying something online: the item, the person buying, the basket, the order..."
    reflectionPrompt: "How did you decide what counts as an 'object' vs just a piece of data?"

  - id: step-2
    sortOrder: 2
    inputType: SHORT_ANSWER
    instruction: "Pick one of your three objects. Write at least two fields and two methods for it. Format: Fields: f1, f2 | Methods: m1(), m2()"
    inputConfig:
      placeholder: "Fields: ... | Methods: ..."
    markingRule: KEYWORD_MATCH
    hint: "Choose fields that the system actually needs to use. A Product might need name and price — but probably not its weight unless you're calculating shipping."
    reflectionPrompt: "Did you include any attributes that turned out to be irrelevant to an online shop? What made you decide to leave them out?"

  - id: step-3
    sortOrder: 3
    inputType: SHORT_ANSWER
    instruction: "Explain in 2-3 sentences what 'relevant' means when choosing fields for an object model. Why not just include every possible attribute?"
    inputConfig:
      placeholder: "Write your explanation here..."
    markingRule: KEYWORD_MATCH
    hint: "A real dog has a favourite sleeping position. Does a Dog class for a vet booking system need that field?"
    reflectionPrompt: "How does the purpose of the system guide which attributes are relevant?"

microCheckpoint:
  - question: "When modelling a Person object for a payroll system, which field is most relevant?"
    options:
      - "favouriteColour (String)"
      - "salary (double)"
      - "shoeSize (int)"
      - "petName (String)"
    correctIndex: 1
    feedback: "Correct — a payroll system needs to process salary, so that is the relevant field. The others are real facts about a person but irrelevant to payroll."

  - question: "A school library system needs a Book object. Which pair of fields is most appropriate?"
    options:
      - "title (String) and printDate (String)"
      - "title (String) and isAvailable (boolean)"
      - "coverColour (String) and weight (double)"
      - "authorBirthYear (int) and publisherCity (String)"
    correctIndex: 1
    feedback: "Yes — the library needs to know the book's name and whether it can be borrowed right now. The other fields are facts about real books but useless to the library system."

retrieval:
  recall: "What principle guides which fields you include when modelling a real-world object?"
  explain: "Explain why two programmers building systems about the same real-world entity (a Car) might produce very different object models, and why both could be correct."
  mistakeId:
    code: |
      // Student models a Student object for a grades app:
      class Student {
          String name;
          int age;
          String hairColour;
          double height;
          String favouriteSport;
          double gradeAverage;
      }
    answer: "hairColour, height, and favouriteSport are irrelevant to a grades application. The model should only include fields the system actually needs: name, age (possibly), and gradeAverage."
---

# Hook

A map of a city does not show every crack in the pavement or every leaf on every tree. It shows roads, buildings, and landmarks — the things you need to navigate. A bad map includes everything and becomes unreadable. Object modelling is exactly the same skill: you must look at a real-world system and decide which details matter for *your purpose*, then faithfully represent those and leave the rest out. The art is not in knowing everything about an object — it is in knowing what to leave behind.

# Lore Introduction

The Academy's cartographers once attempted to create the Perfect Map — one that represented every pebble, every shadow, every blade of grass in the kingdom. It took forty years and filled an entire library. It was also completely useless; no wizard could carry it, let alone read it. The Grandmaster of Design decreed that from that day forward, all Academy constructs would be built by a single rule: *model only what serves the spell*. Apprentices who master this rule become architects; those who do not become buried under their own complexity.

# Core Learning

## Concept Introduction

**Real-world modelling** is the process of looking at a system — a shop, a library, a game — and identifying the key entities (objects), their relevant attributes (fields), and their relevant actions (methods).

The word *relevant* is critical. A real dog has a heartbeat rate, a tooth count, and a favourite sleeping position. For a vet booking app, none of those are needed — but `name`, `ownerName`, and `lastVisitDate` are. The system's *purpose* determines relevance.

**Three-step process:**
1. **Identify entities** — what are the distinct "things" in this system?
2. **Assign state** — what does each thing need to remember (for this system)?
3. **Assign behaviour** — what can each thing do (in this system)?

## Why It Matters

Getting the object model right before writing code prevents enormous amounts of rework. A poorly modelled system is like a house built on a tilted foundation — every room above it is wrong too. Practising real-world modelling on paper before touching the keyboard is one of the most valuable habits you can build as a developer.

## Worked Examples

**System: Cinema Booking App**

*Step 1 — Identify entities:*
- `Film`, `Screening`, `Seat`, `Booking`, `Customer`

*Step 2 & 3 — State and behaviour for `Film`:*
```
Film
  State:  title (String), durationMinutes (int), ageRating (String)
  Behaviour: getTitle(), getDuration(), isRatedSuitableFor(int age)
```

*State and behaviour for `Seat`:*
```
Seat
  State:  seatNumber (String), isBooked (boolean)
  Behaviour: book(), release(), isAvailable()
```

Notice what is **not** in `Film`: the director's birthday, the film's budget, or the colour of the poster. Those facts are real, but irrelevant to a booking app.

## Common Mistakes

- **Over-modelling**: including every possible field because "it might be useful someday". Keep it to what the system actually needs.
- **Under-modelling**: missing key entities. A booking system without a `Booking` object is incomplete — someone has to hold the link between a customer and a seat.
- **Merging separate concerns**: putting a customer's payment details inside the `Seat` object instead of in a separate object.

## Mental Model

Think of real-world modelling as **casting a shadow**. A real object has infinite detail — but when light hits it from a specific angle, it casts a clear, simple shadow. Your object model is the shadow: a simplified projection of reality from the angle that matters for your system. Different angles (different systems) produce different shadows of the same object.

## Mini Summary

- ✔ Identify the key **entities** in a system — these become your classes.
- ✔ Choose **state** based on what the system needs to track, not what is true in reality.
- ✔ Choose **behaviour** based on what actions the system must support.
- ✔ Relevance is always relative to the **purpose** of the system.
- ✔ A simple model that does the job is better than a perfect model that cannot be built.

# Guided Practice Quest

Work through the sidebar steps to model an online shop system. You will identify objects, design fields and methods for one of them, and reflect on the principle of relevance.

# Solo Practice Quest

**Spell: Architect the Academy**

Model a school management system. The system must handle students, teachers, and courses.

1. For each of the three entities, write at least **two fields** (with types) and **two methods** (with signatures).
2. For one entity, explain in two sentences why you included each field — what does the system need it for?
3. Name one real-world attribute of that entity that you deliberately left out, and explain why.

Use the rubric to guide your answer.

# Integration

**Philosophy connection — abstraction**

Plato argued that physical objects are imperfect copies of perfect ideal Forms. A chair in your room is an imperfect copy of the ideal Form of "Chair." Object modelling reverses this: you start with a messy, real-world thing (a specific chair) and deliberately extract the ideal, simplified form your program needs. This is the philosophical concept of **abstraction** — stripping away the accidental details to reveal the essential structure. Every time you decide a field is "irrelevant," you are performing philosophical abstraction.

**Mathematics connection — projections**

In linear algebra, a projection maps a high-dimensional object onto a lower-dimensional space. A 3D sphere projected onto a 2D plane becomes a circle. Information is lost (depth), but the projection is still useful. Object modelling is a projection from the infinite-dimensional reality of a real-world entity down to the small number of dimensions your program needs. The choice of projection depends on your purpose — projecting a sphere from above versus from the side gives different results, just as modelling a "Person" for a payroll system versus a health app gives different fields.

**Question:** A `Person` object needs to be modelled for three different systems: a payroll app, a fitness tracker, and a library membership system. Name one unique field each system would include that the other two would not, and justify your choices using the concept of relevance.

# Lore Conclusion

The cartographers' folly lives on as a cautionary tale in the Academy's great hall. Their map of everything mapped nothing useful. Your task as a construct architect is not to capture all of reality — it is to capture exactly enough. In the next section you will move from the conceptual to the concrete: you will learn to express your models as Java classes, giving your mental blueprints the power to become real, running constructs. The shadow becomes a spell.
