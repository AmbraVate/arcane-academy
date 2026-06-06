---
id: fe-sen-m3-03
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m3
moduleTitle: "Module 3: Performance Engineering"
moduleGlyph: "🚀"
moduleSortOrder: 3
topicSlug: virtualisation
topicTitle: "Virtualisation"
topicSortOrder: 3
lesson: virtualisation
title: "Virtualisation"
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
    - Explains what list virtualisation (windowing) is and why it's needed for large lists
    - Correctly describes how virtualisation works — only visible items in the DOM
    - Identifies the correct libraries (TanStack Virtual, react-window) and their use cases
    - Explains the performance implications: DOM node count, layout, memory
    - Synthesises when virtualisation is necessary vs when it's over-engineering
  keywords: [virtualisation, windowing, visible, DOM, TanStack Virtual, react-window, scroll, row height, overscan, large list, 1000 rows]
  modelAnswer: |
    List virtualisation (windowing) renders only the items currently visible in the viewport, plus a small buffer (overscan). As the user scrolls, items leaving the viewport are unmounted and replaced with incoming items. The DOM always contains only a small fraction of the total items (typically 20-40 nodes regardless of list length).

    Without virtualisation, rendering 10,000 rows creates 10,000+ DOM nodes. The browser must lay out and paint all of them even if only 20 are visible. This causes slow initial render, high memory usage, and janky scrolling. With virtualisation, DOM node count stays constant — 10,000 rows and 100 rows have identical rendering performance.

    TanStack Virtual (formerly react-virtual) is the most flexible modern library — headless, works with any framework. react-window is lightweight for fixed-height lists. Both require knowing the item size — variable-height items need dynamic measurement (more complex).

    Virtualisation is necessary when: list length exceeds ~100 items AND each item is non-trivial to render (not just text). For shorter lists, React's rendering is fast enough. For simple text lists up to ~500 items, pagination or infinite scroll (load more) may be simpler than virtualisation.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A component renders a list of 5,000 transaction rows. Each row has an avatar, name, amount, and status badge. The initial render takes 3 seconds. What is the most likely cause?"
    options:
      - "JavaScript execution is slow on this machine"
      - "5,000 DOM nodes are created, laid out, and painted even though only ~15 fit in the viewport"
      - "The data fetching is slow — 5,000 API calls are made"
      - "React.memo is missing from the row component"
    correctIndex: 1
    feedback: "Rendering 5,000 rows creates 5,000+ DOM nodes (each row may contain 10+ elements). The browser must measure, lay out, and paint all of them — even the 4,985 that are not visible. Virtualisation limits the DOM to ~20-30 rows at any time, making initial render nearly instant regardless of list length."
  - type: SHORT_TEXT
    prompt: "Explain what 'overscan' means in the context of list virtualisation, and why it matters for scroll smoothness."
    hint: "What happens without overscan when the user scrolls quickly?"
  - type: FILL_BLANK
    prompt: "Virtualisation renders only ___ items plus a ___ buffer. Items outside this range are ___ from the DOM."
    answer: "visible; small (overscan); unmounted/removed"
    hint: "The window follows the viewport position."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "You have a virtualised list of 10,000 items. The user scrolls to item 9,000. How many DOM nodes are in the list container?"
    options:
      - "9,000 — all rendered items up to the current position"
      - "10,000 — all items are always rendered"
      - "Approximately 20-40 — only the visible window plus overscan"
      - "1 — virtualisation replaces all items with a single placeholder"
    correctIndex: 2
    feedback: "Virtualisation maintains a constant window of rendered items — typically the number visible in the viewport (e.g. 15) plus the overscan buffer (e.g. 5 above and below). At item 9,000, the DOM contains items 8,990-9,025 or similar — never all 10,000."
  - type: MULTIPLE_CHOICE
    question: "Variable-height list items are harder to virtualise than fixed-height items. Why?"
    options:
      - "Variable-height items have more complex React components"
      - "The virtualiser needs to know each item's height to calculate scroll positions — variable heights require dynamic measurement"
      - "Variable heights are not supported by any virtualisation library"
      - "Variable-height items prevent the user from scrolling smoothly"
    correctIndex: 1
    feedback: "Fixed-height virtualisation is simple: scroll position / item height = first visible index. Variable heights require measuring each item — either estimating first and correcting after render (TanStack Virtual's approach) or pre-measuring (complex). Fixed-height lists should be used wherever possible for simplicity."
retrieval:
  recall: "How many DOM nodes does a virtualised list of 10,000 items have in the DOM at any point? How does this compare to a non-virtualised list?"
  explain: "Why does virtualisation dramatically improve initial render time for large lists?"
  mistakeId:
    code: |
      // A developer virtualises a list of 50 items
      // Each item is a simple <li>{item.name}</li>
      import { useVirtualizer } from '@tanstack/react-virtual';
      // ... complex virtualisation setup for 50 items
    answer: "Virtualisation adds complexity: a container ref, dynamic sizing, scroll event handling, and careful CSS. For 50 simple list items, React renders them in <5ms without any optimisation — there is no performance problem to solve. Virtualisation is appropriate for lists of hundreds to thousands of non-trivial items. For 50 simple items, the virtualisation complexity costs more than it saves. Measure first; virtualise when you have an actual performance problem."
---

# Hook

Your data table has 10,000 rows. It takes 4 seconds to load. The page freezes when data updates. Users complain about scrolling lag.

You added `React.memo`. It made no difference. Memoisation can't help when the problem is that 10,000 DOM nodes exist in the first place.

Virtualisation is the correct tool here.

# Lore Introduction

*"The cartographer does not draw every stone on every path,"* Master Elara explains. *"The map shows what the traveller can see from where they stand. As they walk, the map updates — what lies behind fades, what lies ahead appears."*

She unrolls a map that shows only a portion of the terrain. *"Virtualisation is this map. Render what is visible. Let the rest wait."*

# Core Learning

## Concept Introduction

**List virtualisation (windowing)** renders only items visible in the viewport plus a small buffer (overscan):

```
Full list: 10,000 items
Viewport shows: 15 items
Overscan buffer: 5 above + 5 below
DOM nodes: ~25 at any time (regardless of list length)
```

As the user scrolls, the virtual window moves:
- Items scrolling out of view → unmounted from DOM
- Items scrolling into view → mounted to DOM
- Absolute positioning maintains correct scroll position

**Key libraries:**

| Library | Strengths |
|---|---|
| **TanStack Virtual** | Headless, flexible, variable heights, framework-agnostic |
| **react-window** | Lightweight, fixed-height lists |
| **react-virtuoso** | Auto-measuring variable heights, simpler API |

## Worked Example

```tsx
import { useVirtualizer } from '@tanstack/react-virtual';

function VirtualList({ items }) {
  const parentRef = useRef(null);

  const virtualizer = useVirtualizer({
    count: items.length,
    getScrollElement: () => parentRef.current,
    estimateSize: () => 50, // estimated row height in px
    overscan: 5,
  });

  return (
    <div
      ref={parentRef}
      style={{ height: '600px', overflow: 'auto' }}
    >
      {/* Total scroll height — makes scrollbar correct */}
      <div style={{ height: virtualizer.getTotalSize() }}>
        {/* Only render virtual items */}
        {virtualizer.getVirtualItems().map(vItem => (
          <div
            key={vItem.key}
            style={{
              position: 'absolute',
              top: vItem.start,
              height: vItem.size,
            }}
          >
            <Row item={items[vItem.index]} />
          </div>
        ))}
      </div>
    </div>
  );
}
```

**When to use virtualisation:**
- List length > ~100-500 items
- Each row is non-trivial (avatars, badges, interactive elements)
- Scrolling performance is measured as poor

**When NOT to use it:**
- Simple lists under ~100 items
- When pagination would solve the problem with less complexity
- When items have complex variable heights and you can paginate instead

## Common Mistakes

- **Virtualising small lists.** Adds complexity for no gain. Measure first.
- **Forgetting the container needs a fixed height.** Without a fixed height, the virtualiser can't calculate which items are visible.
- **Using position: absolute without accounting for total size.** The scrollbar height must equal totalSize or the scroll position calculation breaks.
- **Assuming row height.** For variable-height rows, measure with ResizeObserver or use a library that handles dynamic measurement.

## Mini Summary

- ✔ Virtualisation renders only visible items — DOM stays small regardless of list length
- ✔ Necessary for lists of 100s-1000s of non-trivial items
- ✔ TanStack Virtual is the modern choice — headless and flexible
- ✔ Requires a fixed-height container and knowledge of (estimated) item size
- ✔ Don't virtualise small lists — measure, then apply only where needed

# Guided Practice Quest

Work through the guided steps to understand how overscan improves scroll smoothness and why fixed heights are simpler.

# Solo Practice Quest

A customer support tool displays the last 5,000 support tickets. Each ticket has: customer name, subject line, status badge, timestamp, and assignee avatar. A junior developer has rendered all 5,000 without virtualisation. Design the virtualisation solution: what library, what container height, what estimated row height, what overscan? Also: what edge cases would you test?

# Integration

**Mathematics — The Fixed Window Principle**

Virtualisation is a direct application of the sliding window algorithm — a technique for processing streams of data in O(1) space rather than O(n). Instead of storing all n items in memory (DOM nodes), you maintain a window of size k (visible items + overscan). As the stream advances (user scrolls), items enter and exit the window. The total operations over a full scroll from top to bottom is O(n) — each item enters and exits the window once. But at any point in time, the space cost is O(k), independent of n. This is the same principle used in network protocols (TCP window), streaming algorithms, and database cursors. Virtualisation applies it to the browser DOM — a key insight that DOM node creation has a non-trivial cost that makes O(n) space untenable for large n.

# Lore Conclusion

*"The map updates as you walk,"* Master Elara says, watching the virtual list scroll smoothly through 10,000 items. *"The stones behind you are no longer drawn. The stones ahead await. You see what you need, when you need it."*

---
