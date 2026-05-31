---
id: se-jun-m1-07
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m1
moduleTitle: "Module 1: Object-Oriented Design"
moduleGlyph: "🏛️"
moduleSortOrder: 1
topicSlug: solid_principles
topicTitle: "SOLID Principles"
topicSortOrder: 5
lesson: solid_principles
title: "SOLID Principles"
sortOrder: 7
difficulty: 3
estimatedMinutes: 35
xpReward: 80
practiceType: JAVA
questType: INVESTIGATION
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m1-05, se-jun-m1-06]
integrationDomains: [interfaces, dependency_inversion]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies which SOLID principle is violated in a given code snippet"
    - "Explains why the violation is a problem in practical maintenance terms"
    - "Provides a corrected version of the code that respects the principle"
    - "Names at least three of the five SOLID principles correctly with one-line explanations"
    - "Discusses how SOLID principles relate to testability"
  keywords: [single, open, Liskov, interface, dependency, responsibility, violation, refactor, coupling, cohesion]
  modelAnswer: |
    // SRP Violation fixed:
    // BAD: UserService handles users AND sends emails
    public class UserService {
        public void createUser(String name) { /* create */ }
        public void sendWelcomeEmail(String email) { /* send */ }
    }

    // GOOD: Single responsibility — separate concerns
    public class UserService {
        private final EmailService emailService;
        public UserService(EmailService emailService) { this.emailService = emailService; }
        public void createUser(String name, String email) {
            // create user
            emailService.sendWelcomeEmail(email);
        }
    }
    public class EmailService {
        public void sendWelcomeEmail(String email) { /* send */ }
    }
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: REFLECTION
    instruction: "Read this class: `InvoiceManager` that calculates totals, saves to database, prints to PDF, AND sends email. Identify which SOLID principle this violates and explain why it's a maintenance problem."
    inputConfig:
      minWords: 40
    markingRule: "Identifies SRP violation, explains that four responsibilities mean four reasons to change, mentions coupling to database, PDF library, and email system"
    hint: "The Single Responsibility Principle: a class should have one reason to change. How many reasons does InvoiceManager have?"
    reflectionPrompt: "If the PDF library version changes, does that have anything to do with the invoice calculation logic? Should it affect the same file?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "This code uses instanceof checks to handle shapes. Identify which SOLID principle is violated, then refactor to fix it."
    inputConfig:
      language: java
      starterCode: |
        public class AreaCalculator {
            public double calculate(Object shape) {
                if (shape instanceof Circle c) {
                    return Math.PI * c.getRadius() * c.getRadius();
                } else if (shape instanceof Rectangle r) {
                    return r.getWidth() * r.getHeight();
                }
                return 0;
            }
        }
    markingRule: "Identifies Open-Closed violation (must modify AreaCalculator to add new shapes), refactors to use a Shape interface with getArea(), AreaCalculator calls shape.getArea()"
    hint: "Each new shape type requires modifying AreaCalculator. OCP says: open for extension (add new shape), closed for modification (don't touch AreaCalculator)."
    reflectionPrompt: "After the refactor, how many files do you change to add a Triangle shape?"
  - id: step-3
    sortOrder: 3
    inputType: REFLECTION
    instruction: "Describe in your own words the Interface Segregation Principle and give an example of a 'fat interface' violation. What two interfaces would you split it into?"
    inputConfig:
      minWords: 50
    markingRule: "Correctly explains ISP: clients shouldn't be forced to depend on methods they don't use; gives a fat interface example; splits into two focused interfaces"
    hint: "Imagine a Printer interface with print(), scan(), fax(), staple(). A basic printer only needs print(). Should it have to implement fax()?"
    reflectionPrompt: "What does a class forced to implement methods it doesn't need typically do with those methods? What problem does that create?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The Open-Closed Principle says software entities should be:"
    options:
      - "Open for modification, closed for extension"
      - "Open for extension, closed for modification"
      - "Open for both extension and modification"
      - "Closed for both extension and modification"
    correctIndex: 1
    feedback: "OCP means: you should be able to add new behaviour (open for extension) without modifying existing code (closed for modification). Typically achieved via interfaces and polymorphism — add a new implementing class rather than editing the existing class."
  - type: MULTIPLE_CHOICE
    question: "The Interface Segregation Principle primarily addresses:"
    options:
      - "The problem of using too many interfaces in one class"
      - "Clients being forced to depend on methods they don't use"
      - "Interfaces having too many default methods"
      - "The cost of implementing interfaces at runtime"
    correctIndex: 1
    feedback: "ISP says: don't force a class to implement methods it doesn't need. A 'fat interface' with 20 methods is usually doing too many things. Split it into smaller, focused interfaces that clients implement only what they need."
retrieval:
  recall: "Name all five SOLID principles and give a one-sentence description of each."
  explain: "How do the Single Responsibility Principle and the Interface Segregation Principle relate to each other? What is the common underlying concern?"
  mistakeId:
    code: |
      public class DataExporter {
          public void export(String format) {
              if (format.equals("CSV")) {
                  // CSV logic
              } else if (format.equals("JSON")) {
                  // JSON logic
              } else if (format.equals("XML")) {
                  // XML logic
              }
          }
      }
    answer: "Violates OCP: adding a new format requires modifying DataExporter. Fix: create an Exporter interface with export(), implement CsvExporter, JsonExporter, XmlExporter. DataExporter accepts an Exporter and delegates — adding a new format just means a new class."
---

# Hook

Every principle has a name until it's tested by a real maintenance crisis. SOLID is the set of five principles that guard against the most common ways codebases rot: classes that do too much, code that breaks when extended, hierarchies that violate contracts, interfaces that force useless methods, and high-level logic tied to low-level details. Each principle is a specific, actionable lesson learned from decades of painful refactoring. This lesson walks through all five with Java code that shows both the violation and the fix.

# Lore Introduction

The Academy's code archives hold five ancient tablets — the SOLID Codices — each describing a law of maintainable enchantment. Artificers who ignore the first tablet end up with spell components that know too much about each other; those who ignore the second find their libraries collapse every time a new spell school is added. The five laws aren't restrictions — they're the grammar of sustainable magical engineering. Learn them not to recite, but to feel when a violation is occurring.

# Core Learning

## Concept Introduction

**S — Single Responsibility Principle (SRP)**
A class should have only one reason to change. "Reason to change" means one business concern or stakeholder. A class that handles HTTP routing, database persistence, and PDF generation has three reasons to change — and one change can unintentionally break the others.

**O — Open-Closed Principle (OCP)**
Classes should be open for extension and closed for modification. Add new behaviour by adding new code (new class, new implementation), not by editing existing stable code. Achieved primarily through interfaces and polymorphism.

**L — Liskov Substitution Principle (LSP)**
Subtypes must be substitutable for their base types without breaking correctness. If code works with a `Shape`, it must work equally correctly with any `Circle` or `Rectangle`. (You saw this in the "Why Inheritance Fails" lesson.)

**I — Interface Segregation Principle (ISP)**
Clients should not be forced to depend on methods they don't use. A "fat" interface with 20 methods should be split into smaller, focused interfaces. Implementing classes only sign up for what they actually support.

**D — Dependency Inversion Principle (DIP)**
High-level modules should not depend on low-level modules. Both should depend on abstractions. An `OrderService` should depend on a `PaymentGateway` interface, not a `StripePaymentGateway` class.

## Why It Matters

SOLID violations predictably produce the same symptoms: a change in one class breaks unrelated classes (SRP), adding a feature requires editing ten files (OCP/ISP), tests require setting up an entire framework just to test one method (DIP), or a subclass throws exceptions for inherited methods (LSP). Following SOLID doesn't mean perfect code — it means code that resists rotting as requirements change over time.

## Worked Examples

**SRP — Violation and fix**

```java
// VIOLATION: Too many reasons to change
public class UserManager {
    public void createUser(String name, String email) { /* DB logic */ }
    public void sendWelcomeEmail(String email) { /* Email logic */ }
    public void generateUserReport() { /* PDF logic */ }
}

// FIX: Each class has one responsibility
public class UserRepository {
    public void save(User user) { /* DB only */ }
}
public class EmailService {
    public void sendWelcomeEmail(String email) { /* Email only */ }
}
public class UserReportGenerator {
    public void generate(User user) { /* PDF only */ }
}
```

**OCP — Violation and fix**

```java
// VIOLATION: Adding a shape requires editing this class
public double getArea(Object shape) {
    if (shape instanceof Circle c) return Math.PI * c.getRadius() * c.getRadius();
    if (shape instanceof Square s) return s.getSide() * s.getSide();
    return 0;
}

// FIX: New shapes just implement the interface
public interface Shape {
    double getArea();
}
public class Circle implements Shape {
    @Override public double getArea() { return Math.PI * radius * radius; }
}
// Adding Triangle = new class only, zero changes to existing code
```

**ISP — Violation and fix**

```java
// VIOLATION: Fat interface forces BasicPrinter to implement scan() and fax()
public interface Office {
    void print(Document d);
    void scan(Document d);
    void fax(Document d);
}

public class BasicPrinter implements Office {
    @Override public void print(Document d) { /* real */ }
    @Override public void scan(Document d) { throw new UnsupportedOperationException(); } // forced!
    @Override public void fax(Document d) { throw new UnsupportedOperationException(); } // forced!
}

// FIX: Segregated interfaces
public interface Printer { void print(Document d); }
public interface Scanner { void scan(Document d); }
public interface Fax { void fax(Document d); }

public class BasicPrinter implements Printer { /* only print() */ }
public class AllInOnePrinter implements Printer, Scanner, Fax { /* all three */ }
```

**DIP — Violation and fix**

```java
// VIOLATION: High-level OrderService tied to low-level MySQLDatabase
public class OrderService {
    private MySQLDatabase db = new MySQLDatabase(); // concrete dep, hard to test!
    public void placeOrder(Order o) { db.save(o); }
}

// FIX: Depend on abstraction
public interface OrderRepository {
    void save(Order order);
}
public class OrderService {
    private final OrderRepository repository; // abstraction
    public OrderService(OrderRepository repository) { this.repository = repository; }
    public void placeOrder(Order o) { repository.save(o); }
}
// Test: inject InMemoryOrderRepository. Production: inject JpaOrderRepository.
```

## Common Mistakes

- **Treating SRP as "one method per class."** That's too extreme. SRP means one cohesive business responsibility, not one method. A `UserRepository` with `save()`, `findById()`, and `delete()` is fine — they're all data access concerns.
- **Creating an interface for every class to satisfy DIP.** DIP is about architectural boundaries, not every class. Don't create `UserServiceInterface` just for DIP compliance; do it when there's a real reason to swap.
- **Confusing OCP with "never modify code."** OCP says don't modify stable, tested code to add new behaviour. You can absolutely fix bugs by modifying existing code.
- **Writing ISP interfaces so small they have one method each (over-segregation).** ISP prevents fat interfaces, not useful groupings. A `Repository<T>` with `save()`, `findById()`, and `delete()` is fine — those belong together.
- **Learning SOLID as rules to follow, not problems to solve.** Each principle addresses a real pain. Apply them when you feel the pain, not as a ritual.

## Mental Model

Think of SOLID as five traffic laws. Each law exists because without it, a specific accident keeps happening. SRP prevents collisions between unrelated changes. OCP prevents road closures every time you add a new vehicle type. LSP prevents unexpected behaviour when you substitute one vehicle for another. ISP prevents drivers being forced to use controls they don't need. DIP prevents highways from being physically attached to specific petrol stations. The laws are independent but reinforce each other.

## Mini Summary

- **S** — One reason to change; one cohesive responsibility per class.
- **O** — Add new behaviour via new classes/implementations; don't edit stable existing code.
- **L** — Subtypes must honour the supertype's contract everywhere it's used.
- **I** — Split fat interfaces; clients only depend on methods they actually use.
- **D** — High-level code depends on abstractions (interfaces); concretions are injected from outside.
- Violations produce predictable symptoms: tight coupling, brittle tests, change amplification.

# Guided Practice Quest

Work through the investigation steps: analyse a God class for SRP violations, refactor the `AreaCalculator` to respect OCP, and articulate the Interface Segregation Principle with a concrete fat interface example.

# Solo Practice Quest

You're given a `ReportSystem` class that reads data from MySQL, formats it as HTML, emails it, and logs to a file — all in one class. Identify all SOLID violations. Then refactor it into a correct design: name each new class/interface, explain which principle each addresses, and show at least the interface definitions and class signatures in Java code.

# Integration

SOLID principles are not just academic — they're the underlying rationale for almost every architectural decision in Spring Boot. Spring's `@Service`, `@Repository`, `@Controller` annotations enforce SRP by separating layers. `@Autowired` and constructor injection deliver DIP. Spring Data JPA's `CrudRepository` interface is ISP in action. When you learn design patterns in Module 7, you'll find that almost every pattern is a concrete application of one or more SOLID principles: Strategy applies OCP and DIP, Observer applies OCP and SRP, Factory applies OCP.

**Integration question:** A `UserService` with constructor `UserService(UserRepository repo, EmailService email, AuditLogger audit)` — does this respect SRP? What's the maximum number of constructor parameters you'd expect in a class that properly respects SRP, and why?

# Lore Conclusion

The five SOLID tablets have guarded the Academy's codebase for two centuries. When the Great Refactoring came — where half the magical infrastructure had to be replaced overnight — the cost was five new classes, not five hundred changes. Classes that depended on abstractions rather than specific artefacts simply received new implementations. The halls that had respected the single-responsibility tablet required only their own stone, not their neighbours'. SOLID doesn't prevent change; it limits the blast radius of it.
