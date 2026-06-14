---
id: se-jun-m1-03
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m1
moduleTitle: "Module 1: Object-Oriented Design"
moduleGlyph: "🏛️"
moduleSortOrder: 1
topicSlug: polymorphism
topicTitle: "Polymorphism"
topicSortOrder: 2
lesson: polymorphism
title: "Polymorphism"
sortOrder: 3
difficulty: 3
estimatedMinutes: 30
xpReward: 70
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m1-01]
integrationDomains: [interfaces, solid_principles]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Creates a superclass or interface with a polymorphic method"
    - "Creates at least two subclasses/implementations each with distinct behaviour"
    - "Demonstrates runtime dispatch by storing objects in a list typed to the parent/interface"
    - "Iterates over the list and calls the polymorphic method"
    - "Explains in comments what runtime dispatch means and why it's useful"
  keywords: [polymorphism, dispatch, runtime, override, interface, superclass, reference, dynamic, substitution, list]
  modelAnswer: |
    public abstract class Notification {
        public abstract void send(String message);
    }

    public class EmailNotification extends Notification {
        @Override
        public void send(String message) {
            System.out.println("Email: " + message);
        }
    }

    public class SmsNotification extends Notification {
        @Override
        public void send(String message) {
            System.out.println("SMS: " + message);
        }
    }

    // Runtime dispatch in action:
    List<Notification> channels = List.of(new EmailNotification(), new SmsNotification());
    for (Notification n : channels) {
        n.send("Your order has shipped!"); // correct send() called at runtime
    }
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Create an `Animal` class with a `speak()` method returning 'Some animal sound'. Create `Dog` and `Cat` subclasses that override `speak()` with appropriate sounds."
    inputConfig:
      language: java
      starterCode: "// Define Animal, Dog, and Cat here"
    markingRule: "Animal has speak() method, Dog and Cat extend Animal and override speak() with @Override, different return values"
    hint: "Use @Override in both subclasses. The return type must match exactly."
    reflectionPrompt: "What return value does speak() produce when called on an Animal reference pointing to a Dog object?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Create a `List<Animal>` containing a Dog, a Cat, and an Animal. Loop over the list and call speak() on each. Observe which version runs."
    inputConfig:
      language: java
      starterCode: |
        import java.util.List;
        // Write a main method or static method that creates the list and calls speak()
    markingRule: "Creates List<Animal>, adds instances of Dog, Cat, Animal, iterates and calls speak() on each"
    hint: "Animal a = new Dog() is valid — a Dog IS an Animal. The list type is Animal but the objects inside retain their actual types."
    reflectionPrompt: "Could you call a Dog-specific method on an Animal reference? What would you need to do to call it?"
  - id: step-3
    sortOrder: 3
    inputType: CODE
    instruction: "Add a `makeNoise(Animal a)` static method that takes any Animal and calls speak(). Call it with a Dog and a Cat. This is 'programming to the supertype'."
    inputConfig:
      language: java
      starterCode: "public static void makeNoise(Animal a) {\n    // call speak()\n}"
    markingRule: "Method accepts Animal parameter, calls a.speak(), works correctly when passed Dog or Cat"
    hint: "The method doesn't need to know what kind of Animal it received — it just calls speak() and trusts runtime dispatch to handle it."
    reflectionPrompt: "If you add a Parrot class later, does makeNoise() need to change? Why is that significant?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Given `Animal a = new Dog();`, what happens when you call `a.speak()`?"
    options:
      - "Animal's speak() runs because the reference type is Animal"
      - "A compile error occurs because Dog is not an Animal"
      - "Dog's speak() runs because the actual object at runtime is a Dog"
      - "Both Animal's and Dog's speak() run in sequence"
    correctIndex: 2
    feedback: "Java uses runtime dispatch (dynamic method dispatch) — the JVM looks at the actual object type, not the reference type, to decide which method to call. This is the heart of polymorphism."
  - type: MULTIPLE_CHOICE
    question: "What does 'programming to the supertype' mean?"
    options:
      - "Always using the most specific subclass type for variables"
      - "Declaring variables and parameters using the parent type or interface, not the concrete subclass"
      - "Avoiding subclasses and using only superclasses"
      - "Writing code that only runs in the superclass"
    correctIndex: 1
    feedback: "Programming to the supertype means `Animal a = new Dog()` rather than `Dog a = new Dog()`. It makes code flexible — you can swap the Dog for a Cat without changing the variable declaration or method signatures."
retrieval:
  recall: "What is runtime dispatch (dynamic method dispatch) in Java, and what determines which method implementation is called?"
  explain: "Why is it useful to store Dog and Cat objects in a List<Animal> rather than separate List<Dog> and List<Cat> collections?"
  mistakeId:
    code: |
      Animal a = new Dog();
      a.fetch(); // Dog has a fetch() method, Animal does not
    answer: "Compile error: the reference type is Animal, and Animal has no fetch() method. To call Dog-specific methods, cast first: ((Dog) a).fetch(). But if you need Dog-specific methods, consider whether you should be using a Dog reference instead."
---

# Hook

What if you could write a method that processes a list of 50 different animal types — dogs, cats, parrots, tigers — with a single loop and zero if-statements? No `if (animal instanceof Dog)`, no switch on type. Just call `speak()` and let Java figure out which version to run. That's polymorphism: the ability to write code that works correctly for types you haven't invented yet, because the runtime always routes to the right implementation.

# Lore Introduction

The Academy's combat system hosts dozens of spell types: fire, ice, lightning, shadow. Rather than writing separate combat handlers for each, the chief battle-mage wrote a single `executeCombatRound(Spell spell)` method. Every turn, whatever spell a wizard casts, the same method runs — and Java's runtime dispatch ensures the correct `cast()` implementation fires. New spell types can be added without touching the combat engine. This is polymorphism working as designed: one interface, many behaviours, zero extra conditionals.

# Core Learning

## Concept Introduction

**Polymorphism** (Greek: "many shapes") means that objects of different types can be treated through a common interface and each responds with its own behaviour. In Java, the primary mechanism is **runtime method dispatch** (also called dynamic dispatch):

- When you call a method through a reference, Java looks at the **actual object type** at runtime — not the declared reference type.
- This works because method overriding establishes that each subclass provides its own version of the method.

There are two types of polymorphism in Java:
1. **Subtype polymorphism** (runtime dispatch via inheritance/interfaces) — the focus of this lesson.
2. **Ad hoc polymorphism** (method overloading, compile-time) — you already know this from method signatures with different parameter types.

## Why It Matters

Polymorphism is what makes systems extensible without modification. When you add a new `Bird` class to a system that already has `Animal`-typed code, the existing `makeNoise(Animal)` method just works. No if-chains, no switch statements, no changes to existing code. This is the Open-Closed Principle (the "O" in SOLID) in action: open for extension, closed for modification. It's also what makes design patterns like Strategy and Observer possible — both depend on calling methods through an interface without knowing the concrete type.

## Worked Examples

**Example 1 — Basic runtime dispatch**

```java
public class Animal {
    public String speak() { return "..."; }
}

public class Dog extends Animal {
    @Override
    public String speak() { return "Woof!"; }
}

public class Cat extends Animal {
    @Override
    public String speak() { return "Meow!"; }
}

// Runtime dispatch in action:
Animal a1 = new Dog();
Animal a2 = new Cat();

System.out.println(a1.speak()); // "Woof!" — Dog's version runs
System.out.println(a2.speak()); // "Meow!" — Cat's version runs
```

The reference type is `Animal`, but the actual objects are `Dog` and `Cat`. Java's JVM resolves the method at runtime based on the actual object.

**Example 2 — Polymorphism over a collection**

```java
List<Animal> zoo = new ArrayList<>();
zoo.add(new Dog());
zoo.add(new Cat());
zoo.add(new Dog());
zoo.add(new Cat());

// Single loop, handles any number of new Animal types without changing this code:
for (Animal a : zoo) {
    System.out.println(a.speak());
}
// Output:
// Woof!
// Meow!
// Woof!
// Meow!
```

Adding a `Parrot` class that extends `Animal` and overrides `speak()` requires zero changes to this loop.

**Example 3 — Programming to the supertype in method parameters**

```java
// This method works for ANY Animal subclass — present and future:
public static void describeLoudly(Animal animal) {
    System.out.println(">>> " + animal.speak().toUpperCase() + " <<<");
}

describeLoudly(new Dog());   // >>> WOOF! <<<
describeLoudly(new Cat());   // >>> MEOW! <<<
```

Compare this to the type-checking alternative — which breaks every time a new subclass is added:

```java
// DON'T DO THIS — brittle and violates Open-Closed Principle:
public static void describeLoudly(Animal animal) {
    if (animal instanceof Dog) {
        System.out.println(">>> WOOF! <<<");
    } else if (animal instanceof Cat) {
        System.out.println(">>> MEOW! <<<");
    }
    // Must add a new branch for every new Animal type!
}
```

## Common Mistakes

- **Casting back to the subtype to call subtype-specific methods.** If you find yourself writing `((Dog) animal).fetch()` frequently, the method belongs on `Animal` (or an interface), not exclusively on `Dog`.
- **Using `instanceof` chains instead of polymorphism.** A chain of `instanceof` checks is the anti-pattern that polymorphism was invented to eliminate.
- **Forgetting `@Override`.** Without it, a method signature mismatch silently creates a new method rather than an override, and polymorphism won't dispatch to it.
- **Assuming the reference type determines behaviour.** `Animal a = new Dog()` — calling `a.speak()` runs Dog's version, always. The reference type controls *what methods are accessible*, not *which implementation runs*.
- **Declaring method parameters with concrete types.** `void process(Dog d)` prevents passing a Cat. Unless you need Dog-specific methods, use `Animal` or an interface.

## Mental Model

Think of a remote control (the reference) and a television (the object). The remote has a "play" button that maps to the TV's implementation of play. If you plug the same remote into a Blu-ray player instead, the "play" button now plays a disc, not a TV channel — the button is the same (the reference), but the behaviour is different (the object). The JVM is like the plug adapter — it routes the button press to the right device's implementation at the moment you press it.

## Mini Summary

- Polymorphism lets you call a method through a parent-type reference and have the correct subclass implementation run at runtime.
- Java uses dynamic method dispatch: the JVM resolves which method body to execute based on the actual object type, not the reference type.
- Programming to the supertype (`Animal a = new Dog()`) makes code flexible and extensible.
- Avoid `instanceof` chains — they are the sign that polymorphism was not applied.
- `@Override` is essential: without it, a typo creates a new method rather than an override.
- Polymorphism is the mechanism behind the Open-Closed Principle and many design patterns.

# Guided Practice Quest

Follow the guided steps to build an `Animal` hierarchy and observe runtime dispatch in action. You'll store different animal types in a `List<Animal>`, iterate with a single loop, and write a utility method that accepts any `Animal` — demonstrating that your code is immediately open to new types.

# Solo Practice Quest

Design a `PaymentProcessor` system. Create a `PaymentMethod` superclass (or interface) with a `process(double amount)` method. Create `CreditCard`, `PayPal`, and `BankTransfer` subclasses, each printing a different confirmation message. Write a `checkout(List<PaymentMethod> methods, double amount)` method that processes each payment. Demonstrate that adding a fourth `CryptoPayment` type requires zero changes to `checkout()`.

# Integration

Polymorphism and interfaces are deeply connected — in the next lesson on **Composition over Inheritance**, you'll see that composition-based designs use interfaces as the polymorphic contract rather than inheritance hierarchies. This is more flexible because a class can implement multiple interfaces but only extend one parent.

In the **Design Patterns** module, nearly every pattern exploits polymorphism: the **Strategy** pattern selects an algorithm at runtime by calling through an interface; the **Observer** pattern notifies all observers by calling a common method. Spring's whole IoC container is built on calling beans through their interface types — never their concrete class — which means you can swap implementations by changing a configuration, not code.

**Integration question:** A `for (Animal a : zoo)` loop calls `a.speak()` on 100 objects. The loop code never changes regardless of how many Animal subclasses are added. Which SOLID principle does this exemplify, and why?

# Lore Conclusion

The Academy's combat engine now handles every spell type — including five new schools added last season — without a single modification to the core battle loop. When the Archmage added `VoidSpell` last week, the combat system just worked. No patches, no conditionals, no emergency deployments. Polymorphism made the system open to new magic and closed to unnecessary change. That is the art: writing code so clean it outlasts the problems it was built to solve.
