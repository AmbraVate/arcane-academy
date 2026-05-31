---
id: se-jun-m6-05
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m6
moduleTitle: "Module 6: Testing"
moduleGlyph: "🧪"
moduleSortOrder: 6
topicSlug: tdd
topicTitle: "TDD"
topicSortOrder: 5
lesson: test_driven_development
title: "Test-Driven Development"
sortOrder: 5
difficulty: 3
estimatedMinutes: 30
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [mocking]
integrationDomains: [psychology, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the Red-Green-Refactor cycle correctly"
    - "Writes a failing test before the implementation"
    - "Implements only enough code to make the test pass (no over-engineering)"
    - "Refactors after the test passes while keeping tests green"
    - "Articulates at least one design benefit TDD provides beyond test coverage"
  keywords: [red, green, refactor, failing, test, design, minimal, cycle, coverage, before]
  modelAnswer: |
    // Step 1 - RED: write a failing test
    @Test
    void isValid_passwordUnder8Chars_returnsFalse() {
        PasswordValidator validator = new PasswordValidator();
        assertFalse(validator.isValid("short"));
    }
    // Fails: PasswordValidator doesn't exist yet.

    // Step 2 - GREEN: implement just enough to pass
    class PasswordValidator {
        boolean isValid(String password) {
            return password.length() >= 8;
        }
    }

    // Step 3 - REFACTOR: clean up, add constants, etc.
    // Tests still pass.
guidedSteps:
  - id: tdd-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In TDD, what is the correct order of the three steps?
    inputConfig:
      options:
        - "Write code → Write test → Refactor"
        - "Write test → Write code → Refactor"
        - "Write test → Refactor → Write code"
        - "Refactor → Write test → Write code"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Write test → Write code → Refactor"]
      rejectedFeedback: "TDD: **Write a failing test first** (Red), then **write the minimum code to pass it** (Green), then **refactor while keeping tests green** (Refactor). The test always comes before the implementation."
    hint: "Think about what 'test-driven' means — which comes first?"
    reflectionPrompt: "Writing the test first forces you to think about the API from the caller's perspective before you write the implementation. This produces cleaner, more usable interfaces."
  - id: tdd-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In the Red-Green-Refactor cycle, the 'Red' phase means the test is currently ___.
    inputConfig:
      placeholder: "one word"
    markingRule:
      matchMode: NORMALIZED
      accepted: [failing, failed, broken]
      rejectedFeedback: "Red = failing. The test must fail first — this confirms the test is actually testing something real. A test that passes before any implementation either tests the wrong thing or tests nothing at all."
    hint: "Think traffic lights — what does red mean?"
    reflectionPrompt: "Seeing the test fail first is critical. It's the proof that your test would actually catch a regression. If a test passes before you write the code, it's not testing what you think it's testing."
  - id: tdd-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Beyond test coverage, TDD practitioners claim it improves code *design*. Explain one way writing tests first leads to better-designed code.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [interface, api, design, dependency, inject, testable, simple, coupling, clear, small]
      rejectedFeedback: "Writing a test first forces you to think 'how will I use this code?' before 'how will I build it?' This tends to produce simpler, more focused interfaces. Hard-to-test code is often a design smell — if you can't write a simple test for it, the code probably has too many dependencies or does too much."
    hint: "When you write a test before the code, you're acting as the *user* of the API. What design decisions does that force?"
    reflectionPrompt: "TDD is a design tool that happens to produce tests. Code that's easy to test is almost always better designed: smaller, more focused, less coupled. The tests are a by-product of good design pressure."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why must a TDD test fail before you write the implementation?"
    options:
      - "To make the IDE display a red indicator"
      - "To confirm the test would actually detect a regression"
      - "Because JUnit requires it"
      - "To satisfy the refactor step"
    correctIndex: 1
    feedback: "A test that passes before any implementation either tests nothing (a vacuous test) or is testing pre-existing behaviour. Seeing it fail confirms it's genuinely checking new behaviour."
  - type: MULTIPLE_CHOICE
    question: "In the 'Green' step, how much code should you write?"
    options:
      - "The full intended implementation"
      - "The minimum code needed to make the failing test pass"
      - "All tests for the whole class"
      - "A refactored version of the existing code"
    correctIndex: 1
    feedback: "Write the minimum code to pass. This keeps you focused on the test's requirement and avoids over-engineering. If you need more behaviour, write another test first."

retrieval:
  recall: "Describe the Red-Green-Refactor cycle of TDD step by step."
  explain: "Explain to a sceptical colleague why writing tests before code could lead to better design, not just better coverage."
  mistakeId:
    code: |
      // Developer writes all implementation first:
      class ShoppingCart {
          private List<Item> items = new ArrayList<>();
          public void add(Item item) { items.add(item); }
          public double total() { return items.stream().mapToDouble(Item::getPrice).sum(); }
          public void applyDiscount(double percent) { ... }
          public void removeItem(String id) { ... }
          // ... 200 more lines

          // Then writes one test at the end:
          @Test
          void testCart() {
              cart.add(new Item("x", 10.0));
              assertEquals(10.0, cart.total());
          }
      }
    answer: "This is the opposite of TDD. All implementation was written before the tests, so the tests were written to fit the existing code — not to drive its design. The single test also provides minimal coverage. In TDD, each method would have been preceded by a focused failing test, driving the design of each method's interface and ensuring coverage of its behaviour."
---

# Hook

You've been asked to add a new feature. You sit down and start coding. An hour later, you have something that seems to work. You write a quick test to confirm. It passes.

Then someone points out you haven't handled the empty input case. Or the negative value. Or the concurrent access. You go back, patch it, re-test. This loop continues.

What if instead, every case you could think of was written down *before* you wrote a line of implementation? What if the tests drove the code into existence, rather than being attached to it afterward?

That's TDD.

> Have you ever written a test that passed immediately without testing anything real? How would you know?

# Lore Introduction

The Academy's master artificers don't begin forging until the testing crystal has been inscribed with every failure condition the artifact must not exhibit. First the crystal, then the forge. If the crystal glows red, the artifact is incomplete. Only when the crystal stays white may the artifact leave the workshop.

*"You cannot know you have succeeded,"* Archmage Veylan says, *"until you have defined what failure looks like. Define failure first. Then build toward success."*

# Core Learning

## Concept Introduction

**Test-Driven Development (TDD)** is a development practice with a three-step cycle:

1. **Red** — Write a test for the next piece of behaviour. Run it. It fails (it has to — the code doesn't exist yet).
2. **Green** — Write the *minimum* code to make the test pass. No more.
3. **Refactor** — Clean up the code (improve names, remove duplication) while keeping all tests green.

Repeat for each behaviour.

```
Red → Green → Refactor → Red → Green → Refactor → ...
```

Example cycle for a `Stack`:

```java
// RED: write the failing test first
@Test
void push_oneItem_sizeIsOne() {
    Stack stack = new Stack();
    stack.push("hello");
    assertEquals(1, stack.size());
}

// RED: this fails — Stack doesn't exist yet.

// GREEN: implement just enough
class Stack {
    private List<Object> items = new ArrayList<>();
    void push(Object item) { items.add(item); }
    int size() { return items.size(); }
}

// GREEN: test passes. REFACTOR if needed.
```

## Why It Matters

TDD provides:
- **Automatic test coverage** — every piece of code was written to satisfy a test
- **Design feedback** — code that's hard to test has a design problem
- **Confidence to refactor** — green tests prove nothing broke
- **Reduced debugging time** — failures are caught immediately in a tiny scope
- **Living documentation** — tests describe exactly what the code does

## Worked Examples

**Cycle 1 — Simple behaviour:**
```java
// Red
@Test
void isEmpty_newStack_returnsTrue() {
    assertTrue(new Stack().isEmpty());
}
// Green: add isEmpty() { return items.isEmpty(); }
```

**Cycle 2 — Next behaviour:**
```java
// Red
@Test
void pop_singleItem_returnsItem() {
    Stack stack = new Stack();
    stack.push("top");
    assertEquals("top", stack.pop());
}
// Green: add pop() { return items.remove(items.size() - 1); }
```

**Cycle 3 — Error case:**
```java
// Red
@Test
void pop_emptyStack_throwsException() {
    assertThrows(EmptyStackException.class, () -> new Stack().pop());
}
// Green: add if (items.isEmpty()) throw new EmptyStackException();
```

## Common Mistakes

- **Writing implementation before tests** — this is "test-after" development, not TDD; the design benefit is lost.
- **Writing too much in the Green step** — implement *only* what the current test requires; don't anticipate future tests.
- **Skipping Refactor** — the cycle is Red-Green-*Refactor*; refactoring while tests are green is what keeps code clean.
- **TDD for everything** — TDD is hardest for UIs, integrations, and exploratory code; use judgement.
- **Expecting TDD to be faster short-term** — it feels slower at first; the payoff is long-term speed and confidence.

## Mental Model

TDD is **sculpting by removing stone**. You start with a block (a failing test defining the desired shape), and you remove only what you need to reveal the shape — no more. The test is the mould; the code fills it exactly. Over-engineering is like carving past the mould into nothing.

## Mini Summary

- ✔ Red-Green-Refactor: write failing test → pass with minimum code → clean up
- ✔ The test must fail first — that's proof it's testing real behaviour
- ✔ Write only enough code to make the current test pass
- ✔ TDD produces better-designed code because tests are written before implementation
- ✔ Refactoring is safe when tests are green — they catch regressions immediately

# Guided Practice Quest

**The Inscribed Crystal**

Before building a `BankAccount` class, inscribe the testing crystal with its behaviour specifications. Work through the Red-Green-Refactor cycle for the first two behaviours.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Use TDD to develop a `FizzBuzz` class with a method `String convert(int n)`:
- Returns "Fizz" if divisible by 3
- Returns "Buzz" if divisible by 5
- Returns "FizzBuzz" if divisible by both
- Returns the number as a string otherwise

**The rules:**
1. Write ALL tests before ANY implementation
2. For each test, describe the Red phase (what you'd see fail) and the Green phase (what minimal code you'd add)
3. After all tests are green, describe one refactoring you'd make

You must write at least 5 test cases. For each, write the `@Test` method and explain which part of the Red-Green-Refactor cycle it represents.

# Integration

**Connecting to Psychology — Deliberate Practice**

Psychologist Anders Ericsson's research on expert performance found that top performers don't just practise more — they practise *deliberately*: focused repetition with immediate feedback, targeting weaknesses, and gradual extension of capability.

TDD is deliberate practice for software design. Each Red-Green-Refactor cycle is a tight feedback loop: you write a test (intention), implement (action), and immediately get feedback (pass/fail). The cycle takes minutes, not hours. You never write code without knowing whether it works.

Compare this to writing 500 lines and running the app to see if it works. The feedback loop is much longer, errors are harder to diagnose, and the design is harder to change because so much has accumulated.

Ericsson also found that deliberate practice requires leaving the comfort zone — doing things that feel hard. TDD can feel counter-intuitive at first (writing tests before code seems backwards). That discomfort is the signal that genuine skill development is happening.

How might treating each TDD cycle as deliberate practice change how you approach learning a new codebase feature?

# Lore Conclusion

The testing crystal was inscribed first. The artifact was forged to satisfy it. Every specification glows white.

*"Notice,"* Archmage Veylan says, *"that you never wrote more than was needed. The crystal demanded certain properties; the artifact provided exactly those. No waste. No guesswork. No surprises."*

The most disciplined artificers in the Academy are TDD practitioners. Their workshops are smaller, their artifacts are cleaner, and their testing crystals have never gone dark mid-deployment.
---
