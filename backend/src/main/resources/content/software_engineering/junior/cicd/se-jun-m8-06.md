---
id: se-jun-m8-06
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m8
moduleTitle: "Module 8: Professional Practices"
moduleGlyph: "⚙️"
moduleSortOrder: 8
topicSlug: cicd
topicTitle: "CI/CD"
topicSortOrder: 6
lesson: cicd_fundamentals
title: "CI/CD Fundamentals"
sortOrder: 6
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [documentation]
integrationDomains: [design, economics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Distinguishes Continuous Integration from Continuous Delivery from Continuous Deployment"
    - "Names the stages of a typical CI/CD pipeline in order"
    - "Explains why fast feedback is the core value of CI"
    - "Describes what a 'broken build' means and why fixing it immediately matters"
    - "Connects CI/CD to deployment frequency as a quality metric"
  keywords: [integration, delivery, deployment, pipeline, build, test, deploy, feedback, merge, automate]
  modelAnswer: |
    # CI/CD Pipeline for a Java Spring Boot app

    Continuous Integration (CI): every push triggers:
      1. Compile (fail fast on syntax errors)
      2. Unit tests (fast feedback on logic)
      3. Integration tests (verify DB/API interactions)
      4. Static analysis + linting

    Continuous Delivery (CD): if CI passes:
      5. Build Docker image
      6. Deploy to staging environment
      7. Smoke tests on staging

    Continuous Deployment: (optionally)
      8. Auto-deploy to production on staging success

    Key principle: the pipeline is the gatekeeper.
    A broken build is top priority — it blocks everyone.
guidedSteps:
  - id: cicd-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the key principle that distinguishes Continuous Integration from
      developers simply committing to branches occasionally?
    inputConfig:
      options:
        - "CI uses Docker containers"
        - "CI automatically builds and tests every change immediately after it is pushed"
        - "CI deploys to production automatically"
        - "CI requires pull requests before merging"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["CI automatically builds and tests every change immediately after it is pushed"]
      rejectedFeedback: "CI's defining principle is **automation + immediacy**: every push triggers an automated build and test run. The feedback reaches the developer within minutes, not hours or days."
    hint: "The word 'Continuous' is the clue — what happens continuously?"
    reflectionPrompt: "CI's value is the feedback loop. Small, frequent changes + immediate automated testing = bugs caught at the moment of introduction, when context is still fresh and the scope is small."
  - id: cicd-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In a GitHub Actions workflow, the `on: push` trigger means the pipeline runs
      whenever code is ___ to the repository.
    inputConfig:
      placeholder: "one word"
    markingRule:
      matchMode: NORMALIZED
      accepted: [pushed, committed, merged, uploaded]
      rejectedFeedback: "`on: push` triggers the workflow whenever code is pushed to the repository (any branch, unless filtered with `branches:`). Common triggers also include `on: pull_request` and `on: schedule`."
    hint: "Think about what 'push' means in git."
    reflectionPrompt: "Triggering on every push means every change is tested. You can also trigger on pull requests, which tests the proposed change before it merges — an excellent default for team projects."
  - id: cicd-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A team's CI pipeline takes 45 minutes to run. Explain why this is a problem and what the consequences are likely to be.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [slow, feedback, delay, skip, bypass, context, push, ignore, deferred, long, wait]
      rejectedFeedback: "A 45-minute pipeline means a developer waits 45 minutes to know if their change broke anything. In practice, they'll move on to other work. By the time the failure notification arrives, they've lost context. Worse, teams often start bypassing slow pipelines — committing less frequently, skipping checks, losing the benefit of CI entirely."
    hint: "What does a developer do while the 45-minute pipeline runs? What happens to context?"
    reflectionPrompt: "CI pipelines must be fast — under 10 minutes is a common target. Slow pipelines get bypassed. Techniques to keep them fast: parallelise test suites, run unit tests first (they're fastest), cache dependencies, use incremental builds."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the difference between Continuous Delivery and Continuous Deployment?"
    options:
      - "Continuous Delivery deploys automatically; Continuous Deployment requires manual approval"
      - "Continuous Delivery requires a human to trigger the production deployment; Continuous Deployment deploys automatically on pipeline success"
      - "They are the same thing with different names"
      - "Continuous Deployment only works with Kubernetes"
    correctIndex: 1
    feedback: "Continuous Delivery = always ready to deploy, but a human clicks the button. Continuous Deployment = automated all the way to production with no human step. Most teams aim for CD (delivery) with selective deployment."
  - type: MULTIPLE_CHOICE
    question: "Why is a 'broken build' considered a top-priority emergency on a CI-driven team?"
    options:
      - "The build server costs money while it's broken"
      - "A broken build blocks everyone — no one can get reliable feedback from the pipeline until it is fixed"
      - "The pipeline runs tests more slowly when broken"
      - "Broken builds are counted in performance reviews"
    correctIndex: 1
    feedback: "A broken build is a shared blockage. Every team member who pushes after a broken commit gets misleading pipeline results. The rule: fix or revert a broken build immediately — it takes priority over new work."

retrieval:
  recall: "What are the three terms CI, CD (Delivery), and CD (Deployment)? How do they differ?"
  explain: "Explain to a developer who has never used CI why the automated pipeline is worth the setup effort."
  mistakeId:
    code: |
      # GitHub Actions workflow
      name: Build
      on: push
      jobs:
        build:
          runs-on: ubuntu-latest
          steps:
            - uses: actions/checkout@v3
            - name: Run tests
              run: mvn test
            - name: Deploy to production
              run: ./deploy.sh production
              # runs even if tests failed
    answer: "There's no failure gate between tests and deployment. If `mvn test` fails, the workflow continues and `deploy.sh production` still runs. Add `if: success()` on the deploy step, or rely on GitHub Actions' default failure propagation (add `continue-on-error: false` explicitly, or restructure as separate jobs with `needs:` dependency)."
---

# Hook

Two developers are working on the same codebase. Developer A commits a change that breaks the login service. Developer B merges that change into their branch without knowing.

In an hour, they're both stuck and neither knows why the other's tests are failing on their machine but not theirs.

This is called an **integration hell**. It happens when changes are merged infrequently and tested manually. CI/CD exists specifically to prevent it.

> How long after pushing code do you typically find out if it broke something? How many changes have accumulated by then?

# Lore Introduction

Before the Academy installed its Verification Engine, artifacts were tested by their creators alone. Naturally, each artificer believed their work was perfect. When combined, the failures emerged — but by then, the source was a mystery.

*"The Verification Engine tests every contribution the moment it arrives,"* Archmage Veylan explains. *"If a rune conflicts with existing enchantments, we know within minutes, while the artificer still remembers exactly what they changed."*

# Core Learning

## Concept Introduction

**CI/CD** is a set of practices and tools that automate the building, testing, and deployment of software.

- **Continuous Integration (CI)** — every code push triggers an automated build and test run. Developers receive feedback within minutes.
- **Continuous Delivery (CD)** — the pipeline extends through to a deployable artifact; a human triggers the production deployment.
- **Continuous Deployment** — deployment to production is also automated; every passing pipeline ships to users.

**A typical CI pipeline (GitHub Actions):**
```yaml
name: CI
on: push

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up Java 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
      - name: Run tests
        run: ./gradlew test
      - name: Build artifact
        run: ./gradlew build
```

## Why It Matters

CI/CD provides:
- **Fast feedback** — failures found at introduction, not integration day
- **Reduced integration risk** — small, frequent merges; no "big bang" integration
- **Deployment confidence** — every deploy has been verified by the same pipeline
- **Lower cost of change** — small, safe deployments mean you can ship more often
- **Audit trail** — every change, test result, and deployment is logged

DORA research (State of DevOps Report) consistently shows elite teams deploy multiple times per day with low failure rates — enabled by CI/CD.

## Worked Examples

**Minimal GitHub Actions CI for Spring Boot:**
```yaml
name: CI
on: [push, pull_request]

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:15
        env:
          POSTGRES_DB: testdb
          POSTGRES_PASSWORD: test
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with: { java-version: '17', distribution: 'temurin' }
      - name: Cache Gradle
        uses: actions/cache@v3
        with:
          path: ~/.gradle/caches
          key: gradle-${{ hashFiles('**/*.gradle') }}
      - run: ./gradlew test
      - run: ./gradlew build -x test
```

**Pipeline stages in order (fastest first):**
1. Compile (seconds — fail fast)
2. Unit tests (< 1 min)
3. Integration tests (1-5 min)
4. Build artifact (< 1 min)
5. Deploy to staging (2-5 min)
6. Smoke tests on staging

## Common Mistakes

- **Slow pipelines** — over 10 minutes and developers start bypassing the pipeline.
- **No failure gates** — deploying despite test failures because steps aren't properly blocked on success.
- **Testing only in CI** — developers should run tests locally too; CI is the safety net, not the primary test environment.
- **Treating CI failures as normal** — every failure should be investigated and fixed immediately.
- **Not caching dependencies** — downloading Maven/Gradle/npm on every run wastes minutes.

## Mental Model

CI/CD is an **assembly line quality gate**. A car factory doesn't build the whole car then check if anything is wrong — it runs quality checks at every station. Problems are caught at the cheapest point (one component, one step) not at the most expensive point (finished car in the customer's hands).

Every push is a component entering the line. The pipeline checks it at each station. Only perfect components reach production.

## Mini Summary

- ✔ CI: every push triggers automated build + tests with immediate feedback
- ✔ CD (Delivery): pipeline extends to a deployable artifact; human deploys
- ✔ CD (Deployment): fully automated through to production
- ✔ Broken builds are top priority — they block the whole team's feedback loop
- ✔ Keep pipelines fast (under 10 min) by running fastest checks first and caching

# Guided Practice Quest

**The Verification Engine**

The Academy's Verification Engine needs a workflow. Design a GitHub Actions pipeline for a Spring Boot application with tests, build, and a staged deployment gate.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

A startup has no CI/CD. Developers push directly to `main` and deploy manually via SSH. They ship twice a month. When something breaks in production, it takes hours to diagnose which of the 200 commits caused it.

Design a CI/CD pipeline for them. For each stage write:
1. What it does
2. What tool or command would run it
3. What happens if this stage fails (pipeline stops? alert sent? rollback?)
4. Roughly how long it should take

Include at least 5 stages. Then explain: with this pipeline in place, what would the deployment process look like compared to now?

# Integration

**Connecting to Economics — The Theory of Constraints**

Eliyahu Goldratt's Theory of Constraints (1984) argues that every system has exactly one bottleneck that limits throughput. To improve the system, you must identify and relieve the bottleneck — not optimise other parts (which just shifts the bottleneck elsewhere).

In software delivery, the constraint is usually the slowest step in the value stream: manual testing, long release approval processes, or infrequent deployment windows. CI/CD attacks these constraints directly. Automated testing removes the manual testing bottleneck. Continuous deployment removes the deployment-window bottleneck.

The DORA research (Forsgren et al., *Accelerate*) empirically confirmed that high-performing teams have dramatically shorter lead times (time from code to production) — not because they rush, but because they've removed the bottlenecks via automation. Elite performers deploy on-demand; low performers deploy monthly or quarterly.

The economic implication: every hour of lead time is a delay in value delivery. CI/CD doesn't just make developers happier — it reduces the cost of every feature shipped.

How would you identify the biggest bottleneck in your current (or imagined) delivery pipeline?

# Lore Conclusion

The Verification Engine hums. Every submission is tested. Broken runes are caught within minutes. The Academy deploys new enchantments with confidence.

*"The pipeline is not bureaucracy,"* Archmage Veylan says. *"It is trust, made concrete. When the Verification Engine approves an artifact, everyone in the Academy can rely on it. That is worth the time it costs."*

The most productive teams in the industry deploy many times per day. Not because they are reckless — but because each deployment is small, verified, and reversible.
---
