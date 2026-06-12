---
id: de-lead-m5-01
school: DATA_ENGINEERING
domainId: data_engineering
tier: LEAD
moduleId: de-lead-m5
moduleTitle: "Module 5: Emerging Data Technologies"
moduleGlyph: "🔬"
moduleSortOrder: 5
topicSlug: ai_data_infrastructure
topicTitle: "AI Data Infrastructure"
topicSortOrder: 1
lesson: 1
title: "AI Data Infrastructure: Engineering the Foundation for AI"
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
  - de-lead-m4-04
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Describes the components of a feature store and why it prevents training-serving skew"
    - "Explains the ML data pipeline and its differences from standard ETL"
    - "Identifies the data quality requirements specific to ML training data"
    - "Describes the role of the Lead Data Engineer in supporting ML teams"
  keywords:
    - feature store
    - training-serving skew
    - ML pipeline
    - data labelling
    - model registry
    - MLOps
    - lineage for ML
  modelAnswer: |
    AI/ML systems require data infrastructure that goes beyond standard analytics: feature stores, training data pipelines, labelling systems, model registries, and monitoring for data and concept drift.
    A feature store is a centralised repository of engineered features — computed once, stored, and reused for both training and serving. Without a feature store, teams compute the same features differently for training (batch SQL) and serving (online application code), producing training-serving skew: the model is evaluated on features computed differently from how they were computed during training, degrading performance. A feature store ensures identical feature computation at both stages.
    ML data pipelines differ from standard ETL: they must produce reproducible training datasets (the same dataset can be reconstructed for debugging), handle data versioning (which version of the dataset was used to train this model?), manage label quality (are the training labels correct and representative?), and balance class distributions (imbalanced datasets produce biased models).
    The Lead's role: ensure the data platform supports ML workflows — data versioning (DVC, Delta Lake time travel), feature computation infrastructure, labelling pipeline tooling, and lineage that connects training data to model versions to deployed predictions. Without these, ML teams operate on ad-hoc data pipelines that produce irreproducible results.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "The Consortium's ML team trains a churn prediction model. Training uses a SQL query that computes 'lessons completed in past 30 days'. The serving pipeline computes this feature using application code that counts since account creation, not past 30 days. The model performs worse in production than in evaluation. What is this?"
    options:
      - "Model overfitting — the model memorised training data instead of generalising"
      - "Training-serving skew — the feature computed differently at training vs serving time"
      - "Data drift — the distribution of completions changed since training"
      - "Label noise — the churn labels in the training data were incorrect"
    correctIndex: 1
    explanation: "Training-serving skew: the model was trained on 'lessons in past 30 days' but served predictions based on 'lessons since account creation.' New users have inflated lesson counts in the serving pipeline; the model's training distribution doesn't match the serving distribution. A feature store prevents this by computing features once using a single definition and serving the same computation to both the training pipeline and the online serving layer. The model evaluates against training-time features; production serves training-time-equivalent features."
  - type: FILL_BLANK
    question: "A ___ store provides pre-computed, versioned features that are identical whether consumed by a model training pipeline or a real-time serving endpoint — preventing training-serving skew."
    answer: "feature"
    explanation: "The feature store (Feast, Tecton, Hopsworks, or custom-built) stores materialised features keyed by entity (user_id, lesson_id) and timestamp. The training pipeline retrieves point-in-time correct features from the offline store (historical features at the training label timestamp). The serving pipeline retrieves the latest features from the online store (low-latency key-value access). Both use the same feature definitions — computed once, stored consistently."
  - type: SHORT_TEXT
    question: "What is dataset versioning for ML and why does it matter more for ML than for standard data warehousing?"
    modelAnswer: "Dataset versioning tracks which specific version of a dataset (which rows, which feature values, which labels) was used to train a specific model version. This matters for ML because: (1) reproducibility — if a model behaves unexpectedly, you need to reproduce the training conditions exactly to debug; (2) regulatory compliance — the EU AI Act requires documentation of training data for high-risk AI systems; (3) auditability — understanding model behaviour requires knowing what the model was trained on; (4) rollback — if a retrained model performs worse, rolling back to the previous model version requires the previous training dataset. Standard data warehousing doesn't typically need to know which version of a dataset produced a specific report — reports are stateless. ML models are stateful artifacts that encapsulate a specific dataset's information."
microCheckpoint:
  question: "What is training-serving skew and what infrastructure prevents it?"
  answer: "Training-serving skew is the discrepancy between how features are computed during model training vs during model serving. If training uses batch SQL and serving uses different application code, the feature values differ, degrading model performance in production. A feature store prevents this by providing a single feature computation definition used at both training time (offline store) and serving time (online store)."
retrieval:
  recall: "Name four components of ML data infrastructure beyond standard data warehousing."
  explain: "Explain why label quality matters as much as feature quality in ML training data."
  mistakeId: "ml-training-serving-skew"
---

# The Worse-Than-Expected Model

The churn prediction model had AUC 0.84 in evaluation. In production, it was performing at 0.71. The ML engineer had checked everything: no data drift, no concept drift, no code bugs. The Lead Data Engineer pulled up the feature definitions. Training: `COUNT(*) WHERE completed_at > NOW() - INTERVAL '30 days'`. Serving: `COUNT(*) WHERE completed_at > user.created_at`. "The feature is computing two completely different things in training vs production," the Lead said. "The model was never actually tested on the same feature it's served in production." This was a feature store problem. And it was a data infrastructure problem.

# What AI/ML Requires Beyond Standard Data Infrastructure

```
Standard analytics:
  ✓ Historical query answering
  ✓ Aggregation and reporting
  ✓ Schema-consistent data delivery

ML additionally requires:
  ✓ Feature stores (consistent features at training and serving time)
  ✓ Training data versioning (reproducibility and auditability)
  ✓ Labelling infrastructure (supervised learning)
  ✓ Model registry (version, lineage, performance tracking)
  ✓ Data drift monitoring (is training distribution still valid?)
  ✓ Online feature serving (low-latency feature retrieval at inference time)
  ✓ Point-in-time correct joins (features at the time of the label, not today)
```

## The Feature Store

```
Without feature store:
  Training pipeline:  SQL batch query → features computed
  Serving pipeline:   Application code → features computed differently
  → Training-serving skew → production performance degradation

With feature store:
  Feature definition:  "lessons_completed_30d" = COUNT(*) WHERE completed_at
                        > entity.created_at AND within_past_days(30)
  Offline store:  Historical materialised features for training
  Online store:   Latest features for real-time serving (Redis/DynamoDB)
  
  Training pipeline → reads from offline store
  Serving pipeline  → reads from online store
  Both use the same feature definition → no skew
```

```python
 # Feature definition (Feast example)
from feast import FeatureView, Entity, Field
from feast.types import Int64, Float32

learner = Entity(name="learner_id", join_keys=["user_id"])

learner_engagement = FeatureView(
    name="learner_engagement",
    entities=[learner],
    schema=[
        Field(name="lessons_completed_30d", dtype=Int64),
        Field(name="avg_session_duration_mins", dtype=Float32),
        Field(name="xp_earned_7d", dtype=Int64),
    ],
    source=BigQuerySource(table="feature_store.learner_features")
)
```

## Point-in-Time Correct Joins

ML training requires historical features at the time of the training label — not current values.

```
Training label: user_id='U-001' churned at 2024-03-15
Features needed: what was U-001's engagement BEFORE 2024-03-15?
                 NOT current 2024-06-06 engagement values

Wrong (leaks future information):
  SELECT u.id, COUNT(lc.id) as lessons_30d, u.churn_date
  FROM users u JOIN lesson_completions lc ON lc.user_id = u.id
  WHERE lc.completed_at > NOW() - INTERVAL '30 days'  -- uses TODAY, not label date

Correct (point-in-time join):
  SELECT u.id, COUNT(lc.id) as lessons_30d, u.churn_date
  FROM users u JOIN lesson_completions lc ON lc.user_id = u.id
  WHERE lc.completed_at BETWEEN u.churn_date - INTERVAL '30 days' AND u.churn_date
  -- uses features as they were at label time
```

Point-in-time joins are automatically handled by feature stores — a key reason to use one.

## Dataset Versioning

```
Tools:
  DVC (Data Version Control): Git-like versioning for datasets
  Delta Lake: time-travel queries to any past snapshot
  Iceberg: time-travel + schema evolution for training data
  ML platform metadata (MLflow, Weights & Biases): link dataset version to model run

What to track:
  {
    "model_version": "churn_v3",
    "training_dataset": {
      "source": "s3://ml-datasets/churn/v2024-03-01/",
      "row_count": 250000,
      "label_distribution": {"churn": 0.12, "retained": 0.88},
      "feature_schema_version": "v7",
      "created_at": "2024-03-01T02:00:00Z",
      "query_hash": "sha256:a1b2c3..."
    }
  }
```

## Label Quality

Garbage labels → garbage model. Common label quality problems:

```
Label noise:       True churn at day 14, labelled at day 7 (incomplete observation)
                   Fix: add observation window (label only users 30+ days old)

Label leakage:     Feature computed after the label date leaks future information
                   Fix: strict point-in-time feature joins

Class imbalance:   2% churn rate → model predicts "retained" for everyone = 98% accuracy
                   Fix: oversampling (SMOTE), undersampling, class weights, focal loss

Distribution shift: Training labels from 2022 may not reflect 2024 churn patterns
                   Fix: recent training window; drift monitoring; periodic relabelling
```

## The Lead's MLOps Infrastructure Role

```
Data engineering responsibilities in MLOps:

  ✓ Feature store infrastructure (compute, storage, serving layer)
  ✓ Training dataset pipeline (reproducible, versioned, point-in-time correct)
  ✓ Data lineage for ML (training data → model → predictions)
  ✓ Feature drift monitoring (are feature distributions changing?)
  ✓ Label pipeline (data labelling tooling and quality control)
  ✓ Data quality SLAs for ML datasets (completeness, label accuracy)
  
NOT data engineering (but requires coordination):
  ✗ Model architecture decisions (ML engineer)
  ✗ Hyperparameter tuning (ML engineer)
  ✗ Model serving infrastructure (MLOps/platform engineer)
  ✗ Model evaluation metrics (data scientist)
```

## Common Mistakes

> **Ad-Hoc Feature Computation**
> Features computed in notebooks for training are not reproducible, not versioned, and not consistent with serving. Any feature used in a production model must be in the feature store.

> **Ignoring Label Quality**
> ML teams focus on model architecture. Data teams focus on feature pipelines. Label quality falls in the gap — and incorrect labels produce models that learn the wrong thing. Invest in labelling infrastructure and quality checks.

> **No ML Lineage**
> Being unable to answer "what data produced this model's predictions?" is an auditability failure and a debugging failure. ML lineage connects: training data → features → model version → deployed predictions.

## Mental Model

Think of a feature store as **a standardised ingredient supply chain for a restaurant**. Multiple chefs (ML engineers) create dishes (models). Without a standardised supply chain, each chef sources ingredients differently — one uses fresh tomatoes, another uses canned, a third uses sun-dried. The dishes taste different (training-serving skew). With standardised ingredients delivered by the supply chain (feature store), every chef uses the same tomatoes, the dish is reproducible, and substituting one chef for another (serving at inference time) doesn't change the dish.

**Mini Summary**: AI/ML requires data infrastructure beyond analytics: feature stores (eliminate training-serving skew through consistent feature definitions), point-in-time correct joins (use features as of label date, not today), dataset versioning (reproducibility and auditability), and label quality management. The Lead Data Engineer owns: feature store infrastructure, training pipelines, ML data lineage, and feature drift monitoring. Model architecture and serving are ML engineer territory — but the data foundation is the Lead's responsibility.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium's ML team wants to build a lesson difficulty prediction model — predicting the difficulty a specific learner will experience on a specific lesson, to dynamically adjust the learning path.

Design the data infrastructure:
1. What features are needed? For each, describe how they would be computed and at what frequency.
2. How would you ensure point-in-time correctness in the training dataset?
3. What label would you use (how do you operationally define "difficulty experienced")?
4. How would you monitor for feature drift once the model is deployed?

---

# Integration

**Mathematics**: Feature stores implement **temporal joins** — a generalisation of SQL JOINs where the join condition includes a temporal predicate. A point-in-time join: for each (entity, label_time) pair, retrieve the feature value at the most recent timestamp ≤ label_time. This is equivalent to the **AS OF** query in temporal databases. Formally: feature_value(entity, label_time) = feature_value(entity, max{t | t ≤ label_time, feature_exists(entity, t)}). The correctness of this join determines whether a model will exhibit training-serving skew. Incorrect temporal joins introduce **target leakage** — the model sees information unavailable at prediction time — inflating training evaluation metrics while degrading production performance.

**Sciences**: AI data infrastructure mirrors **longitudinal cohort study design** in epidemiology. A cohort study follows participants over time, collecting exposures (features) before outcomes (labels). The critical design principle: exposures must be measured before outcomes occur — exactly the point-in-time correctness requirement. A study that measures "smoking status at time of diagnosis" instead of "smoking status 20 years before diagnosis" conflates cause and effect. Feature leakage in ML training is the same methodological error: using information from after the prediction date as if it were available before. Feature stores enforce the same temporal integrity that epidemiologists enforce through cohort study design protocols.

---

# The Feature Store Deployed

Three months after the diagnosis, the feature store was running. The churn model was retrained from scratch — same architecture, but with consistent feature definitions across training and serving. Production AUC: 0.83 — nearly matching the training evaluation. "The model didn't get better," the Lead Data Engineer said. "We stopped lying to it." The ML engineer filed the retrospective. Training-serving skew was added to the ML infrastructure checklist. Every new model required a feature store definition before training began. The foundation was finally trustworthy.
