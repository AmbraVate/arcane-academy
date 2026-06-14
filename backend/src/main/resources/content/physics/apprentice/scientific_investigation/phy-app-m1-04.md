---
id: phy-app-m1-04
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m1
moduleTitle: "Module 1: Foundations of Physics"
moduleGlyph: "🔭"
moduleSortOrder: 1
topicSlug: scientific_investigation
topicTitle: "Scientific Investigation"
topicSortOrder: 2
title: "The Scientific Method"
sortOrder: 4
xpReward: 30
practiceType: NONE
questType: KNOWLEDGE
learningObjectives:
  - Order the steps of a scientific investigation
  - Write a testable hypothesis
  - Explain why a hypothesis must be falsifiable
integrationDomains: [philosophy, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Describes the cycle observation → question → hypothesis → prediction → test → conclusion
    - Writes a hypothesis that is specific and testable
    - Explains falsifiability — what observation would prove the hypothesis wrong
    - Recognises that a failed prediction is informative, not a failure of science
  keywords: [hypothesis, prediction, test, experiment, falsifiable, observation, evidence, conclusion]
  modelAnswer: |
    Science moves in a cycle: an observation raises a question; a hypothesis proposes a possible
    answer; the hypothesis implies a prediction; an experiment tests the prediction; and the
    result either supports the hypothesis or rules it out. A good hypothesis is specific and
    falsifiable — "heavier pendulums swing at the same rate as light ones" can be proven wrong
    by a single careful experiment, which is exactly what gives it scientific value. When a
    prediction fails, knowledge still advances: one possible explanation has been eliminated.
    Claims that no observation could ever contradict are not scientific claims at all.
guidedSteps:
  - id: phy-app-m1-04-g1
    sortOrder: 1
    inputType: SEQUENCE
    instruction: |
      Arrange these stages of a scientific investigation into their natural order, starting from the very beginning.
    inputConfig:
      items:
        - "Run the experiment"
        - "Make an observation that raises a question"
        - "Derive a testable prediction"
        - "Compare results with the prediction and conclude"
        - "Propose a hypothesis"
    markingRule:
      matchMode: CONTAINS
      accepted:
        - '"make an observation that raises a question","propose a hypothesis","derive a testable prediction","run the experiment","compare results with the prediction and conclude"'
      rejectedFeedback: "The cycle runs: observation → hypothesis → prediction → experiment → conclusion. Each stage feeds the next, and the conclusion usually triggers a new observation."
    hint: "You cannot test anything until your idea has been turned into a concrete prediction."
    reflectionPrompt: "Why is the cycle drawn as a loop rather than a straight line?"
  - id: phy-app-m1-04-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which of these is a **testable hypothesis**?
    inputConfig:
      options:
        - "Magnets are fascinating"
        - "This magnet will lift more paperclips when chilled than when warm"
        - "Magnetism is one of nature's mysteries"
        - "Some magnets are probably stronger than others, in some sense"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["This magnet will lift more paperclips when chilled than when warm"]
      rejectedFeedback: "A testable hypothesis makes a specific, checkable claim. 'Chilled magnet lifts more paperclips' tells you exactly what experiment to run and what result would prove it wrong."
    hint: "Which statement could a simple experiment prove wrong?"
    reflectionPrompt: "Rewrite one of the vague options into a testable form. What did you have to add?"
  - id: phy-app-m1-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A friend claims: "There is an invisible dragon in my garage that no instrument can ever detect." In 2–3 sentences, explain why this claim sits outside science, using the idea of falsifiability.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [falsifiable, test, disprove, evidence, observation, cannot]
      rejectedFeedback: "Key idea: if no possible observation could ever show the claim false, then no experiment can engage with it — it is unfalsifiable, and science can say nothing about it."
    hint: "Ask: what experimental result would the friend accept as proving the dragon absent?"
    reflectionPrompt: "Does unfalsifiable mean false? What exactly is science declining to do here?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "An experiment's result contradicts your hypothesis. What is the scientifically correct response?"
    options:
      - "Discard the result and repeat until it agrees"
      - "Adjust the data slightly so it fits"
      - "Treat the hypothesis as ruled out (or revise it) and test again — the experiment has taught you something"
      - "Conclude the equipment must be broken"
    correctIndex: 2
    feedback: "A failed prediction is information — one candidate explanation is eliminated. (Checking the equipment is fair, but only with the same scrutiny you'd apply if the result had agreed with you.)"
  - type: MULTIPLE_CHOICE
    question: "What makes a hypothesis scientific?"
    options:
      - "It is written by a scientist"
      - "It is expressed with mathematics"
      - "It could, in principle, be proven wrong by some observation"
      - "It has been proven true"
    correctIndex: 2
    feedback: "Falsifiability is the dividing line: a scientific hypothesis sticks its neck out and names the observations that would refute it."
---

# Hook

Around 350 BC, Aristotle declared that heavy objects fall faster than light ones — and for nearly two thousand years, virtually everyone believed him. It sounds plausible. It *feels* right. A hammer obviously beats a feather.

Then Galileo did something that seems obvious in hindsight and was revolutionary at the time: instead of asking whose argument sounded better, he **tested it**. Balls of different weights, rolled down ramps, timed carefully. The heavy ball and the light ball kept pace. Two millennia of confident authority collapsed against one afternoon of careful measurement.

That move — *from argument to test* — is the scientific method, and it is the single most powerful thinking tool our species has built. This lesson teaches you to run the cycle yourself.

# Lore Introduction

In the Observatory's debating gallery, two senior magi are arguing — loudly — about whether the great pendulum swings faster on cold nights. Scrolls of reasoning are produced on both sides. Magus Thorne watches from the back with you, unimpressed. "They could argue until the equinox," he murmurs. "Or they could spend one cold night and one warm night in the pendulum hall with a chronometer." He leads you out. "The Academy was founded on a single heresy: that the universe outranks every authority who describes it. We do not ask *who* is right. We ask the world directly — and we phrase the question so the world can answer."

# Core Learning

## Concept Introduction

The **scientific method** is a cycle for converting curiosity into reliable knowledge:

1. **Observation** — something catches your attention. *The pendulum seems slower on cold nights.*
2. **Question** — make it precise. *Does temperature affect the pendulum's period?*
3. **Hypothesis** — a proposed answer. *Cooling the pendulum increases its period.*
4. **Prediction** — what the hypothesis implies. *At 5 °C the period will be measurably longer than at 25 °C.*
5. **Experiment** — create the conditions and measure.
6. **Conclusion** — compare result with prediction; support, revise, or discard the hypothesis. Each conclusion seeds new observations, so the cycle loops.

The keystone property of a scientific hypothesis is **falsifiability**: it must be possible, in principle, for some observation to prove it wrong. "Cooling increases the period" is falsifiable — measure and see. "The pendulum has an undetectable spirit" is not — no measurement can engage with it, so science sets it aside (note: *unfalsifiable* means untestable, not necessarily false).

A failed prediction is not failure. It is the method working: one wrong explanation eliminated, the search narrowed.

## Why It Matters

- The method is self-correcting — wrong ideas, even beloved ones backed by authority, eventually collide with experiment and lose. No other knowledge system has this property built in.
- You will use this cycle far beyond physics: debugging code, diagnosing a car fault, or testing whether a study technique works are all hypothesis–prediction–test loops.
- Recognising unfalsifiable claims is a life skill: advertising, conspiracy theories, and pseudoscience routinely make claims that no possible evidence could contradict.

## Worked Examples

**Example 1: From vague to testable**
Vague: "Plants like music." Testable: "Bean seedlings exposed to 6 hours of music daily will be taller after 3 weeks than identical seedlings grown in silence." Note what was added: specific subjects, a specific treatment, a measurable outcome, a timeframe — and a clear way to be wrong.

**Example 2: Galileo's falling bodies**
Hypothesis (Aristotle's): heavier objects fall proportionally faster. Prediction: a ball ten times heavier should fall ten times faster. Experiment: ramps and timing. Result: both arrive together (air resistance aside). Conclusion: the hypothesis is falsified — and the door opens to a better theory of motion.

**Example 3: The cycle looping**
You hypothesise your slow laptop is caused by too many browser tabs. Prediction: closing them restores speed. Test: no improvement. Hypothesis falsified — but now you've learned something, and the next hypothesis (background updates?) is better aimed. Three loops later, you find the culprit. That is the method, running at desk scale.

## Common Mistakes

- **Starting with a conclusion and seeking only confirming evidence** — the method demands you try to *break* your idea, not protect it.
- **Untestable hypotheses** — "in some sense", "somehow", "maybe" are warning signs that no experiment could engage the claim.
- **Treating a supported hypothesis as proven forever** — support means "survived testing so far"; every scientific claim remains open to better evidence.
- **Changing the hypothesis after seeing the data and pretending you predicted it** — that destroys the test's value entirely.

## Mental Model

Think of hypotheses as **candidates in a tournament where the universe is the referee**. Argument, authority, and eloquence buy nothing — every candidate must step onto the field and risk losing. A hypothesis that refuses to name the conditions under which it would lose is not a contender; it's a spectator. Knowledge is whatever is still standing after the universe has had its say.

## Mini Summary

- ✔ The method cycles: observation → question → hypothesis → prediction → experiment → conclusion
- ✔ A scientific hypothesis must be falsifiable — it names the evidence that would refute it
- ✔ Failed predictions advance knowledge by eliminating explanations
- ✔ Support is provisional; proof is never final
- ✔ Test your ideas by trying to break them, not defend them

# Guided Practice Quest

Work through the guided steps to order the investigation cycle, recognise testable hypotheses, and apply the falsifiability test to a claim designed to dodge it.

# Solo Practice Quest

Choose a small mystery from your own life — why your phone battery drains fast on some days, why one houseplant thrives and another sulks, why your tea cools quicker in one mug. Write out one full turn of the cycle: the observation, a precise question, a falsifiable hypothesis, a concrete prediction, and a description of the experiment you could actually run this week (you don't have to run it — yet). Finish with one sentence stating exactly what result would prove your hypothesis wrong.

# Integration

**Philosophy**: Falsifiability comes from philosopher Karl Popper, who proposed it as the line between science and non-science. The deeper question — *why* should surviving attempts at refutation make a claim trustworthy? — belongs to epistemology, and physicists rely on its answer every day.

**Psychology**: The greatest obstacle to the method is confirmation bias — the human tendency to notice evidence that agrees with us. The scientific method is best understood as a social technology for catching the errors individual minds reliably make.

# Lore Conclusion

A week later, the two magi from the gallery post their findings: three cold nights, three warm nights, sixty timed swings each. The pendulum *is* slower when cold — by a sliver, as the bronze rod lengthens in... no, wait. You read it again. The cold pendulum is *faster*: the rod contracts. The magus who had argued most passionately for "slower" has signed the report alongside his rival. Thorne watches you absorb this. "He argued beautifully and was wrong, and he signed his name to the refutation anyway. *That* is what we train here — not cleverness. The willingness to be corrected by the world." He hands you a fresh slate. "Next lesson: how to design the test itself so it cannot fool you."
