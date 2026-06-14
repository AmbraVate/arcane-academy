---
id: fe-jun-m7-12
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m7
moduleTitle: "Module 7: Frontend Testing"
moduleGlyph: "🧪"
moduleSortOrder: 7
topicSlug: test_reliability
topicTitle: "Test Reliability"
topicSortOrder: 4
lesson: test_coverage
title: "Test Coverage"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m7-10, fe-jun-m7-11]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what code coverage measures and what it doesn't measure"
    - "Explains why 100% coverage doesn't mean bug-free code"
    - "Describes what should be prioritised over coverage percentage"
    - "Identifies what kinds of code are most important to test"
  keywords: [coverage, line, branch, confidence, quality, false, sense, behaviour, important, critical]
  modelAnswer: |
    Coverage measures which lines/branches of code were executed during tests — it does not measure whether those tests verify the right behaviour. A test can execute every line with meaningless assertions and achieve 100% coverage. The goal is confidence in system behaviour, not a coverage number. Prioritise testing: critical user journeys (login, checkout, core features), error handling paths, and complex business logic. Simple rendering components with no logic may need minimal tests. Coverage is a useful signal for finding untested areas — not a target to hit at any cost.
guidedSteps:
  - id: fe-jun-m7-12-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A developer achieves 100% line coverage by writing: `test('renders', () => { render(<App />); }). What problem does this reveal?"
    inputConfig:
      options:
        - "The test is too simple — it should use async/await"
        - "Coverage is high but the test asserts nothing, giving false confidence"
        - "render() doesn't count toward coverage"
        - "100% coverage is impossible to achieve without assertions"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Coverage is high but the test asserts nothing, giving false confidence"]
      rejectedFeedback: "Coverage measures code execution, not assertion quality. A test can execute every line without asserting anything meaningful. This test would pass even if the component crashed after render — because nothing checks the output. Coverage tells you what code ran; it doesn't tell you whether the code did the right thing."
    hint: "Coverage measures execution, not correctness."
    reflectionPrompt: "Coverage without assertions is theatre. The metric looks good; the safety net has holes you can fall through."
  - id: fe-jun-m7-12-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Your team has a policy: minimum 80% test coverage. What should you tell a junior developer who is writing trivial tests just to hit the number?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [confidence, behaviour, meaningful, quality, target, metric, game, Goodhart, important]
      rejectedFeedback: "Coverage targets create perverse incentives — developers optimise for the number instead of for quality. Tell them: coverage is a signal to help you find untested areas, not a goal to hit at any cost. Tests that exist only to increase a number provide no safety. What matters is: are the important, complex, and risky behaviours covered by meaningful assertions?"
    hint: "What is the purpose of tests? Is coverage the same as that purpose?"
    reflectionPrompt: "Goodhart's Law: when a measure becomes a target, it ceases to be a good measure. Coverage targets are a classic example — the metric is useful as a signal but harmful as a mandate."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which code should you prioritise testing, even if it means lower overall coverage?"
    options:
      - "Simple presentational components with no logic"
      - "Utility functions and business logic with complex branching"
      - "Static constants and configuration files"
      - "Third-party library code"
    correctIndex: 1
    feedback: "High-value tests cover code where bugs hurt most: utility functions that calculate prices, validate inputs, or transform data; authentication flows; payment logic; complex conditional branches. Simple components that just render props need minimal testing. Third-party code is the library's responsibility. Focus testing effort where correctness matters most."
retrieval:
  recall: "What does code coverage measure? What does it not measure?"
  explain: "Why can a codebase have 100% test coverage and still contain significant bugs?"
  mistakeId:
    code: |
      // Team rule: no PR merged under 90% coverage
      // Developer adds this to hit the target:
      test('all functions exist', () => {
        expect(typeof calculateDiscount).toBe('function');
        expect(typeof formatPrice).toBe('function');
        expect(typeof validateEmail).toBe('function');
      });
    answer: "These tests check that functions exist — not that they work correctly. calculateDiscount could return NaN, formatPrice could return undefined, validateEmail could accept everything — all tests pass, coverage hits 90%, and the bugs ship. Meaningful tests for these would: call calculateDiscount with various inputs and assert the correct discount, call validateEmail with valid and invalid addresses and assert true/false. Coverage from meaningful assertions is worth having. Coverage from existence checks is not."
---

# Hook

The PR dashboard shows 95% test coverage — the highest it's ever been. Two days after release, a customer reports that the checkout page accepts invalid promo codes and gives 100% discount.

The test file for the promo code validator: `test('promo validator exists', () => { expect(promoValidator).toBeDefined() })`.

Coverage doesn't measure correctness. It measures execution.

# Lore Introduction

*"The census says every room in the Academy has been visited,"* the Headmaster reports. *"But visited is not the same as inspected. Some rooms were merely glanced at — curtains drawn, doors opened and closed."*

He places the census scroll aside. *"Coverage tells you which rooms were entered. It does not tell you whether the dusty corners were examined."*

# Core Learning

## Concept Introduction

**Code coverage** is a metric that measures what percentage of your code is executed during tests.

| Coverage type | What it measures |
|---|---|
| **Line coverage** | Which lines ran |
| **Branch coverage** | Which if/else paths were taken |
| **Function coverage** | Which functions were called |

**What coverage does NOT measure:**
- Whether assertions are meaningful
- Whether edge cases are tested
- Whether the code does the right thing

## Why It Matters

Coverage is a useful tool for finding **untested areas** — large patches of uncovered code are a smell. But as a target or mandate, coverage creates perverse incentives: developers write tests that execute code without asserting anything useful, just to hit a number.

**Goodhart's Law:** *When a measure becomes a target, it ceases to be a good measure.*

## Worked Example

**High coverage, low confidence:**
```js
// 100% line coverage — but what does it verify?
test('discount service', () => {
  applyDiscount(100, 0.1); // executes the code
  applyDiscount(0, 0);     // but asserts nothing
});
```

**Lower coverage, high confidence:**
```js
test('applies 10% discount correctly', () => {
  expect(applyDiscount(100, 0.1)).toBe(90);
});

test('handles zero price', () => {
  expect(applyDiscount(0, 0.1)).toBe(0);
});

test('handles zero discount', () => {
  expect(applyDiscount(100, 0)).toBe(100);
});

test('throws for negative price', () => {
  expect(() => applyDiscount(-10, 0.1)).toThrow();
});
```

**What to prioritise:**
1. Complex business logic (calculations, validations)
2. Critical user journeys (auth, checkout, core features)
3. Error handling paths
4. Code that has caused bugs before

**What needs minimal testing:**
- Simple presentational components (just renders props)
- Configuration files
- Third-party library code

## Common Mistakes

- **Treating coverage as a goal.** It's a signal, not a target.
- **Writing tests only to hit a number.** These tests provide no safety.
- **Ignoring branch coverage.** Line coverage misses `else` paths; branch coverage is more meaningful.
- **Not covering error paths.** The happy path is usually the easy case. Error handling is where bugs hide.

## Mini Summary

- Coverage measures execution, not correctness or assertion quality
- 100% coverage doesn't mean 100% bug-free — assertions determine quality
- Use coverage as a signal to find untested areas, not as a mandate
- Prioritise testing: business logic, critical paths, error handling

# Guided Practice Quest

Work through the two guided steps to challenge your intuitions about coverage and its relationship to test quality.

# Solo Practice Quest

Look at this coverage report (hypothetical): `calculateTax: 45%`, `renderUserAvatar: 98%`, `sendPayment: 12%`. Which function should you prioritise testing next and why? Write 3–4 sentences explaining your reasoning.

# Integration

**Mathematics — Measurement Theory**

Coverage is a proxy metric — it measures something observable (code execution) as a proxy for what you really care about (software correctness). Proxy metrics are useful when the true metric is hard to measure directly. The danger is when the proxy metric diverges from the true metric — and coverage diverges dramatically when assertions are weak. This is the fundamental problem identified in measurement theory: validity (does the metric measure what it claims to?) vs reliability (is it consistent?). Coverage is reliable (consistent across runs) but has low validity (doesn't measure correctness). Good engineers treat it as one signal among many, not as the primary measure of test quality.

# Lore Conclusion

*"The rooms are truly inspected now,"* the Headmaster says, reviewing the test suite. *"Not just visited. Your coverage number is lower — but your confidence is higher. That is the correct trade."*

The testing module is complete. The grimoire's testing chapter seals itself shut.

---
