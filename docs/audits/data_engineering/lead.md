# Audit — Data Engineering · Lead

**Auditor lens:** Chief Data Officer / VP Engineering evaluating whether this tier produces someone I would hire as a Lead or Principal Data Engineer — a person who thinks strategically about data as an organisational asset, makes technology decisions with full TCO reasoning, leads data governance programmes, architects for AI/ML workloads, and communicates authoritatively to non-technical executives and boards.

**Tier mandate:** Mastery and leadership — data strategy, data mesh, modern data stack decisions, ML/AI data infrastructure, vector databases, knowledge graphs, self-service analytics, governance, regulatory compliance, responsible data use, strategic KPIs.

**Scope:** 21 lessons across 31 topics.

---

## 1. Verdict at a glance

The Lead tier reaches the level of intellectual seriousness required for leadership-level curriculum. The lessons examined (data_as_an_asset, platform_thinking, technology_selection, vector_databases, ai_data_infrastructure, capstone) are all strong — they treat the learner as a professional who needs conceptual frameworks, not just procedures. The income approach data valuation exercise, the self-serve platform transformation roadmap, the TCO-with-ADR technology selection framework, the RAG architecture design, and the feature store / training-serving skew analysis are all content I would expect in a senior consultant's toolkit. The capstone (de-lead-m6-01, The Grand Data Architect) is genuinely exceptional — an 8-part problem covering conceptual modelling, relational design, advanced SQL, data quality programme design, reporting, security/RBAC, governance strategy, and a scalability roadmap with a non-technical executive brief. This is university-equivalent synthesis work. The key weaknesses are: the data mesh topic (no lesson file found despite being listed), an overemphasis on privacy/ethics topics relative to architecture topics, and several gaps in the modern data stack (dbt at scale, real-time analytics, data product thinking) that are conspicuous absences for a Lead curriculum. The topic list (31) again exceeds the lesson count (21), indicating further phantom topics. Scores: **Coverage 3.5/5, Rigor/Depth 5/5, Sequencing 4/5, Practice quality 5/5.**

---

## 2. KEEP — strengths to preserve

- **de-lead-m1-01 (data_as_an_asset):** The three-approach valuation framework (cost, market, income), the non-rivalry and network effects economic properties, carrying costs and liability framing, and the data asset inventory YAML template are all genuinely novel at the curriculum level. Most Lead programmes teach strategy abstractly; this lesson forces quantitative reasoning. The Metcalfe's Law integration is correct and practically relevant.
- **de-lead-m2-02 (platform_thinking):** The "service desk vs platform" dichotomy, the golden path concept (with concrete command-line scaffold example), the platform-as-product framework with NPS/adoption metrics, and the data mesh platform team vs data producer distinction are all correct and production-relevant. The 40-ticket per week scenario is an exact replica of real Lead-level problems.
- **de-lead-m2-03 (technology_selection):** TCO breakdown (licensing is the smallest component), build vs buy decision matrix with defaults (buy infrastructure, build differentiation), technology proliferation quadratic cost growth proof, ADR template (Status/Context/Decision/Alternatives/Consequences), and PoC standards checklist are all Lead-level thinking tools that most practitioners learn only through experience.
- **de-lead-m5-03 (vector_databases):** The embedding space as semantic GPS, HNSW vs IVF comparison, RAG pattern with step-by-step code, hybrid search (BM25 + vector) recommendation, and the "embedding model mismatch" common mistake are all correct and current. The cosine similarity mathematics (Watts-Strogatz small world theorem for HNSW) is unusually rigorous for curriculum content.
- **de-lead-m5-01 (ai_data_infrastructure):** Training-serving skew with code demonstrating the exact bug (30-day window vs account-creation window), feature store architecture (offline store for training, online store for serving), point-in-time correct join explanation with SQL examples, dataset versioning tools (DVC, Delta Lake, MLflow), and label quality failure modes are all present and accurate. This is the best AI infrastructure curriculum content I have reviewed in any programme.
- **de-lead-m6-01 (capstone / The Grand Data Architect):** An 8-part synthesis problem that requires: conceptual data model with design decision justification, relational schema with normalisation reasoning, SQL (CTEs + window functions + quality check + access control view), data quality programme (dimensions + validation rules + monitoring), strategic reporting, RBAC + audit logging + data protection compliance, governance strategy (ownership + lineage + cataloguing + succession), and a non-technical scalability roadmap for the executive council. This is genuinely university-equivalent synthesis work and should be treated as the platform's flagship assessment.
- **de-lead-m3-01 through de-lead-m3-04 (privacy / ethics / regulatory_compliance / responsible_data_use):** The four-lesson governance ethics block is thorough. The regulatory compliance lesson addressing GDPR Article 17/22 with technical implementation detail is above what most programmes provide at any level.

---

## 3. CHANGE — restructure / resequence

- **Ethics over-representation:** Module 3 contains four separate lessons on privacy, ethics, regulatory compliance, and responsible data use. While each has distinct content, the ratio of ethics to architecture in this tier is skewed — 4/21 lessons on ethics vs only 3 on technical architecture (enterprise_modelling, platform_thinking, technology_selection). For a Lead programme claiming to produce data architects, the architecture-to-ethics ratio should be closer to 3:1. **Resequence: consolidate privacy (de-lead-m3-01) and responsible_data_use (de-lead-m3-04) into a single lesson; use the freed lesson slot for a missing technical topic.**
- **Module 5 (Emerging Technologies):** This module positions vector databases and knowledge graphs as "emerging." While appropriate for a 2023 curriculum, vector databases are now production-standard at many organisations. The lesson content is correctly written but the module framing should evolve. **Change module framing:** "Modern Data Technologies" rather than "Emerging."
- **de-lead-m4 sequencing:** The organisational behaviour module (de-lead-m4-03) and change management (de-lead-m4-04) are important but appear mid-tier after strategy. These leadership/influence topics benefit from being near the end of the curriculum, after technical depth is established. **Resequence: move module 4 leadership topics to after module 5 technical content**, so learners apply leadership frameworks to a technically grounded context.
- **Topic/lesson file mismatch (31 topics vs 21 lessons):** Topics data_mesh, data_literacy, governance_frameworks, self_service_analytics, and streaming_and_realtime appear in the topic list but have no corresponding lesson files. These are critical Lead-level topics. **Change: either create these lesson files or explicitly map them to existing content.**

---

## 4. UPDATE — depth / rigor / currency

- **de-lead-m2-01 (enterprise_modelling):** Enterprise data modelling at the Lead level should cover conceptual vs logical vs physical model distinctions, canonical data models, master data management (MDM), and the organisational challenge of establishing a shared enterprise ontology. The lesson content was not sampled but MDM is a canonical Lead-level topic that must be present. **Update: verify MDM and canonical data model coverage; add if absent.**
- **de-lead-m5-01 (ai_data_infrastructure):** The EU AI Act (in force from August 2026) imposes specific data governance requirements on high-risk AI systems, including training data documentation, bias testing, and human oversight. This should be integrated into the AI infrastructure lesson. **Update: add EU AI Act compliance requirements as a governance section.**
- **de-lead-m1-04 (strategic_metrics):** Strategic metrics at the Lead level should cover the distinction between leading and lagging indicators, OKR cascading methodology, and the data engineering-specific DORA metrics (change failure rate, deployment frequency, lead time, MTTR). **Update: add OKR cascading and DE-specific DORA metrics if not present.**
- **de-lead-m4-01 (decision_systems):** Lead data engineers make architecture decisions under uncertainty. Decision frameworks (DECIDE model, weighted scoring matrices, reversible vs irreversible decisions) are relevant here. **Update: add reversible vs irreversible decision framework and weighted scoring matrix for technology evaluation.**
- **de-lead-m5-02 (knowledge_graphs):** The lesson should cover not just knowledge graph theory but practical implementation: RDF vs property graphs, SPARQL vs Cypher, and the specific use cases where knowledge graphs outperform relational (entity resolution, semantic search enrichment, regulatory compliance graphs). **Update: verify practical implementation coverage and add SPARQL vs Cypher comparison.**
- **de-lead-m1-02 (organisational_data_strategy):** The modern data strategy at the Lead level must include the lakehouse architecture decision (data warehouse vs data lake vs lakehouse), the buy vs build vs partner decision for the modern data stack, and the "data product" thinking framework. **Update: add lakehouse architecture positioning decision and data product definition.**

---

## 5. REMOVE — cut or merge

- **de-lead-m3-01 (privacy) and de-lead-m3-04 (responsible_data_use):** These two lessons likely have significant overlap — privacy principles (data minimisation, purpose limitation, storage limitation) and responsible data use principles are closely related. **Merge into one lesson:** "Privacy by Design and Responsible Data Use," freeing a slot for a missing technical topic.
- **de-lead-m4-03 (organisational_behaviour) as a standalone lesson:** Organisational behaviour at the depth required for a Lead data engineer is better addressed through case studies within other lessons (change management, platform thinking, data culture) rather than as a separate lesson. Unless this lesson covers something specific (e.g., Conway's Law applied to data architecture, team topologies), it risks being too abstract. **Merge into change_management (de-lead-m4-04) or data_culture (de-lead-m1-03).**

---

## 6. GAPS — missing canonical topics

| Topic | Why essential at this tier | Suggested placement |
|---|---|---|
| **Data mesh (domain ownership, data products, federated governance, self-serve infrastructure)** | Listed as a topic but no lesson file found. Data mesh is the defining architectural paradigm for Lead-level data architects in 2024-2026. Zhamak Dehghani's four principles (domain ownership, data as a product, self-serve infrastructure, federated governance) must be explicitly taught. | New dedicated lesson, de-lead-m2 or standalone module |
| **Modern data stack (dbt, Airflow, Fivetran, Snowflake/BigQuery/ClickHouse, Metabase/Looker)** | The modern data stack as an integrated decision framework — how these tools compose, when to choose managed vs self-hosted, how to evaluate total cost — is a Lead-level curriculum gap. Listed as a topic but no lesson file visible. | New lesson in de-lead-m2 (Architecture Leadership) |
| **Data product thinking** | The "data as a product" principle requires: defining data product interfaces (schema contracts, SLAs), measuring data product health (freshness, completeness, consumption metrics), and organisational accountability. Distinct from data_as_an_asset and data_mesh, this is the operational implementation of both. | New lesson, complement to platform_thinking |
| **Self-service analytics programme design** | Listed as a topic but no lesson file found. How to enable non-technical users to query and analyse data without engineering support (semantic layer, BI tool selection, data literacy programmes, governed self-service) is a standard CDO-level deliverable. | New lesson in Module 1 (Enterprise Data Strategy) |
| **Streaming and real-time architecture at the Lead level** | Listed as a topic but no lesson file found. Real-time architecture decisions (Lambda vs Kappa architecture, streaming-first design, exactly-once semantics at the platform level) belong at Lead as architectural decisions, not just as Senior-level implementation. | New lesson in de-lead-m2 or new module |
| **Data quality programme management** | While data ethics is covered thoroughly, the operational data quality programme — quality SLA definition, quality scorecard design, DQ tool selection (Great Expectations, Monte Carlo, Soda), escalation procedures — is missing at the Lead level. This is a CDO-level responsibility. | New lesson in de-lead-m1 (Enterprise Data Strategy) |
| **Organisational design for data engineering** | Team topologies (platform team, domain teams, enabling teams), embedding vs centralisation trade-offs, and hiring strategy for data organisations are all Lead-level topics absent from the curriculum. | New lesson in Module 4 (Leadership) |
| **Vendor and cloud platform evaluation** | Technology_selection (de-lead-m2-03) covers the framework but a dedicated lesson on evaluating specific cloud data platforms (Snowflake vs BigQuery vs Databricks vs ClickHouse) with TCO analysis for realistic workloads is a standard Lead deliverable. | Extend or supplement de-lead-m2-03 |

---

## 7. PRACTICE & ASSESSMENT

Practice quality at the Lead tier is the best on the platform. The shift from technical exercises to strategic reasoning tasks is appropriate. The data asset valuation solo (income approach, three-approach bid range), the platform transformation roadmap (12-month self-serve target with metrics at 3/6/12 months), and the TCO analysis with ADR drafting are all formats that require genuine synthesis.

The capstone (de-lead-m6-01) is the platform's best assessment. It is the only assessment that truly tests mastery as the tier mandate defines it: strategic + technical + communication simultaneously. It deserves to be treated as a showcase deliverable — the learner's "portfolio piece."

Two gaps:
1. **Stakeholder communication practice is thin outside the capstone.** A Lead must present data strategies to boards, negotiate with business units, and write executive briefings. The capstone includes this (Part 6: Design Document for the Council) but no intermediate exercise practices it. Add a mid-tier practice task: "Write a one-page executive brief to the board justifying a €1M data platform investment."
2. **No peer review or critique practice.** Lead DEs review junior architects' designs and provide structured feedback. A solo assessment format does not develop this skill. Consider adding a rubric-guided "critique this design" exercise where the learner evaluates a deliberately flawed architecture proposal.

---

## 8. Prioritized action list

1. **Add:** Data mesh lesson (domain ownership, data as a product, self-serve infrastructure platform, federated governance) — the single most important missing topic for a Lead curriculum in 2025-2026.
2. **Add:** Modern data stack lesson (dbt + Airflow + Fivetran + warehouse selection + BI layer as integrated architecture decision) — covers the tooling context within which all other Lead decisions are made.
3. **Add:** Self-service analytics programme design lesson (semantic layer, BI tool governance, data literacy programme, governed self-serve) — listed as a topic with no lesson file.
4. **Add:** Streaming and real-time architecture at the Lead level (Lambda vs Kappa, streaming-first design, platform-level exactly-once guarantees) — listed as a topic with no lesson file.
5. **Merge:** Privacy (de-lead-m3-01) and responsible_data_use (de-lead-m3-04) into a single lesson; use the freed slot for data product thinking.
6. **Add:** Data product thinking lesson (defining data product interfaces, SLAs, health metrics, organisational accountability).
7. **Update:** Add EU AI Act compliance requirements to ai_data_infrastructure (de-lead-m5-01).
8. **Update:** Add data mesh to organisational_data_strategy (de-lead-m1-02) as the dominant current architecture paradigm, with a forward reference to the dedicated data mesh lesson.
9. **Add:** Organisational design for data engineering (team topologies, embedding vs centralisation, hiring strategy) as a new leadership module lesson.
10. **Change:** Resolve topic/lesson file mismatch across all 31 topics — create explicit lesson files or mapping for data_mesh, self_service_analytics, streaming_and_realtime, governance_frameworks, and data_literacy.
