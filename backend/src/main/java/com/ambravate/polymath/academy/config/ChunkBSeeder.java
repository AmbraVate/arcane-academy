package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.stereotype.Component;

@Component
public class ChunkBSeeder extends AbstractChunkSeeder {

    public ChunkBSeeder(ChunkRepository chunkRepository, SubChunkRepository subChunkRepository,
                        QuestionRepository questionRepository, RabbitHoleModuleRepository rabbitHoleRepository) {
        super(chunkRepository, subChunkRepository, questionRepository, rabbitHoleRepository);
    }

    @Override
    public void seed() {
        chunk("B", "Operators & Control Flow", "\uD83D\uDCDC", 2, LearnerPath.FOUNDATION, "A");

        seedB1();
        seedB2();
        seedB3();
        seedB4();
        seedB5();
    }

    private void seedB1() {
        subChunk("B1", "B", "Comparison Operators", 1, 50, "Comparisons.java",
                "<p>Can a program make decisions? Before it can decide, it needs to <em>compare</em>.</p>",
                "<p>Java has six <strong>comparison operators</strong> that produce a <code>boolean</code> result:</p>"
                + "<ul><li><code>==</code> equal to</li><li><code>!=</code> not equal to</li>"
                + "<li><code>&lt;</code> less than</li><li><code>&gt;</code> greater than</li>"
                + "<li><code>&lt;=</code> less than or equal</li><li><code>&gt;=</code> greater than or equal</li></ul>"
                + "<pre><code>int age = 18;\nSystem.out.println(age >= 18); // true\nSystem.out.println(age == 21); // false</code></pre>"
                + "<p><strong>Important:</strong> <code>=</code> is assignment, <code>==</code> is comparison!</p>",
                story(
                    n("Before a spell can branch, it must first learn to <em>compare</em> — to weigh one rune against another and declare a truth."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Comparison operators are the eyes of your code. They look at two values and answer a simple question: true or false?"),
                    e("Comparison example", "int score = 85;\nboolean passed = score >= 60;\nSystem.out.println(passed); // true")
                ),
                "<p>Declare two int variables <code>a</code> and <code>b</code>. Print whether <code>a</code> is greater than <code>b</code>, then whether they are equal.</p>",
                "int a = 10;\nint b = 7;\n// TODO: print whether a is greater than b\n// TODO: print whether a equals b\n",
                tests(test("Greater than", "null", "true"), test("Equals", "null", "false")),
                "Explain the six comparison operators in Java. What type of value do they produce?");

        mcQuestion("B1", QuestionTier.RECALL, "What is the difference between <code>=</code> and <code>==</code>?",
                new String[]{"= assigns a value, == compares two values", "= compares, == assigns", "They are the same", "= is for numbers, == is for strings"},
                "= assigns a value, == compares two values",
                "<code>=</code> is assignment. <code>==</code> is equality comparison.");

        codeQuestion("B1", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "What does this code print?",
                "int x = 5;\nint y = 10;\nSystem.out.println(x != y);\nSystem.out.println(x >= 10);",
                "true\nfalse",
                "5 != 10 is true. 5 >= 10 is false.");

        tfQuestion("B1", QuestionTier.RECALL, "<code>7 &lt;= 7</code> evaluates to <code>true</code>.",
                "True", "<code>&lt;=</code> means less than OR equal to. 7 equals 7, so true.");
    }

    private void seedB2() {
        subChunk("B2", "B", "If/Else Statements", 2, 50, "IfElse.java",
                "<p>A guardian at the gate checks your level before letting you through. How does code make decisions?</p>",
                "<p>An <code>if</code> statement runs code only when a condition is true. Chain <code>else if</code> and <code>else</code> for multiple branches.</p>"
                + "<pre><code>int temp = 35;\nif (temp > 30) {\n    System.out.println(\"Hot\");\n} else if (temp > 15) {\n    System.out.println(\"Warm\");\n} else {\n    System.out.println(\"Cold\");\n}</code></pre>"
                + "<p>Only <strong>one</strong> branch executes — Java checks top to bottom and stops at the first true condition.</p>",
                story(
                    n("The branching spell — one of the most fundamental incantations. It lets your code choose a path."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Think of if/else as a crossroads. Your code examines a condition and takes one path or the other — never both."),
                    e("If/else example", "int age = 20;\nif (age >= 18) {\n    System.out.println(\"Adult\");\n} else {\n    System.out.println(\"Minor\");\n}")
                ),
                "<p>Check a variable <code>score</code>. Print \"A\" if >= 90, \"B\" if >= 80, \"C\" if >= 70, else \"F\".</p>",
                "int score = 85;\nif (score >= 90) {\n    // TODO: print \"A\"\n} else if (score >= 80) {\n    // TODO: print \"B\"\n} else if (score >= 70) {\n    // TODO: print \"C\"\n} else {\n    // TODO: print \"F\"\n}",
                tests(test("Grade B", "null", "B")),
                "Explain if/else to a friend who has never coded. Use a real-world analogy.");

        mcQuestion("B2", QuestionTier.RECALL, "What happens if no <code>if</code>/<code>else if</code> condition is true?",
                new String[]{"The else block runs (if present)", "The program crashes", "The first block runs", "All blocks run"},
                "The else block runs (if present)",
                "If no condition matches, the else block executes. No else = nothing happens.");

        codeQuestion("B2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "What does this print?",
                "int x = 15;\nif (x > 20) {\n    System.out.println(\"big\");\n} else if (x > 10) {\n    System.out.println(\"medium\");\n} else if (x > 5) {\n    System.out.println(\"small\");\n}",
                "medium",
                "15 > 20 is false. 15 > 10 is true → prints \"medium\". Stops checking.");

        codeQuestion("B2", QuestionTier.APPLICATION, QuestionType.DEBUGGING,
                "This always prints \"even\" no matter what. Find the bug.",
                "int n = 7;\nif (n % 2 == 0); {\n    System.out.println(\"even\");\n}",
                "Remove the semicolon after the if condition",
                "The semicolon after <code>if (...)</code> creates an empty body. The block always runs.");
    }

    private void seedB3() {
        subChunk("B3", "B", "Logical Operators", 3, 50, "LogicalOps.java",
                "<p>What if two conditions must <em>both</em> be true? Or if <em>either</em> one is enough?</p>",
                "<p>Three <strong>logical operators</strong>:</p>"
                + "<ul><li><code>&&</code> (AND) — both must be true</li>"
                + "<li><code>||</code> (OR) — at least one true</li>"
                + "<li><code>!</code> (NOT) — flips true↔false</li></ul>"
                + "<pre><code>boolean hasTicket = true;\nboolean isVIP = false;\nif (hasTicket && isVIP) {\n    System.out.println(\"VIP entry\");\n} else if (hasTicket || isVIP) {\n    System.out.println(\"Standard entry\");\n}</code></pre>",
                story(
                    n("Combining conditions is like combining potion ingredients — the right combination unlocks powerful effects."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "AND requires both truths. OR needs only one. NOT inverts. These are the building blocks of all decisions.")
                ),
                "<p>Check if a user can log in: <code>username</code> equals \"admin\" AND <code>password</code> equals \"secret\".</p>",
                "String username = \"admin\";\nString password = \"secret\";\nif (/* TODO: check username AND password */) {\n    // TODO: print \"Login successful\"\n} else {\n    // TODO: print \"Access denied\"\n}",
                tests(test("Login", "null", "Login successful")),
                "Explain &&, ||, and ! operators with a real-world example for each.");

        mcQuestion("B3", QuestionTier.RECALL, "What does <code>&&</code> require?",
                new String[]{"Both conditions true", "At least one true", "Neither true", "Only the left true"},
                "Both conditions true",
                "AND (&&) returns true only when both operands are true.");

        codeQuestion("B3", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "What does this print?",
                "boolean a = true;\nboolean b = false;\nSystem.out.println(a || b);\nSystem.out.println(a && b);\nSystem.out.println(!b);",
                "true\nfalse\ntrue",
                "true||false=true. true&&false=false. !false=true.");

        tfQuestion("B3", QuestionTier.RECALL, "<code>!(true && false)</code> evaluates to <code>true</code>.",
                "True", "true && false = false. !false = true.");
    }

    private void seedB4() {
        subChunk("B4", "B", "Switch Statements", 4, 50, "SwitchStatement.java",
                "<p>A menu with 7 options — would you really write 7 else-if branches?</p>",
                "<p><code>switch</code> is cleaner than long if/else chains when comparing one value against many options.</p>"
                + "<pre><code>int day = 3;\nswitch (day) {\n    case 1: System.out.println(\"Monday\"); break;\n    case 2: System.out.println(\"Tuesday\"); break;\n    case 3: System.out.println(\"Wednesday\"); break;\n    default: System.out.println(\"Other\");\n}</code></pre>"
                + "<p><strong>Don't forget <code>break</code>!</strong> Without it, execution falls through to the next case.</p>",
                story(
                    n("When a spell must choose between many distinct forms, switch provides clarity."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "A switch is like a sorting hat — it examines one value and sends execution down the matching path.")
                ),
                "<p>Write a switch for <code>day</code> (1-7) that prints the day name. Use \"Weekend\" for 6 and 7.</p>",
                "int day = 5;\nswitch (day) {\n    case 1: // TODO: print \"Monday\" then break\n    case 2: // TODO: print \"Tuesday\" then break\n    case 3: // TODO: print \"Wednesday\" then break\n    case 4: // TODO: print \"Thursday\" then break\n    case 5: // TODO: print \"Friday\" then break\n    case 6:\n    case 7: // TODO: print \"Weekend\" then break\n    default: // TODO: print \"Invalid\"\n}",
                tests(test("Friday", "null", "Friday")),
                "Explain switch statements. When use one instead of if/else?");

        mcQuestion("B4", QuestionTier.RECALL, "What happens without <code>break</code> in a switch case?",
                new String[]{"Execution falls through to the next case", "The program crashes", "The default runs", "Nothing"},
                "Execution falls through to the next case",
                "Without break, execution continues into subsequent cases (fall-through).");

        codeQuestion("B4", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "What does this print? (Notice: no break statements!)",
                "int x = 2;\nswitch (x) {\n    case 1: System.out.println(\"one\");\n    case 2: System.out.println(\"two\");\n    case 3: System.out.println(\"three\");\n    default: System.out.println(\"other\");\n}",
                "two\nthree\nother",
                "No breaks! Starts at case 2 and falls through all remaining cases.");
    }

    private void seedB5() {
        subChunk("B5", "B", "Ternary Operator", 5, 40, "Ternary.java",
                "<p>Can you write an if/else in just one line?</p>",
                "<p>The <strong>ternary operator</strong> is shorthand for simple if/else:</p>"
                + "<pre><code>// condition ? valueIfTrue : valueIfFalse\nint age = 20;\nString status = (age >= 18) ? \"adult\" : \"minor\";</code></pre>"
                + "<p>Use it for <em>simple</em> conditions. For complex logic, stick with if/else.</p>",
                story(
                    n("The ternary operator — a spell condensed into a single line."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc",
                      "Use it when a full if/else feels like overkill. But never sacrifice clarity for brevity.")
                ),
                "<p>Use a ternary to find the larger of two numbers and print the result.</p>",
                "int a = 15;\nint b = 22;\n// TODO: use a ternary operator to find the larger value and store it in max\nint max = 0;\nSystem.out.println(\"Max: \" + max);",
                tests(test("Max", "null", "Max: 22")),
                "Explain the ternary operator. When should you use it vs if/else?");

        mcQuestion("B5", QuestionTier.RECALL, "What is the ternary operator syntax?",
                new String[]{"condition ? valueIfTrue : valueIfFalse", "condition : valueIfTrue ? valueIfFalse", "if condition then value else value", "condition && true || false"},
                "condition ? valueIfTrue : valueIfFalse",
                "The ternary uses ? and : to provide two possible values.");

        codeQuestion("B5", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "What does this print?",
                "int x = 10;\nString result = (x % 2 == 0) ? \"even\" : \"odd\";\nSystem.out.println(result);",
                "even",
                "10 % 2 == 0 is true → returns \"even\".");
    }
}
