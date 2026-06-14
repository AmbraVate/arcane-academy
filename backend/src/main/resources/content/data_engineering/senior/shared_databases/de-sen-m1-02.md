---
id: de-sen-m1-02
school: engineering
domainId: data-engineering
tier: SENIOR
moduleId: de-sen-m1
moduleTitle: "Module 1: Database Architecture"
moduleGlyph: "🏗️"
moduleSortOrder: 1
topicSlug: shared_databases
topicTitle: "Shared Databases"
topicSortOrder: 2
lesson: shared_databases
title: "Shared Databases"
sortOrder: 2
difficulty: 4
estimatedMinutes: 35
xpReward: 75
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-sen-m1-01]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains the shared database integration pattern and its trade-offs
    - Describes schema-per-service as a logical separation within a shared database
    - Identifies the coupling risks of shared database access across services
    - Explains the difference between shared schema and shared database
    - Describes multi-tenancy patterns in shared databases
  keywords: [shared database, shared schema, schema-per-service, multi-tenancy, coupling, integration pattern, schema ownership, table-per-tenant, row-level security, schema isolation, integration database, schema coupling]
  modelAnswer: |
    A shared database is a single physical database instance used by multiple services or teams. The shared schema antipattern: all services access all tables — creating tight schema coupling, deployment coordination requirements, and fragility. Shared database with schema isolation: each service owns its own schema (PostgreSQL schema/namespace), other services cannot access it directly — data is shared via APIs or events. Multi-tenancy patterns: (1) Table-per-tenant: each tenant has their own tables — maximum isolation but poor scalability for many tenants. (2) Schema-per-tenant: each tenant has their own schema — good isolation, hundreds of tenants practical. (3) Row-level-security tenant isolation: all tenants in shared tables, RLS filters by tenant_id — most scalable but least isolated. The correct pattern for services in a shared database: schema-per-service with clear ownership, no direct cross-schema access except via defined interfaces.
guidedSteps:
  - id: de-sen-m1-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Service A reads directly from Service B's database tables to get data it needs. Service B's team renames a column. What happens?
    inputConfig:
      options:
        - "Nothing — Service A can use the old column name indefinitely"
        - "Service A breaks immediately because its queries reference the renamed column"
        - "The database automatically translates old column names to new ones"
        - "Service A gets a schema mismatch warning but continues working"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Service A breaks immediately because its queries reference the renamed column"]
      rejectedFeedback: "Direct cross-service database access creates tight schema coupling. The column rename is a backward-incompatible change from Service A's perspective — its queries will fail with 'column does not exist'. Service B's team did not know Service A depended on that column (there was no contract between them). This is the integration database antipattern: using a shared database as an integration mechanism creates a web of invisible dependencies. The correct pattern: Service B owns its schema and exposes data via a defined API (REST, events, or a stable view that Service B controls). Service B can rename internal columns freely as long as the API contract is unchanged. This is why schema ownership and access control (REVOKE SELECT on Service B's schema from Service A's role) are essential in shared databases."
    hint: "If Service A queries Service B's columns directly, what is the dependency between them when Service B changes its schema?"
    reflectionPrompt: "How would you enforce that Service A cannot access Service B's tables, even though they are in the same database?"
  - id: de-sen-m1-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In PostgreSQL, a logical namespace that separates tables from different services or tenants within the same physical database is called a ________.
    inputConfig:
      placeholder: "schema"
    markingRule:
      matchMode: CONTAINS
      accepted: [schema, schemas, "PostgreSQL schema", "database schema", namespace, "search_path"]
      rejectedFeedback: "A PostgreSQL schema (also called a namespace) is a named container for database objects: tables, views, functions, sequences. By default, objects are in the 'public' schema. Multiple schemas in one database: CREATE SCHEMA loans_service; CREATE TABLE loans_service.loans (...); CREATE SCHEMA members_service; CREATE TABLE members_service.members (...). Access control per schema: GRANT USAGE ON SCHEMA loans_service TO loans_app_user; REVOKE ALL ON SCHEMA members_service FROM loans_app_user; — the loans service user cannot access members_service tables. This provides logical isolation within a physical database: teams own their schemas, database administrators can manage all schemas, operational overhead (backup, monitoring) stays at the database level. Schema-per-service in a shared database combines operational simplicity with service schema isolation."
    hint: "In PostgreSQL, this is the container that provides namespacing within a single database — like a folder for tables."
    reflectionPrompt: "How is a PostgreSQL schema different from a database, and when would you use each level of isolation?"
  - id: de-sen-m1-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe the row-level-security (RLS) multi-tenancy pattern and identify one scenario where it is appropriate and one where schema-per-tenant is a better choice.
    inputConfig:
      minWords: 35
    markingRule:
      matchMode: CONTAINS
      accepted: [RLS, row-level, tenant_id, filter, scalable, thousands, isolation, schema, separate, compliance, regulate, GDPR, breach]
      rejectedFeedback: "RLS multi-tenancy: all tenants share tables, tenant_id column identifies each tenant's rows, RLS policy filters rows automatically (USING (tenant_id = current_setting('app.tenant_id')::INT)). One database, one schema, N tenants — scales to tens of thousands of tenants with low overhead. Good for: SaaS with many small tenants, homogeneous tenant data, no per-tenant compliance isolation requirements. Schema-per-tenant is better when: (1) Tenants have dramatically different schema evolution needs (enterprise tenants want custom columns). (2) Compliance requires physical data isolation per tenant (HIPAA, financial data — tenant A's data must not be co-located with tenant B's at the storage level). (3) Tenants need independent backup/restore (cannot restore tenant A's data without restoring the shared table). (4) You have fewer tenants (tens, not thousands) where the operational overhead of separate schemas is acceptable. RLS multi-tenancy trades isolation for scalability; schema-per-tenant trades scalability for isolation."
    hint: "Think about the trade-offs: scalability vs isolation, and which use cases require strong isolation at the storage level."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The 'integration database' antipattern refers to:"
    options:
      - "Using a database as the integration layer between microservices — where services share tables instead of communicating via APIs"
      - "Building a data integration pipeline that merges multiple source databases"
      - "Creating a single read-model database for analytics that aggregates from multiple operational databases"
      - "Integrating a third-party database into your application"
    correctIndex: 0
    feedback: "The integration database antipattern (described by Martin Fowler in Patterns of Enterprise Application Architecture): multiple applications or services share a single database, reading and writing directly to each other's tables. Problems: (1) Tight schema coupling — any schema change potentially breaks other services. (2) No encapsulation — business logic leaks into SQL queries by services that shouldn't own that logic. (3) No versioning contract — there is no agreed API between services. (4) Impossible to test independently — services cannot be tested without the full shared schema. The pattern was common in enterprise integration before service-oriented architecture became standard. The correct alternative: services own their schemas, expose data via stable APIs (REST/GraphQL/events), and other services consume those APIs — not tables."
  - type: MULTIPLE_CHOICE
    question: "For a SaaS application with 50,000 small tenants, which multi-tenancy approach is most scalable?"
    options:
      - "Database-per-tenant: each tenant gets their own PostgreSQL database"
      - "Schema-per-tenant: each tenant gets their own PostgreSQL schema"
      - "Row-per-tenant: all tenants share tables, filtered by tenant_id with RLS"
      - "Table-per-tenant: each tenant gets separate tables prefixed with their tenant ID"
    correctIndex: 2
    feedback: "At 50,000 tenants: database-per-tenant = 50,000 separate PostgreSQL instances (unmanageable). Schema-per-tenant = 50,000 schemas (PostgreSQL handles hundreds of schemas well but 50,000 creates management overhead). Table-per-tenant = 50,000 × N tables (millions of tables — serious performance problems for PostgreSQL's system catalogs). Row-per-tenant with RLS: one set of tables, RLS filters by tenant_id. Scales to hundreds of thousands of tenants with no schema overhead. The trade-off: all tenant data is physically co-located — a bug could leak tenant A's data to tenant B (RLS provides logical, not physical isolation). For compliance-sensitive data (healthcare, finance), schema or database separation may be required despite the scale cost. For typical SaaS without extreme compliance requirements, RLS is the standard scalable approach."
retrieval:
  recall: "Describe three different multi-tenancy patterns (database-per-tenant, schema-per-tenant, row-level security) and list the trade-offs of each across four dimensions: isolation strength, operational complexity, scalability, and per-tenant customisation."
  explain: "Explain why schema-per-service in a shared database can give teams deployment autonomy without requiring separate database instances. What access controls enforce the isolation?"
  mistakeId:
    code: |
      -- loans-service connects as 'app_user' and queries:
      SELECT l.id, m.full_name, m.email, i.title
      FROM loans_schema.loans l
      JOIN members_schema.members m ON l.member_id = m.id
      JOIN items_schema.items i ON l.item_id = i.id
      WHERE l.status = 'OVERDUE';
      
      -- members-service also connects as 'app_user'
      -- items-service also connects as 'app_user'
      -- All three use the same role and can read all schemas
    answer: "Two integration problems: (1) loans-service directly reads members_schema and items_schema — coupling across service boundaries. If members_schema renames full_name to display_name, loans-service breaks. If items_schema adds a NOT NULL constraint that changes item querying, loans-service SQL may be affected. These are invisible dependencies with no contract. Fix: loans-service should call Members API and Items API (or receive member/item data via events), not directly query their schemas. (2) All services share the same database role 'app_user' — no schema isolation. loans-service can read members_schema even if it shouldn't. Fix: create separate roles per service (loans_app, members_app, items_app), grant each only USAGE on their own schema, REVOKE all cross-schema access. For the cross-service data need in the overdue query: either maintain a read-optimised view in loans_schema that is populated via ETL from member/item data, or expose an API endpoint in each service that loans-service calls."
---

# Hook

A shared database can be a well-organised collaboration space or a tight coupling nightmare — the difference is whether teams own their schema zones or treat the entire database as a common area. The patterns and anti-patterns of shared database access determine whether a single database instance empowers teams or creates fragile dependencies.

# Lore Introduction

"The items service is down after the loans team's deploy," the Senior Engineer said, reading the incident report. "The loans migration renamed a column in the shared schema. The items service was querying it directly." The Lead Data Engineer looked at the architecture. "Both services connect as the same database user. Both can access both schemas. There's no enforcement of boundaries." The Senior Engineer leaned back. "We have the worst of both worlds: the coupling of a monolith without the operational clarity of a monolith. We need to define schema ownership and enforce it." She pulled up the access control configuration. "Schema-per-service. Role-per-service. No cross-schema queries. Data sharing via controlled interfaces, not table access."

# Core Learning

## Concept Introduction

### Schema-Per-Service Pattern

```sql
-- Separate schemas for logical isolation within one PostgreSQL database
CREATE SCHEMA loans_service;
CREATE SCHEMA members_service;
CREATE SCHEMA items_service;
CREATE SCHEMA shared_views;    -- controlled cross-service read models

-- Each service creates its own tables:
CREATE TABLE loans_service.loans (...);
CREATE TABLE members_service.members (...);
CREATE TABLE items_service.items (...);

-- Service-specific roles:
CREATE ROLE loans_app;
CREATE ROLE members_app;
CREATE ROLE items_app;

-- Loans service can only see loans_service schema:
GRANT USAGE ON SCHEMA loans_service TO loans_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA loans_service TO loans_app;
REVOKE ALL ON SCHEMA members_service FROM loans_app;   -- enforced isolation
REVOKE ALL ON SCHEMA items_service FROM loans_app;

-- Shared read model: loans service can read a controlled view
CREATE VIEW shared_views.member_lookup AS
    SELECT id, full_name, membership_tier FROM members_service.members WHERE is_active = TRUE;
GRANT SELECT ON shared_views.member_lookup TO loans_app;  -- controlled, versioned
-- members_service team owns this view and maintains its contract
```

### Multi-Tenancy Patterns

```sql
-- Pattern 1: ROW-LEVEL SECURITY (most scalable)
-- All tenants in shared tables, filtered by tenant_id
ALTER TABLE loans ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON loans
    USING (tenant_id = current_setting('app.tenant_id')::BIGINT);
-- Application: SET LOCAL app.tenant_id = :tenantId; SELECT * FROM loans;
-- → 100,000 tenants, single schema, RLS enforces isolation
-- Risk: a missing policy allows cross-tenant data leakage

-- Pattern 2: SCHEMA-PER-TENANT (good for hundreds of tenants)
-- Tenant-specific schemas with identical structure
CREATE SCHEMA tenant_acme;
CREATE TABLE tenant_acme.loans (...);
CREATE SCHEMA tenant_beta;
CREATE TABLE tenant_beta.loans (...);
-- Each tenant gets a dedicated schema; application connects to the right schema
-- Good isolation; harder to query across tenants for aggregates

-- Pattern 3: DATABASE-PER-TENANT (for regulated industries, few tenants)
-- Each tenant is a separate PostgreSQL database (or cluster)
-- Maximum isolation; maximum operational complexity
-- Required when: HIPAA, PCI-DSS, contractual data isolation guarantees

-- Choosing:
-- 10,000+ tenants → Row-level security
-- 100-10,000 tenants → Schema-per-tenant
-- < 100 tenants with compliance requirements → Database-per-tenant
```

### Controlled Cross-Service Data Access

```java
// Anti-pattern: direct cross-schema query (service A reads service B's tables)
// Result: tight coupling, no contract, broken by any schema change

// Pattern 1: Read model view (stable interface across schemas)
// members_service defines a view for other services:
CREATE VIEW shared_views.loan_member_summary AS
    SELECT id AS member_id,
           full_name,
           membership_tier,
           max_concurrent_loans
    FROM members_service.members WHERE is_active = TRUE;
-- loans_service can query this view; members_service maintains the contract

// Pattern 2: API call (loosely coupled)
// loans_service calls members API instead of reading the database:
@Service
public class LoanService {
    private final MembersClient membersClient;  // Feign/RestTemplate client
    
    public Loan createLoan(Long memberId, Long itemId) {
        MemberDto member = membersClient.getMember(memberId);  // HTTP call
        if (!member.canBorrow()) throw new MemberNotEligibleException();
        // ... create loan
    }
}

// Pattern 3: Event-driven (eventual consistency)
// members-service publishes MemberTierChanged event
// loans-service consumes it and maintains a local cache of member tiers
// Loans service reads from local cache — no cross-service DB call
```

## Common Mistakes

- **All services sharing one database role**: use one role per service, grant only access to that service's schema. Shared roles create invisible cross-service access that violates logical boundaries.
- **Services reading each other's tables directly**: this is the integration database antipattern. Define stable interfaces (views, APIs, events) for cross-service data needs.
- **No schema ownership documentation**: "we share a database" without clear schema ownership leads to uncoordinated schema changes. Maintain a schema ownership register.
- **RLS without testing every query path**: a missing RLS policy on one query path allows all tenant data to be returned. Test every query in multi-tenant mode — asserting that tenant A cannot see tenant B's data.

## Mental Model

A shared database with schema isolation is like a multi-tenant office building: different companies (services) work in separate offices (schemas), share the building infrastructure (database server), but cannot walk into each other's offices without permission. The reception desk (shared views API) is the controlled interface for communication — you request information through reception, not by walking directly into another company's filing room. The building manager (DBA) maintains access control to ensure this isolation is enforced, not just politely observed.

## Mini Summary

- ✔ Shared database: one physical instance, multiple services — operational simplicity with isolation via schemas
- ✔ Schema-per-service: logical isolation, separate roles, no direct cross-schema table access
- ✔ Integration database antipattern: services reading each other's tables directly — avoid
- ✔ Shared views and APIs: controlled interfaces for cross-service data
- ✔ Multi-tenancy: RLS (many tenants), schema-per-tenant (medium), database-per-tenant (few, high compliance)
- ✔ Enforce isolation with GRANT/REVOKE per schema per service role

# Guided Practice Quest

Work through the guided steps to refactor the Archive's shared schema into three service schemas (loans, members, items), create service-specific roles with correct GRANT/REVOKE statements, and design the shared view that allows the loans service to read member summary data without accessing the members schema directly.

# Solo Practice Quest

Design the multi-service shared database architecture for the Consortium's Archive platform. Tasks: (1) Define four service schemas (loans, members, items, fines) with ownership and role assignments — write all CREATE SCHEMA, CREATE ROLE, and GRANT statements; (2) Identify five cross-service data needs and design stable interfaces for each (views, not direct table access); (3) The platform needs to support 500 institutional library tenants with schema-per-tenant isolation — design the tenant onboarding process (CREATE SCHEMA, apply migrations, create tenant user); (4) Design an RLS implementation for a simpler tier (public libraries with 50,000 patron accounts) where row isolation per patron is needed; (5) Write the access control audit query that verifies no service role has access to another service's schema tables; (6) One service needs to run a cross-schema report monthly — design this without giving it permanent cross-schema access.

# Integration

**Mathematics**: Schema isolation in a shared database implements information-theoretic access control. The access control matrix for N services and M schemas defines which (service, schema) pairs have access. The fully isolated configuration has N diagonal entries (service i accesses schema i) and N×(M-N) = 0 off-diagonal entries for cross-service access. This is the identity matrix for the access relationship — a diagonal access matrix is the minimum-coupling configuration. Any off-diagonal entry represents a coupling dependency: changes to schema j may break service i. The integration database antipattern is the all-ones matrix — every service accesses every schema. The coupling index C = (number of off-diagonal accesses) / (N×M - N) measures how far the actual architecture deviates from the diagonal ideal. Architectural review goals: minimise C.

**Sciences (Urban Planning — Zoning Laws)**: Schema-per-service access control parallels urban zoning regulations. Zoning law: residential areas cannot be used for industrial purposes, industrial zones cannot expand into residential areas — each zone has permitted uses and restrictions. Database role/schema access control is software zoning: the loans schema is zoned for the loans service, the members schema is zoned for the members service, mixed-use is explicitly prohibited except at defined interfaces (shared views = commercial corridors between zones). Just as zoning prevents a factory from being built next to a school (protecting residential quality of life), schema isolation prevents one service from reading another's internal data (protecting service autonomy). The analogy extends to enforcement: zoning laws are enforced by government permits (GRANT/REVOKE), not by trust and convention.

# Lore Conclusion

"Schema isolation deployed," the Senior Engineer reported. "Four service-specific roles. Each role grants USAGE only on its own schema. Cross-schema access via shared_views only." The Lead Data Engineer reviewed the access matrix. "The items service no longer has read access to loans_schema. What happens to the cross-service report?" The Senior Engineer showed the solution. "A shared view in shared_views.overdue_report, owned by loans_service, read-accessible to reporting. The items and members data they need is joined in that view — but only the columns they need." The Lead Data Engineer closed the incident report. "The column rename won't break anyone else's queries now." The Senior Engineer agreed. "Schema boundaries enforced at the database level, not just by convention. The next topic: when schema isolation in a shared database isn't enough — service-oriented data and why some systems genuinely need separate storage."

---
