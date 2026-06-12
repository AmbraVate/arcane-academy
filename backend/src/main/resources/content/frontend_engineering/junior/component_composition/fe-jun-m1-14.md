---
id: fe-jun-m1-14
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m1
moduleTitle: "Module 1: React Foundations"
moduleGlyph: "⚛️"
moduleSortOrder: 1
topicSlug: component_composition
topicTitle: "Component Composition"
topicSortOrder: 5
lesson: composing_components
title: "Composing Components"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m1-13]
integrationDomains: [design, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what composition means in React"
    - "Builds a complex UI by composing simple components"
    - "Distinguishes composition from inheritance"
    - "Applies single responsibility when composing"
  keywords: [composition, inheritance, single responsibility, nesting, combine, combine, tree, building blocks]
  modelAnswer: |
    Composition in React means building complex UIs by combining simple components. It is preferred over inheritance because it is more flexible — you can compose any combination of behaviours without deep class hierarchies. React's documentation explicitly recommends composition over inheritance for code reuse.
guidedSteps:
  - id: fe-jun-m1-14-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Why does React recommend composition over inheritance for code reuse?"
    inputConfig:
      options:
        - "JavaScript doesn't support inheritance"
        - "Composition is more flexible — you can combine any mix of behaviours without rigid class hierarchies"
        - "Inheritance is slower"
        - "Composition requires less code"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Composition is more flexible — you can combine any mix of behaviours without rigid class hierarchies"]
      rejectedFeedback: "Inheritance creates rigid hierarchies. Composition lets you mix and match behaviours freely. React components with children can compose any combination of behaviours."
    hint: "Think about what happens when you need a component that combines features of two unrelated base classes."
    reflectionPrompt: "The 'composition over inheritance' principle applies throughout software engineering, not just React. It's a core object-oriented design principle (the Gang of Four recommended it in 1994)."
  - id: fe-jun-m1-14-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Describe a ProfilePage composed of at least four smaller components. What components would you create and how do they combine?"
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [Avatar, Header, Bio, Stats, Card, Button, component]
      rejectedFeedback: "Example: ProfilePage > Header (Avatar + Name + Role), BioSection (text), StatsBar (follower counts), PostGrid (PostCard items). Each component has one job; the page composes them."
    hint: "Think about the natural sections of a social profile page."
    reflectionPrompt: "Good component decomposition is a design skill. The question 'what belongs together?' is the same question a UX designer asks when grouping elements."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does 'single responsibility' mean for a component?"
    options:
      - "A component should only accept one prop"
      - "A component should do one thing well and be composable with others"
      - "A component should render one element"
      - "A component should have one state variable"
    correctIndex: 1
    feedback: "Single responsibility: one component = one concern. A UserCard should display a user, not also fetch the user, manage authentication, and control navigation. Keep components focused so they are reusable and testable."
retrieval:
  recall: "What is the difference between composition and inheritance in React?"
  explain: "How does single responsibility make components more reusable?"
  mistakeId:
    code: "Building one large component with all logic inside rather than composing"
    answer: "God components — components that do everything — become impossible to test, hard to reuse, and slow (every state change re-renders the whole thing). Break them into focused components composed together. This is the core practice that separates junior from mid-level React engineers."
---

# Hook

LEGO doesn't give you a house. It gives you bricks that combine into houses, ships, castles. The bricks are dumb. The combinations are intelligent. React composition works the same way — simple, focused components combine into complex UIs.

# Lore Introduction

*"No great spell is woven from a single rune,"* Aelindra says. *"It is composed: this rune for protection, this for warmth, this for light. Each does one thing. Combined, they do everything. This is the way of composition."*

# Core Learning

## Concept Introduction

**Composition** means building complex UIs by nesting and combining simple components. React explicitly recommends composition over inheritance.

```jsx
// Composed dashboard
function DashboardPage({ user, stats, recentActivity }) {
  return (
    <PageLayout>
      <Sidebar>
        <UserProfile user={user} />
        <NavMenu />
      </Sidebar>
      <MainContent>
        <StatsGrid stats={stats} />
        <ActivityFeed items={recentActivity} />
      </MainContent>
    </PageLayout>
  );
}
```

Each component has one job. The page composes them. No component knows the others exist.

## Why It Matters

- **Reusability**: `StatsGrid` can be used on any page
- **Testability**: test each component independently
- **Maintainability**: change `ActivityFeed` without touching anything else
- **Performance**: React can re-render only the affected component

## Worked Example

**Instead of one large component:**
```jsx
// Bad — everything in one place
function UserPage({ userId }) {
  // 200 lines of user data, posts, comments, sidebar...
}
```

**Compose multiple focused components:**
```jsx
// Good — each component has one responsibility
function UserPage({ userId }) {
  return (
    <Page>
      <UserHeader userId={userId} />
      <UserPosts userId={userId} />
      <UserSidebar userId={userId} />
    </Page>
  );
}
```

## Common Mistakes

- **God components.** If a component file is 300+ lines, it probably does too many things.
- **Over-decomposing.** Splitting a `<p>` into a `<Paragraph>` component for no reason adds noise.
- **Tight coupling between siblings.** Components at the same level shouldn't know about each other.

## Mental Model

Composing components is plumbing with standard fittings, not welding custom pipework. A welded system (one big component doing everything) is rigid: rerouting anything means cutting metal. Standard fittings — pipes, joints, valves, each with one job and compatible threads — let a plumber assemble any configuration from a small parts catalogue, and *re*-assemble it next month when requirements change. In React the threads are props and children: a `<Page>` accepts any header, a `<Card>` accepts any body, a `<List>` renders whatever item component it's handed. Composition means building screens by *connecting* these parts — `<Page header={<SearchBar />}>` `<CardGrid>...` — rather than building one MegaComponent with flags controlling internal behaviour (`showSearch`, `cardMode`, `withSidebar`...). The flag-driven component is the welded system: every new requirement adds another valve welded into the middle, and soon nobody can change anything without flooding a bathroom. The compositional version grows differently: new requirements mean new small parts, or new arrangements of existing ones, while every old configuration keeps working — because the fittings never changed, only the assembly. When a component's prop list starts reading like a settings menu, it's asking to be broken into fittings.

## Mini Summary

- Composition = building complex UIs from focused, combined components
- Prefer composition over inheritance
- Single responsibility: each component does one thing well
- The page orchestrates; the components fulfil

# Guided Practice Quest

Work through the guided steps on composition and single responsibility.

# Solo Practice Quest

Take a page you know well (e.g., a news article page, a settings page, a checkout page). Decompose it into components with at least three levels of nesting. Write each component name, its responsibility, and its props. Then write 3–4 sentences explaining how this decomposition improves testability and reusability compared to a single large component.

# Integration

**Psychology — Chunking in Cognitive Science**

Cognitive psychology research shows that humans process information better when it's chunked — grouped into meaningful units. Component composition mirrors this: you chunk UI into named, purposeful pieces (Avatar, NavMenu, ActivityFeed) that the brain recognises as meaningful units. A codebase of well-named components is easier to reason about because each component name activates a schema — a mental model of what it does. Large, unnamed blocks of code lack this chunking benefit.

# Lore Conclusion

*"The page,"* Aelindra says, *"is not an achievement. The components are. The page is merely their arrangement. Master the components, and any arrangement becomes effortless."*

---
