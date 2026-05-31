---
id: se-jun-m7-01
school: engineering
domainId: java
tier: JUNIOR
moduleId: se-jun-m7
moduleTitle: "Module 7: Design Patterns"
moduleGlyph: "🏗️"
moduleSortOrder: 7
topicSlug: patterns_intro
topicTitle: "Patterns Introduction"
topicSortOrder: 1
lesson: what_are_design_patterns
title: "What are Design Patterns?"
sortOrder: 1
difficulty: 2
estimatedMinutes: 22
xpReward: 45
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, architecture]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly identifies the three GoF pattern categories with examples"
    - "Explains what a design pattern is in their own words (not just a code template)"
    - "Describes at least one situation where a pattern would help and one where it would be overkill"
    - "Mentions the communication/vocabulary benefit of patterns"
    - "Identifies at least one risk of over-engineering with patterns"
  keywords: [creational, structural, behavioural, vocabulary, GoF, intent, context, overengineering, abstraction, coupling]
  modelAnswer: |
    // Design patterns are reusable solutions to recurring design problems.
    // Example: recognising the Strategy pattern in a sorting context:
    
    // Without a pattern — rigid, hard to extend
    public void sort(int[] data, String type) {
        if (type.equals("bubble")) { /* bubble sort */ }
        else if (type.equals("quick")) { /* quick sort */ }
    }
    
    // With Strategy pattern — open for extension, closed for modification
    public interface SortStrategy {
        void sort(int[] data);
    }
    
    public class QuickSort implements SortStrategy {
        public void sort(int[] data) { /* quicksort implementation */ }
    }
    
    public class Sorter {
        private SortStrategy strategy;
        public Sorter(SortStrategy strategy) { this.strategy = strategy; }
        public void sort(int[] data) { strategy.sort(data); }
    }
guidedSteps:
  - id: wdp-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      The Gang of Four (GoF) book catalogued 23 classic design patterns. Which of the following correctly lists the three GoF pattern categories?
    inputConfig:
      options:
        - "Creational, Structural, Behavioural"
        - "Abstract, Concrete, Hybrid"
        - "Static, Dynamic, Reactive"
        - "Singleton, Factory, Observer"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Creational, Structural, Behavioural"]
      rejectedFeedback: "The GoF organised patterns into Creational (how objects are created), Structural (how objects are composed), and Behavioural (how objects communicate). The fourth option lists specific pattern names, not categories."
    hint: "Think about the lifecycle stages of objects: making them, organising them, and coordinating them."
    reflectionPrompt: "Why do you think grouping patterns into these three categories is useful when learning them for the first time?"
  - id: wdp-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      Complete the sentence: A design pattern is not a finished piece of code — it is a ___ that describes a proven solution to a recurring design problem in a specific context.
    inputConfig:
      placeholder: "one or two words"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["template", "blueprint", "description", "guide", "recipe"]
      rejectedFeedback: "A design pattern is a blueprint or template — a description of a solution, not ready-made code you paste in. The key insight is that you adapt the pattern to your context."
    hint: "Think of how architects use blueprints before building — the blueprint is not the building."
    reflectionPrompt: "How does understanding patterns as templates rather than code snippets change how you would approach learning them?"
  - id: wdp-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Describe the 'pattern trap' in your own words. Give one example of a situation where applying a design pattern would be over-engineering.
    inputConfig:
      minWords: 15
    markingRule:
      matchMode: CONTAINS
      accepted: [overengineer, simple, unnecessary, complexity, small, straightforward, trap, overkill]
      rejectedFeedback: "The pattern trap is applying a pattern simply because you recognise a superficial similarity, even when the problem is simple enough to solve directly. For example, introducing a full Factory hierarchy for a class that only ever has one implementation adds indirection with no benefit."
    hint: "Think about a 10-line utility class that reads a config file. Does it really need the Abstract Factory pattern?"
    reflectionPrompt: "What question could you ask yourself before applying any pattern to ensure you are solving a real problem rather than creating a solution looking for a problem?"
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which GoF category does the Singleton pattern belong to?"
    options:
      - "Structural"
      - "Behavioural"
      - "Creational"
      - "Reactive"
    correctIndex: 2
    feedback: "Singleton is Creational because it controls how (and how many times) an object is created — ensuring only one instance exists."
  - type: MULTIPLE_CHOICE
    question: "What is the primary benefit of sharing pattern vocabulary with your team?"
    options:
      - "It lets you write less code"
      - "It allows faster communication about design intent"
      - "It guarantees the code will be bug-free"
      - "It replaces the need for documentation"
    correctIndex: 1
    feedback: "Saying 'use an Observer here' instantly conveys structure, intent, and trade-offs to anyone who knows the pattern. This shared vocabulary accelerates design discussions significantly."
retrieval:
  recall: "Name the three categories of GoF design patterns and give one example pattern from each."
  explain: "Explain why applying a design pattern to a trivially simple problem can make the codebase worse, not better."
  mistakeId:
    code: |
      // Developer wants to ensure only one logger exists
      public class Logger {
          public static Logger instance = new Logger();
          
          public Logger() { }
          
          public void log(String message) {
              System.out.println(message);
          }
      }
    answer: "Two problems: the constructor is public (anyone can create new Logger instances, defeating the purpose) and the static field is public (external code can replace it). Fix: make the constructor private, the field private, and add a public static getInstance() method."
---

# Hook

You have been writing Java code for a while now. You have solved problems, fixed bugs, and built features. But have you ever finished implementing something, looked at it, and thought: "I feel like someone must have solved this exact problem before — there has to be a cleaner way"? You are right. There is.

Design patterns are the collected wisdom of thousands of software engineers who faced the same recurring problems you are facing now. They are not magic spells — they are proven, named solutions to common design challenges. Learning them transforms you from someone who writes code that works into someone who writes code that communicates intent, invites collaboration, and can grow without collapsing under its own weight.

The catch? Patterns are powerful, but they are also seductive. The "pattern trap" — applying a pattern just because you can — has caused more harm than good in many codebases. Understanding when to use a pattern is just as important as knowing what it does. So before we start learning individual patterns, we need to understand what patterns actually are.

> Reflection: Think of a piece of code you have written that felt repetitive or messy. With hindsight, do you think there might have been a "better shape" for that solution? What made it feel awkward?

# Lore Introduction

In the grand halls of Arcane Academy, the oldest guild is not the Summoners or the Enchanters — it is the Runesmiths. For centuries, apprentices were taught to forge every spell from scratch, rediscovering the same principles generation after generation. The Academy was inefficient, and knowledge was lost when masters died.

Then came Archmage Veylan's great gift: the Tome of Recurring Forms. Veylan had studied ten thousand spells and distilled them into twenty-three recurring patterns — shapes that appeared again and again across disciplines. He named them, described when to use them, and crucially, when not to. The Tome did not remove the need for skill; it gave skilled engineers a shared language so they could build together faster, and recognise familiar ground in unfamiliar territory.

You are now a Junior Runesmith. The Tome of Recurring Forms lies open before you.

# Core Learning

## Concept Introduction

A **design pattern** is a reusable, named solution to a commonly occurring problem in object-oriented software design. The term was popularised by four authors — Erich Gamma, Richard Helm, Ralph Johnson, and John Vlissides — whose 1994 book *Design Patterns: Elements of Reusable Object-Oriented Software* catalogued 23 patterns. They are known as the **Gang of Four (GoF)**.

Patterns are described in terms of:
- **Intent** — what problem does it solve?
- **Motivation** — why is this problem worth solving in a structured way?
- **Structure** — what classes and relationships are involved?
- **Consequences** — what are the trade-offs?

The 23 patterns are grouped into three categories:

| Category | Concern | Examples |
|---|---|---|
| **Creational** | How objects are created | Singleton, Factory Method, Builder |
| **Structural** | How objects are composed | Adapter, Decorator, Facade |
| **Behavioural** | How objects communicate | Strategy, Observer, Command |

## Why It Matters

Patterns give you two distinct advantages:

1. **Vocabulary.** When a senior engineer says "use a Strategy here", the entire team immediately understands the intended structure, the trade-offs, and the expected extension points. Without patterns, the same idea requires a lengthy explanation. Shared vocabulary speeds up design conversations dramatically.

2. **Proven solutions.** Patterns are battle-tested. They have been refined by millions of developers encountering the same problem. You are not just copying a structure — you are inheriting decades of design wisdom.

## Worked Examples

A simple example illustrates the vocabulary benefit. Imagine a team discussing how to handle multiple discount rules in a shopping cart:

**Without patterns:**
> "We should make a thing that holds the discount logic separately, so we can swap it out without changing the cart."

**With patterns:**
> "Use Strategy here."

Both mean the same thing, but the second version is precise, unambiguous, and brings the full weight of a well-understood pattern with it.

Here is how that conversation translates to code structure:

```java
// Strategy pattern applied to discounts
public interface DiscountStrategy {
    double apply(double price);
}

public class NoDiscount implements DiscountStrategy {
    public double apply(double price) { return price; }
}

public class SeasonalDiscount implements DiscountStrategy {
    public double apply(double price) { return price * 0.85; }
}

public class ShoppingCart {
    private DiscountStrategy discount;

    public ShoppingCart(DiscountStrategy discount) {
        this.discount = discount;
    }

    public double total(double price) {
        return discount.apply(price);
    }
}
```

One word — "Strategy" — captures all of this.

## Common Mistakes

**The Pattern Trap.** The most dangerous misuse is applying a pattern because it *looks* like a good fit, not because it genuinely solves a problem. A simple two-class system does not need an Abstract Factory. A method called by one caller does not need the Command pattern. Over-engineering with patterns creates layers of indirection that confuse future developers.

**Treating patterns as rules, not tools.** Patterns are context-dependent. The same problem can justify different patterns depending on constraints — scale, team size, change frequency. There is no single correct answer.

**Learning patterns before understanding OO principles.** Patterns are built on top of concepts like polymorphism, encapsulation, and interfaces. If those are shaky, patterns become cargo cult rituals rather than informed decisions.

## Mental Model

Think of patterns as named shapes in a vocabulary of solutions. Just as a musician who knows "minor seventh chord" can communicate faster with other musicians, an engineer who knows patterns communicates design intent efficiently. The shape is not the music — it is the building block from which music is made.

## Mini Summary

- Design patterns are proven, named solutions to recurring OO design problems.
- The GoF catalogued 23 patterns in three categories: Creational, Structural, Behavioural.
- The primary value of patterns is shared vocabulary and battle-tested structure.
- Patterns are templates to adapt, not code to paste.
- The "pattern trap" — applying patterns where they add complexity without benefit — is a real risk.

# Guided Practice Quest

**Quest: The Runesmith's Catalogue**

In the Academy archives, a young Runesmith must categorise recovered spell fragments. Each fragment represents a known pattern. Your task is to demonstrate understanding of what patterns are, how they are categorised, and when they add value.

Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

You have been handed a small piece of legacy code from the Academy's inventory system:

```java
public class InventoryManager {
    public void process(String itemType, int quantity) {
        if (itemType.equals("scroll")) {
            System.out.println("Stacking " + quantity + " scrolls on shelf A");
        } else if (itemType.equals("potion")) {
            System.out.println("Refrigerating " + quantity + " potions in vault B");
        } else if (itemType.equals("rune")) {
            System.out.println("Etching " + quantity + " runes into stone wall C");
        }
    }
}
```

Write a short reflection (minimum 80 words) that:
1. Identifies what makes this code inflexible
2. Names which GoF category the pattern you would apply belongs to
3. Describes the new structure without writing the full implementation
4. Explains what trade-off you would accept by applying the pattern to this small class

Assess your answer against the rubric in the frontmatter.

# Integration

**Connecting to Psychology — Pattern Recognition**

The human brain is a pattern-recognition machine. Psychologists studying expertise — from chess grandmasters to experienced surgeons — consistently find that experts do not analyse situations from first principles every time. They recognise familiar patterns and retrieve associated responses. This is called *chunking*: grouping information into meaningful units.

Design patterns work the same way in software. A junior developer sees an `if-else` chain and must analyse it from scratch. A senior engineer sees the same code and instantly recognises it as a candidate for the Strategy pattern. The pattern name is the chunk — it compresses a complex structural idea into a single retrievable token.

This is why memorising pattern names without understanding them is counterproductive. Chunking only works when the chunk is tied to genuine understanding. Shallow knowledge of a pattern name actually slows you down — you waste time second-guessing whether you have identified the right pattern. Deep understanding lets you recognise the *intent* of the problem, and the pattern name becomes a reliable handle for retrieving the solution.

> Reflection: Think of another area of your life where you have developed chunked pattern recognition (cooking, sport, music, driving). How did it feel when you first crossed the threshold from conscious analysis to instinctive recognition? How might that experience inform how you approach learning design patterns?

# Lore Conclusion

Archmage Veylan did not write the Tome of Recurring Forms so that Runesmiths would copy spells without thinking. He wrote it so they would spend less time rediscovering the wheel and more time solving genuinely new problems. The patterns in the Tome are signposts, not cages.

As you progress through Module 7, you will encounter each category of pattern. Some will click immediately; others will take time to feel natural. That is expected. The goal is not to memorise 23 solutions — it is to build a design intuition that reaches for the right tool when the situation calls for it, and leaves the toolbox closed when it does not.

---
