---
id: phy-app-m1-05
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m1
moduleTitle: "Module 1: Foundations of Physics"
moduleGlyph: "🔭"
moduleSortOrder: 1
topicSlug: scientific_investigation
topicTitle: "Scientific Investigation"
topicSortOrder: 2
title: "Designing a Fair Experiment"
sortOrder: 5
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Identify independent, dependent, and control variables in an experiment
  - Explain why only one variable may change at a time
  - Spot design flaws that make an experiment unfair
integrationDomains: [psychology, data_engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Correctly identifies the independent, dependent, and control variables in own design
    - Changes only one variable while holding others fixed
    - Includes repetition to handle random variation
    - Identifies at least one remaining weakness in the design honestly
  keywords: [independent, dependent, control, variable, fair test, repeat, constant]
  modelAnswer: |
    A fair experiment changes exactly one thing (the independent variable), measures one
    outcome (the dependent variable), and holds everything else constant (the control
    variables). If two things change at once, the result is ambiguous — you cannot say which
    change caused the effect. Repeating trials handles random variation, and an honest design
    finishes by naming its own weaknesses: the variables that could not be fully controlled
    and how they might bias the outcome.
guidedSteps:
  - id: phy-app-m1-05-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      You test whether sugar dissolves faster in hot water than cold water. You use hot water in a wide glass with stirring, and cold water in a narrow glass without stirring. What is wrong?
    inputConfig:
      options:
        - "Nothing — hot versus cold is being tested"
        - "Three variables changed at once (temperature, glass shape, stirring), so the cause of any difference is unknowable"
        - "The glasses should both be narrow"
        - "Sugar is the wrong substance to test"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Three variables changed at once (temperature, glass shape, stirring), so the cause of any difference is unknowable"]
      rejectedFeedback: "A fair test changes only the variable under investigation. With temperature, glass shape, AND stirring all different, a faster result tells you nothing about temperature specifically."
    hint: "Count how many things differ between the two setups."
    reflectionPrompt: "How would you redesign this so temperature is the only difference?"
  - id: phy-app-m1-05-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In an experiment testing how ramp angle affects how far a toy car rolls, the ramp angle is the ________ variable.
    inputConfig:
      placeholder: "independent"
    markingRule:
      matchMode: CONTAINS
      accepted: [independent]
      rejectedFeedback: "The variable you deliberately change is the independent variable. The distance rolled — what you measure — is the dependent variable."
    hint: "Is the angle something you set, or something you measure as an outcome?"
    reflectionPrompt: "Name two control variables you'd need to hold fixed in this ramp experiment."
  - id: phy-app-m1-05-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Design a fair test for this question: "Does a heavier teabag steep tea faster?" In 3–4 sentences, state your independent variable, dependent variable, and at least two things you would keep constant.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [independent, dependent, constant, same, control, temperature]
      rejectedFeedback: "A strong design names the teabag mass as independent, a measured outcome (e.g. colour intensity after a fixed time) as dependent, and holds water temperature, volume, cup type, and steeping time constant."
    hint: "What will you change, what will you measure, and what must stay identical between trials?"
    reflectionPrompt: "What random variation might still affect your results, and how would repetition help?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In a fair test, the dependent variable is:"
    options:
      - "The thing you deliberately change"
      - "The thing you measure as the outcome"
      - "Anything kept constant"
      - "A variable that depends on the weather"
    correctIndex: 1
    feedback: "The dependent variable is the outcome — it (potentially) *depends* on the independent variable you changed."
  - type: MULTIPLE_CHOICE
    question: "Why are control variables held constant?"
    options:
      - "To make the experiment cheaper"
      - "So any change in the outcome can be attributed to the one variable you changed"
      - "Because they are unimportant"
      - "To make results look tidier"
    correctIndex: 1
    feedback: "Holding everything else fixed is what earns you the right to say 'the change I made caused the difference I measured'."
---

# Hook

A company once proudly reported that students who ate their breakfast cereal scored higher on tests. True — but the cereal-eating students also tended to come from homes with regular routines, more sleep, and more study support. Was it the cereal? The sleep? The routine? When many things differ at once, *the data cannot tell you which one mattered*.

Physics experiments face exactly the same trap. Roll a ball down two different ramps with two different surfaces at two different angles, and the winner tells you precisely nothing. The cure is one of the most powerful ideas in experimental science, and it is almost embarrassingly simple: **change one thing at a time**.

This lesson turns that slogan into a design checklist you can apply to any investigation, in physics and far beyond.

# Lore Introduction

An apprentice ahead of you in the rotation presents his findings to Magus Thorne: lodestones lose their pull in winter, he claims. His evidence: a lodestone in the cold north tower lifted fewer iron filings than one in the warm study. Thorne walks to the bench, picks up the two lodestones, and weighs them. One is nearly twice the size of the other. "Cold tower, smaller stone, older stone, different filings," he counts off on his fingers. "Four differences. One conclusion. Which of the four earned it?" The apprentice has no answer. Thorne sets the stones down gently — he is never cruel — and says: "The art is not in the measuring, lad. It is in arranging the world so the measurement can only mean *one thing*."

# Core Learning

## Concept Introduction

Every well-designed experiment assigns each quantity one of three roles:

| Role | Meaning | Ramp-and-car example |
|------|---------|---------------------|
| **Independent variable** | The one thing you deliberately change | Ramp angle |
| **Dependent variable** | The outcome you measure | Distance the car rolls |
| **Control variables** | Everything held constant | Same car, same ramp surface, same release point, same floor |

The logic of the **fair test**: if only one variable changed and the outcome changed, then the change in outcome is attributable to that variable. If *two* things changed, the experiment is ambiguous — no statistics, no careful measuring, no enthusiasm can rescue it afterwards.

Two more pillars complete the design:

- **Repetition** — repeat each condition several times and average, to tame random variation (a wobbly release, a gust of air).
- **A written method** — specific enough that a stranger could repeat the experiment exactly. If your method says "roll the car down the ramp", a stranger asks: from where? released how? measured to which point of the car?

## Why It Matters

- Ambiguous experiments waste effort: a month of careful measurement on a confounded design proves nothing.
- The fair-test logic is the core of drug trials, A/B testing in software, and agricultural research — control groups exist precisely to hold every other variable constant.
- Spotting unfair comparisons is daily-life armour: "our customers live longer", "schools with tablets score higher" — ask immediately what *else* differs between the groups.

## Worked Examples

**Example 1: Repairing the lodestone study**
To test cold honestly: take *one* lodestone, count the filings it lifts at room temperature (five trials, averaged), chill *the same stone*, count again (five trials, averaged). One stone, one variable changed, repeated trials. Now a difference means something.

**Example 2: Which kettle boils faster?**
Unfair: old kettle in the morning with 1.5 L; new kettle at night with 1 L. Fair: same volume of water at the same starting temperature, same room, back-to-back trials, alternating order (in case the mains electricity varies), three repeats each, compare average times.

**Example 3: The hidden variable**
A student tests whether a heavier pendulum swings faster, carefully using the same string... but ties the heavier mass with a longer knot, lengthening the pendulum. The design *looked* fair; an unnoticed variable (effective length) changed too. Defence: list every variable you can think of *before* starting, and state how each is controlled.

## Common Mistakes

- **Changing two things at once** — the cardinal sin; the result becomes uninterpretable.
- **Forgetting unglamorous controls** — same observer, same time of day, same instrument; small asymmetries accumulate.
- **No repetition** — a single trial cannot distinguish a real effect from a fluke.
- **Vague methods** — "drop the ball from high up" is not repeatable; "release from 2.00 m, measured to the ball's base" is.
- **Confusing variable names** — if you're unsure which is independent, ask: "which one did I choose the values of in advance?"

## Mental Model

Think of an experiment as a **courtroom where only one suspect is allowed in the dock at a time**. The dependent variable is the verdict; the independent variable is the accused. Control variables are all the other suspects — locked outside the courtroom so they cannot influence the verdict. Let two suspects in at once, and any verdict is mistrial: you will never know which one did it.

## Mini Summary

- ✔ Independent = what you change; dependent = what you measure; controls = what you fix
- ✔ Change exactly one variable at a time — ambiguity cannot be fixed afterwards
- ✔ Repeat and average to tame random variation
- ✔ Write methods a stranger could follow exactly
- ✔ List potential hidden variables before you start, not after

# Guided Practice Quest

Work through the guided steps to diagnose an unfair experiment, classify variables, and design a fair test of your own from scratch.

# Solo Practice Quest

Design — on paper — a fair experiment to answer: "Does the height a ball is dropped from affect how high it bounces?" Specify: the independent and dependent variables; at least four control variables; the number of repeats per height and why; and the exact procedure in numbered steps a stranger could follow. Finally, play your own critic: identify the weakest point of your design — the variable hardest to control or the measurement hardest to make — and propose one improvement.

# Integration

**Psychology**: Human experiments add a wicked variable: expectation. Patients improve when given sugar pills; observers see what they expect. Blinding — hiding who got which treatment — is the fair-test principle applied to the experimenters' own minds.

**Data Engineering**: A/B tests on software are fair tests at scale: users are randomly split, one variable (the feature) differs, and the dependent variable is behaviour. Randomisation does the job that physical control does in physics — it spreads every hidden variable evenly across both groups.

# Lore Conclusion

A fortnight later the lodestone apprentice presents again: one stone, one variable, ten trials warm, ten trials chilled, every condition written out in a hand so precise it could be engraved. The cold stone lifts *the same* number of filings, within his stated uncertainty. His original claim is dead, and he announces this with something close to pride. Thorne nods once — high praise, by his standards. "Now you have learned the difference between *seeing* something and *establishing* it." He turns to you. "Your turn approaches. But first — data is only as good as the records that hold it. Tomorrow, the logbook."
