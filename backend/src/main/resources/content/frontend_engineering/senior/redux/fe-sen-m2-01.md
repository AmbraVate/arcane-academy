---
id: fe-sen-m2-01
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m2
moduleTitle: "Module 2: Advanced State Management"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: redux
topicTitle: "Redux"
topicSortOrder: 1
lesson: redux
title: "Redux"
sortOrder: 1
difficulty: 4
estimatedMinutes: 35
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [philosophy, systems_thinking]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Explains the Redux data flow (store, actions, reducers, selectors) accurately
    - Describes what Redux Toolkit adds over vanilla Redux and why it matters
    - Articulates when Redux is and is not the right tool
    - Discusses common Redux pitfalls (over-normalisation, selector mis-derivation, stale closures)
    - Addresses the relationship between Redux and server state (RTK Query)
  keywords: [store, action, reducer, selector, slice, RTK, immer, thunk, normalisation, reselect, memoised]
  modelAnswer: |
    Redux implements a unidirectional data flow. State lives in a single store. Components dispatch actions (plain objects with a type and payload). Reducers are pure functions that take current state and an action and return new state. Components subscribe to the store via selectors.

    Redux Toolkit (RTK) is the official, opinionated way to use Redux. It provides: createSlice (generates action creators and reducers with Immer for immutable updates using mutable syntax), configureStore (sets up the store with sensible defaults including Redux DevTools and thunk middleware), createAsyncThunk (standard pattern for async operations), and RTK Query (complete server state management with caching, invalidation, and optimistic updates).

    Redux is justified when: client-side state is complex with many interdependencies, multiple components need to derive different views from shared state, or time-travel debugging/audit logging of state changes is needed. It is overkill when: state is primarily server state (use RTK Query or TanStack Query instead), state is local to one component, or the team is small and the product is simple.

    Common pitfalls: over-normalising everything (not all state needs an entity adapter), creating memoised selectors without understanding referential equality (useSelector with an inline object selector triggers every render), and mixing server state in Redux store (leads to cache invalidation complexity).
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A component dispatches an action. Redux processes it through the reducer, which produces a new state. What happens next?"
    options:
      - "The entire React tree re-renders"
      - "Redux notifies all store subscribers; components using `useSelector` re-render only if their selected value changed"
      - "The action is sent to the server"
      - "The component that dispatched the action re-renders immediately"
    correctIndex: 1
    feedback: "Redux uses a subscription model. After a state update, all `useSelector` calls are re-evaluated. A component only re-renders if the value returned by its selector changed (using strict equality `===`). This is why memoised selectors (Reselect) matter — to prevent unnecessary re-renders when computing derived values."
  - type: SHORT_TEXT
    prompt: "What does Immer provide inside Redux Toolkit's `createSlice` reducers, and why does it matter for Redux's immutability rule?"
    hint: "Think about how you can write `state.count++` inside a RTK reducer without actually mutating the store."
  - type: FILL_BLANK
    prompt: "RTK Query's `providesTags` and `invalidatesTags` API handles automatic cache ___ when a mutation succeeds."
    answer: "invalidation"
    hint: "When you POST a new item, the list query needs to be refetched."
  - type: MULTIPLE_CHOICE
    prompt: "Which scenario is the BEST fit for putting state in Redux rather than local component state or server state management?"
    options:
      - "A form's current input values before submission"
      - "A list of users fetched from an API"
      - "A multi-step wizard where each step's selections affect what subsequent steps display, and a summary sidebar shows all selections simultaneously"
      - "Whether a tooltip is open or closed"
    correctIndex: 2
    feedback: "The multi-step wizard represents complex client-side state with cross-component dependencies. Multiple components (each wizard step, the summary sidebar) need to read and derive views from the same shared state. This is Redux's sweet spot. Form inputs belong in local state (or React Hook Form). API data belongs in RTK Query or TanStack Query."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the primary benefit of `createSlice` from Redux Toolkit over writing actions and reducers manually?"
    options:
      - "It makes Redux work without a store"
      - "It auto-generates action creators, action type strings, and the reducer in one place, eliminating boilerplate"
      - "It replaces the need for selectors"
      - "It makes Redux synchronous by default"
    correctIndex: 1
    feedback: "`createSlice` collocates the action types, action creators, and reducer logic in one definition. With vanilla Redux you had separate `ACTION_TYPE` constants, action creator functions, and reducer switch statements. Slice eliminates this fragmentation and uses Immer under the hood so you can write intuitive mutable-style updates that produce immutable state."
  - type: MULTIPLE_CHOICE
    question: "Why does `useSelector(state => ({ user: state.user, posts: state.posts }))` cause a performance problem?"
    options:
      - "Objects are invalid selector return values"
      - "A new object literal is created on every render, so the equality check always returns false and the component always re-renders"
      - "Redux does not support multiple values in one selector"
      - "Arrow functions cannot be used with useSelector"
    correctIndex: 1
    feedback: "`useSelector` uses `===` equality to determine if the selected value changed. An inline object literal `{ user, posts }` creates a new object reference on every call, even if the values inside are identical. This means the component always re-renders. Fix: use separate `useSelector` calls, or use `shallowEqual` as the second argument, or create a memoised selector with Reselect."
retrieval:
  recall: "Describe the Redux unidirectional data flow in four steps."
  explain: "Explain why putting server-fetched data (e.g., a list of products from an API) inside a Redux reducer is considered an anti-pattern, and what the alternative is."
  mistakeId:
    code: |
      // store/userSlice.ts
      const userSlice = createSlice({
        name: 'user',
        initialState: { users: [] as User[], loading: false },
        reducers: {
          fetchUsersStart: (state) => { state.loading = true; },
          fetchUsersSuccess: (state, action) => {
            state.loading = false;
            state.users = action.payload;
          },
          fetchUsersFailure: (state) => { state.loading = false; },
        },
      });

      // Used in a component
      useEffect(() => {
        dispatch(fetchUsersStart());
        fetch('/api/users')
          .then(r => r.json())
          .then(data => dispatch(fetchUsersSuccess(data)))
          .catch(() => dispatch(fetchUsersFailure()));
      }, []);
    answer: "This is the anti-pattern of hand-rolling server state management in Redux. It requires manually tracking loading/error states, has no caching, no automatic refetching, no deduplication of requests, and will refetch on every render where the useEffect fires. The correct approach is RTK Query: `createApi` with `getUsers` endpoint handles loading states, caching, deduplication, and invalidation automatically. The 40 lines shown reduce to 3 lines: `const { data: users, isLoading } = useGetUsersQuery()`."
---

# Hook

You join a team maintaining a 4-year-old React app. The Redux store has 23 slices. Forty percent of the state is data fetched from APIs — user lists, product catalogs, order histories — all with hand-written loading/error/data patterns. The other sixty percent is complex multi-screen UI state. You need to understand the state of the entire application before you can change anything. Redux is blamed for the complexity, but the problem isn't Redux. The problem is using Redux for everything.

# Lore Introduction

In the Arcane Academy's hall of records, the Grand Registry was a single, immutable ledger. Every change to any record required a formal petition (action), processed by the Registry's scribes (reducers), who produced a new authoritative state. The ledger could be read at any historical moment — perfect for audit. Perfect for complex interdependencies. Dangerously over-engineered for writing down whether a candle was lit.

# Core Learning

## Concept Introduction

Redux implements **unidirectional data flow**: actions flow into reducers, reducers produce new state, components subscribe to state via selectors.

```typescript
// store/cartSlice.ts
import { createSlice, PayloadAction, createSelector } from '@reduxjs/toolkit';

interface CartItem {
  id: string;
  name: string;
  price: number;
  quantity: number;
}

interface CartState {
  items: CartItem[];
  couponCode: string | null;
}

const cartSlice = createSlice({
  name: 'cart',
  initialState: { items: [], couponCode: null } as CartState,
  reducers: {
    addItem: (state, action: PayloadAction<CartItem>) => {
      const existing = state.items.find(i => i.id === action.payload.id);
      if (existing) {
        existing.quantity += action.payload.quantity; // Immer makes this safe
      } else {
        state.items.push(action.payload);
      }
    },
    removeItem: (state, action: PayloadAction<string>) => {
      state.items = state.items.filter(i => i.id !== action.payload);
    },
    applyCoupon: (state, action: PayloadAction<string>) => {
      state.couponCode = action.payload;
    },
  },
});

export const { addItem, removeItem, applyCoupon } = cartSlice.actions;

// Memoised selector — recomputes only when items change
export const selectCartTotal = createSelector(
  (state: RootState) => state.cart.items,
  (items) => items.reduce((sum, item) => sum + item.price * item.quantity, 0)
);
```

**RTK Query for server state:**

```typescript
// store/api.ts
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

export const api = createApi({
  reducerPath: 'api',
  baseQuery: fetchBaseQuery({ baseUrl: '/api' }),
  tagTypes: ['Product', 'Order'],
  endpoints: (builder) => ({
    getProducts: builder.query<Product[], void>({
      query: () => '/products',
      providesTags: ['Product'],
    }),
    createOrder: builder.mutation<Order, CreateOrderInput>({
      query: (body) => ({ url: '/orders', method: 'POST', body }),
      invalidatesTags: ['Product'], // Refetch products after ordering
    }),
  }),
});

export const { useGetProductsQuery, useCreateOrderMutation } = api;
```

## Why It Matters

**Predictability**: given any action sequence, the state is deterministic. Redux DevTools lets you time-travel through state changes, replay actions, and inspect exactly what changed.

**Shared state without prop drilling**: deeply nested components can read from the store without intermediate components passing props through.

**Derived state**: selectors with Reselect compute derived values (cart total, filtered lists) efficiently — recomputing only when dependencies change.

## Common Mistakes

**Mistake 1: Storing server state in Redux.** API data (user lists, products) has its own cache management needs (expiry, invalidation, background refetch). Hand-rolling this in Redux reducers is complex and fragile. Use RTK Query or TanStack Query for server state.

**Mistake 2: Inline object selectors.** `useSelector(state => ({ a: state.a, b: state.b }))` creates a new object reference every render, triggering unnecessary re-renders. Either use separate `useSelector` calls or pass `shallowEqual` as the equality function.

**Mistake 3: Normalising everything.** Entity adapters are great for collections of items with relationships. Simple lists or single objects don't need an entity adapter's complexity.

**Mistake 4: Dispatching many small actions per user interaction.** A single user action (submitting a form) that requires updating multiple state fields should ideally be one action, not five sequential dispatches. Each dispatch triggers a re-render cycle.

## Mental Model

Redux is like a **company's official filing system**. Every change goes through formal paperwork (actions), filed by trained staff (reducers) who follow strict rules. You can audit every change. Multiple departments (components) read from the official record. It is excellent for complex, shared, auditable state.

Using Redux for "is this dropdown open?" is like filing a formal document every time you open a desk drawer. The overhead is not justified by the value.

## Mini Summary

- Redux Toolkit (createSlice, configureStore, createAsyncThunk, RTK Query) is the only way to write modern Redux
- Immer inside createSlice enables mutable-looking syntax that produces immutable state
- Server state belongs in RTK Query or TanStack Query — not in hand-written reducer patterns
- Inline object selectors in `useSelector` cause unnecessary re-renders; use Reselect for derived values
- Redux is justified for complex, shared client-side state with interdependencies; not for local or server state

# Guided Practice Quest

You are reviewing a Redux codebase. The store has 15 slices. Analyse the slice list provided and identify which state should be: (a) kept in Redux, (b) moved to RTK Query, and (c) moved to local component state.

# Solo Practice Quest

You are building the state management layer for a multi-step checkout flow. The flow has 4 steps: Cart, Shipping, Payment, and Review. The Review step shows a complete summary of all previous steps. An analytics sidebar shows cart value, estimated delivery date, and selected payment method throughout all steps.

Design the Redux state for this flow. Your answer should:

1. Define the slice structure (name, shape of state, reducers)
2. Identify which data should NOT be in Redux (form field values, API data) and justify
3. Write at least two memoised selectors using createSelector, explaining why memoisation matters for each
4. Describe how RTK Query handles the final `POST /orders` mutation and what cache tags it invalidates
5. Explain how you would test the reducers in isolation from React

# Integration

**Philosophy — Immutability:** The Redux requirement for immutable state updates connects to a deep principle in functional programming: referential transparency. A pure function (reducer) given the same inputs always produces the same outputs and has no side effects. This makes Redux reducers easy to reason about, test in isolation, and reason about historically (time-travel debugging). The constraint of immutability is the price of this clarity.

**Systems Thinking — Single Source of Truth:** Redux's store as the single source of truth eliminates the class of bugs caused by state desynchronisation. When multiple components maintain their own local copies of the same data, they can drift out of sync. A single store means there is one authoritative answer to "what is the cart contents?" — reducing a category of bugs to zero.

# Lore Conclusion

The Grand Registry was powerful precisely because it was the single source of truth. Disputes were resolved by consulting the ledger. History was perfectly reconstructible. But the Registry worked because the scribes had clear rules, the petitions had clear schemas, and everyone agreed that the ledger was authoritative. Redux, like the Registry, works best when its jurisdiction is clear — complex, shared, auditable client state — and when everything else is handled by lighter means.
