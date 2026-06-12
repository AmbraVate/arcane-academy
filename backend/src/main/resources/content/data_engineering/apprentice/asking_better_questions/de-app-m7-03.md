---
id: de-app-m7-03
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m7
moduleTitle: "Module 7: Data Thinking"
moduleGlyph: "💡"
moduleSortOrder: 7
topicSlug: asking_better_questions
topicTitle: "Asking Better Questions"
topicSortOrder: 1
lesson: correlation_vs_causation
title: "Correlation vs Causation"
sortOrder: 3
difficulty: 2
estimatedMinutes: 25
xpReward: 35
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m7-02]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines correlation and causation in own words
    - Explains why correlation does not imply causation with a concrete example
    - Identifies a confounding variable in a given scenario
    - Describes what additional evidence is needed beyond correlation to suggest causation
    - Reflects on the danger of making data-driven decisions based on correlation alone
  keywords: [correlation, causation, confounding, spurious, association, experiment, evidence, third variable, mislead, decision]
  modelAnswer: |
    Correlation means two variables tend to move together — when one goes up, the other tends to go up or down. Causation means one variable directly causes change in another. Correlation does not imply causation because both variables may be caused by a third confounding variable, the causation may run in reverse, or the correlation may be spurious (coincidental). To move from correlation to causation requires: temporal ordering (cause precedes effect), mechanism (a plausible reason for the causal link), elimination of confounds (ruling out alternative explanations), and ideally an experiment (randomised controlled trial). Making decisions based on correlation alone risks investing in interventions that don't work.
guidedSteps:
  - id: de-app-m7-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A data analyst finds that customers who use the mobile app have 40% higher lifetime value than those who don't. A manager concludes: "We should force all customers to use the app to increase lifetime value." What is wrong with this reasoning?
    inputConfig:
      options:
        - "Nothing — the data clearly shows the app causes higher lifetime value"
        - "The sample size is too small to draw conclusions"
        - "App usage may correlate with higher value but not cause it — engaged customers may use the app AND spend more, both driven by greater overall engagement"
        - "The 40% figure is probably wrong"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["App usage may correlate with higher value but not cause it — engaged customers may use the app AND spend more, both driven by greater overall engagement"]
      rejectedFeedback: "This is the classic correlation/causation error. Customers who use the mobile app may simply be more engaged with the brand overall — both app usage and higher spending are effects of engagement, not cause and effect between each other. Forcing all customers to install the app would not replicate the engagement that drove the correlation. The confounding variable (customer engagement/loyalty) explains both the app usage and the higher spend."
    hint: "Could both high app usage and high spending be caused by a third factor — customer loyalty or engagement?"
    reflectionPrompt: "How would you design an experiment to test whether app usage actually causes higher spending?"
  - id: de-app-m7-03-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      When a third variable causes both X and Y to correlate, even though X does not cause Y, that third variable is called a ________ variable.
    inputConfig:
      placeholder: "confounding"
    markingRule:
      matchMode: CONTAINS
      accepted: [confounding, "confounding variable", confounder, lurking]
      rejectedFeedback: "A confounding variable (or confounder) is a third factor that causes both X and Y to appear correlated. Example: ice cream sales and drowning rates correlate — not because ice cream causes drowning, but because both are caused by hot weather (the confounder). In data analysis, identifying and controlling for confounders is essential before drawing causal conclusions from correlational data."
    hint: "A 'hidden' variable that drives both of the things you are observing to be correlated."
    reflectionPrompt: "Name the confounding variable in the classic correlation: 'towns with more hospitals have higher death rates.'"
  - id: de-app-m7-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain what additional evidence beyond correlation would be needed to argue that a new email marketing campaign is causing increased sales.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [before, after, control group, experiment, A/B, timing, preceded, mechanism, alternative, rule out, comparison]
      rejectedFeedback: "Beyond correlation (sales went up after the campaign), you need: (1) temporal ordering — sales increase followed the campaign, not preceded it; (2) a control group — a comparable group that did not receive the campaign and did not see the same sales increase; (3) elimination of confounders — no other change (price reduction, seasonal peak, competitor issue) coincided with the campaign; (4) ideally an A/B test — randomly assign some customers to receive the email and some not, then compare outcomes."
    hint: "What would rule out alternative explanations — that sales went up for some other reason coinciding with the campaign?"
    reflectionPrompt: "What is an A/B test, and why does randomisation help establish causation?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Ice cream sales and shark attacks both increase in summer. What is the most likely explanation?"
    options:
      - "Ice cream causes shark attacks"
      - "Shark attacks cause people to eat ice cream"
      - "Both are caused by hot weather and more people being at the beach in summer"
      - "It is a coincidence with no pattern"
    correctIndex: 2
    feedback: "This is a classic example of a confounding variable: hot summer weather causes both more ice cream sales and more beach attendance (which increases shark attack probability). Neither causes the other — both are caused by the confounder. This is sometimes called a 'spurious correlation'. The data correctly shows they correlate; the error is inferring causation."
  - type: MULTIPLE_CHOICE
    question: "A study finds that students who eat breakfast have higher exam scores. A school decides to provide free breakfast to improve scores. What assumption is this decision making?"
    options:
      - "Correlation implies causation — eating breakfast causes higher scores"
      - "The study has a large enough sample size"
      - "Breakfast is cheaper than other interventions"
      - "Exam scores are a good metric for academic success"
    correctIndex: 0
    feedback: "The decision assumes that the correlation (breakfast → high scores) is causal. But it may be confounded: students from more stable home environments both eat breakfast reliably AND perform better academically — both caused by family stability, not by breakfast itself. Providing breakfast may help in other ways (nutrition, concentration) but the evidence from this correlation alone does not establish that breakfast causes the score improvement."
retrieval:
  recall: "Describe three scenarios where two variables correlate but causation runs in the opposite direction to what seems intuitive."
  explain: "Explain the phrase 'correlation does not imply causation' using the concept of a confounding variable."
  mistakeId:
    code: "concluding from SQL data that users who read more documentation have fewer support tickets, therefore improving documentation will reduce support tickets"
    answer: "The correlation may exist, but the causal direction is uncertain. Users who read documentation may be more technically skilled overall — both reading docs and filing fewer tickets are effects of technical skill (a confounder). Alternatively, the correlation may be reversed: users file fewer tickets because documentation resolved their issues — which is causal, but only for users who already sought out documentation. To establish whether improving documentation reduces tickets, you need a controlled experiment: improve documentation for some users, not others, and compare ticket rates. Correlation is hypothesis-generating, not conclusion-generating."
---

# Hook

"Users who use feature X spend 30% more." "Stores near schools sell more of our product." "Members who attend more events have lower churn." These sound like actionable insights — but each could be a correlation with no causal relationship. Acting on them as if they were causal could waste significant resources.

Correlation vs causation is one of the most important concepts in data analysis. This lesson explains why the distinction matters and how to think about it.

# Lore Introduction

"High-ranked members have twice the transaction rate of low-ranked members," the Analyst reported. "Promote members to higher ranks and their spending will double." Master Selvaris read the report carefully. "Or," she said, "engaged members naturally transact more AND naturally earn higher ranks — both driven by engagement. Promoting a low-engagement member to a high rank does not make them more engaged." The Analyst frowned. "But the data—" "The data shows a correlation," Selvaris said. "Not a cause. The question is: what drives both rank and transaction rate?" She pulled up the member activity logs. "Time since joining and engagement score predict both. The rank correlation is real. The rank causation is not." She set down the report. "Before you act on a correlation, find the mechanism. If you cannot explain why X causes Y, you may be looking at a confounding variable."

# Core Learning

## Concept Introduction

### Correlation Defined

Two variables are correlated when they tend to move together — knowing the value of one gives information about the other:

```
Positive correlation: both increase together (height and weight)
Negative correlation: one increases as the other decreases (exercise and resting heart rate)
No correlation: knowing one tells you nothing about the other
```

Correlation is measurable from data. SQL can surface correlations:

```sql
-- Users who use feature X have higher revenue
SELECT
    CASE WHEN last_feature_x_use IS NOT NULL THEN 'Used X' ELSE 'Did not use X' END AS feature_group,
    COUNT(*)                                    AS user_count,
    ROUND(AVG(lifetime_revenue), 2)             AS avg_revenue
FROM users
GROUP BY 1;
```

### Causation Defined

Causation means X directly produces a change in Y — if you manipulate X, Y changes as a result.

```
Correlation ≠ Causation

Correlation is a statistical fact about two variables moving together.
Causation is a claim about a mechanism — X changes, therefore Y changes.
```

### Why Correlation Doesn't Imply Causation

Three reasons a correlation may not be causal:

**1. Confounding variable**: A third factor causes both X and Y

```
Correlation: mobile app users have higher lifetime value
Confound:    highly engaged customers both use the app AND spend more
             (engagement → app usage; engagement → high spend)
             forcing non-engaged customers to use the app ≠ engagement
```

**2. Reverse causation**: Y causes X, not X causes Y

```
Correlation: hospitals in towns with more deaths have more beds
Causation (wrong): "hospitals cause deaths"
Reality: sicker towns build bigger hospitals — illness causes hospital size, not the reverse
```

**3. Spurious correlation**: random coincidence, no mechanism

```
Historical data shows: US per-capita cheese consumption correlates with
number of people who died by becoming tangled in their bedsheets.
(Tyler Vigen, "Spurious Correlations")
No mechanism → pure coincidence in the data
```

### What Additional Evidence Suggests Causation

| Requirement | Description |
|---|---|
| Temporal ordering | The cause must precede the effect in time |
| Mechanism | A plausible explanation for why X would cause Y |
| Dose-response | More X → more Y (suggests a real relationship) |
| Elimination of confounds | Rule out alternative explanations |
| Experiment | Randomised controlled trial: randomly assign X, measure Y |

### A/B Testing — The Gold Standard

```
Without randomisation (observational study):
  Customers who received email → 12% conversion rate
  Customers who did not → 8% conversion rate
  Problem: the email was sent to more engaged customers (confound)

With randomisation (A/B test):
  Randomly assign 50% of customers to receive email (treatment)
  50% do not receive email (control)
  Both groups are otherwise comparable (randomisation eliminates confounds)
  Treatment: 12% conversion; Control: 9% conversion
  → The 3% difference is now attributable to the email (causation)
```

### SQL Can Show Correlations — Not Causation

SQL is excellent at showing that two things are associated. It cannot establish causation on its own:

```sql
-- This query shows a correlation
SELECT
    completed_onboarding,
    COUNT(*)                    AS users,
    ROUND(AVG(revenue_90_days), 2) AS avg_90_day_revenue
FROM users
GROUP BY completed_onboarding;

-- This correlation might mean:
-- (a) Onboarding causes higher revenue (causal)
-- (b) Motivated users both complete onboarding AND spend more (confound)
-- (c) High-revenue users are more likely to complete onboarding (reverse causation)
-- SQL alone cannot distinguish between these interpretations
```

## Why It Matters

Confusing correlation with causation is the most expensive reasoning error in data work, because it leads to confident actions that don't work:

- Ice-cream sales and drownings rise together — banning ice cream saves no one; summer causes both
- A marketing campaign "drives" signups that were actually seasonal growth, so budget gets poured into the wrong channel
- Health, hiring, and product decisions based on correlations regularly reverse when a hidden third factor is found

Every stakeholder you ever work with will be tempted by this shortcut. Being the person who asks "what else could explain this?" is a professional superpower.

## Common Mistakes

- **"The data shows it"**: Data shows correlations. Causation requires additional reasoning and ideally an experiment.
- **Acting on correlations without a mechanism**: If you cannot explain why X would cause Y, the correlation may be confounded or spurious.
- **Ignoring reverse causation**: Always ask: could Y be causing X, rather than X causing Y?
- **Ecological fallacy**: A correlation at the group level does not necessarily hold at the individual level. A town with high income and high crime doesn't mean rich individuals commit more crime.

## Mental Model

Correlation is like finding two clocks that show the same time. They are synchronised — knowing one tells you the other. But they do not cause each other — someone set them both (a confounding force). Causation is like finding a clock and its alarm: the alarm rings because the clock reached a time. One directly triggers the other. The challenge is distinguishing which type of relationship you are looking at when all you have is the data.

## Mini Summary

- ✔ Correlation: variables move together; does not imply one causes the other
- ✔ Three reasons for non-causal correlation: confounding, reverse causation, spurious
- ✔ Establishing causation requires: mechanism, temporal ordering, controlled comparison
- ✔ A/B tests (randomised experiments) are the gold standard for causal inference
- ✔ SQL reveals correlations — it cannot by itself establish causation

# Guided Practice Quest

Work through the guided steps to identify whether a given correlation is likely causal, name the confounding variable in a scenario, and design a simple A/B test structure that would help establish causation.

# Solo Practice Quest

For each of the following correlations found in a database, explain: (a) what the correlation is, (b) a plausible confounding variable that could explain it without causation, (c) a reverse causation explanation, (d) what experiment you would design to test whether the relationship is truly causal. Scenarios: (1) customers who have been members longer buy more expensive products, (2) products with more reviews have higher sales, (3) employees who attend more training sessions have higher performance scores, (4) users who log in more frequently cancel their subscriptions at a lower rate, (5) regions with more stores per capita have higher brand recognition scores. Then write a SQL query for one scenario that surfaces the correlation, and annotate it with comments explaining what interpretation the data alone cannot tell you.

# Integration

**Mathematics**: Correlation is formally measured by the Pearson correlation coefficient r ∈ [-1, 1], defined as r = Cov(X,Y) / (σ_X × σ_Y), where Cov is the covariance and σ is the standard deviation. r = 1 is perfect positive correlation, r = -1 perfect negative, r = 0 no linear relationship. But even r = 1 does not imply causation — the mathematical formula measures co-variation, not causal mechanism. The formal framework for causal inference (Pearl's do-calculus) distinguishes P(Y|X=x) (observational — the correlation) from P(Y|do(X=x)) (interventional — the causal effect of setting X=x). SQL computes P(Y|X=x); establishing P(Y|do(X=x)) requires randomised experiments or structural causal models.

**Sciences (Medicine — Evidence-Based Practice)**: The history of medicine is filled with cases where correlational evidence led to harmful interventions. For decades, hormone replacement therapy (HRT) was prescribed to post-menopausal women based on observational studies showing lower heart disease rates in users — until randomised trials (the Women's Health Initiative, 2002) found HRT increased cardiovascular risk. The confound: women who sought out HRT were healthier and higher socioeconomic status overall. The observational correlation was real; the causal direction was opposite to what was believed. This episode is the medical evidence for why correlation without controlled experiment is insufficient for clinical decisions — the same principle applied to business data decisions.

# Lore Conclusion

"The rank promotion proposal has been withdrawn," the Analyst reported. "After your analysis, the Guild Council agreed — correlation, not causation. We are testing an engagement programme instead." Master Selvaris nodded. "The rank promotion would have cost 200 gold and likely changed nothing about transaction rates. The engagement programme is a test — structured with a control group. In three months, we will have causal evidence, not just correlation." She paused. "This is the discipline: find the correlation, then find the mechanism, then test. Data shows you where to look. It does not tell you what to do. That requires thinking."

---
