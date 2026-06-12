---
id: fe-jun-m2-09
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m2
moduleTitle: "Module 2: State Management"
moduleGlyph: "🔄"
moduleSortOrder: 2
topicSlug: derived_state
topicTitle: "Derived State"
topicSortOrder: 3
lesson: use_memo
title: "useMemo"
sortOrder: 3
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m2-07]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what useMemo does"
    - "Identifies when useMemo is and isn't appropriate"
    - "Uses useMemo with correct dependency array"
    - "Understands that premature memoisation is harmful"
  keywords: [useMemo, memoisation, dependency, cache, performance, expensive, profiling, over-optimisation]
  modelAnswer: |
    useMemo memoises a computed value — it caches the result and only recomputes when its dependencies change. It should be used sparingly: only when a computation is genuinely expensive (proven by profiling) and runs on every render. Most derived values don't need useMemo. Premature memoisation adds complexity and can introduce bugs.
guidedSteps:
  - id: fe-jun-m2-09-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "When should you use useMemo for a derived value?"
    inputConfig:
      options:
        - "Always — it makes all computations faster"
        - "Only when profiling shows the computation is expensive enough to justify caching"
        - "For all array operations"
        - "Never — useState is always sufficient"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Only when profiling shows the computation is expensive enough to justify caching"]
      rejectedFeedback: "useMemo has a cost: the dependency array comparison, the closure, and the cached value. For cheap computations, this overhead exceeds the benefit. Use it only when profiling confirms a real performance issue."
    hint: "React is fast. Most computations are cheap. Measure before optimising."
    reflectionPrompt: "The React team says: don't use useMemo as a habit. Profile first. Use it when you have evidence of a problem."
  - id: fe-jun-m2-09-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Write a useMemo for `sortedItems` — items sorted by price — that only recomputes when `items` changes."
    inputConfig:
      minWords: 5
    markingRule:
      matchMode: CONTAINS
      accepted: [useMemo, items, sort, price, dependency]
      rejectedFeedback: "Example: `const sortedItems = useMemo(() => [...items].sort((a, b) => a.price - b.price), [items]);` — the dependency array `[items]` means recompute only when items changes."
    hint: "useMemo(computeFn, [dependencies])"
    reflectionPrompt: "Note the [...items] spread before sort — sort() mutates, so we create a copy first. This is an immutability concern even inside useMemo."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What happens if you provide an empty dependency array `[]` to useMemo?"
    options:
      - "The value recomputes on every render"
      - "The value is computed once (on mount) and never recomputed"
      - "React throws an error"
      - "The value is not cached"
    correctIndex: 1
    feedback: "Empty dependency array = run once on mount. If the computation depends on props/state but the array is empty, you'll always get the initial computed value — a stale closure bug. Dependencies must list everything the computation reads."
retrieval:
  recall: "What is the cost of using useMemo?"
  explain: "Why is `[items]` in the dependency array important for sorting memoisation?"
  mistakeId:
    code: "Using useMemo for every derived value as a 'best practice'"
    answer: "useMemo is an optimisation, not a best practice. It adds code complexity and its own overhead. For simple derivations (filtering, counting), the computation cost is negligible — less than the useMemo overhead. Use it only when profiling identifies a real performance bottleneck."
---

# Hook

You've learned to compute derived values during render. But what if the computation is expensive — sorting 10,000 items or running a complex algorithm on every render? `useMemo` is React's tool for caching expensive computed values. It's also one of the most over-used hooks in the ecosystem — this lesson teaches when it helps and when it hurts.

# Lore Introduction

*"The Academy's master archivist,"* Aelindra says, *"does not reindex the entire library every time someone asks for a scroll. She caches the index and refreshes it only when the collection changes. The caching has a cost: maintaining the index. Use it only when the search is frequent and the indexing is expensive."*

# Core Learning

## Concept Introduction

`useMemo` memoises a computed value:

```jsx
// Without useMemo — recomputes every render
const sortedItems = [...items].sort((a, b) => a.name.localeCompare(b.name));

// With useMemo — recomputes only when items changes
const sortedItems = useMemo(
  () => [...items].sort((a, b) => a.name.localeCompare(b.name)),
  [items] // ← dependencies
);
```

| Use useMemo when | Don't use useMemo when |
|---|---|
| Computation is expensive (proven by profiling) | Computation is simple |
| Component re-renders frequently | Component rarely re-renders |
| Result is used in many places | Result is used once |

## Why It Matters

Over-using useMemo adds code noise and can introduce stale closure bugs. Under-using it in genuine performance hotspots wastes render time. Judgement requires understanding the trade-off.

## Worked Example

```jsx
function SearchResults({ products, query, minPrice, maxPrice }) {
  // Expensive: filtering and sorting a large array
  const filteredAndSorted = useMemo(() => {
    return products
      .filter(p => p.name.toLowerCase().includes(query.toLowerCase()))
      .filter(p => p.price >= minPrice && p.price <= maxPrice)
      .sort((a, b) => a.price - b.price);
  }, [products, query, minPrice, maxPrice]);

  return <ProductList items={filteredAndSorted} />;
}
```

## Common Mistakes

- **Empty dependency array with dependencies in computation.** Stale values forever.
- **Memoising simple computations.** More overhead than the computation itself.
- **Using useMemo for side effects.** Use useEffect for side effects, useMemo only for computed values.

## Mental Model

`useMemo` is a chef's batch-prep, and like batch-prep it's only worth doing for the laborious dishes. By default a kitchen makes everything fresh per order (recomputing derived values every render) — and for most dishes that's *correct*: chopping one garnish fresh costs nothing and is always right. Batch-prep — making a vat of sauce and reusing it — pays only when the dish is genuinely expensive (filtering ten thousand rows, heavy sorting), and it introduces the question every cached sauce raises: *when is it stale?* The dependency array is the freshness label: `useMemo(() => filter(items, query), [items, query])` declares "this preparation depends on these ingredients — remake it only when one changes; otherwise serve from the batch". Get the label wrong by omitting an ingredient and you serve stale sauce: the computation reads a value that changed but wasn't listed, and the UI quietly shows yesterday's results. The two disciplines, in order of importance: don't batch-prep garnishes — `useMemo` on cheap computations adds complexity and its own bookkeeping cost for nothing, so default to computing fresh and memoise only what's *measured* slow; and when you do, the dependency array must list every ingredient the recipe touches, no exceptions. Fresh by default; batch the banquet dishes; label honestly.

## Mini Summary

- useMemo caches a computed value until its dependencies change
- Use only for expensive computations proven by profiling
- Dependency array must include everything the computation reads
- Most derived values don't need useMemo

# Guided Practice Quest

Work through the guided steps on when and how to use useMemo.

# Solo Practice Quest

You have a component with 5,000 products that filters by category, price range, and search query, then sorts. Profile (mentally) which operations might benefit from useMemo. Write the memoised version with correct dependency arrays. Then write 3 sentences explaining what would happen if you accidentally omitted `query` from the dependency array.

# Integration

**Mathematics — Memoisation and Dynamic Programming**

Memoisation is a fundamental technique in computer science: cache function results to avoid redundant computation. It's used in dynamic programming algorithms (Fibonacci, longest common subsequence) and in computer graphics (texture caching). The trade-off is always the same: memory for speed. useMemo is memoisation applied to React render cycles. Understanding the general pattern makes the hook intuitive: same inputs → return cached output; different inputs → recompute and cache new output.

# Lore Conclusion

*"The cache is a servant,"* Aelindra says, *"not a master. It serves you when the work is hard and repeated. It costs you when the work is easy. Know the difference. Profile. Then decide."*

---
