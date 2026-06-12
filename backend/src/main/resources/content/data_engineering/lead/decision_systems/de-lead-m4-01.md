---
id: de-lead-m4-01
school: DATA_ENGINEERING
domainId: data_engineering
tier: LEAD
moduleId: de-lead-m4
moduleTitle: "Module 4: Data-Driven Organisations"
moduleGlyph: "🔭"
moduleSortOrder: 4
topicSlug: decision_systems
topicTitle: "Decision Systems"
topicSortOrder: 1
lesson: 1
title: "Decision Systems: How Organisations Turn Data into Action"
sortOrder: 1
difficulty: 5
estimatedMinutes: 40
xpReward: 100
practiceType: GUIDED_AND_SOLO
questType: RETRIEVAL_CHALLENGE
retrievalWeight: 0.6
questTypes:
  - guided
  - solo
  - retrieval
prerequisites:
  - de-lead-m3-04
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Distinguishes automated decisions from decision-support systems"
    - "Identifies the components of an effective decision system"
    - "Explains the role of data quality in decision quality"
    - "Describes how to design decision feedback loops for continuous improvement"
  keywords:
    - decision system
    - automated decision
    - decision support
    - decision quality
    - feedback loop
    - data freshness
    - decision latency
  modelAnswer: |
    A decision system is the complete pipeline from data collection through analysis to decision action. Automated decisions execute without human involvement (fraud scoring, content ranking, price setting). Decision-support systems provide analysis to inform human judgment (recommendation, risk assessment, dashboard). The boundary between them is not technical but governance: automated decisions require higher standards of fairness, explainability, and redress.
    Effective decision system components: data freshness (data must be current enough for the decision — real-time for fraud, daily for strategic); decision latency (how long from data to decision action); model accuracy and calibration; monitoring and drift detection; feedback loop (outcome data flows back to improve the decision model).
    Data quality directly determines decision quality: garbage in, garbage out is not a platitude — a fraud model trained on incorrectly labelled data will produce incorrect fraud scores. Decision systems require a data quality SLA that matches the decision's stakes. A €50k credit decision requires higher data quality than a personalised article recommendation.
    Decision feedback loops close the loop between decision and outcome. A content recommendation decision is followed by engagement data (was the recommendation good?), which flows back to retrain the model. Without feedback loops, models degrade as the world changes and the model's training distribution diverges from current reality (concept drift).
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "The Consortium's lesson recommendation engine makes 50,000 recommendations per day. An engineer proposes adding human review of all recommendations before they are shown to users. What is the trade-off analysis?"
    options:
      - "Human review is always better — AI recommendations are inherently less accurate"
      - "Human review at 50k/day is operationally impossible — the recommendation must be automated; invest in model quality and monitoring instead"
      - "Human review is required by GDPR Article 22 for automated decisions affecting learners"
      - "Human review adds latency but significantly improves quality — worth the investment"
    correctIndex: 1
    explanation: "50,000 reviews per day = ~35/minute = ~1 every 2 seconds, 24/7. This is operationally impossible at reasonable quality. For high-volume, lower-stakes decisions (content recommendations), automation is the only viable path. Investment should go into: model quality metrics, bias auditing, feedback loops, and an appeals mechanism for users who disagree with a recommendation. GDPR Article 22 applies to decisions that 'significantly affect' individuals — a content recommendation typically does not meet this threshold. Automated fraud decisions or credit decisions would."
  - type: FILL_BLANK
    question: "A decision model trained 6 months ago may perform poorly today because the relationship between features and outcomes has changed — this is called concept ___."
    answer: "drift"
    explanation: "Concept drift occurs when the statistical relationship between input features and the target variable changes over time. A fraud model trained in January may fail by July because fraudsters adapt their tactics. A content recommendation model trained before a curriculum change may recommend deprecated content. Decision systems require drift monitoring: track model performance metrics (precision, recall, F1) continuously; alert when they degrade below a threshold; retrain on recent data at a cadence matched to the domain's rate of change."
  - type: SHORT_TEXT
    question: "Design a feedback loop for the Consortium's lesson recommendation system."
    modelAnswer: "Feedback loop: 1. Recommendation made: 'Learner U-123 shown lesson L-456 as recommended next step' (logged with recommendation model version, features used, confidence score). 2. Outcome observed: learner clicks and completes lesson (positive signal), or ignores recommendation (negative signal), or clicks but abandons within 2 minutes (weak negative). 3. Outcome logged: {recommendation_id, outcome, delay, learner_tier, difficulty_level}. 4. Training dataset updated: recommendation-outcome pairs used to retrain the model weekly. 5. A/B testing: new model version shown to 10% of users; performance compared against baseline before full rollout. 6. Drift monitoring: recommendation click-through rate monitored daily; alert if drops >15% from 30-day average. This loop allows the model to continuously learn from actual learner behaviour."
microCheckpoint:
  question: "What is the difference between a decision-support system and an automated decision system?"
  answer: "A decision-support system provides analysis and recommendations to inform a human who makes the final decision. An automated decision system executes a decision without human involvement. The distinction is not purely technical — it is a governance choice. Higher-stakes decisions affecting individuals' significant interests require human oversight (decision-support) rather than full automation."
retrieval:
  recall: "What is concept drift and why does it require active monitoring rather than one-time model training?"
  explain: "Explain why decision quality depends on data quality and give a concrete example of how data quality failure degrades a decision system."
  mistakeId: "decision-system-no-feedback-loop"
---

# The Stale Recommender

The recommendation model had been deployed for nine months without retraining. The Consortium had launched 200 new lessons in that period. The model had never seen them. New learners were being recommended lessons from the original curriculum exclusively, because the model's training data predated the new content. Completion rates from recommendations had fallen from 68% to 41%. Nobody had noticed until a learner complained. "The decision system made decisions. It just stopped making good ones," the Lead Data Engineer said. "Because we broke the feedback loop."

# What a Decision System Is

A decision system converts data into actions at scale.

```
Components of a decision system:

  DATA PIPELINE: collects and prepares the inputs
    ↓ (freshness: is it current enough for this decision?)
    
  DECISION MODEL: computes a decision (rule, ML model, or both)
    ↓ (latency: how fast must the decision be made?)
    
  DECISION ACTION: executes the recommendation or automated action
    ↓ (stakes: how much does a wrong decision cost?)
    
  OUTCOME OBSERVATION: captures whether the decision was good
    ↓ (completeness: do we see all outcomes, or just some?)
    
  FEEDBACK LOOP: routes outcome data back to improve the model
    ↑ (frequency: how often is the model updated?)
```

## Automated vs Decision-Support

```
AUTOMATED DECISIONS:
  No human in the loop between data and action
  Examples: content ranking, fraud scoring trigger, price adjustment
  Requirements: high confidence model, drift monitoring, appeals mechanism
  Volume: typically high (thousands to millions per day)
  Latency: often real-time or near-real-time
  Stakes per decision: typically moderate to low

DECISION-SUPPORT:
  Data informs a human who makes the final call
  Examples: risk assessment for credit officer, clinical decision support
  Requirements: interpretability, calibration, uncertainty quantification
  Volume: typically low (human is the bottleneck)
  Latency: minutes to hours
  Stakes per decision: high to very high
  
GDPR Article 22 applies when automated decisions:
  ● Produce legal effects on individuals, OR
  ● Similarly significantly affect them
  Examples: automated loan rejection, automated insurance pricing,
            automated flagging for dismissal → require human review option
```

## Data Freshness and Decision Quality

```
Decision Type → Required Freshness → Architecture
─────────────────────────────────────────────────
Fraud detection     Real-time (<1s)   Streaming feature store
Price optimisation  Near-real-time    Micro-batch (5 min)
Churn prevention    Same day          Daily ETL
Strategic planning  Weekly/Monthly    Batch warehouse
```

Data that is too stale for the decision produces incorrect actions:
- Fraud model using yesterday's transaction data misses today's fraud patterns
- Recommendation model with 9-month-old training data misses 200 new lessons
- Inventory reorder model using last week's sales misses a sudden spike

## Decision Feedback Loops

```
Feedback loop design:

  1. LOG THE DECISION
     {decision_id, model_version, features_used, recommendation,
      confidence_score, timestamp, user_id}
     
  2. CAPTURE THE OUTCOME
     {decision_id, outcome_type, observed_at, delay_seconds}
     Positive: lesson completed within 24h of recommendation
     Negative: recommendation ignored
     Weak: lesson started but abandoned within 2 minutes
     
  3. LABEL AND AGGREGATE
     Join decisions with outcomes
     Compute precision, recall, F1 per model version per cohort
     
  4. RETRAIN
     New training data = recent (decision, outcome) pairs
     Frequency: daily for high-drift domains, weekly for stable
     
  5. VALIDATE AND DEPLOY
     A/B test new model vs current; deploy when significantly better
     
  6. MONITOR CONTINUOUSLY
     Alert on: metric decline >10%, concept drift, data freshness lag
```

## Building the Decision Monitoring Stack

```python
 # Decision quality monitoring
class RecommendationMonitor:
    def daily_health_check(self):
        metrics = {
            # Is the model making recommendations?
            "recommendation_volume": self.count_recommendations_today(),
            
            # Are recommendations being acted on?
            "click_through_rate": self.compute_ctr(window_days=7),
            
            # Are recommendations leading to completions?
            "completion_rate": self.compute_completion_rate(window_days=7),
            
            # Is the model seeing fresh data?
            "feature_freshness_lag_hours": self.compute_feature_lag(),
            
            # Is model performance degrading?
            "model_performance_trend": self.compute_rolling_performance(),
        }
        
        # Alert on degradation
        if metrics["completion_rate"] < self.COMPLETION_RATE_THRESHOLD:
            alert(severity="HIGH", 
                  message=f"Recommendation completion rate {metrics['completion_rate']:.1%} "
                          f"below threshold {self.COMPLETION_RATE_THRESHOLD:.1%}")
```

## Common Mistakes

> **Training Once, Deploying Forever**
> A model trained at deployment date reflects the world at that date. As the world changes (new content, new learner cohorts, changing user behaviour), the model degrades. Plan for continuous retraining from the start; schedule it as a regular operational process.

> **No Outcome Measurement**
> Building a recommendation engine without measuring whether recommendations are good is like driving without knowing where the car goes. Define the outcome metric before building the model; instrument the system to capture it.

> **Automating Without Appeals**
> Any automated decision that affects a user's experience should have a mechanism for the user to indicate disagreement ("not interested", "this doesn't apply to me"). Appeals provide data quality signals and respect user autonomy.

## Mental Model

Think of a decision system as **a thermostat with memory**. The thermostat measures temperature (data), compares to a target (model), and adjusts heating (decision action). A thermostat without a feedback loop (no temperature measurement after adjustment) will overheat or underheat indefinitely. A thermostat that was calibrated for last winter's house layout won't work after a renovation (concept drift). Good thermostats continuously measure outcomes and update their calibration. Good decision systems do the same.

**Mini Summary**: A decision system converts data into actions — automated or decision-support. Automated decisions require drift monitoring, feedback loops, and appeals; decision-support requires interpretability and calibration. Data freshness must match the decision timescale. Feedback loops close the cycle: log decisions, capture outcomes, retrain, validate, deploy, monitor. Model without feedback loop degrades as the world changes. Monitor decision quality metrics continuously; alert on degradation.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

Design the complete decision system for the Consortium's lesson recommendation engine (addressing the stale recommender problem):

1. Define the decision: what input features are needed and at what freshness?
2. Define the outcome metric: how will you measure whether a recommendation was good?
3. Design the feedback loop: how often do you retrain, on what data, with what validation?
4. Design the drift monitoring: what metrics do you track and what triggers an alert?
5. What is the appeals mechanism for learners who disagree with their recommendation?

---

# Integration

**Mathematics**: Decision system quality can be measured using **Brier score** — a proper scoring rule for probabilistic predictions. For a recommendation system producing probabilities P_i of completion: Brier score = (1/N) × Σ (P_i - O_i)² where O_i ∈ {0,1} is the observed outcome. Lower is better (0 = perfect, 1 = worst). Calibration (are predicted probabilities accurate?) is measured via reliability diagrams. Concept drift is detectable as a statistically significant increase in Brier score over rolling windows: if Brier(last_30_days) > Brier(baseline_30_days) + 2σ, trigger retraining. These mathematical tools operationalise what "model quality" means in a way that drives concrete engineering actions.

**Sciences**: Decision feedback loops mirror **adaptive immune memory** in biology. The adaptive immune system doesn't just respond to pathogens — it remembers encounters (B-cell memory) and responds faster and more accurately to future exposure. Each encounter (decision + outcome) updates the immune memory (training data). When a novel pathogen (concept drift — new fraud pattern, new content category) appears, the initial response is slow; after encountering it, future responses are rapid and targeted. Vaccination (deliberate training data injection) pre-trains the response for anticipated future encounters. The biological parallel maps directly: feedback loops are immune memory; retraining is learning from encounters; proactive data collection for anticipated drift is vaccination.

---

# The Retrained Model

The recommendation model was retrained on nine months of outcome data. The feedback loop was instrumented — every recommendation logged, every completion tracked, the training dataset updated weekly. Click-through rate climbed from 41% back to 65%. The Lead Data Engineer added the retraining job to the SLA monitoring dashboard. "A decision system isn't built at deployment," they said. "It's maintained continuously. Like every other system we care about."
