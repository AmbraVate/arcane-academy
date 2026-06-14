---
id: se-app-m6-06
school: engineering
domainId: software_engineering
tier: APPRENTICE
moduleId: se-app-m6
moduleTitle: "Module 6: Debugging and Engineering Habits"
moduleGlyph: "🔧"
moduleSortOrder: 6
topicSlug: debugging
topicTitle: "Debugging"
topicSortOrder: 1
lesson: ide_debuggers
title: "IDE Debuggers"
sortOrder: 6
difficulty: 2
estimatedMinutes: 25
xpReward: 50
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: []
integrationDomains: [psychology, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what a breakpoint is and how to set one"
    - "Describes the difference between Step Over, Step Into, and Step Out"
    - "Explains what the call stack shows during debugging"
    - "Identifies why print debugging is inferior to using a debugger"
    - "Reflects on a specific scenario where a debugger would save time"
  keywords: [breakpoint, step, debugger, variable, call stack, pause, inspect]
  modelAnswer: |
    // Example: debugging a faulty method
    public int sumPositive(int[] numbers) {
        int total = 0;
        for (int n : numbers) {
            // Set breakpoint here to inspect 'n' and 'total' each iteration
            if (n > 0) {
                total += n;
            }
        }
        return total;
    }
    // Debugger approach:
    // 1. Set breakpoint on 'if (n > 0)' line
    // 2. Run in debug mode
    // 3. Step Over each iteration, watching 'total' and 'n' change
    // 4. Spot if a negative number is incorrectly included
guidedSteps:
  - id: dbg-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      What does setting a **breakpoint** on a line of code do?
    inputConfig:
      options:
        - "It deletes the line from the program"
        - "It pauses program execution at that line so you can inspect the program state"
        - "It logs the line's output to the console automatically"
        - "It causes the program to skip that line when running"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["It pauses program execution at that line so you can inspect the program state"]
      rejectedFeedback: "A breakpoint tells the IDE's debugger to halt execution at that exact line. The program freezes, and you can examine variable values, the call stack, and the program's current state before deciding what to step through next."
    hint: "Think about the word 'break' — what does the debugger break?"
    reflectionPrompt: "Breakpoints give you a freeze-frame view of your program at any moment — something print statements can never fully replicate."

  - id: dbg-step-2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      You are debugging and the execution is paused at a call to `calculateTotal()`. You want to execute that method line-by-line, watching every step inside it. Which debugger action do you choose?
    inputConfig:
      options:
        - "Step Over"
        - "Step Into"
        - "Step Out"
        - "Resume"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["Step Into"]
      rejectedFeedback: "Step Into enters the called method and pauses at its first line, letting you trace execution inside it. Step Over executes the method call as a single unit without entering it. Step Out finishes the current method and pauses at the caller."
    hint: "You want to go *inside* the method — which action moves execution inward?"
    reflectionPrompt: "Choosing Step Into vs Step Over is about deciding whether the bug is inside that method or in how its result is used. Experienced debuggers make this choice deliberately."

  - id: dbg-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      Explain in your own words why using a debugger is generally better than adding lots of System.out.println() statements to track down a bug.
    inputConfig:
      minWords: 20
    markingRule:
      matchMode: CONTAINS
      accepted: [remove, real-time, inspect, pause, every, variable]
      rejectedFeedback: "A debugger lets you inspect any variable in real-time without modifying the code. Print statements must be added, then removed; they only show what you thought to print; and they clutter the codebase. A debugger pauses execution and shows you the entire program state simultaneously."
    hint: "Think about what you have to do before and after using print statements."
    reflectionPrompt: "Every minute spent removing print statements after debugging is waste. Debuggers are reusable and leave no residue."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "The call stack panel in a debugger shows:"
    options:
      - "The list of all variables in the program"
      - "The sequence of method calls that led to the current execution point"
      - "All the breakpoints you have set"
      - "The output the program has printed so far"
    correctIndex: 1
    feedback: "The call stack shows the chain of method invocations that led to where execution is currently paused. It lets you understand *how* the program arrived at the current line — invaluable for tracing unexpected code paths."
  - type: MULTIPLE_CHOICE
    question: "You want the debugger to finish executing the current method and pause back in the method that called it. Which action do you use?"
    options:
      - "Step Into"
      - "Step Over"
      - "Step Out"
      - "Restart"
    correctIndex: 2
    feedback: "Step Out runs the rest of the current method to completion and pauses on the line after the method call in the calling code. It is useful when you have entered a method by mistake or have seen enough of its internals."
retrieval:
  recall: "Name the three main step actions in a debugger and describe what each does."
  explain: "A junior developer says they prefer print debugging because 'it is simpler'. Give two concrete reasons why investing time in learning the IDE debugger pays off."
  mistakeId:
    code: |
      public String getGreeting(String name) {
          System.out.println("DEBUG: name=" + name);
          String greeting = "Hello, " + name + "!";
          System.out.println("DEBUG: greeting=" + greeting);
          return greeting;
      }
    answer: "This code uses print debugging (System.out.println with 'DEBUG:' prefixes). These debug prints must be manually added and then removed before shipping — they are easy to forget, can expose sensitive data in production logs, and clutter the console output for other developers. A breakpoint on the return statement would show the same information without any code modification."
---

# Hook

You have written what looks like correct code, run it, and gotten the wrong answer. You stare at the code for twenty minutes, adding print statements, running again, adding more print statements. Sound familiar? This is the classic trap of **print debugging** — and it is the coding equivalent of trying to locate a leak in a plumbing system by tasting every pipe.

There is a better way. Every serious IDE — IntelliJ IDEA, Eclipse, VS Code — ships with a powerful **debugger** that lets you pause your program mid-execution, peer inside every variable, walk through code one line at a time, and understand exactly what is happening and why. Once you learn this tool, you will wonder how you ever lived without it.

> Think of the last bug you fixed. How long did it take? If you had been able to pause the program and inspect every variable at any point, how might that have changed your approach?

# Lore Introduction

In the Academy's Workshop of Enchanted Constructs, apprentices used to diagnose malfunctioning spell circuits by casting a Glow Cantrip at each node — the magical equivalent of a print statement. The node would light up briefly, confirming it had been reached, but the cantrip would burn out immediately, leaving no record of the state that passed through.

Then the Senior Artificers introduced the **Suspension Rune** — a mark placed on any node that would freeze the entire circuit at that point, allowing the artificer to examine every flowing current, every stored charge, every active pathway. The Suspension Rune became the most prized tool in every debugger's kit. In your IDE, this is the breakpoint.

# Core Learning

## Concept Introduction

A **debugger** is a tool built into your IDE that allows you to:

1. **Set breakpoints** — mark lines where execution should pause
2. **Inspect variables** — see the current value of any variable when paused
3. **Step through code** — advance execution one line at a time
4. **Examine the call stack** — see which methods called which to reach the current point

### Key debugger actions:

| Action | What it does |
|---|---|
| **Step Over** | Executes the current line and moves to the next, treating method calls as single steps |
| **Step Into** | If the current line calls a method, enters that method and pauses at its first line |
| **Step Out** | Finishes the current method and pauses at the line after its call site |
| **Resume** | Continues execution until the next breakpoint (or program end) |

## Why It Matters

Print debugging forces you to guess which variables to inspect before running. You then have to modify code, run, modify again, and eventually delete all your prints. A debugger eliminates this cycle: you pause anywhere, inspect everything, step through logic, and leave zero residue. Studies of developer productivity consistently show that developers proficient with a debugger find and fix bugs significantly faster than those who rely on print statements.

## Worked Examples

**Example 1 — Setting a breakpoint and inspecting a variable**
```java
public class SpellCalculator {
    public int calculateDamage(int basePower, int multiplier) {
        // Set breakpoint here (line below)
        int damage = basePower * multiplier; // pause here; inspect basePower, multiplier, damage
        return damage;
    }
}
// In debug mode: basePower=50, multiplier=0 → damage=0
// Ah — the multiplier was never set! Found the bug without a single print.
```

**Example 2 — Using Step Into to trace inside a helper**
```java
public void processOrder(Order order) {
    double total = calculateTotal(order);  // Step Into here to debug calculateTotal
    applyDiscount(total);
}

private double calculateTotal(Order order) {
    // Debugger pauses here when you Step Into
    double sum = 0;
    for (Item item : order.getItems()) {
        sum += item.getPrice(); // Step Over to watch sum accumulate each iteration
    }
    return sum;
}
```

**Example 3 — Reading the call stack**
```java
// If program pauses at a NullPointerException inside formatName()
// The call stack shows:
// formatName()          <- current frame (where NPE occurred)
// buildProfile()        <- called formatName
// UserService.create()  <- called buildProfile
// main()               <- called UserService.create

// Reading bottom-up tells you the full execution path that led here
```

## Common Mistakes

- **Running in run mode instead of debug mode** — breakpoints only work when you launch via the debug button (bug icon), not the play button.
- **Setting breakpoints in the wrong place** — set breakpoints just before the suspected problem, not at the top of the file.
- **Ignoring the call stack** — the call stack tells you *how* you got here; reading it often reveals the true source of a bug several frames up.
- **Not clearing breakpoints after debugging** — stale breakpoints slow down future debugging sessions; remove them when done.
- **Debugging instead of reading** — sometimes reading the code carefully is faster; use the debugger when the logic is complex or the state is hard to reason about statically.

## Mental Model

Think of the debugger as a magical pause button for your program. At any moment you can press it, look around the entire program world — every variable, every method in flight, every call that led you here — then press play again. It is the difference between watching a magic trick at full speed and watching it frame by frame.

## Mini Summary

✔ A breakpoint pauses program execution so you can inspect the current state.
✔ Step Over advances one line treating method calls as atomic; Step Into enters a method; Step Out exits the current method.
✔ The call stack shows the chain of method calls that led to the current line.
✔ The debugger is faster, cleaner, and more powerful than print-statement debugging.
✔ Run the program in **debug mode** (not run mode) for breakpoints to activate.

# Guided Practice Quest

**The Suspension Rune**
A spell calculation method is returning incorrect values. Use your understanding of the debugger to identify what you would inspect and why.
Steps are defined in the frontmatter `guidedSteps` section above.

# Solo Practice Quest

Take any small Java method you have written previously (or write a new one: a method that finds the maximum value in an array). In writing, describe the exact debugging session you would conduct: (1) where you would place a breakpoint and why; (2) which variables you would inspect; (3) whether you would use Step Over or Step Into, and why; (4) what the call stack would show. You do not need to actually run the debugger — describe the session in 150-200 words as if narrating it to a colleague.

# Integration

**Connecting to Philosophy — Empirical Reasoning**
Debugging is applied **empiricism**: the philosophical tradition that knowledge comes from observation and experiment rather than pure reasoning. When you set a breakpoint and inspect a variable, you are not guessing — you are gathering direct evidence about the program's actual behaviour. This is the scientific method applied to software.

The philosopher Francis Bacon described empiricism as proceeding "from particular observations to general laws". A skilled debugger does exactly this: they observe the specific state at a specific moment, form a hypothesis about the cause, test it by stepping through code, and refine their understanding. The IDE debugger is the microscope that makes these observations possible — without it, you are doing philosophy by intuition alone.

> How does the scientific method (observe, hypothesise, test, conclude) map onto the process of finding and fixing a bug? Can you describe a bug hunt as if it were a scientific experiment?

# Lore Conclusion

The apprentice places the Suspension Rune on the third node of the malfunctioning spell circuit and watches the entire mechanism freeze at that point. There — suspended in time — they can see the flow of magical energy, the charge in each component, the exact moment where the energy was miscalculated. In three minutes, they have found the fault that had eluded them for an hour of Glow Cantrip testing.

The Suspension Rune is now the apprentice's most reliable tool. In the next lesson, you will learn to use it in conjunction with a systematic method for attacking any bug — a structured process that transforms debugging from a stressful scramble into a calm, methodical investigation.

---
