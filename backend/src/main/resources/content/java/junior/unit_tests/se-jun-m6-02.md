---
id: se-jun-m6-02
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m6
moduleTitle: "Module 6: Testing"
moduleGlyph: "🧪"
moduleSortOrder: 6
topicSlug: unit_tests
topicTitle: "Unit Tests"
topicSortOrder: 2
lesson: unit_tests
title: "Unit Tests"
sortOrder: 2
difficulty: 2
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [why_testing_matters]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Writes a JUnit 5 test method with @Test annotation"
    - "Follows the Arrange-Act-Assert structure clearly"
    - "Uses at least one meaningful assertion (assertEquals, assertTrue, assertThrows)"
    - "Names the test method to describe what it tests and expected outcome"
    - "Explains what makes the test isolated and deterministic"
  keywords: [junit, test, assert, arrange, act, assert, annotation, isolated, deterministic, method]
  modelAnswer: |
    @Test
    void calculateDiscount_whenOrderOver100_returnsCorrectDiscount() {
        // Arrange
        Order order = new Order(150.0);
        DiscountService service = new DiscountService();

        // Act
        double discount = service.calculateDiscount(order);

        // Assert
        assertEquals(15.0, discount, 0.001);
    }
guidedSteps:
  - id: ut-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which JUnit 5 annotation marks a method as a test case?
    inputConfig:
      options:
        - "@TestCase"
        - "@Test"
        - "@JUnit"
        - "@Run"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["@Test"]
      rejectedFeedback: "`@Test` from `org.junit.jupiter.api.Test` marks a method as a test case that JUnit will discover and execute. The method must be `void` and have no parameters."
    hint: "It's a single-word annotation from the JUnit Jupiter API."
    reflectionPrompt: "JUnit discovers all methods annotated with `@Test` and runs them. No main method needed — the test runner handles discovery and execution automatically."
  - id: ut-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the AAA pattern for this test:

      ```java
      @Test
      void add_twoPositiveNumbers_returnsSum() {
          // ___: set up the inputs
          int a = 3, b = 4;

          // Act: call the method
          int result = calculator.add(a, b);

          // Assert: verify the outcome
          assertEquals(7, result);
      }
      ```

      What word fills the first comment?
    inputConfig:
      placeholder: "first step of AAA"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Arrange", "arrange"]
      rejectedFeedback: "The three steps are **Arrange** (set up inputs and dependencies), **Act** (call the method under test), **Assert** (verify the result matches expectation)."
    hint: "The three letters in AAA are Arrange, Act, Assert."
    reflectionPrompt: "AAA keeps tests readable and structured. When a test fails, AAA makes it immediately clear what was set up, what was called, and what was expected. This clarity speeds up diagnosis."
  - id: ut-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      What makes a good unit test "isolated"? Give a concrete example of something that would break isolation.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [database, network, file, external, dependency, mock, stub, independent, shared, state]
      rejectedFeedback: "Isolation means the test relies on nothing external — no database calls, no network requests, no file I/O, no shared mutable state. A test that calls a real database is not isolated: it depends on the database being available, having the right data, and not being modified by another test."
    hint: "Think about what could make a test pass on one machine but fail on another, or pass alone but fail when run with other tests."
    reflectionPrompt: "Isolated tests are reliable. They pass or fail based solely on the code under test — not on network latency, database state, or test execution order. That reliability is what makes a test suite trustworthy."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "In the AAA pattern, what does the 'Act' step do?"
    options:
      - "Sets up the test data and dependencies"
      - "Calls the method being tested"
      - "Checks the output against the expected value"
      - "Cleans up resources after the test"
    correctIndex: 1
    feedback: "Act is the single call to the method under test. Keeping it to one line is a signal that the test is focused. Multiple calls in Act often indicate the test is doing too much."
  - type: MULTIPLE_CHOICE
    question: "Which assertion checks that a method throws a specific exception?"
    options:
      - "assertEquals(Exception.class, method())"
      - "assertTrue(method() throws Exception)"
      - "assertThrows(Exception.class, () -> method())"
      - "assertException(method(), Exception.class)"
    correctIndex: 2
    feedback: "`assertThrows(ExceptionType.class, () -> codeUnderTest())` verifies that the lambda throws the specified exception type. It also returns the exception so you can assert on its message."

retrieval:
  recall: "What are the three steps of the Arrange-Act-Assert pattern and what does each do?"
  explain: "Explain what makes a unit test different from running the app manually and checking it works."
  mistakeId:
    code: |
      @Test
      void testCalculator() {
          assertEquals(5, calculator.add(2, 3));
          assertEquals(1, calculator.subtract(3, 2));
          assertEquals(6, calculator.multiply(2, 3));
          assertEquals(2, calculator.divide(6, 3));
      }
    answer: "This test does too much — it tests four different behaviours in one method. If `multiply` fails, you don't know if `divide` is also broken. Each test should verify one behaviour. Split into four separate tests: `add_twoNumbers_returnsSum`, `subtract_smallerFromLarger_returnsDifference`, etc."
---

# Hook

Your code works — you tested it manually. You ran the app, clicked around, and everything looked right.

Then you changed one thing in a utility class. And now, three days later, a completely unrelated feature is broken. You have no idea why. You have no test to run. You have nothing but the memory of "it worked before."

Unit tests are the solution. They are the memory your code doesn't have — a permanent record of what every piece of your system is supposed to do.

> Think about the last time you changed something and broke something else unrelated. How long did it take to notice?

# Lore Introduction

Every enchanted artifact in the Academy is submitted to the Proving Hall before being placed in service. A crystal ball is tested for clarity. A binding rune is tested for strength. A translation stone is tested against a known phrase in every known language.

The proving doesn't take long. But without it, no artifact enters use. *Untested artifacts have failed mid-spell*, Archmage Veylan says, *with consequences too grim to describe.*

*"Tests are not a formality,"* she continues. *"They are a covenant between the artificer and those who will depend on the artifact."*

# Core Learning

## Concept Introduction

A **unit test** verifies that a single, isolated unit of code (typically a method) behaves correctly for specific inputs.

JUnit 5 is the standard testing framework for Java:

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void add_twoPositiveNumbers_returnsSum() {
        Calculator calculator = new Calculator();
        int result = calculator.add(3, 4);
        assertEquals(7, result);
    }
}
```

Key components:
- `@Test` — marks the method as a test case
- `assertEquals(expected, actual)` — verifies the result
- Method name describes the scenario and expected outcome

## Why It Matters

Unit tests give you:
1. **Confidence** — know immediately when you break existing behaviour
2. **Documentation** — tests show exactly how the code is meant to be used
3. **Design feedback** — hard-to-test code is often badly designed
4. **Regression prevention** — the test suite grows as a safety net with every feature

Without tests, every change to the codebase is a leap of faith.

## Worked Examples

**Pattern 1 — Happy path**
```java
@Test
void divide_validDivisor_returnsQuotient() {
    Calculator calc = new Calculator();
    assertEquals(3.0, calc.divide(9, 3), 0.001);
}
```

**Pattern 2 — Exception case**
```java
@Test
void divide_byZero_throwsArithmeticException() {
    Calculator calc = new Calculator();
    assertThrows(ArithmeticException.class, () -> calc.divide(9, 0));
}
```

**Pattern 3 — Setup with `@BeforeEach`**
```java
class OrderServiceTest {
    private OrderService service;

    @BeforeEach
    void setUp() {
        service = new OrderService();
    }

    @Test
    void applyDiscount_orderOver100_appliesTenPercent() {
        Order order = new Order(150.0);
        double total = service.applyDiscount(order);
        assertEquals(135.0, total, 0.001);
    }
}
```

## Common Mistakes

- **Testing implementation, not behaviour** — testing private methods or internal state instead of observable outputs.
- **Poorly named tests** — `test1()` or `testAdd()` tell you nothing when they fail. Use `methodName_scenario_expectedResult`.
- **Multiple assertions testing different behaviours** — split into separate tests so failures are pinpointed.
- **Flaky tests** — tests that sometimes pass and sometimes fail destroy trust in the suite. Eliminate randomness and time dependencies.
- **Not running tests** — tests that aren't run don't help. Run them on every change, in CI at minimum.

## Mental Model

A unit test is a **contract written in code**. It says: "given *these inputs*, this method will produce *this output*." Every test in the suite is a clause in that contract. When you change the code, you are running the contract: pass means you kept your promises; fail means you broke one.

## Mini Summary

- ✔ JUnit 5: `@Test` marks test methods; `assertEquals/assertThrows` verify outcomes
- ✔ Arrange-Act-Assert: set up → call → verify — one behaviour per test
- ✔ Name tests: `methodName_scenario_expectedOutcome` for readable failure messages
- ✔ Tests must be isolated: no external dependencies (database, network, file system)
- ✔ `@BeforeEach` runs before each test to set up fresh shared state

# Guided Practice Quest

**The Proving Hall**

Your newly written `Calculator` class needs to pass the Proving Hall before it enters service. Write tests that verify each contract.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You have a `PasswordValidator` class with a single method:
```java
boolean isValid(String password)
```

The rules are:
1. At least 8 characters long
2. Contains at least one uppercase letter
3. Contains at least one digit

Write **five unit tests** that verify this validator, covering:
- A valid password that meets all rules
- A password that fails each rule individually (3 tests)
- A null input (decide what should happen and test it)

For each test: write the `@Test` method using proper AAA structure and a descriptive name. You don't need to implement `PasswordValidator` — just write what the tests would look like.

# Integration

**Connecting to Philosophy — The Falsifiability Principle**

Karl Popper's philosophy of science (1934) argues that a claim is only scientific if it can, in principle, be *falsified* — proven wrong by evidence. A claim like "all swans are white" is falsifiable (one black swan disproves it). A claim like "everything happens for a reason" is not (no evidence could refute it).

Unit tests are the programming embodiment of falsifiability. When you write `assertEquals(7, calculator.add(3, 4))`, you are making a falsifiable claim about your code: it will be proven wrong the moment `add` returns anything other than 7. Code without tests makes claims that cannot be falsified — "it works" is asserted but never tested.

Popper also argued that science progresses by *eliminating wrong theories*, not by proving right ones. Similarly, a test suite doesn't prove your code is correct — it progressively eliminates wrong versions of your code. Each failing test that you fix is a wrong theory replaced by a better one.

What does this suggest about the relationship between test coverage and confidence in your code?

# Lore Conclusion

The Proving Hall glows green. Every clause in the Calculator's covenant has been verified.

*"A test suite is not finished when all tests pass,"* Archmage Veylan says. *"It is only as complete as the behaviours it describes. A test suite with ten tests and a test suite with one hundred tests both pass — but only one of them will catch the next regression."*

The covenant grows stronger with every test added. The next challenge: testing code that talks to things outside itself.
---
