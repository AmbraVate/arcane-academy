---
id: se-app-m1-10
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m1
moduleTitle: "Module 1: Foundations of Computation"
moduleGlyph: "🧠"
moduleSortOrder: 1
topicSlug: logic_foundations
topicTitle: "Logic Foundations"
topicSortOrder: 2
lesson: logical_operators
title: "Logical Operators"
sortOrder: 10
difficulty: 2
estimatedMinutes: 20
xpReward: 40
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [comparisons]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly uses && to combine two conditions that must both be true"
    - "Correctly uses || to express that at least one condition must be true"
    - "Correctly uses ! to invert a boolean expression"
    - "Constructs a truth table for at least one compound expression"
    - "Explains operator precedence between &&, ||, and !"
  keywords: [and, or, not, logical, operator, truth, combine, condition, boolean]
  modelAnswer: |
    // Age check: adult AND has ticket
    boolean canEnter = age >= 18 && hasTicket;

    // Discount: student OR senior
    boolean getsDiscount = isStudent || isSenior;

    // Not banned
    boolean allowed = !isBanned;

    // Combined: must be allowed AND (student OR senior)
    boolean fullAccess = allowed && (isStudent || isSenior);
guidedSteps:
  - id: log-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does `true && false` evaluate to?
    inputConfig:
      options:
        - "true"
        - "false"
        - "Compile error"
        - "null"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["false"]
      rejectedFeedback: "`&&` (AND) requires BOTH sides to be true. Since one side is `false`, the whole expression is `false`."
    hint: "AND only returns true when BOTH operands are true."
    reflectionPrompt: "Correct. `&&` is an all-or-nothing gate. One false input poisons the whole result. This is the foundation of access control: multiple conditions all must pass."
  - id: log-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the blank: `false || true` evaluates to ____
    inputConfig:
      placeholder: "true or false"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["true"]
      rejectedFeedback: "`||` (OR) returns true if AT LEAST ONE operand is true. Since the right side is `true`, the result is `true`."
    hint: "OR only needs one side to be true."
    reflectionPrompt: "Exactly. `||` is generous — it only needs one success. Used to give users multiple valid paths through a condition."
  - id: log-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      What does `!(5 > 3)` evaluate to, and why?
      Answer in 1-2 sentences.
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [false, invert, not, negate, true, flips]
      rejectedFeedback: "`5 > 3` is `true`. The `!` operator inverts it, so `!(5 > 3)` is `false`. NOT flips the boolean value."
    hint: "First evaluate the inner expression, then apply the ! to flip it."
    reflectionPrompt: "Right. `!` is the flip operator — it turns true to false and false to true. Always evaluate inside the parentheses first, then negate."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which expression is true ONLY when both `a` and `b` are true?"
    options:
      - "`a || b`"
      - "`a && b`"
      - "`!a`"
      - "`a != b`"
    correctIndex: 1
    feedback: "`&&` (AND) returns true only when both operands are true. `||` returns true when at least one is true."
  - type: MULTIPLE_CHOICE
    question: "What is the value of `!true || false`?"
    options:
      - "true"
      - "false"
      - "Compile error"
      - "null"
    correctIndex: 1
    feedback: "`!true` evaluates to `false`. Then `false || false` is `false`. Remember: `!` has higher precedence than `||`."

retrieval:
  recall: "Name the three logical operators in Java and describe what each one does."
  explain: "Explain short-circuit evaluation: why does Java sometimes not evaluate the second operand in && and ||?"
  mistakeId:
    code: |
      boolean result = age > 18 || age < 65 && !isBanned;
    answer: "Operator precedence: `&&` binds tighter than `||`. This evaluates as `age > 18 || (age < 65 && !isBanned)`, which is probably not the intended logic. Use parentheses to make intent explicit: `(age > 18 || age < 65) && !isBanned`."
---

# Hook

You need a user who is over 18 **and** has a valid ticket. Or maybe they qualify if they are a student **or** a senior. Or perhaps they are allowed in as long as they are **not** banned.

Single comparisons only take you so far. Real decisions in code combine multiple conditions — and three operators make that possible. Get them wrong and your access checks, filters, and validations will silently let the wrong things through.

> What would happen to a security system if AND and OR were accidentally swapped?

# Lore Introduction

The Academy's inner vaults use compound runes — not a single check, but chains of conditions woven together. A door may open only when the visitor carries the correct sigil *and* the hour is right. Another corridor admits those who bear a student token *or* a master's seal.

*"One condition is a lock,"* Archmage Veylan tells the apprentices. *"Three logical operators give you a vault."*

# Core Learning

## Concept Introduction

Java has three **logical operators** that combine or invert boolean expressions:

| Operator | Name | Returns true when… |
|---|---|---|
| `&&` | AND | **Both** operands are true |
| `\|\|` | OR | **At least one** operand is true |
| `!` | NOT | The operand is **false** |

**Truth table for `&&`:**

| A | B | A && B |
|---|---|---|
| true | true | true |
| true | false | false |
| false | true | false |
| false | false | false |

**Truth table for `\|\|`:**

| A | B | A \|\| B |
|---|---|---|
| true | true | true |
| true | false | true |
| false | true | true |
| false | false | false |

**`!` simply flips the value:** `!true` → `false`, `!false` → `true`.

### Short-Circuit Evaluation

Java evaluates lazily. With `&&`, if the left side is `false`, the right side is never checked — the result is already `false`. With `||`, if the left side is `true`, the right side is skipped. This matters when the right side has side effects or could cause errors.

```java
// Safe: if list is null, the second check is never reached
if (list != null && list.size() > 0) { ... }
```

### Precedence

`!` binds tightest, then `&&`, then `||`. Use parentheses to make compound expressions explicit:

```java
boolean valid = age >= 18 && (isStudent || hasMembership);
```

## Why It Matters

Nearly every real-world condition is compound. Logical operators are how you build those conditions cleanly — instead of nesting `if` inside `if`, you combine with `&&` and `||` in a single readable expression.

## Worked Examples

```java
int age = 20;
boolean hasTicket = true;
boolean isStudent = false;
boolean isBanned = false;

// Must be adult AND have ticket
boolean canEnter = age >= 18 && hasTicket;        // true

// Student OR senior discount
boolean getsDiscount = isStudent || age >= 65;     // false

// Not banned
boolean allowed = !isBanned;                        // true

// Combined
boolean fullAccess = canEnter && allowed;           // true
```

## Common Mistakes

- Confusing `&&` and `||` — AND requires both, OR needs just one.
- Forgetting that `!` binds tighter than `&&` and `||` — `!a && b` is `(!a) && b`, not `!(a && b)`.
- Omitting parentheses in mixed expressions — always make precedence explicit.
- Checking a condition twice instead of combining: `if (a) { if (b) { } }` can be `if (a && b) { }`.

## Mental Model

Think of `&&` as a **chain of locks** — every lock must open. `||` is a **set of keys** — only one needs to fit. `!` is a **mirror** — it shows you the opposite. Build complex gates by combining these three simple mechanisms.

## Mini Summary

- ✔ `&&` (AND): true only when both sides are true
- ✔ `||` (OR): true when at least one side is true
- ✔ `!` (NOT): inverts a boolean — true becomes false and vice versa
- ✔ Precedence: `!` > `&&` > `||` — use parentheses to be explicit
- ✔ Short-circuit: Java stops evaluating as soon as the result is determined
- ✔ Compound conditions replace deeply nested `if` blocks

# Guided Practice Quest

**The Compound Rune Chamber**

Three runes, three questions. Each tests one logical operator. Evaluate the expressions mentally, then commit to your answer — the chamber remembers wrong guesses.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Write a boolean expression for each of the following. Use `&&`, `||`, and `!` as appropriate:

1. "A user can post if they are logged in AND their account is not suspended."
2. "A file is valid if its extension is `'txt'` OR `'csv'`."
3. "Access is granted if the user is an admin OR (is a member AND is not banned)."
4. "A number is outside the range 1–100."
5. Construct the full truth table for `A && !B` for all four combinations of A and B.

For each, state which operators you used and why.

# Integration

**Connecting to Philosophy and Mathematics — Propositional Logic**

Logical operators in Java are a direct implementation of **propositional logic**, formalised by George Boole in the nineteenth century. Philosophers had studied AND, OR, and NOT as connectives in arguments long before computers existed: "If it rains AND I forget my umbrella, I will get wet." Boolean algebra then gave these connectives precise mathematical rules.

In mathematics, De Morgan's Laws show how AND and OR relate through negation: `!(A && B)` is equivalent to `(!A || !B)`. These laws let you rewrite conditions in different but equivalent forms — useful for simplifying complex guards.

How does knowing that logical operators have deep roots in formal logic change the way you think about writing conditions in code?

# Lore Conclusion

The compound runes now respond to the apprentice's touch. Three operators — three fundamental forces of decision.

*"And, Or, Not,"* Veylan says quietly. *"These three are enough to express every condition a program will ever face. Master the truth tables. Parenthesise your intentions. Never assume precedence."*

The vault doors cycle through their combinations and slide open.
---
