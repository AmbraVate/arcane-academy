---
id: de-jun-m2-04
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m2
moduleTitle: "Module 2: Database Programming"
moduleGlyph: "🔧"
moduleSortOrder: 2
topicSlug: views
topicTitle: "Views"
topicSortOrder: 4
lesson: views
title: "Views"
sortOrder: 4
difficulty: 2
estimatedMinutes: 25
xpReward: 40
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m2-03]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Explains what a view is and how it differs from a table
    - Creates a view and queries it like a table
    - Distinguishes regular views from materialised views
    - Identifies when views improve security, reuse, or abstraction
    - Describes the performance implication of querying views that contain complex joins
  keywords: [view, CREATE VIEW, materialised view, virtual table, stored query, abstraction, security, reuse, refresh, performance, updatable view]
  modelAnswer: |
    A view is a named stored SELECT statement — a virtual table. Querying a view executes the underlying SELECT. Views do not store data (unless materialised). Regular views: always fresh data, but the query re-executes on each access — complex views with many joins can be slow. Materialised views: pre-compute and store the result; must be refreshed when source data changes. Views improve security (expose only certain columns/rows), reuse (complex join expressed once, used from many places), and abstraction (hide schema complexity from consumers). Updatable views allow INSERT/UPDATE/DELETE if they reference one table without aggregation — but this is limited and database-specific.
guidedSteps:
  - id: de-jun-m2-04-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What actually happens when you run SELECT * FROM active_members where active_members is a regular (non-materialised) view?
    inputConfig:
      options:
        - "The database returns a cached snapshot of the data from when the view was last created"
        - "The database executes the view's underlying SELECT query against the live tables"
        - "The database reads from a separate view storage area"
        - "The query only works if the tables referenced in the view have not changed"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The database executes the view's underlying SELECT query against the live tables"]
      rejectedFeedback: "A regular (non-materialised) view is a stored query, not stored data. Every time you query the view, the database executes the view's underlying SELECT against the current table data. This means the view always returns fresh data — but it also means complex views with multiple joins are re-executed on every access. The view definition is just SQL stored in the database catalogue; the data comes from the underlying tables each time."
    hint: "A view stores the query, not the result. What executes when you query it?"
    reflectionPrompt: "If the view's underlying SELECT takes 5 seconds, how long does SELECT * FROM that_view take?"
  - id: de-jun-m2-04-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A ________ view stores the query result physically and must be explicitly refreshed when source data changes.
    inputConfig:
      placeholder: "materialised"
    markingRule:
      matchMode: CONTAINS
      accepted: [materialised, materialized, "materialised view", "materialized view"]
      rejectedFeedback: "A materialised view (materialized in US spelling) stores the query result as physical data. Queries against it read the stored result rather than re-executing the underlying query — potentially much faster for complex aggregations. The trade-off: the materialised view is a snapshot at the time of last refresh. It can become stale when underlying tables change. Refresh can be manual (REFRESH MATERIALISED VIEW name), scheduled, or automatic on data change (depending on the database). PostgreSQL, Oracle, and SQL Server support materialised views; MySQL does not natively."
    hint: "The type of view that physically stores its query result — like a cached result set."
    reflectionPrompt: "When would you choose a materialised view over a regular view?"
  - id: de-jun-m2-04-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain how a view can be used to implement column-level security — hiding sensitive columns from certain users.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [column, sensitive, hide, expose, grant, SELECT, salary, password, restrict, role, access]
      rejectedFeedback: "A view can expose only a subset of columns from an underlying table. For example, a view customer_public exposes (customer_id, name, region) from the customers table but not (email, phone, credit_score). You then grant SELECT on the view to roles that should not see sensitive fields, and revoke direct SELECT on the underlying table. The user can query customer_public and see names and regions, but the email and credit score columns are simply not part of the view — they cannot be accessed through it. This is a standard row-level and column-level access control pattern."
    hint: "What if the view only includes non-sensitive columns, and users only have access to the view — not the table?"
    reflectionPrompt: "Can a view implement row-level security as well as column-level? Give an example."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of these is a limitation of regular views?"
    options:
      - "Views cannot join more than two tables"
      - "Querying a view re-executes the underlying SQL each time — complex views can be slow and cannot be indexed directly"
      - "Views cannot be used in WHERE clauses"
      - "Views are read-only and cannot be modified"
    correctIndex: 1
    feedback: "A regular view is a stored query. Every SELECT against the view re-executes the underlying SQL against the live tables. If the view contains five joins and a window function over millions of rows, every query pays that cost. You cannot create a separate index on a view's virtual columns (you can only index the underlying tables). For frequently queried complex aggregations, a materialised view solves the performance problem by pre-computing and caching the result. The fourth option is partly wrong — some views are updatable under specific conditions."
  - type: MULTIPLE_CHOICE
    question: "A materialised view showing monthly revenue is used by the dashboard. The underlying orders table receives inserts constantly. What is the main trade-off?"
    options:
      - "The materialised view doubles storage requirements"
      - "The dashboard sees fast queries but potentially stale data — the view must be refreshed to show current numbers"
      - "Materialised views cannot aggregate data"
      - "Refreshing a materialised view locks the underlying tables"
    correctIndex: 1
    feedback: "The core materialised view trade-off is freshness vs performance. A materialised view stores a pre-computed snapshot. It is fast to query but becomes stale as the underlying data changes. The dashboard queries return immediately, but the numbers may be from an hour ago (or whenever the last refresh ran). For a real-time orders dashboard, this may be acceptable (refresh every 5 minutes) or not (CEO wants live numbers). The refresh schedule is a business decision about acceptable data latency vs query performance requirements."
retrieval:
  recall: "Create a view called member_loan_summary that shows each member's name, total loans count, active loan count, and overdue loan count."
  explain: "Compare regular views vs materialised views on four dimensions: data freshness, query performance, storage, and update handling."
  mistakeId:
    code: "CREATE VIEW order_summary AS SELECT customer_id, SUM(total_amount) AS total FROM orders GROUP BY customer_id; UPDATE order_summary SET total = total * 1.1 WHERE customer_id = 42;"
    answer: "Views with GROUP BY aggregation are not updatable — the database cannot determine which underlying order rows to update when the view presents aggregated data. The UPDATE will fail with an error. Updatable views require a direct 1:1 mapping to underlying table rows: no GROUP BY, no aggregate functions, no DISTINCT, no subqueries, no UNION. If you need to update order amounts for a customer, update the orders table directly. If you need an updatable summary, you need a materialised view with a refresh mechanism or a separate denormalised table maintained by triggers."
---

# Hook

Subqueries are anonymous and must be repeated. CTEs are named but temporary. Views are named, stored, and permanent — a query saved in the database that any authorised user can reference as if it were a table. Views provide reuse, abstraction, and security without duplicating data.

# Lore Introduction

"The reporting team needs to query member activity, but they should not have access to the members table directly — it contains personal data," the Senior Archivist said. "Create a view." The Junior Engineer looked up. "A view that shows only the columns they need, without the sensitive fields?" The Senior Archivist nodded. "The reporting team gets SELECT on the view. Not on the underlying table. They see activity data. They cannot see the addresses, phone numbers, or payment history." She paused. "Views also solve the repetition problem: the complex five-table join that every report needs is defined once in the view. Every report just queries the view. The join logic lives in one place."

# Core Learning

## Concept Introduction

### Creating and Querying Views

```sql
-- Create a view
CREATE VIEW active_member_loans AS
SELECT
    m.member_id,
    m.name,
    m.email,
    COUNT(l.loan_id) AS active_loans,
    MAX(l.due_date)  AS latest_due_date
FROM members m
LEFT JOIN loans l ON m.member_id = l.member_id
    AND l.return_date IS NULL    -- active loans only
GROUP BY m.member_id, m.name, m.email;

-- Query the view exactly like a table
SELECT * FROM active_member_loans WHERE active_loans > 3;
SELECT name, latest_due_date FROM active_member_loans
WHERE latest_due_date < CURRENT_DATE;  -- overdue
```

### Column-Level Security via Views

```sql
-- Members table has sensitive columns
-- CREATE TABLE members (member_id, name, email, phone, date_of_birth,
--                       credit_check_result, home_address, payment_history)

-- Public-facing view: hides sensitive columns
CREATE VIEW members_public AS
SELECT member_id, name, tier, created_at
FROM members;

-- Reporting view: limited columns for the analytics team
CREATE VIEW members_for_reporting AS
SELECT member_id, tier, region, created_at,
       DATE_PART('year', AGE(date_of_birth)) AS age_group_approx
FROM members;

-- Grant access to views, not to the underlying table
GRANT SELECT ON members_public TO web_app_role;
GRANT SELECT ON members_for_reporting TO reporting_role;
-- REVOKE direct SELECT on members FROM reporting_role; (if needed)
```

### Row-Level Security via Views

```sql
-- Each regional administrator can only see their region's members
CREATE VIEW my_region_members AS
SELECT m.*
FROM members m
WHERE m.region = current_setting('app.current_region');
-- current_setting() reads a session-level setting set by the application
-- on login: SET LOCAL app.current_region = 'North';
```

### Materialised Views

```sql
-- PostgreSQL materialised view for expensive monthly aggregation
CREATE MATERIALISED VIEW monthly_revenue AS
SELECT
    DATE_TRUNC('month', order_date) AS month,
    COUNT(*)                         AS order_count,
    SUM(total_amount)                AS revenue,
    AVG(total_amount)                AS avg_order_value
FROM orders
WHERE status = 'completed'
GROUP BY DATE_TRUNC('month', order_date);

-- Create an index on the materialised view (not possible on regular views)
CREATE INDEX idx_monthly_revenue_month ON monthly_revenue (month);

-- Query is instant — reads the stored result
SELECT * FROM monthly_revenue WHERE month >= '2024-01-01';

-- Refresh when data changes (or schedule with pg_cron)
REFRESH MATERIALISED VIEW monthly_revenue;

-- CONCURRENTLY: refreshes without locking reads (requires unique index)
REFRESH MATERIALISED VIEW CONCURRENTLY monthly_revenue;
```

### Updatable Views

```sql
-- Simple views (no aggregation, no DISTINCT, no JOIN) may be updatable
CREATE VIEW active_members AS
SELECT member_id, name, email, status
FROM members
WHERE status != 'archived';

-- This UPDATE works (view maps directly to the table)
UPDATE active_members SET email = 'new@example.com' WHERE member_id = 42;
-- Equivalent to: UPDATE members SET email = ... WHERE member_id = 42 AND status != 'archived'

-- WITH CHECK OPTION: prevents updates that would remove the row from the view
CREATE VIEW active_members AS
SELECT member_id, name, email, status
FROM members
WHERE status != 'archived'
WITH CHECK OPTION;
-- Now: UPDATE active_members SET status = 'archived' WHERE member_id = 42 → error
-- The update would move the row outside the view's filter — WITH CHECK OPTION rejects it
```

### Views for Query Abstraction

```sql
-- Hide schema complexity from application developers
-- Instead of every app team writing this join:
-- SELECT m.*, l.loan_count, l.overdue_count
-- FROM members m
-- LEFT JOIN (SELECT member_id, COUNT(*) AS loan_count,
--             SUM(CASE WHEN due_date < NOW() AND return_date IS NULL THEN 1 ELSE 0 END) AS overdue_count
--             FROM loans GROUP BY member_id) l
-- ON m.member_id = l.member_id;

-- They query:
CREATE VIEW member_dashboard AS
SELECT m.member_id, m.name, m.tier, m.email,
       COALESCE(l.loan_count, 0)    AS loan_count,
       COALESCE(l.overdue_count, 0) AS overdue_count
FROM members m
LEFT JOIN (
    SELECT member_id,
           COUNT(*) AS loan_count,
           SUM(CASE WHEN due_date < NOW() AND return_date IS NULL THEN 1 ELSE 0 END) AS overdue_count
    FROM loans
    GROUP BY member_id
) l ON m.member_id = l.member_id;

SELECT * FROM member_dashboard WHERE overdue_count > 0;
```

## Common Mistakes

- **Treating a regular view as cached**: Regular views are always live — querying them re-executes the SQL. If performance is the concern, use a materialised view.
- **Updating aggregated views**: Views with GROUP BY, DISTINCT, or aggregate functions are not updatable. Attempting to update them causes an error.
- **Granting access to the view but not refreshing the materialised view**: A materialised view that is never refreshed gives users stale data that looks current.
- **Views hiding performance problems**: A view that contains a slow multi-table join hides the complexity from callers — they may not understand why `SELECT * FROM simple_looking_view` takes 10 seconds.

## Mental Model

A view is a window into the database — not a copy, but a shaped opening. A regular view is a window you look through each time; what you see reflects the current state of the room. A materialised view is a photograph of the room — it shows the state from when the photo was taken, and you need someone to take a new photo to update your view. Security views are windows with frosted glass — you can see shapes (some columns) but not the sensitive details behind. The window metaphor captures all three uses: transparency (live data), opacity (security), and framing (abstraction).

## Mini Summary

- ✔ A view is a stored SELECT statement — a virtual table queried like a real table
- ✔ Regular views re-execute on every query — always fresh, potentially slow
- ✔ Materialised views store the result — fast queries, stale until refreshed
- ✔ Materialised views can have their own indexes; regular views cannot
- ✔ Views provide security (hide columns/rows), reuse (complex joins once), and abstraction (hide schema detail)
- ✔ Simple views may be updatable; aggregated/multi-table views generally are not

# Guided Practice Quest

Work through the guided steps to create a view that provides column-level security for the members table, create a materialised view for an expensive monthly aggregation, and verify that a regular view always returns current data while the materialised view requires an explicit refresh to update.

# Solo Practice Quest

Design a view layer for the Archive system: (1) members_public — hides sensitive columns (address, date_of_birth, payment_history), exposes only safe attributes; (2) overdue_loans — all active loans past their due date, including member name and contact email; (3) catalogue_availability — for each item in the archive, shows the item title, category, and current status (available, on_loan, reserved); (4) monthly_activity_summary (materialised) — pre-aggregated monthly counts of new loans, returns, new members, and overdue notices; create an appropriate index on it. For each view: write the CREATE VIEW/MATERIALISED VIEW statement, specify who should be granted access and why, and describe what happens to the view if a member is deleted from the underlying members table.

# Integration

**Mathematics**: A database view corresponds to a relation in relational algebra derived from base relations through algebraic operations. The view V = π_{name,email}(σ_{status='active'}(Members)) (projection of name and email columns from the selection of active members) is a derived relation — it exists as a logical entity but has no physical storage. Materialised views correspond to memoisation in computer science: the result of a pure function (the view query) is computed once and cached for reuse, traded against staleness risk. The view update problem — determining when modifications to a derived relation can be propagated back to base relations — is a formal problem in relational algebra theory, related to the problem of invertibility: can the mapping f: base_relations → view be inverted to determine how base changes should modify the derived result?

**Sciences (Astronomy — Virtual Observatories)**: The International Virtual Observatory Alliance (IVOA) uses database views as the core abstraction for distributed astronomical data. Different telescopes store observational data in incompatible schemas. Virtual observatory views provide a unified interface: a view on a telescope database exposes only IAU-standard column names and formats, hiding the underlying schema differences. Materialised views cache processed data products — calibrated spectra, source catalogues, light curves — so astronomers can query them without re-running expensive pipelines. The same view patterns used in the Archive system directly correspond to the data access architecture of professional astronomical databases like the Sloan Digital Sky Survey and ESA Gaia mission archives.

# Lore Conclusion

"The reporting team now has three views," the Junior Engineer reported. "They get the data they need, with no access to sensitive columns. The monthly summary view is materialised — their dashboard loads instantly." The Senior Archivist reviewed the view definitions. "Document which views exist, why, and who has access. Views are infrastructure — future engineers need to know the view layer exists before they grant direct table access by accident." She closed the file. "You have completed Module 2: Database Programming. Stored procedures for multi-step operations, functions for reusable computations, triggers for automatic responses, views for abstraction and security. The next module: transactions — the mechanism that keeps all of this consistent under concurrent load."

---
