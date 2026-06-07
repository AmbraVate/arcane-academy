---
id: se-sen-m5-05
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m5
moduleTitle: "Module 5: Security"
moduleGlyph: "🔐"
moduleSortOrder: 5
topicSlug: owasp
topicTitle: "OWASP"
topicSortOrder: 5
lesson: owasp_fundamentals
title: "OWASP Fundamentals"
sortOrder: 5
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [secure_coding]
integrationDomains: [psychology, economics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Names at least five OWASP Top 10 categories (2021)"
    - "Explains Broken Access Control with a concrete example"
    - "Describes a mitigation for Security Misconfiguration"
    - "Connects OWASP to the SDLC — when in development should each category be addressed"
    - "Explains why 'Insecure Design' is a category (not just implementation bugs)"
  keywords: [broken, access, injection, misconfiguration, design, component, logging, cryptographic, identification, supply]
  modelAnswer: |
    OWASP Top 10 (2021) key categories and mitigations:

    1. Broken Access Control — missing resource-level auth checks
       Fix: enforce ownership checks on every data endpoint

    2. Cryptographic Failures — sensitive data exposed in transit/at rest
       Fix: TLS everywhere, encrypt sensitive DB fields, bcrypt passwords

    3. Injection (SQL, OS, LDAP) — untrusted data in commands
       Fix: parameterised queries, input validation

    4. Insecure Design — flawed threat model, missing security controls by design
       Fix: threat modelling during architecture phase, not after

    5. Security Misconfiguration — default creds, stack traces exposed, verbose errors
       Fix: disable debug in prod, remove default accounts, minimal footprint

    6. Vulnerable Components — using libraries with known CVEs
       Fix: OWASP Dependency Check in CI, regular dependency updates

    7. Identification/Authentication Failures — weak passwords, no MFA, broken session mgmt
       Fix: Spring Security defaults, MFA, secure session handling
guidedSteps:
  - id: owasp-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      According to OWASP Top 10 (2021), what is the #1 most critical web application security risk?
    inputConfig:
      options:
        - "SQL Injection"
        - "Broken Access Control"
        - "Cross-Site Scripting (XSS)"
        - "Insecure Cryptography"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Broken Access Control"]
      rejectedFeedback: "Broken Access Control moved to #1 in 2021 (from #5 in 2017). 94% of tested applications had some form of broken access control. It includes IDOR, privilege escalation, and missing authorisation checks — extremely common and high-impact."
    hint: "It moved to #1 in 2021, up from #5. It relates to what users can access, not how they're identified."
    reflectionPrompt: "Broken Access Control is #1 because it's pervasive and impactful. Every API endpoint that returns data must check: is the authenticated user allowed to see *this specific resource*? Forgetting even once creates a vulnerability."
  - id: owasp-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      OWASP category A05:2021 — ___ Misconfiguration includes using default passwords,
      exposing stack traces in error messages, and running unnecessary services.
    inputConfig:
      placeholder: "one word"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Security", "security"]
      rejectedFeedback: "**Security Misconfiguration** is the 5th most critical risk. It includes: default credentials (admin/admin), debug mode enabled in production, overly permissive CORS, stack traces returned to clients, unnecessary features enabled, and missing security headers."
    hint: "It's what you call it when the system itself is set up incorrectly from a security perspective."
    reflectionPrompt: "Spring Boot in development mode exposes `/actuator` endpoints with sensitive information. In production: disable what isn't needed, change defaults, never expose stack traces to end users."
  - id: owasp-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      OWASP A04:2021 is "Insecure Design" — a category about flaws in the security design itself, not just implementation bugs. Explain the difference between an insecure design and a secure design with an insecure implementation, using an example.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [design, architecture, threat, model, requirement, control, missing, fundamental, flaw, implement]
      rejectedFeedback: "Insecure design: a password reset feature that sends the new password via email — no amount of secure implementation fixes this fundamental flaw. Insecure implementation of a secure design: a correct OAuth flow implemented with a timing vulnerability in token comparison. The former requires redesign; the latter is a coding fix."
    hint: "Think of a security control that is completely absent by design vs one that exists but was coded incorrectly."
    reflectionPrompt: "Insecure design is addressed at the architecture phase through threat modelling. Implementation bugs are caught by code review and testing. The category exists because many teams do good code security review but never question whether the design has the right security controls at all."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the primary risk addressed by 'A06:2021 — Vulnerable and Outdated Components'?"
    options:
      - "Users uploading malicious files"
      - "Using libraries or frameworks with known security vulnerabilities (CVEs)"
      - "Storing sensitive data without encryption"
      - "Insufficiently strong password requirements"
    correctIndex: 1
    feedback: "Known CVEs in dependencies are trivially exploited. Log4Shell (2021) showed how a single vulnerability in a common library (Log4j) affected millions of applications globally. OWASP Dependency Check scans your classpath for known CVEs."
  - type: MULTIPLE_CHOICE
    question: "Which OWASP category does 'stack trace returned in HTTP 500 response to users' fall under?"
    options:
      - "Broken Access Control"
      - "Injection"
      - "Security Misconfiguration"
      - "Insecure Design"
    correctIndex: 2
    feedback: "Exposing stack traces to end users is a Security Misconfiguration issue. Stack traces reveal internal implementation details (class names, library versions, file paths) that attackers use to craft targeted attacks. In production, return generic error messages; log the full trace internally."

retrieval:
  recall: "Name the OWASP Top 10 categories for 2021. Which moved to #1 and why?"
  explain: "Explain to your team why addressing OWASP Top 10 should be part of normal development, not a separate security audit."
  mistakeId:
    code: |
      @ExceptionHandler(Exception.class)
      public ResponseEntity<String> handleException(Exception e) {
          return ResponseEntity.status(500)
              .body("Error: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
      }
    answer: "This returns the full stack trace to the client — an OWASP A05 (Security Misconfiguration) vulnerability. Stack traces expose internal class names, library versions, and file paths. Fix: log the full exception internally, return a generic message to the client: `return ResponseEntity.status(500).body('An internal error occurred. Ref: ' + errorId)`."
---

# Hook

You've secured authentication. You've parameterised your queries. You've set up HTTPS.

But what about the admin endpoint you forgot had no authorisation check? The `DEBUG=true` flag you left enabled in production? The Log4j version from 2019 that has a critical CVE in it?

Security is a system. OWASP Top 10 is the checklist that most applications fail on — not exotic zero-days, but the same predictable categories, decade after decade.

> How confident are you that the last application you worked on addressed each of the OWASP Top 10 categories?

# Lore Introduction

The Academy's security assessors make a yearly pilgrimage through every enchanted system, carrying the Codex of Known Failures — ten categories of vulnerability that every artificer knows but every generation rediscovers the hard way.

*"Pride,"* Archmage Veylan says, *"is the vulnerability not in the Codex. Every artificer who has ever said 'that won't happen to my work' appears in our records of incidents."*

# Core Learning

## Concept Introduction

**OWASP Top 10 (2021)** — the most critical web application security risks:

| Rank | Category | Core Risk |
|------|----------|-----------|
| A01 | Broken Access Control | Users access data/functions they shouldn't |
| A02 | Cryptographic Failures | Sensitive data exposed in transit or at rest |
| A03 | Injection | Untrusted data sent to interpreters (SQL, OS, etc.) |
| A04 | Insecure Design | Missing security controls in the design itself |
| A05 | Security Misconfiguration | Default configs, unnecessary features, verbose errors |
| A06 | Vulnerable Components | Libraries/frameworks with known CVEs |
| A07 | Identification/Auth Failures | Broken auth, weak passwords, session flaws |
| A08 | Software Integrity Failures | Untrusted code/data included without verification |
| A09 | Logging/Monitoring Failures | Attacks not detected or logged |
| A10 | Server-Side Request Forgery | Server fetches attacker-controlled URLs |

## Why It Matters

OWASP Top 10 is empirically derived from real vulnerability data across thousands of tested applications. These aren't theoretical risks — they're what attackers actually exploit. Systematically addressing the Top 10 eliminates the vast majority of real-world attack surface.

## Worked Examples

**A01 — Broken Access Control fix:**
```java
// Vulnerable: no ownership check
GET /api/documents/{docId}

// Secure: verify ownership
@GetMapping("/{docId}")
public Document getDocument(@PathVariable Long docId,
                             @AuthenticationPrincipal User user) {
    Document doc = documentService.findById(docId);
    if (!doc.getOwnerId().equals(user.getId()) && !user.isAdmin()) {
        throw new AccessDeniedException("Not your document");
    }
    return doc;
}
```

**A05 — Security Misconfiguration (Spring Boot):**
```yaml
# application-prod.properties
# Disable actuator endpoints in production
management.endpoints.web.exposure.include=health
# Never expose: env, beans, mappings, heapdump in production

spring.jpa.show-sql=false  # don't log SQL in production
server.error.include-stacktrace=never  # never return stack traces
```

**A06 — Vulnerable Components:**
```groovy
// In CI pipeline:
// ./gradlew dependencyCheckAnalyze
// Fails build if CVE score above threshold
dependencyCheck {
    failBuildOnCVSS = 7.0
}
```

## Common Mistakes

- **Treating OWASP as a compliance checkbox** — the goal is mitigated risk, not ticking boxes.
- **Addressing only implementation** — A04 (Insecure Design) requires security thinking at architecture time.
- **Ignoring dependency updates** — Log4Shell affected applications that simply hadn't updated Log4j.
- **No logging/monitoring (A09)** — a breach you don't detect is worse than one you do.
- **Disabling security features "just for now"** — temporary becomes permanent.

## Mental Model

OWASP Top 10 is a **pre-flight checklist**. Pilots don't skip the checklist because they're experienced — they use it precisely because experienced professionals know how often habitual confidence leads to catastrophic omission. The checklist is external memory that compensates for the limitations of expert attention.

## Mini Summary

- ✔ OWASP Top 10 represents the most common, real-world web application vulnerabilities
- ✔ A01 Broken Access Control is #1 — missing resource-level authorisation checks
- ✔ A05 Security Misconfiguration — disable debug, hide stack traces, secure defaults in production
- ✔ A06 Vulnerable Components — scan dependencies for CVEs in CI (OWASP Dependency Check)
- ✔ A04 Insecure Design — security controls must be in the design, not retrofitted

# Guided Practice Quest

**The Codex Assessment**

The Academy's new enchanted portal has been built. Run it through the Codex of Known Failures — identify which OWASP categories each design decision violates.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

A new e-commerce application has just been deployed with these characteristics:
- Users can view any order by ID: `GET /api/orders/{orderId}`
- The admin panel is at `/admin` but only hides the link in the UI — no server-side role check
- Error responses include the full Java stack trace
- The app uses a 2-year-old version of Spring Boot
- Passwords are stored as MD5 hashes
- There is no logging of failed authentication attempts
- The database connection string is hardcoded in `application.properties` which is committed to git

For each of the 7 issues above: identify the OWASP Top 10 category it falls under and describe the specific mitigation. Then prioritise — which 3 would you fix first and why?

# Integration

**Connecting to Economics — The Economics of Security Debt**

Security vulnerabilities are a form of technical debt with asymmetric payback. Normal technical debt costs time proportionally as the codebase grows. Security debt can cost nothing for years, then suddenly cost catastrophically — a data breach, a regulatory fine (GDPR fines up to 4% of global turnover), reputational damage.

The economics are unfavourable: the cost of prevention is known and small (a few hours per OWASP category). The cost of a breach is unknown and potentially enormous. This asymmetry means security investment has excellent expected returns, even though most prevention spending is never "needed" in hindsight.

OWASP also provides economic framing. Vulnerability A03 (Injection) has been in the Top 10 for over 15 years. The cost to eliminate SQL injection from a codebase is a few hours of developer time per application. The cost of a SQL injection breach (data theft, regulatory fines, incident response) is measured in hundreds of thousands to millions. The prevention is 1000x cheaper.

Yet organisations consistently under-invest in security relative to features. Behavioural economics would predict this: prevention benefits are diffuse and future; feature benefits are concrete and immediate. The same cognitive biases that affect personal saving behaviour affect organisational security investment.

How does understanding this economic framing change how you would make the case for security investment to non-technical stakeholders?

# Lore Conclusion

The Codex assessment is complete. Some vulnerabilities are found; most are not. The Academy submits to this assessment every year.

*"The Codex is not a verdict,"* Archmage Veylan says. *"It is a conversation with the past — with every artificer who made this mistake before you and paid the price. Learn from their payments. They were expensive."*

Security is not a destination. It is a practice maintained against the entropy that returns everything to vulnerability.
---
