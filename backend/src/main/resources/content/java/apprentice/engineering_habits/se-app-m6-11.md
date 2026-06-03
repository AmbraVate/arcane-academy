---
id: se-app-m6-11
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m6
moduleTitle: "Module 6: Debugging and Engineering Habits"
moduleGlyph: "🔧"
moduleSortOrder: 6
topicSlug: engineering_habits
topicTitle: "Beginner Engineering Habits"
topicSortOrder: 2
lesson: commenting_wisely
title: "Commenting Wisely"
sortOrder: 11
difficulty: 1
estimatedMinutes: 18
xpReward: 40
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [small_functions]
integrationDomains: [psychology, linguistics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the principle of commenting 'why', not 'what'"
    - "Identifies at least two bad comment patterns (obvious, outdated, misleading)"
    - "Demonstrates a basic Javadoc comment structure"
    - "Explains what 'self-documenting code' means"
    - "Reflects on when a comment genuinely adds value versus when it adds noise"
  keywords: [why, Javadoc, self-documenting, obvious, outdated, comment, intention]
  modelAnswer: |
    // BAD COMMENT — describes what the code already says
    i = i + 1; // increment i by 1

    // GOOD COMMENT — explains why, which the code cannot express
    // Retry limit set to 3 to comply with the payment provider's rate limiting policy.
    // See: https://docs.provider.com/rate-limits
    private static final int MAX_RETRIES = 3;

    /**
     * Calculates the compound interest for a savings account.
     *
     * @param principal  the initial deposit amount in pounds
     * @param rate       the annual interest rate as a decimal (e.g. 0.05 for 5%)
     * @param years      the number of years to compound
     * @return the total amount after compounding
     */
    public double calculateCompoundInterest(double principal, double rate, int years) {
        return principal * Math.pow(1 + rate, years);
    }
guidedSteps:
  - id: comment-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which comment adds genuine value?
      ```java
      // Comment A
      x = x + 1; // add 1 to x

      // Comment B
      retryCount++; // Backing off before retry 3 per the API rate-limit contract
      ```
    inputConfig:
      options:
        - "Comment A — it clearly explains what the code does"
        - "Comment B — it explains why the increment is happening"
        - "Both are equally valuable"
        - "Neither — all comments are noise"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Comment B — it explains why the increment is happening"]
      rejectedFeedback: "Comment A restates what the code already says — any reader can see that x is incremented. Comment B explains something the code cannot: the business reason (rate-limit contract) behind the action. Comments should add context that the code itself cannot express."
    hint: "What can the code NOT tell you by itself?"
    reflectionPrompt: "If deleting a comment loses no information, the comment was not doing any work. Ask: 'Does this comment tell me something the code cannot?'"

  - id: comment-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Javadoc comments start with `/**` and use tags like `@param`, `@return`, and `@throws`. What Javadoc tag describes a method's return value?
    inputConfig:
      placeholder: "@tag"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["@return", "@returns"]
      rejectedFeedback: "@return describes what value a method returns and what it means. For example: @return the total price including tax, in pounds. It is used by IDEs and tools like Javadoc to generate API documentation."
    hint: "The tag name mirrors what you are documenting."
    reflectionPrompt: "@param, @return, and @throws are the three most important Javadoc tags. Together they form a contract: here is what I accept, here is what I give back, here is what can go wrong."

  - id: comment-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain what "self-documenting code" means and describe one technique for making code self-documenting without adding comments.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [name, descriptive, readable, intent, without]
      rejectedFeedback: "Self-documenting code expresses its intent through naming and structure rather than comments. A technique: replace a magic number with a named constant (private static final int DAYS_IN_WEEK = 7;), or replace a cryptic expression with a named method (isEligibleForDiscount(customer) instead of customer.age > 65 && customer.purchases > 10)."
    hint: "If a comment explains what the code does, could the code be rewritten so it explains itself?"
    reflectionPrompt: "Every time you write a 'what' comment, ask: could I rename or restructure the code so this comment is unnecessary?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which type of comment is an example of a BAD commenting practice?"
    options:
      - "A comment explaining a non-obvious business rule"
      - "A comment with a link to the relevant specification"
      - "A comment that describes what the next line of code already makes obvious"
      - "A Javadoc comment explaining a public API method's parameters"
    correctIndex: 2
    feedback: "Comments that restate what the code already clearly says are noise — they add maintenance burden (they can become outdated) without adding information. Comments should explain intent, context, or constraints that the code itself cannot express."
  - type: MULTIPLE_CHOICE
    question: "An outdated comment describes behaviour that was changed six months ago but the comment was never updated. What is the risk?"
    options:
      - "The compiler will warn about it"
      - "The comment is harmless — readers ignore them anyway"
      - "A reader may trust the comment over the code, leading to misunderstanding or a new bug"
      - "It will cause a NullPointerException at runtime"
    correctIndex: 2
    feedback: "Outdated comments are actively harmful — they create a contradiction between what the code does and what a reader is told. A developer may 'fix' what they think is a bug (the code disagreeing with the comment) and re-introduce the old behaviour. Comments must be maintained alongside the code."
retrieval:
  recall: "Name three bad comment patterns and explain why each is harmful."
  explain: "A colleague says 'I comment everything — that way my code is always documented'. Explain why this approach can actually make code harder to maintain."
  mistakeId:
    code: |
      // Loop through the list
      for (String name : names) {
          // Check if the name is not null
          if (name != null) {
              // Print the name
              System.out.println(name);
          }
      }
    answer: "Every comment restates what the code already says — they are noise comments. Any Java developer can read 'for (String name : names)' and understand it is a loop. Deleting all three comments makes the code cleaner and removes the maintenance risk of comments becoming outdated. If the intent needs documenting, it would be: '// Print all non-null names from the registry' — one comment on the block, explaining the purpose."
---

# Hook

There is a paradox at the heart of comments: the more you write, the less readers trust them. If every line has a comment restating the obvious, readers start skimming comments entirely — so the one genuinely important comment (explaining a tricky algorithm or a regulatory constraint) goes unread. Like the boy who cried wolf, over-commenting desensitises the reader.

At the same time, a codebase with no comments at all can be deeply mysterious. Why does this retry loop run exactly three times? Why is that calculation multiplied by 0.85? Why does the code skip records where the timestamp is exactly midnight? Some things the code simply cannot explain — only a well-placed comment can.

The discipline of commenting wisely is about knowing the difference: when the code speaks for itself, let it; when it cannot, speak clearly and concisely.

> Can you think of an example — in a book, manual, or sign — where too many explanatory notes made the main content harder to follow? What would have been more useful?

# Lore Introduction

In the Academy's Archive, the Master Annotators maintain a strict tradition: every inscription may have margin notes, but only if those notes explain what the inscription cannot explain itself. A note that reads "This rune draws fire" next to a rune that is literally shaped like a flame is crossed out and the annotator is fined. But a note that reads "This rune draws fire from the emotional residue of the caster — ensure emotional neutrality before casting" is celebrated: it adds context the symbol cannot carry alone.

This tradition — comment the *why*, not the *what* — is the foundation of wise commenting.

# Core Learning

## Concept Introduction

**When to comment — explain WHY, not WHAT:**
```java
// BAD — restates what the code says
if (user.age >= 18) { // check if user is 18 or older
    ...
}

// GOOD — explains why 18 is significant
if (user.age >= 18) { // Legal minimum age per UK Consumer Credit Act 1974
    ...
}
```

**Self-documenting code** — restructure so the code explains itself:
```java
// Instead of:
if (c.age > 65 && c.orders > 10) { ... }  // check for senior discount

// Write:
if (isEligibleForSeniorDiscount(customer)) { ... }
// No comment needed — the method name IS the documentation
```

**Javadoc for public APIs:**
```java
/**
 * Calculates the discount amount for an eligible customer.
 *
 * @param customer  the customer to evaluate; must not be null
 * @param price     the original price in pounds; must be positive
 * @return the discount amount, or 0.0 if the customer is not eligible
 * @throws IllegalArgumentException if customer is null or price is negative
 */
public double calculateDiscount(Customer customer, double price) { ... }
```

**Bad comment patterns to avoid:**
- **Obvious comments** — restating what the code says
- **Outdated comments** — code was changed but comment was not
- **Misleading comments** — comment says one thing, code does another
- **Commented-out code** — use version control instead; delete it
- **TODO without owner/date** — "// TODO: fix this" with no context becomes permanent clutter

## Why It Matters

Comments that add no value increase maintenance burden without benefit. When code changes, comments must change too — but they often do not. An outdated or misleading comment is worse than no comment at all: it actively misdirects the reader. Good commenting practice means every comment earns its place by providing genuine information the code cannot express.

## Worked Examples

**Example 1 — Explaining a non-obvious decision**
```java
// Using LinkedHashMap specifically to preserve insertion order for the audit log.
// HashMap would be faster but does not guarantee order.
Map<String, Event> auditLog = new LinkedHashMap<>();
```

**Example 2 — Explaining a magic number**
```java
// BAD
double result = price * 0.85;

// GOOD — the constant documents the intent
private static final double MEMBER_DISCOUNT_RATE = 0.85; // 15% off for registered members
double result = price * MEMBER_DISCOUNT_RATE;
```

**Example 3 — Javadoc on a public method**
```java
/**
 * Finds the first available appointment slot on or after the given date.
 *
 * @param fromDate  the earliest date to consider; must not be null
 * @param durationMinutes  the required slot length; must be between 15 and 480
 * @return the first available Slot, or Optional.empty() if none found in the next 30 days
 */
public Optional<Slot> findNextAvailableSlot(LocalDate fromDate, int durationMinutes) { ... }
```

## Common Mistakes

- **Commenting out dead code** — deleted code belongs in git history, not in comment blocks; commented-out code confuses readers.
- **TODO without context** — "// TODO: optimise this" tells no one who should do it, why, or when. Write: "// TODO (jsmith, 2024-01-15): replace with HashMap once data size is confirmed > 10k".
- **Section headers** — comments like `// ===================== SETUP =====================` suggest the method should be split into smaller methods, not annotated.
- **Translating code into English** — `i++; // increment i` adds no information.
- **Stale Javadoc** — a `@param age` that describes a parameter that was renamed three months ago actively misleads.

## Mental Model

Think of a well-commented codebase as a well-annotated map. The map itself shows the roads, rivers, and cities — the code. Annotations are the marks a traveller adds: "Road impassable in winter", "Shortcut discovered 1847", "Toll applies here". The annotations add context the map cannot represent. An annotation that says "this is a road" next to a road symbol is useless clutter — and a traveller who writes too many useless annotations trains everyone to ignore them.

## Mini Summary

✔ Comment *why*, not *what* — the code already shows what; only you know why.
✔ Self-documenting code (good names, small functions) reduces the need for comments.
✔ Use Javadoc (`@param`, `@return`, `@throws`) on public API methods.
✔ Avoid: obvious comments, commented-out code, outdated comments, context-free TODOs.
✔ Every comment must earn its place — if it adds no information, delete it.

# Guided Practice Quest

**The Annotator's Exam**
The Archive's Master Annotator is reviewing a batch of code. Identify which comments are valuable and which should be removed or replaced.
Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You are given this method:
```java
public double calc(double p, double r, int n) {
    return p * Math.pow(1 + r / n, n * 10);
}
```
(1) Rewrite it with descriptive parameter names to make it self-documenting. (2) Add a Javadoc comment explaining what the method does, its parameters, and its return value. (3) Add one inline comment explaining any non-obvious part of the formula. Reflect in 2-3 sentences: what made this code particularly comment-worthy, and how did renaming the parameters change your need for comments?

# Integration

**Connecting to Linguistics — Pragmatics and Context**
In linguistics, **pragmatics** studies how context shapes the meaning of language beyond the literal words. When someone says "Can you pass the salt?" they do not expect the answer "Yes" — context makes clear it is a request. Similarly, code comments operate in a pragmatic space beyond the syntax: they supply the context that cannot be inferred from the code alone — the reasoning, the constraints, the history.

The field of linguistics also studies **maxims of communication** (Grice's maxims), which include the principle of quantity: "Make your contribution as informative as required, but not more." This is exactly the principle of wise commenting. Say what adds information; do not say what is already obvious; avoid both under-commenting (leaving readers without necessary context) and over-commenting (overwhelming them with noise).

> Think of a conversation where someone over-explained something you already knew. How did it affect your attention? Now think of a situation where critical context was omitted. Which was more costly — and why does that asymmetry matter for commenting?

# Lore Conclusion

The Master Annotator returns the apprentice's scroll with only four margin notes — all four explaining subtle constraints that would have been invisible to any other reader. Every other annotation the apprentice had tentatively added has been crossed out with a gentle note: "The inscription itself is clear." The scroll is cleaner, sharper, and more trustworthy because of what was removed.

Archmage Veylan gathers the graduating apprentices for a brief address. "You have learned to name with precision, format with respect, divide with discipline, and annotate with wisdom. These are not merely habits — they are the mark of a professional. The next path before you leads into the realm of Junior Engineering, where the challenges grow larger and the principles deepen. Go forward, and build things worthy of the Archive."

---
