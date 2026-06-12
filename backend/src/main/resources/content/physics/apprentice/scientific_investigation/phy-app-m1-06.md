---
id: phy-app-m1-06
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m1
moduleTitle: "Module 1: Foundations of Physics"
moduleGlyph: "🔭"
moduleSortOrder: 1
topicSlug: scientific_investigation
topicTitle: "Scientific Investigation"
topicSortOrder: 2
title: "Recording and Interpreting Results"
sortOrder: 6
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Construct a results table with units in the headers
  - Identify anomalous results and decide how to handle them
  - Draw conclusions that the data actually supports — and no more
integrationDomains: [data_engineering, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Builds a results table with variable names and units in the headers
    - Records raw readings, not just averages
    - Identifies anomalies and explains how they were handled
    - States a conclusion limited to what the data shows, with uncertainty acknowledged
  keywords: [table, units, anomaly, average, conclusion, evidence, raw data, trend]
  modelAnswer: |
    Good records put units in the table headers, keep every raw reading (never just the
    average), and note conditions that might matter later. Anomalies — readings far outside
    the pattern — are investigated rather than silently deleted: if a cause is found (a
    mistimed trial, a knocked bench) they may be excluded with a note; otherwise they stay
    and widen the stated uncertainty. A sound conclusion claims exactly what the data shows
    ("doubling the height increased bounce by roughly half, within ±2 cm") and resists the
    temptation to claim more.
guidedSteps:
  - id: phy-app-m1-06-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A results table column is headed "Time". Readings below it are 2.1, 2.3, 2.2. What is the most important improvement?
    inputConfig:
      options:
        - "Centre the numbers"
        - "Head the column 'Time (s)' so every reading carries its unit"
        - "Round all values to whole numbers"
        - "Sort the values into ascending order"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Head the column 'Time (s)' so every reading carries its unit"]
      rejectedFeedback: "Units belong in the header so every number beneath inherits them. 'Time' alone leaves a stranger guessing — seconds? minutes?"
    hint: "What will a stranger reading this table next year not be able to work out?"
    reflectionPrompt: "Why are units placed in the header rather than repeated next to every value?"
  - id: phy-app-m1-06-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Drop-bounce readings at one height: 41 cm, 43 cm, 42 cm, 67 cm, 42 cm. What should you do about the 67 cm reading?
    inputConfig:
      options:
        - "Delete it quietly — it spoils the average"
        - "Keep it in the average no matter what"
        - "Investigate it; if a cause is found, exclude it with a note in the record; if not, repeat more trials"
        - "Change it to 42 cm to match the pattern"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Investigate it; if a cause is found, exclude it with a note in the record; if not, repeat more trials"]
      rejectedFeedback: "Anomalies are clues, not embarrassments. Silently deleting data is scientific misconduct; blindly including a known mistake corrupts the result. Investigate, document, decide."
    hint: "An anomaly might be a mistake — or a discovery. What's the only way to find out?"
    reflectionPrompt: "Can you think of a famous case where the 'anomaly' turned out to be the real discovery?"
  - id: phy-app-m1-06-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Your data shows a ball dropped from 50 cm bounces to 31 cm on average, and from 100 cm to 59 cm. A classmate concludes: "bounce height is always about 60% of drop height, for any ball, from any height." In 2–3 sentences, explain what is wrong with this conclusion.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [only, two, tested, range, beyond, overgeneral, extrapolat, evidence]
      rejectedFeedback: "The data covers ONE ball at TWO heights. The ~60% pattern is supported within that range — but 'any ball, any height' extrapolates far beyond the evidence. Conclusions must stay inside the tested range."
    hint: "How many balls and how many heights were actually tested?"
    reflectionPrompt: "What additional measurements would justify extending the conclusion?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Why should raw readings be kept, even after calculating the average?"
    options:
      - "To make the report longer"
      - "So the spread, anomalies, and any later questions can still be checked against the original data"
      - "Averages are usually wrong"
      - "Raw data looks more scientific"
    correctIndex: 1
    feedback: "The average throws away information — the spread, the outliers, the trends across trials. Raw data lets anyone (including future-you) re-examine the evidence."
  - type: MULTIPLE_CHOICE
    question: "Which conclusion is properly limited to its data?"
    options:
      - "Within the tested range of 20–100 cm, bounce height rose roughly in proportion to drop height (±2 cm)"
      - "Bounce height is always proportional to drop height"
      - "All balls bounce to 60% of their drop height"
      - "Gravity makes things bounce"
    correctIndex: 0
    feedback: "It names the tested range, the observed relationship, and the uncertainty — claiming everything the data supports and nothing it doesn't."
---

# Hook

In 1928, Alexander Fleming returned from holiday to find one of his bacterial culture plates ruined — contaminated by a stray mould. Most researchers would have binned it. Fleming paused on the anomaly: around the mould, the bacteria had died. He recorded it, investigated it, and the contamination turned out to be penicillin — arguably the most important medical discovery of the century.

The lesson is not "hope for lucky mould". It is that *results only become science when they are recorded, examined, and interpreted honestly* — including, especially, the readings that don't fit. A brilliant experiment with sloppy records is worth less than a modest experiment documented so well that a stranger could rebuild it.

This lesson covers the craft: tables that carry their units, anomalies treated as clues, and conclusions that claim exactly what the data earned.

# Lore Introduction

The Observatory's Archive of Trials occupies an entire floor: shelf after shelf of logbooks going back nine centuries. Thorne pulls one down, opens it to a page of neat columns, and points to a single entry circled in faded red ink. "Watch-Magus Erren, four hundred years ago, timing star transits. One night, one star arrived early — by a sliver. Her colleagues said: bad reading, strike it out." He taps the circled entry. "She kept it. Marked it, noted the sky conditions, and kept watching. The 'bad reading' returned every eleven months." He closes the book with care. "It was the first evidence that our calendar drifted. One honest record of one inconvenient number. Strike out nothing, apprentice. *Annotate.*"

# Core Learning

## Concept Introduction

**Recording.** A results table earns its keep with three habits:

| Habit | Why |
|-------|-----|
| Units in headers — "Drop height (cm)", "Bounce height (cm)" | Every value beneath inherits its unit; nothing is ambiguous |
| Record raw readings, every trial | Averages hide spread and anomalies; raw data preserves the evidence |
| Note conditions — date, instrument, anything unusual | Today's irrelevant detail is next month's explanation |

**Anomalies.** An anomalous result lies far outside the pattern of its neighbours. The rule is *investigate, then decide*:

- Cause found (mistimed trial, knocked bench, misread scale)? → Exclude it, **with a written note** saying what and why.
- No cause found? → It stays. Run more trials. Either it dissolves into the noise — or it's trying to tell you something, like Fleming's mould.
- **Never** silently delete or "correct" a reading. That is where misconduct begins.

**Interpreting.** A conclusion should state: the relationship observed, the range over which it was tested, and the uncertainty. The cardinal discipline is refusing to claim more than the data shows — two points do not establish "always", and one ball does not speak for all balls.

## Why It Matters

- Science is a relay: your records are the baton. Results that can't be understood or re-checked by others effectively don't exist.
- Anomaly-handling is where scientific integrity lives or dies — several famous frauds began as "tidying up" inconvenient data points.
- Overclaiming is the most common reasoning error in public life: a study on 40 students becomes a headline about "everyone". You are learning the antidote.

## Worked Examples

**Example 1: A table that works**
Testing bounce height vs drop height: columns "Drop height (cm)", "Trial 1 (cm)", "Trial 2 (cm)", "Trial 3 (cm)", "Mean bounce (cm)". A footnote reads: "Trial 2 at 80 cm excluded — ball clipped table edge; noted at time of trial." Everything a stranger needs is on the page.

**Example 2: Anomaly as discovery**
Your pendulum timings are beautifully consistent — except every reading taken near the window is slightly long. Investigation: a draught. The "anomaly" just taught you a control variable you'd missed. Annotated, fixed, redesigned — the experiment improves *because* the odd readings were examined rather than erased.

**Example 3: Sizing the conclusion to the data**
Data: at 20, 40, 60, 80, 100 cm drop heights, mean bounce is roughly 60% of drop each time, spread ±2 cm. Earned conclusion: "Over 20–100 cm, this ball's bounce height was proportional to drop height (≈0.6×, ±2 cm)." Not earned: "all balls", "any height", "always" — nothing above 100 cm was tested, and a ball dropped from 100 m will certainly not bounce 60 m.

## Common Mistakes

- **Units missing from headers** — the single most common table flaw; it makes the data unusable later.
- **Recording only averages** — once the raw trials are gone, the spread and anomalies are gone forever.
- **Silently dropping awkward readings** — exclusions must be visible and justified in the record.
- **Concluding beyond the tested range** — extrapolation is a hypothesis for the *next* experiment, not a result of this one.
- **"Proves"** — experiments support, are consistent with, or rule out; they do not prove.

## Mental Model

Treat your logbook as testimony given **under oath to your future self**. Future-you, six months from now, remembers nothing: not the units, not which trial went wrong, not why one number is missing. Every entry should answer the cross-examination — *what exactly did you see, in what conditions, and how do you know?* Records that survive that cross-examination are science; everything else is anecdote with decimal points.

## Mini Summary

- ✔ Units live in table headers; raw readings are kept forever
- ✔ Anomalies: investigate, annotate, never silently delete
- ✔ Conclusions state the relationship, the tested range, and the uncertainty
- ✔ Claim exactly what the data earned — extrapolation is the next hypothesis
- ✔ Write records for a stranger (or your forgetful future self)

# Guided Practice Quest

Work through the guided steps to repair a flawed table, handle a suspicious reading correctly, and cut an overclaimed conclusion down to its honest size.

# Solo Practice Quest

Run the bounce experiment you designed in the previous lesson (or a simplified version): one ball, three drop heights, three trials each. Produce: (1) a results table with units in every header and all raw readings; (2) a note on any anomaly and how you handled it; (3) a one-paragraph conclusion stating the relationship you observed, the range you tested, and your estimated uncertainty. Then add a final sentence beginning "This experiment does NOT show that..." — and complete it honestly.

# Integration

**Data Engineering**: A results table is a tiny dataset, and the rules are identical at any scale: schema with units (column types), raw data preserved (immutable source tables), anomalies flagged not deleted (data quality pipelines), and lineage notes (metadata). A physicist's logbook discipline is a data engineer's governance discipline.

**Mathematics**: Deciding whether a point is "anomalous" is a statistical judgement — how far from the mean, compared with the spread? Later you'll formalise this with standard deviations; for now, your eye for "far outside the pattern" is doing informal statistics.

# Lore Conclusion

Thorne returns Watch-Magus Erren's logbook to its shelf, beside four centuries of its descendants. "Every book on this floor outlived its author," he says. "That is the point of them." He hands you a fresh, blank logbook — your own, your name already inked on the spine in the Archive's careful hand. "Measurement, method, records. The foundations are laid. Next we sharpen the tools you'll think with — the mathematics. Don't look so grim," he adds, almost smiling. "You already know most of it. You simply don't yet know what it's *for*."
