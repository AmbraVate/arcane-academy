---
id: se-sen-m5-04
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m5
moduleTitle: "Module 5: Security"
moduleGlyph: "🔐"
moduleSortOrder: 5
topicSlug: secure_coding
topicTitle: "Secure Coding"
topicSortOrder: 4
lesson: secure_coding
title: "Secure Coding"
sortOrder: 4
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [encryption_basics]
integrationDomains: [psychology, design]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Identifies SQL injection vulnerability and shows parameterised query fix"
    - "Explains input validation vs output encoding for XSS prevention"
    - "Describes how secrets should be managed (env vars, vaults)"
    - "Applies principle of least privilege to code-level decisions"
    - "Names at least one dependency scanning tool and explains why it matters"
  keywords: [injection, parameterised, sanitise, validate, secret, environment, xss, encode, dependency, privilege]
  modelAnswer: |
    // SQL Injection — VULNERABLE
    String query = "SELECT * FROM users WHERE email = '" + email + "'";

    // SQL Injection — FIXED (parameterised query)
    PreparedStatement stmt = conn.prepareStatement(
        "SELECT * FROM users WHERE email = ?");
    stmt.setString(1, email);

    // XSS — validate on input, encode on output
    // Input: reject HTML in user-supplied strings
    // Output: use Thymeleaf th:text (not th:utext) to auto-escape

    // Secrets — never in code
    String apiKey = System.getenv("PAYMENT_API_KEY"); // correct
    // String apiKey = "sk_live_abc123"; // WRONG — in source control
guidedSteps:
  - id: sc-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A developer builds a search query like this:
      ```java
      String sql = "SELECT * FROM products WHERE name LIKE '%" + searchTerm + "%'";
      ```
      An attacker submits `searchTerm = "'; DROP TABLE products; --"`.
      What type of attack is this and what is the fix?
    inputConfig:
      options:
        - "XSS attack — fix with output encoding"
        - "SQL Injection — fix with parameterised queries or prepared statements"
        - "CSRF attack — fix with CSRF tokens"
        - "Brute force — fix with rate limiting"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["SQL Injection — fix with parameterised queries or prepared statements"]
      rejectedFeedback: "This is SQL Injection. Unsanitised user input is concatenated directly into SQL, allowing attackers to execute arbitrary SQL commands. Fix: use `PreparedStatement` with `?` placeholders. The JDBC driver handles escaping, making injection impossible."
    hint: "The attacker is injecting SQL syntax into the query. The fix separates code from data."
    reflectionPrompt: "Parameterised queries are the canonical fix: `PreparedStatement stmt = conn.prepareStatement('SELECT * FROM products WHERE name LIKE ?'); stmt.setString(1, '%' + searchTerm + '%');` The JDBC driver escapes the input automatically."
  - id: sc-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Sensitive configuration values like API keys and database passwords should be
      read from ___ variables, not hardcoded in source files.
    inputConfig:
      placeholder: "where to read secrets from"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["environment", "environment variables", "env", "env variables"]
      rejectedFeedback: "Environment variables keep secrets out of source code. Anyone with access to your repository can read hardcoded secrets. In production, use a secrets manager (AWS Secrets Manager, HashiCorp Vault) that injects secrets as env vars at runtime."
    hint: "These are variables set in the OS or deployment config, not in code."
    reflectionPrompt: "Even with env vars, be careful: don't log them, don't pass them in URLs, and rotate them regularly. The 12-factor app methodology codified env vars for config as a foundational principle."
  - id: sc-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A colleague says "we validate all input on the frontend, so we don't need server-side validation." Why is this wrong? What specific attacks does server-side validation prevent?
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [bypass, curl, postman, server, validation, api, client, trust, direct, request]
      rejectedFeedback: "Frontend validation can be bypassed trivially — an attacker can send requests directly to the API via curl or Postman, skipping the browser entirely. Server-side validation is mandatory. The rule: never trust client-supplied data. Frontend validation is UX; server-side validation is security."
    hint: "Can an attacker access your API without going through your UI?"
    reflectionPrompt: "Never trust the client. API endpoints are exposed directly to the internet. Any check that only exists in the frontend JavaScript is not a security control — it's a UX convenience."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the correct approach for preventing Cross-Site Scripting (XSS)?"
    options:
      - "Encrypt all user inputs"
      - "Validate inputs and HTML-encode outputs when rendering user-supplied content"
      - "Use HTTPS for all requests"
      - "Block all requests containing HTML tags"
    correctIndex: 1
    feedback: "XSS occurs when user-supplied content is rendered as HTML/JavaScript. Prevention: validate inputs (reject unexpected content types) and encode outputs (convert `<script>` to `&lt;script&gt;` before rendering). Templating engines like Thymeleaf auto-escape by default."
  - type: MULTIPLE_CHOICE
    question: "A developer checks their `application.properties` into git containing `stripe.secret-key=sk_live_abc123`. What is the immediate security risk?"
    options:
      - "The key will be overwritten by environment variables"
      - "Anyone with repository access (current or historical) can extract the live API key"
      - "The file will fail to parse"
      - "Spring Boot will not load the property"
    correctIndex: 1
    feedback: "Git history is permanent. Even if you delete the file later, the key remains in git history and is extractable with `git log`. Rotate the key immediately. Never commit secrets — use environment variables or a secrets manager."

retrieval:
  recall: "Name four secure coding practices that prevent the most common injection and data exposure vulnerabilities."
  explain: "Explain to a junior developer why parameterised queries prevent SQL injection where string concatenation does not."
  mistakeId:
    code: |
      @PostMapping("/users/register")
      public User register(@RequestBody UserRegistrationRequest request) {
          // Store whatever role the user sends
          User user = new User(request.getEmail(), request.getRole());
          return userRepository.save(user);
      }
    answer: "The user can register themselves as ADMIN by sending `{\"email\": \"hacker@evil.com\", \"role\": \"ADMIN\"}` in the request body. Never accept security-sensitive fields (roles, permissions, account IDs) from user input. Assign role server-side: `new User(request.getEmail(), Role.USER)` — always the default role on registration."
---

# Hook

Stack Overflow's 2021 survey found that SQL injection has been the most commonly exploited vulnerability for over a decade. It's not sophisticated. It doesn't require advanced tooling. You just put `' OR '1'='1` in a search box and see what happens.

The fix has been known since the 1990s: parameterised queries. Yet SQL injection vulnerabilities are still introduced daily, in production systems, by developers who know better.

Secure coding isn't exotic. It's discipline applied consistently to the basics.

> Have you ever seen a search field or form in an application you built that wasn't using parameterised queries? What would happen if someone submitted SQL syntax in that field?

# Lore Introduction

The Academy's inscription chamber has a rule carved above the door: *"What the supplicant provides is data. What the scribe writes is command. Never let data become command."*

It was carved there after the Incident of the Corrupted Summons — when an apprentice included raw user input in a binding incantation and the incantation executed a rather different spell than intended.

*"The boundary between data and instruction,"* Archmage Veylan says, *"is the most important boundary in secure code."*

# Core Learning

## Concept Introduction

**The core principle:** never mix untrusted data with code or commands. Every injection vulnerability — SQL, OS command, LDAP, XML — is a violation of this principle.

**Key secure coding practices:**

**1. Parameterised Queries (SQL Injection prevention):**
```java
// VULNERABLE
String q = "SELECT * FROM users WHERE email = '" + email + "'";

// SECURE
PreparedStatement stmt = conn.prepareStatement(
    "SELECT * FROM users WHERE email = ?");
stmt.setString(1, email);  // JDBC handles escaping
```

**2. Input Validation:**
```java
// Validate before processing
if (!email.matches("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")) {
    throw new InvalidInputException("Invalid email format");
}
```

**3. Output Encoding (XSS prevention):**
```java
// Thymeleaf: th:text auto-escapes (safe)
// th:utext does NOT escape (dangerous for user content)
<span th:text="${userComment}">Comment</span>  // safe
<span th:utext="${userComment}">Comment</span>  // DANGEROUS
```

**4. Secrets Management:**
```java
// Never hardcode
String key = System.getenv("PAYMENT_API_KEY");

// Spring: use @Value with env vars
@Value("${payment.api.key}")
private String apiKey;
// Set via PAYMENT_API_KEY env var, not application.properties
```

## Why It Matters

- SQL injection can expose or destroy entire databases
- XSS steals session tokens, enabling account takeover
- Hardcoded secrets are permanently compromised once in git history
- Mass assignment lets users self-assign roles or modify other users' data
- Dependency vulnerabilities (Log4Shell) can affect thousands of applications

## Worked Examples

**Preventing mass assignment:**
```java
// Dangerous — exposes all fields to user input
@PostMapping("/users")
public User create(@RequestBody User user) { ... }

// Safe — explicit allowlist
@PostMapping("/users")
public User create(@RequestBody CreateUserRequest request) {
    User user = new User();
    user.setEmail(request.getEmail());
    user.setName(request.getName());
    // Role set server-side, never from request
    user.setRole(Role.USER);
    return userRepository.save(user);
}
```

**Dependency scanning (build.gradle):**
```groovy
// OWASP Dependency Check plugin
plugins {
    id 'org.owasp.dependencycheck' version '8.4.0'
}
// Run: ./gradlew dependencyCheckAnalyze
```

## Common Mistakes

- **Trusting client input for security decisions** — roles, permissions, prices, IDs
- **String concatenation in SQL** — always use parameterised queries
- **Using `th:utext` for user content** — auto-escaping is a feature, not a limitation
- **Secrets in `application.properties` committed to git** — use env vars or a vault
- **No dependency vulnerability scanning** — known CVEs in dependencies are trivially exploited

## Mental Model

Secure coding is **quarantine**. User input arrives from an untrusted zone. Before it crosses into the trusted zone (your database, your HTML output, your OS commands), it must be sanitised — all contaminants removed. The moment you let raw input through the quarantine border directly into a command, you've created an injection vulnerability.

## Mini Summary

- ✔ Parameterised queries are the only correct defence against SQL injection
- ✔ Validate all input server-side; encode all user-supplied output (XSS prevention)
- ✔ Never hardcode secrets — use environment variables or secrets managers
- ✔ Mass assignment protection: use explicit DTOs, not entity binding
- ✔ Scan dependencies for known CVEs as part of CI

# Guided Practice Quest

**The Inscription Chamber**

Three code samples violate the "data vs command" principle. Identify each vulnerability and write the secure version.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

A colleague has written a REST API endpoint for a comment system:
```java
@PostMapping("/posts/{postId}/comments")
public Comment addComment(@PathVariable Long postId,
                          @RequestBody Map<String, Object> body) {
    String content = (String) body.get("content");
    String authorId = (String) body.get("authorId");
    String query = "INSERT INTO comments (post_id, author_id, content) " +
                   "VALUES (" + postId + ", " + authorId + ", '" + content + "')";
    jdbcTemplate.execute(query);
    ...
}
```

Conduct a security review. For each vulnerability you find:
1. Name the vulnerability type
2. Show a specific exploit — what would an attacker submit?
3. Write the fixed version of the code

Find at least 3 distinct vulnerabilities.

# Integration

**Connecting to Psychology — The Curse of Knowledge**

Psychologist Elizabeth Newton's 1990 experiment had "tappers" tap famous songs and predict whether "listeners" could identify them. Tappers predicted 50% recognition; actual recognition was 2.5%. The tappers *knew* the song in their heads — they couldn't imagine not knowing it.

This "Curse of Knowledge" affects security too. Developers who understand SQL injection find it obvious and hard to imagine forgetting. But in the pressure of a deadline, with attention focused on business logic, the security implication of one string concatenation is easily missed.

This is why secure coding requires **systematic checks**, not just knowledge. Code reviews with a security checklist, automated static analysis (SpotBugs, SonarQube), OWASP dependency scanning in CI — these don't replace understanding, but they systematically surface what human attention misses under pressure.

The principle extends beyond security: expert knowledge creates blindness to novice failure modes. Good systems don't rely on everyone always remembering — they encode the rules in tooling that checks automatically.

How might you design your development workflow to catch security issues regardless of individual attention lapses?

# Lore Conclusion

The inscription chamber is secure. Data arrives in one channel; commands are prepared separately. They never mix.

*"Security is not a feature,"* Archmage Veylan says. *"It is the discipline of never assuming that input is what it claims to be. Every input is potentially hostile. Write code that survives hostile input."*

The most dangerous assumption in software development is that users will be honest.
---
