---
id: se-jun-m7-04
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m7
moduleTitle: "Module 7: Design Patterns"
moduleGlyph: "🏗️"
moduleSortOrder: 7
topicSlug: builder_pattern
topicTitle: "Builder Pattern"
topicSortOrder: 4
lesson: builder_pattern
title: "Builder Pattern"
sortOrder: 4
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [factory_pattern]
integrationDomains: [design, linguistics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies the telescoping constructor problem clearly with an example"
    - "Implements a Builder with method chaining (each setter returns 'this')"
    - "Demonstrates that the built object is immutable (final fields, no setters)"
    - "Shows correct use of the build() terminal method"
    - "Mentions at least one alternative (Lombok @Builder or records) and the trade-off"
  keywords: [builder, chaining, immutable, telescoping, final, construct, fluent, optional, readable, Lombok]
  modelAnswer: |
    public class HttpRequest {
        private final String url;
        private final String method;
        private final String body;
        private final int timeoutMs;
        
        private HttpRequest(Builder builder) {
            this.url       = builder.url;
            this.method    = builder.method;
            this.body      = builder.body;
            this.timeoutMs = builder.timeoutMs;
        }
        
        // Getters only — no setters, object is immutable
        public String getUrl()    { return url; }
        public String getMethod() { return method; }
        
        public static class Builder {
            private final String url; // required
            private String method    = "GET";
            private String body      = "";
            private int    timeoutMs = 5000;
            
            public Builder(String url) { this.url = url; }
            
            public Builder method(String method)    { this.method = method;    return this; }
            public Builder body(String body)        { this.body = body;        return this; }
            public Builder timeoutMs(int timeoutMs) { this.timeoutMs = timeoutMs; return this; }
            
            public HttpRequest build() { return new HttpRequest(this); }
        }
    }
    
    // Usage
    HttpRequest request = new HttpRequest.Builder("https://api.academy.com/quests")
        .method("POST")
        .body("{\"questId\": 42}")
        .timeoutMs(3000)
        .build();
guidedSteps:
  - id: bp-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the "telescoping constructor" problem that the Builder pattern solves?
    inputConfig:
      options:
        - "A constructor that calls another constructor, causing infinite recursion"
        - "Multiple overloaded constructors with increasing numbers of parameters, making them hard to read and use"
        - "A constructor that is too long because it contains business logic"
        - "A constructor that is hidden inside a factory"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Multiple overloaded constructors with increasing numbers of parameters, making them hard to read and use"]
      rejectedFeedback: "Telescoping constructors are a series of overloaded constructors: one takes 2 params, another takes 3, another 4. Callers can't easily tell which argument is which, and optional parameters force you to pass null or defaults. The Builder replaces this mess with a readable, fluent construction API."
    hint: "Imagine calling new User('Alice', null, null, null, 30, true) — which argument is which?"
    reflectionPrompt: "Have you ever seen a constructor call with 6+ arguments and struggled to understand what each one meant? What made it confusing?"
  - id: bp-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In a Builder with method chaining, each setter method returns ___ to allow calls to be chained together fluently.
    inputConfig:
      placeholder: "keyword or reference"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["this", "the builder", "itself", "the same builder"]
      rejectedFeedback: "Each setter in a Builder returns 'this' — a reference to the Builder itself. This enables the fluent API style: builder.name('Alice').age(30).email('a@b.com').build(). Without returning 'this', each setter call would require a separate statement."
    hint: "The keyword that refers to the current object in Java."
    reflectionPrompt: "How does returning 'this' from each setter transform a series of statements into a readable expression?"
  - id: bp-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain why the object produced by Builder.build() is often made immutable. What does immutability mean and why is it beneficial?
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [immutable, final, setter, thread, safe, change, state, predictable, consistent]
      rejectedFeedback: "Immutable means the object's state cannot change after construction. Fields are marked final and no setters are provided. Benefits: thread safety (no shared mutable state), predictability (you know the object won't change under you), and preventing accidental modification. The Builder does the complex construction; the result is a stable, trustworthy value object."
    hint: "Think about what 'final' does to a field, and what problems mutable state can cause in concurrent or shared code."
    reflectionPrompt: "Can you think of a real-world object that is naturally immutable after creation (like a cheque or a contract)? How does that analogy apply to software?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In a Builder pattern, which method signals that construction is complete and produces the final object?"
    options:
      - "create()"
      - "make()"
      - "build()"
      - "construct()"
    correctIndex: 2
    feedback: "By convention, Builder patterns use build() as the terminal method that validates the state and returns the constructed immutable object. This convention is universal enough that IDEs and frameworks (like Lombok) use it automatically."
  - type: MULTIPLE_CHOICE
    question: "Lombok's @Builder annotation generates builder code at compile time. What is the main trade-off of using it?"
    options:
      - "It makes objects mutable"
      - "It requires more boilerplate than writing the builder manually"
      - "It hides the generated code, reducing transparency and making customisation harder"
      - "It does not support method chaining"
    correctIndex: 2
    feedback: "Lombok @Builder reduces boilerplate enormously but the generated code is not visible in your source file. Debugging edge cases, customising validation in build(), or understanding what was generated requires knowledge of what Lombok produces. For most cases the trade-off is worth it; for complex builders, manual implementation may be clearer."
retrieval:
  recall: "Describe the telescoping constructor problem and explain how the Builder pattern eliminates it."
  explain: "Why does the Builder pattern support creating immutable objects more naturally than setter-based construction?"
  mistakeId:
    code: |
      public class User {
          private String name;
          private String email;
          
          public static class Builder {
              private String name;
              private String email;
              
              public void setName(String name)   { this.name = name; }
              public void setEmail(String email) { this.email = email; }
              
              public User build() {
                  User u = new User();
                  u.name = name;
                  u.email = email;
                  return u;
              }
          }
      }
    answer: "Two problems: (1) The Builder setters return void, not 'this' — method chaining is impossible. Fix: change return type to Builder and add 'return this'. (2) The User class has package-private fields and a public no-arg constructor, so anyone can create and mutate a User without using the Builder. Fix: make User's constructor private, taking a Builder parameter, and remove the public no-arg constructor."
---

# Hook

You are building a User registration system. The `User` object needs a name, email, password hash, optional display name, optional avatar URL, optional bio, subscription tier, and creation timestamp. How do you construct it?

Option 1: a constructor with 8 parameters — `new User("Alice", "alice@a.com", "hash", null, null, null, "FREE", Instant.now())`. Six months later, nobody reading that call knows which null is the display name and which is the bio.

Option 2: setters on a mutable object — but now the object is in an invalid half-constructed state until every setter is called, and nothing prevents forgetting one.

The Builder pattern is the elegant third option: a fluent, readable construction API that keeps the object immutable once built, distinguishes required from optional fields, and reads like natural language.

> Reflection: Have you encountered a constructor with many parameters where the order mattered but was hard to remember? What strategies did you (or the code) use to deal with it?

# Lore Introduction

The Academy's Artificers craft enchanted artefacts for adventurers. Each artefact has required properties (name, material) and optional enchantments (fire affinity, shadow resistance, speed rune, healing inscription). In the old workshop, Artificers had to pass all properties at construction time — even the ones they didn't want. Half the workshop's parchment was consumed with null annotations.

Archmage Veylan introduced the Artefact Blueprint: a separate planning document that an Artificer could fill in step by step, specifying only what they wanted, in any order. When the blueprint was complete, they handed it to the forge and received a finished, sealed artefact — unchangeable, exactly as specified. The Blueprint is the Builder.

# Core Learning

## Concept Introduction

The **Builder pattern** separates the construction of a complex object from its representation, allowing the same construction process to produce different configurations. It is particularly useful when an object has many optional parameters.

The pattern has three parts:
- **Builder** — an inner class (or separate class) that accumulates construction parameters via chaining methods
- **build()** — the terminal method that validates and produces the final object
- **Product** — the constructed object, typically immutable

```java
public class User {
    // All fields final — immutable after construction
    private final String name;
    private final String email;
    private final String displayName;
    private final String bio;
    private final String tier;

    // Private constructor — only Builder can call this
    private User(Builder builder) {
        this.name        = builder.name;
        this.email       = builder.email;
        this.displayName = builder.displayName;
        this.bio         = builder.bio;
        this.tier        = builder.tier;
    }

    public String getName()        { return name; }
    public String getEmail()       { return email; }
    public String getDisplayName() { return displayName; }
    public String getBio()         { return bio; }
    public String getTier()        { return tier; }

    public static class Builder {
        // Required fields
        private final String name;
        private final String email;
        // Optional fields with defaults
        private String displayName = "";
        private String bio         = "";
        private String tier        = "FREE";

        public Builder(String name, String email) {
            this.name  = name;
            this.email = email;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this; // enables chaining
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder tier(String tier) {
            this.tier = tier;
            return this;
        }

        public User build() {
            // Validation before creating the object
            if (name == null || name.isBlank()) {
                throw new IllegalStateException("User name is required");
            }
            return new User(this);
        }
    }
}
```

Usage reads like natural language:

```java
User user = new User.Builder("Alice", "alice@academy.com")
    .displayName("Arcane Alice")
    .bio("Runesmith apprentice, third year")
    .tier("PREMIUM")
    .build();
```

Optional fields are simply omitted. Required fields are in the `Builder` constructor, making their necessity visible.

## Why It Matters

**Readability.** `new User.Builder("Alice", "alice@academy.com").tier("PREMIUM").build()` is self-documenting. Compare to `new User("Alice", "alice@academy.com", null, null, "PREMIUM")`.

**Immutability.** The built object has only `final` fields and no setters. It is safe to share across threads and cannot be accidentally mutated.

**Validation.** The `build()` method is the last opportunity to validate that all required fields are present and consistent before the object is created.

## Worked Examples

**HTTP Request Builder:**

```java
HttpRequest request = new HttpRequest.Builder("https://api.academy.com/quests")
    .method("POST")
    .header("Authorization", "Bearer " + token)
    .body("{\"level\": 5}")
    .timeoutMs(3000)
    .build();
```

This is the style used by Java's own `HttpRequest` class (introduced in Java 11).

**Lombok @Builder:**

```java
@Builder
public class QuestReward {
    private final String itemName;
    private final int xp;
    private final String rarity;
}

// Generated automatically:
QuestReward reward = QuestReward.builder()
    .itemName("Arcane Tome")
    .xp(250)
    .rarity("LEGENDARY")
    .build();
```

Lombok eliminates boilerplate at the cost of hidden generated code.

## Common Mistakes

**Returning void from Builder methods.** If setters don't return `this`, chaining breaks and the fluent API disappears entirely. Every setter must `return this`.

**Allowing the Product to be mutable.** If the built object has setters, the immutability guarantee is broken. Keep fields `final` and provide only getters.

**Using Builder when there are only 2-3 parameters.** A 2-parameter object does not need a Builder. A constructor is cleaner. Apply Builder when optional parameters start causing telescoping or null-passing.

## Mental Model

Think of ordering a custom coffee. You tell the barista: "Large, oat milk, two shots, vanilla syrup." You don't specify "no hazelnut, no caramel, no decaf" — the defaults handle what you didn't mention. When you say "that's it", the barista makes your coffee. You receive a complete, unchangeable drink. The Builder is the ordering conversation; `build()` is "that's it".

## Mini Summary

- Builder solves the telescoping constructor problem for objects with many optional parameters.
- Method chaining is enabled by returning `this` from each Builder setter.
- The `build()` method validates and produces the final, typically immutable, object.
- The Product's constructor is private — only the Builder can create it.
- Lombok @Builder generates this boilerplate automatically; useful for simple cases, less transparent for complex ones.

# Guided Practice Quest

**Quest: The Artefact Blueprint**

The Academy's Artificers need a modern blueprint system. You must demonstrate understanding of the Builder pattern by answering questions about its structure, method chaining, and immutability guarantees.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Design a `Quest` Builder for the Academy. A `Quest` has required fields: `title` (String) and `difficulty` (int, 1-5). Optional fields: `description` (String, default empty), `xpReward` (int, default 50), `timeLimit` (Duration, default null), `tier` (String, default "APPRENTICE").

Implement the full `Quest` class with its inner `Builder`, method chaining, validation in `build()`, and immutable fields. Then write a reflection (minimum 80 words) covering:
1. How validation in `build()` compares to validation in a regular constructor
2. Why the `Quest` constructor is private
3. How you would add a required `moduleId` field without breaking the fluent API

# Integration

**Connecting to Linguistics — Syntax and Sentence Construction**

Natural language has syntax rules: sentences are constructed from components (subject, verb, object, modifiers) that combine according to grammar rules to produce a meaningful whole. The order of components matters; some are mandatory (every sentence needs a verb), others are optional (adjectives, adverbs). A sentence is only "complete" — syntactically valid — when its mandatory components are present.

The Builder pattern applies this linguistic structure to object construction. Required fields go in the `Builder` constructor (mandatory — the sentence won't exist without them). Optional fields are fluent modifiers you can attach in any order (like adjectives). `build()` is the period at the end of the sentence: it signals that construction is complete and the expression is valid.

The fluent API style of Builders also borrows from natural language — `request.method("POST").timeout(3000).build()` reads almost like an English description of the intent. This is not accidental; good API design borrows heavily from linguistics to reduce the cognitive load of reading code.

> Reflection: How does the analogy between sentence construction and Builder construction help you remember when to use required vs optional Builder fields?

# Lore Conclusion

Artificer Kael set down the completed blueprint and slid it across to the forge. "Large rune tablet, obsidian base, fire affinity, no shadow resistance. That's it." The forge accepted the blueprint, verified it was complete, and sealed the artefact in a single operation. Kael received a finished item, inscribed exactly as specified.

"What if I want to change the fire affinity later?" Kael asked. Archmage Veylan shook his head. "You don't. That is the point. A sealed artefact holds its enchantments exactly as specified at creation. If you want different properties, build a new one." Kael nodded slowly. Immutability was not a limitation — it was a guarantee. And guarantees, in the Academy's world of chaos and shifting magic, were invaluable.

---
