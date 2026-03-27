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
        log.info("Seeded {} quests.", questRepository.count());
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CHAPTER I — THE FIRST RUNE
    // Variables, types, arithmetic, strings — the absolute foundations
    // ══════════════════════════════════════════════════════════════════════════
    private void seedChapterOne() {

        // ── Quest 1: What is a program? Hello World ───────────────────────────
        q("ch1-q1", "The Wall of First Words", "Chapter I · Quest 1", "Hello World", 1, 1, 80, "HelloWorld.java",
          story(
            n("You push open the great oak doors of Arcane Academy. The entrance hall is vast and dimly lit by floating orbs of blue light. Students hurry past clutching scrolls. At the far end of the hall stands an enormous stone wall — the <em>Wall of First Words</em> — covered from floor to ceiling in glowing text."),
            d("🧙","mentor","Master Velan","s-mentor","Ah, a new apprentice. Welcome. That wall holds the first spell of every wizard who ever passed through this Academy. Every single one of them began with the same thing: making the world say something back."),
            d("🧝","npc","Enchantress Lyra","s-npc","A Java program is just a set of instructions for the computer to follow, top to bottom. The computer can't think for itself — it does exactly, and only, what you write. Nothing more."),
            d("🧙","mentor","Master Velan","s-mentor","Your first instruction will be the most important one you ever learn. In Java, when you want the program to display text, you write: <em>System.out.println()</em> — and you put your message in double quotes inside the brackets. Like this:"),
            e("Worked Example","<span class='cm'>// This prints a message to the screen</span>\n<span class='kw'>System</span>.out.println(<span class='str'>\"Hello, world!\"</span>);"),
            n("Velan points to each part in turn. 'System' is Java's connection to your screen. 'out' means output — sending something out. 'println' means print a line. The message goes in the brackets, wrapped in double quotes."),
            d("🧝","npc","Enchantress Lyra","s-npc","Every statement in Java ends with a semicolon — that little <em>;</em> at the end. Think of it like a full stop at the end of a sentence. Forget it and the program won't run."),
            d("🧙","mentor","Master Velan","s-mentor","The starter code already has the outer shell of a Java program. You can see <em>public class HelloWorld</em> and inside that, <em>public static void main(String[] args)</em>. Every Java program needs that exact structure to start. Your spell goes inside the curly braces of main."),
            n("You approach the Wall. An empty space waits for your inscription. It's time to make the Academy hear your name.")
          ),
          "Add <strong>one line</strong> inside the <code>main</code> method that prints:<br><br><strong>Welcome to Arcane Academy!</strong><br><br>The text must match exactly, including capital letters and the exclamation mark.",
          "Type this inside the curly braces: <code>System.out.println(\"Welcome to Arcane Academy!\");</code> — don't forget the semicolon at the end.",
          "public class HelloWorld {\n    public static void main(String[] args) {\n        // Write your spell below this line\n        \n    }\n}\n",
          "Golden letters blaze across the Wall of First Words: WELCOME TO ARCANE ACADEMY! Master Velan places a hand on your shoulder. \"The Academy hears you. You are a programmer now.\"",
          tests(test("Output", "null", "Welcome to Arcane Academy!")));

        // ── Quest 2: Variables — what they are and why we need them ───────────
        q("ch1-q2", "The Hall of Bindings", "Chapter I · Quest 2", "Variables", 1, 2, 100, "Bindings.java",
          story(
            n("Master Velan leads you through a side corridor into the Hall of Bindings — a circular room lined with hundreds of small glowing jars. Each jar has a label on the outside and a coloured light sealed inside. Some glow steadily. A few are dark and empty."),
            d("🧙","mentor","Master Velan","s-mentor","These are variables. In a Java program, a variable is a named container that holds a value. You give it a name — so you can find it again — and you fill it with something. The label on the jar is the name. The light inside is the value."),
            d("🧟","npc","Caretaker Moss","s-npc","We use variables to remember things. Without them, every piece of information vanishes the moment it's used. A wizard's name, their level, how much mana they have — all of it stored in variables."),
            d("🧙","mentor","Master Velan","s-mentor","In Java, you create a variable in three parts. First, the <em>type</em> — what kind of thing it holds. Second, the <em>name</em> — what you call it. Third, the <em>value</em> — what you put inside it. Then a semicolon."),
            e("Worked Example — Declaring Variables",
              "<span class='cm'>// type   name      value</span>\n<span class='type'>int</span>     level   = <span class='num'>5</span>;\n<span class='type'>String</span>  name    = <span class='str'>\"Aldric\"</span>;\n<span class='type'>double</span>  mana    = <span class='num'>87.5</span>;\n<span class='type'>boolean</span> cursed  = <span class='kw'>false</span>;"),
            d("🧙","mentor","Master Velan","s-mentor","Four types for now. <em>int</em> holds whole numbers — 1, 42, -7. <em>double</em> holds decimal numbers — 3.14, 100.0. <em>boolean</em> holds only true or false. <em>String</em> holds text — always in double quotes."),
            d("🧝","npc","Enchantress Lyra","s-npc","Once you've declared a variable, you can print it just like text. But instead of putting the value in quotes, you just write the variable name — no quotes. Like this:"),
            e("Worked Example — Printing Variables",
              "<span class='type'>int</span> level = <span class='num'>5</span>;\n\n<span class='cm'>// Prints the NUMBER 5, not the word 'level'</span>\n<span class='kw'>System</span>.out.println(level);"),
            n("The Caretaker slides open a drawer and hands you four empty jars. 'Fill these with the correct types and values,' he says. 'The Academy registry won't activate until they're all properly bound.'"),
            d("🧙","mentor","Master Velan","s-mentor","One thing to remember: variable names are case-sensitive. 'level' and 'Level' are two different variables in Java. By convention, names start with a lowercase letter. Make them descriptive — 'wizardLevel' is better than 'x'.")
          ),
          "Declare <strong>four variables</strong> and print each one on its own line:<br><br>• A <code>String</code> named <strong>wizardName</strong> with value <strong>\"Aldric\"</strong><br>• An <code>int</code> named <strong>level</strong> with value <strong>1</strong><br>• A <code>double</code> named <strong>mana</strong> with value <strong>100.0</strong><br>• A <code>boolean</code> named <strong>cursed</strong> with value <strong>false</strong><br><br>Print them in that order. Each on its own line.",
          "Declare each variable first: <code>String wizardName = \"Aldric\";</code> — then print it: <code>System.out.println(wizardName);</code> — repeat for all four. Note: String starts with a capital S.",
          "// Declare four variables and print each one\n// Remember: String needs capital S, text values need double quotes\n\n",
          "The four jars light up in sequence — amber, silver, blue, and white. Caretaker Moss stamps the registry with a satisfied grunt. \"Properly bound. All four types correct. The registry is active.\"",
          tests(test("wizardName", "null", "Aldric"), test("level", "null", "1"), test("mana", "null", "100.0"), test("cursed", "null", "false")));

        // ── Quest 3: Arithmetic and reassignment ──────────────────────────────
        q("ch1-q3", "The Cauldron of Computation", "Chapter I · Quest 3", "Arithmetic", 1, 3, 110, "Cauldron.java",
          story(
            n("The Academy's lowest basement. Steam rises from the Cauldron of Computation — a vast copper vessel inscribed with mathematical symbols. Brewmaster Zyn stands beside it, arms crossed, watching a batch of potion bubble and hiss."),
            d("🧪","npc","Brewmaster Zyn","s-npc","Don't touch anything. This batch needs precise calculation. One wrong number and we get Potion of Misfortune instead of Fortitude. The last apprentice who made that mistake spent a week as a toad."),
            d("🧙","mentor","Master Velan","s-mentor","Variables aren't just for storing things — you can calculate with them. Java understands arithmetic: <em>+</em> to add, <em>-</em> to subtract, <em>*</em> to multiply, <em>/</em> to divide."),
            e("Worked Example — Arithmetic",
              "<span class='type'>int</span> a = <span class='num'>10</span>;\n<span class='type'>int</span> b = <span class='num'>3</span>;\n\n<span class='kw'>System</span>.out.println(a + b);  <span class='cm'>// prints 13</span>\n<span class='kw'>System</span>.out.println(a - b);  <span class='cm'>// prints 7</span>\n<span class='kw'>System</span>.out.println(a * b);  <span class='cm'>// prints 30</span>\n<span class='kw'>System</span>.out.println(a / b);  <span class='cm'>// prints 3 (not 3.33!)</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Notice that last one. When you divide two <em>int</em> values, Java cuts off the decimal — 10 / 3 gives 3, not 3.333. This is called integer division. If you need the decimal, use <em>double</em> instead."),
            d("🧪","npc","Brewmaster Zyn","s-npc","Variables can also be updated. Once you've declared one, you can give it a new value at any time. You just write the name, an equals sign, and the new value. No type needed the second time — it's already declared."),
            e("Worked Example — Reassignment & Shortcuts",
              "<span class='type'>int</span> strength = <span class='num'>10</span>;\n\nstrength = strength + <span class='num'>5</span>;  <span class='cm'>// now 15 (longhand)</span>\nstrength += <span class='num'>5</span>;           <span class='cm'>// now 20 (shortcut, same thing)</span>\nstrength *= <span class='num'>2</span>;           <span class='cm'>// now 40 (multiply shortcut)</span>\nstrength -= <span class='num'>4</span>;           <span class='cm'>// now 36 (subtract shortcut)</span>\nstrength++;             <span class='cm'>// now 37 (add exactly 1)</span>\nstrength--;             <span class='cm'>// now 36 (subtract exactly 1)</span>"),
            d("🧙","mentor","Master Velan","s-mentor","The shortcuts — <em>+=</em>, <em>-=</em>, <em>*=</em>, <em>/=</em> — are just a tidier way of writing the same calculation. <em>x += 5</em> is identical to <em>x = x + 5</em>. Most programmers use the shortcuts."),
            n("Zyn slides a clipboard across the counter. Three calculations must be applied to the potion strength, in exact order, before the batch is ready. Get it wrong and the cauldron resets.")
          ),
          "<code>int potionStrength = 10;</code> is already declared.<br><br>Apply these three steps <strong>in order</strong>, then print the result:<br><br>1. Add <strong>15</strong> to potionStrength<br>2. Multiply potionStrength by <strong>3</strong><br>3. Subtract <strong>20</strong> from potionStrength<br><br>Then print the final value. <em>(Expected: 55)</em>",
          "Use the shortcut operators: <code>potionStrength += 15;</code> then <code>potionStrength *= 3;</code> then <code>potionStrength -= 20;</code> — each on its own line, then <code>System.out.println(potionStrength);</code>",
          "int potionStrength = 10;\n\n// Step 1: add 15\n\n// Step 2: multiply by 3\n\n// Step 3: subtract 20\n\n// Print the result\n",
          "The cauldron glows a deep amber. \"55,\" Zyn reads from his instruments. \"Exactly right. That is the Potion of Fortitude, grade one. Well done, apprentice.\"",
          tests(test("Result = 55", "null", "55")));

        // ── Quest 4: String operations and concatenation ───────────────────────
        q("ch1-q4", "The Scriptorium", "Chapter I · Quest 4", "String Operations", 1, 4, 110, "Scriptorium.java",
          story(
            n("The Scriptorium is the Academy's writing hall — a long, quiet room where quills scratch endlessly on parchment. Librarian Fen oversees the cataloguing of every wizard's record. She looks up as you enter and sets down her pen with a precise click."),
            d("📚","npc","Librarian Fen","s-npc","Ah, the new apprentice. I need your file updated. Name, house, combined title — all assembled from separate pieces of information. Strings, as the programmers call them."),
            d("🧙","mentor","Master Velan","s-mentor","You've used Strings before — they hold text. Now you'll learn to work with them. The most common operation is <em>concatenation</em>: joining two strings together. You do this with the <em>+</em> operator, exactly like adding numbers."),
            e("Worked Example — Concatenation",
              "<span class='type'>String</span> first = <span class='str'>\"Aria\"</span>;\n<span class='type'>String</span> last  = <span class='str'>\"Voss\"</span>;\n\n<span class='cm'>// Join them with a space between</span>\n<span class='type'>String</span> full = first + <span class='str'>\" \"</span> + last;\n<span class='kw'>System</span>.out.println(full);   <span class='cm'>// prints: Aria Voss</span>\n\n<span class='cm'>// You can also mix strings and numbers</span>\n<span class='type'>int</span> level = <span class='num'>7</span>;\n<span class='kw'>System</span>.out.println(<span class='str'>\"Level: \"</span> + level);  <span class='cm'>// prints: Level: 7</span>"),
            d("🧙","mentor","Master Velan","s-mentor","When you use <em>+</em> between a String and a number, Java converts the number to text automatically and joins them. This is incredibly useful for building readable output messages."),
            d("📚","npc","Librarian Fen","s-npc","Strings also have built-in abilities — methods that you can call on them. You write the variable name, a dot, then the method name and brackets. Let me show you the ones you'll need most."),
            e("Worked Example — String Methods",
              "<span class='type'>String</span> name = <span class='str'>\"aldric voss\"</span>;\n\n<span class='cm'>// How many characters?</span>\n<span class='kw'>System</span>.out.println(name.length());        <span class='cm'>// 11</span>\n\n<span class='cm'>// All uppercase</span>\n<span class='kw'>System</span>.out.println(name.toUpperCase());   <span class='cm'>// ALDRIC VOSS</span>\n\n<span class='cm'>// All lowercase</span>\n<span class='kw'>System</span>.out.println(name.toLowerCase());   <span class='cm'>// aldric voss</span>\n\n<span class='cm'>// First character (index 0)</span>\n<span class='kw'>System</span>.out.println(name.charAt(<span class='num'>0</span>));       <span class='cm'>// a</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Notice that <em>charAt(0)</em> uses the number 0 to mean the first character. In Java, counting almost always starts at zero, not one. The first character is at position 0, the second is at position 1, and so on. This trips up beginners, so remember it now."),
            n("Fen hands you a form. 'Fill in the wizard profile. First name, last name, full name, character count, and the name in official uppercase script. All from the two variables I've started you with.'")
          ),
          "<code>String firstName = \"Aria\";</code> and <code>String lastName = \"Voss\";</code> are declared.<br><br>Print four things:<br>1. The full name joined with a space: <strong>Aria Voss</strong><br>2. A label + the full name: <strong>Wizard: Aria Voss</strong><br>3. The length of the full name (including space): <strong>9</strong><br>4. The full name in uppercase: <strong>ARIA VOSS</strong>",
          "Join with: <code>String full = firstName + \" \" + lastName;</code> — then use <code>full.length()</code> and <code>full.toUpperCase()</code>. For the label: <code>System.out.println(\"Wizard: \" + full);</code>",
          "String firstName = \"Aria\";\nString lastName = \"Voss\";\n\n// 1. Print the full name (first + space + last)\n\n// 2. Print 'Wizard: ' followed by the full name\n\n// 3. Print the length of the full name\n\n// 4. Print the full name in uppercase\n",
          "Fen's quill moves with purpose, transcribing each line. \"Complete. Properly formatted. Filed under V for Voss.\" She snaps the folder shut with satisfaction.",
          tests(test("Full name", "null", "Aria Voss"), test("Wizard label", "null", "Wizard: Aria Voss"), test("Length", "null", "9"), test("Uppercase", "null", "ARIA VOSS")));

        // ── Quest 5: Booleans and comparisons ─────────────────────────────────
        q("ch1-q5", "The Chamber of Truth", "Chapter I · Quest 5", "Booleans & Comparisons", 1, 5, 120, "TruthChamber.java",
          story(
            n("Deep in the Academy's west wing, behind an iron door marked with an eye, is the Chamber of Truth. The room is small and circular. In the centre, a pedestal holds six crystals — some glowing gold, some dim. Seer Orin sits cross-legged beside it."),
            d("🔮","npc","Seer Orin","s-npc","These crystals respond only to truth. Each one represents a statement — a comparison. If the statement is true, the crystal glows. If it is false, it stays dark. This is how all decisions in a program begin."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>boolean</em> is Java's type for truth. It can hold one of exactly two values: <em>true</em> or <em>false</em>. That's it. No numbers, no text, just those two possibilities. And most booleans come not from writing 'true' or 'false' directly, but from asking a question — a comparison."),
            e("Worked Example — Comparison Operators",
              "<span class='type'>int</span> a = <span class='num'>10</span>;\n<span class='type'>int</span> b = <span class='num'>5</span>;\n\n<span class='type'>boolean</span> r1 = (a > b);   <span class='cm'>// true  — is 10 greater than 5?</span>\n<span class='type'>boolean</span> r2 = (a < b);   <span class='cm'>// false — is 10 less than 5?</span>\n<span class='type'>boolean</span> r3 = (a >= <span class='num'>10</span>); <span class='cm'>// true  — is 10 greater than or equal to 10?</span>\n<span class='type'>boolean</span> r4 = (a == b);  <span class='cm'>// false — is 10 equal to 5? (TWO equals signs!)</span>\n<span class='type'>boolean</span> r5 = (a != b);  <span class='cm'>// true  — is 10 NOT equal to 5?</span>"),
            d("🧙","mentor","Master Velan","s-mentor","The most important one to remember: checking equality uses TWO equals signs — <em>==</em>. One equals sign <em>=</em> means assignment — you're storing a value. Two equals signs <em>==</em> means comparison — you're asking a question. Confusing these two is one of the most common beginner mistakes."),
            d("🔮","npc","Seer Orin","s-npc","There are also logical operators that combine booleans. AND means both must be true. OR means at least one must be true. NOT flips the value. In Java, these are written with symbols."),
            e("Worked Example — Logical Operators",
              "<span class='type'>boolean</span> sunny = <span class='kw'>true</span>;\n<span class='type'>boolean</span> warm  = <span class='kw'>false</span>;\n\n<span class='cm'>// AND: both must be true</span>\n<span class='kw'>System</span>.out.println(sunny && warm);   <span class='cm'>// false</span>\n\n<span class='cm'>// OR: at least one must be true</span>\n<span class='kw'>System</span>.out.println(sunny || warm);   <span class='cm'>// true</span>\n\n<span class='cm'>// NOT: flips the value</span>\n<span class='kw'>System</span>.out.println(!sunny);          <span class='cm'>// false</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Remember: <em>&&</em> is AND, <em>||</em> is OR, <em>!</em> is NOT. These three let you build complex conditions from simple ones. You'll use them constantly once we reach decision-making next chapter."),
            n("Seer Orin gestures to the six crystals. 'Each one is a question. Answer them with Java and the crystals will reveal which statements are true and which are false.'")
          ),
          "Declare and print <strong>six boolean variables</strong>:<br><br>• <code>boolean a</code> = is 10 greater than 5? → <strong>true</strong><br>• <code>boolean b</code> = is 3 equal to 4? → <strong>false</strong><br>• <code>boolean c</code> = is 7 not equal to 7? → <strong>false</strong><br>• <code>boolean d</code> = is 5 &gt;= 5? → <strong>true</strong><br>• <code>boolean e</code> = true AND false? → <strong>false</strong><br>• <code>boolean f</code> = true OR false? → <strong>true</strong><br><br>Print each variable on its own line.",
          "Use comparison operators: <code>boolean a = (10 > 5);</code> Use <code>&&</code> for AND and <code>||</code> for OR. Print each with <code>System.out.println(a);</code> etc.",
          "// Declare and print six boolean variables\n// Use comparisons and logical operators\n\n",
          "The crystals respond in sequence — gold, dark, dark, gold, dark, gold. Seer Orin traces the pattern with a finger. \"Perfect. Every truth is correctly identified.\"",
          tests(test("a=true", "null", "true"), test("b=false", "null", "false"), test("c=false", "null", "false"), test("d=true", "null", "true"), test("e=false", "null", "false"), test("f=true", "null", "true")));

        // ── Quest 6: double, type casting, integer division ────────────────────
        q("ch1-q6", "The Alchemist's Scales", "Chapter I · Quest 6", "Doubles & Casting", 1, 6, 120, "AlchemistScales.java",
          story(
            n("The Transformation Laboratory. Alchemist Prue stands at a set of gleaming brass scales, measuring powders with surgical precision. She doesn't look up when you enter."),
            d("⚗️","npc","Alchemist Prue","s-npc","Precision matters here. An int gives you whole numbers — blunt instruments. Sometimes you need the decimals. A potion that requires 3.5 grams and gets 3 grams is a failed potion. That is the difference between int and double."),
            d("🧙","mentor","Master Velan","s-mentor","<em>double</em> is Java's type for decimal numbers. It can hold values like 3.14, 0.5, -7.333, or 100.0. Always include the decimal point — writing <em>100.0</em> tells Java this is a double, not an int."),
            e("Worked Example — int vs double",
              "<span class='cm'>// int division — cuts off the decimal</span>\n<span class='type'>int</span> a = <span class='num'>7</span>;\n<span class='type'>int</span> b = <span class='num'>2</span>;\n<span class='kw'>System</span>.out.println(a / b);           <span class='cm'>// prints 3, not 3.5!</span>\n\n<span class='cm'>// double division — keeps the decimal</span>\n<span class='type'>double</span> x = <span class='num'>7.0</span>;\n<span class='type'>double</span> y = <span class='num'>2.0</span>;\n<span class='kw'>System</span>.out.println(x / y);           <span class='cm'>// prints 3.5</span>"),
            d("⚗️","npc","Alchemist Prue","s-npc","But what if you have an int and need a decimal result? You don't have to redeclare everything. You can temporarily convert one value using a <em>cast</em>. You write the target type in brackets before the value."),
            e("Worked Example — Type Casting",
              "<span class='type'>int</span> a = <span class='num'>7</span>;\n<span class='type'>int</span> b = <span class='num'>2</span>;\n\n<span class='cm'>// Cast one int to double before dividing</span>\n<span class='kw'>System</span>.out.println((<span class='type'>double</span>) a / b);   <span class='cm'>// prints 3.5</span>\n\n<span class='cm'>// Cast a double down to int — drops the decimal</span>\n<span class='type'>double</span> pi = <span class='num'>3.14159</span>;\n<span class='kw'>System</span>.out.println((<span class='type'>int</span>) pi);          <span class='cm'>// prints 3</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Widening — going from int to double — is safe. Java can always fit a whole number into a decimal container. Narrowing — going from double to int — loses data. The decimal is cut off, not rounded. <em>3.9</em> becomes <em>3</em>, not <em>4</em>."),
            d("⚗️","npc","Alchemist Prue","s-npc","Three measurements from yesterday's batch need recalculating. Integer division gave us the wrong answers. Your job is to get the correct decimal results using proper types and casting.")
          ),
          "Three calculations are needed. Print each result on its own line:<br><br>1. Declare <code>int total = 7</code> and <code>int portions = 2</code> — print their division as a decimal: <strong>3.5</strong><br>2. Declare <code>double ingredient = 9.75</code> — cast it to int and print: <strong>9</strong><br>3. Compute <code>22 / 7</code> as a decimal using a cast — print to 1 decimal: use <code>System.out.printf(\"%.1f%n\", ...)</code> → <strong>3.1</strong>",
          "For decimal division: cast one value — <code>(double) total / portions</code>. For printf: <code>System.out.printf(\"%.1f%n\", (double) 22 / 7);</code> — the %n adds a newline.",
          "// Three type calculations\nint total = 7;\nint portions = 2;\ndouble ingredient = 9.75;\n\n// 1. Print total / portions as a decimal\n\n// 2. Print ingredient cast to int\n\n// 3. Print 22/7 as a decimal to 1 decimal place\n",
          "The scales glow and settle to perfect equilibrium. Prue lifts each measurement to the light. \"3.5. 9. 3.1. Exact. This is why we use the correct types.\"",
          tests(test("Decimal div", "null", "3.5"), test("Cast to int", "null", "9"), test("22/7", "null", "3.1")));

        // ── Quest 7: Putting it all together — wizard profile ─────────────────
        q("ch1-q7", "The Wizard's Profile", "Chapter I · Quest 7", "Putting It Together", 1, 7, 130, "WizardProfile.java",
          story(
            n("The Academy's central archive. A vast room filled with rows of filing cabinets, each containing the complete records of a wizard. The Head Archivist, a small sharp-eyed woman named Cress, is waiting for you with an empty file folder."),
            d("🗂️","npc","Archivist Cress","s-npc","Every wizard who passes through here gets a profile card — name, level, mana percentage, active status, and a full display line that combines everything into one sentence. We generate them programmatically. You'll build the template."),
            d("🧙","mentor","Master Velan","s-mentor","This quest uses everything from Chapter I — variables of different types, arithmetic, string concatenation, and printing. It will feel like a lot at once, but each piece is something you've already learned. Think of it as your first real program."),
            e("A Complete Worked Example",
              "<span class='cm'>// Declare all the wizard's stats</span>\n<span class='type'>String</span>  name    = <span class='str'>\"Seraphine\"</span>;\n<span class='type'>int</span>     level   = <span class='num'>8</span>;\n<span class='type'>double</span>  mana    = <span class='num'>72.5</span>;\n<span class='type'>boolean</span> active  = <span class='kw'>true</span>;\n\n<span class='cm'>// Calculate mana as a percentage string</span>\n<span class='type'>int</span> manaPercent = (<span class='type'>int</span>) mana;  <span class='cm'>// 72</span>\n\n<span class='cm'>// Build and print the profile</span>\n<span class='kw'>System</span>.out.println(<span class='str'>\"Name: \"</span>   + name);\n<span class='kw'>System</span>.out.println(<span class='str'>\"Level: \"</span>  + level);\n<span class='kw'>System</span>.out.println(<span class='str'>\"Mana: \"</span>   + mana + <span class='str'>\"%\"</span>);\n<span class='kw'>System</span>.out.println(<span class='str">\"Active: \"</span> + active);\n<span class='kw'>System</span>.out.println(name + <span class='str'>\" is a level \"</span> + level + <span class='str'>\" wizard.\"</span>);"),
            d("🗂️","npc","Archivist Cress","s-npc","The profile we need is for a new wizard: Dain Ashford, level 3, mana at 85.0, currently active. Build the five output lines shown in the quest panel. The format must match exactly — I archive these and exact format matters."),
            d("🧙","mentor","Master Velan","s-mentor","Take your time with this one. Declare all four variables first, then write the five print statements. If something doesn't look right, check for missing quotes, missing semicolons, or a + sign where you need it.")
          ),
          "Build a wizard profile for <strong>Dain Ashford</strong>. Declare these four variables:<br><code>String wizardName = \"Dain Ashford\"</code>, <code>int level = 3</code>, <code>double mana = 85.0</code>, <code>boolean active = true</code><br><br>Then print these five lines in this exact format:<br><strong>Name: Dain Ashford<br>Level: 3<br>Mana: 85.0%<br>Active: true<br>Dain Ashford is a level 3 wizard.</strong>",
          "For the mana line: <code>System.out.println(\"Mana: \" + mana + \"%\");</code> — the % is just a String character. For the last line: <code>System.out.println(wizardName + \" is a level \" + level + \" wizard.\");</code>",
          "// Declare four variables for Dain Ashford's profile\n\n\n// Print five lines in the exact format shown\n\n",
          "Cress takes the printout, scans it line by line, and files it. \"Correct format. All fields populated. Dain Ashford is registered.\" She reaches under the counter and hands you a copper badge. \"Chapter One: complete.\"",
          tests(test("Name line", "null", "Name: Dain Ashford"), test("Level line", "null", "Level: 3"), test("Mana line", "null", "Mana: 85.0%"), test("Active line", "null", "Active: true"), test("Summary line", "null", "Dain Ashford is a level 3 wizard.")));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CHAPTER II — THE CONTROL TOME
    // ══════════════════════════════════════════════════════════════════════════
    private void seedChapterTwo() {

        q("ch2-q1","The Oracle's Fork","Chapter II · Quest 1","If / Else",2,1,120,"OraclesFork.java",
          story(
            n("The Bridge of Aethon spans a bottomless chasm. The Bridge Keeper turns away all who cannot answer in logic."),
            d("🧟","enemy","The Bridge Keeper","s-enemy","More than 50 coins: free passage. Between 10 and 50: pay the toll. Fewer than 10: turn back. I will test three travellers."),
            d("🧙","mentor","Master Velan","s-mentor","An <em>if statement</em> runs a block only when its condition is true. Chain <em>else if</em> for additional checks. Add <em>else</em> as the catch-all."),
            e("Worked Example — If / Else",
              "<span class='type'>int</span> score = <span class='num'>75</span>;\n\n<span class='kw'>if</span> (score >= <span class='num'>90</span>) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Excellent\"</span>);\n} <span class='kw'>else if</span> (score >= <span class='num'>60</span>) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Pass\"</span>);\n} <span class='kw'>else</span> {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Fail\"</span>);\n}\n<span class='cm'>// prints: Pass</span>"),
            n("Your code must handle all three cases. The Keeper will test coins = 35, 75, and 3 separately.")
          ),
          "The variable <code>coins</code> is already declared. Write an <strong>if / else if / else</strong> block printing:<br>• <strong>\"You may pass freely.\"</strong> — coins &gt; 50<br>• <strong>\"Pay the toll.\"</strong> — coins 10–50<br>• <strong>\"Turn back.\"</strong> — coins &lt; 10",
          "Use <code>if (coins > 50)</code>, then <code>else if (coins >= 10)</code>, then <code>else</code>.",
          "int coins = 35;\n\n// Write your if / else if / else below:\n",
          "The bridge lowers. The Keeper nods. \"Logical. All three travellers judged correctly. Pass.\"",
          tests(test("coins=35","int coins = 35;","Pay the toll."), test("coins=75","int coins = 75;","You may pass freely."), test("coins=3","int coins = 3;","Turn back.")));

        q("ch2-q2","The Sorting Sigil","Chapter II · Quest 2","Switch Statements",2,2,120,"SortingSigil.java",
          story(
            n("The Sorting Chamber assigns every student to a house based on their elemental affinity. With four possible outcomes, a switch statement is far cleaner than chained if-else."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>switch</em> compares one variable against many specific values. Each <em>case</em> block ends with <em>break</em> to stop execution falling into the next case. The <em>default</em> block handles anything not matched."),
            e("Worked Example — Switch",
              "<span class='type'>String</span> colour = <span class='str'>\"red\"</span>;\n\n<span class='kw'>switch</span> (colour) {\n    <span class='kw'>case</span> <span class='str'>\"red\"</span>:\n        <span class='kw'>System</span>.out.println(<span class='str'>\"Emberhall\"</span>);\n        <span class='kw'>break</span>;\n    <span class='kw'>case</span> <span class='str'>\"blue\"</span>:\n        <span class='kw'>System</span>.out.println(<span class='str'>\"Tidespire\"</span>);\n        <span class='kw'>break</span>;\n    <span class='kw'>default</span>:\n        <span class='kw'>System</span>.out.println(<span class='str'>\"Unknown\"</span>);\n}")
          ),
          "Write a <strong>switch</strong> on <code>String affinity</code> printing:<br>• <strong>\"Emberhall\"</strong> for <em>\"Fire\"</em><br>• <strong>\"Tidespire\"</strong> for <em>\"Water\"</em><br>• <strong>\"Stoneward\"</strong> for <em>\"Earth\"</em><br>• <strong>\"Skyveil\"</strong> for <em>\"Air\"</em><br>• <strong>\"General Intake\"</strong> for anything else",
          "Use <code>switch(affinity) { case \"Fire\": ... break; ... default: ... }</code>",
          "String affinity = \"Fire\";\n\n// Write your switch statement below:\n",
          "The Sorting Sigil flares to the correct colour for all five test affinities.",
          tests(test("Fire","String affinity = \"Fire\";","Emberhall"), test("Water","String affinity = \"Water\";","Tidespire"), test("Earth","String affinity = \"Earth\";","Stoneward"), test("Air","String affinity = \"Air\";","Skyveil"), test("Unknown","String affinity = \"Shadow\";","General Intake")));

        q("ch2-q3","The Clock Tower","Chapter II · Quest 3","While Loops",2,3,120,"ClockTower.java",
          story(
            n("The Clock Tower counts using a while loop — it keeps striking as long as the count is still true."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>while loop</em> repeats as long as a condition is true. Something inside must eventually make the condition false, or the loop runs forever — an infinite loop."),
            e("Worked Example — While Loop",
              "<span class='type'>int</span> count = <span class='num'>1</span>;\n\n<span class='kw'>while</span> (count <= <span class='num'>5</span>) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Tick \"</span> + count);\n    count++;  <span class='cm'>// MUST change count or loop runs forever</span>\n}\n<span class='cm'>// prints: Tick 1, Tick 2, ... Tick 5</span>")
          ),
          "Use a <strong>while loop</strong> starting with <code>int count = 1;</code>. Print each number 1 to 5. After the loop print: <strong>\"Clock resting.\"</strong>",
          "Use <code>while (count <= 5) { System.out.println(count); count++; }</code>",
          "int count = 1;\n\n// Use a while loop to print 1 through 5\n// Then print 'Clock resting.'\n",
          "The clock strikes five times, then falls silent. \"Perfect cadence,\" the Clockmaster notes.",
          tests(test("Prints 1","null","1"), test("Prints 5","null","5"), test("Clock resting","null","Clock resting.")));

        q("ch2-q4","The Tower of Echoes","Chapter II · Quest 4","For Loops",2,4,120,"ForLoop.java",
          story(
            n("The Tower of Echoes requires a chant spoken a precise number of times. A for loop is ideal when the count is known in advance."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>for loop</em> bundles initialise, condition, and update in one line. It's perfect when you know exactly how many repetitions are needed."),
            e("Worked Example — For Loop",
              "<span class='cm'>// Counts 1 to 5</span>\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>1</span>; i <= <span class='num'>5</span>; i++) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Round \"</span> + i);\n}\n<span class='cm'>// i is available inside the loop</span>\n<span class='cm'>// prints: Round 1, Round 2 ... Round 5</span>")
          ),
          "Use a <strong>for loop</strong> to print:<br><strong>Echo 1<br>Echo 2<br>Echo 3<br>Echo 4<br>Echo 5</strong><br>Then print: <strong>\"Tower unlocked.\"</strong>",
          "Use <code>for (int i = 1; i <= 5; i++) { System.out.println(\"Echo \" + i); }</code>",
          "// Use a for loop to print Echo 1 through Echo 5\n// Then print 'Tower unlocked.'\n",
          "Five perfect echoes. The tower opens.",
          tests(test("Echo 1","null","Echo 1"), test("Echo 5","null","Echo 5"), test("Tower unlocked","null","Tower unlocked.")));

        q("ch2-q5","The Nested Labyrinth","Chapter II · Quest 5","Nested Loops",2,5,140,"NestedLoops.java",
          story(
            n("A 3x3 grid of rooms, each sealed. You must walk every row and within each row, every column — a loop inside a loop."),
            d("🧙","mentor","Master Velan","s-mentor","Loops can nest — one inside another. The outer loop runs once per row. For each outer iteration, the inner loop completes entirely. Use different counter names: <em>i</em> for outer, <em>j</em> for inner."),
            e("Worked Example — Nested Loops",
              "<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>1</span>; i <= <span class='num'>3</span>; i++) {\n    <span class='kw'>for</span> (<span class='type'>int</span> j = <span class='num'>1</span>; j <= <span class='num'>3</span>; j++) {\n        <span class='kw'>System</span>.out.println(i + <span class='str'>\"-\"</span> + j);\n    }\n}\n<span class='cm'>// prints: 1-1, 1-2, 1-3, 2-1 ... 3-3</span>")
          ),
          "Use <strong>nested for loops</strong> to print all 9 room coordinates:<br><strong>Room 1-1<br>Room 1-2<br>...</strong><br>All rows 1–3, columns 1–3.",
          "Outer: <code>for (int i = 1; i <= 3; i++)</code> — inner: <code>for (int j = 1; j <= 3; j++) { System.out.println(\"Room \" + i + \"-\" + j); }</code>",
          "// Nested for loops — print Room 1-1 through Room 3-3\n\n",
          "All nine seals break. The Labyrinth opens.",
          tests(test("Room 1-1","null","Room 1-1"), test("Room 2-3","null","Room 2-3"), test("Room 3-3","null","Room 3-3")));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CHAPTER III — ARCANE STRUCTURES
    // ══════════════════════════════════════════════════════════════════════════
    private void seedChapterThree() {

        q("ch3-q1","The Crystal Shelf","Chapter III · Quest 1","Arrays",3,1,130,"CrystalShelf.java",
          story(
            n("Five ingredients. One name. Numbered slots. Pip the apprentice needs an array."),
            d("🧙","mentor","Master Velan","s-mentor","An <em>array</em> stores multiple values of the same type under one name, in numbered slots starting at index 0."),
            e("Worked Example — Arrays",
              "<span class='type'>String</span>[] spells = {<span class='str'>\"Fire\"</span>, <span class='str'>\"Ice\"</span>, <span class='str'>\"Wind\"</span>};\n\n<span class='kw'>System</span>.out.println(spells[<span class='num'>0</span>]);  <span class='cm'>// Fire</span>\n<span class='kw'>System</span>.out.println(spells[<span class='num'>2</span>]);  <span class='cm'>// Wind</span>\n<span class='kw'>System</span>.out.println(spells.length); <span class='cm'>// 3</span>\n\n<span class='cm'>// Loop through all elements</span>\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < spells.length; i++) {\n    <span class='kw'>System</span>.out.println(spells[i]);\n}")
          ),
          "Create a <code>String[]</code> named <strong>ingredients</strong> containing: <strong>\"Moonpetal\"</strong>, <strong>\"Stardust\"</strong>, <strong>\"Dragonscale\"</strong>, <strong>\"Voidmoss\"</strong>, <strong>\"Emberroot\"</strong><br><br>Loop through and print each, then print: <strong>\"Total: 5\"</strong>",
          "Declare: <code>String[] ingredients = {\"Moonpetal\", ...};</code> loop with i from 0 to length.",
          "// Create your ingredients array, loop and print each, then print total\n\n",
          "All five appear in order. Pip is delighted.",
          tests(test("Moonpetal","null","Moonpetal"), test("Emberroot","null","Emberroot"), test("Total","null","Total: 5")));

        q("ch3-q2","The Scroll of Lists","Chapter III · Quest 2","ArrayList",3,2,130,"ScrollOfLists.java",
          story(
            n("Arrays are fixed. Sometimes you don't know the size in advance. ArrayList grows dynamically."),
            d("🧙","mentor","Master Velan","s-mentor","<em>ArrayList</em> is a resizable list. Import it, declare with a type in angle brackets, add with .add(), access with .get(index), and check size with .size()."),
            e("Worked Example — ArrayList",
              "<span class='kw'>import</span> java.util.ArrayList;\n\n<span class='type'>ArrayList</span>&lt;<span class='type'>String</span>&gt; names = <span class='kw'>new</span> <span class='type'>ArrayList</span>&lt;&gt;();\nnames.add(<span class='str'>\"Aldric\"</span>);\nnames.add(<span class='str'>\"Zara\"</span>);\n\n<span class='kw'>System</span>.out.println(names.size());     <span class='cm'>// 2</span>\n<span class='kw'>System</span>.out.println(names.get(<span class='num'>0</span>));    <span class='cm'>// Aldric</span>")
          ),
          "Create an <code>ArrayList&lt;String&gt;</code> named <strong>graduates</strong>. Add <strong>\"Aldric\"</strong>, <strong>\"Zara\"</strong>, <strong>\"Finn\"</strong>. Print size (<strong>3</strong>), then print each name.",
          "Declare, add three names, print size, loop with .size() and .get(i).",
          "import java.util.ArrayList;\n\n// Create ArrayList, add three names, print size then each name\n\n",
          "Three names on the graduation scroll.",
          tests(test("Size=3","null","3"), test("Aldric","null","Aldric"), test("Finn","null","Finn")));

        q("ch3-q3","The Spell Codex","Chapter III · Quest 3","Methods",3,3,140,"SpellCodex.java",
          story(
            n("Define it once, call it many times. Methods are reusable named blocks of code."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>method</em> is declared outside main. Call it by name from anywhere. Methods can take parameters and return values."),
            e("Worked Example — Methods",
              "<span class='cm'>// No return value (void)</span>\n<span class='kw'>static void</span> greet(<span class='type'>String</span> name) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Hello \"</span> + name);\n}\n\n<span class='cm'>// Returns a value</span>\n<span class='kw'>static int</span> add(<span class='type'>int</span> a, <span class='type'>int</span> b) {\n    <span class='kw'>return</span> a + b;\n}\n\n<span class='cm'>// Calling both from main</span>\ngreet(<span class='str'>\"Kael\"</span>);           <span class='cm'>// Hello Kael</span>\n<span class='kw'>System</span>.out.println(add(<span class='num'>3</span>, <span class='num'>4</span>));  <span class='cm'>// 7</span>")
          ),
          "Write method <strong>greetWizard(String name, int level)</strong> printing: <strong>\"Welcome, [name]! Level [level].\"</strong><br>Write method <strong>add(int a, int b)</strong> returning the sum.<br><br>In main: call greetWizard(\"Kael\", 7) and print add(12, 8).",
          "Define methods above main with <code>static</code>. Use <code>return a + b;</code> in add.",
          "public class SpellCodex {\n\n    // Write greetWizard here\n\n    // Write add here\n\n    public static void main(String[] args) {\n        // Call both\n\n    }\n}\n",
          "Both entries appear. Lyra nods: \"Clean parameters, correct return type.\"",
          tests(test("Greeting","null","Welcome, Kael! Level 7."), test("Sum=20","null","20")));

        q("ch3-q4","The Recursion Obelisk","Chapter III · Quest 4","Recursion",3,4,160,"RecursionObelisk.java",
          story(
            n("The Obelisk shows its own reflection endlessly — until the base case stops it. This is recursion."),
            d("🧙","mentor","Master Velan","s-mentor","A recursive method calls itself. It must have a <em>base case</em> — a condition that stops it. Without one, it crashes with a StackOverflowError."),
            e("Worked Example — Recursion",
              "<span class='kw'>static int</span> factorial(<span class='type'>int</span> n) {\n    <span class='kw'>if</span> (n <= <span class='num'>1</span>) <span class='kw'>return</span> <span class='num'>1</span>;       <span class='cm'>// base case</span>\n    <span class='kw'>return</span> n * factorial(n - <span class='num'>1</span>);  <span class='cm'>// recursive case</span>\n}\n<span class='cm'>// factorial(4) = 4 * 3 * 2 * 1 = 24</span>")
          ),
          "Write recursive method <strong>factorial(int n)</strong> returning n!<br>Print <code>factorial(5)</code> → <strong>120</strong> and <code>factorial(1)</code> → <strong>1</strong>",
          "Base case: <code>if (n <= 1) return 1;</code> Recursive: <code>return n * factorial(n - 1);</code>",
          "public class RecursionObelisk {\n\n    static int factorial(int n) {\n        // Base case: if n <= 1, return 1\n        // Recursive case: return n * factorial(n - 1)\n\n    }\n\n    public static void main(String[] args) {\n        System.out.println(factorial(5));\n        System.out.println(factorial(1));\n    }\n}\n",
          "The Obelisk: \"120. Correct. The infinite made finite.\"",
          tests(test("factorial(5)=120","null","120"), test("factorial(1)=1","null","1")));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CHAPTER IV — THE GRAND GRIMOIRE (OOP)
    // ══════════════════════════════════════════════════════════════════════════
    private void seedChapterFour() {

        q("ch4-q1","The Golem Foundry","Chapter IV · Quest 1","Classes & Objects",4,1,150,"GolemFoundry.java",
          story(
            n("Classes are blueprints. Objects are individual instances created from blueprints."),
            d("🧙","mentor","Master Velan","s-mentor","Declare a class with fields and a constructor. Create objects with <em>new</em>. Use <em>this.name</em> in the constructor to distinguish fields from parameters."),
            e("Worked Example — Class & Object",
              "<span class='kw'>class</span> <span class='type'>Wizard</span> {\n    <span class='type'>String</span> name;\n    <span class='type'>int</span>    level;\n\n    <span class='type'>Wizard</span>(<span class='type'>String</span> name, <span class='type'>int</span> level) {\n        <span class='kw'>this</span>.name  = name;\n        <span class='kw'>this</span>.level = level;\n    }\n\n    <span class='kw'>void</span> describe() {\n        <span class='kw'>System</span>.out.println(name + <span class='str'>\" level \"</span> + level);\n    }\n}\n\n<span class='cm'>// Creating objects</span>\n<span class='type'>Wizard</span> w = <span class='kw'>new</span> <span class='type'>Wizard</span>(<span class='str'>\"Aldric\"</span>, <span class='num'>5</span>);\nw.describe();  <span class='cm'>// Aldric level 5</span>")
          ),
          "Create a <strong>Wizard</strong> class with fields <code>name</code> and <code>level</code>, a constructor, and <code>describe()</code> printing: <strong>\"Wizard [name] is level [level].\"</strong><br><br>In main: create <code>new Wizard(\"Aldric\", 5)</code> and <code>new Wizard(\"Zara\", 9)</code> and call describe on each.",
          "Define class above main class. Constructor: <code>this.name = name;</code>",
          "// Define Wizard class here\n\n\npublic class GolemFoundry {\n    public static void main(String[] args) {\n        // Create two Wizard objects and call describe()\n\n    }\n}\n",
          "G-1 booms: \"BLUEPRINTS ACCEPTED. OBJECTS INSTANTIATED.\"",
          tests(test("Aldric","null","Wizard Aldric is level 5."), test("Zara","null","Wizard Zara is level 9.")));

        q("ch4-q2","The Seal of Encapsulation","Chapter IV · Quest 2","Encapsulation",4,2,160,"Encapsulation.java",
          story(
            n("Private fields protected by public getters and setters — the discipline of encapsulation."),
            d("🧙","mentor","Master Velan","s-mentor","Make fields <em>private</em> so only the class itself can access them directly. Provide <em>getters</em> to read and <em>setters</em> to write — with validation in the setter."),
            e("Worked Example — Encapsulation",
              "<span class='kw'>class</span> <span class='type'>Wizard</span> {\n    <span class='kw'>private</span> <span class='type'>int</span> level;\n\n    <span class='kw'>public int</span> getLevel() { <span class='kw'>return</span> level; }\n\n    <span class='kw'>public void</span> setLevel(<span class='type'>int</span> level) {\n        <span class='kw'>if</span> (level > <span class='num'>0</span>) <span class='kw'>this</span>.level = level;\n        <span class='cm'>// ignores invalid values</span>\n    }\n}")
          ),
          "Create a <strong>Wizard</strong> class with <em>private</em> <code>name</code> and <code>level</code>. Add <code>getName()</code>, <code>getLevel()</code>, and <code>setLevel(int)</code> that ignores negatives. In main: create Wizard(\"Aldric\", 5), call setLevel(-1), print name and level. Output: <strong>Aldric</strong> then <strong>5</strong>.",
          "Setter: <code>if (level > 0) this.level = level;</code> — negative values are simply ignored.",
          "public class Encapsulation {\n    static class Wizard {\n        private String name;\n        private int level;\n        Wizard(String name, int level) { this.name = name; this.level = level; }\n        // Add getName(), getLevel(), setLevel()\n\n    }\n    public static void main(String[] args) {\n        Wizard w = new Wizard(\"Aldric\", 5);\n        w.setLevel(-1);\n        System.out.println(w.getName());\n        System.out.println(w.getLevel());\n    }\n}\n",
          "\"Properly guarded,\" Sable says. \"The field cannot be corrupted from outside.\"",
          tests(test("Name=Aldric","null","Aldric"), test("Level=5","null","5")));

        q("ch4-q3","The Order of Lineage","Chapter IV · Quest 3","Inheritance",4,3,170,"Inheritance.java",
          story(
            n("Child classes extend parent classes, inheriting all fields and methods."),
            d("🧙","mentor","Master Velan","s-mentor","Use <em>extends</em>. In the child constructor call <em>super()</em> first to run the parent constructor. Use <em>@Override</em> to replace a parent method."),
            e("Worked Example — Inheritance",
              "<span class='kw'>class</span> <span class='type'>Animal</span> {\n    <span class='type'>String</span> name;\n    <span class='type'>Animal</span>(<span class='type'>String</span> name) { <span class='kw'>this</span>.name = name; }\n    <span class='kw'>void</span> speak() { <span class='kw'>System</span>.out.println(<span class='str'>\"...\"</span>); }\n}\n\n<span class='kw'>class</span> <span class='type'>Dog</span> <span class='kw'>extends</span> <span class='type'>Animal</span> {\n    <span class='type'>Dog</span>(<span class='type'>String</span> name) { <span class='kw'>super</span>(name); }\n\n    @Override\n    <span class='kw'>void</span> speak() { <span class='kw'>System</span>.out.println(name + <span class='str'>\" barks\"</span>); }\n}")
          ),
          "Given the <code>Wizard</code> base class, create <strong>BattleMage</strong> extending Wizard with <code>String weapon</code> field. Override <code>describe()</code> to print: <strong>\"BattleMage [name] wields [weapon].\"</strong><br><br>In main: <code>new BattleMage(\"Kael\", 7, \"Flameblade\")</code>.describe() and <code>new Wizard(\"Zara\", 3)</code>.describe()",
          "Use <code>class BattleMage extends Wizard</code> with <code>super(name, level)</code> in constructor.",
          "class Wizard {\n    String name; int level;\n    Wizard(String name, int level) { this.name = name; this.level = level; }\n    void describe() { System.out.println(\"Wizard \" + name + \" is level \" + level + \".\"); }\n}\n\n// Write BattleMage here\n\npublic class Inheritance {\n    public static void main(String[] args) {\n        // Create both and call describe\n\n    }\n}\n",
          "\"The lineage is complete,\" the Hall intones.",
          tests(test("BattleMage","null","BattleMage Kael wields Flameblade."), test("Wizard","null","Wizard Zara is level 3.")));

        q("ch4-q4","The Polymorphic Mirrors","Chapter IV · Quest 4","Polymorphism",4,4,180,"Polymorphism.java",
          story(
            n("Store different object types under one parent type. Java calls the right method at runtime."),
            d("🧙","mentor","Master Velan","s-mentor","This is <em>polymorphism</em>. A Wizard variable can hold a BattleMage object. When you call describe(), Java uses the actual object's type — not the variable's type."),
            e("Worked Example — Polymorphism",
              "<span class='type'>Wizard</span>[] gallery = {\n    <span class='kw'>new</span> <span class='type'>Wizard</span>(<span class='str'>\"Zara\"</span>, <span class='num'>3</span>),\n    <span class='kw'>new</span> <span class='type'>BattleMage</span>(<span class='str'>\"Kael\"</span>, <span class='num'>7</span>, <span class='str'>\"Sword\"</span>)\n};\n\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < gallery.length; i++) {\n    gallery[i].describe();  <span class='cm'>// calls correct version</span>\n}")
          ),
          "Add a <code>Healer</code> class (spell field) extending Wizard. Override describe() → <strong>\"Healer [name] casts [spell].\"</strong><br>Store Wizard, BattleMage, Healer in a <code>Wizard[]</code> array and loop calling describe on each.<br><br>Expected:<br><strong>Wizard Zara is level 3.<br>BattleMage Kael wields Flameblade.<br>Healer Mira casts Rejuvenate.</strong>",
          "Declare <code>Wizard[] gallery = { new Wizard(...), new BattleMage(...), new Healer(...) };</code> then loop with <code>gallery[i].describe();</code>",
          "class Wizard {\n    String name; int level;\n    Wizard(String n, int l) { name=n; level=l; }\n    void describe() { System.out.println(\"Wizard \"+name+\" is level \"+level+\".\"); }\n}\nclass BattleMage extends Wizard {\n    String weapon;\n    BattleMage(String n, int l, String w) { super(n,l); weapon=w; }\n    @Override void describe() { System.out.println(\"BattleMage \"+name+\" wields \"+weapon+\".\"); }\n}\n// Write Healer class here\n\npublic class Polymorphism {\n    public static void main(String[] args) {\n        // Create Wizard array with all three types\n\n    }\n}\n",
          "Three reflections, each unique. \"One call, three answers,\" Illen whispers.",
          tests(test("Wizard Zara","null","Wizard Zara is level 3."), test("BattleMage Kael","null","BattleMage Kael wields Flameblade."), test("Healer Mira","null","Healer Mira casts Rejuvenate.")));

        q("ch4-q5","The Abstract Sanctum","Chapter IV · Quest 5","Abstract Classes",4,5,200,"AbstractSanctum.java",
          story(
            n("Abstract classes cannot be instantiated — they must be extended. They enforce a contract."),
            d("🧙","mentor","Master Velan","s-mentor","An <em>abstract</em> class can have abstract methods — declared but not implemented. Subclasses must provide the implementation. You cannot create an object of an abstract class directly."),
            e("Worked Example — Abstract Class",
              "<span class='kw'>abstract class</span> <span class='type'>Shape</span> {\n    <span class='kw'>abstract double</span> area();  <span class='cm'>// no body</span>\n}\n\n<span class='kw'>class</span> <span class='type'>Circle</span> <span class='kw'>extends</span> <span class='type'>Shape</span> {\n    <span class='type'>double</span> radius;\n    <span class='type'>Circle</span>(<span class='type'>double</span> r) { radius = r; }\n    @Override\n    <span class='kw'>double</span> area() { <span class='kw'>return</span> Math.PI * radius * radius; }\n}")
          ),
          "Create abstract <strong>Shape</strong> with abstract <code>double area()</code>. Create <strong>Circle</strong> (radius) and <strong>Rectangle</strong> (width, height).<br><br>In main print:<br>• Circle radius 5: <strong>78.5</strong><br>• Rectangle 4×6: <strong>24.0</strong>",
          "Circle area: <code>Math.PI * radius * radius</code>. Round to 1 decimal: <code>Math.round(circle.area() * 10) / 10.0</code>",
          "// Write abstract Shape class\n// Write Circle and Rectangle subclasses\n\npublic class AbstractSanctum {\n    public static void main(String[] args) {\n        Shape circle = new Circle(5);\n        Shape rect = new Rectangle(4, 6);\n        System.out.println(Math.round(circle.area() * 10) / 10.0);\n        System.out.println(rect.area());\n    }\n}\n",
          "Tessara measures each. \"78.5 and 24.0. Geometry is magic made precise.\"",
          tests(test("Circle=78.5","null","78.5"), test("Rectangle=24.0","null","24.0")));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CHAPTER V — THE MASTER'S PATH (Advanced Java)
    // ══════════════════════════════════════════════════════════════════════════
    private void seedChapterFive() {

        q("ch5-q1","The Ward of Exceptions","Chapter V · Quest 1","Exception Handling",5,1,200,"ExceptionWard.java",
          story(
            n("Exceptions happen when something goes wrong at runtime. Handle them gracefully instead of crashing."),
            d("🧙","mentor","Master Velan","s-mentor","Wrap risky code in <em>try</em>. If an exception occurs, the <em>catch</em> block runs. The <em>finally</em> block always runs, regardless."),
            e("Worked Example — Try/Catch/Finally",
              "<span class='kw'>try</span> {\n    <span class='type'>int</span> result = <span class='num'>10</span> / <span class='num'>0</span>;\n} <span class='kw'>catch</span> (<span class='type'>ArithmeticException</span> e) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Caught: \"</span> + e.getMessage());\n} <span class='kw'>finally</span> {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Always runs\"</span>);\n}\n<span class='cm'>// prints: Caught: / by zero</span>\n<span class='cm'>// prints: Always runs</span>")
          ),
          "Write try/catch/finally: in try compute 10/0, in catch print <strong>\"Caught: \"</strong> + the message, in finally print <strong>\"Ward stable.\"</strong>",
          "Use <code>catch (ArithmeticException e) { System.out.println(\"Caught: \" + e.getMessage()); }</code>",
          "// Write your try/catch/finally block here\n\n",
          "\"Exception handled. No crash. This is professional code,\" Nell marks the clipboard.",
          tests(test("Caught","null","Caught: / by zero"), test("Finally","null","Ward stable.")));

        q("ch5-q2","The Generics Forge","Chapter V · Quest 2","Generics",5,2,210,"GenericsForge.java",
          story(
            n("Generic classes use a type parameter — a placeholder replaced with a real type when used."),
            d("🧙","mentor","Master Velan","s-mentor","Declare <em>class Box&lt;T&gt;</em>. Inside, use T as the type. When creating: <em>new Box&lt;String&gt;()</em>. This is how Java's collections work."),
            e("Worked Example — Generics",
              "<span class='kw'>class</span> <span class='type'>Box</span>&lt;<span class='type'>T</span>&gt; {\n    <span class='kw'>private</span> <span class='type'>T</span> item;\n    <span class='kw'>void</span> put(<span class='type'>T</span> item) { <span class='kw'>this</span>.item = item; }\n    <span class='type'>T</span> get() { <span class='kw'>return</span> item; }\n}\n\n<span class='type'>Box</span>&lt;<span class='type'>String</span>&gt; box = <span class='kw'>new</span> <span class='type'>Box</span>&lt;&gt;();\nbox.put(<span class='str'>\"Hello\"</span>);\n<span class='kw'>System</span>.out.println(box.get());  <span class='cm'>// Hello</span>")
          ),
          "Write generic class <strong>Box&lt;T&gt;</strong> with <code>put(T)</code> and <code>get()</code>. In main: store <strong>\"Arcane Scroll\"</strong> in <code>Box&lt;String&gt;</code> and <strong>42</strong> in <code>Box&lt;Integer&gt;</code>, print both.",
          "Declare <code>class Box&lt;T&gt; { private T item; public void put(T item) { this.item = item; } public T get() { return item; } }</code>",
          "// Write your generic Box<T> class here\n\npublic class GenericsForge {\n    public static void main(String[] args) {\n        Box<String> stringBox = new Box<>();\n        stringBox.put(\"Arcane Scroll\");\n        System.out.println(stringBox.get());\n\n        Box<Integer> intBox = new Box<>();\n        intBox.put(42);\n        System.out.println(intBox.get());\n    }\n}\n",
          "\"Type-safe containers. Professional craft,\" Brenn tests each.",
          tests(test("String box","null","Arcane Scroll"), test("Integer box","null","42")));

        q("ch5-q3","The Lambda Loom","Chapter V · Quest 3","Lambdas",5,3,220,"LambdaLoom.java",
          story(
            n("Lambda expressions create anonymous functions. Java 8 introduced treating functions as values."),
            d("🧙","mentor","Master Velan","s-mentor","Syntax: <em>(parameters) -&gt; expression</em>. Works with functional interfaces — interfaces with one abstract method."),
            e("Worked Example — Lambdas",
              "<span class='kw'>import</span> java.util.function.*;\n\n<span class='cm'>// Runnable: no params, no return</span>\n<span class='type'>Runnable</span> r = () -> <span class='kw'>System</span>.out.println(<span class='str'>\"Run!\"</span>);\nr.run();\n\n<span class='cm'>// Predicate: takes T, returns boolean</span>\n<span class='type'>Predicate</span>&lt;<span class='type'>Integer</span>&gt; isEven = n -> n % <span class='num'>2</span> == <span class='num'>0</span>;\n<span class='kw'>System</span>.out.println(isEven.test(<span class='num'>4</span>));  <span class='cm'>// true</span>\n\n<span class='cm'>// Function: takes T, returns R</span>\n<span class='type'>Function</span>&lt;<span class='type'>Integer</span>,<span class='type'>Integer</span>&gt; dbl = n -> n * <span class='num'>2</span>;\n<span class='kw'>System</span>.out.println(dbl.apply(<span class='num'>7</span>));   <span class='cm'>// 14</span>")
          ),
          "Create three lambdas:<br>1. A <code>Runnable</code> printing <strong>\"Loom activated.\"</strong> — call r.run()<br>2. A <code>Predicate&lt;Integer&gt;</code> checking if even — test with 4: <strong>true</strong><br>3. A <code>Function&lt;Integer,Integer&gt;</code> doubling input — apply to 7: <strong>14</strong>",
          "Import <code>java.util.function.Predicate</code> and <code>java.util.function.Function</code>.",
          "import java.util.function.Predicate;\nimport java.util.function.Function;\n\npublic class LambdaLoom {\n    public static void main(String[] args) {\n        // 1. Runnable lambda\n\n        // 2. Predicate lambda\n\n        // 3. Function lambda\n\n    }\n}\n",
          "\"Functions as values. The loom accepts your craft,\" Saya says.",
          tests(test("Loom activated","null","Loom activated."), test("Predicate true","null","true"), test("Doubler 14","null","14")));

        q("ch5-q4","The Stream Conduit","Chapter V · Quest 4","Streams",5,4,230,"StreamConduit.java",
          story(
            n("Streams process collections without explicit loops — filter, transform, collect."),
            d("🧙","mentor","Master Velan","s-mentor","Get a stream with <em>list.stream()</em>. Chain <em>filter()</em>, <em>map()</em>. Terminate with <em>forEach()</em> or <em>count()</em>. Nothing runs until the terminal operation."),
            e("Worked Example — Streams",
              "<span class='type'>List</span>&lt;<span class='type'>Integer</span>&gt; nums = <span class='type'>Arrays</span>.asList(<span class='num'>1</span>,<span class='num'>2</span>,<span class='num'>3</span>,<span class='num'>4</span>,<span class='num'>5</span>);\n\n<span class='cm'>// Filter evens, double them, print</span>\nnums.stream()\n    .filter(n -> n % <span class='num'>2</span> == <span class='num'>0</span>)\n    .map(n -> n * <span class='num'>2</span>)\n    .forEach(<span class='kw'>System</span>.out::println);\n<span class='cm'>// prints: 4, 8</span>")
          ),
          "Given <code>List&lt;Integer&gt; numbers = Arrays.asList(1,2,3,4,5,6,7,8);</code>:<br>1. Filter evens, double each, print all → <strong>4 8 12 16</strong> (one per line)<br>2. Count and print even numbers → <strong>4</strong>",
          "Chain: <code>.filter(n -> n % 2 == 0).map(n -> n * 2).forEach(System.out::println)</code>",
          "import java.util.Arrays;\nimport java.util.List;\n\npublic class StreamConduit {\n    public static void main(String[] args) {\n        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);\n\n        // 1. Filter evens, double, print each\n\n        // 2. Count and print even numbers\n\n    }\n}\n",
          "\"Declarative, efficient, elegant,\" Vael reads. \"You've mastered the stream.\"",
          tests(test("First doubled even","null","4"), test("Second doubled even","null","8"), test("Count","null","4")));

        q("ch5-q5","The Pattern Archive","Chapter V · Quest 5","Design Patterns",5,5,250,"PatternArchive.java",
          story(
            n("Design patterns are proven solutions to recurring problems. Singleton ensures one instance. Builder constructs objects step by step."),
            d("🧙","mentor","Master Velan","s-mentor","Singleton: private constructor, private static instance, public static getInstance(). Builder: inner class with chainable methods returning <em>this</em>, then build() returning the outer object."),
            e("Worked Example — Singleton",
              "<span class='kw'>class</span> <span class='type'>Registry</span> {\n    <span class='kw'>private static</span> <span class='type'>Registry</span> instance;\n    <span class='kw'>private</span> <span class='type'>Registry</span>() {}\n\n    <span class='kw'>public static</span> <span class='type'>Registry</span> getInstance() {\n        <span class='kw'>if</span> (instance == <span class='kw'>null</span>)\n            instance = <span class='kw'>new</span> <span class='type'>Registry</span>();\n        <span class='kw'>return</span> instance;\n    }\n}")
          ),
          "Implement Singleton <strong>Registry</strong>. Call getInstance() twice — print: <strong>\"Same instance: true\"</strong><br><br>Implement <strong>Wizard</strong> with inner <strong>Builder</strong> (chainable name(), level(), build()). Build and print: <strong>\"Aldric level 7\"</strong>",
          "Singleton: <code>if (instance == null) instance = new Registry(); return instance;</code> Test: <code>System.out.println(\"Same instance: \" + (r1 == r2));</code>",
          "// Implement Singleton Registry\n\n// Implement Wizard with inner Builder\n\npublic class PatternArchive {\n    public static void main(String[] args) {\n        Registry r1 = Registry.getInstance();\n        Registry r2 = Registry.getInstance();\n        System.out.println(\"Same instance: \" + (r1 == r2));\n\n        Wizard w = new Wizard.Builder().name(\"Aldric\").level(7).build();\n        System.out.println(w.name + \" level \" + w.level);\n    }\n}\n",
          "\"Singleton confirmed. Builder confirmed,\" Crey reads. \"You understand professional architecture.\"",
          tests(test("Same instance","null","Same instance: true"), test("Builder output","null","Aldric level 7")));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // BOSSES
    // ══════════════════════════════════════════════════════════════════════════
    private void seedBosses() {
        log.info("Seeding bosses...");

        saveBoss("ch1-boss","The Golem of Types","🗿",1,200,
          "The Golem stirs from its pedestal, stone grinding stone. Its eyes glow cold. \"You claim to know types and values. Demonstrate it.\"",
          "[{\"id\":\"c1q1\",\"type\":\"multiple_choice\",\"question\":\"Which type stores the text \\\"Aldric\\\"?\",\"options\":[\"int\",\"boolean\",\"String\",\"double\"],\"correct\":\"String\",\"explanation\":\"String holds text — sequences of characters. Always wrap String values in double quotes.\"},{\"id\":\"c1q2\",\"type\":\"be_the_compiler\",\"question\":\"What does this print?\\n\\nint x = 10;\\nx += 5;\\nx *= 2;\\nSystem.out.println(x);\",\"options\":[\"10\",\"15\",\"25\",\"30\"],\"correct\":\"30\",\"explanation\":\"x starts at 10. +=5 gives 15. *=2 gives 30.\"},{\"id\":\"c1q3\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to declare a decimal variable:\",\"code\":\"______ mana = 99.5;\",\"correct\":\"double\",\"explanation\":\"double holds decimal numbers.\"},{\"id\":\"c1q4\",\"type\":\"be_the_compiler\",\"question\":\"What does this print?\\n\\nint a = 7;\\nint b = 2;\\nSystem.out.println(a / b);\",\"options\":[\"3.5\",\"3\",\"4\",\"Error\"],\"correct\":\"3\",\"explanation\":\"Integer division drops the decimal. 7/2 = 3.\"},{\"id\":\"c1q5\",\"type\":\"multiple_choice\",\"question\":\"What does System.out.println(\\\"Hi\\\" + \\\" \\\" + \\\"there\\\") print?\",\"options\":[\"Hi there\",\"Hi + there\",\"Error\",\"Hithere\"],\"correct\":\"Hi there\",\"explanation\":\"The + operator concatenates Strings.\"}]");

        saveBoss("ch2-boss","The Labyrinth Warden","🐺",2,200,
          "The Warden blocks the maze exit. \"Only those who truly understand control flow may leave.\"",
          "[{\"id\":\"c2q1\",\"type\":\"be_the_compiler\",\"question\":\"What prints?\\n\\nint mana = 100;\\nif (mana > 100) {\\n    System.out.println(\\\"Overcharged\\\");\\n} else if (mana == 100) {\\n    System.out.println(\\\"Full\\\");\\n} else {\\n    System.out.println(\\\"Low\\\");\\n}\",\"options\":[\"Overcharged\",\"Full\",\"Low\",\"Nothing\"],\"correct\":\"Full\",\"explanation\":\"mana == 100 is true so Full prints. Note: == checks equality, = assigns.\"},{\"id\":\"c2q2\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to loop exactly 4 times:\",\"code\":\"for (int i = 0; i __ 4; i++) { }\",\"correct\":\"<\",\"explanation\":\"i < 4 runs for 0,1,2,3 — exactly 4 times.\"},{\"id\":\"c2q3\",\"type\":\"be_the_compiler\",\"question\":\"What is printed?\\n\\nint total = 0;\\nfor (int i = 1; i <= 5; i++) {\\n    total += i;\\n}\\nSystem.out.println(total);\",\"options\":[\"5\",\"10\",\"15\",\"20\"],\"correct\":\"15\",\"explanation\":\"1+2+3+4+5=15.\"},{\"id\":\"c2q4\",\"type\":\"multiple_choice\",\"question\":\"A while loop that never changes its condition is called:\",\"options\":[\"A fast loop\",\"An infinite loop\",\"A break loop\",\"A for loop\"],\"correct\":\"An infinite loop\",\"explanation\":\"If the condition never becomes false the loop runs forever.\"},{\"id\":\"c2q5\",\"type\":\"be_the_compiler\",\"question\":\"How many times does 'Go!' print?\\n\\nfor (int i = 10; i > 0; i -= 3) {\\n    System.out.println(\\\"Go!\\\");\\n}\",\"options\":[\"3\",\"4\",\"10\",\"infinite\"],\"correct\":\"4\",\"explanation\":\"i: 10→7→4→1→stop. Runs 4 times.\"}]");

        saveBoss("ch3-boss","The Vault Keeper","🔐",3,250,
          "The Vault Keeper rises from parchment and shadow. \"Prove you understand the discipline of structure.\"",
          "[{\"id\":\"c3q1\",\"type\":\"be_the_compiler\",\"question\":\"What prints?\\n\\nString[] s = {\\\"Fire\\\", \\\"Ice\\\", \\\"Wind\\\"};\\nSystem.out.println(s[1]);\",\"options\":[\"Fire\",\"Ice\",\"Wind\",\"Error\"],\"correct\":\"Ice\",\"explanation\":\"Indices start at 0. s[1] is Ice.\"},{\"id\":\"c3q2\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to get the array length:\",\"code\":\"int[] nums = {1,2,3,4,5};\\nSystem.out.println(nums.________);\",\"correct\":\"length\",\"explanation\":\".length returns the number of elements.\"},{\"id\":\"c3q3\",\"type\":\"be_the_compiler\",\"question\":\"What does factorial(4) return?\\n\\nstatic int factorial(int n) {\\n    if (n <= 1) return 1;\\n    return n * factorial(n-1);\\n}\",\"options\":[\"4\",\"10\",\"24\",\"Error\"],\"correct\":\"24\",\"explanation\":\"4×3×2×1=24.\"},{\"id\":\"c3q4\",\"type\":\"multiple_choice\",\"question\":\"What is the key difference between an array and an ArrayList?\",\"options\":[\"Arrays are faster\",\"Arrays are fixed size; ArrayList resizes\",\"ArrayList is for ints only\",\"No difference\"],\"correct\":\"Arrays are fixed size; ArrayList resizes\",\"explanation\":\"Arrays have a fixed size. ArrayList grows and shrinks dynamically.\"},{\"id\":\"c3q5\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to make this method return the sum:\",\"code\":\"static int add(int a, int b) {\\n    ______ a + b;\\n}\",\"correct\":\"return\",\"explanation\":\"The return keyword sends a value back to the caller.\"}]");

        saveBoss("ch4-boss","The Ancient Dragon","🐉",4,300,
          "The Ancient Dragon uncoils from the highest spire. \"Object-oriented mastery is claimed easily. Demonstrate it.\"",
          "[{\"id\":\"c4q1\",\"type\":\"be_the_compiler\",\"question\":\"What prints?\\n\\nclass Animal {\\n    void speak() { System.out.println(\\\"sound\\\"); }\\n}\\nclass Dog extends Animal {\\n    @Override void speak() { System.out.println(\\\"bark\\\"); }\\n}\\nAnimal a = new Dog();\\na.speak();\",\"options\":[\"sound\",\"bark\",\"Error\",\"Nothing\"],\"correct\":\"bark\",\"explanation\":\"Polymorphism: Java uses the actual object type (Dog) at runtime.\"},{\"id\":\"c4q2\",\"type\":\"multiple_choice\",\"question\":\"What keyword calls the parent constructor from a child?\",\"options\":[\"parent()\",\"base()\",\"super()\",\"this()\"],\"correct\":\"super()\",\"explanation\":\"super() calls the parent constructor. Must be first in the child constructor.\"},{\"id\":\"c4q3\",\"type\":\"fill_blank\",\"question\":\"Fill the blank:\",\"code\":\"class BattleMage ______ Wizard { }\",\"correct\":\"extends\",\"explanation\":\"extends establishes inheritance.\"},{\"id\":\"c4q4\",\"type\":\"multiple_choice\",\"question\":\"An abstract class differs from an interface because:\",\"options\":[\"Abstract classes can have constructors and concrete methods\",\"Interfaces are faster\",\"Abstract classes can only be used once\",\"No difference\"],\"correct\":\"Abstract classes can have constructors and concrete methods\",\"explanation\":\"Abstract classes can mix abstract and concrete methods and have constructors.\"},{\"id\":\"c4q5\",\"type\":\"be_the_compiler\",\"question\":\"What does making a field 'private' achieve?\",\"options\":[\"It deletes the field\",\"Only code inside the class can access it directly\",\"It becomes read-only\",\"It speeds up access\"],\"correct\":\"Only code inside the class can access it directly\",\"explanation\":\"private restricts direct access. External code must use getters and setters.\"}]");

        saveBoss("ch5-boss","The Archmage","⚡",5,400,
          "The Archmage regards you with ancient eyes. \"You have walked from Hello World to design patterns. One final examination.\"",
          "[{\"id\":\"c5q1\",\"type\":\"be_the_compiler\",\"question\":\"What prints?\\n\\ntry {\\n    int x = 10 / 0;\\n} catch (ArithmeticException e) {\\n    System.out.println(\\\"caught\\\");\\n} finally {\\n    System.out.println(\\\"done\\\");\\n}\",\"options\":[\"caught\",\"done\",\"caught then done\",\"Error\"],\"correct\":\"caught then done\",\"explanation\":\"The catch block runs, then finally always runs.\"},{\"id\":\"c5q2\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to create a lambda doubler:\",\"code\":\"Function<Integer,Integer> doubler = n __ n * 2;\",\"correct\":\"->\",\"explanation\":\"-> is the lambda arrow operator.\"},{\"id\":\"c5q3\",\"type\":\"multiple_choice\",\"question\":\"What does stream.filter(n -> n > 5) do?\",\"options\":[\"Deletes elements greater than 5\",\"Keeps only elements greater than 5\",\"Counts elements greater than 5\",\"Sorts elements\"],\"correct\":\"Keeps only elements greater than 5\",\"explanation\":\"filter() keeps elements where the predicate returns true.\"},{\"id\":\"c5q4\",\"type\":\"multiple_choice\",\"question\":\"The Singleton pattern ensures:\",\"options\":[\"Fast object creation\",\"Only one instance of a class exists\",\"Objects are immutable\",\"Thread safety always\"],\"correct\":\"Only one instance of a class exists\",\"explanation\":\"Singleton restricts instantiation to one object.\"},{\"id\":\"c5q5\",\"type\":\"fill_blank\",\"question\":\"Fill the blank to declare a generic class:\",\"code\":\"class Box<__> { __ item; }\",\"correct\":\"T\",\"explanation\":\"T is the conventional name for a type parameter.\"}]");

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

    private String n(String text) {
        return "{\"type\":\"narration\",\"text\":\"" + esc(text) + "\"}";
    }

    private String d(String av, String cls, String speaker, String sCls, String text) {
        return "{\"type\":\"dialogue\",\"av\":\"" + av + "\",\"cls\":\"" + cls +
               "\",\"speaker\":\"" + speaker + "\",\"sCls\":\"" + sCls +
               "\",\"text\":\"" + esc(text) + "\"}";
    }

    /** Example beat — renders as an inline code block in the story panel */
    private String e(String label, String code) {
        return "{\"type\":\"example\",\"speaker\":\"" + esc(label) +
               "\",\"text\":\"" + esc(code) + "\"}";
    }

    private String tests(String... ts) { return "[" + String.join(",", ts) + "]"; }

    private String test(String label, String input, String expected) {
        return "{\"label\":\"" + label + "\",\"input\":" +
               ("null".equals(input) ? "null" : "\"" + esc(input) + "\"") +
               ",\"expected\":\"" + esc(expected) + "\"}";
    }

    private String esc(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }
}
