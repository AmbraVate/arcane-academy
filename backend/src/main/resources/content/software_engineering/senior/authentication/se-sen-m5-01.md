---
id: se-sen-m5-01
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m5
moduleTitle: "Module 5: Security"
moduleGlyph: "🔐"
moduleSortOrder: 5
topicSlug: authentication
topicTitle: "Authentication"
topicSortOrder: 1
lesson: authentication
title: "Authentication"
sortOrder: 1
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, design]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Distinguishes authentication (who you are) from authorisation (what you can do)"
    - "Explains why passwords are hashed not encrypted, and names bcrypt"
    - "Describes JWT structure (header.payload.signature) and how it is validated"
    - "Outlines the OAuth2 authorisation code flow"
    - "Explains the Spring Security authentication chain"
  keywords: [authentication, authorisation, bcrypt, JWT, OAuth2, OIDC, session, token, Spring Security, MFA]
  modelAnswer: |
    Authentication: verifying identity (who are you?). Authorisation: determining permissions (what can you do?).

    Passwords are hashed (one-way, slow) not encrypted (reversible) because if the
    database is breached, hashes cannot be reversed. bcrypt includes a salt and is
    intentionally slow (work factor) to resist brute force.

    JWT: three Base64-encoded parts separated by dots:
    header (algorithm) . payload (claims: sub, exp, roles) . signature (HMAC/RSA)
    Validation: verify signature with secret/public key, check exp claim not expired.
    Stateless — server does not store sessions.

    OAuth2 authorisation code flow:
    1. Client redirects user to authorisation server
    2. User authenticates and grants consent
    3. Auth server issues authorisation code to redirect URI
    4. Client exchanges code for access token + refresh token
    5. Client uses access token for API calls

    Spring Security chain: filters chain processes each request. Key filters:
    UsernamePasswordAuthenticationFilter, BearerTokenAuthenticationFilter,
    JwtAuthenticationProvider, SecurityContextHolder.
guidedSteps:
  - id: auth-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A user logs in with a username and password. The system verifies their identity and records that they are authenticated. What happens next if they try to access the admin dashboard — what process determines whether they can?
    inputConfig:
      options:
        - "Authentication again — they must re-enter their password"
        - "Authorisation — the system checks whether the authenticated user has the required permissions"
        - "Encryption — the dashboard is decrypted for authorised users"
        - "Identification — the system looks up who the user is"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Authorisation — the system checks whether the authenticated user has the required permissions"]
      rejectedFeedback: "Authentication answers 'who are you?'; authorisation answers 'are you allowed to do this?'. Once authenticated, the system uses authorisation (roles, permissions, policies) to determine access to specific resources."
    hint: "The user's identity is already confirmed. What question remains?"
    reflectionPrompt: "Authentication and authorisation are distinct steps that are often confused. Authentication always comes first; authorisation uses the authenticated identity to make access decisions."
  - id: auth-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      A JWT (JSON Web Token) consists of three Base64URL-encoded parts separated by dots. From left to right, these are: ___, payload, and signature.
    inputConfig:
      placeholder: "first part name"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["header", "Header"]
      rejectedFeedback: "A JWT is structured as: header.payload.signature. The header specifies the token type and signing algorithm (e.g., HS256). The payload contains claims (sub, exp, iat, roles). The signature is a cryptographic hash of the header and payload, signed with a secret or private key."
    hint: "The first part tells you what type of token it is and how it is signed."
    reflectionPrompt: "Never store sensitive data in a JWT payload — it is Base64-encoded, not encrypted. Anyone who holds the token can decode the payload."
  - id: auth-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A developer proposes storing user passwords in the database using AES-256 encryption so they can be "decrypted if needed for account recovery." Explain why this is a security mistake and what the correct approach is.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [hash, bcrypt, one-way, breach, decrypt, irreversible, password reset, salt]
      rejectedFeedback: "Encrypting passwords is wrong because if the encryption key is compromised (alongside the database breach), all passwords can be decrypted. Password hashing is one-way — even with the hash, the original password cannot be recovered. bcrypt includes a per-password random salt and an intentionally slow work factor to resist brute force. Account recovery should use password reset flows (email link), not password decryption."
    hint: "If the database is breached AND the encryption key is stolen, what happens?"
    reflectionPrompt: "The rule: never store passwords in recoverable form. If a user forgets their password, they reset it — they never retrieve it."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the purpose of a 'work factor' (cost parameter) in bcrypt password hashing?"
    options:
      - "It determines the length of the resulting hash"
      - "It controls the number of salt bytes added to the password"
      - "It makes hashing intentionally slow, increasing the cost of brute-force attacks"
      - "It specifies the encryption algorithm used internally"
    correctIndex: 2
    feedback: "The bcrypt work factor (log2 of iterations) controls how slow the hash computation is. A higher work factor means each brute-force attempt takes longer, making large-scale password cracking attacks computationally expensive."
  - type: MULTIPLE_CHOICE
    question: "In the OAuth2 authorisation code flow, what is the purpose of exchanging the authorisation code for tokens (rather than receiving tokens directly)?"
    options:
      - "Authorisation codes are longer and more secure than tokens"
      - "The exchange happens via back-channel (server-to-server), preventing tokens from appearing in browser history or redirect URIs"
      - "It allows the client to verify the user's identity twice"
      - "Tokens require a signature that authorisation codes do not"
    correctIndex: 1
    feedback: "The code-for-token exchange happens server-to-server (back-channel), keeping tokens out of browser history, logs, and redirect URIs. The authorisation code is short-lived and single-use; the actual tokens never appear in the browser URL."
retrieval:
  recall: "Describe the OAuth2 authorisation code flow in 5 steps and explain why the code-to-token exchange is important."
  explain: "Explain to a junior developer why password hashing with bcrypt is better than encryption, using the 'what happens if the database is stolen?' framing."
  mistakeId:
    code: |
      // Spring Security JWT validation
      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          http.oauth2ResourceServer()
              .jwt()
              .jwtAuthenticationConverter(jwtConverter());
          // No expiry check configured
          return http.build();
      }
    answer: "Spring Security's JWT decoder does validate the signature and standard claims by default, but customisations that disable expiry checking (e.g., a custom NimbusJwtDecoder with validators removed) would allow expired tokens to authenticate indefinitely. Always ensure exp claim validation is active and never disable it in production configuration."
---

# Hook

A gate in the Academy has two guards. The first asks: "Who are you?" — authentication. The second asks: "What are you permitted to enter?" — authorisation. Both guards are essential. A gate with only the first lets anyone who knows their name through. A gate with only the second has no idea who they are letting in.

# Lore Introduction

The Academy's Shield of Identity is the most fundamental ward in the security repertoire. Every enchantment, every spell, every room in the Academy starts with the same question: who is asking? Senior mages understand that identity is not given freely — it must be cryptographically verified — and that what a verified identity is permitted to do is a separate question entirely.

# Core Learning

## Concept Introduction

**Authentication**: verifying who a user is (identity verification)
**Authorisation**: determining what an authenticated user is permitted to do (access control)

These are distinct. A valid identity does not imply any permissions — that is determined by authorisation.

**Authentication methods:**
| Method | Mechanism | Security level |
|---|---|---|
| Password | Knowledge factor | Moderate (brute force risk) |
| MFA | Password + TOTP/SMS/hardware key | High |
| OAuth2/OIDC | Delegated identity via trusted provider | High (password stored with IdP) |
| Client certificate | PKI, mutual TLS | Very high (enterprise) |

## Why It Matters

Authentication is the entry point to your system. Weaknesses here — weak password hashing, predictable tokens, incomplete session invalidation — are catastrophic because they allow attackers to impersonate legitimate users. Understanding the full authentication chain (from credential submission to session/token issue) enables you to build and audit secure authentication correctly.

## Worked Examples

### Password hashing with bcrypt

```java
// Spring Security's PasswordEncoder — always use, never roll your own
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12); // work factor 12: ~250ms to hash
}

// Registration: hash before storing
String hashedPassword = passwordEncoder.encode(rawPassword);
userRepository.save(new User(email, hashedPassword));

// Login: verify without decrypting (one-way comparison)
boolean valid = passwordEncoder.matches(rawPassword, storedHash);
// BCryptPasswordEncoder internally: generates salt, hashes, compares
// The original password is never retrievable from the stored hash
```

### JWT generation and validation

```java
// Spring Security + JJWT library
@Service
public class JwtService {

    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.expiry-hours:24}") private int expiryHours;

    public String generateToken(UserDetails user) {
        return Jwts.builder()
            .setSubject(user.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(Date.from(Instant.now().plusSeconds(expiryHours * 3600L)))
            .claim("roles", user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token) // throws if signature invalid or expired
            .getBody();
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
```

### OAuth2 authorisation code flow (Spring Security)

```java
// application.yml — OAuth2 client configuration
security:
  oauth2:
    client:
      registration:
        google:
          client-id: ${GOOGLE_CLIENT_ID}
          client-secret: ${GOOGLE_CLIENT_SECRET}
          scope: openid, profile, email
          redirect-uri: "{baseUrl}/login/oauth2/code/google"
          authorization-grant-type: authorization_code

// Spring Security config — enable OAuth2 login
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(info -> info.oidcUserService(oidcUserService()))
            .successHandler(customSuccessHandler())
        );
    return http.build();
}
```

**Flow:**
1. User clicks "Login with Google"
2. Spring redirects to `https://accounts.google.com/oauth/authorize?...`
3. User authenticates with Google, grants consent
4. Google redirects to `{app}/login/oauth2/code/google?code=ABC`
5. Spring exchanges code for ID token + access token (back-channel)
6. Spring validates ID token, creates `SecurityContext`

### Spring Security filter chain

```java
// The security filter chain processes every request in order
// Key filters for JWT authentication:

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable()); // stateless API — no CSRF needed
        return http.build();
    }
}

// JWT filter extracts token from Authorization: Bearer <token> header
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req, ...) {
        String token = extractBearerToken(req);
        if (token != null && jwtService.isValid(token)) {
            Claims claims = jwtService.validateToken(token);
            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                    extractAuthorities(claims));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(req, response);
    }
}
```

### MFA (Multi-Factor Authentication) — TOTP

```java
// Google Authenticator uses TOTP (RFC 6238)
// Secret key stored per-user; 6-digit code changes every 30 seconds

@Service
public class TotpService {
    public String generateSecret() {
        return new GoogleAuthenticator().createCredentials().getKey();
    }

    public boolean verifyCode(String secret, int code) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(secret, code); // validates current 30s window ± 1
    }
}
```

## Common Mistakes

1. **Confusing authentication and authorisation in code.** Having a single `@Secured("USER")` annotation that both verifies identity AND grants permissions conflates the two layers. Keep authentication (filter chain) and authorisation (method-level) separate.

2. **Using MD5 or SHA-1 for password hashing.** These are fast, which is exactly wrong for passwords. Use bcrypt, Argon2, or scrypt — all are intentionally slow.

3. **Long-lived JWTs without refresh tokens.** A JWT valid for 30 days cannot be revoked without a token blacklist. Use short-lived access tokens (15 minutes) with refresh tokens.

4. **Storing JWTs in localStorage.** JavaScript-accessible storage is vulnerable to XSS. Use HttpOnly, Secure, SameSite cookies to store tokens.

5. **Not validating JWT claims.** Validating the signature but not checking `exp`, `iss`, or `aud` claims allows expired or misrouted tokens to authenticate.

## Mental Model

Authentication is the passport check — it verifies you are who you claim to be. Authorisation is the visa — it specifies where in the country you are allowed to go after entering. A JWT is a self-contained travel document — the bearer can present it at any gate, and the gate can verify it without calling back to the issuing authority. MFA is a passport plus a biometric scan — two independent factors, both required.

## Mini Summary

- Authentication = identity verification; Authorisation = access control; they are distinct
- Passwords are bcrypt-hashed (one-way, salted, slow) — never encrypted, never stored plain
- JWTs are header.payload.signature; validate signature + exp + iss claims server-side
- OAuth2 authorisation code flow: redirect → user authenticates → code → server exchanges for tokens
- Spring Security filter chain: JWT filter → SecurityContextHolder → method-level authorisation

# Guided Practice Quest

Work through the guided steps to practise distinguishing authentication and authorisation, and tracing the JWT validation flow.

# Solo Practice Quest

Implement a Spring Boot `AuthController` with two endpoints:
1. `POST /auth/register` — hashes the password and stores the user
2. `POST /auth/login` — verifies credentials and returns a signed JWT with roles

Then implement a `JwtAuthFilter` that extracts and validates the JWT from the `Authorization` header and populates the Spring SecurityContext. Include error handling for expired and invalid tokens.

# Integration

**Connecting to Psychology — Trust and Design — UX of Security**

Authentication is fundamentally a trust mechanism, and trust in user interfaces is shaped by psychological principles. Users form mental models of how secure a system is based on signals — the presence of a padlock icon, the requirement for MFA, the wording of error messages. These signals affect both security (users who trust a system are more willing to follow its security guidance) and usability (overly strict authentication creates friction that drives users to circumvent security entirely, such as writing passwords on sticky notes).

Good authentication design applies the principle of least friction for legitimate users while maximising friction for attackers. Single Sign-On (OAuth2/OIDC) reduces friction by letting users authenticate once with a trusted provider. Passwordless authentication (magic links, passkeys) eliminates the weakest link — human-chosen passwords. MFA adds friction proportional to the risk: low-risk operations require only one factor; high-risk operations (account recovery, large transactions) require multiple. The UX designer and the security engineer must collaborate on authentication flows, because a perfectly secure but unusable authentication system will be bypassed. The right design balances these forces by placing security friction where it matters and removing it where it does not.

# Lore Conclusion

The Academy's two-guard gate stands firm. The first guard — authentication — verifies the mage's seal and voiceprint, confirming identity. The second guard — authorisation — consults the scroll of permissions. The JWT rune on the mage's badge glows valid; the expiry inscription has not yet faded. They enter. Behind them, a mage who stole a valid badge from a colleague and tried to enter the archives finds the badge expired — the short-lived token has already been replaced. The system held.

---
