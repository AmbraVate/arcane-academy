---
id: fe-jun-m2-14
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
lesson: creating_context
title: "Creating Context"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Creates a context with a sensible default value"
    - "Wraps the Provider with state using useState"
    - "Extracts context into a custom hook for cleaner consumption"
    - "Collocates Provider and its state in a dedicated context file"
  keywords: [createContext, Provider, useState, custom-hook, collocate, default-value, context-file]
  modelAnswer: |
    Best practice: create a context file (ThemeContext.jsx) that exports the context,
    a Provider component wrapping useState, and a custom useTheme() hook. Consumers
    import useTheme() — they never import the raw context object. This encapsulates
    the implementation: changing from Context to a different state manager later
    only requires updating the context file.
guidedSteps:
  - id: fe-jun-m2-14-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the advantage of exporting a custom hook (useTheme) instead of exporting the raw context object?
    inputConfig:
      options:
        - "Custom hooks are faster"
        - "Consumers don't depend on the implementation — you can change the internals without updating consumers"
        - "useContext() cannot be called directly"
        - "Custom hooks allow multiple contexts"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Consumers don't depend on the implementation — you can change the internals without updating consumers"]
      rejectedFeedback: "Encapsulation: if consumers call useTheme() and you later switch from Context to Zustand, you only update the useTheme() hook — all consumers stay the same. If consumers import ThemeContext directly, every consumer must be updated."
    hint: "Think about what changes when you refactor the state management approach."
    reflectionPrompt: "This is the adapter pattern applied to React state. The custom hook is the interface. The implementation (Context, Zustand, Redux) is hidden behind it."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A Context's default value (passed to createContext) is used when:"
    options:
      - "The Provider has no children"
      - "A component consumes the context outside of any Provider"
      - "The Provider's value prop is undefined"
      - "The context is first created"
    correctIndex: 1
    feedback: "The default value is a fallback for components that are not inside any Provider. It is useful for testing components in isolation without wrapping them in a Provider."

retrieval:
  recall: "Write the complete pattern for a ThemeContext: context file, Provider component, and custom hook."
  explain: "Why is a custom hook better than exporting the raw context object for consumers?"
  mistakeId:
    code: "Consumers import ThemeContext directly and call useContext(ThemeContext)"
    answer: "Export a useTheme() hook instead. Consumers call useTheme(). Implementation details stay encapsulated — swap Context for Zustand later by updating only the hook."
---

# Hook

Creating context well means thinking ahead: how will consumers use it, and what happens if the implementation changes? The answer is a custom hook that hides the details.

# Lore Introduction

*"The Academy's communication system,"* says Master Aelindra, *"is accessed through one interface: the messaging desk. The desk may use scrolls, mirrors, or ravens — consumers don't care. They only know the interface."*

# Core Learning

## Concept Introduction

**Best-practice context pattern:**

```jsx
// ThemeContext.jsx
import { createContext, useContext, useState } from 'react';

const ThemeContext = createContext('light');  // default value

export function ThemeProvider({ children }) {
  const [theme, setTheme] = useState('light');
  return (
    <ThemeContext.Provider value={{ theme, setTheme }}>
      {children}
    </ThemeContext.Provider>
  );
}

// Custom hook — the only export consumers should use
export function useTheme() {
  return useContext(ThemeContext);
}
```

```jsx
// App.jsx
import { ThemeProvider } from './ThemeContext';
function App() {
  return <ThemeProvider><Router /></ThemeProvider>;
}

// Button.jsx — clean consumption
import { useTheme } from './ThemeContext';
function Button() {
  const { theme, setTheme } = useTheme();
  return <button onClick={() => setTheme(t => t === 'light' ? 'dark' : 'light')}>{theme}</button>;
}
```

## Mini Summary
- ✔ Collocate: context + Provider + useState + custom hook in one file
- ✔ Export the custom hook, not the raw context object
- ✔ Default value = fallback when no Provider is present (useful in tests)

# Solo Practice Quest

Build an AuthContext with a user state (null when logged out, user object when logged in). Export useAuth(). Provide a login() and logout() function. Test that a ProfilePage correctly displays or redirects based on user state.

# Integration

**Psychology — Abstraction and Cognitive Load:** Custom hooks reduce cognitive load by hiding implementation. Consumers learn one interface (useTheme) rather than understanding the entire Context system. This is why abstraction exists: reducing the vocabulary needed to use a system.

# Lore Conclusion

*"The interface is the promise. The implementation is the detail. Consumers should only know the promise."*

---
