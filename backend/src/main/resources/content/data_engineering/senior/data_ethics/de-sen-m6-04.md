---
id: de-sen-m6-04
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m6
moduleTitle: "Module 6: Data Governance"
moduleGlyph: "🏛️"
moduleSortOrder: 6
topicSlug: data_ethics
topicTitle: "Data Ethics"
topicSortOrder: 4
lesson: 4
title: "Data Ethics: Engineering Choices Have Human Consequences"
sortOrder: 4
difficulty: 4
estimatedMinutes: 35
xpReward: 75
practiceType: GUIDED_AND_SOLO
questType: RETRIEVAL_CHALLENGE
retrievalWeight: 0.6
questTypes:
  - guided
  - solo
  - retrieval
prerequisites:
  - de-sen-m6-03
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies how data collection and use can cause harm beyond legal compliance"
    - "Explains algorithmic bias and how it can emerge from training data"
    - "Describes the data ethics review process for high-impact data decisions"
    - "Articulates the engineer's responsibility beyond technical correctness"
  keywords:
    - algorithmic bias
    - training data bias
    - proxy discrimination
    - fairness
    - data ethics review
    - harm
    - consent
    - transparency
  modelAnswer: |
    Legal compliance is the floor of responsible data use, not the ceiling. Data can be legal but still harmful: reinforcing discrimination, enabling surveillance, excluding marginalised groups from services, or using data in ways users didn't anticipate or consent to in spirit.
    Algorithmic bias emerges when training data reflects historical inequalities. A model trained on historical loan approvals where a protected group was systematically rejected will learn to reject that group — perpetuating the discrimination. Proxy variables (postcode, education level, device type) can correlate with protected characteristics, causing illegal disparate impact even without explicit discrimination.
    A data ethics review evaluates: who is affected by this data decision, how could it harm them, are the affected communities represented in the decision, and what mechanisms exist for challenge and redress. It goes beyond legal compliance to ask whether the use is fair and proportionate.
    Engineers have responsibility beyond technical correctness. "I just built what I was asked to build" is not an ethical position. Engineers who identify harmful consequences have a professional responsibility to raise them — to say "this could discriminate against users in group X" — even when the system would be technically functional and legally compliant.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "A recommendation system trained on historical lesson completion data recommends fewer advanced lessons to users from lower-income postcodes. The model uses postcode as a feature. Why is this problematic even if postcode is not a protected characteristic?"
    options:
      - "It's only problematic if postcode correlates with race, which must be proved before action is taken"
      - "Postcode is a proxy variable that correlates with protected characteristics (race, income, disability). The model produces disparate impact — effectively discriminating — without using a protected variable directly"
      - "Recommendation systems are excluded from equality law because they are not explicit decisions"
      - "It is acceptable because historical completion rates are objective data"
    correctIndex: 1
    explanation: "Proxy discrimination is illegal under UK equality law (Equality Act 2010) and EU law. A postcode that correlates with ethnicity or socioeconomic status can produce disparate impact — the effect is discriminatory even if the intent was not. 'Objective' historical data is not neutral if it reflects structural inequalities. The model must be audited for disparate impact across protected groups, regardless of whether protected characteristics appear directly in the features."
  - type: FILL_BLANK
    question: "When a model trained on biased historical decisions perpetuates and amplifies those biases in future automated decisions, this is called a ___ loop."
    answer: "feedback"
    explanation: "A feedback loop: biased historical data → biased model → biased decisions → biased outcomes → biased training data for the next model. Each cycle reinforces the original bias. Breaking the loop requires: auditing training data for historical bias, collecting representative counter-examples, monitoring deployed model outcomes by demographic group, and retraining with corrected data."
  - type: SHORT_TEXT
    question: "The Consortium wants to use ML to predict which learners are likely to churn and target them with interventions. What ethical considerations must be evaluated before building this system?"
    modelAnswer: "1. Disparate impact: does the model predict churn differently across protected groups (age, disability, ethnicity)? If so, interventions may be distributed inequitably. 2. Consent and transparency: were learners told their behaviour data would be used to predict churn? Is this within the purpose they consented to? 3. Harm from false positives/negatives: labelling a learner as 'high churn risk' may cause staff to invest differently in them — a self-fulfilling prophecy or unfair resource allocation. 4. Right to explanation: if a learner is denied an intervention based on a model prediction, do they have the right to know and challenge the decision? 5. Feedback loop: if interventions are only offered to predicted-churners, the training data becomes biased toward interventions working only for that group."
microCheckpoint:
  question: "Why is 'the data is objective' not a defence against algorithmic bias?"
  answer: "Historical data reflects historical decisions, which may have been systematically biased. A model trained on biased historical outcomes learns to reproduce those outcomes. 'Objective' does not mean 'unbiased' — it means accurately reflecting the historical record, including its inequalities."
retrieval:
  recall: "What is proxy discrimination and how does it arise in ML systems?"
  explain: "Explain what an ethics review adds that a legal compliance review does not."
  mistakeId: "data-ethics-legal-compliance-sufficient"
---

# The Biased Recommendation

The recommendation model had been running for three months. The data scientist ran a disparity analysis. Users from postcodes in the bottom income quintile were recommended beginner-level lessons at twice the rate of users with identical completion histories from higher-income postcodes. The model was doing exactly what it had been trained to do. The training data reflected historical patterns. And the historical patterns reflected structural inequality. "The model is technically correct," the Senior Engineer said. "And it's making things worse." This was an ethics problem, not just a technical one.

# Beyond Legal Compliance

GDPR and equality law set the floor. Ethics asks: even if this is legal, is it right?

```
Legal compliance: "We have consent to use this data."
Ethics: "Did users understand and genuinely agree, or was consent buried in 
         a 40-page terms document they had no meaningful choice but to accept?"

Legal compliance: "We don't use protected characteristics in our model."
Ethics: "Our model uses postcode which correlates with ethnicity in this city.
         The effect is discriminatory even if the feature isn't explicitly so."

Legal compliance: "Our automated decisions comply with Article 22 GDPR."
Ethics: "Do users from marginalised groups experience significantly different
         outcomes from our system? Do they have meaningful recourse?"
```

## Algorithmic Bias

Algorithmic bias is the production of systematically unfair outcomes for specific groups from an automated system.

### Sources of Bias in Training Data

```
Historical lending decisions → model → loan approvals
│
├── Historical biases (minority groups historically denied loans)
│   → model learns protected_group ≈ high risk
│   → perpetuates discrimination
│
├── Proxy variables (postcode correlates with ethnicity)
│   → model discriminates via proxy
│   → illegal disparate impact
│
├── Representation bias (training data underrepresents certain groups)
│   → model performs poorly for those groups
│   → equitable performance not achieved
│
└── Feedback loop (model outputs become future training data)
    → bias amplifies with each cycle
```

### Measuring Fairness

There is no single definition of algorithmic fairness — these are often mathematically incompatible:

| Fairness Criterion | Definition |
|---|---|
| **Demographic parity** | Equal positive outcome rate across groups |
| **Equal opportunity** | Equal true positive rate (recall) across groups |
| **Predictive parity** | Equal precision across groups |
| **Individual fairness** | Similar individuals receive similar treatment |

Calibrate for the criterion that matches the domain's harm model. In educational recommendations: equal opportunity (no group is systematically under-recommended) is typically the relevant criterion.

```python
# Fairness audit (pseudocode — using Fairlearn or Aequitas)
from fairlearn.metrics import MetricFrame

metric_frame = MetricFrame(
    metrics={"completion_rate": completion_rate},
    y_true=outcomes,
    y_pred=recommendations,
    sensitive_features=learner_data["income_quintile"]
)

print(metric_frame.by_group)
# output: disparities in recommendation rates by income quintile
# threshold: maximum 5% disparity per organisation policy
```

## Data Ethics Review

A data ethics review is a structured pre-deployment assessment for high-impact data systems.

```
Ethics Review Checklist:

1. AFFECTED PARTIES
   Who is affected by this system?
   Are marginalised or vulnerable groups disproportionately affected?
   Are affected communities represented in the design process?

2. POTENTIAL HARMS
   What harms could result from false positives? False negatives?
   Who bears the cost of errors?
   Could the system be misused for purposes beyond its stated intent?

3. CONSENT AND TRANSPARENCY
   Do users understand how their data is used?
   Is this use within the spirit of what they agreed to?
   Can users see why a decision was made about them?

4. FAIRNESS
   Are outcomes equitable across protected groups?
   Does any feature serve as a proxy for protected characteristics?
   Was the model audited for disparate impact?

5. REDRESS
   Can affected individuals challenge a decision?
   Is there a human review path for contested decisions?
   How are errors corrected?

6. ONGOING MONITORING
   How will the system be monitored post-deployment?
   What triggers a re-evaluation?
```

## The Engineer's Responsibility

"I just built what I was asked to build" is not an ethical position. Engineers are not neutral executors — they make choices that have human consequences.

**Responsibilities:**
- **Raise concerns**: if you identify a potential harm, say so — in the design review, in the PR comment, in the team meeting. Document it.
- **Propose alternatives**: "We could achieve the same goal without this proxy variable."
- **Document limitations**: model cards, data sheets, ethics review documentation.
- **Refuse clearly harmful work**: some requests should not be built, regardless of legality.

```
Engineering standard of care (analogous to medicine / law):
  A doctor who performs a harmful procedure cannot escape accountability
  by saying "the patient's family asked me to."
  
  A data engineer who builds a discriminatory system cannot escape
  accountability by saying "product management specified the model."
  
  Professional responsibility includes identifying harm and refusing
  or escalating when harm cannot be mitigated.
```

## Common Mistakes

> **Treating Legal Compliance as Sufficient**
> "We checked with legal and it's fine" closes the compliance conversation, not the ethics conversation. Legality is the minimum bar.

> **"The Algorithm is Neutral"**
> Algorithms trained on human data reflect human decisions and their embedded biases. No algorithm applied to social data is neutral. Neutrality claims suppress critical examination.

> **Bias Discovery After Deployment**
> Bias audits conducted only after a system has been running for months affect real people during that period. Ethics review and bias auditing must happen before deployment — part of the acceptance criteria, not a retrospective audit.

> **No Redress Mechanism**
> A system that affects individuals without a mechanism for them to challenge, understand, or appeal decisions violates both GDPR Article 22 and basic fairness norms.

## Mental Model

Think of data ethics as **professional engineering standards**. Civil engineers don't build bridges by following only the building code minimum — they apply professional judgment about safety margins, failure modes, and community impact. A bridge that's technically legal but will collapse in a 1-in-100-year storm is a failure of professional responsibility, not just technical compliance. Data systems that are legally compliant but discriminatory, harmful, or deceptive are the equivalent: within the rules, but failures of professional responsibility.

**Mini Summary**: Legal compliance is the floor, not the ceiling of ethical data use. Algorithmic bias emerges from biased training data through historical discrimination, proxy variables, and feedback loops. Fairness has multiple incompatible mathematical definitions — choose the criterion that matches the domain's harm model. Ethics reviews evaluate affected parties, potential harms, consent, fairness, and redress. Engineers have professional responsibility beyond technical correctness.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium's data team has been asked to build a system that automatically flags learners predicted to "struggle" with senior-level content based on their junior-level performance data, and diverts them to additional remedial modules before progression.

Conduct a data ethics review:
1. Who is affected and what harms could arise from false positives and false negatives?
2. What proxy variable risks exist in junior-level performance data?
3. Is informed consent adequate if learners only see "personalised learning path" in the UI?
4. What redress mechanism should exist if a learner disagrees with their classification?
5. Would you build this system as specified, modify it, or refuse? Justify.

---

# Integration

**Mathematics**: The impossibility theorem of algorithmic fairness (Chouldechova 2016, Kleinberg et al. 2016) proves that **demographic parity, equal opportunity, and predictive parity cannot all hold simultaneously** when base rates differ across groups — unless the classifier is perfect. This is a mathematical theorem, not an engineering limitation: you must choose which fairness criterion to prioritise, because you cannot satisfy all of them at once. This is why ethics review requires domain judgment: which harms are worse — false positives (over-flagging low-risk individuals) or false negatives (under-flagging high-risk ones)? The answer differs by domain, and no algorithm can make that choice for you.

**Sciences**: Algorithmic feedback loops mirror **evolutionary selection pressure**. A model that predicts loan risk based on historical denials creates selection pressure: the group with high predicted risk has fewer resources, is denied more loans, and accumulates more markers of financial distress — the model's training data for the next cycle. This is **runaway selection** (like the peacock's tail) — a feedback loop where the selection criterion (loan denial) reinforces itself. Breaking the loop requires external intervention: counter-factual training data, outcome-based rather than prediction-based training, or actively corrective redistribution — analogous to conservation interventions that break negative evolutionary spirals.

---

# The Recalibration

The recommendation model was retaken back to square zero. The disparity analysis was made acceptance criteria. The model could not deploy unless the difference in advanced-lesson recommendation rates between income quintiles was below 3%. Three iterations of feature engineering later, postcode was removed and replaced with features derived from actual in-platform behaviour. The disparity fell to 1.8%. "It took six weeks longer," the data scientist said. "And the model is more accurate because we removed a noisy proxy." The Senior Engineer updated the ethics review template. Next time, the question would be asked before the model was built.
