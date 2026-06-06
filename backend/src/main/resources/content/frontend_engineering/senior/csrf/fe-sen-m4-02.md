---
id: fe-sen-m4-02
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m4
moduleTitle: "Module 4: Security"
moduleGlyph: "🛡️"
moduleSortOrder: 4
topicSlug: csrf
topicTitle: "CSRF"
topicSortOrder: 2
lesson: csrf
title: "Cross-Site Request Forgery (CSRF)"
sortOrder: 1
difficulty: 4
estimatedMinutes: 30
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
    - Correctly explains how a CSRF attack works
    - Explains why session cookies are automatically sent with cross-origin requests
    - Describes CSRF tokens and how they prevent the attack
    - Explains how SameSite cookies reduce CSRF risk
    - Explains why modern SPAs using JWT Bearer tokens in headers are less vulnerable to CSRF
  keywords: [CSRF, cookie, cross-origin, SameSite, token, JWT, Bearer, Authorization header, same-site, forged request]
  modelAnswer: |
    CSRF exploits the fact that browsers automatically send cookies with every request to a domain — even cross-origin requests. An attacker creates a malicious page with a form that POSTs to your API. When a victim visits the malicious page, their browser submits the form including the victim's session cookie. Your API receives a valid-looking authenticated request that the victim didn't intend.

    CSRF tokens prevent this: the server sends a unique, unpredictable token in the page response. The form must include this token in the request. A cross-origin forged request can't read the CSRF token from your page (same-origin policy) — so it can't include it — so the server rejects it.

    SameSite cookie attribute reduces CSRF: SameSite=Strict never sends the cookie in cross-origin requests; SameSite=Lax only sends it on top-level navigation GETs (safe). Modern browsers default to Lax, providing significant protection.

    Modern SPAs using JWT tokens in Authorization headers are less vulnerable: the attacker's forged form can include cookies automatically, but cannot set custom headers (like Authorization: Bearer <token>) in a cross-origin form submission. This is why Bearer token authentication is more CSRF-resistant than cookie-based authentication.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "An attacker's page contains `<form action='https://bank.com/transfer' method='POST'><input name='amount' value='1000'/><input name='to' value='attacker'/></form>` with auto-submit JavaScript. The victim is logged into bank.com. What happens?"
    options:
      - "The browser blocks it — cross-origin form submissions are not allowed"
      - "The form submits with the victim's bank.com session cookie, potentially transferring funds"
      - "The bank's CSP blocks the cross-origin form"
      - "The victim must click submit — auto-submit is blocked by browsers"
    correctIndex: 1
    feedback: "Browsers allow cross-origin form submissions (CORS restrictions apply to XHR/fetch, not forms). The browser sends the victim's bank.com cookie automatically. If bank.com doesn't validate a CSRF token, the transfer proceeds. This is a classic CSRF attack — the bank's server can't distinguish this forged request from a legitimate one."
  - type: SHORT_TEXT
    prompt: "Why is a modern SPA that uses `Authorization: Bearer <jwt>` in request headers less vulnerable to CSRF than one that uses session cookies?"
    hint: "What can an attacker's cross-origin form submission include? What can it not include?"
  - type: FILL_BLANK
    prompt: "SameSite=___ cookies are never sent in cross-origin requests. SameSite=___ cookies are sent only on top-level GET navigation."
    answer: "Strict; Lax"
    hint: "Two SameSite values provide different levels of CSRF protection."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Your API uses session cookies for authentication. Which header should your server validate on state-changing requests to prevent CSRF?"
    options:
      - "Content-Type: application/json"
      - "X-CSRF-Token with a server-issued, per-session token"
      - "Authorization: Basic"
      - "X-Requested-With: XMLHttpRequest"
    correctIndex: 1
    feedback: "A CSRF token is an unpredictable, per-session value that the server embeds in the page or provides via a dedicated endpoint. State-changing requests must include this token in the body or header. Cross-origin forged forms cannot read the CSRF token (same-origin policy prevents reading the page), so they can't include it, and the server rejects them."
  - type: MULTIPLE_CHOICE
    question: "Which SameSite cookie setting provides the strongest CSRF protection?"
    options:
      - "SameSite=None — sends cookies everywhere"
      - "SameSite=Lax — blocks most cross-site requests"
      - "SameSite=Strict — never sends cookies on cross-site requests"
      - "No SameSite attribute — browser decides"
    correctIndex: 2
    feedback: "SameSite=Strict never sends the cookie in any cross-origin request. The downside: if a user clicks a link from an email to your app, they arrive logged out (the cookie wasn't sent with the navigation). SameSite=Lax is the modern default — it allows the cookie for top-level GET navigation (link clicks) but blocks POST and AJAX cross-origin requests, covering most CSRF vectors."
retrieval:
  recall: "Explain in simple terms why browsers automatically send cookies with cross-origin requests and how this enables CSRF."
  explain: "Why does using JWT tokens in Authorization headers (rather than cookies) reduce CSRF risk?"
  mistakeId:
    code: |
      // API that relies only on the Origin header for CSRF protection
      if (req.headers.origin === 'https://myapp.com') {
        // Process request
      }
    answer: "Origin header validation is not reliable CSRF protection. The Origin header is sent by browsers but: (1) some browsers don't send it for same-origin requests; (2) the Origin header is set by the browser, not the user — but CSRF exploits the browser sending the header with a legitimate origin from an attacker page... wait, actually cross-origin form submissions DO send an Origin header from the attacker's domain. The real problem: Origin can be missing (null) in some contexts. Use CSRF tokens for reliable protection, or SameSite=Strict cookies. The Origin check is a useful additional signal but not a primary defence."
---

# Hook

You get a support ticket: a user accidentally transferred £5,000 to an unknown account. They insist they didn't do it. You check the logs — the transfer came from their IP address with their session cookie, at a time they were browsing another website.

They were the victim of a CSRF attack. Your API trusted the cookie. The attacker's page sent the request. Your server couldn't tell the difference.

# Lore Introduction

*"The seal of the Guild Master can open any vault,"* the Security Advisor explains. *"But the seal travels with the Master — wherever they go, it goes. A thief who lures the Master near the vault, then acts in their name while they're distracted, can open the vault without the Master's intent."*

She places a second seal on the table — one only the Master can produce on demand. *"The CSRF token is the second seal. It must be presented deliberately. It cannot be produced by the thief."*

# Core Learning

## Concept Introduction

**CSRF** exploits browsers' automatic cookie-sending behaviour:

```
1. User logs into bank.com — gets session cookie
2. User visits attacker.com
3. attacker.com's page auto-submits a form to bank.com/transfer
4. Browser automatically includes bank.com's session cookie
5. bank.com sees a valid session — processes the transfer
```

**Defences:**

**1. CSRF Tokens (most reliable for cookie-based auth):**
```python
# Server: include CSRF token in every form/page
<input type="hidden" name="csrf_token" value="{{ csrf_token }}">

# Server: validate on every state-changing request
if request.form['csrf_token'] != session['csrf_token']:
    abort(403)
```

**2. SameSite Cookies:**
```http
Set-Cookie: sessionId=abc; SameSite=Strict; Secure; HttpOnly
```
- `SameSite=Strict`: never sent cross-origin
- `SameSite=Lax` (browser default): sent only on top-level GET navigation

**3. Authorization Header (Bearer tokens):**
```http
Authorization: Bearer <jwt-token>
```
Cross-origin requests cannot set custom headers (browser restricts this). A CSRF form can include cookies — it cannot include a custom Authorization header. SPAs using Bearer tokens in headers are inherently more CSRF-resistant.

**4. Double Submit Cookie:**
```js
// Send CSRF token in both cookie AND request body/header
// Cross-origin requests can send the cookie but can't read it to duplicate it
```

## Common Mistakes

- **Relying only on checking the Referer/Origin header.** These can be absent or spoofed in some contexts.
- **Not protecting state-changing GET requests.** CSRF applies to any request that changes state, including GET requests that shouldn't change state but do.
- **Forgetting SameSite on new cookies.** Modern browsers default to Lax, but explicitly set it for clarity.

## Mini Summary

- ✔ CSRF exploits browsers automatically sending cookies with cross-origin requests
- ✔ CSRF tokens protect cookie-based APIs — the token can't be forged cross-origin
- ✔ SameSite=Strict/Lax cookies prevent most CSRF attacks
- ✔ Bearer tokens in Authorization headers are inherently more CSRF-resistant than cookies
- ✔ Defence in depth: use SameSite + CSRF tokens + CORS configuration together

# Guided Practice Quest

Work through the guided steps to understand the mechanism of CSRF and why different authentication methods have different CSRF exposure.

# Solo Practice Quest

Your team is building a new API. The security discussion is: use session cookies (with CSRF tokens) or JWT Bearer tokens in Authorization headers. Analyse both approaches from a CSRF perspective: what protections does each require? What are the operational trade-offs? What would you recommend for a SPA?

# Integration

**Mathematics — Secrets and Unforgeability**

A CSRF token is a cryptographic primitive: it's an unpredictable value that the server can verify but an attacker cannot forge. The security comes from entropy — a 256-bit random token has 2^256 possible values, making brute force infeasible. The same-origin policy completes the protection: an attacker's cross-origin page cannot read the CSRF token from your page, so they can't include it in a forged request. This is a two-part protection: unforgeability (cryptographic) + inaccessibility (browser policy). Either alone is insufficient — a predictable token would be forgeable even if inaccessible; a random token that's accessible would be forgeable through reading. Together, they make CSRF attack impossible without a vulnerability in the token generation or the same-origin policy.

# Lore Conclusion

*"The second seal is in place,"* the Security Advisor says. *"The thief can lure the Master near the vault, but they cannot produce the second seal on demand. The vault remains closed."*

---
