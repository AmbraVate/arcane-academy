---
id: fe-app-m2-08
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m2
moduleTitle: "Module 2: HTML Foundations"
moduleGlyph: "📄"
moduleSortOrder: 2
topicSlug: content_elements
topicTitle: "Content Elements"
topicSortOrder: 2
lesson: images
title: "Images"
sortOrder: 4
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Writes a correct <img> tag with both src and alt attributes"
    - "Explains why alt text is required (not just 'for accessibility')"
    - "Distinguishes between decorative and informative images"
    - "Names at least two common image formats and when to use each"
    - "Explains what happens when an image fails to load"
  keywords: [img, src, alt, format, jpeg, png, webp, decorative, informative]
  modelAnswer: |
    The <img> element embeds an image using a src attribute pointing to the file.
    The alt attribute provides text shown when the image cannot load and read aloud
    by screen readers — omitting it harms both accessibility and SEO. Decorative
    images use empty alt="" so screen readers skip them; informative images need
    descriptive alt text.
guidedSteps:
  - id: fe-app-m2-08-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which attribute provides the text a screen reader announces when it encounters an image?
    inputConfig:
      options:
        - "title"
        - "alt"
        - "caption"
        - "description"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["alt"]
      rejectedFeedback: "The `alt` attribute is the only attribute screen readers reliably announce for images. `title` shows a tooltip on hover but is not reliably announced."
    hint: "It is required on every informative <img> element."
    reflectionPrompt: "Alt text serves double duty: it is read aloud by assistive technology AND displayed by the browser when the image fails to load. Writing good alt text is one of the highest-impact accessibility habits."

  - id: fe-app-m2-08-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the correct HTML for a company logo image:

      `<___ src="logo.png" alt="Arcane Academy logo">`
    inputConfig:
      placeholder: "img"
    markingRule:
      matchMode: CONTAINS
      accepted: [img]
      rejectedFeedback: "The element is `<img>` — a void element (self-closing, no closing tag needed). It requires `src` for the image source and `alt` for the description."
    hint: "It is a void element — it has no closing tag."
    reflectionPrompt: "<img> is one of the few void elements in HTML. Unlike <p> or <div>, it cannot contain children, so there is no </img>. The src and alt attributes do all the work."

  - id: fe-app-m2-08-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A page shows a purely decorative swirl pattern in the background of a hero section.
      What should the alt attribute contain, and why?
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [empty, blank, decorative, skip, screen reader]
      rejectedFeedback: "Decorative images should have alt=\"\" (empty string). This tells screen readers to skip the image entirely, avoiding noise for users who cannot see it."
    hint: "Think about what a screen reader user gains from hearing a description of a decorative swirl."
    reflectionPrompt: "alt=\"\" is the correct signal for purely decorative images. It is different from omitting the attribute entirely — omitting alt causes some screen readers to read the filename instead, which is even more confusing."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "An image fails to load. What does the browser display instead?"
    options:
      - "Nothing — the space collapses"
      - "The alt text"
      - "The title attribute value"
      - "A broken-image icon only, never text"
    correctIndex: 1
    feedback: "The browser displays the alt text when an image cannot load. This is why meaningful alt text matters even if you expect images to always load — networks are unreliable."
  - type: MULTIPLE_CHOICE
    question: "Which image format is best for photographs on a modern website?"
    options:
      - "BMP — maximum quality"
      - "GIF — universal browser support"
      - "WebP — smaller file size than JPEG with comparable quality"
      - "SVG — vector scales to any size"
    correctIndex: 2
    feedback: "WebP provides better compression than JPEG for photos and better than PNG for graphics, with wide modern browser support. SVG is ideal for icons and illustrations (vector), not photographs."

retrieval:
  recall: "Write the HTML for an image of a cat with appropriate alt text."
  explain: "Explain why alt=\"\" (empty alt) is the correct choice for decorative images, rather than a description."
  mistakeId:
    code: "<img src=\"logo.png\"> — no alt attribute"
    answer: "Omitting alt leaves screen readers to guess (often reading the filename). Always include alt — use a meaningful description for informative images and alt=\"\" for decorative ones."
---

# Hook

A picture is worth a thousand words — but only if your page can actually show it.

Images are among the most powerful elements in web design. They create emotional connection, communicate information faster than text, and make pages come alive. But used badly, they slow pages to a crawl, exclude users who can't see them, and break when networks fail.

Good frontend engineers treat images as carefully as they treat code.

> Before you read on: think of a website where images make a huge difference. What would that page look like without them?

# Lore Introduction

Master Aelindra opens the Academy's illustrated bestiary.

*"Every image in this book carries two things,"* she says. *"The picture itself — and a description written in the margin for those who cannot see it. The picture without the description is incomplete. The web works the same way."*

# Core Learning

## Concept Introduction

The `<img>` element embeds an image into a page. It is a **void element** — it has no closing tag and cannot contain children.

| Attribute | Required | Purpose |
|---|---|---|
| `src` | Yes | Path or URL to the image file |
| `alt` | Yes | Text description (screen readers + load failures) |
| `width` / `height` | Recommended | Prevents layout shift while loading |
| `loading="lazy"` | Optional | Defers loading until near viewport |

```html
<img src="images/castle.jpg" alt="The Arcane Academy's stone entrance gate" width="800" height="600">
```

## Why It Matters

Images account for the majority of page weight on most websites. Choosing the right format, providing `alt` text, and specifying dimensions directly affect performance, accessibility, and SEO — three of the most important properties of a production website.

## Worked Examples

**Informative image (needs alt text):**
```html
<img src="error-diagram.png" alt="Diagram showing request flow when a 404 error occurs">
```

**Decorative image (empty alt):**
```html
<img src="swirl-bg.png" alt="">
```

**Responsive image with lazy loading:**
```html
<img src="hero.webp" alt="Developer writing code at a glowing terminal" width="1200" height="600" loading="lazy">
```

## Common Mistakes

- **Missing `alt`:** Screen readers read the filename. Users get "logo-v3-final-FINAL.png read aloud."
- **Generic alt:** `alt="image"` or `alt="photo"` is useless — describe what the image shows.
- **Missing dimensions:** Without `width` and `height`, the browser doesn't reserve space. Content jumps as images load (Cumulative Layout Shift — a Core Web Vital).
- **Wrong format:** Using PNG for photos (huge files) or JPEG for icons (blocky edges).

## Mental Model

Think of an image as a **letter with an envelope**. The `src` is the letter inside — the visual. The `alt` is written on the outside of the envelope — what someone who can't open it needs to know. Both matter.

## Mini Summary

- ✔ `<img src="..." alt="...">` — void element, no closing tag
- ✔ `alt` is required — describe informative images, use `alt=""` for decorative
- ✔ Specify `width` and `height` to prevent layout shift
- ✔ WebP for photos, SVG for icons, PNG for graphics with transparency
- ✔ `loading="lazy"` defers off-screen images to improve initial load time

# Guided Practice Quest

**The Accessible Gallery** — answer three questions about using images correctly in HTML. Steps are in the frontmatter `guidedSteps` section.

# Solo Practice Quest

Write the HTML for a product card that includes:
1. A product image with meaningful alt text
2. A decorative separator line image with appropriate (empty) alt text
3. A brand logo with alt text

Explain your alt text choices in 2–3 sentences.

# Integration

**Connecting to Psychology — Dual Coding Theory**

Allan Paivio's Dual Coding Theory (1971) proposes that humans process verbal and visual information through separate but connected channels. Information presented both visually (as an image) and verbally (as text) is encoded more strongly and recalled more easily than information in one channel alone.

This is why images paired with descriptive captions outperform images alone — the brain creates two memory traces, not one. Good alt text isn't just an accessibility requirement; it is also the textual counterpart that reinforces the visual. The page that works for screen reader users often works better for sighted users too.

# Lore Conclusion

Master Aelindra closes the bestiary.

*"A page with images and no alt text is like a library with locked books. The knowledge exists — but only for those who can force the lock. Write the descriptions. Open the library."*

---
