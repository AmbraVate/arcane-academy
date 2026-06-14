---
id: fe-jun-m1-06
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m1
moduleTitle: "Module 1: React Foundations"
moduleGlyph: "⚛️"
moduleSortOrder: 1
topicSlug: components
topicTitle: "Components"
topicSortOrder: 2
lesson: component_hierarchy
title: "Component Hierarchy"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m1-04, fe-jun-m1-05]
integrationDomains: [mathematics, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains the parent-child relationship between components"
    - "Describes how data flows in a component tree"
    - "Identifies the root component in a React app"
    - "Explains why hierarchy affects state placement decisions"
  keywords: [parent, child, tree, root, hierarchy, props, data flow, App]
  modelAnswer: |
    React components form a tree, with a root component (usually App) at the top. Parent components pass data down to children via props. Data flows one direction — from parent to child — which makes the application predictable. State should be placed in the lowest common ancestor of all components that need it.
guidedSteps:
  - id: fe-jun-m1-06-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "In which direction does data flow in a React component tree?"
    inputConfig:
      options:
        - "Bidirectionally — parent to child and child to parent"
        - "Downward only — from parent to child via props"
        - "Upward only — from child to parent via events"
        - "Sideways — between sibling components directly"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Downward only — from parent to child via props"]
      rejectedFeedback: "React uses one-way (unidirectional) data flow. Props go from parent to child. Children communicate back via callback functions passed as props — but data still flows down."
    hint: "Think about which direction props travel."
    reflectionPrompt: "Unidirectional data flow makes bugs easier to trace. When something is wrong, you follow the data downward from its source."
  - id: fe-jun-m1-06-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "A UserProfile page has a Header, an Avatar, a Bio, and a PostList. Describe the component tree (who is parent, who is child)."
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [UserProfile, Header, Avatar, Bio, PostList, parent, child, tree]
      rejectedFeedback: "UserProfile is the parent. Header, Avatar, Bio, and PostList are its children. UserProfile passes user data down to each child via props."
    hint: "The page component wraps the others."
    reflectionPrompt: "The component that 'owns' data should be the parent. Children receive what they need via props."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Where should state be placed in a component tree?"
    options:
      - "Always in the root App component"
      - "In the lowest common ancestor of all components that need it"
      - "In each component that displays it"
      - "In a separate state file"
    correctIndex: 1
    feedback: "State should be as low as possible — in the lowest common ancestor of all components that need it. Too high means unnecessary re-renders. Too low means sibling components can't access shared data."
retrieval:
  recall: "What is the root component in a typical React application called?"
  explain: "Why is unidirectional data flow (parent to child) easier to debug than bidirectional flow?"
  mistakeId:
    code: "Sibling components can share state by passing props directly to each other"
    answer: "Sibling components cannot pass props directly to each other. Data must travel up to the common parent, then back down. This is called 'lifting state up' and is the correct pattern."
---

# Hook

A React application is not a flat list of components. It's a tree — components nested inside components, all the way down from a single root. Understanding this tree is essential: it determines how data flows, where state should live, and why some changes trigger widespread re-renders while others are isolated.

# Lore Introduction

*"A city is not a pile of buildings,"* Aelindra says, drawing a diagram. *"It is a hierarchy: districts contain streets, streets contain buildings, buildings contain rooms. Each level has authority over those below — and passes instructions down."*

She taps the diagram. *"Your component tree is this city. Know where you stand in it."*

# Core Learning

## Concept Introduction

Every React application has a **component tree** — a hierarchy of parent and child components:

```
App
├── Header
│   ├── Logo
│   └── NavMenu
├── MainContent
│   ├── ArticleList
│   │   ├── ArticleCard
│   │   └── ArticleCard
│   └── Sidebar
└── Footer
```

| Concept | Meaning |
|---|---|
| **Root** | The top-level component (`<App />`) mounted into the DOM |
| **Parent** | A component that renders child components |
| **Child** | A component rendered inside a parent |
| **Props** | Data passed from parent to child |
| **Unidirectional flow** | Data flows only downward (parent → child) |

## Why It Matters

The tree structure determines:
- **Where state lives** (lowest common ancestor)
- **What re-renders when state changes** (the component and all its children)
- **How to communicate between components** (via shared parent)

## Worked Example

```jsx
function App() {
  const user = { name: "Aelindra", role: "Architect" };
  return (
    <div>
      <Header username={user.name} />
      <ProfileCard user={user} />
    </div>
  );
}

function Header({ username }) {
  return <nav>Welcome, {username}</nav>;
}

function ProfileCard({ user }) {
  return <div>{user.name} — {user.role}</div>;
}
```

`App` owns the data. It passes it down to both children. Neither child knows about the other.

## Common Mistakes

- **Passing state too high.** State in App re-renders the entire tree on every change. Only lift state as far as needed.
- **Trying to pass props sideways.** Siblings can't share props directly — use the parent as the intermediary.
- **Deeply nested props (prop drilling).** Passing props through many levels becomes painful — Context API solves this.

## Mental Model

A component hierarchy is an org chart, and data flows through it like reporting lines. The App component is the CEO at the top; it delegates regions of the screen to managers (Layout, Page components), who delegate to teams (Card, Form), who delegate to individuals (Button, Input). Two org-chart rules carry the whole model. First, communication is structured: parents brief their direct reports (props flow down), never shouting across the room to someone three teams away — if a distant component needs information, it travels down the chart level by level. Second, responsibility has altitude: high components coordinate and decide (own the data and layout); leaf components execute one job well (render a button, accept a keystroke). When a design lands on your desk, sketch its org chart before writing code — who's the manager of this region, who reports to whom — and most "where should this code live?" questions answer themselves.

## Mini Summary

- React apps form a component tree with a root at the top
- Data flows downward from parent to child via props
- State should be in the lowest common ancestor of components that need it
- Siblings communicate via their shared parent

# Guided Practice Quest

Work through the guided steps to demonstrate you can reason about component trees and data flow direction.

# Solo Practice Quest

Design the component tree for a simple Twitter-like feed page. The page should show a user header, a compose box, and a list of tweets (each with a username, text, and like count). Draw or describe the tree with at least three levels. Identify where the tweet data state should live and why.

# Integration

**Mathematics — Trees and Graph Theory**

A component tree is a mathematical tree: a directed acyclic graph where every node has exactly one parent (except the root). Trees have well-studied properties — depth, breadth, traversal algorithms. React's reconciliation algorithm traverses the tree to find changes. Understanding tree traversal (depth-first vs breadth-first) helps you reason about rendering performance. The virtual DOM diffing algorithm is a tree diff — it compares two trees to find the minimal set of DOM mutations required.

# Lore Conclusion

*"The tree,"* Aelindra says, *"is not a constraint. It is a gift. One-way flow means one-way debugging. When something is wrong, follow the data from root to leaf. You will find the fault."*

---
