---
id: fe-jun-m2-08
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
lesson: avoiding_redundant_state
title: "Avoiding Redundant State"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m2-07]
integrationDomains: [mathematics, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies redundant state in code examples"
    - "Refactors redundant state to derived computation"
    - "Applies the DRY principle to state design"
    - "Explains the connection between state minimisation and bug reduction"
  keywords: [redundant, DRY, minimise, single source, refactor, sync, derived, simplify]
  modelAnswer: |
    Redundant state is state that duplicates information already available in other state or props. It violates DRY (Don't Repeat Yourself) and creates synchronisation bugs. Refactoring removes redundant state and derives the value from its source. This reduces the surface area for bugs and simplifies the component.
guidedSteps:
  - id: fe-jun-m2-08-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Which state is redundant? `const [firstName, setFirstName] = useState(''); const [lastName, setLastName] = useState(''); const [isNameEmpty, setIsNameEmpty] = useState(true);`"
    inputConfig:
      options:
        - "firstName is redundant"
        - "isNameEmpty is redundant — derive it: `const isNameEmpty = !firstName && !lastName`"
        - "lastName is redundant"
        - "None are redundant"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["isNameEmpty is redundant — derive it: `const isNameEmpty = !firstName && !lastName`"]
      rejectedFeedback: "`isNameEmpty` can be computed from `firstName` and `lastName`. Storing it separately means you must keep it in sync with both. Derive it: `const isNameEmpty = !firstName.trim() && !lastName.trim()`."
    hint: "Which one can be computed from the others?"
    reflectionPrompt: "Any boolean that can be expressed as a condition on other state is redundant. Forms are full of these: isValid, isEmpty, hasError — all derivable."
  - id: fe-jun-m2-08-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Identify the redundant state: `const [items, setItems] = useState([]); const [selectedItem, setSelectedItem] = useState(null); const [selectedItemDetails, setSelectedItemDetails] = useState(null);`"
    inputConfig:
      minWords: 10
    markingRule:
      matchMode: CONTAINS
      accepted: [selectedItemDetails, find, derive, items, selectedItem]
      rejectedFeedback: "`selectedItemDetails` is redundant if `selectedItem` is an ID or reference — you can find the details: `const selectedItemDetails = items.find(i => i.id === selectedItem)`. Storing it separately risks it being stale."
    hint: "Can you find the details from the existing state?"
    reflectionPrompt: "The pattern: store IDs in state, not full objects. Derive full objects by looking them up. This avoids storing duplicated data that can become stale."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the DRY principle applied to state?"
    options:
      - "Don't write state setters more than once"
      - "Don't store the same information in multiple state variables — have one source of truth"
      - "Don't use Redux when useState is enough"
      - "Don't Re-render unnecessarily"
    correctIndex: 1
    feedback: "DRY (Don't Repeat Yourself) in state: every piece of data has exactly one canonical location. Computed data is not stored — it's derived from the canonical location. One source, many derivations."
retrieval:
  recall: "What are three common types of redundant state in React forms?"
  explain: "Why is storing a selected item's full details (instead of its ID) a common mistake?"
  mistakeId:
    code: "const [items, setItems] = useState([]); const [itemsCopy, setItemsCopy] = useState([]);"
    answer: "itemsCopy is completely redundant — it's a duplicate of items with no source of truth. Any time items changes, itemsCopy must be manually kept in sync. Delete itemsCopy entirely. If you need to 'backup' items before an edit, use a different pattern (e.g., store the original separately only when editing begins)."
---

# Hook

Bad state design is one of the most common causes of React bugs — not wrong logic, but wrong structure. Redundant state — state that duplicates what's already available — creates an entire class of bugs where two versions of the truth disagree. This lesson is a diagnostic: how to spot and eliminate redundant state.

# Lore Introduction

*"The efficient ledger,"* Aelindra says, *"records each ingredient once. The inefficient ledger records both the ingredients and the dish, separately — and when the cook changes the recipe, the dish entry becomes wrong. One truth, many computations."*

# Core Learning

## Concept Introduction

Redundant state patterns to eliminate:

| Redundant | Replace With |
|---|---|
| `isLoading` when you have `status` | `const isLoading = status === 'loading'` |
| `isValid` when you have `errors` | `const isValid = errors.length === 0` |
| `fullName` when you have `first` + `last` | `const fullName = \`${first} ${last}\`` |
| `selectedDetails` when you have `items` + `selectedId` | `const selectedDetails = items.find(i => i.id === selectedId)` |
| `filteredItems` as state | `const filteredItems = items.filter(...)` |

## Why It Matters

Every redundant state variable requires a synchronisation burden. Miss one `setState` call, and the derived value becomes stale. Eliminate the variable; the derivation is always correct.

## Worked Example

**Before (redundant state):**
```jsx
const [items, setItems] = useState([]);
const [completedCount, setCompletedCount] = useState(0);
const [hasItems, setHasItems] = useState(false);

// Must remember to update all three!
function addItem(item) {
  setItems([...items, item]);
  setHasItems(true);
  // Forgot to update completedCount? Bug!
}
```

**After (derived):**
```jsx
const [items, setItems] = useState([]);
// Derived — always correct
const completedCount = items.filter(i => i.done).length;
const hasItems = items.length > 0;

function addItem(item) {
  setItems([...items, item]); // One update — everything else follows
}
```

## Common Mistakes

- **Storing `error` as a boolean when you have an error object.** `const hasError = error !== null`.
- **Storing page count when you have items array and page size.** `const pageCount = Math.ceil(items.length / pageSize)`.
- **Storing UI state derived from data state.** `const isEmpty = items.length === 0` not `const [isEmpty, setIsEmpty] = useState(true)`.

## Mini Summary

- Redundant state is any variable computable from existing state/props
- Eliminate it — compute during render instead
- One state update should cascade to all derived values automatically
- Fewer state variables = fewer sync bugs = simpler mental model

# Guided Practice Quest

Work through the guided steps on identifying and eliminating redundant state.

# Solo Practice Quest

Review this component and identify every redundant state variable, explaining what it should be replaced with:

```jsx
const [username, setUsername] = useState('');
const [password, setPassword] = useState('');
const [usernameLength, setUsernameLength] = useState(0);
const [passwordLength, setPasswordLength] = useState(0);
const [isFormValid, setIsFormValid] = useState(false);
const [submitError, setSubmitError] = useState(null);
const [hasError, setHasError] = useState(false);
```

Write the cleaned-up state declarations and all derived values.

# Integration

**Mathematics — Normalisation in Databases**

Database normalisation (1NF, 2NF, 3NF) is the process of eliminating redundant data by ensuring each fact is stored in exactly one place. The same principle applies to React state: each piece of information has exactly one canonical location; everything else is derived. Third Normal Form (3NF) requires that non-key attributes depend only on the primary key — not on other non-key attributes. In state terms: derived values (non-key) should not be stored alongside the values they're derived from (primary data). React state design is applied database normalisation.

# Lore Conclusion

*"Redundancy,"* Aelindra says, *"is not safety. It is risk. Two records that must agree will eventually disagree. Store the root. Derive the rest. This is not laziness — it is precision."*

---
