---
id: de-app-m7-01
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
lesson: questions_and_metrics
title: "Questions and Metrics"
sortOrder: 1
difficulty: 2
estimatedMinutes: 25
xpReward: 35
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m6-09]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains the difference between a vague question and a precise, answerable data question
    - Defines what a metric is and identifies examples of good and poor metrics
    - Translates a business question into a SQL query structure
    - Identifies what data would be needed to answer a given business question
    - Reflects on why metrics can mislead if they measure the wrong thing
  keywords: [metric, question, measure, precise, business, KPI, proxy, misleading, SQL, translate, answerable]
  modelAnswer: |
    A metric is a quantifiable measure used to track progress toward a goal. Good metrics are precise (specific, measurable), relevant (measure what you actually care about), and honest (not gameable). Vague questions like "is the business doing well?" must be decomposed into precise questions: "what is the monthly revenue trend?" or "what is the customer churn rate this quarter?". Translating a business question into SQL requires identifying the table (what data), the aggregate (what measurement), the filter (which subset), and the grouping (which dimension). Poor metrics measure a proxy that can diverge from the real goal — optimising for the metric without improving the underlying reality.
guidedSteps:
  - id: de-app-m7-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A manager asks: "Are our customers happy?" Which question is the most useful reformulation for data analysis?
    inputConfig:
      options:
        - "Are our customers happy? (unchanged)"
        - "What is the average product rating this month, and how does it compare to last month?"
        - "Do customers like us?"
        - "Is customer happiness improving?"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["What is the average product rating this month, and how does it compare to last month?"]
      rejectedFeedback: "A good data question is specific and measurable. 'Are customers happy?' has no clear data source or measurement. 'What is the average product rating this month vs last month?' names the metric (average rating), the time dimension (this month vs last), and implies the data source (product_reviews table). It is answerable with a SQL query. The others are still vague."
    hint: "A good data question names a specific metric, a time period, and often a comparison."
    reflectionPrompt: "What data would you need in your database to answer the reformulated question?"
  - id: de-app-m7-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A metric that measures something related to the real goal but not the goal itself is called a ________ metric.
    inputConfig:
      placeholder: "proxy"
    markingRule:
      matchMode: CONTAINS
      accepted: [proxy, "proxy metric", surrogate, indirect]
      rejectedFeedback: "A proxy metric measures something correlated with the real goal but not the goal itself. Example: measuring 'lines of code written' as a proxy for 'software quality' — a developer can write more (bad) code to inflate the metric without improving quality. Proxy metrics are sometimes necessary when the real goal is hard to measure directly, but they carry risk: optimising the proxy can diverge from improving the real outcome."
    hint: "It stands in for the real goal when direct measurement is difficult."
    reflectionPrompt: "Give an example of a proxy metric used in education, and describe how it could be gamed."
  - id: de-app-m7-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain how the question "Why are sales down?" needs to be decomposed before it can be answered with data.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [specific, region, product, time, segment, category, which, where, when, decompose, narrow, precise]
      rejectedFeedback: "\"Sales are down\" is a vague observation. To answer it with data, decompose it: sales down in which region? which product category? compared to what time period? for which customer segment? Each decomposition turns the vague question into a specific, answerable query. You might find sales are down 40% in the North region for premium products in Q3 — a precise finding that suggests specific causes and actions."
    hint: "The question 'why are sales down?' needs who, what, where, and when before it can be answered."
    reflectionPrompt: "Write three specific SQL queries that together would help diagnose why overall sales are down."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of these is the best-defined metric for tracking customer retention?"
    options:
      - "Customer happiness"
      - "The percentage of customers who made a purchase in month N who also made a purchase in month N+1"
      - "How loyal our customers are"
      - "Customer satisfaction score (undefined)"
    correctIndex: 1
    feedback: "Option B defines the metric precisely: a specific formula (percentage), a specific event (made a purchase), and a specific time relationship (month N to N+1). It is unambiguous, calculable from database records, and comparable over time. The others are vague concepts without a measurement definition."
  - type: MULTIPLE_CHOICE
    question: "A company measures 'average session length' as a proxy for user engagement. What is the risk of this proxy metric?"
    options:
      - "It is too difficult to calculate"
      - "Sessions could be long because users are confused and struggling, not because they are engaged"
      - "It requires too many database tables"
      - "There is no risk — longer sessions always indicate better engagement"
    correctIndex: 1
    feedback: "A longer session can mean a user is engaged and enjoying the experience — or that they are confused, lost, or waiting for slow page loads. The proxy (session length) correlates with engagement in some scenarios and diverges in others. Good metric design questions whether the proxy truly reflects the underlying goal in all circumstances, not just the typical case."
retrieval:
  recall: "Translate the business question 'Are we acquiring customers efficiently?' into three specific, measurable data questions."
  explain: "Explain what a proxy metric is, give an example, and describe how it can mislead decision-making."
  mistakeId:
    code: "using page_views as the success metric for a content website"
    answer: "Page views measure traffic but not value — a visitor who lands on a page and immediately leaves (a bounce) still counts as a page view. A user who reads every article deeply may generate fewer page views than one who clicks rapidly and leaves each page quickly. Better metrics for content success: time on page, scroll depth, return visits, newsletter sign-ups, or conversion rate. Page views is a proxy that can diverge from actual user value."
---

# Hook

Data engineering is not just about storing and querying data — it is about answering questions. But the quality of the answer depends entirely on the quality of the question. Vague questions produce misleading answers. Precise questions produce actionable insights.

This lesson covers the discipline of turning fuzzy business questions into specific, measurable, answerable data questions.

# Lore Introduction

"The Guild Master wants to know if the Archive is successful," the Librarian reported. Master Selvaris set down her pen. "Successful how? More members? More transactions? Better member retention? Lower overdue rates? Higher satisfaction scores?" The Librarian stared. "She just said 'successful'." Selvaris opened her notebook. "Then our first job is to define what success means — before we run a single query. A vague question produces a misleading answer. We pick numbers that look good and call it success." She wrote four specific questions. "These are measurable. These produce SQL queries with clear interpretations. These I can answer honestly." She paused. "Data thinking starts before the database. It starts with the question."

# Core Learning

## Concept Introduction

### From Vague to Precise

```
Vague question:   "Is the business doing well?"
                  ↓ Decompose
Precise questions: 
  "What is monthly revenue this year vs last year?"
  "What is the customer churn rate this quarter?"
  "What percentage of orders are delivered on time?"
  "What is the average order value trend over 6 months?"
```

Each precise question:
- Names a specific **metric** (revenue, churn rate, on-time rate, AOV)
- Specifies a **time dimension** (this year vs last year, this quarter, 6 months)
- Is **answerable with SQL** against a real database

### What is a Metric?

A metric is a quantifiable measurement used to track performance toward a goal.

```
Good metric qualities:
  ✓ Specific       — has a precise definition
  ✓ Measurable     — can be calculated from data
  ✓ Comparable     — can be tracked over time or across segments
  ✓ Actionable     — change in the metric implies something you can do

Poor metric qualities:
  ✗ Vague          — "customer satisfaction" without a definition
  ✗ Gameable       — "lines of code" as a productivity metric
  ✗ Misleading     — measures the proxy, not the real goal
```

### Translating Business Questions to SQL

```
Business question: "Which products are we losing money on?"

SQL translation:
  - Table: products, order_lines
  - Metric: revenue - cost (profit per product)
  - Filter: WHERE profit < 0
  - Grouping: GROUP BY product_id, product_name
  - Sort: ORDER BY profit ASC

Query:
  SELECT p.name,
         SUM(ol.quantity * ol.unit_price)          AS revenue,
         SUM(ol.quantity * p.cost_price)           AS total_cost,
         SUM(ol.quantity * ol.unit_price) - SUM(ol.quantity * p.cost_price) AS profit
  FROM products p
  JOIN order_lines ol ON p.product_id = ol.product_id
  GROUP BY p.product_id, p.name
  HAVING SUM(ol.quantity * ol.unit_price) - SUM(ol.quantity * p.cost_price) < 0
  ORDER BY profit ASC;
```

### Proxy Metrics and Their Risks

A proxy metric measures something related to the real goal but not the goal itself:

| Real Goal | Proxy Metric | Risk |
|---|---|---|
| Student learning | Exam score | Teaching to the test; score improves without deeper understanding |
| User engagement | Page views | Confusing/poor UX inflates views; engaged users may view fewer pages |
| Code quality | Test coverage | Tests can be written to pass without testing meaningful behaviour |
| Employee productivity | Hours worked | Incentivises presence over output |
| Customer satisfaction | NPS score | Easy to game; outliers dominate; what drives the number? |

### The Decomposition Method

```
1. Start with the vague question
2. Ask: who, what, when, where, which?
3. Name a specific metric for each dimension
4. Verify the metric is in your database (or can be derived)
5. Write the SQL

"Why is churn increasing?"
→ Who is churning? (segment by customer age, region, product type)
→ When did they stop? (monthly cohort analysis)
→ What behaviour preceded churn? (last activity date, support tickets)
→ Which product/feature were they not using? (product usage data)
→ Produces 4–5 specific queries, each answering part of the whole
```

## Why It Matters

Data work begins before any query is written — with a question precise enough to answer. Vague questions produce confident-looking but useless numbers:

- "How are sales doing?" has no answer; "what was monthly revenue per region this year?" does
- Metrics are contracts: once a team agrees what "active user" means, every report must use that definition or chaos follows
- Most "wrong" dashboards aren't broken queries — they're correct answers to badly framed questions

Learning to turn fuzzy business concerns into measurable questions is what separates someone who runs queries from someone who produces insight.

## Common Mistakes

- **Skipping the question refinement step**: Jumping straight to "build a dashboard" without deciding what the dashboard should measure leads to metrics that nobody uses.
- **Measuring what is easy, not what matters**: Page views are easy to measure. Whether the content is valuable is harder. Easy metrics can displace meaningful ones.
- **Optimising the metric, not the goal**: Goodhart's Law — "When a measure becomes a target, it ceases to be a good measure." Metrics can be gamed.
- **Single-metric thinking**: One metric rarely tells the full story. Customer revenue without retention, or revenue without profit margin, both mislead.

## Mental Model

Think of a business question as a blurry photograph. Your job is to sharpen it until you can see the subject clearly. Decomposition is the focusing mechanism — each question narrows the frame. A sharply focused question has a clear subject (the metric), a clear background (the time and segment), and a clear foreground (the specific comparison or threshold). Only then can a SQL query — or any analysis — reliably answer it.

## Mini Summary

- ✔ Decompose vague questions into specific, measurable, time-bounded metrics
- ✔ A good metric is specific, measurable, comparable, and actionable
- ✔ Translate each metric to a SQL structure: table + aggregate + filter + GROUP BY
- ✔ Proxy metrics measure a correlate of the real goal — watch for divergence
- ✔ Goodhart's Law: when a measure becomes a target, it ceases to be a good measure

# Guided Practice Quest

Work through the guided steps to decompose a vague business question into three specific metrics, identify appropriate proxy metrics and their risks, and translate one metric into a complete SQL query structure.

# Solo Practice Quest

A retail company's leadership team has asked the data team for a "health check" on the business. You have been given the following vague questions: (1) "Are customers coming back?", (2) "Is marketing working?", (3) "Which products should we discontinue?", (4) "Are our operations efficient?". For each question: (a) decompose it into 2–3 specific, measurable data questions, (b) name the metric(s) and data source(s) required, (c) identify whether any metric is a proxy and what its risks are, (d) write the SQL query for one of the specific questions. Write a brief reflection on why data thinking — the work done before running any query — determines the quality of the analysis.

# Integration

**Mathematics**: The decomposition of a vague question into specific metrics is an application of operationalisation — the process of defining abstract concepts in terms of measurable observations. In statistics, this is the distinction between a construct (the unobservable concept: "customer satisfaction") and its indicators (measurable proxies: NPS score, repeat purchase rate, support ticket volume). Goodhart's Law has a formal mathematical analogue in optimisation theory: when you optimise for a proxy objective f(x) instead of the true objective g(x), the optimised solution maximises f but may perform poorly on g — especially when f and g are correlated only in a limited domain. This is the mathematical basis for reward hacking in machine learning systems.

**Sciences (Epidemiology)**: In public health, the decomposition of "is the population healthy?" into specific measurable metrics is the foundation of epidemiology. Metrics include: incidence rate (new cases per 100,000 per year), prevalence (total cases in population), mortality rate, years of life lost, and disability-adjusted life years (DALYs). Each is precisely defined, calculable from health records, and answers a specific question. The choice of metric matters enormously: optimising for mortality reduction may not improve quality of life; incidence rate reduction may not reduce hospitalisation. Public health policy has been distorted by optimising for the wrong metric — exactly Goodhart's Law applied to healthcare.

# Lore Conclusion

Four queries ran. Archive membership: up 12% year on year. Transaction volume per active member: down 3%. Members inactive for over 90 days: 153 (11%). Average overdue rate: 4.2%, up from 2.8% last year. "These numbers tell a specific story," Master Selvaris said. "Membership is growing but engagement is declining. The overdue rate suggests members are not returning items on time — possibly because they feel less invested." She closed the results. "The Guild Master asked if the Archive was successful. We could have shown a single number — total members — and called it success. Instead, we asked the right questions. The data now tells us exactly where to focus." She turned to her Apprentice. "The most important query is the one you decide to ask. Everything else is mechanics."

---
