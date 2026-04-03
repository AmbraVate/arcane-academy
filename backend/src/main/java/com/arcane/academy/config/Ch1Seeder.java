package com.arcane.academy.config;

import com.arcane.academy.repository.QuestRepository;
import org.springframework.stereotype.Component;

// ══════════════════════════════════════════════════════════════════════════════
// CHAPTER I — THE FIRST RUNE (Variables & Types)
// ══════════════════════════════════════════════════════════════════════════════
@Component
public class Ch1Seeder extends AbstractChapterSeeder {

    public Ch1Seeder(QuestRepository questRepository) {
        super(questRepository);
    }

    @Override
    public void seed() {

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
}
