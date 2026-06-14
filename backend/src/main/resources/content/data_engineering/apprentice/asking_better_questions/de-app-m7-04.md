---
id: de-app-m7-04
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
lesson: evidence_based_decisions
title: "Evidence-Based Decisions"
sortOrder: 4
difficulty: 2
estimatedMinutes: 25
xpReward: 40
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m7-03]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what evidence-based decision making means in a data context
    - Describes the difference between data-informed and data-driven decisions
    - Identifies the limitations of data as the sole basis for decisions
    - Explains what "base rate" means and why it matters when interpreting results
    - Reflects on how data engineers support better decisions rather than making them
  keywords: [evidence, decision, base rate, data-informed, data-driven, context, limitation, bias, sample, interpretation]
  modelAnswer: |
    Evidence-based decision making uses data to inform decisions, combining quantitative findings with domain knowledge, context, and judgment. Being "data-informed" means data is one input among several; being "data-driven" implies data is the sole driver (which can be problematic). Data has limitations: it reflects only what was measured, can contain bias, may lack context, and cannot capture values or ethics. The base rate matters: a 1% improvement in a high-volume process has different significance than a 1% improvement in a rare event. Data engineers support decisions by ensuring data quality, asking clarifying questions about what is being measured, and flagging limitations in the analysis.
guidedSteps:
  - id: de-app-m7-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A data engineer presents findings: "Our top 10% of customers generate 60% of revenue." A manager responds: "Then we should ignore the bottom 90% and focus all resources on the top 10%." What important consideration is the manager missing?
    inputConfig:
      options:
        - "The analysis is incorrect — customers cannot generate 60% of revenue"
        - "The bottom 90% include future top customers, referral sources, and volume that may support fixed costs"
        - "The data engineer should have used a different query"
        - "Revenue concentration is unusual and probably a data error"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The bottom 90% include future top customers, referral sources, and volume that may support fixed costs"]
      rejectedFeedback: "The data finding is correct, but the decision ignores context: today's bottom 90% include tomorrow's top customers (early-stage customers who haven't bought much yet), word-of-mouth referral sources, customers who provide volume needed to justify certain fixed costs, and diversity that reduces single-customer risk. Data shows a pattern; the decision requires judgment about what that pattern means for strategy. Data engineers surface the finding; strategic decisions require broader context."
    hint: "The data is correct. The interpretation is too narrow — what does the bottom 90% represent that pure revenue data doesn't capture?"
    reflectionPrompt: "What additional data would help make a better decision about which customer segments to focus on?"
  - id: de-app-m7-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      When a 5% click-through rate sounds impressive, but the industry average is 12%, the industry average is called the ________ rate and changes the interpretation entirely.
    inputConfig:
      placeholder: "base"
    markingRule:
      matchMode: CONTAINS
      accepted: [base, "base rate", baseline, benchmark]
      rejectedFeedback: "A base rate (or baseline) is the reference point that gives a number its meaning. A 5% click-through rate sounds high in isolation but is poor if competitors achieve 12%. Similarly, a 2% cancer screening false positive rate sounds small until you consider that with a base rate of 1 in 10,000 cancer incidence, most 'positive' results will be false positives. Always ask: compared to what? The base rate provides the context that makes a metric interpretable."
    hint: "The reference number that gives your metric its meaning — what it should be compared against."
    reflectionPrompt: "What is the base rate for order cancellations in your industry, and how would knowing it change how you interpret your own cancellation rate?"
  - id: de-app-m7-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why data alone cannot determine whether a company should lay off 20% of its workforce to reduce costs, even if the cost savings are clearly shown in the data.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [values, ethics, human, morale, long-term, culture, context, judgment, non-quantifiable, trust, people]
      rejectedFeedback: "Data can quantify the immediate cost savings but cannot capture: the impact on employee morale and productivity of those who remain, the long-term cost of losing institutional knowledge, the reputational damage with customers and future recruits, the ethical dimensions of the decision, or the trust relationship with employees. These are real and consequential factors that data does not — and cannot — measure. Data informs the financial dimension; the decision requires judgment across all dimensions."
    hint: "What aspects of this decision — important ones — cannot be measured in a database?"
    reflectionPrompt: "Name three consequential business decisions where data should inform but not solely determine the outcome."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the difference between 'data-driven' and 'data-informed' decision making?"
    options:
      - "They mean the same thing"
      - "Data-driven means data is the sole input; data-informed means data is one of several inputs alongside context, judgment, and values"
      - "Data-driven is better because it removes human bias"
      - "Data-informed means the data was gathered informally"
    correctIndex: 1
    feedback: "Data-driven implies data alone determines the decision. Data-informed means data is a key input but is combined with domain knowledge, context, ethical considerations, and human judgment. Most thoughtful organisations aim to be data-informed rather than purely data-driven — because data cannot capture everything that matters, and pure data-driven optimisation can lead to unintended consequences (maximise the metric while undermining the goal)."
  - type: MULTIPLE_CHOICE
    question: "A data engineer's role in decision-making is best described as:"
    options:
      - "Making the decision based on the data they analyse"
      - "Providing accurate, well-framed data to inform decision-makers, while flagging limitations"
      - "Only querying the database and presenting raw numbers"
      - "Telling stakeholders what the data means and what they should do"
    correctIndex: 1
    feedback: "Data engineers and analysts provide the factual foundation: accurate data, clear metrics, and honest framing of limitations (what the data shows and what it doesn't). Decision-makers combine this with domain knowledge, strategy, ethics, and judgment. The data professional's job is to make the data as clear and honest as possible — not to make the decision, and not to hide limitations to make the data seem more conclusive than it is."
retrieval:
  recall: "Describe three limitations of data that a data engineer should flag when presenting analysis to a decision-maker."
  explain: "Explain what a base rate is and give two examples of how ignoring the base rate can lead to a misleading interpretation."
  mistakeId:
    code: "presenting a 15% increase in sign-ups to leadership without mentioning this follows a period of zero marketing spend recovering to baseline"
    answer: "Without context, '15% increase' sounds like success. But if it is simply recovering from an anomalous low period (caused by paused marketing spend), it may represent no real improvement — just regression to the mean. Data engineers and analysts have a responsibility to provide context alongside metrics: what was the baseline, what changed in the environment, what alternative explanations exist? Presenting numbers without context is a form of misleading communication, even if the numbers are accurate."
---

# Hook

Data engineering is not about generating reports — it is about supporting better decisions. But data alone cannot make decisions. Numbers need context, comparison, domain knowledge, and judgment to become actionable insight.

This final lesson of the Apprentice tier brings together everything learned about data thinking: asking precise questions, recognising patterns, understanding correlation, and now — using data responsibly to support decisions.

# Lore Introduction

"The analysis is complete," Master Selvaris said, presenting the final report to the Guild Council. "Three months of data. Member retention is down 8% year on year. Top 10% of members generate 61% of revenue. New member acquisition costs have risen 23%." Silence. Then the Guild Master: "What should we do?" Selvaris paused. "The data tells you where the problems are. It does not tell you the right response — that requires judgment about what kind of guild you want to be, what you can afford to invest, and what risks you are willing to accept." She set down the report. "My job is to give you accurate, honest data with clear limitations. Your job is to decide. The data is the best foundation I can give you. But it is a foundation, not a decision."

# Core Learning

## Concept Introduction

### Evidence-Based Decision Making

Evidence-based decisions use data as a key input, combined with:

```
Data        → What happened / what is happening now?
Context     → Why might this be happening?
Domain knowledge → What do we know from experience and research?
Values/Ethics    → What should we do given our principles?
Judgment    → How do we weigh competing considerations?

Decision    = Data + Context + Knowledge + Values + Judgment
```

### Data-Driven vs Data-Informed

| | Data-Driven | Data-Informed |
|---|---|---|
| Data's role | Sole basis for decision | One important input among several |
| Risk | Optimises the metric, may miss what matters | May underweight clear data signals |
| When appropriate | Tactical, high-volume, low-stakes decisions | Strategic, complex, high-stakes decisions |
| Example | Auto-pricing algorithm based on demand | Deciding whether to enter a new market |

### Base Rates — The Missing Context

A number without a reference point is often uninterpretable:

```
Finding:          "Our 5% email open rate"
Without context:  Is this good or bad?
With base rate:   Industry average is 21% → this is poor
With industry:    For transactional emails: 42% is typical → this is very poor

Finding:          "We reduced churn by 2 percentage points (from 10% to 8%)"
Without context:  Is this meaningful?
With base rate:   Industry average churn is 5% → still above average, more work needed
With scale:       On 100,000 customers → 2,000 more retained customers per year
                  At £200 average LTV → £400,000 revenue impact
```

SQL can compute your own base rates and comparisons:

```sql
-- Compare current month churn to 12-month rolling average (your own base rate)
WITH monthly_churn AS (
    SELECT
        DATE_TRUNC('month', cancellation_date) AS month,
        COUNT(*) AS churned_customers
    FROM cancellations
    GROUP BY 1
)
SELECT
    month,
    churned_customers,
    ROUND(AVG(churned_customers) OVER (
        ORDER BY month
        ROWS BETWEEN 11 PRECEDING AND CURRENT ROW
    ), 0) AS rolling_12m_avg
FROM monthly_churn
ORDER BY month;
```

### Limitations Data Engineers Should Flag

When presenting analysis, always communicate what the data cannot tell you:

```
1. Coverage: "This data only covers online orders — in-store purchases are not included"
2. Recency: "Customer data was last updated 3 months ago — some records may be stale"
3. Selection bias: "Survey responses came from 3% of customers — likely the most engaged"
4. Missing data: "15% of orders have no region data — region analysis may be incomplete"
5. Measurement error: "Customer satisfaction scores from a single survey may not represent all customers"
6. Confounding: "The correlation between X and Y may be driven by Z (a confounder we haven't controlled for)"
7. What data doesn't measure: "We have no data on why customers cancelled — only that they did"
```

### The Data Engineer's Role in Decisions

```
What data engineers should do:
  ✓ Provide accurate, queryable, trustworthy data
  ✓ Frame findings clearly and honestly
  ✓ Flag limitations, caveats, and alternative interpretations
  ✓ Distinguish between what the data shows and what it proves
  ✓ Ask clarifying questions about what decision the analysis should support
  ✓ Suggest what additional data would strengthen the analysis

What data engineers should not do:
  ✗ Conceal limitations to make findings look more conclusive
  ✗ Present correlation as causation
  ✗ Make strategic decisions that are above the scope of the analysis
  ✗ Cherry-pick data to support a predetermined conclusion
  ✗ Over-claim certainty in the findings
```

### When Data and Judgment Conflict

Sometimes data points one way and experienced judgment points another:

```
Good response:
  1. Take both seriously — neither is automatically right
  2. Understand the source of the disagreement
  3. What does the data not capture that the expert knows?
  4. Is the expert's intuition based on outdated experience?
  5. Design a test that would resolve the disagreement

Bad response (A): "The data says X, therefore we do X" (ignores valid domain knowledge)
Bad response (B): "My gut says Y, therefore we do Y" (ignores valid data signals)
```

## Why It Matters

Organisations say they want data-driven decisions, but the default human mode is decision-driven data — picking numbers that support what was already decided.

- Evidence-based practice forces the uncomfortable questions: what would change our mind? what data would prove us wrong?
- Decisions anchored to agreed metrics survive personnel changes and politics; gut-feel decisions don't
- The discipline of stating expected outcomes *before* looking at results is what keeps analysis honest

This habit is what makes the difference between data as decoration and data as the actual steering wheel.

## Common Mistakes

- **Treating every data finding as an action item**: Not every finding requires action. Some findings describe the status quo and need monitoring, not intervention.
- **Hiding limitations to appear more confident**: A data engineer who hides caveats is undermining the decision-maker. Honest uncertainty is more useful than false confidence.
- **Ignoring base rates**: A conversion rate, churn rate, or error rate is only meaningful relative to a benchmark.
- **Data as cover for a predetermined decision**: Using data selectively to justify a decision already made, rather than to inform one. This is manipulation, not analysis.

## Mental Model

Think of data as a torch in a dark room. It illuminates part of the room — the parts you aimed the torch at. It doesn't show you the whole room. The decision-maker has to navigate the room, and the torch is invaluable — but they must also remember that there may be furniture in the parts that weren't lit up. The data engineer's job is to aim the torch well, tell the decision-maker what the beam shows, and honestly acknowledge where the beam doesn't reach. The navigation — the decision — belongs to the person who has to walk through the room.

## Mini Summary

- ✔ Evidence-based decisions combine data with context, domain knowledge, and judgment
- ✔ Data-informed > data-driven for complex, strategic decisions
- ✔ Always provide base rates — numbers without comparison are hard to interpret
- ✔ Flag limitations: coverage, recency, bias, missing data, and what data doesn't measure
- ✔ Data engineers support decisions by providing honest, well-framed data — not by making the decisions

# Guided Practice Quest

Work through the guided steps to identify missing context in a data finding, calculate the base rate for a given metric, and frame a data limitation that should be disclosed to a decision-maker.

# Solo Practice Quest

You are presenting the results of a three-month analysis of a subscription service to the leadership team. Your findings: (1) churn is 8% per month, (2) users who use the mobile app have 40% lower churn, (3) customer acquisition cost has risen 15% this quarter, (4) top 5% of users generate 45% of revenue, (5) users who contact support have 60% higher churn. For each finding: (a) provide one piece of context or base rate that would change its interpretation, (b) name one limitation of the data that the leadership team should know, (c) describe what decision the finding might inform and what additional information would be needed before acting. Then write a brief "data caveats" paragraph you would include at the end of your report — the kind of honest framing that makes analysis trustworthy.

# Integration

**Mathematics**: Evidence-based decision making under uncertainty is formalised in Bayesian decision theory. The framework: prior beliefs (base rates), combined with likelihood of evidence given each hypothesis (the data), produce posterior beliefs, which are then combined with a loss function (the cost of different types of errors) to select the optimal action. This formally captures the structure described in this lesson: data (likelihood) updates prior beliefs (base rate + context) to produce a posterior (revised understanding), which informs decisions (optimal action under the posterior). The base rate corresponds to the prior distribution — ignoring it is the "base rate fallacy" that leads to systematic reasoning errors in medicine, law, and business.

**Sciences (Medicine — Evidence Hierarchy)**: Medicine developed a formal "hierarchy of evidence" to guide clinical decisions: at the top, systematic reviews and meta-analyses of randomised controlled trials; followed by individual RCTs; observational studies; case reports; expert opinion at the bottom. This hierarchy reflects exactly the principles in this lesson: not all evidence is equal, correlation (observational studies) is weaker than causation established by RCTs, and expert judgment (opinion) is the least reliable form of evidence. The medical concept of "number needed to treat" (NNT) — how many patients must be treated for one to benefit — is a base-rate-adjusted metric that makes the magnitude of an effect clinically interpretable. Data-informed medicine is the evidence-based practice model adopted worldwide.

# Lore Conclusion

"The Guild Council has decided," the Guild Master announced. "Based on the analysis, we are investing in an engagement programme for mid-tier members — targeted, time-limited, with a control group so we can measure its actual effect." Master Selvaris reviewed the decision. "They used the data as a foundation," she said. "But they also considered what kind of guild they want to be, the risk of alienating existing members, and the cost they could afford." She turned to her Apprentice. "This is how data should be used. Not as a replacement for judgment — as a better foundation for it." She closed her notebook. "You have now completed the Apprentice tier. You can structure data, query it, join it, aggregate it, design the schemas that hold it, and think critically about what it means." She paused. "The technical skills are the beginning. The thinking is what makes them valuable."

---
