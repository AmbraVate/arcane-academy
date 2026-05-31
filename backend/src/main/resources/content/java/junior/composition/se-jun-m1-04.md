---
id: se-jun-m1-04
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m1
moduleTitle: "Module 1: Object-Oriented Design"
moduleGlyph: "🏛️"
moduleSortOrder: 1
topicSlug: composition
topicTitle: "Composition"
topicSortOrder: 3
lesson: composition_over_inheritance
title: "Composition over Inheritance"
sortOrder: 4
difficulty: 3
estimatedMinutes: 32
xpReward: 70
practiceType: JAVA
questType: INVESTIGATION
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m1-01, se-jun-m1-02]
integrationDomains: [interfaces, solid_principles]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies the has-a relationship in the scenario and correctly models it with composition"
    - "Creates a composed class that holds a reference to another class as a field"
    - "Delegates behaviour to the composed object rather than duplicating logic"
    - "Explains in comments why composition is more flexible than inheritance here"
    - "Code compiles and demonstrates the delegated behaviour working correctly"
  keywords: [composition, delegation, has-a, field, flexibility, coupling, interface, inject, dependency, reuse]
  modelAnswer: |
    // Composition: Robot HAS-A locomotion strategy, not IS-A particular locomotion
    public interface Locomotion {
        String move();
    }

    public class WheelDrive implements Locomotion {
        @Override public String move() { return "Rolling on wheels"; }
    }

    public class LegDrive implements Locomotion {
        @Override public String move() { return "Walking on legs"; }
    }

    public class Robot {
        private final String name;
        private final Locomotion locomotion; // composed via field

        public Robot(String name, Locomotion locomotion) {
            this.name = name;
            this.locomotion = locomotion;
        }

        public String act() {
            return name + ": " + locomotion.move(); // delegation
        }
    }

    // Switching locomotion requires no Robot subclassing:
    Robot r1 = new Robot("R2D2", new WheelDrive());
    Robot r2 = new Robot("C3PO", new LegDrive());
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: REFLECTION
    instruction: "Examine this hierarchy: FlyingDuck extends Duck extends Bird. Now add Penguin (can't fly) and RubberDuck (not alive). List what breaks and what the class hierarchy forces on you."
    inputConfig:
      minWords: 50
    markingRule: "Identifies that Penguin shouldn't inherit fly(), RubberDuck breaks the living bird assumption, hierarchy forces unneeded methods onto subclasses"
    hint: "What methods does Bird have that Penguin or RubberDuck shouldn't? What would you have to do with those methods?"
    reflectionPrompt: "Could you model these as interfaces (Flyable, Swimmable) instead? What would that buy you?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Refactor the Duck hierarchy to use composition. Create a `FlyBehaviour` interface with `fly()` and two implementations: `FlyWithWings` and `NoFly`. Give `Duck` a `FlyBehaviour` field and a `performFly()` method that delegates to it."
    inputConfig:
      language: java
      starterCode: |
        public interface FlyBehaviour {
            String fly();
        }
        // Implement FlyWithWings and NoFly, then update Duck:
        public class Duck {
            private String name;
            private FlyBehaviour flyBehaviour;
            // constructor, performFly(), getter/setter for flyBehaviour
        }
    markingRule: "FlyBehaviour interface with fly(), FlyWithWings returns flying string, NoFly returns can't fly, Duck holds FlyBehaviour field, performFly() delegates"
    hint: "Duck holds a FlyBehaviour. When you call duck.performFly(), it delegates to flyBehaviour.fly(). The Duck class itself doesn't know or care which implementation it has."
    reflectionPrompt: "Can you change a duck's fly behaviour at runtime using this design? Could you do that with inheritance?"
  - id: step-3
    sortOrder: 3
    inputType: REFLECTION
    instruction: "Describe one real-world scenario from your experience (or imagine one) where you would choose composition over inheritance. Explain both the has-a relationship and why inheritance would be problematic."
    inputConfig:
      minWords: 60
    markingRule: "Clear has-a relationship identified, explains why inheritance would create tight coupling or hierarchy problems, composition described as delegating to a composed object"
    hint: "Think about things that can have different behaviours that might change independently — a Car with different Engine types, a User with different authentication methods."
    reflectionPrompt: "Does your example involve behaviours that could change at runtime? If so, composition is almost certainly better."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In composition, how does a class access the behaviour it needs?"
    options:
      - "It extends a class that has the behaviour"
      - "It holds a reference to an object that has the behaviour and delegates method calls to it"
      - "It copies the source code of the other class's methods"
      - "It uses static methods from a utility class"
    correctIndex: 1
    feedback: "Composition means 'has-a': the class holds a field (reference) of another type and calls methods on it. This is delegation — the class passes the work to the composed object."
  - type: MULTIPLE_CHOICE
    question: "What is the main flexibility advantage of composition over inheritance for behaviour?"
    options:
      - "Composed classes compile faster"
      - "You can change the composed behaviour object at runtime without creating a new subclass"
      - "Composition avoids the need for interfaces"
      - "Composed classes are always smaller in terms of lines of code"
    correctIndex: 1
    feedback: "Because the behaviour is held as a field, you can replace it at runtime (or inject a different implementation via the constructor). With inheritance, changing behaviour requires a different subclass — you can't change it once the object is created."
retrieval:
  recall: "What is the difference between an is-a relationship and a has-a relationship? Give one example of each."
  explain: "Explain delegation in the context of composition. When a Duck calls performFly(), what actually executes the flying logic and why?"
  mistakeId:
    code: |
      public class Logger extends ArrayList<String> {
          public void log(String message) {
              add("[LOG] " + message);
          }
      }
    answer: "Logger extends ArrayList is an is-a claim: a Logger IS an ArrayList. But that exposes all ArrayList methods (remove, clear, subList) on Logger, which is unintended. Use composition: Logger has-a List<String> as a private field and only exposes log() and getMessages()."
---

# Hook

Imagine designing a character in a game. They can fly, swim, cast spells, and ride a mount. With inheritance, you'd need `FlyingSwimmingSpellcastingMountedCharacter extends Character`. What happens when you add a fourth ability? Or want a character who can fly but not swim? Inheritance hierarchies explode with combinations. Composition solves this cleanly: a character **has** a set of abilities, and each ability is independently swappable. "Favour composition over inheritance" is one of the two most cited principles from the Gang of Four book — and you're about to understand exactly why.

# Lore Introduction

The Academy once tried to model every wizard type through inheritance: `FireWizard`, `IceWizard`, `FireIceWizard`, `FireIceHealingWizard`... The hierarchy reached nine levels before it collapsed under its own weight. A new approach was adopted: every wizard **has** a spellbook, a robe type, and a combat stance. These can be changed independently and combined freely. A single `Wizard` class composes its behaviour from interchangeable components. The hierarchy of nine became a flat composition of three. Clarity restored.

# Core Learning

## Concept Introduction

**Composition** means building a class by holding references to other objects (components) as fields and delegating behaviour to them. Instead of *inheriting* a method, a class *calls* a method on a composed object.

- **Is-a** relationship → inheritance (`Dog extends Animal`)
- **Has-a** relationship → composition (`Car has-a Engine`)

**Delegation** is the mechanism: when a composed class needs to do something, it calls a method on its component rather than implementing the logic itself.

Key advantages over inheritance:
1. **Flexibility at runtime** — you can swap the component object without creating a new subclass.
2. **No fragile base class** — the composed class isn't coupled to any parent's internals.
3. **Mix-and-match** — a class can hold multiple independent components, combining behaviours freely.
4. **Single Responsibility** — each component does one thing; the composing class just coordinates.

## Why It Matters

The guideline "favour composition over inheritance" appears in the Gang of Four (GoF) book, *Effective Java*, and countless design principles because it consistently produces more maintainable systems. When a feature request arrives — "now wizards can also be mounted" — composition just adds a new field. Inheritance would demand a new subclass or a painful restructure. Code built with composition is easier to test (inject mock components), easier to extend (add a new component type), and easier to understand (each component is self-contained).

## Worked Examples

**Example 1 — The classic duck example (from GoF / Head First Design Patterns)**

```java
// Behaviour interface
public interface FlyBehaviour {
    String fly();
}

// Two implementations
public class FlyWithWings implements FlyBehaviour {
    @Override
    public String fly() { return "Flying with wings!"; }
}

public class NoFly implements FlyBehaviour {
    @Override
    public String fly() { return "Cannot fly."; }
}

// Duck composes its behaviour
public class Duck {
    private final String name;
    private FlyBehaviour flyBehaviour;

    public Duck(String name, FlyBehaviour flyBehaviour) {
        this.name = name;
        this.flyBehaviour = flyBehaviour;
    }

    // Delegation: Duck doesn't implement flying itself
    public String performFly() {
        return name + ": " + flyBehaviour.fly();
    }

    // Behaviour can change at runtime!
    public void setFlyBehaviour(FlyBehaviour fb) {
        this.flyBehaviour = fb;
    }
}

Duck mallard = new Duck("Mallard", new FlyWithWings());
Duck rubber = new Duck("Rubber", new NoFly());

System.out.println(mallard.performFly()); // Mallard: Flying with wings!
System.out.println(rubber.performFly());  // Rubber: Cannot fly.

// Change behaviour at runtime — impossible with inheritance:
mallard.setFlyBehaviour(new NoFly());
System.out.println(mallard.performFly()); // Mallard: Cannot fly.
```

**Example 2 — Composition fixing the logger example**

```java
// WRONG: Logger extends ArrayList — exposes all ArrayList internals
public class BadLogger extends ArrayList<String> {
    public void log(String msg) { add("[LOG] " + msg); }
}

// RIGHT: Logger HAS-A list — only exposes what it should
public class Logger {
    private final List<String> entries = new ArrayList<>();

    public void log(String msg) {
        entries.add("[LOG] " + msg);
    }

    public List<String> getEntries() {
        return Collections.unmodifiableList(entries);
    }
    // clear(), remove(), subList() are NOT exposed. Logger controls its contract.
}
```

**Example 3 — Multiple composed behaviours**

```java
public interface AttackBehaviour {
    String attack();
}

public interface DefenceBehaviour {
    String defend();
}

public class SwordAttack implements AttackBehaviour {
    @Override public String attack() { return "Slashes with sword!"; }
}

public class ShieldDefence implements DefenceBehaviour {
    @Override public String defend() { return "Blocks with shield!"; }
}

public class Warrior {
    private final AttackBehaviour attack;
    private final DefenceBehaviour defence;

    public Warrior(AttackBehaviour attack, DefenceBehaviour defence) {
        this.attack = attack;
        this.defence = defence;
    }

    public String fight() {
        return attack.attack() + " " + defence.defend();
    }
}

Warrior knight = new Warrior(new SwordAttack(), new ShieldDefence());
```

Adding a `BowAttack` implementation doesn't change `Warrior` at all.

## Common Mistakes

- **Using composition when inheritance is genuinely correct.** If the is-a relationship is stable and LSP holds, inheritance is fine. Don't compose everything just because you've learned the principle.
- **Creating a composed class that leaks its components publicly.** `public FlyBehaviour getFlyBehaviour()` allows callers to call `duck.getFlyBehaviour().fly()` directly — breaking encapsulation. The composed class should expose meaningful domain methods (`performFly()`), not component references.
- **Deep delegation chains.** If `A` calls `B` which calls `C` which calls `D`, the chain is probably too indirect. Consider whether C and D should be composed directly into A.
- **Forgetting to inject the component.** Hard-coding `private FlyBehaviour flyBehaviour = new FlyWithWings()` inside the constructor removes the flexibility that made composition valuable. Inject it from outside.
- **Recreating inheritance with composition by making every component method public.** If every method on the component is delegated one-to-one, you've just replicated inheritance without the type relationship.

## Mental Model

A car doesn't extend an engine — it has one. You can swap in a petrol engine, an electric motor, or a hybrid without changing what a car fundamentally is. The car delegates "provide power" to the engine, "play music" to the audio system, "navigate" to the GPS. Each component is independently replaceable. That's composition: a coordinator with swappable parts, each responsible for one thing.

## Mini Summary

- Composition is a has-a relationship: the class holds a component object as a field and delegates behaviour to it.
- Inheritance is an is-a relationship: valid only when LSP holds and the hierarchy is stable.
- Composition allows behaviour to change at runtime; inheritance fixes behaviour at compile time.
- Each component in a composition is independently replaceable and testable.
- "Favour composition over inheritance" is a GoF principle that leads to more maintainable, testable systems.
- Composition should not leak its components publicly — expose meaningful domain methods instead.

# Guided Practice Quest

Work through the investigation steps: analyse the Duck hierarchy problem, refactor it to use a `FlyBehaviour` interface with delegation, and then articulate a real-world example where composition beats inheritance.

# Solo Practice Quest

Refactor this inheritance-based design to use composition: `TextDocument extends Document extends StoredFile`. The `Document` adds formatting, the `StoredFile` handles saving/loading. Identify the has-a relationships, create appropriate interfaces or component classes, and write the `TextDocument` class using composition and delegation. Explain in comments why this is more maintainable.

# Integration

Composition is the mechanical foundation of two upcoming design patterns: the **Strategy Pattern** (compose an interchangeable algorithm) and the **Decorator Pattern** (compose additional behaviours around an existing object). Every time Spring injects a dependency into a class via constructor injection, it is setting up composition: the class doesn't create its dependency, it receives it — making the dependency swappable without changing the class.

Understanding composition also sets you up for **SOLID Principles** — specifically the Dependency Inversion Principle. DIP says: "depend on abstractions, not concretions." When you inject a `FlyBehaviour` interface rather than a `FlyWithWings` concrete class, you're applying DIP through composition. The two ideas reinforce each other.

**Integration question:** A `Warrior` composed of `AttackBehaviour` and `DefenceBehaviour` interfaces is easier to unit test than one that extends a `BaseWarrior` class. Why? (Hint: think about what you'd need to inject vs what you'd need to mock.)

# Lore Conclusion

The Academy's new wizard design is a triumph of the composable arts. Every wizard now carries a `SpellBook`, a `CombatStance`, and an `ArmourType` — each a swappable component. When the Headmistress ordered all wizards to switch from leather armour to enchanted silk overnight, the change took three lines: one new `SilkArmour` class and one updated factory. No hierarchy was touched. The tower stands firm because its rooms are modular, not load-bearing — and that is the artisan's secret.
