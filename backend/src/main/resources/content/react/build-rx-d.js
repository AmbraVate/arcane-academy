// build-rx-d.js — generates rx-d.json (React Capstone: The Guild Portal)
const fs = require("fs");
const path = require("path");

const chunk = {
  id: "rx-d",
  title: "React Capstone — The Guild Portal",
  tier: "CAPSTONE",
  topicId: "react",
  prerequisites: ["rx-a", "rx-b", "rx-c"],
  description: "Build a complete, deployed React application from scratch. You will architect a multi-page Guild Portal — a quest-browsing app with routing, data fetching, TypeScript, global state, and a live public URL. This is your proof of mastery.",
  subChunks: [

    // ── rx-d1 ──────────────────────────────────────────────────────────────────
    {
      id: "rx-d1",
      title: "Project Architecture: Laying the Foundation",
      xpReward: 100,
      filename: "App.tsx",
      hookHtml: `<p>Every great structure starts with a blueprint. Before you write a single component, you decide <em>how the pieces fit together</em> — folder structure, routing skeleton, shared types. Get this right and the rest flows naturally. Get it wrong and you'll be refactoring forever.</p>`,

      explanationHtml: `
<h2>Architecting a React App</h2>

<h3>The folder structure we'll use</h3>
<pre><code>src/
  components/     # Reusable UI pieces
  pages/          # Route-level components (one per URL)
  hooks/          # Custom hooks
  store/          # Zustand stores
  types/          # TypeScript interfaces
  api/            # Fetch helpers
  App.tsx         # Routes live here
  main.tsx        # ReactDOM.createRoot</code></pre>

<h3>Routing skeleton first</h3>
<p>Define all your routes before you fill in the pages. This forces you to think about navigation flows upfront.</p>
<pre><code>// App.tsx
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Layout } from "./components/Layout";
import { HomePage } from "./pages/HomePage";
import { QuestsPage } from "./pages/QuestsPage";
import { QuestDetailPage } from "./pages/QuestDetailPage";
import { ProfilePage } from "./pages/ProfilePage";

export default function App() {
  return (
    &lt;BrowserRouter&gt;
      &lt;Routes&gt;
        &lt;Route path="/" element={&lt;Layout /&gt;}&gt;
          &lt;Route index element={&lt;HomePage /&gt;} /&gt;
          &lt;Route path="quests" element={&lt;QuestsPage /&gt;} /&gt;
          &lt;Route path="quests/:id" element={&lt;QuestDetailPage /&gt;} /&gt;
          &lt;Route path="profile" element={&lt;ProfilePage /&gt;} /&gt;
        &lt;/Route&gt;
      &lt;/Routes&gt;
    &lt;/BrowserRouter&gt;
  );
}</code></pre>

<h3>Shared TypeScript types</h3>
<p>Define your data shapes once, import everywhere. This eliminates an entire category of bugs.</p>
<pre><code>// src/types/index.ts
export interface Quest {
  id: number;
  title: string;
  difficulty: "FOUNDATION" | "PRACTITIONER" | "EXPERT";
  xpReward: number;
  completed: boolean;
  tags: string[];
  description: string;
}

export interface User {
  id: number;
  username: string;
  totalXp: number;
  rank: string;
  completedQuestIds: number[];
}</code></pre>

<h3>The Layout component — persistent shell</h3>
<p>Use React Router's <code>Outlet</code> to render child routes inside a persistent navbar/shell. The nav never re-mounts; only the page content swaps.</p>
<pre><code>// src/components/Layout.tsx
import { Outlet, NavLink } from "react-router-dom";

export function Layout() {
  return (
    &lt;div className="app-shell"&gt;
      &lt;nav className="navbar"&gt;
        &lt;span className="brand"&gt;⚗️ Guild Portal&lt;/span&gt;
        &lt;NavLink to="/"&gt;Home&lt;/NavLink&gt;
        &lt;NavLink to="/quests"&gt;Quests&lt;/NavLink&gt;
        &lt;NavLink to="/profile"&gt;Profile&lt;/NavLink&gt;
      &lt;/nav&gt;
      &lt;main className="page-content"&gt;
        &lt;Outlet /&gt;
      &lt;/main&gt;
    &lt;/div&gt;
  );
}</code></pre>

<h3>NavLink vs Link</h3>
<p><code>NavLink</code> automatically adds an <code>active</code> class when its <code>to</code> matches the current URL — perfect for highlighting the current nav item without manual state.</p>
      `,

      storyBeats: [
        {
          type: "narration",
          text: "The Grand Archmage Solomon unrolls a vast scroll across the war table. It's a map — not of lands, but of components."
        },
        {
          speaker: "Solomon",
          emoji: "🧙",
          type: "dialogue",
          cssClass: "solomon",
          text: "You have learned each spell in isolation. Now comes the true test: building something complete. A Guild Portal — where apprentices browse quests, track their progress, and prove their worth."
        },
        {
          type: "narration",
          text: "Vex appears beside you, toolkit already open."
        },
        {
          speaker: "Vex",
          emoji: "⚗️",
          type: "dialogue",
          cssClass: "vex",
          text: "The secret architects know: spend twenty minutes on structure and you save hours of refactoring. Routing skeleton, shared types, layout shell — then fill in the pages. Let's start there."
        }
      ],

      guidedPracticeHtml: `
<h3>🏗️ Guided: Build the App Shell</h3>
<p>You're starting with a blank <code>App.tsx</code>. Wire up the routing skeleton with three routes, a Layout component that uses Outlet, and the NavLink navbar.</p>
<p><strong>Requirements:</strong></p>
<ul>
  <li>BrowserRouter wrapping everything</li>
  <li>Layout route at <code>/</code> with a <code>data-testid="layout"</code> wrapper div</li>
  <li>Three child routes: index (HomePage), <code>/quests</code> (QuestsPage), <code>/quests/:id</code> (QuestDetailPage)</li>
  <li>Navbar with NavLinks to <code>/</code>, <code>/quests</code>, and a brand span reading "Guild Portal"</li>
  <li>Each page renders a heading with its name: "Home", "Quests", "Quest Detail"</li>
</ul>
      `,
      guidedPracticeStarterCode: `import { BrowserRouter, Routes, Route, NavLink, Outlet, useParams } from "react-router-dom";

function Layout() {
  return (
    <div data-testid="layout">
      <nav>
        {/* Add brand span and NavLinks here */}
      </nav>
      <main>
        {/* Outlet renders the matched child route */}
      </main>
    </div>
  );
}

function HomePage() {
  return <h1>Home</h1>;
}

function QuestsPage() {
  return <h1>Quests</h1>;
}

function QuestDetailPage() {
  const { id } = useParams();
  return <h1>Quest Detail</h1>;
}

export default function App() {
  // Wire up BrowserRouter + Routes + Route here
  return <div>TODO</div>;
}`,
      guidedPracticeTests: [
        { selector: "[data-testid='layout']", exists: true },
        { selector: "nav", exists: true },
        { selector: "nav", requiredText: "Guild Portal" },
        { selector: "h1", requiredText: "Home" },
        { action: "navigate", path: "/quests", then: { selector: "h1", requiredText: "Quests" } },
        { action: "navigate", path: "/quests/42", then: { selector: "h1", requiredText: "Quest Detail" } }
      ],

      soloPracticeHtml: `
<h3>🧩 Solo: Add a Profile Route</h3>
<p>Extend the app shell from guided practice:</p>
<ul>
  <li>Add a <code>/profile</code> route rendering a <code>ProfilePage</code> component</li>
  <li>ProfilePage renders <code>&lt;h1&gt;Profile&lt;/h1&gt;</code></li>
  <li>Add a NavLink for <code>/profile</code> in the navbar</li>
  <li>Navigating to <code>/unknown-route</code> should render a <code>&lt;p&gt;404 — Page not found&lt;/p&gt;</code> catch-all</li>
</ul>
      `,

      feynmanPrompt: "Explain why you define routes in App.tsx before filling in page components, as if you were advising a junior developer starting their first React project.",

      questions: [
        {
          id: "rx-d1-q1",
          type: "MCQ",
          text: "What does React Router's <Outlet /> render?",
          options: [
            "The nearest parent route's component",
            "The matched child route's component",
            "A loading spinner until navigation completes",
            "The root <App /> component"
          ],
          correctIndex: 1,
          explanation: "Outlet is a placeholder inside a layout route that renders whichever child route currently matches the URL."
        },
        {
          id: "rx-d1-q2",
          type: "MCQ",
          text: "What is the advantage of NavLink over Link in a navigation bar?",
          options: [
            "NavLink is faster because it skips the virtual DOM",
            "NavLink automatically applies an active CSS class when its href matches the current URL",
            "NavLink prevents page reload, whereas Link causes a full refresh",
            "NavLink supports data-fetching; Link does not"
          ],
          correctIndex: 1,
          explanation: "NavLink adds an 'active' class (customisable) when its 'to' prop matches the current location — perfect for highlighting the current nav item."
        },
        {
          id: "rx-d1-q3",
          type: "MCQ",
          text: "Where is the best place to define shared TypeScript interfaces (e.g. Quest, User) in a multi-page React app?",
          options: [
            "Inline in each component that uses them",
            "In a dedicated src/types/index.ts file, imported wherever needed",
            "In the global window object for easy access",
            "In the store file alongside Zustand state"
          ],
          correctIndex: 1,
          explanation: "A central types/index.ts file means you define the shape once and import it everywhere — one source of truth, no duplication."
        },
        {
          id: "rx-d1-q4",
          type: "MCQ",
          text: "Why is it recommended to set up your routing skeleton before filling in page content?",
          options: [
            "React Router requires all routes to be declared before any components render",
            "It forces you to think through navigation flows and data needs upfront, reducing later refactoring",
            "TypeScript will error if routes reference components that don't exist yet",
            "The browser caches route definitions and requires them at startup"
          ],
          correctIndex: 1,
          explanation: "Defining routes first is an architectural discipline — you reason about what pages exist and how users move between them before writing any page logic."
        }
      ]
    },

    // ── rx-d2 ──────────────────────────────────────────────────────────────────
    {
      id: "rx-d2",
      title: "The Quest Browser: Data, Filtering, and Lists",
      xpReward: 125,
      filename: "QuestsPage.tsx",
      hookHtml: `<p>The beating heart of the Guild Portal is the quest browser. Learners need to see all available quests, filter by difficulty, and find what to tackle next. This page exercises every pattern from the Practitioner tier in one cohesive feature.</p>`,

      explanationHtml: `
<h2>Building the Quest Browser</h2>

<h3>Simulating an API with local data</h3>
<p>In a real app you'd fetch from a server. For the capstone we'll use a local mock so you focus on the React patterns, not network setup.</p>
<pre><code>// src/api/quests.ts
import { Quest } from "../types";

const MOCK_QUESTS: Quest[] = [
  { id: 1, title: "The Component Model", difficulty: "FOUNDATION", xpReward: 75,
    completed: true, tags: ["jsx", "components"], description: "Learn JSX and build your first component." },
  { id: 2, title: "Props: Feeding Components Data", difficulty: "FOUNDATION", xpReward: 75,
    completed: true, tags: ["props", "typescript"], description: "Pass data into components via props." },
  { id: 3, title: "State and useState", difficulty: "FOUNDATION", xpReward: 100,
    completed: false, tags: ["state", "hooks"], description: "Add interactivity with the useState hook." },
  { id: 4, title: "useEffect: Side Effects", difficulty: "PRACTITIONER", xpReward: 125,
    completed: false, tags: ["hooks", "effects"], description: "Synchronise with the outside world." },
  { id: 5, title: "Context API", difficulty: "EXPERT", xpReward: 175,
    completed: false, tags: ["context", "state"], description: "Share state without prop drilling." },
  { id: 6, title: "TypeScript with React", difficulty: "EXPERT", xpReward: 200,
    completed: false, tags: ["typescript"], description: "Add full type safety to your components." },
];

// Simulate async fetch
export async function fetchQuests(): Promise&lt;Quest[]&gt; {
  return new Promise(resolve => setTimeout(() => resolve(MOCK_QUESTS), 300));
}</code></pre>

<h3>Three-state loading pattern (typed)</h3>
<pre><code>type LoadState = "loading" | "error" | "success";

function QuestsPage() {
  const [quests, setQuests] = useState&lt;Quest[]&gt;([]);
  const [status, setStatus] = useState&lt;LoadState&gt;("loading");
  const [filter, setFilter] = useState&lt;"ALL" | Quest["difficulty"]&gt;("ALL");

  useEffect(() => {
    let cancelled = false;
    fetchQuests()
      .then(data => { if (!cancelled) { setQuests(data); setStatus("success"); } })
      .catch(() => { if (!cancelled) setStatus("error"); });
    return () => { cancelled = true; };
  }, []);
  // ...
}</code></pre>

<h3>Derived state via useMemo</h3>
<pre><code>const visibleQuests = useMemo(
  () => filter === "ALL" ? quests : quests.filter(q => q.difficulty === filter),
  [quests, filter]
);</code></pre>

<h3>The QuestCard component</h3>
<pre><code>interface QuestCardProps {
  quest: Quest;
  onSelect: (id: number) => void;
}

function QuestCard({ quest, onSelect }: QuestCardProps) {
  return (
    &lt;div
      className={\`quest-card \${quest.completed ? "completed" : ""}\`}
      data-testid="quest-card"
    &gt;
      &lt;div className="quest-header"&gt;
        &lt;h3&gt;{quest.title}&lt;/h3&gt;
        &lt;span className={\`badge badge-\${quest.difficulty.toLowerCase()}\`}&gt;
          {quest.difficulty}
        &lt;/span&gt;
      &lt;/div&gt;
      &lt;p&gt;{quest.description}&lt;/p&gt;
      &lt;div className="quest-footer"&gt;
        &lt;span className="xp"&gt;+{quest.xpReward} XP&lt;/span&gt;
        {quest.completed
          ? &lt;span className="done"&gt;✓ Complete&lt;/span&gt;
          : &lt;button onClick={() =&gt; onSelect(quest.id)}&gt;Start Quest&lt;/button&gt;
        }
      &lt;/div&gt;
    &lt;/div&gt;
  );
}</code></pre>
      `,

      storyBeats: [
        {
          type: "narration",
          text: "The Quest Board flickers into existence — hundreds of glowing parchments, each a challenge waiting to be accepted."
        },
        {
          speaker: "Vex",
          emoji: "⚗️",
          type: "dialogue",
          cssClass: "vex",
          text: "A board with no filter is just noise. The apprentice's first act of craft is making information findable. Three-state loading, derived filter state, a card per quest — patterns you already know, now working in concert."
        }
      ],

      guidedPracticeHtml: `
<h3>🏗️ Guided: Build QuestsPage with Filtering</h3>
<p>Build a page that fetches quest data (use the mock below), shows a loading state, then renders filterable quest cards.</p>
<p><strong>Mock data is injected — use the <code>fetchQuests</code> function already available in scope.</strong></p>
<p><strong>Requirements:</strong></p>
<ul>
  <li>Show <code>data-testid="loading"</code> while fetching</li>
  <li>Show <code>data-testid="quest-list"</code> with one <code>data-testid="quest-card"</code> per quest</li>
  <li>Each card shows the quest title and difficulty</li>
  <li>Filter buttons: ALL, FOUNDATION, PRACTITIONER, EXPERT — wrap in <code>data-testid="filters"</code></li>
  <li>Clicking a filter shows only matching quests (or all for ALL)</li>
  <li>Active filter button has class <code>active</code></li>
</ul>
      `,
      guidedPracticeStarterCode: `import { useState, useEffect, useMemo } from "react";

// Mock data available in scope:
// interface Quest { id, title, difficulty, xpReward, completed, description }
// async function fetchQuests(): Promise<Quest[]>

export default function QuestsPage() {
  const [quests, setQuests] = useState([]);
  const [status, setStatus] = useState("loading");
  const [filter, setFilter] = useState("ALL");

  useEffect(() => {
    // Fetch quests here — remember the cancellation flag pattern
  }, []);

  const visibleQuests = useMemo(
    () => filter === "ALL" ? quests : quests.filter(q => q.difficulty === filter),
    [quests, filter]
  );

  if (status === "loading") return <div data-testid="loading">Loading quests…</div>;
  if (status === "error") return <p>Failed to load quests.</p>;

  return (
    <div>
      <div data-testid="filters">
        {/* Render filter buttons: ALL, FOUNDATION, PRACTITIONER, EXPERT */}
      </div>
      <div data-testid="quest-list">
        {/* Map visibleQuests to quest cards */}
      </div>
    </div>
  );
}`,
      guidedPracticeTests: [
        { selector: "[data-testid='loading']", exists: true },
        { action: "wait", ms: 400, then: { selector: "[data-testid='quest-list']", exists: true } },
        { selector: "[data-testid='quest-card']", count: 6 },
        { selector: "[data-testid='filters']", exists: true },
        {
          action: "click", selector: "button[data-filter='FOUNDATION']",
          then: { selector: "[data-testid='quest-card']", count: 2 }
        },
        {
          action: "click", selector: "button[data-filter='EXPERT']",
          then: { selector: "[data-testid='quest-card']", count: 2 }
        },
        {
          action: "click", selector: "button[data-filter='ALL']",
          then: { selector: "[data-testid='quest-card']", count: 6 }
        }
      ],

      soloPracticeHtml: `
<h3>🧩 Solo: Add a Search Bar</h3>
<p>Extend the quest list with text search:</p>
<ul>
  <li>Add a text input with <code>data-testid="search"</code> and placeholder "Search quests…"</li>
  <li>Filter cards so only quests whose title <strong>or</strong> description contains the search string (case-insensitive) are shown</li>
  <li>Search and difficulty filter should work together (a quest must pass both)</li>
  <li>Show <code>data-testid="empty-state"</code> with text "No quests match your search" when the combined filter produces 0 results</li>
</ul>
      `,

      feynmanPrompt: "Explain the three-state loading pattern (loading / error / success) to someone who just learned useState yesterday — why three states instead of just a boolean?",

      questions: [
        {
          id: "rx-d2-q1",
          type: "MCQ",
          text: "Why use useMemo for the filtered quest list rather than filtering directly in the render?",
          options: [
            "useMemo is required whenever you call array methods in React",
            "useMemo caches the filtered result and only recomputes when quests or filter changes, avoiding unnecessary work on every render",
            "Filtering in render causes an infinite re-render loop",
            "useMemo makes the filter synchronous, whereas inline filtering is asynchronous"
          ],
          correctIndex: 1,
          explanation: "useMemo memoises expensive computations. Filtering a large array on every render (e.g. triggered by an unrelated state change) wastes CPU. useMemo only recomputes when its dependencies change."
        },
        {
          id: "rx-d2-q2",
          type: "MCQ",
          text: "What does the cancellation flag pattern (let cancelled = false) prevent in a useEffect data fetch?",
          options: [
            "It prevents duplicate API calls when React strict mode double-invokes effects",
            "It prevents setting state on an unmounted component if the response arrives after navigation away",
            "It prevents the browser from caching the fetch response",
            "It prevents TypeScript from inferring the wrong return type"
          ],
          correctIndex: 1,
          explanation: "If a user navigates away before the fetch resolves, the component unmounts. Without a cancellation flag, the .then() still runs and calls setState on the unmounted component — a React warning and potential memory leak."
        },
        {
          id: "rx-d2-q3",
          type: "MCQ",
          text: "A QuestCard receives a quest prop of type Quest. Inside the card, you render quest.difficulty. TypeScript shows no error. Later the API adds a new difficulty level 'ARCHIVED' not in the union type. What happens?",
          options: [
            "TypeScript automatically widens the union to include 'ARCHIVED'",
            "The app crashes immediately when the data loads",
            "TypeScript won't error at compile time, but runtime data won't match the declared type — a reminder that TypeScript only validates compile-time structure, not runtime API responses",
            "The browser silently coerces 'ARCHIVED' to undefined"
          ],
          correctIndex: 2,
          explanation: "TypeScript types are erased at runtime. If the API returns a value outside the declared union, TypeScript won't catch it. Use runtime validation (Zod, type guards) for data that crosses an API boundary."
        },
        {
          id: "rx-d2-q4",
          type: "MCQ",
          text: "You want the quest list to re-filter whenever either quests or filter changes, but not on every render. Which hook and dependency array is correct?",
          options: [
            "useEffect(() => { setVisible(...) }, [quests, filter])",
            "useMemo(() => filter === 'ALL' ? quests : quests.filter(...), [quests, filter])",
            "useState(quests.filter(...))",
            "useCallback(() => quests.filter(...), [quests, filter])"
          ],
          correctIndex: 1,
          explanation: "useMemo computes a derived value and caches it. useEffect with setState would cause an extra render cycle. useState initialises once and doesn't react to prop changes. useCallback is for memoising functions, not values."
        }
      ]
    },

    // ── rx-d3 ──────────────────────────────────────────────────────────────────
    {
      id: "rx-d3",
      title: "Quest Detail Page: URL Params and Navigation",
      xpReward: 125,
      filename: "QuestDetailPage.tsx",
      hookHtml: `<p>A list is only half the picture. When a user selects a quest, they need a full detail view — a separate URL they can bookmark, share, and return to. React Router's <code>useParams</code> and <code>useNavigate</code> make this seamless.</p>`,

      explanationHtml: `
<h2>Dynamic Routes and the Detail Page</h2>

<h3>Reading URL params</h3>
<pre><code>// Route defined as: &lt;Route path="quests/:id" element={&lt;QuestDetailPage /&gt;} /&gt;

import { useParams, useNavigate } from "react-router-dom";

function QuestDetailPage() {
  const { id } = useParams&lt;{ id: string }&gt;();
  const navigate = useNavigate();
  const [quest, setQuest] = useState&lt;Quest | null&gt;(null);
  const [status, setStatus] = useState&lt;"loading" | "error" | "success"&gt;("loading");

  useEffect(() => {
    let cancelled = false;
    fetchQuestById(Number(id))
      .then(data => { if (!cancelled) { setQuest(data); setStatus("success"); } })
      .catch(() => { if (!cancelled) setStatus("error"); });
    return () => { cancelled = true; };
  }, [id]); // re-fetch when the id param changes

  if (status === "loading") return &lt;div data-testid="loading"&gt;Loading…&lt;/div&gt;;
  if (status === "error" || !quest) return &lt;div data-testid="not-found"&gt;Quest not found.&lt;/div&gt;;

  return (
    &lt;div data-testid="quest-detail"&gt;
      &lt;button onClick={() =&gt; navigate(-1)}&gt;← Back&lt;/button&gt;
      &lt;h1&gt;{quest.title}&lt;/h1&gt;
      &lt;span className="badge"&gt;{quest.difficulty}&lt;/span&gt;
      &lt;p&gt;{quest.description}&lt;/p&gt;
      &lt;p&gt;Reward: +{quest.xpReward} XP&lt;/p&gt;
      {!quest.completed &amp;&amp; (
        &lt;button
          data-testid="accept-btn"
          onClick={() =&gt; handleAccept(quest.id)}
        &gt;
          Accept Quest
        &lt;/button&gt;
      )}
    &lt;/div&gt;
  );
}</code></pre>

<h3>Programmatic navigation</h3>
<pre><code>// Go back in history
navigate(-1);

// Navigate to a specific route
navigate("/quests");

// Navigate and replace (no back button history entry)
navigate("/quests", { replace: true });

// Navigate after an action completes
async function handleAccept(questId: number) {
  await markQuestAccepted(questId);
  navigate("/quests"); // return to list
}</code></pre>

<h3>id in useEffect dependencies</h3>
<p>Notice <code>[id]</code> in the dependency array. If the user navigates from <code>/quests/3</code> to <code>/quests/7</code> the component stays mounted (same route, different param) and the effect re-runs with the new id — fetching fresh data automatically.</p>

<h3>Linking from the quest list</h3>
<pre><code>import { Link } from "react-router-dom";

// In QuestCard:
&lt;Link to={"/quests/" + quest.id}&gt;View Details&lt;/Link&gt;</code></pre>
      `,

      storyBeats: [
        {
          speaker: "Vex",
          emoji: "⚗️",
          type: "dialogue",
          cssClass: "vex",
          text: "The URL is a contract. '/quests/3' should always show quest 3 — whether the user arrives by clicking a card or pasting the link into a new tab. useParams lets you honour that contract."
        },
        {
          type: "narration",
          text: "You focus the URL bar and type the address directly. The detail page loads instantly, every field populated from the id alone. The contract holds."
        }
      ],

      guidedPracticeHtml: `
<h3>🏗️ Guided: Build QuestDetailPage</h3>
<p>Build a detail page that reads the quest id from the URL, fetches that quest, and renders it.</p>
<p><strong>Requirements:</strong></p>
<ul>
  <li><code>data-testid="loading"</code> while fetching</li>
  <li><code>data-testid="not-found"</code> if the id doesn't match any quest</li>
  <li><code>data-testid="quest-detail"</code> wrapping the loaded content</li>
  <li>Shows quest title in an h1, difficulty in a span, description in a p, xpReward as "+N XP"</li>
  <li>A back button using <code>navigate(-1)</code></li>
  <li><code>data-testid="accept-btn"</code> shown only for incomplete quests</li>
</ul>
      `,
      guidedPracticeStarterCode: `import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";

// fetchQuestById(id: number): Promise<Quest | null> is available in scope

export default function QuestDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [quest, setQuest] = useState(null);
  const [status, setStatus] = useState("loading");

  useEffect(() => {
    // Fetch quest by id — include cancellation flag
    // On success: setQuest(data), setStatus("success")
    // On error: setStatus("error")
  }, [id]);

  if (status === "loading") return <div data-testid="loading">Loading…</div>;
  if (status === "error" || !quest) return <div data-testid="not-found">Quest not found.</div>;

  return (
    <div data-testid="quest-detail">
      {/* Back button, title, difficulty, description, xpReward, accept button */}
    </div>
  );
}`,
      guidedPracticeTests: [
        { action: "navigate", path: "/quests/1", then: { selector: "[data-testid='loading']", exists: true } },
        { action: "wait", ms: 400, then: { selector: "[data-testid='quest-detail']", exists: true } },
        { selector: "h1", requiredText: "The Component Model" },
        { selector: "[data-testid='accept-btn']", exists: false },
        { action: "navigate", path: "/quests/3", then: { selector: "[data-testid='accept-btn']", exists: true } },
        { action: "navigate", path: "/quests/999", then: { selector: "[data-testid='not-found']", exists: true } }
      ],

      soloPracticeHtml: `
<h3>🧩 Solo: Tags and Related Quests</h3>
<p>Extend the detail page:</p>
<ul>
  <li>Render the quest's <code>tags</code> array as span chips inside a <code>data-testid="tags"</code> container</li>
  <li>Below the detail, add a <code>data-testid="related"</code> section that shows up to 2 other quests with the same difficulty (fetched from the full list, filtered client-side)</li>
  <li>Each related quest is a Link to <code>/quests/:id</code></li>
  <li>If no related quests exist, show <code>data-testid="no-related"</code></li>
</ul>
      `,

      feynmanPrompt: "Explain why id must be in the useEffect dependency array for the detail page, as if explaining to someone who thinks 'it worked without it so why add it?'",

      questions: [
        {
          id: "rx-d3-q1",
          type: "MCQ",
          text: "A user navigates from /quests/3 to /quests/7. The QuestDetailPage component is already mounted. What happens to the useEffect that fetches by id?",
          options: [
            "Nothing — the component is already mounted, so useEffect doesn't re-run",
            "The component unmounts and remounts, triggering the effect from scratch",
            "The effect re-runs because id is in the dependency array and its value changed from '3' to '7'",
            "The effect re-runs only if useNavigate is called inside the component"
          ],
          correctIndex: 2,
          explanation: "React Router keeps the same component mounted for same-pattern routes (/quests/:id). The id param changes, which changes the dependency, which triggers the effect to re-run and fetch the new quest."
        },
        {
          id: "rx-d3-q2",
          type: "MCQ",
          text: "useParams returns id as a string even though your Quest type uses number. What should you do?",
          options: [
            "Change Quest.id to string to match useParams",
            "Cast it with Number(id) or parseInt(id) before using it in fetch calls that expect a number",
            "Nothing — JavaScript automatically coerces string '3' to number 3 in all arithmetic",
            "Use parseInt only if the string might contain decimals"
          ],
          correctIndex: 1,
          explanation: "URL params are always strings. If your data model uses number ids, convert explicitly with Number(id) or parseInt(id, 10). Relying on implicit coercion is fragile and breaks TypeScript's type checking."
        },
        {
          id: "rx-d3-q3",
          type: "MCQ",
          text: "When should you use navigate('/quests', { replace: true }) instead of navigate('/quests')?",
          options: [
            "When you want to skip the loading state on the destination page",
            "When you want the navigation to not create a history entry, so the back button skips past it — e.g. after a successful form submission or login",
            "When navigating to an external URL",
            "When navigating within the same page (hash links)"
          ],
          correctIndex: 1,
          explanation: "replace: true replaces the current history entry instead of pushing a new one. This is useful after actions like login or form submission — you don't want users hitting the back button to re-submit the form."
        },
        {
          id: "rx-d3-q4",
          type: "MCQ",
          text: "Why use <Link to='/quests/3'> instead of <a href='/quests/3'>?",
          options: [
            "Link renders faster because it uses a web worker",
            "Link prevents the full-page reload that href causes, enabling client-side navigation and preserving React state",
            "href attributes are not supported in JSX",
            "Link components automatically prefetch the destination page's data"
          ],
          correctIndex: 1,
          explanation: "<a href> causes a full browser navigation — React state is wiped, the bundle is re-parsed. <Link> intercepts the click, updates the URL via the History API, and lets React Router swap components without a reload."
        }
      ]
    },

    // ── rx-d4 ──────────────────────────────────────────────────────────────────
    {
      id: "rx-d4",
      title: "The Profile Page: Zustand Store and Persistence",
      xpReward: 150,
      filename: "ProfilePage.tsx",
      hookHtml: `<p>A guild portal without progress tracking is just a list. The Profile page needs to know which quests the user has accepted, their total XP, and their current rank — state that persists across page reloads. This is Zustand's home turf.</p>`,

      explanationHtml: `
<h2>Global State with Zustand</h2>

<h3>Defining the store</h3>
<pre><code>// src/store/userStore.ts
import { create } from "zustand";
import { persist } from "zustand/middleware";
import { Quest } from "../types";

interface UserState {
  username: string;
  totalXp: number;
  acceptedQuestIds: number[];
  acceptQuest: (quest: Quest) => void;
  reset: () => void;
}

export const useUserStore = create&lt;UserState&gt;()(
  persist(
    (set) => ({
      username: "Apprentice",
      totalXp: 0,
      acceptedQuestIds: [],

      acceptQuest: (quest) =>
        set((state) => {
          if (state.acceptedQuestIds.includes(quest.id)) return state; // idempotent
          return {
            totalXp: state.totalXp + quest.xpReward,
            acceptedQuestIds: [...state.acceptedQuestIds, quest.id],
          };
        }),

      reset: () => set({ username: "Apprentice", totalXp: 0, acceptedQuestIds: [] }),
    }),
    { name: "guild-portal-user" } // localStorage key
  )
);</code></pre>

<h3>Using the store in components</h3>
<pre><code>// Scoped subscription — component only re-renders when totalXp changes
const totalXp = useUserStore((s) => s.totalXp);
const acceptQuest = useUserStore((s) => s.acceptQuest);

// In QuestDetailPage accept button:
&lt;button onClick={() =&gt; acceptQuest(quest)}&gt;Accept Quest&lt;/button&gt;</code></pre>

<h3>Computing rank from XP</h3>
<pre><code>// src/utils/rank.ts
export function getRank(xp: number): string {
  if (xp >= 8000) return "Archmage";
  if (xp >= 4000) return "Mage";
  if (xp >= 2000) return "Adept";
  if (xp >= 800)  return "Apprentice";
  return "Novice";
}

// Usage in ProfilePage:
const totalXp = useUserStore((s) => s.totalXp);
const rank = getRank(totalXp);</code></pre>

<h3>The Profile page layout</h3>
<pre><code>function ProfilePage() {
  const { username, totalXp, acceptedQuestIds, reset } = useUserStore();
  const rank = getRank(totalXp);

  return (
    &lt;div data-testid="profile"&gt;
      &lt;h1&gt;{username}&lt;/h1&gt;
      &lt;p data-testid="rank"&gt;Rank: {rank}&lt;/p&gt;
      &lt;p data-testid="total-xp"&gt;Total XP: {totalXp}&lt;/p&gt;
      &lt;p data-testid="quest-count"&gt;Quests accepted: {acceptedQuestIds.length}&lt;/p&gt;
      &lt;button onClick={reset}&gt;Reset Progress&lt;/button&gt;
    &lt;/div&gt;
  );
}</code></pre>
      `,

      storyBeats: [
        {
          speaker: "Vex",
          emoji: "⚗️",
          type: "dialogue",
          cssClass: "vex",
          text: "Local state dies when the component unmounts. Context dies when the page reloads. Zustand with persist middleware? That outlives the session — it writes to localStorage and reads back on startup. Your XP survives a browser close."
        },
        {
          type: "narration",
          text: "You accept three quests, note the XP counter climb, then refresh the browser. The numbers hold. The scroll remembers."
        }
      ],

      guidedPracticeHtml: `
<h3>🏗️ Guided: Wire Up the User Store and Profile Page</h3>
<p>Build the Zustand store and Profile page.</p>
<p><strong>Requirements:</strong></p>
<ul>
  <li>Zustand store with <code>totalXp</code>, <code>acceptedQuestIds</code>, <code>acceptQuest(quest)</code>, <code>reset()</code></li>
  <li>Use <code>persist</code> middleware with key <code>"guild-portal-user"</code></li>
  <li>ProfilePage renders <code>data-testid="profile"</code>, <code>data-testid="rank"</code>, <code>data-testid="total-xp"</code>, <code>data-testid="quest-count"</code></li>
  <li>Rank computed by XP thresholds: Novice(0) → Apprentice(800) → Adept(2000) → Mage(4000) → Archmage(8000)</li>
  <li>Reset button calls <code>reset()</code></li>
  <li>Accepting a quest twice does not double-count XP</li>
</ul>
      `,
      guidedPracticeStarterCode: `import { create } from "zustand";
import { persist } from "zustand/middleware";

// --- Store ---
export const useUserStore = create(
  persist(
    (set) => ({
      username: "Apprentice",
      totalXp: 0,
      acceptedQuestIds: [],
      acceptQuest: (quest) => set((state) => {
        // Implement: idempotent, adds xpReward, records id
      }),
      reset: () => set({ username: "Apprentice", totalXp: 0, acceptedQuestIds: [] }),
    }),
    { name: "guild-portal-user" }
  )
);

// --- Rank helper ---
function getRank(xp) {
  // Implement rank thresholds
}

// --- Profile Page ---
export default function ProfilePage() {
  const { username, totalXp, acceptedQuestIds, reset } = useUserStore();
  const rank = getRank(totalXp);

  return (
    <div data-testid="profile">
      {/* Render username, rank, totalXp, quest count, reset button */}
    </div>
  );
}`,
      guidedPracticeTests: [
        { selector: "[data-testid='profile']", exists: true },
        { selector: "[data-testid='rank']", requiredText: "Novice" },
        { selector: "[data-testid='total-xp']", requiredText: "0" },
        { selector: "[data-testid='quest-count']", requiredText: "0" },
        {
          action: "dispatch", event: "acceptQuest", payload: { id: 1, xpReward: 75 },
          then: { selector: "[data-testid='total-xp']", requiredText: "75" }
        },
        {
          action: "dispatch", event: "acceptQuest", payload: { id: 1, xpReward: 75 },
          then: { selector: "[data-testid='total-xp']", requiredText: "75" }
        },
        {
          action: "dispatch", event: "acceptQuest", payload: { id: 2, xpReward: 800 },
          then: { selector: "[data-testid='rank']", requiredText: "Apprentice" }
        }
      ],

      soloPracticeHtml: `
<h3>🧩 Solo: XP Progress Bar</h3>
<p>Add a visual XP progress bar to the Profile page:</p>
<ul>
  <li>Show the XP needed to reach the next rank (e.g. "875 / 2000 XP to Adept")</li>
  <li>Render a <code>data-testid="xp-bar"</code> div with a child <code>data-testid="xp-fill"</code> whose inline <code>width</code> style reflects progress (0–100% within current rank band)</li>
  <li>At max rank (Archmage), show "Max rank achieved" and a full bar</li>
</ul>
      `,

      feynmanPrompt: "Explain why Zustand's persist middleware is preferable to storing user progress in a useState inside the ProfilePage component, as if talking to someone who says 'but useState already works fine for me'.",

      questions: [
        {
          id: "rx-d4-q1",
          type: "MCQ",
          text: "Why should acceptQuest be idempotent (no double-counting if called twice with the same quest)?",
          options: [
            "Zustand's set function automatically deduplicates calls with the same arguments",
            "In real apps, accept buttons may be clicked multiple times, event handlers may fire twice, or optimistic updates may re-trigger — checking before updating prevents corrupted state",
            "Zustand's persist middleware deduplicates localStorage writes",
            "React Strict Mode calls event handlers twice, so all handlers must be idempotent"
          ],
          correctIndex: 1,
          explanation: "Defensive state updates assume that the same action may fire more than once. Checking `acceptedQuestIds.includes(quest.id)` before updating makes the store reliable regardless of how many times the action is dispatched."
        },
        {
          id: "rx-d4-q2",
          type: "MCQ",
          text: "Which selector pattern minimises unnecessary re-renders when reading from a Zustand store?",
          options: [
            "const store = useUserStore(); — read the whole store",
            "const { totalXp } = useUserStore(); — destructure at the call site",
            "const totalXp = useUserStore((s) => s.totalXp); — pass a selector function",
            "const totalXp = useUserStore.getState().totalXp; — read outside React"
          ],
          correctIndex: 2,
          explanation: "Passing a selector function subscribes only to that slice. When other parts of the store change, this component doesn't re-render. Reading the whole store or destructuring from it subscribes to every change."
        },
        {
          id: "rx-d4-q3",
          type: "MCQ",
          text: "The persist middleware writes to localStorage. What are the tradeoffs of this approach?",
          options: [
            "It is synchronous and blocks the main thread, so it should be avoided in production",
            "Data survives page reloads and browser restarts without a server, but is browser-specific, has a ~5 MB limit, and is inaccessible to other browsers or devices",
            "localStorage is encrypted by the browser, so persist is secure for sensitive data",
            "persist requires a server round-trip on every store update"
          ],
          correctIndex: 1,
          explanation: "localStorage persistence is great for non-sensitive, device-local data (preferences, draft state). It doesn't sync across devices, has storage limits, and isn't appropriate for sensitive data. For multi-device sync, you'd combine Zustand with a backend API."
        },
        {
          id: "rx-d4-q4",
          type: "MCQ",
          text: "getRank is a pure function that takes XP and returns a string. Should it be defined inside or outside the React component?",
          options: [
            "Inside the component — it needs access to the component's state",
            "Outside the component — it's a pure utility with no React dependencies; keeping it outside avoids recreating it on every render",
            "Inside a useCallback — pure functions always need memoisation",
            "Inside a useMemo — it must rerun whenever XP changes"
          ],
          correctIndex: 1,
          explanation: "Pure functions that don't depend on React state or props belong outside components. Defining them inside means a new function identity on every render (harmless but wasteful). The component simply calls getRank(totalXp) inline."
        }
      ]
    },

    // ── rx-d5 ──────────────────────────────────────────────────────────────────
    {
      id: "rx-d5",
      title: "Polish and Accessibility: The Final Layer",
      xpReward: 125,
      filename: "App.tsx",
      hookHtml: `<p>A working app and a <em>good</em> app are separated by the details: keyboard navigation, screen reader labels, loading states that don't flash, and transitions that feel alive. This is what separates a portfolio piece from a throwaway exercise.</p>`,

      explanationHtml: `
<h2>Accessibility and Polish</h2>

<h3>Semantic HTML first</h3>
<pre><code>// ❌ div soup
&lt;div class="nav"&gt;
  &lt;div class="nav-item" onClick={...}&gt;Home&lt;/div&gt;
&lt;/div&gt;

// ✅ semantic — keyboard focusable, screen-reader announced correctly
&lt;nav aria-label="Main navigation"&gt;
  &lt;NavLink to="/"&gt;Home&lt;/NavLink&gt;
&lt;/nav&gt;</code></pre>

<h3>Icon-only buttons need labels</h3>
<pre><code>// ❌ screen reader announces "button" with no context
&lt;button onClick={onClose}&gt;✕&lt;/button&gt;

// ✅ aria-label provides the accessible name
&lt;button onClick={onClose} aria-label="Close dialog"&gt;✕&lt;/button&gt;</code></pre>

<h3>Loading states — avoid layout shift</h3>
<pre><code>// ❌ nothing visible while loading → jarring pop-in
if (loading) return null;

// ✅ skeleton or spinner with correct ARIA role
if (loading) return (
  &lt;div role="status" aria-live="polite" aria-label="Loading quests"&gt;
    &lt;div className="skeleton" /&gt;
    &lt;div className="skeleton" /&gt;
    &lt;div className="skeleton" /&gt;
  &lt;/div&gt;
);</code></pre>

<h3>Keyboard navigation for filter buttons</h3>
<pre><code>// Use role="group" + aria-label on the filter container
&lt;div role="group" aria-label="Filter by difficulty"&gt;
  {DIFFICULTIES.map(d =&gt; (
    &lt;button
      key={d}
      onClick={() =&gt; setFilter(d)}
      aria-pressed={filter === d}  // toggles true/false for screen readers
    &gt;
      {d}
    &lt;/button&gt;
  ))}
&lt;/div&gt;</code></pre>

<h3>Focus management after navigation</h3>
<pre><code>// After navigating to the detail page, move focus to the heading
const headingRef = useRef&lt;HTMLHeadingElement&gt;(null);

useEffect(() =&gt; {
  headingRef.current?.focus();
}, [quest?.id]); // runs when quest loads

&lt;h1 ref={headingRef} tabIndex={-1}&gt;{quest.title}&lt;/h1&gt;
// tabIndex={-1} makes it programmatically focusable without adding it to the tab order</code></pre>

<h3>CSS transitions for list re-renders</h3>
<pre><code>.quest-card {
  transition: opacity 200ms ease, transform 200ms ease;
}
/* With a key prop, React remounts on filter change →
   add an entry animation class on mount */</code></pre>

<h3>prefers-reduced-motion</h3>
<pre><code>@media (prefers-reduced-motion: reduce) {
  .quest-card { transition: none; }
}</code></pre>
      `,

      storyBeats: [
        {
          speaker: "Vex",
          emoji: "⚗️",
          type: "dialogue",
          cssClass: "vex",
          text: "A sighted user with a mouse never notices accessibility work. But a keyboard user, a screen-reader user, a user on a slow connection — they notice every gap. Polish is not decoration. It's respect for your users."
        },
        {
          type: "narration",
          text: "You tab through the entire app without touching the mouse. Every button is reachable. Every state is announced. The portal is complete — not just functional, but considerate."
        }
      ],

      guidedPracticeHtml: `
<h3>🏗️ Guided: Accessible Filter Buttons</h3>
<p>Rewrite the quest filter buttons to be fully accessible.</p>
<p><strong>Requirements:</strong></p>
<ul>
  <li>Wrap filters in a div with <code>role="group"</code> and <code>aria-label="Filter by difficulty"</code></li>
  <li>Each button has <code>aria-pressed="true"</code> when active, <code>aria-pressed="false"</code> otherwise</li>
  <li>Active button has class <code>active</code></li>
  <li>All buttons are keyboard focusable and activatable with Enter/Space (default button behaviour)</li>
  <li>The filter container has <code>data-testid="filter-group"</code></li>
</ul>
      `,
      guidedPracticeStarterCode: `import { useState } from "react";

const DIFFICULTIES = ["ALL", "FOUNDATION", "PRACTITIONER", "EXPERT"];

export default function FilterButtons() {
  const [active, setActive] = useState("ALL");

  return (
    <div>
      {/* Convert to accessible filter group */}
      {DIFFICULTIES.map(d => (
        <button key={d} onClick={() => setActive(d)}>
          {d}
        </button>
      ))}
    </div>
  );
}`,
      guidedPracticeTests: [
        { selector: "[data-testid='filter-group']", exists: true },
        { selector: "[role='group'][aria-label='Filter by difficulty']", exists: true },
        { selector: "button[aria-pressed='true']", requiredText: "ALL" },
        {
          action: "click", selector: "button:nth-child(2)",
          then: { selector: "button[aria-pressed='true']", requiredText: "FOUNDATION" }
        },
        {
          action: "click", selector: "button:nth-child(2)",
          then: { selector: "button.active", requiredText: "FOUNDATION" }
        }
      ],

      soloPracticeHtml: `
<h3>🧩 Solo: Add Skip Navigation</h3>
<p>Screen reader users often want to skip repetitive navigation and jump straight to main content.</p>
<ul>
  <li>Add a "Skip to main content" link as the very first focusable element in the page — <code>href="#main-content"</code></li>
  <li>Style it visually hidden by default (<code>position: absolute; left: -9999px</code>) but visible when focused (<code>:focus { left: 0; }</code>)</li>
  <li>Give the main content area <code>id="main-content"</code> and <code>tabIndex={-1}</code></li>
  <li>Add <code>data-testid="skip-link"</code> to the skip link</li>
</ul>
      `,

      feynmanPrompt: "Explain why aria-pressed is used on toggle buttons instead of aria-selected or aria-checked, as if a developer just asked 'aren't they all the same thing?'",

      questions: [
        {
          id: "rx-d5-q1",
          type: "MCQ",
          text: "A button renders only an X icon with no visible text. What is the correct accessibility fix?",
          options: [
            "Add title='Close' — tooltips make the button accessible",
            "Add aria-label='Close dialog' — this provides the accessible name to screen readers",
            "Wrap it in a span with text — then hide the span with CSS",
            "No fix needed — screen readers can infer intent from the icon's Unicode name"
          ],
          correctIndex: 1,
          explanation: "aria-label directly provides the accessible name for screen readers without altering the visual design. title only shows on hover and is not consistently announced by screen readers. Hiding text with CSS is a valid alternative but more complex."
        },
        {
          id: "rx-d5-q2",
          type: "MCQ",
          text: "What does tabIndex={-1} on an element mean?",
          options: [
            "The element is removed from the DOM entirely",
            "The element is skipped in the natural tab order but can be focused programmatically via .focus()",
            "The element becomes the first focusable element on the page",
            "The element receives focus after all tabIndex={0} elements"
          ],
          correctIndex: 1,
          explanation: "tabIndex={-1} removes an element from the sequential tab order (users can't tab to it) but allows programmatic focus via element.focus(). This is the standard pattern for focus management — e.g. moving focus to a heading or dialog when it opens."
        },
        {
          id: "rx-d5-q3",
          type: "MCQ",
          text: "Why should you respect prefers-reduced-motion in your CSS transitions?",
          options: [
            "Animated transitions are deprecated in modern browsers",
            "Some users experience dizziness, nausea, or seizures from motion. Disabling or reducing transitions for users who have enabled this OS-level accessibility setting is a WCAG requirement.",
            "prefers-reduced-motion is only relevant for video content, not CSS transitions",
            "CSS transitions block the main thread and cause jank on low-end devices"
          ],
          correctIndex: 1,
          explanation: "Users with vestibular disorders, epilepsy, or motion sensitivity can set prefers-reduced-motion in their OS settings. Respecting it by disabling or reducing animations is a WCAG 2.1 AA success criterion (2.3.3)."
        },
        {
          id: "rx-d5-q4",
          type: "MCQ",
          text: "Which renders a better loading experience: returning null while data loads, or rendering a skeleton?",
          options: [
            "null is better — fewer DOM nodes means faster rendering",
            "Skeleton is better — it preserves the layout shape so content appears in place rather than jumping into view, reducing cumulative layout shift (CLS) and perceived wait time",
            "Both are equivalent — the user experience is identical",
            "null is better because it avoids the risk of skeleton styles conflicting with content styles"
          ],
          correctIndex: 1,
          explanation: "Returning null causes a content pop-in — the page jumps as content appears. A skeleton (or spinner in a stable container) maintains layout, reduces CLS, and gives users a sense of progress. CLS is a Core Web Vital that affects both UX and SEO."
        }
      ]
    },

    // ── rx-d6 ──────────────────────────────────────────────────────────────────
    {
      id: "rx-d6",
      title: "The Grand Commission: Deploy the Guild Portal",
      xpReward: 350,
      filename: "index.html",
      hookHtml: `<p>Everything you have built lives only on your machine. A craftsperson's work is only real when others can see it. The Grand Commission is your final act: ship the Guild Portal to a live URL, share it, and claim your Expert badge.</p>`,

      explanationHtml: `
<h2>Deploying a Vite + React App</h2>

<h3>Option A — Vercel (recommended, ~3 minutes)</h3>
<ol>
  <li>Push your project to a GitHub repository</li>
  <li>Go to <a href="https://vercel.com" target="_blank">vercel.com</a> → "New Project" → import from GitHub</li>
  <li>Framework preset: <strong>Vite</strong> — Vercel detects it automatically</li>
  <li>Click Deploy. Done. You get a URL like <code>https://guild-portal-abc.vercel.app</code></li>
  <li>Every push to main auto-redeploys</li>
</ol>

<h3>Option B — Netlify (~3 minutes)</h3>
<ol>
  <li>Push to GitHub</li>
  <li>Go to <a href="https://netlify.com" target="_blank">netlify.com</a> → "Add new site" → import from GitHub</li>
  <li>Build command: <code>npm run build</code></li>
  <li>Publish directory: <code>dist</code></li>
  <li>Deploy. URL like <code>https://guild-portal-abc.netlify.app</code></li>
</ol>

<h3>Option C — CodeSandbox (no git, ~2 minutes)</h3>
<ol>
  <li>Go to <a href="https://codesandbox.io" target="_blank">codesandbox.io</a></li>
  <li>Create a new sandbox → "Vite + React" template</li>
  <li>Paste your component files into the editor</li>
  <li>The sandbox URL is live immediately: <code>https://abc123.csb.app</code></li>
  <li>Export → "Fork to GitHub" to make it permanent</li>
</ol>

<h3>Pre-deploy checklist</h3>
<pre><code># Run locally first
npm run build      # Vite compiles to dist/
npm run preview    # Serves dist/ — test this before deploying

# Common issues before deployment:
# 1. Environment variables — any VITE_* vars needed?
# 2. React Router with BrowserRouter — add a _redirects file (Netlify)
#    or configure rewrites (Vercel) so direct URL access works
# 3. Console errors — fix all errors before deploying</code></pre>

<h3>React Router on deployed static hosts</h3>
<p>BrowserRouter uses real URLs like <code>/quests/3</code>. Static hosts serve files by path — they'll 404 on a direct visit to <code>/quests/3</code> because there's no file there. Fix:</p>
<pre><code># Netlify — create public/_redirects:
/*    /index.html   200

# Vercel — create vercel.json at project root:
{
  "rewrites": [{ "source": "/(.*)", "destination": "/index.html" }]
}</code></pre>

<h3>What your submission must include</h3>
<ul>
  <li>✅ A live URL (Vercel / Netlify / CodeSandbox)</li>
  <li>✅ Quest browser page renders and filters work</li>
  <li>✅ Quest detail page loads via /quests/:id</li>
  <li>✅ Profile page shows XP and rank</li>
  <li>✅ Accepting a quest on the detail page increments profile XP</li>
  <li>✅ No console errors in production</li>
</ul>

<blockquote style="border-left:4px solid #7c3aed;padding-left:1rem;margin:1.5rem 0;font-style:italic;color:#6b7280;">
  "The URL is not the destination. It is the proof that the journey happened."
  <br>— Grand Archmage Solomon
</blockquote>
      `,

      storyBeats: [
        {
          type: "narration",
          text: "The Grand Archmage Solomon stands at the window of the Academy's highest tower, looking out over the city."
        },
        {
          speaker: "Solomon",
          emoji: "🧙",
          type: "dialogue",
          cssClass: "solomon",
          text: "From the first component to the final deployment — you have walked the full path. Not every apprentice completes the journey. Many learn. Fewer build. Fewer still ship."
        },
        {
          speaker: "Vex",
          emoji: "⚗️",
          type: "dialogue",
          cssClass: "vex",
          text: "The concepts will fade if you don't use them. But a URL with your name on it? That you can point to in an interview. That's evidence."
        },
        {
          speaker: "Solomon",
          emoji: "🧙",
          type: "dialogue",
          cssClass: "solomon",
          text: "Deploy the portal. Submit the URL. The Expert badge awaits — but more than the badge, you earn the right to say: I built something real."
        }
      ],

      guidedPracticeHtml: `
<h3>🏗️ Guided: Pre-Deploy Build Check</h3>
<p>Before deploying to a host, run the production build locally and fix any issues.</p>
<p><strong>Steps:</strong></p>
<ol>
  <li>Run <code>npm run build</code> in your project root — fix any TypeScript or build errors</li>
  <li>Run <code>npm run preview</code> — open the preview URL and verify all three pages work</li>
  <li>Open the browser console — confirm zero errors and zero warnings</li>
  <li>Manually navigate to <code>/quests</code> and <code>/quests/1</code> in the preview — confirm they load without 404</li>
  <li>Create the React Router fix file for your chosen host (see explanation above)</li>
</ol>
<p><strong>This is a checklist exercise — there is no automated test. Complete each step and check it off before proceeding to the deployment challenge.</strong></p>
      `,
      guidedPracticeStarterCode: `# Pre-deploy checklist (complete each step, then move to Solo)

# Step 1: Build
# npm run build

# Step 2: Preview
# npm run preview

# Step 3: Browser console — must show zero errors

# Step 4: Direct URL navigation — /quests and /quests/1 must load

# Step 5: Create router fix file
# For Netlify — create public/_redirects containing:
#   /*    /index.html   200

# For Vercel — create vercel.json containing:
# {
#   "rewrites": [{ "source": "/(.*)", "destination": "/index.html" }]
# }`,
      guidedPracticeTests: [],

      soloPracticeHtml: `
<h3>🏆 The Grand Commission: Deploy and Submit</h3>
<p>This is your final challenge. No automated grader — your work is judged by whether it runs live.</p>
<ol>
  <li><strong>Choose a host</strong>: Vercel (recommended), Netlify, or CodeSandbox</li>
  <li><strong>Deploy</strong> your Guild Portal following the steps in the explanation</li>
  <li><strong>Verify the live URL</strong>:
    <ul>
      <li>Home page loads</li>
      <li>Quest browser renders and filters correctly</li>
      <li>Clicking a quest opens its detail page at <code>/quests/:id</code></li>
      <li>Accepting a quest updates XP on the Profile page</li>
      <li>Refreshing <code>/quests/1</code> directly does not 404</li>
      <li>Browser console shows zero errors</li>
    </ul>
  </li>
  <li><strong>Submit your URL</strong> in the field below to claim the React Expert badge</li>
</ol>

<div style="background:#f8fafc;border:1px solid #e2e8f0;border-radius:8px;padding:20px;margin-top:20px;">
  <p style="font-weight:700;margin:0 0 8px;">Submit your deployment URL</p>
  <p style="color:#64748b;font-size:14px;margin:0 0 12px;">Paste the live URL of your Guild Portal (e.g. https://guild-portal-abc.vercel.app)</p>
  <input
    type="url"
    data-testid="deployment-url"
    placeholder="https://your-guild-portal.vercel.app"
    style="width:100%;padding:10px;border:1px solid #cbd5e1;border-radius:6px;font-size:14px;"
  />
  <p style="color:#94a3b8;font-size:12px;margin:12px 0 0;">This URL is your proof of completion. No submission = no badge.</p>
</div>
      `,

      feynmanPrompt: "Explain why a React app with BrowserRouter returns a 404 when you directly visit a deep URL on a static host — and why the _redirects fix works — as if explaining to someone who just deployed their first React app and is confused.",

      questions: [
        {
          id: "rx-d6-q1",
          type: "MCQ",
          text: "You deploy a Vite + React app to Netlify without a _redirects file. A user visits https://your-app.netlify.app/quests/3 directly. What happens?",
          options: [
            "The page loads correctly — Netlify serves index.html for all routes by default",
            "The page returns a 404 — Netlify looks for a file at /quests/3, finds none, and serves the default 404 page",
            "The page returns a 500 — Vite builds generate broken paths without special config",
            "The page loads but React Router state is reset"
          ],
          correctIndex: 1,
          explanation: "Static file hosts serve files literally. /quests/3 maps to a file at that path, which doesn't exist. The _redirects rule tells Netlify to serve index.html for every path, letting React Router handle routing client-side."
        },
        {
          id: "rx-d6-q2",
          type: "MCQ",
          text: "You run npm run build and see a TypeScript error about a prop being potentially undefined. You ignore it and deploy anyway. What is the risk?",
          options: [
            "No risk — TypeScript errors are compile-time warnings and don't affect runtime behaviour",
            "The undefined value may cause a runtime crash when the component tries to access a property on it, resulting in a white screen error for users",
            "Vercel will refuse to deploy an app with TypeScript errors",
            "The prop will be silently set to null at runtime, preventing a crash"
          ],
          correctIndex: 1,
          explanation: "TypeScript errors are warnings about places where your code's assumptions don't hold. Ignoring them means the edge case is still there — when the undefined value is accessed at runtime (e.g. quest.title on null), you get an uncaught error and likely a broken UI."
        },
        {
          id: "rx-d6-q3",
          type: "MCQ",
          text: "After deploying, you find a bug: accepting a quest updates XP in the current session but the XP is gone after a browser refresh. What is the most likely cause?",
          options: [
            "Zustand stores are cleared on refresh — you need to re-fetch from the server",
            "The persist middleware was configured but the storage key changed between builds, invalidating the stored data",
            "The Zustand persist middleware was not included, so state is only in memory and lost on page unload",
            "Vercel clears localStorage between deployments as a security measure"
          ],
          correctIndex: 2,
          explanation: "Without persist middleware, Zustand state lives only in memory. A page refresh is a fresh JavaScript environment — the store re-initialises to defaults. Adding persist with a stable key fixes this by reading from localStorage on startup."
        },
        {
          id: "rx-d6-q4",
          type: "MCQ",
          text: "A friend says 'I'll add accessibility later once the features are done.' What is the most important counterargument?",
          options: [
            "Accessibility is required by law in most countries, so it must be done first",
            "Accessibility built in from the start costs far less than retrofitting it — semantic HTML and ARIA labels are written once at component creation; fixing a built app requires auditing and rewriting every component",
            "Accessibility improves SEO, so it should be prioritised for business reasons",
            "Modern browsers handle accessibility automatically if you use React"
          ],
          correctIndex: 1,
          explanation: "Retrofitting accessibility is significantly more expensive than building it in from the start. Writing a semantic button instead of a div-with-onClick takes zero extra time. Auditing and rewriting a complete app later does not. The 'later' that never comes is one of the most common quality debts in frontend development."
        }
      ]
    }
  ]
};

const outPath = path.join(__dirname, "rx-d.json");
fs.writeFileSync(outPath, JSON.stringify(chunk, null, 2), "utf8");
console.log(`rx-d.json written — size: ${fs.statSync(outPath).size} chars (approx)`);
