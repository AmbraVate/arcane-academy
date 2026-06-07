---
id: se-app-m6-08
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m6
moduleTitle: "Module 6: Debugging and Engineering Habits"
moduleGlyph: "🔧"
moduleSortOrder: 6
topicSlug: engineering_habits
topicTitle: "Beginner Engineering Habits"
topicSortOrder: 2
lesson: naming_things
title: "Naming Things"
sortOrder: 8
difficulty: 1
estimatedMinutes: 20
xpReward: 40
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, linguistics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly demonstrates camelCase for variables and methods"
    - "Correctly demonstrates PascalCase for class names"
    - "Gives an example of a cryptic name and its descriptive replacement"
    - "Explains why naming matters for code readers (including future self)"
    - "Reflects on how good naming reduces the need for comments"
  keywords: [camelCase, descriptive, variable, method, class, readable, meaningful]
  modelAnswer: |
    // Bad naming
    int x = 86400;
    boolean f(int a, int b) { return a > b; }
    class D { ... }

    // Good naming
    int secondsPerDay = 86400;
    boolean isOlderThan(int ageA, int ageB) { return ageA > ageB; }
    class DailySchedule { ... }

    // The good names tell a story — no comment needed to explain what secondsPerDay is.
    // Convention: variables and methods → camelCase; classes → PascalCase; constants → UPPER_SNAKE_CASE
guidedSteps:
  - id: naming-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which variable name best follows Java naming conventions and expresses clear intent?
    inputConfig:
      options:
        - "int x;"
        - "int numberOfStudentsEnrolled;"
        - "int NUMBER_OF_STUDENTS_ENROLLED;"
        - "int Num_Students;"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["int numberOfStudentsEnrolled;"]
      rejectedFeedback: "Java variables and method names use camelCase — starting lowercase, capitalising each subsequent word. UPPER_SNAKE_CASE is for constants (static final). PascalCase (capitalised first letter) is for class names. 'x' is cryptic and provides no context."
    hint: "Java variable naming convention: lowerCamelCase — and the name should describe what it holds."
    reflectionPrompt: "numberOfStudentsEnrolled is longer than 'x', but a reader understands it instantly. Clarity beats brevity."

  - id: naming-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A class representing a bank account should be named using ___ (what naming convention)?
    inputConfig:
      placeholder: "naming convention name"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["PascalCase", "UpperCamelCase", "Pascal Case", "Upper Camel Case"]
      rejectedFeedback: "Class names in Java use PascalCase (also called UpperCamelCase): every word starts with a capital letter. For example: BankAccount, SpellRegistry, UserProfileService."
    hint: "Class names start with a capital letter — every word capitalised."
    reflectionPrompt: "PascalCase for classes, camelCase for variables and methods, UPPER_SNAKE_CASE for constants. These three rules cover 99% of Java naming."

  - id: naming-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      The phrase "code is read more than it is written" is a core principle of clean code. Explain what this means and why it justifies using long, descriptive names rather than short abbreviations.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [read, maintain, understand, future, colleague]
      rejectedFeedback: "Code is written once but read many times — by teammates, by future maintainers, and by yourself six months later. A long descriptive name like 'maximumRetryAttempts' takes two extra seconds to type but saves minutes of confusion every time it is read. The cost of typing is small and one-time; the cost of confusion is large and repeated."
    hint: "Who reads your code, and how often?"
    reflectionPrompt: "Every time you shorten a variable name, you are saving yourself 3 seconds and costing every reader 30 seconds. The math rarely favours abbreviation."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of the following is the correct Java convention for a constant (static final) value?"
    options:
      - "maxRetries"
      - "MaxRetries"
      - "MAX_RETRIES"
      - "max-retries"
    correctIndex: 2
    feedback: "Java constants (declared with static final) use UPPER_SNAKE_CASE — all uppercase letters with words separated by underscores. This convention signals to readers that the value never changes."
  - type: MULTIPLE_CHOICE
    question: "Which method name best expresses its intent?"
    options:
      - "process()"
      - "doStuff()"
      - "calculateMonthlyInterest()"
      - "calc()"
    correctIndex: 2
    feedback: "calculateMonthlyInterest() tells you exactly what the method does. process(), doStuff(), and calc() are vague — a reader must look inside the method to understand its purpose. Good names mean you can understand code at a glance."
retrieval:
  recall: "State the three main Java naming conventions (for variables/methods, classes, and constants) and give one example of each."
  explain: "A colleague argues that short variable names like 'i', 'n', and 'x' are fine because 'everyone knows what they mean'. When is this true, and when does it break down?"
  mistakeId:
    code: |
      public class a {
          int X = 10;
          void Calc(int N) {
              int R = N * X;
              System.out.println(R);
          }
      }
    answer: "Multiple naming violations: class 'a' should be PascalCase (e.g. Calculator); 'X' looks like a constant but is not final — should be camelCase (e.g. multiplier); method 'Calc' should be camelCase (e.g. calculate); 'N' and 'R' should be camelCase descriptive names (e.g. number, result). None of these names describe their meaning."
---

# Hook

There is a famous joke among programmers: the two hardest problems in computer science are cache invalidation, naming things, and off-by-one errors. It is funny because naming is genuinely hard — and genuinely important. A variable named `x` or a method called `doStuff()` forces every reader to open the implementation and reverse-engineer the meaning. A variable named `remainingLivesCount` or a method called `applyPenaltyForLateSubmission()` tells its story immediately.

Code is not written primarily for computers. The computer would happily run code with every variable named `a`, `b`, `c`. Code is written for humans — your teammates today, your future colleagues in a year, and most often, your future self in six months when you have completely forgotten what you were thinking.

> Think of a time you had to decipher someone else's (or your own past) writing with poor labelling. How did it feel, and how long did it take?

# Lore Introduction

In the Academy's Script Hall, apprentices learn the ancient Art of Inscription — the discipline of writing spell glyphs that are not merely functional but legible. A glyph scratched hastily in personal shorthand might summon fire correctly today, but when the next Keeper of Flame reads it centuries from now, they must be able to understand it without the original author present to explain.

The First Inscriber's maxim is carved above the Hall's entrance: *"Name the rune for the one who will cast it after you."* This is the foundational principle of good naming: every name in your code is a message to a future reader.

# Core Learning

## Concept Introduction

**Java naming conventions:**

| Element | Convention | Example |
|---|---|---|
| Variables | `lowerCamelCase` | `playerHealth`, `maxRetries` |
| Methods | `lowerCamelCase` | `calculateDamage()`, `isExpired()` |
| Classes | `PascalCase` | `SpellRegistry`, `UserAccount` |
| Constants | `UPPER_SNAKE_CASE` | `MAX_LEVEL`, `DEFAULT_TIMEOUT` |
| Packages | `all.lowercase` | `com.academy.spells` |

**Descriptive vs cryptic names:**
```java
// Cryptic — reader must guess
int d;
boolean f;
void p(String s) { ... }

// Descriptive — reader understands immediately
int daysSinceLastLogin;
boolean isEmailVerified;
void printWelcomeMessage(String username) { ... }
```

## Why It Matters

Research in cognitive science shows that humans read code much as they read natural language — pattern-matching against familiar structures. Good names reduce **cognitive load**: the mental effort required to decode what a piece of code does. When names accurately describe their purpose, bugs become easier to spot (because the name creates an expectation against which the implementation can be checked) and code becomes self-documenting.

## Worked Examples

**Example 1 — Variables that explain their role**
```java
// Bad
int x = 3600;
int y = x * 24;

// Good
int secondsPerHour = 3600;
int secondsPerDay  = secondsPerHour * 24;
// No comment needed — the names are the documentation
```

**Example 2 — Methods that express their behaviour**
```java
// Bad
void go(Player p) {
    p.hp -= 10;
}

// Good
void applyPoisonDamage(Player target) {
    target.setHealth(target.getHealth() - POISON_DAMAGE_PER_TURN);
}
```

**Example 3 — Boolean names that read as questions**
```java
// Bad — unclear what true/false means
boolean status;

// Good — reads as a yes/no question
boolean isAdminUser;
boolean hasCompletedOnboarding;
boolean wasPaymentSuccessful;

// Usage reads naturally
if (isAdminUser) { showAdminPanel(); }
```

## Common Mistakes

- **Single-letter names outside loops** — `i` and `j` are acceptable loop counters; everywhere else, be descriptive.
- **Misleading names** — a method called `getUser()` that also writes to a database violates the principle of least surprise; names should match behaviour.
- **Inconsistent conventions** — mixing `get_user`, `getUser`, and `GetUser` in the same codebase forces readers to remember which style each developer used.
- **Abbreviations** — `usrAcctMgr` saves six characters and costs ten seconds of decoding every time it is read.
- **Comment-dependent names** — if your variable needs a comment to explain what it is, the name is not doing its job.

## Mental Model

Naming is like labelling filing cabinets. A cabinet labelled "STUFF" forces you to open it every time to find what you need. A cabinet labelled "2024 Tax Documents — Receipts" lets you walk past it confidently when you need something else, and reach for it precisely when you do. Good names are self-filing.

## Mini Summary

✔ Variables and methods use `lowerCamelCase`; classes use `PascalCase`; constants use `UPPER_SNAKE_CASE`.
✔ Names should describe *what* something is or does, not *how* it works internally.
✔ Boolean names should read as questions: `isActive`, `hasPermission`, `wasDeleted`.
✔ "Code is read more than it is written" — optimise for the reader, not the typist.
✔ A name that needs a comment to explain it is a name that should be improved.

# Guided Practice Quest

**The Inscription Exam**
The Academy's Script Hall is reviewing a batch of student code for naming violations. Identify the correct conventions and improvements.
Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Take the following cryptically-named code snippet and rewrite it with proper Java naming conventions. Then write a 2-3 sentence reflection on how the renamed version changes your understanding of what the code does:
```java
class Z {
    int q = 5;
    boolean chk(int x) {
        return x > q;
    }
    void pr(int x) {
        if (chk(x)) System.out.println("Y");
        else System.out.println("N");
    }
}
```

# Integration

**Connecting to Linguistics — Semiotics and Signification**
Naming in code is a form of **semiotics** — the study of signs and their meanings. The linguist Ferdinand de Saussure described the relationship between a **signifier** (the word or symbol) and the **signified** (the concept it represents). In code, your variable name is the signifier; the data it holds is the signified. When the two are closely aligned — when `maximumRetryCount` genuinely represents a maximum retry count — the code is semiologically coherent. When they diverge — when `data` holds a user's password hash — the code creates cognitive dissonance.

Good programmers are, among other things, careful semioticians: they choose signifiers that precisely match their signified, so readers can trust that the name tells the truth about the thing it names.

> Can you think of a word in everyday language that means something very different from what it appears to mean? How does this ambiguity create confusion — and how does it parallel the problems caused by misleading variable names?

# Lore Conclusion

The apprentice leaves the Script Hall with a new habit: before writing any glyph, they speak its name aloud and ask, "Will the next Keeper understand what this rune holds from its name alone?" It takes a little longer to craft each inscription, but the finished spell is clear, maintainable, and worthy of the Archive.

In the next lesson, you will learn that naming is only one dimension of readable code. The way you arrange code visually — indentation, spacing, line length — shapes whether a reader can follow its structure at a glance. The Art of Inscription continues.

---
