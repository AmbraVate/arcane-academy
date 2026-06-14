---
id: se-jun-m1-06
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
lesson: interface_driven_design
title: "Interface-Driven Design"
sortOrder: 6
difficulty: 3
estimatedMinutes: 32
xpReward: 80
practiceType: JAVA
questType: PRACTICE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m1-05]
integrationDomains: [solid_principles, dependency_injection]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Designs the interface before writing the implementation"
    - "Declares all variables and parameters using the interface type"
    - "Shows how swapping one implementation for another requires no change to the caller"
    - "Explains the dependency inversion preview: high-level code depends on interface, not concrete class"
    - "Code demonstrates at least one realistic scenario where the swap is practical"
  keywords: [interface, depend, abstraction, swap, decouple, inject, contract, concrete, high-level, low-level]
  modelAnswer: |
    // Step 1: Design the interface first
    public interface NotificationService {
        void notify(String userId, String message);
    }

    // Step 2: Write implementations
    public class EmailService implements NotificationService {
        @Override
        public void notify(String userId, String message) {
            System.out.println("Email to " + userId + ": " + message);
        }
    }

    public class SmsService implements NotificationService {
        @Override
        public void notify(String userId, String message) {
            System.out.println("SMS to " + userId + ": " + message);
        }
    }

    // Step 3: High-level code depends on the interface, not the concrete class
    public class OrderService {
        private final NotificationService notificationService;

        // Inject via constructor — swappable without changing OrderService
        public OrderService(NotificationService notificationService) {
            this.notificationService = notificationService;
        }

        public void placeOrder(String userId, String item) {
            System.out.println("Order placed: " + item);
            notificationService.notify(userId, "Your order for " + item + " is confirmed.");
        }
    }
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Define a `StorageService` interface with `save(String key, String value)` and `load(String key)` methods. Don't write any implementation yet — just the contract."
    inputConfig:
      language: java
      starterCode: "public interface StorageService {\n    // define save and load\n}"
    markingRule: "Interface with void save(String, String) and String load(String) method signatures"
    hint: "Interfaces first means thinking about what callers need, not how it will work. What does a storage consumer need to do?"
    reflectionPrompt: "When you write the interface first, what forces you to think about design before implementation?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Implement `StorageService` with `InMemoryStorage` (using a HashMap internally) and `FileStorage` (just print 'Saved/Loaded from file' — no actual file IO needed)."
    inputConfig:
      language: java
      starterCode: |
        import java.util.HashMap;
        import java.util.Map;

        public class InMemoryStorage implements StorageService {
            // use a HashMap
        }

        public class FileStorage implements StorageService {
            // simulate with println
        }
    markingRule: "InMemoryStorage implements both methods using HashMap; FileStorage implements both with println simulation"
    hint: "InMemoryStorage should have a private Map<String, String> field. load() should handle the key-not-found case."
    reflectionPrompt: "If InMemoryStorage.load() returns null for a missing key and a caller assumes a non-null return, what bug could arise?"
  - id: step-3
    sortOrder: 3
    inputType: CODE
    instruction: "Write a `UserPreferencesManager` class that depends on `StorageService` via constructor injection. Add `savePreference(String key, String value)` and `getPreference(String key)` methods. Show that swapping InMemoryStorage for FileStorage requires zero changes to UserPreferencesManager."
    inputConfig:
      language: java
      starterCode: |
        public class UserPreferencesManager {
            private final StorageService storage;
            // constructor, savePreference, getPreference
        }
    markingRule: "Constructor accepts StorageService (not concrete type), methods delegate to storage, works with both implementations without change"
    hint: "The constructor parameter should be StorageService, not InMemoryStorage. This is the key move: depend on the abstraction."
    reflectionPrompt: "In a test, would you rather inject InMemoryStorage or FileStorage? Why does that matter for test speed?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the key principle of interface-driven design?"
    options:
      - "Always use the most specific concrete type for clarity"
      - "High-level classes should depend on interfaces (abstractions), not concrete implementations"
      - "Interfaces should be designed after the implementations are complete"
      - "Every class must implement at least one interface"
    correctIndex: 1
    feedback: "Interface-driven design means high-level code declares dependencies using the interface type. This decouples the caller from the implementation and makes it easy to swap, test, or extend without changing the caller."
  - type: MULTIPLE_CHOICE
    question: "Why is constructor injection preferred for providing interface dependencies?"
    options:
      - "Constructors run faster than field assignment"
      - "It makes the dependency explicit, required, and swappable from outside the class"
      - "Spring Boot only works with constructor injection"
      - "It prevents the dependency from being null"
    correctIndex: 1
    feedback: "Constructor injection makes it clear what a class needs to function (dependencies are explicit), makes them required (the object can't be created without them), and makes them swappable — perfect for testing with a different implementation."
retrieval:
  recall: "What does it mean to 'program to an interface'? Give a concrete example with a variable declaration."
  explain: "How does interface-driven design make a class easier to unit test? What specifically do you inject during testing?"
  mistakeId:
    code: |
      public class ReportGenerator {
          private EmailSender sender = new EmailSender(); // concrete type, new-ed up inside

          public void generate(String report) {
              sender.send(report);
          }
      }
    answer: "ReportGenerator creates its own EmailSender — it's tightly coupled to the concrete class. To test or swap the sender, you must change the class. Fix: accept a MessageSender interface via constructor injection, making the dependency swappable from outside."
---

# Hook

You know what interfaces are — now let's talk about how to *design* with them. Interface-driven design isn't about writing interfaces everywhere; it's a mindset shift: design the contract first, write the implementation second. When you do this, high-level classes express their needs through abstractions, not concretions. The result is code that's trivially easy to test, swap, and extend. This lesson bridges the interfaces you've learned about to the SOLID principles and dependency injection that come next.

# Lore Introduction

The Grand Architect of the Academy's infrastructure declares: "I don't care if spell components are fetched from the obsidian vault, the silver archive, or a temporary enchantment room — as long as the provider follows the `ComponentStore` contract." The construction of every tower begins with defining the contracts between its floors, not the floors themselves. Buildings designed to contracts survive renovations; those built to specific stone suppliers collapse the moment the quarry closes.

# Core Learning

## Concept Introduction

**Interface-driven design** is a practice built on three habits:

1. **Design the interface first.** Before writing a single implementation, define what the caller needs. What methods must exist? What do they take and return? Write the interface as a pure contract.

2. **Declare variables and parameters using the interface type.** `StorageService s = new InMemoryStorage()` rather than `InMemoryStorage s = new InMemoryStorage()`. The variable type is the contract; the concrete class is the choice.

3. **Inject dependencies through constructors.** Rather than a class creating its own dependencies (`new EmailSender()`), receive them from outside. The caller provides the implementation; the class uses the contract.

This is a preview of the **Dependency Inversion Principle** (SOLID's D): high-level modules should depend on abstractions, not concretions.

## Why It Matters

When `UserPreferencesManager` depends on `StorageService` (interface) rather than `InMemoryStorage` (concrete), three things become easy:

- **Testing:** Inject a simple in-memory implementation during tests — no file system, no database, no network.
- **Swapping:** Change from `InMemoryStorage` to `DatabaseStorage` by changing one line in the constructor call. `UserPreferencesManager` itself doesn't change.
- **Parallel development:** Teams can write the caller and the implementation at the same time, once the interface contract is agreed.

## Worked Examples

**Example 1 — Design interface first**

```java
// Step 1: What does the email system need from a template engine?
public interface TemplateEngine {
    String render(String templateName, Map<String, Object> variables);
}

// Step 2: Write implementations later — or in parallel:
public class MustacheTemplateEngine implements TemplateEngine {
    @Override
    public String render(String templateName, Map<String, Object> variables) {
        // real Mustache rendering logic
        return "Rendered: " + templateName;
    }
}

public class SimpleTemplateEngine implements TemplateEngine {
    @Override
    public String render(String templateName, Map<String, Object> variables) {
        // simpler substitution for testing
        return "Simple: " + templateName + " with " + variables;
    }
}
```

**Example 2 — Constructor injection, interface type**

```java
public class EmailSystem {
    private final TemplateEngine templateEngine;  // interface type!

    // Dependency injected, not created:
    public EmailSystem(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void sendWelcomeEmail(String username) {
        String body = templateEngine.render("welcome", Map.of("name", username));
        System.out.println("Sending: " + body);
    }
}

// Production:
EmailSystem prod = new EmailSystem(new MustacheTemplateEngine());

// Test:
EmailSystem test = new EmailSystem(new SimpleTemplateEngine()); // fast, no files
```

**Example 3 — Swapping implementations at the creation site only**

```java
// Version 1 — in-memory storage:
StorageService storage = new InMemoryStorage();
UserPreferencesManager mgr = new UserPreferencesManager(storage);

// Version 2 — file-based storage. Only THIS line changes:
StorageService storage = new FileStorage("/data/prefs");
UserPreferencesManager mgr = new UserPreferencesManager(storage);
// UserPreferencesManager.java: UNCHANGED.
```

The key insight: the change is localised to where the object graph is assembled (typically a factory or Spring configuration), not scattered throughout the codebase.

## Common Mistakes

- **Designing the interface as a copy of the concrete class.** If you write `InMemoryStorage` first and then extract an interface from it, you've missed the point. The interface should reflect what the *caller* needs, which may differ from what the implementation naturally provides.
- **Injecting concrete types despite having an interface.** `private final InMemoryStorage storage` defeats the purpose. Always use the interface type.
- **Creating interfaces for everything.** Single-implementation interfaces add noise. Introduce an interface when there is a real reason to swap (testing, multiple implementations, or a clear architectural boundary).
- **Passing `null` instead of a proper implementation in tests.** Null causes `NullPointerExceptions`. Use a simple test implementation or a mock framework (covered in the Testing module).
- **Using field injection (`@Autowired` on a field).** Constructor injection makes dependencies explicit and testable. Field injection hides dependencies and makes testing harder.

## Mental Model

Think of an electrical socket (interface) and an appliance plug (implementation). Your wall outlet doesn't care whether you plug in a lamp, a kettle, or a phone charger — it just provides a standard power contract. Designing to interfaces is like installing standard sockets: any compliant appliance can use them. Designing to concrete classes is like hard-wiring a specific lamp directly into your wall. Replacing it means tearing open the plasterwork.

## Mini Summary

- Design the interface (contract) before writing the implementation.
- Declare all variables and parameters using the interface type, not the concrete class.
- Inject dependencies via constructors — make them explicit and swappable.
- The concrete class choice happens at the assembly site, not inside dependent classes.
- Interface-driven design makes classes easy to test (inject a test double), swap, and extend.
- Don't create interfaces for every class — only where there's real reason to vary the implementation.

# Guided Practice Quest

Work through the steps to create a `StorageService` interface, implement it two ways, then build a `UserPreferencesManager` that uses constructor injection. Verify that swapping `InMemoryStorage` for `FileStorage` requires zero changes to `UserPreferencesManager`.

# Solo Practice Quest

Design a `ReportExporter` system interface-first. The interface: `ReportExporter` with `export(Report report)`. Implementations: `PdfExporter` and `CsvExporter`. A `ReportingService` class takes `ReportExporter` via constructor and has a `generateMonthlyReport(int month)` method. Show that `ReportingService` works with both exporters without any change to its code.

# Integration

Interface-driven design is the practical application of three SOLID principles simultaneously: Interface Segregation (small, focused interfaces), Dependency Inversion (depend on abstractions), and Open-Closed (add new implementations without modifying callers). When you reach the **Dependency Injection** pattern lesson, you'll see that Spring's entire IoC container is an automated interface-driven injection system — it reads your interface parameter types and wires in the appropriate concrete bean at startup.

The testing module will show that interface-driven design is also the key to mockability: `Mockito.mock(StorageService.class)` creates a fake implementation of the interface that you control in tests. You can't easily mock a concrete class, but you can always mock an interface.

**Integration question:** You have `UserPreferencesManager` with a `StorageService` constructor parameter. In a test, you want `load("theme")` to return `"dark"` without any actual storage. What would you pass as the `StorageService` — and what are your two options (one with a hand-written class, one with a framework)?

# Lore Conclusion

The Grand Architect's towers now stand on a foundation of contracts, not materials. The obsidian vault burned last winter — but the entire Academy switched to the silver archive in an afternoon, because every system depended on the `ComponentStore` interface, not the vault itself. When the interface holds, the system holds. That is the architect's deepest lesson: build to the contract, not the brick.
