---
id: de-app-m6-08
school: engineering
domainId: data-engineering
tier: APPRENTICE
moduleId: de-app-m6
moduleTitle: "Module 6: Database Design Foundations"
moduleGlyph: "📐"
moduleSortOrder: 6
topicSlug: design_practice
topicTitle: "Design Practice"
topicSortOrder: 2
lesson: designing_school_database
title: "Designing a School Database"
sortOrder: 8
difficulty: 3
estimatedMinutes: 35
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-app-m6-07]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Identifies the main entities in a school system
    - Correctly models the enrolment relationship (many-to-many between students and courses)
    - Places grade data in the enrolment/junction table rather than in students or courses
    - Applies appropriate PKs and FKs throughout
    - Reflects on where to store grade data and why it belongs in the relationship record
  keywords: [student, course, enrolment, teacher, grade, many-to-many, junction table, FK, PK, department, timetable]
  modelAnswer: |
    A school database needs: students (student_id, name, date_of_birth, email), teachers (teacher_id, name, department_id), departments (department_id, name), courses (course_id, title, department_id, teacher_id), enrolments (student_id FK, course_id FK, academic_year, grade — junction table for the many-to-many between students and courses). Grade belongs in the enrolment record, not in students or courses, because a grade is an attribute of the relationship (one student in one course), not of either entity alone. The schema is normalised: no student name in enrolments, no course title in enrolments — only FK references.
guidedSteps:
  - id: de-app-m6-08-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A student receives a grade for each course they take. Where should the grade column live?
    inputConfig:
      options:
        - "In the students table — each student has one grade"
        - "In the courses table — each course has one grade"
        - "In the enrolments junction table — grade is an attribute of the student-course relationship"
        - "In a separate grades table with no FK to either students or courses"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["In the enrolments junction table — grade is an attribute of the student-course relationship"]
      rejectedFeedback: "A grade belongs to neither the student (who has many grades) nor the course (which has many grades). A grade is specific to one student in one course — it is an attribute of the relationship, not of either entity. Junction tables can carry attributes of the relationship, and enrolments(student_id, course_id, grade) is the correct place. This is the general rule: attributes that describe the relationship between two entities belong in the junction table."
    hint: "A grade is about one student in one particular course — it belongs where the two are linked."
    reflectionPrompt: "What other attributes besides grade might logically belong in the enrolments table?"
  - id: de-app-m6-08-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      To prevent the same student from enrolling in the same course twice in the same year, the enrolments table should have a composite primary key of: (student_id, course_id, ________).
    inputConfig:
      placeholder: "academic_year"
    markingRule:
      matchMode: CONTAINS
      accepted: [academic_year, year, academic_year_id]
      rejectedFeedback: "A student may take the same course in different academic years (e.g. resitting an exam). The composite PK (student_id, course_id, academic_year) uniquely identifies one enrolment: one student, one course, one year. Without academic_year, the PK (student_id, course_id) would prevent a student from ever re-taking a course. The business rule determines the PK: 'one enrolment per student per course per year'."
    hint: "What third dimension allows a student to repeat a course in a different year while preventing duplicate enrolments in the same year?"
    reflectionPrompt: "What business rule does the composite PK (student_id, course_id, academic_year) enforce?"
  - id: de-app-m6-08-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      In 2–3 sentences, explain why storing teacher_name directly in the courses table would violate 3NF, and what design change fixes it.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [transitive, teacher_id, teachers table, separate, name, depends, redundancy, update, normalise]
      rejectedFeedback: "If teacher_name is in courses, then: course_id → teacher_id → teacher_name. Teacher_name depends on teacher_id (a non-key column), not directly on course_id — a transitive dependency (3NF violation). If a teacher changes their name, every course row they teach must be updated. The fix: create a teachers table (teacher_id PK, name, ...) and store only teacher_id as a FK in courses. Teacher_name is then stored once and retrieved via a JOIN."
    hint: "Draw the dependency chain: course_id → teacher_id → teacher_name. Which link is the transitive dependency?"
    reflectionPrompt: "What anomaly would occur if you tried to add a new teacher who hasn't been assigned any courses yet, in the non-normalised design?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which table should hold a student's email address?"
    options:
      - "enrolments — because students access courses via email"
      - "courses — to notify enrolled students"
      - "students — email is an attribute of the student entity, not of any relationship"
      - "teachers — for communication purposes"
    correctIndex: 2
    feedback: "Email is an attribute of the student entity — it describes the student, not their relationship to any particular course. It belongs in the students table: students(student_id, name, email, date_of_birth). Storing it in enrolments would repeat the same email in every enrolment row — a normalisation violation and maintenance burden."
  - type: MULTIPLE_CHOICE
    question: "A course belongs to a department. A teacher also belongs to a department. How should this be modelled?"
    options:
      - "Store department_name in both courses and teachers tables"
      - "Create a departments table and add department_id FK to both courses and teachers"
      - "Store all department data in the courses table and reference it from teachers"
      - "Departments do not need a separate table — they can be stored as a text field"
    correctIndex: 1
    feedback: "Departments are an entity in their own right: departments(department_id, name). Both courses and teachers reference departments via a department_id FK. This is the 3NF-compliant design: department_name is stored once, not duplicated in both courses and teachers rows. Renaming a department requires updating one row in departments."
retrieval:
  recall: "Write the complete school database schema: tables, columns, PKs, and FK relationships."
  explain: "Explain why grade belongs in the enrolments table rather than in students or courses."
  mistakeId:
    code: "storing student_name and course_title in the enrolments table to avoid joins"
    answer: "Storing student_name in enrolments creates redundancy: the name is repeated in every enrolment row for that student. If the student's name changes, every enrolment row must be updated. The same problem applies to course_title. The correct design stores only foreign keys in enrolments: student_id (FK) and course_id (FK). Names and titles are retrieved via JOIN to the students and courses tables. This is 3NF: enrolments contain only data about the enrolment relationship itself."
---

# Hook

A school system introduces a pattern that appears in many real-world domains: a relationship that carries its own data. Students enrol in courses — but the enrolment itself has an attribute: the grade. Understanding where to store relationship attributes is a key design skill.

This lesson designs a school database from scratch, with particular focus on the enrolment relationship and the question of where grades belong.

# Lore Introduction

"The Academy needs a student records system," the Registrar said. "Students, courses, teachers, departments — and grades for each course each student completes." Master Selvaris listed the entities. "Students, courses, teachers, departments. The tricky part is grades." The Registrar looked up. "Why tricky?" She wrote: "A grade is not about the student alone — a student has many grades. It is not about the course alone — a course has many students with different grades. A grade belongs to one student in one course. It is a property of the relationship, not of either entity." She drew the enrolments table. "Junction tables can hold data that belongs to the relationship itself. Grade lives here."

# Core Learning

## Concept Introduction

### Step 1: Identify Entities

```
Students    — who is learning
Courses     — what is being taught
Teachers    — who is teaching
Departments — how courses are organised
Enrolments  — the relationship between students and courses (with grade)
```

### Step 2: Identify Relationships

```
Students ↔ Courses:     Many-to-many (student takes many courses; course has many students)
Teachers → Courses:     One-to-many (teacher teaches many courses; course has one teacher)
Departments → Courses:  One-to-many (department offers many courses; course belongs to one department)
Departments → Teachers: One-to-many (department has many teachers; teacher belongs to one department)
```

### Step 3: Design the Tables

```sql
-- Departments
CREATE TABLE departments (
    department_id   SERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL UNIQUE
);

-- Teachers
CREATE TABLE teachers (
    teacher_id      SERIAL PRIMARY KEY,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    email           VARCHAR(255) UNIQUE NOT NULL,
    department_id   INTEGER REFERENCES departments(department_id)
);

-- Students
CREATE TABLE students (
    student_id      SERIAL PRIMARY KEY,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    date_of_birth   DATE,
    email           VARCHAR(255) UNIQUE NOT NULL,
    enrolment_date  DATE NOT NULL DEFAULT CURRENT_DATE
);

-- Courses
CREATE TABLE courses (
    course_id       SERIAL PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    code            VARCHAR(20) UNIQUE,
    department_id   INTEGER NOT NULL REFERENCES departments(department_id),
    teacher_id      INTEGER REFERENCES teachers(teacher_id),
    credits         INTEGER DEFAULT 1
);

-- Enrolments (many-to-many junction with grade as relationship attribute)
CREATE TABLE enrolments (
    student_id      INTEGER NOT NULL REFERENCES students(student_id),
    course_id       INTEGER NOT NULL REFERENCES courses(course_id),
    academic_year   INTEGER NOT NULL,              -- e.g. 2026
    grade           DECIMAL(4, 1),                 -- NULL = not yet graded
    enrolled_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (student_id, course_id, academic_year)
);
```

### The Grade Question: Why It Belongs in Enrolments

```
A grade describes a specific student in a specific course in a specific year.
It is NOT:
  - An attribute of the student  (students don't have "a grade" — they have many)
  - An attribute of the course   (courses don't have "a grade" — many students, many grades)
It IS:
  - An attribute of the relationship — one grade per (student, course, year)

Junction table attributes are properties of the relationship:
  enrolments: student_id | course_id | academic_year | grade
  book_authors: book_id | author_id | contribution_role  (e.g. "lead author", "editor")
  project_assignments: employee_id | project_id | role | hours_allocated
```

### 3NF Check

```
enrolments:
  PK = (student_id, course_id, academic_year)
  grade → depends on the full composite key (one grade per student per course per year) ✓
  enrolled_at → depends on the full composite key ✓
  student_name → NOT in enrolments — stored in students via student_id FK ✓
  course_title → NOT in enrolments — stored in courses via course_id FK ✓

courses:
  teacher_id → FK to teachers (not teacher_name directly) ✓
  department_id → FK to departments (not department_name directly) ✓
```

### Sample Queries

```sql
-- A student's full transcript
SELECT
    c.title         AS course,
    d.name          AS department,
    e.academic_year,
    e.grade
FROM enrolments e
JOIN courses c ON e.course_id = c.course_id
JOIN departments d ON c.department_id = d.department_id
WHERE e.student_id = 42
ORDER BY e.academic_year, c.title;

-- Course pass rate (grade >= 50 is passing)
SELECT
    c.title,
    COUNT(e.student_id)                                     AS enrolled,
    COUNT(CASE WHEN e.grade >= 50 THEN 1 END)              AS passed,
    ROUND(COUNT(CASE WHEN e.grade >= 50 THEN 1 END) * 100.0 / COUNT(e.student_id), 1) AS pass_rate_pct
FROM courses c
JOIN enrolments e ON c.course_id = e.course_id
WHERE e.grade IS NOT NULL
GROUP BY c.course_id, c.title
ORDER BY pass_rate_pct DESC;

-- Students enrolled in a course but not yet graded
SELECT s.first_name, s.last_name, s.email
FROM students s
JOIN enrolments e ON s.student_id = e.student_id
WHERE e.course_id = 7
  AND e.academic_year = 2026
  AND e.grade IS NULL;
```

## Why It Matters

School data models appear in real systems constantly — student information systems, training platforms, certification trackers — and they all share the same hard parts:

- Students take many subjects, subjects have many students: the canonical many-to-many with enrolment data on the relationship itself
- Timetables force you to model time, rooms, and conflicts — constraints a schema must make impossible, not just unlikely
- Grades attach to a student *in a class in a term*, teaching you that some facts only exist on relationships

Designing this well rehearses the exact reasoning used in HR, booking, and scheduling systems across the industry.

## Common Mistakes

- **Storing grade in students**: `grade` in students implies one grade per student (wrong). Students have many grades.
- **Storing grade in courses**: `grade` in courses implies one grade per course (wrong). Courses have many student grades.
- **Missing academic_year in the composite PK**: Without it, a student who resits a course would violate the PK constraint.
- **Storing teacher_name or department_name in courses**: Transitive dependency (3NF violation). Store the FK and retrieve the name via JOIN.

## Mental Model

The school database illustrates a principle: attributes describe the entity or relationship they logically belong to. Student attributes (name, email, birthday) belong in students. Course attributes (title, credits) belong in courses. The grade — which only exists because a specific student took a specific course — belongs in the enrolment record that represents that specific student-course pair. When you are unsure where an attribute belongs, ask: "does this value change if I change the student, or if I change the course, or both?" If both, it belongs in the junction table.

## Mini Summary

- ✔ Students ↔ Courses is many-to-many → enrolments junction table
- ✔ Grade is a relationship attribute → lives in enrolments, not students or courses
- ✔ Composite PK: (student_id, course_id, academic_year) enforces the business rule
- ✔ Teachers and departments are separate entities, referenced via FK
- ✔ No names stored in junction tables — only FK references and relationship attributes

# Guided Practice Quest

Work through the guided steps to design the enrolments table with the correct composite PK, place the grade attribute correctly, check for 3NF violations in the courses table, and write the SQL for a student transcript query.

# Solo Practice Quest

Extend the school database to support a timetable system: each course has multiple scheduled classes per week (day of week, start time, end time, room number). A room has a building, capacity, and equipment list. Students can also belong to clubs (extracurricular many-to-many). For each extension: (a) identify the new entities and relationships, (b) write the table schemas with PKs and FKs, (c) confirm the design is in 3NF, (d) write one query that uses the new tables. Then answer: what is the maximum number of students the room can accommodate, and how would you enforce this constraint in the database?

# Integration

**Mathematics**: The school database models a hypergraph where entities are vertices and relationships are hyperedges. The enrolments junction table represents a ternary relationship (student, course, year) carrying a property (grade). In formal database theory, this is a relation with three key attributes and one non-key attribute: enrolments ⊆ Students × Courses × Years × Grades. The functional dependency (student_id, course_id, academic_year) → grade is the key constraint — grade is functionally determined by the composite key. The schema is in BCNF because every non-trivial functional dependency has a superkey on the left-hand side.

**Sciences (Cognitive Science — Learning Analytics)**: Educational databases are the foundation of learning analytics — the field that uses data to understand and improve learning outcomes. A student transcript (the query above) computes exactly the data used in academic performance analysis: grade distributions, pass rates per course, student progression patterns. Research on factors affecting academic performance (Cognitive Load Theory, spaced practice, retrieval effects) requires the exact schema designed here — students, courses, enrolments with grades, and temporal data (academic_year). The design choices made here (grade in enrolments, not students) determine whether research queries are possible at all.

# Lore Conclusion

"The Academy's records system is complete," the Registrar said. "Student transcripts, course rosters, grade reports, ungraded submissions, pass rates — all answerable from seven tables." Master Selvaris reviewed the schema. "The key insight was the grade: it belongs in the enrolment, not in the student or the course." She ran the pass rate query. "Data Engineering: 87% pass rate. Mathematics: 71%. Both from one query." The Registrar signed off on the design. "Any question about academic performance can be answered from this schema." Selvaris closed her notebook. "That is the measure of a good design. Not whether it stores the data — any schema can do that. Whether it stores the data in a way that makes every important question answerable with a clean, simple query."

---
