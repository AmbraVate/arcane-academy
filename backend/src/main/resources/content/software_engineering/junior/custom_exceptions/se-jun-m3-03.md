---
id: se-jun-m3-03
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m3
moduleTitle: "Module 3: Exception Handling"
moduleGlyph: "⚠️"
moduleSortOrder: 3
topicSlug: custom_exceptions
topicTitle: "Custom Exceptions"
topicSortOrder: 3
lesson: custom_exceptions
title: "Custom Exceptions"
sortOrder: 3
difficulty: 3
estimatedMinutes: 25
xpReward: 70
practiceType: JAVA
questType: PRACTICE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-jun-m3-02]
integrationDomains: [error_strategies, crud_apis]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Creates a custom exception by extending RuntimeException with a message constructor"
    - "Adds a constructor that accepts both a message and a cause (Throwable)"
    - "Throws the custom exception from a domain method with a meaningful message"
    - "Catches the custom exception separately from other exceptions"
    - "Explains when a custom exception is preferable to using a built-in exception"
  keywords: [extends RuntimeException, custom exception, message constructor, cause, domain exception, throw, catch, wrapping, re-throw, meaningful message]
  modelAnswer: |
    public class InsufficientManaException extends RuntimeException {
        public InsufficientManaException(String message) {
            super(message);
        }

        public InsufficientManaException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // Usage
    public void castSpell(Spell spell, Wizard wizard) {
        if (wizard.getMana() < spell.getManaCost()) {
            throw new InsufficientManaException(
                "Cannot cast " + spell.getName() + ": requires " +
                spell.getManaCost() + " mana, wizard has " + wizard.getMana());
        }
    }
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Create an InsufficientManaException that extends RuntimeException. Add a constructor that takes a message String, and a second constructor that takes a message and a Throwable cause."
    inputConfig:
      language: java
      starterCode: |
        public class InsufficientManaException extends RuntimeException {
            // constructor: message only
            // constructor: message + cause
        }
    markingRule: "Class extends RuntimeException, first constructor calls super(message), second constructor calls super(message, cause), no other logic needed"
    hint: "Call super(message) in the message-only constructor and super(message, cause) in the second. RuntimeException provides these constructors."
    reflectionPrompt: "Why do we add the cause constructor? When would you pass a Throwable cause to a custom exception?"
  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Write a SpellCaster class with a castSpell(String spellName, int manaCost, int wizardMana) method that throws InsufficientManaException with a detailed message when wizardMana < manaCost."
    inputConfig:
      language: java
      starterCode: |
        public class SpellCaster {
            public static void castSpell(String spellName, int manaCost, int wizardMana) {
                // validate mana, throw InsufficientManaException with detail
                System.out.println("Casting: " + spellName);
            }
        }
    markingRule: "Checks wizardMana < manaCost, throws InsufficientManaException with a message that includes spell name, required mana, and available mana, normal path prints casting message"
    hint: "Include all relevant values in the message: \"Cannot cast [spell]: needs [X] mana, wizard has [Y]\""
    reflectionPrompt: "How does this custom exception help a caller compared to IllegalArgumentException with the same message?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why extend RuntimeException rather than Exception for most custom domain exceptions?"
    options:
      - "RuntimeException is faster at runtime"
      - "Extending Exception requires more constructors"
      - "Checked exceptions force all callers to declare or catch, creating boilerplate for conditions that indicate programming errors"
      - "RuntimeException is the only class that allows custom messages"
    correctIndex: 2
    feedback: "Checked exceptions (extending Exception) force every caller in the call chain to handle or declare them with throws, even when they cannot meaningfully handle the condition. For domain programming errors, RuntimeException is preferred: callers that can handle it catch it; others let it propagate without boilerplate."
  - type: MULTIPLE_CHOICE
    question: "What does the 'cause' Throwable parameter in a custom exception constructor provide?"
    options:
      - "The stack trace of the original exception is discarded"
      - "It links the custom exception to the underlying exception that triggered it, preserving the root cause"
      - "It automatically retries the operation that failed"
      - "It converts the checked exception to an unchecked one"
    correctIndex: 1
    feedback: "The cause constructor allows you to wrap a lower-level exception (e.g., a SQLException) in a domain-meaningful exception (e.g., UserStorageException) while preserving the original stack trace. Logging or debuggers can access the full chain via getCause() — critical for root cause analysis."
retrieval:
  recall: "What two constructors should every well-designed custom exception include?"
  explain: "Explain when you would create a custom exception instead of throwing IllegalArgumentException. Give a domain-specific example."
  mistakeId:
    code: |
      public class SpellException extends Exception {
          public SpellException() {}
      }

      public void castSpell(Spell spell) throws SpellException {
          if (spell == null) throw new SpellException();
      }
    answer: "Two problems: (1) extending Exception makes it checked — callers must declare throws SpellException everywhere, even when they cannot handle it. Extend RuntimeException instead. (2) The no-arg constructor provides no message, making the exception useless for debugging. Add a message constructor: `public SpellException(String message) { super(message); }`"
---

# Hook

A `NullPointerException` at line 142 tells you a reference was null. An `InsufficientManaException: Cannot cast Fireball — requires 80 mana, wizard has 20` tells you exactly what failed and why, in the language of your domain. Custom exceptions are how you turn a cryptic runtime crash into actionable, self-documenting error information. They make your API's failure modes explicit and give callers the ability to handle specific failures differently.

# Lore Introduction

The Academy's combat simulation crashed constantly with `IllegalStateException: invalid state`. The message was technically accurate and completely useless. Three different failure modes all produced the same exception: insufficient mana, the target wizard was shielded, and the spell was on cooldown. Support engineers spent half their time figuring out which condition had been violated. A senior developer created three custom exceptions — `InsufficientManaException`, `WizardShieldedException`, and `SpellOnCooldownException` — each with a message containing the relevant values. The crashes still happened occasionally, but they were diagnosed and fixed in minutes instead of hours.

# Core Learning

## Concept Introduction

**Creating a custom exception:**
```java
public class InsufficientManaException extends RuntimeException {

    // Required: message constructor
    public InsufficientManaException(String message) {
        super(message);
    }

    // Recommended: message + cause constructor
    public InsufficientManaException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

**When to create a custom exception:**
- Multiple callers need to distinguish this failure from others
- The failure has domain meaning that a generic type does not convey
- You want to carry domain-specific data with the exception (add fields)
- You are wrapping a lower-level exception (e.g., `SQLException`) in a domain exception

**When NOT to create a custom exception:**
- A standard exception already communicates the intent (`IllegalArgumentException`, `IllegalStateException`)
- The exception is only thrown and caught in one place
- You would just be renaming `RuntimeException` with no added value

**Exceptions with additional fields:**
```java
public class SpellNotFoundException extends RuntimeException {
    private final String spellId;

    public SpellNotFoundException(String spellId) {
        super("Spell not found: " + spellId);
        this.spellId = spellId;
    }

    public String getSpellId() {
        return spellId;
    }
}
```

**Wrapping with cause:**
```java
try {
    userRepository.save(user);
} catch (DataAccessException e) {
    throw new UserStorageException("Failed to save user: " + user.getId(), e);
}
```

## Why It Matters

Custom exceptions are part of your public API. When a service method throws `InsufficientManaException`, callers know exactly what happened and can respond — perhaps asking the wizard to rest, or suggesting a cheaper spell. When it throws `RuntimeException("error")`, callers cannot distinguish failure types and must handle everything the same way. In REST APIs, Spring's exception handlers use exception types to determine HTTP response codes: `SpellNotFoundException` → 404, `InsufficientManaException` → 422. The exception type IS the communication channel.

## Worked Examples

**Example 1 — Basic custom exception**

```java
public class InvalidSpellException extends RuntimeException {

    public InvalidSpellException(String message) {
        super(message);
    }

    public InvalidSpellException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Usage
public Spell lookupSpell(String name) {
    if (name == null || name.isBlank()) {
        throw new InvalidSpellException("Spell name must not be null or blank");
    }
    Spell spell = spellRepository.find(name);
    if (spell == null) {
        throw new InvalidSpellException("No spell found with name: " + name);
    }
    return spell;
}
```

**Example 2 — Exception with domain field**

```java
public class SpellOnCooldownException extends RuntimeException {
    private final int remainingSeconds;

    public SpellOnCooldownException(String spellName, int remainingSeconds) {
        super("Spell '" + spellName + "' is on cooldown for " + remainingSeconds + "s");
        this.remainingSeconds = remainingSeconds;
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }
}

// Caller can use the field:
try {
    combat.castSpell("Fireball", wizard);
} catch (SpellOnCooldownException e) {
    showMessage("Wait " + e.getRemainingSeconds() + " seconds before casting again");
}
```

**Example 3 — Wrapping a lower-level exception**

```java
public class SpellPersistenceException extends RuntimeException {
    public SpellPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

// In repository layer:
public void saveSpell(Spell spell) {
    try {
        jdbcTemplate.update(INSERT_SQL, spell.getName(), spell.getPower());
    } catch (DataAccessException e) {
        throw new SpellPersistenceException(
            "Failed to persist spell: " + spell.getName(), e); // wraps cause
    }
}
```

## Common Mistakes

- **Extending `Exception` instead of `RuntimeException`.** Checked custom exceptions force boilerplate `throws` declarations on every caller in the chain.
- **No-arg constructor only.** `throw new SpellException()` gives no context. Always include a message constructor.
- **Message without values.** `"Insufficient mana"` is unhelpful. `"Insufficient mana: required 80, available 20"` is actionable.
- **Creating a custom exception for a single use.** If you throw it in one place and catch it in one place, a standard `IllegalStateException` is sufficient.
- **Losing the cause.** When wrapping a lower-level exception, always pass it as the cause: `super(message, cause)`. Without this, the root cause stack trace is lost.

## Mental Model

A custom exception is a domain-specific error type in your vocabulary. Just as a doctor does not say "something went wrong" but "the patient has appendicitis", your code should not say `RuntimeException("error")` but `InsufficientManaException("needs 80, has 20")`. The exception type is the diagnosis; the message is the details. Together, they tell callers exactly what happened and give them the vocabulary to respond appropriately.

## Mini Summary

- Extend `RuntimeException` for custom domain exceptions (unchecked, no boilerplate).
- Always include a `(String message)` constructor that calls `super(message)`.
- Include a `(String message, Throwable cause)` constructor for wrapping lower-level exceptions.
- Put meaningful values in messages — include what was expected and what was found.
- Add fields for domain data callers need to respond programmatically.
- Create custom exceptions when failure modes need to be distinguishable by type.

# Guided Practice Quest

Complete the two steps: create `InsufficientManaException extends RuntimeException` with both constructors, then write a `SpellCaster.castSpell()` method that throws it with a detailed message when mana is insufficient.

# Solo Practice Quest

Design an exception hierarchy for a wizard academy management system. Create three custom exceptions: `WizardNotFoundException(String wizardId)`, `CourseFullException(String courseName, int maxCapacity)`, and `PrerequisiteNotMetException(String courseName, String requiredCourse)`. Each should extend `RuntimeException`, have appropriate constructors, and carry domain fields with getters. Write a `CourseEnrollmentService.enroll(String wizardId, String courseName)` method that throws these exceptions in appropriate conditions (null wizard → `WizardNotFoundException`, course has reached max capacity → `CourseFullException`, wizard lacks prerequisite → `PrerequisiteNotMetException`). Demonstrate catching them separately with different responses.

# Integration

Custom exceptions are the bridge between domain logic and error handling infrastructure. In **Error Strategies** you will see how custom exceptions integrate with `Optional` and defensive programming patterns. In **CRUD APIs**, Spring's `@ExceptionHandler` maps your custom exception types to HTTP responses: throw `SpellNotFoundException` in a service and `@ExceptionHandler(SpellNotFoundException.class)` returns a 404 response automatically. In **Testing**, you will use `assertThrows(InsufficientManaException.class, () -> spellCaster.castSpell(...))` to verify that the right custom exception is thrown for invalid input.

**Integration question:** You have a REST endpoint that calls a service method. The service throws `SpellNotFoundException`. Without any exception handling configured, what HTTP status code does Spring return? What would be more appropriate, and how would you achieve it?

# Lore Conclusion

The combat simulation now speaks the language of magic. `InsufficientManaException: Cannot cast Fireball — requires 80 mana, wizard has 20`. `WizardShieldedException: Wizard Aldric is shielded until turn 5`. `SpellOnCooldownException: Fireball is on cooldown for 3 seconds`. Support engineers diagnose failures in seconds. Callers handle each failure type with a different response. The simulation still fails — but it fails clearly, informatively, and in terms the whole team understands. That is what custom exceptions are for.
