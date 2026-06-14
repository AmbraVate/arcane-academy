---
id: fe-jun-m6-07
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m6
moduleTitle: "Module 6: Component Design"
moduleGlyph: "🧩"
moduleSortOrder: 6
topicSlug: presentational_vs_container
topicTitle: "Presentational vs Container"
topicSortOrder: 3
lesson: the_pattern
title: "The Presentational/Container Pattern"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m6-06]
integrationDomains: [design, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Accurately describes what a presentational component does and does not do"
    - "Accurately describes what a container component does and does not do"
    - "Explains the benefit of separating these two roles"
  keywords: [presentational, container, data, render, fetch, logic, separate]
  modelAnswer: |
    A presentational component receives data via props and renders UI. It has no knowledge of where data comes from — no fetches, no store connections. A container component handles data concerns: fetching, selecting from a store, transforming data, and passing it down to presentational children. The separation means presentational components are highly reusable and testable in isolation, while containers can swap their data source without touching the UI.
guidedSteps:
  - id: fe-jun-m6-07-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Which characteristic defines a presentational component?"
    inputConfig:
      options:
        - "It fetches its own data using useEffect"
        - "It receives all data via props and renders UI with no side effects"
        - "It connects to the Redux store"
        - "It contains all the business logic for the feature"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["It receives all data via props and renders UI with no side effects"]
      rejectedFeedback: "Presentational components are 'dumb' — they only know how to render what they are given. Data sourcing is the container's job."
    hint: "Think about what a 'display-only' component would look like — no fetches, no stores, just props and JSX."
    reflectionPrompt: "If a presentational component doesn't know where data comes from, what does that mean for testing it?"
  - id: fe-jun-m6-07-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "Why does separating containers from presentational components make it easier to change the data source?"
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [container, swap, change, source, presentational, same]
      rejectedFeedback: "Because the presentational component only cares about props, you can swap the container (REST to GraphQL, Redux to React Query) without touching the UI component."
    hint: "If UserList only cares about getting a users prop, does it matter whether that data came from REST or GraphQL?"
    reflectionPrompt: "What would you have to change in a tightly coupled component if you migrated from REST to GraphQL?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A UserListContainer fetches users and passes them to UserList. UserList renders each user. What pattern is this?"
    options:
      - "Higher-Order Component"
      - "Presentational/Container pattern"
      - "Observer pattern"
      - "Provider pattern"
    correctIndex: 1
    feedback: "UserListContainer is the smart/container component (data concerns); UserList is the presentational component (rendering concerns). Classic presentational/container split."
retrieval:
  recall: "What are the two responsibilities that the presentational/container pattern separates?"
  explain: "How does the presentational/container pattern relate to the separation of concerns principle covered earlier?"
  mistakeId:
    code: |
      const UserList = ({ userId }: { userId: string }) => {
        const [users, setUsers] = useState([]);

        useEffect(() => {
          fetch(`/api/users`).then(r => r.json()).then(setUsers);
        }, []);

        return (
          <ul>
            {users.map(u => <li key={u.id}>{u.name}</li>)}
          </ul>
        );
      };
    answer: "UserList is doing both container work (fetching data) and presentational work (rendering). Extract a UserListContainer that fetches and passes users as a prop to a pure UserList component: <UserList users={users} />."
---

# Hook

You want to test whether your user list renders correctly for an empty state, a loading state, and a full list. But the component fetches its own data — so every test needs a network mock, an async wait, and a running API. What should be a 5-line test becomes a 30-line integration test.

The presentational/container pattern was designed for exactly this problem.

# Lore Introduction

In the Academy's great workshop, there is a division between the **Receivers** and the **Shapers**. Receivers venture into the world to gather raw materials. Shapers stay in the workshop and transform what is brought to them. A Shaper does not know or care where the materials came from — they only care about the transformation. A Receiver does not shape — they only gather and deliver.

This division means each role can be mastered, tested, and improved independently.

# Core Learning

## Concept Introduction

The **Presentational/Container** pattern (also called Smart/Dumb or Stateful/Stateless) separates two concerns that often end up mixed in the same component:

| Presentational (Dumb) | Container (Smart) |
|---|---|
| Receives data via props | Fetches or selects data |
| Renders UI | No rendering (or minimal) |
| No data fetching | Passes data to presentational children |
| No store connections | May connect to Redux/Zustand/React Query |
| Highly reusable | Specific to a feature |
| Easy to test in isolation | Tested with integration tests |

The pattern was popularised by Dan Abramov, who later noted it is not always necessary — but understanding it teaches the core discipline of separating rendering from data concerns.

## Why It Matters

Separating these concerns delivers three clear benefits:

1. **Testability:** Presentational components accept props and render deterministically. You can test every visual state by passing different props — no network, no store, no async needed.

2. **Reusability:** A `UserList` that accepts a `users` prop can render users from a REST API, a GraphQL query, or a static fixture. The container changes; the presentational component doesn't.

3. **Replaceability:** When you migrate from REST to React Query, only the container changes. The UI components are untouched.

## Worked Example

A monolithic component doing both jobs:

```tsx
// Mixed — fetches AND renders
const UserListPage = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('/api/users').then(r => r.json()).then(data => {
      setUsers(data);
      setLoading(false);
    });
  }, []);

  if (loading) return <Spinner />;

  return (
    <ul className="space-y-2">
      {users.map(u => (
        <li key={u.id} className="p-3 border rounded">
          <span className="font-medium">{u.name}</span>
          <span className="text-gray-500 text-sm ml-2">{u.email}</span>
        </li>
      ))}
    </ul>
  );
};
```

Split into container + presentational:

```tsx
// Presentational — only renders, fully testable with props alone
interface UserListProps {
  users: User[];
}

const UserList = ({ users }: UserListProps) => (
  <ul className="space-y-2">
    {users.map(u => (
      <li key={u.id} className="p-3 border rounded">
        <span className="font-medium">{u.name}</span>
        <span className="text-gray-500 text-sm ml-2">{u.email}</span>
      </li>
    ))}
  </ul>
);

// Container — only fetches and orchestrates
const UserListContainer = () => {
  const { data: users = [], isLoading } = useQuery(['users'], fetchUsers);

  if (isLoading) return <Spinner />;
  return <UserList users={users} />;
};
```

Now `UserList` tests are trivial: `render(<UserList users={mockUsers} />)`.

## Common Mistakes

**Making the container render complex UI.** If the container has substantial JSX, the separation is incomplete. Containers should either render nothing (returning the presentational child directly) or render only layout scaffolding.

**Making the presentational component fetch from context.** If `UserList` reaches into a React Query cache or Zustand store, it is no longer purely presentational — it has a hidden data dependency.

**Over-applying the pattern.** A simple component that fetches one value and renders one element does not need to be split. Apply the pattern when the rendering complexity and data complexity are both meaningful.

## Mini Summary

The presentational/container pattern separates rendering (presentational components) from data sourcing (containers). Presentational components are pure renderers driven by props; containers handle fetching, transformation, and orchestration. The separation improves testability, reusability, and the ability to swap data sources without touching the UI.

# Guided Practice Quest

Work through the steps to identify which components in a tree are containers and which are presentational, and practise the split.

# Solo Practice Quest

Take a component you have built or can imagine — one that both fetches data and renders it. Describe:

1. What the presentational component would look like (its props interface)
2. What the container would be responsible for
3. How you would test the presentational component without any network mocking

Write 4–6 sentences.

# Integration

**Philosophy — Division of Labour:** Adam Smith's foundational economic insight was that dividing complex work into specialised roles makes each role more efficient and improves the whole. The presentational/container pattern applies this to components: the "presenter" specialises in rendering, the "container" specialises in data. Each can be optimised and replaced independently.

**Design — Wireframes vs Data:** When designers produce wireframes, they show structure and layout without real data. This is precisely the contract of a presentational component: given the shape of the data, render this structure. The container fills in the real data later — just as a developer populates wireframes when building the real product.

# Lore Conclusion

The Receivers and Shapers of the Academy's workshop have produced the most reliable artefacts in the realm, precisely because neither tries to do the other's job. Your containers gather the raw data; your presentational components shape it into a beautiful UI. Keep the roles distinct, and both will reach their full potential.

---
