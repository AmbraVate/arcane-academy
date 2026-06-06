---
id: fe-lea-m3-02
school: engineering
domainId: frontend-engineering
tier: LEAD
moduleId: fe-lea-m3
moduleTitle: "Module 3: UX Psychology"
moduleGlyph: "🧠"
moduleSortOrder: 3
topicSlug: user_behaviour
topicTitle: "User Behaviour"
topicSortOrder: 2
lesson: user_behaviour
title: "User Behaviour"
sortOrder: 1
difficulty: 4
estimatedMinutes: 40
xpReward: 150
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, philosophy, design]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - Explains satisficing behaviour and how it differs from optimal decision-making
    - Applies Jakob's Law to a concrete design decision
    - Describes how users form mental models and the implications for product design
    - Explains the difference between how users say they will use a product and how they actually use it
    - Proposes specific design strategies that work with rather than against natural user behaviour
  keywords:
    - satisficing
    - Jakob's Law
    - mental model
    - F-pattern
    - Z-pattern
    - scanning
    - reading
    - expectation
    - familiarity
    - observation
    - self-report
    - usability testing
    - behaviour
    - heuristic
  modelAnswer: |
    Users do not make optimal decisions — they satisfice. Herbert Simon's concept of satisficing describes how humans adopt the first solution that meets their threshold requirements, rather than exhaustively evaluating all options to find the best one. This has immediate design implications: users will use the first navigation item that looks plausible for their goal, not the most accurate one. They will click the first 'contact' link they see, even if it leads to a FAQ rather than a direct contact form. Design for first-plausible, not for optimal.

    Jakob's Law (Nielsen): users spend most of their time on other websites. They bring expectations formed elsewhere. When your checkout flow differs dramatically from Amazon's, users don't adapt to your better design — they experience friction from violated expectation. Familiarity reduces cognitive load; novelty increases it. Convention should only be broken when the benefit clearly outweighs the friction of relearning.

    Mental models are internal representations of how a system works. Users approach your product with pre-formed mental models (often from similar products). When your product's behaviour matches their mental model, usage is fast and low-effort. When it doesn't, users make errors, feel frustrated, and blame themselves or the product. Good design aligns with the dominant mental model of the user base — or explicitly teaches a new mental model where the difference provides value.

    Eye-tracking studies reveal scanning patterns (F-pattern for content-heavy pages, Z-pattern for sparse pages) that show users do not read interfaces — they scan. Design for scanning: headings carry weight, the first word of a paragraph is read more often than the fifth sentence, left-aligned content is read more often than right-aligned. These are not preferences — they are observations of actual behaviour.

    The gap between self-report and behaviour is one of the most important lessons from usability research: users say they want more information; they actually prefer less. Users say they read every section; eye tracking shows they scan headings. Design based on observation, not on what users say they do.
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      Your product team wants to introduce a novel navigation pattern: a radial menu instead of the standard sidebar. Users in surveys say they "love innovative interfaces." How do you evaluate whether this is a good idea, and what would you need to see from usability testing before shipping?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [Jakob's Law, mental model, familiarity, test, observation, survey, self-report, friction, expectation, task completion]
      rejectedFeedback: "Survey responses ('I love innovative interfaces') are self-reported preferences — not predictions of actual behaviour. Jakob's Law predicts that a radial menu, unfamiliar to most users, will create friction regardless of how appealing it sounds. What to look for in usability testing: (1) task completion rate and time-to-complete compared to standard navigation, (2) error rate (clicks on wrong items), (3) second-session performance (did users adapt, or does the pattern remain effortful?), (4) qualitative reports during testing vs post-hoc self-report. Ship only if testing shows the radial menu produces better task performance after a reasonable learning period — not just higher preference scores."
    hint: "What is the relationship between what users say they prefer and what they actually perform better with?"
    reflectionPrompt: "The history of 'revolutionary UI paradigms' that failed commercial products is long. Each seemed compelling in surveys; each encountered Jakob's Law in production. Where a novel pattern is worth shipping, the bar is measurably better task performance — not preference."

  - id: step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      User research for your enterprise SaaS shows that new users consistently fail to find the 'Export' feature — even when it's in the primary navigation. 60% of users who fail say afterwards "I looked everywhere." What does this tell you about their mental model, and how would you design a fix?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [mental model, expectation, label, location, contextual, primary, scan, where, looking, category]
      rejectedFeedback: "Users who 'looked everywhere' did not look everywhere — they looked where their mental model told them 'Export' would be. Their mental model places Export contextually (near the data being exported, or under a 'Data' or 'File' menu) rather than in primary navigation. The fix is alignment: (1) move Export to where users expect it — contextually near the data or under a recognisable category, (2) rename it to match how users describe the task ('Download', 'Save', 'Get data'), (3) add a secondary path (search, keyboard shortcut) that routes to the feature regardless of navigation placement. Don't explain where to find Export — put it where users' mental models say it should be."
    hint: "Where in the product did users look for Export? Not where you put it — where their model told them it would be. How do you close that gap?"
    reflectionPrompt: "The solution to navigation failures is almost never 'help users learn our structure' — it's 'align our structure with how users think.' The mental model is the user's; the interface must meet it."

  - id: step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Users scan, not read. Your product's onboarding email is 800 words. Your marketing team insists every word is necessary. Reframe the design problem and propose a solution that respects scanning behaviour while preserving the information the marketing team cares about.
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [scan, heading, hierarchy, F-pattern, progressive, short, bold, bullet, key, summary, call to action, most important]
      rejectedFeedback: "Scanners read: headings, bold text, the first 1-2 sentences of paragraphs, bullets, and calls to action. The rest of an 800-word email goes unread for most recipients. Solution: (1) restructure around headings that carry the key message (scanners read only the headings), (2) lead each paragraph with the key point (first sentence, not last), (3) bold the single most important phrase in each section, (4) one clear CTA per email, (5) a TL;DR summary at the top. The full 800 words can still exist for readers who engage — but the design must work for scanners, who are the majority."
    hint: "If a user reads only the headings and the first sentence of each paragraph, does your email still communicate the most important message? Redesign until it does."
    reflectionPrompt: "Designing for scanning is not dumbing down content — it's acknowledging how humans actually allocate attention. A well-scanned email with 800 words communicates more than an 800-word email that users abandon after the first paragraph."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A user is looking for the 'Settings' page. Your app uses 'Preferences' instead. They spend 90 seconds searching before giving up. What principle explains this?"
    options:
      - "The user has low product literacy"
      - "Label mismatch between the user's mental model vocabulary and the product's vocabulary"
      - "The settings page is in the wrong navigation level"
      - "90 seconds indicates engagement, not frustration"
    correctIndex: 1
    feedback: "Users search for features using their own vocabulary, not the product's. If 'Settings' is in their mental model and 'Preferences' is in the product, they will look for 'Settings' — and not find it, or take much longer. Card sorting and tree testing exercises reveal users' natural vocabulary for features. Align labels to user vocabulary; don't require users to learn yours."

  - type: MULTIPLE_CHOICE
    question: "Satisficing in UX means that users:"
    options:
      - "Are satisfied with the product and no longer need to make decisions"
      - "Choose the first option that meets their minimum requirements, rather than finding the optimal one"
      - "Prefer simple interfaces to complex ones"
      - "Make rational decisions based on all available information"
    correctIndex: 1
    feedback: "Herbert Simon's satisficing: users adopt the first solution that passes their satisfaction threshold — they don't exhaustively evaluate all options. In interface terms: users click the first navigation item that plausibly leads to their goal, not the most accurate one. Design for the plausible path, not the optimal path."

retrieval:
  recall: "Explain Jakob's Law. Give an example of when violating it would cost more than it's worth, and an example where violation might be justified."
  explain: "Why does self-reported user preference often diverge from actual user behaviour in usability testing?"
  mistakeId:
    code: |
      // Product decision
      "User surveys show 78% prefer our new two-column article layout.
       We're shipping it to all users."
    answer: "Survey preference and actual reading behaviour are different things. Users say they prefer two-column because it looks more 'magazine-like' or 'professional.' Eye-tracking studies consistently show that two-column layouts reduce reading comprehension and increase task time for text-heavy content — users must navigate column breaks and re-find their place. A correct decision process: A/B test the two-column layout against single-column on reading engagement metrics (scroll depth, time on page, article completion rate, return visits) — not preference surveys. Ship based on observed behaviour, not reported preference."
---

# Hook

Your feature team runs a survey. 73% of users say they want more information before making a decision. You add detailed explanations to every screen. Engagement drops. Users abandon faster.

Users told you what they wanted. Their behaviour told you something different.

Designing for how users actually behave — not how they say they behave — is the discipline of user behaviour psychology.

# Lore Introduction

*"The Guild surveyed apprentices: would you prefer longer or shorter assignments?"* the Training Master recalls. *"83% said longer — they wanted to feel they were getting more value. We gave them longer assignments. Completion rates dropped to 34%."*

She closes the survey report. *"We learned to observe. Apprentices who said 'longer' worked better with shorter assignments broken into clear milestones. The survey was not useless — it told us about their values. It did not tell us about their behaviour."*

# Core Learning

## Concept Introduction

**Key user behaviour principles:**

**Satisficing (Herbert Simon):** Users don't make optimal decisions — they adopt the first solution that meets their threshold. Design for the plausible path, not the optimal one.

**Jakob's Law (Nielsen):** Users form expectations from other products. Deviation from convention creates friction — even if your design is objectively better. Break conventions only when measurable benefit exceeds relearning cost.

**Mental Models:** Internal representations of how a system works. Users apply their existing model to your product. Gaps between model and reality cause errors and frustration. Design to match the dominant model, or explicitly teach a new one.

**Scanning patterns (F and Z):** Eye-tracking studies show users scan, not read. F-pattern for dense content (top bar first, then left column). Z-pattern for sparse pages (top-left to top-right, diagonal, bottom-left to bottom-right). Design for the scanner, not the reader.

**Self-report vs behaviour gap:** Users consistently misreport their own behaviour. "I read every section" is contradicted by eye tracking showing 15% of page viewed. Design based on observation, not self-report.

## Common Mistakes

- **Designing for the optimal user.** Real users satisfice, scan, and bring incorrect mental models. Designing for the optimal user produces products that work only for the optimal user.
- **Trusting survey results for design decisions.** Surveys reveal values and preferences — not behaviour. Test with observational methods.
- **Novel for novelty's sake.** Violating Jakob's Law has a real cost in user friction. The burden of proof is on the innovation, not the convention.
- **Ignoring mental models.** A feature that's easy to find for engineers (who know the product's architecture) is often hard to find for users (who have a domain-level mental model).

## Mini Summary

- Satisficing: users choose first-plausible, not optimal — design for the plausible path
- Jakob's Law: convention has value; break it only with evidence of superior task performance
- Mental models: design to match the dominant user model for the domain
- Users scan, not read — design for the scanner
- Observe behaviour; don't rely solely on self-report

# Guided Practice Quest

Work through the three guided steps applying behaviour principles to navigation design, feature discovery, and content design.

# Solo Practice Quest

Conduct a behaviour analysis of a product you use regularly. Choose one feature that you find difficult, confusing, or frequently misuse. Apply the behaviour principles from this lesson: (1) what mental model did you bring that conflicts with the product's design? (2) what satisficing behaviour does the design inadvertently encourage that leads to errors? (3) what convention does the design violate, and at what cost? (4) how would you redesign it based on these principles?

# Integration

The study of user behaviour in interface design connects to behavioural economics through the work of Kahneman and Tversky. System 1 (fast, automatic, heuristic) and System 2 (slow, deliberate, analytical) thinking are directly reflected in the satisficing vs optimising distinction. Users operating in System 1 mode — the default for routine product use — satisfice, follow habits, and apply familiar mental models. System 2 is engaged only when System 1 fails (a novel interface, an unexpected result, a confusing error). Good interface design keeps users in System 1 as much as possible by aligning with convention and expectation. System 2 engagement is reserved for the genuinely new — the interaction that's worth the cognitive cost of deliberate thought. Philosophically, this raises the question of paternalism in design: should designers reduce users to System 1 consumers of familiar patterns, or should good design occasionally provoke System 2 thinking? The pragmatic answer is contextual: routine tasks benefit from System 1 fluency; high-stakes decisions benefit from System 2 activation.

# Lore Conclusion

*"We now design assignments based on what we observe,"* the Training Master says. *"Short milestones. Clear structure. The type the apprentices actually complete. We ask them how they feel about it afterwards — and they say they prefer it. But we don't design based on their preferences. We design based on what works."*

---
