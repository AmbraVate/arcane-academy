---
id: fe-jun-m7-06
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m7
moduleTitle: "Module 7: Frontend Testing"
moduleGlyph: "🧪"
moduleSortOrder: 7
topicSlug: component_testing
topicTitle: "Component Testing"
topicSortOrder: 2
lesson: testing_with_props
title: "Testing Components with Props"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m7-04, fe-jun-m7-05]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains how to pass different props in each test"
    - "Describes how to test conditional rendering based on props"
    - "Explains why testing multiple prop combinations matters"
    - "Identifies what boundary cases to test for props"
  keywords: [props, render, conditional, variant, boundary, edge, case, null, undefined, empty]
  modelAnswer: |
    Props control what a component renders, so testing a component means testing how it behaves with different prop values. Pass props directly into render(): render(<Button variant="danger" disabled />). Test conditional rendering by checking what appears (or doesn't) for different prop values. Boundary cases matter: what happens with an empty string, undefined, zero, or a very long value? A component that works for typical props but crashes for edge cases has gaps in its contract — prop-based tests surface these before users do.
guidedSteps:
  - id: fe-jun-m7-06-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "A `<Badge status='active' />` renders green; `<Badge status='inactive' />` renders grey. How should you test this?"
    inputConfig:
      options:
        - "Render once with 'active' and check both colours"
        - "Write two separate tests — one per status value"
        - "Check the component's internal statusColour variable"
        - "Use a snapshot to capture all states at once"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Write two separate tests — one per status value"]
      rejectedFeedback: "Each prop combination should be its own test with its own render. This gives clear failure messages ('active badge should be green — FAILED') and makes it obvious which variant broke. Internal variables should never be tested; snapshots are fragile."
    hint: "One test per behaviour — how would you describe each expected outcome separately?"
    reflectionPrompt: "A test that covers two different behaviours in one test case is harder to read and harder to debug. When it fails, which behaviour broke?"
  - id: fe-jun-m7-06-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "A `<UserCard name={name} />` component renders a user's name. What prop values should you test beyond the happy path?"
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [empty, null, undefined, long, special, character, boundary, edge]
      rejectedFeedback: "Beyond a typical name: empty string '', null/undefined (what does the component show if name is missing?), a very long name (does it overflow?), special characters (apostrophes, unicode). These boundary cases reveal how robust the component contract is."
    hint: "Think about what values the name prop could realistically receive from an API."
    reflectionPrompt: "Props come from external sources — APIs, user input, other components. They can be anything. Testing only happy-path props gives you a false sense of safety."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A `<LoadingSpinner visible={true} />` shows a spinner; `visible={false}` hides it. What should your test file contain?"
    options:
      - "One test that renders with visible=true and checks both cases"
      - "Two tests: one asserting spinner is present, one asserting it is absent"
      - "A snapshot of both states combined"
      - "Only the visible=true test — the false case is obvious"
    correctIndex: 1
    feedback: "Each conditional state deserves its own test. 'Shows spinner when visible=true' and 'hides spinner when visible=false' are two separate behaviours. If one breaks, you'll know exactly which."
retrieval:
  recall: "How do you pass props to a component in an RTL test?"
  explain: "Why is it important to test boundary prop values (null, empty string, undefined) and not just typical values?"
  mistakeId:
    code: |
      // One test checking all variants
      test('Badge renders correctly', () => {
        render(<Badge status="active" />);
        expect(screen.getByText('Active')).toBeInTheDocument();
        render(<Badge status="inactive" />);
        expect(screen.getByText('Inactive')).toBeInTheDocument();
      });
    answer: "Calling render() twice in one test accumulates DOM nodes from both renders in the same container, which can cause unexpected query matches. More importantly, a single test named 'renders correctly' gives no useful failure message. Split into two tests: 'shows Active text when status is active' and 'shows Inactive text when status is inactive'. When one fails, the test name tells you exactly what broke."
---

# Hook

Your `<ProductCard />` component works perfectly in development — real product names, real prices, real images. Then in production: a product with no name crashes the component. A product with a £0 price renders weirdly. A name 200 characters long overflows the card.

The tests passed because you only tested happy-path props. The boundary cases were never checked.

# Lore Introduction

*"You tested the component with a perfect subject,"* Instructor Reva says, reviewing the crash report. *"Full name, valid portrait, known rank. But the archive contains entries with no portrait, entries with no name, entries whose records were corrupted in the last war."*

She places a cracked record on the table. *"Your component met the world — and the world is not perfect. Test with imperfect inputs."*

# Core Learning

## Concept Introduction

Props define a component's **contract**: what it accepts and what it promises to render. Testing with props means:

1. **Testing each significant prop value** — different variants produce different output
2. **Testing conditional rendering** — components often hide/show elements based on props
3. **Testing boundary values** — empty strings, null, undefined, zero, very long values

## Why It Matters

Real data is messy. APIs return `null` instead of a string. Users have names with special characters. Values can be `0` (which is falsy in JavaScript — a common bug source). Tests that only cover typical values give false confidence that the component handles real-world input.

## Worked Example

```jsx
// StatusBadge.jsx
function StatusBadge({ status, label }) {
  if (!label) return null;
  const colour = status === 'active' ? 'green' : 'grey';
  return (
    <span className={`badge bg-${colour}-100 text-${colour}-700`}>
      {label}
    </span>
  );
}
```

```js
// StatusBadge.test.jsx
import { render, screen } from '@testing-library/react';
import { StatusBadge } from './StatusBadge';

// Test each prop variant separately
test('renders with active status', () => {
  render(<StatusBadge status="active" label="Active" />);
  expect(screen.getByText('Active')).toBeInTheDocument();
});

test('renders with inactive status', () => {
  render(<StatusBadge status="inactive" label="Inactive" />);
  expect(screen.getByText('Inactive')).toBeInTheDocument();
});

// Test conditional rendering
test('renders nothing when label is empty', () => {
  render(<StatusBadge status="active" label="" />);
  expect(screen.queryByText('Active')).not.toBeInTheDocument();
});

// Test boundary case
test('renders nothing when label is null', () => {
  render(<StatusBadge status="active" label={null} />);
  expect(document.body.firstChild).toBeNull();
});
```

**Note:** When passing non-string props, use JSX expressions: `label={null}`, `count={0}`, `visible={false}`.

## Common Mistakes

- **Testing only the happy path.** The happy path is usually the case that works without testing. The bugs hide in the edge cases.
- **Rendering twice in one test.** Each render call should be in its own test. Use `cleanup` (automatic in Vitest/Jest with RTL) between tests.
- **Forgetting that `0` is falsy.** A prop `count={0}` will fail `if (count)`. Test the zero case explicitly.
- **Not testing the "nothing" state.** If a component conditionally renders, test both the rendered and not-rendered cases.

## Mini Summary

- Pass different props to render() for each test case
- Test each significant conditional branch with its own test
- Include boundary values: null, undefined, empty string, 0, very long strings
- Keep tests small and named clearly — one behaviour per test

# Guided Practice Quest

Work through the two guided steps to verify you can identify which prop values need testing beyond the obvious happy path.

# Solo Practice Quest

Given this component:
```jsx
function Greeting({ name, isLoggedIn }) {
  if (!isLoggedIn) return <p>Please log in.</p>;
  if (!name) return <p>Welcome, guest!</p>;
  return <p>Welcome, {name}!</p>;
}
```
Write the test cases you would write for this component. List the prop combinations and what each test should assert.

# Integration

**Mathematics — Equivalence Partitioning**

Mathematically, you cannot test every possible prop value (there are infinite strings). Equivalence partitioning solves this: divide inputs into classes where all values in a class produce the same behaviour. For a `name` prop: the "present string" class (any non-empty string), the "empty string" class, and the "null/undefined" class. One representative value from each class is sufficient. Boundary value analysis supplements this: test the edges between classes (empty vs one character, 0 vs 1 vs -1 for numbers). These two mathematical techniques, applied to component testing, give you maximum coverage with minimum tests.

# Lore Conclusion

*"The archive is complete now,"* Instructor Reva says, scanning the test file. *"You have tested the pristine records, the empty records, and the corrupted ones. The component meets its contract — not just in the ideal case, but in the real world."*

The prop-testing seal is affixed to your grimoire.

---
