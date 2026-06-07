---
id: se-jun-m7-07
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m7
moduleTitle: "Module 7: Design Patterns"
moduleGlyph: "🏗️"
moduleSortOrder: 7
topicSlug: adapter_pattern
topicTitle: "Adapter Pattern"
topicSortOrder: 7
lesson: adapter_pattern
title: "Adapter Pattern"
sortOrder: 7
difficulty: 3
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [observer_pattern]
integrationDomains: [design, engineering]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly identifies the Target interface, Adaptee, and Adapter roles"
    - "Demonstrates wrapping an incompatible class without modifying it"
    - "Distinguishes object adapter (composition) from class adapter (inheritance)"
    - "Gives a concrete real-world or code example of incompatible interfaces being bridged"
    - "Identifies when Adapter adds unnecessary complexity vs when it is clearly the right choice"
  keywords: [target, adaptee, adapter, wrap, interface, incompatible, compose, legacy, third-party, bridge, translate]
  modelAnswer: |
    // Target interface our system expects
    public interface MessageSender {
        void send(String to, String message);
    }
    
    // Adaptee: third-party library with incompatible interface
    public class LegacyEmailClient {
        public void sendEmail(String recipient, String subject, String body) {
            System.out.printf("Sending email to %s: [%s] %s%n", recipient, subject, body);
        }
    }
    
    // Adapter: wraps Adaptee, implements Target interface
    public class EmailClientAdapter implements MessageSender {
        private final LegacyEmailClient legacyClient;
        
        public EmailClientAdapter(LegacyEmailClient legacyClient) {
            this.legacyClient = legacyClient;
        }
        
        @Override
        public void send(String to, String message) {
            // Translate: MessageSender.send -> LegacyEmailClient.sendEmail
            legacyClient.sendEmail(to, "Notification", message);
        }
    }
    
    // Client code uses only the Target interface — unaware of LegacyEmailClient
    MessageSender sender = new EmailClientAdapter(new LegacyEmailClient());
    sender.send("alice@academy.com", "Your quest is ready!");
guidedSteps:
  - id: adp-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which best describes the Adapter pattern's purpose?
    inputConfig:
      options:
        - "Creating a single instance of a class"
        - "Converting one interface into another that clients expect"
        - "Defining a family of interchangeable algorithms"
        - "Building complex objects step by step"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Converting one interface into another that clients expect"]
      rejectedFeedback: "The Adapter pattern's sole purpose is interface translation. When a client expects interface A and you have a class that provides interface B, an Adapter wraps B and presents it as A — without modifying either the client or the existing class."
    hint: "Think of a power plug adapter: it doesn't change the device or the socket — it translates between them."
    reflectionPrompt: "Why is it important that neither the client nor the third-party class needs to change when using an Adapter?"
  - id: adp-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The object adapter approach uses ___ to hold a reference to the Adaptee, rather than inheriting from it. This is preferred because it avoids inheriting unwanted methods.
    inputConfig:
      placeholder: "OO concept (one word)"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["composition", "delegation", "wrapping"]
      rejectedFeedback: "Object adapters use composition (containing the Adaptee as a field). Class adapters use inheritance (extending the Adaptee). Composition is preferred because it avoids the tight coupling and unwanted method inheritance that come with extending a class."
    hint: "The principle 'favour ___ over inheritance' applies directly here."
    reflectionPrompt: "What problems could arise from inheriting from a third-party class rather than wrapping it?"
  - id: adp-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Give a real-world or code example (other than the power plug) where an Adapter is clearly the right pattern. Explain what the Target interface, Adaptee, and Adapter would be in your example.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [interface, wrap, legacy, third-party, incompatible, adapt, target, translate, existing]
      rejectedFeedback: "Good examples: wrapping a legacy database library to match a modern Repository interface; adapting a payment provider's SDK to a common PaymentGateway interface; wrapping java.util.Enumeration to behave as an Iterator. In each case: Target = the interface your code expects; Adaptee = the existing class you cannot change; Adapter = the wrapper that translates."
    hint: "Think about times you have used a library that had a different API from what the rest of your code expected."
    reflectionPrompt: "Is the Adapter pattern purely a structural fix, or does it also communicate design intent to future developers reading the code?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "You have a third-party analytics library with a method logEvent(String category, String action, Map<String, String> data). Your system uses an Analytics interface with track(AnalyticsEvent event). What should you create?"
    options:
      - "Modify the third-party library to implement your Analytics interface"
      - "An Adapter class that implements Analytics and delegates to the third-party library"
      - "Rewrite your Analytics interface to match the third-party signature"
      - "Use inheritance to extend the third-party library"
    correctIndex: 1
    feedback: "An Adapter wraps the third-party library and implements your Analytics interface. Your system code only knows about Analytics — it is isolated from the third-party API. If you ever switch analytics providers, you write a new Adapter, not change your system."
  - type: MULTIPLE_CHOICE
    question: "When should you refactor instead of using an Adapter?"
    options:
      - "When the interfaces are slightly different in naming only and you control both codebases"
      - "When the Adaptee is a third-party library you cannot modify"
      - "When you need to integrate a legacy system into a modern codebase"
      - "When the client and Adaptee are in different teams"
    correctIndex: 0
    feedback: "If you control both the client code and the Adaptee, and the difference is minor (e.g., method name), a straightforward refactor is cleaner than adding an Adapter layer. Adapters are most justified when you cannot (or should not) modify the Adaptee — third-party libraries, legacy systems, external APIs."
retrieval:
  recall: "Name the three roles in the Adapter pattern and describe what each does."
  explain: "Why do object adapters (composition) tend to be preferred over class adapters (inheritance) in Java?"
  mistakeId:
    code: |
      // System expects this interface
      public interface Logger {
          void log(String level, String message);
      }
      
      // Third-party library
      public class CloudLogger {
          public void write(String entry) { /* sends to cloud */ }
      }
      
      // Attempted adapter
      public class LoggerAdapter extends CloudLogger implements Logger {
          public void log(String level, String message) {
              write("[" + level + "] " + message);
          }
      }
    answer: "LoggerAdapter uses class inheritance (extends CloudLogger). This exposes all of CloudLogger's public methods — including write() — to any code that holds a LoggerAdapter reference, leaking the Adaptee's interface. Fix: use object composition instead. Remove 'extends CloudLogger', add 'private final CloudLogger cloudLogger' field, inject it in the constructor, and call cloudLogger.write(...) in the log() method."
---

# Hook

You are integrating a third-party payment provider into the Academy's shop. Their SDK has a method `processTransaction(String cardToken, BigDecimal amount, Currency currency, String merchantId)`. Your system expects a `PaymentGateway` interface with a single method `charge(Payment payment)`. You cannot modify the third-party SDK. You should not rewrite your `PaymentGateway` interface just to match one provider.

This is the Adapter pattern's native habitat: you have two incompatible interfaces and you need them to work together without modifying either one. The Adapter is the translator that sits between them, converting one interface's calls into another's.

> Reflection: Think about a time you had to "glue" two things together that were not designed to work with each other (in code, or in real life). What was the cost of that glue code? Would recognising it as an "Adapter" have helped?

# Lore Introduction

The Academy's Eastern and Western guilds each used different communication crystals. Eastern crystals responded to the `transmit(Rune r)` protocol. Western crystals used `broadcast(SignalPacket p)`. When the guilds needed to communicate, messages were lost — the crystals were incompatible.

Artificer Mira built the Crystal Translator: a device that accepted Eastern-protocol messages on one face and converted them into Western-protocol signals on the other. Neither crystal was modified. Neither guild changed their protocol. The Translator sat between them and made incompatibility invisible.

# Core Learning

## Concept Introduction

The **Adapter pattern** converts the interface of a class into another interface that clients expect. Adapter lets classes work together that could not otherwise because of incompatible interfaces.

Three participants:
- **Target** — the interface the client expects
- **Adaptee** — the existing class with an incompatible interface
- **Adapter** — implements the Target interface, wraps the Adaptee, delegates calls with translation

**Object Adapter (preferred — uses composition):**

```java
// Target interface — what our system expects
public interface MessageSender {
    void send(String to, String message);
}

// Adaptee — third-party class we cannot modify
public class LegacyEmailClient {
    public void sendEmail(String recipient, String subject, String body) {
        System.out.printf("Sending email to %s: [%s] %s%n", recipient, subject, body);
    }
}

// Adapter — implements Target, wraps Adaptee
public class EmailClientAdapter implements MessageSender {
    private final LegacyEmailClient legacyClient; // composition, not inheritance

    public EmailClientAdapter(LegacyEmailClient legacyClient) {
        this.legacyClient = legacyClient;
    }

    @Override
    public void send(String to, String message) {
        // Translate: MessageSender.send → LegacyEmailClient.sendEmail
        legacyClient.sendEmail(to, "Notification", message);
    }
}
```

Client code:

```java
MessageSender sender = new EmailClientAdapter(new LegacyEmailClient());
sender.send("alice@academy.com", "Your quest is ready!");
// Client only knows MessageSender — unaware of LegacyEmailClient
```

**Class Adapter (uses inheritance — less common):**

```java
// Inherits from Adaptee and implements Target
public class EmailClientAdapter extends LegacyEmailClient implements MessageSender {
    @Override
    public void send(String to, String message) {
        sendEmail(to, "Notification", message); // inherited method
    }
}
```

Class Adapter exposes all `LegacyEmailClient` methods publicly — usually undesirable.

## Why It Matters

Adapters are critical when:
1. **Integrating third-party libraries** — you cannot change their API, but you want to abstract it behind your own interface so you can swap providers later
2. **Wrapping legacy code** — old code with unfriendly APIs can be given modern interfaces without rewriting the legacy implementation
3. **Isolating external dependencies** — your tests can mock the Target interface instead of calling the real third-party library

## Worked Examples

**Real Java example — Arrays.asList() as an adapter:**

`java.util.Arrays.asList(array)` adapts a raw array (which cannot be used as a `Collection`) into a `List`. The array is the Adaptee; `List` is the Target; `Arrays.asList` is the Adapter.

**Switching payment providers:**

```java
// Target
public interface PaymentGateway {
    void charge(String customerId, double amount);
}

// Adapter for StripeSDK
public class StripeGatewayAdapter implements PaymentGateway {
    private final StripeClient stripe;
    public StripeGatewayAdapter(StripeClient stripe) { this.stripe = stripe; }

    public void charge(String customerId, double amount) {
        stripe.createCharge(customerId, (long)(amount * 100), "gbp");
    }
}

// Adapter for PayPalSDK
public class PayPalGatewayAdapter implements PaymentGateway {
    private final PayPalService paypal;
    public PayPalGatewayAdapter(PayPalService paypal) { this.paypal = paypal; }

    public void charge(String customerId, double amount) {
        paypal.executePayment(customerId, amount, "GBP");
    }
}
```

Switching providers means swapping adapters. No business code changes.

## Common Mistakes

**Using inheritance instead of composition for the Adapter.** Extending the Adaptee leaks all its methods into the Adapter's public API. Use composition.

**Using Adapter to patch bad internal design.** If two classes you wrote yourself are incompatible, that is a design problem to fix — not an Adapter opportunity. Adapters are for bridging things you cannot change (third-party, legacy).

**Over-adapting.** Not every interface difference needs an Adapter. If you can simply add a method to a class you own, do that. Reserve Adapters for cases where you genuinely cannot modify the Adaptee.

## Mental Model

A power plug adapter. Your laptop charger has a UK three-pin plug. The hotel room has a European two-pin socket. You cannot modify the charger or the socket. You use an adapter that fits between them. The laptop charges normally. The socket delivers power normally. The adapter translates between them.

## Mini Summary

- Adapter converts an incompatible interface into one that clients expect.
- Three roles: Target (expected interface), Adaptee (existing class), Adapter (wrapper).
- Object adapters use composition (preferred); class adapters use inheritance (less common).
- Adapters are most valuable for third-party libraries and legacy code you cannot modify.
- Use refactoring instead of Adapter when you control both sides of the incompatibility.

# Guided Practice Quest

**Quest: The Crystal Translator**

The Academy's two communication crystal protocols must be made compatible. You must demonstrate understanding of the Adapter pattern by identifying the three roles, choosing composition over inheritance, and justifying when an Adapter is warranted.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

The Academy has a new map display system that expects a `Coordinate` interface with `getLatitude()` and `getLongitude()` methods. However, the legacy navigation library uses `GeoPoint` with `x()` and `y()` methods (where x = longitude, y = latitude — note the swap).

Write a `GeoPointAdapter` that implements `Coordinate` and wraps `GeoPoint`, correctly mapping x/y to latitude/longitude. Then write a reflection (minimum 80 words) covering:
1. Why you chose object adapter over class adapter
2. What the client would need to change if the legacy library were replaced with a new one that also needed adapting
3. How wrapping the library behind the `Coordinate` interface improves testability

# Integration

**Connecting to Engineering — Interface Standards and Physical Connectors**

Engineering history is largely a story of incompatible standards and the adapters that bridged them. USB replaced dozens of proprietary connectors, but the transition required years of adapter cables. The HDMI/DisplayPort/VGA ecosystem persists because legacy hardware does not disappear overnight. Engineers regularly build connector converters, voltage regulators, and protocol translators — physical adapters for physical incompatibilities.

In software, the same principle governs integration architecture. REST APIs are adapters between HTTP and application logic. JDBC is an adapter between Java code and database-specific SQL dialects. The JVM's `InputStreamReader` adapts byte streams to character streams.

The key engineering insight in both physical and software domains: adapters do not improve the underlying systems — they do not make the socket deliver more power or make the legacy library's API better. They simply bridge the gap between two systems that were not designed together. This is their purpose and their limitation. When the underlying systems are modernised, the adapters become unnecessary and should be removed.

> Reflection: Can you identify an "adapter" in a software stack you use regularly (e.g., an ORM, a cloud SDK wrapper, an API client library)? What two incompatible surfaces does it bridge?

# Lore Conclusion

Artificer Mira placed the Crystal Translator carefully on the conference table between the Eastern and Western guild representatives. "Try it," she said.

The Eastern representative pressed her rune into the Translator's Eastern face. The Western representative's crystal lit up with the signal — translated, compatible, clear. "You modified neither crystal?" the Western guildmaster asked, surprised. "Neither," Mira confirmed. "I designed an intermediary. Each crystal thinks it is talking to its own kind. They are not. But that distinction is the Translator's concern, not theirs."

Veylan, observing from the doorway, made a note in the Tome. *Adapters bridge differences without changing the parties being bridged. Their elegance is in invisibility.*

---
