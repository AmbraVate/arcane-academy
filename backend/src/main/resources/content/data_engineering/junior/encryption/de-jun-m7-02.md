---
id: de-jun-m7-02
school: engineering
domainId: data-engineering
tier: JUNIOR
moduleId: de-jun-m7
moduleTitle: "Module 7: Data Security"
moduleGlyph: "🔐"
moduleSortOrder: 7
topicSlug: encryption
topicTitle: "Encryption"
topicSortOrder: 2
lesson: encryption
title: "Encryption"
sortOrder: 2
difficulty: 3
estimatedMinutes: 30
xpReward: 50
practiceType: NONE
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [de-jun-m7-01]
integrationDomains: [mathematics, sciences]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Distinguishes encryption at rest from encryption in transit
    - Explains TDE (Transparent Data Encryption) and its limitations
    - Describes application-level column encryption and when it is needed
    - Explains password hashing vs encryption, and why passwords must be hashed
    - Identifies key management as the critical challenge in encryption
  keywords: [encryption at rest, encryption in transit, TDE, TLS, SSL, column encryption, AES, pgcrypto, password hashing, bcrypt, argon2, key management, IV, salt, symmetric, asymmetric, HSM]
  modelAnswer: |
    Encryption at rest: data is encrypted on disk — if a hard drive or backup is stolen, it's unreadable. TDE (Transparent Data Encryption) is database-level at-rest encryption handled by the storage engine; it protects against physical disk theft but NOT against SQL queries by authorised users. Encryption in transit: TLS/SSL encrypts data between client and database server — prevents network eavesdropping. Application-level column encryption: encrypt specific sensitive columns in application code before storing — even the DBA cannot read the plaintext (stronger than TDE). Passwords must NEVER be encrypted (decryptable) — they must be hashed (one-way) using bcrypt or Argon2 with a per-user salt. Encryption without key management is useless — keys must be stored separately from encrypted data (HSM, secrets manager), rotated regularly, and access-controlled.
guidedSteps:
  - id: de-jun-m7-02-g1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      The Archive database uses TDE (Transparent Data Encryption) for data at rest. A DBA logs in and runs SELECT * FROM members. What does TDE protect against?
    inputConfig:
      options:
        - "TDE prevents the DBA from reading member data in SQL queries"
        - "TDE protects against physical theft of the disk or backup files — SQL queries by authenticated users bypass TDE and see plaintext"
        - "TDE encrypts each row individually so the DBA can only read rows they have been granted access to"
        - "TDE is equivalent to column-level encryption — sensitive columns are always encrypted in query results"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["TDE protects against physical theft of the disk or backup files — SQL queries by authenticated users bypass TDE and see plaintext"]
      rejectedFeedback: "TDE (Transparent Data Encryption) works at the storage layer: the database engine decrypts data pages as they are loaded into memory, and queries operate on plaintext in memory. A DBA running SELECT * FROM members sees plaintext member data — TDE provides no protection against this. What TDE protects against: if someone steals the physical server, hard drive, or backup file, the data files on disk are encrypted and unreadable without the TDE key. This is storage-level protection, not access-level protection. For protecting data from authorised-but-over-privileged users (DBAs who should not see member emails), column-level application encryption is needed — the DBA sees ciphertext even in query results."
    hint: "TDE is a storage-layer protection — what happens to the data when the database engine reads it into memory?"
    reflectionPrompt: "What threat model does TDE address that RLS and role permissions do not?"
  - id: de-jun-m7-02-g2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Passwords must never be stored encrypted (reversible) — they must be stored using a one-way ________ function with a per-user random salt.
    inputConfig:
      placeholder: "hash"
    markingRule:
      matchMode: CONTAINS
      accepted: [hash, hashing, "hash function", "cryptographic hash", bcrypt, argon2, "hashed"]
      rejectedFeedback: "Password hashing is fundamentally different from encryption. Encryption: plaintext → ciphertext (reversible with the key). Hashing: plaintext → digest (not reversible). Password storage must use hashing because: if the database is breached, the attacker gets ciphertext if using encryption (and can decrypt if they get the key), but only the hash if using hashing (cannot reverse to original password). Salting: each password has a unique random value (salt) prepended before hashing — prevents rainbow table attacks where an attacker pre-computes hashes for common passwords. bcrypt and Argon2 are deliberately slow hash functions (cost factor adjustable) — they make brute-force attacks computationally expensive. Never store passwords with MD5 or SHA-256 alone — too fast, no salting built in. Spring Security's BCryptPasswordEncoder: passwordEncoder.encode('plaintext') → '$2a$10$...'. passwordEncoder.matches('input', storedHash) → true/false."
    hint: "This process converts passwords to a form that cannot be reversed back to the original."
    reflectionPrompt: "Why is a fast hash (MD5, SHA-1) a bad choice for password storage even if combined with a salt?"
  - id: de-jun-m7-02-g3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain why encrypting a column in the application layer (before storing to the database) provides stronger protection for that column than TDE or role-based access control alone.
    inputConfig:
      minWords: 30
    markingRule:
      matchMode: CONTAINS
      accepted: [DBA, plaintext, ciphertext, key, application, storage, breach, SQL, query, even, privilege, bypass]
      rejectedFeedback: "Application-layer column encryption: the application encrypts the sensitive value before it reaches the database. The database stores ciphertext. Even a DBA running SELECT * FROM members sees the ciphertext: 'aNDf8aKj2l...' instead of 'alice@example.com'. The encryption key lives in the application (key management service, HSM, secrets manager) — not accessible by the database or DBA. TDE only helps if the disk is stolen — any SQL query by an authorised user (including a compromised DBA account) sees plaintext with TDE. Role-based access control can be bypassed by privileged accounts. Application-layer encryption means: even with full database access, the attacker needs the application's encryption key to read the data. The key and the ciphertext are stored separately — an attacker needs both. Downside: encrypted columns cannot be searched or indexed efficiently."
    hint: "What happens when a DBA runs SELECT on a column that was encrypted by the application before INSERT?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "When configuring a database connection, TLS (Transport Layer Security) protects against:"
    options:
      - "Unauthorised users logging into the database"
      - "SQL injection attacks in application queries"
      - "Network eavesdropping — someone intercepting the data packets between application and database server"
      - "Disk-level data exposure if the server is physically stolen"
    correctIndex: 2
    feedback: "TLS encrypts the network channel between the application and the database server. Without TLS, all data — queries and results including plaintext passwords, emails, and sensitive data — travels in cleartext over the network. Anyone with network access (e.g. on the same cloud subnet, a compromised router) can read it. With TLS: all traffic is encrypted in transit; an eavesdropper sees only encrypted bytes. PostgreSQL: ssl = on in postgresql.conf, sslmode = verify-full in the connection string (verify-full validates the server certificate, preventing man-in-the-middle attacks). Always use TLS for connections that cross network boundaries — even within a private cloud VPC. TLS does NOT prevent authorised-user access, SQL injection, or at-rest data exposure."
  - type: MULTIPLE_CHOICE
    question: "Which statement about encryption key management is correct?"
    options:
      - "Encryption keys should be stored in the same database as the encrypted data for convenience"
      - "Keys stored with the encrypted data provide no security — an attacker who accesses the database gets both data and key"
      - "Encryption keys should be rotated every 10 years to maintain performance"
      - "Application-layer encryption does not require key management"
    correctIndex: 1
    feedback: "Key management is the hardest part of encryption. The cardinal rule: never store the encryption key in the same place as the encrypted data. If an attacker breaches the database and finds both ciphertext and the decryption key in the same storage, encryption provides no protection. Keys must be stored separately: HSM (Hardware Security Module — dedicated hardware key store), cloud KMS (AWS KMS, Azure Key Vault, GCP KMS), or application-side secrets manager (HashiCorp Vault). Key rotation: periodically generate a new key, re-encrypt existing data, retire the old key. Most cloud KMS services handle automatic rotation. For database column encryption in Java: use JVM-managed keys via Java Crypto API, or delegate to cloud KMS (encrypt with KMS key, store ciphertext in DB, decrypt via KMS API at read time)."
retrieval:
  recall: "List three distinct threats that each of these protections address: (1) TLS in transit, (2) TDE at rest, (3) application-layer column encryption, (4) password hashing. Be specific about what each does and does NOT protect against."
  explain: "Explain why bcrypt is preferred over SHA-256 for password hashing. Include the concepts of work factor, salting, and the specific attack it defends against."
  mistakeId:
    code: |
      @Entity
      public class Member {
          @Id private Long id;
          private String email;
          
          // Storing password encrypted (reversible)
          @Column(name = "password_hash")
          private String encryptedPassword;  // AES-256 encrypted
          
          public void setPassword(String plaintext) {
              this.encryptedPassword = aesEncrypt(plaintext, SECRET_KEY);
          }
          
          public boolean checkPassword(String input) {
              return aesDecrypt(encryptedPassword, SECRET_KEY).equals(input);
          }
      }
    answer: "Using reversible encryption (AES) for passwords is critically insecure. Problems: (1) If the database is breached, attackers get the ciphertext. If they also get SECRET_KEY (from source code, environment variable leak, memory dump), they can decrypt all passwords. (2) SECRET_KEY in application code or environment is a single point of failure — key leakage = all passwords exposed. (3) If SECRET_KEY must be rotated, all stored passwords must be re-encrypted — operational complexity. (4) Encryption implies the password is recoverable — the database operator can read all passwords. Fix: use bcrypt or Argon2 (one-way hashing): passwordEncoder = new BCryptPasswordEncoder(12); // work factor 12 -- store = passwordEncoder.encode(plaintext); -- verify = passwordEncoder.matches(input, storedHash). BCryptPasswordEncoder embeds a random salt in the stored hash — no separate salt management needed. Hashing is irreversible — even the DBA cannot recover the original password."
---

# Hook

Access control determines who can query data. Encryption determines what they can read when they do. These two defences work at different layers: a database breach through a compromised credential exposes all unencrypted data that credential can access. Proper encryption means that even data reached by an attacker is unreadable without the key — a critical additional layer.

# Lore Introduction

"The City Library's breach continued," the Senior Archivist said. "After the initial SQL injection, the attacker extracted the members table. Emails, phone numbers, addresses — all in plaintext. Six thousand members notified." The Junior Engineer looked grim. "We fixed the SQL injection risk with prepared statements. But if someone bypassed access control—" The Senior Archivist nodded. "Our member data would be readable. Encryption is the second layer. Protect data in transit with TLS. Protect data at rest with encryption. And for the most sensitive columns — email, payment data — encrypt at the application level so that even a DBA running SELECT sees only ciphertext." She paused. "And passwords. Passwords are never encrypted. They are hashed. One-way. No recovery. If an attacker gets the hash, they cannot reverse it."

# Core Learning

## Concept Introduction

### Encryption in Transit (TLS)

```sql
-- Enforce TLS for database connections
-- postgresql.conf:
-- ssl = on
-- ssl_cert_file = 'server.crt'
-- ssl_key_file = 'server.key'

-- pg_hba.conf: require SSL for all remote connections
-- hostssl  all  all  0.0.0.0/0  scram-sha-256

-- Application connection string (Spring Boot):
-- spring.datasource.url=jdbc:postgresql://db:5432/archive?sslmode=verify-full
-- spring.datasource.ssl-root-cert=/path/to/ca.crt
-- sslmode options:
--   disable   — no TLS (never use in production)
--   require   — TLS but no certificate verification (vulnerable to MITM)
--   verify-ca — verify server cert is signed by known CA
--   verify-full — verify cert AND hostname match (strongest)

-- Verify connections are using SSL:
SELECT pg_ssl.pid, usename, ssl, version, cipher, bits
FROM pg_stat_ssl
JOIN pg_stat_activity ON pg_ssl.pid = pg_stat_activity.pid;
```

### Encryption at Rest (TDE and pgcrypto)

```sql
-- TDE: database-level (file system / storage encryption)
-- PostgreSQL does not have native TDE; use:
-- - OS-level: LUKS (Linux), BitLocker (Windows)
-- - Cloud: Amazon RDS Encryption, Azure Transparent Data Encryption
-- - PostgreSQL extension: pg_tde (added PostgreSQL 17)

-- pgcrypto: application-level column encryption
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Encrypt a sensitive column at INSERT time:
INSERT INTO members (full_name, email, email_encrypted)
VALUES (
    'Alice Selvaris',
    pgp_sym_encrypt('alice@example.com', current_setting('app.encryption_key')),
    pgp_sym_encrypt('alice@example.com', current_setting('app.encryption_key'))
);
-- The database stores ciphertext in email_encrypted
-- Only the application with the key can decrypt:
SELECT pgp_sym_decrypt(email_encrypted::bytea, current_setting('app.encryption_key'))
FROM members WHERE id = 42;

-- Limitation: encrypted columns cannot be indexed or searched efficiently
-- Solution: store a one-way hash of searchable values alongside ciphertext:
ALTER TABLE members ADD COLUMN email_hash VARCHAR(64);
UPDATE members SET email_hash = encode(digest(LOWER(email), 'sha256'), 'hex');
-- Search by: WHERE email_hash = encode(digest(LOWER(:email), 'sha256'), 'hex')
-- (SHA-256 for lookup only — not for passwords; emails are not secret lookup keys)
```

### Application-Layer Column Encryption

```java
// Java application encrypts before storing, decrypts after reading
@Service
public class MemberEncryptionService {

    // Key retrieved from secrets manager (AWS KMS, HashiCorp Vault) — NOT hardcoded
    @Value("${app.encryption.key}")
    private String encryptionKey;

    public String encrypt(String plaintext) {
        // AES-256-GCM: authenticated encryption (prevents tampering)
        SecretKeySpec key = new SecretKeySpec(
            Base64.decode(encryptionKey), "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);  // random IV per encryption
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes());
        // Store IV + ciphertext (IV needed for decryption)
        return Base64.encode(concat(iv, ciphertext));
    }

    public String decrypt(String stored) {
        byte[] decoded = Base64.decode(stored);
        byte[] iv = Arrays.copyOfRange(decoded, 0, 12);
        byte[] ciphertext = Arrays.copyOfRange(decoded, 12, decoded.length);
        SecretKeySpec key = new SecretKeySpec(Base64.decode(encryptionKey), "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));
        return new String(cipher.doFinal(ciphertext));
    }
}
```

### Password Hashing

```java
// NEVER encrypt passwords — ALWAYS hash them (one-way)
// Spring Security BCryptPasswordEncoder:
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);   // work factor 12 (~250ms per hash)
}

// Setting a password:
member.setPasswordHash(passwordEncoder.encode(plaintext));
// Stores: "$2a$12$randomsalt22chars.hashedresult31chars"
// The salt is embedded in the stored value — no separate salt management

// Verifying a password:
boolean valid = passwordEncoder.matches(inputPassword, member.getPasswordHash());
// Internally: extract salt from stored hash, hash input with same salt, compare

// NEVER do this:
member.setPassword(plaintext);          // stores plaintext — catastrophic
member.setPassword(md5(plaintext));     // MD5: too fast, broken, rainbow tables
member.setPassword(sha256(plaintext));  // SHA-256: too fast, no salt by default
member.setPassword(aes.encrypt(plaintext, key));  // encrypted = reversible = wrong

// Work factor: BCryptPasswordEncoder(n) means 2^n iterations
// Factor 10: ~100ms. Factor 12: ~250ms. Factor 14: ~1000ms
// Choose so hashing takes ~100-500ms — too slow for brute force, OK for login
```

### Key Management

```java
// NEVER hardcode encryption keys:
private static final String KEY = "mySecretKey123!";  // WRONG — in source code

// NEVER store key in same database as encrypted data:
-- encrypted_data_key column in members table  -- WRONG

// Correct: retrieve from secrets manager
// AWS KMS + Spring Cloud AWS:
@Value("${app.secrets.encryption-key}")  // from AWS Secrets Manager at startup
private String encryptionKey;

// Or: use envelope encryption
// Data Encryption Key (DEK): generated per record, stored alongside ciphertext
// Key Encryption Key (KEK): stored in KMS, never leaves HSM
// To decrypt: call KMS to decrypt the DEK, use DEK to decrypt data
// If DEK is compromised: rotate just that DEK without re-encrypting all records
```

## Common Mistakes

- **Storing encryption key alongside encrypted data**: if an attacker gets both the key and the ciphertext, encryption provides zero protection. Keys must live in a separate system (secrets manager, HSM).
- **Using the same IV (Initialization Vector) for multiple encryptions**: AES-GCM requires a unique random IV per encryption. Reusing an IV with the same key allows an attacker to recover the key. Always generate a fresh random IV per encryption operation.
- **Encrypting passwords instead of hashing**: passwords must be stored as one-way hashes (bcrypt, Argon2). Encrypted passwords can be decrypted if the key is obtained. Hashed passwords cannot be reversed.
- **Using TLS but with `sslmode=disable` or `sslmode=require` (no certificate verification)**: `require` uses TLS encryption but doesn't validate the server certificate — vulnerable to man-in-the-middle attacks. Always use `verify-full` in production.

## Mental Model

Think of encryption as a locked box. TLS in transit: the conversation between application and database happens inside a locked tunnel — no one outside can read it. TDE at rest: the database files on disk are in a locked vault — stolen files are unreadable. Application-layer encryption: individual items are locked in smaller boxes before going into the vault — even someone with vault access cannot open the individual boxes without the right key. Password hashing is different: it's a one-way hash, like a fingerprint — you can verify a match, but you cannot recover the original from the fingerprint.

## Mini Summary

- ✔ TLS in transit: encrypts network traffic between application and database
- ✔ TDE at rest: encrypts database files on disk — protects against physical theft
- ✔ Application-layer column encryption: DBA sees ciphertext; protects against privileged access
- ✔ pgcrypto: PostgreSQL extension for column-level encryption/decryption in SQL
- ✔ Password hashing: bcrypt or Argon2, one-way with work factor and salt — never reversible
- ✔ Key management: keys stored separately from data, rotated periodically, accessed via secrets manager
- ✔ Always use `sslmode=verify-full` for production database connections

# Guided Practice Quest

Work through the guided steps to configure TLS on the Archive's database connection string, implement a `MemberEncryptionService` that encrypts email before storing and decrypts after reading, and replace a plain-text password store with BCrypt hashing with work factor 12.

# Solo Practice Quest

Design and implement the encryption layer for the Archive system. Tasks: (1) Identify five fields in the Archive database that require encryption (PII, financial) and classify each as: encrypted at rest (TDE sufficient), application-layer encrypted (column encryption), or hashed (passwords); (2) Write the Spring Boot configuration for TLS database connections with `sslmode=verify-full`; (3) Implement a column encryption service using AES-256-GCM — include proper IV generation, key retrieval from environment variable, and the encrypt/decrypt methods; (4) Write the Member entity with BCrypt password hashing — show the field, the setter that hashes, and the verification method; (5) Design the key management strategy: where keys are stored, how they are accessed at startup, and what happens during key rotation for already-encrypted data; (6) Write a database migration that adds `email_encrypted` column, migrates existing email data to encrypted form, then drops the unencrypted email column.

# Integration

**Mathematics**: Modern symmetric encryption relies on computational hardness — the difficulty of solving specific mathematical problems. AES (Advanced Encryption Standard) operates on 128-bit blocks using a 256-bit key. Security rests on the fact that without the key, brute-forcing the keyspace (2^256 ≈ 10^77 possible keys) is computationally infeasible. At 10^18 AES operations per second (current ASIC speed), exhausting the 256-bit keyspace would take 10^59 years — vastly longer than the age of the universe (1.38 × 10^10 years). BCrypt's work factor adds iterations: BCrypt(cost=12) performs 2^12 = 4,096 iterations. An attacker testing 10^9 passwords/second against SHA-256 cracks an unsalted hash in seconds. Against BCrypt(12) at 250ms/hash, the same attacker can test only 4 passwords/second — making 8-character password brute-force take thousands of years instead of seconds.

**Sciences (Chemistry — Irreversible Reactions)**: Password hashing is analogous to an irreversible chemical reaction. Burning paper (oxidation): paper + oxygen → CO₂ + H₂O + ash. The reaction proceeds in one direction — you cannot recover the paper from ash. The hash function is similarly one-way: plaintext → hash (irreversible). In cryptographic contexts, a good hash function satisfies: (1) Preimage resistance — given hash H, cannot find input M such that hash(M) = H. (2) Collision resistance — cannot find two inputs M₁ ≠ M₂ such that hash(M₁) = hash(M₂). These properties mirror irreversibility in chemistry: just as you cannot reconstruct a molecule from combustion products, you cannot reconstruct a password from its hash. Salting adds a unique reagent to each reaction — even if two users have the same password, they undergo different reactions (different salts) and produce different products (different hashes), defeating rainbow table attacks.

# Lore Conclusion

"TLS configured," the Junior Engineer reported. "Database connections use verify-full. Member email encrypted with AES-256-GCM before storage — only the application can decrypt. Passwords migrated to BCrypt with work factor 12." The Senior Archivist reviewed the implementation. "Key management?" The Junior showed the configuration. "Retrieved from the secrets manager at startup. Not in source code, not in the database." The Senior Archivist nodded. "Now if someone steals a database backup, they have ciphertext they cannot decrypt. If someone intercepts network traffic, they have encrypted bytes they cannot read. If someone dumps the members table as a DBA, they see encrypted emails and BCrypt hashes — not plaintext." She paused. "The third layer: auditing. Knowing that encryption and access control are in place is not enough — you must know when someone tries to bypass them."

---
