---
id: se-app-m5-04
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m5
moduleTitle: "Module 5: Object Thinking Foundations"
moduleGlyph: "🔷"
moduleSortOrder: 5
topicSlug: classes
topicTitle: "Classes"
topicSortOrder: 2
lesson: what_is_a_class
title: "What is a Class?"
sortOrder: 4
difficulty: 1
estimatedMinutes: 18
xpReward: 40
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m5-01, se-app-m5-02]
integrationDomains: [philosophy, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly defines a class as a blueprint or template"
    - "Correctly defines an object as an instance created from a class"
    - "Explains that multiple objects can be created from one class"
    - "Uses an appropriate analogy to illustrate class vs object"
    - "Identifies that each object has its own independent copy of the state"
  keywords: [class, object, instance, blueprint, template, new, independent, state]
  modelAnswer: |
    A class is a blueprint that defines what fields and methods objects of that
    type will have. An object is a specific instance created from that blueprint.
    Just as a cookie cutter (class) can produce many cookies (objects), each
    cookie is independent — cutting out Rex does not affect Fido. Each object
    holds its own copy of the state defined by the class.

guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A class is best described as:"
    inputConfig:
      options:
        - "A specific dog named Rex"
        - "A running program"
        - "A blueprint that defines what objects of a type will look like"
        - "A list of numbers"
      correctIndex: 2
    markingRule: EXACT_MATCH
    hint: "Think of the cookie cutter analogy. The cutter is not a cookie — it is the template."
    reflectionPrompt: "Why is it useful to have one blueprint rather than defining every object individually?"

  - id: step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "You have a Dog class with a field 'name'. You create two Dog objects: rex and fido. You set rex.name = 'Rex'. What is fido.name?"
    inputConfig:
      options:
        - "Also 'Rex' — they share the same field"
        - "null — each object has its own independent copy of the field"
        - "An error — you cannot have two Dog objects"
        - "'Fido' — Java sets it automatically"
      correctIndex: 1
    markingRule: EXACT_MATCH
    hint: "Each object created from a class is independent. Changing one does not affect another."
    reflectionPrompt: "What would go wrong in a program if all objects of the same class shared the same fields?"

  - id: step-3
    sortOrder: 3
    inputType: SHORT_ANSWER
    instruction: "Write your own analogy for class vs object. It must be different from cookie cutter/cookie and different from dog. Format: Class = ___ | Object = ___"
    inputConfig:
      placeholder: "Class = ___ | Object = ___"
    markingRule: KEYWORD_MATCH
    hint: "Think of anything that acts as a template: an architectural blueprint, a recipe, a mould, a stamp..."
    reflectionPrompt: "Does your analogy also capture the fact that each instance is independent?"

microCheckpoint:
  - question: "What is the relationship between a class and an object?"
    options:
      - "A class is an object with extra methods"
      - "A class is the blueprint; an object is a specific instance made from that blueprint"
      - "An object is the blueprint; a class is a specific instance"
      - "They are exactly the same thing"
    correctIndex: 1
    feedback: "Correct — the class is the template/blueprint; creating an object means making a specific instance of that template."

  - question: "How many objects can be created from a single class?"
    options:
      - "Exactly one"
      - "Exactly two"
      - "As many as needed — the class is reused for each"
      - "Zero — classes cannot produce objects"
    correctIndex: 2
    feedback: "Yes — a single class definition can be used to create as many independent objects as needed."

retrieval:
  recall: "What is the difference between a class and an object?"
  explain: "Using the cookie cutter analogy, explain why changing the name field on one Dog object does not affect another Dog object created from the same class."
  mistakeId:
    code: |
      // Student writes: "The class Dog is the actual dog I'm working with."
      Dog rex = new Dog();
      rex.name = "Rex";
    answer: "The class Dog is the blueprint, not the actual dog. 'rex' is the actual object — the specific instance. The class simply defines what every Dog will look like; 'rex' is one particular Dog created from that blueprint."
---

# Hook

A bakery owns exactly one star-shaped cookie cutter. Yet every day the bakery produces hundreds of star-shaped cookies. The cutter is not edible — it is a tool, a template. Each cookie it produces is real, distinct, and independent. You can frost one cookie without affecting any other. This is the most important analogy in all of OOP. The cookie cutter is a **class**. Each cookie is an **object**. One template, unlimited instances.

# Lore Introduction

Before the Academy could teach apprentices to summon constructs, it had to solve a harder problem: how do you teach someone to summon an *infinite variety* of constructs using only a finite set of knowledge? The answer came from the master artificer Velindra, who invented the Arcane Blueprint — a single scroll that encodes the structure of an entire category of construct. Every time a wizard reads the Blueprint and speaks the summoning word, a new, independent construct appears. The Blueprint is never consumed; the construct is always fresh. Velindra's insight became the foundation of all modern spellcraft.

# Core Learning

## Concept Introduction

A **class** is a blueprint that defines:
- What **fields** (state) objects of that type will have.
- What **methods** (behaviour) objects of that type will have.

A class is not itself an object — it is the *definition* of what objects will look like.

An **object** (also called an **instance**) is a specific thing created from a class. Creating an object is called **instantiation**.

| Term | Meaning | Analogy |
|------|---------|---------|
| Class | Blueprint / template | Cookie cutter |
| Object | Specific instance | An individual cookie |
| Instantiation | Creating an object from a class | Cutting a cookie |

## Why It Matters

Without classes, you would have to define every object completely from scratch. With classes, you define the pattern once and create as many instances as you need. A `Dog` class defined once can produce Rex, Fido, Spot, and a thousand other dogs — all with their own independent names, ages, and breeds.

## Worked Examples

```java
// This is the CLASS — the blueprint
class Dog {
    String name;
    int age;

    void bark() {
        System.out.println(name + " says: Woof!");
    }
}

// These are OBJECTS — specific instances
Dog rex  = new Dog();
Dog fido = new Dog();

rex.name  = "Rex";
rex.age   = 3;

fido.name = "Fido";
fido.age  = 5;

rex.bark();   // Rex says: Woof!
fido.bark();  // Fido says: Woof!
```

`rex` and `fido` are two independent objects. Changing `rex.name` has absolutely no effect on `fido.name`. They share the same *class* (blueprint), but each object has its own private copy of the fields.

## Common Mistakes

- **Confusing the class for the object**: `Dog` is not a dog — `rex` is a dog. The class is the definition; the object is the thing.
- **Thinking there can only be one instance**: You can create hundreds of `Dog` objects from one `Dog` class. The class is never "used up."
- **Forgetting `new`**: To create an object you must use the `new` keyword. `Dog rex;` declares a variable but does not create an object yet.

## Mental Model

Think of a class as an **architectural blueprint** for a house. The blueprint describes: number of rooms, layout, window positions. But the blueprint is not a house — you cannot live in it. When a builder constructs a house from the blueprint, *that* is the house (the object). Build ten houses from the same blueprint and you get ten independent houses — painting one does not affect the others.

## Mini Summary

- ✔ A **class** is the blueprint; an **object** is a specific instance made from the blueprint.
- ✔ One class can produce as many independent objects as needed.
- ✔ Each object holds its own independent copy of the fields defined by the class.
- ✔ The `new` keyword is used to create (instantiate) an object.
- ✔ Objects of the same class share the same methods but have their own state.

# Guided Practice Quest

Work through the sidebar steps to distinguish between classes and objects, observe that instances are independent, and create your own analogy for the class-object relationship.

# Solo Practice Quest

**Spell: Name the Blueprint**

Answer the following in your own words:

1. You have a class called `Car`. You create two objects: `myCar` and `yourCar`. You set `myCar.colour = "Red"`. What is `yourCar.colour`? Explain why.
2. A classmate says: "If I change the `Dog` class, my `rex` object will automatically update." Are they correct? Explain.
3. Choose a system (game, shop, library) and name **three classes** you would define for it. For each, write one sentence explaining what kind of objects it would produce.

# Integration

**Philosophy connection — universals and particulars**

Philosophers distinguish between *universals* (abstract categories, like "Dogness") and *particulars* (specific instances, like the dog in front of you). For centuries, debates raged about which was more real. OOP sidesteps the debate by making both explicit: the class represents the universal (all the properties that make something a Dog), and the object represents the particular (this specific dog, right now, with this name and age). The `new` keyword is the moment when a universal becomes a particular.

**Mathematics connection — sets and elements**

In set theory, a set is a collection defined by a rule (e.g. "all even numbers"). Individual elements belong to the set but are not the set itself. A class is like the defining rule of a set; each object is an element that belongs to that set. The class defines the membership criteria (must have a name field, must have a bark method); each `Dog` object is a member that satisfies those criteria.

**Question:** Aristotle believed that the Form (universal) only exists through its particulars. In contrast, Plato believed Forms existed independently. Which view better matches how Java classes and objects work, and why?

# Lore Conclusion

Velindra's Arcane Blueprint changed spellcraft forever — not because it made individual constructs more powerful, but because it made *creating* constructs infinitely efficient. You now understand the deepest foundation of the Academy's art: define the pattern once, then breathe life into as many instances as the spell requires. In the next lesson you will write your first real class in Java, giving physical form to the blueprints you have been imagining.
