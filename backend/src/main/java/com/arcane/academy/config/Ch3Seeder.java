package com.arcane.academy.config;

import com.arcane.academy.repository.QuestRepository;
import org.springframework.stereotype.Component;

// ══════════════════════════════════════════════════════════════════════════════
// CHAPTER III — ARCANE STRUCTURES
// ══════════════════════════════════════════════════════════════════════════════
@Component
public class Ch3Seeder extends AbstractChapterSeeder {

    public Ch3Seeder(QuestRepository questRepository) {
        super(questRepository);
    }

    @Override
    public void seed() {

        q("ch3-q1","The Crystal Shelf","Chapter III · Quest 1","Arrays",3,1,130,"CrystalShelf.java",
          story(
            n("The Armoury of Echoes. Hundreds of crystal vials in numbered slots on a long shelf. Pip the apprentice is frantic — he's been tracking five ingredients using five separate variables and keeps confusing them."),
            d("🧒","npc","Pip","s-npc","I have potionA, potionB, potionC, potionD, potionE... I mix them up constantly. There has to be a better way to group things that belong together."),
            d("🧙","mentor","Master Velan","s-mentor","There is. An <em>array</em> stores multiple values of the same type under a single name, in numbered slots. Instead of five separate variables, you have one array with five positions."),
            e("Array Declaration & Access",
              "<span class='cm'>// Declare and initialise in one line</span>\n<span class='type'>String</span>[] spells = {<span class='str'>\"Fire\"</span>, <span class='str'>\"Ice\"</span>, <span class='str'>\"Wind\"</span>};\n\n<span class='cm'>// Access by index (starts at ZERO)</span>\nSystem.out.println(spells[<span class='num'>0</span>]); <span class='cm'>// Fire</span>\nSystem.out.println(spells[<span class='num'>1</span>]); <span class='cm'>// Ice</span>\nSystem.out.println(spells[<span class='num'>2</span>]); <span class='cm'>// Wind</span>\n\n<span class='cm'>// How many elements?</span>\nSystem.out.println(spells.length); <span class='cm'>// 3</span>"),
            d("🧙","mentor","Master Velan","s-mentor","The critical rule: indices always start at zero. A three-element array has indices 0, 1, and 2. Trying to access index 3 would crash — there is no slot 3. The last valid index is always <em>length minus one</em>."),
            e("Looping Through an Array",
              "<span class='type'>String</span>[] spells = {<span class='str'>\"Fire\"</span>, <span class='str'>\"Ice\"</span>, <span class='str'>\"Wind\"</span>};\n\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < spells.length; i++) {\n    System.out.println(spells[i]);\n}\n<span class='cm'>// i goes 0, 1, 2 — perfectly matches the valid indices</span>"),
            d("🧒","npc","Pip","s-npc","So I start the loop at 0, not 1! And I use i < length, not i <= length — because length is 3 but the last index is 2!"),
            d("🧙","mentor","Master Velan","s-mentor","Exactly. That is the most common off-by-one mistake beginners make. Always: start at 0, stop before length.")
          ),
          "Create a <code>String[]</code> named <strong>ingredients</strong> containing: <strong>\"Moonpetal\"</strong>, <strong>\"Stardust\"</strong>, <strong>\"Dragonscale\"</strong>, <strong>\"Voidmoss\"</strong>, <strong>\"Emberroot\"</strong><br><br>Use a for loop to print each ingredient on its own line. After the loop, print: <strong>\"Total: 5\"</strong>",
          "Declare: <code>String[] ingredients = {\"Moonpetal\", \"Stardust\", \"Dragonscale\", \"Voidmoss\", \"Emberroot\"};</code> Loop from i=0 while i < ingredients.length. Print <code>ingredients[i]</code> each iteration.",
          "// Create your ingredients array\n// Loop through and print each one\n// Then print 'Total: 5' after the loop\n\n",
          "All five ingredients appear in perfect order on Pip's manifest. He stares at the code. \"One name. Five slots. Starting at zero. Why didn't anyone tell me sooner?\"",
          tests(test("Moonpetal","null","Moonpetal"),test("Dragonscale","null","Dragonscale"),test("Emberroot","null","Emberroot"),test("Total","null","Total: 5")));

        q("ch3-q2","The Tome of Totals","Chapter III · Quest 2","Array Operations",3,2,130,"ArrayOps.java",
          story(
            n("The Calculation Hall. A scroll shows seven potion strengths measured this morning. The Head Alchemist needs the total and the average before the batch can be approved for distribution."),
            d("⚗️","npc","Alchemist Voryn","s-npc","Seven readings. I need the sum and the average. The average must be precise to one decimal place — we can't have rounding errors in potion dosage."),
            d("🧙","mentor","Master Velan","s-mentor","The sum pattern is one of the most common in programming. Declare a variable outside the loop set to zero, then add each element to it inside the loop. After the loop, it holds the total."),
            e("Accumulator Pattern",
              "<span class='type'>int</span>[] values = {<span class='num'>10</span>, <span class='num'>20</span>, <span class='num'>30</span>};\n<span class='type'>int</span> total = <span class='num'>0</span>;  <span class='cm'>// start at zero</span>\n\n<span class='kw'>for</span> (<span class='type'>int</span> i = <span class='num'>0</span>; i < values.length; i++) {\n    total += values[i];  <span class='cm'>// add each element</span>\n}\nSystem.out.println(total);  <span class='cm'>// 60</span>"),
            d("🧙","mentor","Master Velan","s-mentor","For the average, divide total by the count. But beware: if both are int, you'll get integer division. Cast one to double first to preserve the decimal."),
            e("Average With Decimal Precision",
              "<span class='cm'>// Cast total to double before dividing</span>\n<span class='type'>double</span> avg = (<span class='type'>double</span>) total / values.length;\nSystem.out.println(avg);  <span class='cm'>// 20.0</span>\n\n<span class='cm'>// Format to exactly 1 decimal place</span>\nSystem.out.printf(<span class='str'>\"%.1f%n\"</span>, avg);  <span class='cm'>// 20.0</span>"),
            d("⚗️","npc","Alchemist Voryn","s-npc","The readings this morning: 12, 7, 19, 4, 28, 11, 5. Sum and average, please.")
          ),
          "<code>int[] readings = {12, 7, 19, 4, 28, 11, 5};</code> is already declared.<br><br>Calculate and print on separate lines:<br>• The sum: <strong>86</strong><br>• The average to 1 decimal place: <strong>12.3</strong>",
          "Use an accumulator for the sum. Then: <code>System.out.printf(\"%.1f%n\", (double) total / readings.length);</code> — the %n adds a newline on all platforms.",
          "int[] readings = {12, 7, 19, 4, 28, 11, 5};\n\n// Calculate and print the sum\n\n// Calculate and print the average to 1 decimal place\n",
          "Voryn reads the two values and checks his own calculations. \"86 total. 12.3 average. Consistent batch. Approved for distribution.\"",
          tests(test("Sum=86","null","86"),test("Avg=12.3","null","12.3")));

        q("ch3-q3","The Scroll of Lists","Chapter III · Quest 3","ArrayList",3,3,130,"ScrollOfLists.java",
          story(
            n("The Library Annexe. Librarian Fen has a problem arrays can't solve: she doesn't know how many graduates there will be this year. Arrays require a fixed size at creation. What she needs is something that grows."),
            d("📚","npc","Librarian Fen","s-npc","I can't declare an array of graduates before I know how many there are. Last year we had 8, the year before 14, this year could be anything. I need a list that expands."),
            d("🧙","mentor","Master Velan","s-mentor","That is exactly what <em>ArrayList</em> is. It's part of Java's collections framework — a resizable list that grows automatically as you add items. You import it at the top of your file, then declare it with the type it holds in angle brackets."),
            e("ArrayList — Declaration & Methods",
              "<span class='kw'>import</span> java.util.ArrayList;\n\n<span class='cm'>// Declare an empty list of Strings</span>\n<span class='type'>ArrayList</span>&lt;<span class='type'>String</span>&gt; names = <span class='kw'>new</span> <span class='type'>ArrayList</span>&lt;&gt;();\n\nnames.add(<span class='str'>\"Aldric\"</span>);      <span class='cm'>// add to end</span>\nnames.add(<span class='str'>\"Zara\"</span>);\n\nSystem.out.println(names.size());    <span class='cm'>// 2</span>\nSystem.out.println(names.get(<span class='num'>0</span>));   <span class='cm'>// Aldric</span>\nnames.remove(<span class='str'>\"Zara\"</span>);             <span class='cm'>// remove by value</span>\nSystem.out.println(names.size());    <span class='cm'>// 1</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Note the angle brackets — <em>ArrayList&lt;String&gt;</em> means a list that holds Strings. <em>ArrayList&lt;Integer&gt;</em> holds integers. This is called a <em>generic type</em> — you'll understand why it's designed this way when we reach Chapter V."),
            d("📚","npc","Librarian Fen","s-npc","Three graduates qualified this year: Aldric, Zara, and Finn. I need them registered and the final count printed, then each name."),
            n("Remember: ArrayList uses .size() for the count, not .length. And .get(i) to access by index, not square brackets.")
          ),
          "Using <code>ArrayList&lt;String&gt;</code>:<br><br>1. Create an empty list named <strong>graduates</strong><br>2. Add: <strong>\"Aldric\"</strong>, <strong>\"Zara\"</strong>, <strong>\"Finn\"</strong><br>3. Print the size: <strong>3</strong><br>4. Print each name using a for loop and <code>.get(i)</code>",
          "Declare: <code>ArrayList&lt;String&gt; graduates = new ArrayList&lt;&gt;();</code> then add three names. Loop: <code>for (int i = 0; i &lt; graduates.size(); i++) { System.out.println(graduates.get(i)); }</code>",
          "import java.util.ArrayList;\n\n// Create ArrayList, add three names, print size then each name\n\n",
          "Three names appear on the graduation scroll in the library register. Fen stamps each one. \"Register complete. Expandable, ordered, and clear. This is how records should be kept.\"",
          tests(test("Size=3","null","3"),test("Aldric listed","null","Aldric"),test("Finn listed","null","Finn")));

        q("ch3-q4","The Spell Codex","Chapter III · Quest 4","Methods",3,4,140,"SpellCodex.java",
          story(
            n("The Grand Codex Hall. Every spell ever mastered by an Academy graduate is recorded here — but not written out in full each time it's used. It's defined once, given a name, and invoked by that name whenever needed. This is the principle of reusability."),
            d("🧝","npc","Enchantress Lyra","s-npc","Before I came here, I wrote the same greeting code every time I needed to greet someone. Twelve lines repeated forty times. A single change meant finding and fixing forty copies. Never again."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>method</em> is a named block of code. You define it once, outside main. Then you <em>call</em> it by name from anywhere in the program — as many times as you need. Change the method once and every call benefits."),
            e("Defining and Calling a Method",
              "<span class='cm'>// Define outside main — note the 'static' keyword</span>\n<span class='kw'>static void</span> greet(<span class='type'>String</span> name) {\n    System.out.println(<span class='str'>\"Hello, \"</span> + name + <span class='str'>\"!\"</span>);\n}\n\n<span class='cm'>// Call it from main</span>\ngreet(<span class='str'>\"Aldric\"</span>);  <span class='cm'>// prints: Hello, Aldric!</span>\ngreet(<span class='str'>\"Zara\"</span>);    <span class='cm'>// prints: Hello, Zara!</span>"),
            d("🧙","mentor","Master Velan","s-mentor","That method has <em>void</em> as its return type — it does something (prints) but sends nothing back. Methods can also return a value. Replace void with the return type, add <em>return</em> at the end. The caller gets the value back."),
            e("Method With Return Value",
              "<span class='kw'>static int</span> add(<span class='type'>int</span> a, <span class='type'>int</span> b) {\n    <span class='kw'>return</span> a + b;  <span class='cm'>// sends the sum back to the caller</span>\n}\n\n<span class='cm'>// In main:</span>\n<span class='type'>int</span> result = add(<span class='num'>3</span>, <span class='num'>4</span>);\nSystem.out.println(result);  <span class='cm'>// 7</span>"),
            d("🧝","npc","Enchantress Lyra","s-npc","The Codex needs two entries: a greeting method, and a calculation method that returns a value. Both will be called from main.")
          ),
          "Write two methods:<br><br>1. <code>greetWizard(String name, int level)</code> — prints: <strong>\"Welcome, [name]! Level [level].\"</strong><br>2. <code>add(int a, int b)</code> — returns the sum of a and b<br><br>In main: call greetWizard with <em>\"Kael\"</em> and <em>7</em>. Then print the result of add(12, 8).",
          "Define methods with <code>static</code> keyword above main. Use <code>void</code> for greetWizard. Use <code>int</code> return type for add with <code>return a + b;</code>",
          "public class SpellCodex {\n\n    // Write greetWizard(String name, int level) here\n\n\n    // Write add(int a, int b) here\n\n\n    public static void main(String[] args) {\n        // Call both methods here\n\n    }\n}\n",
          "Both entries appear in the Codex in flowing script. Lyra reads them. \"Clean parameters, correct return type. You've grasped the essence of reusability.\"",
          tests(test("Greeting","null","Welcome, Kael! Level 7."),test("Sum=20","null","20")));

        q("ch3-q5","The Recursion Obelisk","Chapter III · Quest 5","Recursion",3,5,160,"RecursionObelisk.java",
          story(
            n("The Obelisk of Endless Reflection stands at the Academy's eastern edge. It is said to show its own reflection, which shows another reflection, which shows another — but always stopping at exactly the right moment. This is recursion made stone."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>recursive</em> method is one that calls itself. At first this seems paradoxical — how can something define itself? The answer is the <em>base case</em>: a condition that makes the method stop calling itself and return a direct answer."),
            e("Recursion — Classic Factorial",
              "<span class='cm'>// 5! = 5 × 4 × 3 × 2 × 1 = 120</span>\n<span class='kw'>static int</span> factorial(<span class='type'>int</span> n) {\n    <span class='kw'>if</span> (n <= <span class='num'>1</span>) <span class='kw'>return</span> <span class='num'>1</span>;       <span class='cm'>// BASE CASE — stops here</span>\n    <span class='kw'>return</span> n * factorial(n - <span class='num'>1</span>);  <span class='cm'>// RECURSIVE CASE</span>\n}\n\n<span class='cm'>// Call trace for factorial(4):</span>\n<span class='cm'>// factorial(4) = 4 × factorial(3)</span>\n<span class='cm'>// factorial(3) = 3 × factorial(2)</span>\n<span class='cm'>// factorial(2) = 2 × factorial(1)</span>\n<span class='cm'>// factorial(1) = 1  ← base case, starts returning</span>"),
            d("🧙","mentor","Master Velan","s-mentor","Without the base case, the method calls itself forever until the program runs out of memory and crashes with a <em>StackOverflowError</em>. Every recursive method must have a base case that is reached eventually."),
            e("What NOT To Do",
              "<span class='cm'>// DANGER: no base case — infinite recursion</span>\n<span class='kw'>static int</span> broken(<span class='type'>int</span> n) {\n    <span class='kw'>return</span> n * broken(n - <span class='num'>1</span>);  <span class='cm'>// never stops!</span>\n}"),
            d("🪞","npc","The Obelisk","s-npc","Compute my reflection count. If I am shown once, the answer is one. Otherwise, multiply my depth by the reflection of depth minus one."),
            n("The Obelisk's logic is exactly factorial. Your base case: if n <= 1, return 1. Your recursive case: return n × factorial(n - 1).")
          ),
          "Complete the <strong>factorial</strong> method below. It should return n! recursively.<br><br>When called from main:<br>• <code>factorial(5)</code> should print <strong>120</strong><br>• <code>factorial(1)</code> should print <strong>1</strong>",
          "Base case: <code>if (n <= 1) return 1;</code> Recursive case: <code>return n * factorial(n - 1);</code>",
          "public class RecursionObelisk {\n\n    static int factorial(int n) {\n        // If n is 1 or less, return 1 (base case)\n\n        // Otherwise return n times factorial(n - 1)\n\n    }\n\n    public static void main(String[] args) {\n        System.out.println(factorial(5));\n        System.out.println(factorial(1));\n    }\n}\n",
          "The Obelisk reflects five times, each layer multiplying the one before. Then it collapses back to one. \"120,\" it intones. \"The infinite made finite by a single condition.\"",
          tests(test("factorial(5)=120","null","120"),test("factorial(1)=1","null","1")));

        q("ch3-q6","The Vault of Uniqueness","Chapter III · Quest 6","HashSet",3,6,170,"UniquenessVault.java",
          story(
            n("The Vault of Uniqueness. Every artefact here is one-of-a-kind. Duplicates are rejected at the door. This is exactly how a HashSet behaves."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>HashSet</em> stores elements with no duplicates and no guaranteed order. Unlike ArrayList, adding an element that's already present simply does nothing — it doesn't throw an error. Use <em>contains()</em> for O(1) membership checks, far faster than searching an ArrayList."),
            e("Worked Example — HashSet",
              "<span class='kw'>import</span> java.util.HashSet;\n\n<span class='type'>HashSet</span>&lt;<span class='type'>String</span>&gt; vault = <span class='kw'>new</span> <span class='type'>HashSet</span>&lt;&gt;();\nvault.add(<span class='str'>\"Sword\"</span>);\nvault.add(<span class='str'>\"Shield\"</span>);\nvault.add(<span class='str'>\"Sword\"</span>); <span class='cm'>// duplicate — silently ignored</span>\n\n<span class='kw'>System</span>.out.println(vault.size());              <span class='cm'>// 2</span>\n<span class='kw'>System</span>.out.println(vault.contains(<span class='str'>\"Shield\"</span>)); <span class='cm'>// true</span>"),
            d("🏦","npc","Vault Keeper Oris","s-npc","Add five words to the vault. Some are duplicates. The vault only keeps unique ones. Then check membership and print the size."),
            e("When to use HashSet vs ArrayList",
              "<span class='cm'>// Use ArrayList when:</span>\n<span class='cm'>//   - order matters</span>\n<span class='cm'>//   - duplicates are allowed</span>\n<span class='cm'>//   - you need index access</span>\n\n<span class='cm'>// Use HashSet when:</span>\n<span class='cm'>//   - you need uniqueness guaranteed</span>\n<span class='cm'>//   - you need fast contains() checks</span>\n<span class='cm'>//   - order does not matter</span>")
          ),
          "Create a <code>HashSet&lt;String&gt;</code>. Add: <strong>\"fire\"</strong>, <strong>\"ice\"</strong>, <strong>\"fire\"</strong>, <strong>\"wind\"</strong>, <strong>\"ice\"</strong>.<br><br>Then print:<br>1. <strong>Size: 3</strong> (duplicates removed)<br>2. <strong>true</strong> (contains \"wind\")<br>3. <strong>false</strong> (contains \"earth\")",
          "Import java.util.HashSet. After adding all 5 items (with duplicates), size() will return 3. Use contains() for the boolean checks.",
          "import java.util.HashSet;\n\npublic class UniquenessVault {\n    public static void main(String[] args) {\n        HashSet<String> vault = new HashSet<>();\n        // Add: fire, ice, fire, wind, ice\n\n        // Print: Size: [n]\n        // Print: contains(\"wind\")\n        // Print: contains(\"earth\")\n    }\n}\n",
          "Oris stamps the register. \"Three unique artefacts. Two duplicates rejected. Correct.\"",
          tests(test("Size=3","null","Size: 3"),test("Contains wind","null","true"),test("No earth","null","false")));

        q("ch3-q7","The Arcane Sorters","Chapter III · Quest 7","Collections Utility",3,7,180,"ArcaneSorters.java",
          story(
            n("The Hall of Order. Chaos reigns here — items scattered without logic. The Arcane Sorters bring order to any collection in a single call."),
            d("🧙","mentor","Master Velan","s-mentor","The <em>Collections</em> class (capital C) is a utility class of static methods for working with collections. You don't create a Collections object — you just call its methods directly. The most useful: <em>sort()</em>, <em>reverse()</em>, <em>min()</em>, and <em>max()</em>."),
            e("Worked Example — Collections utility",
              "<span class='kw'>import</span> java.util.ArrayList;\n<span class='kw'>import</span> java.util.Collections;\n\n<span class='type'>ArrayList</span>&lt;<span class='type'>Integer</span>&gt; nums = <span class='kw'>new</span> <span class='type'>ArrayList</span>&lt;&gt;();\nnums.add(<span class='num'>3</span>); nums.add(<span class='num'>1</span>); nums.add(<span class='num'>4</span>); nums.add(<span class='num'>2</span>);\n\nCollections.sort(nums);\n<span class='kw'>System</span>.out.println(nums); <span class='cm'>// [1, 2, 3, 4]</span>\n\nCollections.reverse(nums);\n<span class='kw'>System</span>.out.println(nums); <span class='cm'>// [4, 3, 2, 1]</span>\n\n<span class='kw'>System</span>.out.println(Collections.min(nums)); <span class='cm'>// 1</span>\n<span class='kw'>System</span>.out.println(Collections.max(nums)); <span class='cm'>// 4</span>"),
            d("🔢","npc","Sorter Golem","s-npc","NOTE: Collections.sort() and reverse() modify the list in-place. They do not return a new list. min() and max() return a value without changing the list.")
          ),
          "Given <code>ArrayList&lt;Integer&gt; scores</code> with values <strong>42, 17, 89, 5, 63</strong>:<br>1. Sort ascending → print: <strong>[5, 17, 42, 63, 89]</strong><br>2. Then reverse → print: <strong>[89, 63, 42, 17, 5]</strong><br>3. Print min: <strong>5</strong><br>4. Print max: <strong>89</strong>",
          "Import Collections. Sort modifies in-place — no assignment needed. min()/max() require the list before reversal if you want the correct result, but since they just scan the list, order doesn't matter.",
          "import java.util.ArrayList;\nimport java.util.Collections;\n\npublic class ArcaneSorters {\n    public static void main(String[] args) {\n        ArrayList<Integer> scores = new ArrayList<>();\n        scores.add(42); scores.add(17); scores.add(89);\n        scores.add(5);  scores.add(63);\n\n        // 1. Sort and print\n\n        // 2. Reverse and print\n\n        // 3. Print min\n\n        // 4. Print max\n    }\n}\n",
          "\"Order from chaos,\" the Sorter Golem confirms. \"Collections mastered.\"",
          tests(test("Sorted","null","[5, 17, 42, 63, 89]"),test("Reversed","null","[89, 63, 42, 17, 5]"),test("Min","null","5"),test("Max","null","89")));

        // ── Side quests ───────────────────────────────────────────────────────

        sq("ch3-sq1","The Memory Map","Chapter III · Side Quest","Pass by Value",3,90,70,"MemoryMap.java",
          story(
            n("The Academy's Map Room. Two sections of the floor glow differently: the <em>Stack</em> — bright, fast, for local variables — and the <em>Heap</em> — vast, slower, for objects."),
            d("🦉","npc","Sage Orrin","s-npc","Java is <em>always pass-by-value</em> — no exceptions. When you assign a primitive to another variable, a complete copy of the value is made. Changes to the copy never affect the original."),
            e("Primitive Copy","int original = 42;\\nint copy = original;  // a fresh copy of 42\\ncopy = 100;           // only changes 'copy'\\nSystem.out.println(original); // still 42\\nSystem.out.println(copy);     // 100"),
            d("🧙","mentor","Master Velan","s-mentor","With reference types like String or ArrayList, it is still pass-by-value — but the <em>value being copied is the reference</em> (the memory address of the object). Two variables can point at the same object on the heap. Changing the object through one variable changes what the other sees."),
            d("🦉","npc","Sage Orrin","s-npc","This is why we use <em>.equals()</em> to compare String content — <em>==</em> only checks whether two variables point to the <em>exact same object</em>, not whether the content is the same. <code>new String(\"hello\")</code> creates a fresh object even if the content is identical.")
          ),
          "Demonstrate pass by value:<br>1. <code>int original = 42; int copy = original; copy = 100;</code> — print <strong>original: 42</strong> then <strong>copy: 100</strong><br>2. <code>String a = \"Arcane\"; String b = new String(\"Arcane\");</code> — print <code>a.equals(b)</code> then <code>a == b</code>",
          "After copy = 100, original stays 42 — it was copied by value. a.equals(b) is true (same text content), a == b is false (different object instances).",
          "// Primitive: changes are independent\nint original = 42;\nint copy = original;\n// Set copy to 100, then print both\n\n// Reference: == checks identity, .equals() checks content\nString a = \"Arcane\";\nString b = new String(\"Arcane\");\n// Print a.equals(b) and a == b\n\n",
          "The Map Room crystallises. Sage Orrin taps the floor. \"Original: 42. Copy: 100. Equals: true. Reference ==: false. You now understand memory.\"",
          tests(test("original","null","original: 42"),test("copy","null","copy: 100"),test("equals","null","true"),test("reference ==","null","false")));
    }
}
