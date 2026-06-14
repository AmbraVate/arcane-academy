---
id: fe-sen-m4-03
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m4
moduleTitle: "Module 4: Security"
moduleGlyph: "🛡️"
moduleSortOrder: 4
topicSlug: authentication_flows
topicTitle: "Authentication Flows"
topicSortOrder: 3
lesson: authentication_flows
title: "Authentication Flows"
sortOrder: 1
difficulty: 4
estimatedMinutes: 35
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Correctly describes JWT structure and how the frontend uses JWTs
    - Explains the OAuth 2.0 / OIDC flow for SPAs (PKCE)
    - Describes access token vs refresh token responsibilities
    - Explains the silent refresh pattern and its purpose
    - Synthesises a token management strategy for a SPA
  keywords: [JWT, access token, refresh token, OAuth, OIDC, PKCE, Authorization Code, silent refresh, expiry, rotate, introspect, decode]
  modelAnswer: |
    JWTs (JSON Web Tokens) are signed tokens containing claims (user ID, roles, expiry). The frontend receives a JWT as an access token after login. For API calls, it's included in the Authorization header: 'Authorization: Bearer <jwt>'. The frontend should decode JWTs to read claims (user info, roles) but must never trust the frontend-decoded JWT for security decisions — only the server can validate the signature.

    For SPAs, OAuth 2.0 with PKCE (Proof Key for Code Exchange) is the current best practice. PKCE prevents authorization code interception attacks — the client creates a code_verifier, sends a hashed code_challenge, and must prove ownership of the code_verifier when exchanging the code for tokens. This is critical for public clients (SPAs) that can't keep a client secret.

    Access tokens are short-lived (15-60 minutes) — they're used for API calls. Refresh tokens are long-lived (days/weeks) — they're used to obtain new access tokens without re-authentication. The silent refresh pattern: 1-2 minutes before token expiry, the SPA silently requests a new access token using the refresh token. This maintains seamless sessions.

    Token storage: access tokens in memory (not localStorage — XSS risk). Refresh tokens in HttpOnly cookies (set by the auth server — inaccessible to JavaScript). This balances CSRF protection (no cookie-based API calls) with XSS protection (no tokens in localStorage).
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A SPA's access token expires after 60 minutes. The user is actively using the app when it expires. What should happen?"
    options:
      - "The user is immediately logged out and redirected to login"
      - "The SPA silently exchanges the refresh token for a new access token without interrupting the user"
      - "The SPA retries the failed request after 5 minutes"
      - "The user's data is cached locally for the remainder of the session"
    correctIndex: 1
    feedback: "Silent refresh: a timer fires 1-2 minutes before expiry. The SPA sends the refresh token to the auth server and receives a new access token — silently, without a full page reload or interrupting the user. If the refresh token is also expired, then the user must re-authenticate. This gives users seamless long sessions while limiting the damage of a stolen access token."
  - type: SHORT_TEXT
    prompt: "Why should SPAs use PKCE when implementing the OAuth 2.0 Authorization Code flow?"
    hint: "SPAs are 'public clients' — what secret can't they keep? What does PKCE provide instead?"
  - type: FILL_BLANK
    prompt: "Access tokens are short-lived (15-60 min) and used for ___. Refresh tokens are long-lived and used to ___."
    answer: "API calls; obtain new access tokens"
    hint: "Two tokens, two different purposes."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why is localStorage a poor choice for storing JWT access tokens?"
    options:
      - "localStorage is too slow for frequent reads"
      - "localStorage is accessible to any JavaScript on the page — XSS can steal tokens"
      - "JWTs are too large for localStorage"
      - "localStorage doesn't persist across sessions"
    correctIndex: 1
    feedback: "Any JavaScript on the page can read localStorage. An XSS vulnerability — even in a third-party dependency — can read and exfiltrate all tokens from localStorage. In-memory storage (React state, a module-level variable) is inaccessible from XSS because each page load starts fresh. HttpOnly cookies (for refresh tokens) are inaccessible to JavaScript entirely."
  - type: MULTIPLE_CHOICE
    question: "A JWT's payload contains `{ role: 'admin', exp: 1700000000 }`. The frontend decodes it and grants admin access based on the decoded role. What is wrong?"
    options:
      - "The frontend must use Base64 decoding, not atob()"
      - "The frontend cannot trust the decoded claims — only the server can verify the JWT signature"
      - "JWT payloads should not contain role information"
      - "The exp field is not sufficient to determine validity"
    correctIndex: 1
    feedback: "Anyone can decode a JWT — it's just Base64-encoded JSON. The security comes from the signature. A malicious user could modify the payload and set role='admin'. Only the server can verify the signature and confirm the claims are authentic. Frontend JWT decoding is for reading user info (name, email) for display purposes — never for making security decisions."
retrieval:
  recall: "Describe the OAuth 2.0 Authorization Code + PKCE flow for a SPA in 4-5 steps."
  explain: "Why are access tokens short-lived and refresh tokens long-lived? What risk does each lifetime control?"
  mistakeId:
    code: |
      // SPA auth implementation
      // On login success:
      localStorage.setItem('access_token', response.accessToken);
      localStorage.setItem('refresh_token', response.refreshToken);
      
      // On API call:
      headers: { Authorization: `Bearer ${localStorage.getItem('access_token')}` }
    answer: "Storing both tokens in localStorage exposes them to XSS. Any injected script can exfiltrate both tokens. The refresh token especially is dangerous — it can generate new access tokens indefinitely. Better approach: access token in memory (module variable or state, reset on page refresh — short expiry mitigates this), refresh token in HttpOnly cookie (inaccessible to JavaScript). The SPA can call a refresh endpoint; the server reads the HttpOnly cookie server-side and returns a new access token."
---

# Hook

Your users are complaining about being logged out every 15 minutes. You extend the access token lifetime to 7 days to solve it. Now security auditors flag it: a stolen access token is valid for 7 days. You've traded security for UX.

The right answer is neither short-lived tokens that frustrate users, nor long-lived tokens that linger when stolen. It's the refresh token pattern.

# Lore Introduction

*"The Guild's access token allows entry for one hour — enough for a day's work,"* the Gatekeeper explains. *"At the end of each hour, the bearer presents their long-term credential to the Guild Hall for a fresh token. The Guild Hall verifies the credential is still valid — hasn't been revoked. If valid: a fresh hour. If revoked: no entry."*

She taps the ledger. *"The short token limits damage if stolen. The long credential enables seamless renewal."*

# Core Learning

## Concept Introduction

**Authentication in SPAs:** typically uses OAuth 2.0 / OpenID Connect (OIDC) with JWTs.

**JWT structure:**
```
header.payload.signature
```
```json
// Decoded payload (anyone can decode — base64)
{
  "sub": "user-123",
  "email": "aria@arcane.academy",
  "role": "admin",
  "exp": 1700000000,
  "iat": 1699996400
}
```

**Access token vs Refresh token:**

| | Access Token | Refresh Token |
|---|---|---|
| **Lifetime** | Short (15-60 min) | Long (days-weeks) |
| **Used for** | API authentication | Getting new access tokens |
| **Storage** | Memory (in-app state) | HttpOnly cookie |
| **If stolen** | Valid for minutes | Valid for days |

**OAuth 2.0 + PKCE flow for SPAs:**
```
1. App generates: code_verifier (random), code_challenge = SHA256(verifier)
2. Redirect: /authorize?code_challenge=...&code_challenge_method=S256
3. User logs in → auth server → redirect back with authorization code
4. App exchanges code + code_verifier for tokens (server verifies hash)
5. App stores access token in memory, refresh token in HttpOnly cookie
```

**Silent refresh:**
```tsx
// Set a timer before token expiry
useEffect(() => {
  const expiresIn = tokenExpiresAt - Date.now() - 60_000; // 1 min before
  const timer = setTimeout(refreshAccessToken, expiresIn);
  return () => clearTimeout(timer);
}, [tokenExpiresAt]);
```

## Why It Matters

Authentication is the front door of every application, and the frontend owns more of its security than most engineers realise:

- Where tokens live (memory, cookie, localStorage) decides what an XSS payload can steal; flow design decides what a phishing page can replay
- Broken auth UX has direct business cost — login friction and silent session expiry are leading causes of abandonment and support tickets
- Modern flows (OAuth 2.0 with PKCE, OIDC) exist because earlier patterns leaked tokens through redirects, referrers, and browser history; using them correctly means understanding what each step defends against
- Refresh-token rotation, logout-everywhere, and session timeout are *frontend-visible* behaviours users and auditors both judge

Auth is also unforgiving: a payments page that's slow annoys people; a login flow that's wrong leaks accounts. Few areas of frontend work carry this blast radius, which is exactly why senior engineers are expected to reason about it precisely rather than copy a tutorial.

## Common Mistakes

- **Storing tokens in localStorage.** Accessible to any JS on the page, including XSS injections.
- **Long-lived access tokens.** Stolen access tokens can't be revoked (JWTs are stateless). Keep them short.
- **Not implementing silent refresh.** Users get logged out at token expiry — frustrating.
- **Trusting JWT claims on the frontend for security.** Always validate on the server.

## Mental Model

Think of authentication as a hotel, not a fortress with one gate. Logging in is check-in at reception: you prove identity once (credentials, MFA) and receive a key card (the token). The card — not your passport — opens doors thereafter; it is scoped (your floor, your room), it expires, and the hotel can void it instantly (revocation) without confiscating your passport. A refresh token is the reception desk's record that lets them quietly issue you tomorrow's card without re-checking your passport. Now every design question maps cleanly: where do you keep the card so a pickpocket in the lobby (XSS) can't lift it? What happens when a card expires mid-stay (silent refresh vs forced re-login)? And losing a card must never mean losing the passport — which is why tokens, not credentials, flow through the app.

## Mini Summary

- ✔ Use OAuth 2.0 + PKCE for SPA authentication — no client secrets required
- ✔ Access tokens: short-lived, stored in memory; Refresh tokens: long-lived, HttpOnly cookie
- ✔ Silent refresh maintains seamless sessions without forcing re-login
- ✔ Never use localStorage for tokens — XSS risk
- ✔ Never make security decisions based on frontend-decoded JWT claims

# Guided Practice Quest

Work through the guided steps to understand the access/refresh token split and why PKCE is required for SPAs.

# Solo Practice Quest

Design the complete authentication flow for a React SPA that connects to a REST API. Cover: (1) login flow with PKCE, (2) token storage strategy, (3) silent refresh implementation, (4) handling expired sessions, (5) logout (including token revocation). Explain the security rationale for each choice.

# Integration

**Mathematics — Least Privilege and Token Scope**

Security engineering applies the principle of least privilege: each credential should have the minimum access required. Access tokens embody this mathematically: short lifetime = limited window of opportunity if stolen. PKCE is mathematical proof of client identity — the auth server can verify the client is the same entity that initiated the flow, using the relationship between code_verifier and code_challenge (hash preimage). The security of PKCE rests on the cryptographic assumption that SHA-256 is preimage-resistant — an attacker who intercepts the code_challenge cannot derive the code_verifier and therefore cannot complete the token exchange. This is applied cryptographic security: the math prevents the attack even if the authorization code is intercepted.

# Lore Conclusion

*"The flow is sound,"* the Gatekeeper says, reviewing the PKCE implementation. *"The short token limits risk. The refresh token enables continuity. The PKCE challenge prevents interception. Security and experience, balanced."*

---
