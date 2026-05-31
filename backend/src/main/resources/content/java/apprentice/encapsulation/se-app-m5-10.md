---
id: se-app-m5-10
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m5
moduleTitle: "Module 5: Object Thinking Foundations"
moduleGlyph: "🔷"
moduleSortOrder: 5
topicSlug: encapsulation
topicTitle: "Encapsulation"
topicSortOrder: 3
lesson: access_modifiers
title: "Access Modifiers"
sortOrder: 10
difficulty: 2
estimatedMinutes: 20
xpReward: 60
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m5-09]
integrationDomains: [philosophy, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly defines private as accessible only within the same class"
    - "Correctly defines public as accessible from anywhere"
    - "States that the standard practice is private fields and public methods"
    - "Demonstrates that private fields cannot be accessed directly from outside the class"
    - "Code compiles correctly with no visibility errors"
  keywords: [private, public, access modifier, visibility, outside, class, standard, convention]
  modelAnswer: |
    class Dog {
        private String name;  // private — only Dog methods can access this
        private int age;

        public Dog(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public void bark() {
            System.out.println(name + " says: Woof!"); // OK — inside Dog
        }
    }

    Dog rex = new Dog("Rex", 3);
    // rex.name = "Buddy"; // COMPILE ERROR — name is private
    rex.bark();            // OK — bark() is public

guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Mark both fields in this Dog class as private. The constructor and method should remain public."
    inputConfig:
      language: java
      starterCode: "class Dog {\n    String name;\n    int age;\n\n    public Dog(String name, int age) {\n        this.name = name;\n        this.age = age;\n    }\n\n    public void bark() {\n        System.out.println(name + \" says: Woof!\");\n    }\n}\n"
      expectedPattern: "private\\s+String\\s+name|private\\s+int\\s+age"
    markingRule: REGEX_MATCH
    hint: "Add the 'private' keyword before the type of each field: private String name;"
    reflectionPrompt: "Why should fields be private while the constructor and methods stay public?"

  - id: step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "You try to write: rex.name = 'Buddy'; after marking name as private. What happens?"
    inputConfig:
      options:
        - "It works fine — private only affects methods, not fields"
        - "It causes a runtime crash"
        - "The compiler refuses to compile — name is not accessible from outside the class"
        - "It sets name to null"
      correctIndex: 2
    markingRule: EXACT_MATCH
    hint: "The compiler enforces access modifiers at compile time, before the program ever runs."
    reflectionPrompt: "Why is a compile-time error better than a runtime crash for catching access violations?"

  - id: step-3
    sortOrder: 3
    inputType: SHORT_ANSWER
    instruction: "Complete the rule: Fields should be ___; constructor and methods that form the public interface should be ___."
    inputConfig:
      placeholder: "Fields should be ___ ; methods should be ___"
    markingRule: KEYWORD_MATCH
    hint: "This is the standard encapsulation practice in Java."
    reflectionPrompt: "Can you think of a situation where a method might intentionally be private?"

microCheckpoint:
  - question: "Which access modifier makes a field accessible only from within the same class?"
    options:
      - "public"
      - "open"
      - "hidden"
      - "private"
    correctIndex: 3
    feedback: "Correct — 'private' restricts access to the class itself. External classes cannot read or write private members."

  - question: "What is the standard practice for field visibility in Java?"
    options:
      - "All fields should be public so other classes can access them easily"
      - "Fields should be private; methods that form the interface should be public"
      - "All class members should have no access modifier"
      - "Fields should be public, methods should be private"
    correctIndex: 1
    feedback: "Yes — private fields and public methods is the idiomatic Java approach to encapsulation."

retrieval:
  recall: "What are the two access modifiers covered in this lesson, and what does each one mean?"
  explain: "Explain why making fields private and methods public is considered the standard practice in Java OOP."
  mistakeId:
    code: |
      class Person {
          public String name;
          public int age;
      }

      Person p = new Person();
      p.age = -50;
    answer: "Both fields are public, allowing any code to set them to invalid values (like a negative age). Fields should be private. Access should be provided through public methods that validate the new values before applying them."
---

# Hook

You have the vault. Now you need the lock. In Java, the lock is called an **access modifier** — a single keyword placed in front of a field or method that tells the compiler who is allowed to use it. Two modifiers do most of the work for beginning developers: `private` (only this class) and `public` (anyone). Master these two keywords and you have the core tool of encapsulation in your hands.

# Lore Introduction

The Academy's Warding Codex lists dozens of ward types for construct essence: partial wards, conditional wards, time-limited wards. But for most apprentices, two wards cover every situation. The Seal of Closure (`private`) binds an essence fragment so tightly that only the construct's own internal spells can touch it. The Open Ward (`public`) marks an access point as deliberately exposed — an invitation to those who must interact with the construct's surface. Together they define the boundary between a construct's private inner workings and its public face.

# Core Learning

## Concept Introduction

An **access modifier** is a keyword placed before a type declaration that controls where that member (field or method) can be accessed from.

| Modifier | Who can access it |
|----------|-------------------|
| `public` | Any code anywhere |
| `private` | Only code within the same class |

**Standard practice:**
- Fields → `private`
- Constructor and interface methods → `public`

```java
class Dog {
    private String name;  // hidden from outside
    private int age;      // hidden from outside

    public Dog(String name, int age) {  // constructor accessible from anywhere
        this.name = name;
        this.age  = age;
    }

    public void bark() {                // method accessible from anywhere
        System.out.println(name + " says: Woof!");
    }
}
```

## Why It Matters

The `private` keyword turns the compiler into an enforcer. If any code outside `Dog` tries to write `rex.name = "Buddy"`, the compiler will refuse to compile the program at all. This is much better than a runtime crash — you catch the error before the program ever runs.

Making fields `private` while keeping public methods is the concrete mechanism that makes encapsulation work. The public methods form a controlled *interface* — the only sanctioned ways to interact with the object.

## Worked Examples

```java
class BankAccount {
    private String owner;
    private double balance;

    public BankAccount(String owner, double startingBalance) {
        this.owner   = owner;
        this.balance = startingBalance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public double getBalance() {
        return balance;
    }
}

BankAccount acc = new BankAccount("Alice", 100.0);

// acc.balance = -9999; // COMPILE ERROR — balance is private

acc.deposit(50.0);               // OK — deposit() is public
System.out.println(acc.getBalance()); // 150.0
```

The fields `owner` and `balance` cannot be touched directly from outside. All interaction goes through `deposit()` and `getBalance()`.

## Common Mistakes

- **Forgetting `private` on fields**: If you declare a field with no modifier, it has "package-private" access (accessible within the same package). Always explicitly write `private` on fields.
- **Making everything private including the constructor**: If the constructor is private, no external code can create objects. Make constructors and interface methods `public`.
- **Thinking `private` is about secrecy**: It is about controlled access and preventing invalid state — not about keeping algorithms secret.

## Mental Model

Think of `private` as a **staff-only door** in a building. Guests (external code) can use the public entrance and the public lobby, but they cannot open the staff door. Staff (the class's own methods) can go anywhere — through the public entrance and through the staff door. The building (class) has both a public face and a private back-office where its internal machinery lives.

## Mini Summary

- ✔ `public` = accessible from anywhere.
- ✔ `private` = accessible only within the same class.
- ✔ Standard practice: fields are `private`, constructor and interface methods are `public`.
- ✔ The compiler enforces `private` at compile time — violations prevent compilation.
- ✔ `private` fields are still accessible from within the class's own methods.

# Guided Practice Quest

Work through the sidebar steps to add `private` to the fields of a `Dog` class, observe the compile error when external code tries to access a private field, and articulate the standard access pattern.

# Solo Practice Quest

**Spell: Seal the Essence**

Rewrite this broken class with correct access modifiers:

```java
class Potion {
    String name;
    int healAmount;
    boolean isBrewed;

    Potion(String name, int healAmount) {
        this.name = name;
        this.healAmount = healAmount;
        this.isBrewed = false;
    }

    void brew() {
        isBrewed = true;
        System.out.println(name + " has been brewed!");
    }

    boolean isReady() {
        return isBrewed;
    }
}
```

1. Apply correct access modifiers to all fields, the constructor, and all methods.
2. Explain in one sentence why each field is private.
3. Show an example where external code correctly interacts with a `Potion` object (no compile errors).

# Integration

**Philosophy connection — interface vs implementation**

Philosophers of language distinguish between the *meaning* of a word (its sense) and the *thing it refers to* (its reference). Access modifiers create a similar distinction in code: the `public` interface is the *sense* — what users of the class need to know and interact with. The `private` implementation is the *reference* — the actual mechanism that makes things work. Hiding the implementation means the interface can stay stable even as the implementation changes. Users of the class only depend on the public face, not the internal machinery.

**Mathematics connection — abstraction barriers**

In computer science theory, an *abstraction barrier* is a layer that separates the implementation of a data structure from its use. Access modifiers implement abstraction barriers in Java. When `balance` is private, the barrier prevents users of `BankAccount` from knowing whether it is stored as a `double`, an `int`, or computed on demand — all they know is that `getBalance()` returns a value. If you later change from `double` to `BigDecimal` for precision, no external code breaks because no external code was accessing the raw field.

**Question:** A class `PriceCalculator` has a private `double taxRate` field. If you later need to change `taxRate` to be configurable per region (now a more complex calculation), explain why having it `private` makes this change easier than if it were `public`.

# Lore Conclusion

The Seal of Closure is inscribed upon your constructs. From this point forward, every field you declare will carry the `private` ward, and every external interaction will pass through your carefully designed public methods. But right now those public methods are limited to `void` actions. In the next lesson you will add the final pieces of the encapsulation pattern: getters that let authorised callers *read* private fields, and setters that let them *change* those fields — safely, with validation.
