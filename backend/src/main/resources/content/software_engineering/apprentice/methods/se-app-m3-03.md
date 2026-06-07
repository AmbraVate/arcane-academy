---
id: se-app-m3-03
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
lesson: parameters
title: "Parameters"
sortOrder: 3
difficulty: 2
estimatedMinutes: 22
xpReward: 50
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m3-02]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Declares a method with at least one parameter with its type"
    - "Distinguishes between a parameter (in the declaration) and an argument (in the call)"
    - "Correctly passes values to the method when calling it"
    - "Declares a method with two parameters of different types"
    - "Explains why parameters make methods more flexible"
  keywords: [parameter, argument, type, declare, pass, value, flexible, method]
  modelAnswer: |
    // Parameter: variable declared in the method signature
    // Argument: actual value passed when calling the method
    public static void greetPlayer(String name, int level) {
        System.out.println("Welcome, " + name + "! Level: " + level);
    }

    greetPlayer("Aria", 5); // "Aria" and 5 are arguments
guidedSteps:
  - id: gs-m3-03-1
    sortOrder: 1
    inputType: CODE
    instruction: |
      Complete this method so it accepts a player's name as a String parameter and prints:
      "Greetings, [name]!"
    inputConfig:
      placeholder: |
        public static void greetPlayer(_____ name) {
            // your code here
        }
    markingRule:
      matchMode: CONTAINS
      accepted: ["String name", "System.out.println", "name"]
      rejectedFeedback: "The parameter type should be String. The body should print the name using: System.out.println(\"Greetings, \" + name + \"!\");"
    hint: "The parameter type comes before the parameter name inside the parentheses."
    reflectionPrompt: "String before the name tells Java what type of value to expect when the method is called."
  - id: gs-m3-03-2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Write the call to greetPlayer that passes the name "Veylan".
    inputConfig:
      placeholder: "// call greetPlayer here"
    markingRule:
      matchMode: REGEX
      accepted: ["greetPlayer\\s*\\(\\s*\"Veylan\"\\s*\\)\\s*;"]
      rejectedFeedback: "Call the method with the String argument in quotes: greetPlayer(\"Veylan\");"
    hint: "String values are passed with double quotes around them."
    reflectionPrompt: "The value you pass (the argument) must match the type declared in the parameter list."
  - id: gs-m3-03-3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      A method is declared as `public static void addPoints(String player, int points)`.
      Which call is correct?
    inputConfig:
      options:
        - "addPoints(10, \"Aria\");"
        - "addPoints(\"Aria\", 10);"
        - "addPoints(\"Aria\");"
        - "addPoints(Aria, 10);"
    markingRule:
      matchMode: EXACT
      accepted: ["addPoints(\"Aria\", 10);"]
      rejectedFeedback: "Arguments must match the parameter order and types. String first (with quotes), then int."
    hint: "Check the order: String player comes first, then int points."
    reflectionPrompt: "Java matches arguments to parameters by position. Order and type must match."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What is the difference between a parameter and an argument?"
    options:
      - "They are exactly the same thing"
      - "A parameter is in the method declaration; an argument is the value passed when calling"
      - "A parameter is a value; an argument is a type"
      - "Parameters are for int only; arguments are for String only"
    correctIndex: 1
    feedback: "Parameter: declared in the method header. Argument: the actual value supplied at the call site."
  - type: MULTIPLE_CHOICE
    question: "A method `printScore(int score)` is called with `printScore(42)`. Inside the method, what is the value of `score`?"
    options: ["0", "42", "null", "undefined"]
    correctIndex: 1
    feedback: "42 is passed as the argument. Inside the method, the parameter `score` holds the value 42."
retrieval:
  recall: "Write a method header for a method called `multiply` that takes two int parameters named `a` and `b` and returns void."
  explain: "Explain what happens step-by-step when you call `greetPlayer(\"Aria\")` on a method declared as `public static void greetPlayer(String name)`."
  mistakeId:
    code: |
      public static void showLevel(int level) {
          System.out.println("Level: " + level);
      }
      showLevel("five"); // calling with a String
    answer: "The method expects an int but receives a String. Java will not compile this. Pass an integer literal instead: showLevel(5);"
---

# Hook

A method that always prints "Welcome, adventurer!" is useful — but what if you want to greet different players by name? You would need a different method for every name. That defeats the purpose of reusability. Parameters solve this: they are placeholders in your method that get filled in with real values each time you call it. One method, infinite flexibility.

# Lore Introduction

Archmage Veylan demonstrated a new incantation that could greet any visitor to the Academy by name. The rune vessel in the spell's signature read `name` — a placeholder that the caster filled with a real name at the moment of casting. "An incantation with a vessel," he explained, "adapts to whatever is poured into it. Without vessels, every greeting would need its own spell. With them, one spell serves all." The apprentices named these vessels *parameters* — the slots that make a spell flexible.

# Core Learning

## Concept Introduction

A **parameter** is a variable declared in a method's header. It acts as a placeholder for a value that will be supplied when the method is called. The actual value supplied at the call site is called an **argument**.

```java
// "name" is a parameter — a placeholder
public static void greetPlayer(String name) {
    System.out.println("Welcome, " + name + "!");
}

// "Aria" is the argument — the real value
greetPlayer("Aria");   // prints: Welcome, Aria!
greetPlayer("Veylan"); // prints: Welcome, Veylan!
```

**Multiple parameters** are separated by commas. Each must have its own type:

```java
public static void showStats(String player, int level, int score) {
    System.out.println(player + " | Level: " + level + " | Score: " + score);
}

showStats("Aria", 3, 150);
```

## Why It Matters

Without parameters, a method can only do one fixed thing. Parameters transform a method from a single-use script into a reusable tool that adapts to different inputs. This is what makes methods genuinely powerful — the same method body can produce different results depending on what is passed in.

## Worked Examples

**Example 1 — Single String parameter**

```java
public static void announce(String message) {
    System.out.println(">>> " + message + " <<<");
}

announce("Quest begins!");  // >>> Quest begins! <<<
announce("Level up!");      // >>> Level up! <<<
```

**Example 2 — Multiple parameters**

```java
public static void printProduct(int a, int b) {
    System.out.println(a + " x " + b + " = " + (a * b));
}

printProduct(3, 4);  // 3 x 4 = 12
printProduct(7, 8);  // 7 x 8 = 56
```

**Example 3 — Mixed types**

```java
public static void describeSpell(String spellName, int manaCost, boolean isRanged) {
    System.out.println(spellName + " costs " + manaCost + " mana. Ranged: " + isRanged);
}

describeSpell("Fireball", 30, true);
// Fireball costs 30 mana. Ranged: true
```

## Common Mistakes

- **Wrong argument order.** `addPoints("Aria", 10)` and `addPoints(10, "Aria")` look similar but mean different things — Java matches arguments to parameters by position.
- **Wrong argument type.** Passing a `String` where an `int` is expected causes a compile error.
- **Forgetting quotes around String arguments.** `greetPlayer(Aria)` will fail — Java treats `Aria` as a variable name, not a String. Use `greetPlayer("Aria")`.
- **Reusing the parameter name outside the method.** A parameter only exists inside the method body (this is scope — covered in the next lesson).
- **Too many parameters.** A method with more than three or four parameters is usually a sign it is doing too much. Consider grouping related data.

## Mental Model

Think of a method with parameters as a **form with blank fields**. The form defines what information is needed (`name: ___, age: ___`). Each time you submit the form, you fill in the blanks with real values. The method body uses those filled-in values to do its work. The blanks in the form are parameters; the values you write in are arguments.

## Mini Summary

- Parameters are variables declared in the method header, acting as placeholders.
- Arguments are the actual values passed when the method is called.
- Each parameter must have a declared type (e.g. `int`, `String`).
- Multiple parameters are separated by commas.
- Arguments must match parameters in number, order, and type.
- Parameters make methods reusable across different inputs.

# Guided Practice Quest

Work through each step in order.

**Step 1.** Complete the `greetPlayer` method so it accepts a `String` parameter called `name` and prints `"Greetings, [name]!"`.

**Step 2.** Write the single line that calls `greetPlayer` and passes the name `"Veylan"`.

**Step 3.** A method is declared as `addPoints(String player, int points)`. Which call is correct?

# Solo Practice Quest

Write a method called `describeItem` that takes three parameters:
- `String itemName`
- `int weight` (in kg)
- `boolean isMagical`

The method should print a single line in this format:
```
[itemName] | Weight: [weight]kg | Magical: [isMagical]
```

Then call the method twice with different items. Include a comment explaining what a parameter is versus what an argument is.

# Integration

**Mathematics connection — Functions and substitution**

In mathematics, a function f(x) = x² + 1 takes a value x and substitutes it into the expression. When you write f(3), the 3 replaces every x: 3² + 1 = 10. Java method parameters work identically: the parameter name is a placeholder, and the argument is substituted in when the method runs. This is why parameters in mathematics are sometimes called "formal parameters" and arguments are called "actual parameters" — the same terminology used in some programming languages.

**Psychology connection — Working memory and abstraction**

Cognitive psychology research shows that giving a chunk of information a single label reduces its demand on working memory. A method called `describeSpell(name, cost, ranged)` lets you reason about spell description as a single concept. Without parameters, you would need a separate labelled method for each possible spell — hundreds of names to remember instead of one flexible incantation. Parameters extend the power of naming by making the label work across a whole family of related operations.

**Free question:** A method currently has five parameters. A teammate suggests it is "too complex". What might they suggest instead, and do you agree? What are the tradeoffs?

# Lore Conclusion

Veylan watched his apprentices cast their first parameterised incantations, each pouring a different name into the waiting vessel and watching the greeting change. "A rigid spell," he noted, "is a tool for one job. A spell with vessels is a tool for all jobs of that kind." He paused to let the lesson settle. "The vessel is not the spell. The vessel is the *possibility* within the spell." Outside, a new apprentice arrived at the Academy gates. Without opening a new spellbook, Veylan simply murmured: `greet("Lyra")` — and the tower's lights blazed a welcome.
