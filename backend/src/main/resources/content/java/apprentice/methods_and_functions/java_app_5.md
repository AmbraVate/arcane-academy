---
moduleId: java-app-5
moduleTitle: "Module 5: Methods & Functions"
moduleGlyph: "⚗️"
moduleSortOrder: 5
domainId: java
tier: APPRENTICE
topicSlug: methods_and_functions
topicTitle: "Methods & Functions"
topicSortOrder: 5
id: java-app-5a
title: "Parameters, Return Types & Scope"
sortOrder: 1
xpReward: 70
practiceType: JAVA
questType: KNOWLEDGE
feynmanPrompt: "Explain what a method is and why we use them, as if describing a recipe card to someone who has never cooked before."
learningObjectives:
  - Define a method with a return type, name, and parameters
  - Call a method and use its return value
  - Explain variable scope and why a variable inside a method cannot be seen outside it
  - Distinguish void methods (do something) from value-returning methods (compute something)
integrationDomains:
  - mathematics
  - psychology
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - Defines at least one method with a return type (not void)
    - The method has at least one parameter
    - The method body contains a return statement returning the correct type
    - The method is called from main() and the result is printed or used
    - Variable names inside the method are meaningful and do not conflict with main()
  keywords:
    - method
    - parameter
    - return
    - void
    - call
    - scope
    - static
  modelAnswer: |
    Here is one valid solution — a method that calculates spell damage:

    ```java
    public static int calculateDamage(int basePower, int spellLevel) {
        return basePower * spellLevel;
    }

    public static void main(String[] args) {
        int damage = calculateDamage(10, 3);
        System.out.println("Damage dealt: " + damage);
    }
    // Output: Damage dealt: 30
    ```

    Key checks:
    - `int` before the method name is the *return type* — the type of value the method produces.
    - Parameters are listed inside `()` as `type name` pairs, separated by commas.
    - `return` sends the value back to the caller — without it the method can't produce a result.
    - Calling `calculateDamage(10, 3)` passes 10 as `basePower` and 3 as `spellLevel`.

guidedSteps:
  - id: method-step-1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      A method that **adds two numbers** and gives back the result should have a return type of `int`.

      Complete the method signature (the first line only):

      ```java
      ___ add(int a, int b) {
          return a + b;
      }
      ```
    inputConfig:
      placeholder: "return type + method name"
    markingRule:
      matchMode: NORMALIZED
      accepted:
        - public static int add
        - static int add
        - int add
        - "public static int add"
        - "static int add"
      rejectedFeedback: "The method produces a whole number — so its return type is `int`. The full signature is `public static int add(int a, int b)`. In a simple class with one method, `static int add` also compiles."
    hint: "A method that *returns* an `int` has the word `int` just before the method name. In a class with a static `main`, the helper method also needs `static`."
    reflectionPrompt: "Correct! `public static int add(int a, int b)` tells Java: this method is accessible everywhere (`public`), doesn't need an object (`static`), produces a whole number (`int`), and is named `add`. The parameters `int a` and `int b` are the inputs."

  - id: method-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does the following code print?

      ```java
      public static int double(int x) {
          return x * 2;
      }

      public static void main(String[] args) {
          int result = double(7);
          System.out.println(result);
      }
      ```

      *(Ignore the fact that `double` is a reserved keyword — treat it as a valid name here for the exercise.)*
    inputConfig:
      options:
        - "7"
        - "14"
        - "2"
        - "Nothing — it doesn't compile"
    markingRule:
      matchMode: NORMALIZED
      accepted:
        - "14"
        - 14
      rejectedFeedback: "`double(7)` calls the method with `x = 7`. The method returns `7 * 2 = 14`. That value is stored in `result` and then printed."
    hint: "Trace the call: `double(7)` → `x = 7` → `return 7 * 2` → `return 14`. That value comes back to `main` and is stored in `result`."
    reflectionPrompt: "Exactly! The method takes `x = 7`, computes `7 * 2 = 14`, and returns `14`. `main` receives `14` into `result` and prints it. This is the *call-return* cycle that every method follows."

  - id: method-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Look at this code:

      ```java
      public static void greet(String name) {
          String message = "Hello, " + name + "!";
          System.out.println(message);
      }

      public static void main(String[] args) {
          greet("Elara");
          System.out.println(message); // ← will this compile?
      }
      ```

      Will the last line compile? Explain **why or why not** in one or two sentences.
    inputConfig:
      minWords: 8
    markingRule:
      matchMode: CONTAINS
      accepted:
        - scope
        - outside
        - not visible
        - cannot see
        - only exists
        - local
        - declared inside
        - defined inside
      rejectedFeedback: "The variable `message` is declared *inside* `greet()`. It only exists within that method. Once `greet` returns, `message` is gone. `main` cannot see it — this is called **scope**."
    hint: "Variables only exist in the block (the `{}`) where they are declared. `message` lives in `greet`'s block — not in `main`'s."
    reflectionPrompt: "Right! `message` is a *local variable* of `greet` — its scope is confined to that method's body. After `greet` returns, `message` is gone. `main` can't access it, so `System.out.println(message)` is a compile error: 'cannot find symbol: variable message'. Scope rules prevent accidental interference between methods."
---

# Hook

A spell written once. Cast a thousand times.

That is the promise of the method: define the logic in one place, name it clearly, and invoke it wherever you need. The best code you will ever write is code you wrote once and never need to write again.

# Lore Introduction

In the Academy's Spell Repository, each incantation is inscribed on its own scroll with a precise name. When a caster needs to invoke *Fireball*, they do not rewrite the incantation — they simply say the word and provide the required components.

> *"A method,"* Archmage Veylan explains, *"is a named incantation. You define it once, in full. You call it by name, provide what it needs, and collect what it gives back. Repetition is the enemy of craft — methods are its antidote."*

# Core Learning

## Concept Introduction

A **method** is a named, reusable block of code that:
1. Optionally **receives inputs** (parameters)
2. **Does work** (the body)
3. Optionally **returns a value** (the return type)

The anatomy of a method:

```java
//  access  static  return-type  name         parameters
    public  static  int          add     (int a, int b) {
        return a + b;   // body — the work
    }
```

| Part          | Meaning                                         |
|---------------|-------------------------------------------------|
| `public`      | Other code can call this method                 |
| `static`      | Belongs to the class, not an object instance    |
| `int`         | The type of value this method hands back        |
| `add`         | The name — used when calling the method         |
| `int a, int b`| Parameters — inputs the caller must provide     |
| `return a + b`| The value handed back to the caller             |

When a method does not return a value, use `void`:

```java
public static void printWelcome(String name) {
    System.out.println("Welcome, " + name + "!");
    // no return statement needed
}
```

## Why It Matters

Without methods, every piece of logic would be written inline, repeated wherever it is needed. When the logic changes, every copy would need updating — a guarantee of bugs. Methods enforce the DRY principle (*Don't Repeat Yourself*) and make code testable, readable, and maintainable.

They also enable **decomposition**: breaking a large problem into small, named units. A well-named method (`calculateTax`, `findMax`, `isValidPassword`) is self-documenting — you understand what it does without reading the body.

## Worked Examples

**Example 1 — A method that computes and returns**

```java
public static double circleArea(double radius) {
    return Math.PI * radius * radius;
}

// Calling the method
double area = circleArea(5.0);
System.out.println("Area: " + area);  // Area: 78.53981633974483
```

**Example 2 — A void method that performs an action**

```java
public static void printSeparator(int width) {
    for (int i = 0; i < width; i++) {
        System.out.print("-");
    }
    System.out.println();
}

printSeparator(20); // --------------------
```

**Example 3 — Calling a method inside another method**

```java
public static int clamp(int value, int min, int max) {
    if (value < min) return min;
    if (value > max) return max;
    return value;
}

public static void main(String[] args) {
    System.out.println(clamp(150, 0, 100));  // 100
    System.out.println(clamp(-5,  0, 100));  // 0
    System.out.println(clamp(42,  0, 100));  // 42
}
```

## Common Mistakes

- Forgetting `return` in a non-void method — the compiler will catch this: *"missing return statement"*.
- Returning a value from a `void` method — also a compile error.
- Confusing the parameter (declaration) with the argument (value you pass at the call site) — `add(int a, int b)` declares parameters; `add(3, 5)` passes arguments `3` and `5`.
- Trying to use a local variable outside the method where it was declared — scope violation.
- Writing the method inside `main` — methods are defined at the class level, not nested inside each other (unless using lambda expressions, which comes later).

## Mental Model

Think of a method as a **vending machine**.

The machine has a **name** on its front panel (the method name). You choose it by pressing its button (calling it). You insert **coins** (arguments — the inputs). The machine does its internal processing (the method body). Out comes a **product** (the return value) — or, for void methods, a service is performed (like dispensing a napkin with no monetary value returned).

The machine's internals — the gears, the wiring — are hidden from you. You only care about what goes in and what comes out. That is **encapsulation**.

## Mini Summary

- A method has a **return type**, a **name**, zero or more **parameters**, and a **body**.
- `void` methods perform an action but hand nothing back; non-void methods return a value.
- Parameters are the method's inputs; the caller supplies **arguments** at the call site.
- Variables declared inside a method are **local** — they cannot be accessed outside their method.
- Methods make code **DRY** — define once, call many times.

# Guided Practice Quest

Write a method that computes spell damage and call it from `main`.

Define:
```
public static int spellDamage(int basePower, int level)
```
It should return `basePower * level`.

In `main`, call `spellDamage` with two different pairs of values, store each result in a variable, and print both.

**Expected output** (for `basePower=10, level=3` and `basePower=25, level=2`):
```
30
50
```

# Solo Practice Quest

Write a **temperature converter** method:

```
public static double celsiusToFahrenheit(double celsius)
```

Formula: `fahrenheit = celsius * 9.0 / 5.0 + 32`

Call it from `main` with at least three different values (include 0°C, 100°C, and one of your choice). Print each result.

Then write a second method — `fahrenheitToCelsius` — that performs the reverse. Demonstrate it with one call.

# Integration

**Connecting to Mathematics — Functions**

A Java method that takes inputs and returns a value is the programming equivalent of a mathematical function. `f(x) = 2x + 1` maps an input `x` to an output — just as `static int doubled(int x) { return 2*x + 1; }` maps an integer to an integer. The mathematical notions of *domain* (valid inputs — the parameter type) and *codomain* (valid outputs — the return type) correspond directly. Pure methods (those with no side effects, depending only on their parameters) are *referentially transparent*: `doubled(3)` is always `7`, no matter when or where you call it.

**Connecting to Psychology — Chunking**

Cognitive load theory (Sweller, 1988) shows that the human mind can hold roughly 4 ± 2 items in working memory simultaneously. When you read code without methods, every detail competes for that limited capacity. When code is decomposed into named methods, each method becomes a single *chunk* — an atomic unit you understand as a whole without tracking its internals. `calculateTax(income)` is one thought, not 20. Good method decomposition literally makes code easier to reason about, because it respects the limits of human cognition.

# Lore Conclusion

The apprentice sealed the scroll and placed it on the Repository shelf.

*"One spell,"* said Archmage Veylan, *"written with care and named with precision, is worth a hundred spells scrawled in haste. Call it by name. Trust what it gives back. That is the discipline of the method."*

From this day forward, the apprentice would never write the same logic twice.
