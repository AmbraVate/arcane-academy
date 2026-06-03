---
id: se-app-m3-04
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m3
moduleTitle: "Module 3: Functions and Reusability"
moduleGlyph: "⚗️"
moduleSortOrder: 3
topicSlug: methods
topicTitle: "Methods"
topicSortOrder: 1
lesson: return_values
title: "Return Values"
sortOrder: 4
difficulty: 2
estimatedMinutes: 22
xpReward: 50
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m3-03]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Declares a method with a non-void return type"
    - "Uses the return keyword to send a value back"
    - "Stores or uses the returned value at the call site"
    - "Explains the difference between void and a typed return"
    - "Shows understanding that execution stops at return"
  keywords: [return, type, value, store, void, result, declaration, keyword]
  modelAnswer: |
    public static int add(int a, int b) {
        return a + b;  // sends the result back to the caller
    }

    int result = add(3, 4);  // result holds 7
    System.out.println(result);  // 7
guidedSteps:
  - id: gs-m3-04-1
    sortOrder: 1
    inputType: CODE
    instruction: |
      Complete the method below so it returns the square of the given number.
      The method should return an int.
    inputConfig:
      placeholder: |
        public static int square(int n) {
            // your code here
        }
    markingRule:
      matchMode: CONTAINS
      accepted: ["return n * n", "return n*n"]
      rejectedFeedback: "Use: return n * n; — this multiplies n by itself and returns the result."
    hint: "The return type is int. Use the return keyword followed by n * n."
    reflectionPrompt: "The return keyword both produces a value AND immediately ends the method."
  - id: gs-m3-04-2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Write the code to call `square(5)` and store the result in an int variable called `result`, then print it.
    inputConfig:
      placeholder: |
        // call square and store the result
        // then print result
    markingRule:
      matchMode: CONTAINS
      accepted: ["int result", "square(5)", "System.out.println"]
      rejectedFeedback: "Declare an int variable, assign square(5) to it, then print. Example: int result = square(5); System.out.println(result);"
    hint: "int result = square(5); assigns the return value to a variable."
    reflectionPrompt: "A returned value must be caught — either stored in a variable or used directly in an expression."
  - id: gs-m3-04-3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      What happens immediately after a `return` statement executes?
    inputConfig:
      options:
        - "The method continues to the next line"
        - "The method pauses and waits for input"
        - "The method ends and control returns to the caller"
        - "The method restarts from the beginning"
    markingRule:
      matchMode: EXACT
      accepted: ["The method ends and control returns to the caller"]
      rejectedFeedback: "return immediately exits the method. Any code after return in the same block is unreachable."
    hint: "return does two things: sends a value back AND stops the method."
    reflectionPrompt: "Code written after return in the same block is called 'dead code' — it can never execute."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which method header correctly declares a method that returns a String?"
    options:
      - "public static void getName()"
      - "public static String getName()"
      - "public static return String getName()"
      - "public String static getName()"
    correctIndex: 1
    feedback: "The return type goes between `static` and the method name: public static String getName()"
  - type: MULTIPLE_CHOICE
    question: "A method is declared `public static int getScore()`. Which of the following is a valid use of this method?"
    options:
      - "getScore();"
      - "int s = getScore();"
      - "void s = getScore();"
      - "String s = getScore();"
    correctIndex: 1
    feedback: "The method returns an int, so it must be stored in an int variable: int s = getScore();"
retrieval:
  recall: "Write a complete method called `double` that takes an int and returns that int multiplied by 2."
  explain: "Explain what the return keyword does. Why does code after a return statement never execute?"
  mistakeId:
    code: |
      public static int getLevel() {
          int level = 5;
          System.out.println(level);
      }
    answer: "The method declares return type int but has no return statement. It will not compile. Add: return level; after the println, or before it."
---

# Hook

Your `square()` method calculates a value — but where does that value go? Right now it disappears. `void` methods do work but hand nothing back. What if you need the result for a calculation? What if you want to store it, pass it to another method, or display it? This is where return values come in — the method produces a result and sends it back to whoever called it.

# Lore Introduction

Archmage Veylan presented a new kind of incantation to his apprentices — one that did not merely perform an action but *returned* a treasure to the caster. "A void spell acts on the world," he explained. "But a returning spell reaches into the aether and brings something back." He demonstrated: the incantation `calculatePower` reached into the flow of mana, performed its computation, and deposited a glowing rune into the caster's waiting hand. "The return keyword," he said, "is how a spell delivers its gift."

# Core Learning

## Concept Introduction

A method with a **return type** produces a value and sends it back to the caller using the `return` keyword.

```java
// Declare the return type instead of void
public static int square(int n) {
    return n * n;  // sends n * n back to the caller
}

// Use the return value
int result = square(5);   // result = 25
System.out.println(result); // 25
```

The return type replaces `void` in the method header. The `return` statement ends the method immediately and sends the value back.

**Key difference:**

| Void method | Non-void method |
|---|---|
| Performs an action | Computes and returns a value |
| `public static void print()` | `public static int add(int a, int b)` |
| No return statement needed | Must have `return` matching the declared type |
| Called as a statement: `print();` | Result must be used: `int x = add(1,2);` |

## Why It Matters

Returned values are what allow methods to chain together. One method calculates a subtotal, another applies tax, another formats the result for display. Each method calls the previous and uses its return value as input. Without return values, every method would have to print its own output — making it impossible to build up calculations in steps.

## Worked Examples

**Example 1 — Returning an int**

```java
public static int add(int a, int b) {
    return a + b;
}

int sum = add(10, 5);  // sum = 15
```

**Example 2 — Returning a String**

```java
public static String buildGreeting(String name) {
    return "Welcome, " + name + "!";
}

String message = buildGreeting("Aria");
System.out.println(message); // Welcome, Aria!
```

**Example 3 — Using a return value directly**

```java
public static int max(int a, int b) {
    if (a > b) {
        return a;
    }
    return b;
}

System.out.println(max(7, 3));  // 7
System.out.println(max(2, 9));  // 9
```

Note: there are two `return` statements, but only one executes each time the method runs.

## Common Mistakes

- **Declaring void but trying to return a value.** If the header says `void`, the method cannot return anything.
- **Forgetting to use the return value.** Calling `square(5)` without storing the result discards it entirely.
- **Returning the wrong type.** A method declared as `int` must `return` an `int`, not a `String`.
- **Writing code after return.** Any line after a `return` in the same block is unreachable. Many IDEs will warn you about this.
- **Not returning on all paths.** If your method has an `if` block with a `return`, the `else` path must also have a `return`. Java will give a compile error if any path can end without returning.

## Mental Model

Think of a void method as a **vending machine action button** — you press it and the machine does something (lights flash, a mechanism moves). A return-value method is more like an **ATM withdrawal** — you put in your card and amount, and the machine *gives something back* to you. You take the money (the return value) and decide what to do with it.

## Mini Summary

- Replace `void` with a type (`int`, `String`, etc.) to make a method return a value.
- Use the `return` keyword followed by the value to send it back to the caller.
- `return` immediately ends the method — no further lines execute.
- The returned value must be used: stored in a variable or used in an expression.
- All paths through a non-void method must reach a `return` statement.
- Void methods do; non-void methods compute and deliver.

# Guided Practice Quest

Work through each step in order.

**Step 1.** Complete the `square(int n)` method so it returns `n * n`.

**Step 2.** Write code to call `square(5)`, store the result in an `int` called `result`, and print it.

**Step 3.** What happens immediately after a `return` statement executes?

# Solo Practice Quest

Write a method called `clamp` that takes three parameters: `int value`, `int min`, `int max`. The method should return:
- `min` if value is less than min
- `max` if value is greater than max
- `value` otherwise

Then write three calls to demonstrate each case, storing and printing each result. Include a comment explaining what "return type" means.

# Integration

**Mathematics connection — Functions as value producers**

In mathematics, every function has a codomain — the set of possible output values. A function f: ℤ → ℤ (integers to integers) always produces an integer. Java's return types enforce exactly this contract: `public static int square(int n)` declares that the domain is `int` and the codomain is `int`. The Java compiler verifies the contract at compile time. This is a direct implementation of mathematical function typing, and understanding it makes reading type signatures natural.

**Philosophy connection — Cause and effect**

A void method embodies one-way causation: you cause an effect (a print, a save) but receive nothing in return. A method with a return value establishes a two-way relationship: you provide inputs, and the universe (the method) gives back a result. Philosophers of science distinguish between *actions* (one-way) and *transactions* (two-way). Designing software well often means deciding whether an operation should be an action or a transaction — and using void vs return types to make that decision explicit in code.

**Free question:** Could you rewrite `square(int n)` as a void method that prints the result instead of returning it? What would you lose? When is each approach better?

# Lore Conclusion

The apprentice held out her hand as the incantation completed, and a glowing integer materialized in her palm — the result of the computation. "It returned something," she breathed. Veylan nodded. "A spell that returns speaks a truth: here is what I computed, do with it what you will. A void spell acts on the world silently." He gestured at the Tome. "Learn to know when your spell should act and when it should deliver. That choice shapes the architecture of all the magic you will ever write."
