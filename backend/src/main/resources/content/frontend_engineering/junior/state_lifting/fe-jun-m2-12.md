---
id: fe-jun-m2-12
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m2
moduleTitle: "Module 2: State Management"
moduleGlyph: "🔄"
moduleSortOrder: 2
topicSlug: state_lifting
topicTitle: "State Lifting"
topicSortOrder: 4
lesson: prop_drilling
title: "Prop Drilling"
sortOrder: 3
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
    - "Defines prop drilling and explains why it is a problem"
    - "Identifies when prop drilling becomes problematic (3+ levels)"
    - "Names two solutions to prop drilling (Context API, state management library)"
    - "Explains the trade-off between lifting state and introducing global state"
  keywords: [prop-drilling, context, intermediate, layers, global-state, passing, depth, trade-off]
  modelAnswer: |
    Prop drilling occurs when props must pass through intermediate components that don't
    use them — just to reach a deeply nested consumer. It is a symptom of lifting state
    too high without a mechanism to skip intermediate layers. At 3+ layers, prop drilling
    becomes a maintenance burden. Context API or a state management library (Zustand,
    Redux) provide alternatives that let consumers access state directly.
guidedSteps:
  - id: fe-jun-m2-12-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Props are passed App → Layout → Sidebar → UserWidget → UserAvatar. UserAvatar is the only consumer. This is:
    inputConfig:
      options:
        - "Good state architecture"
        - "Prop drilling — props pass through 3 components that don't use them"
        - "A performance optimisation"
        - "Required by React's unidirectional data flow"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Prop drilling — props pass through 3 components that don't use them"]
      rejectedFeedback: "Prop drilling: Layout, Sidebar, and UserWidget each receive and forward the user prop without using it. They are intermediaries. When the user shape changes, all intermediaries must be updated. Context or a state manager eliminates the intermediaries."
    hint: "How many components receive the prop without using it?"
    reflectionPrompt: "Prop drilling is not always wrong — 1-2 levels is often fine and clearer than alternatives. The problem emerges at 3+ levels where intermediate components become coupling points."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the best solution when prop drilling reaches 4+ component levels?"
    options:
      - "Flatten the component tree"
      - "Context API or a state management library to skip intermediate levels"
      - "Put all state in App and drill everything"
      - "Duplicated state in each component"
    correctIndex: 1
    feedback: "Context API lets any descendant access state without props passing through each intermediate layer. State management libraries (Zustand, Redux) offer more powerful patterns for complex cases."

retrieval:
  recall: "Define prop drilling and explain at what point it becomes a problem."
  explain: "Compare prop drilling vs Context API — when would you choose each?"
  mistakeId:
    code: "user prop passed through 5 components, none of which use it"
    answer: "Use React Context. Create a UserContext, provide it at the top, and consume it directly in UserAvatar — no intermediate drilling."
---

# Hook

Lifting state solves sibling sharing. But what happens when the consumer is deeply nested? Props start travelling through components that have no interest in them — just acting as conduits. That is prop drilling.

# Lore Introduction

*"Passing a message through five scribes who don't read it,"* says Master Aelindra, *"is not a communication system. It is a chain of liability. Each intermediary can lose the message, garble it, or refuse to pass it on."*

# Core Learning

## Concept Introduction

```
App (owns user state)
  └── Layout (passes user down — doesn't use it)
        └── Sidebar (passes user down — doesn't use it)
              └── UserWidget (passes user down — doesn't use it)
                    └── UserAvatar (USES user — finally!)
```

**The problem:** Every intermediate component must accept and forward `user`. Change the shape of `user` → update 4 files, not 1.

**Solutions:**
| Approach | Best for |
|---|---|
| Prop drilling | 1–2 levels, simple data |
| Context API | Moderate depth, shared UI state |
| Zustand / Redux | Complex state, many consumers |

## Common Mistakes
- Treating all prop drilling as bad (shallow drilling is fine)
- Reaching for global state prematurely

## Mini Summary
- ✔ Prop drilling = props passing through components that don't use them
- ✔ Becomes painful at 3+ intermediate levels
- ✔ Context API skips intermediate layers; good for theme, locale, user

# Solo Practice Quest

Refactor a prop-drilled user auth state (passed 4 levels deep) using React Context. Create UserContext, provide at App level, consume directly where needed.

# Integration

**Mathematics — Graph Shortest Path:** Prop drilling forces data to travel along the tree's edges (O(depth)). Context creates a direct edge from provider to consumer (O(1)). The Context API is the shortest-path optimisation for state access in a component tree.

# Lore Conclusion

*"When the message must travel far, do not rely on intermediaries. Create a direct channel."*

---
