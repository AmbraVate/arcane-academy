---
id: se-sen-m5-02
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m5
moduleTitle: "Module 5: Security"
moduleGlyph: "🔐"
moduleSortOrder: 5
topicSlug: authorisation
topicTitle: "Authorisation"
topicSortOrder: 2
lesson: authorisation
title: "Authorisation"
sortOrder: 2
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [authentication]
integrationDomains: [philosophy, design]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Distinguishes authentication from authorisation clearly"
    - "Explains RBAC vs ABAC with an example of each"
    - "Demonstrates @PreAuthorize usage in Spring Security"
    - "Describes the principle of least privilege and its implementation"
    - "Identifies a real-world authorisation bug (privilege escalation, IDOR)"
  keywords: [role, permission, rbac, abac, preauthorize, least, privilege, resource, access, control]
  modelAnswer: |
    // RBAC with Spring Security
    @RestController
    @RequestMapping("/api/admin")
    public class AdminController {

        @GetMapping("/users")
        @PreAuthorize("hasRole('ADMIN')")
        public List<UserDto> getAllUsers() { ... }

        @DeleteMapping("/users/{id}")
        @PreAuthorize("hasRole('ADMIN') or hasAuthority('USER_DELETE')")
        public void deleteUser(@PathVariable Long id) { ... }
    }

    // ABAC: resource-level check
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public UserProfile getProfile(@PathVariable Long userId) { ... }
guidedSteps:
  - id: authz-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A user logs in with valid credentials. They then try to access `/api/admin/reports`.
      Their token is valid but they don't have the ADMIN role. Which concept governs this?
    inputConfig:
      options:
        - "Authentication — their identity is not verified"
        - "Authorisation — their identity is verified but they lack the required permission"
        - "Encryption — their data is not secured"
        - "Session management — their session has expired"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Authorisation — their identity is verified but they lack the required permission"]
      rejectedFeedback: "Authentication answers 'who are you?' (verified via login). Authorisation answers 'are you allowed to do this?' The user's identity is confirmed; what's in question is their permission level."
    hint: "Authentication = identity. Authorisation = permission. Which question is being asked here?"
    reflectionPrompt: "A common mistake is conflating these. Successful authentication does NOT imply authorisation. A valid token only proves who you are — not what you're allowed to do."
  - id: authz-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In Spring Security, which annotation restricts a method to users with the 'ADMIN' role?

      ```java
      @GetMapping("/admin/dashboard")
      @___(\"hasRole('ADMIN')\")
      public DashboardData getDashboard() { ... }
      ```
    inputConfig:
      placeholder: "Spring Security annotation"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["@PreAuthorize", "PreAuthorize"]
      rejectedFeedback: "`@PreAuthorize` evaluates a Spring Expression Language (SpEL) expression before the method runs. If the expression returns false, Spring throws `AccessDeniedException` (HTTP 403)."
    hint: "The annotation evaluates its condition *before* the method executes."
    reflectionPrompt: "`@PreAuthorize` supports complex expressions: `hasRole('ADMIN')`, `hasAuthority('READ_REPORTS')`, `#userId == authentication.principal.id`. The last form is Attribute-Based Access Control — checking a resource attribute against the authenticated user."
  - id: authz-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the Principle of Least Privilege and describe how it applies to both user roles in an application and to service-to-service permissions in a microservices architecture.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [minimum, least, privilege, necessary, role, permission, service, scope, access, restrict]
      rejectedFeedback: "Least Privilege: grant only the permissions required to perform the task, nothing more. For users: a 'viewer' role should not have write permissions. For services: a reporting service should only have read access to the database, not write. This limits the blast radius if a principal is compromised."
    hint: "Think about what 'minimum necessary access' means and why it reduces risk."
    reflectionPrompt: "Least privilege is a fundamental security principle. Over-permissioned services and users are consistently among the most exploited attack vectors. The more permissions an entity has, the more damage a compromised entity can do."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the difference between RBAC and ABAC?"
    options:
      - "RBAC is for users; ABAC is for services"
      - "RBAC grants permissions based on roles; ABAC grants permissions based on attributes of the user, resource, and environment"
      - "RBAC uses tokens; ABAC uses sessions"
      - "RBAC is older and ABAC replaced it"
    correctIndex: 1
    feedback: "RBAC (Role-Based): ADMIN can delete users, USER cannot. ABAC (Attribute-Based): a user can edit their OWN document but not others'. ABAC is more expressive and fine-grained; RBAC is simpler to implement and reason about."
  - type: MULTIPLE_CHOICE
    question: "What HTTP status code should a server return when an authenticated user accesses a resource they don't have permission to access?"
    options:
      - "401 Unauthorized"
      - "403 Forbidden"
      - "404 Not Found"
      - "400 Bad Request"
    correctIndex: 1
    feedback: "403 Forbidden = authenticated but not authorised. 401 Unauthorized = not authenticated (or invalid credentials). Using 404 to hide existence of a resource is sometimes appropriate for sensitive data (information hiding), but 403 is the semantic default for authorisation failures."

retrieval:
  recall: "What is the difference between authentication and authorisation? Give an example of each."
  explain: "Explain to a junior developer why you should return 403 (not 401) when an authenticated user tries to access an admin endpoint without the admin role."
  mistakeId:
    code: |
      @GetMapping("/api/invoices/{invoiceId}")
      public Invoice getInvoice(@PathVariable Long invoiceId) {
          // Just check they're logged in — no resource-level check
          return invoiceRepository.findById(invoiceId).orElseThrow();
      }
    answer: "This is an Insecure Direct Object Reference (IDOR) vulnerability. Any authenticated user can access any invoice by guessing the ID. The endpoint must verify the authenticated user owns (or has permission to view) the requested invoice: `if (!invoice.getUserId().equals(currentUserId)) throw new AccessDeniedException(...)`."
---

# Hook

Authentication says who you are. Authorisation says what you're allowed to do.

Getting authentication right and authorisation wrong is one of the most common — and most consequential — security mistakes in web applications. Broken Access Control has been the #1 OWASP vulnerability for years. It's not because developers don't understand authorisation — it's because access control checks are easy to forget on individual endpoints.

> Have you ever seen an endpoint that authenticated the user but didn't check whether they should have access to the specific resource they requested?

# Lore Introduction

The Academy's vault requires two enchantments to enter. The first confirms your identity: your resonance signature is yours alone. The second checks your clearance level: not everyone with a confirmed identity may access the restricted archives.

An apprentice with a perfectly valid identity rune who lacks clearance will hear the vault refuse them. *"Identification is not authorisation,"* the vault guardian intones. *"You have proven who you are. Now prove you are permitted."*

# Core Learning

## Concept Introduction

**Authorisation** determines what an authenticated user is allowed to do.

**Two primary models:**

**RBAC (Role-Based Access Control):**
```java
@PreAuthorize("hasRole('ADMIN')")
public void deleteUser(Long id) { ... }

@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
public void suspendUser(Long id) { ... }
```
Roles: ADMIN, EDITOR, VIEWER. Permissions flow from role membership.

**ABAC (Attribute-Based Access Control):**
```java
// User can only access their own profile (unless ADMIN)
@PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
public UserProfile getProfile(@PathVariable Long userId) { ... }
```
Decisions based on attributes: who is the user, what resource is it, what environment.

## Why It Matters

Broken Access Control (OWASP #1, 2021) includes:
- Accessing another user's data by changing an ID (IDOR)
- Accessing admin endpoints without admin role
- Elevation of privilege by modifying JWT claims
- Missing method-level security on sensitive operations

Access control bugs are often silent — no error, just wrong data returned.

## Worked Examples

**Method-level security:**
```java
@Configuration
@EnableMethodSecurity
public class SecurityConfig { }

// Now @PreAuthorize works on any @Service or @Controller method
```

**Resource ownership check:**
```java
@GetMapping("/orders/{orderId}")
public Order getOrder(@PathVariable Long orderId,
                      @AuthenticationPrincipal UserDetails user) {
    Order order = orderService.findById(orderId);
    if (!order.getUserId().equals(((AppUser) user).getId())) {
        throw new AccessDeniedException("Not your order");
    }
    return order;
}
```

**JWT role claims used in authorisation:**
```java
// JWT claims: { "sub": "42", "roles": ["USER"] }
// Spring Security reads roles from claims and populates SecurityContext
// @PreAuthorize("hasRole('USER')") then checks against SecurityContext
```

## Common Mistakes

- **Missing resource-level checks** — validating role but not ownership (IDOR vulnerability).
- **Frontend-only restrictions** — hiding the "Admin" button in the UI but not checking the role on the backend API.
- **Over-privileged roles** — "ADMIN" role that can do everything; breaks least privilege.
- **Forgetting method security** — `@EnableMethodSecurity` must be added for `@PreAuthorize` to take effect.
- **Trusting user-supplied IDs without verification** — always verify the authenticated user owns the requested resource.

## Mental Model

Authorisation is a **bouncer with a list**. Authentication is showing your ID (you are who you claim). Authorisation is the bouncer checking the list (are you on the VIP list for this area?). Knowing your name (authentication) doesn't automatically get you backstage (authorisation).

## Mini Summary

- ✔ Authorisation = what you can do; Authentication = who you are
- ✔ RBAC: role-based coarse-grained access; ABAC: attribute-based fine-grained access
- ✔ `@PreAuthorize` + Spring Security enables method-level authorisation
- ✔ Always check resource ownership, not just role (prevents IDOR)
- ✔ Principle of Least Privilege: grant only what is needed, nothing more

# Guided Practice Quest

**The Vault Guardian**

The Academy's vault has multiple access tiers. Configure Spring Security authorisation rules to enforce them correctly.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

A multi-tenant SaaS application has three user types:
- **SUPER_ADMIN**: can access any tenant's data
- **TENANT_ADMIN**: can manage users within their own tenant only
- **USER**: can only access their own profile and data

Design the authorisation model for these five endpoints:
1. `GET /api/tenants` — list all tenants
2. `GET /api/tenants/{tenantId}/users` — list users in a tenant
3. `POST /api/tenants/{tenantId}/users` — create a user in a tenant
4. `PUT /api/users/{userId}/profile` — update a user's profile
5. `DELETE /api/users/{userId}` — delete a user

For each endpoint: specify which roles can access it and what additional attribute check (if any) is needed beyond the role. Then explain which of your rules prevent IDOR vulnerabilities.

# Integration

**Connecting to Philosophy — Rights and Permissions**

Political philosopher John Locke distinguished between *natural rights* (inherent, not granted by anyone) and *civil rights* (granted by a social contract for mutual benefit). In software, authorisation is a model of *civil rights* — no permission is inherent; all are explicitly granted.

This creates interesting edge cases. When a new endpoint is added, it has no authorisation rules — it's "open by default." This is the **allow-by-default** failure mode, the most dangerous in security. The alternative — **deny-by-default** (reject any request not explicitly permitted) — is harder to build but far safer.

Spring Security's `@EnableMethodSecurity` with a default deny posture forces developers to explicitly grant access with `@PreAuthorize`. Any method without an annotation fails closed (returns 403). This embeds the deny-by-default principle architecturally — the system doesn't trust you until you've proven permission, rather than trusting you until you're caught.

Locke's social contract also suggests that permissions should be granted for a *reason* and with *accountability*. Audit logging — recording who did what and when — is the accountability layer of an authorisation system.

How does the design of your permission system reflect a philosophy of trust?

# Lore Conclusion

The vault's second gate is correctly enchanted. Identity is confirmed. Clearance is checked. Resources are protected even from authenticated users who lack ownership.

*"Authentication without authorisation is an unlocked door with a peephole,"* Archmage Veylan says. *"You know who is knocking. But you have not decided whether to let them in."*

Access control is not a feature added at the end. It is a discipline built into every endpoint, every method, every data access. Build it in from the start.
---
