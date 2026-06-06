---
id: fe-app-m2-03
school: engineering
domainId: frontend-engineering
tier: APPRENTICE
moduleId: fe-app-m2
moduleTitle: "Module 2: HTML Foundations"
moduleGlyph: "📄"
moduleSortOrder: 2
topicSlug: html_basics
topicTitle: "HTML Basics"
topicSortOrder: 1
lesson: attributes
title: "Attributes"
sortOrder: 3
difficulty: 1
estimatedMinutes: 20
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-app-m2-02]
integrationDomains: [psychology, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines what an HTML attribute is"
    - "Explains the name-value syntax of attributes"
    - "Names at least four global attributes"
    - "Explains what boolean attributes are"
    - "Describes the accessibility purpose of `alt` and `aria-*` attributes"
  keywords: [attribute, value, class, id, href, src, alt, disabled, required, aria, global, boolean]
  modelAnswer: |
    An HTML attribute provides additional information about an element. Attributes are written inside
    the opening tag as name-value pairs (e.g. `href="https://example.com"`). Global attributes apply
    to any element (e.g. `class`, `id`, `lang`, `title`, `tabindex`). Boolean attributes are present
    or absent — their presence means true (e.g. `disabled`, `required`, `checked`). Attributes like
    `alt` (for images) and `aria-*` (ARIA roles) are critical for accessibility.
guidedSteps:
  - id: fe-app-m2-03-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In `<a href="https://example.com">Click here</a>`, what is `href`?
    inputConfig:
      options:
        - "The element type"
        - "An attribute that specifies where the link points"
        - "The visible text of the link"
        - "A CSS class"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["An attribute that specifies where the link points"]
      rejectedFeedback: "`href` is an **attribute** — specifically, the hyperlink reference attribute that tells the browser where the link should navigate. Without it, `<a>` is not a functioning link."
    hint: "It's inside the opening tag and provides extra information about the element."
    reflectionPrompt: "Attributes configure elements. `<a>` without `href` is not a link. `<img>` without `src` loads nothing. `<input>` without `type` defaults to text. Attributes define what an element actually does."

  - id: fe-app-m2-03-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence:

      "The `alt` attribute on an `<img>` element provides ___ text — read aloud by screen readers when the image cannot be seen."
    inputConfig:
      placeholder: "alternative"
    markingRule:
      matchMode: CONTAINS
      accepted: [alternative, alt, "alternative text", "alt text"]
      rejectedFeedback: "The `alt` attribute provides **alternative text** — a textual description of the image. Screen readers read this aloud. It also displays if the image fails to load. An empty `alt` (`alt=\"\"`) tells screen readers to skip the image entirely (for decorative images)."
    hint: "The word 'alt' is short for this word."
    reflectionPrompt: "Missing or poor `alt` text is one of the most common accessibility failures on the web. Every informative image needs a meaningful alt description. Decorative images need `alt=\"\"`. This is not optional — it's a legal requirement in many jurisdictions."

  - id: fe-app-m2-03-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the difference between a standard attribute (like `class="button"`) and a boolean attribute (like `disabled`). Give an example of each.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [boolean, disabled, required, present, absent, value, true, class]
      rejectedFeedback: "Standard attributes have a name and a value: `class=\"button\"`. Boolean attributes are true by their presence alone — `disabled` means disabled; omitting it means not disabled. Writing `disabled=\"false\"` doesn't work — the element is still disabled. To un-disable it, remove the attribute entirely."
    hint: "What is different about how you write `disabled` compared to how you write `class`?"
    reflectionPrompt: "Boolean attributes catch many beginners: `disabled=\"false\"` still disables the element because the attribute is present. To conditionally apply boolean attributes with JavaScript, you must add or remove them — not set them to 'false'."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which attribute uniquely identifies an element within a page, and must be unique per document?"
    options:
      - "`class`"
      - "`name`"
      - "`id`"
      - "`key`"
    correctIndex: 2
    feedback: "`id` must be unique within a document. It's used for fragment links (`#section`), CSS targeting (`#my-id`), and JavaScript (`document.getElementById`). `class` can be shared across many elements. Using duplicate `id` values is invalid HTML and causes JavaScript bugs."
  - type: MULTIPLE_CHOICE
    question: "What does `<input type=\"text\" required>` mean?"
    options:
      - "The input will only accept text formatted as 'required'"
      - "The field is required and must be filled before form submission"
      - "`required` is not a valid attribute for inputs"
      - "The input displays the placeholder text 'required'"
    correctIndex: 1
    feedback: "`required` is a boolean attribute — its presence means the form cannot be submitted if this field is empty. Browsers show a built-in validation message. This is native HTML form validation — no JavaScript needed for basic required field validation."

retrieval:
  recall: "What is the syntax for an HTML attribute? Give three examples of different attributes."
  explain: "Why is a missing `alt` attribute on an `<img>` tag an accessibility problem?"
  mistakeId:
    code: "To disable a button with JavaScript, I set `button.disabled = 'false'`"
    answer: "Setting `disabled` to the string `'false'` still disables the button because the attribute is present. To enable a disabled button, set `button.disabled = false` (boolean false) or call `button.removeAttribute('disabled')`. Boolean attributes are controlled by presence/absence, not by their value."
---

# Hook

An HTML element without attributes is a body without clothes — present, but lacking specifics.

`<a>` without `href` goes nowhere. `<img>` without `src` shows nothing. `<input>` without `type` is vague. Attributes are what transform generic elements into specific, functional pieces of a web page.

More than that: attributes carry accessibility information, connect elements to JavaScript and CSS, define validation behaviour, and provide metadata that search engines use to understand your content.

Attributes are not optional extras. They are how elements know what to do.

> Look at this: `<img src="hero.jpg" alt="A mountain at sunrise" width="1200" height="600" loading="lazy">`. Can you identify each attribute and guess what it does?

# Lore Introduction

In the Academy's equipment store, tools come with specification tags: weight, material, sharpness, intended use. Without the tags, every tool looks the same.

*"Attributes are specification tags,"* says Master Aelindra, holding up a sword. *"This is a sword — that's the element. But without its attributes, I don't know its weight, its material, or who it belongs to."*

She points to the tag: *"Forged by the southern smiths. Weight: 1.2kg. Owner: Cadet Elara. Purpose: training."*

*"This is what attributes do for HTML elements. They specify. They connect. They configure."*

# Core Learning

## Concept Introduction

An **attribute** provides additional information about an HTML element. Attributes appear in the **opening tag** as name-value pairs:

```html
<element attributeName="value">content</element>
```

For example:
```html
<a href="https://example.com" target="_blank" rel="noopener">Visit</a>
```

Here, `href`, `target`, and `rel` are all attributes of the `<a>` element.

### Global Attributes

Some attributes apply to any HTML element:

| Attribute | Purpose |
|---|---|
| `id` | Unique identifier within the document |
| `class` | One or more CSS class names |
| `lang` | Language of the element's content |
| `title` | Tooltip text on hover |
| `tabindex` | Controls keyboard navigation order |
| `hidden` | Hides the element |
| `data-*` | Custom data attributes (for JavaScript) |
| `aria-*` | Accessibility properties (ARIA) |

### Element-Specific Attributes

| Attribute | Element | Purpose |
|---|---|---|
| `href` | `<a>` | Link destination |
| `src` | `<img>`, `<script>` | Resource URL |
| `alt` | `<img>` | Alternative text (accessibility) |
| `type` | `<input>` | Input type (text, email, password...) |
| `action` | `<form>` | Where form data is submitted |
| `method` | `<form>` | HTTP method (GET/POST) |

### Boolean Attributes

Boolean attributes are true by their presence and false by their absence. They have no value (or their value equals their name — both are valid):

```html
<input type="checkbox" checked>       <!-- Checked -->
<button disabled>Can't click</button> <!-- Disabled -->
<input type="email" required>         <!-- Required field -->
<details open>...</details>           <!-- Open by default -->
```

**Critical:** `disabled="false"` still disables the element. Remove the attribute to un-disable.

### Accessibility Attributes

```html
<!-- Always include alt on images -->
<img src="chart.png" alt="Bar chart showing revenue growth of 42% in Q3">

<!-- Decorative images: empty alt tells screen readers to skip -->
<img src="divider.png" alt="">

<!-- ARIA for custom interactive components -->
<div role="button" aria-pressed="false" tabindex="0">Toggle</div>
```

## Why It Matters

Attributes connect HTML to CSS (`class`, `id`), to JavaScript (`id`, `data-*`), to accessibility tools (`alt`, `aria-*`), and to browser behaviour (`type`, `required`, `loading`). Getting them right is not just correctness — it's the difference between an accessible, functional page and a broken one.

## Worked Examples

**Example 1 — Image with full attribute set:**
```html
<img
  src="/images/team-photo.jpg"
  alt="The Arcane Academy team at the 2026 graduation ceremony"
  width="1200"
  height="800"
  loading="lazy"
  class="team-photo"
>
```

**Example 2 — A link that opens in a new tab safely:**
```html
<a href="https://external-site.com" target="_blank" rel="noopener noreferrer">
  External resource
</a>
```
Note: `rel="noopener noreferrer"` is a security requirement when using `target="_blank"`.

**Example 3 — A form input with validation:**
```html
<input
  type="email"
  id="userEmail"
  name="email"
  placeholder="you@example.com"
  required
  autocomplete="email"
>
```

## Common Mistakes

- **Missing `alt` on images.** Required for accessibility.
- **Using `target="_blank"` without `rel="noopener noreferrer"`.** Security vulnerability.
- **Duplicate `id` values.** Must be unique per document.
- **Setting boolean attributes to `"false"`.** They are true by presence. Remove the attribute to make them false.

## Mental Model

Think of attributes as **properties on a form**:
- Name (element type): "Vehicle"
- Colour (class): "blue"
- Registration (id): "ABC-123"
- Owner (data-*): "Cadet Elara"
- Status (boolean): "Insured" ✓ / not present = not insured

Just as a blank property form is incomplete, an element without the right attributes is underspecified.

## Mini Summary

- Attributes appear in the opening tag as name-value pairs
- Global attributes apply to any element: `id`, `class`, `lang`, `title`, `data-*`, `aria-*`
- Boolean attributes are true by presence; remove them to make them false
- `alt` on images is required for accessibility
- Attributes connect HTML to CSS, JavaScript, browser behaviour, and assistive technology

# Guided Practice Quest

**The Specification Clerk**

The Academy's equipment has arrived untagged. Apprentices must correctly attribute each item — providing the right information in the right format.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Write the HTML for a contact form that includes:
- A name field (text, required)
- An email field (email type, required)
- A phone field (tel type, optional)
- A message textarea (required, minimum description length)
- A submit button (disabled until you say otherwise)
- A link to the privacy policy that opens in a new tab

For each element, include all relevant attributes. After writing it, review: have you included all accessibility attributes needed? Are boolean attributes correct?

# Integration

**Connecting to Sciences — Metadata and the Information Value of Attributes**

In information theory (Shannon, 1948), the value of information is measured by how much uncertainty it resolves. Attributes resolve uncertainty about HTML elements — they transform a general tag into a specific, configured resource.

This principle appears throughout science. In biology, gene expression attributes (methylation patterns) determine which genes are active. In chemistry, molecular attributes (valence electrons, electronegativity) determine how elements bond. In physics, quantum state attributes determine particle behaviour.

The pattern: a base entity (gene, element, particle, HTML tag) plus attributes determines specific behaviour. The entity type constrains what's possible; the attributes specify what actually happens.

What does this suggest about the relationship between structure and configuration in well-designed systems?

# Lore Conclusion

Every tool in the store is tagged. Every attribute is correct. The equipment room is in order.

*"An element without the right attributes,"* says Master Aelindra, *"is a promise half-made. You've told the browser what kind of thing it is — but not how it should behave, or what it connects to, or who it's for."*

She adds a rune.

*"Now: structure. Before you add any content, you must build the bones of the page. The HTML document structure — where everything lives and why."*
