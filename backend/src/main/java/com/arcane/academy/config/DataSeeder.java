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

    private void seedQuests() {
        log.info("Seeding quests...");
        seedChapterOne();
        seedChapterTwo();
        seedChapterThree();
        seedChapterFour();
        seedChapterFive();
        seedChapterSix();
        seedChapterSeven();
        log.info("Seeded {} quests.", questRepository.count());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CHAPTER I — THE FIRST RUNE (Variables & Types)
    // ══════════════════════════════════════════════════════════════════════════
    private void seedChapterOne() {

        q("ch1-q1","The Wall of First Words","Chapter I · Quest 1","Hello World",1,1,80,"HelloWorld.java",
          story(
            n("You push open the great oak doors of Arcane Academy. The entrance hall is vast and dimly lit by floating orbs of blue light. At the far end stands an enormous stone wall — the <em>Wall of First Words</em> — covered floor to ceiling in glowing text."),
            d("🧙","mentor","Master Velan","s-mentor","Ah, a new apprentice. Every wizard who ever graduated began with the same thing: making the world say something back. A Java program is just instructions for the computer, read top to bottom. Your first instruction makes it speak."),
            d("🧝","npc","Enchantress Lyra","s-npc","To display text, you write <em>System.out.println()</em> and put your message in double quotes inside the brackets. System is Java's connection to your screen. println means print a line. The semicolon at the end is like a full stop — miss it and nothing runs."),
            e("Worked Example","<span class='cm'>// This prints a message to the screen</span>\n<span class='kw'>System</span>.out.println(<span class='str'>\"Hello, world!\"</span>);\n<span class='cm'>// Output: Hello, world!</span>"),
            n("The starter code already has the outer shell — public class and main method. Every Java program needs that structure. Your spell goes inside the curly braces of main.")
          ),
          "Add <strong>one line</strong> inside the <code>main</code> method that prints exactly:<br><strong>Welcome to Arcane Academy!</strong>",
          "Type inside the curly braces: <code>System.out.println(\"Welcome to Arcane Academy!\");</code>",
          "public class HelloWorld {\n    public static void main(String[] args) {\n        // Write your spell below\n        \n    }\n}\n",
          "Golden letters blaze across the Wall of First Words. Master Velan places a hand on your shoulder. \"The Academy hears you. You are a programmer now.\"",
          tests(test("Output","null","Welcome to Arcane Academy!")));

        q("ch1-q2","The Hall of Bindings","Chapter I · Quest 2","Variables",1,2,100,"Bindings.java",
          story(
            n("Master Velan leads you into the Hall of Bindings — a circular room lined with hundreds of small glowing jars. Each has a label on the outside and coloured light sealed inside."),
            d("🧙","mentor","Master Velan","s-mentor","These are variables. A variable is a named container that holds a value. You give it a name so you can find it again, and fill it with something. The label on the jar is the name. The light inside is the value."),
            d("🧟","npc","Caretaker Moss","s-npc","Without variables, every piece of information vanishes the moment it's used. A wizard's name, their level, how much mana they have — all stored in variables."),
            d("🧙","mentor","Master Velan","s-mentor","In Java, you create a variable in three parts: the <em>type</em> — what kind of thing it holds. The <em>name</em> — what you call it. The <em>value</em> — what you put inside. Then a semicolon."),
            e("Worked Example — Declaring Variables","<span class='cm'>// type    name      value</span>\n<span class='type'>int</span>     level   = <span class='num'>5</span>;\n<span class='type'>String</span>  name    = <span class='str'>\"Aldric\"</span>;\n<span class='type'>double</span>  mana    = <span class='num'>87.5</span>;\n<span class='type'>boolean</span> cursed  = <span class='kw'>false</span>;"),
            d("🧙","mentor","Master Velan","s-mentor","Four types: <em>int</em> for whole numbers. <em>double</em> for decimals. <em>boolean</em> for true or false. <em>String</em> for text — always in double quotes. Note: String starts with a capital S."),
            e("Worked Example — Printing Variables","<span class='type'>int</span> level = <span class='num'>5</span>;\n\n<span class='cm'>// Prints the NUMBER 5, not the word 'level'</span>\n<span class='kw'>System</span>.out.println(level);\n<span class='cm'>// Output: 5</span>"),
            n("Variable names are case-sensitive. 'level' and 'Level' are different variables. By convention, names start lowercase. Make them descriptive — wizardLevel is better than x.")
          ),
          "Declare four variables and print each on its own line:<br>• <code>String wizardName</code> = <strong>\"Aldric\"</strong><br>• <code>int level</code> = <strong>1</strong><br>• <code>double mana</code> = <strong>100.0</strong><br>• <code>boolean cursed</code> = <strong>false</strong>",
          "Declare first: <code>String wizardName = \"Aldric\";</code> then print: <code>System.out.println(wizardName);</code>",
          "// Declare four variables and print each one\n\n",
          "The four jars seal. Caretaker Moss stamps the registry. \"First wizard in years to get the types right first try.\"",
          tests(test("wizardName","null","Aldric"),test("level","null","1"),test("mana","null","100.0"),test("cursed","null","false")));

        q("ch1-q3","The Cauldron of Computation","Chapter I · Quest 3","Arithmetic",1,3,110,"Cauldron.java",
          story(
            n("The Academy basement. The Cauldron of Computation bubbles and hisses. Potions are brewed by calculation. One wrong operator and the batch explodes."),
            d("🧪","npc","Brewmaster Zyn","s-npc","Java understands arithmetic: + to add, - to subtract, * to multiply, / to divide. But be careful with integers — 7 / 2 gives 3, not 3.5. Java cuts the decimal off. Use double if you need it."),
            e("Worked Example — Arithmetic","<span class='type'>int</span> a = <span class='num'>10</span>, b = <span class='num'>3</span>;\n<span class='kw'>System</span>.out.println(a + b); <span class='cm'>// 13</span>\n<span class='kw'>System</span>.out.println(a * b); <span class='cm'>// 30</span>\n<span class='kw'>System</span>.out.println(a / b); <span class='cm'>// 3 — not 3.33!</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Variables can also be updated. Once declared, write the name, equals, and new value. No type needed the second time. Or use shortcuts: <em>x += 5</em> means x = x + 5. <em>x++</em> adds exactly 1."),
            e("Worked Example — Shortcuts","<span class='type'>int</span> strength = <span class='num'>10</span>;\nstrength += <span class='num'>5</span>;  <span class='cm'>// now 15</span>\nstrength *= <span class='num'>2</span>;  <span class='cm'>// now 30</span>\nstrength -= <span class='num'>4</span>;  <span class='cm'>// now 26</span>\nstrength++;    <span class='cm'>// now 27</span>")
          ),
          "<code>int potionStrength = 10;</code> is declared. Apply three steps then print:<br>1. Add <strong>15</strong> using +=<br>2. Multiply by <strong>3</strong> using *=<br>3. Subtract <strong>20</strong> using -=<br><em>Expected: 55</em>",
          "10 + 15 = 25, × 3 = 75, − 20 = 55",
          "int potionStrength = 10;\n\n// Step 1: add 15\n// Step 2: multiply by 3\n// Step 3: subtract 20\n// Print the result\n",
          "\"55,\" Zyn reads. \"The Potion of Fortitude. Exactly right.\"",
          tests(test("Result = 55","null","55")));

        q("ch1-q4","The Scriptorium","Chapter I · Quest 4","String Operations",1,4,110,"Scriptorium.java",
          story(
            n("The Scriptorium — where wizards work with text. Librarian Fen needs a wizard's record assembled from separate pieces."),
            d("🧙","mentor","Master Velan","s-mentor","Join two Strings with the <em>+</em> operator — this is called concatenation. You can also mix Strings and numbers: Java converts the number to text automatically."),
            e("Worked Example — Concatenation","<span class='type'>String</span> first = <span class='str'>\"Aria\"</span>;\n<span class='type'>String</span> last  = <span class='str'>\"Voss\"</span>;\n<span class='type'>String</span> full  = first + <span class='str'>\" \"</span> + last;\n<span class='kw'>System</span>.out.println(full);  <span class='cm'>// Aria Voss</span>\n\n<span class='type'>int</span> level = <span class='num'>7</span>;\n<span class='kw'>System</span>.out.println(<span class='str'>\"Level: \"</span> + level); <span class='cm'>// Level: 7</span>"),
            d("📚","npc","Librarian Fen","s-npc","Strings have built-in methods. Call them with a dot after the variable name. length() counts characters. toUpperCase() shouts. charAt(0) picks the first character — remember, Java counts from zero."),
            e("Worked Example — String Methods","<span class='type'>String</span> name = <span class='str'>\"aldric\"</span>;\n<span class='kw'>System</span>.out.println(name.length());       <span class='cm'>// 6</span>\n<span class='kw'>System</span>.out.println(name.toUpperCase());  <span class='cm'>// ALDRIC</span>\n<span class='kw'>System</span>.out.println(name.charAt(<span class='num'>0</span>));      <span class='cm'>// a</span>")
          ),
          "<code>String firstName = \"Aria\";</code> and <code>String lastName = \"Voss\";</code> declared. Print:<br>1. Full name joined: <strong>Aria Voss</strong><br>2. Label + full name: <strong>Wizard: Aria Voss</strong><br>3. Full name length: <strong>9</strong><br>4. Uppercase: <strong>ARIA VOSS</strong>",
          "Join: <code>String full = firstName + \" \" + lastName;</code> then use <code>full.length()</code> and <code>full.toUpperCase()</code>",
          "String firstName = \"Aria\";\nString lastName = \"Voss\";\n\n// 1. Print the full name\n// 2. Print 'Wizard: ' + full name\n// 3. Print the length\n// 4. Print uppercase\n",
          "Fen's quill transcribes each line. \"Complete. Filed under V for Voss.\"",
          tests(test("Full name","null","Aria Voss"),test("Wizard label","null","Wizard: Aria Voss"),test("Length","null","9"),test("Uppercase","null","ARIA VOSS")));

        q("ch1-q5","The Chamber of Truth","Chapter I · Quest 5","Booleans & Comparisons",1,5,120,"TruthChamber.java",
          story(
            n("The Chamber of Truth. Six crystals on a pedestal — some glowing gold, some dark. Each represents a comparison that is either true or false."),
            d("🔮","npc","Seer Orin","s-npc","A boolean holds exactly two values: true or false. Most booleans come from comparisons — asking a question about two values. The answer is either true or false."),
            e("Worked Example — Comparisons","<span class='type'>int</span> a = <span class='num'>10</span>, b = <span class='num'>5</span>;\n<span class='type'>boolean</span> r1 = (a > b);   <span class='cm'>// true</span>\n<span class='type'>boolean</span> r2 = (a == b);  <span class='cm'>// false — TWO equals signs!</span>\n<span class='type'>boolean</span> r3 = (a != b);  <span class='cm'>// true — not equal</span>\n<span class='type'>boolean</span> r4 = (a >= <span class='num'>10</span>); <span class='cm'>// true — greater or equal</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Critical rule: <em>==</em> checks equality. <em>=</em> assigns a value. Confusing these is the most common beginner mistake. Also: <em>&&</em> means AND (both true), <em>||</em> means OR (at least one true), <em>!</em> flips true to false."),
            e("Worked Example — Logical Operators","<span class='type'>boolean</span> a = <span class='kw'>true</span>, b = <span class='kw'>false</span>;\n<span class='kw'>System</span>.out.println(a && b);  <span class='cm'>// false — AND</span>\n<span class='kw'>System</span>.out.println(a || b);  <span class='cm'>// true  — OR</span>\n<span class='kw'>System</span>.out.println(!a);      <span class='cm'>// false — NOT</span>")
          ),
          "Declare and print six booleans:<br>• <code>boolean a</code> = is 10 > 5? → <strong>true</strong><br>• <code>boolean b</code> = is 3 == 4? → <strong>false</strong><br>• <code>boolean c</code> = is 7 != 7? → <strong>false</strong><br>• <code>boolean d</code> = is 5 >= 5? → <strong>true</strong><br>• <code>boolean e</code> = true && false? → <strong>false</strong><br>• <code>boolean f</code> = true || false? → <strong>true</strong>",
          "Use <code>boolean a = (10 > 5);</code> — use && for AND, || for OR",
          "// Declare and print six boolean variables\n\n",
          "Six crystals respond. \"Perfect. Every truth correctly identified.\"",
          tests(test("a=true","null","true"),test("b=false","null","false"),test("c=false","null","false"),test("d=true","null","true"),test("e=false","null","false"),test("f=true","null","true")));

        q("ch1-q6","The Alchemist's Scales","Chapter I · Quest 6","Doubles & Casting",1,6,120,"AlchemistScales.java",
          story(
            n("The Transformation Laboratory. Alchemist Prue measures powders with surgical precision."),
            d("⚗️","npc","Alchemist Prue","s-npc","An int gives you whole numbers. A double gives you decimals. 3.5 grams and 3 grams are not the same potion. That is the difference."),
            e("Worked Example — int vs double","<span class='cm'>// int division — drops decimal</span>\n<span class='type'>int</span> a = <span class='num'>7</span>, b = <span class='num'>2</span>;\n<span class='kw'>System</span>.out.println(a / b);          <span class='cm'>// 3, not 3.5!</span>\n\n<span class='cm'>// double — keeps decimal</span>\n<span class='type'>double</span> x = <span class='num'>7.0</span>, y = <span class='num'>2.0</span>;\n<span class='kw'>System</span>.out.println(x / y);          <span class='cm'>// 3.5</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Use a <em>cast</em> to temporarily convert a type — write the target type in brackets before the value. Widening (int → double) is safe. Narrowing (double → int) drops the decimal — 3.9 becomes 3, not 4."),
            e("Worked Example — Casting","<span class='type'>int</span> a = <span class='num'>7</span>, b = <span class='num'>2</span>;\n<span class='kw'>System</span>.out.println((<span class='type'>double</span>) a / b);  <span class='cm'>// 3.5</span>\n\n<span class='type'>double</span> pi = <span class='num'>3.14159</span>;\n<span class='kw'>System</span>.out.println((<span class='type'>int</span>) pi);       <span class='cm'>// 3</span>")
          ),
          "Print three results:<br>1. <code>int total=7, portions=2</code> — divide as decimal: <strong>3.5</strong><br>2. <code>double ingredient=9.75</code> — cast to int: <strong>9</strong><br>3. Compute 22/7 as decimal to 1dp using printf: <strong>3.1</strong>",
          "Cast: <code>(double) total / portions</code>. Printf: <code>System.out.printf(\"%.1f%n\", (double)22/7);</code>",
          "int total = 7;\nint portions = 2;\ndouble ingredient = 9.75;\n\n// 1. Print total/portions as decimal\n// 2. Print ingredient cast to int\n// 3. Print 22/7 to 1 decimal place\n",
          "\"3.5. 9. 3.1. Exact,\" Prue confirms. \"This is why types matter.\"",
          tests(test("Decimal div","null","3.5"),test("Cast to int","null","9"),test("22/7","null","3.1")));

        q("ch1-q7","The Wizard's Profile","Chapter I · Quest 7","Putting It Together",1,7,130,"WizardProfile.java",
          story(
            n("The Academy Archive. Archivist Cress waits with an empty file folder."),
            d("🗂️","npc","Archivist Cress","s-npc","Every wizard gets a profile card — name, level, mana, active status, and a display line combining everything. Build the template for Dain Ashford."),
            d("🧙","mentor","Master Velan","s-mentor","This quest uses everything from Chapter I. Declare all four variables first, then write the five print statements. Take your time — check for missing quotes and semicolons."),
            e("A Complete Example","<span class='type'>String</span>  name   = <span class='str'>\"Seraphine\"</span>;\n<span class='type'>int</span>     level  = <span class='num'>8</span>;\n<span class='type'>double</span>  mana   = <span class='num'>72.5</span>;\n<span class='type'>boolean</span> active = <span class='kw'>true</span>;\n\n<span class='kw'>System</span>.out.println(<span class='str'>\"Name: \"</span> + name);\n<span class='kw'>System</span>.out.println(<span class='str'>\"Level: \"</span> + level);\n<span class='kw'>System</span>.out.println(<span class='str'>\"Mana: \"</span> + mana + <span class='str'>\"%\"</span>);\n<span class='kw'>System</span>.out.println(<span class='str'>\"Active: \"</span> + active);\n<span class='kw'>System</span>.out.println(name + <span class='str'>\" is a level \"</span> + level + <span class='str'>\" wizard.\"</span>);")
          ),
          "For <strong>Dain Ashford</strong>: <code>String wizardName=\"Dain Ashford\", int level=3, double mana=85.0, boolean active=true</code><br><br>Print exactly:<br><strong>Name: Dain Ashford<br>Level: 3<br>Mana: 85.0%<br>Active: true<br>Dain Ashford is a level 3 wizard.</strong>",
          "Mana line: <code>System.out.println(\"Mana: \" + mana + \"%\");</code> Last line: <code>System.out.println(wizardName + \" is a level \" + level + \" wizard.\");</code>",
          "// Declare four variables for Dain Ashford\n\n// Print five lines in the exact format\n\n",
          "Cress files the card. \"Correct format. All fields populated. Chapter One: complete.\" She hands you a copper badge.",
          tests(test("Name line","null","Name: Dain Ashford"),test("Level line","null","Level: 3"),test("Mana line","null","Mana: 85.0%"),test("Active line","null","Active: true"),test("Summary","null","Dain Ashford is a level 3 wizard.")));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CHAPTER II — THE CONTROL TOME
    // ══════════════════════════════════════════════════════════════════════════
    private void seedChapterTwo() {

        q("ch2-q1","The Oracle's Fork","Chapter II · Quest 1","If / Else",2,1,120,"OraclesFork.java",
          story(
            n("The Bridge of Aethon spans a bottomless chasm. The Bridge Keeper turns away all who cannot answer in logic."),
            d("🧟","enemy","The Bridge Keeper","s-enemy","Every traveller is judged. Rich or poor, their fate decided by their gold. Logic only."),
            d("🧙","mentor","Master Velan","s-mentor","An <em>if statement</em> runs a block of code only when its condition is true. Chain <em>else if</em> for a second check. <em>else</em> is the catch-all when nothing matched."),
            e("Worked Example — If / Else","<span class='type'>int</span> score = <span class='num'>75</span>;\n\n<span class='kw'>if</span> (score >= <span class='num'>90</span>) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Excellent\"</span>);\n} <span class='kw'>else if</span> (score >= <span class='num'>60</span>) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Pass\"</span>);\n} <span class='kw'>else</span> {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Fail\"</span>);\n}\n<span class='cm'>// prints: Pass</span>"),
            d("🧟","enemy","The Bridge Keeper","s-enemy","More than 50 coins: free passage. Ten to fifty: pay the toll. Under ten: turn back. I will test three travellers.")
          ),
          "Variable <code>coins</code> is declared. Write <strong>if/else if/else</strong> printing:<br>• <strong>\"You may pass freely.\"</strong> — coins &gt; 50<br>• <strong>\"Pay the toll.\"</strong> — coins 10–50<br>• <strong>\"Turn back.\"</strong> — coins &lt; 10",
          "Use <code>if (coins > 50)</code>, then <code>else if (coins >= 10)</code>, then <code>else</code>",
          "int coins = 35;\n\n// Write your if / else if / else below:\n",
          "The bridge lowers. \"Logical. All three travellers judged correctly. Pass.\"",
          tests(test("coins=35","int coins = 35;","Pay the toll."),test("coins=75","int coins = 75;","You may pass freely."),test("coins=3","int coins = 3;","Turn back.")));

        q("ch2-q2","The Sorting Sigil","Chapter II · Quest 2","Switch Statements",2,2,120,"SortingSigil.java",
          story(
            n("The Sorting Chamber. Four houses, four affinities. A long chain of if-else statements would be messy — there is a more elegant rune for this."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>switch statement</em> compares one variable against many specific values. Each <em>case</em> block ends with <em>break</em> to stop execution. Without break, it falls into the next case — usually not what you want. The <em>default</em> block handles anything unmatched."),
            e("Worked Example — Switch","<span class='type'>String</span> colour = <span class='str'>\"red\"</span>;\n\n<span class='kw'>switch</span> (colour) {\n    <span class='kw'>case</span> <span class='str'>\"red\"</span>:\n        <span class='kw'>System</span>.out.println(<span class='str'>\"Emberhall\"</span>);\n        <span class='kw'>break</span>;\n    <span class='kw'>case</span> <span class='str'>\"blue\"</span>:\n        <span class='kw'>System</span>.out.println(<span class='str'>\"Tidespire\"</span>);\n        <span class='kw'>break</span>;\n    <span class='kw'>default</span>:\n        <span class='kw'>System</span>.out.println(<span class='str'>\"Unknown\"</span>);\n}"),
            d("🎓","npc","Headmistress Aldara","s-npc","Fire → Emberhall. Water → Tidespire. Earth → Stoneward. Air → Skyveil. Anything else → General Intake. Test against all five.")
          ),
          "Write a <strong>switch</strong> on <code>String affinity</code> printing the correct house for Fire, Water, Earth, Air, and any unknown affinity.",
          "Use <code>switch(affinity) { case \"Fire\": System.out.println(\"Emberhall\"); break; ... default: ... }</code>",
          "String affinity = \"Fire\";\n\n// Write your switch statement:\n",
          "The Sorting Sigil flares the correct colour for all five affinities.",
          tests(test("Fire","String affinity = \"Fire\";","Emberhall"),test("Water","String affinity = \"Water\";","Tidespire"),test("Earth","String affinity = \"Earth\";","Stoneward"),test("Air","String affinity = \"Air\";","Skyveil"),test("Unknown","String affinity = \"Shadow\";","General Intake")));

        q("ch2-q3","The Clock Tower","Chapter II · Quest 3","While Loops",2,3,120,"ClockTower.java",
          story(
            n("The Clock Tower. A magical clock that only strikes when its condition is still true. The key rule: something inside the loop must change the condition, or it runs forever."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>while loop</em> repeats a block as long as a condition is true. The condition is checked before every iteration. If it's false from the start, the body never runs at all."),
            e("Worked Example — While Loop","<span class='type'>int</span> count = <span class='num'>1</span>;\n\n<span class='kw'>while</span> (count <= <span class='num'>5</span>) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Tick \"</span> + count);\n    count++;  <span class='cm'>// MUST change count — or infinite loop!</span>\n}\n<span class='cm'>// prints: Tick 1, Tick 2 ... Tick 5</span>"),
            d("🕰️","npc","Clockmaster Fen","s-npc","The clock starts at 1 and must count to 5, then rest. Each tick announces the hour. When it hits 6, the condition fails and it stops.")
          ),
          "Using <code>int count = 1;</code> declared, use a <strong>while loop</strong> to print 1 through 5. After the loop print: <strong>\"Clock resting.\"</strong>",
          "Use <code>while (count <= 5) { System.out.println(count); count++; }</code>",
          "int count = 1;\n\n// Use a while loop to print 1 through 5\n// Then print 'Clock resting.'\n",
          "The clock strikes five times, then falls silent. \"Perfect cadence.\"",
          tests(test("Prints 1","null","1"),test("Prints 5","null","5"),test("Clock resting","null","Clock resting.")));

        q("ch2-q4","The Tower of Echoes","Chapter II · Quest 4","For Loops",2,4,120,"ForLoop.java",
          story(
            n("The Tower of Echoes. Five sealed floors, each demanding a chant spoken a precise number of times. When the count is known in advance, there is a more precise rune than a while loop."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>for loop</em> bundles initialise, condition, and update into one line. It's the standard choice when you know exactly how many iterations are needed. The loop variable is available inside the body — useful for numbering output."),
            e("Worked Example — For Loop","<span class='cm'>// Counts 1 to 5</span>\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>1</span>; i <= <span class='num'>5</span>; i++) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Round \"</span> + i);\n}\n<span class='cm'>// prints: Round 1, Round 2 ... Round 5</span>"),
            n("The Tower counts. The chant must echo exactly five times and each echo must be numbered.")
          ),
          "Use a <strong>for loop</strong> to print:<br><strong>Echo 1<br>Echo 2<br>Echo 3<br>Echo 4<br>Echo 5</strong><br>Then print: <strong>\"Tower unlocked.\"</strong>",
          "Use <code>for (int i = 1; i <= 5; i++) { System.out.println(\"Echo \" + i); }</code>",
          "// Use a for loop to print Echo 1 through Echo 5\n// Then print 'Tower unlocked.'\n",
          "Five perfect echoes. The tower opens.",
          tests(test("Echo 1","null","Echo 1"),test("Echo 5","null","Echo 5"),test("Tower unlocked","null","Tower unlocked.")));

        q("ch2-q5","The Nested Labyrinth","Chapter II · Quest 5","Nested Loops",2,5,140,"NestedLoops.java",
          story(
            n("The Nested Labyrinth — a 3×3 grid of sealed rooms. To unseal every room you must walk every row, and within each row, every column."),
            d("🧙","mentor","Master Velan","s-mentor","Loops can be <em>nested</em> — placed inside other loops. The outer loop runs once per row. For each outer iteration, the inner loop runs completely. Use different counter names: <em>i</em> for outer, <em>j</em> for inner."),
            e("Worked Example — Nested Loops","<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>1</span>; i <= <span class='num'>3</span>; i++) {\n    <span class='kw'>for</span> (<span class='type'>int</span> j = <span class='num'>1</span>; j <= <span class='num'>3</span>; j++) {\n        <span class='kw'>System</span>.out.println(i + <span class='str'>\"-\"</span> + j);\n    }\n}\n<span class='cm'>// 1-1, 1-2, 1-3, 2-1 ... 3-3 (9 total)</span>"),
            n("3 rows × 3 columns = 9 rooms. The inner loop must complete fully before the outer loop moves to the next row.")
          ),
          "Use <strong>nested for loops</strong> to print all 9 room coordinates:<br><strong>Room 1-1 through Room 3-3</strong>",
          "Outer: <code>for (int i = 1; i <= 3; i++)</code> Inner: <code>for (int j = 1; j <= 3; j++) { System.out.println(\"Room \" + i + \"-\" + j); }</code>",
          "// Nested for loops — print Room 1-1 through Room 3-3\n\n",
          "All nine seals break simultaneously. The Labyrinth opens.",
          tests(test("Room 1-1","null","Room 1-1"),test("Room 2-3","null","Room 2-3"),test("Room 3-3","null","Room 3-3")));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CHAPTER III — ARCANE STRUCTURES
    // ══════════════════════════════════════════════════════════════════════════
    private void seedChapterThree() {

        q("ch3-q1","The Crystal Shelf","Chapter III · Quest 1","Arrays",3,1,130,"CrystalShelf.java",
          story(
            n("The Armoury of Echoes. Pip the apprentice has five ingredients stored in five separate variables. Every time a new ingredient is added he has to create a new variable. There must be a better way."),
            d("🧙","mentor","Master Velan","s-mentor","An <em>array</em> stores multiple values of the same type under one name, in numbered slots. The slot number is the <em>index</em>. It always starts at zero. The first element is at index 0, not index 1 — this trips up beginners constantly."),
            e("Worked Example — Arrays","<span class='type'>String</span>[] spells = {<span class='str'>\"Fire\"</span>, <span class='str'>\"Ice\"</span>, <span class='str'>\"Wind\"</span>};\n\n<span class='kw'>System</span>.out.println(spells[<span class='num'>0</span>]);     <span class='cm'>// Fire</span>\n<span class='kw'>System</span>.out.println(spells[<span class='num'>2</span>]);     <span class='cm'>// Wind</span>\n<span class='kw'>System</span>.out.println(spells.length);  <span class='cm'>// 3</span>\n\n<span class='cm'>// Loop through all</span>\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < spells.length; i++) {\n    <span class='kw'>System</span>.out.println(spells[i]);\n}"),
            d("🧒","npc","Pip the Apprentice","s-npc","Five ingredients! Moonpetal, Stardust, Dragonscale, Voidmoss, Emberroot. One array. Print them all then tell me the total count.")
          ),
          "Create <code>String[] ingredients</code> with the five ingredients. Loop and print each, then print: <strong>\"Total: 5\"</strong>",
          "Declare: <code>String[] ingredients = {\"Moonpetal\", \"Stardust\", \"Dragonscale\", \"Voidmoss\", \"Emberroot\"};</code> Loop from i=0 to length.",
          "// Create your ingredients array, loop and print each, then print total\n\n",
          "All five appear in order. Pip claps: \"Brilliant! One array, five ingredients!\"",
          tests(test("Moonpetal","null","Moonpetal"),test("Dragonscale","null","Dragonscale"),test("Total","null","Total: 5")));

        q("ch3-q2","The Tome of Totals","Chapter III · Quest 2","Array Operations",3,2,130,"ArrayOps.java",
          story(
            n("The Calculation Hall. A list of potion strengths needs analysing. Total and average — the two most common array operations in real software."),
            d("🧙","mentor","Master Velan","s-mentor","The classic pattern: declare a total variable at 0, loop through the array adding each element, divide at the end for the average. For decimal average from integer data, cast before dividing."),
            e("Worked Example — Sum & Average","<span class='type'>int</span>[] nums = {<span class='num'>5</span>, <span class='num'>10</span>, <span class='num'>15</span>};\n<span class='type'>int</span> total = <span class='num'>0</span>;\n\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < nums.length; i++) {\n    total += nums[i];\n}\n\n<span class='kw'>System</span>.out.println(total);                      <span class='cm'>// 30</span>\n<span class='kw'>System</span>.out.println((<span class='type'>double</span>) total / nums.length); <span class='cm'>// 10.0</span>"),
            d("⚗️","npc","Head Alchemist Voryn","s-npc","Seven readings: 12, 7, 19, 4, 28, 11, 5. I need the sum and the average to one decimal place. Use printf for the decimal.")
          ),
          "<code>int[] readings = {12, 7, 19, 4, 28, 11, 5};</code> declared. Calculate and print:<br>• Sum: <strong>86</strong><br>• Average to 1dp: <strong>12.3</strong>",
          "Sum loop then: <code>System.out.printf(\"%.1f%n\", (double) total / readings.length);</code>",
          "int[] readings = {12, 7, 19, 4, 28, 11, 5};\n\n// Calculate and print the sum\n// Calculate and print the average to 1 decimal place\n",
          "\"86 total, 12.3 average. Consistent batch,\" Voryn confirms.",
          tests(test("Sum = 86","null","86"),test("Average = 12.3","null","12.3")));

        q("ch3-q3","The Scroll of Lists","Chapter III · Quest 3","ArrayList",3,3,130,"ScrollOfLists.java",
          story(
            n("The Library Annexe. A different kind of collection — one that grows and shrinks. Arrays are fixed in size at creation. But sometimes you don't know how many items you'll end up with."),
            d("🧙","mentor","Master Velan","s-mentor","An <em>ArrayList</em> is resizable. Import it from java.util. Declare with a type in angle brackets. Add with <em>.add()</em>. Access with <em>.get(index)</em>. Size with <em>.size()</em>. Remove with <em>.remove()</em>."),
            e("Worked Example — ArrayList","<span class='kw'>import</span> java.util.ArrayList;\n\n<span class='type'>ArrayList</span>&lt;<span class='type'>String</span>&gt; names = <span class='kw'>new</span> <span class='type'>ArrayList</span>&lt;&gt;();\nnames.add(<span class='str'>\"Aldric\"</span>);\nnames.add(<span class='str'>\"Zara\"</span>);\n\n<span class='kw'>System</span>.out.println(names.size());    <span class='cm'>// 2</span>\n<span class='kw'>System</span>.out.println(names.get(<span class='num'>0</span>));   <span class='cm'>// Aldric</span>"),
            d("📚","npc","Librarian Fen","s-npc","The graduation register starts empty. Three wizards qualify today: Aldric, Zara, Finn. Add them, print the count, then print each name.")
          ),
          "Create <code>ArrayList&lt;String&gt; graduates</code>. Add Aldric, Zara, Finn. Print size (<strong>3</strong>), then each name.",
          "Declare, .add() three times, print .size(), loop with .get(i)",
          "import java.util.ArrayList;\n\n// Create ArrayList, add three names, print size then each name\n\n",
          "Three names on the graduation scroll. Fen stamps each.",
          tests(test("Size=3","null","3"),test("Aldric","null","Aldric"),test("Finn","null","Finn")));

        q("ch3-q4","The Spell Codex","Chapter III · Quest 4","Methods",3,4,140,"SpellCodex.java",
          story(
            n("The Grand Codex Hall. Senior wizards write spells as reusable incantations — defined once, called whenever needed. This is the essence of methods."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>method</em> is a named block of code defined outside main. Mark it <em>static</em> when in the same class as main. Call it by name. Methods can accept <em>parameters</em> (inputs) and <em>return</em> a value to the caller."),
            e("Worked Example — Methods","<span class='cm'>// void: returns nothing</span>\n<span class='kw'>static void</span> greet(<span class='type'>String</span> name) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Hello \"</span> + name);\n}\n\n<span class='cm'>// int: returns a value</span>\n<span class='kw'>static int</span> add(<span class='type'>int</span> a, <span class='type'>int</span> b) {\n    <span class='kw'>return</span> a + b;\n}\n\n<span class='cm'>// Calling from main</span>\ngreet(<span class='str'>\"Kael\"</span>);                       <span class='cm'>// Hello Kael</span>\n<span class='kw'>System</span>.out.println(add(<span class='num'>3</span>, <span class='num'>4</span>));   <span class='cm'>// 7</span>"),
            d("🧝","npc","Enchantress Lyra","s-npc","The Codex needs two entries: a greeting method and a calculation method. Define them above main, call them inside main.")
          ),
          "Write method <strong>greetWizard(String name, int level)</strong> printing: <strong>\"Welcome, [name]! Level [level].\"</strong><br>Write method <strong>add(int a, int b)</strong> returning the sum.<br><br>In main: greetWizard(\"Kael\", 7) and print add(12, 8).",
          "Define methods with <code>static</code> keyword. Return: <code>return a + b;</code>",
          "public class SpellCodex {\n\n    // Write greetWizard here\n\n    // Write add here\n\n    public static void main(String[] args) {\n        // Call both methods\n\n    }\n}\n",
          "Both entries appear. Lyra nods: \"Clean parameters, correct return type. You understand reusability.\"",
          tests(test("Greeting","null","Welcome, Kael! Level 7."),test("Sum=20","null","20")));

        q("ch3-q5","The Recursion Obelisk","Chapter III · Quest 5","Recursion",3,5,160,"RecursionObelisk.java",
          story(
            n("The Obelisk of Endless Reflection. A monolith that shows its own reflection — which shows another — which shows another. At some point it must stop. This is recursion."),
            d("🧙","mentor","Master Velan","s-mentor","A recursive method calls itself. Without a stopping condition it runs forever — a stack overflow. The <em>base case</em> is the condition that makes it stop. Every recursive method must have one that is always eventually reached."),
            e("Worked Example — Recursion","<span class='kw'>static int</span> factorial(<span class='type'>int</span> n) {\n    <span class='kw'>if</span> (n <= <span class='num'>1</span>) <span class='kw'>return</span> <span class='num'>1</span>;       <span class='cm'>// base case</span>\n    <span class='kw'>return</span> n * factorial(n - <span class='num'>1</span>);  <span class='cm'>// recursive case</span>\n}\n<span class='cm'>// factorial(4) = 4 * 3 * 2 * 1 = 24</span>"),
            d("🪞","npc","The Obelisk","s-npc","COMPUTE MY REFLECTION COUNT. IF N IS 1 OR LESS, RETURN 1. OTHERWISE RETURN N TIMES THE REFLECTION OF N MINUS 1.")
          ),
          "Write recursive method <strong>factorial(int n)</strong> returning n!<br>Print: <code>factorial(5)</code> → <strong>120</strong> and <code>factorial(1)</code> → <strong>1</strong>",
          "Base case: <code>if (n <= 1) return 1;</code> Recursive: <code>return n * factorial(n - 1);</code>",
          "public class RecursionObelisk {\n\n    static int factorial(int n) {\n        // Base case: n <= 1, return 1\n        // Recursive: return n * factorial(n - 1)\n\n    }\n\n    public static void main(String[] args) {\n        System.out.println(factorial(5));\n        System.out.println(factorial(1));\n    }\n}\n",
          "The Obelisk: \"120. Correct. The infinite made finite.\"",
          tests(test("factorial(5)=120","null","120"),test("factorial(1)=1","null","1")));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CHAPTER IV — THE GRAND GRIMOIRE (OOP)
    // ══════════════════════════════════════════════════════════════════════════
    private void seedChapterFour() {

        q("ch4-q1","The Golem Foundry","Chapter IV · Quest 1","Classes & Objects",4,1,150,"GolemFoundry.java",
          story(
            n("The Golem Foundry. Wizards here don't just write spells — they create beings. Every golem starts as a blueprint, then is brought to life as a unique individual. This is the heart of object-oriented programming."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>class</em> is the blueprint. An <em>object</em> is one specific instance. You can create many objects from one class, each with different field values. Think of 'Wizard' as the class; Aldric is one specific Wizard object."),
            e("Worked Example — Class & Object","<span class='kw'>class</span> <span class='type'>Wizard</span> {\n    <span class='type'>String</span> name;\n    <span class='type'>int</span>    level;\n\n    <span class='type'>Wizard</span>(<span class='type'>String</span> name, <span class='type'>int</span> level) {\n        <span class='kw'>this</span>.name  = name;   <span class='cm'>// 'this' = the field</span>\n        <span class='kw'>this</span>.level = level;\n    }\n\n    <span class='kw'>void</span> describe() {\n        <span class='kw'>System</span>.out.println(name + <span class='str'>\" level \"</span> + level);\n    }\n}\n\n<span class='cm'>// Creating objects</span>\n<span class='type'>Wizard</span> w = <span class='kw'>new</span> <span class='type'>Wizard</span>(<span class='str'>\"Aldric\"</span>, <span class='num'>5</span>);\nw.describe();  <span class='cm'>// Aldric level 5</span>"),
            d("⚙️","npc","Foundry Golem G-1","s-npc","BLUEPRINT REQUIRED. FIELDS: NAME AND LEVEL. CONSTRUCTOR ACCEPTS BOTH. METHOD: PRINT SELF-DESCRIPTION.")
          ),
          "Create class <strong>Wizard</strong> with <code>name</code>, <code>level</code>, constructor, and <code>describe()</code> printing: <strong>\"Wizard [name] is level [level].\"</strong><br><br>Create two: <code>Wizard(\"Aldric\", 5)</code> and <code>Wizard(\"Zara\", 9)</code> and call describe on each.",
          "Define class above the main class. Use <code>this.name = name;</code> in constructor.",
          "// Define Wizard class here\n\npublic class GolemFoundry {\n    public static void main(String[] args) {\n        // Create two Wizard objects and call describe()\n\n    }\n}\n",
          "G-1 booms: \"BLUEPRINTS ACCEPTED. OBJECTS INSTANTIATED. WELL DONE.\"",
          tests(test("Aldric","null","Wizard Aldric is level 5."),test("Zara","null","Wizard Zara is level 9.")));

        q("ch4-q2","The Seal of Encapsulation","Chapter IV · Quest 2","Encapsulation",4,2,160,"Encapsulation.java",
          story(
            n("The Vault of Secrets. Not all knowledge should be freely accessible. Powerful wizards protect their inner workings — exposing only what others need to interact with."),
            d("🧙","mentor","Master Velan","s-mentor","Make a field <em>private</em> and only code inside the class can access it directly. Provide a <em>getter</em> to read it and a <em>setter</em> to write it. The setter can validate the new value before accepting it — this is the power of encapsulation."),
            e("Worked Example — Encapsulation","<span class='kw'>class</span> <span class='type'>Wizard</span> {\n    <span class='kw'>private</span> <span class='type'>int</span> level;\n\n    <span class='kw'>public int</span> getLevel() {\n        <span class='kw'>return</span> level;\n    }\n\n    <span class='kw'>public void</span> setLevel(<span class='type'>int</span> level) {\n        <span class='kw'>if</span> (level > <span class='num'>0</span>) <span class='kw'>this</span>.level = level;\n        <span class='cm'>// invalid values are silently ignored</span>\n    }\n}"),
            d("🔒","npc","Vault Keeper Sable","s-npc","A wizard's level must never go negative. The setter must enforce this. I'll test that invalid values are rejected without error — they're simply ignored.")
          ),
          "Create <strong>Wizard</strong> with <em>private</em> <code>name</code> and <code>level</code>. Add <code>getName()</code>, <code>getLevel()</code>, <code>setLevel(int)</code> (ignores negatives).<br><br>In main: create Wizard(\"Aldric\", 5), call setLevel(-1), print name then level. Output: <strong>Aldric</strong> then <strong>5</strong>.",
          "Setter: <code>if (level > 0) this.level = level;</code> — negative values ignored.",
          "public class Encapsulation {\n    static class Wizard {\n        private String name;\n        private int level;\n        Wizard(String name, int level) { this.name = name; this.level = level; }\n        // Add getName(), getLevel(), setLevel()\n\n    }\n    public static void main(String[] args) {\n        Wizard w = new Wizard(\"Aldric\", 5);\n        w.setLevel(-1);\n        System.out.println(w.getName());\n        System.out.println(w.getLevel());\n    }\n}\n",
          "\"Properly guarded,\" Sable says. \"The field cannot be corrupted from outside.\"",
          tests(test("Name=Aldric","null","Aldric"),test("Level=5","null","5")));

        q("ch4-q3","The Order of Lineage","Chapter IV · Quest 3","Inheritance",4,3,170,"Inheritance.java",
          story(
            n("The Hall of Lineage. Every wizard order shares common ground — name, level, the ability to describe themselves. Writing the same code twice is a cardinal sin."),
            d("🧙","mentor","Master Velan","s-mentor","<em>Inheritance</em> lets one class extend another, gaining all its fields and methods. Use <em>extends</em>. In the child constructor, call <em>super()</em> first to run the parent constructor. Use <em>@Override</em> to replace a parent method with your own version."),
            e("Worked Example — Inheritance","<span class='kw'>class</span> <span class='type'>Animal</span> {\n    <span class='type'>String</span> name;\n    <span class='type'>Animal</span>(<span class='type'>String</span> n) { name = n; }\n    <span class='kw'>void</span> speak() { <span class='kw'>System</span>.out.println(<span class='str'>\"...\"</span>); }\n}\n\n<span class='kw'>class</span> <span class='type'>Dog</span> <span class='kw'>extends</span> <span class='type'>Animal</span> {\n    <span class='type'>Dog</span>(<span class='type'>String</span> n) { <span class='kw'>super</span>(n); }  <span class='cm'>// calls Animal constructor</span>\n\n    @Override\n    <span class='kw'>void</span> speak() { <span class='kw'>System</span>.out.println(name + <span class='str'>\" barks\"</span>); }\n}"),
            d("🧝","npc","Enchantress Lyra","s-npc","Create a BattleMage that extends Wizard. It adds a weapon field and overrides describe to mention the weapon.")
          ),
          "Given <code>Wizard</code> base class, create <strong>BattleMage</strong> extending Wizard with <code>String weapon</code>. Override <code>describe()</code>: <strong>\"BattleMage [name] wields [weapon].\"</strong><br><br>In main: BattleMage(\"Kael\", 7, \"Flameblade\").describe() and Wizard(\"Zara\", 3).describe()",
          "Use <code>class BattleMage extends Wizard</code> with <code>super(name, level)</code> in constructor.",
          "class Wizard {\n    String name; int level;\n    Wizard(String name, int level) { this.name = name; this.level = level; }\n    void describe() { System.out.println(\"Wizard \" + name + \" is level \" + level + \".\"); }\n}\n\n// Write BattleMage here\n\npublic class Inheritance {\n    public static void main(String[] args) {\n        // Create both and call describe\n\n    }\n}\n",
          "\"The lineage is complete,\" the Hall intones.",
          tests(test("BattleMage","null","BattleMage Kael wields Flameblade."),test("Wizard","null","Wizard Zara is level 3.")));

        q("ch4-q4","The Polymorphic Mirrors","Chapter IV · Quest 4","Polymorphism",4,4,180,"Polymorphism.java",
          story(
            n("The Mirror Gallery. Each mirror holds a different type of wizard. When you call their name, each responds in their own way — even though from the outside they're all just 'wizards'."),
            d("🧙","mentor","Master Velan","s-mentor","This is <em>polymorphism</em>. A child object can be stored in a parent-type variable. When you call a method, Java looks at the actual object's type at runtime and calls the correct overridden version. This is how you write flexible code that works with any subtype."),
            e("Worked Example — Polymorphism","<span class='type'>Wizard</span>[] gallery = {\n    <span class='kw'>new</span> <span class='type'>Wizard</span>(<span class='str'>\"Zara\"</span>, <span class='num'>3</span>),\n    <span class='kw'>new</span> <span class='type'>BattleMage</span>(<span class='str'>\"Kael\"</span>, <span class='num'>7</span>, <span class='str'>\"Sword\"</span>)\n};\n\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < gallery.length; i++) {\n    gallery[i].describe();  <span class='cm'>// calls the right version</span>\n}"),
            d("🪞","npc","Mirror Keeper Illen","s-npc","Add a Healer class with a spell field. Store Wizard, BattleMage, Healer in one array. Loop and call describe — three different outputs from one loop.")
          ),
          "Add <code>Healer</code> class with <code>spell</code> field. Override describe → <strong>\"Healer [name] casts [spell].\"</strong><br><br>Store all three in <code>Wizard[]</code> array, loop calling describe. Expected:<br><strong>Wizard Zara is level 3.<br>BattleMage Kael wields Flameblade.<br>Healer Mira casts Rejuvenate.</strong>",
          "Declare <code>Wizard[] gallery = { new Wizard(...), new BattleMage(...), new Healer(...) };</code>",
          "class Wizard {\n    String name; int level;\n    Wizard(String n, int l) { name=n; level=l; }\n    void describe() { System.out.println(\"Wizard \"+name+\" is level \"+level+\".\"); }\n}\nclass BattleMage extends Wizard {\n    String weapon;\n    BattleMage(String n, int l, String w) { super(n,l); weapon=w; }\n    @Override void describe() { System.out.println(\"BattleMage \"+name+\" wields \"+weapon+\".\"); }\n}\n// Write Healer class here\n\npublic class Polymorphism {\n    public static void main(String[] args) {\n        // Create Wizard array with all three types\n\n    }\n}\n",
          "\"One call, three answers,\" Illen whispers. \"That is polymorphism.\"",
          tests(test("Wizard Zara","null","Wizard Zara is level 3."),test("BattleMage Kael","null","BattleMage Kael wields Flameblade."),test("Healer Mira","null","Healer Mira casts Rejuvenate.")));

        q("ch4-q5","The Abstract Sanctum","Chapter IV · Quest 5","Abstract Classes",4,5,200,"AbstractSanctum.java",
          story(
            n("The Sanctum of Forms. Some blueprints are too general to build directly. You can't make a generic 'Shape' — only specific shapes. These incomplete blueprints are abstract classes."),
            d("🧙","mentor","Master Velan","s-mentor","An <em>abstract class</em> uses the <em>abstract</em> keyword. It can have abstract methods — declared but not implemented, ending with a semicolon. Any concrete subclass must implement all abstract methods. You cannot create an object of an abstract class directly."),
            e("Worked Example — Abstract Class","<span class='kw'>abstract class</span> <span class='type'>Shape</span> {\n    <span class='kw'>abstract double</span> area();  <span class='cm'>// no body — subclass must provide it</span>\n}\n\n<span class='kw'>class</span> <span class='type'>Circle</span> <span class='kw'>extends</span> <span class='type'>Shape</span> {\n    <span class='type'>double</span> radius;\n    <span class='type'>Circle</span>(<span class='type'>double</span> r) { radius = r; }\n\n    @Override\n    <span class='type'>double</span> area() { <span class='kw'>return</span> Math.PI * radius * radius; }\n}"),
            d("📐","npc","Architect Tessara","s-npc","Abstract Shape with area(). Circle (radius) and Rectangle (width, height). Both extend Shape. I need the area of each printed.")
          ),
          "Create abstract <strong>Shape</strong> with <code>abstract double area()</code>. Create <strong>Circle</strong> and <strong>Rectangle</strong>.<br><br>In main print:<br>• Circle radius 5: <strong>78.5</strong><br>• Rectangle 4×6: <strong>24.0</strong>",
          "Circle area: <code>Math.PI * radius * radius</code>. Round: <code>Math.round(circle.area()*10)/10.0</code>",
          "// Write abstract Shape, Circle, and Rectangle\n\npublic class AbstractSanctum {\n    public static void main(String[] args) {\n        Shape circle = new Circle(5);\n        Shape rect = new Rectangle(4, 6);\n        System.out.println(Math.round(circle.area() * 10) / 10.0);\n        System.out.println(rect.area());\n    }\n}\n",
          "\"78.5 and 24.0. Geometry is magic made precise,\" Tessara confirms.",
          tests(test("Circle=78.5","null","78.5"),test("Rectangle=24.0","null","24.0")));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CHAPTER V — THE MASTER'S PATH (Advanced Java)
    // ══════════════════════════════════════════════════════════════════════════
    private void seedChapterFive() {

        q("ch5-q1","The Ward of Exceptions","Chapter V · Quest 1","Exception Handling",5,1,200,"ExceptionWard.java",
          story(
            n("The Ward of Exceptions. Even the best-written spells can go wrong at runtime: bad input, missing files, division by zero. Without handling these, your program crashes."),
            d("🧙","mentor","Master Velan","s-mentor","Wrap risky code in a <em>try</em> block. If an exception occurs, execution jumps to the <em>catch</em> block instead of crashing. The <em>finally</em> block always runs — whether an exception occurred or not."),
            e("Worked Example — Try/Catch/Finally","<span class='kw'>try</span> {\n    <span class='type'>int</span> result = <span class='num'>10</span> / <span class='num'>0</span>;  <span class='cm'>// throws ArithmeticException</span>\n} <span class='kw'>catch</span> (<span class='type'>ArithmeticException</span> e) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Caught: \"</span> + e.getMessage());\n} <span class='kw'>finally</span> {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Always runs\"</span>);\n}\n<span class='cm'>// Caught: / by zero</span>\n<span class='cm'>// Always runs</span>"),
            d("🏥","npc","Ward Keeper Nell","s-npc","Try to divide 10 by 0. Catch the ArithmeticException. Print the message. The finally block prints 'Ward stable.' no matter what.")
          ),
          "Write try/catch/finally:<br>• try: compute 10/0<br>• catch: print <strong>\"Caught: \"</strong> + message<br>• finally: print <strong>\"Ward stable.\"</strong>",
          "Use <code>catch (ArithmeticException e) { System.out.println(\"Caught: \" + e.getMessage()); }</code>",
          "// Write your try/catch/finally block here\n\n",
          "\"Exception handled. No crash. This is professional code,\" Nell marks the clipboard.",
          tests(test("Caught","null","Caught: / by zero"),test("Finally","null","Ward stable.")));

        q("ch5-q2","The Generics Forge","Chapter V · Quest 2","Generics",5,2,210,"GenericsForge.java",
          story(
            n("The Forge of Forms. You've used ArrayList<String> — the type in angle brackets is a generic parameter. Now you'll write your own generic class that works with any type."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>generic class</em> uses a type parameter — a placeholder, usually written <em>T</em>. When someone creates an instance, they specify what T actually is. This is how all of Java's collections work."),
            e("Worked Example — Generics","<span class='kw'>class</span> <span class='type'>Box</span>&lt;<span class='type'>T</span>&gt; {\n    <span class='kw'>private</span> <span class='type'>T</span> item;\n    <span class='kw'>void</span> put(<span class='type'>T</span> item) { <span class='kw'>this</span>.item = item; }\n    <span class='type'>T</span> get() { <span class='kw'>return</span> item; }\n}\n\n<span class='type'>Box</span>&lt;<span class='type'>String</span>&gt; b = <span class='kw'>new</span> <span class='type'>Box</span>&lt;&gt;();\nb.put(<span class='str'>\"Hello\"</span>);\n<span class='kw'>System</span>.out.println(b.get());  <span class='cm'>// Hello</span>"),
            d("⚒️","npc","Forgemaster Brenn","s-npc","Build a generic Box class. Use it with a String and an Integer. Two different types, one class.")
          ),
          "Write <strong>Box&lt;T&gt;</strong> with <code>put(T)</code> and <code>T get()</code>.<br>In main: Box&lt;String&gt; storing <strong>\"Arcane Scroll\"</strong>, Box&lt;Integer&gt; storing <strong>42</strong>, print both.",
          "Declare <code>class Box&lt;T&gt; { private T item; public void put(T item) { this.item = item; } public T get() { return item; } }</code>",
          "// Write your generic Box<T> class here\n\npublic class GenericsForge {\n    public static void main(String[] args) {\n        Box<String> stringBox = new Box<>();\n        stringBox.put(\"Arcane Scroll\");\n        System.out.println(stringBox.get());\n\n        Box<Integer> intBox = new Box<>();\n        intBox.put(42);\n        System.out.println(intBox.get());\n    }\n}\n",
          "\"Type-safe containers,\" Brenn tests each. \"Professional craft.\"",
          tests(test("String box","null","Arcane Scroll"),test("Integer box","null","42")));

        q("ch5-q3","The Lambda Loom","Chapter V · Quest 3","Lambdas",5,3,220,"LambdaLoom.java",
          story(
            n("The Lambda Loom. Java 8 introduced treating functions as values — passing them around like variables. This is the foundation of modern Java style."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>lambda</em> is a short anonymous function. Syntax: <em>(params) -&gt; body</em>. Works with functional interfaces — interfaces with exactly one abstract method. Java provides: Runnable, Predicate&lt;T&gt;, Function&lt;T,R&gt;, Consumer&lt;T&gt;, Supplier&lt;T&gt;."),
            e("Worked Example — Lambdas","<span class='kw'>import</span> java.util.function.*;\n\n<span class='cm'>// Runnable: no input, no return</span>\n<span class='type'>Runnable</span> r = () -> <span class='kw'>System</span>.out.println(<span class='str'>\"Run!\"</span>);\nr.run();\n\n<span class='cm'>// Predicate: takes T, returns boolean</span>\n<span class='type'>Predicate</span>&lt;<span class='type'>Integer</span>&gt; isEven = n -> n % <span class='num'>2</span> == <span class='num'>0</span>;\n<span class='kw'>System</span>.out.println(isEven.test(<span class='num'>4</span>));  <span class='cm'>// true</span>\n\n<span class='cm'>// Function: takes T, returns R</span>\n<span class='type'>Function</span>&lt;<span class='type'>Integer</span>,<span class='type'>Integer</span>&gt; dbl = n -> n * <span class='num'>2</span>;\n<span class='kw'>System</span>.out.println(dbl.apply(<span class='num'>7</span>));    <span class='cm'>// 14</span>"),
            d("🧶","npc","Weaver Saya","s-npc","Three lambdas: a Runnable, a Predicate, and a Function. Show me functions treated as values.")
          ),
          "Create:<br>1. <code>Runnable</code> printing <strong>\"Loom activated.\"</strong> — call r.run()<br>2. <code>Predicate&lt;Integer&gt;</code> checking even — test(4): <strong>true</strong><br>3. <code>Function&lt;Integer,Integer&gt;</code> doubling — apply(7): <strong>14</strong>",
          "Import <code>java.util.function.Predicate</code> and <code>java.util.function.Function</code>",
          "import java.util.function.Predicate;\nimport java.util.function.Function;\n\npublic class LambdaLoom {\n    public static void main(String[] args) {\n        // 1. Runnable lambda\n\n        // 2. Predicate lambda — test with 4\n\n        // 3. Function lambda — apply to 7\n\n    }\n}\n",
          "\"Functions as values,\" Saya says. \"The loom accepts your craft.\"",
          tests(test("Loom activated","null","Loom activated."),test("Predicate true","null","true"),test("Doubler 14","null","14")));

        q("ch5-q4","The Stream Conduit","Chapter V · Quest 4","Streams",5,4,230,"StreamConduit.java",
          story(
            n("The Stream Conduit. A river of data that can be filtered, transformed, and collected as it flows — without explicit loops."),
            d("🧙","mentor","Master Velan","s-mentor","Get a stream with <em>list.stream()</em>. Chain intermediate operations — <em>filter()</em>, <em>map()</em>. Terminate with <em>forEach()</em> or <em>count()</em>. Nothing happens until the terminal operation. This is called lazy evaluation."),
            e("Worked Example — Streams","<span class='type'>List</span>&lt;<span class='type'>Integer</span>&gt; nums = <span class='type'>Arrays</span>.asList(<span class='num'>1</span>,<span class='num'>2</span>,<span class='num'>3</span>,<span class='num'>4</span>,<span class='num'>5</span>);\n\n<span class='cm'>// Filter evens, double them, print</span>\nnums.stream()\n    .filter(n -> n % <span class='num'>2</span> == <span class='num'>0</span>)\n    .map(n -> n * <span class='num'>2</span>)\n    .forEach(<span class='kw'>System</span>.out::println);\n<span class='cm'>// prints: 4, 8</span>"),
            d("🌊","npc","Conduit Keeper Vael","s-npc","Take numbers 1 to 8. Filter evens. Double each. Print. Then count how many evens there were.")
          ),
          "Given <code>List&lt;Integer&gt; numbers = Arrays.asList(1,2,3,4,5,6,7,8);</code>:<br>1. Filter evens, double, print each → <strong>4 8 12 16</strong><br>2. Count and print evens → <strong>4</strong>",
          "Chain: <code>.filter(n -> n % 2 == 0).map(n -> n * 2).forEach(System.out::println)</code>",
          "import java.util.Arrays;\nimport java.util.List;\n\npublic class StreamConduit {\n    public static void main(String[] args) {\n        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);\n\n        // 1. Filter evens, double, print each\n\n        // 2. Count and print even numbers\n\n    }\n}\n",
          "\"Declarative, efficient, elegant,\" Vael reads. \"You have mastered the stream.\"",
          tests(test("First doubled even","null","4"),test("Second doubled even","null","8"),test("Count","null","4")));

        q("ch5-q5","The Pattern Archive","Chapter V · Quest 5","Design Patterns",5,5,250,"PatternArchive.java",
          story(
            n("The Pattern Archive. These are proven solutions to recurring problems — the vocabulary of professional developers."),
            d("🧙","mentor","Master Velan","s-mentor","The <em>Singleton</em> ensures only one instance of a class ever exists: private constructor, private static instance, public static getInstance(). Used for loggers, config managers, DB connections."),
            e("Worked Example — Singleton","<span class='kw'>class</span> <span class='type'>Registry</span> {\n    <span class='kw'>private static</span> <span class='type'>Registry</span> instance;\n    <span class='kw'>private</span> <span class='type'>Registry</span>() {}\n\n    <span class='kw'>public static</span> <span class='type'>Registry</span> getInstance() {\n        <span class='kw'>if</span> (instance == <span class='kw'>null</span>)\n            instance = <span class='kw'>new</span> <span class='type'>Registry</span>();\n        <span class='kw'>return</span> instance;\n    }\n}"),
            d("📜","npc","Archivist Crey","s-npc","Implement the Singleton Registry. Call getInstance() twice — they must be the same object. Then build a Wizard using the Builder pattern and print its details.")
          ),
          "Implement Singleton <strong>Registry</strong>. Call getInstance() twice → print: <strong>\"Same instance: true\"</strong><br><br>Implement <strong>Wizard</strong> with inner <strong>Builder</strong> (chainable name(), level(), build()). Build and print: <strong>\"Aldric level 7\"</strong>",
          "Singleton test: <code>System.out.println(\"Same instance: \" + (r1 == r2));</code>",
          "// Implement Singleton Registry\n\n// Implement Wizard with inner Builder class\n\npublic class PatternArchive {\n    public static void main(String[] args) {\n        Registry r1 = Registry.getInstance();\n        Registry r2 = Registry.getInstance();\n        System.out.println(\"Same instance: \" + (r1 == r2));\n\n        Wizard w = new Wizard.Builder().name(\"Aldric\").level(7).build();\n        System.out.println(w.name + \" level \" + w.level);\n    }\n}\n",
          "\"Singleton confirmed. Builder confirmed,\" Crey reads. \"You understand professional architecture.\"",
          tests(test("Same instance","null","Same instance: true"),test("Builder output","null","Aldric level 7")));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // BOSSES
    // ══════════════════════════════════════════════════════════════════════════
    private void seedBosses() {
        log.info("Seeding bosses...");

        saveBoss("ch1-boss","The Golem of Types","🗿",1,200,
          "The Golem stirs from its pedestal, stone grinding stone. Its eyes glow cold. \"You claim to know types and values. Demonstrate it.\"",
          "[{\"id\":\"c1q1\",\"type\":\"multiple_choice\",\"question\":\"Which type stores text like \\\"Aldric\\\"?\",\"options\":[\"int\",\"boolean\",\"String\",\"double\"],\"correct\":\"String\",\"explanation\":\"String holds text — sequences of characters. Always wrap String values in double quotes.\"},{\"id\":\"c1q2\",\"type\":\"be_the_compiler\",\"question\":\"What does this print?\\n\\nint x = 10;\\nx += 5;\\nx *= 2;\\nSystem.out.println(x);\",\"options\":[\"10\",\"15\",\"25\",\"30\"],\"correct\":\"30\",\"explanation\":\"x starts at 10. +=5 gives 15. *=2 gives 30.\"},{\"id\":\"c1q3\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to declare a decimal variable:\",\"code\":\"______ mana = 99.5;\",\"correct\":\"double\",\"explanation\":\"double holds decimal numbers.\"},{\"id\":\"c1q4\",\"type\":\"be_the_compiler\",\"question\":\"What does this print?\\n\\nint a = 7;\\nint b = 2;\\nSystem.out.println(a / b);\",\"options\":[\"3.5\",\"3\",\"4\",\"Error\"],\"correct\":\"3\",\"explanation\":\"Integer division drops the decimal. 7/2=3.\"},{\"id\":\"c1q5\",\"type\":\"multiple_choice\",\"question\":\"What does System.out.println(\\\"Hi\\\" + \\\" \\\" + \\\"there\\\") print?\",\"options\":[\"Hi there\",\"Hi + there\",\"Error\",\"Hithere\"],\"correct\":\"Hi there\",\"explanation\":\"The + operator concatenates Strings.\"}]");

        saveBoss("ch2-boss","The Labyrinth Warden","🐺",2,200,
          "The Warden blocks the maze exit. \"Only those who truly understand control flow may leave.\"",
          "[{\"id\":\"c2q1\",\"type\":\"be_the_compiler\",\"question\":\"What prints?\\n\\nint mana = 100;\\nif (mana > 100) {\\n    System.out.println(\\\"Overcharged\\\");\\n} else if (mana == 100) {\\n    System.out.println(\\\"Full\\\");\\n} else {\\n    System.out.println(\\\"Low\\\");\\n}\",\"options\":[\"Overcharged\",\"Full\",\"Low\",\"Nothing\"],\"correct\":\"Full\",\"explanation\":\"100>100 is false. 100==100 is true. Full prints.\"},{\"id\":\"c2q2\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to loop exactly 4 times:\",\"code\":\"for (int i = 0; i __ 4; i++) { }\",\"correct\":\"<\",\"explanation\":\"i<4 runs for 0,1,2,3 — exactly 4 times.\"},{\"id\":\"c2q3\",\"type\":\"be_the_compiler\",\"question\":\"What is printed?\\n\\nint total = 0;\\nfor (int i = 1; i <= 5; i++) {\\n    total += i;\\n}\\nSystem.out.println(total);\",\"options\":[\"5\",\"10\",\"15\",\"20\"],\"correct\":\"15\",\"explanation\":\"1+2+3+4+5=15.\"},{\"id\":\"c2q4\",\"type\":\"multiple_choice\",\"question\":\"A while loop that never changes its condition is called:\",\"options\":[\"A fast loop\",\"An infinite loop\",\"A break loop\",\"A for loop\"],\"correct\":\"An infinite loop\",\"explanation\":\"If the condition never becomes false the loop runs forever.\"},{\"id\":\"c2q5\",\"type\":\"be_the_compiler\",\"question\":\"How many times does 'Go!' print?\\n\\nfor (int i = 10; i > 0; i -= 3) {\\n    System.out.println(\\\"Go!\\\");\\n}\",\"options\":[\"3\",\"4\",\"10\",\"infinite\"],\"correct\":\"4\",\"explanation\":\"i: 10→7→4→1→stop. Runs 4 times.\"}]");

        saveBoss("ch3-boss","The Vault Keeper","🔐",3,250,
          "The Vault Keeper rises from parchment and shadow. \"Prove you understand the discipline of structure.\"",
          "[{\"id\":\"c3q1\",\"type\":\"be_the_compiler\",\"question\":\"What prints?\\n\\nString[] s = {\\\"Fire\\\", \\\"Ice\\\", \\\"Wind\\\"};\\nSystem.out.println(s[1]);\",\"options\":[\"Fire\",\"Ice\",\"Wind\",\"Error\"],\"correct\":\"Ice\",\"explanation\":\"Indices start at 0. s[1] is Ice.\"},{\"id\":\"c3q2\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to get the array length:\",\"code\":\"int[] nums = {1,2,3};\\nSystem.out.println(nums.________);\",\"correct\":\"length\",\"explanation\":\".length returns the number of elements.\"},{\"id\":\"c3q3\",\"type\":\"be_the_compiler\",\"question\":\"What does factorial(4) return?\\n\\nstatic int factorial(int n) {\\n    if (n<=1) return 1;\\n    return n * factorial(n-1);\\n}\",\"options\":[\"4\",\"10\",\"24\",\"Error\"],\"correct\":\"24\",\"explanation\":\"4×3×2×1=24.\"},{\"id\":\"c3q4\",\"type\":\"multiple_choice\",\"question\":\"Key difference between array and ArrayList?\",\"options\":[\"Arrays are faster\",\"Arrays are fixed size; ArrayList resizes\",\"ArrayList is for ints only\",\"No difference\"],\"correct\":\"Arrays are fixed size; ArrayList resizes\",\"explanation\":\"Arrays have fixed size at creation. ArrayList grows dynamically.\"},{\"id\":\"c3q5\",\"type\":\"fill_blank\",\"question\":\"Fill blank to return the sum:\",\"code\":\"static int add(int a, int b) {\\n    ______ a + b;\\n}\",\"correct\":\"return\",\"explanation\":\"The return keyword sends a value back to the caller.\"}]");

        saveBoss("ch4-boss","The Ancient Dragon","🐉",4,300,
          "The Ancient Dragon uncoils from the highest spire. \"Object-oriented mastery is claimed easily. Demonstrate it.\"",
          "[{\"id\":\"c4q1\",\"type\":\"be_the_compiler\",\"question\":\"What prints?\\n\\nclass Animal {\\n    void speak() { System.out.println(\\\"sound\\\"); }\\n}\\nclass Dog extends Animal {\\n    @Override void speak() { System.out.println(\\\"bark\\\"); }\\n}\\nAnimal a = new Dog();\\na.speak();\",\"options\":[\"sound\",\"bark\",\"Error\",\"Nothing\"],\"correct\":\"bark\",\"explanation\":\"Polymorphism: Java uses the actual object type (Dog) at runtime.\"},{\"id\":\"c4q2\",\"type\":\"multiple_choice\",\"question\":\"What calls the parent constructor from a child?\",\"options\":[\"parent()\",\"base()\",\"super()\",\"this()\"],\"correct\":\"super()\",\"explanation\":\"super() calls the parent constructor. Must be first in child constructor.\"},{\"id\":\"c4q3\",\"type\":\"fill_blank\",\"question\":\"Fill the blank:\",\"code\":\"class BattleMage ______ Wizard { }\",\"correct\":\"extends\",\"explanation\":\"extends establishes inheritance.\"},{\"id\":\"c4q4\",\"type\":\"multiple_choice\",\"question\":\"What makes a field 'private'?\",\"options\":[\"It deletes the field\",\"Only code inside the class can access it directly\",\"It becomes read-only\",\"It speeds up access\"],\"correct\":\"Only code inside the class can access it directly\",\"explanation\":\"private restricts direct access. External code must use getters and setters.\"},{\"id\":\"c4q5\",\"type\":\"multiple_choice\",\"question\":\"An abstract class differs from an interface because:\",\"options\":[\"Abstract classes can have concrete methods\",\"Interfaces are faster\",\"Abstract classes can only be used once\",\"No difference\"],\"correct\":\"Abstract classes can have concrete methods\",\"explanation\":\"Abstract classes can mix abstract and concrete methods, and have constructors.\"}]");

        saveBoss("ch5-boss","The Archmage","⚡",5,400,
          "The Archmage regards you with ancient eyes. \"You have walked from Hello World to design patterns. One final examination before the title is yours.\"",
          "[{\"id\":\"c5q1\",\"type\":\"be_the_compiler\",\"question\":\"What prints?\\n\\ntry {\\n    int x = 10 / 0;\\n} catch (ArithmeticException e) {\\n    System.out.println(\\\"caught\\\");\\n} finally {\\n    System.out.println(\\\"done\\\");\\n}\",\"options\":[\"caught\",\"done\",\"caught then done\",\"Error\"],\"correct\":\"caught then done\",\"explanation\":\"The catch block runs, then finally always runs.\"},{\"id\":\"c5q2\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to create a lambda doubler:\",\"code\":\"Function<Integer,Integer> doubler = n __ n * 2;\",\"correct\":\"->\",\"explanation\":\"-> is the lambda arrow operator.\"},{\"id\":\"c5q3\",\"type\":\"multiple_choice\",\"question\":\"What does stream.filter(n -> n > 5) do?\",\"options\":[\"Deletes elements > 5\",\"Keeps only elements > 5\",\"Counts elements > 5\",\"Sorts elements\"],\"correct\":\"Keeps only elements > 5\",\"explanation\":\"filter() keeps elements where the predicate returns true.\"},{\"id\":\"c5q4\",\"type\":\"multiple_choice\",\"question\":\"The Singleton pattern ensures:\",\"options\":[\"Fast creation\",\"Only one instance exists\",\"Immutability\",\"Thread safety\"],\"correct\":\"Only one instance exists\",\"explanation\":\"Singleton restricts instantiation to one object.\"},{\"id\":\"c5q5\",\"type\":\"fill_blank\",\"question\":\"Fill the blank for a generic class:\",\"code\":\"class Box<__> { __ item; }\",\"correct\":\"T\",\"explanation\":\"T is the conventional name for a type parameter.\"}]");

        saveBoss("ch6-boss","The Senior Developer","👨\u200d💻",6,400,
          "A senior developer in a tech company t-shirt leans back in his chair. \"Walk me through your Task Manager. What would you change to make it production-ready?\"",
          "[{\"id\":\"c6q1\",\"type\":\"multiple_choice\",\"question\":\"Your Task class has public fields. What should they be in production code?\",\"options\":[\"Still public for simplicity\",\"Private with getters and setters\",\"Static\",\"Final\"],\"correct\":\"Private with getters and setters\",\"explanation\":\"Encapsulation. Private fields protect the data. Getters/setters control access and allow validation.\"},{\"id\":\"c6q2\",\"type\":\"fill_blank\",\"question\":\"To override the default string representation of an object, you override:\",\"code\":\"@Override\\npublic ______ toString() { return ...; }\",\"correct\":\"String\",\"explanation\":\"toString() returns a String. Java calls it automatically when you println an object.\"},{\"id\":\"c6q3\",\"type\":\"be_the_compiler\",\"question\":\"What does this print?\\n\\nArrayList<String> list = new ArrayList<>();\\nlist.add(\\\"a\\\");\\nlist.add(\\\"b\\\");\\nlist.add(\\\"c\\\");\\nSystem.out.println(list.size());\",\"options\":[\"0\",\"2\",\"3\",\"Error\"],\"correct\":\"3\",\"explanation\":\"Three elements were added. size() returns 3.\"},{\"id\":\"c6q4\",\"type\":\"multiple_choice\",\"question\":\"The pattern of separating data (Task) from logic (TaskManager) is called:\",\"options\":[\"Singleton\",\"Separation of concerns\",\"Polymorphism\",\"Recursion\"],\"correct\":\"Separation of concerns\",\"explanation\":\"Keeping data models separate from business logic makes code easier to test and maintain.\"},{\"id\":\"c6q5\",\"type\":\"multiple_choice\",\"question\":\"In a real app, where would tasks typically be stored between sessions?\",\"options\":[\"In a variable\",\"In an ArrayList\",\"In a database or file\",\"In the CPU\"],\"correct\":\"In a database or file\",\"explanation\":\"In-memory data disappears when the program stops. Databases and files provide persistence.\"}]");

        saveBoss("ch7-boss","The Technical Interviewer","🎯",7,500,
          "The final round. A technical interviewer with a whiteboard and a stopwatch. \"Two minutes per question. No looking things up. Begin.\"",
          "[{\"id\":\"c7q1\",\"type\":\"be_the_compiler\",\"question\":\"What prints?\\n\\nfor (int i = 1; i <= 20; i++) {\\n    if (i % 3 == 0 && i % 5 == 0) System.out.println(\\\"FizzBuzz\\\");\\n    else if (i % 3 == 0) System.out.println(\\\"Fizz\\\");\\n    else if (i % 5 == 0) System.out.println(\\\"Buzz\\\");\\n    else System.out.println(i);\\n}\\n\\nWhat prints for i=15?\",\"options\":[\"15\",\"Fizz\",\"Buzz\",\"FizzBuzz\"],\"correct\":\"FizzBuzz\",\"explanation\":\"15 is divisible by both 3 and 5. FizzBuzz is checked first and matches.\"},{\"id\":\"c7q2\",\"type\":\"multiple_choice\",\"question\":\"What is the time complexity of finding max in an unsorted array of n elements?\",\"options\":[\"O(1)\",\"O(log n)\",\"O(n)\",\"O(n\\u00b2)\"],\"correct\":\"O(n)\",\"explanation\":\"You must check every element once. One pass = O(n) linear time.\"},{\"id\":\"c7q3\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to count word occurrences elegantly:\",\"code\":\"counts.put(word, counts.________(word, 0) + 1);\",\"correct\":\"getOrDefault\",\"explanation\":\"getOrDefault(key, 0) returns the existing count or 0 if not yet in the map. Add 1 and put it back.\"},{\"id\":\"c7q4\",\"type\":\"multiple_choice\",\"question\":\"A palindrome check using two pointers runs in:\",\"options\":[\"O(1)\",\"O(log n)\",\"O(n)\",\"O(n\\u00b2)\"],\"correct\":\"O(n)\",\"explanation\":\"In the worst case you check n/2 character pairs — still O(n) linear time.\"},{\"id\":\"c7q5\",\"type\":\"multiple_choice\",\"question\":\"Which data structure is best for counting word frequencies?\",\"options\":[\"Array\",\"ArrayList\",\"HashMap\",\"Stack\"],\"correct\":\"HashMap\",\"explanation\":\"HashMap provides O(1) average-case put and get. Perfect for key-value counting.\"}]");

        log.info("Seeded {} bosses.", bossRepository.count());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ══════════════════════════════════════════════════════════════════════════
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
    private String n(String t) { return "{\"type\":\"narration\",\"text\":\"" + esc(t) + "\"}"; }
    private String d(String av, String cls, String sp, String sCls, String t) {
        return "{\"type\":\"dialogue\",\"av\":\""+av+"\",\"cls\":\""+cls+"\",\"speaker\":\""+sp+"\",\"sCls\":\""+sCls+"\",\"text\":\""+esc(t)+"\"}";
    }
    private String e(String label, String code) {
        return "{\"type\":\"example\",\"speaker\":\""+esc(label)+"\",\"text\":\""+esc(code)+"\"}";
    }
    private String tests(String... ts) { return "[" + String.join(",", ts) + "]"; }
    private String test(String label, String input, String expected) {
        return "{\"label\":\""+label+"\",\"input\":"+("null".equals(input)?"null":"\""+esc(input)+"\"")
               +",\"expected\":\""+esc(expected)+"\"}";
    }
    private String esc(String s) {
        return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n").replace("\t","\\t");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CHAPTER VI — THE CAPSTONE FORGE
    // ══════════════════════════════════════════════════════════════════════════
    private void seedChapterSix() {
        if (questRepository.existsById("ch6-q1")) return;
        log.info("Seeding Chapter VI — Capstone...");

        q("ch6-q1","The Architect's Brief","Chapter VI · Quest 1","Capstone: Task Class",6,1,200,"TaskClass.java",
          story(
            n("The Master's Tower. You have been summoned not as a student but as a developer. The Academy needs a Task Manager — a real program, built from scratch. This is your capstone."),
            d("📐","npc","Architect Tessara","s-npc","By the end of this chapter you will have built a complete Java application: a Task Manager with a data model, a service layer, analysis methods, and full integration. This is what junior developers build in their first week at a real company."),
            d("🧙","mentor","Master Velan","s-mentor","We start with the data model. A Task has three fields: a title (String), a done flag (boolean), and a priority (int: 1=high, 2=medium, 3=low). Build it with encapsulation and a display method."),
            e("Task Class Design","<span class='kw'>class</span> <span class='type'>Task</span> {\n    <span class='kw'>private</span> <span class='type'>String</span>  title;\n    <span class='kw'>private</span> <span class='type'>boolean</span> done;\n    <span class='kw'>private</span> <span class='type'>int</span>     priority;\n\n    <span class='type'>Task</span>(<span class='type'>String</span> title, <span class='type'>int</span> priority) {\n        <span class='kw'>this</span>.title    = title;\n        <span class='kw'>this</span>.priority = priority;\n        <span class='kw'>this</span>.done     = <span class='kw'>false</span>;\n    }\n\n    <span class='kw'>void</span> display() {\n        <span class='type'>String</span> status = done ? <span class='str'>\"[x]\"</span> : <span class='str'>\"[ ]\"</span>;\n        <span class='kw'>System</span>.out.println(status + <span class='str'>\" [P\"</span> + priority + <span class='str'>\"] \"</span> + title);\n    }\n}"),
            n("The ternary operator — done ? \"[x]\" : \"[ ]\" — is shorthand for an if/else that returns a value. It reads: if done is true, return [x], otherwise return [ ].")
          ),
          "Build class <strong>Task</strong> with private <code>title</code>, <code>done</code>, <code>priority</code>.<br>Constructor: <code>Task(String title, int priority)</code> — done starts false.<br>Add getters for all three and a setter for done.<br>Add <code>display()</code> printing: <code>[status] [P#] title</code><br><br>In main create two tasks and display:<br><strong>[ ] [P1] Write unit tests<br>[ ] [P3] Update README</strong>",
          "Use ternary for status: <code>String status = done ? \"[x]\" : \"[ ]\";</code>",
          "// Define your Task class here\n\npublic class TaskClass {\n    public static void main(String[] args) {\n        // Create two tasks and call display() on each\n\n    }\n}\n",
          "Two tasks materialise. \"Clean encapsulation. Correct defaults. The foundation is solid,\" Tessara says.",
          tests(test("Task 1","null","[ ] [P1] Write unit tests"),test("Task 2","null","[ ] [P3] Update README")));

        q("ch6-q2","The Task Registry","Chapter VI · Quest 2","Capstone: Service Layer",6,2,220,"TaskRegistry.java",
          story(
            n("The Task model is built. Now the service layer — the TaskManager that holds a list of tasks and provides operations: add, list, and complete."),
            d("🧙","mentor","Master Velan","s-mentor","The manager class owns the collection and controls access. This is the Service pattern — data lives in one class, logic in another. This separation makes code testable and maintainable."),
            e("TaskManager Design","<span class='kw'>class</span> <span class='type'>TaskManager</span> {\n    <span class='kw'>private</span> <span class='type'>ArrayList</span>&lt;<span class='type'>Task</span>&gt; tasks = <span class='kw'>new</span> <span class='type'>ArrayList</span>&lt;&gt;();\n\n    <span class='kw'>void</span> addTask(<span class='type'>String</span> title, <span class='type'>int</span> priority) {\n        tasks.add(<span class='kw'>new</span> <span class='type'>Task</span>(title, priority));\n    }\n\n    <span class='kw'>void</span> completeTask(<span class='type'>String</span> title) {\n        <span class='kw'>for</span> (<span class='type'>int</span> i=<span class='num'>0</span>; i<tasks.size(); i++) {\n            <span class='kw'>if</span> (tasks.get(i).getTitle().equals(title))\n                tasks.get(i).setDone(<span class='kw'>true</span>);\n        }\n    }\n}"),
            d("📐","npc","Architect Tessara","s-npc","String comparison uses .equals(), not ==. The == operator checks memory address. .equals() checks content. Always use .equals() for Strings — this trips up many developers.")
          ),
          "Implement <strong>TaskManager</strong> with <code>addTask</code>, <code>listTasks</code>, <code>completeTask</code>.<br><br>In main: add \"Fix login bug\" (P1), \"Write docs\" (P3), \"Deploy to staging\" (P2). Complete \"Fix login bug\". Call listTasks().<br><br>Expected:<br><strong>[x] [P1] Fix login bug<br>[ ] [P3] Write docs<br>[ ] [P2] Deploy to staging</strong>",
          "Use <code>tasks.get(i).getTitle().equals(title)</code> — never == for Strings.",
          "import java.util.ArrayList;\n\nclass Task {\n    private String title; private boolean done; private int priority;\n    Task(String title, int priority) { this.title=title; this.priority=priority; this.done=false; }\n    void display() { System.out.println((done?\"[x]\":\"[ ]\")+\" [P\"+priority+\"] \"+title); }\n    public String getTitle() { return title; }\n    public boolean isDone() { return done; }\n    public int getPriority() { return priority; }\n    public void setDone(boolean done) { this.done = done; }\n}\n\n// Write TaskManager class here\n\npublic class TaskRegistry {\n    public static void main(String[] args) {\n        TaskManager manager = new TaskManager();\n        // Add three tasks, complete one, call listTasks()\n\n    }\n}\n",
          "The task list renders. Tessara traces the [x] mark. \"One completed, two pending. The service layer works.\"",
          tests(test("Fixed bug [x]","null","[x] [P1] Fix login bug"),test("Docs pending","null","[ ] [P3] Write docs"),test("Deploy pending","null","[ ] [P2] Deploy to staging")));

        q("ch6-q3","The Statistics Engine","Chapter VI · Quest 3","Capstone: Analysis",6,3,230,"StatsEngine.java",
          story(
            n("The TaskManager stores and completes tasks. Now add analysis: completed count, pending count, and a percentage."),
            d("🧙","mentor","Master Velan","s-mentor","Loop through the ArrayList checking isDone() and count the trues. For percentage, cast to double before dividing. Use printf for formatted decimal output."),
            e("Analysis Pattern","<span class='type'>int</span> done = <span class='num'>0</span>;\n<span class='kw'>for</span> (<span class='type'>int</span> i=<span class='num'>0</span>; i<tasks.size(); i++) {\n    <span class='kw'>if</span> (tasks.get(i).isDone()) done++;\n}\n<span class='type'>double</span> pct = (<span class='type'>double</span>) done / tasks.size() * <span class='num'>100</span>;\n<span class='kw'>System</span>.out.printf(<span class='str'>\"%.1f%%%n\"</span>, pct);"),
            d("📐","npc","Architect Tessara","s-npc","Add getCompletedCount(), getPendingCount(), and printStats() to TaskManager. printStats format: 'Completed: X | Pending: Y | Progress: Z%'")
          ),
          "Add to <strong>TaskManager</strong>: <code>getCompletedCount()</code>, <code>getPendingCount()</code>, <code>printStats()</code>.<br><br>In main: add 3 tasks, complete 1, call printStats().<br>Expected: <strong>Completed: 1 | Pending: 2 | Progress: 33.3%</strong>",
          "Printf: <code>System.out.printf(\"Completed: %d | Pending: %d | Progress: %.1f%%%n\", c, p, pct);</code>",
          "import java.util.ArrayList;\n\nclass Task {\n    private String title; private boolean done; private int priority;\n    Task(String t, int p) { title=t; priority=p; done=false; }\n    void display() { System.out.println((done?\"[x]\":\"[ ]\")+\" [P\"+priority+\"] \"+title); }\n    public String getTitle() { return title; }\n    public boolean isDone() { return done; }\n    public int getPriority() { return priority; }\n    public void setDone(boolean d) { done=d; }\n}\n\nclass TaskManager {\n    private ArrayList<Task> tasks = new ArrayList<>();\n    void addTask(String title, int priority) { tasks.add(new Task(title, priority)); }\n    void listTasks() { for(int i=0;i<tasks.size();i++) tasks.get(i).display(); }\n    void completeTask(String title) { for(int i=0;i<tasks.size();i++) if(tasks.get(i).getTitle().equals(title)) tasks.get(i).setDone(true); }\n\n    // Add getCompletedCount(), getPendingCount(), printStats() here\n\n}\n\npublic class StatsEngine {\n    public static void main(String[] args) {\n        TaskManager m = new TaskManager();\n        m.addTask(\"Task A\", 1);\n        m.addTask(\"Task B\", 2);\n        m.addTask(\"Task C\", 3);\n        m.completeTask(\"Task A\");\n        m.printStats();\n    }\n}\n",
          "\"33.3%. Accurate. This is the kind of output a project manager wants to see,\" Tessara confirms.",
          tests(test("Stats output","null","Completed: 1 | Pending: 2 | Progress: 33.3%")));

        q("ch6-q4","The Final Integration","Chapter VI · Quest 4","Capstone: Full Program",6,4,250,"TaskManagerApp.java",
          story(
            n("The final piece. Combine everything: data model, service layer, analysis methods. One complete program."),
            d("🧝","npc","Enchantress Lyra","s-npc","This is the program you'll explain in an interview. 'I built a task manager in Java — encapsulated data model, ArrayList service layer, completion statistics.' That is a real answer to 'tell me about a Java project you've built.'"),
            d("🧙","mentor","Master Velan","s-mentor","Write it cleanly. A professional program's output should explain itself — someone running it for the first time should immediately understand what it does.")
          ),
          "Complete Task Manager: add 5 tasks, complete tasks 1 and 3, call listTasks() then printStats().<br><br>Tasks: \"Implement authentication\" P1, \"Write API tests\" P1, \"Update database schema\" P2, \"Deploy to production\" P1, \"Write release notes\" P3.<br><br>Expected stats: <strong>Completed: 2 | Pending: 3 | Progress: 40.0%</strong>",
          "2/5 = 40.0% exactly. Complete by title using .equals().",
          "import java.util.ArrayList;\n\n// Paste your complete Task class here\n\n// Paste your complete TaskManager class (with all methods) here\n\npublic class TaskManagerApp {\n    public static void main(String[] args) {\n        TaskManager manager = new TaskManager();\n\n        // Add 5 tasks, complete 2, listTasks(), printStats()\n\n    }\n}\n",
          "The full system runs. Tessara turns to the class. \"A working Task Manager. Encapsulated data, ArrayList storage, analysis methods. This goes on your CV.\"",
          tests(test("Completed stat","null","Completed: 2 | Pending: 3 | Progress: 40.0%"),test("Auth complete","null","[x] [P1] Implement authentication"),test("Schema complete","null","[x] [P2] Update database schema")));

        log.info("Chapter VI seeded.");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CHAPTER VII — THE INTERVIEW GAUNTLET
    // ══════════════════════════════════════════════════════════════════════════
    private void seedChapterSeven() {
        if (questRepository.existsById("ch7-q1")) return;
        log.info("Seeding Chapter VII — Interview Gauntlet...");

        q("ch7-q1","FizzBuzz","Chapter VII · Quest 1","Interview: FizzBuzz",7,1,200,"FizzBuzz.java",
          story(
            n("The Interview Chamber. Assessor Vorn has seen ten thousand applications. She doesn't waste words."),
            d("📋","npc","Assessor Vorn","s-npc","FizzBuzz. Print numbers 1 to 20. Multiples of 3: Fizz. Multiples of 5: Buzz. Multiples of both: FizzBuzz. 30% of candidates fail this. Don't be one of them."),
            d("🧙","mentor","Master Velan","s-mentor","Check FizzBuzz first — divisible by both 3 AND 5. If you check 3 first, 15 will print Fizz and never reach FizzBuzz. Order matters."),
            e("Key Insight","<span class='cm'>// Check FizzBuzz FIRST (both conditions)</span>\n<span class='kw'>if</span> (i % <span class='num'>3</span> == <span class='num'>0</span> && i % <span class='num'>5</span> == <span class='num'>0</span>) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"FizzBuzz\"</span>);\n} <span class='kw'>else if</span> (i % <span class='num'>3</span> == <span class='num'>0</span>) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Fizz\"</span>);\n} <span class='kw'>else if</span> (i % <span class='num'>5</span> == <span class='num'>0</span>) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Buzz\"</span>);\n} <span class='kw'>else</span> {\n    <span class='kw'>System</span>.out.println(i);\n}")
          ),
          "Print numbers 1–20 with FizzBuzz rules. Check FizzBuzz (both) first.",
          "Loop 1–20. Check % 3 == 0 && % 5 == 0 first, then % 3 == 0, then % 5 == 0, else print i.",
          "// FizzBuzz — print 1 to 20 with the rules\n// Check FizzBuzz (both divisors) first!\n\n",
          "Vorn ticks her clipboard. \"Correct order. FizzBuzz at 15. You passed.\"",
          tests(test("FizzBuzz at 15","null","FizzBuzz"),test("Fizz at 3","null","Fizz"),test("Buzz at 5","null","Buzz"),test("Number 7","null","7")));

        q("ch7-q2","Reverse a String","Chapter VII · Quest 2","Interview: Strings",7,2,200,"ReverseString.java",
          story(
            n("Vorn's pen moves without pause."),
            d("📋","npc","Assessor Vorn","s-npc","Reverse a string. Write a method. reverseString takes a String, returns it reversed. No library reversal methods."),
            d("🧙","mentor","Master Velan","s-mentor","Loop from the end of the string to the start, building a result string by appending each character. charAt(i) gets the character at position i."),
            e("Reversal Pattern","<span class='kw'>static</span> <span class='type'>String</span> reverseString(<span class='type'>String</span> s) {\n    <span class='type'>String</span> result = <span class='str'>\"\"</span>;\n    <span class='kw'>for</span> (<span class='type'>int</span> i = s.length() - <span class='num'>1</span>; i >= <span class='num'>0</span>; i--) {\n        result += s.charAt(i);\n    }\n    <span class='kw'>return</span> result;\n}")
          ),
          "Write <strong>reverseString(String s)</strong> returning the reversed string.<br><br>Test:<br>• reverseString(\"hello\") → <strong>olleh</strong><br>• reverseString(\"arcane\") → <strong>enacra</strong>",
          "Loop from s.length()-1 down to 0, appending s.charAt(i) to result.",
          "public class ReverseString {\n\n    static String reverseString(String s) {\n        // Loop backwards and build the result\n\n    }\n\n    public static void main(String[] args) {\n        System.out.println(reverseString(\"hello\"));\n        System.out.println(reverseString(\"arcane\"));\n    }\n}\n",
          "\"Correct.\" Vorn ticks a second box.",
          tests(test("hello→olleh","null","olleh"),test("arcane→enacra","null","enacra")));

        q("ch7-q3","Find the Maximum","Chapter VII · Quest 3","Interview: Arrays",7,3,200,"FindMax.java",
          story(
            n("No pause."),
            d("📋","npc","Assessor Vorn","s-npc","Find the maximum in an array. Write the loop yourself — no Math.max on the whole array."),
            d("🧙","mentor","Master Velan","s-mentor","Assume the first element is the maximum. Loop through the rest. If any element is larger, update max. Return at the end. This is the standard linear scan pattern."),
            e("Linear Scan","<span class='kw'>static int</span> findMax(<span class='type'>int</span>[] arr) {\n    <span class='type'>int</span> max = arr[<span class='num'>0</span>];  <span class='cm'>// assume first is max</span>\n    <span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>1</span>; i < arr.length; i++) {\n        <span class='kw'>if</span> (arr[i] > max) max = arr[i];\n    }\n    <span class='kw'>return</span> max;\n}")
          ),
          "Write <strong>findMax(int[] arr)</strong> returning the largest value.<br><br>Test:<br>• {3,7,2,9,1} → <strong>9</strong><br>• {-5,-1,-8} → <strong>-1</strong>",
          "Start: <code>int max = arr[0];</code> Loop from i=1: <code>if (arr[i] > max) max = arr[i];</code>",
          "public class FindMax {\n\n    static int findMax(int[] arr) {\n        // Find and return the largest value\n\n    }\n\n    public static void main(String[] args) {\n        System.out.println(findMax(new int[]{3, 7, 2, 9, 1}));\n        System.out.println(findMax(new int[]{-5, -1, -8}));\n    }\n}\n",
          "\"Handles negatives. Good edge-case thinking,\" Vorn notes.",
          tests(test("Max positives","null","9"),test("Max negatives","null","-1")));

        q("ch7-q4","Is a Palindrome","Chapter VII · Quest 4","Interview: String Logic",7,4,210,"Palindrome.java",
          story(
            n("Another card."),
            d("📋","npc","Assessor Vorn","s-npc","Palindrome check. Same forwards and backwards. 'racecar' is one. 'hello' isn't. Write the method."),
            d("🧙","mentor","Master Velan","s-mentor","Two pointers: one at the start, one at the end. Compare characters and move inward. Return false on first mismatch. Return true if they meet without mismatch. More memory-efficient than reversing."),
            e("Two-Pointer Approach","<span class='kw'>static boolean</span> isPalindrome(<span class='type'>String</span> s) {\n    <span class='type'>int</span> left = <span class='num'>0</span>, right = s.length() - <span class='num'>1</span>;\n    <span class='kw'>while</span> (left < right) {\n        <span class='kw'>if</span> (s.charAt(left) != s.charAt(right)) <span class='kw'>return false</span>;\n        left++;  right--;\n    }\n    <span class='kw'>return true</span>;\n}")
          ),
          "Write <strong>isPalindrome(String s)</strong>.<br><br>Test:<br>• isPalindrome(\"racecar\") → <strong>true</strong><br>• isPalindrome(\"hello\") → <strong>false</strong>",
          "Two pointers: left=0, right=length-1. While left < right: if charAt(left) != charAt(right) return false. Move both inward.",
          "public class Palindrome {\n\n    static boolean isPalindrome(String s) {\n        // Two-pointer check\n\n    }\n\n    public static void main(String[] args) {\n        System.out.println(isPalindrome(\"racecar\"));\n        System.out.println(isPalindrome(\"hello\"));\n    }\n}\n",
          "\"Two-pointer. Efficient.\" Third box ticked.",
          tests(test("racecar=true","null","true"),test("hello=false","null","false")));

        q("ch7-q5","Count Word Frequency","Chapter VII · Quest 5","Interview: HashMap",7,5,250,"WordCount.java",
          story(
            n("The final question."),
            d("📋","npc","Assessor Vorn","s-npc","Count word occurrences. Given a sentence, how many times does each word appear? Use a HashMap."),
            d("🧙","mentor","Master Velan","s-mentor","Split the string into words with .split(\" \"). Loop through. For each word, check if it's in the map — if yes, increment; if no, add with count 1. getOrDefault handles both cases elegantly in one line."),
            e("HashMap Word Count","<span class='type'>HashMap</span>&lt;<span class='type'>String</span>,<span class='type'>Integer</span>&gt; counts = <span class='kw'>new</span> <span class='type'>HashMap</span>&lt;&gt;();\n<span class='type'>String</span>[] words = sentence.split(<span class='str'>\" \"</span>);\n\n<span class='kw'>for</span> (<span class='type'>int</span> i=<span class='num'>0</span>; i<words.length; i++) {\n    <span class='type'>String</span> word = words[i];\n    counts.put(word, counts.getOrDefault(word, <span class='num'>0</span>) + <span class='num'>1</span>);\n}\n<span class='cm'>// Elegant: getOrDefault returns 0 if not found</span>")
          ),
          "Given <code>String sentence = \"the cat sat on the mat\";</code><br>Count frequencies with HashMap. Print each word and count.<br><br>Expected (order may vary):<br><strong>the: 2<br>cat: 1<br>sat: 1<br>on: 1<br>mat: 1</strong>",
          "Split: <code>sentence.split(\" \")</code>. Count: <code>counts.put(w, counts.getOrDefault(w, 0) + 1)</code>. Print: <code>counts.forEach((w,c) -> System.out.println(w+\": \"+c))</code>",
          "import java.util.HashMap;\n\npublic class WordCount {\n    public static void main(String[] args) {\n        String sentence = \"the cat sat on the mat\";\n\n        // Split, count with HashMap, print each entry\n\n    }\n}\n",
          "Vorn sets down her pen. \"HashMap. getOrDefault. forEach. You know modern Java. You are hired.\"",
          tests(test("the: 2","null","the: 2"),test("cat: 1","null","cat: 1")));

        log.info("Chapter VII seeded.");
    }

}
