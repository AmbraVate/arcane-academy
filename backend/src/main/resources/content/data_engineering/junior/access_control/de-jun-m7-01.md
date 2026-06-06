---
id: de-jun-m7-01
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m7
moduleTitle: "Module 7: Data Security"
moduleGlyph: "🔐"
moduleSortOrder: 7
topicSlug: access_control
topicTitle: "Access Control"
topicSortOrder: 1
lesson: access_control
title: "Access Control"
sortOrder: 1
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m6-04]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains the principle of least privilege and its application to database roles
    - Distinguishes GRANT and REVOKE, and their effect on role permissions
    - Describes row-level security and column-level security
    - Explains the difference between authentication and authorisation
    - Identifies common access control mistakes (shared superuser accounts, wildcard grants)
  keywords: [least privilege, GRANT, REVOKE, role, permission, row-level security, RLS, column-level, authentication, authorisation, superuser, schema permission, object permission, pg_roles, DENY]
  modelAnswer: |
    Least privilege: each user/service should have only the permissions needed for its function — nothing more. Implemented via roles: CREATE ROLE app_reader; GRANT SELECT ON TABLE loans TO app_reader; GRANT app_reader TO app_user. Authentication (who are you?) is separate from authorisation (what can you do?). GRANT assigns permissions; REVOKE removes them. Row-level security (RLS): restricts which rows a user can see — CREATE POLICY visible_loans ON loans FOR SELECT USING (member_id = current_setting('app.member_id')::BIGINT). Column-level security: GRANT SELECT (id, name) ON members TO analyst — analyst cannot SELECT email or phone. Common mistakes: shared superuser/admin accounts (no audit trail), GRANT ALL ON ALL TABLES (violates least privilege), applications connecting as database owner (can DROP tables).
guidedSteps:
  - id: de-jun-m7-01-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      The Archive application connects to the database using a single account named 'postgres' (the superuser). Why is this a security problem?
    inputConfig:
      options:
        - "Superuser accounts have lower performance due to additional permission checks"
        - "The application can execute any DDL (DROP TABLE, ALTER TABLE) or read any data — SQL injection or a bug could destroy the entire database"
        - "PostgreSQL prevents superuser accounts from being used by applications"
        - "Superuser accounts cannot use prepared statements, enabling SQL injection"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The application can execute any DDL (DROP TABLE, ALTER TABLE) or read any data — SQL injection or a bug could destroy the entire database"]
      rejectedFeedback: "Using superuser credentials in an application violates least privilege. A superuser can: DROP any table, ALTER any schema, TRUNCATE any data, read any table including system tables, bypass row-level security. If a SQL injection vulnerability exists (even a mild one), an attacker has full database access. If a bug causes an unintended query, it could drop production tables. The principle of least privilege: the application should connect as a role that can only INSERT/UPDATE/DELETE/SELECT on the tables it actually needs. A schema migration tool needs DDL permissions — it runs separately, not as the application process. Creating separate roles (app_read, app_write, app_admin) limits the blast radius of any single component being compromised."
    hint: "What could a SQL injection attack do with a superuser connection that it couldn't do with a limited application user?"
    reflectionPrompt: "How would you separate the permissions needed by the application vs. those needed by the database migration tool?"
  - id: de-jun-m7-01-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The SQL command to give a role permission to SELECT rows from a table is ________, and the command to remove that permission is REVOKE.
    inputConfig:
      placeholder: "GRANT"
    markingRule:
      matchMode: CONTAINS
      accepted: [GRANT, grant]
      rejectedFeedback: "GRANT assigns database permissions: GRANT SELECT ON TABLE loans TO analyst_role. REVOKE removes them: REVOKE SELECT ON TABLE loans FROM analyst_role. Permission types: SELECT, INSERT, UPDATE, DELETE (DML), REFERENCES (FK constraints), TRIGGER, TRUNCATE, EXECUTE (functions), USAGE (schemas, sequences). GRANT ALL includes all applicable permissions — avoid for most roles. WITH GRANT OPTION: allows the grantee to further grant that permission to others — rarely appropriate. In PostgreSQL, permissions are object-level (per table, per schema) and role-based (roles granted to users). Schema-level: GRANT USAGE ON SCHEMA public TO analyst_role (allows seeing the schema). Table-level: GRANT SELECT ON TABLE loans TO analyst_role. Both are needed — schema USAGE alone doesn't allow table access."
    hint: "Two opposite commands: one adds permissions, one takes them away."
    reflectionPrompt: "What is the difference between granting SELECT on a specific table vs. SELECT on ALL TABLES IN SCHEMA?"
  - id: de-jun-m7-01-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain how row-level security (RLS) allows multiple members to share a database table while ensuring each member can only see their own loans — without any application-level filtering.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [policy, USING, RLS, row-level, enable, current_user, session, variable, filter, invisible, automatically, secure]
      rejectedFeedback: "Row-level security (RLS) in PostgreSQL: ALTER TABLE loans ENABLE ROW LEVEL SECURITY. CREATE POLICY member_isolation ON loans FOR SELECT USING (member_id = current_setting('app.member_id')::BIGINT). When a query runs: SELECT * FROM loans, PostgreSQL appends the RLS policy as a WHERE clause automatically. The query becomes effectively: SELECT * FROM loans WHERE member_id = <current member's id>. The application sets the session variable: SET LOCAL app.member_id = 42. The member sees only their rows — even if the application accidentally omits a WHERE clause. This is defence in depth: application-level filtering is still good practice, but RLS is a database-enforced backstop. BYPASSRLS role attribute: superusers and roles with BYPASSRLS skip the policy — needed for DBA access and ETL processes."
    hint: "RLS attaches a filter condition to every query automatically, without the application needing to add it."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Column-level security in SQL is implemented by:"
    options:
      - "Adding a permission flag column to each table"
      - "Using GRANT SELECT (column1, column2) ON table TO role — only granting access to specific columns"
      - "Encrypting sensitive columns so only authorised roles can decrypt them"
      - "Using triggers to block SELECT on sensitive columns"
    correctIndex: 1
    feedback: "Column-level permission grants restrict which columns a role can access. GRANT SELECT (id, full_name, membership_tier) ON members TO analyst_role — the analyst can SELECT these three columns but not email, phone, address, or birth_date. If the analyst queries SELECT * FROM members, they receive only the columns they have permission for (PostgreSQL) or receive an error (depending on the database). Implementation: create a view that exposes only safe columns (CREATE VIEW public_members AS SELECT id, full_name, tier FROM members; GRANT SELECT ON public_members TO analyst_role) — an alternative approach that's more portable. Column-level grants are useful when most columns are safe but a few are PII (personally identifiable information) that only specific roles should see."
  - type: MULTIPLE_CHOICE
    question: "The principle of least privilege applied to database roles means:"
    options:
      - "Every user should have the minimum number of database connections possible"
      - "Each role should have only the permissions required for its specific function — no more"
      - "Database administrators should have the least number of roles assigned"
      - "Queries should use the minimum number of tables possible to reduce permission complexity"
    correctIndex: 1
    feedback: "Least privilege: a role has only the permissions it needs to perform its function. Application read role: SELECT on specific tables. Application write role: SELECT + INSERT + UPDATE on specific tables, not DELETE. ETL role: SELECT on source tables, INSERT on target tables. Report role: SELECT only, specific schemas. Admin role: full access to specific schemas, no access to system tables. Benefits: (1) Blast radius reduction — a compromised application credential cannot DROP tables. (2) Defence in depth — application bugs cannot delete data the application has no DELETE grant on. (3) Audit clarity — permissions that are granted must be justified. Implementation: use role groups (app_read, app_write, etl_user), grant to roles, then grant roles to users: GRANT app_read TO reporting_service."
retrieval:
  recall: "Write the SQL to create a read-only database role for the Archive analytics team. They need SELECT on the loans, members, and items tables in the archive schema, but must NOT see the email, phone, or address columns in members."
  explain: "Explain the difference between authentication (who you are) and authorisation (what you can do) in the context of database security. How does each apply to a PostgreSQL connection?"
  mistakeId:
    code: |
      -- Setup for new reporting service
      GRANT ALL ON ALL TABLES IN SCHEMA public TO reporting_service;
      GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO reporting_service;
      GRANT ALL PRIVILEGES ON DATABASE archive TO reporting_service;
    answer: "Granting ALL on all tables and ALL PRIVILEGES on the database violates least privilege for a reporting service. Problems: (1) GRANT ALL ON ALL TABLES includes INSERT, UPDATE, DELETE, TRUNCATE — a reporting service only needs SELECT. If the service is compromised or has a bug, it can modify or delete production data. (2) GRANT ALL PRIVILEGES ON DATABASE includes CONNECT, CREATE, TEMP — the reporting service can create new schemas and tables, cluttering the database. (3) Wildcard grants (ALL TABLES) apply to current tables; new tables added later must be granted separately anyway (or you add DEFAULT PRIVILEGES, which should also be restricted). Fix: GRANT CONNECT ON DATABASE archive TO reporting_service; GRANT USAGE ON SCHEMA public TO reporting_service; GRANT SELECT ON TABLE loans, members, items TO reporting_service — only the tables actually needed, only SELECT."
---

# Hook

Every database breach in the news follows the same story: the application connected as a privileged account, an attacker got the credentials or exploited a vulnerability, and they had access to everything. Database security starts with a simple principle — least privilege — and builds from there with roles, row-level policies, and column-level restrictions that limit what any single credential can do.

# Lore Introduction

"The member data breach at the City Library last quarter — do you know how it happened?" the Senior Archivist asked. The Junior Engineer shook their head. "SQL injection in their search form. The application connected as a database administrator. The attacker read the entire members table: emails, addresses, phone numbers." She pulled up the Archive's database configuration. "We connect as postgres. The superuser." The Junior Engineer felt a cold realisation. "We have the same problem." The Senior Archivist nodded. "Everything the application can do, an attacker can do with the application's credentials. We fix this today. Least privilege: the application gets exactly the permissions it needs. No more."

# Core Learning

## Concept Introduction

### Roles and Permissions

```sql
-- Create roles (groups of permissions, not tied to specific users)
CREATE ROLE archive_app_read;
CREATE ROLE archive_app_write;
CREATE ROLE archive_analytics;
CREATE ROLE archive_etl;
CREATE ROLE archive_admin;

-- Grant permissions to roles (least privilege — only what each role needs)

-- Read-only application role
GRANT CONNECT ON DATABASE archive TO archive_app_read;
GRANT USAGE ON SCHEMA public TO archive_app_read;
GRANT SELECT ON TABLE loans, members, items, categories TO archive_app_read;

-- Read-write application role
GRANT archive_app_read TO archive_app_write;         -- inherits read permissions
GRANT INSERT, UPDATE ON TABLE loans TO archive_app_write;
GRANT INSERT ON TABLE members TO archive_app_write;
GRANT USAGE ON SEQUENCE loans_id_seq, members_id_seq TO archive_app_write;
-- No DELETE — application cannot delete; only admin can

-- Analytics role (read-only, no PII columns)
GRANT CONNECT ON DATABASE archive TO archive_analytics;
GRANT USAGE ON SCHEMA public, warehouse TO archive_analytics;
GRANT SELECT ON ALL TABLES IN SCHEMA warehouse TO archive_analytics;
-- Column-level: analysts see members but not PII
GRANT SELECT (id, full_name, membership_tier, city, country, join_date)
    ON TABLE members TO archive_analytics;

-- Create database users and assign roles
CREATE USER app_service WITH PASSWORD 'strong-password';
GRANT archive_app_write TO app_service;

CREATE USER analyst_jane WITH PASSWORD 'strong-password';
GRANT archive_analytics TO analyst_jane;
```

### Row-Level Security (RLS)

```sql
-- Enable RLS on the table
ALTER TABLE loans ENABLE ROW LEVEL SECURITY;

-- Policy: members can only see their own loans
-- Application sets the current member context via session variable
CREATE POLICY member_sees_own_loans ON loans
    FOR SELECT
    USING (member_id = current_setting('app.current_member_id', TRUE)::BIGINT);

-- Application code (Spring):
@Transactional
public List<Loan> getMemberLoans(Long memberId) {
    entityManager.createNativeQuery(
        "SET LOCAL app.current_member_id = :memberId")
        .setParameter("memberId", memberId)
        .executeUpdate();
    return loanRepository.findAllByMemberId(memberId);
}
-- With RLS: even if the WHERE clause is accidentally omitted,
-- only the current member's loans are returned

-- Staff bypass (librarians see all loans):
ALTER ROLE librarian_role BYPASSRLS;  -- staff bypass RLS
-- Or: CREATE POLICY staff_sees_all ON loans FOR SELECT USING (
--     pg_has_role(current_user, 'archive_staff', 'member')
-- );

-- Check RLS is active:
SELECT tablename, rowsecurity FROM pg_tables WHERE tablename = 'loans';
```

### Schema and Object Permissions Hierarchy

```sql
-- Permission hierarchy: DATABASE → SCHEMA → TABLE → COLUMN
-- A user needs permission at EVERY level in the chain

-- Wrong: grant table permission without schema USAGE
GRANT SELECT ON TABLE archive.loans TO analyst;
-- analyst still cannot access loans — needs USAGE on schema archive

-- Correct order:
GRANT CONNECT ON DATABASE archive TO analyst;           -- 1. database
GRANT USAGE ON SCHEMA archive TO analyst;              -- 2. schema
GRANT SELECT ON TABLE archive.loans TO analyst;        -- 3. table
-- Optional: restrict to columns:
REVOKE SELECT ON TABLE archive.members FROM analyst;   -- remove full row access
GRANT SELECT (id, full_name, tier) ON TABLE archive.members TO analyst;  -- column subset

-- Default privileges (for future tables):
ALTER DEFAULT PRIVILEGES IN SCHEMA warehouse
    GRANT SELECT ON TABLES TO archive_analytics;
-- Any table created in warehouse schema automatically grants SELECT to analytics
```

### Auditing Access

```sql
-- Check who has what permissions:
SELECT grantee, privilege_type, table_name
FROM information_schema.role_table_grants
WHERE table_schema = 'public'
ORDER BY grantee, table_name;

-- Check role memberships:
SELECT r.rolname AS role, m.rolname AS member
FROM pg_roles r
JOIN pg_auth_members am ON r.oid = am.roleid
JOIN pg_roles m ON am.member = m.oid
ORDER BY r.rolname;

-- pg_hba.conf: controls authentication methods per connection type
-- local   all  postgres  peer     ← local superuser: OS user match
-- host    all  app_service  0.0.0.0/0  scram-sha-256  ← app: password auth
-- host    all  all       127.0.0.1/32  reject         ← block all from localhost except above
```

## Common Mistakes

- **Application connecting as superuser**: the most common and highest-severity mistake. Create a dedicated application role with minimal permissions. The superuser account is for DBAs and migrations only, with credentials stored in a secrets manager, not in application config.
- **`GRANT ALL ON ALL TABLES`**: grants INSERT, UPDATE, DELETE, TRUNCATE to a role that may only need SELECT. Enumerate exactly which tables and which operations each role needs.
- **Shared credentials across services**: if the ETL service and the application share one database role, a compromised ETL credential gives application-level access (and vice versa). One role per service.
- **No RLS or application-level filtering**: relying solely on application code to filter data is one bug away from a data leak. RLS provides a database-enforced backstop.

## Mental Model

Database access control is like a building's security system. Authentication is the front door — you need a valid keycard to enter. Authorisation is the room access list — your keycard opens the offices assigned to your role, not every room. Least privilege: a junior employee's keycard doesn't open the server room or the executive suite. Row-level security is like a filing cabinet with personalised locks — even within a room everyone can enter, each person can only open the drawers containing their own files. The DBA is the building manager who can access everywhere, but only uses master access when necessary.

## Mini Summary

- ✔ Least privilege: each role has only the permissions needed for its function
- ✔ GRANT/REVOKE: assign and remove permissions at database, schema, table, and column level
- ✔ Roles: group permissions, assign roles to users — one role per functional purpose
- ✔ Never connect application as superuser — create a dedicated app role
- ✔ Row-level security: database-enforced row filtering, defence in depth against missing WHERE clauses
- ✔ Column-level grants: restrict PII columns to specific roles
- ✔ Schema USAGE + table SELECT both required for a role to read a table

# Guided Practice Quest

Work through the guided steps to identify the minimum permissions required for the Archive application (can check out and return loans, cannot delete members), create the role with those permissions, and enable RLS to prevent one member seeing another member's loans.

# Solo Practice Quest

Audit and secure the Archive database's access control. Tasks: (1) Identify all database roles currently needed by the Archive system: application (read/write), analytics (read-only, no PII), ETL process (read from operational, write to warehouse), DBA (full access) — define the exact permissions for each; (2) Write all GRANT statements needed to implement the four roles from scratch; (3) Enable RLS on the loans table and write two policies: one for members (own loans only), one for librarian staff (all loans); (4) The analytics team requests access to member email for a targeted campaign. Write the process for granting this access temporarily (column-level GRANT) and the process for revoking it when the campaign ends; (5) Audit the current role assignments using information_schema and pg_roles — write the queries; (6) Identify two scenarios where you would grant BYPASSRLS and explain the security implications.

# Integration

**Mathematics**: Access control matrices formalise the permission model. A permissions matrix A has rows = subjects (users/roles) and columns = objects (tables/schemas/databases). A[i][j] = {SELECT, INSERT, UPDATE} means subject i has those permissions on object j. This is equivalent to an access control list (ACL): each object has a list of (subject, permissions) pairs. The principle of least privilege minimises |A| — the number of non-empty cells. An empty cell means access denied (default-deny model). The complement of ACL is capability-based security: subjects carry tokens proving their permissions, rather than objects checking lists. PostgreSQL uses the ACL model: pg_class.relacl stores the ACL for each table as an array of aclitem values. Role inheritance adds an additional dimension: permissions are the union of all roles in a user's membership chain (transitive closure of the role graph).

**Sciences (Political Science — Separation of Powers)**: The principle of least privilege in database security mirrors the doctrine of separation of powers in democratic governance. Just as legislative, executive, and judicial branches are given distinct, limited powers to prevent any single entity from accumulating unchecked control, database roles divide access into distinct functions: application roles (execute operations), analytics roles (read-only observation), admin roles (schema management), ETL roles (data movement). The separation is both functional (each role does only what it is meant to do) and protective (a failure or compromise of one role cannot affect others). The analogy extends to audit requirements: just as democratic governance requires public records of who has what power and how it was used, database security requires audit logs of permission grants, revocations, and access events.

# Lore Conclusion

"The application now connects as archive_app_service," the Junior Engineer reported. "Read permissions on five tables. Write on loans and items. No DELETE, no DDL." The Senior Archivist reviewed the configuration. "And the analytics team?" The Junior pulled up the role list. "archive_analytics: SELECT on the warehouse schema. Column-level restrictions on members — no email, no phone. RLS enabled on loans: members see only their own." The Senior Archivist nodded. "The blast radius of any single credential is now bounded. A compromised application account cannot drop tables. An analytics breach exposes no PII." She set the configuration aside. "Access control is the first layer. Next: encryption — protecting data at rest and in transit, so that even if access controls fail, the data is unreadable."

---
