package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Deprecated
@Profile("never")
@Component
public class ChunkCtoFSeeder extends AbstractChunkSeeder {

    public ChunkCtoFSeeder(ChunkRepository chunkRepository, SubChunkRepository subChunkRepository,
                           QuestionRepository questionRepository, RabbitHoleModuleRepository rabbitHoleRepository) {
        super(chunkRepository, subChunkRepository, questionRepository, rabbitHoleRepository);
    }

    @Override
    public void seed() {
        seedChunkC();
        seedChunkD();
        seedChunkE();
        seedChunkF();
    }

    // =========================================================================
    // CHUNK C — Loops
    // =========================================================================
    private void seedChunkC() {

        chunk("C", "Loops", "\uD83D\uDD04", 3, LearnerPath.FOUNDATION, "B");

        // =====================================================================
        // C1 — For Loops
        // =====================================================================
        subChunk("C1", "C", "For Loops", 1, 50, "ForLoops.java",

                // hook
                "<p>Imagine you need to print the numbers 1 through 100. "
                + "Would you really write <code>System.out.println(1);</code> one hundred times? "
                + "There is a much better way — the <strong>for loop</strong>.</p>",

                // explanation
                "<h3>The For Loop</h3>"
                + "<p>A <code>for</code> loop repeats a block of code a controlled number of times. "
                + "It has three parts separated by semicolons:</p>"
                + "<pre><code>for (initialisation; condition; update) {\n"
                + "    // code to repeat\n"
                + "}</code></pre>"
                + "<ol>"
                + "<li><strong>Initialisation</strong> — runs once before the loop starts, e.g. <code>int i = 1</code></li>"
                + "<li><strong>Condition</strong> — checked before every iteration; the loop continues while it is <code>true</code></li>"
                + "<li><strong>Update</strong> — runs after every iteration, e.g. <code>i++</code> to increment the counter</li>"
                + "</ol>"
                + "<pre><code>for (int i = 1; i <= 5; i++) {\n"
                + "    System.out.println(i);\n"
                + "}</code></pre>"
                + "<p>This prints 1, 2, 3, 4, 5. The counter <code>i</code> starts at 1, keeps going while "
                + "<code>i &lt;= 5</code>, and increases by 1 each time (<code>i++</code>).</p>"
                + "<p><strong>Counting down</strong> — just flip the logic:</p>"
                + "<pre><code>for (int i = 5; i >= 1; i--) {\n"
                + "    System.out.println(i);\n"
                + "}</code></pre>"
                + "<p><strong>Off-by-one pitfall</strong> — using <code>i &lt; 5</code> instead of <code>i &lt;= 5</code> "
                + "prints 1–4, not 1–5. Always double-check your boundary condition!</p>",

                // story
                story(
                    n("Archmage Eldrin unrolls a long scroll covered in identical runes — the same symbol repeated one hundred times."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A foolish apprentice once spent three days copying this repetition scroll by hand. "
                      + "There is a better way. We call it the <strong>repetition incantation</strong> — or, in Java, the <em>for loop</em>."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A for loop has three sacred runes: where to <strong>start</strong>, when to <strong>stop</strong>, "
                      + "and how to <strong>step forward</strong> each time."),
                    e("Counting from 1 to 5", "for (int i = 1; i <= 5; i++) {\n"
                      + "    System.out.println(i);\n"
                      + "}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Magnificent! Five lines of output from three lines of code. "
                      + "Beware the off-by-one curse — if you write <code>i &lt; 5</code> instead of <code>i &lt;= 5</code>, "
                      + "the loop stops one step too early!")
                ),

                // guided practice HTML
                "<p>Use a <code>for</code> loop to print the numbers <strong>1 through 10</strong>, each on its own line.</p>",

                // starter code
                "// Use a for loop to print 1 through 10\n",

                // tests
                tests(
                    test("Prints 1 to 10", "null", "1\n2\n3\n4\n5\n6\n7\n8\n9\n10")
                ),

                // feynman prompt
                "Explain the three parts of a for loop (initialisation, condition, update) as if you were teaching someone who has never seen a loop before."
        );

        // C1 questions
        mcQuestion("C1", QuestionTier.RECALL,
                "<p>Which part of a <code>for</code> loop runs exactly once, before the loop starts?</p>",
                new String[]{"The condition", "The initialisation", "The update", "The body"},
                "The initialisation",
                "<p>The <em>initialisation</em> (e.g. <code>int i = 0</code>) runs once before the first iteration. "
                + "The condition is checked before each iteration, and the update runs after each iteration.</p>");

        tfQuestion("C1", QuestionTier.RECALL,
                "<p><code>for (int i = 1; i &lt; 5; i++)</code> will print the number 5 if you put <code>System.out.println(i)</code> inside the loop body.</p>",
                "False",
                "<p>The condition <code>i &lt; 5</code> stops the loop when <code>i</code> equals 5 — so 5 is never printed. "
                + "Use <code>i &lt;= 5</code> to include 5.</p>");

        codeQuestion("C1", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "for (int i = 0; i < 3; i++) {\n    System.out.println(i * 2);\n}",
                "0\n2\n4",
                "<p><code>i</code> takes values 0, 1, 2. Multiplied by 2: 0, 2, 4.</p>");

        fillQuestion("C1", QuestionTier.APPLICATION,
                "<p>Fill in the blank to make this loop count from 1 to 100:<br>"
                + "<code>for (int i = 1; ______; i++) { ... }</code></p>",
                "i <= 100",
                "<p>The condition <code>i &lt;= 100</code> keeps the loop running while <code>i</code> is at most 100, "
                + "so all values 1–100 are included.</p>");

        codeQuestion("C1", QuestionTier.DISCRIMINATION, QuestionType.DEBUGGING,
                "<p>This loop is supposed to count from 10 down to 1. What is wrong?</p>",
                "for (int i = 10; i >= 1; i++) {\n    System.out.println(i);\n}",
                "Change i++ to i--",
                "<p>The update <code>i++</code> increases <code>i</code> each iteration, so the loop runs forever "
                + "(infinite loop). To count down, use <code>i--</code>.</p>");

        // =====================================================================
        // C2 — While Loops
        // =====================================================================
        subChunk("C2", "C", "While Loops", 2, 50, "WhileLoops.java",

                // hook
                "<p>Sometimes you don't know how many times a loop should run — you just know the condition "
                + "that should stop it. That is exactly what the <strong>while loop</strong> is for.</p>",

                // explanation
                "<h3>The While Loop</h3>"
                + "<p>A <code>while</code> loop keeps running as long as its condition is <code>true</code>:</p>"
                + "<pre><code>while (condition) {\n"
                + "    // code to repeat\n"
                + "}</code></pre>"
                + "<p>The condition is checked <em>before</em> each iteration. If it is false the very first time, "
                + "the body never runs.</p>"
                + "<pre><code>int count = 1;\n"
                + "while (count <= 3) {\n"
                + "    System.out.println(\"Count: \" + count);\n"
                + "    count++;\n"
                + "}</code></pre>"
                + "<p><strong>Infinite loop danger</strong> — if you forget to update the variable inside the loop, "
                + "the condition never becomes false and the program hangs forever. Always make sure something inside "
                + "the loop moves toward the exit condition.</p>"
                + "<p><strong>do-while variant</strong> — the body runs at least once before the condition is checked:</p>"
                + "<pre><code>int x = 10;\n"
                + "do {\n"
                + "    System.out.println(x);\n"
                + "    x--;\n"
                + "} while (x > 5);</code></pre>"
                + "<p><strong>When to use while vs for?</strong> — Use <code>for</code> when you know the number of "
                + "iterations in advance. Use <code>while</code> when you only know the stopping condition.</p>",

                // story
                story(
                    n("In the Academy courtyard, a stone golem patrols endlessly — until its enchantment runs out."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Notice the golem, young apprentice. It does not patrol a fixed number of steps — "
                      + "it patrols <em>until its energy is depleted</em>. That is the essence of a <code>while</code> loop."),
                    e("The patrolling golem", "int energy = 5;\nwhile (energy > 0) {\n"
                      + "    System.out.println(\"Patrolling... energy: \" + energy);\n"
                      + "    energy--;\n"
                      + "}\nSystem.out.println(\"Golem halted.\");"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The loop checks <code>energy &gt; 0</code> before each patrol. "
                      + "When energy reaches zero, the golem stops. "
                      + "But if you forget <code>energy--</code> inside the loop... the golem walks forever. "
                      + "That is an <strong>infinite loop</strong> — a dangerous curse indeed!")
                ),

                // guided practice HTML
                "<p>Use a <code>while</code> loop to count down from <strong>5 to 1</strong>, printing each number. "
                + "After the loop, print <code>Blast off!</code>.</p>",

                // starter code
                "int count = 5;\n\n// Use a while loop to count down from 5 to 1\n\n\n// Print Blast off!\n",

                // tests
                tests(
                    test("Countdown and blast off", "null", "5\n4\n3\n2\n1\nBlast off!")
                ),

                // feynman prompt
                "Explain when you would choose a while loop over a for loop, and what makes infinite loops dangerous."
        );

        // C2 questions
        mcQuestion("C2", QuestionTier.RECALL,
                "<p>When is the condition of a <code>while</code> loop checked?</p>",
                new String[]{"After every iteration", "Before every iteration", "Only at the start", "Only at the end"},
                "Before every iteration",
                "<p>The <code>while</code> loop checks its condition <em>before</em> each iteration. "
                + "If the condition is false from the start, the body never runs.</p>");

        tfQuestion("C2", QuestionTier.RECALL,
                "<p>A <code>do-while</code> loop is guaranteed to execute its body at least once.</p>",
                "True",
                "<p><code>do-while</code> runs the body first, then checks the condition. "
                + "So the body always executes at least once, even if the condition is false initially.</p>");

        codeQuestion("C2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "int n = 1;\nwhile (n < 4) {\n    System.out.println(n);\n    n += 2;\n}",
                "1\n3",
                "<p><code>n</code> starts at 1 (prints), then becomes 3 (prints), then becomes 5 which is not &lt; 4, so the loop ends.</p>");

        mcQuestion("C2", QuestionTier.DISCRIMINATION,
                "<p>Which scenario is best handled by a <code>while</code> loop rather than a <code>for</code> loop?</p>",
                new String[]{
                    "Print numbers 1 to 100",
                    "Keep reading user input until they type 'quit'",
                    "Print every element in a fixed-size array",
                    "Repeat an animation exactly 60 times"
                },
                "Keep reading user input until they type 'quit'",
                "<p>When you don't know in advance how many iterations are needed — only the stopping condition — "
                + "a <code>while</code> loop is the natural choice.</p>");

        // =====================================================================
        // C3 — Loop Control: break & continue
        // =====================================================================
        subChunk("C3", "C", "Loop Control: break & continue", 3, 50, "LoopControl.java",

                // hook
                "<p>What if you want to skip one specific iteration, or stop the loop entirely when "
                + "something unexpected happens? <strong>break</strong> and <strong>continue</strong> give you precise control.</p>",

                // explanation
                "<h3>break and continue</h3>"
                + "<p><strong><code>break</code></strong> — exits the entire loop immediately, regardless of the condition.</p>"
                + "<pre><code>for (int i = 1; i <= 10; i++) {\n"
                + "    if (i == 5) break; // stop when i reaches 5\n"
                + "    System.out.println(i);\n"
                + "}</code></pre>"
                + "<p>This prints 1, 2, 3, 4 — the loop ends before 5 is ever printed.</p>"
                + "<p><strong><code>continue</code></strong> — skips the rest of the current iteration and jumps to the next one.</p>"
                + "<pre><code>for (int i = 1; i <= 6; i++) {\n"
                + "    if (i % 2 != 0) continue; // skip odd numbers\n"
                + "    System.out.println(i);\n"
                + "}</code></pre>"
                + "<p>This prints 2, 4, 6 — odd numbers are skipped but the loop keeps going.</p>"
                + "<p><strong>Nested loops</strong> — <code>break</code> and <code>continue</code> only affect the "
                + "<em>innermost</em> loop they are inside. To break out of an outer loop, you can use labelled break: "
                + "<code>outerLoop: for (...)</code> and then <code>break outerLoop;</code>.</p>",

                // story
                story(
                    n("Archmage Eldrin sits at a grand pipe organ, playing a magical melody. "
                      + "Certain notes must be skipped; others signal the end of the piece."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "This musical spell has twelve notes. But certain dissonant notes — the odd ones — "
                      + "must be skipped, or the magic goes awry. And if I reach note eight, I must "
                      + "<em>shatter the loop entirely</em> before the spell spirals out of control."),
                    e("Skipping notes with continue", "for (int note = 1; note <= 12; note++) {\n"
                      + "    if (note % 2 != 0) continue; // skip odd notes\n"
                      + "    System.out.println(\"Playing note \" + note);\n"
                      + "}"),
                    e("Shattering the loop with break", "for (int note = 1; note <= 12; note++) {\n"
                      + "    if (note == 8) break; // halt at note 8\n"
                      + "    System.out.println(\"Playing note \" + note);\n"
                      + "}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "<code>continue</code> merely hops over a single note and plays on. "
                      + "<code>break</code> ends the entire performance. Two very different powers — use them wisely!")
                ),

                // guided practice HTML
                "<p>Use a <code>for</code> loop from 1 to 20. Use <code>continue</code> to skip odd numbers "
                + "and only print the even numbers (2, 4, 6 … 20).</p>",

                // starter code
                "// Use a for loop from 1 to 20, printing only even numbers\n",

                // tests
                tests(
                    test("Even numbers 2 to 20", "null", "2\n4\n6\n8\n10\n12\n14\n16\n18\n20")
                ),

                // feynman prompt
                "Explain the difference between break and continue with a real-world analogy for each."
        );

        // C3 questions
        mcQuestion("C3", QuestionTier.RECALL,
                "<p>What does the <code>break</code> statement do inside a loop?</p>",
                new String[]{"Skips the current iteration", "Exits the entire loop", "Restarts the loop", "Pauses execution"},
                "Exits the entire loop",
                "<p><code>break</code> immediately exits the loop — no more iterations occur. "
                + "To skip just one iteration while keeping the loop going, use <code>continue</code>.</p>");

        codeQuestion("C3", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "for (int i = 1; i <= 5; i++) {\n    if (i == 3) continue;\n    System.out.println(i);\n}",
                "1\n2\n4\n5",
                "<p><code>continue</code> skips the rest of the loop body when <code>i == 3</code>, "
                + "so 3 is never printed. The loop continues with 4 and 5.</p>");

        codeQuestion("C3", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "for (int i = 1; i <= 5; i++) {\n    if (i == 3) break;\n    System.out.println(i);\n}",
                "1\n2",
                "<p><code>break</code> exits the loop entirely when <code>i == 3</code>. "
                + "Only 1 and 2 are printed before that point.</p>");

        mcQuestion("C3", QuestionTier.DISCRIMINATION,
                "<p>You want to search a list for a specific name and stop as soon as you find it. Which statement should you use?</p>",
                new String[]{"continue", "break", "return", "exit"},
                "break",
                "<p><code>break</code> exits the loop the moment you find what you're looking for, "
                + "making the search efficient. <code>continue</code> would only skip the current name and keep searching.</p>");

        // =====================================================================
        // C4 — Enhanced For & Nested Loops
        // =====================================================================
        subChunk("C4", "C", "Enhanced For & Nested Loops", 4, 60, "EnhancedFor.java",

                // hook
                "<p>You've used the classic <code>for</code> loop with a counter. "
                + "But when iterating over a collection, Java offers a cleaner syntax — "
                + "and sometimes you need a loop inside a loop to handle grids and tables.</p>",

                // explanation
                "<h3>Enhanced For-Each Loop</h3>"
                + "<p>The enhanced <code>for</code> loop (also called for-each) iterates over an array or collection "
                + "without needing an index variable:</p>"
                + "<pre><code>String[] spells = {\"Fireball\", \"Frostbolt\", \"Thunder\"};\n"
                + "for (String spell : spells) {\n"
                + "    System.out.println(spell);\n"
                + "}</code></pre>"
                + "<p>Read the colon as \"in\" — \"for each <code>spell</code> in <code>spells</code>\".</p>"
                + "<p>Use for-each when: you don't need the index, you're not modifying the collection, "
                + "and you want cleaner, less error-prone code.</p>"
                + "<h3>Nested Loops</h3>"
                + "<p>A loop inside another loop. The inner loop completes fully for each iteration of the outer loop:</p>"
                + "<pre><code>for (int row = 1; row <= 3; row++) {\n"
                + "    for (int col = 1; col <= 3; col++) {\n"
                + "        System.out.print(row * col + \" \");\n"
                + "    }\n"
                + "    System.out.println();\n"
                + "}</code></pre>"
                + "<p><strong>Performance note:</strong> if the outer loop runs N times and the inner loop runs M times, "
                + "the total number of iterations is N × M. With large values this grows quickly — "
                + "this is called O(N²) complexity.</p>",

                // story
                story(
                    n("Archmage Eldrin kneels before an ornate chest. Inside, row upon row of potion vials glimmer in the candlelight."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Sometimes we must examine every item in a collection, one by one. "
                      + "The <em>for-each</em> loop is the perfect incantation for this — elegant, unambiguous, impossible to mis-count."),
                    e("Examining each potion", "String[] potions = {\"Health\", \"Mana\", \"Speed\"};\n"
                      + "for (String potion : potions) {\n"
                      + "    System.out.println(\"Found: \" + potion);\n"
                      + "}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "But what if the chest is a <em>grid</em> — three rows, three columns? "
                      + "Then we need a loop within a loop — a <strong>nested</strong> incantation. "
                      + "Watch closely as I draw the star grid:"),
                    e("3×3 star grid", "for (int row = 1; row <= 3; row++) {\n"
                      + "    for (int col = 1; col <= 3; col++) {\n"
                      + "        System.out.print(\"* \");\n"
                      + "    }\n"
                      + "    System.out.println();\n"
                      + "}")
                ),

                // guided practice HTML
                "<p>Use nested <code>for</code> loops to print a <strong>3×3 multiplication table</strong>. "
                + "Print each number followed by a space, and a newline after each row.</p>"
                + "<p>Expected output:</p>"
                + "<pre>1 2 3 \n2 4 6 \n3 6 9 </pre>",

                // starter code
                "// Use nested for loops to print a 3x3 multiplication table\n// Each number followed by a space, newline after each row\n",

                // tests
                tests(
                    test("3x3 multiplication table", "null", "1 2 3 \n2 4 6 \n3 6 9 ")
                ),

                // feynman prompt
                "Explain the enhanced for-each loop and describe a situation where you would use nested loops, including why nested loops can be slow for large inputs."
        );

        // C4 questions
        mcQuestion("C4", QuestionTier.RECALL,
                "<p>What does the colon (<code>:</code>) mean in a for-each loop: <code>for (String s : list)</code>?</p>",
                new String[]{"Divide", "In (iterate over)", "Assign to", "Compare with"},
                "In (iterate over)",
                "<p>Read it as \"for each <code>s</code> <em>in</em> <code>list</code>\". "
                + "The colon separates the loop variable from the collection being iterated.</p>");

        tfQuestion("C4", QuestionTier.RECALL,
                "<p>You can use a for-each loop to modify the original elements of a primitive array.</p>",
                "False",
                "<p>In a for-each loop, the loop variable is a copy of each element (for primitives). "
                + "Modifying <code>x</code> in <code>for (int x : arr)</code> does <em>not</em> change the array. "
                + "Use a regular indexed <code>for</code> loop to modify elements.</p>");

        codeQuestion("C4", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>How many times does <code>System.out.println(\"*\")</code> execute?</p>",
                "for (int i = 0; i < 4; i++) {\n    for (int j = 0; j < 3; j++) {\n        System.out.println(\"*\");\n    }\n}",
                "12",
                "<p>The outer loop runs 4 times, the inner loop runs 3 times per outer iteration: 4 × 3 = 12.</p>");

        mcQuestion("C4", QuestionTier.DISCRIMINATION,
                "<p>Which loop type is <em>best</em> for iterating over all elements of a <code>String[]</code> without needing the index?</p>",
                new String[]{"for loop with counter", "Enhanced for-each loop", "while loop", "do-while loop"},
                "Enhanced for-each loop",
                "<p>The enhanced for-each loop is cleaner and less error-prone when you only need the values, "
                + "not the indices. It also works on any <code>Iterable</code>, not just arrays.</p>");
    }

    // =========================================================================
    // CHUNK D — Methods & Scope
    // =========================================================================
    private void seedChunkD() {

        chunk("D", "Methods & Scope", "\uD83D\uDD11", 4, LearnerPath.FOUNDATION, "B");

        // =====================================================================
        // D1 — Defining & Calling Methods
        // =====================================================================
        subChunk("D1", "D", "Defining & Calling Methods", 1, 50, "MethodBasics.java",

                // hook
                "<p>Suppose you copy-paste the same 10 lines of code in 5 different places. "
                + "Then you discover a bug in those lines. How many places do you need to fix? "
                + "Five. <strong>Methods</strong> solve this problem by letting you write code once and reuse it anywhere.</p>",

                // explanation
                "<h3>Methods: Named Incantations</h3>"
                + "<p>A <strong>method</strong> is a named block of code that performs a specific task. "
                + "You define it once, then call it by name whenever you need it.</p>"
                + "<p>A <strong>void</strong> method performs an action but returns no value:</p>"
                + "<pre><code>public static void greet() {\n"
                + "    System.out.println(\"Welcome to Arcane Academy!\");\n"
                + "}\n\n"
                + "public static void main(String[] args) {\n"
                + "    greet(); // call the method\n"
                + "    greet(); // call it again\n"
                + "}</code></pre>"
                + "<p>The <strong>method signature</strong> consists of: access modifier (<code>public</code>), "
                + "<code>static</code> (needed to call from main without creating an object), "
                + "return type (<code>void</code>), method name, and parentheses for parameters.</p>"
                + "<p>To call a method: write its name followed by parentheses: <code>greet();</code>.</p>"
                + "<p>Methods promote <strong>DRY code</strong> — Don't Repeat Yourself.</p>",

                // story
                story(
                    n("Archmage Eldrin's grimoire is filled with named spells. Each page holds a complete incantation — "
                      + "ready to be cast the moment its name is spoken."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Every time you find yourself writing the same lines twice, stop and ask: "
                      + "should this be a <em>named incantation</em>? A method? Write it once, in one place, "
                      + "and summon it whenever needed."),
                    e("A simple greeting method", "public static void greet() {\n"
                      + "    System.out.println(\"Greetings, traveller!\");\n"
                      + "}\n\n"
                      + "// In main:\n"
                      + "greet();\n"
                      + "greet();\n"
                      + "greet();"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Three calls to <code>greet()</code> — three identical lines of output — yet the implementation "
                      + "is written only once. Change the message in one place, and all three calls update instantly. "
                      + "That is the power of methods!")
                ),

                // guided practice HTML
                "<p>Create a <code>static void</code> method called <code>printBanner()</code> that prints "
                + "<code>=== Welcome ===</code>. Call it <strong>3 times</strong> from <code>main</code>.</p>",

                // starter code
                "public class MethodBasics {\n\n"
                + "    // Define your printBanner() method here\n\n\n"
                + "    public static void main(String[] args) {\n"
                + "        // Call printBanner() three times\n\n\n"
                + "    }\n"
                + "}\n",

                // tests
                tests(
                    test("Prints banner 3 times", "null", "=== Welcome ===\n=== Welcome ===\n=== Welcome ===")
                ),

                // feynman prompt
                "Explain what a method is, why we use them, and what the 'static' and 'void' keywords mean in a method signature."
        );

        // D1 questions
        mcQuestion("D1", QuestionTier.RECALL,
                "<p>What return type should a method have if it performs an action but does NOT return a value?</p>",
                new String[]{"int", "String", "void", "null"},
                "void",
                "<p><code>void</code> means the method returns nothing. It simply executes its statements and finishes.</p>");

        tfQuestion("D1", QuestionTier.RECALL,
                "<p>You can call the same method multiple times in a program.</p>",
                "True",
                "<p>That is the whole point of methods — define once, call as many times as needed. "
                + "Each call executes the method body from top to bottom.</p>");

        codeQuestion("D1", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this program print?</p>",
                "public static void shout() {\n    System.out.println(\"HELLO!\");\n}\n\npublic static void main(String[] args) {\n    shout();\n    shout();\n}",
                "HELLO!\nHELLO!",
                "<p><code>shout()</code> is called twice, so <code>HELLO!</code> is printed twice on separate lines.</p>");

        fillQuestion("D1", QuestionTier.APPLICATION,
                "<p>Complete the method signature to define a method with no parameters that returns nothing:<br>"
                + "<code>public ______ void sayHello() { ... }</code></p>",
                "static",
                "<p>The <code>static</code> keyword allows the method to be called from a static context "
                + "(like <code>main</code>) without needing to create an object.</p>");

        // =====================================================================
        // D2 — Parameters & Return Values
        // =====================================================================
        subChunk("D2", "D", "Parameters & Return Values", 2, 50, "MethodParams.java",

                // hook
                "<p>A spell that does the exact same thing regardless of what you feed it is not very useful. "
                + "Real methods take <strong>input</strong> (parameters) and give back <strong>output</strong> (return values).</p>",

                // explanation
                "<h3>Parameters & Return Values</h3>"
                + "<p><strong>Parameters</strong> are variables listed in the method signature. "
                + "They act as inputs to the method. When you call the method, you pass <strong>arguments</strong> — "
                + "the actual values — for each parameter.</p>"
                + "<pre><code>public static void greet(String name) { // name is a parameter\n"
                + "    System.out.println(\"Hello, \" + name + \"!\");\n"
                + "}\n\n"
                + "greet(\"Eldrin\");  // \"Eldrin\" is the argument\n"
                + "greet(\"Alice\");</code></pre>"
                + "<p>A method can have multiple parameters, separated by commas:</p>"
                + "<pre><code>public static void describe(String name, int level) {\n"
                + "    System.out.println(name + \" is level \" + level);\n"
                + "}</code></pre>"
                + "<p><strong>Return values</strong> — instead of <code>void</code>, specify the type the method will return, "
                + "then use the <code>return</code> keyword to send a value back to the caller:</p>"
                + "<pre><code>public static int add(int a, int b) {\n"
                + "    return a + b;\n"
                + "}\n\n"
                + "int result = add(3, 4); // result == 7\n"
                + "System.out.println(result);</code></pre>"
                + "<p>The <code>return</code> statement also <em>ends</em> the method — no code after it in the same path runs.</p>",

                // story
                story(
                    n("Archmage Eldrin prepares a potion. He reaches for the ingredient shelf, pausing to explain."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A generic potion recipe is useless — you must specify the <em>ingredients</em>. "
                      + "In Java, those ingredients are called <strong>parameters</strong>. "
                      + "And every worthwhile recipe produces a result — a <strong>return value</strong>."),
                    e("Method with parameters", "public static void brew(String herb, int quantity) {\n"
                      + "    System.out.println(\"Brewing \" + quantity + \" units of \" + herb);\n"
                      + "}"),
                    e("Method with return value", "public static int doublePotion(int potency) {\n"
                      + "    return potency * 2;\n"
                      + "}\n\n"
                      + "int result = doublePotion(15);\n"
                      + "System.out.println(result); // 30"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The <em>parameter</em> is the placeholder in the recipe: <code>int potency</code>. "
                      + "The <em>argument</em> is the actual ingredient you pass: <code>15</code>. "
                      + "Master this distinction and your spells will always be flexible!")
                ),

                // guided practice HTML
                "<p>Write a <code>static int add(int a, int b)</code> method that returns the sum of <code>a</code> and <code>b</code>. "
                + "In <code>main</code>, call it twice and print the results:</p>"
                + "<ul>"
                + "<li><code>add(7, 5)</code> → should print <code>12</code></li>"
                + "<li><code>add(100, 23)</code> → should print <code>123</code></li>"
                + "</ul>",

                // starter code
                "public class MethodParams {\n\n"
                + "    // Define your add(int a, int b) method here\n\n\n"
                + "    public static void main(String[] args) {\n"
                + "        // Call add() and print the results\n\n\n"
                + "    }\n"
                + "}\n",

                // tests
                tests(
                    test("add(7, 5) == 12", "null", "12\n123")
                ),

                // feynman prompt
                "Explain the difference between a parameter and an argument, and why return values make methods more useful."
        );

        // D2 questions
        mcQuestion("D2", QuestionTier.RECALL,
                "<p>What is the term for the actual value you pass when <em>calling</em> a method?</p>",
                new String[]{"Parameter", "Argument", "Return value", "Variable"},
                "Argument",
                "<p>The <em>parameter</em> is the placeholder in the method definition. "
                + "The <em>argument</em> is the concrete value you provide when calling the method.</p>");

        codeQuestion("D2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "public static int square(int n) {\n    return n * n;\n}\n\nSystem.out.println(square(4));\nSystem.out.println(square(3));",
                "16\n9",
                "<p><code>square(4)</code> returns 4×4=16 and <code>square(3)</code> returns 3×3=9.</p>");

        tfQuestion("D2", QuestionTier.RECALL,
                "<p>A method declared with return type <code>int</code> must contain a <code>return</code> statement that returns an <code>int</code>.</p>",
                "True",
                "<p>If the return type is not <code>void</code>, the method must return a value of the declared type "
                + "on every possible execution path, or the code will not compile.</p>");

        codeQuestion("D2", QuestionTier.DISCRIMINATION, QuestionType.DEBUGGING,
                "<p>What is wrong with this method?</p>",
                "public static int multiply(int a, int b) {\n    int result = a * b;\n    System.out.println(result);\n}",
                "The method declares return type int but has no return statement — add 'return result;'",
                "<p>A non-void method must return a value. The method computes <code>result</code> but never returns it. "
                + "Add <code>return result;</code> (or <code>return a * b;</code>) before the closing brace.</p>");

        // =====================================================================
        // D3 — Method Overloading
        // =====================================================================
        subChunk("D3", "D", "Method Overloading", 3, 50, "Overloading.java",

                // hook
                "<p>Can two methods share the same name? In Java, yes — as long as their parameter lists differ. "
                + "This is called <strong>overloading</strong>, and it makes your API feel natural to use.</p>",

                // explanation
                "<h3>Method Overloading</h3>"
                + "<p>Java allows multiple methods with the same name in the same class, "
                + "provided they have different <em>parameter types or counts</em>. "
                + "Java picks the right method at compile time based on what arguments you pass.</p>"
                + "<pre><code>public static void greet(String name) {\n"
                + "    System.out.println(\"Hello, \" + name + \"!\");\n"
                + "}\n\n"
                + "public static void greet(String name, int age) {\n"
                + "    System.out.println(\"Hello, \" + name + \"! You are \" + age + \".\");\n"
                + "}\n\n"
                + "greet(\"Alice\");       // calls the 1-parameter version\n"
                + "greet(\"Bob\", 25);   // calls the 2-parameter version</code></pre>"
                + "<p><strong>Overloading is NOT about return type.</strong> You cannot overload two methods that differ "
                + "only in return type — Java would not know which to call and will refuse to compile.</p>"
                + "<p>You have already seen overloading: <code>System.out.println()</code> is overloaded to accept "
                + "<code>int</code>, <code>String</code>, <code>double</code>, and more — all the same name!</p>",

                // story
                story(
                    n("Deep in the Academy's vault, Archmage Eldrin opens three matching vials, each labelled 'Fireball'."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The Academy's three Fireball incantations share a name — yet each differs by what you provide. "
                      + "A small fireball needs only a target. A medium one needs a target and intensity. "
                      + "The grand fireball needs all three. Java will choose the right one automatically."),
                    e("Three Fireball overloads", "public static void fireball(String target) {\n"
                      + "    System.out.println(\"Small fireball at \" + target);\n"
                      + "}\n\n"
                      + "public static void fireball(String target, int power) {\n"
                      + "    System.out.println(\"Fireball (\" + power + \") at \" + target);\n"
                      + "}\n\n"
                      + "public static void fireball(String target, int power, String colour) {\n"
                      + "    System.out.println(colour + \" fireball (\" + power + \") at \" + target);\n"
                      + "}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The compiler is the academy registrar — it matches your call to the correct overload by examining "
                      + "exactly what you pass. This is why you cannot distinguish overloads by return type alone: "
                      + "the registrar cannot see the return type at the call site!")
                ),

                // guided practice HTML
                "<p>Define three overloaded versions of a method called <code>describe</code>:</p>"
                + "<ol>"
                + "<li><code>describe(String name)</code> — prints <code>Name: Alice</code></li>"
                + "<li><code>describe(String name, int age)</code> — prints <code>Name: Bob, Age: 25</code></li>"
                + "<li><code>describe(String name, int age, String role)</code> — prints <code>Name: Carol, Age: 30, Role: Engineer</code></li>"
                + "</ol>"
                + "<p>Call all three from <code>main</code> with the values shown.</p>",

                // starter code
                "public class Overloading {\n\n"
                + "    // Overload 1: describe(String name)\n\n\n"
                + "    // Overload 2: describe(String name, int age)\n\n\n"
                + "    // Overload 3: describe(String name, int age, String role)\n\n\n"
                + "    public static void main(String[] args) {\n"
                + "        describe(\"Alice\");\n"
                + "        describe(\"Bob\", 25);\n"
                + "        describe(\"Carol\", 30, \"Engineer\");\n"
                + "    }\n"
                + "}\n",

                // tests
                tests(
                    test("All three overloads", "null", "Name: Alice\nName: Bob, Age: 25\nName: Carol, Age: 30, Role: Engineer")
                ),

                // feynman prompt
                "Explain method overloading: what it is, how Java decides which overload to call, and why you cannot overload based on return type alone."
        );

        // D3 questions
        mcQuestion("D3", QuestionTier.RECALL,
                "<p>What makes two methods with the same name valid overloads in Java?</p>",
                new String[]{
                    "They have different return types",
                    "They have different parameter lists (types or counts)",
                    "One is private, the other public",
                    "They are in different files"
                },
                "They have different parameter lists (types or counts)",
                "<p>Overloading is determined by the <em>parameter signature</em>. "
                + "The return type is irrelevant — Java looks at the types and number of parameters to choose the overload.</p>");

        codeQuestion("D3", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>Which method is called and what is printed?</p>",
                "public static void show(int n) { System.out.println(\"int: \" + n); }\npublic static void show(double n) { System.out.println(\"double: \" + n); }\n\nshow(5);",
                "int: 5",
                "<p>The literal <code>5</code> is an <code>int</code>, so Java picks the <code>show(int)</code> overload.</p>");

        tfQuestion("D3", QuestionTier.DISCRIMINATION,
                "<p>You can create two methods with the same name and same parameters if they have different return types.</p>",
                "False",
                "<p>Return type is <em>not</em> part of the method signature for overloading purposes. "
                + "Two methods with the same name and same parameter list are always ambiguous — the code will not compile.</p>");

        // =====================================================================
        // D4 — Scope & Variable Lifetime
        // =====================================================================
        subChunk("D4", "D", "Scope & Variable Lifetime", 4, 50, "Scope.java",

                // hook
                "<p>Why can't you use a variable declared inside an <code>if</code> block once you exit it? "
                + "The answer is <strong>scope</strong> — every variable only lives within the curly braces where it was born.</p>",

                // explanation
                "<h3>Variable Scope</h3>"
                + "<p><strong>Scope</strong> is the region of code where a variable is accessible. "
                + "In Java, scope is determined by curly braces <code>{ }</code>:</p>"
                + "<ul>"
                + "<li><strong>Local scope</strong> — a variable declared inside a method or block lives only until its closing <code>}</code></li>"
                + "<li><strong>Block scope</strong> — variables inside <code>if</code>, <code>for</code>, or <code>while</code> blocks "
                + "are invisible outside them</li>"
                + "<li><strong>Parameter scope</strong> — method parameters are local to the method body</li>"
                + "</ul>"
                + "<pre><code>public static void example() {\n"
                + "    int outer = 10;\n"
                + "    if (true) {\n"
                + "        int inner = 20;\n"
                + "        System.out.println(outer); // OK — outer is in scope\n"
                + "        System.out.println(inner); // OK — inner is in scope here\n"
                + "    }\n"
                + "    System.out.println(outer); // OK\n"
                + "    // System.out.println(inner); // ERROR — inner is out of scope!\n"
                + "}</code></pre>"
                + "<p><strong>Shadowing</strong> — if you declare a variable with the same name in an inner scope, "
                + "it temporarily hides the outer one. This is legal but can be confusing; avoid it when possible.</p>",

                // story
                story(
                    n("Archmage Eldrin leads the apprentice through the Academy's winding corridors. "
                      + "Each room has its own slate board covered in glowing runes."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Every room in the Academy has its own rune board. Runes inscribed in the alchemy lab "
                      + "cannot be read from the library — they exist only within that room's walls. "
                      + "In Java, every pair of curly braces is such a room."),
                    e("Scope of rooms (blocks)", "public static void academy() {\n"
                      + "    int hallRune = 10;          // visible throughout this method\n"
                      + "    if (hallRune > 5) {\n"
                      + "        int labRune = 20;       // visible only in this if-block\n"
                      + "        System.out.println(hallRune); // OK\n"
                      + "        System.out.println(labRune);  // OK\n"
                      + "    }\n"
                      + "    System.out.println(hallRune); // OK\n"
                      + "    // System.out.println(labRune); // ERROR: labRune is in the lab only!\n"
                      + "}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "You can read a rune from any room you can reach from where the rune was inscribed — "
                      + "including inner rooms. But once you leave the lab, the lab's runes vanish.")
                ),

                // guided practice HTML
                "<p>Write a program that demonstrates scope. Declare <code>int outer = 10</code> in the method. "
                + "Inside an <code>if (true)</code> block, declare <code>int inner = 20</code> and print it. "
                + "After the block, print <code>outer</code> again.</p>"
                + "<p>Expected output:</p>"
                + "<pre>Outer: 10\nInner: 20\n10</pre>",

                // starter code
                "public class Scope {\n"
                + "    public static void main(String[] args) {\n"
                + "        int outer = 10;\n"
                + "        System.out.println(\"Outer: \" + outer);\n\n"
                + "        if (true) {\n"
                + "            // TODO: declare int inner = 20 and print \"Inner: \" + inner\n\n"
                + "        }\n\n"
                + "        // TODO: print outer again\n\n"
                + "    }\n"
                + "}\n",

                // tests
                tests(
                    test("Scope demo", "null", "Outer: 10\nInner: 20\n10")
                ),

                // feynman prompt
                "Explain variable scope in Java using a real-world analogy, describing what happens when you try to use a variable outside its scope."
        );

        // D4 questions
        mcQuestion("D4", QuestionTier.RECALL,
                "<p>Where can a variable declared inside a <code>for</code> loop be accessed?</p>",
                new String[]{
                    "Anywhere in the class",
                    "Anywhere in the method",
                    "Only inside the for loop's body",
                    "Only on the line it is declared"
                },
                "Only inside the for loop's body",
                "<p>A variable declared in a <code>for</code> loop header or body is scoped to that loop. "
                + "It cannot be accessed before the loop or after it closes.</p>");

        codeQuestion("D4", QuestionTier.APPLICATION, QuestionType.DEBUGGING,
                "<p>What is wrong with this code?</p>",
                "public static void main(String[] args) {\n    if (true) {\n        int x = 5;\n    }\n    System.out.println(x);\n}",
                "x is declared inside the if block and cannot be accessed outside it — declare x before the if block",
                "<p><code>x</code> is scoped to the <code>if</code> block. Move the declaration before the <code>if</code> "
                + "block to make it accessible after it closes.</p>");

        tfQuestion("D4", QuestionTier.RECALL,
                "<p>A method parameter can be accessed anywhere in the class, not just inside the method.</p>",
                "False",
                "<p>Method parameters have <em>local scope</em> — they exist only within the method body. "
                + "They are destroyed when the method returns.</p>");

        mcQuestion("D4", QuestionTier.DISCRIMINATION,
                "<p>Two variables are declared: <code>int x = 1</code> in the method and <code>int x = 2</code> inside a nested block. What happens?</p>",
                new String[]{
                    "Compile error — duplicate variable",
                    "The inner x shadows the outer x inside the block",
                    "Both variables are merged into one",
                    "The outer x is permanently overwritten"
                },
                "The inner x shadows the outer x inside the block",
                "<p>This is called <em>shadowing</em>. Inside the inner block, <code>x</code> refers to the inner variable. "
                + "Outside the block, <code>x</code> refers to the outer one. Legal but confusing — avoid when possible.</p>");
    }

    // =========================================================================
    // CHUNK E — Strings & Text Processing
    // =========================================================================
    private void seedChunkE() {

        chunk("E", "Strings & Text Processing", "\uD83D\uDCDC", 5, LearnerPath.FOUNDATION, "D");

        // =====================================================================
        // E1 — String Basics & Immutability
        // =====================================================================
        subChunk("E1", "E", "String Basics & Immutability", 1, 50, "StringBasics.java",

                // hook
                "<p>You have used <code>String</code> since day one — but String is not a primitive like <code>int</code>. "
                + "It is an <strong>object</strong>, and that distinction has important consequences for how it behaves.</p>",

                // explanation
                "<h3>String as an Object</h3>"
                + "<p>A <code>String</code> is an instance of the <code>java.lang.String</code> class. "
                + "Because it is an object, it has methods you can call on it.</p>"
                + "<pre><code>String name = \"Arcane Academy\";\n"
                + "System.out.println(name.length());     // 14\n"
                + "System.out.println(name.charAt(7));    // A\n"
                + "System.out.println(name.isEmpty());    // false</code></pre>"
                + "<h3>Immutability</h3>"
                + "<p>Strings are <strong>immutable</strong> — once created, they cannot be changed. "
                + "Any operation that appears to modify a String actually creates a brand-new String object:</p>"
                + "<pre><code>String s = \"hello\";\n"
                + "s = s.toUpperCase(); // creates a NEW String \"HELLO\"\n"
                + "// The original \"hello\" object is unchanged</code></pre>"
                + "<p><strong>String literal pool</strong> — string literals are cached in a special pool. "
                + "Two variables assigned the same literal may point to the same object in memory: "
                + "<code>String a = \"hi\"</code> and <code>String b = \"hi\"</code> often refer to the exact same object.</p>"
                + "<p>Key methods at a glance:</p>"
                + "<ul>"
                + "<li><code>length()</code> — number of characters</li>"
                + "<li><code>charAt(i)</code> — character at index <code>i</code> (0-based)</li>"
                + "<li><code>isEmpty()</code> — true if length is 0</li>"
                + "</ul>",

                // story
                story(
                    n("Archmage Eldrin leads the apprentice to a wall of stone tablets, each engraved with runes."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Observe these tablets. Once the runes are carved into the stone, they cannot be altered — "
                      + "the stone is <strong>immutable</strong>. If I wish to change the inscription, "
                      + "I must carve a <em>new</em> tablet. The original remains in the vault, unchanged."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "This is precisely how Java Strings work. Every 'modification' creates a fresh tablet. "
                      + "But you can always <em>read</em> from a tablet — asking its length, or the rune at a given position."),
                    e("Reading a String", "String name = \"Arcane Academy\";\n"
                      + "System.out.println(name.length());   // 14\n"
                      + "System.out.println(name.charAt(7));  // A\n"
                      + "System.out.println(name.isEmpty());  // false"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Remember: indices start at <strong>zero</strong>. The 'A' in 'Academy' sits at position 7, "
                      + "not 8. A common mistake costs many apprentices dearly!")
                ),

                // guided practice HTML
                "<p>Given the string <code>\"Arcane Academy\"</code>, print three things on separate lines:</p>"
                + "<ol>"
                + "<li>Its length</li>"
                + "<li>The character at index 7</li>"
                + "<li>Whether it is empty</li>"
                + "</ol>",

                // starter code
                "String s = \"Arcane Academy\";\n\n// Print the length\n\n// Print the character at index 7\n\n// Print whether it's empty\n",

                // tests
                tests(
                    test("Length, charAt, isEmpty", "null", "14\nA\nfalse")
                ),

                // feynman prompt
                "Explain String immutability in Java: what it means, why it matters, and what actually happens when you 'change' a String."
        );

        // E1 questions
        mcQuestion("E1", QuestionTier.RECALL,
                "<p>What does <code>\"Hello\".charAt(1)</code> return?</p>",
                new String[]{"H", "e", "l", "1"},
                "e",
                "<p>String indices are 0-based. Index 0 is 'H', index 1 is 'e'.</p>");

        tfQuestion("E1", QuestionTier.RECALL,
                "<p>Calling <code>toUpperCase()</code> on a String changes the original String object.</p>",
                "False",
                "<p>Strings are immutable. <code>toUpperCase()</code> returns a <em>new</em> String. "
                + "The original is unchanged. You must assign the result: <code>s = s.toUpperCase();</code>.</p>");

        codeQuestion("E1", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "String s = \"Java\";\nSystem.out.println(s.length());\nSystem.out.println(s.charAt(0));",
                "4\nJ",
                "<p><code>\"Java\"</code> has 4 characters. Index 0 is 'J'.</p>");

        mcQuestion("E1", QuestionTier.DISCRIMINATION,
                "<p>Why is String immutability considered a design advantage?</p>",
                new String[]{
                    "It makes Strings faster to create",
                    "It enables safe sharing and caching of String objects",
                    "It prevents Strings from being stored in variables",
                    "It forces all Strings to be uppercase"
                },
                "It enables safe sharing and caching of String objects",
                "<p>Because Strings cannot change, they can be safely shared between multiple variables and threads "
                + "without risk of one part of the code unexpectedly modifying a value another part depends on. "
                + "This enables the string literal pool.</p>");

        // =====================================================================
        // E2 — String Methods
        // =====================================================================
        subChunk("E2", "E", "String Methods", 2, 50, "StringMethods.java",

                // hook
                "<p>Java's <code>String</code> class comes with dozens of built-in methods — "
                + "spells for slicing, searching, trimming, and transforming text without writing any extra logic yourself.</p>",

                // explanation
                "<h3>Essential String Methods</h3>"
                + "<p>All of these methods return a <em>new</em> String (remember: immutability).</p>"
                + "<pre><code>String s = \"  Hello, World!  \";\n\n"
                + "s.toUpperCase()          // \"  HELLO, WORLD!  \"\n"
                + "s.toLowerCase()          // \"  hello, world!  \"\n"
                + "s.trim()                 // \"Hello, World!\" (removes leading/trailing spaces)\n"
                + "s.trim().substring(0,5)  // \"Hello\" (chars from index 0 up to but not including 5)\n"
                + "s.indexOf(\"World\")       // 9 (position of first match, or -1 if not found)\n"
                + "s.contains(\"World\")      // true\n"
                + "s.replace(\"World\",\"Java\") // \"  Hello, Java!  \"\n"
                + "s.split(\", \")            // [\"  Hello\", \"World!  \"] array of parts</code></pre>"
                + "<p><strong>substring(start, end)</strong> — note that <code>end</code> is <em>exclusive</em>: "
                + "<code>\"Hello\".substring(1, 3)</code> returns <code>\"el\"</code>.</p>"
                + "<p><strong>indexOf</strong> returns the starting index of the first occurrence, or <code>-1</code> if not found.</p>",

                // story
                story(
                    n("Archmage Eldrin spreads a dusty prophecy scroll on the table. "
                      + "It is smudged, oddly capitalised, and needs work before the apprentice can read it."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "This scroll has come from a far archive — dusty, misaligned, in all the wrong cases. "
                      + "String methods are my toolkit for transforming such a raw text into something usable."),
                    e("Transforming the prophecy", "String scroll = \"  the Arcane PROPHECY speaks of doom  \";\n"
                      + "String cleaned  = scroll.trim();\n"
                      + "String upper    = cleaned.toUpperCase();\n"
                      + "String keyPhrase = cleaned.substring(4, 20);\n"
                      + "System.out.println(cleaned);\n"
                      + "System.out.println(upper);\n"
                      + "System.out.println(keyPhrase);"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "<code>trim()</code> removes the leading and trailing dust — spaces. "
                      + "<code>toUpperCase()</code> shouts the prophecy loudly. "
                      + "<code>substring(4, 20)</code> extracts exactly the key phrase we need. "
                      + "Each method returns a <em>new scroll</em> — the original is preserved in the vault!")
                ),

                // guided practice HTML
                "<p>Given <code>String spell = \"  fireball of doom  \"</code>, print three things:</p>"
                + "<ol>"
                + "<li>The trimmed version (no leading/trailing spaces)</li>"
                + "<li>The trimmed version in uppercase</li>"
                + "<li>The first 8 characters of the trimmed version (should be <code>fireball</code>)</li>"
                + "</ol>",

                // starter code
                "String spell = \"  fireball of doom  \";\n\n// Print trimmed\n\n// Print trimmed uppercase\n\n// Print first 8 chars of trimmed\n",

                // tests
                tests(
                    test("trim, toUpperCase, substring", "null", "fireball of doom\nFIREBALL OF DOOM\nfireball")
                ),

                // feynman prompt
                "Describe at least five String methods, what they do, and give a small example of each."
        );

        // E2 questions
        mcQuestion("E2", QuestionTier.RECALL,
                "<p>What does <code>\"  hello  \".trim()</code> return?</p>",
                new String[]{"\"  hello  \"", "\"hello\"", "\"HELLO\"", "\"hello  \""},
                "\"hello\"",
                "<p><code>trim()</code> removes all leading and trailing whitespace characters, returning <code>\"hello\"</code>.</p>");

        codeQuestion("E2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "String s = \"Arcane\";\nSystem.out.println(s.substring(0, 3));\nSystem.out.println(s.indexOf(\"c\"));",
                "Arc\n2",
                "<p><code>substring(0,3)</code> returns characters at indices 0, 1, 2 → <code>\"Arc\"</code>. "
                + "<code>indexOf(\"c\")</code> finds 'c' at index 2.</p>");

        tfQuestion("E2", QuestionTier.RECALL,
                "<p><code>\"apple\".indexOf(\"z\")</code> returns -1.</p>",
                "True",
                "<p><code>indexOf</code> returns -1 when the specified substring or character is not found.</p>");

        mcQuestion("E2", QuestionTier.DISCRIMINATION,
                "<p>You want to check if a string ends with <code>\".java\"</code>. Which method is most appropriate?</p>",
                new String[]{"indexOf", "substring", "endsWith", "contains"},
                "endsWith",
                "<p><code>endsWith(String suffix)</code> returns <code>true</code> if the string ends with the given suffix. "
                + "While <code>contains</code> or <code>indexOf</code> could work, <code>endsWith</code> is more expressive and correct.</p>");

        // =====================================================================
        // E3 — String Comparison & Formatting
        // =====================================================================
        subChunk("E3", "E", "String Comparison & Formatting", 3, 50, "StringCompare.java",

                // hook
                "<p>Why does <code>\"hello\" == \"hello\"</code> sometimes give unexpected results — "
                + "and how do you produce beautifully formatted output like <code>\"The score is 95.00\"</code>?</p>",

                // explanation
                "<h3>Comparing Strings: .equals() vs ==</h3>"
                + "<p>The <code>==</code> operator compares <em>references</em> — whether two variables point to the "
                + "exact same object in memory. With String literals this sometimes works (because of the string pool), "
                + "but with Strings created at runtime it often fails.</p>"
                + "<p>Always use <code>.equals()</code> to compare String <em>contents</em>:</p>"
                + "<pre><code>String a = new String(\"hello\");\n"
                + "String b = new String(\"hello\");\n"
                + "System.out.println(a == b);         // false — different objects!\n"
                + "System.out.println(a.equals(b));    // true  — same contents</code></pre>"
                + "<p><code>equalsIgnoreCase()</code> — compares without case sensitivity:</p>"
                + "<pre><code>\"Wizard\".equalsIgnoreCase(\"wizard\"); // true</code></pre>"
                + "<h3>String Formatting</h3>"
                + "<p><code>String.format()</code> and <code>System.out.printf()</code> use format specifiers:</p>"
                + "<ul>"
                + "<li><code>%s</code> — String</li>"
                + "<li><code>%d</code> — integer</li>"
                + "<li><code>%f</code> — floating point</li>"
                + "<li><code>%.2f</code> — floating point with 2 decimal places</li>"
                + "</ul>"
                + "<pre><code>String msg = String.format(\"The %s ranks as level %d\", \"Apprentice\", 3);\n"
                + "System.out.println(msg); // The Apprentice ranks as level 3</code></pre>",

                // story
                story(
                    n("Archmage Eldrin holds two identical-looking scrolls, one in each hand. "
                      + "The apprentice squints — they look exactly alike."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "These two scrolls contain the same prophecy, word for word. Yet they are stored in "
                      + "different vaults — different <em>locations</em> in the archive. "
                      + "If you ask the librarian '==' — 'are these the exact same physical scroll?' — she says no. "
                      + "But if you ask '.equals()' — 'do they say the same thing?' — she says yes!"),
                    e("== vs .equals()", "String a = new String(\"Archmage\");\n"
                      + "String b = new String(\"Archmage\");\n"
                      + "System.out.println(a == b);              // false (different vaults)\n"
                      + "System.out.println(a.equals(b));         // true  (same contents)\n"
                      + "System.out.println(a.equalsIgnoreCase(\"archmage\")); // true"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "And when formatting proclamations, use <code>String.format()</code> — "
                      + "it lets you slot values into a template with precision. Perfect for tournament announcements!"),
                    e("Formatted proclamation", "String.format(\"The %s ranks as level %d\", \"Apprentice\", 3);\n"
                      + "// → \"The Apprentice ranks as level 3\"")
                ),

                // guided practice HTML
                "<p>Given <code>String a = \"Wizard\"</code> and <code>String b = \"wizard\"</code>, print three things:</p>"
                + "<ol>"
                + "<li>Whether <code>a.equals(b)</code> (true/false)</li>"
                + "<li>Whether <code>a.equalsIgnoreCase(b)</code> (true/false)</li>"
                + "<li>The result of <code>String.format(\"The %s ranks as level %d\", \"Apprentice\", 3)</code></li>"
                + "</ol>",

                // starter code
                "String a = \"Wizard\";\nString b = \"wizard\";\n\n// Print equals result\n\n// Print equalsIgnoreCase result\n\n// Print formatted string\n",

                // tests
                tests(
                    test("equals, equalsIgnoreCase, format", "null", "false\ntrue\nThe Apprentice ranks as level 3")
                ),

                // feynman prompt
                "Explain why you should always use .equals() instead of == for String comparison in Java, and describe how String.format() works."
        );

        // E3 questions
        mcQuestion("E3", QuestionTier.RECALL,
                "<p>Which method compares the <em>contents</em> of two Strings in Java?</p>",
                new String[]{"==", ".equals()", ".compareTo()", ".matches()"},
                ".equals()",
                "<p><code>.equals()</code> compares the actual character sequence. "
                + "<code>==</code> compares object references (memory addresses), not contents.</p>");

        codeQuestion("E3", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "String s = String.format(\"Score: %d / %.1f\", 95, 100.0);\nSystem.out.println(s);",
                "Score: 95 / 100.0",
                "<p><code>%d</code> formats the integer 95, <code>%.1f</code> formats 100.0 with one decimal place.</p>");

        tfQuestion("E3", QuestionTier.RECALL,
                "<p>\"hello\".equalsIgnoreCase(\"HELLO\") returns true.</p>",
                "True",
                "<p><code>equalsIgnoreCase</code> ignores case differences when comparing. "
                + "<code>\"hello\"</code> and <code>\"HELLO\"</code> have the same letters, so it returns <code>true</code>.</p>");

        mcQuestion("E3", QuestionTier.DISCRIMINATION,
                "<p>A user types a password at runtime (not a literal). Which comparison is correct?</p>",
                new String[]{
                    "if (input == storedPassword)",
                    "if (input.equals(storedPassword))",
                    "if (input.length() == storedPassword.length())",
                    "if (input.contains(storedPassword))"
                },
                "if (input.equals(storedPassword))",
                "<p>Runtime Strings are not guaranteed to share memory, so <code>==</code> will give wrong results. "
                + "Always use <code>.equals()</code> to compare String contents reliably.</p>");

        // =====================================================================
        // E4 — StringBuilder
        // =====================================================================
        subChunk("E4", "E", "StringBuilder", 4, 60, "StringBuilderEx.java",

                // hook
                "<p>If you concatenate Strings inside a loop using <code>+</code>, Java creates a brand-new String object "
                + "on every iteration. Concatenating 1,000 strings produces 999 throwaway objects. "
                + "<strong>StringBuilder</strong> solves this with a single mutable buffer.</p>",

                // explanation
                "<h3>StringBuilder: The Efficient Scroll</h3>"
                + "<p><code>StringBuilder</code> is a mutable sequence of characters. Unlike <code>String</code>, "
                + "it can be modified in place without creating new objects.</p>"
                + "<pre><code>StringBuilder sb = new StringBuilder();\n"
                + "sb.append(\"Hello\");\n"
                + "sb.append(\", \");\n"
                + "sb.append(\"World\");\n"
                + "System.out.println(sb.toString()); // Hello, World</code></pre>"
                + "<p>Key methods:</p>"
                + "<ul>"
                + "<li><code>append(x)</code> — adds x to the end</li>"
                + "<li><code>insert(index, x)</code> — inserts x at the given position</li>"
                + "<li><code>delete(start, end)</code> — removes characters from start to end-1</li>"
                + "<li><code>reverse()</code> — reverses the contents</li>"
                + "<li><code>toString()</code> — converts to a regular <code>String</code></li>"
                + "</ul>"
                + "<p><strong>Why it matters in loops:</strong></p>"
                + "<pre><code>// Bad — creates a new String each iteration:\n"
                + "String result = \"\";\n"
                + "for (int i = 1; i <= 1000; i++) {\n"
                + "    result += i; // 999 garbage String objects!\n"
                + "}\n\n"
                + "// Good — one mutable buffer:\n"
                + "StringBuilder sb = new StringBuilder();\n"
                + "for (int i = 1; i <= 1000; i++) {\n"
                + "    sb.append(i);\n"
                + "}\n"
                + "String result = sb.toString();</code></pre>",

                // story
                story(
                    n("Archmage Eldrin faces an enormous wall scroll. A novice apprentice is painstakingly rewriting the entire "
                      + "scroll from scratch each time he needs to add a single sentence."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Stop! Every time you add a sentence, you copy every word that came before it onto a new scroll — "
                      + "then discard the old one. By the time you have written the prophecy's hundredth sentence, "
                      + "you have filled the vault with ninety-nine useless scrolls."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Instead, use a <strong>living scroll</strong> — a <code>StringBuilder</code>. "
                      + "Append each sentence directly to the same surface. "
                      + "Only at the end do you seal it into a permanent, immutable <code>String</code>."),
                    e("Building a prophecy efficiently", "StringBuilder prophecy = new StringBuilder();\n"
                      + "prophecy.append(\"In the age of runes, \");\n"
                      + "prophecy.append(\"darkness shall rise, \");\n"
                      + "prophecy.append(\"and the Apprentice shall prevail.\");\n"
                      + "System.out.println(prophecy.toString());"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "One scroll, three sentences appended directly. No waste. "
                      + "Call <code>toString()</code> at the very end when you need the sealed, immutable result.")
                ),

                // guided practice HTML
                "<p>Use a <code>StringBuilder</code> to build a comma-separated list of numbers 1 through 5: "
                + "<code>1, 2, 3, 4, 5</code>. Print the final result.</p>"
                + "<p><em>Hint:</em> append each number, and for numbers 1–4 also append <code>\", \"</code>. "
                + "For the last number (5), do not append a trailing comma.</p>",

                // starter code
                "public class StringBuilderEx {\n"
                + "    public static void main(String[] args) {\n"
                + "        StringBuilder sb = new StringBuilder();\n\n"
                + "        // Build \"1, 2, 3, 4, 5\" using a loop\n\n\n"
                + "        System.out.println(sb.toString());\n"
                + "    }\n"
                + "}\n",

                // tests
                tests(
                    test("Comma-separated 1 to 5", "null", "1, 2, 3, 4, 5")
                ),

                // feynman prompt
                "Explain why StringBuilder is more efficient than using + for String concatenation inside a loop, and describe three useful StringBuilder methods."
        );

        // E4 questions
        mcQuestion("E4", QuestionTier.RECALL,
                "<p>Which method converts a <code>StringBuilder</code> to a regular <code>String</code>?</p>",
                new String[]{"getValue()", "toString()", "build()", "finalize()"},
                "toString()",
                "<p><code>sb.toString()</code> returns the current contents of the StringBuilder as an immutable <code>String</code>.</p>");

        tfQuestion("E4", QuestionTier.RECALL,
                "<p>StringBuilder objects are mutable — you can change their contents after creation.</p>",
                "True",
                "<p>That is the key difference from <code>String</code>. <code>StringBuilder</code> maintains a "
                + "resizable internal buffer that can be modified with <code>append</code>, <code>insert</code>, "
                + "<code>delete</code>, etc.</p>");

        codeQuestion("E4", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "StringBuilder sb = new StringBuilder(\"Hello\");\nsb.reverse();\nSystem.out.println(sb.toString());",
                "olleH",
                "<p><code>reverse()</code> reverses the character sequence in place. <code>\"Hello\"</code> reversed is <code>\"olleH\"</code>.</p>");

        mcQuestion("E4", QuestionTier.DISCRIMINATION,
                "<p>In which situation is it MOST important to use StringBuilder instead of String concatenation?</p>",
                new String[]{
                    "Joining two strings once outside a loop",
                    "Concatenating strings inside a loop that runs thousands of times",
                    "Calling toUpperCase() on a string",
                    "Comparing two strings for equality"
                },
                "Concatenating strings inside a loop that runs thousands of times",
                "<p>String <code>+</code> in a loop creates a new object every iteration. "
                + "For small or one-off concatenations, <code>+</code> is fine. "
                + "For loops, use <code>StringBuilder</code> to avoid creating thousands of intermediate objects.</p>");
    }

    // =========================================================================
    // CHUNK F — Console I/O
    // =========================================================================
    private void seedChunkF() {

        chunk("F", "Console I/O", "\uD83D\uDCAC", 6, LearnerPath.FOUNDATION, "D");

        // =====================================================================
        // F1 — Output: System.out
        // =====================================================================
        subChunk("F1", "F", "Output: System.out", 1, 40, "SystemOut.java",

                // hook
                "<p>You have been using <code>System.out.println()</code> since your very first line of Java. "
                + "But <code>println</code> is only one of three output spells — and the others give you far more control.</p>",

                // explanation
                "<h3>Three Output Methods</h3>"
                + "<p><code>System.out</code> is a PrintStream that writes to the console. It has three key methods:</p>"
                + "<ul>"
                + "<li><code>println(x)</code> — prints x then moves to a new line</li>"
                + "<li><code>print(x)</code> — prints x <em>without</em> a trailing newline</li>"
                + "<li><code>printf(format, args...)</code> — formatted output using format specifiers</li>"
                + "</ul>"
                + "<pre><code>System.out.println(\"Line 1\"); // prints and moves to next line\n"
                + "System.out.print(\"A\");\n"
                + "System.out.print(\"B\");\n"
                + "System.out.println(\"C\"); // AB and C are on the same line, then newline</code></pre>"
                + "<h3>printf Format Specifiers</h3>"
                + "<pre><code>System.out.printf(\"Name: %s%n\", \"Eldrin\");\n"
                + "System.out.printf(\"Level: %d%n\", 99);\n"
                + "System.out.printf(\"Power: %.2f%n\", 9.5);\n"
                + "// Output:\n"
                + "// Name: Eldrin\n"
                + "// Level: 99\n"
                + "// Power: 9.50</code></pre>"
                + "<p>Common specifiers: <code>%s</code> (String), <code>%d</code> (int), <code>%f</code> (double), "
                + "<code>%.2f</code> (2 decimal places). Use <code>%n</code> for a platform-safe newline in printf.</p>"
                + "<h3>Escape Sequences</h3>"
                + "<p>Inside string literals: <code>\\t</code> = tab, <code>\\n</code> = newline, "
                + "<code>\\\\</code> = literal backslash, <code>\\\"</code> = literal quote.</p>",

                // story
                story(
                    n("Archmage Eldrin demonstrates three speaking spells to the assembled apprentices."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Three incantations — each suited to a different occasion. "
                      + "<code>println</code> shouts your words and stamps a full stop at the end, moving you to the next line. "
                      + "<code>print</code> whispers, leaving no line break. "
                      + "And <code>printf</code>? That is a <em>precise proclamation</em> — you control every character of the output."),
                    e("All three in action", "System.out.println(\"Shout!\");    // newline appended\n"
                      + "System.out.print(\"Whisper \");   // no newline\n"
                      + "System.out.print(\"continues \"); // same line\n"
                      + "System.out.println(\"here.\");    // newline\n"
                      + "System.out.printf(\"Power: %.2f%n\", 9.5); // Power: 9.50"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The <code>%.2f</code> incantation instructs Java to render a floating-point number "
                      + "with exactly two decimal places — no more, no fewer. Precision is power, apprentice!")
                ),

                // guided practice HTML
                "<p>Print three lines using the appropriate output methods:</p>"
                + "<ol>"
                + "<li>Print <code>Name: Eldrin</code> using <code>println</code></li>"
                + "<li>Print <code>Level: 99</code> using <code>println</code></li>"
                + "<li>Print <code>Power: 9.50</code> using <code>printf</code> with <code>%.2f</code></li>"
                + "</ol>",

                // starter code
                "// Print Name: Eldrin\n\n// Print Level: 99\n\n// Print Power: 9.50 using printf\n",

                // tests
                tests(
                    test("println and printf", "null", "Name: Eldrin\nLevel: 99\nPower: 9.50")
                ),

                // feynman prompt
                "Explain the difference between System.out.println, System.out.print, and System.out.printf, including when you would use each."
        );

        // F1 questions
        mcQuestion("F1", QuestionTier.RECALL,
                "<p>Which method prints output WITHOUT moving to a new line?</p>",
                new String[]{"System.out.println()", "System.out.print()", "System.out.printf()", "System.out.newline()"},
                "System.out.print()",
                "<p><code>print()</code> outputs the value without appending a newline character. "
                + "<code>println()</code> always adds a newline at the end.</p>");

        codeQuestion("F1", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "System.out.print(\"A\");\nSystem.out.print(\"B\");\nSystem.out.println(\"C\");\nSystem.out.println(\"D\");",
                "ABC\nD",
                "<p><code>print</code> outputs without newlines, so A, B, C appear on the same line "
                + "(the <code>println</code> for C adds the newline). Then D is on its own line.</p>");

        tfQuestion("F1", QuestionTier.RECALL,
                "<p><code>System.out.printf(\"%.0f\", 3.7)</code> prints <code>4</code>.</p>",
                "True",
                "<p><code>%.0f</code> formats a floating-point number with zero decimal places, which rounds to the nearest integer. 3.7 rounds to 4.</p>");

        mcQuestion("F1", QuestionTier.DISCRIMINATION,
                "<p>You want to print a formatted table where each row shows a name and a price with 2 decimal places. Which output method is best?</p>",
                new String[]{"println", "print", "printf", "toString"},
                "printf",
                "<p><code>printf</code> with format specifiers like <code>%-10s %6.2f%n</code> gives precise control "
                + "over column widths and decimal places, making it ideal for formatted tables.</p>");

        // =====================================================================
        // F2 — Input: Scanner Basics
        // =====================================================================
        subChunk("F2", "F", "Input: Scanner Basics", 2, 50, "ScannerBasics.java",

                // hook
                "<p>Your programs so far only speak — they never listen. "
                + "To read input from the user, you need a <strong>Scanner</strong>: Java's listening orb.</p>",

                // explanation
                "<h3>Reading Input with Scanner</h3>"
                + "<p>Scanner is in the <code>java.util</code> package — you must import it:</p>"
                + "<pre><code>import java.util.Scanner;</code></pre>"
                + "<p>Create a Scanner connected to the keyboard (<code>System.in</code>):</p>"
                + "<pre><code>Scanner sc = new Scanner(System.in);</code></pre>"
                + "<p>Read different types of input:</p>"
                + "<pre><code>String name = sc.nextLine();   // reads a full line of text\n"
                + "int age    = sc.nextInt();     // reads the next integer\n"
                + "double d   = sc.nextDouble();  // reads the next decimal number</code></pre>"
                + "<p>Always close the Scanner when done to release system resources:</p>"
                + "<pre><code>sc.close();</code></pre>"
                + "<p>In the test system, input is injected automatically — your program reads from the test input "
                + "as if the user typed it. The Scanner code is identical whether the input comes from a keyboard "
                + "or from the test runner.</p>",

                // story
                story(
                    n("Archmage Eldrin produces a glowing crystal orb from his robes and places it on the lectern."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Until now your spells have been proclamations — they speak, but they do not listen. "
                      + "This orb is the <strong>Scanner</strong> — it captures the words of whoever stands before it. "
                      + "Ask it for a name, a number, any input, and it will deliver."),
                    e("Opening the listening orb", "import java.util.Scanner;\n\n"
                      + "public class Listen {\n"
                      + "    public static void main(String[] args) {\n"
                      + "        Scanner sc = new Scanner(System.in);\n"
                      + "        String name = sc.nextLine();\n"
                      + "        int age = sc.nextInt();\n"
                      + "        System.out.println(\"Hello, \" + name + \"! You are \" + age + \" years old.\");\n"
                      + "        sc.close();\n"
                      + "    }\n"
                      + "}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The orb listens for a full line first — that is the name. "
                      + "Then it listens for a number — the age. "
                      + "Always remember to close the orb when you are done, or resources leak from the Academy!")
                ),

                // guided practice HTML
                "<p>Write a program that reads a <strong>name</strong> (whole line) and an <strong>age</strong> (integer) from the Scanner, "
                + "then prints: <code>Hello, [name]! You are [age] years old.</code></p>",

                // starter code
                "import java.util.Scanner;\n\n"
                + "public class ScannerBasics {\n"
                + "    public static void main(String[] args) {\n"
                + "        Scanner sc = new Scanner(System.in);\n\n"
                + "        // Read name (use nextLine)\n"
                + "        String name = sc.nextLine();\n\n"
                + "        // Read age (use nextInt)\n"
                + "        int age = sc.nextInt();\n\n"
                + "        // Print the greeting\n\n\n"
                + "        sc.close();\n"
                + "    }\n"
                + "}\n",

                // tests
                tests(
                    test("Read name and age", "Merlin\n42", "Hello, Merlin! You are 42 years old.")
                ),

                // feynman prompt
                "Explain how the Scanner class works to read user input, why we need to import it, and what nextLine vs nextInt does."
        );

        // F2 questions
        mcQuestion("F2", QuestionTier.RECALL,
                "<p>Which import statement is needed to use <code>Scanner</code>?</p>",
                new String[]{"import java.io.Scanner;", "import java.util.Scanner;", "import java.lang.Scanner;", "import scanner.*;"},
                "import java.util.Scanner;",
                "<p><code>Scanner</code> lives in the <code>java.util</code> package. "
                + "Without the import, the compiler cannot find the class.</p>");

        codeQuestion("F2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>Given input <code>Alice\\n</code>, what does this code print?</p>",
                "Scanner sc = new Scanner(System.in);\nString name = sc.nextLine();\nSystem.out.println(\"Hi, \" + name + \"!\");",
                "Hi, Alice!",
                "<p><code>nextLine()</code> reads the whole line (\"Alice\"). The program prints the greeting with that name.</p>");

        tfQuestion("F2", QuestionTier.RECALL,
                "<p><code>nextLine()</code> reads only one word, stopping at whitespace.</p>",
                "False",
                "<p><code>nextLine()</code> reads everything up to (and consuming) the newline character, "
                + "so it captures a full line including spaces. <code>next()</code> reads only one token (up to whitespace).</p>");

        fillQuestion("F2", QuestionTier.APPLICATION,
                "<p>Fill in the blank to create a Scanner that reads from the keyboard:<br>"
                + "<code>Scanner sc = new Scanner(________);</code></p>",
                "System.in",
                "<p><code>System.in</code> is the standard input stream, connected to the keyboard. "
                + "Passing it to the Scanner constructor enables reading keyboard input.</p>");

        // =====================================================================
        // F3 — Reading Different Types & Validation
        // =====================================================================
        subChunk("F3", "F", "Reading Different Types & Validation", 3, 50, "ScannerTypes.java",

                // hook
                "<p>What happens when your user types <code>banana</code> where your program expected a number? "
                + "Without validation, your program crashes. Let's learn how to read different types safely "
                + "— and avoid a classic Scanner pitfall.</p>",

                // explanation
                "<h3>Scanner Type Methods</h3>"
                + "<p>Scanner provides type-specific read methods:</p>"
                + "<pre><code>Scanner sc = new Scanner(System.in);\n"
                + "int i    = sc.nextInt();    // reads next token as int\n"
                + "double d = sc.nextDouble(); // reads next token as double\n"
                + "String w = sc.next();       // reads next whitespace-delimited token\n"
                + "String l = sc.nextLine();   // reads remaining line including spaces</code></pre>"
                + "<h3>The nextLine-after-nextInt Pitfall</h3>"
                + "<p>When <code>nextInt()</code> reads a number, it consumes the digits but leaves the newline "
                + "character in the buffer. The very next <code>nextLine()</code> call reads that leftover newline "
                + "and returns an empty String:</p>"
                + "<pre><code>int age = sc.nextInt();\n"
                + "sc.nextLine(); // consume the leftover newline\n"
                + "String name = sc.nextLine(); // NOW reads the actual next line</code></pre>"
                + "<p>Fix: add a blank <code>sc.nextLine()</code> call after any <code>nextInt()</code> or "
                + "<code>nextDouble()</code> if you plan to read a full line afterward.</p>"
                + "<h3>Input Validation with hasNextInt()</h3>"
                + "<pre><code>if (sc.hasNextInt()) {\n"
                + "    int value = sc.nextInt();\n"
                + "} else {\n"
                + "    System.out.println(\"That wasn't a number!\");\n"
                + "}</code></pre>",

                // story
                story(
                    n("Archmage Eldrin's enchanted orb begins vibrating erratically. "
                      + "An apprentice has just offered it the word 'banana' when it expected a number."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The orb only speaks the language of integers when you invoke <code>nextInt()</code>. "
                      + "Feed it text, and the ritual collapses with an <code>InputMismatchException</code>. "
                      + "Always validate your offerings before the ceremony begins."),
                    e("Checking before reading", "if (sc.hasNextInt()) {\n"
                      + "    int power = sc.nextInt();\n"
                      + "    System.out.println(\"Power set to: \" + power);\n"
                      + "} else {\n"
                      + "    System.out.println(\"Invalid offering — expected a number!\");\n"
                      + "}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "And beware the silent curse — the lingering newline! When you invoke <code>nextInt()</code>, "
                      + "it sips the number but leaves the enter-key's ghost in the buffer. "
                      + "Your next <code>nextLine()</code> will capture that ghost — an empty String — not the line you wanted. "
                      + "Banish it first with a sacrificial <code>sc.nextLine()</code>.")
                ),

                // guided practice HTML
                "<p>Write a program that reads two integers and prints their sum.</p>"
                + "<p>Expected: given input <code>15</code> and <code>27</code>, print <code>42</code>.</p>",

                // starter code
                "import java.util.Scanner;\n\n"
                + "public class ScannerTypes {\n"
                + "    public static void main(String[] args) {\n"
                + "        Scanner sc = new Scanner(System.in);\n\n"
                + "        // Read first integer\n"
                + "        int a = sc.nextInt();\n\n"
                + "        // Read second integer\n"
                + "        int b = sc.nextInt();\n\n"
                + "        // Print their sum\n\n\n"
                + "        sc.close();\n"
                + "    }\n"
                + "}\n",

                // tests
                tests(
                    test("Sum of two integers", "15\n27", "42")
                ),

                // feynman prompt
                "Explain the nextLine-after-nextInt pitfall in Java's Scanner class: why it happens and how to fix it."
        );

        // F3 questions
        mcQuestion("F3", QuestionTier.RECALL,
                "<p>Which Scanner method reads only one whitespace-delimited token (not the entire line)?</p>",
                new String[]{"nextLine()", "nextToken()", "next()", "readWord()"},
                "next()",
                "<p><code>next()</code> reads the next token up to (but not including) whitespace. "
                + "<code>nextLine()</code> reads until the end of the line including spaces.</p>");

        codeQuestion("F3", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>Given input <code>3\\n7\\n</code>, what does this program print?</p>",
                "Scanner sc = new Scanner(System.in);\nint a = sc.nextInt();\nint b = sc.nextInt();\nSystem.out.println(a + b);",
                "10",
                "<p><code>nextInt()</code> reads 3 then 7. <code>3 + 7 = 10</code>.</p>");

        tfQuestion("F3", QuestionTier.RECALL,
                "<p>After calling <code>sc.nextInt()</code>, the newline character from pressing Enter remains in the Scanner's buffer.</p>",
                "True",
                "<p><code>nextInt()</code> (and <code>nextDouble()</code>) read only the token, leaving the newline. "
                + "A subsequent <code>nextLine()</code> will consume that empty newline, appearing to return an empty string. "
                + "Fix by calling <code>sc.nextLine()</code> immediately after <code>nextInt()</code> to clear the buffer.</p>");

        mcQuestion("F3", QuestionTier.DISCRIMINATION,
                "<p>Your program reads an integer with <code>nextInt()</code> and then a name with <code>nextLine()</code>. "
                + "The name always comes back empty. What is the fix?</p>",
                new String[]{
                    "Use nextLine() to read the integer instead",
                    "Add a sc.nextLine() call between nextInt() and the name nextLine()",
                    "Switch to a different Scanner constructor",
                    "Read the name with next() instead"
                },
                "Add a sc.nextLine() call between nextInt() and the name nextLine()",
                "<p>After <code>nextInt()</code>, a newline remains in the buffer. "
                + "An extra <code>sc.nextLine()</code> consumes that leftover newline, "
                + "so the subsequent <code>nextLine()</code> correctly reads the name.</p>");
    }
}
