---
id: se-app-m5-07
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m5
moduleTitle: "Module 5: Object Thinking Foundations"
moduleGlyph: "🔷"
moduleSortOrder: 5
topicSlug: classes
topicTitle: "Classes"
topicSortOrder: 2
lesson: fields
title: "Fields"
sortOrder: 7
difficulty: 2
estimatedMinutes: 20
xpReward: 60
practiceType: JAVA
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m5-06]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly distinguishes instance fields (declared in class body) from local variables (declared inside methods)"
    - "Explains that instance fields belong to the object and persist for its lifetime"
    - "States correct default values for int (0), String (null), boolean (false), double (0.0)"
    - "Demonstrates correct access to instance fields via dot notation"
    - "Explains why relying on default values is risky and a constructor is preferred"
  keywords: [instance field, local variable, default value, null, persist, lifetime, dot notation, scope]
  modelAnswer: |
    Instance fields are declared inside the class body but outside any method.
    They belong to the object and exist as long as the object exists.
    Local variables are declared inside a method and exist only while that method runs.
    Uninitialised int fields default to 0, String fields to null, boolean to false.
    Relying on null defaults is risky because accessing a null String can cause a NullPointerException.
    A constructor ensures fields are set to meaningful values immediately.

guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: "Where is an instance field declared?"
    inputConfig:
      options:
        - "Inside a method, before the first loop"
        - "Inside the class body but outside any method"
        - "Inside the constructor only"
        - "At the top of the file, outside the class"
      correctIndex: 1
    markingRule: EXACT_MATCH
    hint: "Instance fields define the state of the object — they need to be accessible from any method in the class."
    reflectionPrompt: "What would happen if a field were declared inside a single method — could other methods use it?"

  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "Predict the output: what will this print? Write your prediction, then explain why."
    inputConfig:
      language: java
      starterCode: "class Counter {\n    int count;  // instance field — not initialised\n}\nCounter c = new Counter();\nSystem.out.println(c.count);\n"
      expectedPattern: "0"
    markingRule: KEYWORD_MATCH
    hint: "Java sets uninitialised int fields to a default value. What is the default for int?"
    reflectionPrompt: "Would it be safer to initialise count to 0 explicitly in the constructor? Why?"

  - id: step-3
    sortOrder: 3
    inputType: CODE
    instruction: "Add a field 'score' (int) to this Player class and access it via dot notation to print it."
    inputConfig:
      language: java
      starterCode: "class Player {\n    String name;\n    // Add score field here\n\n    Player(String name) {\n        this.name = name;\n    }\n}\n\nPlayer p = new Player(\"Alex\");\n// Print p.score here\n"
      expectedPattern: "int\\s+score|p\\.score"
    markingRule: REGEX_MATCH
    hint: "Declare 'int score;' inside the class body. Then use p.score to access it."
    reflectionPrompt: "p.score will print 0 because score was not initialised. How would you fix this so score is always set when a Player is created?"

microCheckpoint:
  - question: "What is the default value of an uninitialised String field in Java?"
    options:
      - "An empty string \"\""
      - "\"undefined\""
      - "null"
      - "0"
    correctIndex: 2
    feedback: "Correct — uninitialised object-type fields (like String) default to null in Java."

  - question: "What is the difference between an instance field and a local variable?"
    options:
      - "They are the same — just different names for the same thing"
      - "An instance field belongs to the object and persists; a local variable exists only while its method runs"
      - "A local variable belongs to the object; an instance field is only in the constructor"
      - "Instance fields are always public; local variables are always private"
    correctIndex: 1
    feedback: "Yes — instance fields are part of the object's state and last as long as the object does. Local variables only exist during the method call that declares them."

retrieval:
  recall: "What are the default values for int, String, boolean, and double fields that are never initialised?"
  explain: "Explain why a String field that defaults to null can cause a problem, and how using a constructor prevents it."
  mistakeId:
    code: |
      class Rectangle {
          void area() {
              int width = 5;
              int height = 10;
          }

          void printDimensions() {
              System.out.println(width + " x " + height);
          }
      }
    answer: "width and height are local variables declared inside area(). They are not accessible in printDimensions(). To share them across methods, they must be instance fields declared in the class body."
---

# Hook

A spell book has pages that exist as long as the book exists. A sticky note you put on a page exists only until you peel it off. Instance fields are the pages — permanent for the object's lifetime. Local variables are the sticky notes — alive only while a method runs, then gone. Understanding this difference is the key to understanding why some data persists and some disappears. Miss this distinction and your programs will be haunted by variables that vanish mid-spell.

# Lore Introduction

The Academy's archivists distinguish between two kinds of runes: Bound Runes, which are inscribed directly on the construct's core and persist until the construct is dissolved, and Transient Runes, which flare briefly during a single spell invocation and then fade. A wizard who mistakes a Transient Rune for a Bound Rune will reach for knowledge that has already vanished. In Java, this distinction is called instance fields versus local variables — and mastering it separates apprentices from adepts.

# Core Learning

## Concept Introduction

**Instance fields** are declared inside the class body, outside any method. They belong to the object and exist as long as the object exists. Any method in the class can access them.

**Local variables** are declared inside a method (or constructor). They only exist while that method is running. Once the method ends, they disappear. Other methods cannot access them.

```java
class Dog {
    // Instance fields — belong to the object
    String name;   // exists for Dog's entire lifetime
    int age;

    void birthday() {
        // Local variable — only exists during this method call
        int newAge = age + 1;
        age = newAge;
        System.out.println(name + " is now " + age);
    }
}
```

## Why It Matters

If you accidentally declare something as a local variable when you meant it to be an instance field, other methods will not be able to see it. The code will fail to compile with a "cannot find symbol" error — or worse, if the name happens to match something else, it will compile but produce wrong results. Knowing where to declare each type of variable is fundamental.

## Worked Examples

**Default values for uninitialised instance fields:**

| Type | Default Value |
|------|--------------|
| `int` | `0` |
| `double` | `0.0` |
| `boolean` | `false` |
| `String` (and all objects) | `null` |

```java
class Counter {
    int count;      // defaults to 0
    String label;   // defaults to null
    boolean active; // defaults to false
}

Counter c = new Counter();
System.out.println(c.count);   // 0
System.out.println(c.label);   // null
System.out.println(c.active);  // false
```

**Why `null` is dangerous:**
```java
Counter c = new Counter();
// c.label is null — it has no String value
System.out.println(c.label.length()); // NullPointerException! Cannot call methods on null
```

This crash (NullPointerException) is one of the most common errors in Java. A constructor prevents it by ensuring `label` is always set before anything uses it.

## Common Mistakes

- **Declaring a variable in a method when it should be a field**: If two methods both need to know the same value, it must be a field, not a local variable in each method.
- **Assuming default values are safe**: `null` is not an empty string. Calling any method on a `null` String crashes immediately.
- **Redeclaring a field inside a method**: Writing `String name = "Rex";` inside a method creates a *new local variable* called `name` — it does not change the field.

## Mental Model

Think of instance fields as **drawers in a desk**. The desk (object) has the drawers at all times; any drawer can be opened by anyone standing at the desk. Local variables are like **items you hold in your hand** while working. Once you sit down, they exist. Once you walk away, they are gone.

## Mini Summary

- ✔ Instance fields are declared in the class body, outside methods — they belong to the object.
- ✔ Local variables are declared inside methods — they exist only while the method runs.
- ✔ Uninitialised fields have default values: `int→0`, `double→0.0`, `boolean→false`, objects→`null`.
- ✔ `null` is dangerous — calling methods on a null object causes a NullPointerException.
- ✔ A constructor prevents null problems by initialising fields at creation time.

# Guided Practice Quest

Work through the sidebar steps to identify the scope of variables, predict default values for uninitialised fields, and practise accessing instance fields via dot notation.

# Solo Practice Quest

**Spell: Trace the Runes**

Given this class:
```java
class Lamp {
    String colour;
    boolean isOn;
    int brightness;
}
```

1. What will Java print for each field of `new Lamp()` if you access them without setting them first? Give the values.
2. Rewrite the class with a constructor that requires all three values to be set at creation.
3. Add a method `describe()` that prints: `"A [colour] lamp, brightness [brightness], on: [isOn]"`.

Show the full class and a usage example.

# Integration

**Mathematics connection — scope in functions**

In mathematics, a function like `f(x) = x * 2` uses `x` only inside the function definition. If you have a global variable `y = 5`, it exists outside and independently of `f`. Java maps this exactly: local variables inside a method are like the bound variable `x` in a function definition — their scope is strictly limited. Instance fields are more like the global `y` — accessible from many contexts. Understanding scope in maths makes scope in Java immediately intuitive.

**Psychology connection — working memory vs long-term memory**

Cognitive science distinguishes between working memory (holds a few items you are actively using) and long-term memory (stores information indefinitely). Local variables are like working memory — they hold temporary values during a task, then release them. Instance fields are like long-term memory — they store the object's persistent knowledge. Just as overloading working memory causes errors, trying to store too much in local variables (when fields are needed) causes program failures.

**Question:** A `BankAccount` has `balance` as an instance field and uses a local variable `interest` inside a `calculateInterest()` method. Explain why `balance` must be a field and `interest` can safely be a local variable, using the concepts of lifetime and scope.

# Lore Conclusion

The distinction between Bound and Transient Runes is now inscribed in your understanding. You know where each piece of data lives, how long it lasts, and what happens when you reach for a rune that has already faded. Next you will learn to give your fields the power to act — turning static state into dynamic behaviour by writing instance methods that read, modify, and combine the fields you have just mastered.
