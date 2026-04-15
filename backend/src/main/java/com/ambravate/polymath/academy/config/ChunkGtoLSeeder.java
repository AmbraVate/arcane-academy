package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.stereotype.Component;

@Component
public class ChunkGtoLSeeder extends AbstractChunkSeeder {

    public ChunkGtoLSeeder(ChunkRepository chunkRepository, SubChunkRepository subChunkRepository,
                           QuestionRepository questionRepository, RabbitHoleModuleRepository rabbitHoleRepository) {
        super(chunkRepository, subChunkRepository, questionRepository, rabbitHoleRepository);
    }

    @Override
    public void seed() {
        seedChunkG();
        seedChunkH();
        seedChunkI();
        seedChunkJ();
        seedChunkK();
        seedChunkL();
    }

    // =========================================================================
    // CHUNK G — Arrays
    // =========================================================================
    private void seedChunkG() {
        chunk("G", "Arrays", "\uD83D\uDCE6", 7, LearnerPath.FOUNDATION, "C");

        // G1 — Array Basics
        subChunk("G1", "G", "Array Basics", 1, 50, "ArrayBasics.java",

                "<p>You have 100 student scores to store. Would you really declare 100 separate variables? "
                + "There is a far better way — the <strong>array</strong>.</p>",

                "<h3>Arrays: Fixed-Size Ordered Containers</h3>"
                + "<p>An array is a numbered sequence of elements of the <em>same type</em>. "
                + "You declare it once and access any element instantly by its <strong>index</strong> (starting at 0).</p>"
                + "<pre><code>// Declaration and initialisation\nint[] scores = new int[5];   // five ints, all zero\nscores[0] = 85;\nscores[1] = 92;\n\n// Or use an initialiser\nint[] grades = {85, 92, 78, 96, 88};\nSystem.out.println(grades[2]); // 78</code></pre>"
                + "<p>Key rules:</p>"
                + "<ul>"
                + "<li>Indexes run from <code>0</code> to <code>length - 1</code></li>"
                + "<li>Access <code>arr.length</code> (no parentheses) for the size</li>"
                + "<li>Accessing an out-of-bounds index throws <code>ArrayIndexOutOfBoundsException</code></li>"
                + "<li>Arrays have a <strong>fixed size</strong> — once created they cannot grow or shrink</li>"
                + "</ul>"
                + "<pre><code>int[] nums = {10, 20, 30};\nSystem.out.println(nums.length); // 3\n// nums[3] → ArrayIndexOutOfBoundsException!</code></pre>",

                story(
                    n("Archmage Eldrin leads you to a long stone shelf in the Arcane Library. Exactly five identically shaped vials stand in a row, each numbered from 0 to 4."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "This shelf is an <strong>array</strong>. Every slot holds the same type of essence, and each slot has a number — its <em>index</em>. The first slot is always zero, never one. Many apprentices stumble on that."),
                    e("Shelf of potions", "int[] potions = {100, 75, 50, 25, 10};\nSystem.out.println(potions[0]); // 100  — first\nSystem.out.println(potions[4]); // 10   — last\nSystem.out.println(potions.length); // 5"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The shelf's length is fixed. You cannot magically conjure a sixth slot. If you reach for slot five, the spell <em>backfires</em> — an ArrayIndexOutOfBoundsException. Always check your bounds.")
                ),

                "<p>Declare an array <code>int[] scores</code> with five values: <code>85, 92, 78, 96, 88</code>. "
                + "Print each element on its own line.</p>",

                "// Declare and initialise the array\n\n// Print each element using a for loop\n",

                tests(
                    test("All scores", "null", "85\n92\n78\n96\n88")
                ),

                "Explain what an array is in Java, how you declare one, and what happens if you access an invalid index."
        );

        mcQuestion("G1", QuestionTier.RECALL,
                "<p>What is the index of the <strong>first</strong> element in a Java array?</p>",
                new String[]{"0", "1", "-1", "Depends on the array"},
                "0",
                "<p>Java arrays are zero-indexed — the first element is always at index <code>0</code>.</p>");

        tfQuestion("G1", QuestionTier.RECALL,
                "<p>You can change the size of a Java array after it has been created.</p>",
                "False",
                "<p>Java arrays have a <strong>fixed size</strong>. Once created, their length cannot change. If you need a resizable container, use <code>ArrayList</code>.</p>");

        codeQuestion("G1", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "int[] arr = {10, 20, 30, 40};\nSystem.out.println(arr[1] + arr[3]);",
                "60",
                "<p><code>arr[1]</code> is 20 and <code>arr[3]</code> is 40. Their sum is 60.</p>");

        // G2 — Array Operations
        subChunk("G2", "G", "Array Operations & Algorithms", 2, 50, "ArrayOps.java",

                "<p>Knowing how to declare an array is just the start — now you need to traverse it, search it, and find insights within it.</p>",

                "<h3>Iterating, Searching, and the Arrays Class</h3>"
                + "<p>Loop over an array with an index-based <code>for</code> loop or an enhanced <code>for-each</code>:</p>"
                + "<pre><code>int[] nums = {5, 2, 8, 1, 9};\n\n// Index-based (lets you modify)\nfor (int i = 0; i < nums.length; i++) {\n    System.out.println(nums[i]);\n}\n\n// Enhanced for-each (read only)\nfor (int n : nums) {\n    System.out.println(n);\n}</code></pre>"
                + "<p>Common algorithms:</p>"
                + "<pre><code>// Sum\nint sum = 0;\nfor (int n : nums) sum += n;\n\n// Maximum\nint max = nums[0];\nfor (int i = 1; i < nums.length; i++) {\n    if (nums[i] > max) max = nums[i];\n}</code></pre>"
                + "<p>The <code>java.util.Arrays</code> utility class provides ready-made methods:</p>"
                + "<pre><code>import java.util.Arrays;\nArrays.sort(nums);                        // sort in place\nSystem.out.println(Arrays.toString(nums)); // [1, 2, 5, 8, 9]\nint[] copy = Arrays.copyOf(nums, 3);      // [1, 2, 5]</code></pre>",

                story(
                    n("Eldrin tips five differently-powered vials onto the workbench and slides a quill towards you."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Every master of the arcane arts learns three fundamental operations: traverse every element, find the strongest, and sum the total power. These are the building blocks of every array algorithm you will ever write."),
                    e("Sum and maximum", "int[] power = {40, 85, 30, 95, 60};\nint sum = 0;\nint max = power[0];\nfor (int p : power) {\n    sum += p;\n    if (p > max) max = p;\n}\nSystem.out.println(\"Total: \" + sum); // 310\nSystem.out.println(\"Max: \" + max);   // 95"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "And when you need them sorted, spare yourself the drudgery — the <code>Arrays</code> grimoire has already written that spell for you.")
                ),

                "<p>Given <code>int[] vals = {42, 7, 88, 13, 55}</code>, print the <strong>sum</strong> of all elements on one line, "
                + "then the <strong>largest</strong> value on the next line.</p>",

                "import java.util.Arrays;\n\npublic class ArrayOps {\n    public static void main(String[] args) {\n        int[] vals = {42, 7, 88, 13, 55};\n        // Calculate and print sum\n\n        // Find and print maximum\n\n    }\n}\n",

                tests(
                    test("Sum", "null", "205"),
                    test("Maximum", "null", "88")
                ),

                "Explain how to find the maximum value in an array without using built-in sort. Walk through the algorithm step by step."
        );

        codeQuestion("G2", QuestionTier.RECALL, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this print?</p>",
                "import java.util.Arrays;\nint[] a = {3, 1, 4, 1, 5};\nArrays.sort(a);\nSystem.out.println(Arrays.toString(a));",
                "[1, 1, 3, 4, 5]",
                "<p><code>Arrays.sort()</code> sorts in ascending order in place. <code>Arrays.toString()</code> formats the result.</p>");

        mcQuestion("G2", QuestionTier.APPLICATION,
                "<p>You need to find a value in an array without using any library methods. Which approach is correct?</p>",
                new String[]{"Loop through every element and compare", "Access arr[value] directly", "Use arr.find()", "Use arr.indexOf()"},
                "Loop through every element and compare",
                "<p>Linear search loops through each element comparing it to the target. Arrays don't have <code>find()</code> or <code>indexOf()</code> methods — those belong to <code>ArrayList</code>.</p>");

        tfQuestion("G2", QuestionTier.DISCRIMINATION,
                "<p><code>Arrays.sort()</code> returns a new sorted array and leaves the original unchanged.</p>",
                "False",
                "<p><code>Arrays.sort()</code> sorts the array <strong>in place</strong>. The original array is modified. Use <code>Arrays.copyOf()</code> first if you need to preserve the original.</p>");

        // G3 — Multi-dimensional Arrays
        subChunk("G3", "G", "Multi-dimensional Arrays", 3, 60, "MultiArrays.java",

                "<p>A seating chart, a chessboard, a multiplication table — all of these are grids. Java models them with multi-dimensional arrays.</p>",

                "<h3>2D Arrays: Arrays of Arrays</h3>"
                + "<p>A 2D array is an array whose elements are themselves arrays — think rows and columns:</p>"
                + "<pre><code>// 3 rows, 4 columns\nint[][] grid = new int[3][4];\ngrid[0][0] = 1;  // row 0, column 0\ngrid[2][3] = 99; // row 2, column 3\n\n// Or initialise directly\nint[][] matrix = {\n    {1, 2, 3},\n    {4, 5, 6},\n    {7, 8, 9}\n};\nSystem.out.println(matrix[1][2]); // 6</code></pre>"
                + "<p>Iterate with nested loops — outer loop for rows, inner for columns:</p>"
                + "<pre><code>for (int row = 0; row < matrix.length; row++) {\n    for (int col = 0; col < matrix[row].length; col++) {\n        System.out.print(matrix[row][col] + \" \");\n    }\n    System.out.println();\n}</code></pre>"
                + "<p>Jagged arrays (rows of different lengths) are allowed — each <code>matrix[i]</code> is just an array.</p>",

                story(
                    n("Eldrin unrolls a map of the Academy's potion storage vault — a vast grid of rows and columns, each cell holding a different ingredient."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "One dimension is a shelf. Two dimensions are a room of shelves. You address any cell with two coordinates — row first, then column. Always row, then column."),
                    e("Identity matrix", "int[][] id = {\n    {1, 0, 0},\n    {0, 1, 0},\n    {0, 0, 1}\n};\nfor (int[] row : id) {\n    for (int val : row)\n        System.out.print(val + \" \");\n    System.out.println();\n}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Nested loops are the incantation for 2D arrays. The outer loop walks the rows; the inner loop walks each cell within the row. Never confuse the two.")
                ),

                "<p>Create a 3×3 multiplication table: <code>grid[i][j] = (i+1) * (j+1)</code>. "
                + "Print each row with values separated by spaces. Expected output:</p>"
                + "<pre>1 2 3 \n2 4 6 \n3 6 9 </pre>",

                "public class MultiArrays {\n    public static void main(String[] args) {\n        int[][] grid = new int[3][3];\n        // Fill the grid: grid[i][j] = (i+1) * (j+1)\n\n        // Print each row\n\n    }\n}\n",

                tests(
                    test("Multiplication table", "null", "1 2 3 \n2 4 6 \n3 6 9 ")
                ),

                "How do you declare, initialise, and iterate over a 2D array in Java? Give an example of a real-world problem it would model well."
        );

        mcQuestion("G3", QuestionTier.RECALL,
                "<p>Given <code>int[][] m = new int[4][6]</code>, how many elements does it contain?</p>",
                new String[]{"24", "10", "46", "12"},
                "24",
                "<p>4 rows × 6 columns = 24 elements.</p>");

        codeQuestion("G3", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this print?</p>",
                "int[][] m = {{1,2},{3,4},{5,6}};\nSystem.out.println(m.length + \" \" + m[0].length);",
                "3 2",
                "<p><code>m.length</code> is the number of rows (3). <code>m[0].length</code> is the number of columns in row 0 (2).</p>");

        tfQuestion("G3", QuestionTier.DISCRIMINATION,
                "<p>All rows in a Java 2D array must have the same length.</p>",
                "False",
                "<p>Java supports <em>jagged arrays</em> — each row is an independent array and can have a different length. However, rectangular 2D arrays (all rows same length) are far more common.</p>");
    }

    // =========================================================================
    // CHUNK H — ArrayList & HashMap
    // =========================================================================
    private void seedChunkH() {
        chunk("H", "Collections: List & Map", "\uD83C\uDF92", 8, LearnerPath.FOUNDATION, "G");

        // H1 — ArrayList
        subChunk("H1", "H", "ArrayList", 1, 50, "ArrayListEx.java",

                "<p>Arrays are great — but what if you don't know how many items you'll have? What if the number changes at runtime?</p>",

                "<h3>ArrayList: Dynamic Lists</h3>"
                + "<p><code>ArrayList</code> is a resizable array. Unlike a plain array, it grows automatically as you add items.</p>"
                + "<pre><code>import java.util.ArrayList;\n\nArrayList&lt;String&gt; spells = new ArrayList&lt;&gt;();\nspells.add(\"Fireball\");\nspells.add(\"Ice Lance\");\nspells.add(\"Thunder\");\n\nSystem.out.println(spells.size());    // 3\nSystem.out.println(spells.get(1));   // Ice Lance\nspells.remove(0);                     // removes Fireball\nSystem.out.println(spells);          // [Ice Lance, Thunder]</code></pre>"
                + "<p>Essential methods:</p>"
                + "<ul>"
                + "<li><code>add(item)</code> — append to end</li>"
                + "<li><code>add(index, item)</code> — insert at position</li>"
                + "<li><code>get(index)</code> — retrieve by index</li>"
                + "<li><code>set(index, item)</code> — replace at index</li>"
                + "<li><code>remove(index)</code> — delete by index</li>"
                + "<li><code>size()</code> — current count</li>"
                + "<li><code>contains(item)</code> — true if present</li>"
                + "</ul>"
                + "<p>The <code>&lt;String&gt;</code> is a <strong>type parameter</strong> — it tells Java what type of objects this list will hold, enabling compile-time type safety.</p>",

                story(
                    n("Eldrin reaches into his robe and pulls out what appears to be a small leather pouch — then keeps pulling, drawing out scroll after scroll far beyond what should fit."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "An ArrayList is an enchanted bag. You can keep adding scrolls without ever running out of space — it expands as needed. Unlike the fixed shelf, this container is <em>dynamic</em>."),
                    e("The enchanted bag", "import java.util.ArrayList;\nArrayList<String> bag = new ArrayList<>();\nbag.add(\"Fireball\");\nbag.add(\"Ice Lance\");\nbag.add(\"Thunder\");\nSystem.out.println(bag.size());   // 3\nSystem.out.println(bag.get(0));   // Fireball\nbag.remove(1);                    // removes Ice Lance\nSystem.out.println(bag);          // [Fireball, Thunder]"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Notice the angle brackets — <code>ArrayList&lt;String&gt;</code>. That is the <em>type parameter</em>. It locks the bag to hold only Strings. Try adding a number, and the compiler refuses.")
                ),

                "<p>Create an <code>ArrayList&lt;String&gt;</code> containing these four wizard names: "
                + "<code>\"Merlin\"</code>, <code>\"Gandalf\"</code>, <code>\"Dumbledore\"</code>, <code>\"Saruman\"</code>. "
                + "Remove <code>\"Saruman\"</code>, then print each remaining name on its own line.</p>",

                "import java.util.ArrayList;\n\npublic class ArrayListEx {\n    public static void main(String[] args) {\n        ArrayList<String> wizards = new ArrayList<>();\n        // Add the four names\n\n        // Remove Saruman\n\n        // Print each name\n\n    }\n}\n",

                tests(
                    test("Remaining wizards", "null", "Merlin\nGandalf\nDumbledore")
                ),

                "Explain ArrayList in Java. How does it differ from a plain array, and what are the three most commonly used methods?"
        );

        mcQuestion("H1", QuestionTier.RECALL,
                "<p>Which method returns the number of elements in an <code>ArrayList</code>?</p>",
                new String[]{"size()", "length", "count()", "length()"},
                "size()",
                "<p>For <code>ArrayList</code> (and all Java collections) use <code>size()</code>. Arrays use <code>.length</code> (a field, not a method).</p>");

        codeQuestion("H1", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this print?</p>",
                "import java.util.ArrayList;\nArrayList<Integer> nums = new ArrayList<>();\nnums.add(10); nums.add(20); nums.add(30);\nnums.remove(1);\nSystem.out.println(nums);",
                "[10, 30]",
                "<p><code>remove(1)</code> removes the element at index 1 (value 20), leaving [10, 30].</p>");

        tfQuestion("H1", QuestionTier.DISCRIMINATION,
                "<p><code>ArrayList&lt;int&gt;</code> is a valid declaration in Java.</p>",
                "False",
                "<p>Generics require <em>reference types</em>, not primitives. Use <code>ArrayList&lt;Integer&gt;</code> (the wrapper class). Java auto-boxes <code>int</code> ↔ <code>Integer</code> automatically.</p>");

        // H2 — HashMap
        subChunk("H2", "H", "HashMap", 2, 50, "HashMapEx.java",

                "<p>You need to look up a student's grade by their name, not by their position in a list. What you need is a <strong>map</strong>.</p>",

                "<h3>HashMap: Key-Value Storage</h3>"
                + "<p>A <code>HashMap</code> stores data as <em>key → value</em> pairs. Given the key, you can retrieve the value in O(1) time — instantly, regardless of how many entries the map holds.</p>"
                + "<pre><code>import java.util.HashMap;\n\nHashMap&lt;String, Integer&gt; scores = new HashMap&lt;&gt;();\nscores.put(\"Alice\", 95);\nscores.put(\"Bob\", 82);\nscores.put(\"Carol\", 91);\n\nSystem.out.println(scores.get(\"Bob\"));         // 82\nSystem.out.println(scores.containsKey(\"Dave\")); // false\nSystem.out.println(scores.size());             // 3\nscores.remove(\"Bob\");</code></pre>"
                + "<p>Key facts:</p>"
                + "<ul>"
                + "<li>Keys must be <strong>unique</strong> — putting the same key again overwrites the value</li>"
                + "<li><code>get(key)</code> returns <code>null</code> if the key doesn't exist</li>"
                + "<li>Order is <strong>not guaranteed</strong> — use <code>LinkedHashMap</code> to preserve insertion order</li>"
                + "<li><code>getOrDefault(key, fallback)</code> avoids null checks</li>"
                + "</ul>",

                story(
                    n("Eldrin gestures to a magical registry on the wall — a vast tome where every wizard's name is instantly linked to their power level."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A HashMap is the Academy registry. You look up any entry by <em>name</em> — the key — and instantly receive the <em>value</em> it maps to. No searching, no counting positions. Direct access."),
                    e("The registry", "import java.util.HashMap;\nHashMap<String, Integer> registry = new HashMap<>();\nregistry.put(\"Eldrin\", 9999);\nregistry.put(\"Apprentice\", 10);\nSystem.out.println(registry.get(\"Eldrin\"));    // 9999\nSystem.out.println(registry.get(\"Unknown\"));   // null"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Always check for null or use <code>getOrDefault</code> when looking up keys that may not exist. A null result where you expected a number will give you a NullPointerException — the most common mistake with maps.")
                ),

                "<p>Create a <code>HashMap&lt;String, Integer&gt;</code> mapping three spell names to their power levels: "
                + "<code>\"Fireball\" → 75</code>, <code>\"Ice Lance\" → 60</code>, <code>\"Thunder\" → 90</code>. "
                + "Print the power of <code>\"Ice Lance\"</code>, then print whether <code>\"Shadow Bolt\"</code> is in the map.</p>",

                "import java.util.HashMap;\n\npublic class HashMapEx {\n    public static void main(String[] args) {\n        HashMap<String, Integer> powers = new HashMap<>();\n        // Add the three spells\n\n        // Print Ice Lance power\n\n        // Print whether Shadow Bolt exists\n\n    }\n}\n",

                tests(
                    test("Ice Lance power", "null", "60"),
                    test("Shadow Bolt absent", "null", "false")
                ),

                "Explain HashMap — what is a key, what is a value, and how does retrieval work? Why is HashMap faster than searching a list?"
        );

        mcQuestion("H2", QuestionTier.RECALL,
                "<p>What does <code>map.get(\"missingKey\")</code> return if the key doesn't exist?</p>",
                new String[]{"null", "0", "Throws an exception", "An empty string"},
                "null",
                "<p><code>HashMap.get()</code> returns <code>null</code> for missing keys. Use <code>getOrDefault(key, fallback)</code> to avoid dealing with null.</p>");

        codeQuestion("H2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this print?</p>",
                "import java.util.HashMap;\nHashMap<String, Integer> m = new HashMap<>();\nm.put(\"a\", 1);\nm.put(\"b\", 2);\nm.put(\"a\", 99);\nSystem.out.println(m.get(\"a\") + \" \" + m.size());",
                "99 2",
                "<p>Putting the same key twice overwrites the value. So <code>\"a\"</code> maps to 99. The map has 2 unique keys, so size is 2.</p>");

        tfQuestion("H2", QuestionTier.DISCRIMINATION,
                "<p>A <code>HashMap</code> guarantees that entries are iterated in the order they were inserted.</p>",
                "False",
                "<p><code>HashMap</code> does <strong>not</strong> guarantee insertion order. Use <code>LinkedHashMap</code> for insertion-ordered iteration.</p>");

        // H3 — Iterating & Choosing Collections
        subChunk("H3", "H", "Iterating Collections & Choosing the Right One", 3, 60, "Collections.java",

                "<p>You have an ArrayList and a HashMap — but how do you loop over them? And when do you choose one over the other?</p>",

                "<h3>Iterating Collections</h3>"
                + "<pre><code>// ArrayList — enhanced for\nfor (String name : nameList) {\n    System.out.println(name);\n}\n\n// HashMap — iterate entries\nfor (Map.Entry&lt;String, Integer&gt; entry : scores.entrySet()) {\n    System.out.println(entry.getKey() + \": \" + entry.getValue());\n}\n\n// HashMap — just keys\nfor (String key : scores.keySet()) {\n    System.out.println(key);\n}</code></pre>"
                + "<h3>Choosing the Right Container</h3>"
                + "<table style='border-collapse:collapse;width:100%'>"
                + "<tr><th>Structure</th><th>Use when</th><th>Access time</th></tr>"
                + "<tr><td><code>array</code></td><td>Fixed size, primitives, performance-critical</td><td>O(1) by index</td></tr>"
                + "<tr><td><code>ArrayList</code></td><td>Dynamic list, order matters, duplicates OK</td><td>O(1) by index, O(n) search</td></tr>"
                + "<tr><td><code>HashMap</code></td><td>Lookup by key, unique keys required</td><td>O(1) by key</td></tr>"
                + "<tr><td><code>HashSet</code></td><td>Unique elements only, fast membership test</td><td>O(1) contains</td></tr>"
                + "</table>",

                story(
                    n("Eldrin spreads four containers on the workbench: a fixed stone shelf, an enchanted bag, a magical registry, and a glowing orb that only holds unique crystals."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The wisest choice of container is made before you start filling it. Ask yourself: do I need order? Do I need fast lookups by name? Do I need unique elements only? Each answer points to a different container."),
                    e("Iterating both", "// List iteration\nfor (String spell : spellList) {\n    System.out.println(spell);\n}\n\n// Map iteration\nfor (Map.Entry<String, Integer> e : powerMap.entrySet()) {\n    System.out.println(e.getKey() + \" -> \" + e.getValue());\n}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Using a HashMap where an ArrayList would do is like carrying a full registry to check three names. Know your tools and use the right one.")
                ),

                "<p>Create an <code>ArrayList&lt;Integer&gt;</code> of five numbers: <code>3, 1, 4, 1, 5</code>. "
                + "Then create a <code>HashMap&lt;String, Integer&gt;</code> with two entries: "
                + "<code>\"min\" → 1</code>, <code>\"max\" → 5</code>. "
                + "Print the list size, then print the map's <code>max</code> entry.</p>",

                "import java.util.ArrayList;\nimport java.util.HashMap;\n\npublic class Collections {\n    public static void main(String[] args) {\n        ArrayList<Integer> nums = new ArrayList<>();\n        // Add five numbers\n\n        HashMap<String, Integer> stats = new HashMap<>();\n        // Add min and max entries\n\n        // Print list size\n        // Print the max value\n\n    }\n}\n",

                tests(
                    test("List size", "null", "5"),
                    test("Max value", "null", "5")
                ),

                "Describe three different Java collection types and explain a real-world scenario where you would choose each one."
        );

        mcQuestion("H3", QuestionTier.APPLICATION,
                "<p>You need to track whether each student has submitted their assignment. Which collection is most appropriate?</p>",
                new String[]{"HashMap<String, Boolean>", "ArrayList<String>", "int[]", "String[]"},
                "HashMap<String, Boolean>",
                "<p>A <code>HashMap&lt;String, Boolean&gt;</code> lets you look up any student's status by name in O(1). An ArrayList would require O(n) searching.</p>");

        codeQuestion("H3", QuestionTier.DISCRIMINATION, QuestionType.DEBUGGING,
                "<p>This code intends to print each entry in the map. What is wrong?</p>",
                "HashMap<String,Integer> m = new HashMap<>();\nm.put(\"a\",1);\nfor (String key : m) {\n    System.out.println(key);\n}",
                "Replace 'm' with 'm.keySet()' in the for-each (or use m.entrySet())",
                "<p>You cannot iterate a <code>HashMap</code> directly. You must iterate <code>m.keySet()</code>, <code>m.values()</code>, or <code>m.entrySet()</code>.</p>");

        scenarioQuestion("H3",
                "<p>Your application stores a leaderboard with thousands of player scores. You need to: "
                + "(1) quickly find any player's score by username, and "
                + "(2) display the top 10 players sorted by score. "
                + "Which approach best handles both needs?</p>",
                "Store scores in a HashMap<String,Integer> for fast lookups; separately maintain a sorted List for the leaderboard display",
                "<p>A <code>HashMap</code> handles O(1) lookup by username. For sorted display, maintain a sorted list or sort on demand. Using a single sorted list for both would give O(n) lookups.</p>",
                "H2", "H1");
    }

    // =========================================================================
    // CHUNK I — OOP: Classes & Objects
    // =========================================================================
    private void seedChunkI() {
        chunk("I", "OOP: Classes & Objects", "\uD83C\uDFDB\uFE0F", 9, LearnerPath.FOUNDATION, "D");

        // I1 — Classes & Objects
        subChunk("I1", "I", "Classes & Objects", 1, 50, "ClassBasics.java",

                "<p>Every wizard has a name, a power level, and a school of magic. How do you group this data together with the behaviours that act on it?</p>",

                "<h3>Classes: Blueprints for Objects</h3>"
                + "<p>A <strong>class</strong> is a blueprint. An <strong>object</strong> is an instance of that blueprint — a concrete thing created from it.</p>"
                + "<pre><code>public class Wizard {\n    // Fields (data)\n    String name;\n    int level;\n\n    // Method (behaviour)\n    void greet() {\n        System.out.println(\"I am \" + name + \", level \" + level);\n    }\n}\n\n// Create objects (instances)\nWizard w1 = new Wizard();\nw1.name = \"Merlin\";\nw1.level = 99;\nw1.greet(); // I am Merlin, level 99\n\nWizard w2 = new Wizard();\nw2.name = \"Apprentice\";\nw2.level = 1;</code></pre>"
                + "<p>Each object has its <em>own copy</em> of the fields — changing <code>w1.level</code> has no effect on <code>w2.level</code>.</p>",

                story(
                    n("Eldrin unrolls an enormous parchment labelled <em>Wizard Blueprint</em>. It shows fields for name, level, and school — but the parchment itself is not a wizard."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The class is the <strong>blueprint</strong>. The object is what you build from it. You build me — Archmage Eldrin — from the Wizard blueprint. You could build a hundred wizards from the same blueprint; each one is distinct."),
                    e("Blueprint and instances", "class Wizard {\n    String name;\n    int level;\n    void greet() {\n        System.out.println(\"I am \" + name);\n    }\n}\n\nWizard eldrin = new Wizard();\neldrin.name = \"Eldrin\";\neldrin.level = 9999;\neldrin.greet(); // I am Eldrin"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The <code>new</code> keyword is the incantation that brings the blueprint to life. It allocates memory and returns a reference to the newly created object.")
                ),

                "<p>Create a class <code>Spell</code> with fields <code>String name</code> and <code>int power</code>, "
                + "and a method <code>describe()</code> that prints <code>\"[name] deals [power] damage\"</code>. "
                + "Create two Spell objects and call <code>describe()</code> on each.</p>",

                "public class ClassBasics {\n    // Define the Spell class here\n\n    public static void main(String[] args) {\n        // Create two Spell objects and call describe()\n\n    }\n}\n",

                tests(
                    test("Fireball", "null", "Fireball deals 80 damage"),
                    test("Ice Lance", "null", "Ice Lance deals 55 damage")
                ),

                "Explain the relationship between a class and an object in Java using an analogy from everyday life."
        );

        mcQuestion("I1", QuestionTier.RECALL,
                "<p>What keyword is used to create a new object from a class in Java?</p>",
                new String[]{"new", "create", "make", "instantiate"},
                "new",
                "<p>The <code>new</code> keyword allocates memory for the object and calls its constructor.</p>");

        tfQuestion("I1", QuestionTier.RECALL,
                "<p>Changing a field on one object affects all other objects created from the same class.</p>",
                "False",
                "<p>Each object has its <strong>own independent copy</strong> of its instance fields. Changing one object's field has no effect on others (unless the field is <code>static</code>).</p>");

        codeQuestion("I1", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this print?</p>",
                "class Dog {\n    String name;\n    void bark() { System.out.println(name + \" says Woof!\"); }\n}\nDog d = new Dog();\nd.name = \"Rex\";\nd.bark();",
                "Rex says Woof!",
                "<p>The <code>name</code> field is set to \"Rex\" before <code>bark()</code> is called, so it prints \"Rex says Woof!\".</p>");

        // I2 — Constructors
        subChunk("I2", "I", "Constructors & the this Keyword", 2, 50, "Constructors.java",

                "<p>Setting fields one by one after creating an object is tedious and error-prone. Constructors solve this.</p>",

                "<h3>Constructors: Initialising Objects at Birth</h3>"
                + "<p>A constructor is a special method with the same name as the class and no return type. It runs automatically when you use <code>new</code>.</p>"
                + "<pre><code>public class Wizard {\n    String name;\n    int level;\n\n    // Constructor\n    public Wizard(String name, int level) {\n        this.name = name;   // 'this' refers to THIS object\n        this.level = level;\n    }\n}\n\n// Now creation and initialisation happen together\nWizard w = new Wizard(\"Merlin\", 99);</code></pre>"
                + "<p>The <code>this</code> keyword refers to the <em>current object</em>. Use it when a parameter has the same name as a field — without <code>this</code>, the parameter would shadow the field.</p>"
                + "<p><strong>Default constructor:</strong> if you write no constructor, Java silently provides a no-arg constructor. Once you write any constructor, the default disappears.</p>",

                story(
                    n("Eldrin taps the Wizard blueprint. Below the fields, a section reads <em>Initialisation Ritual</em>."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Every well-made object is initialised at the moment of creation. The constructor is that ritual. You provide the ingredients — name and level — and the object emerges fully formed, ready to use."),
                    e("Constructor in action", "class Spell {\n    String name;\n    int power;\n    Spell(String name, int power) {\n        this.name = name;\n        this.power = power;\n    }\n    void describe() {\n        System.out.println(name + \": \" + power + \" dmg\");\n    }\n}\nSpell s = new Spell(\"Fireball\", 80);\ns.describe(); // Fireball: 80 dmg"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Notice <code>this.name = name</code>. The <code>this</code> on the left refers to the field; the plain <code>name</code> on the right is the parameter. Without <code>this</code>, the field would never be set.")
                ),

                "<p>Create a class <code>Potion</code> with fields <code>String type</code> and <code>int strength</code>, "
                + "and a constructor that takes both. Add a method <code>use()</code> that prints "
                + "<code>\"Used [type] potion (+[strength] HP)\"</code>. Create and use two potions.</p>",

                "public class Constructors {\n    // Define Potion class with constructor\n\n    public static void main(String[] args) {\n        // Create a Health potion with strength 50\n        // Create a Mana potion with strength 30\n        // Call use() on each\n\n    }\n}\n",

                tests(
                    test("Health potion", "null", "Used Health potion (+50 HP)"),
                    test("Mana potion", "null", "Used Mana potion (+30 HP)")
                ),

                "What is a constructor in Java? Why do we use 'this' inside constructors, and what happens if you forget it?"
        );

        mcQuestion("I2", QuestionTier.RECALL,
                "<p>What does the <code>this</code> keyword refer to inside a method or constructor?</p>",
                new String[]{"The current object", "The parent class", "The class itself", "The calling method"},
                "The current object",
                "<p><code>this</code> is a reference to the object on which the method or constructor was invoked.</p>");

        codeQuestion("I2", QuestionTier.APPLICATION, QuestionType.DEBUGGING,
                "<p>This constructor does not correctly set the field. What is the bug?</p>",
                "class Cat {\n    String name;\n    Cat(String name) {\n        name = name; // bug!\n    }\n}",
                "Change to: this.name = name;",
                "<p>Without <code>this</code>, both sides refer to the local parameter. The field <code>name</code> is never assigned. Use <code>this.name = name</code> to distinguish field from parameter.</p>");

        tfQuestion("I2", QuestionTier.DISCRIMINATION,
                "<p>If you define a parameterised constructor in a Java class, Java still provides the no-argument constructor automatically.</p>",
                "False",
                "<p>Once you define any constructor, Java stops providing the default no-arg constructor. If you need both, define both explicitly.</p>");

        // I3 — toString & equals
        subChunk("I3", "I", "toString() and equals()", 3, 60, "ToStringEquals.java",

                "<p>When you print an object with <code>System.out.println(myObject)</code>, you get something like <code>Wizard@1b6d3586</code> — a memory address. That is rarely helpful.</p>",

                "<h3>Overriding toString() and equals()</h3>"
                + "<p><strong>toString()</strong> is called automatically whenever you concatenate or print an object. Override it to return a meaningful description:</p>"
                + "<pre><code>public class Wizard {\n    String name; int level;\n    Wizard(String name, int level) { this.name=name; this.level=level; }\n\n    @Override\n    public String toString() {\n        return \"Wizard{name='\" + name + \"', level=\" + level + \"}\";\n    }\n}\nSystem.out.println(new Wizard(\"Merlin\", 99));\n// Wizard{name='Merlin', level=99}</code></pre>"
                + "<p><strong>equals()</strong> determines if two objects are meaningfully the same. By default, <code>==</code> and <code>equals()</code> both compare memory addresses:</p>"
                + "<pre><code>@Override\npublic boolean equals(Object obj) {\n    if (this == obj) return true;\n    if (!(obj instanceof Wizard)) return false;\n    Wizard other = (Wizard) obj;\n    return this.name.equals(other.name) && this.level == other.level;\n}</code></pre>"
                + "<p>Always override both <code>equals()</code> and <code>hashCode()</code> together — they form a contract used by collections like <code>HashMap</code>.</p>",

                story(
                    n("Eldrin prints two wizard scrolls. Both show <code>Wizard@4e50df2e</code> — completely useless information."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Java's default <code>toString()</code> shows a memory address — the vault room and shelf number, not the wizard's identity. Override it to tell Java how your object should introduce itself."),
                    e("Meaningful toString", "class Wizard {\n    String name; int level;\n    Wizard(String n, int l) { name=n; level=l; }\n    @Override\n    public String toString() {\n        return name + \" (Lv.\" + level + \")\";\n    }\n}\nSystem.out.println(new Wizard(\"Eldrin\", 99)); // Eldrin (Lv.99)"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "And <code>equals()</code> — by default it checks if two variables point to the <em>same object in memory</em>. Override it to check if two objects represent the same <em>value</em>.")
                ),

                "<p>Create a <code>Point</code> class with <code>int x</code> and <code>int y</code>. Override <code>toString()</code> "
                + "to return <code>\"(x, y)\"</code>. Override <code>equals()</code> to return true when both coordinates match. "
                + "Print two points and check if they are equal.</p>",

                "public class ToStringEquals {\n    static class Point {\n        int x, y;\n        Point(int x, int y) { this.x = x; this.y = y; }\n        // Override toString()\n\n        // Override equals()\n\n    }\n    public static void main(String[] args) {\n        Point p1 = new Point(3, 4);\n        Point p2 = new Point(3, 4);\n        Point p3 = new Point(1, 2);\n        System.out.println(p1);           // (3, 4)\n        System.out.println(p1.equals(p2)); // true\n        System.out.println(p1.equals(p3)); // false\n    }\n}\n",

                tests(
                    test("toString", "null", "(3, 4)"),
                    test("equals true", "null", "true"),
                    test("equals false", "null", "false")
                ),

                "Why should you override both equals() and hashCode() together in Java? What can go wrong if you only override one?"
        );

        mcQuestion("I3", QuestionTier.RECALL,
                "<p>When is <code>toString()</code> called automatically in Java?</p>",
                new String[]{"When the object is printed or concatenated with a String", "When the object is created", "When equals() is called", "When the object is compared with =="},
                "When the object is printed or concatenated with a String",
                "<p><code>toString()</code> is called by <code>System.out.println(obj)</code> and string concatenation like <code>\"Value: \" + obj</code>.</p>");

        codeQuestion("I3", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this print? (No toString override)</p>",
                "class Box {}\nBox b = new Box();\nSystem.out.println(b == b);\nBox c = new Box();\nSystem.out.println(b == c);",
                "true\nfalse",
                "<p><code>b == b</code> compares a reference to itself — always true. <code>b == c</code> compares two different objects — different memory addresses, so false.</p>");
    }

    // =========================================================================
    // CHUNK J — OOP: Encapsulation
    // =========================================================================
    private void seedChunkJ() {
        chunk("J", "OOP: Encapsulation", "\uD83D\uDD12", 10, LearnerPath.FOUNDATION, "I");

        // J1 — Access Modifiers
        subChunk("J1", "J", "Access Modifiers", 1, 50, "AccessModifiers.java",

                "<p>If any code anywhere can directly change your object's fields, bugs become impossible to trace. Encapsulation is the solution.</p>",

                "<h3>Controlling Access to Your Class</h3>"
                + "<p>Java has four access levels:</p>"
                + "<table style='border-collapse:collapse;width:100%'>"
                + "<tr><th>Modifier</th><th>Same class</th><th>Same package</th><th>Subclass</th><th>Anywhere</th></tr>"
                + "<tr><td><code>private</code></td><td>✓</td><td>✗</td><td>✗</td><td>✗</td></tr>"
                + "<tr><td><code>package</code> (default)</td><td>✓</td><td>✓</td><td>✗</td><td>✗</td></tr>"
                + "<tr><td><code>protected</code></td><td>✓</td><td>✓</td><td>✓</td><td>✗</td></tr>"
                + "<tr><td><code>public</code></td><td>✓</td><td>✓</td><td>✓</td><td>✓</td></tr>"
                + "</table>"
                + "<p>The golden rule: make fields <code>private</code>, expose behaviour through <code>public</code> methods. This is the core of encapsulation.</p>"
                + "<pre><code>public class BankAccount {\n    private double balance; // nobody can touch this directly\n\n    public void deposit(double amount) {\n        if (amount > 0) balance += amount;\n    }\n\n    public double getBalance() { return balance; }\n}</code></pre>",

                story(
                    n("Eldrin leads you to the Academy vault. A sign reads: <em>Private — Headmaster access only</em>."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Access modifiers are the locks on your rooms. <code>private</code> means only you may enter. <code>public</code> opens the door to the world. Without locks, any passing apprentice could tamper with your most sensitive artefacts."),
                    e("The locked vault", "class BankAccount {\n    private double balance;\n    public void deposit(double amount) {\n        if (amount > 0) balance += amount;\n    }\n    public double getBalance() { return balance; }\n}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Notice: <code>balance</code> is private. Outsiders cannot set it to -1000. They can only interact through the controlled <code>deposit()</code> method — which validates the amount first.")
                ),

                "<p>Create a class <code>BankAccount</code> with a <code>private double balance</code>. "
                + "Add a <code>deposit(double amount)</code> method (only deposits positive amounts), "
                + "a <code>withdraw(double amount)</code> method (only if balance is sufficient), "
                + "and a <code>getBalance()</code> method. Demonstrate with a deposit of 100, withdrawal of 30, and print the final balance.</p>",

                "public class AccessModifiers {\n    static class BankAccount {\n        private double balance;\n        // Add deposit, withdraw, getBalance methods\n\n    }\n    public static void main(String[] args) {\n        BankAccount account = new BankAccount();\n        account.deposit(100);\n        account.withdraw(30);\n        System.out.println(account.getBalance());\n    }\n}\n",

                tests(
                    test("Final balance", "null", "70.0")
                ),

                "Explain the four access modifiers in Java. Why is 'private' the recommended choice for class fields?"
        );

        mcQuestion("J1", QuestionTier.RECALL,
                "<p>Which access modifier makes a field visible only within the class it is declared in?</p>",
                new String[]{"private", "protected", "public", "default"},
                "private",
                "<p><code>private</code> restricts access to the declaring class only — the strictest access level.</p>");

        tfQuestion("J1", QuestionTier.RECALL,
                "<p>Making a field <code>public</code> is generally considered good practice in Java classes.</p>",
                "False",
                "<p>Public fields violate encapsulation — any external code can change them directly, bypassing validation. Fields should be <code>private</code> with access controlled through methods.</p>");

        // J2 — Getters, Setters & Static Members
        subChunk("J2", "J", "Getters, Setters & Static Members", 2, 60, "GettersSetters.java",

                "<p>Private fields are safely locked away — but how does the outside world read or update them? Through <em>getters</em> and <em>setters</em>.</p>",

                "<h3>Getters, Setters, and Static</h3>"
                + "<p><strong>Getters</strong> return the field value; <strong>setters</strong> accept a new value, usually with validation:</p>"
                + "<pre><code>public class Player {\n    private int health;\n\n    public int getHealth() { return health; }\n\n    public void setHealth(int health) {\n        if (health < 0) health = 0;\n        if (health > 100) health = 100;\n        this.health = health;\n    }\n}</code></pre>"
                + "<p><strong>Static members</strong> belong to the class, not to any instance. All objects share the same static field:</p>"
                + "<pre><code>public class Wizard {\n    private static int count = 0; // shared across all Wizards\n    private String name;\n\n    public Wizard(String name) {\n        this.name = name;\n        count++; // incremented every time a new Wizard is created\n    }\n\n    public static int getCount() { return count; }\n}</code></pre>"
                + "<p>Call static methods on the class, not on instances: <code>Wizard.getCount()</code>.</p>",

                story(
                    n("Eldrin shows you two controls on the Academy's power regulator: a dial to read the current level (getter), and a lever to set it (setter) — but the lever has stops preventing dangerously low or high values."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A setter without validation is just a public field in disguise. The <em>point</em> of a setter is to enforce rules. Always ask: what invariants must hold on this data?"),
                    e("Validated setter", "class Player {\n    private int health = 100;\n    public int getHealth() { return health; }\n    public void setHealth(int h) {\n        this.health = Math.max(0, Math.min(100, h));\n    }\n}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Static members are the shared runes of the academy — every wizard reads from the same tome. Use them for counters, constants (<code>static final</code>), and utility methods that don't need any object state.")
                ),

                "<p>Create a <code>Wizard</code> class with: a private <code>static int count</code>, "
                + "a private <code>String name</code>, a constructor that increments <code>count</code>, "
                + "and a static <code>getCount()</code> method. Create three Wizards and print the count.</p>",

                "public class GettersSetters {\n    static class Wizard {\n        private static int count = 0;\n        private String name;\n        // Constructor (increments count)\n\n        // Static getCount()\n\n    }\n    public static void main(String[] args) {\n        new Wizard(\"Merlin\");\n        new Wizard(\"Gandalf\");\n        new Wizard(\"Dumbledore\");\n        System.out.println(Wizard.getCount());\n    }\n}\n",

                tests(
                    test("Wizard count", "null", "3")
                ),

                "What is the difference between an instance variable and a static variable in Java? Give an example of when you'd use each."
        );

        mcQuestion("J2", QuestionTier.RECALL,
                "<p>A static method in Java can access which of the following directly?</p>",
                new String[]{"Only static fields and other static methods", "Instance fields of any object", "Both instance and static fields", "Only final fields"},
                "Only static fields and other static methods",
                "<p>Static methods belong to the class, not to any instance. They cannot access instance fields or methods without a reference to an object.</p>");

        codeQuestion("J2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this print?</p>",
                "class Counter {\n    static int n = 0;\n    Counter() { n++; }\n}\nnew Counter(); new Counter(); new Counter();\nSystem.out.println(Counter.n);",
                "3",
                "<p>The static field <code>n</code> is shared by all instances. Each constructor increments it, so after 3 objects, <code>n</code> equals 3.</p>");

        tfQuestion("J2", QuestionTier.DISCRIMINATION,
                "<p>You can override a static method in a subclass in Java.</p>",
                "False",
                "<p>Static methods belong to the class, not to instances, so they cannot be overridden — only <em>hidden</em>. Polymorphism does not apply to static methods.</p>");
    }

    // =========================================================================
    // CHUNK K — OOP: Inheritance
    // =========================================================================
    private void seedChunkK() {
        chunk("K", "OOP: Inheritance", "\uD83C\uDF33", 11, LearnerPath.FOUNDATION, "J");

        // K1 — Extends
        subChunk("K1", "K", "Extending Classes", 1, 50, "Inheritance.java",

                "<p>A FireWizard and an IceWizard are both Wizards — they share name and level, but each has unique abilities. Must you copy-paste the shared code?</p>",

                "<h3>Inheritance: Reuse Through IS-A Relationships</h3>"
                + "<p>Use <code>extends</code> to create a class that inherits all fields and methods from a parent class:</p>"
                + "<pre><code>public class Wizard {\n    protected String name;\n    protected int level;\n\n    public Wizard(String name, int level) {\n        this.name = name;\n        this.level = level;\n    }\n\n    public void greet() {\n        System.out.println(\"I am \" + name);\n    }\n}\n\npublic class FireWizard extends Wizard {\n    private int firepower;\n\n    public FireWizard(String name, int level, int firepower) {\n        super(name, level); // call parent constructor\n        this.firepower = firepower;\n    }\n\n    public void castFireball() {\n        System.out.println(name + \" casts Fireball for \" + firepower + \" damage!\");\n    }\n}</code></pre>"
                + "<p>Key points:</p>"
                + "<ul>"
                + "<li>The child class gets all <code>public</code> and <code>protected</code> members of the parent</li>"
                + "<li><code>super(args)</code> calls the parent constructor — must be the first line in child constructor</li>"
                + "<li>Java supports single inheritance only (one parent class)</li>"
                + "<li>Every class implicitly extends <code>Object</code> if no parent is specified</li>"
                + "</ul>",

                story(
                    n("Eldrin unrolls the Academy's great lineage scroll. At the top: <em>Wizard</em>. Below it, branching like a tree: FireWizard, IceWizard, StormWizard, each inheriting the foundation."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Inheritance models an IS-A relationship. A FireWizard <em>is a</em> Wizard. It inherits everything — name, level, the ability to greet — and adds its own specialisation on top."),
                    e("FireWizard in action", "class Wizard {\n    protected String name;\n    Wizard(String n) { name = n; }\n    void greet() { System.out.println(\"I am \" + name); }\n}\nclass FireWizard extends Wizard {\n    FireWizard(String n) { super(n); }\n    void castFireball() { System.out.println(name + \" casts Fireball!\"); }\n}\nFireWizard fw = new FireWizard(\"Ignis\");\nfw.greet();       // I am Ignis  (inherited)\nfw.castFireball(); // Ignis casts Fireball!"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Notice <code>super(n)</code> — the first thing any child constructor does is call its parent's constructor. The parent must be fully built before the child can add its own enchantments.")
                ),

                "<p>Create a class <code>Animal</code> with a <code>protected String name</code> and a method <code>speak()</code> that prints <code>\"...\"</code>. "
                + "Create <code>Dog extends Animal</code> with its own <code>speak()</code> that prints <code>\"[name] says: Woof!\"</code>. "
                + "Create <code>Cat extends Animal</code> printing <code>\"[name] says: Meow!\"</code>. "
                + "Construct one of each and call <code>speak()</code>.</p>",

                "public class Inheritance {\n    static class Animal {\n        protected String name;\n        Animal(String name) { this.name = name; }\n        void speak() { System.out.println(\"...\"); }\n    }\n    // Define Dog and Cat extending Animal\n\n    public static void main(String[] args) {\n        Animal dog = new Dog(\"Rex\");\n        Animal cat = new Cat(\"Whiskers\");\n        dog.speak();\n        cat.speak();\n    }\n}\n",

                tests(
                    test("Dog speaks", "null", "Rex says: Woof!"),
                    test("Cat speaks", "null", "Whiskers says: Meow!")
                ),

                "Explain inheritance in Java. What is the IS-A relationship, and what does the super() call do in a child constructor?"
        );

        mcQuestion("K1", QuestionTier.RECALL,
                "<p>Which keyword is used in Java to inherit from a parent class?</p>",
                new String[]{"extends", "implements", "inherits", "super"},
                "extends",
                "<p><code>extends</code> establishes an inheritance relationship. <code>implements</code> is used for interfaces, not class inheritance.</p>");

        tfQuestion("K1", QuestionTier.RECALL,
                "<p>A child class in Java can extend multiple parent classes simultaneously.</p>",
                "False",
                "<p>Java supports only <strong>single inheritance</strong> for classes. A class can extend only one parent. Multiple interface implementation is allowed instead.</p>");

        // K2 — Overriding & Abstract
        subChunk("K2", "K", "Overriding, Abstract & final", 2, 60, "Overriding.java",

                "<p>The parent's <code>speak()</code> prints <code>\"...\"</code> — useless for a Dog. You need the child to replace the parent's implementation.</p>",

                "<h3>Method Overriding</h3>"
                + "<p>A child class <em>overrides</em> a parent method by declaring a method with the same signature. Use <code>@Override</code> to let the compiler verify you're doing it correctly:</p>"
                + "<pre><code>class Animal {\n    void speak() { System.out.println(\"...\"); }\n}\nclass Dog extends Animal {\n    @Override\n    void speak() { System.out.println(\"Woof!\"); }\n}</code></pre>"
                + "<p>Call the parent's version using <code>super.speak()</code> if you want to extend rather than replace it.</p>"
                + "<h3>Abstract Classes</h3>"
                + "<p>An <code>abstract</code> class cannot be instantiated directly — it exists to be extended. <code>abstract</code> methods have no body; subclasses <em>must</em> provide them:</p>"
                + "<pre><code>abstract class Shape {\n    abstract double area(); // no body\n\n    void describe() { // concrete method\n        System.out.println(\"Area: \" + area());\n    }\n}\nclass Circle extends Shape {\n    double radius;\n    Circle(double r) { radius = r; }\n    @Override double area() { return Math.PI * radius * radius; }\n}</code></pre>"
                + "<p>The <code>final</code> keyword prevents extension (<code>final class</code>), overriding (<code>final void method()</code>), or reassignment (<code>final int X = 5</code>).</p>",

                story(
                    n("Eldrin draws the Shape archetype on the board — no specific form, just a promise that every shape will know its own area."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "An abstract class is an <em>incomplete</em> blueprint — a template that mandates certain behaviours without specifying how. You cannot conjure a raw Shape, but you can conjure a Circle or Rectangle that fulfils the Shape contract."),
                    e("Shape hierarchy", "abstract class Shape {\n    abstract double area();\n    void print() {\n        System.out.printf(\"Area: %.2f%n\", area());\n    }\n}\nclass Circle extends Shape {\n    double r;\n    Circle(double r) { this.r = r; }\n    @Override double area() { return Math.PI * r * r; }\n}\nnew Circle(5).print(); // Area: 78.54"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "And <code>final</code> — the seal of completion. A <code>final</code> class may not be extended; its design is closed. Use it when you must guarantee a class's behaviour can never be altered by subclasses.")
                ),

                "<p>Create abstract class <code>Shape</code> with abstract method <code>area()</code>. "
                + "Implement <code>Rectangle</code> (width × height) and <code>Triangle</code> (0.5 × base × height). "
                + "Create one of each and call <code>area()</code>.</p>",

                "public class Overriding {\n    abstract static class Shape {\n        abstract double area();\n    }\n    // Implement Rectangle and Triangle\n\n    public static void main(String[] args) {\n        Shape r = new Rectangle(4, 5);\n        Shape t = new Triangle(6, 3);\n        System.out.println(r.area()); // 20.0\n        System.out.println(t.area()); // 9.0\n    }\n}\n",

                tests(
                    test("Rectangle area", "null", "20.0"),
                    test("Triangle area", "null", "9.0")
                ),

                "What is an abstract class in Java? How does it differ from a concrete class and from an interface?"
        );

        mcQuestion("K2", QuestionTier.RECALL,
                "<p>What does the <code>@Override</code> annotation do?</p>",
                new String[]{"Asks the compiler to verify you are correctly overriding a parent method", "Forces the method to replace all parent code", "Makes the method run faster", "Hides the parent method permanently"},
                "Asks the compiler to verify you are correctly overriding a parent method",
                "<p><code>@Override</code> is optional but recommended — it tells the compiler to check that a matching method exists in the parent. If not, it gives a compile error, preventing silent bugs from typos.</p>");

        codeQuestion("K2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this print?</p>",
                "class A { void hello() { System.out.println(\"A\"); } }\nclass B extends A {\n    @Override void hello() {\n        super.hello();\n        System.out.println(\"B\");\n    }\n}\nnew B().hello();",
                "A\nB",
                "<p><code>super.hello()</code> calls the parent version first (prints \"A\"), then the child version continues (prints \"B\").</p>");

        mcQuestion("K2", QuestionTier.DISCRIMINATION,
                "<p>Which of the following statements about <code>abstract</code> classes is correct?</p>",
                new String[]{
                    "An abstract class can have both abstract and concrete methods",
                    "An abstract class must have only abstract methods",
                    "You can create instances of an abstract class directly",
                    "An abstract class cannot have constructors"
                },
                "An abstract class can have both abstract and concrete methods",
                "<p>Abstract classes can mix abstract methods (no body, must be overridden) with concrete methods (have a body, can be inherited or overridden). They cannot be instantiated directly but can have constructors for subclasses to call via <code>super()</code>.</p>");
    }

    // =========================================================================
    // CHUNK L — OOP: Interfaces & Polymorphism
    // =========================================================================
    private void seedChunkL() {
        chunk("L", "OOP: Interfaces & Polymorphism", "\u2728", 12, LearnerPath.FOUNDATION, "K");

        // L1 — Interfaces
        subChunk("L1", "L", "Interfaces", 1, 50, "Interfaces.java",

                "<p>A Wizard and a Dragon have nothing in common in terms of class hierarchy — but both can be healed. How do you express a shared capability without forcing a shared parent?</p>",

                "<h3>Interfaces: Contracts for Behaviour</h3>"
                + "<p>An <code>interface</code> defines a contract — a set of methods that any implementing class must provide:</p>"
                + "<pre><code>public interface Healable {\n    void heal(int amount);\n    int getHealth();\n}\n\npublic class Wizard implements Healable {\n    private int health = 100;\n    @Override public void heal(int amount) { health += amount; }\n    @Override public int getHealth() { return health; }\n}\n\npublic class Dragon implements Healable {\n    private int health = 500;\n    @Override public void heal(int amount) { health += amount; }\n    @Override public int getHealth() { return health; }\n}</code></pre>"
                + "<p>Key characteristics of interfaces:</p>"
                + "<ul>"
                + "<li>All methods are implicitly <code>public abstract</code> (before Java 8)</li>"
                + "<li>All fields are implicitly <code>public static final</code></li>"
                + "<li>A class can <code>implement</code> <strong>multiple</strong> interfaces</li>"
                + "<li>An interface cannot be instantiated directly</li>"
                + "<li>Java 8+ allows <code>default</code> methods with implementations</li>"
                + "</ul>",

                story(
                    n("Eldrin taps two completely different creatures — a Wizard and a Stone Golem — and says they both signed the same Healable contract."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "An interface is a <em>contract</em>. I do not care what you are — Wizard, Dragon, Golem — as long as you promise to implement these methods. Unlike a class, you can sign as many contracts as you wish."),
                    e("Multiple interfaces", "interface Flyable { void fly(); }\ninterface Healable { void heal(int hp); }\n\nclass Dragon implements Flyable, Healable {\n    public void fly() { System.out.println(\"Dragon soars!\"); }\n    public void heal(int hp) { System.out.println(\"Dragon healed \" + hp); }\n}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A class may implement many interfaces — that is Java's answer to the limitation of single inheritance. Interfaces are contracts; a class can hold many contracts simultaneously.")
                ),

                "<p>Define an interface <code>Describable</code> with one method: <code>String describe()</code>. "
                + "Implement it in two classes: <code>Weapon</code> (returns <code>\"Sword: 40 dmg\"</code>) "
                + "and <code>Armour</code> (returns <code>\"Shield: 30 def\"</code>). "
                + "Store both in a <code>Describable[]</code> array and print each description.</p>",

                "public class Interfaces {\n    interface Describable {\n        String describe();\n    }\n    // Implement Weapon and Armour\n\n    public static void main(String[] args) {\n        Describable[] items = { new Weapon(), new Armour() };\n        for (Describable d : items) {\n            System.out.println(d.describe());\n        }\n    }\n}\n",

                tests(
                    test("Weapon", "null", "Sword: 40 dmg"),
                    test("Armour", "null", "Shield: 30 def")
                ),

                "What is a Java interface? How does it differ from an abstract class, and why would you prefer an interface?"
        );

        mcQuestion("L1", QuestionTier.RECALL,
                "<p>How many interfaces can a Java class implement?</p>",
                new String[]{"Any number", "Only one", "Maximum two", "Only as many as it has methods"},
                "Any number",
                "<p>A class can implement any number of interfaces, separated by commas: <code>class Foo implements A, B, C { ... }</code>.</p>");

        tfQuestion("L1", QuestionTier.RECALL,
                "<p>You can create an instance of a Java interface directly using <code>new</code>.</p>",
                "False",
                "<p>Interfaces cannot be instantiated. You create instances of <em>classes that implement</em> the interface. (Anonymous classes and lambda expressions can provide inline implementations.)</p>");

        // L2 — Polymorphism
        subChunk("L2", "L", "Polymorphism", 2, 60, "Polymorphism.java",

                "<p>You have a list of different creatures — all different types but all implementing the same interface. Can you call the same method on all of them?</p>",

                "<h3>Polymorphism: One Interface, Many Forms</h3>"
                + "<p><em>Polymorphism</em> means you can use a parent type or interface to refer to any subclass/implementor, and the correct method is called at runtime:</p>"
                + "<pre><code>// Using the parent type\nAnimal a1 = new Dog(\"Rex\");  // Dog IS-A Animal\nAnimal a2 = new Cat(\"Luna\");\na1.speak(); // Woof! — calls Dog's speak()\na2.speak(); // Meow! — calls Cat's speak()</code></pre>"
                + "<p>This enables you to write code against the abstraction, not the concrete type — the key to flexible, extensible design:</p>"
                + "<pre><code>// Works with ANY Animal subclass — now and in future\nvoid makeNoise(Animal animal) {\n    animal.speak();\n}\n\nmakeNoise(new Dog(\"Rex\"));  // Woof!\nmakeNoise(new Cat(\"Luna\")); // Meow!</code></pre>"
                + "<p><strong>Casting:</strong> use <code>instanceof</code> before casting down to a concrete type:</p>"
                + "<pre><code>if (a1 instanceof Dog d) {\n    d.fetch(); // Dog-specific method\n}</code></pre>",

                story(
                    n("Eldrin lines up a Wizard, a Dragon, and a Golem — all very different — and issues a single command."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Polymorphism is the power to give one command and have every creature respond in its own unique way. I say <code>speak()</code>; the Dragon roars, the Golem rumbles, the Wizard recites verse. One command — many forms."),
                    e("One command, many responses", "interface Being { void speak(); }\nclass Wizard implements Being {\n    public void speak() { System.out.println(\"Greetings!\"); }\n}\nclass Dragon implements Being {\n    public void speak() { System.out.println(\"ROOAAR!\"); }\n}\nBeing[] beings = { new Wizard(), new Dragon() };\nfor (Being b : beings) b.speak();"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "This is the magic of coding against an abstraction. Tomorrow you add a Golem — you need not change a single line of the loop. The new type simply fulfils the same contract.")
                ),

                "<p>Create an interface <code>Attacker</code> with method <code>void attack()</code>. "
                + "Implement it in <code>Swordsman</code> (prints <code>\"Swings sword!\"</code>), "
                + "<code>Archer</code> (prints <code>\"Shoots arrow!\"</code>), "
                + "and <code>Mage</code> (prints <code>\"Casts spell!\"</code>). "
                + "Store all three in a <code>List&lt;Attacker&gt;</code> and call <code>attack()</code> on each.</p>",

                "import java.util.List;\nimport java.util.ArrayList;\n\npublic class Polymorphism {\n    interface Attacker {\n        void attack();\n    }\n    // Implement Swordsman, Archer, Mage\n\n    public static void main(String[] args) {\n        List<Attacker> party = new ArrayList<>();\n        party.add(new Swordsman());\n        party.add(new Archer());\n        party.add(new Mage());\n        for (Attacker a : party) a.attack();\n    }\n}\n",

                tests(
                    test("Swordsman attacks", "null", "Swings sword!"),
                    test("Archer attacks", "null", "Shoots arrow!"),
                    test("Mage attacks", "null", "Casts spell!")
                ),

                "Explain polymorphism in Java. Why is it powerful to write code against an interface rather than a concrete class?"
        );

        mcQuestion("L2", QuestionTier.RECALL,
                "<p>What determines which version of an overridden method runs when called through a parent-type reference?</p>",
                new String[]{"The actual type of the object at runtime", "The declared type of the reference variable", "The most recently defined method", "The first class in the inheritance chain"},
                "The actual type of the object at runtime",
                "<p>This is <em>dynamic dispatch</em> — Java determines at runtime which method to call based on the object's actual type, not the declared type of the reference.</p>");

        codeQuestion("L2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this print?</p>",
                "class Base { void print() { System.out.println(\"Base\"); } }\nclass Child extends Base {\n    @Override void print() { System.out.println(\"Child\"); }\n}\nBase obj = new Child();\nobj.print();",
                "Child",
                "<p>The reference type is <code>Base</code>, but the actual object is a <code>Child</code>. At runtime, Java calls <code>Child</code>'s overridden <code>print()</code>.</p>");

        // L3 — Default Methods & Composition
        subChunk("L3", "L", "Default Methods & Composition vs Inheritance", 3, 60, "DefaultMethods.java",

                "<p>Java 8 added default methods to interfaces — and understanding when to use composition over inheritance separates good design from brittle design.</p>",

                "<h3>Interface Default Methods</h3>"
                + "<p><code>default</code> methods provide a body inside an interface, giving implementors a ready-made implementation they can override if needed:</p>"
                + "<pre><code>public interface Flyable {\n    void fly();\n\n    default void land() {\n        System.out.println(\"Landing gracefully...\");\n    }\n}\n\nclass Eagle implements Flyable {\n    @Override public void fly() { System.out.println(\"Eagle soars!\"); }\n    // land() is inherited from the interface\n}</code></pre>"
                + "<h3>Composition vs Inheritance</h3>"
                + "<p>Prefer <strong>composition</strong> (HAS-A) over inheritance (IS-A) when you want to mix behaviours flexibly:</p>"
                + "<pre><code>// Instead of class FlyingFighterWizard extends Wizard (fragile)\nclass Hero {\n    private Weapon weapon;   // HAS-A\n    private Armour  armour;  // HAS-A\n    private Spell   spell;   // HAS-A\n    // delegates to components\n    void attack() { weapon.use(); }\n}</code></pre>"
                + "<p>The classic rule: <em>favour composition over inheritance</em> — it creates looser coupling and more reusable parts.</p>",

                story(
                    n("Eldrin demonstrates an Eagle that flies — and lands for free without any extra work, courtesy of the Flyable contract's default method."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Default methods let you evolve an interface without breaking every class that implements it. Add a new default method and existing implementors get it automatically — they can override if they want, or accept the default."),
                    e("Default method", "interface Greetable {\n    String getName();\n    default void greet() {\n        System.out.println(\"Hello, I am \" + getName());\n    }\n}\nclass Wizard implements Greetable {\n    private String name;\n    Wizard(String n) { name = n; }\n    public String getName() { return name; }\n}\nnew Wizard(\"Eldrin\").greet(); // Hello, I am Eldrin"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "And composition — the wisest design pattern of all. A DragonRider is not a Dragon and not a Rider; it <em>has</em> a Dragon and <em>has</em> a Rider. Components can be swapped, tested, and reused independently.")
                ),

                "<p>Define interface <code>Printable</code> with abstract <code>String content()</code> and a <code>default void print()</code> "
                + "that prints <code>\"[content()]\"</code> (with square brackets). "
                + "Implement it in <code>Report</code> and <code>Invoice</code> classes with different content. "
                + "Call <code>print()</code> on both.</p>",

                "public class DefaultMethods {\n    interface Printable {\n        String content();\n        default void print() {\n            System.out.println(\"[\" + content() + \"]\");\n        }\n    }\n    // Implement Report and Invoice\n\n    public static void main(String[] args) {\n        new Report().print();\n        new Invoice().print();\n    }\n}\n",

                tests(
                    test("Report printed", "null", "[Monthly Report]"),
                    test("Invoice printed", "null", "[Invoice #42]")
                ),

                "What are default methods in Java interfaces, and why were they introduced? When would you prefer composition over inheritance?"
        );

        mcQuestion("L3", QuestionTier.RECALL,
                "<p>Which keyword is used to define a method with a body inside a Java interface?</p>",
                new String[]{"default", "concrete", "implemented", "body"},
                "default",
                "<p>The <code>default</code> keyword (introduced in Java 8) allows interface methods to have a default implementation that implementing classes can use or override.</p>");

        codeQuestion("L3", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this print?</p>",
                "interface Greeter {\n    String name();\n    default void greet() { System.out.println(\"Hi \" + name()); }\n}\nclass Bot implements Greeter {\n    public String name() { return \"R2D2\"; }\n}\nnew Bot().greet();",
                "Hi R2D2",
                "<p><code>Bot</code> inherits the <code>greet()</code> default method. When called, it invokes <code>name()</code> which <code>Bot</code> implements as \"R2D2\".</p>");

        scenarioQuestion("L3",
                "<p>You are building a game with a <code>Character</code> class. You need characters to be able to fly, swim, and cast spells — but not all characters do all three. "
                + "What design approach is most flexible?</p>",
                "Define Flyable, Swimmable, and Castable as interfaces; each character class implements the relevant ones",
                "<p>Interfaces model capabilities cleanly — a character implements only the interfaces matching its abilities. Using inheritance for this would require complex hierarchies or code duplication.</p>",
                "L1", "K1");
    }
}
