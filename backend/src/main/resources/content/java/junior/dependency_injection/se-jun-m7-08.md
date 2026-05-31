---
id: se-jun-m7-08
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m7
moduleTitle: "Module 7: Design Patterns"
moduleGlyph: "🏗️"
moduleSortOrder: 7
topicSlug: dependency_injection
topicTitle: "Dependency Injection"
topicSortOrder: 8
lesson: dependency_injection
title: "Dependency Injection"
sortOrder: 8
difficulty: 3
estimatedMinutes: 30
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [adapter_pattern]
integrationDomains: [design, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly explains the Dependency Inversion Principle and its relationship to DI"
    - "Demonstrates constructor injection with a concrete Java example"
    - "Explains why field injection is discouraged (hides dependencies, harder to test)"
    - "Describes how the Spring IoC container resolves and injects dependencies"
    - "Shows how DI improves testability by enabling mock injection"
  keywords: [constructor, inject, interface, inversion, IoC, Spring, mock, test, decouple, depend, abstract, field, setter]
  modelAnswer: |
    // Dependency Inversion: depend on abstraction
    public interface QuestRepository {
        Optional<Quest> findById(Long id);
        void save(Quest quest);
    }
    
    // Constructor injection (preferred)
    @Service
    public class QuestService {
        private final QuestRepository questRepository;
        private final NotificationService notifications;
        
        // Dependencies declared explicitly — visible, immutable, testable
        public QuestService(QuestRepository questRepository,
                            NotificationService notifications) {
            this.questRepository = questRepository;
            this.notifications   = notifications;
        }
        
        public void completeQuest(Long id) {
            Quest quest = questRepository.findById(id)
                .orElseThrow(() -> new QuestNotFoundException(id));
            quest.complete();
            questRepository.save(quest);
            notifications.send("Quest completed: " + quest.getTitle());
        }
    }
    
    // In tests: inject mocks
    QuestRepository mockRepo = mock(QuestRepository.class);
    NotificationService mockNotify = mock(NotificationService.class);
    QuestService service = new QuestService(mockRepo, mockNotify);
guidedSteps:
  - id: di-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the Dependency Inversion Principle (DIP)?
    inputConfig:
      options:
        - "High-level modules should depend on low-level modules directly"
        - "High-level modules should depend on abstractions, not concretions; abstractions should not depend on details"
        - "Dependencies should be created by the class that needs them"
        - "Every class should have at most one dependency"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["High-level modules should depend on abstractions, not concretions; abstractions should not depend on details"]
      rejectedFeedback: "DIP states that high-level policy code (business logic) should not depend on low-level detail code (database implementations, HTTP clients). Both should depend on abstractions (interfaces). This prevents business logic from being coupled to infrastructure decisions."
    hint: "Think about what 'inversion' means here — what gets flipped? Who depends on whom?"
    reflectionPrompt: "If QuestService directly imports and instantiates JpaQuestRepository, what problems does that create for testing and future change?"
  - id: di-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which type of injection is generally preferred in modern Spring applications, and why?
    inputConfig:
      options:
        - "Field injection with @Autowired — simplest to write"
        - "Setter injection — most flexible"
        - "Constructor injection — dependencies are explicit, immutable, and the class is testable without a container"
        - "Method injection — most granular"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Constructor injection — dependencies are explicit, immutable, and the class is testable without a container"]
      rejectedFeedback: "Constructor injection is preferred because: dependencies are declared explicitly in the constructor (visible to any reader), they can be marked final (immutable), and you can create the class in a plain unit test without needing Spring at all — just pass the dependencies directly. Field injection (@Autowired on a private field) hides dependencies and requires the Spring container or reflection to set them."
    hint: "Which approach lets you write a unit test with 'new MyService(mockDep1, mockDep2)' without a Spring context?"
    reflectionPrompt: "What would it tell you about a class if it had 7 constructor parameters? What design principle might it be violating?"
  - id: di-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain what an IoC (Inversion of Control) container does. How does it differ from a class creating its own dependencies with 'new'?
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [container, manage, create, inject, wire, Spring, resolve, register, control, inversion]
      rejectedFeedback: "An IoC container (like Spring's ApplicationContext) manages the creation and wiring of objects. Instead of a class using 'new DependencyImpl()' to create its own dependencies (control in the class), the container creates all objects and injects the right ones into each class (control inverted to the container). This is why it's called Inversion of Control — the class no longer controls its own dependency creation."
    hint: "What does 'inversion' mean — inversion of what, from who to who?"
    reflectionPrompt: "If the IoC container resolves all dependencies, what does that mean for the class itself — what responsibility has been removed from it?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the main problem with @Autowired field injection?"
    options:
      - "It is too slow at runtime"
      - "It uses too much memory"
      - "Dependencies are hidden and the class cannot be instantiated outside a Spring container without reflection"
      - "It only works with interfaces, not concrete classes"
    correctIndex: 2
    feedback: "Field injection hides dependencies — you cannot tell what a class needs by reading its constructor. You cannot instantiate it in a unit test with 'new' because the fields are private and only Spring can inject them. This makes classes harder to test and their dependencies harder to understand."
  - type: MULTIPLE_CHOICE
    question: "In Spring, what annotation marks a class as a component to be managed by the IoC container?"
    options:
      - "@Inject"
      - "@Component"
      - "@Singleton"
      - "@Bean"
    correctIndex: 1
    feedback: "@Component (and its specialisations @Service, @Repository, @Controller) marks a class for Spring component scanning. Spring creates one instance and manages it. @Bean is used in @Configuration classes for manually defined beans. @Inject is from JSR-330, not Spring-specific."
retrieval:
  recall: "Describe the three types of dependency injection (constructor, setter, field) and state which is preferred and why."
  explain: "Explain the relationship between Dependency Inversion Principle and Dependency Injection. Are they the same thing?"
  mistakeId:
    code: |
      @Service
      public class OrderService {
          @Autowired
          private OrderRepository orderRepository;
          
          @Autowired
          private EmailService emailService;
          
          public void placeOrder(Order order) {
              orderRepository.save(order);
              emailService.send(order.getCustomerEmail(), "Order placed");
          }
      }
    answer: "Field injection is used. Problems: (1) Dependencies are hidden — not visible in the constructor. (2) Fields are private and final cannot be added, so dependencies are mutable. (3) Unit tests cannot use 'new OrderService(mockRepo, mockEmail)' — they require the Spring context or reflection hacks. Fix: remove @Autowired from fields, make fields private final, add a constructor that takes both dependencies (Spring will automatically inject via the constructor)."
---

# Hook

Imagine building a `QuestService` that needs to save quests to a database. The natural approach: `private final JpaQuestRepository repo = new JpaQuestRepository()`. Done? No — now `QuestService` is hard-wired to JPA. If you want to use a different database in production, a file-based store in a prototype, or a mock in a unit test, you have to change the class itself. The class that should only care about quest business logic now cares about infrastructure details.

Dependency Injection is the solution. Instead of a class creating its own dependencies, dependencies are *provided to* the class from the outside. The class declares what it needs; something else (the IoC container) decides what to provide. This single shift produces enormous benefits in testability, flexibility, and clarity.

> Reflection: Think about what would happen if every class in a large application constructed its own dependencies. How would you test any individual class in isolation? How would you swap a database implementation?

# Lore Introduction

In the Academy, every senior Runesmith was once responsible for crafting all their own tools: enchanting their own quill, forging their own measuring rods, brewing their own focus potions. This left little time for actual rune-work, and each Runesmith used subtly different tools, making collaboration difficult.

Veylan established the Equipment Chamber: a central facility that produced and distributed all standard tools. A Runesmith arriving for work would declare their needs — "I need a precision quill and a focus potion" — and the Chamber would provide exactly the right tools. The Runesmith's control over their own tool creation was inverted. The Chamber now controlled it. And the Runesmith could finally focus entirely on runic craftsmanship.

# Core Learning

## Concept Introduction

**Dependency Injection (DI)** is a technique where a class's dependencies are provided externally rather than created internally. It is the practical application of the **Dependency Inversion Principle (DIP)** from SOLID.

**DIP states:**
1. High-level modules should not depend on low-level modules. Both should depend on abstractions.
2. Abstractions should not depend on details. Details should depend on abstractions.

**Three injection styles:**

**Constructor Injection (preferred):**
```java
@Service
public class QuestService {
    private final QuestRepository questRepository; // interface, not JPA class
    private final NotificationService notifications;

    // Spring sees one constructor and injects automatically (no @Autowired needed in Spring 5+)
    public QuestService(QuestRepository questRepository,
                        NotificationService notifications) {
        this.questRepository = questRepository;
        this.notifications   = notifications;
    }
}
```

**Setter Injection (for optional dependencies):**
```java
@Service
public class ReportService {
    private AuditLogger auditLogger; // optional

    @Autowired(required = false)
    public void setAuditLogger(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }
}
```

**Field Injection (discouraged):**
```java
@Service
public class QuestService {
    @Autowired
    private QuestRepository questRepository; // hidden dependency, hard to test
}
```

## Why It Matters

**Testability.** Constructor injection lets you write plain unit tests:
```java
QuestRepository mockRepo = mock(QuestRepository.class);
NotificationService mockNotify = mock(NotificationService.class);
QuestService service = new QuestService(mockRepo, mockNotify);
// No Spring context needed. Fast, isolated test.
```

**Decoupling.** `QuestService` depends on the `QuestRepository` interface, not `JpaQuestRepository`. You can swap implementations (JPA, in-memory, MongoDB) by changing what is injected, not the service.

**Explicitness.** Constructor injection makes dependencies visible at a glance. If a constructor has 10 parameters, that is a clear signal the class has too many responsibilities — a design smell surfaced by DI.

## Worked Examples

**Without DI — tightly coupled:**
```java
public class QuestService {
    private final JpaQuestRepository repo = new JpaQuestRepository(); // hard-wired

    public Quest findQuest(Long id) {
        return repo.findById(id).orElseThrow(); // tied to JPA forever
    }
}
```

**With DI — loosely coupled:**
```java
public interface QuestRepository {
    Optional<Quest> findById(Long id);
    void save(Quest quest);
}

@Repository
public class JpaQuestRepository implements QuestRepository {
    // JPA implementation details
}

@Service
public class QuestService {
    private final QuestRepository questRepository; // interface

    public QuestService(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }

    public Quest findQuest(Long id) {
        return questRepository.findById(id).orElseThrow();
    }
}
```

Spring sees `@Service` and `@Repository`, creates one instance of each, and wires `JpaQuestRepository` into `QuestService`'s constructor automatically.

**In tests:**
```java
@Test
void findQuest_throwsWhenNotFound() {
    QuestRepository mockRepo = mock(QuestRepository.class);
    when(mockRepo.findById(99L)).thenReturn(Optional.empty());

    QuestService service = new QuestService(mockRepo);
    assertThrows(NoSuchElementException.class, () -> service.findQuest(99L));
}
```

No database. No Spring context. Fast, deterministic, isolated.

## Common Mistakes

**Field injection in production code.** It is the most visible anti-pattern in Spring applications. Dependencies are hidden, cannot be made `final`, and require Spring/reflection to test.

**Circular dependencies.** If A depends on B and B depends on A, Spring will throw a `BeanCurrentlyInCreationException`. Solution: redesign — usually by extracting a third class.

**Injecting too many dependencies.** If a class needs 7+ injected dependencies, it likely violates Single Responsibility. Extract smaller, more focused classes.

**Using @Autowired unnecessarily.** In Spring 5+, a single-constructor class does not need `@Autowired` on the constructor. Spring injects automatically. Adding `@Autowired` is noise.

## Mental Model

Think of a restaurant kitchen. The head chef (class) does not grow their own vegetables, raise their own livestock, or manufacture their own knives. A supplier (IoC container) provides exactly what the kitchen needs each day. The chef focuses on cooking. The supplier decision (which farm, which knife brand) can change without affecting the chef's recipes. The chef's "dependencies" — ingredients and tools — are injected, not self-created.

## Mini Summary

- DI provides a class's dependencies externally rather than letting it create them with `new`.
- Constructor injection is preferred: dependencies are explicit, immutable, and testable.
- Field injection (@Autowired on fields) is discouraged: hides dependencies and breaks testability.
- Spring's IoC container manages creation and wiring of all `@Component`-annotated beans.
- DI enables mocking in tests: inject a mock instead of the real implementation.

# Guided Practice Quest

**Quest: The Equipment Chamber**

The Academy's Equipment Chamber must distribute tools to Runesmiths correctly. You must demonstrate understanding of Dependency Injection: choosing constructor injection, understanding the IoC container's role, and explaining testability improvements.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

The Academy's `GradeService` currently creates its own dependencies:

```java
public class GradeService {
    private final DatabaseGradeRepository repo = new DatabaseGradeRepository();
    private final SmtpEmailService emailer = new SmtpEmailService("smtp.academy.com");

    public void submitGrade(Long studentId, double score) {
        repo.save(studentId, score);
        if (score < 50) {
            emailer.send("tutor@academy.com", "Student " + studentId + " failed");
        }
    }
}
```

Refactor `GradeService` to use constructor injection with interfaces. Then write a reflection (minimum 100 words) covering:
1. What you changed and why
2. How a unit test for `submitGrade` looks after the refactor (no real database or SMTP required)
3. How Spring would automatically wire your service when the app starts

# Integration

**Connecting to Philosophy — Autonomy vs Structure**

Political philosophy debates the tension between individual autonomy and institutional structure. Libertarians argue that individuals should determine their own needs and acquire their own resources; institutionalists argue that centralised coordination produces better outcomes through specialisation and standardisation.

Dependency Injection resolves an analogous tension in software design. A class that creates its own dependencies is "autonomous" — fully self-contained. But autonomy at the class level comes at a cost: the class must know how to build everything it needs (coupling to infrastructure), cannot be tested in isolation (coupling to external systems), and cannot be reconfigured without modification (coupling to specific implementations).

The IoC container is the institutionalist counterargument: centralised coordination of dependency creation. Classes give up autonomy over construction in exchange for testability, flexibility, and focus. Kant's categorical imperative provides a useful check: "Act only according to that maxim by which you can at the same time will that it should become a universal law." If every class creates its own dependencies, the universal law produces untestable, inflexible systems. The IoC model universalises better.

> Reflection: Are there situations where a class should manage its own dependency creation? What criteria would you use to decide when `new` is acceptable vs when injection is required?

# Lore Conclusion

The first morning after the Equipment Chamber opened, the Runesmiths arrived at their workbenches and found their tools already laid out. Precision quills, calibrated measuring rods, the correct potions. Nothing was missing. Nothing was surplus. The Chamber had consulted each Runesmith's registered needs and provided accordingly.

Senior Runesmith Taryn sat down and immediately began work on a difficult binding rune — the kind of intricate task that previously waited until she had finished tool preparation. "The Chamber knows what I need," she said to her apprentice. "I don't have to know how it was made or where it came from. I just need it to work." Her apprentice nodded, watching her hands move with new efficiency. "That," Taryn added, "is what it means to depend on the interface, not the implementation."

---
