---
id: fe-jun-m6-11
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m6
moduleTitle: "Module 6: Component Design"
moduleGlyph: "🧩"
moduleSortOrder: 6
topicSlug: composition_patterns
topicTitle: "Composition Patterns"
topicSortOrder: 4
lesson: higher_order_components
title: "Higher-Order Components"
sortOrder: 2
difficulty: 5
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m6-10]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what a Higher-Order Component is (a function that takes a component and returns a new component)"
    - "Gives a real use-case where HOCs are useful"
    - "Compares HOCs to custom hooks and identifies when to prefer each"
  keywords: [higher-order, wrap, component, function, behaviour, hook, prefer]
  modelAnswer: |
    A Higher-Order Component is a function that accepts a component and returns a new component that wraps it with additional behaviour or props. Classic uses include withAuth (redirect if not logged in), withLogging (log renders), or withTheme (inject theme props). Today, custom hooks solve most of the same problems with less indirection — but HOCs are still useful for wrapping third-party components (which cannot use hooks) and for cross-cutting concerns that need to wrap the render tree itself, such as error boundaries.
guidedSteps:
  - id: fe-jun-m6-11-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "What does a Higher-Order Component (HOC) do?"
    inputConfig:
      options:
        - "It renders multiple child components at once"
        - "It is a function that takes a component and returns an enhanced component"
        - "It is a component that accepts functions as props"
        - "It is a component that manages global state"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["It is a function that takes a component and returns an enhanced component"]
      rejectedFeedback: "A HOC is a higher-order function applied to components: it takes a component as input and returns a new, enhanced component as output."
    hint: "Think about higher-order functions in JavaScript (like .map, .filter) — they take functions as arguments. HOCs do the same for components."
    reflectionPrompt: "What does it mean to 'enhance' a component via a HOC? What kinds of enhancements make sense?"
  - id: fe-jun-m6-11-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: "When should you prefer a custom hook over a HOC for sharing behaviour between components?"
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [hook, state, logic, simpler, readable, HOC, indirection]
      rejectedFeedback: "Prefer custom hooks when the shared behaviour involves state or effects and the consuming component is under your control — hooks are simpler and avoid the extra component wrapper."
    hint: "Consider the debugging experience: a HOC adds a wrapper to the component tree. A hook does not."
    reflectionPrompt: "In what scenario would you have NO choice but to use a HOC rather than a hook?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "You want to redirect unauthenticated users away from a route. Which approach is most appropriate?"
    options:
      - "A custom hook that returns true/false"
      - "A HOC (withAuth) that wraps the protected component and redirects if not logged in"
      - "Adding an if statement in every protected component"
      - "A utility function"
    correctIndex: 1
    feedback: "A HOC is appropriate here because the redirection wraps the render tree — if the user is not authenticated, the protected component should not render at all. This is a cross-cutting concern that wraps the component itself."
retrieval:
  recall: "What is the signature of a Higher-Order Component function?"
  explain: "Why do custom hooks generally replace HOCs for sharing stateful logic in modern React?"
  mistakeId:
    code: |
      const withLoading = (WrappedComponent) => {
        return (props) => {
          if (props.isLoading) return <Spinner />;
          return <WrappedComponent {...props} />;  // isLoading still in props!
        };
      };
    answer: "The isLoading prop is forwarded to WrappedComponent via {...props}, which may cause unexpected prop warnings or behaviour. Destructure and omit it: const { isLoading, ...rest } = props; return <WrappedComponent {...rest} />."
---

# Hook

You have 10 protected pages in your app. Each one starts with the same 5-line authentication check and redirect. Copy-pasted 10 times. Now you need to change the redirect logic. That is 10 files to find and update.

A Higher-Order Component solves this by wrapping the behaviour once and applying it everywhere.

# Lore Introduction

The Academy's enchanting press does not engrave every scroll individually. Instead, a master enchanter creates a **binding matrix** — a pattern that, when applied to any scroll, adds a standard protection layer. One binding matrix; infinite protected scrolls. The scroll itself does not need to know about the protection — the matrix handles it.

Higher-Order Components are your binding matrix.

# Core Learning

## Concept Introduction

A **Higher-Order Component (HOC)** is a function that takes a component and returns a new, enhanced component. It is the component equivalent of a higher-order function (a function that takes a function as an argument).

The pattern:
```
const EnhancedComponent = withSomething(OriginalComponent);
```

The HOC wraps the original component, adding behaviour before or around its render.

```tsx
// A simple withLoading HOC
function withLoading<T extends object>(
  WrappedComponent: React.ComponentType<T>
) {
  return function WithLoadingComponent({
    isLoading,
    ...props
  }: T & { isLoading: boolean }) {
    if (isLoading) {
      return (
        <div className="flex justify-center p-8">
          <div className="animate-spin h-8 w-8 border-4 border-blue-500 rounded-full border-t-transparent" />
        </div>
      );
    }
    return <WrappedComponent {...(props as T)} />;
  };
}

// Usage
const UserListWithLoading = withLoading(UserList);

<UserListWithLoading isLoading={loading} users={users} />
```

## A Real-World HOC: withAuth

```tsx
import { Navigate } from 'react-router-dom';
import { useAuth } from '@/hooks/useAuth';

function withAuth<T extends object>(WrappedComponent: React.ComponentType<T>) {
  return function ProtectedComponent(props: T) {
    const { isAuthenticated, isLoading } = useAuth();

    if (isLoading) return <Spinner />;
    if (!isAuthenticated) return <Navigate to="/login" replace />;

    return <WrappedComponent {...props} />;
  };
}

// Apply once to each protected page
const DashboardPage = withAuth(DashboardPageBase);
const SettingsPage = withAuth(SettingsPageBase);
const ProfilePage = withAuth(ProfilePageBase);
```

Changing the redirect path or loading behaviour now requires changing one HOC.

## HOCs vs Custom Hooks

Since React 16.8 introduced hooks, most of what HOCs did can be accomplished more simply with custom hooks:

| Scenario | HOC | Custom Hook |
|---|---|---|
| Wrapping third-party components | Yes — only option | No — hooks can't be added to components you don't own |
| Sharing stateful logic between your components | Possible but indirect | Preferred — simpler, no wrapper in the tree |
| Conditional rendering (e.g., redirect) | Yes — wraps the render tree | Not possible — hooks can't prevent rendering |
| Cross-cutting concerns (logging, error boundaries) | Yes | Error boundaries specifically require HOCs |

The modern guidance: **prefer custom hooks for stateful logic; use HOCs when you need to wrap the render tree itself.**

## Common Mistakes

**Not forwarding props correctly.** If the HOC does not spread `{...props}` onto the wrapped component, the wrapped component loses its props.

**Not forwarding refs.** By default, HOCs do not forward refs to the wrapped component. Use `React.forwardRef` if the wrapped component needs ref support.

**Stacking too many HOCs.** `withAuth(withLogging(withTheme(Component)))` creates a deep wrapper stack that is hard to debug in React DevTools. Custom hooks avoid this.

**Creating HOCs inside a render function.** Defining a HOC inside a component's render causes React to see it as a new component type on every render, destroying state. Define HOCs at module scope.

## Mini Summary

Higher-Order Components are functions that take a component and return an enhanced component, adding cross-cutting behaviour like authentication, logging, or loading states. They are most useful for wrapping render trees and enhancing third-party components. Custom hooks have replaced HOCs for most stateful-logic-sharing use-cases due to their simplicity and better debugging experience.

# Guided Practice Quest

Work through the steps to identify HOC use-cases and understand the props-forwarding pattern.

# Solo Practice Quest

Design a `withErrorBoundary` HOC that wraps a component and renders a fallback UI if the wrapped component throws. Describe:

1. The HOC's function signature
2. What state it needs internally
3. How it renders the fallback vs the wrapped component
4. Why this cannot easily be a custom hook

Write 4–6 sentences.

# Integration

**Mathematics — Higher-Order Functions:** In mathematics and functional programming, a higher-order function takes or returns other functions. `f(g) = h` where f takes function g and returns function h. HOCs are exactly this applied to React components — they are higher-order in the mathematical sense. Understanding HOFs in JavaScript (Array.map, Array.filter, function composition) provides direct intuition for HOCs.

**Philosophy — Decorator Pattern:** In object-oriented design, the Decorator pattern adds behaviour to an object at runtime by wrapping it in a decorator object with the same interface. HOCs are the functional programming equivalent: wrapping a component in a new component with the same props interface, adding behaviour transparently. Both approaches follow the Open/Closed Principle — the original is unchanged, the wrapper adds the extension.

# Lore Conclusion

The enchanting press applies binding matrices to scrolls without altering the scrolls themselves. The scrolls emerge enhanced — protected, or tracked, or translated — while remaining exactly what they were. Your HOCs operate on the same principle: wrap the component, add the behaviour, pass everything else through unchanged. Compose your binding matrices wisely, and your application gains powerful cross-cutting capabilities with a single enchantment.

---
