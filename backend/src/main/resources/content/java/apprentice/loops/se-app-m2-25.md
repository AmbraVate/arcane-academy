---
id: se-app-m2-25
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m2
moduleTitle: "Module 2: Programming Foundations"
moduleGlyph: "📝"
moduleSortOrder: 2
topicSlug: loops
topicTitle: "Loops"
topicSortOrder: 4
lesson: break_and_continue
title: "Break & Continue"
sortOrder: 25
difficulty: 2
estimatedMinutes: 20
xpReward: 50
practiceType: JAVA
questType: GUIDED
retrievalWeight: medium
questTypes: [guided, solo, retrieval]
prerequisites: [se-app-m2-24]
integrationDomains: [psychology, philosophy]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Correctly explains `break` exits the loop entirely"
    - "Correctly explains `continue` skips the rest of the current iteration and moves to the next"
    - "Gives a practical use case for `break` (e.g., early exit on finding an item)"
    - "Gives a practical use case for `continue` (e.g., skipping invalid items in a list)"
    - "Explains that `break` only exits the innermost loop in nested loops"
  keywords: [break, continue, exit, skip, iteration, innermost, early, loop]
  modelAnswer: |
    `break` immediately exits the innermost loop, continuing execution at the first statement after the loop's closing brace. It is used for early exit — stopping a search when the target is found, or exiting a `while(true)` loop when a condition is met.

    `continue` skips the rest of the current iteration's body and jumps to the next evaluation of the loop condition (for while) or the update clause (for for). It is used to filter out unwanted cases — skipping invalid values, skipping items that do not meet a criterion, or avoiding processing errors.

    Practical break example: searching an array — once the target is found, there is no reason to continue checking remaining elements; `break` exits immediately.

    Practical continue example: processing a list of numbers and skipping negatives — `if (n < 0) continue;` moves to the next iteration without processing that value.

    In nested loops, `break` only exits the innermost loop. The outer loop continues from its next iteration. To break out of multiple levels, you need labelled breaks (advanced) or a flag variable.
guidedSteps:
  - id: se-app-m2-25-step1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      ```java
      for (int i = 0; i < 5; i++) {
          if (i == 3) break;
          System.out.println(i);
      }
      ```
      What is printed?
    inputConfig:
      options:
        - "0 1 2 3 4"
        - "0 1 2"
        - "0 1 2 3"
        - "3 4"
    markingRule:
      matchMode: EXACT
      accepted: ["0 1 2"]
      rejectedFeedback: "i takes values 0, 1, 2 — printing each. When i reaches 3, the `break` executes before the `println`, exiting the loop. So 3 is never printed. After the break, execution continues after the loop's closing brace. Printed: 0, 1, 2."
    hint: "What happens when i equals 3? Does break run before or after println?"
    reflectionPrompt: "`break` exits the loop immediately when the `if` condition is met, before any code below it in the body runs. In this case, `println(i)` is below the `if(i==3) break`, so for i=3 the break fires first and println never runs."

  - id: se-app-m2-25-step2
    sortOrder: 2
    inputType: MULTIPLE_CHOICE
    instruction: |
      ```java
      for (int i = 0; i < 6; i++) {
          if (i % 2 == 0) continue;
          System.out.println(i);
      }
      ```
      What is printed?
    inputConfig:
      options:
        - "0 2 4"
        - "1 3 5"
        - "0 1 2 3 4 5"
        - "1 2 3 4 5"
    markingRule:
      matchMode: EXACT
      accepted: ["1 3 5"]
      rejectedFeedback: "When i is even (0, 2, 4), `i % 2 == 0` is true, so `continue` skips the `println` and moves to the next iteration. When i is odd (1, 3, 5), the condition is false and `println` runs. Output: 1, 3, 5."
    hint: "Which values of i (0-5) make `i % 2 == 0` true? Those are skipped."
    reflectionPrompt: "`continue` is useful for filtering: skip items that match a criterion, process the rest. This is often cleaner than wrapping the body in an `if (processThisItem)` block."

  - id: se-app-m2-25-step3
    sortOrder: 3
    inputType: CODE
    instruction: |
      Write a for loop that searches an array of ints for the first occurrence of the value 7. When found, print "Found 7 at index: X" and stop searching. Use `break`.
    inputConfig:
      language: java
      starterCode: |
        int[] numbers = {3, 9, 7, 2, 7, 5};
        // Write your loop below
    markingRule:
      matchMode: CONTAINS
      accepted: ["for", "numbers", "== 7", "break", "Found"]
      rejectedFeedback: |
        ```java
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] == 7) {
                System.out.println("Found 7 at index: " + i);
                break;
            }
        }
        ```
        `break` exits the loop after the first 7 is found. Without it, the loop would continue and find the second 7 at index 4 as well.
    hint: "Check each element with `if (numbers[i] == 7)`. After printing, use `break` to stop."
    reflectionPrompt: "Using `break` to exit early is a common search optimisation. Once you have found what you need, there is no reason to keep checking. This makes linear search correct and efficient for finding the *first* occurrence."

microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "What does `continue` do inside a for loop?"
    options:
      - "Exits the for loop entirely"
      - "Restarts the loop from the beginning (resets i to its initial value)"
      - "Skips the rest of the current iteration and jumps to the update clause (i++)"
      - "Pauses the loop for one iteration"
    correctIndex: 2
    feedback: "In a for loop, `continue` skips to the update clause (`i++`) of the for header, then re-evaluates the condition. The rest of the loop body for the current iteration is skipped, but the loop itself continues from the next iteration. The loop variable is not reset."

  - type: MULTIPLE_CHOICE
    question: "In a nested loop, `break` exits:"
    options:
      - "All loops immediately"
      - "Only the innermost loop containing the break"
      - "Only the outermost loop"
      - "The entire program"
    correctIndex: 1
    feedback: "`break` only exits the innermost enclosing loop. If you have a for loop inside a while loop, a `break` in the for loop exits the for loop but the while loop continues. To break out of multiple levels, Java supports labelled breaks (an advanced feature)."

retrieval:
  recall: "Write a while loop using `break` that exits when a counter reaches 5, printing each value."
  explain: "Explain the difference between using `continue` and wrapping the loop body in `if (!skipCondition) { ... }`."
  mistakeId:
    code: |
      for (int i = 0; i < 10; i++) {
          System.out.println(i);
          if (i == 5) continue;
          System.out.println("Still processing " + i);
      }
    answer: "The `continue` is placed AFTER `println(i)`, so it does not skip the first print. It only skips `println(\"Still processing \" + i)`. This is probably not the intended behaviour — if the goal was to skip all processing for i==5, the `continue` should be placed at the top of the body, before any printing. Placement of `break` and `continue` within the body matters: they only skip/exit from the point they appear, not the whole iteration."
---

# Hook

You are searching through a thousand records looking for one specific entry. You find it at record 5. Do you keep checking the other 995 records? No — you stop immediately. That is `break`. Now you are processing a list of transactions and need to skip any that are negative. You do not want to stop the whole loop — just skip the bad ones and continue with the rest. That is `continue`. Two small keywords. Significant control over your loops.

# Lore Introduction

"Every repetition rune has two override commands," Archmage Veylan says, placing two small stones on the table — one red, one yellow. "The red stone: *break*. Cast it, and the rune stops immediately, as if the task is done." He places the red stone down firmly. "The yellow stone: *continue*. Cast it, and the rune skips the remainder of this cycle and begins the next." He picks up the yellow stone and tosses it gently in the air. "Both are tools of precision. The break is decisive. The continue is selective. Neither is to be overused — but both have moments where they are the most honest expression of intent."

# Core Learning

## Concept Introduction

**`break`** — exits the loop immediately:
```java
while (running) {
    doWork();
    if (shouldStop) {
        break; // jumps to the statement after the loop
    }
}
// execution continues here after break
```

**`continue`** — skips the rest of the current iteration, moves to the next:
```java
for (int i = 0; i < n; i++) {
    if (shouldSkip(i)) {
        continue; // jumps to i++ (update), then re-checks condition
    }
    process(i); // skipped for items where shouldSkip is true
}
```

**Placement matters:** `break` and `continue` only affect code below them in the body.

**In nested loops:** `break` and `continue` apply only to the **innermost** loop they are inside.

## Why It Matters

`break` enables early exit — essential for search algorithms, menu systems, and `while(true)` event loops. `continue` enables filtering — skipping invalid or irrelevant items without wrapping the entire body in a nested `if`. Both lead to cleaner, more direct code when used appropriately.

## Worked Examples

**Example 1 — `break` for linear search:**
```java
int[] data = {4, 7, 2, 9, 1};
int target = 9;
int foundIndex = -1;
for (int i = 0; i < data.length; i++) {
    if (data[i] == target) {
        foundIndex = i;
        break; // no need to check further
    }
}
System.out.println("Found at index: " + foundIndex); // 3
```

**Example 2 — `continue` to skip negatives:**
```java
int[] values = {5, -3, 8, -1, 12};
int sum = 0;
for (int v : values) {
    if (v < 0) continue; // skip negative values
    sum += v;
}
System.out.println("Sum of positives: " + sum); // 25
```

**Example 3 — `break` in a nested loop exits only the inner loop:**
```java
for (int i = 1; i <= 3; i++) {
    for (int j = 1; j <= 3; j++) {
        if (j == 2) break; // exits inner loop only
        System.out.println(i + "," + j);
    }
}
// Prints: 1,1 then 2,1 then 3,1 — only j=1 for each row
```

## Common Mistakes

- **Placing `continue` after the statement you meant to skip:** `continue` only skips statements below it in the body. If `println` is above the `continue`, it still runs.
- **Using `break` to exit nested loops:** `break` only exits the innermost loop. Use a flag variable or labelled break for outer loops.
- **Overusing `break` and `continue`:** When every other line is a `break` or `continue`, consider refactoring the logic. Too many can make code hard to follow.
- **Confusing `continue` with `break` in while loops:** `continue` in a while loop jumps back to the condition check — it does not reset variables.
- **Using `break` in a switch and expecting it to break a surrounding loop:** `break` in a switch exits the switch, not any surrounding loop.

## Mental Model

Think of `break` as a **fire alarm**: when it goes off, everyone leaves the building (the loop) immediately. Think of `continue` as **pressing the skip button on a playlist**: the current song (iteration) is abandoned and the next one starts immediately. The fire alarm ends the event entirely; the skip button just moves to the next item.

## Mini Summary

- `break` exits the entire loop immediately; execution resumes after the loop.
- `continue` skips the rest of the current iteration and moves to the next.
- Both only affect the innermost loop they are inside.
- Placement within the body matters — only code below the statement is skipped.
- `break` is useful for early exit (search found, stop condition met).
- `continue` is useful for filtering (skip items that should not be processed).

# Guided Practice Quest

*"The Academy's spell registry has some corrupted entries marked with -1," Archmage Veylan explains. "Write a loop over this array: `{10, -1, 25, -1, 8, 15}`. Skip entries that are -1 using `continue`. Sum the valid entries and print the total. Then, as a second task, write a loop that finds the first entry above 20 and prints it, then breaks."*

# Solo Practice Quest

**The Filter and Find**

Given the array `{5, 12, 3, 18, 7, 22, 9, 15}`:

1. Write a for loop using `continue` to print only values greater than 10.
2. Write a separate for loop using `break` to find and print the first value greater than 20.

Trace both loops on paper (write the value of `i` and the output or action for each iteration).

# Integration

**Psychology connection:** Psychologists studying attention describe "selective attention" — the ability to focus on relevant stimuli and ignore irrelevant ones. `continue` is the programming equivalent of selective attention: the loop "attends" only to iterations where the condition is not met, ignoring (skipping) the ones that fail the filter. This parallels how experienced analysts process data — they have patterns for what to ignore, which lets them focus cognitive resources on what matters.

**Philosophy connection:** The philosopher William of Ockham proposed a principle (Occam's Razor): "entities should not be multiplied beyond necessity." Applied to `break` and `continue`: use them when they make the code's intent clearer and simpler. Do not add complexity by forcing every loop into a form that avoids them. But equally, do not use them to paper over a loop structure that should be redesigned. The simplest structure that correctly expresses the intent is the right choice.

*Free question: Java supports labelled `break` — the ability to break out of an outer loop from within an inner loop. Do you think labelled `break` is good practice? What might be an alternative approach that avoids it?*

# Lore Conclusion

The search rune finds its target at the fifth inscription and snaps shut. "It stopped the moment it found what it needed," Archmage Veylan says. "No wasted motion. No redundant effort." The second rune filters the corrupted entries, processing only what is valid. "Selective and decisive." He sets both stones down. "You now hold the full toolkit for loop control: while, for, nested, break, continue. Combined with decisions and variables, you have everything needed to write meaningful algorithms." He gestures to a new section of the teaching board. "The next domain: data collections — structures that hold not one value, but many."
