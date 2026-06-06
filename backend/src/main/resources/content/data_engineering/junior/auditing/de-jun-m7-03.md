---
id: de-jun-m7-03
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m7
moduleTitle: "Module 7: Data Security"
moduleGlyph: "🔐"
moduleSortOrder: 7
topicSlug: auditing
topicTitle: "Auditing"
topicSortOrder: 3
lesson: auditing
title: "Auditing"
sortOrder: 3
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m7-02]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what an audit log records and why it is needed for security and compliance
    - Describes the trigger-based audit table pattern
    - Distinguishes audit logging from application logging
    - Identifies the properties of a tamper-evident audit trail
    - Explains why audit logs must be stored separately from the database being audited
  keywords: [audit log, audit table, trigger, tamper-evident, immutable, compliance, GDPR, who, what, when, before, after, application log, separate storage, retention, forensic, pg_audit, change data capture]
  modelAnswer: |
    An audit log records who did what to which data and when — essential for security investigations, compliance (GDPR, HIPAA, SOX), and forensic analysis. Trigger-based audit pattern: AFTER INSERT/UPDATE/DELETE trigger on sensitive tables inserts a row into an audit table containing: table_name, operation, old_values (JSON), new_values (JSON), changed_by (database user), changed_at (timestamp). Audit logs are separate from application logs (application logs record events in the application; audit logs record changes to data). Tamper-evident: once written, audit rows should be unmodifiable — achieved by denying UPDATE/DELETE on the audit table to all roles, using append-only storage, or sending to an external log aggregator. Audit logs stored in the same database can be deleted by a database admin — use separate storage (S3, log aggregator, SIEM) for compliance-grade audit trails.
guidedSteps:
  - id: de-jun-m7-03-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A member's account is deleted from the database. A week later, a compliance audit requires knowing: who deleted it, when, and what the member's data was before deletion. Which audit approach captures this?
    inputConfig:
      options:
        - "Application logs — the Spring application logs all service method calls"
        - "An audit table populated by an AFTER DELETE trigger on the members table — stores old_values, deleted_by, deleted_at"
        - "PostgreSQL WAL (Write-Ahead Log) — all changes are logged automatically"
        - "Database backups — restore the previous night's backup to find the deleted record"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["An audit table populated by an AFTER DELETE trigger on the members table — stores old_values, deleted_by, deleted_at"]
      rejectedFeedback: "An audit table with a trigger is the correct pattern for compliance-grade audit trails. (A) Application logs record method calls and business events but rarely capture the full before/after data state. If the deletion happened via a direct SQL query (not through the application), application logs miss it entirely. (B) The trigger captures the deletion regardless of how it happened — direct SQL, ORM, or application call. (C) PostgreSQL WAL records changes but is not easily queryable for historical data and is regularly overwritten. (D) Restoring a backup is an emergency measure, not an audit log — you lose all changes between the backup and the deletion, and it doesn't tell you WHO deleted it. The trigger-based audit table provides: queryable history, before-state preservation, actor identification, and timestamp — all required for a compliance audit."
    hint: "Which approach captures the state of the data BEFORE the deletion, plus who performed it?"
    reflectionPrompt: "What information would you include in the audit table row for a DELETE operation?"
  - id: de-jun-m7-03-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To make an audit table tamper-evident, all roles (including DBAs) should be denied ________ and DELETE permissions on the audit table, so that once a row is inserted, it cannot be changed.
    inputConfig:
      placeholder: "UPDATE"
    markingRule:
      matchMode: CONTAINS
      accepted: [UPDATE, update, "UPDATE and DELETE", "UPDATE/DELETE", modify, modification]
      rejectedFeedback: "Tamper-evidence means: once an audit record is written, it cannot be modified or deleted. Implementation: REVOKE UPDATE, DELETE ON audit_log FROM ALL; — even the DBA role cannot modify audit rows. The audit log INSERT-only permission can be granted to the trigger function's SECURITY DEFINER context while denying direct table modification. Additional measures: (1) Move old audit records to external storage (S3, immutable log service) where no database credential can reach. (2) Use write-once object storage with retention locks (AWS S3 Object Lock, Azure Immutable Blob Storage). (3) SIEM (Security Information and Event Management) systems receive log events and store them externally. (4) Hash chaining: each audit row includes a hash of the previous row — tampering with any row breaks the chain and is detectable. Compliance standards (PCI-DSS, HIPAA) typically require that audit logs be stored in a separate, access-controlled location."
    hint: "To make audit records unchangeable, remove the ability to modify them — what two DML operations could change an existing row?"
    reflectionPrompt: "If audit records are stored only in the same database, what can a malicious DBA do to cover their tracks?"
  - id: de-jun-m7-03-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the difference between database audit logs and application logs. What does each capture that the other misses?
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [application, direct, SQL, bypass, ORM, trigger, data, change, before, after, business, event, context, user, session, request]
      rejectedFeedback: "Database audit logs (trigger/pg_audit): capture every data change at the database level — regardless of how the change was made (ORM, direct SQL, psql command, migration script). Records: table, operation, old_values, new_values, database user, timestamp. Does NOT capture: application business context (which API endpoint, which user session, which feature flow triggered the change). Application logs: capture business events — 'Member 42 logged in from IP 1.2.3.4', 'User updated profile via PUT /api/members/42'. Records: application username, HTTP context, business operation name, request metadata. Does NOT capture: the actual data values before/after change (unless explicitly logged), changes made directly in the database bypassing the application. Complete audit trail: use both. Database audit log = data-level forensics. Application log = business context. Correlate via timestamp + database session ID."
    hint: "Think about what bypasses the application entirely — like a DBA running SQL directly."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A trigger-based audit log records CURRENT_USER as the actor. What is the limitation of this in an application that uses connection pooling?"
    options:
      - "Connection pooling prevents triggers from firing"
      - "All queries in the pool use the same database role — CURRENT_USER is always the application service account, not the individual end user who made the change"
      - "Connection pooling makes CURRENT_USER return NULL"
      - "Triggers cannot access CURRENT_USER when called from a pooled connection"
    correctIndex: 1
    feedback: "Connection pools (HikariCP, PgBouncer) reuse a fixed number of connections, all authenticated as the same database user (e.g. archive_app_service). CURRENT_USER in a trigger always returns 'archive_app_service' — it cannot distinguish which of your 10,000 application users made the change. Solution: set a session-level variable before sensitive operations: SET LOCAL app.current_user_id = :userId. The trigger reads: current_setting('app.current_user_id', TRUE)::BIGINT to get the application user ID. This pattern: (1) Set session variable at the start of the transaction (service layer). (2) Trigger reads it and stores it in the audit row. (3) application_user_id = 42 (Alice) is now in the audit log, alongside db_user = 'archive_app_service'. Both pieces of information are preserved."
  - type: MULTIPLE_CHOICE
    question: "pg_audit (PostgreSQL Audit Extension) compared to trigger-based audit tables:"
    options:
      - "pg_audit audits only DDL changes; trigger-based audits DML changes"
      - "pg_audit logs to files/system log at the database level — captures all queries including SELECT; triggers capture only DML data changes to specific tables"
      - "pg_audit is a newer replacement for triggers and should always be used instead"
      - "pg_audit stores audit data in the same table as the audited data"
    correctIndex: 1
    feedback: "pg_audit is a PostgreSQL extension that logs all SQL statements to the PostgreSQL server log (and optionally to syslog/CSV). It can log: all DDL (CREATE, ALTER, DROP), all DML (INSERT, UPDATE, DELETE, TRUNCATE), and even SELECT queries (data access audit). Trigger-based audit tables: capture only DML changes (INSERT/UPDATE/DELETE) on specific tables, store before/after data in the database, queryable via SQL. The approaches are complementary: pg_audit for comprehensive query-level logging and compliance reporting (e.g. 'log all SELECT on members table'); trigger-based for structured before/after data capture in queryable format. pg_audit logs are typically shipped to a SIEM. Trigger audit tables live in the database and can be queried by analysts. Use both for comprehensive coverage."
retrieval:
  recall: "Write the SQL to create an audit table for the members table, the AFTER INSERT/UPDATE/DELETE trigger function, and the trigger definition. Include: operation, old_values, new_values, db_user, app_user_id, changed_at."
  explain: "Explain three properties that a compliance-grade audit trail must have (immutability, completeness, queryability) and describe one technical implementation choice for each property."
  mistakeId:
    code: |
      CREATE TABLE member_audit (
          id           BIGSERIAL PRIMARY KEY,
          member_id    BIGINT,
          operation    VARCHAR(10),
          changed_at   TIMESTAMP DEFAULT NOW(),
          changed_by   VARCHAR(100)
          -- No old_values, no new_values columns
      );
      
      -- Trigger:
      CREATE OR REPLACE FUNCTION audit_member_changes() RETURNS TRIGGER AS $$
      BEGIN
          INSERT INTO member_audit (member_id, operation, changed_by)
          VALUES (NEW.id, TG_OP, CURRENT_USER);
          RETURN NEW;
      END;
      $$ LANGUAGE plpgsql;
    answer: "Three problems with this audit implementation: (1) No old_values or new_values: the audit records THAT a change occurred but not WHAT changed. For compliance ('what was the member's email before it was changed?') or forensics ('what data was in this record before it was deleted?'), you need the actual before/after values. Fix: add old_values JSONB, new_values JSONB columns, populated with row_to_json(OLD) and row_to_json(NEW). (2) Uses NEW.id — for DELETE operations, NEW is NULL in PostgreSQL (OLD contains the deleted row). The trigger will fail on DELETE. Fix: use COALESCE(NEW.id, OLD.id) or handle TG_OP = 'DELETE' separately using OLD.id. (3) No tamper protection — anyone with UPDATE/DELETE on member_audit can modify or delete audit records. Fix: REVOKE UPDATE, DELETE ON member_audit FROM PUBLIC; ensure only the trigger's SECURITY DEFINER function can INSERT."
---

# Hook

Access control and encryption prevent unauthorised access. Auditing answers the question that comes after: when something does happen — a data breach, a compliance violation, an insider threat — who did it, what did they change, and when? An audit trail is the forensic record that turns a security incident into an investigation.

# Lore Introduction

"A librarian's account was used to delete 200 member records last Tuesday," the Senior Archivist said, reading an alert. "The librarian says they did not do it. Their credentials may have been compromised." The Junior Engineer pulled up the database. "The records are gone. How do we investigate?" The Senior Archivist looked at the audit log table. "We cannot. We have no audit log. We know the records are gone and the approximate time — nothing more. We do not know which specific records, what their data was, or which connection made the change." She closed the query. "This is the lesson. An incident without an audit trail cannot be investigated, cannot be reported to the regulator, and cannot be learned from. We build the audit trail before we need it — not after."

# Core Learning

## Concept Introduction

### The Audit Table Pattern

```sql
-- Generic audit table for any table
CREATE TABLE audit_log (
    id              BIGSERIAL PRIMARY KEY,
    table_name      VARCHAR(100)  NOT NULL,
    operation       VARCHAR(10)   NOT NULL,  -- 'INSERT', 'UPDATE', 'DELETE'
    record_id       BIGINT,                   -- PK of the affected row
    old_values      JSONB,                    -- row state BEFORE change (NULL for INSERT)
    new_values      JSONB,                    -- row state AFTER change (NULL for DELETE)
    db_user         VARCHAR(100)  NOT NULL,   -- database role (CURRENT_USER)
    app_user_id     BIGINT,                   -- application user from session variable
    changed_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    client_addr     INET,                     -- inet_client_addr()
    application     VARCHAR(100)              -- application name
);

-- Tamper protection: no role can modify audit records
REVOKE UPDATE, DELETE ON audit_log FROM PUBLIC;
REVOKE TRUNCATE ON audit_log FROM PUBLIC;
-- Even DBA: create a separate audit_admin role for emergencies, document each use
```

### Trigger-Based Audit Logging

```sql
-- Generic audit trigger function (works on any table)
CREATE OR REPLACE FUNCTION log_changes() RETURNS TRIGGER AS $$
DECLARE
    v_old_data JSONB;
    v_new_data JSONB;
    v_record_id BIGINT;
    v_app_user_id BIGINT;
BEGIN
    -- Safely get application user from session variable (NULL if not set)
    BEGIN
        v_app_user_id := current_setting('app.current_user_id', TRUE)::BIGINT;
    EXCEPTION WHEN OTHERS THEN
        v_app_user_id := NULL;
    END;

    IF TG_OP = 'INSERT' THEN
        v_old_data := NULL;
        v_new_data := to_jsonb(NEW);
        v_record_id := NEW.id;
    ELSIF TG_OP = 'UPDATE' THEN
        v_old_data := to_jsonb(OLD);
        v_new_data := to_jsonb(NEW);
        v_record_id := NEW.id;
    ELSIF TG_OP = 'DELETE' THEN
        v_old_data := to_jsonb(OLD);
        v_new_data := NULL;
        v_record_id := OLD.id;
    END IF;

    INSERT INTO audit_log (table_name, operation, record_id,
                            old_values, new_values, db_user,
                            app_user_id, changed_at, client_addr, application)
    VALUES (TG_TABLE_NAME, TG_OP, v_record_id,
            v_old_data, v_new_data, CURRENT_USER,
            v_app_user_id, NOW(), inet_client_addr(), current_setting('application_name', TRUE));

    RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;  -- runs with trigger owner's privileges

-- Attach trigger to sensitive tables
CREATE TRIGGER audit_members
    AFTER INSERT OR UPDATE OR DELETE ON members
    FOR EACH ROW EXECUTE FUNCTION log_changes();

CREATE TRIGGER audit_loans
    AFTER INSERT OR UPDATE OR DELETE ON loans
    FOR EACH ROW EXECUTE FUNCTION log_changes();
```

### Application-Side Audit Context

```java
// Set the application user context before sensitive operations
// so the database trigger can record the actual user
@Service
public class AuditContextService {

    @PersistenceContext
    private EntityManager em;

    public void setAuditContext(Long userId) {
        em.createNativeQuery("SET LOCAL app.current_user_id = :userId")
          .setParameter("userId", userId)
          .executeUpdate();
    }
}

@Service
public class MemberService {

    @Transactional
    public void deleteMember(Long memberId, Long adminUserId) {
        auditContextService.setAuditContext(adminUserId);  // set before operation
        memberRepository.deleteById(memberId);
        // Trigger fires: audit_log records DELETE with app_user_id = adminUserId
    }
}
```

### Querying the Audit Trail

```sql
-- Who deleted member records in the last 7 days?
SELECT changed_at, db_user, app_user_id, record_id,
       old_values->>'full_name' AS deleted_name,
       old_values->>'email' AS deleted_email
FROM audit_log
WHERE table_name = 'members'
  AND operation = 'DELETE'
  AND changed_at >= NOW() - INTERVAL '7 days'
ORDER BY changed_at DESC;

-- What changed in a specific member record?
SELECT changed_at, operation,
       old_values - 'password_hash' AS before_change,  -- exclude sensitive
       new_values - 'password_hash' AS after_change
FROM audit_log
WHERE table_name = 'members' AND record_id = 42
ORDER BY changed_at;

-- Count changes by user (anomaly detection: who is most active?):
SELECT app_user_id, operation, COUNT(*) AS change_count
FROM audit_log
WHERE table_name = 'members'
  AND changed_at >= NOW() - INTERVAL '24 hours'
GROUP BY app_user_id, operation
ORDER BY change_count DESC;

-- Detect after-hours changes (possible insider threat indicator):
SELECT *
FROM audit_log
WHERE table_name = 'members'
  AND EXTRACT(HOUR FROM changed_at AT TIME ZONE 'UTC') NOT BETWEEN 7 AND 20
  AND operation IN ('UPDATE', 'DELETE')
ORDER BY changed_at DESC;
```

### pg_audit and External Log Shipping

```sql
-- Enable pg_audit for comprehensive query logging (postgresql.conf):
-- shared_preload_libraries = 'pgaudit'
-- pgaudit.log = 'ddl,write,role'   -- log DDL, DML writes, role changes
-- pgaudit.log_relation = on         -- log table name per statement

-- PostgreSQL server log entry (pg_audit format):
-- AUDIT: SESSION,1,1,DDL,DROP TABLE,,,DROP TABLE members,<not logged>
-- AUDIT: SESSION,2,1,WRITE,DELETE,public,members,"DELETE FROM members WHERE id=42",<not logged>

-- Log shipping to external SIEM (e.g., CloudWatch, Elasticsearch):
-- postgresql.conf:
-- log_destination = 'csvlog'
-- logging_collector = on
-- log_directory = '/var/log/postgresql'
-- log_filename = 'postgresql-%Y-%m-%d.csv'
-- Then: filebeat/fluentd agent ships CSV logs to SIEM for retention and alerting
```

## Common Mistakes

- **Audit log in the same database**: a malicious DBA can truncate or delete audit records that incriminate them. Ship audit logs to external, append-only storage (S3 with Object Lock, a SIEM, CloudWatch Logs).
- **Not recording old_values for UPDATE**: knowing that a field changed is useless without knowing what it changed from. Always store both `old_values` and `new_values` for UPDATE operations.
- **Recording passwords in audit logs**: `to_jsonb(NEW)` includes all columns — including `password_hash`. Exclude sensitive columns: `to_jsonb(NEW) - 'password_hash' - 'email_encrypted'`.
- **No application user ID**: `CURRENT_USER` is always the database service account in pooled connections. Without the session-variable pattern, the audit log records 'archive_app_service' for every change — useless for identifying the actual user.

## Mental Model

An audit log is the security camera footage of your database. Access control is the locked door; encryption is the locked safe inside — but if someone gets past those, you need to know what happened. The camera (audit log) runs continuously and records everything: who entered, what they touched, when they left. Like security camera footage, audit logs are only useful if: they are always running (triggered on every change), the recording cannot be tampered with (stored externally), and you can search the footage (queryable with timestamps and user IDs).

## Mini Summary

- ✔ Audit logs record: who, what, when, before-state, after-state for every sensitive data change
- ✔ Trigger-based: AFTER INSERT/UPDATE/DELETE trigger → insert to audit_log table
- ✔ Store old_values (JSON) and new_values (JSON) — not just metadata
- ✔ Record app_user_id via session variable — CURRENT_USER is the service account, not the end user
- ✔ Tamper protection: REVOKE UPDATE/DELETE on audit table; ship to external storage
- ✔ pg_audit: comprehensive query-level logging including SELECT — for compliance and SIEM
- ✔ Audit logs in same database are not compliance-grade — use external append-only storage

# Guided Practice Quest

Work through the guided steps to implement the log_changes() trigger function for the members table, write the SQL to set the application user context before a sensitive operation, and query the audit log to reconstruct what happened during the deletion incident.

# Solo Practice Quest

Design and implement a complete audit trail for the Archive system. Tasks: (1) Identify the five tables that require audit logging and justify why (which operations: INSERT/UPDATE/DELETE, and what compliance requirement requires it); (2) Implement the generic log_changes() trigger function with proper handling of INSERT/UPDATE/DELETE, old_values/new_values exclusions for sensitive columns, and application user context; (3) Attach audit triggers to all five tables; (4) Write five audit queries: who deleted members in the last 30 days, all changes to loan due dates (extension detection), members whose email was changed (phishing indicator), bulk deletions (more than 10 records in one session), and after-hours changes by staff; (5) Design the audit log retention and storage strategy: how long to retain in the database, when to archive to external storage, and how to ensure tamper-evidence; (6) The GDPR right-to-erasure requires deleting a member's personal data. How do you reconcile this with audit trail immutability?

# Integration

**Mathematics**: Audit log integrity can be verified mathematically using hash chaining — the same principle as blockchain. Each audit row stores: `prev_hash = SHA-256(previous_row's content + previous_row's hash)`. To verify the chain: recompute each row's hash and verify it matches the stored value. If any row is modified or deleted, all subsequent hashes become invalid — tampering is immediately detectable. Formally, this creates a cryptographically linked list where every link depends on all prior links. An attacker modifying row N must also recompute all hashes from N+1 to the latest row — computationally detectable if the root hash is stored externally. This structure is equivalent to a Merkle chain: the final hash is a commitment to the entire history. Blockchain uses Merkle trees for efficiency; an audit log uses a simpler linear chain sufficient for tamper detection.

**Sciences (Forensic Science — Chain of Custody)**: The properties required of a legal audit trail mirror the chain of custody requirements in forensic evidence. Forensic evidence: must be collected by an identified person, at an identified time, with documented handling at each step, with no gaps in the custody record. Any break in chain of custody makes evidence inadmissible. Database audit logs for legal and regulatory purposes (GDPR Article 5 accountability principle, SOX Section 302, PCI-DSS Requirement 10) must satisfy analogous requirements: every data access and modification must be recorded (completeness), records must be tamper-evident and attributable to a specific actor (integrity), records must be retained for the required period (retention), and records must be accessible for inspection (availability). pg_audit's logging to a SIEM satisfies these requirements; a trigger-based audit table in the same database does not — it fails the forensic chain of custody test because the database owner can modify it.

# Lore Conclusion

"Audit triggers deployed on members, loans, and items," the Junior Engineer reported. "The trigger records old_values, new_values, CURRENT_USER, and the application user ID from the session variable." The Senior Archivist reviewed the schema. "And external shipping?" The Junior pulled up the pipeline. "pg_audit logs ship to CloudWatch every minute. The audit_log table has REVOKE UPDATE/DELETE from all roles. Old entries archive to S3 after 90 days with Object Lock." The Senior Archivist nodded. "If we face the deletion incident now, we can answer: which records were deleted, by which database user, via which application user, from which IP address, at exactly what time, and what the data contained before deletion." She closed the configuration. "One module topic left in security: compliance. The legal and regulatory frameworks that define what all of this — access control, encryption, auditing — is actually required to achieve."

---
