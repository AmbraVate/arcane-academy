---
id: fe-jun-m4-02
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
lesson: client_side_routing
title: "Client-Side Routing"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains how the History API enables URL changes without page reload"
    - "Describes the role of BrowserRouter vs HashRouter"
    - "Explains why SPAs need server fallback configuration"
    - "Lists what information is stored in URL (path, query, hash)"
  keywords: [history-api, pushState, BrowserRouter, HashRouter, fallback, 404, URL, path, query, hash]
  modelAnswer: |
    Client-side routing uses the History API (pushState, replaceState) to update the URL
    without a page reload. React Router's BrowserRouter uses this approach — clean URLs
    like /about. HashRouter uses URL hash (#/about) — no server config needed but ugly URLs.
    BrowserRouter requires server-side fallback: all URLs must return index.html so React
    can handle routing client-side. Without it, refreshing /about returns a 404.
guidedSteps:
  - id: fe-jun-m4-02-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A user refreshes the browser while on /dashboard in a React SPA. The server returns 404. Why?
    inputConfig:
      options:
        - "React Router has a bug"
        - "The server doesn't know about /dashboard — it needs to return index.html for all routes"
        - "The user doesn't have permission to view dashboard"
        - "SPAs can't handle browser refreshes"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The server doesn't know about /dashboard — it needs to return index.html for all routes"]
      rejectedFeedback: "The server only has index.html. When you refresh /dashboard, the browser asks the server for /dashboard — which doesn't exist as a file. Configure the server to return index.html for all routes, and React Router handles the client-side routing."
    hint: "The server knows nothing about client-side routes."
    reflectionPrompt: "This is a common gotcha when deploying SPAs. Netlify and Vercel handle this automatically with _redirects files or vercel.json. Apache/Nginx need manual configuration. Always test refreshing a non-root route after deployment."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the URL hash (#) used for in HashRouter?"
    options:
      - "Security — hashes are encrypted"
      - "The hash (#/about) is ignored by servers — all requests go to the root, allowing client-side routing without server config"
      - "To anchor to page sections"
      - "To indicate private routes"
    correctIndex: 1
    feedback: "Browsers don't send the hash to the server — only the path before # is sent. HashRouter exploits this: /about becomes /#/about. The server always receives /, returns index.html, and React Router reads the hash to determine which component to render."

retrieval:
  recall: "Explain why a SPA needs a server fallback configuration (serving index.html for all routes)."
  explain: "Compare BrowserRouter and HashRouter — trade-offs of each."
  mistakeId:
    code: "Deploying a SPA to Apache without configuring a fallback — 404 on all non-root refreshes"
    answer: "Add .htaccess: RewriteEngine On, RewriteRule ^index\\.html$ - [L], RewriteCond %{REQUEST_FILENAME} !-f, RewriteRule . /index.html [L]"
---

# Hook

Client-side routing feels like magic — the URL changes, the content changes, but nothing reloads. Understanding the mechanism demystifies it and prevents the deployment surprises.

# Lore Introduction

*"The Academy's directory system,"* says Master Aelindra, *"handles all location requests itself. The entrance hall returns the same map regardless of which wing you claim to be heading to — the routing happens inside."*

# Core Learning

## Concept Introduction

```
History API:
history.pushState(state, title, url)  — change URL without reload
history.replaceState(...)             — replace current history entry
window.onpopstate                     — back/forward button events

React Router uses pushState internally:
navigate('/about')  → history.pushState({}, '', '/about') + re-render

Server configuration for BrowserRouter (nginx example):
location / {
  try_files $uri $uri/ /index.html;
}
```

**BrowserRouter vs HashRouter:**

| | BrowserRouter | HashRouter |
|---|---|---|
| URL | /about | /#/about |
| Server config | Required | Not required |
| URL quality | Clean | Ugly |
| Use when | Full server control | Static hosting without config |

## Common Mistakes

- **Using BrowserRouter without configuring a server fallback**: When a user refreshes on `/dashboard`, the server tries to find a file at that path and returns 404. All non-root routes must be redirected to `index.html` server-side.
- **Choosing HashRouter for a public-facing site**: Hash URLs (`/#/about`) are ugly, not indexable by all crawlers, and cannot be used with server-side rendering. Use BrowserRouter and configure the fallback properly.
- **Mixing `history.pushState` calls with React Router navigation**: Calling the History API directly bypasses React Router's state and can desynchronise the URL from the rendered component. Always use `useNavigate()` or `<Link>` within a React Router app.
- **Putting `<BrowserRouter>` inside a component that renders conditionally**: `BrowserRouter` should wrap the entire application at the top level — nesting it inside conditional components causes routing context to be lost.

## Mini Summary
- ✔ History API: pushState changes URL without page reload
- ✔ BrowserRouter: clean URLs, needs server fallback config
- ✔ HashRouter: hash prefix, no server config needed
- ✔ Deploy: configure server to serve index.html for all routes

# Solo Practice Quest

Deploy a simple React Router app to a static host. Test refreshing on a non-root route. Configure the fallback (Netlify: \_redirects file with `/* /index.html 200`). Document what you needed to change.

# Integration

**Mathematics — Address Space and Indirection:** Client-side routing adds an indirection layer: the URL space is a virtual address space mapped by JavaScript, not a file system. This is analogous to virtual memory in operating systems — the virtual address (URL) is mapped to a physical resource (component) by a translation layer (React Router).

# Lore Conclusion

*"The map is in the hall, not in the archive. The hall must return the map regardless of which wing the visitor claims to seek."*

---
