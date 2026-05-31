---
id: se-app-m5-08
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m5
moduleTitle: "Module 5: Object Thinking Foundations"
moduleGlyph: "🔷"
moduleSortOrder: 5
topicSlug: classes
topicTitle: "Classes"
topicSortOrder: 2
lesson: methods_in_classes
title: "Methods in Classes"
sortOrder: 8
difficulty: 2
estimatedMinutes: 23
xpReward: 60
practiceType: JAVA
questType: PRACTICE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m5-07]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Writes an instance method without the static keyword"
    - "Uses 'this' correctly to reference instance fields inside the method"
    - "Calls the method on an object using dot notation"
    - "Method accesses or modifies at least one instance field"
    - "Code is syntactically correct and would compile"
  keywords: [instance method, this, dot notation, no static, field, call, object, behaviour]
  modelAnswer: |
    class BankAccount {
        String owner;
        double balance;

        BankAccount(String owner, double initialBalance) {
            this.owner = owner;
            this.balance = initialBalance;
        }

        void deposit(double amount) {
            this.balance += amount;
        }

        void printBalance() {
            System.out.println(owner + "'s balance: " + balance);
        }
    }

    BankAccount acc = new BankAccount("Alice", 100.0);
    acc.deposit(50.0);
    acc.printBalance(); // Alice's balance: 150.0

guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Add an instance method called 'describe()' to this Car class. It should print: '[colour] car, year [year]'."
    inputConfig:
      language: java
      starterCode: "class Car {\n    String colour;\n    int year;\n\n    Car(String colour, int year) {\n        this.colour = colour;\n        this.year = year;\n    }\n\n    // Add describe() method here\n}\n"
      expectedPattern: "void\\s+describe\\s*\\(\\s*\\)"
    markingRule: REGEX_MATCH
    hint: "An instance method looks like: void methodName() { ... } — no 'static', no return type if void."
    reflectionPrompt: "How does the describe() method access colour and year without them being passed as parameters?"

  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Create a Car object and call describe() on it."
    inputConfig:
      language: java
      starterCode: "class Car {\n    String colour;\n    int year;\n    Car(String colour, int year) {\n        this.colour = colour;\n        this.year = year;\n    }\n    void describe() {\n        System.out.println(colour + \" car, year \" + year);\n    }\n}\n// Create a Car and call describe()\n"
      expectedPattern: "\\.describe\\s*\\(\\s*\\)"
    markingRule: REGEX_MATCH
    hint: "Use: Car myCar = new Car(\"Red\", 2021); then myCar.describe();"
    reflectionPrompt: "What does the dot before describe() tell Java?"

  - id: step-3
    sortOrder: 3
    inputType: CODE
    instruction: "Add a method 'birthday()' to Dog that increments the age field by 1 and prints '[name] is now [age] years old.'"
    inputConfig:
      language: java
      starterCode: "class Dog {\n    String name;\n    int age;\n\n    Dog(String name, int age) {\n        this.name = name;\n        this.age = age;\n    }\n\n    // Add birthday() method here\n}\n\nDog rex = new Dog(\"Rex\", 3);\nrex.birthday();\n"
      expectedPattern: "void\\s+birthday|age\\s*\\+\\s*=\\s*1|age\\+\\+"
    markingRule: REGEX_MATCH
    hint: "Inside birthday(), use: age++ or age = age + 1 to increment the field."
    reflectionPrompt: "Why does incrementing 'age' inside birthday() change the field on the object, not just a local copy?"

microCheckpoint:
  - question: "What keyword must NOT appear in an instance method signature?"
    options:
      - "void"
      - "public"
      - "static"
      - "return"
    correctIndex: 2
    feedback: "Correct — instance methods do not use 'static'. Static methods belong to the class itself, not to individual objects."

  - question: "How do you call an instance method 'bark()' on a Dog object called 'rex'?"
    options:
      - "bark(rex)"
      - "Dog.bark()"
      - "rex.bark()"
      - "call bark on rex"
    correctIndex: 2
    feedback: "Yes — use dot notation: objectName.methodName(). This tells Java to call bark() on the specific object rex."

retrieval:
  recall: "What is the difference between an instance method and a static method?"
  explain: "Explain how an instance method accesses the fields of the object it belongs to, without those fields being passed as parameters."
  mistakeId:
    code: |
      class Dog {
          String name;
          int age;

          static void bark() {
              System.out.println(name + " says: Woof!");
          }
      }
    answer: "bark() is declared static, so it belongs to the class, not to any particular object. A static method cannot access instance fields like 'name' because there is no specific object to access. Remove 'static' to make it an instance method."
---

# Hook

A blueprint that only describes what an object *is* produces a statue. A blueprint that also describes what an object *can do* produces a living construct. Instance methods are where the action lives — they are the behaviours you attach to every object created from your class. Unlike the static methods you may have seen before, instance methods belong to a *specific object*, and they can access that object's fields directly. This is what makes OOP powerful: data and behaviour travel together.

# Lore Introduction

A construct without Action Runes is a display piece — magnificent to look at, useless in the field. The Academy's Combat Division made this mistake during the Third Age, commissioning hundreds of constructs with perfect Essence Registers but blank Action Registers. The constructs stood in the great hall, gleaming, doing nothing. Instance methods are your Action Runes: bound to the construct's very essence, they carry the object's fields within them and act upon them without needing to be told what the construct is. They already know.

# Core Learning

## Concept Introduction

An **instance method** is a method declared inside a class without the `static` keyword. It belongs to each individual object and can access the object's fields directly.

```java
class Dog {
    String name;
    int age;

    // Instance method — no 'static'
    void bark() {
        System.out.println(name + " says: Woof!");
    }
}
```

To call an instance method, you use dot notation on an object:
```java
Dog rex = new Dog("Rex", 3);
rex.bark();  // Rex says: Woof!
```

The method knows about `name` and `age` because it is inside the `Dog` class — those fields are part of every `Dog` object.

## Why It Matters

Without instance methods, objects would only hold data — you would need separate, unconnected functions to do anything with them. Instance methods fuse the data and the behaviour together, making objects self-contained. When you call `rex.bark()`, you are not just calling a function — you are asking *Rex specifically* to bark. If Rex has a different name from Fido, their barks produce different output automatically.

## Worked Examples

```java
class BankAccount {
    String owner;
    double balance;

    BankAccount(String owner, double startingBalance) {
        this.owner   = owner;
        this.balance = startingBalance;
    }

    void deposit(double amount) {
        balance += amount;  // modifies the field
        System.out.println(owner + " deposited " + amount);
    }

    void printBalance() {
        System.out.println(owner + "'s balance: £" + balance);
    }
}

BankAccount alice = new BankAccount("Alice", 100.0);
BankAccount bob   = new BankAccount("Bob",   200.0);

alice.deposit(50.0);     // Alice deposited 50.0
alice.printBalance();    // Alice's balance: £150.0
bob.printBalance();      // Bob's balance: £200.0
```

Notice: `alice.deposit()` modifies `alice`'s balance — it does not touch `bob`'s balance. The method operates on the specific object it was called on.

## Common Mistakes

- **Adding `static`**: `static void bark()` — this turns the method into a class-level method that cannot access instance fields.
- **Forgetting the object when calling**: `bark();` alone will not work outside the class. You need `rex.bark();`.
- **Declaring fields inside the method**: `String name = "Rex";` inside a method is a local variable, not the field.

## Mental Model

Think of instance methods as **instructions attached to a specific employee record**. A `promote()` instruction knows which employee's salary to raise because it is *attached* to that employee's record. You do not need to pass the employee's details in as arguments — the method already has them. Calling `alice.promote()` raises Alice's salary; calling `bob.promote()` raises Bob's salary. Same instructions, different data, independent results.

## Mini Summary

- ✔ Instance methods are declared without `static` and belong to individual objects.
- ✔ They can directly access the object's fields without those fields being passed as parameters.
- ✔ Call them using dot notation: `objectName.methodName()`.
- ✔ Modifying a field inside an instance method changes that field on the object permanently.
- ✔ Different objects calling the same method produce results based on their own fields.

# Guided Practice Quest

Work through the sidebar steps to add a `describe()` method to a `Car` class, call it on an object, and then add a `birthday()` method to a `Dog` class that modifies a field.

# Solo Practice Quest

**Spell: Animate the Construct**

Write a `BankAccount` class that:
- Has fields: `owner` (String), `balance` (double)
- Has a constructor that sets both fields
- Has a `deposit(double amount)` method that adds to balance
- Has a `withdraw(double amount)` method that subtracts from balance (assume balance never goes below 0 for now)
- Has a `printBalance()` method that prints `"[owner]: £[balance]"`

Create two accounts, make some deposits and withdrawals, and print both balances. Show the full code.

# Integration

**Mathematics connection — functions as transformations**

A mathematical function transforms an input into an output: `f(x) = x + 5`. An instance method is a transformation of an object's state. `deposit(50.0)` transforms `balance` from its current value to `balance + 50.0`. Unlike a pure mathematical function, an instance method can have *side effects* — it changes the object's state as it runs. This is intentional: an object's methods represent the legal ways its state is allowed to change.

**Philosophy connection — identity over time**

Philosophers debate what makes an object the same object over time despite changes. If you replace every plank of a ship, is it still the same ship? In OOP, an object maintains its *identity* (its memory address) even as its state changes. When you call `rex.birthday()` and Rex's age increments, Rex is still Rex — the same object. His state changed, but his identity did not. This models the philosophical view that identity persists through change of properties.

**Question:** A `Counter` object has a `count` field and an `increment()` method. After calling `increment()` five times, the counter's state has changed five times. In what sense is it still the "same" Counter object throughout, and how does this relate to the philosophical concept of identity over time?

# Lore Conclusion

Your constructs now live. They do not merely hold essence — they act upon it. The Action Runes are bound to the Essence Register, and each construct expresses its nature through methods that know exactly whose fields they serve. You have assembled all the pieces of a working class: fields to hold state, a constructor to initialise it, and methods to operate on it. The next topic takes this further still — teaching you the warding spells that protect a construct's essence from external corruption. Welcome to Encapsulation.
