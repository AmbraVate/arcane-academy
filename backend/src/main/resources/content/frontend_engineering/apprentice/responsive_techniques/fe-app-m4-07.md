---
id: fe-app-m4-07
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m4
moduleTitle: "Module 4: Responsive Design"
moduleGlyph: "📱"
moduleSortOrder: 4
topicSlug: responsive_techniques
topicTitle: "Responsive Techniques"
topicSortOrder: 2
lesson: responsive_images
title: "Responsive Images"
sortOrder: 4
difficulty: 2
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Uses srcset to serve different image sizes at different viewports"
    - "Applies max-width: 100% to prevent images from overflowing"
    - "Uses the sizes attribute correctly"
    - "Explains the benefit of serving WebP format on supporting browsers"
    - "Uses loading='lazy' for images below the fold"
  keywords: [srcset, sizes, responsive, webp, lazy-loading, max-width, picture, source, resolution, DPR]
  modelAnswer: |
    Responsive images serve appropriately sized files for each viewport and device pixel
    ratio. `srcset` lists candidate images with width descriptors; `sizes` tells the browser
    how wide the image will render at each viewport. The browser picks the optimal image.
    `<picture>` with `<source>` elements enables format selection (WebP with JPEG fallback).
    All images should have max-width: 100% to prevent overflow, and loading="lazy" for
    images below the fold.
guidedSteps:
  - id: fe-app-m4-07-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      An image is always displayed at 100% viewport width on mobile and 50% viewport width on tablet+. Which `sizes` attribute is correct?
    inputConfig:
      options:
        - "sizes=\"100px\""
        - "sizes=\"(min-width: 768px) 50vw, 100vw\""
        - "sizes=\"auto\""
        - "sizes=\"50%, 100%\""
    markingRule:
      matchMode: NORMALIZED
      accepted: ["sizes=\"(min-width: 768px) 50vw, 100vw\""]
      rejectedFeedback: "The sizes attribute tells the browser how wide the image will render, so it can choose the right srcset candidate. (min-width: 768px) 50vw = at 768px+, the image is 50% of the viewport. The last value (100vw) is the default (mobile): full viewport width."
    hint: "sizes mirrors media queries but describes image display size, not layout breakpoints."
    reflectionPrompt: "sizes is evaluated left-to-right — first match wins. The browser uses sizes to pick from srcset: if the image renders at 50vw on a 1000px screen with 2× DPR, it needs ~1000px image. Without sizes, the browser assumes 100vw and may load unnecessarily large images."

  - id: fe-app-m4-07-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To prevent an image from overflowing its container at any screen size, add to your CSS:

      `img { max-width: ___; height: auto; }`
    inputConfig:
      placeholder: "100%"
    markingRule:
      matchMode: CONTAINS
      accepted: ["100%"]
      rejectedFeedback: "max-width: 100% is the most important single CSS rule for responsive images. It prevents images from overflowing their container regardless of their intrinsic size. height: auto maintains the aspect ratio. This two-line rule should be in every CSS reset."
    hint: "This makes the image flex with its container, never exceeding it."
    reflectionPrompt: "This is often included in the CSS reset/normalise that every project starts with: img { max-width: 100%; height: auto; display: block; }. display: block removes the default bottom gap on inline images. max-width: 100% makes images responsive by default."

  - id: fe-app-m4-07-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in 2-3 sentences why serving a 3000px-wide image to a 375px phone is wasteful, and how srcset solves this.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [bandwidth, size, download, srcset, candidate, choose, wasteful, small, mobile]
      rejectedFeedback: "A 3000px image might be 2MB. A phone only needs a 750px image (375px × 2× DPR = 750px), perhaps 150KB. Downloading 2MB to display 150KB wastes bandwidth, slows page load, and uses mobile data. srcset provides candidate images; the browser downloads only the appropriate one."
    hint: "Think about the file size difference between a 3000px and a 750px image."
    reflectionPrompt: "Images are the largest contributors to page weight on most sites. Serving appropriately sized images can reduce page weight by 80–90% for mobile users. This directly impacts load time, bounce rate, and Google Lighthouse performance scores — all of which affect real user outcomes."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `loading=\"lazy\"` on an image do?"
    options:
      - "Makes the image load more slowly for dramatic effect"
      - "Defers loading the image until it is near the visible viewport"
      - "Loads the image in the background after all other resources"
      - "Requires JavaScript to function"
    correctIndex: 1
    feedback: "loading=\"lazy\" is native browser lazy loading — no JavaScript required. The browser defers loading images below the fold until the user scrolls near them. This significantly improves initial page load time by not downloading images the user may never see."
  - type: MULTIPLE_CHOICE
    question: "The `<picture>` element with `<source type=\"image/webp\">` achieves:"
    options:
      - "Background images that change on hover"
      - "Serving WebP to browsers that support it, with a JPEG fallback for others"
      - "Animated pictures that work in all browsers"
      - "Images that load from multiple URLs simultaneously"
    correctIndex: 1
    feedback: "<picture> with <source type=\"image/webp\"> serves WebP (30-50% smaller than JPEG) to supporting browsers, falling back to the <img> src for older browsers. This provides the best format for each browser without compromising compatibility."

retrieval:
  recall: "Write an <img> with srcset offering 400px and 800px versions, max-width: 100%, and lazy loading."
  explain: "Explain what the sizes attribute does and why it matters for srcset image selection."
  mistakeId:
    code: "<img src=\"hero.jpg\" width=\"3000\" height=\"2000\"> — serving one huge image to all devices"
    answer: "Use srcset for responsive images: srcset=\"hero-400.jpg 400w, hero-800.jpg 800w, hero-1600.jpg 1600w\" sizes=\"(min-width: 768px) 50vw, 100vw\". Add max-width: 100% in CSS and loading=\"lazy\" if below the fold. The browser picks the right size automatically."
---

# Hook

Images are the largest files on most web pages. A hero image that looks sharp on a Retina MacBook may weigh 5MB — downloaded by every mobile user on 4G, even though they only need 300KB.

Responsive images serve the right file to the right device. Done well, they are invisible to users. Done badly, they are your biggest performance problem.

# Lore Introduction

*"The Academy's messengers,"* says Master Aelindra, *"carry different sized maps for different journeys. A scout carries a pocket map — clear enough for the mission. The cartographer carries the full atlas. Sending a scout with an atlas is absurd. So is sending a 3MB image to a phone."*

# Core Learning

## Concept Introduction

**CSS foundation (apply to all images):**
```css
img {
  max-width: 100%;
  height: auto;
  display: block;
}
```

**srcset — serve different sizes:**
```html
<img
  src="hero-800.jpg"
  srcset="
    hero-400.jpg  400w,
    hero-800.jpg  800w,
    hero-1600.jpg 1600w
  "
  sizes="
    (min-width: 1024px) 1200px,
    (min-width: 768px)  768px,
    100vw
  "
  alt="Academy entrance"
  loading="lazy"
  width="1600"
  height="900"
>
```

**Picture — serve different formats:**
```html
<picture>
  <source type="image/webp" srcset="hero.webp 1600w, hero-800.webp 800w">
  <source type="image/jpeg" srcset="hero.jpg 1600w, hero-800.jpg 800w">
  <img src="hero.jpg" alt="Academy entrance" loading="lazy">
</picture>
```

**loading="lazy" — defer off-screen images:**
```html
<img src="below-fold.jpg" alt="..." loading="lazy">
```

**Width/height attributes — prevent layout shift:**
```html
<!-- Always include width and height to prevent CLS -->
<img src="..." alt="..." width="800" height="600" loading="lazy">
```

## Common Mistakes

- **Serving one large image at all screen sizes**: Sending a 3000px image to a 375px phone wastes 80–90% of the downloaded bytes. `srcset` lets the browser pick the appropriately sized file.
- **Omitting `max-width: 100%` on images**: Without this, images render at their intrinsic pixel width and overflow their containers on small screens.
- **Writing `sizes` incorrectly**: `sizes` tells the browser how wide the image renders, not its intrinsic size. A common mistake is setting `sizes="100vw"` for an image that only occupies 50% of the layout on desktop, causing the browser to fetch a needlessly large file.
- **Applying `loading="lazy"` to above-the-fold images**: Hero images and content in the initial viewport should not be lazy-loaded — they need to start loading immediately to avoid slow LCP (Largest Contentful Paint) scores.
- **Omitting `width` and `height` attributes**: Without explicit dimensions, the browser cannot reserve space before the image loads, causing Cumulative Layout Shift (CLS).

## Why It Matters

Images typically make up 60–80% of a page's total weight. Optimised responsive images can reduce load time by 60% for mobile users. Google uses Core Web Vitals (which includes image performance) in search rankings.

## Mental Model

Responsive images work like a courier choosing a vehicle. The package is the picture; the delivery is the user's screen and connection. Shipping one giant original to everyone is sending a removal van for every parcel — a 4000px photo delivered to a phone on mobile data wastes time, battery, and bandwidth. `srcset` is the courier's garage: the same image in several sizes, and the *browser* (which alone knows the screen, pixel density, and network) picks the right vehicle per delivery. Your job isn't choosing the vehicle — it's stocking the garage and describing the route (`sizes`) honestly.

## Mini Summary

- ✔ `max-width: 100%; height: auto` — every image is responsive by default
- ✔ `srcset` and `sizes` — let the browser choose the right size
- ✔ `<picture>` with `<source>` — serve WebP with JPEG fallback
- ✔ `loading="lazy"` — defer loading images below the fold
- ✔ Always include `width` and `height` attributes to prevent layout shift

# Guided Practice Quest

**The Efficient Messenger** — three questions on responsive image techniques. Steps in `guidedSteps`.

# Solo Practice Quest

Write the HTML and CSS for a three-image gallery. Each image should: use srcset with two sizes (400w and 800w), have appropriate alt text, lazy-load, prevent overflow (max-width: 100%), include width and height to prevent layout shift, and use WebP with JPEG fallback via `<picture>`. Explain why you chose each technique.

# Integration

**Connecting to Sciences — Information Theory and Compression**

Image file formats are an application of information theory. Shannon's source coding theorem establishes that any lossless compression scheme approaches an information-theoretic limit. JPEG, PNG, and WebP use different approaches: JPEG uses discrete cosine transform (lossy, good for photographs), PNG uses DEFLATE (lossless, good for graphics), WebP combines both techniques achieving better compression than either alone. Choosing the right format is an engineering decision with mathematical foundations — you are selecting the encoding scheme that minimises file size while preserving perceptual quality above a threshold.

# Lore Conclusion

*"An image that takes five seconds to load,"* says Master Aelindra, *"has failed before it appeared. The user left. The responsive image engineer's job is to make images invisible — to serve them so efficiently that they arrive before the user notices they were absent. Performance is a feature. Bloat is a bug."*

---
