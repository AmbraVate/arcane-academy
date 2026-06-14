---
id: fe-jun-m4-01
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m4
moduleTitle: "Module 4: Routing and Navigation"
moduleGlyph: "🗺️"
moduleSortOrder: 4
topicSlug: spa_architecture
topicTitle: "SPA Architecture"
topicSortOrder: 1
lesson: what_is_a_spa
title: "What is a SPA?"
sortOrder: 1
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what a SPA is vs a Multi-Page Application (MPA)"
    - "Describes the benefits of SPAs (faster navigation, no page reload)"
    - "Describes the trade-offs (initial load, SEO, complexity)"
    - "Explains client-side routing vs server-side routing"
  keywords: [SPA, MPA, client-side, server-side, routing, page-reload, bundle, SEO, hydration]
  modelAnswer: |
    A Single Page Application loads one HTML page and dynamically updates the DOM as users
    navigate, rather than fetching a new HTML page from the server. Benefits: faster
    navigation (no full reload), better interactivity. Trade-offs: larger initial bundle,
    SEO challenges (crawlers may not execute JS), blank screen while JS loads.
    Client-side routing intercepts navigation and renders new components without server
    requests. Server-side routing fetches new HTML for each URL.
guidedSteps:
  - id: fe-jun-m4-01-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What happens when a user clicks a link in a SPA vs a traditional MPA?
    inputConfig:
      options:
        - "SPA: full page reload. MPA: JavaScript update only"
        - "SPA: JavaScript intercepts the click, renders new content. MPA: browser requests new HTML from server"
        - "They work identically — routing is routing"
        - "SPA: server responds with JSON. MPA: server responds with HTML"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["SPA: JavaScript intercepts the click, renders new content. MPA: browser requests new HTML from server"]
      rejectedFeedback: "SPA routing is client-side: JS catches the navigation event, renders the new 'page' (actually a new component), and updates the URL using the History API. No server round-trip. MPA routing requests a new HTML document from the server for each page."
    hint: "In a SPA, does the browser ever leave the original HTML page?"
    reflectionPrompt: "SPAs feel faster because the shell (nav, layout) persists — only the content area updates. MPAs reload everything including the shell. The trade-off: SPAs need JS to work at all, while MPAs work without JavaScript."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which is the main SEO challenge for SPAs?"
    options:
      - "SPAs cannot use meta tags"
      - "Search engine crawlers may not execute JavaScript, missing dynamically rendered content"
      - "SPAs cannot be indexed at all"
      - "URL routes in SPAs are invisible to search engines"
    correctIndex: 1
    feedback: "Traditional search crawlers fetch HTML and parse it — they may not execute the JS that renders SPA content. Solutions: Server-Side Rendering (SSR) or Static Site Generation (SSG) with Next.js, or ensuring dynamic rendering for crawlers. Modern Google does execute JS but may delay indexing."

retrieval:
  recall: "List three advantages and three trade-offs of Single Page Applications."
  explain: "Explain what 'client-side routing' means and how it differs from server-side routing."
  mistakeId:
    code: "Building a content-heavy blog as a SPA with no SSR — poor SEO"
    answer: "Use SSR (Next.js) or SSG for content that needs to be discoverable by search engines. SPAs are best for authenticated app experiences (dashboards, editors) where SEO matters less."
---

# Hook

Single Page Applications load once and update dynamically — no page reloads, smooth transitions, app-like feel. They are also a set of architectural decisions with real trade-offs. Understanding both sides makes you a better architect.

# Lore Introduction

*"The Academy's reading room,"* says Master Aelindra, *"was redesigned: instead of fetching new scrolls from the archive for every question, one scribe holds all resources and updates the display in place. Faster? Yes. But the scribe must arrive before any reading can begin."*

# Core Learning

## Concept Introduction

**SPA vs MPA:**

| | SPA | MPA |
|---|---|---|
| Navigation | JS re-renders components | Browser requests new HTML |
| Page reload | None after first load | On every navigation |
| Initial load | Larger bundle, slower start | Smaller, faster first page |
| SEO | Harder (requires SSR/SSG) | Easier (HTML on server) |
| Best for | Apps (dashboard, editor) | Content sites (blog, docs) |

**The History API (how SPAs update the URL):**
```javascript
// Push a new URL without reloading
history.pushState({}, '', '/about');

// React Router abstracts this:
navigate('/about');  // from useNavigate hook
```

## Common Mistakes

- **Building a content-heavy public site as a SPA**: Blogs, documentation, and marketing sites need SEO. A pure SPA returns a near-empty HTML shell to search crawlers — use SSG (Astro, Next.js static) or SSR instead.
- **Thinking SPAs are always faster**: SPAs have a slower initial load because the entire JavaScript bundle must download and execute before the first page renders. They are faster for subsequent navigation, not for first load.
- **Using `<a href>` for internal navigation in a React Router app**: Regular anchor tags cause a full page reload, losing React state and negating the SPA advantage. Use `<Link>` for all internal routes.
- **Deploying a SPA without configuring a server fallback**: Without `try_files $uri /index.html` (nginx) or equivalent, refreshing any non-root URL returns a 404 from the server.

## Mini Summary
- ✔ SPA: one HTML page, JS renders all "pages" as components
- ✔ Faster navigation; larger initial bundle
- ✔ SEO requires SSR or SSG for crawlable content
- ✔ React Router handles client-side routing

# Solo Practice Quest

Research and compare: Next.js (SSR/SSG) vs Create React App (pure SPA). List three scenarios where you'd choose each. Write a 200-word comparison.

# Integration

**Sciences — System Architecture Trade-offs:** SPAs and MPAs are different points on the same design space. Neither is universally better. Good architecture requires understanding the axes of the trade-off: initial load vs navigation speed, SEO vs interactivity, simplicity vs capability. Engineering is choosing the right trade-off for the specific problem.

# Lore Conclusion

*"The SPA trades load time for speed. Know what you are trading and choose deliberately."*

---
