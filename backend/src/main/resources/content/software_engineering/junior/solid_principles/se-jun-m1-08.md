---
id: se-jun-m1-08
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m1
moduleTitle: "Module 1: Object-Oriented Design"
moduleGlyph: "🏛️"
moduleSortOrder: 1
topicSlug: solid_principles
topicTitle: "SOLID Principles"
topicSortOrder: 5
lesson: dependency_inversion
title: "Dependency Inversion"
sortOrder: 8
difficulty: 3
estimatedMinutes: 32
xpReward: 80
practiceType: JAVA
questType: PRACTICE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m1-06, se-jun-m1-07]
integrationDomains: [dependency_injection, interfaces]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies a concrete dependency and replaces it with an interface abstraction"
    - "Uses constructor injection to provide the dependency from outside"
    - "Demonstrates the high-level class working with at least two different implementations"
    - "Explains why the design is now easier to test"
    - "Uses correct Java syntax with interface type on the field and parameter"
  keywords: [inversion, abstraction, injection, constructor, high-level, low-level, interface, decouple, testable, IoC]
  modelAnswer: |
    // Abstraction
    public interface MessageSender {
        void send(String recipient, String message);
    }

    // Low-level details
    public class SmtpEmailSender implements MessageSender {
        @Override
        public void send(String recipient, String message) {
            System.out.println("SMTP email to " + recipient + ": " + message);
        }
    }

    public class MockMessageSender implements MessageSender {
        public String lastRecipient;
        public String lastMessage;
        @Override
        public void send(String recipient, String message) {
            lastRecipient = recipient;
            lastMessage = message;
        }
    }

    // High-level module — depends on abstraction only
    public class NotificationService {
        private final MessageSender sender;
        public NotificationService(MessageSender sender) { this.sender = sender; }
        public void alertUser(String userId, String alert) {
            sender.send(userId, "[ALERT] " + alert);
        }
    }
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Extract an abstraction. Given this class that creates its own database connection, identify the problem and create a `UserRepository` interface that `SqlUserRepository` implements."
    inputConfig:
      language: java
      starterCode: |
        // This is the problem:
        public class UserService {
            private SqlDatabase db = new SqlDatabase("jdbc:mysql://localhost/app");

            public User findUser(int id) {
                return db.query("SELECT * FROM users WHERE id = " + id);
            }
        }

        // Create:
        public interface UserRepository {
            // what methods does UserService actually need?
        }

        public class SqlUserRepository implements UserRepository {
            // implement using SqlDatabase
        }
    markingRule: "UserRepository interface with findById or findUser method; SqlUserRepository implements it with the db logic; interface type used"
    hint: "The interface should expose what the caller (UserService) needs, not what SqlDatabase provides. Think from the caller's perspective."
    reflectionPrompt: "After this refactor, does UserService need to know anything about SQL or JDBC? What has been inverted?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Update `UserService` to accept `UserRepository` via constructor injection. Remove the `new SqlDatabase()` line entirely."
    inputConfig:
      language: java
      starterCode: |
        public class UserService {
            private final UserRepository userRepository;
            // constructor injection here
            public User findUser(int id) {
                // delegate to repository
            }
        }
    markingRule: "Constructor accepts UserRepository (interface type), field is final, findUser delegates to repository, no SqlDatabase reference in UserService"
    hint: "The constructor parameter type should be UserRepository (the interface), not SqlUserRepository (the concrete class)."
    reflectionPrompt: "Could you now use UserService in a test without a real database? What would you inject?"
  - id: step-3
    sortOrder: 3
    inputType: CODE
    instruction: "Write an `InMemoryUserRepository` for tests. It stores users in a HashMap. Show that UserService works with both SqlUserRepository and InMemoryUserRepository."
    inputConfig:
      language: java
      starterCode: |
        import java.util.HashMap;
        import java.util.Map;

        public class InMemoryUserRepository implements UserRepository {
            private Map<Integer, User> store = new HashMap<>();
            // implement required interface methods
        }

        // Show instantiation with both implementations:
    markingRule: "InMemoryUserRepository implements UserRepository using HashMap; both UserService instantiations shown with different repos"
    hint: "For the test repo, just use a HashMap<Integer, User>. Add a helper method like addUser(User u) for seeding test data."
    reflectionPrompt: "The test with InMemoryUserRepository runs in milliseconds and needs no database running. How does this affect your ability to run tests in CI/CD pipelines?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The Dependency Inversion Principle says high-level modules should:"
    options:
      - "Always create their own dependencies using the new keyword"
      - "Depend on abstractions (interfaces), not concretions (concrete classes)"
      - "Be inherited by low-level modules"
      - "Avoid all dependencies entirely"
    correctIndex: 1
    feedback: "DIP says both high-level and low-level modules should depend on abstractions. High-level code (OrderService) should depend on a repository interface, not a specific MySQL implementation. The concrete class is injected from outside."
  - type: MULTIPLE_CHOICE
    question: "What is 'Inversion of Control' (IoC)?"
    options:
      - "Running code in reverse order"
      - "A framework taking control of object creation and dependency wiring instead of the class doing it itself"
      - "Inverting all if-else conditions for clarity"
      - "Reversing the class hierarchy so subclasses become superclasses"
    correctIndex: 1
    feedback: "IoC inverts the traditional flow where a class creates its own dependencies. Instead, an external framework or caller creates and injects them. Spring's IoC container is the most famous Java example — it creates beans and wires their dependencies automatically."
retrieval:
  recall: "What is the difference between Dependency Inversion Principle (DIP) and Dependency Injection (DI)? Are they the same thing?"
  explain: "Why does using `new ConcreteClass()` inside a class to create a dependency violate DIP, even if the field is typed to an interface?"
  mistakeId:
    code: |
      public class ReportService {
          private final PdfGenerator generator;

          public ReportService() {
              this.generator = new PdfGenerator(); // tight coupling!
          }
      }
    answer: "The constructor creates its own PdfGenerator — ReportService is coupled to PdfGenerator's existence and construction. Extract an interface (e.g., ReportGenerator), use constructor injection: `public ReportService(ReportGenerator generator)`. Now you can inject any implementation, including a test double."
---

# Hook

You know interfaces and you know SOLID. Now let's close the loop on the most architectural of the five principles: Dependency Inversion. The name sounds abstract but the implementation is concrete and repeatable: extract an interface, inject via constructor, never use `new ConcreteClass()` inside a high-level class. This one change — applied consistently — is what makes Spring's IoC container possible, what makes unit testing actually fast, and what separates code you can evolve from code you can only abandon.

# Lore Introduction

The Academy's supply chain once had the Grand Library directly ordering parchment from Thornwick's Paper Mill. When Thornwick burned, the entire Library halted for a week — no parchment, no knowledge. Now the Library declares a need through the `SupplyManifest` contract: "We need parchment meeting these specifications." The Procurement Guild sources it from whoever fulfils the contract. The Library doesn't know or care which mill ships the parchment. The dependency is inverted: the Library depends on an abstract contract; the concrete mill depends on meeting it.

# Core Learning

## Concept Introduction

The **Dependency Inversion Principle** has two parts:

1. **High-level modules should not depend on low-level modules.** Both should depend on abstractions.
2. **Abstractions should not depend on details.** Details (concrete classes) should depend on abstractions (interfaces).

**High-level module** = business logic (e.g., `OrderService`, `ReportGenerator`)  
**Low-level module** = implementation details (e.g., `MySQLDatabase`, `SmtpEmailSender`, `PdfRenderer`)

The "inversion" refers to traditional dependency structure: in naive code, high-level code creates and uses low-level code directly. DIP inverts this — the high-level code owns the interface, and the low-level code implements it to serve the high-level code's needs.

**DIP vs Dependency Injection:**  
- DIP is a *design principle* — a rule about how to structure dependencies.
- Dependency Injection (DI) is a *technique* for delivering those dependencies from outside.
- IoC (Inversion of Control) is the broader *pattern* where a framework (like Spring) manages object creation and wiring.

## Why It Matters

When `OrderService` creates `new MySQLDatabase()` internally, it is coupled to MySQL. Changing to PostgreSQL requires editing `OrderService`. Testing requires a real MySQL instance. DIP breaks this: `OrderService` depends only on `OrderRepository` (interface). MySQL, PostgreSQL, or an in-memory fake — all implement the same interface, injected from outside. The result: `OrderService` is testable without a database, deployable against any data store, and changeable without touching its own code.

## Worked Examples

**Example 1 — The violation**

```java
// BAD: OrderService tightly coupled to MySQL
public class OrderService {
    private MySQLOrderRepository repo = new MySQLOrderRepository(); // tied to concrete!

    public void placeOrder(Order order) {
        // business logic...
        repo.save(order);  // can't test this without MySQL running
    }
}
```

**Example 2 — DIP applied**

```java
// Step 1: High-level module OWNS the abstraction
public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(long id);
}

// Step 2: Low-level module depends on the abstraction (implements it)
public class MySQLOrderRepository implements OrderRepository {
    @Override
    public void save(Order order) { /* MySQL-specific code */ }
    @Override
    public Optional<Order> findById(long id) { /* MySQL query */ }
}

// Step 3: High-level module receives dependency from outside
public class OrderService {
    private final OrderRepository repository;  // interface type!

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public void placeOrder(Order order) {
        // business logic...
        repository.save(order);
    }
}
```

**Example 3 — Swapping for tests**

```java
// Test double — fast, no database:
public class InMemoryOrderRepository implements OrderRepository {
    private final Map<Long, Order> store = new HashMap<>();

    @Override
    public void save(Order order) { store.put(order.getId(), order); }

    @Override
    public Optional<Order> findById(long id) { return Optional.ofNullable(store.get(id)); }
}

// Unit test — zero infrastructure needed:
@Test
void should_save_order_when_placed() {
    InMemoryOrderRepository repo = new InMemoryOrderRepository();
    OrderService service = new OrderService(repo);

    service.placeOrder(new Order(1L, "Book"));

    assertTrue(repo.findById(1L).isPresent());
}

// Production wiring:
OrderRepository repo = new MySQLOrderRepository(dataSource);
OrderService service = new OrderService(repo);
// Or with Spring: Spring injects MySQLOrderRepository automatically.
```

## Common Mistakes

- **Using field injection (`@Autowired` on fields) instead of constructor injection.** Field injection hides dependencies, makes them non-final, and requires reflection to populate — harder to understand and test. Always use constructor injection.
- **Creating the concrete dependency inside the constructor.** `this.repo = new MySQLOrderRepository()` inside the constructor still violates DIP — the class is still coupled to the concrete class. The dependency must be *received*, not created.
- **Applying DIP to every class indiscriminately.** Not every class needs an interface. Simple value objects (`Money`, `Address`) don't need interfaces. Apply DIP at architectural boundaries — service-to-repository, service-to-external-API, controller-to-service.
- **Naming the interface after the implementation.** `MySQLOrderRepositoryInterface` is not an abstraction — it's just a copy of the class name. The interface should be named for what it *represents*: `OrderRepository`.
- **Forgetting that DIP benefits test speed.** The main daily benefit is fast unit tests. A test suite that runs in 3 seconds beats one that takes 3 minutes because it requires a real database.

## Mental Model

Imagine a universal power socket (the interface). Every country has its own plug type (concrete class), but all appliances connect through the same socket specification. The appliance (high-level) doesn't care what country's power grid (low-level) it's running on — it just requires the standard socket interface. The socket specification was designed by the appliance maker, not the power grid. That's the "inversion": the high-level consumer owns the contract; the low-level supplier implements it.

## Mini Summary

- High-level modules depend on interfaces (abstractions), not concrete implementations.
- The interface is owned by the high-level module — it defines what it needs.
- Concrete implementations are provided from outside via constructor injection.
- This makes high-level classes testable (inject a test double), swappable (inject a different implementation), and change-resistant (implementations change without touching the high-level class).
- DIP (principle) + Dependency Injection (technique) + IoC container (framework) are three separate but related ideas.
- Apply DIP at real architectural boundaries — not every class needs an abstraction.

# Guided Practice Quest

Work through the three steps: extract a `UserRepository` interface from a class with a hard-coded database, update `UserService` to use constructor injection, then write an `InMemoryUserRepository` for fast testing. By the end, `UserService` tests require zero infrastructure.

# Solo Practice Quest

A `NotificationService` currently creates `new SmtpEmailSender()` inside its constructor. Refactor it: create a `MessageSender` interface, keep `SmtpEmailSender` as one implementation, add an `SmsMessageSender`, and write a `LoggingMessageSender` for tests that records what was sent. Show `NotificationService` working with all three implementations via constructor injection.

# Integration

Dependency Inversion is the architectural principle that makes **Spring Boot** possible. Spring's IoC container is a sophisticated dependency injector: you declare your classes, annotate their interfaces with `@Repository`, `@Service`, or `@Component`, and Spring assembles the dependency graph for you. `@Autowired` on a constructor triggers Spring to find a bean that implements the required interface and inject it — DIP automated at scale.

In the **Mocking** lesson in Module 6, you'll see that `Mockito.mock(OrderRepository.class)` creates a programmable fake implementation of an interface in one line. This only works because `OrderService` depends on the interface, not the concrete class. Every testable service in your career will follow this pattern.

**Integration question:** Spring creates a single `MySQLOrderRepository` bean and injects it into every `OrderService` instance. What term describes a dependency that is only created once and shared? Why might that be important for database connections?

# Lore Conclusion

The Library's dependency on the Thornwick Paper Mill is now ancient history. The Procurement Guild sources parchment from three different mills, a recycling cooperative, and even an enchanted self-renewing scroll supplier — all because the Library's `SupplyManifest` interface remains stable. When the enchanted supplier delivered parchment 40% faster last quarter, the Library didn't change a single scroll of its operational procedures. The dependency had been inverted: the Library commands the contract; the world obeys it.
