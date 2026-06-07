---
id: se-app-m5-06
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
lesson: constructors
title: "Constructors"
sortOrder: 6
difficulty: 2
estimatedMinutes: 22
xpReward: 60
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m5-05]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Writes a constructor with the same name as the class and no return type"
    - "Constructor accepts parameters that match the fields to be initialised"
    - "Uses 'this.field = parameter' to assign values inside the constructor"
    - "Creates an instance by passing arguments to the constructor via new"
    - "Code is syntactically correct and would compile"
  keywords: [constructor, this, parameters, initialise, same name, no return type, new]
  modelAnswer: |
    class Car {
        String colour;
        int year;

        Car(String colour, int year) {
            this.colour = colour;
            this.year = year;
        }
    }

    Car myCar = new Car("Red", 2023);
    System.out.println(myCar.colour); // Red

guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Add a constructor to this Book class that accepts title (String) and pages (int) as parameters and sets the fields."
    inputConfig:
      language: java
      starterCode: "class Book {\n    String title;\n    int pages;\n\n    // Add constructor here\n}\n"
      expectedPattern: "Book\\s*\\(\\s*String\\s+\\w+\\s*,\\s*int\\s+\\w+"
    markingRule: REGEX_MATCH
    hint: "A constructor has the same name as the class, no return type, and assigns this.field = parameter."
    reflectionPrompt: "Why does the constructor have no return type — not even void?"

  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Create a Book object called myBook by calling the constructor with title 'Clean Code' and pages 431."
    inputConfig:
      language: java
      starterCode: "class Book {\n    String title;\n    int pages;\n    Book(String title, int pages) {\n        this.title = title;\n        this.pages = pages;\n    }\n}\n\n// Create your Book here\n"
      expectedPattern: "new\\s+Book\\s*\\(\\s*\"[^\"]+\"\\s*,\\s*\\d+"
    markingRule: REGEX_MATCH
    hint: "Use: Book myBook = new Book(\"Clean Code\", 431);"
    reflectionPrompt: "What happens to the parameter values once the constructor finishes running?"

  - id: step-3
    sortOrder: 3
    inputType: CODE
    instruction: "Print the title and pages of myBook using dot notation to verify the constructor set them correctly."
    inputConfig:
      language: java
      starterCode: "class Book {\n    String title;\n    int pages;\n    Book(String title, int pages) {\n        this.title = title;\n        this.pages = pages;\n    }\n}\nBook myBook = new Book(\"Clean Code\", 431);\n// Print title and pages here\n"
      expectedPattern: "myBook\\.title|myBook\\.pages"
    markingRule: REGEX_MATCH
    hint: "Use System.out.println(myBook.title) and System.out.println(myBook.pages)."
    reflectionPrompt: "If you had not used 'this.title = title' in the constructor, what would myBook.title be?"

microCheckpoint:
  - question: "What are the two special rules about a constructor's signature?"
    options:
      - "It must return void and be named 'constructor'"
      - "It must have the same name as the class and have no return type"
      - "It must be static and named 'init'"
      - "It must return the class type and be named 'create'"
    correctIndex: 1
    feedback: "Correct — a constructor has exactly the same name as the class and declares no return type (not even void)."

  - question: "Why do we write 'this.name = name' inside a constructor when both parameter and field have the same name?"
    options:
      - "'this.name' refers to the field; 'name' alone refers to the parameter"
      - "They are interchangeable — it does not matter"
      - "'this' means 'the class', so 'this.name' is the class name"
      - "Java requires 'this' on every assignment"
    correctIndex: 0
    feedback: "Yes — 'this.name' explicitly refers to the object's field, while 'name' alone refers to the constructor parameter. Without 'this', you would assign the parameter to itself."

retrieval:
  recall: "What two rules define a constructor's signature in Java?"
  explain: "Explain what 'this' refers to inside a constructor and why it is needed when the parameter has the same name as the field."
  mistakeId:
    code: |
      class Person {
          String name;
          int age;

          void Person(String name, int age) {
              name = name;
              age = age;
          }
      }
    answer: "Two mistakes: (1) Constructors must NOT have a return type — remove 'void'. (2) 'name = name' assigns the parameter to itself, not the field. Use 'this.name = name' and 'this.age = age'."
---

# Hook

Imagine summoning a construct and having to manually poke values into every single field immediately after creation — and if you forget even one, the construct starts its existence in an undefined, broken state. Constructors solve this problem. A constructor is the initialisation spell built directly into the blueprint: when a new object is created, the constructor runs automatically, setting up the object's state from the very first moment. No more partially built constructs.

# Lore Introduction

In the early days of the Academy's construct labs, every newly summoned entity had to be initialised by hand — a tedious and error-prone ritual that often left constructs with missing essence fragments. Grandmaster Rowen of the Second Age devised the Binding Constructor: a special invocation embedded within the Blueprint itself. Now, the moment any construct is summoned, the Binding Constructor fires automatically, weaving the initial essence directly from the summoning parameters. A construct born of a proper Binding Constructor arrives whole, ready, and true.

# Core Learning

## Concept Introduction

A **constructor** is a special method that runs automatically when you create an object with `new`. Its purpose is to **initialise the object's fields** so the object starts in a valid, useful state.

**Constructor rules:**
1. It has the **same name as the class**.
2. It has **no return type** — not even `void`.
3. It can accept **parameters** to receive initial values.

```java
class ClassName {
    // fields...

    ClassName(parameters) {
        // initialise fields here
    }
}
```

## Why It Matters

Without a constructor, your objects start with default values (`null` for Strings, `0` for ints, `false` for booleans). This is often not what you want. A constructor lets you guarantee that every object starts in a meaningful state — like requiring a `Person` to always have a name, or a `BankAccount` to always start with a specified balance.

## Worked Examples

```java
class Dog {
    String name;
    int age;

    // Constructor
    Dog(String name, int age) {
        this.name = name;  // this.name = field; name = parameter
        this.age  = age;
    }

    void bark() {
        System.out.println(name + " says: Woof!");
    }
}

// Creating objects via constructor
Dog rex  = new Dog("Rex",  3);
Dog fido = new Dog("Fido", 5);

rex.bark();   // Rex says: Woof!
fido.bark();  // Fido says: Woof!

System.out.println(rex.name);  // Rex
System.out.println(fido.age);  // 5
```

**The `this` keyword** refers to the current object — the one being constructed right now. When the parameter and the field have the same name, `this.name` unambiguously means "the field on this object," while `name` alone means "the parameter."

## Common Mistakes

- **Adding `void` to the constructor**: `void Dog(...)` — this creates a regular method named `Dog`, not a constructor. Remove the `void`.
- **Assigning parameter to itself**: `name = name;` — both sides refer to the parameter. Use `this.name = name;`.
- **Wrong name**: A constructor named `init()` or `create()` is just a regular method — Java will never call it automatically.

## Mental Model

Think of a constructor as a **form you fill in when opening a bank account**. The bank (Java) demands you provide certain information (name, initial deposit) *before* the account exists. Once you submit the form (call `new BankAccount("Alice", 100.0)`), the account is created and immediately set up with those values. You cannot open the account without filling in the form — the constructor enforces this.

## Mini Summary

- ✔ A constructor has the **same name as its class** and **no return type**.
- ✔ It runs automatically when you use `new ClassName(arguments)`.
- ✔ Use `this.field = parameter` to assign constructor parameters to fields.
- ✔ `this` refers to the current object being constructed.
- ✔ Constructors guarantee objects start in a valid, initialised state.

# Guided Practice Quest

Work through the sidebar steps to add a constructor to a `Book` class, create an instance by calling the constructor, and verify the fields were set correctly.

# Solo Practice Quest

**Spell: Bind the Construct**

Write a class called `Wizard` with:
- Fields: `name` (String), `level` (int), `school` (String)
- A constructor that accepts all three values and initialises them using `this`

Then create two `Wizard` objects with different values and print a sentence about each one, like:
```
Merlin is a level 10 wizard of the Arcane school.
Gandalf is a level 15 wizard of the Grey school.
```

Include the full class definition and object creation code.

# Integration

**Mathematics connection — functions with required inputs**

A mathematical function `f(x, y) = x + y` requires two inputs to produce a result. You cannot call it without providing both — `f()` is undefined. A constructor with parameters works the same way: `new Dog()` without the required name and age will fail to compile if you have defined a constructor that expects them. Constructors formalise the *preconditions* for object creation, just as function definitions formalise the required inputs for computation.

**Psychology connection — cognitive load and defaults**

Research on decision fatigue shows that humans make worse choices the more decisions they face. A default-free constructor forces the caller to make every initialisation decision up front — which is actually good in programming. It means you cannot forget to set a critical field because you never had the option to skip it. The constructor transfers the responsibility of initialisation from "do it manually later (and maybe forget)" to "do it now, as part of creation."

**Question:** A `BankAccount` class has a constructor that requires `ownerName` (String) and `initialBalance` (double). Why is it better to have these as required constructor parameters rather than optional fields you set afterwards? Use the idea of object validity to support your answer.

# Lore Conclusion

The Binding Constructor is now part of your craft. Every construct you build from this point forward will arrive whole — not hollow. This seemingly small addition to your Blueprint changes everything: no more uninitialized fields lurking like ticking rune-bombs. In the next lesson you will go deeper into fields themselves — exploring the difference between instance fields and local variables, and learning how Java handles fields that have never been set.
