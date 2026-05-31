---
id: se-app-m6-05
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m6
moduleTitle: "Module 6: Debugging & Engineering Habits"
moduleGlyph: "🔬"
moduleSortOrder: 6
topicSlug: debugging
topicTitle: "Debugging"
topicSortOrder: 2
lesson: print_debugging
title: "Print Debugging"
sortOrder: 5
difficulty: 1
estimatedMinutes: 20
xpReward: 50
practiceType: JAVA
questType: PRACTICE
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m6-04]
integrationDomains: [mathematics, psychology]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Adds print statements at strategic points rather than randomly"
    - "Prints variable values with descriptive labels so output is readable"
    - "Uses print statements to confirm assumptions about what the code is doing"
    - "Explains that print statements should be removed once the bug is fixed"
    - "Demonstrates a finding based on the print output"
  keywords: [print, System.out.println, variable, label, assumption, strategic, remove, inspect, trace]
  modelAnswer: |
    System.out.println("Before loop: total = " + total);
    for (int i = 0; i < items.length; i++) {
        System.out.println("Processing item " + i + ": " + items[i]);
        total += items[i];
        System.out.println("After adding item " + i + ": total = " + total);
    }
    System.out.println("Final total: " + total);
    // Each print has a label so output is readable.
    // Remove all print statements once the bug is found and fixed.

guidedSteps:
  - id: step-1
    sortOrder: 1
    inputType: CODE
    instruction: "Add print statements to trace the value of 'sum' at the start, inside the loop, and after the loop. Use descriptive labels."
    inputConfig:
      language: java
      starterCode: "int sum = 0;\nfor (int i = 1; i <= 5; i++) {\n    sum += i;\n}\nSystem.out.println(\"Result: \" + sum);\n"
      expectedPattern: "System\\.out\\.println"
    markingRule: REGEX_MATCH
    hint: "Add: System.out.println(\"Before loop: sum = \" + sum); before the loop, and inside the loop add: System.out.println(\"i=\" + i + \" sum=\" + sum);"
    reflectionPrompt: "What information do you get from the print inside the loop that you would not get from just printing at the end?"

  - id: step-2
    sortOrder: 2
    inputType: CODE
    instruction: "This code should sum only even numbers from 1 to 10, but returns 25. Add a print inside the loop to find the bug."
    inputConfig:
      language: java
      starterCode: "int evenSum = 0;\nfor (int i = 1; i <= 10; i++) {\n    if (i % 2 == 0) {\n        evenSum += i;\n    }\n}\nSystem.out.println(\"Even sum: \" + evenSum);\n// evenSum should be 30 (2+4+6+8+10)\n// But it returns 25... add print to investigate\n"
      expectedPattern: "System\\.out\\.println.*i|println.*evenSum"
    markingRule: REGEX_MATCH
    hint: "Print i and evenSum inside the loop to see which numbers are being added."
    reflectionPrompt: "Wait — does this code actually produce 25? Run it mentally. What is it really doing wrong?"

  - id: step-3
    sortOrder: 3
    inputType: SHORT_ANSWER
    instruction: "After you find a bug using print statements, what should you do with those print statements?"
    inputConfig:
      placeholder: "After fixing the bug, I should..."
    markingRule: KEYWORD_MATCH
    hint: "Production code should not be filled with debug output."
    reflectionPrompt: "What problems could arise from leaving debug print statements in production code?"

microCheckpoint:
  - question: "What makes a print statement for debugging more useful: 'System.out.println(x)' or 'System.out.println(\"x = \" + x)'?"
    options:
      - "They are equally useful"
      - "System.out.println(x) — shorter is better"
      - "'System.out.println(\"x = \" + x)' — the label makes the output readable and unambiguous"
      - "Neither — you should use a debugger instead"
    correctIndex: 2
    feedback: "Correct — a labelled print includes context. When you see '42' in output you may not know which variable it came from. 'x = 42' is immediately clear."

  - question: "What is the purpose of placing print statements INSIDE a loop during debugging?"
    options:
      - "To make the loop run faster"
      - "To check the value of variables on each iteration and find where the logic goes wrong"
      - "To prevent runtime errors"
      - "To satisfy the compiler"
    correctIndex: 1
    feedback: "Yes — printing inside a loop lets you watch variables change on every iteration, making it easy to spot the iteration where the logic first goes wrong."

retrieval:
  recall: "What are the three rules of effective print debugging?"
  explain: "Describe how you would use print statements to find a bug in a method that is supposed to return the maximum value from an array but sometimes returns the wrong answer."
  mistakeId:
    code: |
      // Debugging a loop — student adds this:
      System.out.println("here");
      System.out.println("here");
      System.out.println("here");
    answer: "The prints have no labels and no variable values — 'here' tells you nothing useful. Each print statement should include a descriptive label and the value of the relevant variable at that point, e.g. System.out.println(\"Loop iteration \" + i + \": total = \" + total);"
---

# Hook

Long before debuggers existed, programmers fixed bugs by adding print statements — and seasoned developers still reach for this technique first. It is immediate, requires no special tool, and works everywhere. The trick is not to scatter prints randomly like confetti: it is to place them *strategically*, label them *clearly*, and remove them *completely* once the bug is found. Mastered, print debugging is a superpower available in every language, every IDE, every environment.

# Lore Introduction

In the Academy's early centuries, before the Scrying Lens was invented, construct analysers had only one tool: the Whisper Rune. Placed on a construct at key junctions, a Whisper Rune would emit a brief pulse whenever the construct passed through that point, reporting the current state of its essence registers. Tactically placed Whisper Runes could map the precise path a corrupt construct took through its execution. The technique was dismissed by some as primitive. Master analyst Deren Kast disagreed: "Give me five well-placed Whisper Runes and I will find any flaw in any construct. Give me fifty poorly placed ones and I will drown in noise."

# Core Learning

## Concept Introduction

**Print debugging** uses `System.out.println()` to inspect the values of variables at specific points in the code. It makes the program *tell you* what is happening as it runs.

**Three rules of effective print debugging:**
1. **Place strategically** — at key decision points, before and inside loops, where you suspect the bug.
2. **Label clearly** — always include what variable or what point in the code you are printing. `"i = " + i` not just `i`.
3. **Remove when done** — debug prints do not belong in finished code.

## Why It Matters

When a logical error produces wrong output, you have a hypothesis about where the bug is. Print statements let you *test* that hypothesis by seeing the actual values at runtime. Instead of guessing, you observe. This is the scientific method applied to code.

## Worked Examples

**Finding a wrong sum:**
```java
int total = 0;
int[] prices = {10, 20, 30};

System.out.println("Starting total: " + total); // debug

for (int i = 0; i < prices.length; i++) {
    total += prices[i];
    System.out.println("After adding prices[" + i + "] (" + prices[i] + "): total = " + total); // debug
}

System.out.println("Final total: " + total); // debug
```

Output:
```
Starting total: 0
After adding prices[0] (10): total = 10
After adding prices[1] (20): total = 30
After adding prices[2] (30): total = 60
Final total: 60
```

Now you can see exactly how `total` changes and at which step something unexpected might happen. If the expected total was 50 and you see 60, you can spot which iteration added an unexpected amount.

**Strategic placement:**
- Before a method call: confirm the input is what you expect.
- Inside a loop: confirm the loop iterates correctly with the right values.
- After a conditional: confirm you entered the right branch.
- Before a return statement: confirm the return value is correct.

## Common Mistakes

- **Unlabelled prints**: `System.out.println(x)` prints "42" — you have no idea which variable or which iteration.
- **Too many prints**: Flooding output makes it impossible to read. Add only what you need to test your hypothesis.
- **Leaving prints in finished code**: Debug output clutters logs, confuses users, and can expose sensitive data.
- **Printing after the fix**: If you fix the bug first and then add prints, the prints no longer show the problem.

## Mental Model

Think of print debugging as **shining a torch** along a dark corridor. You do not light the whole corridor at once — you choose where to point the beam. Before a suspicious loop? Shine it there. After a calculation? Point it there. The label on the print is the label on the torch: it tells you which part of the corridor you are currently looking at so you can orient yourself.

## Mini Summary

- ✔ Use `System.out.println("label: " + variable)` to inspect values at runtime.
- ✔ Place prints strategically: before/inside loops, before/after key calculations.
- ✔ Always include a descriptive label so output is readable.
- ✔ Add prints to *test a hypothesis* about where the bug is, not randomly.
- ✔ Remove all debug print statements once the bug is fixed.

# Guided Practice Quest

Work through the sidebar steps to add labelled print statements to trace a loop, use them to investigate a bug in an even-sum calculation, and articulate the cleanup rule.

# Solo Practice Quest

**Spell: Cast the Whisper Rune**

This method is supposed to find the largest number in an array and return it, but it is returning the wrong value for some inputs. Add print statements to diagnose what is happening, then fix the bug:

```java
int findMax(int[] numbers) {
    int max = 0;
    for (int i = 0; i < numbers.length; i++) {
        if (numbers[i] > max) {
            max = numbers[i];
        }
    }
    return max;
}
```

Test it with `{3, 7, 2, 9, 4}` (should return 9) and with `{-5, -1, -8}` (should return -1 but returns 0). Show the prints you added, what they reveal, and your fixed version.

# Integration

**Mathematics connection — empirical verification**

In mathematics, a conjecture is verified either by proof (formal) or counterexample (empirical). Print debugging is empirical verification of a program's behaviour: instead of proving it algebraically correct, you observe its actual output. This is acceptable and efficient for finding bugs — but the final fix should be logically correct, not just "I changed something and the prints showed it worked." Use print debugging to observe; use logical reasoning to fix.

**Psychology connection — confirmation bias**

Psychologists warn that we tend to seek evidence that confirms our existing beliefs — confirmation bias. In debugging, this means developers place print statements where they *expect* the bug and miss it entirely because the real bug is elsewhere. The discipline of print debugging counters confirmation bias: put the first print *before* your suspect point to confirm whether the input is even correct before checking the transformation. Let the output lead you to the bug; do not assume you already know where it is.

**Question:** You believe a bug is in a specific method. How would the principle of print debugging — placing prints *before* your suspected location as well as inside it — help you avoid confirmation bias and find where the problem actually originates?

# Lore Conclusion

The Whisper Rune is placed, its pulses are read, the flaw is found. This humble technique — one line of code, a label, a variable — has solved more bugs than any fancy tool in the Academy's arsenal. In the next lesson you will learn about the Scrying Lens itself: the IDE debugger, which gives you the power to pause a running construct mid-spell, inspect its entire essence register at once, and step through its invocations one at a time.
