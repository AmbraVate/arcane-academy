---
id: se-app-m5-02
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
lesson: state_and_behaviour
title: "State & Behaviour"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 40
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m5-01]
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly defines state as the data (fields) an object holds"
    - "Correctly defines behaviour as the actions (methods) an object can perform"
    - "Provides at least two examples of state and two of behaviour for a chosen object"
    - "Explains that state can change over time while the object remains the same object"
    - "Uses correct vocabulary: state, behaviour, field, method"
  keywords: [state, behaviour, field, method, data, action, change, object]
  modelAnswer: |
    A BankAccount object has state: balance (double) and accountNumber (String).
    Its behaviour includes deposit(amount), withdraw(amount), and getBalance().
    State is what the account *knows* about itself — the balance can change when
    money is deposited or withdrawn, but the object remains the same BankAccount.
    Behaviour is what the account can *do* — it acts on its own state.

guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A Dog object has fields: name, age, breed. What term describes these fields collectively?"
    inputConfig:
      options:
        - "Behaviour"
        - "Methods"
        - "State"
        - "Output"
      correctIndex: 2
    markingRule: EXACT_MATCH
    hint: "Fields hold the data an object *knows* about itself."
    reflectionPrompt: "How does the word 'state' capture the idea that these values can change over time?"

  - id: step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "A Dog object has methods: bark(), fetch(), sit(). What term describes these methods collectively?"
    inputConfig:
      options:
        - "State"
        - "Fields"
        - "Variables"
        - "Behaviour"
      correctIndex: 3
    markingRule: EXACT_MATCH
    hint: "Methods describe what an object can *do*."
    reflectionPrompt: "Why is it useful to name methods as verbs and fields as nouns?"

  - id: step-3
    sortOrder: 3
    inputType: SHORT_ANSWER
    instruction: "Choose any real-world object. List its state (at least 2 fields) and behaviour (at least 2 methods). Use the format: State: field1, field2 | Behaviour: method1(), method2()"
    inputConfig:
      placeholder: "State: ... | Behaviour: ..."
    markingRule: KEYWORD_MATCH
    hint: "Think about a vending machine, a traffic light, or a bank account."
    reflectionPrompt: "Did you notice that your methods tend to use or change your fields? That is intentional — they work together."

microCheckpoint:
  - question: "Which of the following is an example of STATE in a Car object?"
    options:
      - "accelerate()"
      - "int speed"
      - "honk()"
      - "refuel()"
    correctIndex: 1
    feedback: "Correct — 'int speed' is a field that holds data, so it is part of the Car's state."

  - question: "Which of the following is an example of BEHAVIOUR in a Car object?"
    options:
      - "String colour"
      - "int fuelLevel"
      - "String model"
      - "accelerate()"
    correctIndex: 3
    feedback: "Yes — 'accelerate()' is a method describing something the car can do, making it behaviour."

retrieval:
  recall: "What is the difference between state and behaviour in an object?"
  explain: "A traffic light object has fields: currentColour, timer. It has methods: changeLight(), getColour(). Explain which parts are state and which are behaviour, and describe how the behaviour interacts with the state."
  mistakeId:
    code: |
      // A student labels these as "behaviour":
      String name;
      int age;
    answer: "These are fields — they hold data about the object, so they are state, not behaviour. Behaviour refers to methods (functions) that the object can perform."
---

# Hook

Imagine a traffic light. At any given moment it *knows* something — which colour is currently showing. It can also *do* something — change to the next colour. Now freeze the light in time. The colour it is showing right now is its **state**. Press play and watch it change — that change is its **behaviour**. Every object in programming has exactly this split: what it currently is, and what it can do. Master this distinction and you will understand half of OOP.

# Lore Introduction

In the Academy's spellcraft workshops, every summoned construct has two registers: the Essence Register, which records what the construct currently knows about itself, and the Action Register, which records the spells it can cast. A construct with a full Essence Register but no Action Register is a statue — inert and useless. A construct with Actions but no Essence has nothing to act upon. Only when both registers are filled does a construct come alive. Apprentices learn to read both registers before they ever cast a summoning spell.

# Core Learning

## Concept Introduction

Every object in Java has two fundamental aspects:

**State** — the data an object holds about itself, stored in *fields* (also called instance variables). State describes *what an object currently is* at any given moment.

**Behaviour** — the actions an object can perform, implemented as *methods*. Behaviour describes *what an object can do*.

State can change over time. A `Dog` named Rex might start with `age = 1` and become `age = 5` later — it is still Rex, but his state has changed. Behaviour stays consistent — Rex can always `bark()`, but *how* the bark executes might depend on the current state.

## Why It Matters

Separating state from behaviour gives you a clean mental framework for designing any object:

- **State questions**: What does this object need to remember? What data defines it?
- **Behaviour questions**: What should this object be able to do? What operations make sense on its data?

When you can answer both questions cleanly, your class design is usually solid. When you cannot, it is a signal to rethink whether your object is trying to do too many things at once.

## Worked Examples

```java
class Dog {
    // STATE — fields
    String name;   // what is the dog's name?
    int age;       // how old is the dog?
    String breed;  // what breed is the dog?

    // BEHAVIOUR — methods
    void bark() {
        System.out.println(name + " says: Woof!");
    }

    void fetch() {
        System.out.println(name + " fetches the ball!");
    }

    void birthday() {
        age = age + 1;  // behaviour that CHANGES state
        System.out.println(name + " is now " + age + " years old.");
    }
}
```

Notice how `birthday()` modifies the `age` field. This shows a key relationship: **behaviour often acts on state**, reading it, changing it, or using it to decide what to do.

## Common Mistakes

- **Calling methods "state"**: If it is a verb/action, it is behaviour. If it holds a value, it is state.
- **Thinking state never changes**: State *is supposed* to change — that is the point. A `BankAccount` with a frozen `balance` field would be useless.
- **Forgetting that behaviour uses state**: Methods without any reference to fields are unusual. Most methods read or modify the object's own data.

## Mental Model

Think of a **whiteboard** (state) and a **person** (behaviour). The whiteboard holds the current information — numbers, diagrams, whatever has been written. The person can read the whiteboard, erase parts of it, write new information, or use what is on it to solve a problem. The whiteboard is state; everything the person does to it is behaviour.

## Mini Summary

- ✔ **State** = the data an object holds in its fields; describes what it *is* at a moment in time.
- ✔ **Behaviour** = the methods an object can execute; describes what it *can do*.
- ✔ Fields are typically nouns; methods are typically verbs.
- ✔ State can change — behaviour is what causes those changes.
- ✔ Methods often read or modify the object's own fields.

# Guided Practice Quest

Work through the sidebar steps to practise distinguishing state from behaviour. You will categorise fields and methods for a Dog object, then design the state and behaviour for your own chosen object.

# Solo Practice Quest

**Spell: Classify the Construct**

Design a `BankAccount` object on paper (no IDE needed). Write:
1. At least **three fields** that represent its state (include the Java type, e.g. `double balance`).
2. At least **three methods** that represent its behaviour (include the method signature, e.g. `void deposit(double amount)`).
3. One sentence explaining how at least one of your methods interacts with at least one of your fields.

Use the rubric to self-assess your answer before submitting.

# Integration

**Mathematics connection — functions and variables**

In mathematics, a function takes inputs and produces an output: `f(x) = x + 1`. The variable `x` holds a value (state in maths terms) and the function transforms it (behaviour). An object's fields are like the variables of a mathematical system, and its methods are like the functions that operate on those variables. The key difference: in a maths function, variables are usually separate from the function; in OOP, they are bundled together inside the object. This bundling is what makes the object self-contained.

**Psychology connection — schemas**

Psychologists describe mental models called *schemas* — organised frameworks the brain uses to understand familiar things. When you hear "dog", your brain automatically activates a schema that includes typical dog attributes (furry, four legs) and typical dog actions (bark, fetch). OOP classes encode exactly this kind of schema in software: the fields match the "what it is" part of the schema and the methods match the "what it does" part. Writing a class is, in a sense, teaching the program your schema for a concept.

**Question:** A `TrafficLight` object has fields `currentColour` and `timer`, and methods `changeLight()` and `getColour()`. Identify which are state and which are behaviour, then describe in mathematical terms how `changeLight()` acts as a function on the state variables.

# Lore Conclusion

You have filled both registers of your first mental construct. State and behaviour are not rivals — they are partners. Every field you write is a promise: "this object will remember this." Every method you write is a contract: "this object can do this." In the next lesson you will practise finding these two registers in real-world systems and mapping them precisely to Java constructs. The Academy's grandmasters say the truest test of an apprentice is not whether they can write a class, but whether they can *read* the world and see the objects hiding inside it.
