---
id: fe-jun-m5-14
school: engineering
domainId: frontend-engineering
tier: JUNIOR
moduleId: fe-jun-m5
moduleTitle: "Module 5: API Integration"
moduleGlyph: "🌐"
moduleSortOrder: 5
topicSlug: rest_consumption
topicTitle: "REST Consumption"
topicSortOrder: 5
lesson: authentication_headers
title: "Authentication Headers"
sortOrder: 2
difficulty: 4
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [fe-jun-m5-13]
integrationDomains: [psychology, sociology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what a Bearer token is and how it's sent"
    - "Shows the correct Authorization header format"
    - "Describes why storing tokens in localStorage vs memory matters"
    - "Explains what a 401 response means in the context of auth"
  keywords: [Authorization, Bearer, token, header, localStorage, memory, 401, auth]
  modelAnswer: |
    A Bearer token is a credential sent in every authenticated request via the Authorization header: 'Authorization': 'Bearer <token>'. The server validates this token to confirm the request is from an authenticated user. Token storage: localStorage persists across page refreshes but is vulnerable to XSS attacks. Memory (React state) is safer from XSS but is lost on refresh. HttpOnly cookies (server-set) are the most secure option. A 401 response means the token is missing, expired, or invalid — the user needs to log in again.
guidedSteps:
  - id: fe-jun-m5-14-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "What is the correct format for sending a Bearer token in an HTTP request header?"
    inputConfig:
      options:
        - "Token: <your-token-here>"
        - "Authorization: Bearer <your-token-here>"
        - "Auth-Token: <your-token-here>"
        - "X-Access-Token: Bearer <your-token-here>"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Authorization: Bearer <your-token-here>"]
      rejectedFeedback: "The standard HTTP authentication header is Authorization, and the Bearer scheme prefixes the token value. Format: 'Authorization': 'Bearer ' + token."
    hint: "The header name is Authorization (standard HTTP). The value starts with the scheme: Bearer."
    reflectionPrompt: "Why do you think the token format uses the word 'Bearer'? What does bearer mean?"
  - id: fe-jun-m5-14-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: "The server returns 401 Unauthorized on an authenticated request. What most likely happened?"
    inputConfig:
      options:
        - "The user doesn't have permission for this specific resource (that's 403)"
        - "The token is missing, expired, or invalid — the user needs to authenticate"
        - "The server is offline"
        - "The request body was malformed"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The token is missing, expired, or invalid — the user needs to authenticate"]
      rejectedFeedback: "401 means authentication failed — the server couldn't identify who you are. 403 means you're authenticated but lack permission. 401 → log in; 403 → you don't have access."
    hint: "Think about the difference between 'I don't know who you are' vs 'I know who you are but you can't do this'."
    reflectionPrompt: "How should your app respond to a 401 in the middle of a user session? What should happen next?"
  - id: fe-jun-m5-14-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: "Explain the security trade-off between storing an auth token in localStorage vs in memory (React state). Which is more secure against XSS and why?"
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [localStorage, memory, XSS, persist, refresh, vulnerable, secure]
      rejectedFeedback: "localStorage persists but any malicious JavaScript on your page can read it (XSS attack). Memory (React state) is inaccessible to injected scripts but is lost on page refresh. Memory is safer; localStorage is more convenient."
    hint: "XSS (Cross-Site Scripting) means malicious scripts can run on your page. What can those scripts access?"
    reflectionPrompt: "What is the recommended approach for production applications — and why do HttpOnly cookies provide the best of both worlds?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the difference between a 401 and a 403 HTTP status code?"
    options:
      - "401 is a server error; 403 is a client error"
      - "401 means unauthenticated (who are you?); 403 means unauthorised (you can't do this)"
      - "Both mean the same thing — the request was rejected"
      - "401 is for expired tokens; 403 is for invalid tokens"
    correctIndex: 1
    feedback: "401 = unauthenticated: the server doesn't know who you are. 403 = forbidden: the server knows who you are but you don't have permission. Different problems, different responses."
retrieval:
  recall: "Write the exact fetch header syntax for sending a Bearer token stored in a variable called `authToken`."
  explain: "Explain the difference between 401 and 403, and describe how a frontend app should respond to each."
  mistakeId:
    code: |
      const token = localStorage.getItem('auth_token');
      fetch('/api/profile', {
        headers: {
          'Authorization': token  // missing "Bearer " prefix
        }
      });
    answer: "The Authorization header value must include the 'Bearer ' scheme prefix: 'Authorization': 'Bearer ' + token. Without it, the server won't recognise the credential format and will return 401."
---

# Hook

You build the perfect API integration — fetching, error handling, loading states. You ship. Users log in. Then they try to access their profile — 401. Their orders — 401. Anything personal — 401. You stare at the requests in DevTools. There's no Authorization header. Every protected endpoint in any real API requires proof of identity on every request. That proof is a token, and it must be sent as a header. This lesson covers the mechanics of auth headers and the basics of token storage.

# Lore Introduction

*Not every wing of the Academy is open to all visitors. The Restricted Archives, the Masters' Council Chamber, the Enchantment Vault — these require a Guild seal. An apprentice must present their seal at the door of each restricted wing with every visit. The seal is not stored in the wall; the apprentice carries it. This is the Bearer token: a credential the user carries, presented with each request to prove their identity. The Academy does not simply trust that you are who you say — it asks for the seal, every time.*

# Core Learning

## Concept Introduction

Most APIs have endpoints that require authentication — you must prove who you are before accessing them. The standard mechanism for frontend JavaScript applications is the **Bearer token**:

1. User logs in → server responds with a token (typically a JWT)
2. Frontend stores the token
3. Every subsequent request to a protected endpoint includes the token in the `Authorization` header

**Header format**:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Token storage options**:

| Storage | XSS Risk | Persists | Notes |
|---------|----------|----------|-------|
| `localStorage` | High | Yes | Accessible to any JS on the page |
| `sessionStorage` | High | Tab only | Cleared when tab closes |
| Memory (React state) | Low | No | Lost on page refresh |
| HttpOnly cookie | Very low | Yes | Set by server, unreadable by JS |

For production applications, HttpOnly cookies are preferred. For learning and simple apps, memory is safer than localStorage.

## Why It Matters

Almost every real application has authentication. Understanding how to send tokens, where to store them, and how to handle auth errors (401 vs 403) is essential for building any private-data feature: user profiles, settings, orders, messages.

## Worked Example

```js
// After login, store token in memory (or localStorage for simplicity in examples)
let authToken = null;

async function login(email, password) {
  const res = await fetch('/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
  if (!res.ok) throw new Error('Login failed');
  const { token } = await res.json();
  authToken = token; // store in memory
}

// Helper: make authenticated requests
async function authedFetch(url, options = {}) {
  const res = await fetch(url, {
    ...options,
    headers: {
      ...options.headers,
      'Authorization': `Bearer ${authToken}`,
      'Content-Type': 'application/json'
    }
  });
  if (res.status === 401) {
    // Token expired or invalid — redirect to login
    authToken = null;
    window.location.href = '/login';
    return;
  }
  if (!res.ok) throw new Error(`HTTP ${res.status}`);
  return res.json();
}

// Using it
const profile = await authedFetch('/api/profile');
const orders = await authedFetch('/api/orders');
```

In a React context-based auth system:
```jsx
// Simple auth context storing token in memory
const AuthContext = React.createContext(null);

function AuthProvider({ children }) {
  const [token, setToken] = React.useState(null);

  async function login(email, password) {
    const res = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });
    if (!res.ok) throw new Error('Invalid credentials');
    const data = await res.json();
    setToken(data.token); // in memory — lost on refresh, but safe from XSS
  }

  function logout() {
    setToken(null);
  }

  return (
    <AuthContext.Provider value={{ token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

// Using the token in a component
function ProfilePage() {
  const { token } = React.useContext(AuthContext);
  const [profile, setProfile] = React.useState(null);

  React.useEffect(() => {
    if (!token) return;
    fetch('/api/profile', {
      headers: { 'Authorization': `Bearer ${token}` }
    })
      .then(r => r.json())
      .then(setProfile);
  }, [token]);

  if (!token) return <div className="p-4">Please log in.</div>;
  if (!profile) return <div className="p-4">Loading profile...</div>;

  return (
    <div className="p-4">
      <h1 className="text-xl font-bold">{profile.name}</h1>
      <p className="text-gray-500">{profile.email}</p>
    </div>
  );
}
```

## Common Mistakes

**Mistake 1: Missing "Bearer " prefix**
```js
// WRONG — server expects "Bearer <token>", not just the token
headers: { 'Authorization': token }

// CORRECT
headers: { 'Authorization': `Bearer ${token}` }
```

**Mistake 2: Confusing 401 and 403**
```js
if (res.status === 401) {
  // User isn't authenticated — redirect to login
  redirectToLogin();
}
if (res.status === 403) {
  // User IS authenticated but lacks permission — show "access denied"
  showAccessDenied();
}
```

**Mistake 3: Storing sensitive tokens in localStorage without considering XSS**
```js
// localStorage is convenient but readable by any JS on the page
localStorage.setItem('token', jwt); // XSS can steal this
// For production: use HttpOnly cookies or at minimum understand the risk
```

## Mini Summary

- Send tokens in the `Authorization` header: `'Bearer ' + token`
- `authToken` is typically a JWT received after a successful login
- **401** = unauthenticated (no valid token) → redirect to login
- **403** = forbidden (valid token, but insufficient permission) → show access denied
- Token storage: memory is safer than `localStorage` (no XSS risk); HttpOnly cookies are best
- Build a helper function that adds the auth header to every authenticated request

# Guided Practice Quest

Work through the guided steps to practise the correct Authorization header format and understanding the difference between 401 and 403 responses.

# Solo Practice Quest

Explain Bearer token authentication: what it is, how it's sent with fetch, and the difference between 401 and 403 responses. Describe the trade-offs between storing a token in localStorage vs in React memory. Write a helper function that adds the Authorization header to any fetch call.

# Integration

**Psychology — Trust and Verification:** Bearer token authentication implements the psychological principle that trust must be earned and regularly renewed. Unlike a one-time password at the door, tokens have expiry times — trust is re-evaluated on every request cycle. This mirrors how trust operates in human organisations: credentials are checked regularly, not assumed permanent. The UX implication is real: when a session expires, the user should be gently redirected to re-authenticate with a clear explanation, not stranded with a 401 and no guidance.

**Sociology — Identity in Institutions:** Every institution — the Academy, a bank, an employer — requires members to demonstrate their identity to access different spaces. The Bearer token is the digital Guild seal: a portable credential that establishes identity across many contexts. Sociologist Erving Goffman described how individuals "perform" identity in different contexts — the token is the mechanism by which a digital identity is performed and verified across the distributed context of HTTP requests.

# Lore Conclusion

*You carry your Guild seal. Each door you approach, you present it. The ward-keeper examines it: is it valid? Is it current? Does it grant access to this chamber? A good seal is honoured immediately. An expired seal prompts a respectful "please renew your credentials." A missing seal prompts "please identify yourself." The Academy's wards are not obstacles — they are the mechanism by which the right apprentice reaches the right knowledge. Your token is your identity in motion.*

---
