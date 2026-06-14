---
id: fe-lea-m3-04
school: engineering
domainId: frontend-engineering
tier: LEAD
moduleId: fe-lea-m3
moduleTitle: "Module 3: UX Psychology"
moduleGlyph: "🧠"
moduleSortOrder: 3
topicSlug: behavioural_design
topicTitle: "Behavioural Design"
topicSortOrder: 4
lesson: behavioural_design
title: "Behavioural Design"
sortOrder: 4
difficulty: 4
estimatedMinutes: 40
xpReward: 150
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-lea-m3-03]
integrationDomains: [psychology, philosophy, ethics]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - Correctly applies the Fogg Behaviour Model (Motivation × Ability × Prompt = Behaviour)
    - Uses the Hook Model (Trigger → Action → Variable Reward → Investment) to analyse a product flow
    - Distinguishes between designing to help users build beneficial habits and designing to create compulsion
    - Identifies at least two specific dark patterns and explains the psychological mechanism each exploits
    - Proposes an ethical framework for evaluating behavioural design decisions in a product context
  keywords:
    - Fogg Behaviour Model
    - Hook Model
    - motivation
    - ability
    - trigger
    - variable reward
    - habit loop
    - compulsion
    - engagement
    - retention
    - dark pattern
    - ethical design
    - autonomy
    - persuasion
    - manipulation
  modelAnswer: |
    Fogg's Behaviour Model states that a behaviour occurs when Motivation, Ability, and a Prompt converge at the right moment. If motivation is low, ability must be very high (frictionless action) for behaviour to occur. If ability is low (requires effort), motivation must be high. A well-timed prompt at the intersection of sufficient motivation and ability triggers behaviour.

    Applied to onboarding: a user who just signed up has high motivation (they just committed to the product). This is the moment to ask for the highest-effort setup action. Asking them to configure integrations three weeks later, when motivation has decayed, produces much lower completion.

    The Hook Model (Nir Eyal) describes how habits form: an external or internal trigger prompts an action; the action delivers a variable reward (unpredictable positive outcome); the user invests (data, preferences, social connections) which raises the cost of leaving and triggers future internal triggers. This is how email, social feeds, and notifications create habitual use.

    The ethical divide: the Hook Model describes how habits form — it is neutral. Its application determines ethics. Designing a to-do app that creates a daily check-in habit helps users achieve their stated goals. Designing a social feed with infinite scroll and unpredictable content rewards creates compulsion that serves the platform's engagement metrics at the user's expense.

    The test (Eyal's own framework): is the designer willing to use the product themselves, in the way they are designing it? Does it help users do what they already want to do, or does it create wants that serve only the platform? Dark patterns (fake urgency, hidden costs, roach motels) are behavioural design in service of the product against the user.

    An ethical framework: behavioural design is justified when it (1) helps users achieve goals they have stated or would endorse on reflection, (2) is transparent enough that users would not feel manipulated if they understood the mechanism, and (3) preserves the user's ability to easily disengage.
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      A fitness app wants to increase daily workout logging. Using Fogg's Behaviour Model (Motivation × Ability × Prompt), design three specific interventions — one that increases motivation, one that increases ability (reduces friction), and one that improves prompt timing. For each, explain the psychological mechanism.
    inputConfig:
      minWords: 60
    markingRule:
      matchMode: CONTAINS
      accepted: [motivation, ability, prompt, trigger, friction, reduce, goal, streak, reminder, time]
      rejectedFeedback: "Motivation: introduce workout streaks with social visibility ('You've logged 7 days in a row — 3 friends are watching your streak'). Streaks leverage loss aversion (fear of breaking the streak) and social proof. The motivation is not intrinsic (fitness) but extrinsic (social performance) — which can sustain behaviour but may not survive streak breaks. Ability: implement a one-tap 'Quick Log' from the notification itself — users log a workout without opening the app, selecting a type, entering duration, etc. Reducing the action to a single tap massively increases conversion at any motivation level. Prompt: send the workout reminder 15 minutes before the user's historical workout window (derived from past data) on days when they haven't logged yet. The prompt arrives when motivation is naturally high (they usually work out at this time) and ability is available (they're in their workout window). Prompt timing is the most commonly misused lever — generic 9am reminders arrive when motivation and ability are not aligned."
    hint: "Think of each lever independently: what raises the internal desire? What removes barriers to the action? When is the best moment to remind?"
    reflectionPrompt: "The Fogg model shows that even highly motivated users fail to act if ability is low or the prompt is poorly timed. All three must converge."

  - id: step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Analyse Instagram's infinite scroll feed using the Hook Model (Trigger → Action → Variable Reward → Investment). Then identify whether this design serves users, serves the platform, or both — and what design change would shift the balance towards users.
    inputConfig:
      minWords: 60
    markingRule:
      matchMode: CONTAINS
      accepted: [trigger, action, reward, variable, investment, scroll, data, habit, internal, external, engagement, serve]
      rejectedFeedback: "Trigger: external (notification 'X liked your photo') → internal (boredom, social anxiety, FOMO). Action: open app, scroll. Variable Reward: unpredictable mix of interesting content, social validation (likes, comments), and low-value filler — the unpredictability is essential (fixed rewards create satiation; variable rewards create compulsion). Investment: follow history, posted content, social connections, DM history — all make leaving costly and create internal triggers ('I wonder if anyone commented'). The platform benefits: every scroll, every minute, every return visit is an ad impression. This design optimises for time-on-app and return frequency — metrics that correlate with ad revenue but not with user wellbeing or stated goals. What would shift the balance to users: (1) replace infinite scroll with a session end ('You're caught up — check back tomorrow'), (2) replace algorithmic feed with chronological (reduces anxiety-inducing unpredictability), (3) weekly screen time report surfaced in the app. Instagram has introduced some of these — but they remain opt-in and non-prominent, which tells you something about who the design primarily serves."
    hint: "The variable reward is the psychological engine. Ask: what makes the reward unpredictable, and who chose that unpredictability?"
    reflectionPrompt: "The Hook Model is not inherently unethical — it describes how habits form. Eyal's own test: are you building something you are proud to have users be hooked on?"

  - id: step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Your product team proposes adding a 'Last X items remaining' label to products on your e-commerce platform. The label would be shown when inventory drops below 10 — but also automatically shown for new products regardless of stock. What is the psychological mechanism? What is the ethical verdict? Propose an alternative that achieves legitimate urgency without false scarcity.
    inputConfig:
      minWords: 40
    markingRule:
      matchMode: CONTAINS
      accepted: [scarcity, urgency, false, manipulate, honest, real, stock, trust, ethical]
      rejectedFeedback: "Psychological mechanism: scarcity heuristic — rare or limited things are perceived as more valuable, and fear of missing out activates loss aversion. Real scarcity ('3 left in stock' when true) is informative and helps users make timely decisions. Fake scarcity ('Last 10 remaining' shown for items with 500 in warehouse) is a dark pattern — it exploits the scarcity heuristic to create false urgency. This is deceptive by definition and erodes trust when discovered. Ethical verdict: showing real stock counts is legitimate and helpful. Fabricating scarcity is deceptive. Legitimate alternative: only show stock indicators when genuinely low (≤5 items), with the actual count ('4 remaining'). Show 'Popular — selling fast' based on actual velocity data. Show 'Back in stock today' when an item was genuinely sold out. These create accurate urgency from real signals — not manufactured pressure from false ones."
    hint: "The scarcity heuristic is real and powerful. The question is whether you are informing the user of real scarcity or fabricating it."
    reflectionPrompt: "The short-term conversion lift from fake scarcity is real. So is the long-term trust erosion when users discover they were misled. Dark patterns often win the A/B test and lose the product."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In Fogg's Behaviour Model, behaviour occurs when:"
    options:
      - "The product is well-designed and the user is intelligent"
      - "Motivation, Ability, and a Prompt converge at the same moment"
      - "The user has been repeatedly reminded to complete an action"
      - "The user's motivation is high enough to overcome any friction"
    correctIndex: 1
    tier: RECALL
    feedback: "Fogg's formula: B = MAP — Behaviour happens when Motivation, Ability (low friction), and a Prompt all align at the right moment. High motivation cannot overcome extremely low ability (too much friction); a brilliant design fails without the right prompt at the right time. All three must converge."

  - type: MULTIPLE_CHOICE
    question: "What makes variable rewards more compelling than fixed rewards in habit formation?"
    options:
      - "Variable rewards are always larger than fixed rewards"
      - "Unpredictability activates dopaminergic anticipation, creating stronger drive to repeat the action"
      - "Users prefer variety and become bored with consistent outcomes"
      - "Variable rewards are easier to implement in software"
    correctIndex: 1
    tier: APPLICATION
    feedback: "Neuroscience research shows that dopamine release peaks in anticipation of a possible reward, not on receiving a certain one. Variable-ratio reinforcement (unpredictable reward per action) creates stronger and more persistent behaviour than fixed-ratio or fixed-interval schedules. This is why slot machines and social media feeds are so compelling — the next scroll might contain something extraordinary, or might not. The uncertainty drives the action."

retrieval:
  recall: "Name the four stages of the Hook Model and give one example from a product you use regularly."
  explain: "A product manager says 'we're designing for engagement, not manipulation.' What questions would you ask to determine whether the behavioural design features in their product serve users or exploit them?"
  mistakeId:
    code: |
      // Onboarding push notification strategy
      "We send 3 onboarding notifications per day for the first 14 days.
       Notification 1 (9am): 'Don't forget to complete your profile!'
       Notification 2 (1pm): 'Your peers are already using Feature X.'
       Notification 3 (6pm): 'You have unfinished tasks waiting.'
       All notifications link to the home screen."
    answer: "Multiple problems: (1) Three notifications per day for 14 days is 42 notifications in 14 days — volume at this rate signals spam rather than helpfulness and drives notification permission revocation. (2) 'You have unfinished tasks waiting' is false urgency if no specific tasks exist — this is a dark pattern. (3) Fixed time notifications (9am, 1pm, 6pm) ignore individual user schedules — a better prompt arrives based on actual usage patterns. (4) All notifications link to the home screen — a notification about profile completion should link to profile setup, not the home screen. Redesign: send one contextually relevant notification per day maximum, at the time the user has been active previously, linking directly to the feature mentioned, with content that is true. After 3 days of non-engagement, pause and send a single re-engagement message rather than escalating volume."
---

# Hook

Your product's DAU/MAU ratio is 0.3. Users sign up, come back a few times, then disappear. The feature is genuinely useful — but it hasn't become a habit.

Meanwhile, a competitor with an inferior product has a DAU/MAU of 0.7.

Understanding why behaviour becomes habitual — and how to design for it responsibly — is one of the highest-leverage skills in frontend leadership.

# Lore Introduction

*"The Academy's bell rings at dawn,"* the senior instructor tells the new cohort. *"Not because dawn is the best time to study. Because after three weeks, wizards wake before the bell — drawn by the habit the bell established."*

*"The bell is a trigger. The session is the action. Mastery — variable, unpredictable, some days profound and some days frustrating — is the reward. And the investment: the notes taken, the spells half-learned, the relationships with instructors and peers. These investments make the habit sticky."*

*"You are learning to design bells,"* she says. *"The question is what you are training people to do — and whether they would thank you for it."*

# Core Learning

## Concept Introduction

### Fogg's Behaviour Model

**B = MAP:** Behaviour = Motivation × Ability × Prompt

A behaviour occurs when all three converge at the right moment:

| Element | Low → No Behaviour | High → Behaviour possible |
|---|---|---|
| **Motivation** | No desire to act | Strong desire to act |
| **Ability** | Action is difficult / many steps | Action is effortless / one tap |
| **Prompt** | Arrives at wrong moment | Arrives when M and A are sufficient |

**Design application:** When launching a feature, identify which element is the bottleneck. If users understand the value but don't act — ability (friction) is the problem. If users try but abandon — motivation (perceived value) is the problem. If users forgot — prompt (timing and salience) is the problem.

### The Hook Model (Nir Eyal)

```
External Trigger → Action → Variable Reward → Investment → Internal Trigger → ...
```

**Trigger:** External (notification, email) or internal (emotion: boredom, FOMO, anxiety). Habit-forming products shift users from external to internal triggers.

**Action:** The simplest behaviour possible in anticipation of reward. Lower friction = higher completion.

**Variable Reward:** Unpredictable positive outcome. Three types: Tribe (social validation), Hunt (information search), Self (mastery/achievement). Variability — not size — creates compulsion.

**Investment:** User adds value to the product (data, content, relationships, preferences) — raising the cost of leaving and generating the next trigger.

### The Ethical Boundary

| Ethical Behavioural Design | Dark Pattern |
|---|---|
| Helps users achieve goals they would endorse on reflection | Creates compulsion that serves platform at user expense |
| Uses real data to create accurate urgency | Fabricates scarcity, urgency, or social proof |
| Prompts arrive when they help, not just when they drive metrics | Notification volume maximises opens, not user wellbeing |
| Users can easily disengage and feel good about their usage | Disengagement is obscured or made difficult |

**Eyal's test:** Would you be comfortable if your users fully understood the psychological mechanisms you have designed? Would you be proud for your own children to use the product the way you have designed it?

## Why It Matters

Behavioural design is where interface decisions stop being aesthetic and start being ethical — every default, nudge, and friction point steers real human behaviour at scale:

- Defaults are destiny: opt-out organ donation countries have donor rates several times higher than opt-in ones; your settings defaults carry the same gravity over what users share, spend, and consent to
- The same toolkit builds both helpful nudges (savings prompts, safe defaults) and dark patterns (confirm-shaming, roach-motel subscriptions) — the technique is neutral, the application is not, and regulators increasingly treat manipulative patterns as enforcement targets
- Leads set the line: a team will ship whatever converts unless someone with authority defines which persuasion techniques the product will not use

Understanding behavioural mechanics is no longer optional at lead level — you either design behaviour deliberately and accountably, or you design it accidentally and find out from a journalist.

## Common Mistakes

- **Optimising for engagement metrics instead of user outcomes.** High DAU and long session times can mask products that are compelling but unhelpful.
- **Generic notifications at fixed times.** Prompts that don't align with individual user patterns and contexts are friction, not help.
- **Treating variable rewards as always good.** Variable rewards create compulsion — which is appropriate for some products (games, social) and inappropriate for others (banking, health tools that should be reliable and predictable).

## Mental Model

Think of behavioural design as urban planning for decisions. A city planner never forces anyone anywhere, yet placement decides behaviour: the pedestrian bridge people actually use because it's on their desire line, the staircase nobody takes because the escalator is nearer the door. Interfaces are decision-cities — defaults are the paved path (most travelled by far), friction is distance and stairs, prompts are signage. The planner's ethics translate directly: good planning makes the *beneficial* route the easy one (the crosswalk where people cross anyway); hostile architecture makes the route the *operator* prefers easy and everything else exhausting (the cancellation buried four menus deep). Audit your product like a planner walking the city: where do the paved paths lead, and who benefits at each destination?

## Mini Summary

- B = MAP: Behaviour requires Motivation, Ability, and Prompt to converge
- The Hook Model describes how external triggers become internal habits through variable rewards and investment
- Variable rewards create compulsion — which can serve users (mastery, social connection) or exploit them (infinite scroll)
- Ethical behavioural design: helps users achieve goals they endorse, is transparent in its mechanism, preserves the ability to disengage

# Guided Practice Quest

Apply Fogg to a fitness app, analyse the Instagram Hook loop, and evaluate a scarcity dark pattern.

# Solo Practice Quest

You are leading the design of a new productivity tool. The core feature is a daily focus timer. Define the full behavioural design strategy: (1) the Hook loop that creates daily habit, (2) specific Fogg interventions for the onboarding week, (3) the trigger strategy (external and internal), (4) how you will measure whether the product is creating healthy habits vs unhealthy compulsion, and (5) the ethical constraints you will apply to your notification and engagement design.

# Integration

The neuroscience of habit formation (Duhigg's Habit Loop: Cue → Routine → Reward; corresponding to Fogg and Eyal's frameworks) is grounded in basal ganglia function — the brain region responsible for procedural memory and automatic behaviour. When a habit is established, the cortex disengages and the basal ganglia drives the routine — this is why habits are energy-efficient and persistent, and why breaking them requires conscious cortical re-engagement. The philosophical implication is significant: a designer who creates a habit is, in a meaningful sense, programming behaviour at a level below conscious deliberation. This is not inherently unethical — habits are how humans function — but it creates a responsibility proportional to the power. The engineering corollary: the same design decisions that make a product habitual for users who benefit also make it habitual for users who do not. The mechanism does not discriminate. Ethical behavioural design therefore requires knowing who your users are and whether the habit you are building serves them — not just whether it serves your retention metrics.

# Lore Conclusion

*"The bell rings at dawn,"* the instructor repeats, looking at the cohort three months later — more alert, more practised, more capable. *"You wake before it now. You built the habit."*

*"Consider: we could have built a different bell. One that rang whenever we wanted more students in the hall. One that promised rewards that never came. One that made it hard to leave when you wanted to study elsewhere."*

*"We did not. Because the habit we wanted to create — the habit of genuine learning — required that the bell be trustworthy."*

*"Design bells that deserve to be answered."*

---
