---
id: se-app-m6-09
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m6
moduleTitle: "Module 6: Debugging and Engineering Habits"
moduleGlyph: "🔧"
moduleSortOrder: 6
topicSlug: engineering_habits
topicTitle: "Beginner Engineering Habits"
topicSortOrder: 2
lesson: clean_formatting
title: "Clean Formatting"
sortOrder: 9
difficulty: 1
estimatedMinutes: 18
xpReward: 40
practiceType: NONE
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [naming_things]
integrationDomains: [psychology, design]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Identifies at least three formatting issues in an example code snippet"
    - "Explains why consistent indentation aids comprehension"
    - "Describes the purpose of blank lines between logical sections"
    - "Names at least one auto-formatter tool relevant to Java"
    - "Reflects on how formatting affects collaboration in teams"
  keywords: [indentation, blank lines, brace, formatting, readability, consistent, auto-format]
  modelAnswer: |
    // Poorly formatted
    public int add(int a,int b){
    int result=a+b;
    return result;}

    // Well formatted (Google Java Style / IntelliJ default)
    public int add(int a, int b) {
        int result = a + b;
        return result;
    }

    // Key rules applied:
    // 1. Space after commas in parameter lists
    // 2. Opening brace on same line as declaration (K&R style)
    // 3. 4-space (or 1 tab) indentation for method body
    // 4. Space around operators (=, +)
    // 5. Closing brace on its own line
guidedSteps:
  - id: format-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Which version of this code follows standard Java formatting?
      ```java
      // Version A
      if(x>0){
      System.out.println("positive");
      }

      // Version B
      if (x > 0) {
          System.out.println("positive");
      }
      ```
    inputConfig:
      options:
        - "Version A — it is more compact"
        - "Version B — it follows standard Java spacing and indentation"
        - "Both are equally acceptable"
        - "Neither — Java requires curly braces on a new line"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Version B — it follows standard Java spacing and indentation"]
      rejectedFeedback: "Version B follows the standard: space between 'if' and the condition, spaces around operators, opening brace on the same line, and 4-space indentation for the body. Version A is technically valid Java but violates readability conventions."
    hint: "Standard Java uses spaces around operators, between keywords and brackets, and indented bodies."
    reflectionPrompt: "Formatting is like typography in a book — it does not change the words but dramatically affects how easily they are read."

  - id: format-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      In most Java style guides, the body of a method or block is indented by ___ spaces (or one tab).
    inputConfig:
      placeholder: "number"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["4", "four"]
      rejectedFeedback: "The Google Java Style Guide and most IntelliJ defaults use 4 spaces per indentation level. Some teams use 2 spaces; what matters is consistency across the entire codebase."
    hint: "The most common Java indentation is four of what character?"
    reflectionPrompt: "The exact number matters less than consistency — a codebase that mixes 2 and 4 spaces is harder to read than one that uses either, consistently."

  - id: format-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain why having an auto-formatter (like IntelliJ's built-in formatter or Google Java Format) configured in a team project is valuable. What problem does it solve?
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [consistent, review, style, debate, automatic]
      rejectedFeedback: "An auto-formatter enforces a single consistent style across all developers' code automatically. It eliminates style debates in code reviews ('you used 2 spaces, I use 4'), reduces formatting noise in diffs, and lets developers focus review energy on logic and correctness rather than whitespace."
    hint: "Think about what happens in a team when each developer has their own formatting preferences."
    reflectionPrompt: "Auto-formatters turn formatting from a social problem (who is right?) into a technical one (what does the tool say?). This reduces conflict and keeps code diffs clean."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the primary purpose of blank lines between methods in a Java class?"
    options:
      - "They are required by the Java compiler"
      - "They visually separate logical sections, making the code easier to scan"
      - "They prevent the compiler from merging adjacent methods"
      - "They signal that the code block is complete"
    correctIndex: 1
    feedback: "Blank lines are visual separators for human readers — the compiler ignores them entirely. Separating methods and logical sections with blank lines lets a reader's eye quickly identify where one concept ends and another begins."
  - type: MULTIPLE_CHOICE
    question: "A line of code is 180 characters wide. What is the typical recommended maximum line length in Java style guides?"
    options:
      - "60 characters"
      - "80-100 characters"
      - "200 characters"
      - "There is no recommended limit"
    correctIndex: 1
    feedback: "Most Java style guides (Google, Oracle) recommend 80-100 characters per line. Longer lines force horizontal scrolling, make side-by-side diffs harder, and are difficult to read without wrapping."
retrieval:
  recall: "List four specific formatting rules that improve Java code readability."
  explain: "A junior developer says formatting is just personal preference and shouldn't be enforced. Give two concrete reasons why consistent team formatting matters."
  mistakeId:
    code: |
      public class calc{
      public int multiply(int a,int b){
      int answer=a*b;
      return answer;
      }
      }
    answer: "Several formatting issues: (1) class name 'calc' violates PascalCase naming (should be 'Calc' or better 'Calculator'); (2) no space before opening braces; (3) method body is not indented; (4) no spaces around operator '=' and after comma in parameter list; (5) closing brace of class is on the same level as the method body. Fix by applying standard Java formatting: indent body by 4 spaces, add spaces around operators and after commas, place opening braces correctly."
---

# Hook

Imagine reading a book where every paragraph is jammed together without line breaks, dialogue runs into narration, and chapter headings appear randomly in the middle of sentences. The words might all be correct, but the reading experience would be exhausting. Your code is no different: its visual structure determines how quickly — and how accurately — a reader can extract meaning from it.

Formatting is not cosmetic. Studies of code comprehension show that well-formatted code is understood significantly faster and with fewer misreadings than identical but poorly-formatted code. When you work on a team, formatting is also a social contract: it signals respect for the next person who reads your work.

> Have you ever tried to read a document (a form, a legal contract, a technical manual) where the layout actively worked against you? What would have made it easier?

# Lore Introduction

At the Academy, the Scribes' Guild maintains the Rule of Visual Clarity: every inscription in the Great Archive must follow a standard layout. Runes are spaced evenly, sections are separated by thin lines, and each clause of a spell is indented beneath its governing rune. A visiting mage from any school should be able to read any inscription in the Archive without confusion.

The Guild has a saying: "A rune inscribed for speed is a rune that slows every future reader." Clean formatting is an act of professional generosity — you invest a few extra moments now so that every reader after you saves time.

# Core Learning

## Concept Introduction

**Clean formatting** involves several practices:

**1. Consistent indentation**
```java
// Each block level indents 4 spaces
public void greet(String name) {
    if (name != null) {
        System.out.println("Hello, " + name);
    }
}
```

**2. Spaces around operators and after commas**
```java
int total = price + tax;        // space around +, =
printItem(name, price, quantity); // space after each comma
```

**3. Opening brace on the same line (K&R style — Java standard)**
```java
if (condition) {    // brace here, not on next line
    doSomething();
}
```

**4. Blank lines to separate logical sections**
```java
public class Order {

    private String customerId;
    private List<Item> items;

    public Order(String customerId) {
        this.customerId = customerId;
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

}
```

**5. Reasonable line length (80-100 characters)**

## Why It Matters

Clean formatting reduces **cognitive load** — the mental effort needed to parse code structure. When indentation reliably reflects block nesting, a reader can see the structure without parsing every brace. When operators have spaces, expressions are legible at a glance. When methods are separated by blank lines, a reader can scan the class structure in seconds.

## Worked Examples

**Example 1 — Before and after formatting**
```java
// Before
public boolean check(int x,int y){
boolean result=x>y&&y>0;
return result;}

// After (standard formatting)
public boolean check(int x, int y) {
    boolean result = x > y && y > 0;
    return result;
}
```

**Example 2 — Using blank lines to separate concerns**
```java
public void processPayment(Order order) {
    // Validation
    if (order == null) {
        throw new IllegalArgumentException("Order must not be null");
    }

    // Calculate total
    double total = order.calculateTotal();

    // Charge customer
    paymentGateway.charge(order.getCustomerId(), total);
}
```

**Example 3 — Long method chain — how to break it**
```java
// Hard to read on one 150-char line
String result = list.stream().filter(x -> x > 0).map(x -> x * 2).sorted().collect(Collectors.joining(", "));

// Readable with line breaks
String result = list.stream()
    .filter(x -> x > 0)
    .map(x -> x * 2)
    .sorted()
    .collect(Collectors.joining(", "));
```

## Common Mistakes

- **Inconsistent indentation** — mixing tabs and spaces, or different numbers of spaces on different lines.
- **No blank lines** — methods and fields jammed together make a class look like one giant paragraph.
- **Very long lines** — lines over 120 characters force horizontal scrolling and make side-by-side code review hard.
- **Missing spaces around operators** — `x=a+b*c` is technically valid but harder to parse than `x = a + b * c`.
- **Relying on memory** — configure your IDE's auto-formatter and use it. Formatting by hand is error-prone.

## Mental Model

Think of formatted code like a well-set piece of typography. The margins, spacing, line breaks, and indentation create a visual hierarchy that guides the eye before the brain has even read the words. Well-typeset text is processed faster and retained longer. Code is the same: formatting is the typography of programming.

## Mini Summary

✔ Indent each block level by 4 spaces; be consistent across the entire file.
✔ Put spaces around operators and after commas; place the opening brace on the same line.
✔ Use blank lines to separate methods, fields, and logical sections within methods.
✔ Keep lines to 80-100 characters; break long chains across multiple lines.
✔ Configure an auto-formatter (IntelliJ: Code → Reformat Code) and use it habitually.

# Guided Practice Quest

**The Scribes' Guild Audit**
The Guild has received a batch of poorly-formatted code. Identify the specific violations and describe the corrected versions.
Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Take the following poorly-formatted Java class and rewrite it with clean, standard formatting. Then list every change you made and explain why each change improves readability:
```java
public class bankaccount{
int balance=0;
public void deposit(int amount){if(amount>0){balance+=amount;}else{System.out.println("invalid");}}
public int getBalance(){return balance;}
}
```

# Integration

**Connecting to Design — Visual Hierarchy**
Clean code formatting is a form of **visual design**. Designers use the principle of **visual hierarchy** — arranging elements so the eye naturally moves from most important to least important, and so structure is immediately perceptible. In graphic design, this is achieved with size, weight, colour, and spacing. In code, the equivalent tools are indentation, blank lines, and consistent brace placement.

When a method body is indented relative to its declaration, the hierarchy is clear: declaration → body. When a conditional block is indented inside the method, the nesting is visible at a glance. Poorly formatted code lacks visual hierarchy — everything is at the same visual level, and the reader must parse every character to understand structure.

> Next time you see a beautifully designed web page or printed document, try to identify the visual hierarchy — what catches your eye first, second, third. How does the designer guide your attention? Can you apply the same thinking to your code layout?

# Lore Conclusion

The apprentice submits their re-inscribed spell to the Scribes' Guild. The Guild Master runs a practiced eye across the evenly-spaced runes, the properly indented sub-clauses, the clean separation between each invocation section. "This," she says, "will still be readable in a hundred years." She stamps it with the Guild's seal of approval.

Naming and formatting are now part of the apprentice's automatic practice. The next lesson will go deeper: not just how individual lines look, but how the work is **divided** — the principle that each function should do one thing, and do it well. Small, focused functions are the foundation of code that is easy to understand, test, and change.

---
