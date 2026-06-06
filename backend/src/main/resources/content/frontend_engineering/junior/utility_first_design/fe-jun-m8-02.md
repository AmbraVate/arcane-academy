---
id: fe-jun-m8-02
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m8
moduleTitle: "Module 8: Tailwind CSS"
moduleGlyph: "🎨"
moduleSortOrder: 8
topicSlug: utility_first_design
topicTitle: "Utility-First Design"
topicSortOrder: 1
lesson: tailwind_fundamentals
title: "Tailwind Fundamentals"
sortOrder: 2
difficulty: 4
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m8-01]
integrationDomains: [design, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly identifies what the numeric scale in Tailwind represents (4px per unit)"
    - "Can read and explain at least 4 common Tailwind utilities (e.g. p-4, text-lg, bg-blue-500, flex)"
    - "Explains how to use the Tailwind documentation to look up an unfamiliar class"
    - "Demonstrates understanding of colour shades (e.g. -100 to -900 scale)"
  keywords: [scale, spacing, colour, typography, documentation, utility, px, rem]
  modelAnswer: |
    Tailwind uses a numeric spacing scale where 1 unit = 4px (or 0.25rem). So `p-4` means 16px of padding. Colour utilities follow a shade scale from 50 (lightest) to 900 (darkest) — `bg-blue-500` is the mid-tone blue. Typography utilities like `text-lg` set font size, while `font-bold` sets weight. The Tailwind docs are searchable and group utilities by property, making it easy to find the right class. Common utilities include: `flex`/`grid` for layout, `p-*`/`m-*` for spacing, `text-*` for typography, `bg-*` for backgrounds, `border`/`rounded-*` for borders.
guidedSteps:
  - id: fe-jun-m8-02-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "In Tailwind's spacing scale, what does `p-8` mean in pixels?"
    inputConfig:
      options:
        - "8px of padding"
        - "16px of padding"
        - "32px of padding"
        - "2rem of padding"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["32px of padding"]
      rejectedFeedback: "Tailwind's scale multiplies the number by 4px. So p-8 = 8 × 4 = 32px. p-1 = 4px, p-4 = 16px, p-8 = 32px."
    hint: "Each unit in Tailwind's spacing scale equals 4px."
    reflectionPrompt: "Why might a consistent mathematical scale make it easier to create visually balanced layouts?"
  - id: fe-jun-m8-02-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "What Tailwind class would you use to: set background to medium blue, add 6 units of padding, make text large, and make text white?"
    inputConfig:
      minWords: 8
    markingRule:
      matchMode: CONTAINS
      accepted: [bg-blue, p-6, text-lg, text-white]
      rejectedFeedback: "Think: bg-blue-500 for background, p-6 for padding, text-lg for size, text-white for colour. The format is always property-value."
    hint: "Each utility follows the pattern: property-value. Blue shades range from 100 to 900."
    reflectionPrompt: "How does the naming convention make it possible to guess classes you haven't memorised yet?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `text-gray-700` mean in Tailwind?"
    options:
      - "Gray text at shade 700 (dark gray, near black)"
      - "Text that is 700px wide in gray"
      - "A font-weight of 700 in gray"
      - "Gray text at 70% opacity"
    correctIndex: 0
    feedback: "In Tailwind, colour classes follow `property-colour-shade`. Shade 700 is a dark gray. Shade 100 is very light, 900 is very dark."
retrieval:
  recall: "What is Tailwind's base spacing unit and how does it relate to the numeric scale?"
  explain: "If someone writes `mx-auto`, what does this do and what does the `m` and `x` signify?"
  mistakeId:
    code: |
      // Developer wants 24px of padding and writes:
      <div className="p-24">Content</div>
    answer: "p-24 applies 96px of padding (24 × 4px), not 24px. To get 24px you need p-6 (6 × 4 = 24). The numeric value is a scale unit, not a pixel value."
---

# Hook

A new apprentice opens the Tailwind documentation for the first time and sees hundreds of classes: `p-4`, `mt-6`, `text-xl`, `bg-emerald-500`, `rounded-full`, `border-2`... It looks like an intimidating grimoire. But after ten minutes of practice, a pattern emerges. Every incantation follows the same grammar, and the numeric scales make perfect logical sense. The grimoire is actually a beautifully organised reference manual.

# Lore Introduction

Master Chromaweave tells her students: "Tailwind is not a collection of random spells. It is a system. Learn the system, and you can read any class at a glance — even ones you have never seen before." The apprentices lean in. A system they can learn is far more powerful than a list they must memorise.

# Core Learning

## Concept Introduction

Tailwind's utilities follow consistent patterns that, once understood, let you read and write classes instinctively.

**The Spacing Scale**

Tailwind uses a base-4 spacing scale: 1 unit = 0.25rem = 4px.

| Class | Size |
|-------|------|
| p-1   | 4px  |
| p-2   | 8px  |
| p-4   | 16px |
| p-6   | 24px |
| p-8   | 32px |
| p-16  | 64px |

The axis modifier: `p` = all sides, `px` = left+right, `py` = top+bottom, `pt/pr/pb/pl` = individual sides. The same pattern applies to margins (`m-*`).

**Colour Scale**

Colours follow a shade scale from 50 (near-white) to 950 (near-black):
```
bg-blue-50   // almost white blue
bg-blue-100  // very light
bg-blue-500  // mid-tone (the "pure" shade)
bg-blue-700  // dark
bg-blue-900  // very dark
```

Works for backgrounds (`bg-*`), text (`text-*`), borders (`border-*`), rings (`ring-*`).

**Typography**

```
text-xs / text-sm / text-base / text-lg / text-xl / text-2xl ... text-9xl
font-thin / font-light / font-normal / font-medium / font-semibold / font-bold
leading-tight / leading-normal / leading-loose     (line-height)
tracking-tight / tracking-normal / tracking-wide   (letter-spacing)
```

## Why It Matters

When you understand the scale system, you can:
- Guess classes you haven't memorised (need 20px padding? Try `p-5`)
- Read others' code at a glance
- Maintain consistent spacing without ad-hoc pixel values
- Use the docs efficiently — they're organised by category

## Worked Example

```jsx
// A user profile card built from fundamentals
function ProfileCard({ name, role, avatar }) {
  return (
    <div className="flex items-center gap-4 p-6 bg-white rounded-xl shadow border border-gray-100">
      <img
        src={avatar}
        alt={name}
        className="w-16 h-16 rounded-full object-cover"
      />
      <div>
        <h3 className="text-lg font-semibold text-gray-900">{name}</h3>
        <p className="text-sm text-gray-500 mt-1">{role}</p>
      </div>
    </div>
  );
}
```

Reading this JSX, you can picture the component without running it: flex row, 24px gap, 24px padding, white background, rounded corners, a small shadow, a subtle border, a 64px circular avatar, a large bold name, and a small muted role label.

## Common Mistakes

- **Mixing px and scale** — `p-[16px]` and `p-4` both give 16px, but the arbitrary bracket syntax should be reserved for values outside the scale.
- **Forgetting the axis** — `px-4` adds horizontal padding only. `py-4` adds vertical. A common slip is writing `p-x-4` (invalid).
- **Wrong colour direction** — `text-blue-500` sets text colour; `bg-blue-500` sets background. The property prefix always comes first.

## Mini Summary

Tailwind's fundamentals are a system, not a list. The 4px spacing scale, the 50–950 colour shades, and consistent `property-colour-shade` naming let you read and write any utility confidently once you understand the grammar.

# Guided Practice Quest

Work through the guided steps to practise reading the spacing scale and colour naming conventions.

# Solo Practice Quest

Without looking at the docs, write the Tailwind classes for a button that has: medium blue background (shade 600), white bold text, medium font size, 12px vertical padding, 24px horizontal padding, rounded corners, and a slight shadow.

Then look up each class to verify.

# Integration

**Mathematics:** Tailwind's base-4 scale is a geometric progression that creates visually harmonious spacing ratios. The relationship between adjacent steps creates consistent visual rhythm — the same principle used in musical scales and typographic modular scales.

**Design:** The colour shade system (50–950) mirrors how professional designers think about tints and shades of a hue. Designers use the same vocabulary — "use the 700 shade for text" — making designer-developer handoff smoother.

# Lore Conclusion

Master Chromaweave watches her apprentices style components without a single custom CSS file. "Do you see?" she says. "Once you know that blue-500 is the anchor and shades fan out from there, you don't need to memorise — you reason." The apprentices nod. The system, once understood, is its own documentation.

---
