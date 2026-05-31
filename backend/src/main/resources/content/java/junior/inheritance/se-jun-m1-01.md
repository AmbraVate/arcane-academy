---
id: se-jun-m1-01
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
lesson: inheritance
title: "Inheritance"
sortOrder: 1
difficulty: 2
estimatedMinutes: 30
xpReward: 60
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [solid_principles, polymorphism]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines a superclass with shared fields and a constructor called via super()"
    - "Defines at least two subclasses that extend the superclass"
    - "Overrides at least one method with @Override annotation"
    - "Calls super() or super.method() appropriately"
    - "Explains in comments why inheritance is appropriate for this relationship"
  keywords: [extends, super, override, superclass, subclass, inherit, polymorphism, constructor, annotation, hierarchy]
  modelAnswer: |
    public class Vehicle {
        protected String brand;
        protected int year;

        public Vehicle(String brand, int year) {
            this.brand = brand;
            this.year = year;
        }

        public String describe() {
            return brand + " (" + year + ")";
        }
    }

    public class Car extends Vehicle {
        private int doors;

        public Car(String brand, int year, int doors) {
            super(brand, year);
            this.doors = doors;
        }

        @Override
        public String describe() {
            return super.describe() + " - Car with " + doors + " doors";
        }
    }

    public class Motorcycle extends Vehicle {
        public Motorcycle(String brand, int year) {
            super(brand, year);
        }

        @Override
        public String describe() {
            return super.describe() + " - Motorcycle";
        }
    }
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Create a `Shape` superclass with a `color` field, a constructor, and a `getArea()` method that returns 0.0."
    inputConfig:
      language: java
      starterCode: "public class Shape {\n    // your code here\n}"
    markingRule: "Contains protected or private color field, constructor accepting color, and getArea() returning double"
    hint: "Use protected for fields you want subclasses to access. The getArea() method should return 0.0 as a default."
    reflectionPrompt: "Why might you want getArea() to return 0.0 rather than be abstract at this stage?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Create a `Circle` subclass that extends `Shape`. It should have a `radius` field and override `getArea()` to return π * r²."
    inputConfig:
      language: java
      starterCode: "public class Circle extends Shape {\n    // your code here\n}"
    markingRule: "Uses extends Shape, calls super() in constructor, overrides getArea() with Math.PI * radius * radius, uses @Override"
    hint: "Use Math.PI for the value of π. Don't forget to call super(color) in the Circle constructor."
    reflectionPrompt: "What does @Override do — and what happens if you misspell the method name without it?"
  - id: step-3
    sortOrder: 3
    inputType: CODE
    instruction: "Create a `Rectangle` subclass with `width` and `height` fields. Override `getArea()` and also add a `getPerimeter()` method."
    inputConfig:
      language: java
      starterCode: "public class Rectangle extends Shape {\n    // your code here\n}"
    markingRule: "Extends Shape, overrides getArea() returning width*height, adds getPerimeter() returning 2*(width+height)"
    hint: "Rectangle has its own methods beyond what Shape provides — subclasses can extend, not just override."
    reflectionPrompt: "Can you call getPerimeter() on a Shape reference pointing to a Rectangle? Why or why not?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `super()` do when called inside a subclass constructor?"
    options:
      - "Calls the superclass's static initialiser"
      - "Calls the superclass's constructor"
      - "Creates a new instance of the superclass"
      - "Copies all superclass fields into the subclass"
    correctIndex: 1
    feedback: "super() invokes the superclass constructor, allowing the subclass to initialise inherited fields. It must be the first statement in the subclass constructor."
  - type: MULTIPLE_CHOICE
    question: "What does the @Override annotation enforce?"
    options:
      - "It makes the method run faster at runtime"
      - "It tells the compiler to check that you are actually overriding a superclass method"
      - "It prevents the method from being called on superclass references"
      - "It automatically generates the method body"
    correctIndex: 1
    feedback: "@Override is a compile-time check. If the method signature doesn't match any superclass method, the compiler raises an error — saving you from silent bugs caused by typos."
retrieval:
  recall: "What keyword is used to inherit from another class in Java, and what is the first statement typically required in the subclass constructor?"
  explain: "Explain the difference between overriding a method and overloading a method. Give a brief example of each."
  mistakeId:
    code: |
      public class Animal {
          private String name;
          public Animal(String name) { this.name = name; }
      }
      public class Dog extends Animal {
          public Dog(String name) {
              this.name = name; // compile error
          }
      }
    answer: "Dog cannot access the private field `name` directly. It must call `super(name)` to delegate initialisation to the Animal constructor."
---

# Hook

You've been building objects with fields and methods. Now imagine you're designing a game with a `Wizard`, a `Warrior`, and an `Archer` — they all have a name, health points, and an attack method, but each behaves differently. Do you copy-paste the common code into all three classes? That path leads to nightmares when you need to fix a bug in `takeDamage()` and have to hunt down three identical copies. Inheritance lets you define shared behaviour once in a parent class and specialise it in child classes — one fix, propagated everywhere.

# Lore Introduction

In the Arcane Academy archives, every spell belongs to a school of magic. A `FireSpell` and an `IceSpell` are both `Spell` objects — they share a name, a mana cost, and a `cast()` method, but each school of magic has its own casting effect. By capturing what spells have in common in a `Spell` superclass, the Academy can treat all spells uniformly while allowing each to behave distinctly. Inheritance models the "is-a" relationship: a `FireSpell` **is a** `Spell`, just as a `Car` **is a** `Vehicle`. When you extend a class, you gain everything the parent has and add your own flavour on top.

# Core Learning

## Concept Introduction

**Inheritance** allows a class (the **subclass** or child) to acquire the fields and methods of another class (the **superclass** or parent). You declare this relationship with the `extends` keyword.

Key mechanics:
- `extends ClassName` — establishes the inheritance relationship
- `super()` — calls the superclass constructor (must be first line of subclass constructor)
- `super.method()` — calls a superclass method from within an override
- `@Override` — annotation that asks the compiler to verify you're genuinely overriding a superclass method

Java supports **single inheritance** for classes: a class can extend only one parent. However, a parent can itself extend another class, creating a chain.

## Why It Matters

Inheritance eliminates duplication across related types. When shared behaviour lives in one place, a bug fix or feature addition only needs to happen once. It also enables **polymorphism** — the ability to treat different subclasses through a common superclass reference — which you'll explore in the next lesson. Used well, inheritance expresses genuine domain relationships clearly. Used poorly, it creates rigid, brittle hierarchies. The discipline is knowing *when* inheritance is the right tool versus composition (a topic you'll investigate very soon).

## Worked Examples

**Example 1 — Basic inheritance with super()**

```java
public class Spell {
    protected String name;
    protected int manaCost;

    public Spell(String name, int manaCost) {
        this.name = name;
        this.manaCost = manaCost;
    }

    public String cast() {
        return name + " costs " + manaCost + " mana.";
    }
}

public class FireSpell extends Spell {
    private int burnDuration;

    public FireSpell(String name, int manaCost, int burnDuration) {
        super(name, manaCost);          // delegate to Spell constructor
        this.burnDuration = burnDuration;
    }

    @Override
    public String cast() {
        return super.cast() + " Burns for " + burnDuration + " seconds!";
    }
}
```

```java
FireSpell fireball = new FireSpell("Fireball", 30, 5);
System.out.println(fireball.cast());
// Output: Fireball costs 30 mana. Burns for 5 seconds!
```

**Example 2 — Subclass adds its own methods**

```java
public class IceSpell extends Spell {
    private boolean freezes;

    public IceSpell(String name, int manaCost, boolean freezes) {
        super(name, manaCost);
        this.freezes = freezes;
    }

    @Override
    public String cast() {
        String base = super.cast();
        return freezes ? base + " Target is frozen!" : base;
    }

    // IceSpell-specific method — not on Spell
    public String thaw() {
        return name + " effect is thawing...";
    }
}
```

Notice that `thaw()` is only available on `IceSpell` references, not on a `Spell` reference pointing to an `IceSpell`. Subclasses extend the parent contract; they don't limit it.

**Example 3 — super.method() for incremental extension**

```java
public class BossSpell extends FireSpell {
    public BossSpell(String name) {
        super(name, 100, 10);
    }

    @Override
    public String cast() {
        return "[BOSS] " + super.cast() + " The arena shakes!";
    }
}
```

`super.cast()` here calls `FireSpell.cast()`, which itself called `Spell.cast()`. This chaining avoids duplicating logic at each level.

## Common Mistakes

- **Forgetting `super()` in the subclass constructor.** Java calls the no-arg parent constructor implicitly only if one exists. If the parent has no no-arg constructor, you *must* explicitly call `super(args)` — or the code won't compile.
- **Accessing `private` superclass fields directly.** Private fields are not inherited — they're invisible to the subclass. Use `protected` if subclasses need direct access, or use getters/setters.
- **Omitting `@Override`.** Without it, a typo in the method name creates a new method rather than an override, and the bug is silent. Always use `@Override`.
- **Creating deep inheritance chains.** More than two or three levels of inheritance becomes hard to reason about. Prefer shallow hierarchies.
- **Overriding without calling `super.method()` when the parent behaviour should be preserved.** If the parent does important setup, discarding it silently by not calling `super` can cause subtle bugs.

## Mental Model

Think of inheritance as a blueprint stack. The `Spell` blueprint defines what all spells have. The `FireSpell` blueprint sits on top — it takes everything from `Spell` and adds fire-specific behaviour. Each layer specialises the one below it. When you call a method, Java looks in the most specific blueprint first, then works its way up the stack until it finds the method.

## Mini Summary

- `extends` establishes an is-a relationship between a subclass and a superclass.
- `super()` delegates constructor initialisation up the chain; it must be the first statement.
- `@Override` is a compile-time safety net — always use it when overriding.
- Subclasses inherit all non-private fields and methods from their parent.
- `super.method()` lets you call the parent version of an overridden method.
- Keep inheritance hierarchies shallow — two or three levels is usually the limit before composition becomes a better choice.

# Guided Practice Quest

Follow the guided steps above to build a `Shape` hierarchy with a `Circle` and a `Rectangle`. Each step builds on the last — by the end you'll have a working inheritance chain with proper use of `super()`, `@Override`, and subclass-specific methods.

# Solo Practice Quest

Design a small `Character` hierarchy for a role-playing game. Create a `Character` superclass with `name` and `health` fields, a constructor, and an `attack()` method that returns a String. Then create two subclasses — `Mage` and `Knight` — each with at least one extra field and an overridden `attack()` method. Use `super()` in both constructors. Add comments explaining why inheritance is appropriate here (is-a relationship).

# Integration

Inheritance doesn't exist in isolation — it is the foundation of **polymorphism**, the subject of your next lesson. Once you have a `Shape` superclass and multiple concrete shapes, you can store them all in a `List<Shape>` and iterate over them, calling `getArea()` on each without knowing or caring which concrete type each element is. This is the core power that makes object-oriented systems extensible.

Inheritance also has a dark side. The moment you add a third level to your hierarchy — say `ColourfulRectangle extends Rectangle extends Shape` — changes to `Shape` ripple downward in ways that are easy to overlook. This is called the **fragile base class problem**, and it's explored in the next lesson on "Why Inheritance Fails." The discipline of OOP is knowing not just *how* to use inheritance, but *when* to stop and reach for composition or interfaces instead.

**Integration question:** If you have a `List<Spell>` containing `FireSpell`, `IceSpell`, and `BossSpell` objects, what happens when you call `cast()` on each element through the `Spell` reference? Which version of `cast()` runs, and why?

# Lore Conclusion

The Academy's cataloguing system now groups all spells under a unified `Spell` ancestor. Archivists can treat any scroll as a `Spell` when indexing the library, yet each spell retains its unique casting behaviour. The hierarchy is clean, the duplication is gone, and the next apprentice who invents a `LightningSpell` simply extends `Spell` and overrides `cast()` — no changes to the library system required. Inheritance, used with discipline, makes the system open to new types without reopening old code.
