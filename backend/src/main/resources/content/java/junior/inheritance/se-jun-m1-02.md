---
id: se-jun-m1-02
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m1
moduleTitle: "Module 1: Object-Oriented Design"
moduleGlyph: "🏛️"
moduleSortOrder: 1
topicSlug: inheritance
topicTitle: "Inheritance"
topicSortOrder: 1
lesson: why_inheritance_fails
title: "Why Inheritance Fails"
sortOrder: 2
difficulty: 3
estimatedMinutes: 30
xpReward: 60
practiceType: JAVA
questType: INVESTIGATION
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m1-01]
integrationDomains: [composition, solid_principles]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies at least one concrete problem with the given inheritance hierarchy"
    - "Explains the square-rectangle paradox or fragile base class problem in own words"
    - "Proposes a composition-based alternative with concrete Java code"
    - "Compares inheritance vs composition trade-offs for the given scenario"
    - "Uses correct Java syntax for the proposed refactoring"
  keywords: [fragile, hierarchy, composition, square, rectangle, LSP, Liskov, override, coupling, delegation]
  modelAnswer: |
    // Problem: Square extends Rectangle violates LSP
    // If Rectangle has setWidth/setHeight independently, Square breaks the contract.

    // Composition alternative:
    public interface Shape {
        double getArea();
    }

    public class Rectangle implements Shape {
        private double width, height;
        public Rectangle(double width, double height) {
            this.width = width; this.height = height;
        }
        public double getArea() { return width * height; }
    }

    public class Square implements Shape {
        private double side;
        public Square(double side) { this.side = side; }
        public double getArea() { return side * side; }
    }
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: REFLECTION
    instruction: "Read this class hierarchy: Animal → Pet → Dog → GoldenRetriever → ShowGoldenRetriever. List at least three problems you would face maintaining this five-level hierarchy."
    inputConfig:
      minWords: 40
    markingRule: "Mentions at least two of: deep coupling, fragile base class, hard to test, superclass changes break subclasses, violates single responsibility"
    hint: "Think about what happens when someone changes a field in Animal. How many classes are affected?"
    reflectionPrompt: "Which level of this hierarchy could you eliminate without losing any important domain concept?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "You're given a `Rectangle` class with setWidth and setHeight. Write a `Square` class that extends it. Then write a comment explaining what breaks when you call setWidth(5) on a Square."
    inputConfig:
      language: java
      starterCode: |
        public class Rectangle {
            protected double width, height;
            public void setWidth(double w) { this.width = w; }
            public void setHeight(double h) { this.height = h; }
            public double getArea() { return width * height; }
        }

        public class Square extends Rectangle {
            // your code here
        }
    markingRule: "Square extends Rectangle, overrides setWidth and setHeight to keep sides equal, comment explains the Liskov Substitution violation"
    hint: "In a Square, setting the width must also set the height. But that means a Square doesn't behave like a Rectangle everywhere a Rectangle is expected."
    reflectionPrompt: "Would the problem disappear if Rectangle was made immutable (no setters)? Why?"
  - id: step-3
    sortOrder: 3
    inputType: REFLECTION
    instruction: "Describe in 3–5 sentences when you would choose inheritance vs composition. Give one real-world class pair for each choice."
    inputConfig:
      minWords: 50
    markingRule: "Chooses inheritance for genuine is-a relationships with stable hierarchies; chooses composition for has-a, flexibility, or to avoid fragile base class"
    hint: "Dog is-a Animal is genuine. Car has-a Engine is a has-a. Which lends itself to extending behaviour vs delegating it?"
    reflectionPrompt: "Can you think of a case where what seems like an is-a relationship should actually be modelled as has-a?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The Liskov Substitution Principle states that:"
    options:
      - "Every subclass must override every method of its superclass"
      - "Objects of a subclass must be substitutable for objects of the superclass without breaking correctness"
      - "Subclasses should always call super() before adding their own logic"
      - "Inheritance should only be used when the superclass has at least three methods"
    correctIndex: 1
    feedback: "LSP (the L in SOLID) means anywhere you use a superclass reference, a subclass object should work correctly. If Square breaks Rectangle's contract, it violates LSP."
  - type: MULTIPLE_CHOICE
    question: "What is the 'fragile base class' problem?"
    options:
      - "Abstract base classes compile more slowly"
      - "Changes to a superclass can unexpectedly break subclasses that depend on its internal behaviour"
      - "Base classes with more than five methods are too fragile to extend"
      - "Using final on base classes causes fragility"
    correctIndex: 1
    feedback: "When a superclass changes its internal implementation, all subclasses that relied on that internal behaviour can break — even if the public API didn't change. This is the fragile base class problem."
retrieval:
  recall: "What is the Liskov Substitution Principle, and how does the square-rectangle paradox violate it?"
  explain: "Explain why deep inheritance hierarchies (four or more levels) are considered a code smell. What problems do they create in practice?"
  mistakeId:
    code: |
      public class Bird {
          public void fly() { System.out.println("Flying!"); }
      }
      public class Penguin extends Bird {
          @Override
          public void fly() {
              throw new UnsupportedOperationException("Penguins can't fly");
          }
      }
    answer: "Penguin violates LSP — code expecting a Bird can call fly() and crash. Penguin should not extend Bird if fly() is a core Bird contract. Use an interface like `Swimmable` instead, or model Bird without a fly() method in the base class."
---

# Hook

You've learned inheritance — now let's break it. Not because inheritance is bad, but because understanding *when it fails* is what separates a developer who writes clever code from one who writes maintainable code. The square-rectangle paradox is one of the most famous traps in OOP: mathematically, every square is a rectangle, so `Square extends Rectangle` seems obvious. But in Java, it leads to code that violates one of the most important design principles in the field. Let's find out exactly why, and what you should do instead.

# Lore Introduction

The Academy's enchantment library once had a great hierarchy: `Enchantment` → `WeaponEnchantment` → `SwordEnchantment` → `RunicSwordEnchantment` → `AncientRunicSwordEnchantment`. When the head archivist changed the mana calculation in `Enchantment`, two dozen subclasses shattered overnight. The deeper the hierarchy, the more brittle the tower. Senior artificers now speak of the "fragile base class" as a cautionary tale — a reminder that inheritance chains are load-bearing walls. Change one brick at the foundation and the whole structure trembles.

# Core Learning

## Concept Introduction

Inheritance has three well-documented failure modes:

**1. The Fragile Base Class Problem**
When a superclass changes its internal implementation, subclasses that override its methods and rely on its internal behaviour can break silently. The subclass is tightly coupled to the superclass's internals, not just its public contract.

**2. Deep Hierarchies**
Each level of inheritance adds coupling. A five-level hierarchy means that to understand a leaf class, you must understand all five ancestors. This makes the code hard to read, test, and change.

**3. The Square-Rectangle Paradox (Liskov Substitution Violation)**
Mathematically, a square is a special case of a rectangle. So `Square extends Rectangle` seems natural. But if `Rectangle` has independent `setWidth()` and `setHeight()` methods, a `Square` must override both to keep its sides equal. This breaks the **Liskov Substitution Principle (LSP)**: code written for `Rectangle` objects may break when given a `Square`.

```java
public void doubleWidth(Rectangle r) {
    double originalHeight = r.getHeight();
    r.setWidth(r.getWidth() * 2);
    // For Rectangle: area = 2 * original area. Fine.
    // For Square: setWidth also sets height, so height is no longer originalHeight!
    assert r.getArea() == r.getWidth() * originalHeight; // fails for Square!
}
```

LSP says: if `S extends T`, you must be able to use `S` anywhere `T` is expected without breaking correctness. `Square` fails this test.

## Why It Matters

LSP violations cause the most insidious bugs: the code compiles, the tests pass in isolation, but the system behaves incorrectly at runtime when polymorphism routes to the "wrong" subclass. Deep hierarchies produce maintenance nightmares — the cost of understanding and changing code grows with each level. Recognising these patterns early is what prevents a "clever" design from becoming a codebase nobody wants to touch.

## Worked Examples

**Example 1 — The fragile base class in action**

```java
// Original superclass
public class CountingList<T> extends ArrayList<T> {
    private int addCount = 0;

    @Override
    public boolean add(T element) {
        addCount++;
        return super.add(element);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() { return addCount; }
}

// Problem: ArrayList.addAll() internally calls add() repeatedly in some JVM versions.
// This double-counts! The superclass implementation detail leaks into the subclass.
CountingList<String> list = new CountingList<>();
list.addAll(List.of("a", "b", "c"));
System.out.println(list.getAddCount()); // may print 6, not 3!
```

The fix is **composition** — hold an `ArrayList` as a field and delegate to it, rather than extending it.

**Example 2 — The square-rectangle paradox**

```java
public class Rectangle {
    protected double width, height;
    public void setWidth(double w) { this.width = w; }
    public void setHeight(double h) { this.height = h; }
    public double getArea() { return width * height; }
}

public class Square extends Rectangle {
    @Override
    public void setWidth(double w) {
        this.width = w;
        this.height = w; // keep square invariant
    }
    @Override
    public void setHeight(double h) {
        this.height = h;
        this.width = h; // keep square invariant
    }
}

// Caller written for Rectangle — breaks with Square:
void stretchWidth(Rectangle r) {
    r.setHeight(10);
    r.setWidth(20);
    // Expects area = 200. For Square, area = 400 (setWidth resets height to 20).
}
```

**Example 3 — The composition fix**

```java
public interface Shape {
    double getArea();
}

// Rectangle and Square are now siblings, not parent-child.
public class Rectangle implements Shape {
    private final double width, height;
    public Rectangle(double width, double height) {
        this.width = width; this.height = height;
    }
    @Override public double getArea() { return width * height; }
}

public class Square implements Shape {
    private final double side;
    public Square(double side) { this.side = side; }
    @Override public double getArea() { return side * side; }
}
```

Both implement `Shape`. Neither inherits from the other. No LSP violation. No fragile dependency.

## Common Mistakes

- **Reaching for inheritance because "it's an is-a" without checking LSP.** The is-a test is necessary but not sufficient. Ask: can I substitute a subclass object everywhere the superclass is expected without breaking anything?
- **Adding `throws UnsupportedOperationException` in a subclass method.** This is a red flag that the subclass doesn't actually honour the superclass's contract — inheritance was the wrong choice.
- **Building hierarchies with more than three levels.** This is almost always a sign that composition or interfaces would serve better.
- **Overriding methods in ways that change observable behaviour from the caller's perspective.** Adding side effects, narrowing return types in unexpected ways, or changing exception contracts all violate LSP.
- **Confusing code reuse with inheritance.** Inheritance should express type relationships, not just share code. For reuse without relationship, use composition or utility classes.

## Mental Model

Think of inheritance as a contract. When you say `Square extends Rectangle`, you're promising: "Anywhere a Rectangle is expected, a Square will work correctly." If you can't keep that promise — because the subclass needs to override methods in ways that change the caller's expectations — then the promise was wrong from the start. Don't use inheritance to share code; use it only to express genuine substitutable type relationships.

## Mini Summary

- The fragile base class problem: subclasses can break when superclass internals change, even without API changes.
- Deep hierarchies (four or more levels) create tight coupling and high cognitive load.
- The square-rectangle paradox shows that mathematical is-a does not always map to a valid inheritance relationship.
- The Liskov Substitution Principle: subclasses must be substitutable for their superclass without breaking correctness.
- When a subclass throws `UnsupportedOperationException` for an inherited method, it's a signal inheritance was the wrong choice.
- Composition (has-a) is usually more flexible and less fragile than deep inheritance chains.

# Guided Practice Quest

Work through the investigation steps above. You'll articulate the problems in a deep hierarchy, implement the square-rectangle paradox in code, observe exactly where the substitution breaks, and then describe when you would choose inheritance versus composition.

# Solo Practice Quest

You're given a hierarchy: `Employee extends Person`, `Manager extends Employee`, `Director extends Manager`. Identify at least two problems with this design for a real HR system (where job responsibilities change frequently). Then propose a composition-based alternative for at least one of the relationships — write the Java code for your alternative design and explain in comments why it is more maintainable.

# Integration

Understanding where inheritance fails is the bridge to the next two lessons: **Composition over Inheritance** and **SOLID Principles**. The Liskov Substitution Principle is the "L" in SOLID — and violations of it almost always trace back to inheritance misuse. When you later work with Spring's IoC container and dependency injection, you'll see that the entire framework is built on interfaces rather than inheritance hierarchies, making it trivially easy to swap implementations without touching the caller.

The square-rectangle paradox also connects to **immutability**: if `Rectangle` had no setters (all fields set in the constructor and never changed), the paradox disappears entirely, because there's no way to mutate an object into an inconsistent state. This is why immutable value objects are increasingly favoured in modern Java design — they remove a whole class of LSP violations.

**Integration question:** A colleague proposes `AdminUser extends User extends Person`. Using what you've learned about LSP and the fragile base class problem, what questions would you ask before approving this design?

# Lore Conclusion

The Academy's artificers now keep inheritance hierarchies shallow — two levels at most before they reach for interfaces or composition. The runic catastrophe of the great enchantment collapse is taught to every new artificer as a reminder: the deeper the tower, the harder the fall. Inheritance is a powerful tool precisely because it carries strong promises. Make only the promises you can keep.
