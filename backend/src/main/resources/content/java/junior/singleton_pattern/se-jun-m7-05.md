---
id: se-jun-m7-05
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m7
moduleTitle: "Module 7: Design Patterns"
moduleGlyph: "🏗️"
moduleSortOrder: 7
topicSlug: singleton_pattern
topicTitle: "Singleton Pattern"
topicSortOrder: 5
lesson: singleton_pattern
title: "Singleton Pattern"
sortOrder: 5
difficulty: 3
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [builder_pattern]
integrationDomains: [design, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly explains why the constructor must be private"
    - "Demonstrates thread-safe singleton (double-checked locking or enum)"
    - "Explains at least two reasons Singleton is often overused or problematic"
    - "Contrasts DIY Singleton with Spring's managed singleton beans"
    - "Identifies a legitimate use case for Singleton and an illegitimate one"
  keywords: [private, static, instance, thread, synchronized, volatile, enum, Spring, bean, overused, global, testability]
  modelAnswer: |
    // Enum singleton — simplest thread-safe approach
    public enum AppConfig {
        INSTANCE;
        
        private final String environment = System.getenv("APP_ENV");
        
        public String getEnvironment() { return environment; }
    }
    
    // Usage
    String env = AppConfig.INSTANCE.getEnvironment();
    
    // Double-checked locking (when enum is not suitable)
    public class ConnectionPool {
        private static volatile ConnectionPool instance;
        
        private ConnectionPool() { }
        
        public static ConnectionPool getInstance() {
            if (instance == null) {
                synchronized (ConnectionPool.class) {
                    if (instance == null) {
                        instance = new ConnectionPool();
                    }
                }
            }
            return instance;
        }
    }
guidedSteps:
  - id: sng-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What two things must be true about a class for it to enforce the Singleton pattern correctly?
    inputConfig:
      options:
        - "The constructor must be public and the instance field must be static"
        - "The constructor must be private and a public static getInstance() method must control access"
        - "The class must be abstract and the instance must be created in a subclass"
        - "The class must implement Serializable and override readObject()"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The constructor must be private and a public static getInstance() method must control access"]
      rejectedFeedback: "A private constructor prevents external code from calling 'new ClassName()' directly. The public static getInstance() method is the only access point, and it ensures only one instance is ever created. Without the private constructor, anyone can bypass getInstance() and create a second instance."
    hint: "One modifier on the constructor, one method that controls access to the single instance."
    reflectionPrompt: "What would happen if the constructor was package-private instead of private? Is that still a true Singleton?"
  - id: sng-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In double-checked locking, the instance field must be declared ___ to ensure that changes made in one thread are immediately visible to all other threads.
    inputConfig:
      placeholder: "Java keyword"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["volatile"]
      rejectedFeedback: "The 'volatile' keyword ensures the JVM does not cache the field in a CPU register or reorder writes to it. Without volatile, double-checked locking has a race condition where a thread can see a partially-constructed object. volatile + synchronized together make DCL correctly thread-safe."
    hint: "This Java keyword ensures memory visibility across threads — no CPU caching of the field value."
    reflectionPrompt: "Why is thread safety important for a Singleton specifically? What goes wrong if two threads both check 'instance == null' at the same time?"
  - id: sng-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain two reasons why the Singleton pattern is often considered problematic and overused, particularly in testable code.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [global, test, mock, state, shared, difficult, coupling, inject, replace, hidden]
      rejectedFeedback: "Two core problems: (1) Global state — Singletons carry state that persists across tests, causing tests to affect each other. (2) Hard to mock/replace — because the instance is accessed via a static method, you can't inject a mock easily. Both problems are solved by Spring-managed beans, which are singletons by default but can be replaced in tests via dependency injection."
    hint: "Think about what happens when you run test A, then test B, and the Singleton carries state from test A into test B."
    reflectionPrompt: "If Spring manages singletons for you via its IoC container, in what situation would you still need to implement the Singleton pattern manually?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why is the enum approach to Singleton considered superior to double-checked locking?"
    options:
      - "Enums are faster at runtime"
      - "Enums are thread-safe and serialisation-safe by the JVM spec, with no extra code required"
      - "Enums allow multiple instances unlike DCL"
      - "Enums support inheritance better"
    correctIndex: 1
    feedback: "The JVM guarantees that enum constants are instantiated exactly once, thread-safely, during class loading. They are also protected against serialisation creating a second instance (a subtle bug in classic Singleton). This makes enum the simplest and most robust Singleton implementation."
  - type: MULTIPLE_CHOICE
    question: "In Spring, most beans are Singleton-scoped by default. What is the key difference between a Spring singleton bean and a hand-crafted Singleton class?"
    options:
      - "Spring singletons are faster"
      - "Spring singletons can be replaced with mocks via DI; hand-crafted ones cannot"
      - "Spring singletons are not thread-safe"
      - "Hand-crafted singletons are always better for production use"
    correctIndex: 1
    feedback: "The crucial difference is testability. A Spring bean is injected — in tests you can inject a different implementation or a mock. A hand-crafted Singleton accessed via getInstance() is a global variable that is very difficult to replace in tests. Spring gives you the 'one instance' benefit without the testing pain."
retrieval:
  recall: "Describe the private constructor + static getInstance() approach to Singleton. Why is each part necessary?"
  explain: "Explain why a Singleton with mutable state is particularly dangerous in a multi-threaded application."
  mistakeId:
    code: |
      public class Logger {
          private static Logger instance;
          
          public Logger() { }
          
          public static Logger getInstance() {
              if (instance == null) {
                  instance = new Logger();
              }
              return instance;
          }
          
          public void log(String message) { System.out.println(message); }
      }
    answer: "Two bugs: (1) The constructor is public — anyone can call 'new Logger()' and bypass getInstance(). Fix: make it private. (2) getInstance() is not thread-safe — two threads could both see instance==null simultaneously and create two Logger instances. Fix: use synchronized, double-checked locking with volatile, or the enum approach."
---

# Hook

Your application needs a configuration object — one object that holds the loaded config file, and that every part of the application reads from. Creating a new config object every time someone needs it wastes memory and could produce inconsistent state if the underlying file changes between reads. What you want is exactly one config object, shared globally.

The Singleton pattern guarantees a class has exactly one instance and provides a global access point to it. Simple enough. But Singleton is also one of the most controversial patterns in the GoF book — it is the one most likely to be abused, misused, and to cause hidden bugs in tests. Understanding both its legitimate purpose and its pitfalls is essential.

> Reflection: Think about what "global state" means. In what contexts is globally shared state helpful (e.g., application config)? In what contexts is it harmful (e.g., shared counters in tests)?

# Lore Introduction

In the Academy's Grand Library, there is exactly one Keeper of the Index — the master record of every spell, scroll, and artefact in existence. The Keeper cannot be duplicated: two Keepers with separate indexes would immediately diverge and cause chaos. When an Apprentice needs to look up a tome, they approach the Index desk and address the Keeper. There is always exactly one.

Archmage Veylan formalised this into the Singleton Rune: a binding sigil that ensures only one instance of a magical entity can exist in the Academy's metaphysical plane. Powerful — but Veylan added a warning in the margin: "Use sparingly. A Singleton is a global. Globals invite hidden dependencies and chaos in controlled experimentation."

# Core Learning

## Concept Introduction

The **Singleton pattern** ensures that a class has exactly one instance, and provides a global access point to that instance.

**Basic implementation:**

```java
public class AppConfig {
    // The single instance — held as a static field
    private static AppConfig instance;

    // Private constructor — prevents external instantiation
    private AppConfig() {
        // load configuration from file/environment
    }

    // The only access point
    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public String getDatabaseUrl() { /* ... */ return "jdbc:..."; }
}
```

This basic version is **not thread-safe**. In a multi-threaded application, two threads could both evaluate `instance == null` as true and create two instances.

**Thread-safe: Double-Checked Locking (DCL):**

```java
public class ConnectionPool {
    private static volatile ConnectionPool instance; // volatile is critical

    private ConnectionPool() { }

    public static ConnectionPool getInstance() {
        if (instance == null) {                         // first check (no lock)
            synchronized (ConnectionPool.class) {
                if (instance == null) {                 // second check (with lock)
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }
}
```

**Thread-safe: Enum Singleton (preferred):**

```java
public enum Registry {
    INSTANCE;

    private final Map<String, String> entries = new HashMap<>();

    public void register(String key, String value) { entries.put(key, value); }
    public String lookup(String key) { return entries.get(key); }
}

// Usage
Registry.INSTANCE.register("spell:fireball", "FireballSpell");
String spell = Registry.INSTANCE.lookup("spell:fireball");
```

The JVM guarantees enum constants are instantiated exactly once, thread-safely, during class loading. No `synchronized` keyword needed.

## Why It Matters

Legitimate uses of Singleton:
- Read-only application configuration loaded once at startup
- Connection pool management
- Logging infrastructure
- Registry/cache that must have exactly one source of truth

Spring's `@Component` beans are singleton-scoped by default — but they are managed by the IoC container, making them injectable and replaceable in tests.

## Worked Examples

**Spring-managed Singleton (preferred approach in modern Java):**

```java
@Component  // Spring creates exactly one instance and manages it
public class AppLogger {
    public void log(String message) {
        System.out.printf("[%s] %s%n", Instant.now(), message);
    }
}

// Injected wherever needed — can be mocked in tests
@Service
public class QuestService {
    private final AppLogger logger;

    public QuestService(AppLogger logger) {  // injected by Spring
        this.logger = logger;
    }

    public void startQuest(String name) {
        logger.log("Starting quest: " + name);
    }
}
```

The key insight: Spring provides singleton lifecycle management AND testability (you can inject a mock `AppLogger` in tests). Hand-crafted Singleton gives you the former but sacrifices the latter.

## Common Mistakes

**Public constructor.** The moment the constructor is public, Singleton is broken. External code can call `new ClassName()` and bypass `getInstance()`.

**Non-volatile instance field in DCL.** Without `volatile`, the JVM can reorder memory writes, causing threads to see a partially-constructed instance. `volatile` prevents this.

**Using Singleton for service classes.** Services with dependencies should be injected via DI, not accessed as globals. "I need this everywhere, so I'll make it a Singleton" is the pattern trap.

**Mutable singleton state.** If a Singleton holds mutable state, every thread that reads it must synchronize access. Singletons with mutable state are a concurrency nightmare.

## Mental Model

Think of a school office. There is one office, one set of records, one secretary. Every student, teacher, and parent who needs administrative information goes to the same office. The school runs correctly precisely because there is one consistent source of truth for administrative data. If each classroom had its own separate "office" with different records, administrative chaos would follow.

## Mini Summary

- Singleton ensures exactly one instance of a class, with a global access point.
- The constructor must be private; `getInstance()` is the only creation path.
- The enum approach is the simplest and most robust thread-safe Singleton.
- Double-checked locking requires `volatile` on the instance field.
- Singleton is frequently overused; Spring-managed beans provide singleton behaviour with better testability.

# Guided Practice Quest

**Quest: The Grand Library Keeper**

The Academy's Index Keeper must be a true Singleton. You must demonstrate understanding of how the pattern enforces a single instance, how thread safety is achieved, and when the pattern becomes a liability.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

The Academy's Alchemy Lab uses a `ReagentInventory` that must have exactly one instance shared across all lab stations.

Implement `ReagentInventory` using the enum Singleton approach. It should maintain a `Map<String, Integer>` of reagent name to quantity, with `addStock(String reagent, int qty)`, `useStock(String reagent, int qty)` (throws if insufficient), and `getStock(String reagent)` methods.

Then write a reflection (minimum 80 words) covering:
1. Why the enum approach is safer than DCL
2. What would go wrong if `ReagentInventory` were not a Singleton in the lab context
3. Whether this could be replaced by a Spring bean, and what that would look like

# Integration

**Connecting to Philosophy — Monism and the Single Source of Truth**

Several philosophical traditions propose that reality is ultimately one unified substance (monism). While that is a grand metaphysical claim, the practical software principle it mirrors is the "single source of truth" — the idea that a particular piece of data or state should have exactly one authoritative location. When the same information exists in two places, they will eventually diverge.

The Singleton pattern is a structural enforcement of single source of truth for a particular kind of object. The configuration object, the registry, the connection pool — these are all better as one thing than as many divergent copies. Philosophers from Spinoza to modern data architects have arrived at the same conclusion through different paths: duplication of authoritative state is the source of inconsistency.

But the Singleton's philosophical dark side is also real: Plato warned that fixating on a single, unchangeable Form prevents adaptation. A hand-crafted Singleton is fixed — you cannot replace it, adapt it, or mock it in a different context. Spring's approach — one instance per context, replaceable per context — is the more philosophically sophisticated answer.

> Reflection: Where else in software do you apply the "single source of truth" principle? What happens in practice when there are two sources of truth (e.g., two databases that should be in sync, or two config files)?

# Lore Conclusion

The Index Keeper looked up from the register as Apprentice Marcus arrived for the third time that day. "You again," she said, without irritation. She was accustomed to being the one consulted. All day, every day, by everyone who needed information. There was one Index, one Keeper, one source of truth for the Academy's knowledge.

"There is a cost to being the only one," the Keeper admitted, almost to herself, as Marcus left. "Every query comes to me. If I am wrong, everyone is wrong. If I am slow, everyone waits." She paused her writing. "That is why Archmage Veylan taught us to be singletons only when necessary — and to accept help when the container could manage the work instead."

---
