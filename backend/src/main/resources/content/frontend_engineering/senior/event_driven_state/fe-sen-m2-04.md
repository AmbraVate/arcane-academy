---
id: fe-sen-m2-04
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m2
moduleTitle: "Module 2: Advanced State Management"
moduleGlyph: "⚡"
moduleSortOrder: 2
topicSlug: event_driven_state
topicTitle: "Event-Driven State"
topicSortOrder: 4
lesson: event_driven_state
title: "Event-Driven State"
sortOrder: 4
difficulty: 4
estimatedMinutes: 30
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [systems_thinking, philosophy]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Explains pub/sub and event bus patterns in the frontend context
    - Articulates when event-driven state is appropriate vs prop drilling or global store
    - Discusses the tradeoffs of event-driven approaches (debugging difficulty, hidden coupling)
    - Describes how to implement a type-safe event bus in TypeScript
    - Addresses the relationship between custom events and framework-specific solutions
  keywords: [event bus, pub/sub, publisher, subscriber, decouple, CustomEvent, EventEmitter, broadcast, typed events, observable]
  modelAnswer: |
    Event-driven state uses a publish/subscribe (pub/sub) model to communicate between components or modules without direct references. A publisher emits an event; any number of subscribers receive it. Neither knows about the other directly.

    In the frontend, this is useful when: communicating between deeply nested components without prop drilling through unrelated ancestors, coordinating between micro-frontends that cannot share a React context, or triggering cross-cutting concerns (analytics, logging, notifications) from business events without coupling business code to infrastructure code.

    A type-safe event bus in TypeScript maps event names to their payload types, providing compile-time safety. The browser's native CustomEvent API can work for same-origin communication. Libraries like mitt or EventEmitter3 are popular.

    The primary tradeoff is debugging difficulty: event-driven code creates hidden coupling (components that respond to events are invisible to the component that emits them) and makes control flow harder to follow. Tools like Redux DevTools (which logs actions as events) mitigate this. Event-driven state should be used sparingly — when direct coupling (props, context, store) would create architectural problems, not as a general communication pattern.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A `<Sidebar>` component needs to open when a button deep inside `<ProductCard>` inside `<ProductGrid>` is clicked. ProductGrid, ProductList, and ProductCard are all 'dumb' display components with no knowledge of the Sidebar. Which solution is best?"
    options:
      - "Pass an `openSidebar` callback prop through ProductGrid -> ProductCard"
      - "Lift all state to the top-level App component and pass props everywhere"
      - "Put `sidebarOpen` in a shared store and have ProductCard dispatch an event/action"
      - "Use an event bus: ProductCard emits 'SIDEBAR_OPEN'; Sidebar subscribes"
    correctIndex: 2
    feedback: "Both a shared store and an event bus can work. A shared store (Zustand/Redux) is generally preferred because it maintains the connection to React's rendering model and is easier to debug. An event bus is appropriate when you cannot or don't want a shared store — e.g., in micro-frontends where store sharing is complex."
  - type: SHORT_TEXT
    prompt: "What is the memory leak risk with event bus subscriptions in React components, and how do you prevent it?"
    hint: "Think about component unmounting and the useEffect cleanup function."
  - type: FILL_BLANK
    prompt: "In a type-safe event bus using TypeScript, we define a type mapping event names to payload types. This allows the compiler to enforce that subscribers receive the correct ___ for each event."
    answer: "payload type"
    hint: "The data shape associated with a specific event name."
  - type: MULTIPLE_CHOICE
    prompt: "You have two micro-frontends (MFE-A and MFE-B) that cannot share a React store. MFE-A needs to notify MFE-B when a user logs in. What is the most appropriate approach?"
    options:
      - "Make a direct import from MFE-A into MFE-B's code"
      - "Use the browser's native CustomEvent / window.dispatchEvent with a documented event name and payload schema"
      - "Reload the entire page on login"
      - "Use localStorage polling"
    correctIndex: 1
    feedback: "The browser's native CustomEvent API dispatched on `window` is the standard cross-bundle communication channel for micro-frontends. Both MFEs can subscribe to and publish events via `window.addEventListener` / `window.dispatchEvent` without any shared code. The event name and payload schema become the explicit contract."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the primary risk of using an event bus heavily for component communication within a single React application?"
    options:
      - "Events are too slow for real-time UIs"
      - "It creates hidden coupling — the relationship between emitters and subscribers is invisible in the code, making debugging and refactoring difficult"
      - "React does not support event buses"
      - "Event buses cannot handle async operations"
    correctIndex: 1
    feedback: "The fundamental tradeoff of pub/sub is decoupling at the cost of traceability. You cannot look at a component that emits an event and immediately see what will respond to it. This makes debugging non-linear and refactoring risky. Within a React app, a shared store with explicit action dispatch is almost always more traceable. Event buses are best reserved for cross-bundle or cross-framework communication where direct coupling is impossible."
  - type: MULTIPLE_CHOICE
    question: "Which library is a popular, minimal event emitter for browser/Node.js use?"
    options:
      - "Redux"
      - "mitt"
      - "React Query"
      - "EventBridge"
    correctIndex: 1
    feedback: "`mitt` is a tiny (~200 bytes) type-safe event emitter library. It provides `on`, `off`, and `emit`. Its generic type parameter accepts a map of event names to payload types, enabling full TypeScript inference for both emitters and subscribers."
retrieval:
  recall: "Describe two scenarios where an event bus is the better architectural choice over a shared store."
  explain: "Explain what a memory leak in an event bus subscription looks like in a React component and how useEffect cleanup prevents it."
  mistakeId:
    code: |
      // components/NotificationBell.tsx
      import { eventBus } from '@/lib/eventBus';

      export function NotificationBell() {
        const [count, setCount] = useState(0);

        // Subscribe to new notification events
        eventBus.on('NEW_NOTIFICATION', () => {
          setCount(c => c + 1);
        });

        return <Bell count={count} />;
      }
    answer: "The subscription is created on every render but never cleaned up. Each re-render adds a new listener. After 10 renders, there are 10 listeners, each incrementing count — so a single 'NEW_NOTIFICATION' event increments count by 10. After unmounting and remounting, stale listeners from the first mount remain active in memory (memory leak). Fix: wrap in useEffect with a cleanup function: `useEffect(() => { const handler = () => setCount(c => c + 1); eventBus.on('NEW_NOTIFICATION', handler); return () => eventBus.off('NEW_NOTIFICATION', handler); }, []);`"
---

# Hook

Your notification system works perfectly for six months. Then the platform adds micro-frontends. The checkout team's MFE needs to show a toast notification when an order is placed — but the notification component lives in the shell application, and the shell cannot import from the checkout MFE at build time (that's the whole point of micro-frontends). Props and context don't cross bundle boundaries. You need a way for checkout to say "something happened" and for the shell to hear it, without either knowing about the other. Welcome to event-driven communication.

# Lore Introduction

When the Academy's towers became autonomous under the Guild structure, the apprentices faced a communication problem. A discovery in the Alchemy Tower needed to alert practitioners in the Healing Tower and the Combat Tower — but the towers had no direct passages. The solution: the Academy Bell. Any tower could ring a bell with a named tone; any tower could hang a listener that responded to that tone. The bell was the contract; the tone was the event name. No tower needed to know who was listening.

# Core Learning

## Concept Introduction

Event-driven state uses a **publish/subscribe (pub/sub)** model. Publishers emit named events; subscribers listen for them. Neither knows about the other directly.

**A type-safe event bus with `mitt`:**

```typescript
// lib/eventBus.ts
import mitt from 'mitt';

type AppEvents = {
  'order:placed': { orderId: string; total: number };
  'user:logged-in': { userId: string; displayName: string };
  'user:logged-out': void;
  'notification:show': { message: string; type: 'info' | 'success' | 'error' };
  'cart:cleared': void;
};

export const eventBus = mitt<AppEvents>();
```

**Publishing an event:**

```typescript
// features/checkout/hooks/useSubmitOrder.ts
import { eventBus } from '@/lib/eventBus';

export function useSubmitOrder() {
  return async (order: Order) => {
    const result = await api.submitOrder(order);
    eventBus.emit('order:placed', {
      orderId: result.orderId,
      total: order.total,
    });
    eventBus.emit('notification:show', {
      message: 'Order placed successfully!',
      type: 'success',
    });
  };
}
```

**Subscribing in a React component (with cleanup):**

```typescript
// shell/components/NotificationToast.tsx
import { useEffect, useState } from 'react';
import { eventBus } from '@/lib/eventBus';

export function NotificationToast() {
  const [notifications, setNotifications] = useState<Notification[]>([]);

  useEffect(() => {
    const handler = (payload: { message: string; type: string }) => {
      setNotifications(prev => [...prev, { ...payload, id: Date.now() }]);
    };

    eventBus.on('notification:show', handler);
    return () => eventBus.off('notification:show', handler);
    // Cleanup on unmount — prevents memory leaks and duplicate handlers
  }, []);

  return <ToastContainer notifications={notifications} />;
}
```

**Cross-bundle events with native CustomEvent (micro-frontends):**

```typescript
// checkout MFE — dispatches event to window
window.dispatchEvent(
  new CustomEvent('acme:order-placed', {
    detail: { orderId: '123', total: 99.99 },
    bubbles: true,
  })
);

// shell — listens on window
window.addEventListener('acme:order-placed', (event) => {
  const { orderId, total } = (event as CustomEvent).detail;
  showNotification(`Order ${orderId} placed for £${total}`);
});
```

## Why It Matters

**Cross-bundle communication**: events via `window` work across micro-frontend bundles where import-based coupling is impossible.

**Cross-cutting concerns**: analytics, logging, and monitoring can subscribe to domain events without the domain code knowing about them. Checkout emits `order:placed`; the analytics module listens and records the conversion. No coupling between business logic and tracking code.

**Decoupled UI updates**: a deeply nested component can emit `notification:show` without knowing where notifications are rendered or how many consumers exist.

## Common Mistakes

**Mistake 1: Event bus as the default communication pattern.** Within a React app, a shared store is almost always better — it's easier to trace, debug, and test. Event buses are for when direct coupling is architecturally impossible or undesirable.

**Mistake 2: Missing cleanup in React components.** Subscribing in a component without unsubscribing in `useEffect`'s cleanup function causes memory leaks and duplicate event handlers.

**Mistake 3: Event names without namespacing.** Generic names like `'click'` or `'update'` collide. Namespace events: `'checkout:order-placed'`, `'analytics:page-viewed'`.

**Mistake 4: Complex state management via events.** If multiple components need to derive computed values from event-driven state, you're reinventing a store badly. Use a store.

## Mental Model

An event bus is like a **radio broadcast tower**. The broadcaster (emitter) speaks into the microphone. Any receiver tuned to the right frequency hears it. The broadcaster doesn't know who is listening, and the listeners don't know who is broadcasting. The frequency (event name) and the content format (payload type) are the contract. Change the format without updating listeners, and radios fall silent.

Compare to a telephone call (direct function call): you know exactly who you're calling, and they know exactly who called. More coupling, but more traceable.

## Mini Summary

- Event-driven state uses pub/sub: publishers emit named events; subscribers listen without direct references
- `mitt` provides a minimal, type-safe event emitter for in-app communication
- `window.dispatchEvent / addEventListener` enables cross-bundle events in micro-frontend architectures
- Always unsubscribe in `useEffect` cleanup to prevent memory leaks
- Use event buses sparingly: for cross-bundle comms and cross-cutting concerns; use a store for most inter-component communication

# Guided Practice Quest

You are building an analytics integration. Business events (`product:viewed`, `cart:updated`, `order:placed`) are scattered across many components. Design an event-driven analytics bridge that captures these events without coupling business components to analytics code.

# Solo Practice Quest

You are architecting the communication layer for a shell application that hosts three micro-frontends: Catalogue, Cart, and Account. The following cross-MFE communication is required:
- When a user adds an item in Catalogue, Cart's badge count must update
- When a user logs in via Account, Catalogue must refresh to show personalised pricing
- When Account logs out, Cart must clear and Catalogue must reset

Design the event-driven communication system. Your answer should:

1. Define the full event schema (event names, payload types) as a TypeScript type map
2. Decide which events use an in-app event bus vs native `window.CustomEvent` and justify
3. Write the subscription setup for the Cart MFE in a React hook with proper cleanup
4. Identify one scenario where this event-driven approach would break and propose a mitigation
5. Describe how you would document the event contracts so that teams developing separate MFEs stay in sync

# Integration

**Systems Thinking — Loose Coupling:** Event-driven architectures embody the principle of loose coupling taken to its logical conclusion. Two systems that communicate only through a shared event channel have no compile-time dependency on each other. They can evolve independently, be replaced, or fail without directly breaking each other. The cost is that the contract (event name + payload) must be maintained explicitly — it is not enforced by the type system at the boundary.

**Philosophy — Causality:** David Hume argued that causality is not directly observable — we observe correlation and infer cause. In event-driven systems, causality is genuinely non-obvious: following the chain of events to find why something happened requires tooling (event logging, event replay). This is the philosophical cost of decoupling: we gain independence but lose direct causal visibility. Good tooling restores the visibility that decoupling removes.

# Lore Conclusion

The Academy Bell system worked well for a generation. Then it grew: dozens of tone combinations, hundreds of listeners. A practitioner in the Healing Tower rang a tone about a breakthrough in reagent synthesis. Seven other towers responded in ways the Healer never anticipated, some of which caused minor catastrophes. The Bell system needed governance: an event registry, written contracts for each tone, and a monitoring system that could trace which towers had responded to which bell. Decoupling created freedom. Freedom created the need for discipline.
