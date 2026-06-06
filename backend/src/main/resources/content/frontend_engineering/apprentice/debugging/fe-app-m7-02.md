---
id: fe-app-m7-02
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m7
moduleTitle: "Module 7: Frontend Engineering Habits"
moduleGlyph: "🛠️"
moduleSortOrder: 7
topicSlug: debugging
topicTitle: "Debugging"
topicSortOrder: 1
lesson: using_devtools
title: "Using DevTools"
sortOrder: 2
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Names the key DevTools panels and their purpose"
    - "Uses the Elements panel to inspect and live-edit CSS"
    - "Uses the Console to log values and test expressions"
    - "Uses the Network tab to see HTTP requests and their responses"
    - "Uses the Sources panel to set breakpoints and step through code"
  keywords: [DevTools, Elements, Console, Network, Sources, inspect, breakpoint, step, live-edit, request]
  modelAnswer: |
    Browser DevTools has four key panels for frontend debugging. Elements: inspect DOM
    and live-edit CSS in real time. Console: evaluate expressions, log values, see errors.
    Network: monitor all HTTP requests (status, headers, response body). Sources: set
    breakpoints and step through JavaScript execution line by line. Live-editing CSS
    in Elements is the fastest way to prototype visual changes.
guidedSteps:
  - id: fe-app-m7-02-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You want to temporarily change a CSS value to see how it looks, without editing your source file. Which DevTools panel do you use?
    inputConfig:
      options:
        - "Console — type CSS rules here"
        - "Elements — select the element, then edit styles in the Styles pane"
        - "Sources — find the CSS file and edit it"
        - "Application — change styles in the localStorage"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Elements — select the element, then edit styles in the Styles pane"]
      rejectedFeedback: "The Elements panel shows the DOM tree and Styles pane. Click any element in the DOM tree (or right-click → Inspect on the page) to select it. The Styles pane shows applied CSS — click any value to edit it live. Changes are temporary (reset on refresh) but perfect for prototyping."
    hint: "This panel shows the DOM tree and the CSS applied to each element."
    reflectionPrompt: "Live CSS editing in DevTools is one of the most powerful development workflows. Prototype a change visually, confirm it looks right, then copy the working value to your source file. This is much faster than editing the file, saving, refreshing, checking — repeat. DevTools first, source file second."

  - id: fe-app-m7-02-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To pause JavaScript execution at a specific line and inspect all variable values at that moment, you set a ___ in the Sources panel.
    inputConfig:
      placeholder: "breakpoint"
    markingRule:
      matchMode: CONTAINS
      accepted: [breakpoint, break-point, "break point"]
      rejectedFeedback: "A breakpoint pauses execution at a specific line. In Sources, click the line number to add a blue marker. When the code reaches that line, execution pauses — you can inspect all variables in scope, step to the next line, or step into/over functions. This is far more powerful than console.log() for complex bugs."
    hint: "This debugging feature pauses code execution at a specific point."
    reflectionPrompt: "Breakpoints reveal the actual state of your program at any moment — all variables in scope, the call stack, the current DOM state. console.log() is like putting a camera at one point. A breakpoint is like stopping time and looking around. For complex bugs, breakpoints save hours."

  - id: fe-app-m7-02-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in 2-3 sentences how the Network tab helps you debug a fetch request that seems to be failing silently.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [network, request, response, status, headers, URL, body, 404, 500, CORS]
      rejectedFeedback: "The Network tab shows every HTTP request your page makes. For a failing fetch: check if the request appears (if not, it wasn't made), check the status code (404 = wrong URL, 500 = server error, CORS error = check headers), and inspect the response body for error messages from the server."
    hint: "The Network tab shows every HTTP request and its full response."
    reflectionPrompt: "Most 'my fetch is broken' bugs are visible immediately in the Network tab: the status code tells you if the server responded, the headers show CORS issues, the response body shows server errors. Without the Network tab, you're guessing. With it, the bug is almost always obvious."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "How do you open browser DevTools in Chrome/Edge/Firefox?"
    options:
      - "Right-click → View Page Source"
      - "F12, or Ctrl+Shift+I (Windows), Cmd+Option+I (Mac)"
      - "File → Developer Options"
      - "Ctrl+U"
    correctIndex: 1
    feedback: "F12 opens DevTools in all major browsers. Right-click → Inspect also opens DevTools focused on the clicked element. Ctrl+Shift+J opens DevTools and focuses the Console. Ctrl+U opens the raw page source (not DevTools). Memorise F12 — you will use it thousands of times."
  - type: MULTIPLE_CHOICE
    question: "The Lighthouse panel in DevTools does what?"
    options:
      - "Shows all network requests with timestamps"
      - "Audits the page for performance, accessibility, SEO, and best practices"
      - "Shows the JavaScript call stack"
      - "Displays all cookies and local storage"
    correctIndex: 1
    feedback: "Lighthouse runs automated audits against your page and scores it across: Performance (loading speed), Accessibility (WCAG checks), Best Practices (security, coding standards), SEO (search engine optimisation), and Progressive Web App. A score below 90 in any category is worth investigating."

retrieval:
  recall: "Name the four key DevTools panels and what each is used for."
  explain: "Explain the difference between using console.log() and setting a breakpoint for debugging JavaScript."
  mistakeId:
    code: "Editing the CSS file, saving, refreshing, and checking the result — repeatedly for every small change"
    answer: "Use the Elements panel: right-click the element → Inspect, then edit CSS directly in the Styles pane. See results instantly without saving or refreshing. Once you confirm the value works, copy it to your source file. This saves enormous time on visual debugging."
---

# Hook

DevTools is a superpower. It lets you inspect any element, live-edit CSS, monitor network requests, pause JavaScript mid-execution, and audit performance — all in real time.

Most beginners know DevTools exists but use only the Console. Mastering all four major panels transforms you from a developer who guesses to a developer who knows.

# Lore Introduction

*"The Academy's master tinkers,"* says Master Aelindra, opening a workshop drawer full of precision instruments, *"do not fix mechanisms by hitting them until they work. They use the right instruments: calipers to measure, probes to test, lenses to see. DevTools is your precision instrument set."*

# Core Learning

## Concept Introduction

**The four essential panels:**

| Panel | What it shows | Use it for |
|---|---|---|
| **Elements** | DOM tree + applied CSS | Inspect, live-edit HTML and CSS |
| **Console** | JS errors, warnings, logs | Test expressions, debug values |
| **Network** | All HTTP requests | Debug API calls, check status codes |
| **Sources** | JS files, breakpoints | Step-through debugging |

**Elements panel workflow:**
1. Right-click any element on the page → Inspect
2. See the element highlighted in the DOM tree
3. Styles pane shows all applied CSS (and where it comes from)
4. Click any value → edit live → see changes instantly
5. Box Model visualiser shows margin/padding/border values

**Console workflow:**
```javascript
// Log a value
console.log(user);

// Check multiple values
console.log({ user, posts, currentPage });

// Time an operation
console.time('fetch');
await fetchData();
console.timeEnd('fetch');
```

**Network tab workflow:**
1. Filter by: All / Fetch/XHR / JS / CSS / Images
2. Click a request → see Status, Headers, Response
3. Right-click → Replay XHR to re-send a request
4. Look for red rows (failed requests) first

**Sources breakpoints:**
1. Open Sources → find your file
2. Click a line number → blue dot (breakpoint)
3. Trigger the code → execution pauses
4. Inspect variable values in Scope panel
5. Step Over (F10) / Step Into (F11) / Resume (F8)

## Common Mistakes

- Never leaving the Console tab — missing the power of other panels
- Not clicking the filename in error messages to jump to the failing line
- Editing source files instead of prototyping in Elements first

## Mini Summary

- ✔ Elements: inspect + live-edit HTML/CSS (no save/refresh cycle)
- ✔ Console: errors, warnings, test expressions
- ✔ Network: every HTTP request with status, headers, response
- ✔ Sources: breakpoints for step-through debugging
- ✔ Lighthouse: audit performance, accessibility, SEO

# Guided Practice Quest

**The Precision Instruments** — three questions on DevTools panels and workflows. Steps in `guidedSteps`.

# Solo Practice Quest

Open DevTools on any web page and complete: (1) Find an element and see what CSS gives it its current colour. (2) Temporarily change that colour using the Styles pane. (3) Run `document.title = 'Hacked!'` in the Console. (4) Go to the Network tab and find a network request. Read its status code and response. Report what you found.

# Integration

**Connecting to Mathematics — Observability and Measurability**

Debugging is a special case of the scientific method: observe, hypothesise, test, conclude. DevTools provides the observability layer — without it, your application is a black box. The console is a probe for values; the Network tab is a probe for communication; breakpoints provide time-sliced state snapshots. In control theory, a system is "observable" if its internal state can be inferred from its outputs. DevTools makes the browser fully observable — every state, every request, every execution path is visible. Good engineering makes systems observable. DevTools provides that observability for the browser.

# Lore Conclusion

*"Every instrument in this workshop,"* says Master Aelindra, *"exists because someone once had to fix something without it and suffered. DevTools was built by engineers who spent too many hours guessing. Open it early. Leave it open. Use every panel. The instruments are here. The questions are yours."*

---
