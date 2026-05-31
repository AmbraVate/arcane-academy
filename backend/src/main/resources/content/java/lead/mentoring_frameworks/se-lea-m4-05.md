---
id: se-lea-m4-05
school: engineering
domainId: java
tier: LEAD
moduleId: se-lea-m4
moduleTitle: "Module 4: Knowledge Transfer"
moduleGlyph: "📚"
moduleSortOrder: 4
topicSlug: mentoring_frameworks
topicTitle: "Mentoring Frameworks"
topicSortOrder: 5
lesson: mentoring_frameworks
title: "Mentoring Frameworks"
sortOrder: 5
difficulty: 4
estimatedMinutes: 38
xpReward: 75
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [technical_documentation]
integrationDomains: [psychology, education]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - "Distinguishes mentoring, coaching, and sponsorship with examples of each"
    - "Applies the GROW model to a specific engineering development conversation"
    - "Uses the SBI feedback model (Situation, Behaviour, Impact) in a concrete example"
    - "Describes situational leadership and how it changes approach by competence level"
    - "Identifies at least one way mentoring can go wrong (dependency, learned helplessness)"
  keywords: [grow, sbi, mentor, coach, sponsor, situational, feedback, competence, confidence, development]
  modelAnswer: |
    Mentoring vs coaching vs sponsorship:
    - Mentoring: sharing experience and guidance; mentee benefits from mentor's expertise
    - Coaching: helping the coachee find their own answers; coach uses questions not advice
    - Sponsorship: actively advocating for someone's advancement in public/leadership contexts
    
    GROW model for coaching:
    Goal: "What do you want to achieve in this conversation?"
    Reality: "Where are you now? What have you already tried?"
    Options: "What options do you have? What else could you try?"
    Will/Way Forward: "What will you actually do? By when?"
    
    SBI feedback:
    Situation: "In yesterday's architecture review..."
    Behaviour: "...when you dismissed the security concern without exploring it..."
    Impact: "...two senior engineers felt their input wasn't valued and one told me afterwards."
    
    Situational Leadership (Blanchard):
    D1 (low competence, high enthusiasm): Directing — task-specific instruction
    D2 (some competence, lower confidence): Coaching — directive with explanation
    D3 (good competence, variable confidence): Supporting — collaborative, non-directive
    D4 (high competence, high confidence): Delegating — full autonomy
guidedSteps:
  - id: mf-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A junior engineer comes to you and says: "I don't know how to approach this
      database optimisation problem." Which response exemplifies coaching
      (rather than mentoring or directing)?
    inputConfig:
      options:
        - "Here's how I would approach it: first check the query execution plan, then look at indexes..."
        - "What have you already tried? What do you think the most likely cause might be?"
        - "Look at the documentation for EXPLAIN ANALYZE and read it carefully."
        - "Let me pair with you and we'll solve it together."
    markingRule:
      matchMode: NORMALIZED
      accepted: ["What have you already tried? What do you think the most likely cause might be?"]
      rejectedFeedback: "Coaching uses questions to help the person find their own answer. 'What have you already tried?' and 'What do you think?' activate the coachee's own thinking rather than replacing it. Mentoring would share your experience ('here's how I'd approach it'). Directing would give explicit instructions."
    hint: "Coaching is about asking questions that activate the other person's thinking. Which response does that?"
    reflectionPrompt: "Coaching is often counterintuitively harder than mentoring. It requires resisting the urge to give the answer you already know. The payoff: the coachee builds genuine problem-solving capability rather than dependency on your knowledge."
  - id: mf-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Apply the SBI (Situation, Behaviour, Impact) feedback model to give feedback to a senior engineer who frequently interrupts junior engineers in design meetings. Write the complete SBI feedback statement.
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [situation, behaviour, impact, meeting, interrupt, junior, felt, noticed, observed, when, because]
      rejectedFeedback: "SBI example: 'In the design review on Tuesday (Situation), when you interrupted Alex three times before they'd finished their proposal (Behaviour), Alex told me afterwards they felt their design wasn't being taken seriously, and two other juniors mentioned they're now hesitant to speak in those meetings (Impact).' The power: observable facts (not judgment) + concrete effects (not speculation about intent)."
    hint: "Situation: specific context. Behaviour: observable action (not judgment). Impact: specific effect on people or outcomes."
    reflectionPrompt: "SBI works because it's separates the person from the behaviour and the behaviour from its impact. 'You disrespect junior engineers' (judgment) = defensiveness. 'When you interrupted Alex three times (behaviour), Alex felt unvalued (impact)' = opens a conversation about specific change."
  - id: mf-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe how you would apply Situational Leadership to a junior engineer at D1 level (high enthusiasm, low competence) versus a senior engineer at D3 level (good competence, variable confidence) who is struggling with a new technical area.
    inputConfig:
      minWords: 45
    markingRule:
      matchMode: CONTAINS
      accepted: [d1, direct, instruction, d3, support, confidence, autonomy, different, approach, level]
      rejectedFeedback: "D1 engineer: Directing style — specific task instruction, clear expectations, frequent check-ins. They need to know what to do and how; enthusiasm is high but they don't yet know enough to self-direct. D3 engineer: Supporting style — acknowledge their competence, explore what's undermining their confidence in this area, collaborative problem-solving rather than instruction. They have the skills; they need confidence restoration, not instruction."
    hint: "What does a D1 engineer need from you that is different from what a D3 engineer needs? Think about competence vs confidence for each."
    reflectionPrompt: "Mismatching the leadership style to the development level is a common mentoring failure. Directing a D4 engineer is demotivating (they're capable — micromanagement implies you don't trust them). Supporting a D1 engineer leaves them adrift (they genuinely need instruction, not just encouragement)."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the difference between sponsorship and mentoring?"
    options:
      - "Sponsorship is for executives; mentoring is for junior employees"
      - "A mentor provides guidance; a sponsor actively advocates for the person's advancement in public and with decision-makers"
      - "Sponsorship is formal; mentoring is informal"
      - "They are the same thing with different names"
    correctIndex: 1
    feedback: "Mentors talk *to* you. Sponsors talk *about* you — in rooms you're not in, to people who make decisions about promotions, high-profile projects, and leadership opportunities. Research (Hewlett, 2013) shows women and minorities are over-mentored and under-sponsored, explaining part of the leadership representation gap."
  - type: MULTIPLE_CHOICE
    question: "What is 'learned helplessness' in the context of mentoring, and how is it caused?"
    options:
      - "When a mentee refuses to learn new skills"
      - "When a mentee becomes dependent on the mentor for answers, losing confidence in their own problem-solving because the mentor always solves things for them"
      - "When a mentor loses motivation to continue"
      - "When a mentee fails repeatedly and gives up"
    correctIndex: 1
    feedback: "Learned helplessness in mentoring: if a mentor always answers questions directly (even when the mentee could figure it out), the mentee gradually stops trying to solve problems independently. They've learned that asking the mentor is faster and more reliable. The fix: coaching questions that activate the mentee's thinking, and letting them struggle appropriately."

retrieval:
  recall: "What are the four stages of the GROW coaching model? What distinguishes coaching from mentoring?"
  explain: "Explain to a new engineering lead why they should use coaching conversations rather than just answering every question their team members bring to them."
  mistakeId:
    code: |
      // Mentoring conversation:
      Mentee: "I'm struggling with designing the API for this feature."
      Mentor: "Here's what I would do: use REST with these specific endpoints,
               use this naming convention, handle errors like this..."
               [30-minute detailed explanation]
      Mentee: "Great, thanks!"
      
      // Two weeks later, mentee asks the same type of question again.
    answer: "The mentor has answered the question but not built the mentee's capability to answer it themselves. The mentee has learned to ask the mentor, not to design APIs. Better approach: coach first — 'What have you considered so far? What are the trade-offs you see?' Share your experience only after they've articulated their own thinking. This builds mental models, not dependency."
---

# Hook

You're a senior engineer and your team comes to you with every question. You're always the bottleneck. Your answers are good — but three months later, the same questions come back.

You haven't been mentoring. You've been answering. These are different things.

Effective mentoring builds capability, not dependency. It requires different skills than being technically excellent — and different skills than just being nice. It's a craft, and like all crafts, it can be learned.

> Think about someone who significantly influenced your professional development. What did they do that made the difference? Was it advice, questions, or something else?

# Lore Introduction

The Guild of Mentors is the smallest and most selective guild in the Academy. Entry requires not mastery of enchantment — that is assumed — but mastery of teaching. The ability to see where an apprentice's mental model is incomplete. The discipline to ask questions when you already know the answer. The wisdom to let someone struggle productively rather than solve for them.

*"The most dangerous mentor,"* Archmage Veylan says, *"is the one who makes their apprentice feel helped while actually making them helpless."*

# Core Learning

## Concept Introduction

**Three distinct development relationships:**

| Relationship | Core mechanism | Your role |
|-------------|----------------|----------|
| **Mentoring** | Sharing experience and expertise | Adviser with relevant experience |
| **Coaching** | Activating the coachee's own thinking | Question-asker, not answer-giver |
| **Sponsorship** | Advocating publicly for advancement | Champion in rooms they're not in |

**GROW Model (Whitmore, 1992):**
A coaching conversation structure:
- **G**oal — what do you want from this conversation?
- **R**eality — where are you now? what have you tried?
- **O**ptions — what could you do? what else?
- **W**ill/Way Forward — what will you actually do? by when?

**SBI Feedback (Center for Creative Leadership):**
- **S**ituation — specific context
- **B**ehaviour — observable action (not judgment or intent)
- **I**mpact — specific effect on people or outcomes

## Why It Matters

Engineering leaders who develop others multiply their impact across the team. A leader who is the answer to every question creates:
- A personal bottleneck in every decision
- Engineers without problem-solving capability
- A team that regresses when the leader is unavailable

A leader who develops others creates:
- Engineers who grow independent capability
- Distributed decision-making
- A team that improves even when the leader isn't present

## Worked Examples

**GROW coaching conversation:**
```
Engineer: "I'm not sure how to structure the service layer for this feature."

Mentor (GOAL): "What would be most useful to explore in our time today?"
Engineer: "I want to figure out where the business logic should live."

Mentor (REALITY): "What have you already considered? What feels uncertain?"
Engineer: "I'm torn between putting it in the service or the domain objects."

Mentor (OPTIONS): "What are the trade-offs you see with each? 
                   What other approaches have you used in the past?"
Engineer: [articulates trade-offs] "I think the domain objects approach is cleaner..."

Mentor (WILL): "So what will you try? How will you know if it's working?"
```

**SBI feedback example:**
```
Situation:  "In the architecture review on Thursday..."
Behaviour:  "...when you moved on from the database schema discussion before 
             Maya had finished her concern about the foreign key design..."
Impact:     "...we shipped an indexing approach that caused the performance issue
             we found in testing last week. And Maya told me she hesitated to
             raise it again because she felt dismissed."
```

**Situational Leadership (Blanchard):**
```
D1: Low competence, High enthusiasm → Directing: task-specific instruction, close oversight
D2: Growing competence, Lower confidence → Coaching: directive + explanation, build understanding
D3: Good competence, Variable confidence → Supporting: collaborative, confidence-building
D4: High competence, High commitment → Delegating: full autonomy, results-focused
```

## Common Mistakes

- **Always answering questions** — creates dependency; use coaching questions first.
- **Using the same style for everyone** — D1 engineers need direction; D4 engineers need autonomy.
- **Feedback on character not behaviour** — "you're dismissive" vs "when you interrupted Alex."
- **Mentoring without sponsorship** — talking to someone without advocating for them is incomplete development.
- **Missing the struggle threshold** — too much support prevents growth; too little is abandonment.

## Mental Model

Mentoring is **teaching someone to fish vs giving them a fish**. Coaching is even further: asking them what they know about fish, where they think fish might be, what they've tried, and what they'll do next. The best mentors use all three modes (mentoring, coaching, directing) at the right moment — and know that the goal is always to need themselves less over time.

## Mini Summary

- ✔ Mentoring, coaching, and sponsorship are distinct — each serves a different developmental need
- ✔ GROW model: Goal → Reality → Options → Will — structures a coaching conversation
- ✔ SBI feedback: Situation + Behaviour + Impact — specific, observable, not judgmental
- ✔ Situational Leadership: match your style to the engineer's development level (D1-D4)
- ✔ Effective mentoring builds independence, not dependency

# Guided Practice Quest

**The Master's Craft**

Apply GROW, SBI, and Situational Leadership to three different mentoring scenarios. Choose the right tool for each context.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You are a lead engineer with three direct reports:

**Alex** (junior, 6 months): High enthusiasm, learning quickly but makes frequent design mistakes from over-confidence. Asks questions constantly but sometimes doesn't try to solve problems independently first.

**Sam** (mid-level, 3 years): Technically excellent but consistently underestimates their own capabilities. Frequently says "I'm not sure I should make this decision" about decisions well within their competence.

**Jordan** (senior, 7 years): Highly capable, autonomous, and ready for a staff engineer role. Frustrated with the current team's technical direction but hasn't been vocal about it in the right forums.

For each person:
1. What is their Situational Leadership level (D1-D4) and what leadership style does that require?
2. Write a GROW coaching conversation opening (just the Goal and Reality questions) for the most important thing to address with them
3. What sponsorship action could you take for any of them this month?
4. What is the greatest risk of mentoring each person incorrectly (the specific failure mode for each)?

# Integration

**Connecting to Psychology — Self-Determination Theory**

Edward Deci and Richard Ryan's Self-Determination Theory (1985) identifies three basic psychological needs that drive intrinsic motivation: autonomy (feeling in control of one's actions), competence (feeling effective and growing), and relatedness (feeling connected to others). When these needs are met, people are intrinsically motivated; when they're frustrated, motivation becomes external and fragile.

Mentoring is most effective when it satisfies all three. Coaching-style conversations support autonomy (the coachee makes their own decisions). Building genuine competence (not just telling people what to do) satisfies the competence need. The mentoring relationship itself satisfies relatedness.

The implications are practical: tell someone what to do → they're competent only while you're there, dependent on your instruction. Coach them to discover the answer themselves → they experience autonomy and competence, reinforcing intrinsic motivation to continue developing. The effect compounds: intrinsically motivated engineers seek learning opportunities independently.

The flip side: micromanagement systematically frustrates autonomy. Over-helping frustrates competence. Transactional relationships frustrate relatedness. All three are common management behaviours that, through the SDT lens, explain why so many engineers describe their managers as "blocking their growth."

How does understanding Self-Determination Theory change the way you would structure your regular 1-on-1s?

# Lore Conclusion

The apprentice completes the enchantment. The mentor watches, smiles, says nothing.

*"The measure of a mentor,"* Archmage Veylan says, *"is not how much the apprentice learned from them. It is how much the apprentice can now learn without them. We are not the destination. We are the bridge."*

Build engineers who surpass you. That is the highest achievement.
---
