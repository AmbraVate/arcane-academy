---
id: fe-jun-m2-13
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m2
moduleTitle: "Module 2: State Management"
moduleGlyph: "🔄"
moduleSortOrder: 2
topicSlug: context_api
topicTitle: "Context API"
topicSortOrder: 5
lesson: what_is_context
title: "What is Context?"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what React Context is and the problem it solves"
    - "Names the three parts of Context (createContext, Provider, useContext)"
    - "Identifies good use cases for Context vs bad ones"
    - "Explains why Context is not a full replacement for prop drilling in all cases"
  keywords: [context, createContext, Provider, useContext, global, skip, intermediate, theme, locale, auth]
  modelAnswer: |
    React Context provides a way to pass data through the component tree without explicit
    props at every level. It has three parts: createContext() creates the context object,
    Provider wraps the tree and supplies the value, useContext() reads the value in any
    descendant. Best for: theme, locale, auth user, UI preferences. Not ideal for
    frequently-changing state (every consumer re-renders on every change).
guidedSteps:
  - id: fe-jun-m2-13-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of these is the best use case for Context API?
    inputConfig:
      options:
        - "A counter that increments on every click — changes every second"
        - "The current user's auth status — needed by many components, changes rarely"
        - "A list of products fetched from an API"
        - "A single form's input values"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The current user's auth status — needed by many components, changes rarely"]
      rejectedFeedback: "Context is ideal for shared, infrequently-changing state that many components need: auth user, theme, locale, feature flags. It is NOT ideal for high-frequency updates — every Context change triggers a re-render of all consumers."
    hint: "Think about frequency of change and breadth of consumption."
    reflectionPrompt: "Context's weakness is performance: all consumers re-render when the context value changes. For slowly-changing global values (auth, theme), this is fine. For rapidly-changing values (a counter, scroll position), use local state or a more targeted solution."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does the Context Provider component do?"
    options:
      - "Fetches data from an API"
      - "Makes the context value available to all descendant components"
      - "Creates the context object"
      - "Subscribes to external state"
    correctIndex: 1
    feedback: "Provider wraps a subtree and supplies the context value. Any descendant can read the value using useContext(). Components outside the Provider tree cannot access the context."

retrieval:
  recall: "Name the three parts of the React Context API and what each does."
  explain: "Explain why Context is not ideal for frequently-changing state."
  mistakeId:
    code: "Using Context for a shopping cart that updates on every add/remove"
    answer: "Every add/remove would re-render all Context consumers. For high-frequency mutations, use a state manager like Zustand that supports selective subscriptions."
---

# Hook

Context is React's built-in solution to prop drilling. Instead of passing data through every layer, you broadcast it from a provider and let any descendant tune in directly.

# Lore Introduction

*"The Academy's broadcast system,"* says Master Aelindra, *"announces messages to the entire hall. Anyone who needs the information hears it directly — no chain of messengers required."*

# Core Learning

## Concept Introduction

```jsx
// 1. Create the context
const ThemeContext = createContext('light');

// 2. Provide the value
function App() {
  return (
    <ThemeContext.Provider value="dark">
      <Layout />  {/* doesn't need to pass theme */}
    </ThemeContext.Provider>
  );
}

// 3. Consume anywhere in the tree
function Button() {
  const theme = useContext(ThemeContext);
  return <button className={theme}>Click</button>;
}
```

**Good Context use cases:** theme, locale, auth user, sidebar open/closed, feature flags.

**Poor Context use cases:** frequently-updated values, server data (use a data-fetching library).

## Mini Summary
- ✔ createContext → Provider → useContext — three-part pattern
- ✔ Any descendant reads the value without props through intermediaries
- ✔ Best for infrequently-changing global UI state

# Solo Practice Quest

Create a ThemeContext that toggles between light and dark mode. Provide it at App level. Consume it in a Button and a Header component without passing any props.

# Integration

**Mathematics — Broadcast vs Point-to-Point Communication:** Props are point-to-point (explicit path). Context is broadcast (any subscriber receives). These correspond to unicast vs multicast network topologies in networking theory.

# Lore Conclusion

*"Not every message needs a courier chain. Some messages belong on the broadcast channel."*

---
