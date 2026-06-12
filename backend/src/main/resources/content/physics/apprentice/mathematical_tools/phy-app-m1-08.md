---
id: phy-app-m1-08
domainId: physics
tier: APPRENTICE
moduleId: phy-app-m1
moduleTitle: "Module 1: Foundations of Physics"
moduleGlyph: "🔭"
moduleSortOrder: 1
topicSlug: mathematical_tools
topicTitle: "Mathematical Tools"
topicSortOrder: 3
title: "Graphs and Proportionality"
sortOrder: 8
xpReward: 30
practiceType: NONE
questType: GUIDED
learningObjectives:
  - Plot experimental data with the independent variable on the x-axis
  - Recognise direct proportionality from a straight line through the origin
  - Interpret the gradient of a line as a physical quantity
integrationDomains: [mathematics, data_engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Plots independent variable on x, dependent on y, with labelled axes and units
    - Draws a line of best fit rather than connecting dots
    - Identifies whether the relationship is proportional, linear, or neither
    - Interprets the gradient with its physical meaning and unit
  keywords: [gradient, slope, origin, proportional, best fit, axis, linear, intercept]
  modelAnswer: |
    The independent variable goes on the x-axis and the dependent on the y-axis, both labelled
    with units. A line of best fit smooths random error rather than chasing every point. If the
    best-fit line is straight AND passes through the origin, the quantities are directly
    proportional — doubling one doubles the other. The gradient (rise over run) is itself a
    physical quantity: on a distance–time graph its unit is m/s, and it IS the speed. A straight
    line that misses the origin is linear but not proportional — the intercept also has meaning.
guidedSteps:
  - id: phy-app-m1-08-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A distance–time graph for a walker is a straight line through the origin with gradient 1.4. What does the 1.4 represent?
    inputConfig:
      options:
        - "The walker's speed: 1.4 metres per second"
        - "The total distance walked"
        - "The time the walk took"
        - "Nothing — gradients are just numbers"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The walker's speed: 1.4 metres per second"]
      rejectedFeedback: "Gradient = rise/run = distance/time = speed. On a distance–time graph, the slope IS the speed: 1.4 m of distance gained per second."
    hint: "Gradient = (change in y) ÷ (change in x). What are y and x here, and what is distance ÷ time?"
    reflectionPrompt: "What would a steeper line mean? A horizontal line?"
  - id: phy-app-m1-08-g2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which graph shows that y is **directly proportional** to x?
    inputConfig:
      options:
        - "A straight line passing through the origin"
        - "A straight line crossing the y-axis at 5"
        - "A smooth upward curve"
        - "Any line that goes up from left to right"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["A straight line passing through the origin"]
      rejectedFeedback: "Direct proportionality means y = kx: doubling x doubles y. That requires a straight line AND passage through (0,0) — zero in, zero out."
    hint: "Proportional means doubling one doubles the other. What must y be when x = 0?"
    reflectionPrompt: "Why is a straight line with a non-zero intercept NOT proportional, even though it's linear?"
  - id: phy-app-m1-08-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A student plots 8 data points that scatter slightly around an upward trend, then joins every dot with zigzag line segments. In 2–3 sentences, explain what they should have done instead, and why.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [best fit, smooth, random, scatter, trend, average, error]
      rejectedFeedback: "Draw a single line of best fit through the trend. The zigzags chase random measurement error; the best-fit line averages it away and reveals the underlying relationship."
    hint: "Is the wiggle in the data physics, or is it measurement noise?"
    reflectionPrompt: "When might a curve of best fit be more honest than a straight line?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "On a graph of experimental results, the independent variable belongs:"
    options: ["On the y-axis", "On the x-axis", "Wherever it fits best", "In the title only"]
    correctIndex: 1
    feedback: "Convention: the variable you controlled (independent) goes on x; the measured outcome (dependent) goes on y. The graph then reads 'as I changed this, that happened'."
  - type: MULTIPLE_CHOICE
    question: "A spring's extension–force graph is a straight line through the origin with gradient 0.02 m/N. What does doubling the force do to the extension?"
    options: ["Halves it", "Doubles it", "Squares it", "Nothing"]
    correctIndex: 1
    feedback: "Straight line through the origin = direct proportionality: double the force, double the extension. (You've just previewed Hooke's law.)"
---

# Hook

A column of numbers keeps its secrets. Look at this data: (1, 2.1), (2, 3.9), (3, 6.2), (4, 7.8), (5, 10.1). Anything jump out? Now imagine those five points plotted: they fall, near enough, on a straight line climbing through the origin. Instantly you *see* it — double the input, double the output. One glance did what a minute of squinting at digits couldn't.

That is what graphs are for. They turn relationships into shapes, and shapes are something the human eye is spectacularly good at judging. A straight line, a curve, a kink, a stray point sitting away from its companions — each is a sentence written in geometry.

This lesson teaches you to draw graphs that tell the truth, and to read the two most important things a line can say: *proportionality* and *gradient*.

# Lore Introduction

The Observatory's map room is walled with charts — not of coastlines, but of measurements: star brightness against time, pendulum period against length, tide height against moon phase. Thorne unrolls one from the great drought of two centuries past. "The granary masters had ledgers of numbers — rainfall, river depth, harvest weights — years of them, and saw nothing. Then a junior chart-keeper plotted river depth against the mountain snowfall of the *previous* winter." His finger traces a clean rising line across the old vellum. "One straight line, and suddenly the city knew its droughts a season in advance. The numbers always knew. The *picture* is what let someone finally hear them."

# Core Learning

## Concept Introduction

**Building the graph.** Independent variable on the x-axis, dependent on the y-axis, both labelled *with units* ("Force (N)", "Extension (m)"). Scales chosen so the data fills the page. Points marked precisely. Then — crucially — a single smooth **line of best fit** through the trend, not dot-to-dot zigzags: the wiggle in real data is mostly random error, and the best-fit line averages it away.

**Reading the line.** Two questions unlock most graphs:

1. **Is it straight, and does it pass through the origin?** If yes to both, y is **directly proportional** to x: y = kx, doubling x doubles y. Straight but missing the origin is *linear, not proportional* — the intercept means something physical (often a starting value or a systematic offset). A curve means the relationship is something richer.

2. **What is the gradient?** Gradient = rise ÷ run = (change in y) ÷ (change in x) — and it carries units. On a distance–time graph: m ÷ s = m/s. The gradient *is the speed*. On force–extension: the gradient measures stiffness. The slope of a physics graph is almost always a physical quantity in its own right.

| Line shape | Meaning |
|------------|---------|
| Straight, through origin | Direct proportion: y = kx |
| Straight, intercept ≠ 0 | Linear: y = kx + c; the intercept c has physical meaning |
| Curve steepening | y grows faster than x (perhaps y ∝ x²) |
| Horizontal | y doesn't depend on x at all |

**Anomalies on a graph** announce themselves: one point far off the line, instantly visible — another reason pictures beat tables.

## Why It Matters

- Half the laws you'll meet were *discovered* as straight lines on graphs — Hooke's law, Ohm's law, and (a steepening curve revealing v ∝ t under gravity) Galileo's free fall.
- Gradients are the standard way physics extracts a quantity from many noisy measurements at once — far better than computing speed from any single (distance, time) pair.
- Misleading graphs — truncated axes, cherry-picked scales — are everywhere in media and marketing. Knowing how honest graphs are built is how you spot dishonest ones.

## Worked Examples

**Example 1: Extracting speed from noisy data**
A trolley's position is logged every second; the points scatter slightly around a straight trend. Best-fit line gradient: take two well-separated points ON THE LINE (not data points), say (1.0 s, 2.2 m) and (9.0 s, 13.0 m). Gradient = (13.0 − 2.2)/(9.0 − 1.0) = 10.8/8.0 = 1.35 m/s. Every data point contributed to that line, so the speed estimate beats any single measurement.

**Example 2: The meaningful intercept**
A spring's loaded length vs applied force gives a straight line with intercept 0.30 m. Not proportional! The intercept is the spring's *natural length* — the line says: length = natural length + stretch per newton × force. The graph just separated the spring's geometry from its stiffness.

**Example 3: Spotting the law in the curve**
Drop-height vs fall-time data curves: doubling height does *not* double time. Plot height against time *squared*, and the points snap onto a straight line through the origin — revealing h ∝ t². Re-plotting against transformed axes is the physicist's favourite trick for taming curves.

## Common Mistakes

- **Axes swapped** — putting the measured outcome on x scrambles the story; convention exists so graphs read the same way for everyone.
- **Missing units on axes** — a graph without units is decoration, not data.
- **Dot-to-dot lines** — chasing noise instead of revealing trend.
- **Calculating gradient from two data points** — use two well-separated points on the *best-fit line*; data points contain the very noise the line removed.
- **"It's linear so it's proportional"** — check the origin. The intercept changes the physics.

## Mental Model

A graph is a **conversation between two variables, transcribed**. The x-axis asks, steadily: "and if I increase a little more?" The y-axis answers. A straight line through the origin is the simplest possible answer — "I match you, step for step, from zero." The gradient is the *exchange rate* between the two quantities. And like any exchange rate, it has units, and it is often the most valuable single number the conversation produces.

## Mini Summary

- ✔ Independent on x, dependent on y, units on both axes
- ✔ Best-fit line averages random error; never join the dots
- ✔ Straight + through origin = directly proportional (y = kx)
- ✔ Gradient = rise/run, carries units, and is usually a physical quantity
- ✔ Intercepts mean something; check the origin before claiming proportionality

# Guided Practice Quest

Work through the guided steps to interpret gradients, distinguish proportional from merely linear, and correct the most common graphing habit beginners bring from joining dots.

# Solo Practice Quest

Use the bounce data you collected earlier (or quickly gather: one ball, four drop heights, three trials each). Plot mean bounce height (y) against drop height (x) on paper or any charting tool: labelled axes with units, plotted points, one line of best fit. Then answer in writing: (1) Is the relationship proportional, linear, or curved — and what's your evidence? (2) Calculate the gradient from two points on your line, with its unit. (3) What does the gradient physically represent about your ball? (4) If there's an intercept, what might it mean — or is it within your uncertainty of zero?

# Integration

**Mathematics**: y = kx and y = mx + c are the equations of lines you know from algebra — physics simply insists the constants have units and meanings. The "plot against t² to straighten the curve" trick is your first taste of changing variables, a technique that runs all the way to advanced mathematics.

**Data Engineering**: A line of best fit is the simplest model ever fitted to data — linear regression with one feature. Every dashboard trendline and forecast curve in industry is this lesson's idea industrialised, and the same warnings (noise, outliers, extrapolation) apply at every scale.

# Lore Conclusion

Thorne lets you unroll the drought chart fully, and you see what the margins hold: generations of chart-keepers re-plotting the same relationship as new data arrived, each line of best fit drawn in a different fading ink, all of them within a hair of each other. "Two hundred years of strangers, agreeing," Thorne says quietly. "That is what a good graph buys you." He rolls the vellum closed. "You can measure, design, record, and now read the shapes in data. One tool remains — the roughest and, I confess, my favourite. Tomorrow you learn to be *usefully wrong on purpose*. We call it estimation."
