---
id: se-sen-m5-03
school: engineering
domainId: software_engineering
tier: SENIOR
moduleId: se-sen-m5
moduleTitle: "Module 5: Security"
moduleGlyph: "🔐"
moduleSortOrder: 5
topicSlug: encryption_basics
topicTitle: "Encryption Basics"
topicSortOrder: 3
lesson: encryption_basics
title: "Encryption Basics"
sortOrder: 3
difficulty: 4
estimatedMinutes: 30
xpReward: 60
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [authorisation]
integrationDomains: [mathematics, history]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Distinguishes symmetric from asymmetric encryption with examples"
    - "Explains what TLS provides and how the handshake works at a high level"
    - "Describes the difference between encryption at rest and in transit"
    - "Names at least two common algorithms (AES, RSA, TLS 1.3)"
    - "Articulates why 'don't roll your own crypto' is a critical principle"
  keywords: [symmetric, asymmetric, aes, rsa, tls, certificate, key, encrypt, decrypt, handshake]
  modelAnswer: |
    Symmetric encryption (AES): same key encrypts and decrypts.
    Fast, used for bulk data. Challenge: key distribution.

    Asymmetric encryption (RSA): public key encrypts, private key decrypts.
    Slow, used for key exchange and signatures. Public key is shareable.

    TLS uses both: asymmetric for the handshake (exchange a symmetric key),
    symmetric for the session (fast bulk encryption).

    At rest: encrypt database fields (AES-256), disk encryption.
    In transit: HTTPS/TLS — never transmit sensitive data over plain HTTP.

    Don't roll your own: cryptographic implementations have subtle flaws
    that only years of expert scrutiny reveal. Use battle-tested libraries
    (Java Cryptography Architecture, Bouncy Castle, not homebrew).
guidedSteps:
  - id: enc-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      HTTPS uses TLS to secure HTTP connections. Which of the following best describes
      what TLS provides?
    inputConfig:
      options:
        - "It compresses data to make transfers faster"
        - "It encrypts the data in transit and authenticates the server's identity"
        - "It stores passwords securely on the server"
        - "It prevents SQL injection attacks"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["It encrypts the data in transit and authenticates the server's identity"]
      rejectedFeedback: "TLS provides: (1) **confidentiality** — the data is encrypted and cannot be read by eavesdroppers; (2) **integrity** — the data cannot be tampered with in transit; (3) **authentication** — the server's identity is verified via its certificate."
    hint: "TLS does two main things. One is obvious (encryption). The other relates to the certificate."
    reflectionPrompt: "Certificate authentication prevents man-in-the-middle attacks: your browser verifies the server is actually who it claims to be via its certificate, signed by a trusted Certificate Authority."
  - id: enc-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Which encryption algorithm uses the same key for both encrypting and decrypting data,
      making it fast for bulk data but requiring secure key distribution?
    inputConfig:
      placeholder: "algorithm type or name"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["symmetric", "AES", "symmetric encryption", "aes"]
      rejectedFeedback: "**Symmetric** encryption (e.g. AES-256) uses the same key for encryption and decryption. It's fast — suitable for large volumes of data. The challenge: both parties need the same key, so securely distributing the key is the core problem symmetric encryption doesn't solve alone."
    hint: "Think: same key both ways. Opposite of asymmetric."
    reflectionPrompt: "AES (Advanced Encryption Standard) with 256-bit keys is the standard for bulk data encryption at rest and in TLS sessions. RSA is too slow for bulk use — it's used to exchange the AES key, then AES does the heavy lifting."
  - id: enc-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Why is it dangerous to implement your own encryption algorithm instead of using a well-known library? Be specific about where the risk lies.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [subtle, timing, padding, oracle, flaw, expert, review, tested, vulnerability, implementation]
      rejectedFeedback: "Cryptographic algorithms have subtle implementation vulnerabilities that take years of expert scrutiny to find. Examples: timing side-channels (comparing bytes one at a time leaks information via response time), padding oracle attacks (error messages reveal plaintext), IV reuse vulnerabilities. Even correct algorithm logic can be broken by flawed implementation details."
    hint: "Even if the mathematical algorithm is correct, what can go wrong in its implementation?"
    reflectionPrompt: "The principle 'Don't Roll Your Own Crypto' is not about distrust of your mathematics — it's recognising that cryptography has subtle failure modes that only emerge under adversarial analysis that your homebrew implementation will never receive."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In asymmetric encryption, what is the public key used for?"
    options:
      - "Decrypting data sent to you"
      - "Encrypting data to send to you (or verifying signatures)"
      - "Generating the symmetric session key"
      - "Authenticating the user's identity"
    correctIndex: 1
    feedback: "The public key encrypts data (or verifies a signature). Only the corresponding private key can decrypt it (or create the signature). The public key is shared openly — anyone can encrypt a message *to* you, but only you can decrypt it."
  - type: MULTIPLE_CHOICE
    question: "Sensitive data in a database should be protected by which of the following?"
    options:
      - "HTTPS on the API layer — that's sufficient"
      - "Encryption at rest (e.g., AES-256 on sensitive fields or full disk encryption)"
      - "Storing data as hashes"
      - "Using a private database with no public access"
    correctIndex: 1
    feedback: "Encryption in transit (TLS/HTTPS) protects data moving between client and server. Encryption at rest protects data stored on disk. Both are needed. A data breach (stolen disk, database dump) exposes unencrypted at-rest data regardless of TLS."

retrieval:
  recall: "What are the differences between symmetric and asymmetric encryption? When is each used in TLS?"
  explain: "Explain to a non-technical colleague what HTTPS does and why the padlock in the browser matters."
  mistakeId:
    code: |
      public String encrypt(String data, String password) {
          // XOR each character with the password character (cycling)
          StringBuilder result = new StringBuilder();
          for (int i = 0; i < data.length(); i++) {
              result.append((char)(data.charAt(i) ^ password.charAt(i % password.length())));
          }
          return result.toString();
      }
    answer: "This is a homebrew XOR cipher — cryptographically broken. XOR ciphers are trivially defeated if any plaintext is known (known-plaintext attack). It has no authentication, no IV, no key derivation, and is vulnerable to frequency analysis. Use AES from `javax.crypto` or a library like Bouncy Castle. Never implement encryption algorithms yourself."
---

# Hook

Your application stores credit card numbers. They're in the database, in the column `card_number`, as plain text. You think it's fine because the database is "private" and requires authentication.

Then your database is breached. The attacker has a dump of every row. Every card number is immediately readable.

Encryption — both at rest and in transit — is not optional for sensitive data. It's the last line of defence when access controls fail. And access controls always eventually fail.

> What sensitive data does a system you work on (or have worked on) store? Is it encrypted at rest?

# Lore Introduction

The Academy's most sensitive scrolls are stored in a cipher — not to hide them from authorised readers, but to protect them if the vault is ever breached. A thief who steals an encrypted scroll gains nothing but meaningless characters.

*"Two seals protect these scrolls,"* Archmage Veylan explains. *"The vault seal prevents unauthorised access. The cipher seal protects the content if the vault is ever bypassed. Neither seal alone is sufficient."*

# Core Learning

## Concept Introduction

**Symmetric Encryption** — same key encrypts and decrypts:
```java
// AES-256 encryption (simplified)
SecretKey key = generateAESKey(); // 256-bit key
Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
byte[] encrypted = cipher.doFinal(plaintext.getBytes());
```
- Fast, suitable for large data
- Challenge: both parties need the same key

**Asymmetric Encryption** — public key encrypts, private key decrypts:
```
Alice's public key → anyone can encrypt a message TO Alice
Alice's private key → only Alice can decrypt it
```
- RSA, elliptic curve
- Slow — used for key exchange, digital signatures
- Public key is freely distributed

**TLS uses both:**
1. Asymmetric handshake (exchange symmetric session key)
2. Symmetric encryption for the session (fast bulk data)

## Why It Matters

- **In transit**: plain HTTP is visible to any network observer. HTTPS/TLS encrypts all data between client and server.
- **At rest**: database fields, backups, and disk images containing sensitive data must be encrypted. A breach of encrypted data is far less damaging.
- **Key management**: encryption is only as strong as key protection. Keys must be rotated, stored separately from data, and never hardcoded.

## Worked Examples

**HTTPS in Spring Boot** (automatic with `spring-boot-starter-security` + SSL cert):
```properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}
```

**Field-level encryption for sensitive data (using Jasypt):**
```java
@Column(name = "card_number_enc")
@Convert(converter = EncryptedStringConverter.class)
private String cardNumber;
```

**Password hashing (not encryption — use for passwords):**
```java
PasswordEncoder encoder = new BCryptPasswordEncoder();
String hash = encoder.encode(rawPassword);
boolean matches = encoder.matches(rawPassword, hash);
```
Note: passwords should be *hashed*, not *encrypted*. Hashing is one-way; encryption is reversible.

## Common Mistakes

- **Encrypting passwords** — passwords should be hashed (bcrypt), not encrypted. If the key leaks, encrypted passwords are readable.
- **Hardcoded encryption keys** — keys in source code are compromised when the repo is cloned.
- **No IV/nonce in AES** — reusing the same Initialisation Vector with the same key breaks AES security.
- **HTTP in production** — any page that transmits credentials or personal data must use HTTPS.
- **Rolling your own crypto** — use established libraries; cryptographic implementations have subtle failure modes.

## Mental Model

Encryption is a **safe within a vault**. The vault (access control) prevents most thieves. The safe (encryption) protects the most valuable items if the vault is breached. Belt and braces: each layer independent, each adding protection against a different failure mode.

## Mini Summary

- ✔ Symmetric (AES): same key both ways — fast, for bulk data and at-rest
- ✔ Asymmetric (RSA/EC): public encrypts, private decrypts — for key exchange, signatures
- ✔ TLS: asymmetric handshake + symmetric session = HTTPS
- ✔ Encrypt sensitive data at rest (database fields, backups) AND in transit (TLS)
- ✔ Never roll your own crypto — use established, battle-tested implementations

# Guided Practice Quest

**The Cipher Seals**

The Academy's archivists need to implement two seals: an in-transit seal (TLS) and an at-rest seal (field encryption). Evaluate the design of each.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

A healthcare application stores patient records with these fields:
- `patient_id` (integer)
- `name` (string)
- `date_of_birth` (date)
- `diagnosis` (string)
- `prescription_details` (string)
- `email` (string)

Analyse this data and produce a security design document covering:
1. Which fields require encryption at rest and why
2. What encryption approach you would use for each (symmetric field-level vs full disk vs no encryption needed)
3. How the application transmits this data to the frontend securely
4. Where encryption keys should be stored and how they should be managed
5. What threat you're defending against with each measure

# Integration

**Connecting to History — Cryptography Through the Ages**

Cryptography predates computers by millennia. Julius Caesar used a simple letter-shift cipher (ROT-3). The Enigma machine during World War II used polyalphabetic substitution — considered unbreakable, yet cracked by Turing and team at Bletchley Park. The crack of Enigma is estimated to have shortened the war by two years and saved millions of lives.

What Bletchley demonstrated is that cryptographic systems fail not because the mathematics is wrong, but because of implementation flaws, operational errors (Enigma operators reusing message keys), and insufficient adversarial pressure before deployment. The same lesson applies today: security systems thought unbreakable are broken by smart adversaries finding the seams between mathematical perfection and human implementation.

The development of public-key cryptography (Diffie-Hellman, 1976; RSA, 1977) solved the centuries-old key distribution problem. Before public keys, two parties needed a secret meeting to exchange keys before they could communicate securely. Public key cryptography made secure communication between strangers possible — enabling the modern internet.

What does the history of cryptography suggest about the relationship between theoretical security and practical security?

# Lore Conclusion

The scrolls are sealed with both ciphers. Even if the vault is breached, the thief reads only noise.

*"Cryptography is not magic,"* Archmage Veylan says. *"It is mathematics applied carefully by those who understand its constraints. Use the established ciphers. Understand their limitations. Never assume a seal is unbreakable — assume only that it is hard enough to make the effort not worthwhile."*

Encryption is never the final word. It is one layer in a defence with many layers.
---
