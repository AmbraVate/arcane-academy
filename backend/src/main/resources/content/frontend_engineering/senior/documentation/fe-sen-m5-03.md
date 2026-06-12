---
id: fe-sen-m5-03
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m5
moduleTitle: "Module 5: Design Systems"
moduleGlyph: "🎨"
moduleSortOrder: 5
topicSlug: documentation
topicTitle: "Documentation"
topicSortOrder: 3
lesson: design_system_documentation
title: "Design System Documentation"
sortOrder: 1
difficulty: 4
estimatedMinutes: 30
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, design]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Explains what Storybook is and what it provides
    - Describes the key elements of good component documentation
    - Explains the difference between component documentation and usage guidelines
    - Explains how to write effective stories (happy path, edge cases, variants)
    - Synthesises why design system documentation is critical for adoption
  keywords: [Storybook, story, args, controls, autodocs, usage, do, dont, props, variant, visual regression, documentation]
  modelAnswer: |
    Storybook is an isolated component development and documentation environment. Each story represents one state of a component. Stories serve multiple purposes: development environment, visual regression test target, and the documentation site for consuming teams.

    Good component documentation has four layers: (1) the rendered component in all its variants and states; (2) the props table (type, description, default, required); (3) usage guidelines (do/don't examples, when to use which variant, accessibility notes); (4) code examples that consumers can copy.

    The difference between component documentation and usage guidelines: component docs describe what the component can do (all props, all variants). Usage guidelines describe when and how to use the component in product contexts — which variant for destructive actions, how to handle disabled states gracefully, what to write in button labels.

    Good stories cover: the default state, each named variant, important edge cases (very long text, empty state, loading state, error state). Each story is a living test — if a story breaks, something changed.

    Documentation drives adoption. A design system component that no one can find or understand doesn't get used — teams build their own. The system's value is proportional to its adoption, and adoption is proportional to documentation quality.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A design system component has 5 variants and 3 sizes. How many Storybook stories should it have at minimum?"
    options:
      - "1 — one story showing the default state"
      - "8 — one per variant plus one per size"
      - "At least one per variant, plus edge cases (loading, disabled, long text)"
      - "15 — every combination of variant × size"
    correctIndex: 2
    feedback: "Each meaningful variant and state should have its own story: primary variant, secondary variant, danger variant, ghost variant, disabled state, loading state, icon-only, with long text. You don't need every combination (15 stories for 5×3 would be excessive) — but each variant and each important state should be independently visible and testable."
  - type: SHORT_TEXT
    prompt: "A Button component in your design system has no usage guidelines — only a props table. What is a consuming engineer likely to do wrong when implementing a confirmation dialog?"
    hint: "What question does 'which variant should I use for a dangerous action?' require?"
  - type: FILL_BLANK
    prompt: "Storybook stories serve as: a development environment, a ___ documentation site, and a target for ___ regression testing."
    answer: "living/interactive; visual"
    hint: "Stories document and test simultaneously."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does Storybook's 'Controls' addon provide?"
    options:
      - "Automated accessibility testing for each story"
      - "An interactive panel to change component props in real-time"
      - "Automatic TypeScript type generation"
      - "Visual regression comparison against a baseline"
    correctIndex: 1
    feedback: "Controls generates an interactive panel from the component's TypeScript types (or manually defined argTypes). Consumers can change variant, size, label, disabled — and see the component update in real-time. This is significantly more useful than static screenshots for understanding a component's capabilities."
  - type: MULTIPLE_CHOICE
    question: "Why should a design system's documentation include 'Do / Don't' examples?"
    options:
      - "To demonstrate that the design system team is thorough"
      - "To explicitly model correct usage and prevent common misuse patterns"
      - "DOs and DON'Ts are required by accessibility standards"
      - "They replace the need for a props table"
    correctIndex: 1
    feedback: "Do/Don't examples make the difference between 'here's what this component can do' and 'here's how to use this component correctly in product contexts'. They prevent misuse: 'DON'T use the danger variant for non-destructive actions.' 'DO place destructive action buttons on the right in dialogs.' These guidelines reduce incorrect usage without requiring a manual review of every implementation."
retrieval:
  recall: "What are the four layers of good design system component documentation?"
  explain: "Why does poor design system documentation reduce adoption — even when the components themselves are excellent?"
  mistakeId:
    code: |
      // The only documentation for the Alert component:
      /**
       * Alert component
       * Props:
       * - type: 'info' | 'success' | 'warning' | 'error'
       * - message: string
       */
    answer: "This documents what the props are, but not how to use the component correctly. Missing: (1) rendered examples of each type variant, (2) when to use each type (error vs warning), (3) do/don't guidance (don't use info for error states), (4) accessibility notes (aria-live for dynamic alerts), (5) code examples. A consuming developer reading this has to guess. They'll make wrong choices and re-invent conventions the design system should have established."
---

# Hook

Your design system has 40 components. Documentation is a props table auto-generated from TypeScript types. Adoption rate: 23%. Teams build their own components instead.

They can't find what they need. They don't know how to use what they find. They don't trust that it covers their use case.

Documentation isn't optional. It's what makes the system usable.

# Lore Introduction

*"The Academy's spell library contains a thousand spells,"* the Head Librarian explains. *"But the index is unclear, the descriptions are brief, and the usage notes are absent. Apprentices spend more time reading the library than casting. Many invent their own spells — inferior, inconsistent."*

She holds up a well-documented spell book. *"Documentation is not about the spells. It is about the caster. Make the caster successful, and the spells are used."*

# Core Learning

## Concept Introduction

**Storybook** is the standard documentation and development environment for design systems. Each **story** captures one component state.

**A complete story file:**
```tsx
// Button.stories.tsx
import type { Meta, StoryObj } from '@storybook/react';
import { Button } from './Button';

const meta: Meta<typeof Button> = {
  title: 'Design System/Button',
  component: Button,
  tags: ['autodocs'],       // generates docs page from stories
  argTypes: {
    variant: {
      description: 'Visual variant for different semantic purposes',
      control: 'select',
    },
  },
};
export default meta;

type Story = StoryObj<typeof Button>;

// One story per meaningful state
export const Primary: Story = { args: { variant: 'primary', children: 'Save Changes' } };
export const Secondary: Story = { args: { variant: 'secondary', children: 'Cancel' } };
export const Danger: Story = { args: { variant: 'danger', children: 'Delete Account' } };
export const Loading: Story = { args: { variant: 'primary', isLoading: true, children: 'Saving...' } };
export const Disabled: Story = { args: { variant: 'primary', isDisabled: true, children: 'Save' } };
export const LongLabel: Story = { args: { variant: 'primary', children: 'This is a very long button label that tests text overflow' } };
```

**The four documentation layers:**
1. **Rendered component** — every variant, every state, edge cases
2. **Props table** — type, description, default, required flag
3. **Usage guidelines** — when/how to use, do/don't examples
4. **Code snippets** — copy-paste examples

**Usage guidelines example:**
```markdown
 ## When to Use

- **Primary**: One per screen. The main call-to-action.
- **Secondary**: Alternative actions alongside a primary button.
- **Danger**: Destructive actions only (delete, remove, revoke).

 ## Do / Don't

✅ DO: Use danger variant for permanent destructive actions
❌ DON'T: Use danger for actions that are reversible
✅ DO: Write button labels that describe the action ('Save Changes', not 'Submit')
❌ DON'T: Use generic labels ('OK', 'Yes', 'Confirm')
```

## Why It Matters

An undocumented design system is a private joke — the components exist, but only their authors can use them, and the system fails at its actual job of scaling design decisions:

- Documentation is the system's user interface: engineers adopt what they can evaluate in two minutes (live examples, copy-paste code, prop tables) and fork or rebuild what they can't
- The most expensive documentation gap is *when and why*: without usage guidance ("use a Banner for page-level, a Toast for transient"), teams pick components by appearance and the design language drifts even with perfect components
- Docs are the contract that makes versioning survivable — migration guides and deprecation notes are what let a system evolve without freezing or breaking its consumers
- Support load is the tell: every Slack question answered by a maintainer is a docs page that doesn't exist, paid for repeatedly

Design systems live or die on adoption, and adoption is a developer-experience problem. Documentation *is* that developer experience — which is why mature systems staff it like a product, not an afterthought.

## Common Mistakes

- **Documenting only the happy path.** Consuming teams encounter the unhappy paths in production — document loading, error, empty, and disabled states.
- **No usage guidelines.** A props table documents capability, not intent. Teams need both.
- **Not maintaining docs when components change.** Outdated documentation is worse than no documentation.
- **Documentation without examples.** Prose descriptions are less useful than a rendered example.

## Mental Model

Component documentation is the difference between IKEA and a lumber yard. Both technically provide everything needed to furnish a room — but IKEA succeeds because each product ships with the full experience around the parts: a showroom where you see it assembled in context (live examples and demos), an instruction sheet with every screw named (prop tables and API reference), warnings about what it's *not* for ("not load-bearing" — usage do's and don'ts), and a catalogue that helps you choose between similar items (when to use Card vs Panel). A lumber yard hands you excellent wood and wishes you luck — which is precisely how teams treat an undocumented system: they wander in, can't tell what anything is for, and go build their own table. If you want people to furnish rooms your way, sell furniture, not timber.

## Mini Summary

- ✔ Storybook provides the documentation, development, and visual regression platform for design systems
- ✔ Each story captures one component state — create stories for all variants and edge cases
- ✔ Documentation needs four layers: rendered examples, props table, usage guidelines, code snippets
- ✔ Do/Don't guidance prevents common misuse without requiring review of every implementation
- ✔ Documentation quality directly drives adoption — undocumented components get forked

# Guided Practice Quest

Work through the guided steps to understand what stories to write and what guidelines to document.

# Solo Practice Quest

Write the documentation plan for an `Alert` component (types: info, success, warning, error; dismissible; with/without title). Describe: which stories to write, what usage guidelines to include, what do/don't examples matter, and what accessibility notes belong in the docs.

# Integration

**Psychology — Cognitive Load and Documentation Design**

Good documentation reduces cognitive load for consumers — they don't have to reason from first principles about how to use a component. This is an application of Sweller's Cognitive Load Theory to technical writing. Intrinsic load (the inherent complexity of the component) cannot be reduced. Extraneous load (confusion about how to use it correctly) can be eliminated by clear documentation. Germane load (learning the system's patterns so they transfer to other components) can be cultivated through consistent documentation structure. The pattern: rendered examples (reduce extraneous load by showing, not telling), props tables (reduce extraneous load by surfacing capabilities), usage guidelines (reduce extraneous load by pre-deciding common questions), do/don't (reduce extraneous load by preventing common mistakes before they occur).

# Lore Conclusion

*"The spell book is complete,"* the Head Librarian says. *"Every spell: demonstrated, described, with guidance on when each form is appropriate and what not to do. The apprentice who reads it casts correctly on the first attempt. The adoption numbers climb."*

---
