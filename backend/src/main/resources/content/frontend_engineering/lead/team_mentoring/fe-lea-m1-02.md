---
id: fe-lea-m1-02
school: engineering
domainId: frontend-engineering
tier: LEAD
moduleId: fe-lea-m1
moduleTitle: "Module 1: Frontend Leadership"
moduleGlyph: "👑"
moduleSortOrder: 1
topicSlug: team_mentoring
topicTitle: "Team Mentoring"
topicSortOrder: 2
lesson: team_mentoring
title: "Team Mentoring"
sortOrder: 2
difficulty: 4
estimatedMinutes: 40
xpReward: 150
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [fe-lea-m1-01]
integrationDomains: [psychology, sociology, philosophy]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - "Distinguishes between mentoring at different levels (junior/mid/senior) with concrete differences in approach"
    - "Demonstrates understanding of the Socratic method in code review — asking questions that develop reasoning rather than providing answers"
    - "Distinguishes scaffolding (providing structure) from enablement (removing constraints) and knows when each applies"
    - "Addresses how to identify and grow potential future technical leaders"
    - "Identifies at least two concrete mentoring anti-patterns and explains the harm they cause"
  keywords:
    - scaffolding
    - enablement
    - Socratic
    - growth
    - autonomy
    - coaching
    - potential
    - anti-pattern
  modelAnswer: |
    Mentoring is not a single thing — it is a practice that must adapt to where the engineer is in their journey. A junior engineer needs scaffolding: clear expectations, frequent feedback, and safe problems that stretch without overwhelming. A mid-level engineer needs a different challenge — not more hand-holding but bigger problems, visibility with stakeholders, and the space to make mistakes with someone nearby who can catch the fall. A senior engineer being prepared for leadership needs something else entirely: exposure to ambiguity, conversations about organisational dynamics, and deliberate responsibility for outcomes rather than tasks.

    The Socratic method in code review means replacing "you should do X" with "what happens if this list is empty?" or "how does this scale if we have a thousand concurrent users?" The goal is to develop the engineer's reasoning process, not just fix the current code. Engineers who receive Socratic reviews build pattern recognition; those who receive directive reviews learn to wait for answers.

    Scaffolding is appropriate when an engineer lacks the knowledge or confidence to proceed independently — you provide structure (templates, pairing, checkpoints) so they can make progress. Enablement is appropriate when an engineer has the capability but lacks the opportunity or permission — you remove blockers, delegate ownership, and then get out of the way. Knowing which mode is needed requires observation; providing scaffolding to an engineer who needs enablement is infantilising, and providing enablement to one who needs scaffolding is abandonment.

    Growing the next technical leaders requires intentional exposure. Put promising senior engineers in cross-functional conversations. Assign them to architectural decisions where their input genuinely matters. Debrief with them afterward. The path from senior to lead is not longer experience — it is different experience.

    The two most common mentoring anti-patterns are the Answer Machine (always providing solutions, creating dependency) and the Phantom Mentor (saying "my door is always open" but never proactively investing). Both result in stagnant engineers who do not grow into the leads the organisation needs.
guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: SHORT_TEXT
    instruction: |
      You are reviewing a PR from a mid-level engineer. The code works correctly but uses a pattern that will be difficult to extend — there is a clearly better architectural approach. You have two options: leave a comment explaining the better pattern, or ask a question that leads the engineer to discover it themselves.

      Write the Socratic comment you would leave on this PR. Then explain your reasoning: why this approach over the directive one? What are the risks of the Socratic approach, and when would you switch to being directive instead?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [question, reasoning, discover, directive, time, context, learning]
      rejectedFeedback: "A strong answer includes an actual Socratic question and a nuanced view of when the approach is appropriate. Consider: is Socratic review always better? What does it cost?"
    hint: "Think about what the engineer will learn from each approach. What do you want them to be able to do next time, without you?"
    reflectionPrompt: "How often do you give directives when a question would serve better? What makes questions feel risky in code review?"
  - id: step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      A junior engineer on your team is talented but consistently underestimates the complexity of tasks, which leads to missed commitments and visible stress. You observe that they take on work enthusiastically but struggle to ask for help until a problem is already serious.

      Design a brief mentoring plan for this engineer over the next six weeks. What specific interventions would you put in place? How would you build the psychological safety needed for them to surface problems earlier?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [check-in, safety, estimation, debrief, trust, scaffolding, pattern, early]
      rejectedFeedback: "Consider both the technical skill (estimation, scope management) and the behavioural pattern (reluctance to escalate). A good mentoring plan addresses both dimensions."
    hint: "What does this engineer need to believe about how they will be received when they say 'I'm stuck'? How do you build that belief?"
    reflectionPrompt: "What in your own experience of asking for help would inform your approach here?"
  - id: step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      You have identified a senior engineer on your team who has the raw material to become a technical lead within 18 months — strong technical instincts, good relationships, genuine curiosity. But they are comfortable in their current role and have not expressed ambition for leadership.

      How do you approach this? Do you actively cultivate their path to leadership or wait for them to ask? What experiences would you intentionally create? What conversation would you have, and when?
    inputConfig:
      minWords: 50
    markingRule:
      matchMode: CONTAINS
      accepted: [conversation, potential, exposure, ownership, ambition, ask, respect, development]
      rejectedFeedback: "Strong responses engage with both the opportunity (cultivating future leadership) and the respect dimension (not projecting your ambitions onto someone else). Consider: how do you open the door without pushing someone through it?"
    hint: "What does it mean to offer someone a vision of themselves they haven't yet imagined? What is your responsibility to do this, and what is their right to decline?"
    reflectionPrompt: "Who invested in your potential before you saw it yourself? What did they do that mattered?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A senior engineer consistently receives prescriptive code review comments like 'change this to X'. What is the most significant long-term cost of this approach?"
    options:
      - "The engineer will become resentful and leave the team"
      - "The engineer's reasoning ability does not develop — they learn answers, not thinking"
      - "The code quality will decrease because engineers stop trying"
      - "Review cycles will take too long and slow down delivery"
    correctIndex: 1
    feedback: "Directive code review produces the right output in the short term but stunts the reviewer's development as a thinking engineer. The Socratic approach builds capability that outlasts any single review."
  - type: MULTIPLE_CHOICE
    question: "The difference between scaffolding and enablement in mentoring is best described as:"
    options:
      - "Scaffolding is for junior engineers; enablement is for senior engineers"
      - "Scaffolding provides structure for those who lack knowledge; enablement removes blockers for those who have capability but lack opportunity"
      - "Scaffolding is temporary support; enablement is permanent empowerment"
      - "Scaffolding focuses on technical skills; enablement focuses on soft skills"
    correctIndex: 1
    feedback: "The mode required depends on the specific gap — knowledge vs opportunity. Applying scaffolding to someone who is capable but constrained is condescending; applying enablement to someone who is lost is abandonment."
retrieval:
  recall: "What is the Socratic method in code review, and why might it be more valuable than directive feedback in the long term?"
  explain: "Describe two mentoring anti-patterns that are common among technical leads, and explain the mechanism by which each one limits engineer growth."
  mistakeId:
    code: |
      Anti-pattern: The Answer Machine
      A frontend lead is available to every engineer at any time, always provides the correct answer
      immediately, prides themselves on never blocking anyone, and measures their mentoring success
      by how quickly they resolve their team's questions.
    answer: "The Answer Machine creates dependency rather than capability. Engineers learn to wait for the lead rather than developing their own problem-solving processes. The lead feels valued but is actually preventing growth. Good mentoring sometimes means sitting with the engineer while they find the answer themselves — which is slower in the moment and faster across a career."
---

# Hook

She sends me the same kind of Slack message for the third week in a row: "Hey, quick question — what's the best way to handle this state update?" She's talented. She learns fast. Her code is clean. And yet every time there's a decision point, she checks with me before proceeding.

I realise, with some discomfort, that I have created this. I have been so eager to be helpful, so quick with the right answer, that I have inadvertently built a dependency. She is not growing because I am always in the way.

This is the hardest lesson in mentoring: the most helpful thing you can do is sometimes the thing that feels least helpful in the moment.

# Lore Introduction

The great Archmage Valdur was known not for the power of his own spells but for the quality of his graduates. Of the twelve members of the current Grand Council, seven studied under Valdur. When asked his secret, he said only: "I never answered a question I could turn into a better question."

His former students describe a teacher who would sit in silence for thirty seconds after a student posed a problem — not because he did not know the answer, but because he was crafting the question that would lead the student to it themselves. Some found it maddening. All of them became masters.

This is the Arcane Academy's tradition of mentoring: not the transfer of knowledge, but the cultivation of wisdom.

# Core Learning

## Concept Introduction

Mentoring is the practice of deliberately investing in another person's growth. In a technical context it requires distinguishing between three different relationships with knowledge: giving answers (efficient but dependency-creating), asking questions (slower but capability-building), and creating experiences (the most powerful but most effortful form).

Effective mentoring adapts to the engineer's level and specific developmental need:

**Junior engineers** need scaffolding: clear expectations, frequent feedback loops, safe problems that stretch without overwhelming. They are building foundational mental models and benefit from structure. The risk is over-scaffolding — providing so much support that they never develop independent problem-solving.

**Mid-level engineers** need increasing autonomy and bigger problems. They have the technical basics; what they need is exposure to ambiguity, cross-team work, and ownership of outcomes. The risk is under-investing — assuming they are "sorted" and leaving them without challenge or feedback.

**Senior engineers approaching leadership** need a fundamentally different kind of development: organisational visibility, experience with ambiguous priorities, and deliberate coaching on the human dimensions of technical work. They need to understand that their next growth is not more technical depth — it is breadth and influence.

## Why It Matters

The leverage of mentoring is asymmetric. One hour of good mentoring, applied at the right moment, can change the trajectory of an engineer's career. The aggregate effect across a team compounds over years.

Organisations that systematically develop their engineers create a self-reinforcing advantage: better engineers attract better candidates, reduce attrition, and generate more innovation. Technical leads who mentor well are investing in organisational resilience — the team's capability does not disappear when any one person leaves.

## Worked Examples

**Example 1: The Socratic code review**
PR comment on a naive nested loop: "This works for our current dataset size. What happens to the behaviour of this function if the product list grows to 10,000 items? What would you change?" The engineer returns with an optimised approach and, crucially, the reasoning for it — which they now own.

**Example 2: Designing a growth experience for a mid-level engineer**
A mid-level engineer has never run a technical design session. The lead assigns them ownership of a medium-complexity component design, asks them to write the proposal, and books them time with the senior engineers to present and receive feedback. The lead attends as an observer, not a participant. The experience is uncomfortable for the engineer; it is formative.

**Example 3: Opening the leadership conversation**
"I've noticed you tend to be the one people come to when they're stuck, even when they haven't formally asked you for help. I think you have instincts that go beyond individual contribution. I'd like to talk about what the next few years might look like for you — not to push you in any direction, but because I want you to know the door is open." This plants a seed without projection.

## Common Mistakes

**The Answer Machine.** Always providing the correct answer immediately feels helpful and demonstrates expertise. It creates engineers who wait for the answer rather than developing the capacity to find it. Measured over a year, a team of Answer Machine graduates is slower and more fragile than a team of Socratic graduates.

**The Phantom Mentor.** "My door is always open" is not a mentoring strategy. It places the entire burden of initiating development on the mentee and guarantees that the engineers who most need investment — often the ones least likely to ask — receive none.

**Mentoring through projection.** Assuming that what helped you grow will help this engineer. Introverts and extroverts learn differently. Engineers with imposter syndrome need different things than engineers with overconfidence. The obligation is to understand what this person needs, not to repeat what worked for you.

**Confusing feedback with mentoring.** Feedback about specific work is important but insufficient. Mentoring is a sustained relationship with someone's development, not a series of post-mortems on individual deliverables.

## Mental Model

Think of the lead as a **climbing wall setter**. A good wall setter creates routes that are just beyond the current climber's reach — hard enough to grow, achievable enough to complete. They watch how the climber moves, adjust the routes over time, and know when to increase the grade and when to back off. The climber does the climbing. The setter designs the conditions for growth.

## Mini Summary

- Mentoring adapts to level: scaffolding for juniors, autonomy for mids, organisational exposure for senior-to-lead transitions
- Socratic code review develops reasoning; directive code review produces compliance
- Scaffolding provides structure; enablement removes blockers — know which gap you are filling
- Growing future leaders requires intentional experiences, not just time
- The two most dangerous anti-patterns are the Answer Machine and the Phantom Mentor

# Guided Practice Quest

Work through the three guided steps in sequence. Each presents a realistic mentoring scenario requiring nuanced reasoning about both the technical and human dimensions of development.

# Solo Practice Quest

You are writing a "Mentoring Handbook" section for your team's engineering handbook. Write the section titled "How We Grow Engineers Here" (approximately 300 words). Cover the principles your team operates by, how you approach code review as a development tool, how you identify and cultivate leadership potential, and what engineers on your team can expect from you as their lead. Make it specific enough that a new engineer joining the team would understand the culture and what they could ask of you.

# Integration

**Psychology:** Lev Vygotsky's Zone of Proximal Development describes the space between what a learner can do alone and what they can do with support. Effective mentoring operates in this zone — challenging without overwhelming. Problems too far below the ZPD are boring; problems too far above it are paralysing. A lead who understands this calibrates challenge deliberately rather than assigning work by availability.

**Sociology:** Mentoring relationships carry power dynamics that require careful navigation. The mentor holds positional power (title, review authority) that can inhibit honest communication from the mentee. Creating psychological safety — the sense that it is safe to be uncertain, wrong, or struggling — is a sociological act as much as an interpersonal one.

**Philosophy:** The Confucian concept of *junzi* — the exemplary person — suggests that virtue is cultivated through practice and relationship, not through instruction alone. This maps directly to mentoring: engineers do not become excellent by being told what excellence looks like. They become excellent by practising in proximity to someone who models it and reflects it back to them.

# Lore Conclusion

Valdur's most famous graduate, Archmage Seraphel, was asked late in her career what she remembered most about studying under him. She said: "He asked me a question I still haven't fully answered. I've been working on it for thirty years."

That is the measure of great mentoring. Not whether the engineer wrote good code this quarter. Whether you left them with questions good enough to pursue for a career.

---
