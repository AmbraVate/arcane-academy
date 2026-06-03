---
id: se-app-m7-01
school: engineering
domainId: java
tier: APPRENTICE
moduleId: se-app-m7
moduleTitle: "Module 7: Apprentice Project"
moduleGlyph: "🏗️"
moduleSortOrder: 7
topicSlug: mini_project
topicTitle: "Mini Project"
topicSortOrder: 1
lesson: the_console_companion
title: "The Console Companion"
sortOrder: 1
difficulty: 3
estimatedMinutes: 90
xpReward: 150
practiceType: NONE
questType: SYNTHESIS
retrievalWeight: low
questTypes: [solo]
prerequisites:
  - se-app-m1-01
  - se-app-m2-01
  - se-app-m3-01
  - se-app-m4-01
  - se-app-m5-01
  - se-app-m6-01
integrationDomains: [psychology, mathematics]
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Program runs without crashing on valid input"
    - "Uses at least one collection (array or list) to store data"
    - "Uses at least one class with fields and methods"
    - "All major logic is extracted into named methods (no single monolithic block)"
    - "Control flow (if/else, loops) is used correctly and intentionally"
    - "Variable and method names are clear and descriptive"
    - "Input validation prevents the program crashing on unexpected input"
    - "Written reflection explains one design decision and one thing you would improve"
  keywords: [class, method, loop, list, array, input, validation, variable, output]
  modelAnswer: |
    A complete Console Companion correctly stores, retrieves, and displays entries
    using a collection, wraps behaviour in a class, separates logic into small
    named methods, validates user input before processing it, and produces clear
    output. The reflection demonstrates awareness of at least one design tradeoff.
---

# Hook

Six modules. Hundreds of concepts. Variables, loops, functions, objects, collections, debugging.

Now it's time to use them all together — not in isolation, not in a single exercise, but in a small program that does something real.

This project is your first complete program. It will be imperfect. That is expected. The goal is not perfection — it is *integration*: proving to yourself (and your assessor) that you can bring the pieces together.

> What is the simplest program that would still feel *useful* to you?

# Lore Introduction

The Archmage sets down her quill.

*"You have learned the words of the craft — variables, loops, functions, objects. But knowing words is not the same as writing a sentence. This is your first sentence."*

She slides a parchment across the table. On it: a simple brief. A problem to solve. A program to build.

*"It need not be elegant. It need not be complete. It must work, and it must show that you understand what you are doing. The Academy does not reward perfection at this stage. It rewards thinking."*

# Project Brief

Build a **text-based menu application** called the **Console Companion** — a program a user can interact with through a terminal to manage a simple personal list.

Choose one of the following list types:

| Option | What it manages |
|---|---|
| **Contact Book** | Name + phone number |
| **To-Do List** | Task description + done/not done |
| **Shopping List** | Item name + quantity |
| **Student Grades** | Student name + grade (0–100) |

Your program must run in a loop, presenting the user with a menu and responding to their choice, until they choose to exit.

---

## Requirements

### Functional Requirements

The program must support **at least three of these four operations**:

| # | Operation | Description |
|---|---|---|
| 1 | **Add** | Add a new entry to the list |
| 2 | **View** | Display all current entries |
| 3 | **Remove** | Remove an entry by name or position |
| 4 | **Search** | Find and display a specific entry |

### Technical Requirements

You must use and demonstrate:

| Requirement | Where / how |
|---|---|
| At least one **class** | Model the data (e.g., `Contact`, `Task`, `Student`) |
| At least one **collection** | Store the list (array or `ArrayList`) |
| **Control flow** | Drive the menu and handle each option |
| **Loops** | Keep the menu running; iterate over the list |
| **Named methods** | One method per operation — no monolithic main |
| **Input validation** | Reject empty names, invalid numbers, etc. |
| **Clean naming** | Variables and methods that explain themselves |

---

## Scaffolding

You are not starting from scratch. Use this structure as a starting point — fill in the blanks.

```
ConsoleCompanion
├── Main.java              — Entry point; starts the menu loop
├── [YourModel].java       — Class representing one entry (e.g. Contact, Task)
└── [YourManager].java     — Class holding the list + all operations
```

**Suggested method structure for your manager class:**

```java
void add(...)           // Prompts user, creates object, adds to list
void viewAll()          // Prints all entries
void remove(...)        // Finds and removes an entry
void search(...)        // Finds and prints a single entry
void showMenu()         // Prints the menu options
void run()              // Main loop: show menu, read choice, dispatch
```

**Getting started:**

```java
Scanner scanner = new Scanner(System.in);

while (true) {
    showMenu();
    String choice = scanner.nextLine().trim();
    switch (choice) {
        case "1" -> add();
        case "2" -> viewAll();
        case "3" -> remove();
        case "4" -> search();
        case "0" -> { System.out.println("Goodbye."); return; }
        default  -> System.out.println("Invalid choice. Please try again.");
    }
}
```

---

## Acceptance Criteria

Your submission is complete when:

- [ ] The menu loop runs until the user chooses to exit
- [ ] At least 3 operations work correctly
- [ ] A class represents each item in the list (not just a String)
- [ ] The list is stored in a collection (not individual variables)
- [ ] Each operation is in its own method
- [ ] The program does not crash on empty input or invalid numbers
- [ ] You can explain what every class and method does

---

## Reflection Prompt

After completing the program, write **3–5 sentences** addressing:

1. One design decision you made and why (e.g., why you chose an ArrayList over an array)
2. One part of the program that was harder than you expected
3. One thing you would change or add if you had more time

This reflection is part of your submission and is assessed.

---

# Integration

**Connecting to Psychology — Working Memory and Interface Design**

When you designed your menu, you were making decisions about cognitive load. Research on working memory (Miller's Law, 1956) shows that humans reliably hold about 7 items in short-term memory at once. Menu designers who ignore this produce interfaces that overwhelm users — too many options, unclear labels.

Look at your menu. Can a new user understand what each option does in under three seconds? If not, the interface has a cognitive load problem — not a technical one. The best programs are not just correct; they are considerate of the human reading them.

What does this suggest about the relationship between code and the people who use it?

# Lore Conclusion

The Archmage reviews the parchment one final time.

*"Does it work? Does it hold together? Can you explain every line?"*

She marks it with a small rune — not a great seal, but a beginning.

*"Every system you will ever build starts here: a loop, a menu, a class, a decision. The scale changes. The principles do not."*

The first rune of construction is complete. The Console Companion lives.

---
