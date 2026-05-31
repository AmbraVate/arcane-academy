---
id: se-jun-m8-04
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m8
moduleTitle: "Module 8: Professional Practices"
moduleGlyph: "⚙️"
moduleSortOrder: 8
topicSlug: refactoring
topicTitle: "Refactoring"
topicSortOrder: 4
lesson: refactoring
title: "Refactoring"
sortOrder: 4
difficulty: 3
estimatedMinutes: 28
xpReward: 55
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [code_reviews]
integrationDomains: [design, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly defines refactoring as improving structure without changing external behaviour"
    - "Demonstrates at least two named refactoring techniques (Extract Method, Rename Variable, etc.)"
    - "Explains the Boy Scout Rule and how it prevents codebase decay"
    - "Identifies at least two code smells with an example of each"
    - "Explains why tests are essential before and during refactoring"
  keywords: [behaviour, structure, extract, rename, constant, smell, test, safe, Boy-Scout, duplicate, long-method, feature-envy]
  modelAnswer: |
    // Before: Extract Method refactoring
    public void processOrder(Order order) {
        // Validate
        if (order.getItems().isEmpty()) throw new IllegalArgumentException("No items");
        if (order.getCustomer() == null) throw new IllegalArgumentException("No customer");
        // Calculate total
        double total = 0;
        for (Item item : order.getItems()) {
            total += item.getPrice() * item.getQuantity();
        }
        if (order.isMember()) total *= 0.9;
        // Send confirmation
        emailService.send(order.getCustomer().getEmail(), "Total: " + total);
    }
    
    // After: each concern extracted to a named method
    public void processOrder(Order order) {
        validateOrder(order);
        double total = calculateTotal(order);
        sendConfirmation(order, total);
    }
    
    private void validateOrder(Order order) {
        if (order.getItems().isEmpty()) throw new IllegalArgumentException("No items");
        if (order.getCustomer() == null) throw new IllegalArgumentException("No customer");
    }
    
    private double calculateTotal(Order order) {
        double total = order.getItems().stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
        return order.isMember() ? total * 0.9 : total;
    }
    
    private void sendConfirmation(Order order, double total) {
        emailService.send(order.getCustomer().getEmail(), "Total: " + total);
    }
guidedSteps:
  - id: ref-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which statement correctly defines refactoring?
    inputConfig:
      options:
        - "Rewriting code from scratch to make it faster"
        - "Adding new features while also cleaning up existing code"
        - "Improving the internal structure of existing code without changing its external behaviour"
        - "Fixing bugs found during code review"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Improving the internal structure of existing code without changing its external behaviour"]
      rejectedFeedback: "Refactoring's defining constraint is that external behaviour must not change. You are restructuring the code — making it clearer, more maintainable, less duplicated — while the system continues to behave identically from the outside. If behaviour changes, that is a feature or bug fix, not refactoring. This distinction matters because tests must pass before and after."
    hint: "The 'external behaviour' part is the crucial constraint — what must NOT change during refactoring?"
    reflectionPrompt: "Why is it important to have tests passing before you start refactoring? What would happen if you refactored and introduced a bug without tests to catch it?"
  - id: ref-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      What is the 'Extract Method' refactoring?
    inputConfig:
      options:
        - "Moving a method from one class to another"
        - "Taking a block of code and turning it into a separate, named method"
        - "Extracting a method into an interface"
        - "Deleting a method that is no longer used"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Taking a block of code and turning it into a separate, named method"]
      rejectedFeedback: "Extract Method takes a coherent block of code (often a commented section or a section that does one thing) and moves it into its own method with a descriptive name. The original site calls the new method. Result: the parent method reads as a high-level narrative of steps, and each step is named and independently understandable."
    hint: "You are taking something that was inline and giving it its own named location."
    reflectionPrompt: "How does giving a block of code its own named method improve readability for future developers?"
  - id: ref-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain the 'Boy Scout Rule' in software development and how it prevents codebase decay over time.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [clean, leave, better, found, scout, decay, small, improve, accumulate, standard]
      rejectedFeedback: "The Boy Scout Rule says: 'Leave the campsite cleaner than you found it.' Applied to code: whenever you touch a file, make a small improvement — rename a confusing variable, extract a method, remove a dead code block. This continuous micro-refactoring prevents the accumulation of technical debt. No heroic rewrites needed; just tiny, consistent improvements every day."
    hint: "The rule is named after the Boy Scout camping principle. What is that principle?"
    reflectionPrompt: "What happens to a codebase over years if developers always say 'I'll clean that up later' instead of following the Boy Scout Rule?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of these is a 'Long Method' code smell?"
    options:
      - "A method that returns a long string"
      - "A method that takes a long time to run"
      - "A method with 150 lines of code handling validation, business logic, persistence, and notification"
      - "A method with a long name"
    correctIndex: 2
    feedback: "Long Method is a code smell where a single method does too many things — it is too long to understand at a glance and violates Single Responsibility. The fix is Extract Method: identify each distinct concern and extract it into a named method. The parent method then reads as a high-level summary of steps."
  - type: MULTIPLE_CHOICE
    question: "The 'Replace Magic Number with Named Constant' refactoring converts '0.9' into 'MEMBER_DISCOUNT'. What is the primary benefit?"
    options:
      - "The program runs faster"
      - "The number becomes visible in all IDEs"
      - "The intent is explicit — readers understand what 0.9 represents, and changing it requires one edit instead of finding every occurrence"
      - "It reduces file size"
    correctIndex: 2
    feedback: "A magic number (0.9) has no self-evident meaning. MEMBER_DISCOUNT_MULTIPLIER = 0.9 explains the intent. If the discount changes, you update one constant declaration — not every occurrence scattered through the code. Named constants serve both as documentation and as a single source of truth."
retrieval:
  recall: "Name four refactoring techniques (e.g., Extract Method) and describe what each does."
  explain: "Why must refactoring be done with a test suite in place? What specific risks exist when refactoring without tests?"
  mistakeId:
    code: |
      public double calc(List<Map<String, Object>> d, boolean f) {
          double t = 0;
          for (Map<String, Object> i : d) {
              double p = (double) i.get("p");
              int q = (int) i.get("q");
              t += p * q;
          }
          if (f) t = t * 0.9;
          return t;
      }
    answer: "Multiple smells: (1) Cryptic variable names (d, f, t, i, p, q) — apply Rename Variable: 'items', 'isMember', 'total', 'item', 'price', 'quantity'. (2) Magic number 0.9 — apply Replace Magic Number with Named Constant: MEMBER_DISCOUNT = 0.9. (3) Using Map<String,Object> instead of a typed class — introduce an Item record/class with price and quantity fields. (4) Poor method name 'calc' — rename to 'calculateOrderTotal(List<Item> items, boolean isMember)'."
---

# Hook

Six months ago, a developer added a method to process quiz submissions. It was clear enough at the time. Today, that method is 200 lines long, handles validation, calculates scores, saves results, awards badges, sends emails, and updates a leaderboard — all interwoven. Nobody wants to touch it. Changing one thing risks breaking everything. Adding a feature requires reading 200 lines to find where to insert code.

This is technical debt: accumulated structural complexity that makes the code increasingly expensive to work with. Refactoring is the disciplined practice of paying that debt down — improving the structure of code without changing its behaviour, using tests as a safety net.

> Reflection: Think of a piece of code (yours or someone else's) that you felt reluctant to touch because it was too complicated. What made it that way? What small improvement would have made the biggest difference?

# Lore Introduction

The Academy's Binding Tome — the central spell registry — had grown organically for decades. Each Runesmith added entries when needed, with no consistent structure. By the current era, some entries spanned twelve pages, mixed three unrelated enchantments, and were indecipherable without consulting their original author. Modifying any entry was dangerous; the risk of accidentally altering a neighbour was high.

Archmage Veylan commissioned a Great Reorganisation — not a rewriting of any spell, but a restructuring of how they were recorded. Spells were separated, titled, and cross-referenced. Complex entries were split into named components. The enchantments themselves were unchanged. The Tome became navigable again. That process, Veylan wrote, is refactoring.

# Core Learning

## Concept Introduction

**Refactoring** is the process of improving the internal structure of existing code without changing its external behaviour. The code does the same thing — it is just cleaner, clearer, and easier to work with.

**Key constraint:** tests must pass before and after every refactoring step. This is the safety net that guarantees behaviour is preserved.

**Named refactoring techniques:**

| Technique | What it does |
|---|---|
| **Extract Method** | Move a code block into its own named method |
| **Rename Variable/Method** | Give a misleading or cryptic name a clear, accurate one |
| **Replace Magic Number with Named Constant** | `0.9` → `MEMBER_DISCOUNT_MULTIPLIER` |
| **Extract Class** | Move related responsibilities from one class into a new, focused class |
| **Inline Method** | Remove a trivial method by inlining its body where called |
| **Introduce Parameter Object** | Replace multiple related parameters with a single object |

## Why It Matters

**Readability.** Refactored code is easier to read, understand, and modify. Future developers (including future you) spend less time deciphering and more time delivering.

**Safety.** Small, focused classes and methods are easier to test in isolation. Bugs are easier to locate. Changes have smaller blast radii.

**The Boy Scout Rule.** "Always leave the code cleaner than you found it." Applied consistently, small refactoring touches prevent the accumulation of technical debt that turns codebases into unmaintainable messes.

## Worked Examples

**Extract Method:**

```java
// Before: one long method, hard to scan
public double processOrder(Order order) {
    // Validation block
    if (order.getItems() == null || order.getItems().isEmpty()) {
        throw new IllegalArgumentException("Order must have items");
    }
    if (order.getCustomer() == null) {
        throw new IllegalArgumentException("Order must have a customer");
    }
    // Calculation block
    double total = 0;
    for (Item item : order.getItems()) {
        total += item.getPrice() * item.getQuantity();
    }
    if (order.isMember()) {
        total = total * 0.9; // magic number
    }
    // Notification block
    emailService.send(order.getCustomer().getEmail(),
        "Your order total is £" + total);
    return total;
}

// After: top-level method reads as a narrative
private static final double MEMBER_DISCOUNT = 0.9;

public double processOrder(Order order) {
    validateOrder(order);
    double total = calculateTotal(order);
    notifyCustomer(order, total);
    return total;
}

private void validateOrder(Order order) {
    if (order.getItems() == null || order.getItems().isEmpty()) {
        throw new IllegalArgumentException("Order must have items");
    }
    if (order.getCustomer() == null) {
        throw new IllegalArgumentException("Order must have a customer");
    }
}

private double calculateTotal(Order order) {
    double subtotal = order.getItems().stream()
        .mapToDouble(item -> item.getPrice() * item.getQuantity())
        .sum();
    return order.isMember() ? subtotal * MEMBER_DISCOUNT : subtotal;
}

private void notifyCustomer(Order order, double total) {
    emailService.send(order.getCustomer().getEmail(),
        "Your order total is £" + total);
}
```

**Rename Variable:**

```java
// Before
double d = calculateDiscount(t, f);

// After
double discountedPrice = calculateDiscount(totalPrice, isMember);
```

**Replace Magic Number:**

```java
// Before
if (score >= 50) { ... }
if (score >= 70) { ... }
if (score >= 90) { ... }

// After
private static final int PASS_THRESHOLD        = 50;
private static final int MERIT_THRESHOLD       = 70;
private static final int DISTINCTION_THRESHOLD = 90;
```

## Common Mistakes

**Refactoring without tests.** If no tests exist for the code you are refactoring, you cannot verify that behaviour was preserved. Write tests first, then refactor.

**Changing behaviour while refactoring.** "While I'm here, I'll also fix this logic." That is a feature or bug fix — do it in a separate commit. Keep refactoring commits pure.

**Big-bang refactors.** Rewriting 10,000 lines "properly" carries enormous risk. Prefer continuous, small refactors guided by the Boy Scout Rule.

**Renaming things inconsistently.** A good IDE rename tool updates all references. Manually renaming only some occurrences creates bugs.

## Mental Model

Think of refactoring like decluttering a workshop. You are not replacing tools or building new ones — you are organising what already exists so you can find things faster and work more efficiently. The workshop does the same work after decluttering. But the craftsperson can move faster, make fewer mistakes, and onboard helpers more easily.

## Mini Summary

- Refactoring improves structure without changing external behaviour — tests must pass before and after.
- Key techniques: Extract Method, Rename Variable/Method, Replace Magic Number, Extract Class.
- The Boy Scout Rule: leave code cleaner than you found it — small, consistent improvements prevent accumulation.
- Code smells (Long Method, Magic Numbers, Duplicate Code) are signals that refactoring is needed.
- Never change behaviour and structure simultaneously — separate commits keep intent clear.

# Guided Practice Quest

**Quest: The Tome Reorganisation**

The Academy's Binding Tome has a chaotic entry that must be reorganised. You must demonstrate understanding of what refactoring is, how named techniques apply, and why tests come first.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Refactor the following code. Apply at least three named refactoring techniques, explain each change, and ensure the before/after behaviour is identical.

```java
public class Calc {
    public String run(int s, boolean p) {
        String r = "";
        if (s >= 90) { r = "A"; }
        else if (s >= 70) { r = "B"; }
        else if (s >= 50) { r = "C"; }
        else { r = "F"; }
        if (p) { r = r + "+"; }
        System.out.println("Grade: " + r);
        return r;
    }
}
```

Write a reflection (minimum 100 words) covering:
1. What each refactoring technique you applied is called
2. How you would verify that behaviour was preserved
3. What code smell was the most harmful to readability and why

# Integration

**Connecting to Psychology — The Accumulation of Cognitive Load**

Cognitive load theory, developed by educational psychologist John Sweller, distinguishes three types of cognitive load: *intrinsic* (the inherent complexity of the subject), *extraneous* (complexity caused by poor presentation), and *germane* (the productive effort of learning). Good instruction minimises extraneous load — poor formatting, irrelevant details, inconsistent structure — so learners can direct mental energy toward intrinsic complexity.

Technical debt in code is extraneous cognitive load. When a developer reads a 200-line method with cryptic variable names, magic numbers, and mixed concerns, their mental energy is consumed decoding presentation problems rather than understanding the actual business logic. Refactoring removes extraneous cognitive load from code: clear names, focused methods, named constants — all reduce the mental overhead of understanding, leaving more capacity for the actual complexity of the problem.

The Boy Scout Rule can be understood as a professional commitment to managing cognitive load for your future self and your teammates. Each small improvement is a small reduction in the extraneous load the next reader faces. In a codebase touched by dozens of developers over years, those small reductions compound into a dramatically more productive environment.

> Reflection: Think about the most readable piece of code you have encountered. What made it easy to read? Map each property to a specific cognitive load reduction — what extraneous load did it remove?

# Lore Conclusion

The Great Reorganisation of the Binding Tome took three months. Not a single enchantment was altered — every spell still functioned exactly as before. But the Tome was transformed. Entries were titled. Long rituals were broken into named components. Magic symbols were labelled with their purpose in plain rune-script. The cross-references were cleaned up.

When a new Runesmith arrived for their first assignment, they opened the Tome and found, to their surprise, that they could actually read it. The senior Runesmith observed their expression. "We didn't change what it does," she said. "We changed how easy it is to understand. Those are very different things. But the second makes the first survivable."

---
