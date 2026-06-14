---
id: se-jun-m7-02
school: engineering
domainId: software_engineering
tier: JUNIOR
moduleId: se-jun-m7
moduleTitle: "Module 7: Design Patterns"
moduleGlyph: "🏗️"
moduleSortOrder: 7
topicSlug: strategy_pattern
topicTitle: "Strategy Pattern"
topicSortOrder: 2
lesson: strategy_pattern
title: "Strategy Pattern"
sortOrder: 2
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [what_are_design_patterns]
integrationDomains: [mathematics, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines a Strategy interface and at least two concrete implementations"
    - "Demonstrates composition — the context holds a reference to the strategy interface, not a concrete type"
    - "Shows how the strategy can be swapped at runtime without changing the context class"
    - "Connects the pattern to the Open/Closed principle correctly"
    - "Identifies one situation where Strategy would be overkill"
  keywords: [interface, composition, encapsulate, algorithm, runtime, open-closed, inject, delegate, swap, polymorphism]
  modelAnswer: |
    public interface PaymentStrategy {
        void pay(double amount);
    }
    
    public class CreditCardPayment implements PaymentStrategy {
        public void pay(double amount) {
            System.out.println("Charging £" + amount + " to credit card");
        }
    }
    
    public class PayPalPayment implements PaymentStrategy {
        public void pay(double amount) {
            System.out.println("Sending £" + amount + " via PayPal");
        }
    }
    
    public class Checkout {
        private PaymentStrategy paymentStrategy;
        
        public Checkout(PaymentStrategy paymentStrategy) {
            this.paymentStrategy = paymentStrategy;
        }
        
        public void complete(double amount) {
            paymentStrategy.pay(amount); // delegates — context doesn't know which strategy
        }
    }
guidedSteps:
  - id: sp-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      In the Strategy pattern, what does the Context class hold a reference to?
    inputConfig:
      options:
        - "A concrete strategy class (e.g., CreditCardPayment)"
        - "The Strategy interface"
        - "A static utility method"
        - "An abstract base class with all algorithm implementations"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["The Strategy interface"]
      rejectedFeedback: "The Context holds a reference to the Strategy *interface*, not a concrete implementation. This is the key to the pattern — the context does not know which algorithm it is using, only that it conforms to the interface. This is 'programming to an interface, not an implementation'."
    hint: "The whole point of the pattern is that the context doesn't need to know which algorithm it's using."
    reflectionPrompt: "Why would holding a reference to a concrete class defeat the purpose of the pattern?"
  - id: sp-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      The Strategy pattern is an example of favouring ___ over inheritance. Instead of subclassing a Context with different algorithm variants, we inject the algorithm as a separate object.
    inputConfig:
      placeholder: "one word"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["composition", "delegation"]
      rejectedFeedback: "The answer is 'composition' (sometimes described as 'delegation'). Rather than inheriting behaviour, we compose objects together. This keeps classes focused and allows algorithms to vary independently of the objects that use them."
    hint: "Think of the OO principle: 'favour ___ over inheritance'."
    reflectionPrompt: "Can you think of a scenario where inheritance would have been the first instinct but the Strategy pattern is clearly the better choice?"
  - id: sp-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain how the Strategy pattern supports the Open/Closed Principle. Use a concrete example (e.g., adding a new payment method or sorting algorithm).
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [open, closed, new, implement, extend, modify, interface, concrete, add]
      rejectedFeedback: "The Open/Closed Principle says classes should be open for extension but closed for modification. Strategy supports this because adding a new algorithm means creating a new class that implements the interface — no changes to the Context or existing strategies are required."
    hint: "What happens to the existing code when you add a brand-new payment method as a Strategy?"
    reflectionPrompt: "What would adding a new payment type look like if you used if-else chains instead of Strategy?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "You have a Sorter class that currently uses bubble sort. You want to add quicksort and mergesort without changing Sorter. Which part of the Strategy pattern makes this possible?"
    options:
      - "Making Sorter abstract"
      - "Sorter holds a reference to a SortStrategy interface"
      - "Using static factory methods inside Sorter"
      - "Subclassing Sorter for each algorithm"
    correctIndex: 1
    feedback: "When Sorter holds a SortStrategy interface reference, you simply create new implementing classes (QuickSort, MergeSort) and inject them. Sorter never changes. This is the Open/Closed Principle in action."
  - type: MULTIPLE_CHOICE
    question: "At what point can a strategy be swapped in the Strategy pattern?"
    options:
      - "Only at compile time"
      - "Only during class loading"
      - "At runtime, by changing the strategy reference"
      - "Never — once set it is fixed"
    correctIndex: 2
    feedback: "One of the core benefits of Strategy is runtime flexibility. The context can accept a different strategy via a setter or constructor, enabling behaviour to change dynamically without recompiling."
retrieval:
  recall: "Describe the three participants in the Strategy pattern: Context, Strategy interface, and ConcreteStrategy."
  explain: "Why does the Strategy pattern prefer composition over inheritance? What problem does inheritance cause that composition avoids?"
  mistakeId:
    code: |
      public class ReportGenerator {
          private CsvExporter exporter; // concrete type
          
          public ReportGenerator(CsvExporter exporter) {
              this.exporter = exporter;
          }
          
          public void generate(List<String> data) {
              exporter.export(data);
          }
      }
    answer: "ReportGenerator is coupled to CsvExporter (a concrete class). If you later need PDF or JSON export, you must modify ReportGenerator. Fix: extract an Exporter interface, change the field and constructor parameter to Exporter, and have CsvExporter implement it. This makes ReportGenerator open for extension."
---

# Hook

Imagine you are building a payment system for the Academy's enchanted item shop. At launch, you only support credit cards. Three weeks later, the guild master demands PayPal support. Then crypto. Then guild vouchers. Every time, you crack open the checkout class and splice in another `if-else` branch. The class grows. The tests multiply. The risk of breaking existing behaviour with every change climbs.

This is the problem the Strategy pattern solves. Instead of hard-coding algorithms inside a class, you define a family of interchangeable algorithms, encapsulate each one as an object, and inject the one you need. The checkout class never changes — you just supply a different payment strategy.

Strategy is one of the most practically useful patterns you will encounter. It shows up in sorting, pricing, validation, export, rendering — anywhere a behaviour needs to vary independently of the object that uses it.

> Reflection: Think of a real piece of code (or imagine one) where a class has grown a long if-else chain because new behaviour kept getting added. How would it feel to extend that class versus extending a Strategy-based system?

# Lore Introduction

The Academy's enchantment forge can produce spells in many forms: rune tablets, potion vials, wand inscriptions. For years, each output format required a different forge — a Tablet Forge, a Vial Forge, a Wand Forge. The equipment overlapped enormously. Maintaining three forges was costly and error-prone.

Archmage Veylan solved this with a single Adaptive Forge. The forge itself never changed. What changed was the *enchanting crystal* inserted into its core — a Strategy crystal that contained the specific instructions for each output format. To add a new output type, a Runesmith simply created a new crystal. The forge was unaware of which crystal it held. It just ran the enchantment.

# Core Learning

## Concept Introduction

The **Strategy pattern** defines a family of algorithms, encapsulates each one, and makes them interchangeable. Strategy lets the algorithm vary independently from the clients that use it.

Three participants:
- **Strategy interface** — declares the method that all algorithms implement
- **ConcreteStrategy** — implements a specific algorithm
- **Context** — holds a reference to a Strategy and delegates to it

```java
// Strategy interface
public interface SortStrategy {
    void sort(int[] data);
}

// ConcreteStrategy A
public class BubbleSort implements SortStrategy {
    public void sort(int[] data) {
        // bubble sort implementation
        for (int i = 0; i < data.length - 1; i++) {
            for (int j = 0; j < data.length - 1 - i; j++) {
                if (data[j] > data[j + 1]) {
                    int temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                }
            }
        }
    }
}

// ConcreteStrategy B
public class QuickSort implements SortStrategy {
    public void sort(int[] data) {
        // quicksort implementation (simplified)
        Arrays.sort(data); // for illustration
    }
}

// Context
public class DataProcessor {
    private SortStrategy sortStrategy;

    public DataProcessor(SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    public void setSortStrategy(SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    public void process(int[] data) {
        sortStrategy.sort(data); // delegates — doesn't know which sort it is
        System.out.println(Arrays.toString(data));
    }
}
```

## Why It Matters

Strategy directly supports two SOLID principles:

- **Open/Closed Principle** — add new algorithms by creating new classes, not modifying existing ones
- **Dependency Inversion Principle** — the Context depends on the abstract Strategy interface, not concrete implementations

It also enables **runtime flexibility**: the algorithm can be swapped dynamically based on user preference, configuration, or context, without recompiling anything.

## Worked Examples

**Payment processing:**

```java
public interface PaymentStrategy {
    void pay(double amount);
}

public class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    public CreditCardPayment(String cardNumber) { this.cardNumber = cardNumber; }
    
    public void pay(double amount) {
        System.out.printf("Charging £%.2f to card ending %s%n",
            amount, cardNumber.substring(cardNumber.length() - 4));
    }
}

public class PayPalPayment implements PaymentStrategy {
    private String email;
    public PayPalPayment(String email) { this.email = email; }
    
    public void pay(double amount) {
        System.out.printf("Sending £%.2f via PayPal to %s%n", amount, email);
    }
}

public class Checkout {
    private final PaymentStrategy paymentStrategy;

    public Checkout(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void complete(double amount) {
        // No if-else. No knowledge of which payment type is being used.
        paymentStrategy.pay(amount);
    }
}

// Usage
Checkout checkout = new Checkout(new CreditCardPayment("4111111111111234"));
checkout.complete(49.99);

// Swap strategy — no Checkout changes needed
checkout = new Checkout(new PayPalPayment("user@example.com"));
checkout.complete(49.99);
```

Adding a new payment method (e.g., `CryptoPayment`) requires zero changes to `Checkout`.

## Common Mistakes

**Coupling the Context to a concrete strategy.** If the field type is `CreditCardPayment` instead of `PaymentStrategy`, you have lost all the benefits. Always program to the interface.

**Using Strategy for algorithms with only one implementation.** If there is only one algorithm and you have no plans to add more, the interface adds indirection without value. Apply patterns when they solve a real, present problem.

**Overusing it for simple conditional logic.** Two branches in a method do not need Strategy. If the conditions are unlikely to grow and the logic is simple, an if-else is cleaner.

## Mental Model

Think of a Swiss Army knife. The knife handle (Context) stays the same. You attach different tools (Strategies) depending on what you need to do. The handle does not know or care whether you attached a blade, a screwdriver, or scissors — it just provides the grip. The tool does the actual work.

## Mini Summary

- Strategy encapsulates interchangeable algorithms behind a shared interface.
- The Context delegates to the Strategy; it does not know which concrete algorithm it holds.
- Strategy favours composition over inheritance — the algorithm is injected, not inherited.
- It supports Open/Closed: new algorithms = new classes, existing code unchanged.
- Avoid applying Strategy when there is genuinely only one algorithm or the logic is trivially simple.

# Guided Practice Quest

**Quest: The Enchantment Crystal Forge**

The Academy forge needs a new adaptive enchantment system. You must demonstrate that a Context (the Forge) can operate with interchangeable Strategy crystals (enchanting algorithms) without knowing which crystal it holds.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

The Academy's grade calculator currently uses a tangled method:

```java
public double calculateGrade(double score, String method) {
    if (method.equals("standard")) {
        return score >= 50 ? score : 0;
    } else if (method.equals("curved")) {
        return Math.min(score * 1.1, 100);
    } else if (method.equals("strict")) {
        return score >= 70 ? score : score * 0.5;
    }
    return score;
}
```

Design a Strategy-based replacement. Write the interface, at least two concrete strategies, and the refactored context class. Then write a reflection (minimum 100 words) covering:
1. What changed structurally
2. How adding a fourth grading method compares to the original approach
3. Which SOLID principle the refactor most directly supports and why

# Integration

**Connecting to Mathematics — Functions as First-Class Objects**

In mathematics, a function is an abstract mapping — it takes inputs and produces outputs, with no reference to how the computation is performed internally. The notation f(x) does not specify which function f is; it specifies the *shape* of the mapping. You can substitute any function that has the right signature: linear, quadratic, trigonometric. The recipient of the result does not care which function was used, only that the output is valid.

The Strategy pattern is this mathematical abstraction made concrete in object-oriented code. A `PaymentStrategy` is a function shape: it takes an amount and produces a side effect (payment). `CreditCardPayment` and `PayPalPayment` are specific functions substituted into that shape. The `Checkout` is the mathematical expression that calls f(amount) — it doesn't care whether f is linear or trigonometric, card or PayPal.

In modern Java (Java 8+), this connection is made explicit: a Strategy interface with a single method is a functional interface, and you can pass lambda expressions as strategies directly:

```java
PaymentStrategy printOnly = amount -> System.out.println("Processing: " + amount);
new Checkout(printOnly).complete(29.99);
```

> Reflection: How does thinking of strategies as "substitutable functions with a shape" help you decide when Strategy is the right pattern to apply?

# Lore Conclusion

The Adaptive Forge hummed contentedly as Apprentice Sera slotted in the third crystal of the morning — a Vial Crystal, replacing the Wand Crystal from the previous enchantment. The Forge did not stutter or recalibrate. It simply ran the enchantment with the new crystal's instructions, producing three perfect sleeping-potion vials.

Sera smiled. The old forge would have required a complete retooling. With the Adaptive Forge, adding a new crystal type — a Tome Crystal, for enchanted books — required nothing more than crafting the crystal itself. The Forge, the workshop, the whole production line remained untouched. That, Archmage Veylan had written, is the power of encapsulated strategy.

---
