---
id: se-app-m3-05
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
lesson: method_scope
title: "Method Scope"
sortOrder: 5
difficulty: 2
estimatedMinutes: 20
xpReward: 50
practiceType: JAVA
questType: KNOWLEDGE
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m3-04]
integrationDomains: [philosophy, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Defines scope as the region of code where a variable exists"
    - "Explains that a local variable cannot be accessed outside its method"
    - "Explains that two methods can each have a variable with the same name without conflict"
    - "Identifies a scope error and explains how to fix it"
    - "Connects scope to the safety of encapsulation"
  keywords: [scope, local, variable, method, block, declare, access, encapsulation]
  modelAnswer: |
    public static void methodA() {
        int count = 10;  // local to methodA
        // count is accessible here
    }

    public static void methodB() {
        // count is NOT accessible here — it is out of scope
        // each method has its own independent 'count' if needed
        int count = 20;  // a separate variable, also called count
    }
guidedSteps:
  - id: gs-m3-05-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      Variable `x` is declared inside `methodA`. Can it be read inside `methodB`?
    inputConfig:
      options:
        - "Yes, all variables are shared between methods"
        - "No, x is local to methodA and does not exist in methodB"
        - "Yes, but only if both methods are in the same class"
        - "No, but only if x is an int"
    markingRule:
      matchMode: EXACT
      accepted: ["No, x is local to methodA and does not exist in methodB"]
      rejectedFeedback: "A variable declared inside a method is local to that method. It cannot be seen by other methods."
    hint: "Local means confined to the block where it was declared."
    reflectionPrompt: "Scope keeps each method self-contained. A variable in one method does not pollute or affect another."
  - id: gs-m3-05-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      Two methods both declare `int score = 0;` inside their bodies. What happens?
    inputConfig:
      options:
        - "Compile error — you cannot have two variables with the same name"
        - "They share the same variable and overwrite each other"
        - "Each method has its own independent `score` variable"
        - "The second declaration is ignored"
    markingRule:
      matchMode: EXACT
      accepted: ["Each method has its own independent `score` variable"]
      rejectedFeedback: "Local variables are independent per method. Same name in different methods is perfectly fine — they are separate variables."
    hint: "Each method has its own isolated box of variables."
    reflectionPrompt: "This is why scope is valuable: methods can use common names like 'count' or 'result' without colliding."
  - id: gs-m3-05-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      The code below will not compile. Identify the scope error in plain English.
      ```java
      public static void setup() {
          int lives = 3;
      }
      public static void play() {
          System.out.println(lives); // ERROR
      }
      ```
    inputConfig:
      minWords: 8
    markingRule:
      matchMode: CONTAINS
      accepted: ["setup", "lives", "local", "declared", "scope", "not accessible"]
      rejectedFeedback: "`lives` is declared inside `setup()`. It is local to that method and cannot be accessed inside `play()`."
    hint: "Where was `lives` declared? Where is it being used?"
    reflectionPrompt: "To share data between methods, you can pass it as a parameter or return it — not by relying on scope leaking."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "A variable declared inside a method is called a:"
    options: ["global variable", "class variable", "local variable", "static variable"]
    correctIndex: 2
    feedback: "Variables declared inside a method are local variables. They only exist while the method is running."
  - type: MULTIPLE_CHOICE
    question: "What is the correct way to make a value computed in `methodA` available in `methodB`?"
    options:
      - "Declare the variable without any keyword"
      - "Return the value from methodA and pass it as an argument to methodB"
      - "Use the same variable name in both methods"
      - "Scope automatically shares variables between methods in the same class"
    correctIndex: 1
    feedback: "Return the value from methodA, then pass it as an argument to methodB. This is the correct, safe way to share data."
retrieval:
  recall: "Define 'local variable' and state its scope."
  explain: "Why is it safe and useful that two methods can each have a variable named `count`? What problem would arise if they shared the same variable automatically?"
  mistakeId:
    code: |
      public static void initGame() {
          int playerHealth = 100;
      }
      public static void showHealth() {
          System.out.println("HP: " + playerHealth); // error
      }
    answer: "playerHealth is a local variable declared inside initGame(). It does not exist inside showHealth(). Fix: return playerHealth from initGame() and pass it as a parameter to showHealth(int health)."
---

# Hook

You declare a variable called `score` inside a method. Later you try to use `score` inside a different method — and Java refuses to compile. It says `score` cannot be found. Where did it go? It never left its method. This is scope — the rule that variables exist only where they are declared. Understanding scope is essential for writing methods that are safe, predictable, and independent.

# Lore Introduction

Archmage Veylan drew a circle of chalk around one of his apprentices. "Everything you create inside this circle," he announced, "exists only inside this circle. The moment you step outside, it ceases to be." A rune vessel inscribed within the circle — `int lives = 3` — glowed brightly inside but faded to invisibility when viewed from outside. "This is scope," Veylan said. "Every incantation has its own sealed circle. What is created within stays within." The apprentices understood: power and safety came from that same boundary.

# Core Learning

## Concept Introduction

**Scope** is the region of code where a variable can be accessed. Variables declared inside a method are called **local variables**. They exist only for the duration of that method call — they are created when the method starts and destroyed when it ends.

```java
public static void methodA() {
    int count = 10;  // local to methodA
    System.out.println(count); // OK
}

public static void methodB() {
    System.out.println(count); // COMPILE ERROR: count not found
}
```

`count` is local to `methodA`. It simply does not exist anywhere else.

**Each method has its own isolated namespace:**

```java
public static void methodA() {
    int score = 100; // methodA's score
}

public static void methodB() {
    int score = 200; // methodB's own score — no conflict
}
```

Both variables are named `score`, but they are completely independent. They live in separate scopes.

## Why It Matters

Scope makes methods safe and predictable. Without scope, a variable changed in one method could accidentally affect every other method. With scope, methods are isolated — changes inside one method cannot leak out. This is a foundational aspect of writing reliable software. It also means that methods can be developed and tested independently, because they do not share hidden state.

## Worked Examples

**Example 1 — Local variable only visible inside its method**

```java
public static void greet() {
    String greeting = "Hello!";
    System.out.println(greeting); // OK — inside greet()
}

// System.out.println(greeting); // COMPILE ERROR — outside greet()
```

**Example 2 — Sharing data via parameters and return values**

```java
public static int calculateBonus(int score) {
    int bonus = score / 10; // local to this method
    return bonus;           // pass the value out via return
}

public static void displayBonus(int b) {
    System.out.println("Bonus: " + b); // b is the local copy
}

// In main:
int b = calculateBonus(200);
displayBonus(b);
```

**Example 3 — Same name, different scope**

```java
public static void levelUp() {
    int newLevel = 5;
    System.out.println("Levelled up to " + newLevel);
}

public static void showLevel() {
    int newLevel = 12; // completely separate variable
    System.out.println("Current level: " + newLevel);
}
```

Both methods have a `newLevel` variable. They never interact.

## Common Mistakes

- **Assuming variables travel between methods automatically.** They do not. Use parameters or return values to move data.
- **Declaring the same variable twice in the same method.** Within one method, each variable must have a unique name.
- **Thinking class-level (static) fields have method-level scope.** Fields declared at class level are visible to all methods — but local variables inside methods are not.
- **Using a variable before it is declared.** Within a method, a variable must be declared before it can be used.
- **Forgetting that parameters are also local variables.** A parameter declared in the method header is local to that method.

## Mental Model

Think of each method as a **sealed room**. Anything you create in that room stays in that room. If you want to send something to another room, you must pass it through the door (return value) or have it delivered as a package before the room is sealed (parameter). You cannot simply reach through a wall and grab something from the next room.

## Mini Summary

- Scope defines where a variable can be accessed.
- Variables declared inside a method are local variables — they exist only inside that method.
- Local variables are created when the method is called and destroyed when it ends.
- Two methods can each have a variable with the same name without conflict.
- To share data between methods: use parameters (passing in) or return values (sending out).
- Parameters are also local variables — scoped to the method they belong to.

# Guided Practice Quest

Work through each step in order.

**Step 1.** Variable `x` is declared inside `methodA`. Can it be read inside `methodB`?

**Step 2.** Two methods both declare `int score = 0`. What happens — compile error, shared variable, or independent variables?

**Step 3.** The code shown declares `int lives` inside `setup()` but uses it in `play()`. Describe the scope error in plain English.

# Solo Practice Quest

Write a short reflection (at least 70 words) that:
1. Defines scope in your own words.
2. Explains why it is actually a *good* thing that methods cannot access each other's variables.
3. Describes the correct way to make a value from one method available in another — using a concrete Java example.

# Integration

**Philosophy connection — Boundaries and identity**

Philosophers have long explored the idea that identity requires boundaries. A self that bleeds into everything around it has no distinct identity. Method scope enforces a similar principle: each method has a bounded identity. Variables declared inside it belong to it alone. This boundary is what makes a method a coherent, independent unit of behaviour — rather than a fragment of a larger, tangled whole. Clean architecture in software mirrors the philosophical insight that meaningful things have clear boundaries.

**Psychology connection — Reducing interference**

Cognitive psychology identifies *proactive interference* as the phenomenon where earlier learned information disrupts the recall of later information. In code, variables that "bleed" between methods create a similar effect: a variable changed in method A interferes with method B's expectations. Scope eliminates this interference by giving each method its own clean mental namespace. Programmers who understand scope spend less time debugging mysterious variable mutations — their mental model of each method stays accurate.

**Free question:** If scope keeps variables inside methods, how do you build a program where multiple methods need to work with the same piece of data (like a game's total score)? What are the options?

# Lore Conclusion

Veylan erased the chalk circle and the rune vessel faded completely. "Scope is not a limitation," he told the assembled apprentices. "It is a gift. Without it, every incantation would risk corrupting every other incantation's rune vessels — the tower would descend into chaos." He gestured at the walls of the Academy, each stone a sealed, independent room. "Build each incantation as a clean, sealed space. Pass treasures through the door when needed. That is how the tower stays standing."
