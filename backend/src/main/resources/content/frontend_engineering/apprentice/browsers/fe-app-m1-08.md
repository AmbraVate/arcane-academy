---
id: fe-app-m1-08
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m1
moduleTitle: "Module 1: Understanding the Web"
moduleGlyph: "🌐"
moduleSortOrder: 1
topicSlug: browsers
topicTitle: "Browsers"
topicSortOrder: 2
lesson: browser_storage
title: "Browser Storage"
sortOrder: 3
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m1-06]
integrationDomains: [psychology, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Names the four main browser storage types"
    - "Explains the difference between localStorage and sessionStorage"
    - "Describes what cookies are and when to use them"
    - "Mentions privacy and security considerations"
    - "Gives appropriate use cases for each storage type"
  keywords: [localstorage, sessionstorage, cookie, indexeddb, storage, persist, session, expiry, secure, httponly]
  modelAnswer: |
    Browsers provide four main storage mechanisms: cookies (small, sent with every HTTP request, used
    for auth/sessions), localStorage (persistent key-value store, stays until cleared, not sent with requests),
    sessionStorage (same as localStorage but cleared when the tab closes), and IndexedDB (a full client-side
    database for large structured data). Choosing the right storage type depends on persistence needs, data
    size, and security requirements. Sensitive data should be in httpOnly cookies; large structured data
    belongs in IndexedDB.
guidedSteps:
  - id: fe-app-m1-08-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A user logs in to a website. The server creates a session token to identify them. Where should this token be stored?
    inputConfig:
      options:
        - "localStorage — for easy JavaScript access"
        - "A cookie with HttpOnly and Secure flags"
        - "sessionStorage — so it clears when the tab closes"
        - "A JavaScript variable in memory"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A cookie with HttpOnly and Secure flags"]
      rejectedFeedback: "Session tokens should be in **HttpOnly cookies** — JavaScript cannot access them, protecting against XSS attacks. The `Secure` flag ensures they're only sent over HTTPS. localStorage is accessible to any JavaScript on the page, making it vulnerable to XSS."
    hint: "Which storage type is inaccessible to JavaScript — protecting against XSS attacks?"
    reflectionPrompt: "Storing auth tokens in localStorage is a common mistake with serious security implications. HttpOnly cookies are the secure choice — they're sent with every request automatically but are inaccessible to JavaScript. Security decisions are frontend engineering decisions."

  - id: fe-app-m1-08-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence:

      "___ persists data across browser sessions (stays when you close and reopen the browser), while sessionStorage clears its data when the tab is closed."
    inputConfig:
      placeholder: "localStorage"
    markingRule:
      matchMode: CONTAINS
      accepted: [localStorage, "local storage", localstorage]
      rejectedFeedback: "**localStorage** persists until explicitly cleared — it survives tab closes, browser restarts, and computer restarts. **sessionStorage** clears when the tab closes. Use localStorage for user preferences; use sessionStorage for temporary form state."
    hint: "One of the two Web Storage APIs that persists data permanently."
    reflectionPrompt: "The persistence difference matters in practice. User theme preferences should be in localStorage (persist forever). Form draft data might be in sessionStorage (discard when done). Auth tokens should be in secure cookies (never in either Web Storage API)."

  - id: fe-app-m1-08-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A colleague wants to store a user's shopping cart contents in the browser so they persist across sessions. They suggest using cookies. Evaluate this idea — is it a good choice? What would you recommend instead?
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [localstorage, indexeddb, cookie, size, kb, request, large, better]
      rejectedFeedback: "Cookies have a 4KB size limit and are sent with EVERY HTTP request — wasteful for cart data. localStorage (5MB, not sent with requests) or IndexedDB (much larger, structured) are better choices. Cookies are for small auth/session data, not large application state."
    hint: "Think about the size of cookie data and what happens to cookies on every HTTP request."
    reflectionPrompt: "Choosing the right storage type is an architectural decision. Using cookies for large data bloats every HTTP request. Using localStorage for auth tokens creates security holes. Matching the tool to the requirement is craftsmanship."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the key difference between localStorage and sessionStorage?"
    options:
      - "localStorage is faster; sessionStorage is more secure"
      - "localStorage persists across sessions; sessionStorage clears when the tab closes"
      - "sessionStorage can store more data than localStorage"
      - "localStorage is only available over HTTPS"
    correctIndex: 1
    feedback: "The persistence difference is the defining one. Both are key-value stores with ~5MB limits. Both are accessible to JavaScript. The only difference is when data is cleared: localStorage never (until explicitly cleared); sessionStorage on tab close."
  - type: MULTIPLE_CHOICE
    question: "What makes cookies different from localStorage and sessionStorage?"
    options:
      - "Cookies can store more data"
      - "Cookies are automatically sent with every HTTP request to the matching domain"
      - "Cookies are more secure by default"
      - "Cookies are only stored in memory, not on disk"
    correctIndex: 1
    feedback: "Cookies are automatically attached to HTTP requests. This makes them useful for authentication (the server receives the session cookie with every request) but costly for large data (bloats every request). localStorage and sessionStorage are not sent with requests."

retrieval:
  recall: "Name the four browser storage types and give one appropriate use case for each."
  explain: "Why is storing an authentication token in localStorage a security risk?"
  mistakeId:
    code: "localStorage is the right place to store all client-side data because it's the easiest to use"
    answer: "localStorage is convenient but not appropriate for all data. Auth tokens in localStorage are vulnerable to XSS attacks (any JS on the page can read them). Large structured data is better in IndexedDB. Session-only data belongs in sessionStorage. The right storage type depends on the security requirements, data size, and persistence needs."
---

# Hook

You add items to a shopping cart, close your browser, reopen it, and your cart is still there.

You log in to a website, and somehow the server knows who you are on every subsequent request — even though HTTP is stateless.

You set a dark theme preference, and it's still dark the next time you visit.

None of this happens by magic. The browser is storing data on your behalf. Understanding what it stores, where, and why is essential to building applications that remember.

> Before reading on: where do you think a website stores your shopping cart between sessions?

# Lore Introduction

The Academy has a system of memory vaults — some last only as long as a session lasts, some persist through time, some are sent back automatically whenever a message is dispatched.

*"Each vault has a different purpose,"* says Master Aelindra. *"Matching the right vault to the right information — that is a skill. Store the wrong thing in the wrong vault, and you create either a privacy problem or a performance problem."*

She points to a small envelope in the corner.

*"And see this? It is attached to every message sent to the realm's towers. Small. Efficient. For identity only. Never for luggage."*

# Core Learning

## Concept Introduction

Browsers provide four main storage mechanisms:

| Type | Capacity | Persists? | Sent with requests? | Accessible to JS? |
|---|---|---|---|---|
| **Cookie** | ~4KB | Configurable | Yes (same domain) | Configurable |
| **localStorage** | ~5MB | Yes (until cleared) | No | Yes |
| **sessionStorage** | ~5MB | No (clears on tab close) | No | Yes |
| **IndexedDB** | Hundreds of MB | Yes | No | Yes |

### Cookies

Cookies are small key-value pairs set by the server (via `Set-Cookie` header) or JavaScript. They are automatically sent with every HTTP request to the matching domain.

Important cookie attributes:
- **`HttpOnly`** — cannot be accessed by JavaScript (protects against XSS)
- **`Secure`** — only sent over HTTPS
- **`SameSite`** — controls cross-site sending (protects against CSRF)
- **`Expires`/`Max-Age`** — controls how long the cookie lives

Use cookies for: session tokens, authentication, small preferences the server needs.

### Web Storage (localStorage and sessionStorage)

The Web Storage API provides two key-value stores:

```javascript
// localStorage — persists forever
localStorage.setItem('theme', 'dark');
localStorage.getItem('theme'); // 'dark'

// sessionStorage — clears when tab closes
sessionStorage.setItem('draftMessage', 'Hello...');
```

Use localStorage for: user preferences, cached data that should survive sessions.
Use sessionStorage for: temporary form state, wizard progress within a session.

### IndexedDB

A full client-side database for large, structured data. Supports complex queries, indexes, and transactions. Used by progressive web apps for offline functionality.

Use IndexedDB for: offline-capable apps, large datasets, structured data that needs querying.

## Why It Matters

Storage decisions affect:
- **Security** — auth tokens in localStorage are vulnerable to XSS
- **Performance** — large cookies bloat every HTTP request
- **Privacy** — data persisted without user consent may violate regulations (GDPR, CCPA)
- **User experience** — losing state unexpectedly frustrates users

## Worked Examples

**Example 1 — Reading/writing user theme preference:**
```javascript
// Set preference
localStorage.setItem('colorTheme', 'dark');

// Read on page load
const theme = localStorage.getItem('colorTheme') || 'light';
document.body.classList.add(theme);
```

**Example 2 — Checking for a server-set session:**
Server sets: `Set-Cookie: sessionId=abc123; HttpOnly; Secure; SameSite=Strict`
Browser attaches automatically on every request to that domain.
Frontend code doesn't read the value (HttpOnly) — it just knows: if the server returns user data, the session is valid.

## Common Mistakes

- **Storing auth tokens in localStorage.** XSS can steal them. Use HttpOnly cookies.
- **Using cookies for large data.** Cookies are sent with every request — large cookies slow everything.
- **Not setting cookie expiry.** Session cookies (no expiry) clear on browser close — often not what you want for "remember me" features.
- **Ignoring GDPR/privacy regulations.** Storing persistent data without user consent may be illegal.

## Mental Model

Think of browser storage like **different kinds of physical storage in an office**:
- **Cookies** = the badge clipped to your lanyard — small, always on you, shown at every checkpoint
- **localStorage** = your locked desk drawer — stays there until you empty it yourself
- **sessionStorage** = your in-tray — cleared at end of day when you leave
- **IndexedDB** = the filing room — large, structured, searchable, for big datasets

## Mini Summary

- Four storage types: cookies, localStorage, sessionStorage, IndexedDB
- Cookies are sent with every HTTP request; the others are not
- localStorage persists until cleared; sessionStorage clears on tab close
- Auth tokens belong in HttpOnly, Secure cookies — not localStorage
- Storage decisions have security, performance, and privacy implications

# Guided Practice Quest

**The Memory Vaults**

The Academy's knowledge vaults need classification. Apprentices must decide which storage type is appropriate for each piece of information the Academy needs to remember.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You are building a web application. For each requirement below, specify which storage type you would use and justify your choice in one or two sentences:

1. Keeping a user logged in across browser restarts
2. Storing a user's preferred language
3. Saving a draft blog post the user is writing (discard when they leave)
4. Caching 50MB of product data for an offline-capable shopping app
5. Remembering that a user dismissed a promotional banner

# Integration

**Connecting to Psychology — Memory Systems and Storage Metaphors**

Human memory researchers distinguish between different memory systems: **working memory** (short-term, limited capacity, cleared quickly), **episodic memory** (events tied to time), and **semantic memory** (persistent facts not tied to specific events).

Browser storage mirrors this structure. sessionStorage resembles working memory — current context, cleared when no longer needed. localStorage resembles semantic memory — persistent facts about the user. Cookies with short expiry resemble episodic memory — tied to a specific session or time period.

This parallel is not coincidental. Software systems that store and retrieve information face the same fundamental tradeoffs as biological memory systems: what should persist? What should be available quickly? What is too sensitive to retain long-term?

How might understanding human memory systems help a frontend engineer make better decisions about what to store and when?

# Lore Conclusion

The vaults are organised. The right information is in the right place. The wrong vaults are sealed.

*"Memory is powerful,"* says Master Aelindra. *"It enables continuity. But misused, it creates vulnerability. Every piece of data you store is a promise to the user — and a responsibility."*

The rune settles into the path.

*"Now we enter the browser's workshop — the developer tools. These are your instruments for understanding what is happening inside the pages you build."*
