package com.arcane.academy.config;

import com.arcane.academy.model.Quest;
import com.arcane.academy.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChapterThreeSeeder {

    private final QuestRepository questRepository;

    void seed() {

        q("ch3-q1","The Crystal Shelf","Chapter III · Quest 1","Arrays",3,1,130,"CrystalShelf.java",
          story(
            n("The Armoury of Echoes. Hundreds of numbered crystal vials line a shelf thirty metres long. Each slot has a number etched above it. Pip the apprentice stands in the middle of the room surrounded by five unlabelled jars, looking helpless."),
            d("🧒","npc","Pip the Apprentice","s-npc","I have five potion ingredients and I keep losing them. I've been using five separate variables — ingredientOne, ingredientTwo, ingredientThree... and when I want the third one I have to remember what I called it. There has to be a better way."),
            d("🧙","mentor","Master Velan","s-mentor","There is. An <em>array</em>. Instead of five separate variables, you have one variable that holds five things in numbered slots. One name, many values. The slot number is called the <em>index</em>, and in Java, indexing always starts at zero."),
            e("Worked Example — Declaring and Using an Array",
              "<span class='cm'>// Declare an array of three Strings</span>\n<span class='type'>String</span>[] spells = {<span class='str'>\"Fire\"</span>, <span class='str'>\"Ice\"</span>, <span class='str'>\"Wind\"</span>};\n\n<span class='cm'>// Access by index (starts at 0!)</span>\n<span class='kw'>System</span>.out.println(spells[<span class='num'>0</span>]);  <span class='cm'>// Fire</span>\n<span class='kw'>System</span>.out.println(spells[<span class='num'>1</span>]);  <span class='cm'>// Ice</span>\n<span class='kw'>System</span>.out.println(spells[<span class='num'>2</span>]);  <span class='cm'>// Wind</span>\n\n<span class='cm'>// How many elements?</span>\n<span class='kw'>System</span>.out.println(spells.length);  <span class='cm'>// 3</span>"),
            d("🧙","mentor","Master Velan","s-mentor","The most common use of arrays is looping through them. Combine the array with a for loop: start at index 0, run while i is less than the array's length, and increment i each time. This visits every element in order."),
            e("Worked Example — Looping Through an Array",
              "<span class='type'>String</span>[] spells = {<span class='str'>\"Fire\"</span>, <span class='str'>\"Ice\"</span>, <span class='str'>\"Wind\"</span>};\n\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < spells.length; i++) {\n    <span class='kw'>System</span>.out.println(spells[i]);\n}\n<span class='cm'>// prints: Fire, Ice, Wind</span>"),
            d("🧒","npc","Pip the Apprentice","s-npc","So spells.length always tells me how many there are, and I can change the array without updating the loop? That's much better than five separate variables. Can I print the count at the end too?"),
            n("Master Velan nods. 'One array, one loop, one length. That is the power of ordered storage.'")
          ),
          "Create a <code>String[]</code> named <strong>ingredients</strong> containing these five values in order: <strong>\"Moonpetal\"</strong>, <strong>\"Stardust\"</strong>, <strong>\"Dragonscale\"</strong>, <strong>\"Voidmoss\"</strong>, <strong>\"Emberroot\"</strong><br><br>Loop through and print each on its own line. Then print: <strong>\"Total: 5\"</strong>",
          "Declare: <code>String[] ingredients = {\"Moonpetal\", \"Stardust\", \"Dragonscale\", \"Voidmoss\", \"Emberroot\"};</code> then loop with <code>for (int i = 0; i &lt; ingredients.length; i++)</code>",
          "// Create your ingredients array\n// Loop through and print each one\n// Then print: Total: 5\n\n",
          "All five ingredients appear in perfect sequence. Pip examines the list. 'That took me three seconds. It used to take me all morning.' He carefully files each jar.",
          tests(test("Moonpetal","null","Moonpetal"),test("Dragonscale","null","Dragonscale"),test("Emberroot","null","Emberroot"),test("Total","null","Total: 5")));

        q("ch3-q2","The Tome of Totals","Chapter III · Quest 2","Array Operations",3,2,130,"ArrayOps.java",
          story(
            n("The Calculation Hall. Seven potion-strength readings from yesterday's batch sit on a scroll. Head Alchemist Voryn needs the total and the average before he can submit the batch report. He's been doing it by hand for twenty years and isn't happy about it."),
            d("⚗️","npc","Head Alchemist Voryn","s-npc","Seven readings. I add them up, divide by seven, and that gives me the average. I've been doing it manually. I've heard programmers can do this in seconds. Prove it."),
            d("🧙","mentor","Master Velan","s-mentor","The classic array pattern: start with a <em>total</em> variable set to zero, then loop through the array adding each element to it. After the loop, total contains the sum of everything."),
            e("Worked Example — Array Sum",
              "<span class='type'>int</span>[] scores = {<span class='num'>10</span>, <span class='num'>20</span>, <span class='num'>30</span>};\n<span class='type'>int</span> total = <span class='num'>0</span>;\n\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < scores.length; i++) {\n    total += scores[i];  <span class='cm'>// add each score to total</span>\n}\n<span class='kw'>System</span>.out.println(total);  <span class='cm'>// 60</span>"),
            d("🧙","mentor","Master Velan","s-mentor","For the average, divide total by the count. But careful — if both are ints, Java does integer division and drops the decimal. Cast one to double first: <em>(double) total / scores.length</em>. And use <em>printf</em> to control decimal places in the output."),
            e("Worked Example — Average with printf",
              "<span class='type'>double</span> avg = (<span class='type'>double</span>) total / scores.length;\n\n<span class='cm'>// %.1f means: format as float, 1 decimal place</span>\n<span class='cm'>// %n is a newline (works on all platforms)</span>\n<span class='kw'>System</span>.out.printf(<span class='str'>\"%.1f%n\"</span>, avg);  <span class='cm'>// e.g. 20.0</span>"),
            n("Voryn slides the scroll across the bench. The seven readings are: 12, 7, 19, 4, 28, 11, 5. Sum: 86. Average: 12.3. Those are the expected answers.")
          ),
          "<code>int[] readings = {12, 7, 19, 4, 28, 11, 5};</code> is declared. Calculate and print:<br>• The <strong>sum</strong>: <strong>86</strong><br>• The <strong>average</strong> to 1 decimal place: <strong>12.3</strong>",
          "Sum loop: <code>int total = 0; for (int i = 0; i &lt; readings.length; i++) total += readings[i];</code> Then: <code>System.out.printf(\"%.1f%n\", (double) total / readings.length);</code>",
          "int[] readings = {12, 7, 19, 4, 28, 11, 5};\n\n// Calculate the sum and print it\n// Calculate the average and print it to 1 decimal place\n",
          "Voryn inspects both numbers. '86. 12.3. Correct.' He stamps the batch report. 'Same answer I got by hand. Considerably less time.'",
          tests(test("Sum=86","null","86"),test("Average=12.3","null","12.3")));

        q("ch3-q3","The Scroll of Lists","Chapter III · Quest 3","ArrayList",3,3,130,"ScrollOfLists.java",
          story(
            n("The Library Annexe. Librarian Fen is updating the graduation register — a list that grows by three names each day. She's been using arrays but keeps running into a problem."),
            d("📚","npc","Librarian Fen","s-npc","Arrays are fixed. I declare my array as size ten and then the eleventh student qualifies and the whole thing breaks. I need something that can grow. Is there such a thing in Java?"),
            d("🧙","mentor","Master Velan","s-mentor","Yes. <em>ArrayList</em> — a resizable list. Unlike arrays, it grows automatically when you add more items. It lives in Java's utility library, so you need to import it at the top of your file. Then declare it with the type it holds in angle brackets."),
            e("Worked Example — ArrayList Basics",
              "<span class='kw'>import</span> java.util.ArrayList;\n\n<span class='cm'>// Create an empty ArrayList of Strings</span>\n<span class='type'>ArrayList</span>&lt;<span class='type'>String</span>&gt; names = <span class='kw'>new</span> <span class='type'>ArrayList</span>&lt;&gt;();\n\n<span class='cm'>// Add items</span>\nnames.add(<span class='str'>\"Aldric\"</span>);\nnames.add(<span class='str'>\"Zara\"</span>);\n\n<span class='cm'>// How many items?</span>\n<span class='kw'>System</span>.out.println(names.size());    <span class='cm'>// 2 (not .length!)</span>\n\n<span class='cm'>// Access by index</span>\n<span class='kw'>System</span>.out.println(names.get(<span class='num'>0</span>));  <span class='cm'>// Aldric</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Notice the difference from arrays: ArrayList uses <em>.size()</em> not <em>.length</em>, and accesses elements with <em>.get(index)</em> not square brackets. The loop pattern is the same — just replace <em>arr.length</em> with <em>list.size()</em>."),
            e("Worked Example — Looping an ArrayList",
              "<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < names.size(); i++) {\n    <span class='kw'>System</span>.out.println(names.get(i));\n}"),
            d("📚","npc","Librarian Fen","s-npc","And I can remove from it too? Without the whole thing collapsing?"),
            d("🧙","mentor","Master Velan","s-mentor","Correct. <em>names.remove(0)</em> removes by index. <em>names.remove(\"Aldric\")</em> removes by value. The list adjusts automatically. Three graduates today: Aldric, Zara, and Finn.")
          ),
          "Using <code>ArrayList&lt;String&gt;</code> (remember the import):<br>1. Create an empty list named <strong>graduates</strong><br>2. Add: <strong>\"Aldric\"</strong>, <strong>\"Zara\"</strong>, <strong>\"Finn\"</strong><br>3. Print the size: <strong>3</strong><br>4. Print each name using a for loop",
          "Import: <code>import java.util.ArrayList;</code> Declare: <code>ArrayList&lt;String&gt; graduates = new ArrayList&lt;&gt;();</code> Loop with <code>graduates.size()</code> and <code>graduates.get(i)</code>",
          "import java.util.ArrayList;\n\n// Create the ArrayList, add three names, print size, then loop and print each\n\n",
          "Three names appear on the scroll in neat ink. Fen nods approvingly. 'And when the fourth student qualifies tomorrow, it will simply grow. No redeclaring. No crashing.'",
          tests(test("Size=3","null","3"),test("Aldric","null","Aldric"),test("Finn","null","Finn")));

        q("ch3-q4","The Spell Codex","Chapter III · Quest 4","Methods",3,4,140,"SpellCodex.java",
          story(
            n("The Grand Codex Hall. Enchantress Lyra is annotating a spellbook — one where every complex operation is defined once at the top and then invoked by name throughout. She explains why."),
            d("🧝","npc","Enchantress Lyra","s-npc","If you need to greet a wizard in seventeen different parts of your program, you could write System.out.println seventeen times. Or you could define a greet method once and call it seventeen times. When you want to change the greeting, you change it in one place and every invocation updates automatically."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>method</em> is a named block of code. Define it outside main with <em>static</em> so it can be called from main. Give it a return type — <em>void</em> if it doesn't return a value, or the type of what it returns. Then list its parameters in parentheses."),
            e("Worked Example — void Method",
              "<span class='cm'>// Method definition — outside main</span>\n<span class='kw'>static void</span> greet(<span class='type'>String</span> name) {\n    <span class='kw'>System</span>.out.println(<span class='str'>\"Hello, \"</span> + name + <span class='str'>\"!\"</span>);\n}\n\n<span class='cm'>// Calling it from main</span>\ngreet(<span class='str'>\"Aldric\"</span>);  <span class='cm'>// prints: Hello, Aldric!</span>\ngreet(<span class='str'>\"Zara\"</span>);    <span class='cm'>// prints: Hello, Zara!</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Methods can also return values — use <em>return</em> to send a value back to whoever called the method. Change <em>void</em> to the type being returned. The caller can then store or print that value."),
            e("Worked Example — Method with Return Value",
              "<span class='kw'>static int</span> add(<span class='type'>int</span> a, <span class='type'>int</span> b) {\n    <span class='kw'>return</span> a + b;  <span class='cm'>// sends the result back</span>\n}\n\n<span class='cm'>// In main:</span>\n<span class='type'>int</span> result = add(<span class='num'>10</span>, <span class='num'>5</span>);\n<span class='kw'>System</span>.out.println(result);   <span class='cm'>// 15</span>\n\n<span class='cm'>// Or directly in println:</span>\n<span class='kw'>System</span>.out.println(add(<span class='num'>3</span>, <span class='num'>4</span>));  <span class='cm'>// 7</span>"),
            d("🧝","npc","Enchantress Lyra","s-npc","The Codex needs two entries today: a greetWizard method that prints a formatted greeting, and an add method that returns the sum of two integers. Write them above main."),
            n("Remember: method definitions go inside the class but outside the main method. Java reads the whole class before running anything.")
          ),
          "Write two methods <strong>outside</strong> main:<br><br>1. <code>static void greetWizard(String name, int level)</code> — prints: <strong>\"Welcome, [name]! Level [level].\"</strong><br>2. <code>static int add(int a, int b)</code> — returns the sum<br><br>In main: call <code>greetWizard(\"Kael\", 7)</code> and print <code>add(12, 8)</code>",
          "Method 1: <code>static void greetWizard(String name, int level) { System.out.println(\"Welcome, \" + name + \"! Level \" + level + \".\"); }</code> Method 2: <code>static int add(int a, int b) { return a + b; }</code>",
          "public class SpellCodex {\n\n    // Write greetWizard method here\n\n    // Write add method here\n\n    public static void main(String[] args) {\n        // Call greetWizard(\"Kael\", 7)\n        // Print add(12, 8)\n\n    }\n}\n",
          "Both entries appear in the Codex in flowing script. Lyra reads them aloud and nods. 'Clean parameters. Correct return type. And when the greeting changes, you change it exactly once.'",
          tests(test("Greeting","null","Welcome, Kael! Level 7."),test("Sum=20","null","20")));

        q("ch3-q5","The Recursion Obelisk","Chapter III · Quest 5","Recursion",3,5,160,"RecursionObelisk.java",
          story(
            n("The Obelisk of Endless Reflection stands at the centre of the Academy's courtyard. Its surface is polished obsidian and shows a perfect reflection of itself — which shows another reflection of itself, which shows another. At some point, this has to stop."),
            d("🧙","mentor","Master Velan","s-mentor","Some problems are naturally self-similar. The factorial of 5 is 5 times the factorial of 4. The factorial of 4 is 4 times the factorial of 3. And so on down to 1, which is just 1. A method that calls itself is called <em>recursive</em>."),
            d("🧙","mentor","Master Velan","s-mentor","Every recursive method needs two things. First: the <em>base case</em> — the condition where it stops and returns a direct answer. Second: the <em>recursive case</em> — where it calls itself with a simpler version of the problem. Without a base case, it calls itself forever and crashes."),
            e("Worked Example — Recursive Countdown",
              "<span class='kw'>static void</span> countdown(<span class='type'>int</span> n) {\n    <span class='kw'>if</span> (n <= <span class='num'>0</span>) {\n        <span class='kw'>System</span>.out.println(<span class='str'>\"Blastoff!\"</span>);\n        <span class='kw'>return</span>;  <span class='cm'>// ← base case: stop here</span>\n    }\n    <span class='kw'>System</span>.out.println(n);\n    countdown(n - <span class='num'>1</span>);  <span class='cm'>// ← recursive case: simpler problem</span>\n}\n<span class='cm'>// countdown(3) prints: 3, 2, 1, Blastoff!</span>"),
            e("Worked Example — Factorial",
              "<span class='kw'>static int</span> factorial(<span class='type'>int</span> n) {\n    <span class='kw'>if</span> (n <= <span class='num'>1</span>) <span class='kw'>return</span> <span class='num'>1</span>;      <span class='cm'>// base case</span>\n    <span class='kw'>return</span> n * factorial(n - <span class='num'>1</span>);  <span class='cm'>// recursive case</span>\n}\n<span class='cm'>// factorial(5) = 5 × factorial(4)</span>\n<span class='cm'>//             = 5 × 4 × factorial(3)</span>\n<span class='cm'>//             = 5 × 4 × 3 × 2 × 1 = 120</span>"),
            d("🪞","npc","The Obelisk","s-npc","COMPUTE MY REFLECTION DEPTH. IF N EQUALS 1, RETURN 1. OTHERWISE RETURN N MULTIPLIED BY THE DEPTH OF N MINUS ONE. I HAVE BEEN WAITING FIVE HUNDRED YEARS FOR SOMEONE TO ANSWER THIS CORRECTLY."),
            n("The base case is n == 1 (or n <= 1 to be safe). Everything else multiplies n by the result of calling factorial with n-1.")
          ),
          "Write a recursive method <strong>factorial(int n)</strong> that returns n!<br><br>In main, call it and print:<br>• <code>factorial(5)</code> → <strong>120</strong><br>• <code>factorial(1)</code> → <strong>1</strong>",
          "Base case: <code>if (n <= 1) return 1;</code> Recursive case: <code>return n * factorial(n - 1);</code>",
          "public class RecursionObelisk {\n\n    static int factorial(int n) {\n        // Base case: if n is 1 (or less), return 1\n        // Recursive case: return n * factorial(n - 1)\n\n    }\n\n    public static void main(String[] args) {\n        System.out.println(factorial(5));\n        System.out.println(factorial(1));\n    }\n}\n",
          "The Obelisk counts silently: five, four, three, two, one. '120,' it intones. 'Correct. The reflection terminates. I am satisfied after five centuries.'",
          tests(test("factorial(5)=120","null","120"),test("factorial(1)=1","null","1")));
    }

    private void q(String id, String title, String eyebrow, String topic, int chapter, int order, int xp, String file, String story, String problem, String hint, String starter, String win, String tests) {
        questRepository.save(Quest.builder().id(id).title(title).eyebrow(eyebrow).topic(topic).chapterNumber(chapter).orderInChapter(order).xpReward(xp).filename(file).storyJson(story).problemHtml(problem).hint(hint).starterCode(starter).winStory(win).testCasesJson(tests).build());
    }
    private String story(String... b) { return "[" + String.join(",", b) + "]"; }
    private String n(String t) { return "{\"type\":\"narration\",\"text\":\"" + esc(t) + "\"}"; }
    private String d(String av, String cls, String sp, String sCls, String t) { return "{\"type\":\"dialogue\",\"av\":\""+av+"\",\"cls\":\""+cls+"\",\"speaker\":\""+sp+"\",\"sCls\":\""+sCls+"\",\"text\":\""+esc(t)+"\"}"; }
    private String e(String label, String code) { return "{\"type\":\"example\",\"speaker\":\""+esc(label)+"\",\"text\":\""+esc(code)+"\"}"; }
    private String tests(String... ts) { return "[" + String.join(",", ts) + "]"; }
    private String test(String label, String input, String expected) { return "{\"label\":\""+label+"\",\"input\":"+("null".equals(input)?"null":"\""+esc(input)+"\"") +",\"expected\":\""+esc(expected)+"\"}"; }
    private String esc(String s) { return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n").replace("\t","\\t"); }
}
