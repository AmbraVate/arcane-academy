---
id: de-jun-m8-04
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m8
moduleTitle: "Module 8: Database Testing"
moduleGlyph: "🧪"
moduleSortOrder: 8
topicSlug: migration_testing
topicTitle: "Migration Testing"
topicSortOrder: 4
lesson: migration_testing
title: "Migration Testing"
sortOrder: 4
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m8-03]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what Flyway/Liquibase does and why versioned migrations are needed
    - Describes how to test that a migration runs correctly without breaking existing data
    - Explains blue-green deployment and zero-downtime migration strategies
    - Identifies destructive migration risks and how to mitigate them
    - Describes the expand-contract pattern for backwards-compatible schema changes
  keywords: [Flyway, Liquibase, migration, versioned migration, schema evolution, backwards compatible, expand-contract, blue-green deployment, zero-downtime, destructive migration, rollback, V1__description, checksum, migration testing, additive change]
  modelAnswer: |
    Flyway/Liquibase manage versioned database migrations: each schema change is a numbered SQL script applied in order. Flyway tracks applied migrations in the flyway_schema_history table — it re-applies only new scripts. Testing migrations: apply the migration against a Testcontainers PostgreSQL instance with realistic data, then verify the post-migration state (expected rows, constraints, no data loss). Destructive migrations (DROP COLUMN, DROP TABLE) risk data loss; always back up first and verify the column is unused. Expand-contract pattern for zero-downtime: (1) Expand: add new column (nullable), keep old column. (2) Migrate: backfill new column, update application to write both. (3) Contract: remove old column once application is fully deployed. Blue-green deployment: run two identical environments; deploy new code to green, migrate green's database, switch traffic — allows instant rollback to blue if green fails.
guidedSteps:
  - id: de-jun-m8-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A migration renames column 'member_name' to 'full_name'. The deployment process is: (1) Deploy new application code (uses 'full_name'), (2) Run migration (renames column). What breaks between steps 1 and 2?
    inputConfig:
      options:
        - "Nothing — the application ignores columns it doesn't recognise"
        - "The new application code fails because it queries 'full_name' which doesn't exist yet — the migration hasn't run"
        - "The migration cannot run while the application is using the table"
        - "The old application code continues working because column renames are backwards-compatible"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The new application code fails because it queries 'full_name' which doesn't exist yet — the migration hasn't run"]
      rejectedFeedback: "This is a deployment ordering problem. The new application queries 'full_name' but the migration to rename the column hasn't run yet — the column is still 'member_name'. Result: ColumnNotFoundException, queries fail, the application is broken. The wrong order: deploy code first → code runs against old schema → failure. Correct order: run migration first → then deploy code. But this creates the same problem in reverse: old application code runs against a schema that has 'full_name' (renamed from 'member_name') — old code queries 'member_name' which no longer exists. The solution: expand-contract pattern. Expand: add 'full_name' column (keep 'member_name'). Both columns exist. Deploy application that writes both and reads from 'full_name'. Contract: drop 'member_name' after the application is fully deployed. At no point is the application reading a column that doesn't exist."
    hint: "Deploy code first, then migrate — what does the new code try to query before the migration runs?"
    reflectionPrompt: "How does the expand-contract pattern ensure both old and new application versions can run simultaneously against the same database schema?"
  - id: de-jun-m8-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Flyway applies migrations in order and tracks them in a table called flyway_______history. Once a migration is applied, Flyway verifies its ________ before running it again — changed scripts are rejected.
    inputConfig:
      placeholder: "schema / checksum"
    markingRule:
      matchMode: CONTAINS
      accepted: [checksum, "flyway_schema_history", schema_history, "schema history", checksum, checksums, hash]
      rejectedFeedback: "Flyway's flyway_schema_history table records every applied migration: version, description, script filename, checksum (hash of the script), applied_on timestamp, and success flag. When Flyway starts, it compares each applied migration's stored checksum with the checksum of the current script file on disk. If they differ (someone edited an already-applied migration), Flyway refuses to start with: ERROR: Found non-empty schema(s) with failed migration. This prevents accidental modification of applied migrations — a critical safety feature. Rule: never modify a Flyway migration script after it has been applied to any environment. To fix a mistake: write a new migration script (e.g. V3__fix_the_mistake.sql). The immutability of applied migrations is what makes Flyway's version tracking reliable."
    hint: "Flyway uses a mathematical fingerprint of each migration file to detect if it's been changed after being applied."
    reflectionPrompt: "What should you do if you accidentally edited and committed a Flyway migration that has already been applied to a shared development database?"
  - id: de-jun-m8-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the expand-contract pattern for adding a NOT NULL column to a large production table with zero downtime.
    inputConfig:
      minWords: 35
    markingRule:
      matchMode: CONTAINS
      accepted: [expand, contract, nullable, backfill, NOT NULL, default, application, deploy, both, old, new, drop, column, additive, backwards]
      rejectedFeedback: "Adding a NOT NULL column to a large table directly: ALTER TABLE loans ADD COLUMN new_col TEXT NOT NULL requires either a DEFAULT value (applied to all existing rows — table lock during the update) or fails because existing rows would have NULL. For zero-downtime expand-contract: (1) Expand migration: ADD COLUMN new_col TEXT NULL (nullable — existing rows get NULL, no lock). (2) Application update: deploy new code that writes new_col on INSERT/UPDATE. Old code still works (column is nullable). (3) Backfill migration: UPDATE loans SET new_col = derive_value(old_col) WHERE new_col IS NULL — run in batches to avoid long locks. (4) Validate migration: verify no NULLs remain. (5) Contract migration: ALTER TABLE loans ALTER COLUMN new_col SET NOT NULL — fast because no NULLs exist. (6) Drop old column in a subsequent migration once all code is updated. Each step is safe to run while the application is live."
    hint: "Break the single risky operation into multiple safe additive steps — expand the schema first, then contract it."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Testing a database migration using Testcontainers should verify:"
    options:
      - "Only that the migration SQL runs without syntax errors"
      - "That the migration applies without errors, existing data survives correctly, new constraints are enforced, and the application code works against the migrated schema"
      - "That the migration can be rolled back to the previous version"
      - "Only that the Flyway version number increments correctly"
    correctIndex: 1
    feedback: "Migration testing must verify multiple properties: (1) Migration applies without errors (Flyway runs the script successfully). (2) Existing data survives: if you rename a column, existing data is still accessible by the new name. If you add a NOT NULL constraint, existing rows have valid values for that column after the migration. (3) New constraints are enforced: after adding a CHECK constraint, violating rows should be rejected. (4) Application code compatibility: after the migration, run the full integration test suite against the migrated database schema — this catches any repository queries that break on the new schema. (5) Rollback (if applicable): some teams require rollback scripts — verify the down migration restores the previous schema state. Testcontainers makes this easy: start a clean container, apply migrations via Flyway, insert test data, run assertions."
  - type: MULTIPLE_CHOICE
    question: "In a Flyway migration file named 'V5__add_fine_table.sql', what does V5 mean?"
    options:
      - "The migration requires PostgreSQL version 5"
      - "The migration is the 5th version and will be applied after V4 and before V6"
      - "The migration affects version 5 of the application"
      - "The migration has 5 SQL statements"
    correctIndex: 1
    feedback: "Flyway migration naming convention: V{version}__{description}.sql. The version (V5) determines execution order — Flyway applies migrations in ascending version order: V1, V2, V3, V4, V5. Gaps are allowed (you can have V1, V3, V5 — Flyway applies them in that order, skipping nothing). Once V5 is applied, V4 can never run again. The double underscore (__) separates the version from the human-readable description. Checksum: calculated for each applied migration — editing V5__add_fine_table.sql after it's been applied breaks the checksum and Flyway refuses to proceed. Alternative: Flyway supports timestamp-based versioning (V20240315143022__description.sql) for teams where version numbers collide in parallel branches. Liquibase uses changeSets with author + id, providing better merge conflict resolution for parallel work."
retrieval:
  recall: "Describe the Flyway migration lifecycle for a new project from initialization through five migrations. What does flyway_schema_history contain after each step?"
  explain: "Explain the expand-contract migration pattern for renaming a column. List all the migration steps, what application deployment happens between them, and why each step is safe to run while the application is live."
  mistakeId:
    code: |
      -- Migration V7__rename_member_name.sql (already applied to production)
      -- Developer edits this file to fix a typo in the comment:

      -- V7: Rename member_name to full_name
      ALTER TABLE members RENAME COLUMN member_name TO full_name;
      -- Add index for name search
      CREATE INDEX idx_members_full_name ON members (full_name);
    answer: "Editing an already-applied Flyway migration (V7) is a critical mistake. Flyway stored the checksum of the original V7 script. When the application next starts (in any environment where V7 was already applied), Flyway calculates the new checksum of the modified file — it differs from the stored checksum. Flyway throws: ERROR: Validate failed: Migration checksum mismatch for migration version 7. The application refuses to start. Impact: if this reaches a shared development or staging environment before being caught, all developers are blocked. If it reaches production (less likely but possible), the production application cannot start after a restart. Fix: revert V7 to its exact original content. Create a new migration V8__add_full_name_index.sql with ONLY the new content (the index creation). Never edit applied migrations — write a new one. The fix to apply to a development database where V7 was already applied with the old content: either repair the checksum (flyway repair) after verifying the schema state, or reset the development database entirely."
---

# Hook

Every schema change is a deployment risk. A forgotten column rename breaks the application the moment the migration runs. A NOT NULL constraint added to a populated table locks the table for minutes. The wrong deployment order leaves the application querying columns that don't exist yet. Migration testing is the discipline that converts "deployment day is scary" into "deployment day is boring."

# Lore Introduction

"Production deployment failed at 11pm," the Senior Archivist said, reviewing the incident report. "The migration ran — it added a NOT NULL constraint to a column that had NULL values in existing rows. The constraint was rejected. The application was down for forty minutes." The Junior Engineer looked at the migration. "We didn't test it against realistic data. In the test database the table was empty — the constraint applied without issue." The Senior Archivist set the report down. "Test environment had empty tables. Production had two hundred thousand rows, some with NULL in that column. The migration was never tested against realistic populated data." She opened the migration tool configuration. "Migration testing: every schema change applied against a realistic data volume in a Testcontainers environment, with assertions on both the migration result and the data survival. Empty-table tests provide false confidence."

# Core Learning

## Concept Introduction

### Flyway Migration Management

```sql
-- Migration file naming: V{version}__{description}.sql
-- V1__create_initial_schema.sql
-- V2__add_member_tier_column.sql
-- V3__add_loan_constraints.sql

-- Example: V3__add_loan_constraints.sql
ALTER TABLE loans
    ALTER COLUMN member_id SET NOT NULL,
    ALTER COLUMN item_id SET NOT NULL,
    ALTER COLUMN loan_date SET NOT NULL;

ALTER TABLE loans
    ADD CONSTRAINT chk_due_after_loan CHECK (due_date > loan_date),
    ADD CONSTRAINT chk_status CHECK (status IN ('ACTIVE', 'RETURNED', 'OVERDUE', 'LOST'));

CREATE INDEX idx_loans_member_id ON loans (member_id);
CREATE INDEX idx_loans_due_date ON loans (due_date) WHERE status = 'ACTIVE';  -- partial

-- flyway_schema_history after V1, V2, V3:
-- installed_rank | version | description               | checksum   | success
-- 1              | 1       | create initial schema     | 1042833749 | true
-- 2              | 2       | add member tier column    | 983774621  | true
-- 3              | 3       | add loan constraints      | 1237890123 | true
```

### Spring Boot Flyway Configuration

```yaml
 # application.yml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: false
    validate-on-migrate: true    # fail if checksum mismatch
    out-of-order: false          # fail if V5 applied when V3 is missing
  jpa:
    hibernate:
      ddl-auto: validate         # validate schema against entities, don't modify
```

### Migration Testing with Testcontainers

```java
@SpringBootTest
@Testcontainers
class MigrationIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void allMigrationsApply_withoutErrors() {
        // Spring Boot auto-applies Flyway migrations on startup
        // If startup succeeds, all migrations applied without error
        // Verify the final schema:
        assertThat(tableExists("loans")).isTrue();
        assertThat(tableExists("members")).isTrue();
        assertThat(columnExists("loans", "fine_amount")).isTrue();
    }

    @Test
    void migration_preservesExistingData() {
        // For migrations that transform data (not just add structure):
        // 1. Insert data in the pre-migration state (via a test that stops before the migration)
        // 2. Apply the migration
        // 3. Verify data survival
        // OR: use Flyway's test-only baseline approach

        // Check counts (basic data survival):
        int loanCount = jdbc.queryForObject("SELECT COUNT(*) FROM loans", Integer.class);
        assertThat(loanCount).isGreaterThanOrEqualTo(0);  // table exists and is queryable
    }

    @Test
    void checkConstraint_rejectsInvalidStatus() {
        // Verify that constraints added by migration are enforced:
        assertThatThrownBy(() ->
            jdbc.execute("INSERT INTO loans (member_id, item_id, loan_date, due_date, status) " +
                         "VALUES (1, 1, '2024-01-01', '2024-02-01', 'INVALID_STATUS')")
        ).isInstanceOf(DataAccessException.class);
    }

    private boolean tableExists(String tableName) {
        return jdbc.queryForObject(
            "SELECT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = ?)",
            Boolean.class, tableName);
    }

    private boolean columnExists(String table, String column) {
        return jdbc.queryForObject(
            "SELECT EXISTS(SELECT 1 FROM information_schema.columns " +
            "WHERE table_name = ? AND column_name = ?)",
            Boolean.class, table, column);
    }
}
```

### The Expand-Contract Pattern

```sql
-- SCENARIO: Rename 'member_name' to 'full_name' with zero downtime

-- Step 1: EXPAND — add new column (backward-compatible — old app still works)
-- V8__expand_add_full_name.sql
ALTER TABLE members ADD COLUMN full_name VARCHAR(200);
UPDATE members SET full_name = member_name;  -- backfill existing data
-- OLD application still uses 'member_name' (it exists). NEW application can use 'full_name'.

-- Step 2: Deploy application that writes BOTH columns
--  (interim version that supports both old and new schema)
-- member.setMemberName(name);  // old code
-- member.setFullName(name);    // new code — both columns updated

-- Step 3: CONTRACT — remove old column (once all app instances use new column)
-- V9__contract_drop_member_name.sql
ALTER TABLE members DROP COLUMN member_name;
-- OLD application (still deployed on a canary) will fail here — coordinate deployment

-- Timeline:
-- t0: V8 migration → both columns exist
-- t1: Deploy new app → writes both columns
-- t2: Verify new app is fully deployed
-- t3: V9 migration → drop old column
-- t4: Remove backward-compat code from app (next release)
```

### Destructive Migration Safety Checks

```sql
-- Before DROP COLUMN or DROP TABLE:
-- 1. Verify column/table is unused
SELECT COUNT(*) FROM loans WHERE status_old IS NOT NULL;  -- if 0, safe to drop

-- 2. Verify no application queries reference it (via query logs)
SELECT query FROM pg_stat_statements
WHERE query LIKE '%member_name%'
ORDER BY calls DESC;  -- should be 0 after app deployment

-- 3. Always backup before destructive migration
-- pg_dump -t members archive > members_backup_before_v9.sql

-- 4. Stage destructive migrations separately from additive ones
-- V8a__add_full_name.sql (additive, safe)
-- V8b__backfill_full_name.sql (data migration, safe)
-- V8c__add_not_null.sql (constraint, safe after backfill)
-- V9__drop_member_name.sql (destructive — deploy separately after verification)
```

## Why It Matters

Schema migrations are the riskiest code most teams deploy — they mutate production state, often irreversibly, while the app is live:

- An untested migration that locks a large table for minutes is a self-inflicted outage
- Testing migrations against realistic data volumes catches problems staging-sized data never shows
- The rollback plan matters as much as the migration: some changes (dropped columns, lossy type changes) cannot be undone without a tested backup path

Application bugs can be rolled back; a botched migration can't always be. That asymmetry is why migration testing deserves its own discipline.

## Common Mistakes

- **Testing migrations against empty tables**: an empty-table test passes constraints that fail on populated tables. Always test migrations with realistic data — use data seeds or a masked production snapshot.
- **Editing applied migrations**: changing any file that Flyway has already applied breaks its checksum. Write new migrations to fix mistakes. Never edit applied ones.
- **Combining additive and destructive changes in one migration**: additive changes (ADD COLUMN) are safe. Destructive changes (DROP COLUMN) are irreversible. Keep them in separate migration files with separate deployment steps.
- **Not testing rollback**: for critical migrations, have a tested rollback script ready before deploying. It is too late to write the rollback after the migration has already failed in production.

## Mental Model

Database migrations are like version control for your database schema — but with one crucial difference: you cannot "git revert" a schema change that has already modified data. Every migration is a one-way door. The expand-contract pattern builds a two-door airlock: enter the first door (additive change), both doors coexist, exit the second door (remove old). Flyway is the lock on the airlock: it ensures everyone moves through the doors in the same order, and that no one propped the first door open (edited an applied migration) without everyone knowing.

## Mini Summary

- ✔ Flyway: versioned SQL migrations applied in order, tracked by checksum in flyway_schema_history
- ✔ Never edit applied migrations — write new ones
- ✔ Test migrations against realistic data volume (Testcontainers + seed data)
- ✔ Expand-contract: additive change → application update → destructive change (zero-downtime)
- ✔ Destructive migrations (DROP COLUMN/TABLE) require: verify unused, backup, separate deployment
- ✔ `spring.jpa.hibernate.ddl-auto: validate` in production — never `create` or `update`
- ✔ Migration tests verify: applies without error, data survives, constraints enforced, app compatible

# Guided Practice Quest

Work through the guided steps to write a Flyway migration test that applies V3 against a Testcontainers PostgreSQL with 1,000 pre-seeded rows, verify the migration completes without errors, then verify a constraint violation test works against the migrated schema.

# Solo Practice Quest

Design and implement a complete migration testing strategy for the Archive system. Tasks: (1) Write a migration integration test that verifies all current Flyway migrations apply in order against a fresh Testcontainers PostgreSQL instance; (2) Implement the expand-contract migration for adding a new 'preferred_name' field to members — write all three migration files (expand, backfill, contract) and the test for each step; (3) Write a destructive migration test for dropping an obsolete column — include: pre-migration data verification, post-migration data survival check, and constraint/index verification; (4) A migration must add a NOT NULL constraint to 'status' in the loans table — but some legacy rows have NULL status. Write the migration that handles this: backfill NULLs, verify no NULLs remain, then add the constraint — all in separate safe steps; (5) Implement a CI/CD pipeline check that runs migration tests on every PR that contains a new Flyway migration file; (6) Write the incident response procedure for a failed migration in production: how to assess impact, options for recovery (revert app, fix-forward, restore backup), and how to prevent recurrence.

# Integration

**Mathematics**: Database migration versioning is an instance of state machine theory. The database schema is a state; each migration is a state transition function. The migration graph is a directed acyclic graph (DAG): nodes are schema versions, edges are migration scripts. Flyway enforces a total ordering on this DAG (linear version numbers), ensuring all environments traverse the same path from the initial state to the current state. A rollback migration (the inverse of a migration) is the inverse state transition. Not all migrations are invertible: a DROP COLUMN migration has no inverse that can recover the original data (only the structure). This formalises why rollbacks are hard: they can restore structure but not data. The expand-contract pattern avoids this by keeping both versions of the schema in a superposition state — both the old and new column exist simultaneously, making the transition reversible at any point until the contract step removes the old column.

**Sciences (Engineering — Configuration Management)**: Database migration management implements configuration management principles standardised in IEEE 828 (Configuration Management Plans). Configuration management: systematic control of changes to a system, ensuring traceability, consistency, and auditability. Flyway satisfies these requirements: each migration has a unique identifier (version number), a description, a change record (applied_on, applied_by), and an integrity check (checksum). This mirrors the change control process in safety-critical engineering: no change is applied without a unique change reference, a description of the change, and a record of who applied it and when. The prohibition on editing applied migrations mirrors the engineering change control rule that approved documents cannot be retroactively altered — only superseded by new, separately controlled documents. This is why aviation maintenance logs, engineering drawing revisions, and Flyway migrations all share the same fundamental property: immutability of applied records.

# Lore Conclusion

"Module 8 complete," the Senior Archivist said, reviewing the test suite results. "Data validation with assertions and constraints. Integration tests against a real PostgreSQL database. Minimal, isolated test data with builders. And migration tests that catch the NOT NULL on populated table problem before it reaches production." The Junior Engineer looked at the complete test suite report. "All green. Including the migration tests." The Senior Archivist set the report down. "You have completed the Junior tier of Data Engineering. Eight modules: Advanced SQL, Database Programming, Transactions, Indexing and Performance, Application Data Access, Data Warehousing Foundations, Data Security, and Database Testing." The Junior looked at the path ahead. "What comes next?" The Senior Archivist looked at the horizon. "At the Senior tier, you stop building individual features and start designing systems. Distributed databases. Data pipelines. Analytics engineering. Governance. The Archive becomes one node in a larger network — and the problems become correspondingly harder."

---
