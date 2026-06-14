---
id: se-app-m5-05
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
lesson: creating_classes
title: "Creating Classes"
sortOrder: 5
difficulty: 2
estimatedMinutes: 22
xpReward: 60
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m5-04]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Declares a class using the correct syntax with the class keyword and PascalCase name"
    - "Declares at least two fields inside the class with correct types"
    - "Creates an instance using the new keyword"
    - "Accesses fields using dot notation on the instance"
    - "Code is syntactically correct and would compile"
  keywords: [class, new, fields, instance, dot notation, PascalCase, declare, access]
  modelAnswer: |
    class Car {
        String colour;
        int year;
        String model;
    }

    Car myCar = new Car();
    myCar.colour = "Blue";
    myCar.year = 2022;
    myCar.model = "Sedan";
    System.out.println(myCar.colour + " " + myCar.model);

guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Declare a class called Book with two fields: title (String) and pages (int). Do not add any methods yet."
    inputConfig:
      language: java
      starterCode: "// Declare your Book class here\n"
      expectedPattern: "class\\s+Book\\s*\\{"
    markingRule: REGEX_MATCH
    hint: "Use: class ClassName { fields go here }"
    reflectionPrompt: "What does the curly brace pair {} define in a class declaration?"

  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Now create an instance of Book called myBook using the new keyword, then set its title to 'Java Magic' and its pages to 350."
    inputConfig:
      language: java
      starterCode: "class Book {\n    String title;\n    int pages;\n}\n\n// Create your instance here\n"
      expectedPattern: "new\\s+Book\\s*\\(\\)"
    markingRule: REGEX_MATCH
    hint: "Use: ClassName variableName = new ClassName();"
    reflectionPrompt: "Why must you use 'new' — what does it actually do?"

  - id: step-3
    sortOrder: 3
    inputType: CODE
    instruction: "Print both fields of myBook using System.out.println and dot notation."
    inputConfig:
      language: java
      starterCode: "class Book {\n    String title;\n    int pages;\n}\n\nBook myBook = new Book();\nmyBook.title = \"Java Magic\";\nmyBook.pages = 350;\n\n// Print both fields here\n"
      expectedPattern: "myBook\\.title|myBook\\.pages"
    markingRule: REGEX_MATCH
    hint: "Use objectName.fieldName to access a field on an object."
    reflectionPrompt: "What does the dot (.) mean when you write myBook.title?"

microCheckpoint:
  - question: "Which keyword is used to declare a class in Java?"
    options:
      - "object"
      - "new"
      - "class"
      - "type"
    correctIndex: 2
    feedback: "Correct — the 'class' keyword starts a class declaration."

  - question: "How do you access the 'name' field of an object called 'cat'?"
    options:
      - "cat->name"
      - "name.cat"
      - "cat::name"
      - "cat.name"
    correctIndex: 3
    feedback: "Yes — dot notation (objectName.fieldName) is used to access fields and methods on an object."

retrieval:
  recall: "What keyword declares a class and what keyword creates an instance of it?"
  explain: "Explain what each part of this line means: Dog rex = new Dog();"
  mistakeId:
    code: |
      class dog {
          String Name;
          int Age;
      }
      dog rex = Dog();
    answer: "Three mistakes: (1) class name should be PascalCase: 'Dog' not 'dog'. (2) fields should be camelCase: 'name' and 'age' not 'Name' and 'Age'. (3) instantiation must use 'new': 'new Dog()' not 'Dog()'."
---

# Hook

You have seen the blueprint in theory. Now you write one. In Java, declaring a class takes just a handful of characters — the `class` keyword, a name, and a pair of curly braces — yet inside those braces lives the entire definition of a new type of object. When you add the `new` keyword, you breathe life into the blueprint and produce a real, working construct. This lesson is where thinking becomes code.

# Lore Introduction

The Academy's Inscription Hall is where apprentices carve their first Arcane Blueprints into living stone. Before this moment, a blueprint existed only as a thought-form — powerful in imagination, useless in practice. The act of inscription — keyword by keyword, field by field — transforms the mental model into an artefact that the Academy's construct engines can read and execute. Every class you write in Java is an act of inscription. Every `new` keyword is the summoning that brings your blueprint to life.

# Core Learning

## Concept Introduction

In Java, a class is declared using the `class` keyword:

```java
class ClassName {
    // fields (state)
    // methods (behaviour)
}
```

**Naming rules:**
- Class names use **PascalCase**: first letter of each word capitalised. `Dog`, `BankAccount`, `TrafficLight`.
- Field names use **camelCase**: first word lowercase, subsequent words capitalised. `name`, `accountBalance`, `currentColour`.

**Creating an instance** uses the `new` keyword:
```java
ClassName variableName = new ClassName();
```

**Accessing fields** uses **dot notation**:
```java
variableName.fieldName
```

## Why It Matters

The `class` declaration is the most fundamental building block in Java. Almost everything in a real Java program lives inside a class. Learning the exact syntax now means you will never be confused by the scaffolding around your code — you will always know what each keyword means and why it is there.

## Worked Examples

```java
// Class declaration
class Dog {
    String name;    // field 1
    int age;        // field 2
    String breed;   // field 3
}

// Creating objects (instances)
Dog rex  = new Dog();
Dog fido = new Dog();

// Setting field values via dot notation
rex.name  = "Rex";
rex.age   = 3;
rex.breed = "Labrador";

fido.name  = "Fido";
fido.age   = 5;
fido.breed = "Poodle";

// Reading field values
System.out.println(rex.name + " is a " + rex.breed);  // Rex is a Labrador
System.out.println(fido.name + " is " + fido.age);    // Fido is 5
```

**Breakdown of `Dog rex = new Dog();`:**

| Part | Meaning |
|------|---------|
| `Dog` | The type (the class being used) |
| `rex` | The variable name for this object |
| `=` | Assignment |
| `new` | Keyword that allocates memory and creates the object |
| `Dog()` | Calls the constructor (more on this next lesson) |
| `;` | Statement terminator |

## Common Mistakes

- **Lowercase class name**: `class dog` — Java allows it but breaks convention. Always use `PascalCase`.
- **Forgetting `new`**: `Dog rex = Dog();` — this is not valid. The `new` keyword must be present.
- **Forgetting the type on the left**: `rex = new Dog();` — Java needs to know what type `rex` is when you first declare it.
- **Accessing fields before creating the object**: Declaring `Dog rex;` without `= new Dog()` means `rex` is `null` — accessing `rex.name` will crash.

## Mental Model

Think of a class declaration as **registering a new word in a dictionary**. Once you define `Dog`, Java's compiler knows what `Dog` means whenever it sees it. The `new Dog()` call is like looking up the word and creating a real example of it in the world. The dot notation is like following a pointer from the example back to the specific detail you want.

## Mini Summary

- ✔ Use the `class` keyword to declare a class; name it in PascalCase.
- ✔ Fields go inside the curly braces with their type and camelCase name.
- ✔ Use `new ClassName()` to create an instance (object).
- ✔ Use dot notation (`object.field`) to read or write a field.
- ✔ Each object has its own independent copy of every field.

# Guided Practice Quest

Follow the sidebar steps to write a `Book` class, create an instance, set its fields, and print them. This is your first complete Java class from scratch.

# Solo Practice Quest

**Spell: Inscribe the Blueprint**

Write a complete Java snippet (no need for a `main` method wrapper) that:
1. Declares a class called `Planet` with three fields: `name` (String), `distanceFromSunAU` (double), and `hasLife` (boolean).
2. Creates two `Planet` instances.
3. Sets meaningful values for all three fields on each instance.
4. Prints a sentence about each planet using its fields.

Your output should look something like:
```
Earth is 1.0 AU from the Sun and has life: true
Mars is 1.52 AU from the Sun and has life: false
```

# Integration

**Mathematics connection — type systems**

In mathematics, a *type* constrains the values a variable can hold: a natural number cannot be negative; a fraction is not an integer. Java's type system does the same thing: declaring `int age` means `age` can only hold whole numbers. When you write a field declaration like `double distanceFromSunAU`, you are not just naming a slot in memory — you are asserting a mathematical constraint about what kind of data belongs there. A class is, in this sense, a compound type: a structured collection of typed variables.

**Philosophy connection — nominalism**

Philosophers debate whether abstract categories (like "Dog") exist independently or only exist because we name them. In Java, a class only exists because you declare it — before you write `class Dog`, Java has no concept of dogs at all. This is nominalism in action: the category exists because you named it. Once named (declared), the class becomes a real, usable entity that the compiler recognises. Your act of writing a class declaration is a creative act that brings a new concept into the program's universe.

**Question:** When you write `class Planet { ... }` in Java, you are creating a new type. How does this relate to the mathematical notion of a type as a constraint, and what constraints are you implicitly imposing on any `Planet` object created from this class?

# Lore Conclusion

Your first Blueprint has been inscribed. The curly braces are the wards of the Inscription, protecting the fields within from the chaos outside. The `new` keyword is the breath of life. And dot notation is the pointing finger of intent — reaching into the construct and extracting exactly the essence you need. You have gone from imagining objects to commanding them. In the next lesson you will learn to give your Blueprints a proper initialisation spell: the Constructor, which ensures every construct is born ready for action.
