---
id: fe-jun-m7-03
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
lesson: testing_pure_functions
title: "Testing Pure Functions"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m7-01, fe-jun-m7-02]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly applies the Arrange-Act-Assert pattern to a test"
    - "Identifies and tests at least two edge cases for a function"
    - "Explains why pure functions are the easiest things to unit test"
  keywords: [arrange, act, assert, pure, edge, case, boundary, input, output]
  modelAnswer: |
    Arrange-Act-Assert structures a test into three clear phases: set up the inputs (arrange), call the function (act), and verify the output (assert). Pure functions are the easiest to unit test because they have no side effects and no external dependencies — the only thing needed is an input value. Edge cases include boundary conditions (0, -1, empty string, null), invalid inputs, and maximum/minimum values. A well-tested function has tests for the happy path, the edge cases, and the error cases.
guidedSteps:
  - id: fe-jun-m7-03-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "What does the 'Arrange' phase of Arrange-Act-Assert involve?"
    inputConfig:
      options:
        - "Writing the assertion that checks the result"
        - "Calling the function being tested"
        - "Setting up the inputs and any required state before calling the function"
        - "Cleaning up after the test"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Setting up the inputs and any required state before calling the function"]
      rejectedFeedback: "Arrange is the setup phase: define your inputs, create any needed objects, set any state. Act is the call. Assert is the verification."
    hint: "If you are testing calculateTax(income, rate), the Arrange phase is defining income = 50000 and rate = 0.2."
    reflectionPrompt: "For a simple pure function, is the Arrange phase sometimes just one line? Why?"
  - id: fe-jun-m7-03-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Name three types of edge cases you should consider when testing a function that accepts a numeric argument."
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [zero, negative, null, undefined, boundary, NaN, large, infinity, maximum]
      rejectedFeedback: "Good edge cases for numbers: zero (boundary), negative numbers, very large numbers, NaN, null/undefined (if the type allows it), and boundary values at the limits of the expected range."
    hint: "Think about what numbers might cause unexpected behaviour: zero, negatives, non-numbers passed in."
    reflectionPrompt: "Why do bugs often hide at the boundaries (0, -1, max) rather than in the middle of a valid range?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A function clampValue(n, min, max) returns n constrained between min and max. Which test case is MOST important to include?"
    options:
      - "clampValue(5, 1, 10) — n is within range"
      - "clampValue(0, 1, 10) — n is exactly at the boundary"
      - "clampValue(100, 1, 10) — n exceeds max"
      - "All three are equally important"
    correctIndex: 3
    feedback: "All three are important: within range (happy path), at exact boundary (edge case), and out of range (core functionality). Comprehensive testing covers all three."
retrieval:
  recall: "What are the three phases of the Arrange-Act-Assert pattern?"
  explain: "Why are pure functions easier to unit test than functions with side effects?"
  mistakeId:
    code: |
      it('validates email', () => {
        expect(isValidEmail('test@example.com')).toBe(true);
        expect(isValidEmail('notanemail')).toBe(false);
        expect(isValidEmail('')).toBe(false);
        expect(isValidEmail(null)).toBe(false);
        expect(isValidEmail('user@')).toBe(false);
        expect(isValidEmail('@domain.com')).toBe(false);
      });
    answer: "Having 6 assertions in one test makes it hard to identify which case failed. Split into separate it blocks: it('accepts valid email'), it('rejects missing @'), it('rejects empty string'), etc. When one fails, the test name tells you exactly what broke."
---

# Hook

You finish writing a `calculateDiscount` function. It works perfectly on the test data the product manager gave you: 10% off orders over £50. Three days after shipping, a support ticket arrives: someone got a negative discount on a £0.01 order. You never tested edge cases.

Testing pure functions — especially their edge cases — is the most reliable bug prevention available.

# Lore Introduction

Before an artefact leaves the Academy's testing chamber, the artificer runs the Standard Probe Sequence. First, they test normal conditions. Then they apply the Edge Probes: zero load, maximum load, corrupted input. Every artificer knows that the normal case is almost never where artefacts fail. It is the edge — the corner, the boundary, the unexpected input — where craftsmanship is tested.

Your edge cases are the Academy's Edge Probes.

# Core Learning

## Concept Introduction

**Arrange-Act-Assert (AAA)** is a pattern for structuring unit tests clearly:

- **Arrange:** set up the inputs and preconditions
- **Act:** call the function under test
- **Assert:** verify the output

This structure makes each phase of the test explicit and readable:

```ts
it('calculates 10% discount on an eligible order', () => {
  // Arrange
  const orderTotal = 100;
  const discountRate = 0.10;

  // Act
  const result = calculateDiscount(orderTotal, discountRate);

  // Assert
  expect(result).toBe(10);
});
```

For simple tests, Arrange and Act may merge into one line, but the Assert is always explicit.

## Why Pure Functions Are Ideal Unit Test Targets

A **pure function** has no side effects and depends only on its inputs. This means:
- No mocking required (no HTTP, no database, no globals)
- The test is fully self-contained
- The same inputs always produce the same outputs — tests are deterministic

Any utility function, formatter, validator, or calculator in your codebase is a perfect unit test target.

## Testing Validators

```ts
// utils/validators.ts
export const isValidEmail = (email: string): boolean => {
  if (!email) return false;
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
};

// utils/validators.test.ts
describe('isValidEmail', () => {
  it('accepts a standard email address', () => {
    expect(isValidEmail('user@example.com')).toBe(true);
  });

  it('accepts subdomains', () => {
    expect(isValidEmail('user@mail.example.com')).toBe(true);
  });

  it('rejects empty string', () => {
    expect(isValidEmail('')).toBe(false);
  });

  it('rejects missing @ symbol', () => {
    expect(isValidEmail('userexample.com')).toBe(false);
  });

  it('rejects missing domain', () => {
    expect(isValidEmail('user@')).toBe(false);
  });

  it('rejects missing local part', () => {
    expect(isValidEmail('@example.com')).toBe(false);
  });
});
```

## Testing Formatters with Edge Cases

```ts
// utils/formatDate.ts
export const formatDate = (date: Date | null, locale = 'en-GB'): string => {
  if (!date) return '—';
  return date.toLocaleDateString(locale, { day: 'numeric', month: 'short', year: 'numeric' });
};

describe('formatDate', () => {
  it('formats a valid date', () => {
    const date = new Date('2024-06-15');
    expect(formatDate(date)).toBe('15 Jun 2024');
  });

  it('returns dash for null input', () => {
    expect(formatDate(null)).toBe('—');
  });

  it('respects the locale parameter', () => {
    const date = new Date('2024-06-15');
    expect(formatDate(date, 'en-US')).toBe('Jun 15, 2024');
  });
});
```

## Testing Calculators

```ts
// utils/discount.ts
export const calculateDiscount = (total: number, rate: number): number => {
  if (total < 0) throw new Error('Total cannot be negative');
  if (rate < 0 || rate > 1) throw new Error('Rate must be between 0 and 1');
  return Math.round(total * rate * 100) / 100;
};

describe('calculateDiscount', () => {
  it('calculates 10% of 100', () => {
    expect(calculateDiscount(100, 0.10)).toBe(10);
  });

  it('handles zero total', () => {
    expect(calculateDiscount(0, 0.10)).toBe(0);
  });

  it('handles zero rate', () => {
    expect(calculateDiscount(100, 0)).toBe(0);
  });

  it('rounds to two decimal places', () => {
    expect(calculateDiscount(99.99, 0.10)).toBe(10);
  });

  it('throws for negative total', () => {
    expect(() => calculateDiscount(-1, 0.10)).toThrow('Total cannot be negative');
  });

  it('throws for rate above 1', () => {
    expect(() => calculateDiscount(100, 1.5)).toThrow('Rate must be between 0 and 1');
  });
});
```

## Edge Case Checklist

For any function, consider testing:
- **Zero / empty:** `0`, `''`, `[]`, `{}`
- **Null / undefined:** if the type allows
- **Boundary values:** minimum valid input, maximum valid input, one below and one above
- **Negative numbers:** if the domain includes negatives
- **Invalid type inputs:** if the function should handle them gracefully
- **Large inputs:** performance edge cases

## Common Mistakes

**Testing only the happy path.** One test that passes a perfect input is not a test suite — it is a false sense of security.

**Multiple unrelated assertions in one test.** Each test should have a single, clear purpose. When a test with 6 assertions fails, you know *a* case is broken — but not *which* without reading carefully.

**Over-testing getter properties.** `expect(user.name).toBe('Alice')` is testing JavaScript object access, not your code. Test your functions, not language features.

## Mini Summary

Arrange-Act-Assert gives every unit test a clear structure: setup, call, verify. Pure functions are the ideal unit test target — no mocks needed, deterministic, self-contained. A thorough test suite covers the happy path, edge cases (zero, null, boundary), and error cases. One assertion per concept makes test failures immediately informative.

# Guided Practice Quest

Work through the steps to apply Arrange-Act-Assert and identify edge cases for pure utility functions.

# Solo Practice Quest

Write a full test suite for this function:

```ts
export const truncate = (text: string, maxLength: number): string => {
  if (text.length <= maxLength) return text;
  return text.slice(0, maxLength - 3) + '...';
};
```

Include: exact boundary (text.length === maxLength), below boundary, above boundary, empty string, maxLength of 3, and any other edge cases you can think of. Write the complete test code.

# Integration

**Mathematics — Boundary Value Analysis:** In formal testing theory, Boundary Value Analysis (BVA) is the practice of testing at and around boundary conditions. For `clamp(n, 1, 10)`, BVA says test at `1`, `10`, `0`, and `11`. The theory is that bugs cluster near boundaries — where the conditions switch from one branch to another. Mathematical testing methodology confirms what experienced developers learn empirically.

**Psychology — Test-Driven Confidence:** Developers who write tests as they code report higher confidence in their work, lower stress before deployments, and less anxiety when refactoring. The cognitive benefit is real: a test suite is an external memory system that relieves the developer from holding all edge cases in their head simultaneously.

# Lore Conclusion

The Academy's Edge Probe Sequence is not optional — it is the final certification before an artefact leaves the workshop. The artificers who run it most diligently are those who have once shipped a flawed artefact and watched it fail in the field. Learn from the wisdom of those who came before: test the edges before they find your users. A thorough test suite is not paranoia. It is professional responsibility.

---
