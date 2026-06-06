---
id: fe-sen-m6-01
school: engineering
domainId: frontend-engineering
tier: SENIOR
moduleId: fe-sen-m6
moduleTitle: "Module 6: Advanced Accessibility"
moduleGlyph: "♿"
moduleSortOrder: 6
topicSlug: wcag_compliance
topicTitle: "WCAG Compliance"
topicSortOrder: 1
lesson: wcag_compliance
title: "WCAG Compliance"
sortOrder: 1
difficulty: 4
estimatedMinutes: 35
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
    - Correctly states the four POUR principles and their meaning
    - Explains the difference between conformance levels (A, AA, AAA)
    - Identifies which level is legally required in most jurisdictions
    - Names at least 6 specific WCAG 2.1 success criteria and what each requires
    - Explains how WCAG compliance connects to legal risk and broader inclusion
  keywords: [POUR, Perceivable, Operable, Understandable, Robust, Level A, Level AA, Level AAA, WCAG 2.1, EAA, ADA, EN 301 549, criterion, contrast, keyboard, label, focus]
  modelAnswer: |
    WCAG (Web Content Accessibility Guidelines) are organised around four POUR principles: Perceivable (information must be presentable in ways users can perceive), Operable (UI components must be operable by all users), Understandable (information and UI must be understandable), Robust (content must be interpreted by assistive technologies).

    Conformance levels: A (minimum — must be met), AA (standard — required by most laws), AAA (enhanced — aspirational). Most legal requirements (EU Accessibility Act, ADA Title III case law, UK Equality Act, Section 508) mandate AA compliance.

    Key WCAG 2.1 success criteria: 1.1.1 (text alternatives for non-text content), 1.3.1 (information conveyed via structure, not just colour/position), 1.4.3 (contrast ratio ≥4.5:1 for normal text), 2.1.1 (all functionality via keyboard), 2.4.7 (focus visible for keyboard navigation), 3.1.1 (page language identified), 3.3.1 (error identification in forms), 4.1.2 (name/role/value for UI components).

    Legal risk is significant and growing: the EU Accessibility Act requires AA compliance for all digital products sold in the EU by 2025+. In the US, thousands of ADA accessibility lawsuits are filed annually. Beyond legal risk, accessibility reaches the 26% of adults who have a disability — and benefits everyone (captions help in noisy environments; keyboard navigation helps power users).
guidedSteps:
  - type: MULTIPLE_CHOICE
    prompt: "Your form shows validation errors using only red colour (no error icon, no text description). Which WCAG criterion does this violate?"
    options:
      - "1.4.3 — Colour Contrast"
      - "1.4.1 — Use of Colour (information cannot be conveyed by colour alone)"
      - "2.1.1 — Keyboard Accessible"
      - "3.3.1 — Error Identification"
    correctIndex: 1
    feedback: "1.4.1 (Use of Colour) requires that information is not conveyed by colour alone. A user who is colour blind cannot distinguish red from other colours. Add a text description ('This field is required') or an error icon (with aria-label) alongside the colour. 3.3.1 (Error Identification) additionally requires that the error is described in text."
  - type: SHORT_TEXT
    prompt: "Explain why WCAG Level AA is the practical compliance target, even though Level AAA exists and is more comprehensive."
    hint: "What do laws require? What is Level AAA realistically achievable for?"
  - type: FILL_BLANK
    prompt: "The POUR principles are: Perceivable, ___, ___, and Robust."
    answer: "Operable; Understandable"
    hint: "POUR — the four pillars of WCAG."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "WCAG 2.1 criterion 1.4.3 requires a minimum contrast ratio of 4.5:1 for normal text. A developer uses text colour #767676 on white background (#FFFFFF). The ratio is 4.48:1. What should they do?"
    options:
      - "The difference is negligible — ship it"
      - "Darken the text colour slightly until the ratio reaches 4.5:1"
      - "Add a darker background instead"
      - "The rule only applies to headings, not body text"
    correctIndex: 1
    feedback: "4.48:1 fails the 4.5:1 requirement. Even a small adjustment — #757575 achieves 4.6:1. Tools like the WebAIM Contrast Checker show the exact ratio and what values pass. This is one of the most commonly failing criteria and one of the easiest to fix with the right colour values. Note: large text (18pt+) only needs 3:1."
  - type: MULTIPLE_CHOICE
    question: "An enterprise SaaS product must comply with the EU Accessibility Act. Which conformance level is required?"
    options:
      - "Level A only"
      - "Level AA"
      - "Level AAA"
      - "Compliance is optional for B2B products"
    correctIndex: 1
    feedback: "The EU Accessibility Act (and most comparable legislation worldwide) requires WCAG 2.1 Level AA. Level A is the minimum but insufficient legally. Level AAA is aspirational — WCAG itself acknowledges that some AAA criteria are not achievable for all content types. Level AA is the practical and legal standard."
retrieval:
  recall: "Explain the four POUR principles. Give one example success criterion for each."
  explain: "Why does accessibility compliance benefit users beyond those with disabilities? Name three groups."
  mistakeId:
    code: |
      // A developer's view of accessibility compliance
      "Accessibility is for blind users. Our analytics show 0.3% of users
       use screen readers. The business priority is elsewhere."
    answer: "This argument fails on multiple dimensions: (1) The 26% of adults with disabilities spans many conditions — not just blindness. Visual impairments, motor disabilities, cognitive differences, hearing loss. Screen reader usage is a proxy metric, not total disabled user count. (2) The benefits are broader: captions (originally for deaf users) are used in noisy environments; voice control (motor disability) is used by everyone with smart speakers. (3) Legal risk is real and growing — EU Accessibility Act fines can reach millions. (4) Inaccessible sites actively exclude users who may be customers. The 0.3% metric likely undercounts by an order of magnitude."
---

# Hook

A law firm sends a demand letter: your website is not accessible to users with visual impairments, violating the ADA. The remediation cost estimate: £200,000. The original cost to build it accessibly: £15,000.

WCAG compliance is not a nicety. It is a legal requirement, a technical standard, and an inclusion commitment.

# Lore Introduction

*"The Academy's gates must be passable to all apprentices,"* the Registrar announces. *"Those who navigate by touch. Those who cannot hear. Those who process information differently. The gates serve all, or they serve none."*

She holds up the WCAG charter. *"This document codifies what 'all' means, and how to achieve it."*

# Core Learning

## Concept Introduction

**WCAG (Web Content Accessibility Guidelines)** is the international standard for web accessibility, published by the W3C.

**The POUR Principles:**
| Principle | Meaning |
|---|---|
| **Perceivable** | Content must be presentable in ways all users can perceive (text alternatives, captions, sufficient contrast) |
| **Operable** | UI must be operable by all users (keyboard accessible, no seizure triggers, enough time) |
| **Understandable** | Content must be understandable (readable language, predictable behaviour, error identification) |
| **Robust** | Content must work with current and future assistive technologies (valid HTML, ARIA) |

**Conformance Levels:**
- **Level A** — Must meet (minimum). 30 criteria.
- **Level AA** — Should meet (standard). Required by most laws. 20 additional criteria.
- **Level AAA** — May meet (enhanced). 28 additional criteria. Not required for all content.

**Key WCAG 2.1 AA criteria:**
| Criterion | Requirement |
|---|---|
| 1.1.1 Non-text Content | Images need alt text |
| 1.4.1 Use of Colour | Information not conveyed by colour alone |
| 1.4.3 Contrast | 4.5:1 for normal text, 3:1 for large text |
| 2.1.1 Keyboard | All functionality accessible via keyboard |
| 2.4.7 Focus Visible | Keyboard focus must be visible |
| 3.3.1 Error Identification | Errors identified and described in text |
| 4.1.2 Name/Role/Value | UI components have accessible names and roles |

**Legal landscape (2025):**
- EU Accessibility Act: WCAG 2.1 AA for all digital products
- US ADA: court interpretations mandate accessibility for commercial websites
- UK Equality Act: public sector mandated; case law extending to private sector
- Section 508: US federal government and contractors

## Common Mistakes

- **Treating contrast as a one-time audit.** Contrast failures happen with every design change. Integrate contrast checking into design review.
- **Relying only on automated tools.** They catch 30-40%. Manual testing is essential.
- **Confusing "users with disabilities" with "screen reader users only".** Keyboard-only users, users with motor disabilities, cognitive differences, low vision — all affected by different criteria.

## Mini Summary

- ✔ WCAG POUR: Perceivable, Operable, Understandable, Robust
- ✔ Target: WCAG 2.1 Level AA — the legal and industry standard
- ✔ Key criteria: contrast (1.4.3), keyboard (2.1.1), focus visible (2.4.7), error text (3.3.1), ARIA (4.1.2)
- ✔ Legal risk is real and growing across all major jurisdictions
- ✔ Accessibility benefits everyone — captions, keyboard navigation, clear errors

# Guided Practice Quest

Work through the guided steps to identify WCAG violations and understand the legal compliance landscape.

# Solo Practice Quest

Review a web page (real or hypothetical). For each POUR principle, identify one potential WCAG criterion that the page might violate and one that it likely meets. Propose a fix for each violation. This is the same process as a WCAG audit.

# Integration

**Philosophy — Rights, Inclusion, and Universal Design**

WCAG embodies a philosophical shift from charity to rights. Early disability policy treated accessibility as special accommodation for a marginalised group. Universal Design theory (Ronald Mace, 1985) reframes it: design for the full range of human capability, and everyone benefits. The curb cut effect is the empirical evidence: curb cuts (designed for wheelchair users) benefit parents with pushchairs, cyclists, delivery workers, and elderly pedestrians. Captioning (designed for deaf users) benefits anyone watching in a noisy environment. Keyboard navigation (designed for users with motor disabilities) benefits power users. The philosophical implication: inclusive design is good design. WCAG compliance is not about accommodating outliers — it's about recognising that human capability is a spectrum, and design that serves the full spectrum serves everyone better.

# Lore Conclusion

*"The gates are now passable to all,"* the Registrar says. *"The provisions are specific, the standards are measurable, the audit is repeatable. Accessibility is not aspiration — it is compliance."*

---
