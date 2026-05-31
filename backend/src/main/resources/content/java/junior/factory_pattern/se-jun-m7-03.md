---
id: se-jun-m7-03
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m7
moduleTitle: "Module 7: Design Patterns"
moduleGlyph: "🏗️"
moduleSortOrder: 7
topicSlug: factory_pattern
topicTitle: "Factory Pattern"
topicSortOrder: 3
lesson: factory_pattern
title: "Factory Pattern"
sortOrder: 3
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [strategy_pattern]
integrationDomains: [design, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly distinguishes Factory Method (subclass decides) from Abstract Factory (family of objects)"
    - "Shows a concrete Java example of hiding 'new' behind a factory method"
    - "Explains how the factory decouples the client from concrete types"
    - "Identifies at least one context where a factory adds unnecessary complexity"
    - "Uses correct OO terminology: interface, concrete class, factory method, client"
  keywords: [factory, interface, decouple, concrete, client, creation, hide, new, abstract, polymorphism]
  modelAnswer: |
    // Factory Method example: notification factory
    public interface Notification {
        void send(String message);
    }
    
    public class EmailNotification implements Notification {
        public void send(String message) {
            System.out.println("Email: " + message);
        }
    }
    
    public class SmsNotification implements Notification {
        public void send(String message) {
            System.out.println("SMS: " + message);
        }
    }
    
    public class NotificationFactory {
        public static Notification create(String type) {
            return switch (type) {
                case "email" -> new EmailNotification();
                case "sms"   -> new SmsNotification();
                default      -> throw new IllegalArgumentException("Unknown type: " + type);
            };
        }
    }
    
    // Client code — no 'new' keyword, no knowledge of concrete classes
    Notification n = NotificationFactory.create("email");
    n.send("Your quest is ready!");
guidedSteps:
  - id: fp-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which statement best describes the core purpose of the Factory pattern?
    inputConfig:
      options:
        - "To guarantee only one instance of a class is created"
        - "To hide the 'new' keyword from client code, decoupling it from concrete types"
        - "To define a family of interchangeable algorithms"
        - "To convert one interface into another"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["To hide the 'new' keyword from client code, decoupling it from concrete types"]
      rejectedFeedback: "The Factory pattern's core job is creation encapsulation — the client asks for an object without knowing which concrete class will be instantiated. Option A describes Singleton, C describes Strategy, and D describes Adapter."
    hint: "What keyword does the Factory pattern hide from the calling code?"
    reflectionPrompt: "Why is it valuable for the client to not know which concrete class it is using?"
  - id: fp-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In the Factory Method pattern, the decision about which concrete class to instantiate is made by a ___ class or method, not the client.
    inputConfig:
      placeholder: "one word"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["factory", "creator", "subclass"]
      rejectedFeedback: "The factory (or creator subclass in the classic GoF formulation) is responsible for deciding which concrete product to create. The client only calls the factory method and receives the interface type it needs."
    hint: "The pattern is named after this entity."
    reflectionPrompt: "How does centralising object creation in one place improve maintainability when new types are added?"
  - id: fp-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the difference between Factory Method and Abstract Factory patterns. Give a brief example of when you would choose one over the other.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [method, abstract, family, subclass, related, product, single, multiple]
      rejectedFeedback: "Factory Method uses one method (often in a subclass) to create one product. Abstract Factory creates a family of related products through an interface with multiple creation methods. Use Factory Method for one object type; use Abstract Factory when multiple related objects must be created together (e.g., all UI widgets must belong to the same theme)."
    hint: "Think: one product type vs a coordinated family of product types."
    reflectionPrompt: "Can you think of a UI toolkit (light theme vs dark theme) and how Abstract Factory would ensure all components match?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A client calls NotificationFactory.create('push') and receives a PushNotification object typed as Notification. What does the client know about PushNotification?"
    options:
      - "Everything — it has full access to PushNotification's concrete methods"
      - "Only what the Notification interface exposes"
      - "Nothing at all — it cannot call any methods"
      - "The implementation details of how push is sent"
    correctIndex: 1
    feedback: "The client receives a Notification reference. It only knows what the Notification interface declares. The concrete type is hidden. This is the decoupling the factory provides."
  - type: MULTIPLE_CHOICE
    question: "When does a factory method add unnecessary complexity?"
    options:
      - "When there are multiple concrete types that need to be created"
      - "When the creation logic is non-trivial"
      - "When only one concrete type exists and no new types are anticipated"
      - "When the client needs to be decoupled from creation"
    correctIndex: 2
    feedback: "If there is only ever one concrete implementation and no reason to expect others, a factory adds an extra layer of indirection for no benefit. Patterns should solve real, present problems."
retrieval:
  recall: "Describe the three roles in the Factory Method pattern: Product interface, ConcreteProduct, and Creator/Factory."
  explain: "How does a factory decouple client code from concrete classes? What specific coupling does it remove?"
  mistakeId:
    code: |
      public class OrderService {
          public void process(String type) {
              if (type.equals("email")) {
                  EmailNotification n = new EmailNotification();
                  n.send("Order confirmed");
              } else if (type.equals("sms")) {
                  SmsNotification n = new SmsNotification();
                  n.send("Order confirmed");
              }
          }
      }
    answer: "OrderService is coupled to concrete notification classes. Adding 'push' requires modifying OrderService. Fix: introduce a NotificationFactory and a Notification interface. OrderService calls NotificationFactory.create(type).send('Order confirmed') — it becomes unaware of concrete types and closed to modification when new types are added."
---

# Hook

You are building the Academy's notification system. At first, there is only one channel: email. So you write `new EmailNotification()` directly in your service. Then push notifications are added. Then SMS. Then in-app alerts. Your service class now contains four blocks of `if-else` creation logic, each knowing about a different concrete class. The service that was supposed to handle *business logic* has become entangled with *creation logic*.

This is the problem the Factory pattern solves. The Factory pattern hides `new` behind a dedicated creation mechanism. The client code no longer says "give me a `CreditCardPayment`" — it says "give me something that can process a payment". The factory decides what "something" is. The client never knows.

> Reflection: Have you ever found `new ConcreteClass()` scattered throughout a codebase and wished there was a single place to control what gets created? What problems did that scattering cause?

# Lore Introduction

The Academy's Enchantment Registry handles requests from all guilds. Each guild needs different types of enchanted items: the Warriors need blades, the Healers need chalices, the Scribes need ink-quills. In the old days, every request required the registrar to know the exact crafting ritual for every item type. The registry was a mess of scrolls and conditional annotations.

Archmage Veylan established the Enchantment Forge — a factory that accepted item type requests and produced the appropriate enchanted object, shielding requesters from the specifics of each crafting ritual. "Guild representatives," Veylan decreed, "need only know what they asked for, not how it was made."

# Core Learning

## Concept Introduction

The **Factory pattern** (in its various forms) provides an interface for creating objects without specifying their exact concrete class. The client depends on an abstraction (interface or abstract type) rather than a concrete `new` expression.

**Factory Method** — a single method (usually static, or defined in a creator class/subclass) that creates one product type:

```java
public interface Notification {
    void send(String message);
}

public class EmailNotification implements Notification {
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

public class SmsNotification implements Notification {
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

public class PushNotification implements Notification {
    public void send(String message) {
        System.out.println("Push: " + message);
    }
}

// The factory — single place where 'new' is used
public class NotificationFactory {
    public static Notification create(String type) {
        return switch (type) {
            case "email" -> new EmailNotification();
            case "sms"   -> new SmsNotification();
            case "push"  -> new PushNotification();
            default      -> throw new IllegalArgumentException("Unknown notification type: " + type);
        };
    }
}
```

**Abstract Factory** — an interface with multiple factory methods that create a *family* of related objects:

```java
public interface UIFactory {
    Button createButton();
    TextField createTextField();
}

public class LightThemeFactory implements UIFactory {
    public Button createButton() { return new LightButton(); }
    public TextField createTextField() { return new LightTextField(); }
}

public class DarkThemeFactory implements UIFactory {
    public Button createButton() { return new DarkButton(); }
    public TextField createTextField() { return new DarkTextField(); }
}
```

The Abstract Factory ensures all created objects are consistent (all light-themed, or all dark-themed).

## Why It Matters

**Decoupling.** Client code that calls `NotificationFactory.create("email")` has no `import` on `EmailNotification`. It does not know the class exists. This means you can rename, replace, or add notification types without touching any client code.

**Single Responsibility.** Object creation is a responsibility. Moving it to a factory keeps business logic classes focused on business logic.

**Open/Closed.** Adding a new notification type means adding a new class and one line in the factory — no client changes.

## Worked Examples

Without factory (tightly coupled):

```java
public class OrderService {
    public void confirmOrder(String channel) {
        if (channel.equals("email")) {
            new EmailNotification().send("Order confirmed!");
        } else if (channel.equals("sms")) {
            new SmsNotification().send("Order confirmed!");
        }
        // Add push? Add another else-if here AND everywhere else
    }
}
```

With factory (decoupled):

```java
public class OrderService {
    public void confirmOrder(String channel) {
        Notification notification = NotificationFactory.create(channel);
        notification.send("Order confirmed!");
        // Adding push? Zero changes here.
    }
}
```

## Common Mistakes

**Making the factory too simple or too complex.** A factory of one creates one concrete type forever is pointless. A factory that contains complex business logic is a different problem (that belongs elsewhere).

**Forgetting to handle unknown types gracefully.** The factory should throw a clear, descriptive exception for unrecognised types, not silently return null.

**Confusing Factory Method with Abstract Factory.** Factory Method produces *one type* of product; Abstract Factory produces a *consistent family* of related products.

## Mental Model

Think of a restaurant kitchen. Diners (clients) order "a main course" — they don't specify the cooking technique or the precise ingredients. The kitchen (factory) decides exactly what to make and how, based on the order. The diner receives a plate of food and eats it. They don't need to know what happened in the kitchen.

## Mini Summary

- Factory patterns hide `new` from client code, decoupling the client from concrete types.
- Factory Method creates one product type; Abstract Factory creates a consistent family of products.
- The client only knows the product interface, never the concrete class.
- Factories centralise creation logic, making it easy to add new types without modifying clients.
- Factories are overkill when only one concrete type exists and variation is not anticipated.

# Guided Practice Quest

**Quest: The Enchantment Registry**

The Academy registry receives requests for different enchanted items. You must demonstrate that a factory can produce the correct item without the requesting guild knowing how it was made.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

The Academy's transport system needs to book different types of travel: `BroomTravel`, `PortalTravel`, and `DragonTravel`. Each has a `book(String destination)` method.

Design a `TravelFactory` with a static `create(String type)` method. Then write a `TravelService` that books travel using only the factory — no `new` keyword in the service class. Write a reflection (minimum 100 words) covering:
1. What coupling was removed
2. How the service would change (or not change) if `WarpTravel` were added
3. Whether this situation calls for Factory Method or Abstract Factory, and why

# Integration

**Connecting to Philosophy — Plato's Theory of Forms**

Plato argued that the physical objects we see are imperfect copies of perfect abstract "Forms". A specific chair in a room is an instance of the ideal Form of "Chair". You perceive the physical chair, but you interact with the concept of chairness — the properties that make something a chair — rather than needing to understand the details of its construction.

The Factory pattern embodies this philosophical distinction. The `Notification` interface is the Form — the ideal concept of "something that can send a message". `EmailNotification` and `SmsNotification` are the imperfect physical instances. Client code interacts only with the Form (the interface). The factory bridges the gap between the abstract Form and the concrete instance, deciding which imperfect copy best fits the current situation.

This is not just an analogy. The Dependency Inversion Principle — "depend on abstractions, not concretions" — is a direct software engineering expression of Plato's insight: the higher a component is in a system, the more it should interact with abstract Forms rather than specific instantiations.

> Reflection: In what other areas of software design do you interact with "Forms" (interfaces, abstractions) rather than the physical instances behind them? Why does this make systems more robust?

# Lore Conclusion

The Registry hummed with efficiency. Guild representatives arrived, stated their need — "a blade", "a chalice", "an ink-quill" — and left within minutes, enchanted item in hand. None of them had witnessed the inner workings of the Forge. None of them needed to.

When a new item type was added — a teleportation crystal — the Runesmith simply added a new ritual to the Forge's registry. The guilds noticed nothing changed. They continued to arrive, state a need, and leave satisfied. The Forge adapted silently. That, Archmage Veylan noted in his journal, is the mark of a well-designed creation system.

---
