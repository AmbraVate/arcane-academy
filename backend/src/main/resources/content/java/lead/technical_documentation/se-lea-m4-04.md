---
id: se-lea-m4-04
school: engineering
domainId: java
tier: LEAD
moduleId: se-lea-m4
moduleTitle: "Module 4: Knowledge Transfer"
moduleGlyph: "📚"
moduleSortOrder: 4
topicSlug: technical_documentation
topicTitle: "Technical Documentation"
topicSortOrder: 4
lesson: technical_documentation
title: "Technical Documentation"
sortOrder: 4
difficulty: 4
estimatedMinutes: 35
xpReward: 70
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [designing_learning_systems]
integrationDomains: [linguistics, design]
soloAssessment:
  type: AI_REVIEW
  rubricItems:
    - "Applies the Diátaxis framework to organise a documentation set"
    - "Explains docs-as-code methodology and its benefits"
    - "Designs a C4 model diagram (at least two levels) for a system"
    - "Creates an OpenAPI specification for at least two endpoints"
    - "Addresses documentation maintenance as an ongoing engineering discipline"
  keywords: [diataxis, docs-as-code, c4, openapi, swagger, maintain, review, pr, system, architecture]
  modelAnswer: |
    Documentation at scale requires structure and tooling.
    
    Diátaxis applied to a payment service:
    - Tutorial: "Process your first test payment in 10 minutes"
    - How-to: "Configure webhook retry policies"
    - Reference: "PaymentsAPI endpoint reference (OpenAPI)"
    - Explanation: "Why we chose Stripe Connect over direct processing"
    
    Docs-as-code:
    - Documentation lives in the same repo as code
    - Changes require PRs (same review process)
    - CI validates links, generates API docs from annotations
    - Documentation never diverges from the version it describes
    
    C4 model (Simon Brown):
    Level 1 (Context): System + external actors/dependencies
    Level 2 (Container): Major deployable components
    Level 3 (Component): Key modules within a container
    Level 4 (Code): UML class diagrams (usually auto-generated)
    
    OpenAPI (Swagger) for REST:
    - Machine-readable contract for API consumers
    - Auto-generates client SDKs, test scaffolding
    - In Spring: @Operation, @ApiResponse, @Schema annotations
    - Spring generates /v3/api-docs automatically
guidedSteps:
  - id: td-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A developer asks: "Where should I put documentation about how to set up the local development environment?"
      Using the Diátaxis framework, which documentation type is this?
    inputConfig:
      options:
        - "Tutorial — it's teaching a newcomer to use the system"
        - "How-to guide — it's a step-by-step guide to accomplish a specific goal (get local dev running)"
        - "Reference — it lists all the required tools"
        - "Explanation — it explains why the setup is structured this way"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["How-to guide — it's a step-by-step guide to accomplish a specific goal (get local dev running)"]
      rejectedFeedback: "Setup guides are how-to guides: task-oriented, goal-specific, step-by-step, assuming existing knowledge. A tutorial would guide a newcomer through building something while learning; a setup guide assumes they want to be productive quickly. The key distinction: tutorial = learning journey; how-to = task completion."
    hint: "The reader has a specific goal (get local dev running) and wants to accomplish it efficiently. Is this learning-oriented or task-oriented?"
    reflectionPrompt: "Diátaxis confusion is common with 'getting started' content. If the goal is 'understand the system by building something' = tutorial. If the goal is 'get your environment ready so you can work' = how-to. Most 'getting started' guides should be how-tos."
  - id: td-step-2
    sortOrder: 2
    inputType: SHORT_TEXT
    instruction: |
      Explain docs-as-code methodology and describe specifically how it solves the "stale documentation" problem.
    inputConfig:
      minWords: 35
    markingRule:
      matchMode: CONTAINS
      accepted: [same, repo, pr, review, ci, validate, version, code, change, together, stale, drift]
      rejectedFeedback: "Docs-as-code: documentation lives in the code repository, changes are made via PRs, CI validates documentation (link checking, API doc generation from annotations). Stale docs problem solved: when a developer changes an API endpoint and the change requires a PR, the PR review catches missing documentation updates. The code change and doc change ship together. Documentation can't silently drift from the code it describes."
    hint: "What makes documentation go stale? And how does treating docs like code (PRs, CI, same repo) prevent that?"
    reflectionPrompt: "The strongest form of docs-as-code is when CI fails the build if documentation doesn't exist for new API endpoints. This makes documentation maintenance a gating requirement, not an optional afterthought."
  - id: td-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe the C4 model's four levels and explain which level is most valuable for engineering communication across teams, and why.
    inputConfig:
      minWords: 40
    markingRule:
      matchMode: CONTAINS
      accepted: [context, container, component, code, level, c4, brown, system, diagram, architecture]
      rejectedFeedback: "C4 levels: Context (system + external actors), Container (deployable components: services, databases, frontends), Component (internal modules of a container), Code (class-level). Most valuable cross-team: Container diagram. It shows how the system is structured at a level everyone can understand — non-engineers see the system; engineers see the technical structure. Component diagrams are too detailed for cross-team use; Context is too abstract."
    hint: "Which C4 level is detailed enough to show how the system is structured, but abstract enough to be understood by all engineers on all teams?"
    reflectionPrompt: "The C4 Container diagram is the 'executive summary' of your architecture. It answers: what are the major deployable pieces, how do they talk to each other, and what are the external dependencies? This is the starting point for any architecture conversation."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the primary benefit of generating API documentation from code annotations (like Spring's @Operation) rather than writing it separately?"
    options:
      - "It requires less developer effort"
      - "The documentation is always in sync with the implementation because it's generated from the same code"
      - "It produces better-formatted documentation"
      - "It allows non-engineers to write the documentation"
    correctIndex: 1
    feedback: "Code-generated API docs cannot drift from the implementation — they're generated from the same source. If you rename a field, the generated docs reflect the rename immediately. Separately-maintained docs require a developer to remember to update them on every change, which reliably fails."
  - type: MULTIPLE_CHOICE
    question: "At what C4 model level would you show 'the OrderService calls the PaymentService via REST and stores orders in PostgreSQL'?"
    options:
      - "Level 1: Context"
      - "Level 2: Container"
      - "Level 3: Component"
      - "Level 4: Code"
    correctIndex: 1
    feedback: "Level 2 (Container) shows the major deployable units (services, databases, message brokers) and the interactions between them. OrderService, PaymentService, and PostgreSQL are all containers in the C4 sense. This is where runtime technology choices and integration patterns are visible."

retrieval:
  recall: "What are the four C4 model levels and what does each show? What is docs-as-code?"
  explain: "Explain to a new engineering manager why investing in an OpenAPI specification for your REST APIs pays dividends beyond just documentation."
  mistakeId:
    code: |
      // Architecture documentation:
      "PaymentService.java: L1-L4 uses a repository pattern.
       Lines 45-67 implement the payment processor interface.
       Method processPayment() at L71 calls external API.
       Database schema defined in migration V004.
       Exception handling in PaymentException.java."
    answer: "This is code-level documentation written for no identifiable audience. It describes what is already visible from reading the code. C4 documentation would instead: show how PaymentService fits in the larger system (Container level), what its responsibilities are (Component level), and why key design decisions were made (ADR). Document the architecture, not the code — code is its own documentation."
---

# Hook

Your company has grown from 5 to 80 engineers. The monolith has become 12 services. New engineers spend two weeks trying to understand the system before they can contribute. Cross-team API integrations repeatedly fail because there's no shared understanding of contracts.

Documentation exists — scattered across Confluence pages, README files, Slack history, and people's heads. It's not structured. It's not maintained. It's not trusted.

At scale, documentation is infrastructure. Treat it with the same engineering rigour you'd apply to production systems.

> How would a new engineer joining your team today understand the system architecture? What would they read? How long would it take?

# Lore Introduction

The Academy's knowledge administration challenge has grown with each century. In the early years, master artificers simply knew the system. As the Academy grew, that approach failed.

The great Documentation Reform created the current structure: Context Scrolls (how the Academy fits the world), Workshop Maps (what each workshop produces and depends on), Component Grimoires (the details of each component), and Decision Histories (why the Academy is structured as it is).

*"Not all knowledge needs to be in all places,"* Archmage Veylan says. *"The art is knowing what level of detail belongs where — and keeping each scroll accurate to the version it describes."*

# Core Learning

## Concept Introduction

Technical documentation at scale requires:
1. **Structure** (Diátaxis framework for content types)
2. **Architecture models** (C4 for visualising systems)
3. **API contracts** (OpenAPI for machine-readable specifications)
4. **Maintenance methodology** (docs-as-code for keeping documentation current)

**The C4 Model (Simon Brown):**
Four levels of abstraction for software architecture:
- **Context**: system + external actors and dependencies
- **Container**: major deployable units (services, databases, frontends)
- **Component**: key modules within a container
- **Code**: class-level detail (usually auto-generated)

**Docs-as-Code:**
Documentation lives in source control. Changes require PRs. CI validates and generates docs. Documentation cannot silently diverge from the code it describes.

## Why It Matters

Good documentation architecture:
- Enables new engineers to become productive weeks faster
- Makes cross-team API integration reliable (not "call someone")
- Preserves architectural reasoning across team turnover
- Enables async decision-making (review ADRs, not hold meetings)
- Creates system understanding distributed across the team, not locked in individuals

## Worked Examples

**C4 Container diagram (textual):**
```
[User Browser] → HTTPS → [React SPA]
                              ↓ REST
                         [API Gateway] → [OrderService]
                                              ↓ gRPC
                                         [PaymentService] → [Stripe API]
                                              ↓ JDBC
                                         [PostgreSQL DB]
                                              ↑ event
                                         [Kafka Cluster]
                                              ↑ consumes
                                         [NotificationService] → [SendGrid API]
```

**OpenAPI (Spring + Springdoc):**
```java
@Operation(summary = "Place an order",
           description = "Creates a new order and initiates payment processing")
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Order created",
                 content = @Content(schema = @Schema(implementation = OrderResponse.class))),
    @ApiResponse(responseCode = "400", description = "Invalid order request"),
    @ApiResponse(responseCode = "402", description = "Payment failed")
})
@PostMapping("/orders")
public ResponseEntity<OrderResponse> placeOrder(@RequestBody @Valid OrderRequest request) { ... }
```

**Docs-as-code CI pipeline:**
```yaml
# In .github/workflows/docs.yml
- name: Validate documentation
  run: |
    # Check all internal links are valid
    npx linkcheck docs/
    # Generate API docs from Spring annotations
    ./gradlew generateOpenApiDocs
    # Fail if OpenAPI spec changes without corresponding PR label
    python scripts/check_api_doc_freshness.py
```

## Common Mistakes

- **Documentation without audience** — writing C4 diagrams nobody reads because they're at the wrong level for the audience.
- **Diagrams in slide decks** — architecture diagrams in Confluence pages or Miro boards that nobody updates; use code-driven diagrams (Structurizr, Mermaid).
- **API docs without code generation** — separately maintained API docs inevitably drift.
- **No documentation review in PRs** — documentation updates are optional unless the CI enforces them.
- **Over-documenting code** — document architecture and decisions; let code document itself.

## Mental Model

Technical documentation is **navigation infrastructure**. A city map has four levels: a world map (context), a city map (containers), a street map (components), and a building blueprint (code). Different navigation questions require different map levels. Mixing levels (street map detail on a world map) makes all of them useless. Match the map to the navigation question.

## Mini Summary

- ✔ Diátaxis: four types (tutorial, how-to, reference, explanation) — never mix them in one document
- ✔ C4 model: four levels (context, container, component, code) — match the level to the audience
- ✔ OpenAPI: machine-readable API contracts generated from code annotations — never manually maintained
- ✔ Docs-as-code: documentation in source control, reviewed in PRs, validated in CI
- ✔ Document architecture and decisions — code is its own documentation for implementation

# Guided Practice Quest

**The Documentation Reform**

Apply the C4 model to a distributed system, create an OpenAPI fragment, and audit a documentation set for Diátaxis compliance.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You are the lead engineer on a 6-service microservices platform. An architectural review has found:
- No system-level architecture documentation
- API docs exist only as Confluence pages that are 6 months out of date
- New engineers take 3 months to understand how services interact
- Two teams recently built conflicting integrations because they didn't know about each other's APIs

Design a complete documentation system for this platform:
1. Create a C4 Context diagram and Container diagram (textual is fine — describe nodes and connections)
2. Define what Diátaxis documents each service should have (at minimum)
3. Design the OpenAPI strategy (which services get it, how it's generated, how it's published)
4. Design the docs-as-code workflow (where does documentation live, what does the CI check?)
5. How would you manage the migration from current state (Confluence chaos) to this system without disrupting the team?

# Integration

**Connecting to Design — Information Architecture**

Information Architecture (IA) is a discipline concerned with the structure and organisation of information to support usability and findability. Peter Morville and Louis Rosenfeld's work (1998) established IA as a formal design discipline addressing: organisation (how information is grouped), labelling (how it's described), navigation (how people move through it), and search (how people find specific items).

Technical documentation is an information architecture problem. When engineers can't find the documentation they need (navigation failure), when similar documents are scattered across three systems (organisation failure), when documentation titles don't reflect content (labelling failure) — these are IA problems, not just "documentation problems."

The Diátaxis framework is an IA solution: it provides a clear organisational principle (four types based on user need) and labelling convention (tutorial, how-to, reference, explanation). C4 provides IA for architecture diagrams: four levels, each with a defined audience and purpose.

Applying IA thinking to documentation design: before creating any document, ask: what is the user's question? what level of detail do they need? where would they look for it? Does this document fit into an existing structure or does it need a new one?

How would you conduct an information architecture audit of your team's current documentation and prioritise what to fix?

# Lore Conclusion

The Documentation Reform is complete. New artificers find the Context Scroll immediately, understand the Workshop Map in an hour, and are productive in two weeks.

*"The scrolls serve those who read them, not those who write them,"* Archmage Veylan says. *"Write for the reader. Organise for the reader. Update for the reader. The writer's convenience is irrelevant — the reader's understanding is everything."*

Documentation is a gift to the future. Write it with that intention.
---
