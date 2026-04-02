package com.arcane.academy.config;

import com.arcane.academy.model.Quest;
import com.arcane.academy.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChapterTwoSeeder {

    private final QuestRepository questRepository;

    void seed() {

        q("ch2-q1","The Oracle's Fork","Chapter II · Quest 1","If / Else",2,1,120,"OraclesFork.java",
          story(
            n("The Bridge of Aethon stretches across a chasm so deep that mist swallows everything below the first ten metres. You and Master Velan stand at the near end. A hooded figure occupies the centre — the Bridge Keeper, who has judged travellers here for three hundred years."),
            d("🧙","mentor","Master Velan","s-mentor","So far, every program you've written has run top to bottom — one line after another, no choices. Real programs need to make decisions. 'If this is true, do this. Otherwise, do that.' That is an <em>if statement</em>."),
            d("🧙","mentor","Master Velan","s-mentor","The structure is: <em>if (condition) { code to run when true }</em>. The condition is any boolean expression — something that evaluates to true or false. If it's true, the code inside the curly braces runs. If it's false, Java skips that block entirely."),
            e("Worked Example — Simple If",
              "<span class='type'>int</span> mana = <span class='num'>80</span>;\n\n<span class='kw'>if</span> (mana > <span class='num'>50</span>) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Enough mana to cast!\"</span>);\n}\n<span class='cm'>// prints: Enough mana to cast!</span>\n\n<span class='cm'>// But if mana were 20, nothing would print.</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Sometimes you want something to happen in BOTH cases — when the condition is true AND when it's false. Add an <em>else</em> block after. Only one of the two blocks will ever run."),
            e("Worked Example — If / Else",
              "<span class='type'>int</span> mana = <span class='num'>20</span>;\n\n<span class='kw'>if</span> (mana > <span class='num'>50</span>) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Cast the spell!\"</span>);\n} <span class='kw'>else</span> {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Rest and recover.\"</span>);\n}\n<span class='cm'>// prints: Rest and recover.</span>"),
            d("🧙","mentor","Master Velan","s-mentor","And for three or more branches, chain <em>else if</em> between them. Java checks each condition in order — the first one that's true wins. The rest are skipped entirely. Always put the most specific condition first."),
            e("Worked Example — If / Else If / Else",
              "<span class='type'>int</span> coins = <span class='num'>35</span>;\n\n<span class='kw'>if</span> (coins > <span class='num'>50</span>) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Free passage.\"</span>);\n} <span class='kw'>else if</span> (coins >= <span class='num'>10</span>) {\n    <span class='cm'>// Only reaches here if coins is NOT > 50</span>\n    <span class='cm'>// So this catches 10 to 50</span>\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Pay the toll.\"</span>);\n} <span class='kw'>else</span> {\n    <span class='cm'>// Everything below 10</span>\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Turn back.\"</span>);\n}\n<span class='cm'>// prints: Pay the toll.</span>"),
            d("🧟","enemy","The Bridge Keeper","s-enemy","The rule is simple. More than 50 coins — pass freely. Between 10 and 50 — pay the toll. Fewer than 10 — turn back. I will test three different travellers. Your logic must handle all of them."),
            n("Master Velan steps back. 'The variable coins is declared for you. Write the three-branch decision that the Keeper demands.'")
          ),
          "The variable <code>coins</code> is already declared. Write an <strong>if / else if / else</strong> block that prints:<br><br>• <strong>\"You may pass freely.\"</strong> — when coins &gt; 50<br>• <strong>\"Pay the toll.\"</strong> — when coins is 10–50 (inclusive)<br>• <strong>\"Turn back.\"</strong> — when coins &lt; 10<br><br>Your code will be tested with coins = 35, 75, and 3.",
          "Use <code>if (coins > 50)</code>, then <code>else if (coins >= 10)</code> — since &gt;50 is already excluded this catches 10–50. Then <code>else</code> catches everything below 10.",
          "int coins = 35;\n\n// Write your if / else if / else below:\n",
          "The bridge lowers with a grinding rumble. The Keeper steps aside, visibly reluctant. \"Logical. Correct. Pass.\"",
          tests(test("coins=35","int coins = 35;","Pay the toll."),test("coins=75","int coins = 75;","You may pass freely."),test("coins=3","int coins = 3;","Turn back.")));

        q("ch2-q2","The Sorting Sigil","Chapter II · Quest 2","Switch Statements",2,2,120,"SortingSigil.java",
          story(
            n("The Sorting Chamber is a tall hexagonal room with four glowing archways, each leading to a different wizard house. Every new student stands on the central platform and speaks their elemental affinity. The Sorting Sigil judges them."),
            d("🎓","npc","Headmistress Aldara","s-npc","We have four houses. Fire wizards to Emberhall, Water to Tidespire, Earth to Stoneward, Air to Skyveil. And every year, a handful of students with unusual affinities — Shadow, Void, Time — who go to General Intake until we assess them properly."),
            d("🧙","mentor","Master Velan","s-mentor","You could write this with a long chain of if / else if / else if... but that gets unwieldy when you're checking one variable against many specific values. Java has a cleaner tool for this: the <em>switch statement</em>."),
            d("🧙","mentor","Master Velan","s-mentor","A switch takes one variable and compares it against a list of cases. When a match is found, that case's code runs. The <em>break</em> at the end of each case stops execution falling into the next case. The <em>default</em> block handles anything that didn't match."),
            e("Worked Example — Switch",
              "<span class='type'>String</span> day = <span class='str'>\"Monday\"</span>;\n\n<span class='kw'>switch</span> (day) {\n    <span class='kw'>case</span> <span class='str'>\"Monday\"</span>:\n        <span class='kw'>System</span>.out.println(<span class='str'>\"Start of the week\"</span>);\n        <span class='kw'>break</span>;  <span class='cm'>// IMPORTANT: stops here</span>\n    <span class='kw'>case</span> <span class='str'>\"Friday\"</span>:\n        <span class='kw'>System</span>.out.println(<span class='str'>\"End of the week\"</span>);\n        <span class='kw'>break</span>;\n    <span class='kw'>default</span>:\n        <span class='kw'>System</span>.out.println(<span class='str'>\"Middle of the week\"</span>);\n}\n<span class='cm'>// prints: Start of the week</span>"),
            d("🧙","mentor","Master Velan","s-mentor","What happens if you forget the <em>break</em>? Java 'falls through' into the next case and runs that code too — even though it didn't match. This is almost always a bug. Always include break unless you're deliberately combining cases."),
            d("🎓","npc","Headmistress Aldara","s-npc","The Sigil will be tested with all five affinities: Fire, Water, Earth, Air, and Shadow. Write the switch that routes each one to the correct house.")
          ),
          "The variable <code>String affinity</code> is declared. Write a <strong>switch</strong> that prints:<br>• <strong>\"Emberhall\"</strong> for <em>\"Fire\"</em><br>• <strong>\"Tidespire\"</strong> for <em>\"Water\"</em><br>• <strong>\"Stoneward\"</strong> for <em>\"Earth\"</em><br>• <strong>\"Skyveil\"</strong> for <em>\"Air\"</em><br>• <strong>\"General Intake\"</strong> for anything else",
          "Use <code>switch(affinity) { case \"Fire\": System.out.println(\"Emberhall\"); break; ... default: System.out.println(\"General Intake\"); }</code>",
          "String affinity = \"Fire\";\n\n// Write your switch statement below:\n",
          "The Sigil pulses to the correct colour for all five affinities. Headmistress Aldara closes the ledger with a click. \"Every student sorted correctly.\"",
          tests(test("Fire","String affinity = \"Fire\";","Emberhall"),test("Water","String affinity = \"Water\";","Tidespire"),test("Earth","String affinity = \"Earth\";","Stoneward"),test("Air","String affinity = \"Air\";","Skyveil"),test("Shadow","String affinity = \"Shadow\";","General Intake")));

        q("ch2-q3","The Clock Tower","Chapter II · Quest 3","While Loops",2,3,120,"ClockTower.java",
          story(
            n("The Academy's clock tower rises seven storeys above the courtyard. Clockmaster Fen lives at the top and hasn't come down in eleven years. He communicates by lowering messages on a rope. Today's message reads: 'The clock is broken. It needs to count from 1 to 5 and then rest. Please send a programmer.'"),
            d("🧙","mentor","Master Velan","s-mentor","So far every program has run a fixed number of lines and stopped. But what if you need something to repeat? Not a fixed number of times — just keep going while a condition is true? That's a <em>while loop</em>."),
            d("🧙","mentor","Master Velan","s-mentor","Structure: <em>while (condition) { code block }</em>. Before each iteration, Java checks the condition. If it's true, the block runs. If it's false, the loop ends and execution continues after it. If the condition is never false — infinite loop. The program freezes."),
            e("Worked Example — While Loop",
              "<span class='type'>int</span> count = <span class='num'>1</span>;\n\n<span class='kw'>while</span> (count <= <span class='num'>3</span>) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Count: \"</span> + count);\n    count++;  <span class='cm'>// ← CRUCIAL: changes count each iteration</span>\n}\n<span class='cm'>// prints: Count: 1, Count: 2, Count: 3</span>\n<span class='cm'>// Loop stops because count becomes 4, which is not <= 3</span>"),
            d("🕰️","npc","Clockmaster Fen","s-npc","The most common mistake: forgetting to change the variable that controls the condition. If count never increments, it's always 1, the condition is always true, and the clock strikes forever. I've seen it happen. Very loud."),
            d("🧙","mentor","Master Velan","s-mentor","The pattern is always the same: declare a counter before the loop, check it in the condition, change it inside the loop. The counter controls how many times the loop runs."),
            n("A rope descends with a second note: 'The clock should print 1 through 5, then print Clock resting. so we know it has stopped.'")
          ),
          "Using <code>int count = 1;</code> already declared, write a <strong>while loop</strong> that prints 1 through 5, one per line. After the loop finishes, print: <strong>\"Clock resting.\"</strong>",
          "Use <code>while (count <= 5) { System.out.println(count); count++; }</code> — the <code>count++</code> inside is essential or the loop runs forever.",
          "int count = 1;\n\n// Use a while loop to print 1 through 5\n// Then print 'Clock resting.' after the loop ends\n",
          "The clock strikes five clear tones and falls silent. Fen lowers another note: 'Perfect. Exactly five. No more, no less. You may go.'",
          tests(test("Prints 1","null","1"),test("Prints 5","null","5"),test("Clock resting","null","Clock resting.")));

        q("ch2-q4","The Tower of Echoes","Chapter II · Quest 4","For Loops",2,4,120,"ForLoop.java",
          story(
            n("The Tower of Echoes sits at the Academy's east end — five floors, each sealed by a rune that demands a chant spoken a precise number of times. Not approximately. Exactly. The runes count."),
            d("🧙","mentor","Master Velan","s-mentor","The while loop is powerful when you don't know in advance how many times to repeat. But when you DO know — 'I want to do this exactly 5 times' — Java has a tighter tool: the <em>for loop</em>. It packages the counter setup, the condition, and the update all in one line."),
            e("Worked Example — For Loop Anatomy",
              "<span class='cm'>//  ┌── Step 1: run once before loop starts</span>\n<span class='cm'>//  │         ┌── Step 2: checked before EACH iteration</span>\n<span class='cm'>//  │         │          ┌── Step 3: runs AFTER each iteration</span>\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < <span class='num'>5</span>; i++) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Iteration: \"</span> + i);\n}\n<span class='cm'>// i goes: 0, 1, 2, 3, 4  →  5 iterations</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Notice: starting at 0 and using <em>i &lt; 5</em> gives you exactly 5 iterations — i is 0, 1, 2, 3, 4. If you start at 1 and use <em>i &lt;= 5</em>, same result. The second style is sometimes more readable when you need the numbers to be 1 through 5."),
            e("Worked Example — Starting at 1",
              "<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>1</span>; i <= <span class='num'>5</span>; i++) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Echo \"</span> + i);\n}\n<span class='cm'>// prints: Echo 1, Echo 2, Echo 3, Echo 4, Echo 5</span>"),
            d("🚪","enemy","The Sealed Door","s-enemy","FIVE ECHOES. THE RUNE WILL COUNT. SPEAK THE WORD 'APERIO' FIVE TIMES — NUMBERED — THEN DECLARE THE TOTAL. DEVIATION IN EITHER DIRECTION WILL BE NOTED."),
            n("The rune glows an impatient amber. The Tower has been waiting long enough.")
          ),
          "Use a <strong>for loop</strong> to print exactly 5 numbered echo lines:<br><strong>Echo 1<br>Echo 2<br>Echo 3<br>Echo 4<br>Echo 5</strong><br><br>Then print: <strong>\"Tower unlocked.\"</strong>",
          "Use <code>for (int i = 1; i <= 5; i++) { System.out.println(\"Echo \" + i); }</code> — then after the closing brace, print the final line.",
          "// Use a for loop to print Echo 1 through Echo 5\n// Then print 'Tower unlocked.' after the loop\n",
          "Five seals break in sequence, each with a satisfying crack. The Tower door swings open releasing cool archival air. Five perfect echoes. Not four. Not six.",
          tests(test("Echo 1","null","Echo 1"),test("Echo 5","null","Echo 5"),test("Tower unlocked","null","Tower unlocked.")));

        q("ch2-q5","The Nested Labyrinth","Chapter II · Quest 5","Nested Loops",2,5,140,"NestedLoops.java",
          story(
            n("The Academy's most disorienting space: a 3×3 grid of identical rooms connected by identical corridors. New students get lost here for hours. The Academy uses it to teach one important lesson: systematic exploration. Visit every row. Within each row, visit every column. Miss nothing."),
            d("🧙","mentor","Master Velan","s-mentor","What if you need a loop inside a loop? The outer loop handles rows. For every single iteration of the outer loop, the inner loop runs completely from start to finish. Then the outer loop moves to the next iteration, and the inner loop starts over."),
            e("Worked Example — Nested Loops",
              "<span class='cm'>// Outer loop: rows 1 to 3</span>\n<span class='kw'>for</span> (<span class='type'>int</span> row = <span class='num'>1</span>; row <= <span class='num'>3</span>; row++) {\n\n    <span class='cm'>// Inner loop: cols 1 to 3 — runs 3 times per row</span>\n    <span class='kw'>for</span> (<span class='type'>int</span> col = <span class='num'>1</span>; col <= <span class='num'>3</span>; col++) {\n        <span class='kw'>System</span>.out.println(row + <span class='str'>\"-\"</span> + col);\n    }\n}\n<span class='cm'>// prints: 1-1, 1-2, 1-3, 2-1, 2-2, 2-3, 3-1, 3-2, 3-3</span>\n<span class='cm'>// 3 outer iterations × 3 inner = 9 total prints</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Use different variable names for each loop — <em>i</em> and <em>j</em> by convention, or more descriptive names like <em>row</em> and <em>col</em>. If you use the same name for both, the inner loop overwrites the outer counter and the logic breaks."),
            d("🗺️","npc","Maze Keeper Torrin","s-npc","Map every room. The grid is 3 rows by 3 columns. Each room is identified as Row-Column. Start from Room 1-1, end at Room 3-3. Miss one room and the labyrinth resets."),
            n("Nine rooms. Each one must be announced in order. The Keeper watches from above, checking each one off a list.")
          ),
          "Use <strong>nested for loops</strong> to print all 9 room coordinates, one per line:<br><strong>Room 1-1<br>Room 1-2<br>Room 1-3<br>Room 2-1<br>...</strong> through to <strong>Room 3-3</strong>",
          "Outer loop rows 1–3, inner loop cols 1–3: <code>System.out.println(\"Room \" + row + \"-\" + col);</code>",
          "// Use nested for loops to print Room 1-1 through Room 3-3\n// Outer loop = rows, inner loop = columns\n",
          "Nine checkmarks on Torrin's list. All nine rooms announced. 'Systematic. Thorough. The labyrinth is satisfied.' The exit seal breaks.",
          tests(test("Room 1-1","null","Room 1-1"),test("Room 2-3","null","Room 2-3"),test("Room 3-3","null","Room 3-3")));
    }

    // ── helpers ──────────────────────────────────────────────────────────────
    private void q(String id, String title, String eyebrow, String topic,
                   int chapter, int order, int xp, String file,
                   String story, String problem, String hint, String starter, String win, String tests) {
        questRepository.save(Quest.builder()
            .id(id).title(title).eyebrow(eyebrow).topic(topic)
            .chapterNumber(chapter).orderInChapter(order).xpReward(xp).filename(file)
            .storyJson(story).problemHtml(problem).hint(hint)
            .starterCode(starter).winStory(win).testCasesJson(tests).build());
    }
    private String story(String... beats) { return "[" + String.join(",", beats) + "]"; }
    private String n(String t) { return "{\"type\":\"narration\",\"text\":\"" + esc(t) + "\"}"; }
    private String d(String av, String cls, String sp, String sCls, String t) {
        return "{\"type\":\"dialogue\",\"av\":\""+av+"\",\"cls\":\""+cls+"\",\"speaker\":\""+sp+"\",\"sCls\":\""+sCls+"\",\"text\":\""+esc(t)+"\"}";
    }
    private String e(String label, String code) {
        return "{\"type\":\"example\",\"speaker\":\""+esc(label)+"\",\"text\":\""+esc(code)+"\"}";
    }
    private String tests(String... ts) { return "[" + String.join(",", ts) + "]"; }
    private String test(String label, String input, String expected) {
        return "{\"label\":\""+label+"\",\"input\":"+("null".equals(input)?"null":"\""+esc(input)+"\"") +",\"expected\":\""+esc(expected)+"\"}";
    }
    private String esc(String s) {
        return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n").replace("\t","\\t");
    }
}
