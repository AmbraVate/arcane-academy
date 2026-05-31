---
id: se-jun-m3-04
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m3
moduleTitle: "Module 3: Exception Handling"
moduleGlyph: "⚠️"
moduleSortOrder: 3
topicSlug: error_strategies
topicTitle: "Error Strategies"
topicSortOrder: 4
lesson: error_strategies
title: "Error Strategies"
sortOrder: 4
difficulty: 3
estimatedMinutes: 30
xpReward: 80
practiceType: JAVA
questType: INVESTIGATION
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m3-03]
integrationDomains: [crud_apis, unit_tests]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Distinguishes fail-fast (throw immediately on invalid input) from fail-safe (continue with defaults)"
    - "Uses Optional<T> to represent a value that may or may not be present, avoiding null returns"
    - "Chains Optional methods: map, filter, orElse, orElseThrow"
    - "Applies defensive programming by validating preconditions at method entry"
    - "Adds contextual information when logging an error (what failed, with what input)"
  keywords: [fail-fast, fail-safe, Optional, orElse, orElseThrow, map, filter, defensive programming, null safety, precondition, logging, context]
  modelAnswer: |
    import java.util.*;

    // Fail-fast: throw immediately
    public void enroll(String wizardId) {
        Objects.requireNonNull(wizardId, "wizardId must not be null");
        // ...
    }

    // Optional: no null returns
    public Optional<Spell> findSpell(String name) {
        return spells.stream()
            .filter(s -> s.getName().equals(name))
            .findFirst();
    }

    // Caller uses Optional safely
    findSpell("Fireball")
        .map(Spell::getPower)
        .orElse(0);

    findSpell("Fireball")
        .orElseThrow(() -> new SpellNotFoundException("Fireball"));
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Refactor a method that returns null when a spell is not found to return Optional<Spell> instead. Show both the refactored method and two callers: one that uses orElse() for a default, one that uses orElseThrow()."
    inputConfig:
      language: java
      starterCode: |
        import java.util.*;
        record Spell(String name, int power) {}

        // BEFORE (returns null — bad):
        public static Spell findSpell(List<Spell> spells, String name) {
            for (Spell s : spells) {
                if (s.name().equals(name)) return s;
            }
            return null; // caller might forget to null-check
        }

        // AFTER: return Optional<Spell>
        public static Optional<Spell> findSpellSafe(List<Spell> spells, String name) {
            // implement using stream or loop
        }

        // Caller 1: use orElse(defaultSpell)
        // Caller 2: use orElseThrow(() -> new RuntimeException(...))
    markingRule: "Method returns Optional.of(spell) when found or Optional.empty() when not, caller 1 uses .orElse(default), caller 2 uses .orElseThrow with exception lambda"
    hint: "return spells.stream().filter(s -> s.name().equals(name)).findFirst(); — this returns an Optional<Spell>."
    reflectionPrompt: "When would you prefer Optional over throwing an exception for a missing item? When would you prefer throwing?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Apply defensive programming: write a UserService.createUser(String name, int age) method that validates all preconditions using Objects.requireNonNull and explicit checks, throwing descriptive IllegalArgumentExceptions before any business logic runs."
    inputConfig:
      language: java
      starterCode: |
        import java.util.*;
        public class UserService {
            public User createUser(String name, int age) {
                // 1. name must not be null
                // 2. name must not be blank
                // 3. age must be between 0 and 120
                // Only after all checks: create and return user
                return new User(name, age);
            }
        }
        record User(String name, int age) {}
    markingRule: "Uses Objects.requireNonNull or explicit null check for name, checks name.isBlank(), checks age bounds with IllegalArgumentException, all checks precede business logic"
    hint: "Objects.requireNonNull(name, \"name must not be null\"); is idiomatic. Follow with explicit checks for blank and age range."
    reflectionPrompt: "Why is fail-fast (validate all inputs at the start) better than fail-later (let operations fail when they encounter bad data)?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does Optional.orElseThrow(() -> new SpellNotFoundException(\"x\")) do when the Optional is empty?"
    options:
      - "Returns null"
      - "Returns a default Spell object"
      - "Throws the SpellNotFoundException"
      - "Prints a warning and continues"
    correctIndex: 2
    feedback: "orElseThrow() returns the value if present, or invokes the supplier to throw the exception if empty. It is the preferred pattern when absence is an error condition — the method always returns a value or throws, never null."
  - type: MULTIPLE_CHOICE
    question: "Which approach is 'fail-fast'?"
    options:
      - "Returning null when a lookup fails so the caller can check"
      - "Catching all exceptions and continuing with a default value"
      - "Validating preconditions at method entry and throwing immediately on violation"
      - "Waiting until an NPE occurs naturally and logging it"
    correctIndex: 2
    feedback: "Fail-fast means detecting and reporting errors as early as possible — at the method boundary, before any processing begins. This produces clear, immediate error messages at the source rather than confusing failures deep in the call chain. The alternative — letting bad input propagate — is fail-slow and makes debugging much harder."
retrieval:
  recall: "What does Optional.empty() return, and how does it differ from returning null?"
  explain: "Explain the fail-fast principle and give one concrete example of applying it in a method."
  mistakeId:
    code: |
      public String getWizardName(int id) {
          Wizard wizard = wizardRepository.findById(id);
          if (wizard != null) {
              return wizard.getName();
          }
          return null; // caller must null-check... or NPE
      }
    answer: "Returning null forces every caller to null-check, and if they forget, they get a NullPointerException far from the source. Fix: return Optional<String> — `return Optional.ofNullable(wizard).map(Wizard::getName);`. Or throw if absence is an error: `if (wizard == null) throw new WizardNotFoundException(id);`"
---

# Hook

Exceptions tell you when something broke. Error strategies determine what happens next. Should you throw immediately on bad input or proceed cautiously? Return null to signal absence or use `Optional`? Log the error with context or swallow it silently? These choices determine whether your system fails clearly at the source or produces mysterious symptoms three layers later. This lesson covers the four core strategies: fail-fast, fail-safe, `Optional<T>`, and defensive programming.

# Lore Introduction

Two wizards built identical spell-lookup services. The first returned null when a spell was not found. Six months later, NullPointerExceptions appeared randomly across the codebase wherever callers forgot to check. The second used `Optional<Spell>` — callers were forced by the type system to handle the "not found" case. The first wizard's service had thirty separate null-check bugs. The second had zero. Strategy is not style — it is the difference between a system that fails clearly at the source and one that fails mysteriously three calls later.

# Core Learning

## Concept Introduction

**Fail-Fast:**
Detect and throw at the earliest possible point. Validate all preconditions at method entry.
```java
Objects.requireNonNull(value, "value must not be null");
if (value.isBlank()) throw new IllegalArgumentException("value must not be blank");
```
- Errors surface immediately with clear messages
- Simplifies debugging (stack trace points directly to the bad input)
- Preferred for programming errors and invalid input

**Fail-Safe:**
Continue operation with a safe default rather than throwing.
```java
return value != null ? value.toUpperCase() : "";
```
- Appropriate when absence is expected and a sensible default exists
- Used for optional features, defaults, graceful degradation
- Dangerous when used to mask real errors

**Optional\<T\>:**
A container for a value that may or may not be present. Eliminates null returns.
```java
Optional<Spell> findSpell(String name) { ... }
// Caller cannot use the value without handling the empty case
```

| Method | Behaviour |
|---|---|
| `.get()` | Returns value or throws if empty (avoid — use orElse/orElseThrow) |
| `.orElse(default)` | Returns value or the default |
| `.orElseGet(Supplier)` | Returns value or calls supplier (lazy) |
| `.orElseThrow(Supplier)` | Returns value or throws supplied exception |
| `.map(Function)` | Transform if present, empty if absent |
| `.filter(Predicate)` | Keep if predicate true, empty otherwise |
| `.isPresent()` / `.isEmpty()` | Check presence (use sparingly) |

**Defensive Programming:**
Validate all inputs at method boundaries. Assume nothing about caller behaviour.
```java
public void process(String input) {
    Objects.requireNonNull(input, "input must not be null");
    if (input.length() > MAX_LENGTH) {
        throw new IllegalArgumentException(
            "input exceeds max length " + MAX_LENGTH + ": " + input.length());
    }
    // proceed with confidence
}
```

**Logging with context:**
When logging an error, include: what operation failed, with what input, the exception type and message.
```java
log.error("Failed to load spell [name={}]: {}", spellName, e.getMessage(), e);
```

## Why It Matters

Null is Java's billion-dollar mistake (as Tony Hoare called it). Every null return is a potential NullPointerException waiting to happen, one forgotten null-check away. `Optional` makes absence a type-safe, visible concept instead of an invisible trap. Fail-fast validates assumptions early, so when something goes wrong you get a clear message at the source rather than a confusing NPE three calls deep. Together, these strategies make code that fails clearly, at the right place, with the right message.

## Worked Examples

**Example 1 — Optional instead of null**

```java
import java.util.*;

public class SpellRegistry {
    private final Map<String, Spell> spells = new HashMap<>();

    // Never returns null — caller must handle Optional
    public Optional<Spell> find(String name) {
        return Optional.ofNullable(spells.get(name));
    }
}

// Caller: get with default
Spell spell = registry.find("Fireball").orElse(Spell.DEFAULT);

// Caller: throw if not found (absence is an error)
Spell required = registry.find("Fireball")
    .orElseThrow(() -> new SpellNotFoundException("Fireball"));

// Caller: transform if present
int power = registry.find("Fireball")
    .map(Spell::getPower)
    .orElse(0);
```

**Example 2 — Fail-fast with Objects.requireNonNull**

```java
import java.util.*;

public class EnrollmentService {
    public void enroll(Wizard wizard, Course course) {
        Objects.requireNonNull(wizard, "wizard must not be null");
        Objects.requireNonNull(course, "course must not be null");
        if (course.isFull()) {
            throw new CourseFullException(course.getName(), course.getMaxCapacity());
        }
        // preconditions met — safe to proceed
        wizard.addCourse(course);
    }
}
```

**Example 3 — Optional chaining**

```java
// Without Optional (null-unsafe):
User user = userService.find(id);
if (user != null) {
    Address address = user.getAddress();
    if (address != null) {
        String city = address.getCity();
        System.out.println(city);
    }
}

// With Optional (explicit, type-safe):
userService.findOptional(id)
    .map(User::getAddress)
    .map(Address::getCity)
    .ifPresent(System.out::println);
```

## Common Mistakes

- **Returning `null` from methods.** Always use `Optional<T>` for values that might not be present, so callers are forced to handle absence.
- **Calling `optional.get()` without checking presence.** `get()` throws `NoSuchElementException` if empty. Use `orElse()`, `orElseThrow()`, or `ifPresent()` instead.
- **Using Optional as a field type.** `Optional` is designed for return types only — not for fields or constructor parameters. Use null for field representation and `Optional` in the method API.
- **Fail-safe when fail-fast is appropriate.** Returning a default instead of throwing hides errors that callers need to know about. Only use fail-safe when absence is genuinely expected.
- **Logging without context.** `log.error("Error occurred")` is useless. Include the operation, input values, and the exception.

## Mental Model

Think of `Optional` as a gift box. The box is always there — you do not get a null pointer when you receive it. But the box might be empty. Before using what is inside, you must decide: what if the box is empty? Use the default? Throw an error? Do nothing? The box forces you to make that decision at compile time rather than discovering you forgot at runtime. Fail-fast is the practice of checking who handed you the box before you even try to open it.

## Mini Summary

- Fail-fast: validate preconditions at method entry and throw immediately — errors surface at the source.
- Fail-safe: continue with a safe default — only when absence is genuinely expected.
- `Optional<T>`: type-safe representation of a value that might not exist — replaces null returns.
- `orElse()` for defaults, `orElseThrow()` when absence is an error, `map()` to transform.
- `Objects.requireNonNull(param, message)` is idiomatic fail-fast validation.
- Always log with context: what, which input, what exception.

# Guided Practice Quest

Complete the two steps: refactor a null-returning method to return `Optional<Spell>` with `orElse` and `orElseThrow` callers, then apply defensive programming to a `createUser` method with full precondition validation.

# Solo Practice Quest

Build a `QuestService` class. Implement `findQuest(String questId)` returning `Optional<Quest>`, `assignQuest(Wizard wizard, String questId)` that fails fast on null wizard and null questId, uses `findQuest` with `orElseThrow` (throw `QuestNotFoundException`), and validates the wizard is not already on a quest (throw `IllegalStateException`). Implement `getQuestReward(String questId)` that returns `Optional<Integer>` by chaining `findQuest()` and mapping to reward. Write a `main` method demonstrating all three methods with valid and invalid input, showing the `Optional` chaining pattern.

# Integration

Error strategies are applied throughout the entire stack. In **CRUD APIs**, your Spring controllers receive user input and must fail-fast if it is invalid — Spring's `@Valid` annotation automates precondition checks. In **ORMs**, Spring Data's `findById()` returns `Optional<T>` — you will use `orElseThrow()` there. In **Testing**, you will test that your fail-fast code throws the right exception, and that your `Optional`-returning methods return empty when appropriate. In production, good logging with context is the difference between a five-minute and a five-hour incident response.

**Integration question:** Spring Data JPA's `findById(Long id)` returns `Optional<T>`. Write a service method `getWizard(Long id)` that uses this Optional correctly — throwing `WizardNotFoundException` if absent, and returning the wizard if present.

# Lore Conclusion

The spell-lookup service that returned Optional never produced a NullPointerException. The service that returned null generated thirty bugs over six months. The difference was not talent — it was strategy. Fail-fast validation, Optional returns, and contextual logging are not advanced techniques. They are the baseline habits that separate systems that fail clearly from systems that fail mysteriously. Adopt them early, and your future self will spend debugging time on real problems rather than chasing null references.
