---
id: se-sen-m1-03
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m1
moduleTitle: "Module 1: System Design"
moduleGlyph: "🏗️"
moduleSortOrder: 1
topicSlug: modular_architectures
topicTitle: "Modular Architectures"
topicSortOrder: 3
lesson: modular_architectures
title: "Modular Architectures"
sortOrder: 3
difficulty: 3
estimatedMinutes: 35
xpReward: 90
practiceType: NONE
questType: INVESTIGATION
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [philosophy, economics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - Explains domain boundaries and how they are identified through domain-driven design techniques
    - Articulates module contracts and the importance of published interfaces over internal access
    - Describes Spring Modulith's approach to enforcing module boundaries
    - Explains package-by-feature as opposed to package-by-layer and its architectural implications
    - Identifies the big-ball-of-mud anti-pattern and its specific causes
  keywords: [domain boundary, module contract, Spring Modulith, package-by-feature, package-by-layer, bounded context, big-ball-of-mud, encapsulation, coupling, cohesion]
  modelAnswer: |
    Modular architecture is the discipline of organising a codebase into high-cohesion, low-coupling modules with explicit contracts between them. Unlike a simple monolith or a fully distributed system, modular architecture occupies the productive middle ground: the deployment simplicity of a monolith with the domain clarity of microservices. The key insight is that decomposition is first a conceptual exercise in domain modelling, not a deployment decision.

    Domain boundaries are identified through Domain-Driven Design (DDD) techniques. A bounded context is a region of the domain where a single model applies consistently — where the same word means the same thing, and no external concept bleeds in uninvited. In an e-commerce system, "order" means different things in the sales context (a customer intent to purchase) versus the warehouse context (a fulfilment instruction) versus the billing context (a financial obligation). These different meanings signal domain boundaries. Each bounded context becomes a module with its own model, its own data, and its own vocabulary.

    Module contracts are the public interfaces through which modules interact. A well-designed module exposes only the minimum surface area needed by other modules. Everything else is private. This is encapsulation at the architectural level: the internal representation of a module's domain objects, its database schema, its service classes — all hidden behind the contract. When a module's internals change, no other module is affected.

    Spring Modulith provides explicit support for this pattern in Java. It enforces that only types in the module's root package (or explicitly exported sub-packages) are accessible to other modules. A module structure test validates the boundary rules at build time. This is a significant improvement over the common pattern of "we agreed not to access internal packages" — agreements that degrade under deadline pressure.

    Package-by-feature organises code by domain capability (`order`, `inventory`, `notification`) rather than by technical layer (`controllers`, `services`, `repositories`). The advantage is that all code related to a feature lives together, can be owned by a single team, and can be extracted as a service later without hunting across the codebase for scattered components. Package-by-layer is a symptom of thinking about architecture as a technical concern rather than a domain concern.

    The big-ball-of-mud emerges when module boundaries are not enforced. Any class calls any other class. The database is a shared mutable state. No team owns any part of the system. Changes ripple unexpectedly across the codebase. The fix is not a rewrite — it is imposing boundaries gradually: identify the domains, establish interface contracts, migrate cross-boundary calls to use the contracts, and then enforce the rules in the build pipeline.
guidedSteps:
  - type: SHORT_TEXT
    prompt: "An e-commerce system has modules for Order, Inventory, and Payment. The Order module directly queries the Inventory database table to check stock levels. Why is this a module boundary violation, and what should be done instead?"
    hint: "Consider what happens when the Inventory module changes its database schema. Who breaks?"
  - type: MULTIPLE_CHOICE
    prompt: "What does package-by-feature give you over package-by-layer that is architecturally significant?"
    options:
      - "Better performance due to class locality"
      - "All domain logic for a feature is co-located and can be owned by one team"
      - "Cleaner separation of HTTP from business logic"
      - "Easier dependency injection configuration"
    correctIndex: 1
    feedback: "Package-by-feature aligns code structure with domain structure. A team owning the 'order' feature owns all its controllers, services, repositories, and events. This cohesion makes future extraction possible and reduces cross-team interference."
  - type: FILL_BLANK
    prompt: "Spring Modulith enforces module boundaries by allowing access only to types in the module's ___ package by default."
    answer: "root (or top-level)"
    hint: "Types in sub-packages are treated as internal unless explicitly exported."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the key signal that two concepts belong in different bounded contexts?"
    options: ["They have different performance requirements", "The same word has different meanings in each context", "They are managed by different teams", "They have different database schemas"]
    correctIndex: 1
    feedback: "Ubiquitous language divergence is the DDD signal for a context boundary. When 'customer' means 'authenticated user' in one context but 'billing entity' in another, you have two distinct contexts that should not share a model."
  - type: MULTIPLE_CHOICE
    question: "Which practice most directly prevents a modular monolith from degenerating into a big ball of mud?"
    options: ["Writing comprehensive unit tests", "Enforcing module boundaries in the build pipeline", "Using a single database schema", "Following SOLID principles in service classes"]
    correctIndex: 1
    feedback: "Build-time enforcement (via Spring Modulith module structure tests or ArchUnit) makes boundary violations fail the build. Agreements and conventions alone degrade under pressure. The constraint must be automated."
retrieval:
  recall: "Describe the three properties that define a well-designed module: what it should maximise, what it should minimise, and what it must explicitly define."
  explain: "Explain why package-by-layer encourages accidental coupling between domains, using a concrete example of a common refactoring that breaks module boundaries."
  mistakeId:
    code: |
      // In the Order module
      public class OrderService {
          @Autowired
          private InventoryRepository inventoryRepository; // direct DB access
          
          @Autowired
          private PaymentRepository paymentRepository; // direct DB access
          
          public void placeOrder(OrderRequest req) {
              // bypasses module boundaries entirely
              int stock = inventoryRepository.findStock(req.productId());
              paymentRepository.savePayment(new Payment(req));
          }
      }
    answer: "This OrderService violates module boundaries in two ways. First, it directly injects repositories from the Inventory and Payment modules — bypassing those modules' public contracts entirely. This means OrderService is coupled to the internal database representation of both modules. When the Inventory team changes their database schema (a perfectly reasonable internal change), OrderService breaks. Second, it bypasses any business logic that lives in the Inventory and Payment modules — for example, Inventory's stock reservation logic or Payment's fraud detection. The fix is for Order to call published interfaces: InventoryService.checkAndReserveStock() and PaymentService.authorisePayment(). These are the module contracts. Order does not need to know how inventory is stored — it only needs to know whether stock is available."
---

# Hook

Every codebase that has ever grown uncontrolled was once clean. The first engineer wrote tidy, well-structured code. The second added their feature alongside it. By the tenth sprint, the organisational chart of the classes looked like a bowl of spaghetti. Modular architecture is the discipline that prevents this entropy — not through heroic refactoring, but through enforced boundaries that make violations impossible to ship.

# Lore Introduction

The Arcane Academy teaches that architecture is not what you draw on whiteboards — it is what the build pipeline enforces. A module boundary drawn in a diagram but unchecked in code is a suggestion, not a constraint. Senior architects earn their title by building systems where the right thing to do is the only thing the compiler will allow. Modular architecture is the science of making the structure of the code reflect the structure of the domain, with walls strong enough to resist deadline pressure.

# Core Learning

## Concept Introduction

Modular architecture organises a system into cohesive, loosely coupled modules where each module:
- Has a single, well-defined responsibility aligned with a domain concept
- Exposes a minimal public interface (its contract) to other modules
- Hides all internal implementation details
- Owns its own data and does not share database tables with other modules

This pattern applies both within a single deployable monolith (the modular monolith) and across distributed services. In both cases, the thinking is the same: find the natural seams of the domain, draw boundaries along those seams, and enforce those boundaries mechanically.

**Identifying Domain Boundaries**

Domain-Driven Design gives us the bounded context as the primary tool for identifying boundaries. A bounded context is a region where a single coherent model applies. The test for a boundary is the ubiquitous language: if the same word means different things in two parts of the system, you have two bounded contexts.

In an e-commerce platform:
- "Product" means a catalogue entry in the Search context, a stocked item in the Inventory context, and a purchased line item in the Order context
- These three meanings are different enough that forcing a single `Product` class to serve all three creates a class loaded with conditionals and cross-cutting concerns

Each bounded context becomes a module with its own `Product` representation, relevant only to that context.

**Module Contracts**

A contract is the public interface through which a module communicates with the outside world. It includes:
- Public service interfaces (e.g., `InventoryService.checkStock()`)
- Domain events published for other modules to consume
- Data Transfer Objects (DTOs) used in the interface — not internal domain entities

Everything that is not in the contract is private. A module's database tables, its internal domain objects, its helper services — all invisible to the outside. This encapsulation means the module can be refactored internally without breaking its consumers.

## Why It Matters

The modular architecture is not an aesthetic preference — it is a risk management strategy. When a module has a clean boundary, a change inside that module cannot accidentally break another module. Teams can work in parallel without constant merge conflicts. New engineers can understand their module without needing to understand the entire system. And when the domain eventually demands distribution, the module boundaries become service boundaries with minimal additional design work. The cost of retrofitting boundaries into an unstructured codebase is measured in months; the cost of establishing them from the start is measured in days.

## Worked Examples

**Example 1: Package-by-Feature vs Package-by-Layer**

Package-by-layer (common, problematic):
```
src/main/java/
  controllers/
    OrderController.java
    InventoryController.java
    PaymentController.java
  services/
    OrderService.java
    InventoryService.java
    PaymentService.java
  repositories/
    OrderRepository.java
    InventoryRepository.java
    PaymentRepository.java
```

In this structure, OrderController, OrderService, and OrderRepository are spread across three different packages. When an engineer works on the Order feature, they navigate between three packages. When a new engineer joins, they cannot determine which classes are related without tracing dependencies. Extracting Order into a separate service means harvesting classes from three different package trees.

Package-by-feature (correct):
```
src/main/java/
  order/
    OrderController.java       (public)
    OrderService.java          (public)
    OrderRepository.java       (private/internal)
    OrderDomainEntity.java     (private/internal)
  inventory/
    InventoryService.java      (public)
    InventoryRepository.java   (private/internal)
  payment/
    PaymentService.java        (public)
    PaymentRepository.java     (private/internal)
```

All Order code lives in one place. Internal details are in internal packages. Only the public interfaces are visible to other modules.

**Example 2: Spring Modulith Boundary Enforcement**

Spring Modulith treats each top-level package under the application package as a module. Types in the module's root package are accessible to other modules. Types in sub-packages (e.g., `order.internal`) are not.

A `ModuleStructureTests` class runs at build time and fails if any module accesses another module's internal types. This turns an architectural principle into a build constraint — it is impossible to accidentally bypass a module boundary without failing the build.

**Example 3: The Big Ball of Mud Anti-Pattern**

The big ball of mud emerges gradually through three stages:
1. A developer needs data from another module urgently. They add a direct repository injection instead of calling the module's interface.
2. Under deadline pressure, three more developers do the same thing. Nobody enforces the boundary.
3. The "shared" entities accumulate fields for every consuming module. The "Order" entity has 47 fields, half of which are only used by the Payment module.

After six months, no team can change anything without breaking everything else. The fix is not a rewrite — it is a systematic strangler fig: identify the violations, introduce proper interfaces for each violated boundary, migrate callers to use the interface, and then enforce the rule in the build pipeline.

## Common Mistakes

- **Believing that agreed conventions are sufficient.** Without automated enforcement, boundary rules degrade under deadline pressure. A failing build is the only reliable enforcer.
- **Drawing module boundaries around technical layers rather than domains.** A module called "services" or "repositories" is a technical module, not a domain module. Domain modules contain all layers of a single domain concern.
- **Sharing database tables between modules.** Even in a single database, module A should never write to module B's tables. Schema changes become impossible to make safely when tables are shared.
- **Making modules too small too early.** Premature granularity produces modules that are so small they cannot be developed independently. A module that contains only one class is probably not a module — it is an implementation detail.
- **Confusing module boundaries with service boundaries.** Identifying the right module boundaries within a monolith is the first step. Distributing along those boundaries comes only after they are validated through actual usage.

## Mental Model

Think of modules as walled city districts in a medieval city. Each district (module) has a gatehouse (public interface). You can enter the district through the gate; you cannot tunnel through walls. What happens inside the district is the district's own business. The city architect (the build pipeline) enforces that no tunnels are dug, regardless of how urgently a citizen claims to need one. The city functions because every district respects every other district's boundaries.

## Mini Summary

- ✔ Modules are identified by finding domain boundaries where ubiquitous language diverges
- ✔ Each module exposes a minimal public contract and hides all internal details
- ✔ Package-by-feature aligns code structure with domain structure, enabling team ownership and future extraction
- ✔ Spring Modulith enforces module boundaries at build time, making violations impossible to ship
- ✔ The big ball of mud is prevented through automated enforcement, not agreed conventions
- ✔ Module boundaries should be established before distribution is considered — they are the prerequisite for safe decomposition

# Guided Practice Quest

Work through the guided steps above. For each scenario, identify the specific boundary violation and articulate the rule that should enforce the correct boundary.

# Solo Practice Quest

Design the module structure for a hospital management system. The system handles: patient records, appointment scheduling, billing, pharmacy dispensing, and lab results. For each module:

1. Name the module and describe its bounded context in one sentence
2. List three public interface operations it exposes
3. Identify one piece of data it must NOT share with other modules
4. Identify one integration point (event or interface call) it uses from another module

Then describe one specific scenario where a developer under time pressure might be tempted to violate a module boundary, and explain how you would prevent it architecturally.

# Integration

**Philosophy connection:** The philosophical concept of abstraction — hiding complexity behind a simpler representation — is the foundation of modular architecture. Each module boundary is an abstraction boundary: the calling module knows what the module does, not how it does it. This is Plato's allegory of the cave applied to software: the callers see only the shadows (the public interface) of the complex reality within. The question for architectural design is: what is the right level of abstraction? Too much abstraction hides information needed for good decisions; too little abstraction creates coupling. How do you calibrate the right abstraction depth for a given domain?

**Economics connection:** Module boundaries are a form of technical debt prevention. The economic argument is straightforward: the cost of maintaining clean boundaries is low (design time and build enforcement). The cost of not maintaining them compounds over time — each additional cross-boundary coupling makes the next change more expensive. This is a classic compounding interest problem. The team that invested in boundaries early earns architectural dividends; the team that skipped them pays compounding maintenance interest indefinitely. At what point does the interest outweigh the ability to repay the principal?

# Lore Conclusion

The greatest modular systems look inevitable in hindsight — each module clearly owned, each boundary obvious. But that clarity was earned through disciplined thinking upfront and mechanical enforcement throughout. Every module boundary you draw and enforce is an investment in your future self's ability to change the system quickly and safely. The academy's highest achievers are not those who wrote the cleverest code inside the modules — they are those who designed the cleanest boundaries between them.
