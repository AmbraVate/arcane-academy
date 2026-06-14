---
id: fe-sen-m3-01
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m3
moduleTitle: "Module 3: Performance Engineering"
moduleGlyph: "🚀"
moduleSortOrder: 3
topicSlug: rendering_performance
topicTitle: "Rendering Performance"
topicSortOrder: 1
lesson: rendering_performance
title: "Rendering Performance"
sortOrder: 1
difficulty: 4
estimatedMinutes: 35
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Accurately describes React's reconciliation algorithm and when re-renders are triggered
    - Explains the difference between render phase and commit phase
    - Identifies the three main causes of unnecessary re-renders in React
    - Describes how to use React DevTools Profiler to diagnose rendering issues
    - Synthesises a diagnostic approach: how to identify, measure, and fix a rendering performance problem
  keywords: [reconciliation, virtual DOM, commit, fiber, re-render, props, state, context, profiler, flame graph, memo, useMemo, useCallback, parent, expensive]
  modelAnswer: |
    React re-renders a component whenever its state changes, its parent re-renders, or a context value it subscribes to changes. The render phase produces a new virtual DOM tree; the reconciliation algorithm (diffing) compares it to the previous tree to determine the minimal set of DOM operations needed. The commit phase applies those operations to the real DOM.

    Unnecessary re-renders occur for three reasons: (1) parent re-renders cause all children to re-render by default, even if their props haven't changed; (2) object/function props are recreated on every render, causing referential inequality even when the value is semantically identical; (3) context updates re-render all subscribers, even those that only use a part of the context that didn't change.

    The React DevTools Profiler records a flame graph of each render, showing which components rendered, how long each took, and why they rendered. Components that render frequently with long render times are candidates for optimisation. The diagnostic approach: measure first (Profiler), identify the expensive component, determine why it's re-rendering unnecessarily, then apply the minimum fix (React.memo for components, useMemo for expensive calculations, useCallback for stable function references).

    Key insight: not all re-renders are problems. A re-render that takes 0.1ms is irrelevant. Only re-renders that cause noticeable jank (>16ms per frame) or that trigger expensive child renders need intervention.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A parent component re-renders with the same props every 100ms (polling). A deeply nested child has no state of its own and receives the same props on every render. What happens to the child?"
    options:
      - "The child doesn't re-render because its props haven't changed"
      - "The child re-renders on every parent render, even if its props are identical"
      - "React skips children whose props are unchanged by default"
      - "The child only re-renders if it calls setState"
    correctIndex: 1
    feedback: "By default, React re-renders all children when a parent re-renders — regardless of whether props changed. React.memo wraps the child and performs a shallow comparison of props; if props are shallowly equal, the child is skipped. Without memo, the child pays the render cost on every parent render."
  - type: SHORT_TEXT
    prompt: "A component re-renders 50 times per second despite only needing to update twice per second. Using the React DevTools Profiler, describe your diagnostic process to identify the source."
    hint: "What does the Profiler's flame graph show? What does the 'why did this render?' panel reveal?"
  - type: FILL_BLANK
    prompt: "The render phase produces a new ___ tree. The commit phase applies changes to the ___."
    answer: "virtual DOM (fiber tree); real DOM"
    hint: "React works in two phases. The first builds; the second applies."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which React DevTools Profiler view shows which components rendered and for how long in a single commit?"
    options:
      - "Components tree"
      - "Flame graph"
      - "Timeline"
      - "Props inspector"
    correctIndex: 1
    feedback: "The flame graph visualises component render times in a single commit. Wide bars = long render times; grey bars = components that didn't re-render in that commit. Start with the widest coloured bars in your hottest path."
  - type: MULTIPLE_CHOICE
    question: "A component creates a new object `{ userId: id }` on every render and passes it as a prop. The child uses React.memo. Does memo prevent the child from re-rendering?"
    options:
      - "Yes — memo prevents all re-renders"
      - "No — the new object fails the shallow comparison on every render"
      - "Yes — objects are compared by value"
      - "No — memo only works with primitive props"
    correctIndex: 1
    feedback: "React.memo uses shallow comparison. Two different objects `{ userId: 1 }` and `{ userId: 1 }` are referentially unequal (`{}` !== `{}`), so memo's comparison fails and the child re-renders. Fix: useMemo to stabilise the object reference, or pass primitive props instead of objects."
retrieval:
  recall: "Name the three main causes of unnecessary re-renders in a React application."
  explain: "Explain the difference between the render phase and the commit phase in React's update cycle."
  mistakeId:
    code: |
      // Profiler shows ExpensiveList re-renders 60x/sec
      // Developer adds React.memo to ExpensiveList
      // Re-renders continue at 60x/sec
      
      function Parent() {
        const filters = { status: 'active', page: 1 }; // recreated every render
        return <ExpensiveList filters={filters} />;
      }
    answer: "React.memo performs a shallow comparison. The `filters` object is created inline — a new object reference on every render. Shallow comparison: `{} !== {}` — memo sees different props and re-renders. Fix: `const filters = useMemo(() => ({ status: 'active', page: 1 }), [])` — the same object reference is reused across renders, memo's comparison passes, and ExpensiveList is skipped."
---

# Hook

Your dashboard is slow. The frame rate drops when data updates. You add React.memo everywhere. It's still slow. You've optimised without measuring — the classic mistake.

Performance engineering in React starts with measurement, not with adding `memo`. The Profiler tells you what's expensive. Understanding reconciliation tells you why.

# Lore Introduction

The Academy's Observatory charts the movements of celestial bodies — but only where the movement is significant. Tracking every leaf on every tree would overwhelm the Observatory. It focuses on what matters.

*"Rendering performance is the same,"* Master Brennan explains. *"Not every re-render is a problem. The problem is the expensive one — the one that takes 30ms while the eye expects 16. Find it first. Fix only it."*

# Core Learning

## Concept Introduction

**React's update cycle has two phases:**

**Render phase:** React calls component functions and builds a new virtual DOM tree (fiber tree). This is interruptible and can happen multiple times for complex updates.

**Commit phase:** React compares the new tree to the previous (reconciliation/diffing), calculates the minimal DOM operations, and applies them. This is synchronous and must complete in one pass.

**What triggers a re-render:**
1. Component's own state changes
2. Parent re-renders (default: all children re-render)
3. Context value changes (all subscribers re-render)

**The key insight:** Not all re-renders are problems. A component that renders in 0.1ms and rarely triggers large subtrees is irrelevant. Focus on components that are expensive (>5ms render time) or that trigger expensive subtrees unnecessarily.

## Why It Matters

A React app that re-renders too frequently on the main thread causes jank — frames that take >16ms produce visible stuttering. Understanding what causes unnecessary re-renders is the first step to eliminating them.

## Worked Examples

**Example 1: Diagnosing with the Profiler**
1. Open React DevTools → Profiler tab
2. Click Record, interact with the slow part of the UI, click Stop
3. Look at the flame graph: which components are widest? Which re-rendered when they shouldn't have?
4. The "Rendered by" panel shows exactly why each component re-rendered

**Example 2: Parent re-render cascade**
```tsx
function App() {
  const [tick, setTick] = useState(0);
  useEffect(() => {
    const id = setInterval(() => setTick(t => t + 1), 100);
    return () => clearInterval(id);
  }, []);

  return (
    <div>
      <Ticker value={tick} />
      <ExpensiveChart /> {/* Re-renders every 100ms even if data unchanged */}
    </div>
  );
}
```

Fix: `React.memo(ExpensiveChart)` — but only after Profiler confirms it's the bottleneck.

## Common Mistakes

- **Optimising without measuring.** Add memo everywhere and create more overhead than you save.
- **Expecting memo to solve object prop problems.** Inline objects fail shallow comparison every render.
- **Conflating render phase cost with commit phase cost.** Long renders are usually computation in the render function. Long commits are usually large DOM changes.
- **Ignoring subtree size.** A fast component that causes 500 children to re-render is still expensive.

## Mental Model

Think of React renders as paint jobs. The render phase decides what needs repainting. The commit phase actually repaints. React.memo is masking tape — it prevents certain components from being included in the repaint decision. But if you put tape on everything without knowing what needs repainting, you waste time applying and removing tape.

## Mini Summary

- ✔ Re-renders are triggered by: state change, parent re-render, context change
- ✔ Render phase (virtual DOM) is interruptible; commit phase (real DOM) is synchronous
- ✔ Not all re-renders are problems — measure with Profiler first
- ✔ Inline objects and functions create new references on every render, defeating React.memo
- ✔ Fix: React.memo for component skipping, useMemo/useCallback for reference stability

# Guided Practice Quest

Work through the guided steps to develop your diagnostic mental model for rendering performance.

# Solo Practice Quest

You have a data table that re-renders every 500ms as part of a live dashboard. The table has 50 rows, each a `RowComponent` that receives a row object as a prop. The data rarely changes — usually only 1-2 rows update. Design an optimisation strategy:
1. How would you use the Profiler to confirm where time is spent?
2. What would you apply and why?
3. What do you need to be careful about when objects are passed as props?

# Integration

**Mathematics — Amortised Complexity**

React's reconciliation algorithm is O(n) in the number of elements diffed — linear, not quadratic, because React doesn't compare every node against every other. The trade-off: React trades optimal diff accuracy (which would be O(n³)) for a heuristic that's O(n) and correct for 99% of real UI patterns. Understanding this trade-off explains both React's performance characteristics and why the `key` prop is critical — without stable keys, React can't short-circuit the diff, and a list reorder produces O(n) DOM mutations instead of O(1). Performance engineering in React is applied algorithmic thinking: understanding the complexity of what you're asking the framework to do.

**Psychology — Detection Threshold**

Human perception of UI sluggishness has a non-linear detection threshold. Frame rates above ~60fps (16ms/frame) are imperceptible to the conscious mind. Drops to 30fps (33ms/frame) are noticeable but tolerable. Drops below 20fps (50ms/frame) are jarring. This means performance work below the 16ms threshold has zero perceptible impact. The psychological insight: optimise only what users can detect. Over-optimising imperceptible renders wastes engineering time without improving the user experience.

# Lore Conclusion

*"The Observatory focuses only on the bodies whose movement is significant,"* Master Brennan says, reviewing the Profiler output. *"Three components account for 94% of the render time. Fix those three. Leave the rest. The sky remains charted."*

---
