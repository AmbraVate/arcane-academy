---
id: fe-jun-m2-15
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
lesson: consuming_context
title: "Consuming Context"
sortOrder: 3
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
    - "Uses useContext (or custom hook) correctly to consume a context value"
    - "Understands that consuming context causes re-render on every context change"
    - "Knows how to avoid unnecessary re-renders with memoisation"
    - "Can split contexts to isolate re-renders"
  keywords: [useContext, re-render, memoisation, split-context, memo, useMemo, performance, consumer]
  modelAnswer: |
    useContext(SomeContext) subscribes a component to the context — it re-renders whenever
    the context value changes. For performance: split one large context into multiple
    smaller ones so components only subscribe to what they need. Use useMemo on the
    context value object to prevent unnecessary re-renders when the parent re-renders
    but the value hasn't changed.
guidedSteps:
  - id: fe-jun-m2-15-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A context provides `{ user, theme, language }` as one object. A component only needs `theme`. When `user` changes, the component:
    inputConfig:
      options:
        - "Does not re-render — it only uses theme"
        - "Re-renders because the context object reference changes"
        - "Throws an error if it doesn't use all context values"
        - "Caches the theme value automatically"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Re-renders because the context object reference changes"]
      rejectedFeedback: "Context doesn't do granular subscriptions — any change to the context value triggers a re-render of all consumers. The fix: split into UserContext, ThemeContext, LanguageContext. Components subscribe to only what they need."
    hint: "React Context compares the entire value object, not individual properties."
    reflectionPrompt: "This is why large monolithic contexts cause performance issues. Split contexts by update frequency: user (rare), theme (occasional), search query (frequent). Components that don't need search won't re-render when search changes."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "How can you prevent a context value object from causing unnecessary consumer re-renders?"
    options:
      - "Use React.memo on the Provider"
      - "Wrap the value in useMemo so it only changes when its dependencies change"
      - "Context always causes re-renders — cannot be prevented"
      - "Use useRef for the context value"
    correctIndex: 1
    feedback: "useMemo on the value: value={useMemo(() => ({ user, login }), [user])}. Without useMemo, every Parent re-render creates a new object — even if user hasn't changed — triggering all consumers."

retrieval:
  recall: "Explain why splitting a large context into multiple smaller contexts improves performance."
  explain: "How does useMemo on a context value prevent unnecessary consumer re-renders?"
  mistakeId:
    code: "One giant AppContext with 20 values — every consumer re-renders on any state change"
    answer: "Split by domain and update frequency: AuthContext, ThemeContext, CartContext. Each component subscribes to only its relevant context."
---

# Hook

Consuming context is a subscription. Every time the context value changes, every subscriber re-renders. Understanding this cost tells you when to split contexts and when to memoize.

# Lore Introduction

*"A scribe who subscribes to all Academy announcements,"* says Master Aelindra, *"is interrupted constantly. One who subscribes only to their department hears only what matters. Subscribe precisely."*

# Core Learning

## Concept Introduction

```jsx
// Basic consumption
function UserAvatar() {
  const { user } = useUser();  // subscribes to UserContext
  return <img src={user.avatar} alt={user.name} />;
}

// Performance: split contexts
const UserContext = createContext(null);
const ThemeContext = createContext('light');

// Performance: memoize the value
function UserProvider({ children }) {
  const [user, setUser] = useState(null);
  const value = useMemo(() => ({ user, setUser }), [user]);
  return <UserContext.Provider value={value}>{children}</UserContext.Provider>;
}
```

**When to split contexts:**
- Group by update frequency (auth vs theme vs search)
- Each consumer subscribes only to what it needs

## Common Mistakes

- **Subscribing to a fat context to read one field**: A component consuming `{ user, theme, cart }` re-renders on every cart update, even if it only uses `theme`. Split contexts by rate of change so components only subscribe to what they read.
- **Calling `useContext` directly instead of a named custom hook**: `useContext(AuthContext)` deep in a component is invisible to search and lacks error handling. A `useAuth()` hook makes the dependency explicit, typed, and guarded.
- **Reading context outside a Provider and getting silent wrong data**: When no Provider wraps the component, `useContext` returns the `createContext` default — often `null` — leading to hard-to-debug rendering with fallback values. Use a throwing custom hook to catch this at development time.
- **Omitting `useMemo` on frequently-updated context values**: Without memoization, each render of the Provider produces a new value object reference, re-rendering all consumers even when the underlying data is unchanged.

## Mental Model

Consuming context is owning a radio receiver, and the habits of a good radio operator map one-to-one onto good consumption patterns. `useContext(ThemeContext)` switches on a receiver tuned to one frequency: from then on, the component hears the *current* broadcast (the present value) and automatically reacts to every programme change (re-renders when the value updates) — no couriers, no forwarding, just reception. The operator's disciplines follow from how radio behaves. You can't half-listen to a channel: a receiver reacts to *everything* aired on its frequency, so if the station bundles news, weather, and sport into one stream (a fat context value), the component that only wanted weather still re-renders for every football score — the reason mature apps run separate stations for separately changing concerns. Know your coverage: switch on a receiver outside any transmitter's range and you hear the pre-recorded default tape — often silence that *sounds* like programming, the silent-fallback bug — so professional operators use receivers with a built-in signal check (custom hooks that throw when no Provider is found) and discover dead zones at install time, not from confused users. And label your equipment: a bare `useContext(SomeContext)` deep in a component is an unmarked receiver someone will trip over during refactoring, while `useAuth()` is a labelled unit — findable by search, typed, and documenting exactly which station this room depends on. Reception is one line; *accountable* reception is the pattern around the line.

## Why It Matters

Consuming context is where the architecture meets daily code — and the consumption patterns decide whether context stays a convenience or becomes a hidden tax:

- `useContext` makes a component a subscriber: one line replaces a chain of forwarded props, which is the payoff — but the component now has an invisible dependency that its props no longer document, so disciplined teams consume through *named custom hooks* (`useTheme`, `useAuth`) that make the dependency searchable, typed, and guarded
- The re-render contract is the part juniors discover in production: every consumer re-renders whenever the context value changes — consume a context holding `{user, theme, cart}` to read only the theme, and you re-render on every cart change anyway, which is the standard argument for splitting contexts by *rate and audience of change*
- The wrong-place failure matters: read context outside any Provider and you silently get the default value — the bug where a component renders with the fallback theme and nobody knows why; the throwing-hook pattern converts that silence into an immediate stack trace
- Testing changes shape: a component that consumes context can't be rendered bare — your tests wrap it in a Provider with controlled values, which is also the superpower: swap in mock auth states or themes per test without touching the component

Context consumption is the moment a component trades self-containment for convenience. The patterns here — named hooks, focused contexts, loud failures — are how you take the convenience without losing the traceability.

## Mini Summary
- ✔ useContext = subscription; consumer re-renders on every context change
- ✔ Split large contexts by domain and update frequency
- ✔ useMemo on the value object prevents re-renders when nothing changed

# Solo Practice Quest

Refactor an AppContext providing `{ user, theme, cartItems }` into three separate contexts. Measure (with React DevTools Profiler) how much less each component re-renders.

# Integration

**Mathematics — Event Subscription Complexity:** A single broadcast to N subscribers is O(N) re-renders. Splitting into k smaller contexts where each component subscribes to one reduces average re-renders to O(N/k) per event. Context splitting is a linear complexity reduction.

# Lore Conclusion

*"Subscribe only to what you need to hear. Every unnecessary subscription is an unnecessary interruption."*

---
