package com.arcane.academy.config;

import com.arcane.academy.repository.QuestRepository;
import org.springframework.stereotype.Component;

// ══════════════════════════════════════════════════════════════════════════════
// CHAPTER II — THE CONTROL TOME
// ══════════════════════════════════════════════════════════════════════════════
@Component
public class Ch2Seeder extends AbstractChapterSeeder {

    public Ch2Seeder(QuestRepository questRepository) {
        super(questRepository);
    }

    @Override
    public void seed() {

        q("ch2-q1","The Oracle's Fork","Chapter II · Quest 1","If / Else",2,1,120,"OraclesFork.java",
          story(
            n("The Bridge of Aethon spans a bottomless chasm shrouded in mist. You've heard about it from older students — a legendary checkpoint that separates those who can think logically from those who cannot. The Bridge Keeper materialises from the fog, a tall cloaked figure whose face you cannot see."),
            d("🧟","enemy","The Bridge Keeper","s-enemy","I judge every traveller by their gold. Three rules. Three outcomes. You will write the logic or you will not cross."),
            d("🧙","mentor","Master Velan","s-mentor","This is where we meet the <em>if statement</em> — Java's most fundamental decision tool. An if statement asks a question. If the answer is true, it runs the code inside its curly braces. If false, it skips it."),
            e("If Statement — Basic Form",
              "<span class='kw'>int</span> mana = <span class='num'>75</span>;\n\n<span class='kw'>if</span> (mana > <span class='num'>50</span>) {\n    System.out.println(<span class='str'>\"Mana is high\"</span>);\n}\n<span class='cm'>// The condition (mana > 50) is true, so this prints</span>"),
            d("🧙","mentor","Master Velan","s-mentor","But what if you need a fallback? Add <em>else</em> — a block that runs only when the condition is false. And if you have several possible outcomes, chain <em>else if</em> between them. Only the first true branch ever runs."),
            e("If / Else If / Else — Full Chain",
              "<span class='kw'>int</span> score = <span class='num'>65</span>;\n\n<span class='kw'>if</span> (score >= <span class='num'>90</span>) {\n    System.out.println(<span class='str'>\"Distinction\"</span>);\n} <span class='kw'>else if</span> (score >= <span class='num'>60</span>) {\n    System.out.println(<span class='str'>\"Pass\"</span>);\n} <span class='kw'>else</span> {\n    System.out.println(<span class='str'>\"Fail\"</span>);\n}\n<span class='cm'>// 65 >= 90 is false, 65 >= 60 is true → prints: Pass</span>"),
            d("🧟","enemy","The Bridge Keeper","s-enemy","My rule is this. More than 50 coins: free passage. Ten to fifty: pay a toll. Under ten: turn back. Your code will be tested with three different travellers."),
            d("🧙","mentor","Master Velan","s-mentor","Notice something clever: once you've written <em>else if (coins >= 10)</em>, you've already ruled out anything above 50 — the first if handled that. So the else if only runs for values between 10 and 50. The else catches everything below 10.")
          ),
          "The variable <code>coins</code> is already declared. Write an <strong>if / else if / else</strong> block that prints:<br><br>• <strong>\"You may pass freely.\"</strong> when coins &gt; 50<br>• <strong>\"Pay the toll.\"</strong> when coins is 10–50 inclusive<br>• <strong>\"Turn back.\"</strong> when coins &lt; 10<br><br>Your code will be tested with coins = 35, 75, and 3.",
          "Use <code>if (coins > 50)</code>, then <code>else if (coins >= 10)</code>, then <code>else</code>. Make sure to match the output strings exactly — capitals, spaces, and punctuation.",
          "int coins = 35;\n\n// Write your if / else if / else below:\n",
          "The bridge lowers with a grinding rumble. The Keeper steps aside without a word. \"Logical. Correct. All three travellers judged properly. Pass.\"",
          tests(test("coins=35","int coins = 35;","Pay the toll."),test("coins=75","int coins = 75;","You may pass freely."),test("coins=3","int coins = 3;","Turn back.")));

        q("ch2-q2","The Sorting Sigil","Chapter II · Quest 2","Switch Statements",2,2,120,"SortingSigil.java",
          story(
            n("The Sorting Chamber. Every new student is assessed for elemental affinity and routed to the corresponding house. This happens dozens of times a day, and the sorting mechanism must be fast and readable. Headmistress Aldara has been using a chain of if-else statements, but it's getting unwieldy."),
            d("🎓","npc","Headmistress Aldara","s-npc","Four affinities, four houses. The if-else chain works, but it's twelve lines for four options and grows every time we add a new affinity. There must be a cleaner spell."),
            d("🧙","mentor","Master Velan","s-mentor","There is. A <em>switch statement</em> compares one variable against multiple specific values. It's much cleaner than chaining else-if when you're checking the same variable each time."),
            e("Switch Statement — Structure",
              "<span class='kw'>String</span> day = <span class='str'>\"Monday\"</span>;\n\n<span class='kw'>switch</span> (day) {\n    <span class='kw'>case</span> <span class='str'>\"Monday\"</span>:\n        System.out.println(<span class='str'>\"Start of week\"</span>);\n        <span class='kw'>break</span>;\n    <span class='kw'>case</span> <span class='str'>\"Friday\"</span>:\n        System.out.println(<span class='str'>\"End of week\"</span>);\n        <span class='kw'>break</span>;\n    <span class='kw'>default</span>:\n        System.out.println(<span class='str'>\"Middle of week\"</span>);\n}\n<span class='cm'>// prints: Start of week</span>"),
            d("🧙","mentor","Master Velan","s-mentor","The <em>break</em> keyword is critical — without it, Java 'falls through' into the next case and runs it too, which is almost never what you want. The <em>default</em> block is like else — it runs when nothing matches."),
            d("🧝","npc","Enchantress Lyra","s-npc","One thing worth noting: switch works well with Strings and ints, but not with booleans or floating-point numbers. And it does an exact match — 'fire' and 'Fire' are different cases."),
            n("The sorting mechanism needs four houses mapped to four affinities, plus a fallback for unknown affinities. The Headmistress watches over your shoulder.")
          ),
          "Write a <strong>switch statement</strong> on the variable <code>String affinity</code> that prints:<br><br>• <strong>\"Emberhall\"</strong> for <em>\"Fire\"</em><br>• <strong>\"Tidespire\"</strong> for <em>\"Water\"</em><br>• <strong>\"Stoneward\"</strong> for <em>\"Earth\"</em><br>• <strong>\"Skyveil\"</strong> for <em>\"Air\"</em><br>• <strong>\"General Intake\"</strong> for anything else<br><br>Don't forget <code>break;</code> after each case.",
          "Structure: <code>switch(affinity) { case \"Fire\": System.out.println(\"Emberhall\"); break; ... default: System.out.println(\"General Intake\"); }</code>",
          "// 'affinity' is a String provided by each test (e.g. \"Fire\", \"Water\", ...)\n\nswitch (affinity) {\n    case \"Fire\":\n        // print the house name\n        break;\n    // add the remaining cases and a default\n}\n",
          "The Sorting Sigil pulses five times — once for each test affinity. Aldara watches each result appear and nods. \"Clean. Readable. Exactly right.\"",
          tests(test("Fire","String affinity = \"Fire\";","Emberhall"),test("Water","String affinity = \"Water\";","Tidespire"),test("Earth","String affinity = \"Earth\";","Stoneward"),test("Air","String affinity = \"Air\";","Skyveil"),test("Shadow","String affinity = \"Shadow\";","General Intake")));

        q("ch2-q3","The Clock Tower","Chapter II · Quest 3","While Loops",2,3,120,"ClockTower.java",
          story(
            n("The Academy's Clock Tower rises seven storeys into the sky. Every hour, it strikes a bell — but the enchantment that drives it was written by a careless apprentice three centuries ago and nobody has dared touch it since. Clockmaster Fen needs it rewritten, cleanly, from scratch."),
            d("🕰️","npc","Clockmaster Fen","s-npc","The mechanism is simple. Start at one. Strike. Check if we've reached five. If not, add one and strike again. Keep going until we've struck exactly five times. Then stop."),
            d("🧙","mentor","Master Velan","s-mentor","What Fen describes is a <em>while loop</em>. It repeats a block of code for as long as a condition remains true. The moment the condition becomes false, the loop stops and execution continues after it."),
            e("While Loop — Basic Form",
              "<span class='kw'>int</span> count = <span class='num'>1</span>;\n\n<span class='kw'>while</span> (count <= <span class='num'>3</span>) {\n    System.out.println(<span class='str'>\"Tick \"</span> + count);\n    count++;  <span class='cm'>// MUST update count or loop runs forever</span>\n}\n<span class='cm'>// prints: Tick 1, Tick 2, Tick 3</span>"),
            d("🧙","mentor","Master Velan","s-mentor","The most important rule with while loops: something inside the loop must eventually make the condition false. If nothing changes, the condition stays true forever and the loop never stops — an <em>infinite loop</em>. Your program appears to freeze."),
            e("Common Mistake — Infinite Loop",
              "<span class='cm'>// DANGER: count never changes — runs forever</span>\n<span class='kw'>int</span> count = <span class='num'>1</span>;\n<span class='kw'>while</span> (count <= <span class='num'>5</span>) {\n    System.out.println(count);\n    <span class='cm'>// forgot count++ here!</span>\n}"),
            d("🕰️","npc","Clockmaster Fen","s-npc","After the five strikes, the mechanism should declare itself rested. One final message. Then silence until the next hour."),
            n("Write the loop. Be sure to print each number 1 through 5, then the resting message after the loop closes.")
          ),
          "Use a <strong>while loop</strong> starting with <code>int count = 1;</code>. Print each number from 1 to 5 on its own line. After the loop ends, print: <strong>\"Clock resting.\"</strong>",
          "Use <code>while (count <= 5) { System.out.println(count); count++; }</code> — the <code>count++</code> inside is essential. Print the resting message after the closing brace.",
          "int count = 1;\n\n// Use a while loop to print 1 through 5\n// Then print 'Clock resting.' after the loop\n",
          "The tower strikes five times, each bell tone clear and steady. Then silence. Fen marks his ledger. \"Perfectly cadenced. Not a tick wasted.\"",
          tests(test("Prints 1","null","1"),test("Prints 5","null","5"),test("Clock resting","null","Clock resting.")));

        q("ch2-q4","The Tower of Echoes","Chapter II · Quest 4","For Loops",2,4,120,"ForLoop.java",
          story(
            n("The Tower of Echoes — five floors, each sealed by an ancient curse that demands a chant spoken a specific number of times. The while loop works for any repetition, but when you know the exact count ahead of time, Java offers a more precise instrument."),
            d("🚪","enemy","The Sealed Door","s-enemy","FIVE ECHOES. EXACTLY FIVE. THE CURSE COUNTS EVERY SYLLABLE."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>for loop</em> is purpose-built for counted repetition. It packs three things into one line: where to start, when to stop, and how to step. This makes the intent crystal clear to anyone reading your code."),
            e("For Loop — Structure Explained",
              "<span class='cm'>//   start     stop      step</span>\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>1</span>; i <= <span class='num'>5</span>; i++) {\n    System.out.println(<span class='str'>\"Echo \"</span> + i);\n}\n<span class='cm'>// i starts at 1, runs while i <= 5, adds 1 each time</span>\n<span class='cm'>// prints: Echo 1, Echo 2, Echo 3, Echo 4, Echo 5</span>"),
            d("🧙","mentor","Master Velan","s-mentor","The variable <em>i</em> is available inside the loop and changes each iteration — it's 1 on the first pass, 2 on the second, and so on. This makes for loops ideal when you need to number things, access array indices, or build patterns."),
            e("Comparing While vs For",
              "<span class='cm'>// These two loops do the same thing:</span>\n\n<span class='cm'>// While version</span>\n<span class='kw'>int</span> i = <span class='num'>0</span>;\n<span class='kw'>while</span> (i < <span class='num'>5</span>) {\n    System.out.println(i);\n    i++;\n}\n\n<span class='cm'>// For version (cleaner, preferred)</span>\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < <span class='num'>5</span>; i++) {\n    System.out.println(i);\n}"),
            d("🧙","mentor","Master Velan","s-mentor","Use a while loop when you don't know in advance how many iterations you need. Use a for loop when you do. The Tower demands exactly five echoes — so a for loop is exactly right here."),
            n("After the five echoes, a second seal demands a declaration of completion. Your final print statement goes outside the loop.")
          ),
          "Use a <strong>for loop</strong> to print five numbered lines:<br><strong>Echo 1<br>Echo 2<br>Echo 3<br>Echo 4<br>Echo 5</strong><br><br>Then, after the loop, print: <strong>\"Tower unlocked.\"</strong>",
          "Use <code>for (int i = 1; i <= 5; i++) { System.out.println(\"Echo \" + i); }</code> — then print the unlock message outside the closing brace.",
          "// Use a for loop to print Echo 1 through Echo 5\n// Then print 'Tower unlocked.' after the loop\n",
          "Five echoes ring through the tower. Each floor seal cracks open in sequence. A deep boom as the final door swings free. \"Five perfect echoes,\" the stone walls whisper.",
          tests(test("Echo 1","null","Echo 1"),test("Echo 3","null","Echo 3"),test("Echo 5","null","Echo 5"),test("Tower unlocked","null","Tower unlocked.")));

        q("ch2-q5","The Nested Labyrinth","Chapter II · Quest 5","Nested Loops",2,5,140,"NestedLoops.java",
          story(
            n("The deepest section of the Academy — the Nested Labyrinth. A grid of nine sealed rooms arranged in three rows and three columns. To clear the curse, you must visit every room in order: row by row, column by column. One loop alone cannot do this."),
            d("🧙","mentor","Master Velan","s-mentor","When you need to iterate over a two-dimensional structure — rows and columns, a grid, a table — you need a <em>nested loop</em>: a loop inside another loop. The outer loop handles one dimension, the inner loop handles the other."),
            e("Nested Loops — Grid Pattern",
              "<span class='kw'>for</span> (<span class='type'>int</span> row = <span class='num'>1</span>; row <= <span class='num'>3</span>; row++) {\n    <span class='kw'>for</span> (<span class='type'>int</span> col = <span class='num'>1</span>; col <= <span class='num'>3</span>; col++) {\n        System.out.println(row + <span class='str'>\"-\"</span> + col);\n    }\n}\n<span class='cm'>// Prints: 1-1, 1-2, 1-3, then 2-1, 2-2, 2-3, then 3-1...</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Think of it as: for each row, visit every column. The outer loop runs 3 times. Each time it runs, the inner loop runs 3 times completely. That gives 9 total inner executions — exactly enough for a 3x3 grid."),
            e("How Nesting Works — Step by Step",
              "<span class='cm'>// Outer: row=1</span>\n<span class='cm'>//   Inner: col=1 → prints 1-1</span>\n<span class='cm'>//   Inner: col=2 → prints 1-2</span>\n<span class='cm'>//   Inner: col=3 → prints 1-3</span>\n<span class='cm'>// Outer: row=2</span>\n<span class='cm'>//   Inner: col=1 → prints 2-1</span>\n<span class='cm'>//   ... and so on until 3-3</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Always use different variable names for each loop — <em>i</em> and <em>j</em>, or <em>row</em> and <em>col</em> as here. Using the same name would shadow the outer variable and cause confusion."),
            n("Nine rooms. Three rows. Three columns. Label each one correctly and the labyrinth yields.")
          ),
          "Use <strong>nested for loops</strong> to print all 9 room labels:<br><strong>Room 1-1<br>Room 1-2<br>Room 1-3<br>Room 2-1<br>...</strong><br>All rows 1–3, columns 1–3 in order.",
          "Outer loop: <code>for (int i = 1; i <= 3; i++)</code> — inner loop: <code>for (int j = 1; j <= 3; j++) { System.out.println(\"Room \" + i + \"-\" + j); }</code>",
          "// Use nested for loops\n// Outer loop for rows 1-3, inner loop for columns 1-3\n\n",
          "All nine seals break in rapid sequence, light flooding every room simultaneously. The Labyrinth Keeper bows: \"No room unvisited. Precise and methodical.\"",
          tests(test("Room 1-1","null","Room 1-1"),test("Room 2-3","null","Room 2-3"),test("Room 3-3","null","Room 3-3")));

        q("ch2-q6","The Sentinel Loop","Chapter II · Quest 6","Do-While Loop",2,6,140,"SentinelLoop.java",
          story(
            n("The Sentinel Gate. It swings open before you can even read the inscription. Some loops must execute at least once — the check comes after the action. This is the do-while loop."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>while</em> loop checks its condition before running. If the condition is false at the start, the loop body never executes. A <em>do-while</em> loop runs the body first, then checks — guaranteeing at least one execution."),
            e("Worked Example — do-while",
              "<span class='kw'>int</span> count = <span class='num'>1</span>;\n<span class='kw'>do</span> {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Count: \"</span> + count);\n    count++;\n} <span class='kw'>while</span> (count <= <span class='num'>3</span>);\n<span class='cm'>// Count: 1</span>\n<span class='cm'>// Count: 2</span>\n<span class='cm'>// Count: 3</span>"),
            d("🚪","npc","Gate Keeper Thorm","s-npc","Notice the semicolon after the while condition. Easy to forget. The loop body is always in curly braces before the while. Think of it as: do { action } while (condition);"),
            e("Do-while vs while — key difference",
              "<span class='cm'>// This while loop never runs at all:</span>\n<span class='type'>int</span> x = <span class='num'>10</span>;\n<span class='kw'>while</span> (x < <span class='num'>5</span>) { System.out.println(x); }\n\n<span class='cm'>// This do-while runs once:</span>\n<span class='kw'>do</span> { System.out.println(x); } <span class='kw'>while</span> (x < <span class='num'>5</span>);\n<span class='cm'>// Output: 10</span>")
          ),
          "Use a <code>do-while</code> loop to print the numbers <strong>1 through 5</strong>, one per line.",
          "Start with int i = 1; do { print i; i++; } while (i <= 5); — don't forget the semicolon after the while condition.",
          "// Use a do-while loop to print 1 through 5\n\n",
          "The gate swings open. Thorm nods. \"You understand the difference. That will matter.\"",
          tests(test("Prints 1","null","1"),test("Prints 3","null","3"),test("Prints 5","null","5")));

        q("ch2-q7","The Shepherd's Pass","Chapter II · Quest 7","Enhanced For Loop",2,7,140,"ShepherdsPass.java",
          story(
            n("The Shepherd's Pass. A flock moves through the gate one by one. You don't count them by index — you just work with each one in turn. Java's enhanced for loop does exactly this."),
            d("🧙","mentor","Master Velan","s-mentor","Before we loop, we must understand what we loop <em>over</em>. An <em>array</em> is a fixed-size, ordered container of values of the same type. You declare one with square brackets after the type — <em>int[]</em> — and fill it with a comma-separated list in curly braces. Think of it as a numbered shelf: slot 0, slot 1, slot 2, and so on."),
            e("Array — a quick preview","int[] scores = {85, 92, 78, 95, 88};\n\nscores.length  →  5        // how many elements\nscores[0]      →  85       // first (index starts at 0)\nscores[4]      →  88       // last element"),
            d("🧝","npc","Enchantress Lyra","s-npc","Arrays will get their own full chapter later. For now, just know that you can store a series of values in one named variable. The enhanced for loop is the cleanest way to visit each one in order."),
            d("🧙","mentor","Master Velan","s-mentor","The <em>enhanced for loop</em> — also called the <em>for-each loop</em> — iterates over every element in an array or collection without needing an index. Syntax: <em>for (Type item : collection)</em>. Read it as: for each item in collection."),
            e("Worked Example — Enhanced For",
              "<span class='type'>String</span>[] spells = {<span class='str'>\"Fireball\"</span>, <span class='str'>\"Freeze\"</span>, <span class='str'>\"Heal\"</span>};\n\n<span class='cm'>// Index-based (old way)</span>\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < spells.length; i++) {\n    System.out.println(spells[i]);\n}\n\n<span class='cm'>// Enhanced for (cleaner)</span>\n<span class='kw'>for</span> (<span class='type'>String</span> spell : spells) {\n    System.out.println(spell);\n}"),
            d("🧝","npc","Enchantress Lyra","s-npc","Use the enhanced for when you only need the value, not the index. If you need to modify elements or track position, use the indexed for loop. For reading and processing, the enhanced for is cleaner and harder to get wrong — no off-by-one errors.")
          ),
          "Given <code>int[] scores = {85, 92, 78, 95, 88};</code>, use an enhanced for loop to print each score and calculate the total. Print each score on its own line, then print: <strong>Total: 438</strong>",
          "for (int score : scores) { System.out.println(score); total += score; } then print total.",
          "int[] scores = {85, 92, 78, 95, 88};\nint total = 0;\n\n// Use enhanced for loop to print each score and add to total\n\n// Print the total\n",
          "Each sheep counted. \"Total: 438. The flock is complete.\"",
          tests(test("First score","null","85"),test("Last score","null","88"),test("Total","null","Total: 438")));

        // ── Side quests ───────────────────────────────────────────────────────

        sq("ch2-sq1","The Logic Gates","Chapter II · Side Quest","Boolean Algebra",2,90,70,"LogicGates.java",
          story(
            n("The Chamber of Logical Mechanisms. Brass gates line the walls, each demonstrating a fundamental law of boolean logic. A sign reads: <em>Master these and no condition will ever surprise you.</em>"),
            d("🦉","npc","Sage Orrin","s-npc","Three fundamental operators: <em>&amp;&amp;</em> (AND — both must be true), <em>||</em> (OR — at least one must be true), <em>!</em> (NOT — inverts). All logic is built from these three."),
            e("Truth Table","a = true,  b = false:\na && b  →  false  (AND requires both)\na || b  →  true   (OR needs one)\n!a      →  false  (NOT inverts)\n!b      →  true"),
            d("🦉","npc","Sage Orrin","s-npc","<em>De Morgan's Theorem</em>: NOT (A AND B) equals (NOT A) OR (NOT B). And: NOT (A OR B) equals (NOT A) AND (NOT B). This lets you distribute a NOT across a compound condition and simplify complex logic."),
            e("De Morgan's Laws","// These pairs are always equivalent:\n!(a && b)  ==  (!a || !b)\n!(a || b)  ==  (!a && !b)"),
            d("🧙","mentor","Master Velan","s-mentor","Bonus: <em>short-circuit evaluation</em>. With <em>&amp;&amp;</em>, if the left side is false, Java never evaluates the right. This is why <em>list != null &amp;&amp; list.size() > 0</em> is safe — if list is null the size check never executes.")
          ),
          "With <code>boolean a = true, b = false</code>, print four labeled lines:<br>1. <code>\"AND: \" + (a && b)</code><br>2. <code>\"OR: \" + (a || b)</code><br>3. <code>\"De Morgan 1: \" + (!(a && b) == (!a || !b))</code><br>4. <code>\"De Morgan 2: \" + (!(a || b) == (!a && !b))</code>",
          "De Morgan 1: !(true&&false)=true, (!true||!false)=true — both true, so equal=true. De Morgan 2: !(true||false)=false, (!true&&!false)=false — both false, so equal=true.",
          "boolean a = true, b = false;\n\n// 1. AND\n// 2. OR\n// 3. De Morgan's first law\n// 4. De Morgan's second law\n",
          "The four gates click open. Sage Orrin marks a tick. \"De Morgan's laws hold. As they always will.\"",
          tests(test("AND","null","AND: false"),test("OR","null","OR: true"),test("De Morgan 1","null","De Morgan 1: true"),test("De Morgan 2","null","De Morgan 2: true")));
    }
}
