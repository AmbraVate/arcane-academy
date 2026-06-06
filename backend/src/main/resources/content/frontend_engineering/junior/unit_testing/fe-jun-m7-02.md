---
id: fe-jun-m7-02
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m7
moduleTitle: "Module 7: Frontend Testing"
moduleGlyph: "🧪"
moduleSortOrder: 7
topicSlug: unit_testing
topicTitle: "Unit Testing"
topicSortOrder: 1
lesson: vitest_basics
title: "Vitest Basics"
sortOrder: 2
difficulty: 3
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m7-01]
integrationDomains: [design, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the role of describe, it/test, and expect"
    - "Gives a correct example of a Vitest test for a simple function"
    - "Names at least two matchers and what they check"
  keywords: [describe, it, test, expect, matcher, toBe, toEqual, describe]
  modelAnswer: |
    describe groups related tests under a named suite. it (or test) defines a single test case with a description and a callback. expect wraps the actual value and chains to a matcher that defines the assertion. Common matchers include toBe (strict equality), toEqual (deep equality for objects/arrays), toBeNull, toBeTruthy, toContain, toThrow, and toHaveLength. A typical test reads: it('does X', () => { const result = doX(input); expect(result).toBe(expectedValue); }).
guidedSteps:
  - id: fe-jun-m7-02-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "What is the difference between toBe and toEqual in Vitest?"
    inputConfig:
      options:
        - "toBe is for strings only; toEqual is for numbers"
        - "toBe uses strict (===) equality; toEqual does deep structural equality"
        - "toBe checks type; toEqual checks value"
        - "They are identical — toEqual is just an alias for toBe"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["toBe uses strict (===) equality; toEqual does deep structural equality"]
      rejectedFeedback: "toBe is like ===: it fails for two objects with the same values but different references. toEqual recursively compares structure and values — correct for objects and arrays."
    hint: "Would expect({a: 1}).toBe({a: 1}) pass? What about expect({a: 1}).toEqual({a: 1})?"
    reflectionPrompt: "When would you use toBe and when would you use toEqual? Give an example of each."
  - id: fe-jun-m7-02-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "What does a describe block do and why is it useful?"
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [group, related, suite, organise, together, name]
      rejectedFeedback: "describe groups related test cases together under a named suite, making it easier to read test output and find failing tests."
    hint: "Think about a test file for a formatPrice function — would you describe('formatPrice', ...) grouping be useful?"
    reflectionPrompt: "How do nested describe blocks help when testing a function with multiple behaviours?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which Vitest code correctly tests that add(2, 3) returns 5?"
    options:
      - "test('adds', add(2, 3) === 5)"
      - "it('returns 5', () => { expect(add(2, 3)).toBe(5); })"
      - "assert(add(2, 3) == 5)"
      - "vitest.check(() => add(2, 3) === 5)"
    correctIndex: 1
    feedback: "it (or test) takes a description string and a callback. Inside the callback, expect wraps the actual value and .toBe(5) asserts the expected value."
retrieval:
  recall: "What are the three main building blocks of a Vitest test file?"
  explain: "Why would expect([1, 2, 3]).toBe([1, 2, 3]) fail even though the arrays contain the same values?"
  mistakeId:
    code: |
      test('formats name', () => {
        const result = formatFullName('Alice', 'Smith');
        result === 'Alice Smith';  // no assertion!
      });
    answer: "The test has no assertion — it will always pass regardless of what formatFullName returns. Use expect(result).toBe('Alice Smith') to actually verify the value."
---

# Hook

Your first pull request is rejected: "No tests." You open a blank test file and stare at it. Where do you start? What is the syntax? What matchers do you need?

Vitest is the standard unit testing tool for modern Vite/React projects. In this lesson you will learn everything you need to write your first tests with confidence.

# Lore Introduction

The Academy's testing chamber is simple: a surface for the artefact, a set of standard probes, and a ledger to record results. New artificers sometimes fear the chamber, imagining it to be complicated. But a journeyman artificer who knows the three tools — the placement rune (`describe`), the probe command (`it`), and the expectation seal (`expect`) — can test any artefact they forge.

These are your three tools. Learn them well.

# Core Learning

## Concept Introduction

**Vitest** is a fast, modern test runner built for Vite projects. It uses the same syntax as Jest but runs significantly faster because it shares the Vite transform pipeline.

A Vitest test file has three main concepts:

```ts
import { describe, it, expect } from 'vitest';

describe('formatPrice', () => {           // Groups related tests
  it('formats a GBP price correctly', () => { // One test case
    const result = formatPrice(49.99, 'GBP');
    expect(result).toBe('£49.99');            // Assertion
  });
});
```

- **`describe`** — a named block that groups related tests. Optional but recommended for organisation.
- **`it` / `test`** — defines a single test. Takes a description and a callback function.
- **`expect`** — wraps the actual value and chains to a matcher.

## Matchers

Matchers are the assertions — they define what the expected value should be:

```ts
// Primitive equality (uses ===)
expect(2 + 2).toBe(4);
expect('hello').toBe('hello');

// Deep equality for objects and arrays (compares structure)
expect({ name: 'Alice' }).toEqual({ name: 'Alice' });
expect([1, 2, 3]).toEqual([1, 2, 3]);

// Truthiness
expect(true).toBeTruthy();
expect(null).toBeFalsy();
expect(null).toBeNull();
expect(undefined).toBeUndefined();

// Strings
expect('hello world').toContain('world');
expect('hello').toMatch(/^hell/);

// Numbers
expect(10).toBeGreaterThan(5);
expect(3.14159).toBeCloseTo(3.14, 2);

// Arrays
expect([1, 2, 3]).toHaveLength(3);
expect([1, 2, 3]).toContain(2);

// Errors
expect(() => parseJSON('bad')).toThrow();
expect(() => divide(1, 0)).toThrow('Cannot divide by zero');

// Negation — prefix any matcher with .not
expect('hello').not.toBe('world');
expect([]).not.toContain(5);
```

## A Complete Test File

```ts
// utils/formatPrice.test.ts
import { describe, it, expect } from 'vitest';
import { formatPrice } from './formatPrice';

describe('formatPrice', () => {
  describe('GBP formatting', () => {
    it('formats a whole number', () => {
      expect(formatPrice(10, 'GBP')).toBe('£10.00');
    });

    it('formats a decimal', () => {
      expect(formatPrice(49.99, 'GBP')).toBe('£49.99');
    });

    it('rounds to two decimal places', () => {
      expect(formatPrice(1.999, 'GBP')).toBe('£2.00');
    });
  });

  describe('edge cases', () => {
    it('handles zero', () => {
      expect(formatPrice(0, 'GBP')).toBe('£0.00');
    });

    it('throws for negative prices', () => {
      expect(() => formatPrice(-1, 'GBP')).toThrow('Price cannot be negative');
    });
  });
});
```

## Running Tests

```bash
# Run all tests once
npx vitest run

# Run in watch mode (re-runs on file save)
npx vitest

# Run a specific file
npx vitest run src/utils/formatPrice.test.ts

# Show coverage
npx vitest run --coverage
```

## Common Mistakes

**Using `toBe` for objects/arrays.** `expect({a: 1}).toBe({a: 1})` fails because `===` compares references, not values. Use `toEqual` for objects and arrays.

**Writing assertions outside the callback.** Assertions placed outside a test's callback will not run during the test — they become orphaned.

**Empty tests that always pass.** `it('works', () => {})` — a test with no assertions always passes. Vitest will warn about this if you enable the `expect.hasAssertions()` call.

**Testing too many things in one test.** One test should have one clear purpose. If a test is called "handles all price formats", it is probably testing too much.

## Mini Summary

Vitest tests are built from `describe` (grouping), `it`/`test` (individual cases), and `expect` with a matcher (assertion). Common matchers include `toBe` (strict equality), `toEqual` (deep equality), `toContain`, `toThrow`, and their negations via `.not`. Well-named tests and focused assertions make test output readable and failures easy to diagnose.

# Guided Practice Quest

Work through the steps to practice reading and writing Vitest test syntax and selecting the right matcher.

# Solo Practice Quest

Write a full describe block for a `slugify(text: string)` function that converts a string to a URL slug (e.g. `slugify('Hello World')` → `'hello-world'`). Include:

1. At least 4 test cases covering: basic conversion, special characters, multiple spaces, empty string
2. At least one test using `.not`
3. Correct matcher choices

Write the test code directly in your answer.

# Integration

**Design — Naming and Communication:** Test names are a form of documentation. `it('returns null when the input is empty')` communicates behaviour clearly to any developer reading the test output. Treating test names with the same care as variable or function names makes a test suite a readable specification of the system's behaviour.

**Mathematics — Formal Specification:** A complete test suite is a partial formal specification: it enumerates known inputs and their expected outputs. While it cannot prove correctness for all inputs (unlike a formal mathematical proof), it builds evidence through sampled cases — analogous to empirical testing in mathematics and science.

# Lore Conclusion

The testing chamber's three tools have been standard at the Academy for generations: the placement rune, the probe command, the expectation seal. Artificers who master these three can verify any artefact they create. You now hold those tools. Write your first tests today — even a handful of simple assertions — and you have begun the habit of a professional engineer.

---
