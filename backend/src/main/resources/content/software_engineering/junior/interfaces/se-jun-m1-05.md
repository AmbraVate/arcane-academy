---
id: se-jun-m1-05
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m1
moduleTitle: "Module 1: Object-Oriented Design"
moduleGlyph: "🏛️"
moduleSortOrder: 1
topicSlug: interfaces
topicTitle: "Interfaces"
topicSortOrder: 4
lesson: interfaces
title: "Interfaces"
sortOrder: 5
difficulty: 2
estimatedMinutes: 28
xpReward: 70
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m1-01, se-jun-m1-03]
integrationDomains: [composition, solid_principles]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines at least one interface with two or more method signatures"
    - "Creates at least two classes that implement the interface"
    - "Uses implements keyword correctly with all interface methods provided"
    - "Demonstrates a class implementing multiple interfaces"
    - "Explains in comments the difference between interface and abstract class"
  keywords: [interface, implements, contract, abstract, default, multiple, signature, polymorphism, type, decouple]
  modelAnswer: |
    public interface Printable {
        void print();
        String getFormat();
    }

    public interface Exportable {
        byte[] export();
    }

    // Implements both interfaces — possible with interfaces, not with inheritance
    public class Report implements Printable, Exportable {
        private final String content;

        public Report(String content) { this.content = content; }

        @Override
        public void print() { System.out.println(content); }

        @Override
        public String getFormat() { return "TEXT"; }

        @Override
        public byte[] export() { return content.getBytes(); }
    }
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Define a `Drawable` interface with a `draw()` method returning void, and a `getColour()` method returning String."
    inputConfig:
      language: java
      starterCode: "public interface Drawable {\n    // define the two methods\n}"
    markingRule: "Interface keyword used, contains void draw() and String getColour() as method signatures (no body)"
    hint: "Interface methods have no body — just the signature followed by a semicolon. No 'public' keyword needed on method signatures (they're implicitly public abstract)."
    reflectionPrompt: "Why don't interface methods have a body (implementation)? What is the purpose of defining just the signature?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Create a `Circle` and a `Square` class that both implement `Drawable`. Each should have a colour field set via constructor, and each draws differently."
    inputConfig:
      language: java
      starterCode: |
        public class Circle implements Drawable {
            // constructor, implement draw() and getColour()
        }

        public class Square implements Drawable {
            // constructor, implement draw() and getColour()
        }
    markingRule: "Both classes use implements Drawable, both implement draw() with @Override, both implement getColour() with @Override"
    hint: "The compiler will reject the class if any interface method is missing. Check both methods are implemented in each class."
    reflectionPrompt: "What error would you get if you omitted getColour() from Circle?"
  - id: step-3
    sortOrder: 3
    inputType: CODE
    instruction: "Write a method `renderAll(List<Drawable> shapes)` that calls draw() on each shape. Then add a `Resizable` interface with `resize(double factor)` and make Square implement both Drawable and Resizable."
    inputConfig:
      language: java
      starterCode: |
        // renderAll method
        // Resizable interface
        // Update Square to implement both
    markingRule: "renderAll takes List<Drawable> and calls draw() on each; Resizable interface defined; Square implements both Drawable and Resizable"
    hint: "Multiple interface implementation: `public class Square implements Drawable, Resizable`. Java allows this unlike multiple class inheritance."
    reflectionPrompt: "Could you pass a Square to renderAll()? Could you pass a Circle to a method expecting Resizable? Why or why not?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does Java enforce when a class says `implements MyInterface`?"
    options:
      - "The class must extend the interface's parent class"
      - "The class must provide an implementation for every abstract method in the interface"
      - "The class automatically inherits all default implementations"
      - "The class can only be instantiated via a factory"
    correctIndex: 1
    feedback: "The compiler checks that every abstract method declared in the interface has an implementation in the class. If any method is missing, the code won't compile unless the class is declared abstract."
  - type: MULTIPLE_CHOICE
    question: "What is a 'default method' in a Java interface?"
    options:
      - "A method that runs when no other method matches"
      - "A method with a body provided directly in the interface, which implementing classes inherit automatically"
      - "A private method only accessible within the interface"
      - "A method marked with @Default that generates boilerplate"
    correctIndex: 1
    feedback: "Default methods (introduced in Java 8) let interfaces provide a default implementation. Implementing classes inherit it automatically but can override it. This allows interfaces to evolve without breaking all existing implementations."
retrieval:
  recall: "What is the difference between an interface and an abstract class in Java? Name one thing each can do that the other cannot."
  explain: "Why does Java allow a class to implement multiple interfaces but only extend one class? What problem would multiple class inheritance create?"
  mistakeId:
    code: |
      public interface Greeter {
          public void greet() {
              System.out.println("Hello!");
          }
      }
    answer: "Interface methods cannot have a body unless marked as 'default'. Either remove the body (making it abstract), or add the 'default' keyword: `default void greet() { ... }`."
---

# Hook

What if you could promise to any caller: "This object will always have a `send()` method and a `disconnect()` method" — without specifying what kind of object it is? That's an interface. It's a contract: any class that implements it must provide those methods. No implementation details, no inheritance chain, just a guarantee. And crucially, a class can honour multiple contracts at once — impossible with class inheritance. Interfaces are the backbone of almost every flexible Java system ever built.

# Lore Introduction

The Academy's messenger network supports ravens, crystal balls, and enchanted mirrors — all completely different objects. But they all fulfil one contract: they implement `Messenger`. Any system component that needs to send a message just receives a `Messenger` reference and calls `send()`. It doesn't know if it's talking to a raven or a mirror. The entire infrastructure works because of this shared contract. Interfaces make that agreement formal and compiler-enforced.

# Core Learning

## Concept Introduction

An **interface** in Java is a type that defines a contract — a set of method signatures that any implementing class must provide. Key properties:

- Declared with `interface` keyword (not `class`)
- Methods are implicitly `public abstract` (no body by default)
- No instance fields (only `public static final` constants)
- A class implements an interface with `implements`
- A class can implement **multiple** interfaces (unlike extending classes)
- Since Java 8: `default` methods provide a body in the interface itself
- Since Java 9: `private` methods allowed for helper logic within the interface

**Interface vs Abstract Class:**

| Feature | Interface | Abstract Class |
|---|---|---|
| Multiple | Yes — implement many | No — extend one |
| Fields | No instance fields | Can have instance fields |
| Constructor | No | Yes |
| Default impl | Via `default` keyword | Any non-abstract method |
| Use when | Defining a capability/role | Sharing code in a hierarchy |

## Why It Matters

Interfaces are the primary tool for **decoupling** in Java. When a method parameter is typed to an interface rather than a concrete class, the caller and the implementation are independent — you can swap one implementation for another without touching the caller. This is the "D" in SOLID (Dependency Inversion) and the foundation of dependency injection. Every Java collection you use (`List`, `Map`, `Set`) is an interface — the concrete class (`ArrayList`, `HashMap`) is hidden behind the contract.

## Worked Examples

**Example 1 — Basic interface and implementation**

```java
public interface Messenger {
    void send(String message);
    boolean isConnected();
}

public class RavenMessenger implements Messenger {
    @Override
    public void send(String message) {
        System.out.println("Raven delivers: " + message);
    }

    @Override
    public boolean isConnected() { return true; }
}

public class CrystalBallMessenger implements Messenger {
    @Override
    public void send(String message) {
        System.out.println("Crystal ball transmits: " + message);
    }

    @Override
    public boolean isConnected() { return Math.random() > 0.1; } // sometimes unreliable!
}
```

**Example 2 — Multiple interface implementation**

```java
public interface Saveable {
    void save();
}

public interface Printable {
    void print();
}

// One class, two contracts:
public class Report implements Saveable, Printable {
    private final String content;

    public Report(String content) { this.content = content; }

    @Override
    public void save() { System.out.println("Saving: " + content); }

    @Override
    public void print() { System.out.println("Printing: " + content); }
}

// Can be used wherever either interface is expected:
Saveable s = new Report("Q3 results");
Printable p = new Report("Q3 results");
```

**Example 3 — Default methods for backwards-compatible evolution**

```java
public interface Collection<T> {
    void add(T element);
    int size();

    // Default method — existing implementations don't need to change:
    default boolean isEmpty() {
        return size() == 0;
    }
}
```

If `isEmpty()` was added as an abstract method, every class implementing `Collection` would break. The `default` keyword lets interfaces evolve without breaking all existing implementations.

**Example 4 — Programming through an interface reference**

```java
// Method accepts any Messenger — doesn't care which concrete type:
public static void broadcastAlert(Messenger messenger, String alert) {
    if (messenger.isConnected()) {
        messenger.send("[ALERT] " + alert);
    } else {
        System.out.println("Messenger unavailable.");
    }
}

broadcastAlert(new RavenMessenger(), "Enemy approaching!");
broadcastAlert(new CrystalBallMessenger(), "Storm incoming!");
```

## Common Mistakes

- **Implementing the interface but missing a method.** The compiler will tell you, but reading the error carefully matters: it tells you exactly which method is missing.
- **Adding instance fields to an interface.** Interface fields are implicitly `public static final` (constants). They are not instance state.
- **Making interface methods private or package-private.** Interface methods are always public. You cannot restrict their visibility in the interface declaration.
- **Treating `default` methods as the solution to code sharing.** Default methods are for backwards-compatible API evolution, not for building shared logic. For shared code, use an abstract class or a separate utility class.
- **Naming interfaces with "I" prefix.** `IMessenger` is a .NET convention and looks out of place in Java. Prefer `Messenger`, `Comparable`, `Runnable` — role nouns or adjectives.

## Mental Model

An interface is a job description. "To work as a Messenger, you must be able to `send()` messages and report whether you're `isConnected()`." The job description doesn't care whether the messenger is a raven, a crystal ball, or an email server. Any object that meets the job description can fill the role. The caller hires for the role, not the specific person — which means you can replace one employee with another as long as they meet the same specification.

## Mini Summary

- An interface defines a contract (method signatures) with no implementation (unless using `default`).
- A class implements an interface using `implements` and must provide all abstract methods.
- A class can implement multiple interfaces — solving the "multiple inheritance" need in Java.
- `default` methods in interfaces allow backwards-compatible evolution.
- Declare variables and parameters using the interface type, not the concrete class.
- Interfaces decouple callers from implementations — the foundation of SOLID DIP.

# Guided Practice Quest

Follow the guided steps to create a `Drawable` interface, implement it in two shape classes, then write a `renderAll()` method and add a second `Resizable` interface that `Square` implements alongside `Drawable`.

# Solo Practice Quest

Design a payment processing system using interfaces. Create a `PaymentGateway` interface with `processPayment(double amount)` and `refund(double amount)` methods. Implement it with `StripeGateway` and `PayPalGateway` classes. Then add a `Loggable` interface with `logTransaction(String details)`. Make `StripeGateway` implement both interfaces. Write a method that accepts a `PaymentGateway` and processes three different amounts. Explain why the method doesn't need to know which gateway it uses.

# Integration

Interfaces are the glue that makes Java's standard library work. `List`, `Map`, `Set`, `Comparable`, `Runnable`, `Callable`, `Iterable` — all interfaces. When you call `Collections.sort(list)` and pass a `Comparator`, you're passing an interface implementation. When Spring injects a dependency, it injects it through the interface type. When you write a JUnit test, `@Test` methods work because JUnit uses the `Method` reflection API, which itself calls methods polymorphically through interface references.

In the next lesson, **Interface-Driven Design**, you'll go deeper on the practice of designing systems around interfaces first, writing to the contract before writing the implementation. This is the discipline that makes your systems testable, swappable, and maintainable at scale.

**Integration question:** `List<String> list = new ArrayList<>()` — why is this preferred over `ArrayList<String> list = new ArrayList<>()`? What does it allow you to do later that the second form prevents?

# Lore Conclusion

The Academy's messenger network is now fully interface-driven. New communication methods — enchanted mirrors, dream-sending orbs, carrier pigeons — can be added by any artificer who implements `Messenger`. The alert system doesn't need patching, the archive system doesn't need changing, and the diplomatic corps keeps running without interruption. Interfaces made the network open to new magic and closed to unnecessary disruption. The contract holds the system together; the implementation can change beneath it freely.
