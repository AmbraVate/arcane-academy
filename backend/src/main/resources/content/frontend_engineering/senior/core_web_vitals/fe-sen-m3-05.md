---
id: fe-sen-m3-05
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m3
moduleTitle: "Module 3: Performance Engineering"
moduleGlyph: "🚀"
moduleSortOrder: 3
topicSlug: core_web_vitals
topicTitle: "Core Web Vitals"
topicSortOrder: 5
lesson: core_web_vitals
title: "Core Web Vitals"
sortOrder: 1
difficulty: 4
estimatedMinutes: 35
xpReward: 120
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Correctly defines LCP, INP, and CLS and what each measures
    - Explains the good/needs improvement/poor thresholds for each metric
    - Connects LCP to bundle size and server response time
    - Connects INP to main thread blocking and JavaScript execution
    - Connects CLS to layout instability caused by late-loading content
  keywords: [LCP, INP, CLS, Largest Contentful Paint, Interaction to Next Paint, Cumulative Layout Shift, main thread, layout shift, threshold, Lighthouse, real user]
  modelAnswer: |
    Core Web Vitals are Google's user-experience metrics that directly affect search ranking. LCP (Largest Contentful Paint) measures how long it takes for the largest visible content element (hero image, headline) to render — good is <2.5s. INP (Interaction to Next Paint, replacing FID) measures how long the browser takes to respond to user interactions — good is <200ms. CLS (Cumulative Layout Shift) measures how much visible content shifts during load — good is <0.1.

    LCP is affected by: server response time, render-blocking resources, image optimisation, and JavaScript bundle size. Fix: preload the hero image, optimise server response, reduce bundle size.

    INP is affected by: main thread blocking (long JavaScript tasks), large event handler code, and synchronous DOM operations during interactions. Fix: break long tasks into smaller async chunks, defer non-critical JavaScript, minimise event handler work.

    CLS is affected by: images without explicit dimensions, ads that push content down, fonts that cause layout shifts on load, and late-loading elements. Fix: set explicit width/height on images and iframes, use font-display: optional or swap, reserve space for dynamic content.
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "A user clicks a button and the app appears to freeze for 800ms before responding. Which Core Web Vital is most affected?"
    options:
      - "LCP — the visual content was slow to appear"
      - "CLS — the button's position shifted"
      - "INP — the interaction response was delayed"
      - "TTFB — the server was slow to respond"
    correctIndex: 2
    feedback: "INP (Interaction to Next Paint) measures the latency from user interaction to visual response. An 800ms delay is far above the 200ms 'good' threshold. INP is affected by main thread blocking — likely a large JavaScript execution in the click handler, or a synchronous DOM operation. The fix: break the work into smaller async chunks using setTimeout or scheduler.postTask."
  - type: SHORT_TEXT
    prompt: "A news site's hero article image loads 3 seconds after the page. The image has no dimensions specified. How does this affect both LCP and CLS?"
    hint: "Think about what LCP measures and what happens to the layout when a large image loads without reserved space."
  - type: FILL_BLANK
    prompt: "LCP measures when the ___ content element is rendered. CLS measures ___. INP measures the time from ___ to visual response."
    answer: "largest visible; cumulative layout shift score; user interaction"
    hint: "Each metric captures a different dimension of user experience."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "An e-commerce site has ads that load 500ms after content, pushing the page down by 200px. Which metric is primarily harmed?"
    options:
      - "LCP — the largest element loaded late"
      - "CLS — existing content shifted when the ad loaded"
      - "INP — the ad interaction is slow"
      - "TTFB — the ad server responds slowly"
    correctIndex: 1
    feedback: "CLS (Cumulative Layout Shift) captures unexpected movement of visible content. An ad that pushes content down after load creates a large layout shift — the user's reading position jumps. Fix: reserve space for the ad container with a fixed height before the ad loads. Users and search engines both penalise CLS."
  - type: MULTIPLE_CHOICE
    question: "Lighthouse measures performance in a controlled lab environment. Why should teams also measure Real User Monitoring (RUM) for Core Web Vitals?"
    options:
      - "Lighthouse doesn't support Core Web Vitals"
      - "RUM captures real network conditions, devices, and interaction patterns that lab tests can't replicate"
      - "Lighthouse scores are too generous"
      - "RUM is free; Lighthouse requires payment"
    correctIndex: 1
    feedback: "Lighthouse runs on a simulated device with a throttled connection — useful for catching regressions but not representative of all real users. RUM from the field shows metrics for actual users on actual devices and networks. A Lighthouse score of 90 doesn't mean all users have a good experience — P75 field data might tell a different story."
retrieval:
  recall: "Name the three Core Web Vitals and give the 'good' threshold for each."
  explain: "Why does loading a large image without explicit dimensions harm CLS?"
  mistakeId:
    code: |
      // Image with no dimensions — common CLS cause
      <img src="/hero.jpg" alt="Hero" className="w-full" />
      
      // Appears in hero section above the fold
    answer: "Without explicit height, the browser doesn't know how much space to reserve for the image. It renders the page, then when the image loads, the layout shifts to accommodate it — pushing all subsequent content down. Every element below the image shifts, accumulating a large CLS score. Fix: add height prop (or use aspect-ratio CSS), or use the img's intrinsic dimensions: <img src='/hero.jpg' width='1200' height='600' alt='Hero' className='w-full h-auto' />. The browser now reserves the correct space before the image loads."
---

# Hook

Your app passes all your functional tests. Users are happy with the features. Then you check Google Search Console: your pages are ranked lower because your Core Web Vitals are poor. LCP is 4.2 seconds. CLS is 0.25. Users on slow connections bounce before the page loads.

Core Web Vitals are not abstract metrics — they affect real user experience and search ranking.

# Lore Introduction

*"The Guild judges a completed quest not only by whether the dragon was slain,"* the Record Keeper explains, *"but by three measures: how quickly the party arrived at the lair, how quickly they responded when the dragon moved, and whether the village was left in order when they departed."*

She marks three columns in the ledger. *"LCP. INP. CLS. Arrival speed. Response speed. Order preserved. All three matter."*

# Core Learning

## Concept Introduction

**Core Web Vitals** are Google's standardised metrics for user experience quality:

| Metric | What it Measures | Good | Needs Improvement | Poor |
|---|---|---|---|---|
| **LCP** (Largest Contentful Paint) | How long until the largest visible element renders | <2.5s | 2.5–4s | >4s |
| **INP** (Interaction to Next Paint) | Delay from user interaction to visual response | <200ms | 200–500ms | >500ms |
| **CLS** (Cumulative Layout Shift) | Unexpected movement of page content | <0.1 | 0.1–0.25 | >0.25 |

**Improving LCP:**
- Preload the hero image: `<link rel="preload" as="image" href="/hero.jpg">`
- Reduce server response time (TTFB)
- Eliminate render-blocking CSS/JS
- Set explicit image dimensions to avoid layout shift during load
- Use next-gen image formats (WebP, AVIF)

**Improving INP:**
- Break long JavaScript tasks (>50ms) into smaller chunks
- Defer non-critical JavaScript with `defer` or dynamic import
- Minimise work in event handlers — defer heavy processing

**Improving CLS:**
- Always set `width` and `height` on `<img>` and `<iframe>`
- Reserve space for ads and dynamic content
- Use `font-display: optional` or preload fonts
- Avoid inserting content above existing content after page load

## Why It Matters

Since 2021, Core Web Vitals are a Google ranking signal. Poor metrics = lower search ranking = less organic traffic. Beyond SEO: each metric measures a real user experience dimension. LCP correlates with bounce rate. INP correlates with task completion. CLS correlates with accidental clicks.

## Common Mistakes

- **Only measuring in Lighthouse.** Lighthouse is lab data — measure field data via Google Search Console or RUM.
- **Optimising LCP without checking INP.** A fast-loading page that freezes on interaction still fails.
- **Forgetting CLS from fonts.** A font that loads after content causes text to reflow — significant CLS. Use `font-display: swap` or preload.
- **Treating 75th percentile.** Google evaluates the 75th percentile of field data — 25% of your users must have a good experience even on slow connections.

## Mental Model

Core Web Vitals are the restaurant inspection scores of the web — three numbers standing in for the full dining experience. LCP is how long until the main course reaches the table: the moment the customer feels served, not when the kitchen started cooking. INP is how quickly waiters respond when customers ask for something — any tap or keystroke that gets a sluggish reaction is a waiter ignoring a raised hand. CLS is the table not lurching mid-meal: content that shifts under a user's finger is the plate sliding as they reach for it. The inspection analogy carries the deeper truths too — Google grades on *field data* (real diners' experiences, the 75th percentile, on their phones and networks), not your lab kitchen demo on a fast machine; and the score is posted on the door (search ranking), so it's a business number, not an engineering vanity metric.

## Mini Summary

- ✔ LCP: time to render largest visible element — fix by preloading, reducing bundle, fast server
- ✔ INP: interaction response latency — fix by reducing main thread blocking
- ✔ CLS: layout shift score — fix by reserving space for images, ads, and dynamic content
- ✔ Measure in the field (RUM) not just in Lighthouse
- ✔ Core Web Vitals affect both user experience and Google search ranking

# Guided Practice Quest

Work through the guided steps to connect each metric to its cause and fix.

# Solo Practice Quest

Audit a web page you know (or use https://web.dev/measure). For each Core Web Vital: what is the score, is it good/needs improvement/poor, and what is likely causing it? Propose one specific fix for each metric that needs improvement.

# Integration

**Psychology — Perception Thresholds and User Tolerance**

Each Core Web Vital threshold is grounded in perceptual psychology research. The 2.5s LCP threshold aligns with the ~2-3s "flow" state before users perceive loading as problematic (Miller's Response Time Limits, 1968). The 200ms INP threshold is close to the 100-300ms window where interactions feel instant — above 300ms, users perceive a response delay (Nielsen, 1994). The 0.1 CLS threshold captures shifts that cause "fat finger" errors — accidental taps on content that moved just as the user was tapping. These are not arbitrary engineering numbers — they represent human perceptual limits, verified by Google's analysis of billions of real user sessions correlating metrics with bounce rates and conversion rates.

# Lore Conclusion

*"The three ledger columns are filled,"* the Record Keeper says. *"Arrival: 2.1 seconds. Response: 180ms. Village: undisturbed. All three within the good threshold."*

She stamps the quest record. *"Performance Engineering module complete."*

---
