// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.arcane.academy.config;

import com.arcane.academy.model.Boss;
import com.arcane.academy.model.Quest;
import com.arcane.academy.repository.BossRepository;
import com.arcane.academy.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final QuestRepository questRepository;
    private final BossRepository bossRepository;

    @Bean
    public ApplicationRunner seedData() {
        return args -> {
            if (questRepository.count() == 0) seedQuests();
            if (bossRepository.count() == 0) seedBosses();
        };
    }

    // ─────────────────────────────────────────────────────────────────────────
    // QUESTS
    // ─────────────────────────────────────────────────────────────────────────
    private void seedQuests() {
        log.info("Seeding quests...");

        // ══════════════════════════════════════════════════
        // CHAPTER I — THE FIRST RUNE (Variables & Types)
        // ══════════════════════════════════════════════════

        q("ch1-q1","Your First Spell","Chapter I · Quest 1","Hello World",1,1,80,"HelloWorld.java",
          story(
            n("You stand before the Academy's most ancient wall — the Wall of First Words. Every wizard who ever graduated left their mark here with the simplest of spells: making the world say something back."),
            d("🧙","mentor","Master Velan","s-mentor","Before anything else, an apprentice must learn to speak. In Java, you make the program speak with one command: <em>System.out.println(\"your words here\");</em> — the program will print exactly what you put in the quotes."),
            d("🧝","npc","Enchantress Lyra","s-npc","Think of it as writing a letter that reads itself aloud. The words in quotes are your message. The Academy records every word ever spoken this way."),
            n("Master Velan points to the starter code. A shell of a program is already written. You just need to add the spell inside it.")
          ),
          "Inside the <code>main</code> method, add one line that prints: <strong>\"Welcome to Arcane Academy!\"</strong>",
          "Add this line inside the curly braces: <code>System.out.println(\"Welcome to Arcane Academy!\");</code>",
          "public class HelloWorld {\n    public static void main(String[] args) {\n        // Add your spell here\n        \n    }\n}\n",
          "The words appear on the Wall of First Words in glowing gold. Master Velan smiles. \"Your first spell. The Academy hears you.\"",
          tests(test("Output","null","Welcome to Arcane Academy!")));

        q("ch1-q2","The Naming Ceremony","Chapter I · Quest 2","Variables",1,2,100,"NamingCeremony.java",
          story(
            n("The Hall of First Bindings. Shelves of glowing jars stretch floor to ceiling, each labelled with a rune and filled with coloured light. These are variables — containers that hold values and wait to be used."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>variable</em> is a named container. To create one, write the type first, then the name, then = and the value, then a semicolon. Like this: <em>int level = 5;</em> — that creates an integer container called 'level' holding the value 5."),
            d("🧙","mentor","Master Velan","s-mentor","Four types you'll use constantly: <em>int</em> for whole numbers like 5 or -3, <em>double</em> for decimals like 3.14, <em>boolean</em> for true or false, and <em>String</em> for text in double quotes like \"Aldric\"."),
            d("🧟","npc","Caretaker Moss","s-npc","We track every wizard's stats in variables. Name, level, mana, cursed status. Four empty jars here. Fill them correctly and the registry activates."),
            n("You look at the four jars. The label on each says what type of essence it needs.")
          ),
          "Declare four variables then print each on its own line:<br><br>• <code>String</code> <strong>wizardName</strong> = <strong>\"Aldric\"</strong><br>• <code>int</code> <strong>level</strong> = <strong>1</strong><br>• <code>double</code> <strong>mana</strong> = <strong>100.0</strong><br>• <code>boolean</code> <strong>cursed</strong> = <strong>false</strong>",
          "Declare: <code>String wizardName = \"Aldric\";</code> then print: <code>System.out.println(wizardName);</code> — repeat for each.",
          "// Fill the Academy registry — declare four variables and print each\n\n",
          "All four jars seal with a soft click. Caretaker Moss mutters approvingly: \"First wizard in years to get the types right first try.\"",
          tests(test("wizardName","null","Aldric"),test("level","null","1"),test("mana","null","100.0"),test("cursed","null","false")));

        q("ch1-q3","The Cauldron of Computation","Chapter I · Quest 3","Arithmetic",1,3,100,"Cauldron.java",
          story(
            n("The Academy basement. The Cauldron of Computation bubbles and hisses — a device that transforms numbers through arithmetic. Potions here are brewed by calculation. One wrong operator and the batch explodes."),
            d("🧙","mentor","Master Velan","s-mentor","Variables aren't fixed. You can change them. After declaring <em>int x = 10;</em>, you can write <em>x = x + 5;</em> to update it to 15. Or use the shortcut <em>x += 5;</em> — they mean the same thing."),
            d("🧪","npc","Brewmaster Zyn","s-npc","The four operations: <em>+</em> adds, <em>-</em> subtracts, <em>*</em> multiplies, <em>/</em> divides. With integers, division cuts off the decimal — 7 / 2 gives 3, not 3.5. Use double for decimals."),
            d("🧙","mentor","Master Velan","s-mentor","Shortcut assignments: <em>x += 5</em> adds 5 to x. <em>x -= 3</em> subtracts 3. <em>x *= 2</em> doubles it. <em>x /= 4</em> divides. And <em>x++</em> adds exactly 1 — called an increment."),
            n("Three calculations must be applied to the cauldron's base strength, in order. The Brewmaster watches closely.")
          ),
          "Start with <code>int potionStrength = 10;</code> already declared.<br><br>1. Add <strong>5</strong> using <code>+=</code><br>2. Multiply by <strong>2</strong> using <code>*=</code><br>3. Subtract <strong>4</strong> using <code>-=</code><br><br>Print the final value. <em>Expected: 26</em>",
          "10 + 5 = 15, × 2 = 30, − 4 = 26. Write each operation on its own line.",
          "int potionStrength = 10;\n\n// Step 1: add 5\n// Step 2: multiply by 2\n// Step 3: subtract 4\n// Print the result\n",
          "The cauldron glows amber. \"Exactly 26,\" Zyn whispers reverently. \"The Potion of Fortitude. I haven't seen one brewed correctly in a decade.\"",
          tests(test("potionStrength = 26","null","26")));

        q("ch1-q4","The String Scriptorium","Chapter I · Quest 4","Strings",1,4,100,"Scriptorium.java",
          story(
            n("The Scriptorium is where wizards learn to work with text. Strings are the most human of all types — they carry names, messages, incantations, and stories. But they have their own rules."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>String</em> is a sequence of characters wrapped in double quotes. You can join two Strings using <em>+</em> — this is called <em>concatenation</em>. So <em>\"Hello, \" + \"Aldric\"</em> produces <em>\"Hello, Aldric\"</em>."),
            d("🧙","mentor","Master Velan","s-mentor","You can also mix Strings and other types: <em>\"Level: \" + 5</em> gives <em>\"Level: 5\"</em>. Java converts the number to a String automatically when combined this way."),
            d("📚","npc","Librarian Fen","s-npc","Strings have built-in powers too. <em>name.length()</em> tells you how many characters are in it. <em>name.toUpperCase()</em> shouts it. <em>name.toLowerCase()</em> whispers it. <em>name.charAt(0)</em> picks the first character."),
            n("A message must be assembled from separate parts and the librarian needs the name analysed.")
          ),
          "Declare <code>String firstName = \"Aria\";</code> and <code>String lastName = \"Voss\";</code><br><br>Then print three things:<br>1. The full name joined: <strong>\"Aria Voss\"</strong><br>2. The total character count of the full name (including space): <strong>9</strong><br>3. The full name in uppercase: <strong>\"ARIA VOSS\"</strong>",
          "Join with: <code>String full = firstName + \" \" + lastName;</code> then use <code>full.length()</code> and <code>full.toUpperCase()</code>.",
          "String firstName = \"Aria\";\nString lastName = \"Voss\";\n\n// Print the full name, its length, and its uppercase version\n",
          "The Scriptorium quill writes all three answers in flowing gold ink. Librarian Fen nods: \"Text mastery. The foundation of all readable spells.\"",
          tests(test("Full name","null","Aria Voss"),test("Length","null","9"),test("Uppercase","null","ARIA VOSS")));

        q("ch1-q5","The Casting Type","Chapter I · Quest 5","Type Casting",1,5,120,"TypeCasting.java",
          story(
            n("The Transformation Chamber — where wizards learn that types, though fixed at declaration, can sometimes be converted. An int can become a double. A double can be forced back into an int, but carefully."),
            d("🧙","mentor","Master Velan","s-mentor","<em>Widening</em> is safe: a smaller type fits into a larger one automatically. <em>int</em> becomes <em>double</em> with no effort: <em>double d = 5;</em> works fine — Java silently converts 5 to 5.0."),
            d("🧙","mentor","Master Velan","s-mentor","<em>Narrowing</em> is risky: forcing a larger type into a smaller one loses data. To do it deliberately, use a <em>cast</em>: write the target type in parentheses before the value. <em>(int) 3.9</em> gives <em>3</em> — the decimal is cut off, not rounded."),
            d("⚗️","npc","Alchemist Prue","s-npc","We also use casting to fix integer division. <em>7 / 2</em> gives 3. But <em>(double) 7 / 2</em> gives 3.5 — by casting 7 to double first, the whole division becomes decimal."),
            n("Three conversions must be demonstrated for Alchemist Prue's records.")
          ),
          "Perform three type operations and print each result:<br><br>1. Declare <code>int whole = 7;</code> — print it as a double by casting: <strong>7.0</strong><br>2. Declare <code>double decimal = 9.99;</code> — cast to int and print: <strong>9</strong><br>3. Compute <code>(double) 7 / 2</code> and print: <strong>3.5</strong>",
          "Cast with: <code>(double) whole</code> and <code>(int) decimal</code>. For division: <code>System.out.println((double) 7 / 2);</code>",
          "int whole = 7;\ndouble decimal = 9.99;\n\n// 1. Print whole as a double\n// 2. Print decimal as an int\n// 3. Print 7 divided by 2 as a decimal\n",
          "The transformation crystals glow in sequence. Prue records each reading. \"Clean conversions. You understand the cost of narrowing.\"",
          tests(test("Cast to double","null","7.0"),test("Cast to int","null","9"),test("Decimal division","null","3.5")));

        // ══════════════════════════════════════════════════
        // CHAPTER II — THE CONTROL TOME
        // ══════════════════════════════════════════════════

        q("ch2-q1","The Truth Crystals","Chapter II · Quest 1","Booleans & Comparisons",2,1,100,"TruthCrystals.java",
          story(
            n("The Chamber of Truth holds crystals that glow only when a statement is true. Every decision in Java begins here — with a condition that is either true or false."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>boolean</em> holds exactly one of two values: <em>true</em> or <em>false</em>. You've seen it as a type. But booleans also come from comparisons. The expression <em>5 > 3</em> evaluates to <em>true</em>. The expression <em>5 > 10</em> evaluates to <em>false</em>."),
            d("🧙","mentor","Master Velan","s-mentor","The comparison operators: <em>></em> greater than, <em><</em> less than, <em>>=</em> greater than or equal, <em><=</em> less than or equal, <em>==</em> exactly equal (two equals signs!), <em>!=</em> not equal."),
            d("🔮","npc","Seer Orin","s-npc","And the logical operators that combine booleans: <em>&&</em> means AND — both must be true. <em>||</em> means OR — at least one must be true. <em>!</em> means NOT — flips true to false and vice versa."),
            n("Six truth crystals need to be evaluated. Declare the result of each comparison as a boolean and print it.")
          ),
          "Declare and print six boolean variables:<br><br>• <code>boolean a</code> = is 10 greater than 5? → <strong>true</strong><br>• <code>boolean b</code> = is 3 equal to 4? → <strong>false</strong><br>• <code>boolean c</code> = is 7 not equal to 7? → <strong>false</strong><br>• <code>boolean d</code> = is 5 >= 5? → <strong>true</strong><br>• <code>boolean e</code> = true AND false? → <strong>false</strong><br>• <code>boolean f</code> = true OR false? → <strong>true</strong>",
          "Use comparison operators: <code>boolean a = 10 > 5;</code> Use && for AND, || for OR.",
          "// Declare and print six boolean variables\n\n",
          "Six crystals glow in sequence — four gold, two dark. Seer Orin studies the pattern. \"Your logic is clean. No false positives.\"",
          tests(test("a=true","null","true"),test("b=false","null","false"),test("c=false","null","false"),test("d=true","null","true"),test("e=false","null","false"),test("f=true","null","true")));

        q("ch2-q2","The Oracle's Fork","Chapter II · Quest 2","If / Else",2,2,110,"OraclesFork.java",
          story(
            n("The Bridge of Aethon spans a bottomless chasm. The Bridge Keeper guards it, judging every traveller by their gold. No gold, no passage — but the judgement has three tiers."),
            d("🧟","enemy","The Bridge Keeper","s-enemy","More than 50 coins: free passage. Between 10 and 50: pay the toll. Fewer than 10: turn back. I will test three travellers with different amounts."),
            d("🧙","mentor","Master Velan","s-mentor","An <em>if statement</em> runs a block only when its condition is true. Chain <em>else if</em> for additional checks — only the first true branch runs. Add <em>else</em> at the end as a catch-all for when nothing matched."),
            d("🧙","mentor","Master Velan","s-mentor","Structure: <em>if (condition) { ... } else if (condition2) { ... } else { ... }</em> — the curly braces define what runs for each branch. Only one branch ever runs per execution."),
            n("Your code must handle all three cases. The Keeper will test coins = 35, 75, and 3 separately.")
          ),
          "The variable <code>coins</code> is already declared. Write an <strong>if / else if / else</strong> block printing:<br>• <strong>\"You may pass freely.\"</strong> — coins &gt; 50<br>• <strong>\"Pay the toll.\"</strong> — coins 10–50<br>• <strong>\"Turn back.\"</strong> — coins &lt; 10",
          "Use <code>if (coins > 50)</code>, then <code>else if (coins >= 10)</code> — since >50 is excluded, this covers 10–50. Then <code>else</code> catches below 10.",
          "int coins = 35;\n\n// Write your if / else if / else below:\n",
          "The bridge lowers. The Keeper nods. \"Logical. All three travellers judged correctly. Pass.\"",
          tests(test("coins=35","int coins = 35;","Pay the toll."),test("coins=75","int coins = 75;","You may pass freely."),test("coins=3","int coins = 3;","Turn back.")));

        q("ch2-q3","The Sorting Sigil","Chapter II · Quest 3","Switch Statements",2,3,110,"SortingSigil.java",
          story(
            n("The Sorting Chamber. Every new student is assigned to one of four houses based on their elemental affinity. The sorting runs hundreds of times a day — and a long chain of if/else statements would be cumbersome. There is a more elegant rune."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>switch statement</em> is cleaner than a long if/else chain when you're comparing one variable against many specific values. Write <em>switch(variable)</em> and list <em>case</em> blocks for each possible value."),
            d("🧙","mentor","Master Velan","s-mentor","Each case ends with <em>break;</em> — this stops the switch after that case runs. Without break, execution 'falls through' into the next case. Always add a <em>default:</em> block at the end for values that don't match any case."),
            d("🎓","npc","Headmistress Aldara","s-npc","The four houses: Fire affinity goes to Emberhall, Water to Tidespire, Earth to Stoneward, Air to Skyveil. Any unknown affinity is sent to the general intake."),
            n("Write the switch statement that assigns houses. It will be tested with all four affinities plus an unknown one.")
          ),
          "The variable <code>String affinity</code> is declared. Write a <strong>switch</strong> that prints:<br>• <strong>\"Emberhall\"</strong> for <em>\"Fire\"</em><br>• <strong>\"Tidespire\"</strong> for <em>\"Water\"</em><br>• <strong>\"Stoneward\"</strong> for <em>\"Earth\"</em><br>• <strong>\"Skyveil\"</strong> for <em>\"Air\"</em><br>• <strong>\"General Intake\"</strong> for anything else",
          "Use <code>switch(affinity) { case \"Fire\": System.out.println(\"Emberhall\"); break; ... default: ... }</code>",
          "String affinity = \"Fire\";\n\n// Write your switch statement below:\n",
          "The Sorting Sigil pulses and flares to the correct colour. All five test affinities sorted correctly. Headmistress Aldara marks the ledger.",
          tests(test("Fire","String affinity = \"Fire\";","Emberhall"),test("Water","String affinity = \"Water\";","Tidespire"),test("Earth","String affinity = \"Earth\";","Stoneward"),test("Air","String affinity = \"Air\";","Skyveil"),test("Unknown","String affinity = \"Shadow\";","General Intake")));

        q("ch2-q4","The Eternal Binding","Chapter II · Quest 4","While Loops",2,4,110,"WhileLoop.java",
          story(
            n("The Clock Tower. A magical clock that only strikes when its condition is still true. The bell rings, then checks again — and keeps ringing until something changes."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>while loop</em> repeats a block as long as a condition remains true. Write <em>while (condition) { ... }</em> — the block runs, then the condition is checked again, and so on until the condition becomes false."),
            d("🧙","mentor","Master Velan","s-mentor","The crucial rule: something inside the loop must change the condition. If nothing changes, the loop runs forever — an infinite loop. Always ensure the loop will eventually stop."),
            d("🕰️","npc","Clockmaster Fen","s-npc","The clock starts at 1 and must count up to 5, then stop. Each tick it announces the current hour. When it reaches 6, the condition fails and it rests."),
            n("Write a while loop that counts from 1 to 5, printing each number.")
          ),
          "Use a <strong>while loop</strong> with <code>int count = 1;</code> already declared.<br><br>Print each number from 1 to 5, one per line:<br><strong>1<br>2<br>3<br>4<br>5</strong><br><br>Then print: <strong>\"Clock resting.\"</strong>",
          "Use <code>while (count <= 5) { System.out.println(count); count++; }</code> — the <code>count++</code> inside ensures it eventually stops.",
          "int count = 1;\n\n// Use a while loop to print 1 through 5\n// Then print 'Clock resting.'\n",
          "The clock strikes five times, then falls silent. Clockmaster Fen winds the key. \"Perfect cadence. Not a tick wasted.\"",
          tests(test("Prints 1","null","1"),test("Prints 3","null","3"),test("Prints 5","null","5"),test("Clock resting","null","Clock resting.")));

        q("ch2-q5","The Tower of Echoes","Chapter II · Quest 5","For Loops",2,5,110,"ForLoop.java",
          story(
            n("The Tower of Echoes. Each of the five sealed floors demands the same chant — but the number must be exact. While loops are powerful, but when the count is known in advance, there is a more precise rune."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>for loop</em> bundles initialisation, condition, and update into one line: <em>for (int i = 0; i &lt; 5; i++)</em>. The first part runs once at the start. The second is checked before each iteration. The third runs after each iteration."),
            d("🧙","mentor","Master Velan","s-mentor","With <em>i</em> starting at 0 and running while <em>i &lt; 5</em>, you get: 0, 1, 2, 3, 4 — exactly five values. Inside the loop, <em>i</em> is available for use — helpful for printing 'Round 1', 'Round 2'... just use <em>i + 1</em>."),
            n("The Tower counts. The chant must echo five times and the floor must know which echo number it is.")
          ),
          "Use a <strong>for loop</strong> to print five lines numbered 1 through 5:<br><strong>Echo 1<br>Echo 2<br>Echo 3<br>Echo 4<br>Echo 5</strong><br><br>After the loop: <strong>\"Tower unlocked.\"</strong>",
          "Use <code>for (int i = 1; i <= 5; i++) { System.out.println(\"Echo \" + i); }</code> — starting at 1 and going to 5 inclusive is cleaner here.",
          "// Use a for loop to print Echo 1 through Echo 5\n// Then print 'Tower unlocked.'\n",
          "Each floor seal breaks in sequence. A deep resonant boom as the tower opens. \"Five perfect echoes,\" the stone walls whisper.",
          tests(test("Echo 1","null","Echo 1"),test("Echo 3","null","Echo 3"),test("Echo 5","null","Echo 5"),test("Tower unlocked","null","Tower unlocked.")));

        q("ch2-q6","The Nested Labyrinth","Chapter II · Quest 6","Nested Loops",2,6,130,"NestedLoops.java",
          story(
            n("The deepest section of the Academy — the Nested Labyrinth. A grid of 3 by 3 rooms, each one sealed. To unseal every room you must walk every row, and within each row, every column."),
            d("🧙","mentor","Master Velan","s-mentor","Loops can be <em>nested</em> — placed inside other loops. The outer loop runs once per row. The inner loop runs completely for every outer iteration. So for 3 rows and 3 columns, the inner body runs 9 times total."),
            d("🧙","mentor","Master Velan","s-mentor","Use different variable names for each loop: <em>i</em> for the outer, <em>j</em> for the inner. This avoids confusion. You can also use <em>break</em> to exit a loop early, and <em>continue</em> to skip the rest of the current iteration."),
            n("Map every room in the 3×3 grid. Each room is identified by its row and column numbers.")
          ),
          "Use <strong>nested for loops</strong> to print all 9 room coordinates in this format:<br><strong>Room 1-1<br>Room 1-2<br>Room 1-3<br>Room 2-1<br>...</strong><br>All 9 rooms (rows 1–3, columns 1–3).",
          "Outer loop: <code>for (int i = 1; i <= 3; i++)</code> — inner loop: <code>for (int j = 1; j <= 3; j++) { System.out.println(\"Room \" + i + \"-\" + j); }</code>",
          "// Use nested for loops to print Room 1-1 through Room 3-3\n\n",
          "All nine seals break simultaneously. Light floods every chamber. The Labyrinth Keeper bows: \"No room unvisited. Impressive precision.\"",
          tests(test("Room 1-1","null","Room 1-1"),test("Room 2-3","null","Room 2-3"),test("Room 3-3","null","Room 3-3")));

        // ══════════════════════════════════════════════════
        // CHAPTER III — ARCANE STRUCTURES
        // ══════════════════════════════════════════════════

        q("ch3-q1","The Crystal Shelf","Chapter III · Quest 1","Arrays",3,1,120,"CrystalShelf.java",
          story(
            n("The Armoury of Echoes. Hundreds of crystal vials arranged in numbered slots on a long shelf. Pip the apprentice has been losing track of ingredients by using separate variables for each one. There is a better way."),
            d("🧙","mentor","Master Velan","s-mentor","An <em>array</em> stores multiple values of the same type under one name, in numbered slots. Declare it with: <em>String[] spells = {\"Fire\", \"Ice\", \"Wind\"};</em> — three slots, automatically numbered 0, 1, 2."),
            d("🧙","mentor","Master Velan","s-mentor","The slot number is the <em>index</em>. Always starts at zero. Access any element with <em>spells[0]</em> for Fire, <em>spells[2]</em> for Wind. The last valid index is always <em>length - 1</em>. Use <em>spells.length</em> for the count."),
            d("🧒","npc","Pip the Apprentice","s-npc","Five ingredients. I need them under one name, in order, with the total count. Can you set it up for me?"),
            n("Pip hands you a list of five ingredients and waits expectantly.")
          ),
          "Create a <code>String[]</code> named <strong>ingredients</strong> with: <strong>\"Moonpetal\"</strong>, <strong>\"Stardust\"</strong>, <strong>\"Dragonscale\"</strong>, <strong>\"Voidmoss\"</strong>, <strong>\"Emberroot\"</strong><br><br>Loop through and print each on its own line, then print: <strong>\"Total: 5\"</strong>",
          "Declare: <code>String[] ingredients = {\"Moonpetal\", ...};</code> Loop: <code>for (int i = 0; i &lt; ingredients.length; i++) { System.out.println(ingredients[i]); }</code>",
          "// Create your ingredients array\n// Loop through and print each one\n// Then print the total\n\n",
          "All five ingredients appear in order. Pip claps. \"Brilliant! I'm never losing track again.\"",
          tests(test("Moonpetal","null","Moonpetal"),test("Dragonscale","null","Dragonscale"),test("Emberroot","null","Emberroot"),test("Total","null","Total: 5")));

        q("ch3-q2","The Tome of Totals","Chapter III · Quest 2","Array Operations",3,2,130,"ArrayOps.java",
          story(
            n("The Calculation Hall. Arrays aren't just storage — they can be analysed. A scroll on the desk shows a list of seven potion strengths. The Head Alchemist needs the total and the average."),
            d("🧙","mentor","Master Velan","s-mentor","A common pattern: declare a <em>total</em> variable set to 0, then loop through the array adding each element to it. After the loop, total holds the sum of everything."),
            d("🧙","mentor","Master Velan","s-mentor","For the average, divide total by the count. But be careful — if both are ints, you'll get integer division. Cast one to double first: <em>(double) total / arr.length</em>."),
            d("⚗️","npc","Head Alchemist Voryn","s-npc","I need the sum and average of these seven readings: 12, 7, 19, 4, 28, 11, 5. Print both. The average should show one decimal place — use printf."),
            n("A new formatting tool: <em>System.out.printf(\"%.1f\", value)</em> — the .1f means one decimal place. Follow it with a newline using <em>System.out.println();</em>")
          ),
          "<code>int[] readings = {12, 7, 19, 4, 28, 11, 5};</code> is already declared.<br><br>Calculate and print:<br>• The <strong>sum</strong>: <strong>86</strong><br>• The <strong>average</strong> to 1 decimal: <strong>12.3</strong>",
          "Sum loop: <code>int total = 0; for (int i = 0; i < readings.length; i++) total += readings[i];</code> Then average: <code>System.out.printf(\"%.1f%n\", (double) total / readings.length);</code>",
          "int[] readings = {12, 7, 19, 4, 28, 11, 5};\n\n// Calculate the sum and print it\n// Calculate and print the average to 1 decimal place\n",
          "Alchemist Voryn studies the numbers. \"86 total, 12.3 average. Consistent batch. Approved for distribution.\"",
          tests(test("Sum = 86","null","86"),test("Average = 12.3","null","12.3")));

        q("ch3-q3","The Scroll of Lists","Chapter III · Quest 3","ArrayList",3,3,130,"ScrollOfLists.java",
          story(
            n("The Library Annexe. A different kind of collection — one that can grow and shrink. Arrays are fixed in size from the moment they're created. But sometimes you don't know how many items you'll need."),
            d("🧙","mentor","Master Velan","s-mentor","An <em>ArrayList</em> is a resizable list. Import it at the top, then declare: <em>ArrayList&lt;String&gt; names = new ArrayList&lt;&gt;();</em> — the type in angle brackets specifies what it holds."),
            d("🧙","mentor","Master Velan","s-mentor","Add items with <em>names.add(\"Aldric\");</em>. Get the size with <em>names.size()</em>. Access an element by index with <em>names.get(0)</em>. Remove one with <em>names.remove(0)</em> or <em>names.remove(\"Aldric\")</em>."),
            d("📚","npc","Librarian Fen","s-npc","The graduation register starts empty. We add names as they qualify, then print the final list. Three wizards qualify today: Aldric, Zara, and Finn."),
            n("ArrayList needs an import at the top of the file: import java.util.ArrayList;")
          ),
          "Using <code>ArrayList&lt;String&gt;</code>:<br><br>1. Create an empty list named <strong>graduates</strong><br>2. Add: <strong>\"Aldric\"</strong>, <strong>\"Zara\"</strong>, <strong>\"Finn\"</strong><br>3. Print the size: <strong>3</strong><br>4. Print each name using a for loop and <code>.get(i)</code>",
          "Declare: <code>ArrayList&lt;String&gt; graduates = new ArrayList&lt;&gt;();</code> then <code>graduates.add(\"Aldric\");</code> etc. Loop with <code>for (int i = 0; i &lt; graduates.size(); i++)</code>",
          "import java.util.ArrayList;\n\n// Create an ArrayList, add three names, print size then each name\n\n",
          "Three names appear on the graduation scroll. Librarian Fen stamps each one. \"Register complete. Class of the year.\"",
          tests(test("Size = 3","null","3"),test("Aldric listed","null","Aldric"),test("Finn listed","null","Finn")));

        q("ch3-q4","The Spell Codex","Chapter III · Quest 4","Methods",3,4,140,"SpellCodex.java",
          story(
            n("The Grand Codex Hall. Senior wizards write their most powerful spells as reusable incantations. Rather than rewriting the same spell a hundred times, they define it once and invoke it by name whenever needed."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>method</em> is a named block of code. Define it outside main: <em>static void greet() { System.out.println(\"Hello\"); }</em> — then call it from anywhere: <em>greet();</em>. The word <em>static</em> means it belongs to the class, not an object."),
            d("🧙","mentor","Master Velan","s-mentor","Methods can accept <em>parameters</em> — values passed in when calling: <em>static void greet(String name)</em> — call with <em>greet(\"Aldric\");</em>. Parameters are like variables that exist only inside the method."),
            d("🧝","npc","Enchantress Lyra","s-npc","And methods can <em>return</em> values. Replace <em>void</em> with the return type, then use <em>return value;</em> at the end. Whoever called the method gets that value back: <em>int result = add(3, 4);</em>"),
            n("The Codex needs two methods: one that greets wizards, one that adds two numbers together.")
          ),
          "Write two methods:<br><br>1. <strong>greetWizard(String name, int level)</strong> — prints: <strong>\"Welcome, [name]! Level [level].\"</strong><br>2. <strong>add(int a, int b)</strong> — returns the sum<br><br>In main: call greetWizard with <em>\"Kael\", 7</em>. Then print add(12, 8).",
          "Method 1: <code>static void greetWizard(String name, int level) { ... }</code> Method 2: <code>static int add(int a, int b) { return a + b; }</code>",
          "public class SpellCodex {\n\n    // Write greetWizard here\n\n    // Write add here\n\n    public static void main(String[] args) {\n        // Call both methods\n\n    }\n}\n",
          "The Codex glows. Both entries appear in flowing script. Lyra reads them and nods. \"Clean parameters, correct return type. You've grasped reusability.\"",
          tests(test("Greeting","null","Welcome, Kael! Level 7."),test("Sum = 20","null","20")));

        q("ch3-q5","The Recursion Obelisk","Chapter III · Quest 5","Recursion",3,5,160,"RecursionObelisk.java",
          story(
            n("The Obelisk of Endless Reflection. A monolith that shows its own reflection — which shows another reflection, which shows another. At some point, it must stop. This is recursion."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>recursive</em> method calls itself. This sounds circular — and it would be infinite without a <em>base case</em>: a condition that makes it stop. The base case must be reached eventually or the program crashes with a stack overflow."),
            d("🧙","mentor","Master Velan","s-mentor","Classic example: factorial. 5! = 5 × 4 × 3 × 2 × 1. Recursively: factorial(5) = 5 × factorial(4), factorial(4) = 4 × factorial(3)... until factorial(1) = 1 (the base case)."),
            d("🪞","npc","The Obelisk","s-npc","COMPUTE MY REFLECTION COUNT. IF N IS 1, RETURN 1. OTHERWISE RETURN N TIMES THE REFLECTION OF N MINUS 1."),
            n("Write a recursive factorial method. The base case is n == 1. All other cases multiply n by the result of calling factorial with n-1.")
          ),
          "Write a recursive method <strong>factorial(int n)</strong> that returns n!<br><br>Call it from main and print:<br>• <code>factorial(5)</code> → <strong>120</strong><br>• <code>factorial(1)</code> → <strong>1</strong>",
          "Base case: <code>if (n == 1) return 1;</code> Recursive case: <code>return n * factorial(n - 1);</code>",
          "public class RecursionObelisk {\n\n    static int factorial(int n) {\n        // Base case: if n is 1, return 1\n        // Recursive case: return n * factorial(n - 1)\n\n    }\n\n    public static void main(String[] args) {\n        System.out.println(factorial(5));\n        System.out.println(factorial(1));\n    }\n}\n",
          "The Obelisk counts its reflections. Five layers deep, then collapses back to one. \"120. Correct,\" it intones. \"The infinite made finite.\"",
          tests(test("factorial(5)=120","null","120"),test("factorial(1)=1","null","1")));

        // ══════════════════════════════════════════════════
        // CHAPTER IV — THE GRAND GRIMOIRE (OOP)
        // ══════════════════════════════════════════════════

        q("ch4-q1","The Golem Foundry","Chapter IV · Quest 1","Classes & Objects",4,1,150,"GolemFoundry.java",
          story(
            n("The Golem Foundry — where wizards don't just write spells, they create beings. Every golem is defined by a blueprint before it's brought to life as a unique individual. This is object-oriented programming."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>class</em> is the blueprint. An <em>object</em> is one specific instance made from it. Think: 'Wizard' is the class; Aldric is an object of that class. You can make many objects from one class, each with different field values."),
            d("🧙","mentor","Master Velan","s-mentor","A class has <em>fields</em> (its variables) and a <em>constructor</em> (a method that runs when you create a new object). To create an object: <em>Wizard w = new Wizard(\"Aldric\", 5);</em> — the arguments go to the constructor."),
            d("⚙️","npc","Foundry Golem G-1","s-npc","BLUEPRINT REQUIRED. FIELDS: NAME AND LEVEL. CONSTRUCTOR MUST ACCEPT BOTH. METHOD: PRINT SELF-DESCRIPTION."),
            n("Inside a constructor, use <em>this.name = name;</em> to distinguish the field from the parameter when they share a name.")
          ),
          "Create a <strong>Wizard</strong> class with:<br>• Fields: <code>String name</code>, <code>int level</code><br>• Constructor accepting both<br>• Method <code>describe()</code> printing: <strong>\"Wizard [name] is level [level].\"</strong><br><br>In main, create two wizards and call describe():<br>• <code>new Wizard(\"Aldric\", 5)</code><br>• <code>new Wizard(\"Zara\", 9)</code>",
          "Define <code>class Wizard { ... }</code> above the main class. Use <code>this.name = name;</code> in the constructor.",
          "// Define your Wizard class here\n\n\npublic class GolemFoundry {\n    public static void main(String[] args) {\n        // Create two Wizard objects and call describe()\n\n    }\n}\n",
          "Two golems materialise in flashes of light. G-1 booms: \"BLUEPRINTS ACCEPTED. OBJECTS INSTANTIATED.\"",
          tests(test("Aldric","null","Wizard Aldric is level 5."),test("Zara","null","Wizard Zara is level 9.")));

        q("ch4-q2","The Seal of Encapsulation","Chapter IV · Quest 2","Encapsulation",4,2,160,"Encapsulation.java",
          story(
            n("The Vault of Secrets. Not all knowledge should be freely accessible. Powerful wizards protect their inner workings, exposing only what others need to know. This discipline is called encapsulation."),
            d("🧙","mentor","Master Velan","s-mentor","Make fields <em>private</em> by adding the word before the type: <em>private String name;</em> — now only code inside the class can access it directly. Others must go through controlled doorways."),
            d("🧙","mentor","Master Velan","s-mentor","Those doorways are <em>getters</em> and <em>setters</em>. A getter returns the field: <em>public String getName() { return name; }</em>. A setter validates before changing it: <em>public void setLevel(int level) { if (level > 0) this.level = level; }</em>"),
            d("🔒","npc","Vault Keeper Sable","s-npc","A wizard's level must never go negative. Add that validation to the setter. I'll test that invalid values are rejected."),
            n("Private fields, public getters and setters — this pattern is the foundation of safe, maintainable code.")
          ),
          "Create a <strong>Wizard</strong> class with <em>private</em> fields <code>name</code> and <code>level</code>.<br><br>Add:<br>• <code>getName()</code> — returns name<br>• <code>getLevel()</code> — returns level<br>• <code>setLevel(int level)</code> — only sets if level &gt; 0<br><br>In main: create a wizard (level 5), try setting level to -1, then print name and level. Output: <strong>\"Aldric\"</strong> then <strong>5</strong>",
          "Private fields can't be accessed with <code>w.level</code> — use <code>w.getLevel()</code>. The setter ignores negative values so level stays at 5.",
          "public class Encapsulation {\n\n    // Define Wizard class with private fields and getters/setters here\n    static class Wizard {\n        private String name;\n        private int level;\n\n        Wizard(String name, int level) {\n            this.name = name;\n            this.level = level;\n        }\n\n        // Add getName(), getLevel(), setLevel() here\n\n    }\n\n    public static void main(String[] args) {\n        Wizard w = new Wizard(\"Aldric\", 5);\n        w.setLevel(-1);          // should be ignored\n        System.out.println(w.getName());\n        System.out.println(w.getLevel());\n    }\n}\n",
          "The vault door tests the lock. A negative value bounces off. Sable nods: \"Properly guarded. The field cannot be corrupted from outside.\"",
          tests(test("Name = Aldric","null","Aldric"),test("Level = 5","null","5")));

        q("ch4-q3","The Order of Lineage","Chapter IV · Quest 3","Inheritance",4,3,170,"Inheritance.java",
          story(
            n("The Hall of Lineage. Every wizard order shares common ground — name, level, the ability to describe themselves — but each specialises. Writing the same code twice is a cardinal sin in the Academy."),
            d("🧙","mentor","Master Velan","s-mentor","<em>Inheritance</em> lets one class extend another, gaining all its fields and methods automatically. Write <em>class BattleMage extends Wizard</em> — BattleMage immediately has everything Wizard has."),
            d("🧙","mentor","Master Velan","s-mentor","In the child constructor, call <em>super(name, level)</em> first to run the parent constructor. Then initialise any additional fields. The child can also add new methods that the parent doesn't have."),
            d("🧝","npc","Enchantress Lyra","s-npc","Use <em>@Override</em> above a method in the child class to replace the parent's version. The child's version will run instead when called on a child object."),
            n("The Hall needs two specialist classes: BattleMage and Healer, both extending Wizard.")
          ),
          "Given the <code>Wizard</code> base class, create <strong>BattleMage</strong> extending Wizard with a <code>String weapon</code> field.<br>Override <code>describe()</code> to print: <strong>\"BattleMage [name] wields [weapon].\"</strong><br><br>In main:<br>• <code>new BattleMage(\"Kael\", 7, \"Flameblade\")</code>.describe()<br>• <code>new Wizard(\"Zara\", 3)</code>.describe()",
          "Use <code>class BattleMage extends Wizard { String weapon; BattleMage(...) { super(name,level); this.weapon=weapon; } @Override void describe() {...} }</code>",
          "class Wizard {\n    String name; int level;\n    Wizard(String name, int level) { this.name = name; this.level = level; }\n    void describe() { System.out.println(\"Wizard \" + name + \" is level \" + level + \".\"); }\n}\n\n// Write BattleMage here\n\n\npublic class Inheritance {\n    public static void main(String[] args) {\n        // Create a BattleMage and a plain Wizard, call describe on each\n\n    }\n}\n",
          "Kael's portrait appears in battle dress, Flameblade gleaming. A voice: \"The lineage is complete.\"",
          tests(test("BattleMage","null","BattleMage Kael wields Flameblade."),test("Wizard","null","Wizard Zara is level 3.")));

        q("ch4-q4","The Polymorphic Mirrors","Chapter IV · Quest 4","Polymorphism",4,4,180,"Polymorphism.java",
          story(
            n("The Mirror Gallery. Each mirror reflects a different kind of wizard — but when you call their name, each responds in their own way, even though they're all 'wizards' from the outside."),
            d("🧙","mentor","Master Velan","s-mentor","<em>Polymorphism</em> means 'many forms'. A child object can be stored in a parent-type variable: <em>Wizard w = new BattleMage(...);</em>. Java looks at the actual object type at runtime and calls the right overridden method."),
            d("🧙","mentor","Master Velan","s-mentor","This is powerful. You can have an array of <em>Wizard</em> objects that actually holds BattleMages, Healers, Seers — and when you loop through calling <em>describe()</em>, each one responds correctly to its own type."),
            d("🪞","npc","Mirror Keeper Illen","s-npc","Three wizards in the gallery — a plain Wizard, a BattleMage, a Healer. Store all three in a Wizard array. Loop through and call describe on each."),
            n("Create a Healer class extending Wizard with a spell field. Override describe. Then store all three in a Wizard[] array and loop.")
          ),
          "Using <code>Wizard</code>, <code>BattleMage</code> (weapon field), and a new <code>Healer</code> class (spell field):<br><br>Override describe() in each subclass.<br>Store all three in a <code>Wizard[]</code> array.<br>Loop and call describe() on each.<br><br>Expected output:<br><strong>Wizard Zara is level 3.<br>BattleMage Kael wields Flameblade.<br>Healer Mira casts Rejuvenate.</strong>",
          "Declare <code>Wizard[] gallery = { new Wizard(...), new BattleMage(...), new Healer(...) };</code> then loop with a for loop calling <code>gallery[i].describe();</code>",
          "class Wizard {\n    String name; int level;\n    Wizard(String n, int l) { name=n; level=l; }\n    void describe() { System.out.println(\"Wizard \"+name+\" is level \"+level+\".\"); }\n}\nclass BattleMage extends Wizard {\n    String weapon;\n    BattleMage(String n, int l, String w) { super(n,l); weapon=w; }\n    @Override void describe() { System.out.println(\"BattleMage \"+name+\" wields \"+weapon+\".\"); }\n}\n// Write Healer class here\n\npublic class Polymorphism {\n    public static void main(String[] args) {\n        // Create Wizard array with all three types and loop\n\n    }\n}\n",
          "Three reflections appear, each unique. Illen whispers: \"One call, three answers. That is the power of polymorphism.\"",
          tests(test("Wizard Zara","null","Wizard Zara is level 3."),test("BattleMage Kael","null","BattleMage Kael wields Flameblade."),test("Healer Mira","null","Healer Mira casts Rejuvenate.")));

        q("ch4-q5","The Abstract Sanctum","Chapter IV · Quest 5","Abstract Classes & Interfaces",4,5,200,"AbstractSanctum.java",
          story(
            n("The Sanctum of Forms. Some blueprints are too general to build directly — you can't make a generic 'Shape', only specific shapes. These are abstract classes: templates that demand to be extended."),
            d("🧙","mentor","Master Velan","s-mentor","An <em>abstract class</em> is declared with the <em>abstract</em> keyword. It can have abstract methods — declared but not implemented, ending with a semicolon. Any concrete subclass must implement all abstract methods."),
            d("🧙","mentor","Master Velan","s-mentor","An <em>interface</em> goes further — it's a pure contract with no implementation at all (in classic Java). A class <em>implements</em> an interface and must provide all its methods. A class can implement multiple interfaces but only extend one class."),
            d("📐","npc","Architect Tessara","s-npc","I need a Shape abstract class with an abstract area() method, and a Circle and Rectangle implementing it. Then print the area of each."),
            n("Use Math.PI for pi. Circle area = π × r². Rectangle area = width × height.")
          ),
          "Create abstract class <strong>Shape</strong> with abstract method <code>double area()</code>.<br><br>Create <strong>Circle</strong> (radius field) and <strong>Rectangle</strong> (width, height fields) extending Shape.<br><br>In main, print:<br>• Circle radius 5: <strong>78.5</strong> (use <code>Math.round(area * 10) / 10.0</code>)<br>• Rectangle 4×6: <strong>24.0</strong>",
          "Abstract class: <code>abstract class Shape { abstract double area(); }</code> Circle: <code>@Override double area() { return Math.PI * radius * radius; }</code>",
          "// Write abstract Shape class\n// Write Circle and Rectangle subclasses\n\npublic class AbstractSanctum {\n    public static void main(String[] args) {\n        Shape circle = new Circle(5);\n        Shape rect = new Rectangle(4, 6);\n        System.out.println(Math.round(circle.area() * 10) / 10.0);\n        System.out.println(rect.area());\n    }\n}\n",
          "The two shapes materialise in the Sanctum. Tessara measures each. \"78.5 and 24.0. Geometry is magic made precise.\"",
          tests(test("Circle=78.5","null","78.5"),test("Rectangle=24.0","null","24.0")));

        // ══════════════════════════════════════════════════
        // CHAPTER V — THE MASTER'S PATH (Advanced Java)
        // ══════════════════════════════════════════════════

        q("ch5-q1","The Ward of Exceptions","Chapter V · Quest 1","Exception Handling",5,1,200,"ExceptionWard.java",
          story(
            n("The Ward of Exceptions — a place that handles the unexpected. Even the best-written spells can go wrong: a user provides bad input, a file doesn't exist, a number is divided by zero. These moments are called exceptions."),
            d("🧙","mentor","Master Velan","s-mentor","In Java, errors that happen at runtime are called <em>exceptions</em>. Without handling them, your program crashes. Wrap risky code in a <em>try</em> block. If something goes wrong, the <em>catch</em> block runs instead of crashing."),
            d("🧙","mentor","Master Velan","s-mentor","Structure: <em>try { risky code } catch (ExceptionType e) { recovery code }</em>. The variable <em>e</em> holds the exception. Use <em>e.getMessage()</em> to get the error description. Add a <em>finally</em> block for code that runs no matter what."),
            d("🏥","npc","Ward Keeper Nell","s-npc","Division by zero is the most common. Try dividing 10 by 0. Catch the ArithmeticException. Print the message. Then print 'Ward stable.' in finally."),
            n("The finally block always runs — even if there was no exception, even if you caught one.")
          ),
          "Write code that:<br>1. In a <strong>try</strong> block: compute and print <code>10 / 0</code><br>2. In a <strong>catch</strong> for <code>ArithmeticException e</code>: print <strong>\"Caught: \"</strong> + the message<br>3. In a <strong>finally</strong> block: print <strong>\"Ward stable.\"</strong>",
          "Use <code>try { System.out.println(10/0); } catch (ArithmeticException e) { System.out.println(\"Caught: \" + e.getMessage()); } finally { System.out.println(\"Ward stable.\"); }</code>",
          "// Write your try/catch/finally block here\n\n",
          "The ward absorbs the division error cleanly. Nell marks the clipboard. \"Exception handled. No crash. This is professional code.\"",
          tests(test("Caught message","null","Caught: / by zero"),test("Finally runs","null","Ward stable.")));

        q("ch5-q2","The Generics Forge","Chapter V · Quest 2","Generics",5,2,210,"GenericsForge.java",
          story(
            n("The Forge of Forms. You've used ArrayList<String> and ArrayList<Integer> — the type in angle brackets is a generic parameter. Now you'll write your own generic class that works with any type."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>generic</em> class uses a type parameter — a placeholder, usually written <em>T</em>. Declare: <em>class Box&lt;T&gt;</em>. Inside, use T as if it were a real type. When someone creates a Box, they specify T: <em>new Box&lt;String&gt;()</em>."),
            d("🧙","mentor","Master Velan","s-mentor","This is how Java's collections work. A single ArrayList class handles String, Integer, Wizard — any type — because of generics. Your own generic classes follow the same pattern."),
            d("⚒️","npc","Forgemaster Brenn","s-npc","Build a generic Box class that holds one item of any type. Give it a put() method and a get() method. Then use it with a String and an Integer."),
            n("Generic classes are a hallmark of professional Java. The standard library is built almost entirely on them.")
          ),
          "Write a generic class <strong>Box&lt;T&gt;</strong> with:<br>• A private field <code>T item</code><br>• <code>void put(T item)</code> — stores the item<br>• <code>T get()</code> — returns it<br><br>In main:<br>• Store <strong>\"Arcane Scroll\"</strong> in a <code>Box&lt;String&gt;</code> and print it<br>• Store <strong>42</strong> in a <code>Box&lt;Integer&gt;</code> and print it",
          "Declare <code>class Box&lt;T&gt; { private T item; public void put(T item) { this.item = item; } public T get() { return item; } }</code>",
          "// Write your generic Box<T> class here\n\npublic class GenericsForge {\n    public static void main(String[] args) {\n        Box<String> stringBox = new Box<>();\n        stringBox.put(\"Arcane Scroll\");\n        System.out.println(stringBox.get());\n\n        Box<Integer> intBox = new Box<>();\n        intBox.put(42);\n        System.out.println(intBox.get());\n    }\n}\n",
          "Two boxes form on the forge — one glowing silver with text, one pulsing gold with a number. Brenn tests each. \"Type-safe containers. Professional craft.\"",
          tests(test("String box","null","Arcane Scroll"),test("Integer box","null","42")));

        q("ch5-q3","The Lambda Loom","Chapter V · Quest 3","Lambdas & Functional Interfaces",5,3,220,"LambdaLoom.java",
          story(
            n("The Lambda Loom — a weaving machine that creates function-objects on the fly. Java 8 introduced a revolutionary concept: treating functions as values, passing them around like variables."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>lambda expression</em> is a short, anonymous function. Syntax: <em>(parameters) -&gt; { body }</em>. If there's one parameter and one expression, you can simplify: <em>x -&gt; x * 2</em>. This doubles whatever is passed in."),
            d("🧙","mentor","Master Velan","s-mentor","Lambdas work with <em>functional interfaces</em> — interfaces with exactly one abstract method. Java provides many built-in ones. <em>Runnable</em> takes no input and returns nothing. <em>Predicate&lt;T&gt;</em> takes T and returns boolean. <em>Function&lt;T,R&gt;</em> takes T and returns R."),
            d("🧶","npc","Weaver Saya","s-npc","I need a Runnable that prints a message, a Predicate that checks if a number is even, and a Function that doubles a number. Show me lambdas for all three."),
            n("Import java.util.function.Predicate and java.util.function.Function at the top.")
          ),
          "Using lambdas, create and use:<br>1. A <code>Runnable</code> that prints <strong>\"Loom activated.\"</strong> — call <code>r.run()</code><br>2. A <code>Predicate&lt;Integer&gt;</code> that returns true if number is even — test with 4: prints <strong>true</strong><br>3. A <code>Function&lt;Integer,Integer&gt;</code> that doubles input — apply to 7: prints <strong>14</strong>",
          "Runnable: <code>Runnable r = () -> System.out.println(\"Loom activated.\");</code> Predicate: <code>Predicate&lt;Integer&gt; isEven = n -> n % 2 == 0;</code> Function: <code>Function&lt;Integer,Integer&gt; doubler = n -> n * 2;</code>",
          "import java.util.function.Predicate;\nimport java.util.function.Function;\n\npublic class LambdaLoom {\n    public static void main(String[] args) {\n        // 1. Runnable lambda\n\n        // 2. Predicate lambda — test with 4\n\n        // 3. Function lambda — apply to 7\n\n    }\n}\n",
          "Three threads weave themselves on the loom. Saya catches each. \"Functions as values. The loom accepts your craft.\"",
          tests(test("Loom activated","null","Loom activated."),test("Predicate true","null","true"),test("Doubler 14","null","14")));

        q("ch5-q4","The Stream Conduit","Chapter V · Quest 4","Streams API",5,4,230,"StreamConduit.java",
          story(
            n("The Stream Conduit — a river of data that can be filtered, transformed, and collected as it flows. The Streams API is Java's most elegant tool for processing collections without explicit loops."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>stream</em> is a sequence of elements supporting operations. Get one from a list: <em>list.stream()</em>. Then chain operations. <em>filter(predicate)</em> keeps only matching elements. <em>map(function)</em> transforms each element. <em>collect(Collectors.toList())</em> gathers results into a list."),
            d("🧙","mentor","Master Velan","s-mentor","Streams are lazy — nothing happens until a terminal operation (like collect or forEach). You can chain as many intermediate operations as you like before the terminal. <em>forEach</em> prints each element. <em>count()</em> counts them."),
            d("🌊","npc","Conduit Keeper Vael","s-npc","Take a list of numbers from 1 to 8. Filter to only the even ones. Double each. Print them. Then count how many there are."),
            n("Import java.util.Arrays, java.util.List, java.util.stream.Collectors if needed.")
          ),
          "Given <code>List&lt;Integer&gt; numbers = Arrays.asList(1,2,3,4,5,6,7,8);</code>:<br><br>1. Use a stream to filter even numbers, double each, print all — output: <strong>4 8 12 16</strong> (one per line)<br>2. Count and print how many even numbers: <strong>4</strong>",
          "Chain: <code>numbers.stream().filter(n -> n % 2 == 0).map(n -> n * 2).forEach(System.out::println);</code> For count: <code>numbers.stream().filter(n -> n % 2 == 0).count()</code>",
          "import java.util.Arrays;\nimport java.util.List;\n\npublic class StreamConduit {\n    public static void main(String[] args) {\n        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);\n\n        // 1. Filter evens, double, print each\n\n        // 2. Count and print even numbers\n\n    }\n}\n",
          "The conduit flows cleanly — four values emerge, doubled and filtered. Vael reads the output. \"Declarative, efficient, elegant. You've mastered the stream.\"",
          tests(test("First doubled even","null","4"),test("Second doubled even","null","8"),test("Count","null","4")));

        q("ch5-q5","The Pattern Archive","Chapter V · Quest 5","Design Patterns — Singleton & Builder",5,5,250,"PatternArchive.java",
          story(
            n("The Pattern Archive. Every professional developer knows these blueprints — proven solutions to recurring problems. Two of the most common: Singleton and Builder."),
            d("🧙","mentor","Master Velan","s-mentor","The <em>Singleton</em> pattern ensures only one instance of a class ever exists. Make the constructor private. Add a private static field holding the single instance. Add a public static method that returns it, creating it if needed."),
            d("🧙","mentor","Master Velan","s-mentor","The <em>Builder</em> pattern constructs complex objects step by step. Instead of a constructor with ten parameters, you chain method calls: <em>new Wizard.Builder().name(\"Aldric\").level(5).weapon(\"Staff\").build()</em> — each method returns the builder itself for chaining."),
            d("📜","npc","Archivist Crey","s-npc","Implement a Singleton Registry that returns the same instance every time. Call getInstance() twice — they must be the same object. Then build a Wizard using the Builder pattern and print its description."),
            n("Singletons are used for logging, database connections, config managers — anything that should exist only once.")
          ),
          "Implement two patterns:<br><br><strong>1. Singleton:</strong> class <code>Registry</code> — private constructor, static getInstance(), should return same object each time. Print: <strong>\"Same instance: true\"</strong><br><br><strong>2. Builder:</strong> inner <code>Wizard.Builder</code> with chainable name(), level() and build() returning a Wizard. Print: <strong>\"Aldric level 7\"</strong>",
          "Singleton: <code>private static Registry instance; public static Registry getInstance() { if (instance == null) instance = new Registry(); return instance; }</code>",
          "// Implement Singleton Registry\n\n// Implement Wizard with inner Builder class\n\npublic class PatternArchive {\n    public static void main(String[] args) {\n        // Test Singleton\n        Registry r1 = Registry.getInstance();\n        Registry r2 = Registry.getInstance();\n        System.out.println(\"Same instance: \" + (r1 == r2));\n\n        // Test Builder\n        Wizard w = new Wizard.Builder().name(\"Aldric\").level(7).build();\n        System.out.println(w.name + \" level \" + w.level);\n    }\n}\n",
          "The Archive seals two patterns in amber. Crey reads them. \"Singleton confirmed. Builder confirmed. You understand professional architecture.\"",
          tests(test("Same instance","null","Same instance: true"),test("Builder output","null","Aldric level 7")));

        log.info("Seeded {} quests.", questRepository.count());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // BOSSES
    // ─────────────────────────────────────────────────────────────────────────
    private void seedBosses() {
        log.info("Seeding bosses...");

        saveBoss("ch1-boss","The Golem of Types","🗿",1,200,
          "The Golem stirs, stone grinding stone. \"You claim to know types and values. Demonstrate it.\"",
          "[{\"id\":\"c1q1\",\"type\":\"multiple_choice\",\"question\":\"Which type stores the text \\\"Aldric\\\"?\",\"options\":[\"int\",\"boolean\",\"String\",\"double\"],\"correct\":\"String\",\"explanation\":\"String holds text sequences. Always use double quotes for String values.\"},{\"id\":\"c1q2\",\"type\":\"be_the_compiler\",\"question\":\"What does this print?\",\"code\":\"int x = 10;\\nx += 5;\\nx *= 2;\\nSystem.out.println(x);\",\"options\":[\"10\",\"15\",\"25\",\"30\"],\"correct\":\"30\",\"explanation\":\"10+5=15, 15×2=30.\"},{\"id\":\"c1q3\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to declare a decimal variable:\",\"code\":\"______ mana = 99.5;\",\"correct\":\"double\",\"explanation\":\"double holds decimal numbers.\"},{\"id\":\"c1q4\",\"type\":\"be_the_compiler\",\"question\":\"What does this print?\",\"code\":\"int a = 7;\\nint b = 2;\\nSystem.out.println(a / b);\",\"options\":[\"3.5\",\"3\",\"4\",\"Error\"],\"correct\":\"3\",\"explanation\":\"Integer division drops the decimal. 7/2=3.\"},{\"id\":\"c1q5\",\"type\":\"multiple_choice\",\"question\":\"What does System.out.println(\\\"Hi\\\" + \\\" \\\" + \\\"there\\\") print?\",\"options\":[\"Hi there\",\"Hi + there\",\"Error\",\"Hithere\"],\"correct\":\"Hi there\",\"explanation\":\"The + operator concatenates Strings, joining them into one.\"}]");

        saveBoss("ch2-boss","The Labyrinth Warden","🐺",2,200,
          "The Warden blocks the maze exit. \"Only those who truly understand control flow may leave. Answer correctly.\"",
          "[{\"id\":\"c2q1\",\"type\":\"be_the_compiler\",\"question\":\"What prints?\",\"code\":\"int mana = 100;\\nif (mana > 100) {\\n    System.out.println(\\\"Overcharged\\\");\\n} else if (mana == 100) {\\n    System.out.println(\\\"Full\\\");\\n} else {\\n    System.out.println(\\\"Low\\\");\\n}\",\"options\":[\"Overcharged\",\"Full\",\"Low\",\"Nothing\"],\"correct\":\"Full\",\"explanation\":\"100 > 100 is false. 100 == 100 is true. Full prints.\"},{\"id\":\"c2q2\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to loop exactly 4 times:\",\"code\":\"for (int i = 0; i __ 4; i++) { }\",\"correct\":\"<\",\"explanation\":\"i < 4 runs for 0,1,2,3 — exactly 4 times.\"},{\"id\":\"c2q3\",\"type\":\"be_the_compiler\",\"question\":\"What is printed?\",\"code\":\"int total = 0;\\nfor (int i = 1; i <= 5; i++) {\\n    total += i;\\n}\\nSystem.out.println(total);\",\"options\":[\"5\",\"10\",\"15\",\"20\"],\"correct\":\"15\",\"explanation\":\"1+2+3+4+5=15.\"},{\"id\":\"c2q4\",\"type\":\"multiple_choice\",\"question\":\"A while loop that never changes its condition is called what?\",\"options\":[\"A fast loop\",\"An infinite loop\",\"A break loop\",\"A for loop\"],\"correct\":\"An infinite loop\",\"explanation\":\"If the condition never becomes false, the loop runs forever — an infinite loop. Always ensure something changes the condition.\"},{\"id\":\"c2q5\",\"type\":\"be_the_compiler\",\"question\":\"How many times does 'Go!' print?\",\"code\":\"for (int i = 10; i > 0; i -= 3) {\\n    System.out.println(\\\"Go!\\\");\\n}\",\"options\":[\"3\",\"4\",\"10\",\"infinite\"],\"correct\":\"4\",\"explanation\":\"i: 10→7→4→1→stop. Runs 4 times.\"}]");

        saveBoss("ch3-boss","The Vault Keeper","🔐",3,250,
          "The Vault Keeper rises from parchment and shadow. \"Arrays, methods, recursion — the structure of all things. Prove you understand.\"",
          "[{\"id\":\"c3q1\",\"type\":\"be_the_compiler\",\"question\":\"What prints?\",\"code\":\"String[] s = {\\\"Fire\\\", \\\"Ice\\\", \\\"Wind\\\"};\\nSystem.out.println(s[1]);\",\"options\":[\"Fire\",\"Ice\",\"Wind\",\"Error\"],\"correct\":\"Ice\",\"explanation\":\"Indices start at 0. s[1] is Ice.\"},{\"id\":\"c3q2\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to get the array length:\",\"code\":\"int[] nums = {1,2,3,4,5};\\nSystem.out.println(nums.________);\",\"correct\":\"length\",\"explanation\":\".length returns the number of elements.\"},{\"id\":\"c3q3\",\"type\":\"be_the_compiler\",\"question\":\"What does this return?\",\"code\":\"static int factorial(int n) {\\n    if (n == 1) return 1;\\n    return n * factorial(n-1);\\n}\\n// Called with factorial(4)\",\"options\":[\"4\",\"10\",\"24\",\"Error\"],\"correct\":\"24\",\"explanation\":\"4×3×2×1=24.\"},{\"id\":\"c3q4\",\"type\":\"multiple_choice\",\"question\":\"What is the difference between an array and an ArrayList?\",\"options\":[\"Arrays are faster\",\"Arrays are fixed size; ArrayList resizes\",\"ArrayList is for ints only\",\"No difference\"],\"correct\":\"Arrays are fixed size; ArrayList resizes\",\"explanation\":\"Arrays have a fixed size set at creation. ArrayList grows and shrinks dynamically.\"},{\"id\":\"c3q5\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to make this method return the sum:\",\"code\":\"static int add(int a, int b) {\\n    ______ a + b;\\n}\",\"correct\":\"return\",\"explanation\":\"The return keyword sends a value back to the caller.\"}]");

        saveBoss("ch4-boss","The Ancient Dragon","🐉",4,300,
          "The Ancient Dragon uncoils from the highest spire, centuries of knowledge in its eyes. \"Object-oriented mastery is claimed easily. Demonstrate it.\"",
          "[{\"id\":\"c4q1\",\"type\":\"be_the_compiler\",\"question\":\"What prints?\",\"code\":\"class Animal {\\n    void speak() { System.out.println(\\\"sound\\\"); }\\n}\\nclass Dog extends Animal {\\n    @Override void speak() { System.out.println(\\\"bark\\\"); }\\n}\\nAnimal a = new Dog();\\na.speak();\",\"options\":[\"sound\",\"bark\",\"Error\",\"Nothing\"],\"correct\":\"bark\",\"explanation\":\"Polymorphism: Java uses the actual object type (Dog) at runtime, calling bark.\"},{\"id\":\"c4q2\",\"type\":\"multiple_choice\",\"question\":\"What keyword is used in a child constructor to call the parent constructor?\",\"options\":[\"parent()\",\"base()\",\"super()\",\"this()\"],\"correct\":\"super()\",\"explanation\":\"super() calls the parent constructor. Must be first in the child constructor.\"},{\"id\":\"c4q3\",\"type\":\"fill_blank\",\"question\":\"Fill the blank:\",\"code\":\"class BattleMage ______ Wizard { }\",\"correct\":\"extends\",\"explanation\":\"extends establishes inheritance.\"},{\"id\":\"c4q4\",\"type\":\"multiple_choice\",\"question\":\"An abstract class differs from an interface because:\",\"options\":[\"Abstract classes can have constructors and concrete methods\",\"Interfaces are faster\",\"Abstract classes can only be used once\",\"No difference\"],\"correct\":\"Abstract classes can have constructors and concrete methods\",\"explanation\":\"Abstract classes can mix abstract and concrete methods, and have constructors. Traditional interfaces can only have abstract methods.\"},{\"id\":\"c4q5\",\"type\":\"be_the_compiler\",\"question\":\"What does making a field 'private' achieve?\",\"options\":[\"It deletes the field\",\"Only code inside the class can access it directly\",\"It becomes read-only\",\"It speeds up access\"],\"correct\":\"Only code inside the class can access it directly\",\"explanation\":\"private restricts direct access. External code must use getters and setters — the basis of encapsulation.\"}]");

        saveBoss("ch5-boss","The Archmage","⚡",5,400,
          "The Archmage regards you with ancient eyes. \"You have walked from Hello World to design patterns. One final examination before the title is yours.\"",
          "[{\"id\":\"c5q1\",\"type\":\"be_the_compiler\",\"question\":\"What prints?\",\"code\":\"try {\\n    int x = 10 / 0;\\n} catch (ArithmeticException e) {\\n    System.out.println(\\\"caught\\\");\\n} finally {\\n    System.out.println(\\\"done\\\");\\n}\",\"options\":[\"caught\",\"done\",\"caught\\ndone\",\"Error\"],\"correct\":\"caught\\ndone\",\"explanation\":\"The catch block runs (caught), then finally always runs (done).\"},{\"id\":\"c5q2\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to create a lambda doubler:\",\"code\":\"Function<Integer,Integer> doubler = n __ n * 2;\",\"correct\":\"->\",\"explanation\":\"-> is the lambda arrow operator. It separates parameters from the body.\"},{\"id\":\"c5q3\",\"type\":\"multiple_choice\",\"question\":\"What does stream.filter(n -> n > 5) do?\",\"options\":[\"Deletes elements greater than 5\",\"Keeps only elements greater than 5\",\"Counts elements greater than 5\",\"Sorts elements\"],\"correct\":\"Keeps only elements greater than 5\",\"explanation\":\"filter() keeps elements where the predicate returns true. Elements where it returns false are excluded from the stream.\"},{\"id\":\"c5q4\",\"type\":\"multiple_choice\",\"question\":\"The Singleton pattern ensures:\",\"options\":[\"Fast object creation\",\"Only one instance of a class exists\",\"Objects are immutable\",\"Thread safety always\"],\"correct\":\"Only one instance of a class exists\",\"explanation\":\"Singleton restricts instantiation to one object. Used for shared resources like loggers and config managers.\"},{\"id\":\"c5q5\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to declare a generic class:\",\"code\":\"class Box<__> { __ item; }\",\"correct\":\"T\",\"explanation\":\"T is the conventional name for a type parameter in generic classes. It's a placeholder replaced with a real type when the class is used.\"}]");

        log.info("Seeded {} bosses.", bossRepository.count());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HELPERS — keep seeder code readable
    // ─────────────────────────────────────────────────────────────────────────
    private void q(String id, String title, String eyebrow, String topic,
                   int chapter, int order, int xp, String file,
                   String story, String problem, String hint, String starter,
                   String win, String tests) {
        questRepository.save(Quest.builder()
            .id(id).title(title).eyebrow(eyebrow).topic(topic)
            .chapterNumber(chapter).orderInChapter(order).xpReward(xp).filename(file)
            .storyJson(story).problemHtml(problem).hint(hint)
            .starterCode(starter).winStory(win).testCasesJson(tests)
            .build());
    }

    private void saveBoss(String id, String name, String glyph, int chapter,
                           int xp, String intro, String questions) {
        bossRepository.save(Boss.builder()
            .id(id).name(name).glyph(glyph).chapterNumber(chapter)
            .xpReward(xp).intro(intro).questionsJson(questions)
            .build());
    }

    private String story(String... beats) { return "[" + String.join(",", beats) + "]"; }
    private String n(String text) { return "{\"type\":\"narration\",\"text\":\"" + esc(text) + "\"}"; }
    private String d(String av, String cls, String speaker, String sCls, String text) {
        return "{\"type\":\"dialogue\",\"av\":\"" + av + "\",\"cls\":\"" + cls +
               "\",\"speaker\":\"" + speaker + "\",\"sCls\":\"" + sCls +
               "\",\"text\":\"" + esc(text) + "\"}";
    }
    private String tests(String... ts) { return "[" + String.join(",", ts) + "]"; }
    private String test(String label, String input, String expected) {
        return "{\"label\":\"" + label + "\",\"input\":" + ("null".equals(input) ? "null" : "\"" + esc(input) + "\"") +
               ",\"expected\":\"" + esc(expected) + "\"}";
    }
    private String esc(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\t", "\\t");
    }
}
