---
id: se-app-m3-02
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m3
moduleTitle: "Module 3: Functions and Reusability"
moduleGlyph: "⚗️"
moduleSortOrder: 3
topicSlug: methods
topicTitle: "Methods"
topicSortOrder: 1
lesson: creating_methods
title: "Creating Methods"
sortOrder: 2
difficulty: 1
estimatedMinutes: 20
xpReward: 50
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m3-01]
integrationDomains: [philosophy, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Writes a correct public static void method declaration"
    - "Includes a method body with at least one statement"
    - "Calls the method correctly by name with parentheses"
    - "Explains the difference between void and a return type"
    - "Uses a descriptive method name that reflects what it does"
  keywords: [static, void, public, method, call, return, declare, body]
  modelAnswer: |
    public static void printDivider() {
        System.out.println("----------");
    }

    // Call it:
    printDivider();
    // void means the method does not give back a value.
    // A return type like int or String would mean it produces a result.
guidedSteps:
  - id: gs-m3-02-1
    sortOrder: 1
    inputType: CODE
    instruction: |
      Complete the method declaration below so it prints "Spell cast!" when called.
      Fill in the method body.
    inputConfig:
      placeholder: |
        public static void castSpell() {
            // your code here
        }
    markingRule:
      matchMode: CONTAINS
      accepted: ["System.out.println", "Spell cast"]
      rejectedFeedback: "Use System.out.println(\"Spell cast!\"); inside the method body."
    hint: "The method body goes between the curly braces. Use System.out.println() to print."
    reflectionPrompt: "Every method body is wrapped in curly braces. The code inside runs each time you call the method."
  - id: gs-m3-02-2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Now write the line of code that calls (invokes) the castSpell method you just created.
    inputConfig:
      placeholder: "// call the method here"
    markingRule:
      matchMode: REGEX
      accepted: ["castSpell\\s*\\(\\s*\\)\\s*;"]
      rejectedFeedback: "To call a method, write its name followed by () and a semicolon: castSpell();"
    hint: "Method name, open parenthesis, close parenthesis, semicolon."
    reflectionPrompt: "Calling a method is what makes it actually run. Declaring it is just the blueprint."
  - id: gs-m3-02-3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      A method declared as `public static void greet()` returns which type of value?
    inputConfig:
      options:
        - "A String"
        - "An int"
        - "Nothing — void methods do not return a value"
        - "A boolean"
    markingRule:
      matchMode: EXACT
      accepted: ["Nothing — void methods do not return a value"]
      rejectedFeedback: "void is a keyword meaning 'no return value'. The method performs an action but hands nothing back to the caller."
    hint: "Look at the word between 'static' and the method name."
    reflectionPrompt: "void means the method does work but does not produce a value. When a value is needed, replace void with the type (e.g. int, String)."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which keyword in `public static void myMethod()` signals that no value is returned?"
    options: ["public", "static", "void", "myMethod"]
    correctIndex: 2
    feedback: "void means the method performs an action but returns nothing to the caller."
  - type: MULTIPLE_CHOICE
    question: "You have declared a method called `printBanner()`. To make it run, you write:"
    options: ["declare printBanner;", "run printBanner;", "printBanner();", "call printBanner()"]
    correctIndex: 2
    feedback: "Methods are called by writing the method name followed by parentheses and a semicolon: printBanner();"
retrieval:
  recall: "Write the skeleton (header and empty body) of a void method called `displayScore`."
  explain: "Explain the difference between declaring a method and calling a method. Why must you do both?"
  mistakeId:
    code: |
      public static void sayHello() {
          System.out.println("Hello!");
      }
      sayHello;   // attempt to call the method
    answer: "The call is missing parentheses. It should be sayHello(); — without () Java treats sayHello as a variable reference, not a method call."
---

# Hook

You know that functions help avoid repetition. But how do you actually write one in Java? There is a specific syntax — a precise pattern of keywords that the Java compiler recognises as a method. Get one keyword wrong and your code will not compile. Get it right and you have created a reusable incantation that you can call as many times as you like. This lesson teaches you that pattern from first principles.

# Lore Introduction

Archmage Veylan held up his hand and revealed a glowing inscription etched into his palm — a single incantation, sealed in the ancient format. "Every spell has three parts," he said. "Its visibility, its nature, and its name. Miss any one of them and the magical binding fails to form." He traced the words slowly: `public static void`. The apprentices leaned in. The same three words appear at the start of every basic method in the Academy's spellbook. Learn them, and you hold the key to writing your own incantations.

# Core Learning

## Concept Introduction

In Java, the simplest method you can write looks like this:

```java
public static void methodName() {
    // method body
}
```

Each part has a role:

| Part | Meaning |
|---|---|
| `public` | The method can be called from anywhere |
| `static` | The method belongs to the class, not an object |
| `void` | The method does not return a value |
| `methodName` | The name you choose — use camelCase |
| `()` | Parentheses — may hold parameters (covered next lesson) |
| `{ }` | The body — code that runs when the method is called |

To run the method, you **call** it by writing its name with parentheses and a semicolon:

```java
methodName();
```

## Why It Matters

Knowing the exact syntax lets you create methods confidently without guessing. The `public static void` pattern is the standard starting point for methods in a Java `main`-style program. Once you understand what each keyword does, you can read any Java method in any codebase and immediately understand its structure. This is the grammar that all Java methods share.

## Worked Examples

**Example 1 — A simple void method**

```java
public static void printWelcome() {
    System.out.println("Welcome to the Academy!");
    System.out.println("Your training begins now.");
}

// Calling it:
printWelcome();
```

Output:
```
Welcome to the Academy!
Your training begins now.
```

**Example 2 — Calling a method multiple times**

```java
public static void drawLine() {
    System.out.println("====================");
}

public static void main(String[] args) {
    drawLine();
    System.out.println("Exam Results");
    drawLine();
    System.out.println("Pass");
    drawLine();
}
```

Output:
```
====================
Exam Results
====================
Pass
====================
```

The `drawLine()` method is called three times. Its body executes each time.

**Example 3 — Void vs return type (preview)**

```java
// void — does work, returns nothing
public static void printScore(int score) {
    System.out.println("Score: " + score);
}

// int — does work AND returns a value
public static int doubleScore(int score) {
    return score * 2;
}
```

`void` methods are used for side effects (printing, saving). Methods with a return type produce a value you can store or use.

## Common Mistakes

- **Forgetting the parentheses when calling.** `myMethod;` will not call the method — `myMethod();` is required.
- **Writing the method body outside a class.** All Java methods must live inside a class.
- **Calling the method before defining it.** Java generally requires the method to exist in the same class, though the order within the class is flexible.
- **Using a capital letter to start the method name.** Java convention is camelCase starting with a lowercase letter: `printScore`, not `PrintScore`.
- **Confusing `void` with `null`.** `void` is a return type meaning no value is returned. `null` is a value meaning "no object".

## Mental Model

Think of a method declaration as a **named button on a control panel**. Wiring up the button (writing the declaration and body) does nothing on its own — the panel just has a new labelled button. Pressing the button (calling the method) is what triggers the action. You must both wire it *and* press it.

## Mini Summary

- A basic Java method uses `public static void methodName()` as its header.
- `void` means the method does not return a value.
- The method body (the code that runs) lives between `{ }`.
- Call a method by writing `methodName();` — parentheses and semicolon required.
- Java convention: method names start with a lowercase letter in camelCase.
- Declaring a method and calling a method are two separate steps.

# Guided Practice Quest

Work through each step in order.

**Step 1.** Complete the `castSpell()` method so it prints `"Spell cast!"` when called. Write only the body.

**Step 2.** Write the single line of code that calls the `castSpell()` method.

**Step 3.** A method is declared as `public static void greet()`. What type of value does it return?

# Solo Practice Quest

Write a complete, working Java method called `printBanner`. It should print the following three lines:

```
********************
*  Arcane Academy  *
********************
```

Then, below the method, write the code to call it twice. Include a comment above the method explaining in plain English what it does.

Your answer must include the full method declaration, the method body, and both calls.

# Integration

**Mathematics connection — Functions as mappings**

In mathematics, a function maps an input to an output: f(x) = 2x takes x and produces 2x. Void methods in Java are like mathematical procedures rather than functions — they perform operations but do not produce a value to hand back. A Java method with a return type is much closer to a mathematical function: it takes inputs and maps them to an output. Understanding this distinction helps you decide whether a method should be `void` (it does something) or should return a value (it computes something).

**Philosophy connection — Form and substance**

The method signature — `public static void methodName()` — is the *form*. The body — the code between the braces — is the *substance*. Philosophers since Aristotle have debated whether form or substance is more fundamental. In Java, both are necessary: the form provides the name and access rules; the substance provides the actual behaviour. A method with no body would be an empty promise. A body with no name could not be called. Form and substance must work together.

**Free question:** If `void` methods do not return a value, why are they useful? Give two real examples of tasks that are naturally void (they do something but produce no result).

# Lore Conclusion

Veylan watched the apprentices inscribe their first incantations — each one bearing the ritual header `public static void`, followed by a name chosen with care. "The three words are not decorative," he reminded them. "Public tells the tower where the spell can be cast from. Static says it needs no vessel to carry it. Void means it acts without giving anything back." One apprentice raised her hand. "What if we want it to give something back?" Veylan smiled. "Then we replace void with the type of treasure it returns. But that lesson," he said, "comes next."
