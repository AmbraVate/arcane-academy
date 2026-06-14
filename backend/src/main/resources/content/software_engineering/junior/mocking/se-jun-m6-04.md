---
id: se-jun-m6-04
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m6
moduleTitle: "Module 6: Testing"
moduleGlyph: "🧪"
moduleSortOrder: 6
topicSlug: mocking
topicTitle: "Mocking"
topicSortOrder: 4
lesson: mocking
title: "Mocking"
sortOrder: 4
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [integration_tests]
integrationDomains: [design, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what a mock is and why it is used in unit tests"
    - "Uses @Mock and @InjectMocks (or Mockito.mock()) correctly"
    - "Stubs a method call with when/thenReturn"
    - "Verifies an interaction occurred with verify()"
    - "Identifies a scenario where over-mocking harms test quality"
  keywords: [mock, stub, mockito, when, thenReturn, verify, inject, dependency, isolation, behaviour]
  modelAnswer: |
    @ExtendWith(MockitoExtension.class)
    class NotificationServiceTest {

        @Mock
        private EmailClient emailClient;

        @InjectMocks
        private NotificationService notificationService;

        @Test
        void sendWelcome_validUser_sendsEmail() {
            User user = new User("alice@example.com", "Alice");
            when(emailClient.send(any())).thenReturn(true);

            notificationService.sendWelcome(user);

            verify(emailClient).send(argThat(email ->
                email.getTo().equals("alice@example.com")));
        }
    }
guidedSteps:
  - id: mock-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      `OrderService` depends on `PaymentGateway`. You want to unit test `OrderService`
      without making real payment calls. What is the Mockito annotation to create a fake
      `PaymentGateway`?
    inputConfig:
      options:
        - "@Spy"
        - "@Mock"
        - "@Fake"
        - "@Stub"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["@Mock"]
      rejectedFeedback: "`@Mock` creates a Mockito mock — a fake object that records calls and returns configured values. `@Spy` wraps a real object and intercepts calls selectively. `@Stub` is not a Mockito annotation."
    hint: "The annotation is the same name as the concept — what do you call a fake object in testing?"
    reflectionPrompt: "`@Mock` creates a complete stand-in. By default, all methods return zero/null/empty unless you configure them with `when(...).thenReturn(...)`. You control exactly what the fake returns."
  - id: mock-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the stubbing to make `paymentGateway.charge(order)` return `true`:

      ```java
      ___(paymentGateway.charge(order)).thenReturn(true);
      ```
    inputConfig:
      placeholder: "Mockito method"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["when", "Mockito.when"]
      rejectedFeedback: "`when(mock.method(args)).thenReturn(value)` configures the mock to return a specific value when called with specified arguments. This is called stubbing — defining the mock's behaviour."
    hint: "It starts with 'when' — as in 'when this is called, return this.'"
    reflectionPrompt: "`when(...).thenReturn(...)` is the core stubbing pattern. For exceptions: `.thenThrow(new RuntimeException())`. For dynamic results: `.thenAnswer(invocation -> ...)`. You can chain: `.thenReturn(true).thenReturn(false)` for multiple calls."
  - id: mock-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      What is the risk of mocking too many things in a test? Give a concrete scenario where over-mocking makes a test misleading.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [misleading, false, confidence, real, behaviour, bug, mock, pass, wrong, assumption]
      rejectedFeedback: "Over-mocking can make tests pass even when the real code is broken. Example: if you mock the `UserRepository.save()` method in a test of `UserService`, the test won't catch a bug in the actual save logic — it just tests that `UserService` *calls* save, not that save *works*. The test gives false confidence."
    hint: "If a mock always returns what you told it to return, and the real implementation behaves differently, what happens to the test?"
    reflectionPrompt: "The rule of thumb: mock what you can't control (external services, databases, email clients) or what would make tests slow/flaky. Don't mock your own domain logic — test that directly."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `verify(emailClient).send(any())` assert?"
    options:
      - "That emailClient.send() returned a non-null value"
      - "That emailClient.send() was called at least once with any argument"
      - "That emailClient is not null"
      - "That emailClient.send() will return true next time"
    correctIndex: 1
    feedback: "`verify()` checks that an interaction *occurred*. `verify(mock).method(args)` asserts that `method` was called exactly once with matching args. Use `verify(mock, times(2))` for a specific call count."
  - type: MULTIPLE_CHOICE
    question: "What is the difference between a mock and a stub?"
    options:
      - "A mock is created by Mockito; a stub is created manually"
      - "A stub returns pre-configured values; a mock also verifies interactions occurred"
      - "A stub is faster than a mock"
      - "There is no difference — the terms are interchangeable"
    correctIndex: 1
    feedback: "A stub provides pre-configured responses to calls (state-based). A mock additionally records interactions so you can verify them (behaviour-based). In Mockito, a single mock object can be used as both a stub (when/thenReturn) and a mock (verify)."

retrieval:
  recall: "What are @Mock and @InjectMocks used for in Mockito? How are they different?"
  explain: "Explain to a classmate why we mock dependencies in unit tests rather than using the real implementations."
  mistakeId:
    code: |
      @Test
      void processOrder_success() {
          PaymentGateway gateway = mock(PaymentGateway.class);
          when(gateway.charge(any())).thenReturn(true);
          when(gateway.refund(any())).thenReturn(true);
          when(gateway.getStatus()).thenReturn("ONLINE");

          OrderService service = new OrderService(gateway, mock(Inventory.class),
              mock(NotificationService.class), mock(AuditLog.class),
              mock(FraudDetector.class));

          Order order = mock(Order.class);
          when(order.getTotal()).thenReturn(100.0);
          when(order.getUserId()).thenReturn(1L);

          service.process(order);

          verify(gateway).charge(any());
      }
    answer: "This test has severe over-mocking. Almost every dependency and even the `Order` domain object is mocked. The test only verifies that `charge` was called — it says nothing about correct order processing logic. Use real `Order` objects; only mock truly external dependencies (PaymentGateway). The test as written would pass even if the order processing logic was completely wrong."
---

# Hook

You have written a `NotificationService` that sends emails. To unit test it, you need to call the real email API — which sends actual emails, costs money, and fails in CI because there's no internet access.

Or: you replace the real `EmailClient` with a fake one that records what was asked of it, returns whatever you configure, and never actually sends anything.

That fake is a **mock**. And Mockito makes creating them trivially easy.

> What external systems does your code interact with that would be painful to use in a unit test?

# Lore Introduction

Not all tests can be run with live materials. Testing a ward against real fire would burn the Academy down. Instead, the artificers use **simulacra** — perfect magical duplicates that behave exactly as instructed. Ask a simulacrum fire ward to "act as if there is an inferno," and it will respond as if there were.

*"The simulacrum is not the real thing,"* Archmage Veylan says. *"But for the purposes of testing the ward, it doesn't need to be. It needs only to behave as instructed — and to record what it was asked to do."*

# Core Learning

## Concept Introduction

A **mock** is a fake implementation of a dependency that:
1. Returns pre-configured values when called (**stubbing**)
2. Records interactions so you can verify they happened (**verification**)

```java
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private PaymentGateway paymentGateway;   // fake payment gateway

    @InjectMocks
    private OrderService orderService;       // real service, injected with mock

    @Test
    void processOrder_successfulPayment_completesOrder() {
        Order order = new Order(100.0);
        when(paymentGateway.charge(order)).thenReturn(true);  // stub

        orderService.process(order);

        verify(paymentGateway).charge(order);  // verify the call happened
        assertEquals(OrderStatus.COMPLETE, order.getStatus());
    }
}
```

## Why It Matters

Mocks enable:
- **Speed** — no real database, email server, or payment API
- **Isolation** — test the unit under test, not its dependencies
- **Control** — simulate success, failure, and edge cases on demand
- **Reliability** — tests don't depend on network availability

Without mocks, unit testing code with external dependencies is impractical.

## Worked Examples

**Stubbing a return value:**
```java
when(userRepository.findById(1L)).thenReturn(Optional.of(new User("Alice")));
```

**Stubbing an exception:**
```java
when(emailClient.send(any())).thenThrow(new EmailException("SMTP error"));
```

**Verifying call count:**
```java
verify(auditLog, times(2)).record(any());
verify(emailClient, never()).send(any());
```

**Argument matchers:**
```java
verify(emailClient).send(argThat(email ->
    email.getSubject().contains("Welcome")));
```

## Common Mistakes

- **Mocking your own domain objects** — mock external systems, not your own `Order`, `User`, etc. Test those directly.
- **Over-mocking** — if everything is mocked, the test is just checking that code calls things. It doesn't test logic.
- **Not verifying** — stubbing without verifying means the method might not be called at all and the test still passes.
- **Forgetting `@ExtendWith(MockitoExtension.class)`** — mocks are null without it (or an explicit `MockitoAnnotations.openMocks(this)` in `@BeforeEach`).
- **Mocking `final` classes** — Mockito can't mock final classes by default (requires additional config).

## Mental Model

A mock is a **flight simulator** for your dependencies. Real pilots don't train in real planes for emergency scenarios — too dangerous, too expensive. Instead, the simulator mimics the plane perfectly and lets the pilot experience a hydraulic failure safely.

Your tests fly in the simulator (mock), not the real plane (live API). The mock behaves exactly as configured, never crashes the server, and records everything the "pilot" (service) does.

## Mini Summary

- ✔ `@Mock` creates a Mockito mock; `@InjectMocks` injects mocks into the class under test
- ✔ `when(mock.method()).thenReturn(value)` stubs the return value
- ✔ `verify(mock).method(args)` asserts the interaction occurred
- ✔ Mock external systems (APIs, email, payment); test your own domain logic directly
- ✔ Over-mocking produces false confidence — tests pass even when logic is wrong

# Guided Practice Quest

**The Simulacrum Workshop**

The Academy's testing workshop needs you to configure simulacra for a `NotificationService` test. Stub the email client and verify it was called correctly.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You have a `WeatherAlertService` that:
1. Fetches current weather from `WeatherApiClient.getCurrentConditions()`
2. If temperature exceeds 35°C, calls `AlertService.sendHeatAlert(String city)`
3. Logs the check with `AuditLogger.log(String message)`

Write **three unit tests** using Mockito:
1. When temperature is 40°C → `sendHeatAlert` is called with the correct city
2. When temperature is 20°C → `sendHeatAlert` is never called
3. `AuditLogger.log()` is always called regardless of temperature

For each test: set up mocks, stub the weather client return, call the service, and verify the interactions. Explain what each `verify()` call checks.

# Integration

**Connecting to Philosophy — Simulation and Reality**

Philosopher Jean Baudrillard argued in *Simulacra and Simulation* (1981) that simulations can become indistinguishable from reality — and eventually more "real" than the original. His extreme claim aside, the philosophical question of when a simulation is *good enough* for a purpose is genuine.

In testing, mocks are simulations of dependencies. The question is not "is the mock exactly like the real thing?" (it isn't) but "is the mock *good enough* to test the behaviour I care about?" A mock email client that confirms it was called with the right address is good enough for testing notification logic — we don't need to actually send the email.

But this has limits. Baudrillard's warning applies: if we mock too much, we test only the simulation, not reality. A test suite full of mocks can pass perfectly while the real system is broken — because the mocks don't simulate the real system's failure modes, quirks, and constraints.

The right balance: mock what you can't control or afford to use; test reality wherever you can.

How does this tension between simulation fidelity and test practicality influence your approach to test design?

# Lore Conclusion

The simulacra performed exactly as configured. The NotificationService was tested without sending a single real email.

*"Use simulacra wisely,"* Archmage Veylan cautions. *"They show you what your code *intends* to do. They cannot show you what actually happens when the real system responds unexpectedly. That is what integration tests are for."*

Mock where necessary. Test reality where possible. Know the difference.
---
