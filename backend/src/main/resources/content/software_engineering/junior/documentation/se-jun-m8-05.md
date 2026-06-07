---
id: se-jun-m8-05
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m8
moduleTitle: "Module 8: Professional Practices"
moduleGlyph: "⚙️"
moduleSortOrder: 8
topicSlug: documentation
topicTitle: "Documentation"
topicSortOrder: 5
lesson: documentation
title: "Documentation"
sortOrder: 5
difficulty: 2
estimatedMinutes: 22
xpReward: 45
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [refactoring]
integrationDomains: [linguistics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what belongs in a README and what does not"
    - "Writes a Javadoc comment with @param, @return, and @throws"
    - "Distinguishes code comments that explain 'why' from comments that explain 'what'"
    - "Names one type of formal documentation (ADR, OpenAPI, wiki) and its purpose"
    - "Identifies a scenario where outdated documentation is worse than none"
  keywords: [readme, javadoc, comment, why, adr, api, docs, update, audience, clear]
  modelAnswer: |
    /**
     * Calculates the compound interest on a principal amount.
     * Uses annual compounding regardless of payment frequency.
     *
     * @param principal  the initial amount (must be positive)
     * @param rate       annual interest rate as a decimal (e.g., 0.05 for 5%)
     * @param years      number of years (must be positive)
     * @return           the final amount after compounding
     * @throws IllegalArgumentException if principal, rate, or years is negative
     */
    public double calculateCompoundInterest(double principal, double rate, int years) {
        if (principal < 0 || rate < 0 || years < 0) {
            throw new IllegalArgumentException("All parameters must be non-negative");
        }
        return principal * Math.pow(1 + rate, years);
    }
guidedSteps:
  - id: doc-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which comment adds the most value?
    inputConfig:
      options:
        - "// increment i by 1\ni++;"
        - "// using linear search because dataset is always < 20 items\nfor (int i = 0; i < items.size(); i++)"
        - "// this is a for loop\nfor (int i = 0; i < items.size(); i++)"
        - "// TODO fix this later"
      
    markingRule:
      matchMode: NORMALIZED
      accepted: ["// using linear search because dataset is always < 20 items\nfor (int i = 0; i < items.size(); i++)"]
      rejectedFeedback: "The best comment explains **why** the code is written this way — information not visible from reading the code itself. Comments that explain *what* the code does are redundant; the code already shows that."
    hint: "Which comment tells you something you couldn't figure out by reading the code itself?"
    reflectionPrompt: "Good comments document intent, constraints, and trade-offs — things that future maintainers (including you) will wonder about. 'Why linear search?' is a question worth answering. 'What does i++ do?' is not."
  - id: doc-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In Javadoc, which tag documents what a method returns?
    inputConfig:
      placeholder: "Javadoc tag"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["@return", "return"]
      rejectedFeedback: "`@return` documents the return value. Key Javadoc tags: `@param name description`, `@return description`, `@throws ExceptionType reason`. These are used by IDEs to show inline documentation."
    hint: "The tag starts with @. It's a common English word."
    reflectionPrompt: "Javadoc generates browsable HTML documentation from specially formatted comments. Every public API method should have Javadoc. Private methods can use regular comments if documentation is warranted."
  - id: doc-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Why can outdated documentation be *worse* than no documentation? Give a concrete example.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [wrong, misleading, incorrect, stale, trust, false, outdated, old, confusion, misdirect]
      rejectedFeedback: "Outdated docs are actively misleading. Example: a README says 'run `mvn test` to run tests' but the project switched to Gradle six months ago. A developer follows the README, gets an error, and spends time debugging — not knowing the docs are wrong. No README would have been less misleading."
    hint: "Think about what a developer does when they follow incorrect documentation."
    reflectionPrompt: "Documentation is a liability as well as an asset. It must be maintained or it actively harms the team. The best docs-as-code approaches keep documentation close to the code so it's updated in the same PR."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is an Architecture Decision Record (ADR)?"
    options:
      - "A UML diagram of the system architecture"
      - "A short document capturing a significant architectural decision, its context, and the reasoning"
      - "A log of all code changes in the system"
      - "A test that verifies the architecture is correct"
    correctIndex: 1
    feedback: "An ADR captures: the decision made, the context that led to it, the options considered, and the rationale. Future developers can understand *why* the architecture is the way it is — not just what it is."
  - type: MULTIPLE_CHOICE
    question: "In a README, what is the most important section for a new developer joining a project?"
    options:
      - "The project's history and changelog"
      - "How to get the project running locally (setup / getting started)"
      - "The list of contributors"
      - "The project's license"
    correctIndex: 1
    feedback: "The getting-started section is the highest-value README content. New developers need to be productive quickly. Instructions for: cloning, prerequisites, running tests, and starting the app locally are immediately actionable."

retrieval:
  recall: "What are the key sections of a good README? What is Javadoc used for?"
  explain: "Explain the difference between a comment that explains 'what' and one that explains 'why', with an example of each."
  mistakeId:
    code: |
      // get user from database
      User user = userRepository.findById(id);

      // check if user is null
      if (user == null) {

      // throw exception
          throw new UserNotFoundException(id);
      }

      // return user
      return user;
    answer: "All these comments explain *what* the code does — information already visible from reading the code. They add noise and will drift out of sync with the code over time. Remove them entirely. The code is self-documenting. If there's a non-obvious *why* (e.g. why we throw this specific exception type), that would be worth a comment."
---

# Hook

Six months later, a new developer joins the team. They clone the repository. They read the README. It says to run `mvn install` — but the project moved to Gradle last quarter and nobody updated the README.

They spend two hours debugging a non-existent problem before someone tells them the README is wrong.

Documentation is a multiplier — for good and bad. Great documentation makes a new developer productive in an hour. Outdated documentation loses them an afternoon and erodes their trust in everything else they read.

> Have you ever followed documentation that turned out to be wrong? How long before you realised it wasn't you — it was the docs?

# Lore Introduction

The Academy's library contains thousands of scrolls — and the most dangerous ones aren't the forbidden texts. They're the *outdated* ones: spell formulations from a century ago that were superseded, binding incantations that no longer work with modern mana flow.

*"An outdated scroll,"* Archmage Veylan says, *"is worse than no scroll. The apprentice with no scroll at least knows they need to ask. The apprentice with a wrong scroll thinks they already know."*

# Core Learning

## Concept Introduction

Good documentation has layers:
1. **README** — project orientation: what it does, how to run it, how to contribute
2. **Javadoc** — public API documentation generated from code comments
3. **Inline comments** — explain *why*, not *what*
4. **Architecture Decision Records (ADRs)** — capture significant decisions and their context
5. **API docs** — OpenAPI/Swagger for REST APIs

**README essentials:**
```markdown
# Project Name
Brief description of what this does.

## Getting Started
Prerequisites: Java 17, Docker
1. Clone: `git clone ...`
2. Run tests: `./gradlew test`
3. Start app: `./gradlew bootRun`

## Configuration
Environment variables: DB_URL, JWT_SECRET

## Architecture
Brief overview + link to ADRs/diagrams
```

**Javadoc:**
```java
/**
 * Validates whether a given token is still active.
 *
 * @param token    the JWT token string (must not be null)
 * @param userId   the expected user ID in the token claims
 * @return         true if the token is valid and matches the user
 * @throws TokenExpiredException if the token has passed its expiry date
 */
public boolean validateToken(String token, Long userId) { ... }
```

## Why It Matters

Documentation is communication across time. You are writing to your future self and your future teammates. Good documentation:
- Reduces onboarding time from days to hours
- Prevents repeated answers to the same questions
- Preserves the *why* behind decisions (which code can't capture)
- Enables async collaboration across time zones

## Worked Examples

**Good inline comment (explains why):**
```java
// Retry three times with exponential backoff — the payment API
// occasionally returns 503 during peak hours (known vendor issue, see ticket #4421)
for (int attempt = 0; attempt < 3; attempt++) {
    ...
}
```

**Bad inline comment (explains what — redundant):**
```java
// loop three times
for (int attempt = 0; attempt < 3; attempt++) {
```

**ADR example (in `docs/adr/0003-use-jwt-for-auth.md`):**
```markdown
## Decision
Use JWT tokens for authentication instead of session-based auth.

## Context
We are building a stateless REST API deployed across multiple instances.

## Rationale
JWTs are self-contained and don't require a shared session store.
The trade-off is larger request headers and difficulty revoking tokens.

## Status: Accepted
```

## Common Mistakes

- **Writing what, not why** — code shows what; comments should show why it's that way.
- **Not updating docs with code changes** — stale docs actively mislead.
- **Over-documenting trivial code** — `i++; // increment i` wastes readers' attention.
- **Hiding important docs deep in the repo** — developers find the README; they often don't find `docs/internal/setup/legacy.md`.
- **No getting-started guide** — the most common missing piece for new contributors.

## Mental Model

Think of documentation as **street signs**. A city with no signs is confusing, but you can explore and eventually learn it. A city with wrong signs is worse — you confidently drive the wrong way.

Good documentation is like accurate street signs: placed where people look, saying what they mean, and updated when the roads change.

## Mini Summary

- ✔ README: what, how to run, how to contribute — orient the new developer immediately
- ✔ Javadoc: public API methods documented with @param, @return, @throws
- ✔ Comments explain *why*, not *what* — code already shows what
- ✔ ADRs capture architectural decisions and their reasoning for future maintainers
- ✔ Outdated documentation is actively harmful — keep docs close to code (docs-as-code)

# Guided Practice Quest

**The Library Restoration**

The Academy's technical library has been neglected. Restore three artifacts: a missing README section, a Javadoc block, and a useful inline comment.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You have just inherited a `PaymentService` class with no documentation. It has three public methods:
1. `processPayment(String cardToken, double amount, String currency)`
2. `refund(String transactionId, double amount)`
3. `getTransactionStatus(String transactionId)`

Write:
1. A **Javadoc block** for each of the three methods (include @param, @return, @throws where appropriate)
2. **One inline comment** somewhere in the conceptual implementation that explains a *why* (invent a plausible reason — e.g. "we retry because...")
3. A **README section** for this payment service explaining what it does, what external services it depends on, and what environment variables it needs

Reflect: What questions do these docs answer that a new developer would otherwise have to ask someone about?

# Integration

**Connecting to Linguistics — The Cooperative Principle**

Linguist Paul Grice (1975) described the Cooperative Principle: in conversation, people expect contributions to be truthful, relevant, clear, and appropriately informative. We interpret communication through this lens — when someone says "some" we infer "not all," because if it were all they would have said so.

Technical documentation violates Grice's maxims in two common ways:
1. **Quantity violations** — saying too much (over-documented trivial code) or too little (missing setup instructions for a critical step)
2. **Quality violations** — outdated or incorrect documentation (violating truthfulness)

When documentation violates these maxims, developers compensate by becoming suspicious of all documentation — a classic Goodhart's Law effect. Teams that can't trust their docs spend more time verifying by reading source code, which defeats the purpose entirely.

The implication: documentation quality is not just a nicety. It's an economic lever. A team that trusts its docs moves faster than a team that doesn't — but only if the docs are trustworthy.

How does maintaining documentation discipline relate to building team trust?

# Lore Conclusion

The library scrolls are accurate and current. New apprentices find the getting-started scroll in under a minute.

*"Documentation is a gift to the future,"* Archmage Veylan says. *"Every time you write something that saves a colleague an hour of confusion, you have multiplied your own work. And every time you leave something undocumented that costs them that hour — you have also multiplied it, in the other direction."*

Write for the developer who will read this in six months. That developer is probably you.
---
