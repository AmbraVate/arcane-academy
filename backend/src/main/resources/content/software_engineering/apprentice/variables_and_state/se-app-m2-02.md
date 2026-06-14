---
id: se-app-m2-02
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
topicSlug: variables_and_state
topicTitle: "Variables and State"
topicSortOrder: 1
lesson: variables
title: "Variables"
sortOrder: 2
difficulty: 1
estimatedMinutes: 22
xpReward: 40
practiceType: JAVA
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-01]
integrationDomains: [mathematics, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Declares at least three variables using correct Java syntax (type name = value;)"
    - "Uses appropriate types (int, String, boolean) for each value"
    - "Assigns a new value to an existing variable correctly"
    - "Uses meaningful variable names that describe the stored value"
    - "Code compiles without syntax errors"
  keywords: [variable, declare, assign, type, int, String, boolean, value, name]
  modelAnswer: |
    ```java
    public class AdventureHero {
        public static void main(String[] args) {
            // Declare and initialise variables
            int gold = 100;
            String heroName = "Lyra";
            boolean isAlive = true;
            int health = 85;

            // Print initial state
            System.out.println(heroName + " has " + gold + " gold.");
            System.out.println("Health: " + health + ", Alive: " + isAlive);

            // Update a variable
            gold = gold + 50;
            System.out.println("After reward: " + gold + " gold.");
        }
    }
    ```
guidedSteps:
  - id: se-app-m2-02-step1
    sortOrder: 1
    inputType: FILL_BLANK
    instruction: |
      Complete the variable declaration below to store the number of spell charges a wizard has. The value should start at 5.

      ```java
      ___ spellCharges = 5;
      ```
    inputConfig:
      placeholder: "type keyword"
    markingRule:
      matchMode: EXACT
      accepted: ["int"]
      rejectedFeedback: "The value 5 is a whole number, so the correct type is `int`. The full declaration is: `int spellCharges = 5;`"
    hint: "What Java type holds whole numbers (integers)?"
    reflectionPrompt: "`int` is the type for whole numbers. Since spell charges are always a whole number (you can't have 2.5 charges), `int` is the right choice."

  - id: se-app-m2-02-step2
    sortOrder: 2
    inputType: CODE
    instruction: |
      Declare a variable called `gold` that stores the integer value `100`, then declare a variable called `heroName` that stores the String `"Lyra"`. Write both declarations on separate lines.
    inputConfig:
      placeholder: "Write your two variable declarations here"
    markingRule:
      matchMode: REGEX
      accepted: ["int\\s+gold\\s*=\\s*100\\s*;", "String\\s+heroName\\s*=\\s*\"Lyra\"\\s*;"]
      rejectedFeedback: "You need two lines: `int gold = 100;` and `String heroName = \"Lyra\";`. Remember that String starts with a capital S, and text values go inside double quotes."
    hint: "Integer values use `int`, text values use `String`. String always has a capital S in Java."
    reflectionPrompt: "Notice how the type comes first, then the name, then `=`, then the value. This order is always the same in Java: `type name = value;`"

  - id: se-app-m2-02-step3
    sortOrder: 3
    inputType: MULTIPLE_CHOICE
    instruction: |
      After running the following code, what is the value of `gold`?

      ```java
      int gold = 100;
      gold = 250;
      ```
    inputConfig:
      options:
        - "100"
        - "250"
        - "350"
        - "The code will not compile"
    markingRule:
      matchMode: EXACT
      accepted: ["250"]
      rejectedFeedback: "The second line `gold = 250;` reassigns the variable. The original value 100 is replaced by 250. Variables can be updated — that is their purpose."
    hint: "The second line changes what is stored in gold. What does it change it to?"
    reflectionPrompt: "Variables can be updated at any time by assigning a new value. The old value is simply replaced. This is the essence of mutable state."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "Which of the following correctly declares an integer variable called `score` with a value of 0?"
    options:
      - "score int = 0;"
      - "int score = 0;"
      - "int = score 0;"
      - "integer score = 0;"
    correctIndex: 1
    feedback: "Java variable declarations follow the pattern: `type name = value;`. So `int score = 0;` is correct. The type always comes first."

  - type: MULTIPLE_CHOICE
    question: "What is a variable in Java?"
    options:
      - "A command that prints text to the screen"
      - "A named storage location that holds a value of a specific type"
      - "A type of loop that repeats code"
      - "A method that performs a calculation"
    correctIndex: 1
    feedback: "A variable is a named storage location. It has a type (what kind of value it holds), a name (how you refer to it), and a value (what is currently stored in it)."

retrieval:
  recall: "Write the Java syntax pattern for declaring and initialising a variable."
  explain: "Explain why a variable must have a type in Java. What problem does specifying the type solve?"
  mistakeId:
    code: |
      String points = 42;
      int playerName = "Zara";
    answer: "Both lines have mismatched types. `points` is declared as a `String` but assigned an integer value `42` — it should be `int points = 42;`. `playerName` is declared as `int` but assigned a text value `\"Zara\"` — it should be `String playerName = \"Zara\";`. In Java, the type must match the value being stored."
---

# Hook

Every wizard worth their robes knows a spell must be *bound* before it can be used. An unbound spell is just energy — formless, inaccessible, impossible to invoke. Variables are exactly this binding: they give a name and a form to a value that would otherwise float loose in memory, ungraspable. When you write `int gold = 100;`, you are not just storing a number — you are performing an act of naming, and naming is how humans (and computers) gain control over the world. What would programming look like if variables did not exist?

# Lore Introduction

"Every rune vessel begins with three inscriptions," Archmage Veylan says, gesturing to a glowing receptacle on the Academy's central altar. "Its substance — what kind of essence it may hold. Its name — how it shall be called upon. And its charge — the value it carries at this moment." In Arcane Academy, variables are the rune vessels of computation: named containers that hold a specific kind of value. A vessel inscribed for gold cannot hold a name, and a vessel inscribed for a truth-rune cannot hold a number. The Academy's fundamental law of binding governs all: *type, then name, then value*.

# Core Learning

## Concept Introduction

A **variable** is a named storage location that holds a value of a specific type. In Java, you declare a variable using this pattern:

```
type name = value;
```

| Part | Example | Meaning |
|------|---------|---------|
| `type` | `int` | What kind of value this variable holds |
| `name` | `gold` | The label used to refer to this variable |
| `=` | `=` | Assignment — store this value in this variable |
| `value` | `100` | The initial value being stored |
| `;` | `;` | End of statement |

**Common examples:**
```java
int gold = 100;           // holds a whole number
String heroName = "Lyra"; // holds text
boolean isAlive = true;   // holds true or false
double health = 98.6;     // holds a decimal number
```

Once declared, a variable can be read (used in expressions) and written to (assigned a new value).

## Why It Matters

Variables are the atoms of every program. Without them, you could not store a user's name, track a score, or remember any value between instructions. Every feature you have ever used in software — login names, shopping carts, game progress, settings — is built on variables. Learning to declare, name, and update variables correctly is the single most important skill in programming.

## Worked Examples

**Example 1 — Declaring and using a variable:**
```java
int gold = 100;
System.out.println(gold); // prints: 100
```
The variable `gold` is declared as an `int` and given the value `100`. The second line reads from `gold` and prints it.

**Example 2 — Changing a variable's value:**
```java
int gold = 100;
gold = 250;               // reassign — old value 100 is replaced
System.out.println(gold); // prints: 250
```
The second line reassigns `gold`. After this, `gold` holds `250`. The original `100` is gone.

**Example 3 — Multiple variables working together:**
```java
String heroName = "Lyra";
int level = 5;
int gold = 100;

System.out.println(heroName + " is level " + level + " and has " + gold + " gold.");
// prints: Lyra is level 5 and has 100 gold.
```
Multiple variables of different types can be combined using the `+` operator to build messages.

## Common Mistakes

- **Wrong order:** Writing `gold int = 100;` instead of `int gold = 100;`. The type always comes first.
- **Using `integer` instead of `int`:** In Java, the primitive integer type is spelled `int`, not `integer`.
- **Forgetting the semicolon:** Every statement in Java ends with `;`. Missing it causes a compile error.
- **Using the wrong type:** Storing text in an `int` variable (`int name = "Zara";`) causes a type mismatch error.
- **Forgetting quotes around Strings:** `String name = Zara;` will not compile. Text values must be in double quotes: `"Zara"`.

## Mental Model

Think of a variable like a **labelled jar in a pantry**. The label is the variable's name (`gold`). The type of jar determines what can go inside — a jar labelled "Spices" cannot hold liquid. The contents are the current value (`100`). You can take out what is inside, look at it, use it in a recipe, and put something new back in. The jar remains — only its contents change. Java's type system ensures you never accidentally pour water into a spice jar.

## Mini Summary

- A variable is a named storage location that holds a value of a specific type.
- Declaration syntax: `type name = value;`
- Common types: `int` (whole numbers), `String` (text), `boolean` (true/false), `double` (decimals).
- Variables can be reassigned — the old value is replaced by the new one.
- The type always comes first in a declaration, followed by the name, then `=`, then the value.
- Every declaration statement ends with a semicolon `;`.

# Guided Practice Quest

*Archmage Veylan hands you a blank rune vessel and a chisel. "Inscribe it correctly," he says, "and it will hold your spell's essence. Inscribe it wrongly, and the vessel shatters." Complete the steps above to practise declaring and using variables.*

# Solo Practice Quest

**The Hero's Ledger**

Create a small Java program that represents a hero at the start of an adventure. Declare the following variables:
- The hero's name (a String)
- The hero's starting health (an int, value 100)
- The hero's starting gold (an int, value 50)
- Whether the hero has a weapon equipped (a boolean, value true)

Then print a summary line using all four variables, such as:
`"Kira starts with 100 health, 50 gold, and weapon equipped: true"`

Finally, update the gold variable to reflect the hero spending 20 gold on a potion. Print the updated gold amount.

# Integration

**Mathematics connection:** Variables in Java are directly descended from algebraic variables. When a mathematician writes `x = 5`, they are binding the symbol `x` to the value `5` — exactly what Java does with `int x = 5;`. However, there is a crucial difference: in algebra, `x = 5` is a statement of equality (x *is* 5). In Java, `=` means *assignment* (store 5 *into* x). This distinction matters enormously: in Java, `x = x + 1` is perfectly valid (add 1 to x's current value, store the result back), but in algebra it would be a contradiction. Programming inherited the symbol but changed the meaning.

**Philosophy connection:** The act of naming is one of humanity's oldest forms of power — a theme found in mythology, philosophy, and linguistics alike. In Plato's *Cratylus*, Socrates debates whether names have inherent meaning or are merely conventions. In computing, naming conventions are deliberate: a variable named `x` tells you nothing; a variable named `playerHealthPoints` tells you everything. The philosophy of naming — choosing names that accurately represent the thing they refer to — is just as important in code as in any other form of human communication.

*Free question: What do you think would happen if two variables in the same program had exactly the same name? How do you think Java handles this?*

# Lore Conclusion

Archmage Veylan examines your completed rune vessels and nods with quiet satisfaction. "You have bound three essences correctly," he says. "Name, substance, and charge — all inscribed in the proper order." A vessel for gold glows amber, a vessel for a hero's name shimmers silver, and a vessel for truth pulses with pale light. You have taken the first step: from formless memory, you have carved meaning. In the next lesson, you will learn the Academy's naming laws — why a vessel called `g` is a poor vessel, and why `goldRemainingAfterPurchase` serves the spellcaster far better.
