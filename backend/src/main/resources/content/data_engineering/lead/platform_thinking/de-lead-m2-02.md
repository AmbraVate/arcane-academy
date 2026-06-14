---
id: de-lead-m2-02
school: DATA_ENGINEERING
domainId: data_engineering
tier: LEAD
moduleId: de-lead-m2
moduleTitle: "Module 2: Data Architecture Leadership"
moduleGlyph: "🏗️"
moduleSortOrder: 2
topicSlug: platform_thinking
topicTitle: "Platform Thinking"
topicSortOrder: 2
lesson: 2
title: "Platform Thinking: Building Infrastructure Others Build On"
sortOrder: 2
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
  - de-lead-m2-01
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines a data platform and distinguishes it from a collection of tools"
    - "Explains the internal platform as a product concept"
    - "Describes the self-serve capability and why it is the key measure of platform success"
    - "Identifies the data mesh platform's role as infrastructure provider vs data producer"
  keywords:
    - data platform
    - platform-as-product
    - self-serve
    - developer experience
    - data mesh
    - platform team
    - golden path
  modelAnswer: |
    A data platform is not just a collection of tools — it is a coherent set of capabilities that enable other teams to build data products without needing to solve infrastructure problems from scratch. A platform provides reusable primitives: data ingestion, storage, transformation, orchestration, quality monitoring, access control, and lineage tracking.
    Treating the platform as a product means the platform team has customers (data producers and consumers), understands their needs through research and usage metrics, maintains a product roadmap, and measures success by customer satisfaction and adoption. A platform that nobody uses is not a platform — it is expensive infrastructure.
    The key measure of platform success is self-serve capability: can a domain team build and publish a new data product without engaging the platform team for routine work? The platform team should be engaged for hard problems (new data sources, complex architectural changes), not routine ones (adding a new table, deploying a dbt model).
    In data mesh, the platform team's role is radically different: they don't own data products; they provide the infrastructure that enables domain teams to own their data products. The platform provides: compute, storage, observability, cataloguing, access control, and CI/CD for data products. Domain teams own the data within those capabilities.
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "A data platform team receives 40 tickets per week from domain teams for tasks like 'add a column to this table', 'grant read access to this dataset', 'deploy this dbt model'. What does this indicate about the platform's maturity?"
    options:
      - "The platform team is highly productive — 40 deliverables per week shows great output"
      - "The platform is too complex — domain teams need training to use it independently"
      - "The platform lacks self-serve capability — routine tasks require platform team involvement, creating a bottleneck"
      - "The domain teams lack capability — hire more data engineers in the domain teams"
    correctIndex: 2
    explanation: "40 tickets/week for routine operations means the platform team is the bottleneck for every data operation in the organisation. A mature platform enables domain teams to perform routine operations autonomously. 'Add a column' should require no platform team involvement — domain teams should have write access to their own schemas and a CI/CD pipeline that deploys their changes. The platform team should be engaged only for platform-level changes (new data source connectors, new storage tiers, platform upgrades). The ticket queue is a self-serve failure signal."
  - type: FILL_BLANK
    question: "The ___ is the pre-paved, opinionated path that the platform team provides for common data engineering tasks, reducing cognitive load and ensuring consistent standards."
    answer: "golden path"
    explanation: "The golden path (from Backstage/Spotify's platform model) is the recommended, pre-integrated way to accomplish common tasks on the platform: a dbt project template, a pre-configured Airflow DAG pattern, a standard Kafka consumer scaffold. Teams can deviate from the golden path for specialised needs, but the golden path should handle 80% of use cases with minimal friction. The platform's job is to make the golden path so smooth that deviation requires deliberate effort."
  - type: SHORT_TEXT
    question: "What does 'treating the data platform as a product' mean in practice, and how does it change how the platform team operates?"
    modelAnswer: "Treating the platform as a product means: the platform team has internal customers (domain teams, analysts, data scientists); they research customer needs (through interviews, usage analytics, feedback surveys); they maintain a product roadmap with releases; they measure success by adoption rate and customer satisfaction NPS, not by infrastructure uptime alone; they have a developer experience owner (like a product manager); they do user testing before shipping new capabilities; they deprecate unused features. It changes team operations: instead of 'we built this feature', they ask 'who asked for this?', 'how many teams will use it?', 'what's the adoption target?', and 'are our customers more productive this quarter than last?'"
microCheckpoint:
  question: "What is the primary measure of a data platform's success?"
  answer: "Self-serve capability — the degree to which domain teams can build, deploy, and operate data products without requiring the platform team for routine tasks. A platform succeeds when it removes the platform team from the critical path of day-to-day data work."
retrieval:
  recall: "What capabilities does a data platform provide as infrastructure primitives?"
  explain: "Explain how the data mesh model changes the relationship between the platform team and data producers."
  mistakeId: "platform-bottleneck-team"
---

# The Bottleneck Team

The data platform team had four engineers and 40 weekly tickets. The average ticket took two days. The backlog was six weeks deep. "We've built a data warehouse, an orchestration layer, and a catalogue," the Lead Data Engineer said. "But every team has to come to us for every routine operation. We're the bottleneck for the whole organisation's data work." The CDO listened. "We haven't built a platform. We've built a centralised service desk. Those are very different things."

# Platform vs Service Desk

```
Centralised service desk (what not to build):
  Domain team needs new table → ticket → platform team creates it → 2 days
  Domain team needs access → ticket → platform team grants it → 1 day
  Domain team deploys dbt model → ticket → platform team deploys → 3 days
  
  Domain teams: dependent, slow, frustrated
  Platform team: overwhelmed, doing low-value routine work
  Organisation: bottlenecked, slow to innovate

Data Platform (what to build):
  Domain team needs new table → self-service schema tool → 10 minutes
  Domain team needs access → access request portal → auto-approved with data owner approval
  Domain team deploys dbt model → CI/CD pipeline → automated deploy → 20 minutes
  
  Domain teams: autonomous, fast, empowered
  Platform team: focused on platform capabilities (hard problems)
  Organisation: distributed innovation at scale
```

## What a Data Platform Provides

A data platform is a coherent set of reusable capabilities that enable data work without per-team infrastructure setup.

```
Data Platform Capabilities:

  INGEST:       Connectors (Fivetran, Airbyte, CDC), streaming (Kafka)
  STORE:        Data lake (S3/GCS), warehouse (BigQuery/Snowflake/ClickHouse)
  TRANSFORM:    dbt project template, orchestration (Airflow), compute (Spark)
  OBSERVE:      Data quality monitoring, lineage (OpenLineage), alerting
  CATALOGUE:    Data discovery (DataHub/Alation), metadata management
  ACCESS:       Role-based access, column masking, audit logging
  PUBLISH:      Data product publishing, SLA tracking
  CI/CD:        dbt CI, schema migration pipelines, deployment automation
```

Each capability is a primitive that domain teams compose to build data products without re-solving infrastructure problems.

## The Platform as Product

```
Platform team as service desk:   Platform team as product team:
  Driven by tickets                 Driven by user research
  Measures output (tickets closed)  Measures outcomes (adoption, NPS)
  No roadmap                        Maintains product roadmap
  Reactive                          Proactive
  No customer understanding         Conducts user interviews
  Features requested ad hoc         Features prioritised by impact

Platform Product Manager responsibilities:
  - Define platform vision and roadmap
  - Conduct quarterly customer research (interview 5 domain teams)
  - Track adoption metrics per capability
  - Run platform NPS survey semi-annually
  - Prioritise platform investments by impact × adoption
```

## The Golden Path

The golden path is the pre-paved, opinionated way to accomplish common data engineering tasks.

```
Golden Path Example: "I need to ingest a new REST API source"

  Without golden path:
    Engineer figures out Airflow from scratch → 2 days
    Configures Kafka → 1 day
    Sets up dbt models → 1 day
    Configures quality checks → 0.5 day
    Total: 4.5 days, inconsistent quality

  With golden path:
    engineer runs: platform init-source --type rest-api --name payments_api
    → Generates: Airflow DAG template (authenticated, retrying, alerting)
    → Generates: dbt source definition
    → Generates: standard quality checks (freshness, completeness)
    → Generates: data catalogue entry stub
    Total: 2 hours, consistent quality, platform-compliant
    
  Platform team maintains the template; domain team customises for their source.
```

## Data Mesh Platform Model

In a data mesh, the platform team's mandate shifts from "build everything" to "enable everyone to build".

```
Traditional centralised:
  Platform team: owns all pipelines, all schemas, all data products
  Domain teams: consumers only

Data mesh:
  Platform team: owns platform infrastructure and capabilities
  Domain teams: own their data products within the platform
  
Platform team provides:
  ✓ Compute and storage infrastructure
  ✓ Deployment pipelines for data products
  ✓ Data catalogue and discovery tooling
  ✓ Access control infrastructure
  ✓ Quality monitoring primitives
  ✗ Does NOT own domain data, domain pipelines, domain schemas
```

## Measuring Platform Success

```
Platform adoption metrics:
  Active data products on platform: target 90% of known data products
  Self-serve operations per week: target >80% of routine ops without ticket
  Mean time from data product idea to deployed: target <2 days
  
Platform quality metrics:
  Platform uptime: >99.9%
  Mean time to provision new capability: target <30 minutes
  
Customer satisfaction:
  Domain team NPS: >50
  Quarterly user research findings: documenting unmet needs
  
Efficiency metrics:
  Platform team tickets for routine operations: target <5/week (vs 40 today)
```

## Common Mistakes

> **Platform Team Owns Too Much**
> A platform team that also owns domain data pipelines is a centralised data team, not a platform team. The cognitive overhead prevents them from building platform capabilities. Separate domain data ownership from platform infrastructure.

> **No Developer Experience Investment**
> A technically correct platform nobody uses is worthless. Invest explicitly in developer experience: documentation, onboarding, SDKs, templates. DX investment has higher ROI than feature investment when adoption is the limiting factor.

> **Deprecating Old Tools Without Migration Support**
> Forcing domain teams to migrate to new platform capabilities without tooling, documentation, and migration support causes resistance and fragmentation (teams keep using the old tool because migration is too painful).

## Mental Model

Think of a data platform as **cloud infrastructure for your own organisation**. AWS doesn't process your customer's orders — it provides the compute, storage, and networking so you can build the systems that do. Your data platform doesn't know domain-specific business logic — it provides the ingestion, storage, orchestration, and observability so domain teams can build data products that do. AWS's success metric is how much value customers create on top of it. Your platform's success metric is how much data product value domain teams create on top of it.

**Mini Summary**: A data platform is a coherent set of reusable capabilities that enable domain teams to build data products without re-solving infrastructure problems. Treat it as a product — with customers, roadmap, and adoption metrics. The golden path reduces cognitive load for common tasks. Self-serve capability (routine operations without platform team involvement) is the primary success metric. In data mesh, the platform team is infrastructure provider, not data product owner.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium currently has 4 platform engineers handling 40 weekly tickets. The team wants to transform into a true platform team in 12 months.

Design the transformation roadmap:
1. What self-serve capabilities would you build first (based on ticket analysis) to have the highest impact on reducing ticket volume?
2. How would you implement a golden path for the most common platform operation (deploying a new dbt model)?
3. What metrics would you use to track transformation progress at the 3, 6, and 12-month marks?
4. How do you manage the transition — existing domain teams still need support while you build the self-serve platform?

---

# Integration

**Mathematics**: Platform adoption follows a **two-sided market model** (Rochet and Tirole, 2003). A data platform has two types of users: producers (domain teams creating data products) and consumers (analysts, data scientists, ML engineers consuming them). Platform value for consumers increases with more producers (more data products available); platform value for producers increases with more consumers (more use of their data products). This creates a positive feedback loop — the **platform network effect**. The critical mass problem: at low adoption, neither side has enough value to justify switching to the platform. The golden path solves this by making producer onboarding cheap enough to bootstrap past critical mass.

**Sciences**: Platform thinking mirrors **ecosystem engineering** in ecology. Keystone species (platform team) modify the physical environment in ways that enable many other species (domain teams) to thrive that couldn't otherwise. Beavers build dams (platform infrastructure) that create wetlands (data capabilities) supporting dozens of species (data products) that couldn't exist on dry land. The keystone species doesn't produce all the biomass in the ecosystem — it creates the conditions for others to produce it. Removing the keystone species collapses the ecosystem; but the keystone species' direct biomass is small relative to what it enables.

---

# The Self-Serve Launch

Three months into the transformation, the weekly ticket count had dropped from 40 to 22. The golden path for dbt model deployment had been adopted by 8 of 12 domain teams. The platform team's developer experience survey showed NPS rising from 12 to 38. "We haven't added any new features," the Lead Data Engineer said. "We've removed the friction from the ones we have." The CDO reviewed the ticket trend. The platform team was finally doing platform work — not service desk work. "This is what leverage looks like," the CDO said.
