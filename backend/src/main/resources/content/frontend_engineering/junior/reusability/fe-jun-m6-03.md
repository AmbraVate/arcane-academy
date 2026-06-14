---
id: fe-jun-m6-03
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m6
moduleTitle: "Module 6: Component Design"
moduleGlyph: "🧩"
moduleSortOrder: 6
topicSlug: reusability
topicTitle: "Reusability"
topicSortOrder: 1
lesson: when_not_to_reuse
title: "When Not to Reuse"
sortOrder: 3
difficulty: 5
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m6-01, fe-jun-m6-02]
integrationDomains: [philosophy, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies a specific scenario where premature abstraction caused or would cause problems"
    - "Explains the 'rule of three' or equivalent heuristic in their own words"
    - "Acknowledges that duplication is sometimes the correct short-term choice"
  keywords: [abstraction, duplication, rule of three, premature, wrong]
  modelAnswer: |
    The rule of three says: duplicate once, abstract on the third use. This avoids locking in a wrong abstraction before the true shape of the problem is clear. A wrong abstraction — one that forces different concepts into a single component via ever-growing conditional logic — is harder to maintain than duplication. Sometimes two components that look similar today will diverge tomorrow, making the forced abstraction actively harmful.
guidedSteps:
  - id: fe-jun-m6-03-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "You have two components that look nearly identical. What is the best next step according to the 'rule of three'?"
    inputConfig:
      options:
        - "Immediately abstract them into one shared component"
        - "Leave them as separate components and wait for a third use-case"
        - "Delete one and only keep one version"
        - "Add a flag prop to the first component to handle the second use-case"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Leave them as separate components and wait for a third use-case"]
      rejectedFeedback: "The rule of three says: wait until you have three similar uses before abstracting. Two similarities might be coincidental — the components may diverge."
    hint: "What is the cost of waiting vs the cost of the wrong abstraction?"
    reflectionPrompt: "What are the consequences if you abstract two components that later need to diverge?"
  - id: fe-jun-m6-03-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "A shared FormField component has grown to 15 props including showInvoiceFields, isProfileForm, and hideForMobile. What problem does this indicate?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [abstraction, over, wrong, separate, different, split]
      rejectedFeedback: "When a component needs context-specific conditional flags, it is a sign the abstraction has consumed too many distinct concepts. It should be split."
    hint: "Props like isProfileForm suggest the component 'knows' it is used in specific places — that breaks the purpose of abstraction."
    reflectionPrompt: "At what point does a 'reusable' component stop being reusable and become a tangle of special cases?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which scenario is the clearest sign of a wrong abstraction?"
    options:
      - "A component with 3 optional props"
      - "A component with a boolean prop called isInvoicePage that changes its layout"
      - "A component that requires a label prop"
      - "A component that uses a default variant"
    correctIndex: 1
    feedback: "Context-specific props (isInvoicePage) mean the component 'knows' about specific call sites. That is a wrong abstraction — the component is no longer truly generic."
retrieval:
  recall: "What is the rule of three and why does it help avoid premature abstraction?"
  explain: "Why is a wrong abstraction sometimes worse than duplication?"
  mistakeId:
    code: |
      interface ListItemProps {
        title: string;
        isUserList?: boolean;
        isProductList?: boolean;
        isOrderList?: boolean;
        showAvatar?: boolean;
        showPrice?: boolean;
        showOrderStatus?: boolean;
      }
    answer: "This component has absorbed three distinct concepts (user lists, product lists, order lists). The boolean flags are a smell. Each list type should be its own component — they have different data shapes and different rendering needs."
---

# Hook

Six months ago, you abstracted all your list items into one `ListItem` component to keep things DRY. Today it has 23 props, seven of which are booleans that enable completely different layouts. Adding a new feature requires understanding the entire component to avoid breaking the others. Congratulations: you have created a monster.

DRY done wrong is worse than no DRY at all.

# Lore Introduction

There is an old Academy parable about an artificer who tried to forge a single rune that could ward against fire, water, lightning, and shadow all at once. The rune grew so complex that it took an hour to inscribe correctly — and any error caused all protections to fail. Simple, focused runes, inscribed where needed, proved far more reliable.

Not every repetition is a problem. Some things *should* be duplicated — at least for now.

# Core Learning

## Concept Introduction

The drive to reuse is healthy. Taken too far, it produces **over-abstraction**: a component so loaded with conditional logic that it is harder to understand and change than three separate components would have been.

The key insight is: **duplication is not always the problem. The wrong abstraction is worse.**

A classic heuristic is the **Rule of Three**:

1. First use — write it directly.
2. Second use — copy it. Two similar things might just be a coincidence.
3. Third use — now you have evidence of a pattern. Abstract.

Waiting for the third use prevents you from locking in an abstraction before you fully understand the problem shape.

## Why It Matters

When you abstract too early, you make a bet: that all future uses of the component will fit the interface you designed. If you bet wrong, every new use-case either:

- Forces an awkward new prop onto the component (`isSpecialCase?: boolean`)
- Gets excluded from the abstraction, leaving inconsistency anyway

Over time, a prematurely abstracted component accumulates these special-case props until it is unmaintainable. This is sometimes called **the abstraction trap**: you paid the cost of abstraction but none of the benefits materialised.

## Worked Example

A premature abstraction being stretched beyond its purpose:

```tsx
// Started as a reusable card. Now it knows too much.
interface CardProps {
  title: string;
  isProfileCard?: boolean;    // shows avatar and role
  isProductCard?: boolean;    // shows price and stock
  isEventCard?: boolean;      // shows date and RSVP button
  isCompact?: boolean;
  showFooterDivider?: boolean;
  legacyMode?: boolean;       // for the old dashboard only
}

const Card = ({ title, isProfileCard, isProductCard, isEventCard, ...rest }: CardProps) => {
  if (isProfileCard) return <ProfileCardLayout title={title} {...rest} />;
  if (isProductCard) return <ProductCardLayout title={title} {...rest} />;
  if (isEventCard)   return <EventCardLayout title={title} {...rest} />;
  return <DefaultCardLayout title={title} {...rest} />;
};
```

This is not one component — it is three components pretending to be one. The correct solution is three separate, focused components: `ProfileCard`, `ProductCard`, `EventCard`. They may share a `BaseCard` layout helper internally, but they have separate interfaces.

The "before" state — three separate components — would have been healthier.

## When Duplication Is the Right Choice

- **The two things are similar today but likely to diverge.** A `UserProfileCard` and an `InvoiceCard` share structure now but will evolve differently. Keep them separate.
- **Combining them requires context-specific flags.** If a prop name contains a page name, a feature name, or the word "legacy", that is a sign of a wrong abstraction.
- **The abstraction saves very little.** If two components are two lines of JSX, the overhead of a shared abstraction (import path, props types, documentation) may exceed the benefit.

## Common Mistakes

**Adding boolean flags for special cases.** Each `isSomethingSpecific` prop is a sign the abstraction is absorbing a concept it was not designed for.

**Abstracting based on visual similarity alone.** Two components can look identical and represent completely different domain concepts. `InvoiceLineItem` and `CartLineItem` may look the same today but diverge as features evolve.

**Feeling obligated to DRY everything.** DRY is a guideline, not a law. The goal is maintainable code — sometimes that means two simple components rather than one complex one.

## Mini Summary

Premature reuse can create abstractions worse than the duplication they replaced. The rule of three advises waiting for the third use-case before abstracting. Context-specific props are a signal that an abstraction has grown wrong. Sometimes duplication is the correct, maintainable choice.

# Guided Practice Quest

Work through the steps to identify over-abstracted components and decide when to split rather than merge.

# Solo Practice Quest

Think of (or invent) a component that started simple but grew into a tangle of conditional props. Describe:

1. What the component was originally designed for
2. What special-case props were added over time
3. How you would refactor it — would you split it, or is there a clean common abstraction?

Write 4–6 sentences with a clear recommendation.

# Integration

**Philosophy — Occam's Razor:** "Entities should not be multiplied beyond necessity." In software, this applies both ways: don't duplicate when abstraction is clean, but don't abstract when duplication is simpler. The principle counsels finding the *minimum necessary* solution — not always the most abstract one.

**Psychology — Sunk Cost Fallacy:** Developers often resist splitting a component because they invested effort in building the abstraction. Recognising that the time already spent is a sunk cost — and that the correct action now is to split — is a genuine cognitive challenge. Good engineers make decisions based on future cost, not past investment.

# Lore Conclusion

Wisdom at the Arcane Academy is not measured by the number of runes an artificer can consolidate into one. It is measured by knowing *when* to consolidate and *when* to leave things separate. The most respected artificers in the library are those who refactored a bloated master rune into three clean, focused ones — and the codebase ran better for it.

---
