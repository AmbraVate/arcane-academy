package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.stereotype.Component;

/**
 * Seeds chunks C through K with structure (chunks + sub-chunks + minimal questions).
 * Full content for these chunks will be added incrementally.
 */
@Component
public class ChunkCtoKSeeder extends AbstractChunkSeeder {

    public ChunkCtoKSeeder(ChunkRepository chunkRepository, SubChunkRepository subChunkRepository,
                           QuestionRepository questionRepository, RabbitHoleModuleRepository rabbitHoleRepository) {
        super(chunkRepository, subChunkRepository, questionRepository, rabbitHoleRepository);
    }

    @Override
    public void seed() {
        seedChunkC();
        seedChunkD();
        seedChunkE();
        seedChunkF();
        seedChunkG();
        seedChunkH();
        seedChunkI();
        seedChunkJ();
        seedChunkK();
    }

    private void seedChunkC() {
        chunk("C", "Loops", "\uD83D\uDD04", 3, "B");

        subChunk("C1", "C", "For Loops", 1, 50, "ForLoops.java",
                "<p>How would you print every number from 1 to 100 without writing 100 lines of code?</p>",
                "<p>A <strong>for loop</strong> repeats a block of code a specific number of times. It has three parts: initialisation, condition, and update.</p><pre><code>for (int i = 0; i < 5; i++) {\n    System.out.println(i);\n}</code></pre><p>This prints 0, 1, 2, 3, 4. The loop starts at 0, checks if i < 5, runs the body, then increments i.</p>",
                story(
                      n("The ancient scrolls speak of <em>repetition spells</em> — incantations that echo themselves until their power is spent. Eldrin unfurls a long scroll covered in identical glyphs, each one cascading into the next. <em>This</em> is the power of the for loop."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "A <strong>for loop</strong> is a controlled repetition spell. You inscribe three runes into its header: an <strong>initialiser</strong> that sets the starting value, a <strong>condition</strong> that decides when to stop, and an <strong>update</strong> that advances each step."),
                      e("Counting incantation", "for (int i = 1; i <= 5; i++) {\n    System.out.println(\"Cast #\" + i);\n}")),
                "<p>Print the numbers 1 through 5, each on a new line.</p>",
                "// TODO: write a for loop that starts at 1, goes up to 5, and prints each number\n",
                tests(test("Prints 1-5", "null", "1\n2\n3\n4\n5")),
                "Explain how a for loop works to someone who has never programmed. What are its three parts?");

        mcQuestion("C1", QuestionTier.RECALL, "What are the three parts of a for loop?",
                new String[]{"Initialisation, condition, update", "Start, middle, end", "Input, process, output", "Declaration, assignment, return"},
                "Initialisation, condition, update", "A for loop has: initialisation (runs once), condition (checked each iteration), and update (runs after each iteration).");

        codeQuestion("C1", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "What does this code print?",
                "for (int i = 0; i < 3; i++) {\n    System.out.println(i * 2);\n}", "0\n2\n4",
                "The loop runs with i=0,1,2. Multiplying each by 2 gives 0, 2, 4.");

        subChunk("C2", "C", "While Loops", 2, 50, "WhileLoops.java",
                "<p>What if you don't know in advance how many times you need to repeat something?</p>",
                "<p>A <strong>while loop</strong> repeats as long as a condition is true. Use it when you don't know the exact number of iterations.</p><pre><code>int count = 0;\nwhile (count < 3) {\n    System.out.println(count);\n    count++;\n}</code></pre>",
                story(
                      n("A stone golem stands at the academy gate, repeating its patrol without end — until the sun rises and the condition is broken. Eldrin watches it trudge back and forth, nodding approvingly. <em>That</em> golem runs on a while loop."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "A <strong>while loop</strong> keeps casting so long as its <strong>condition</strong> remains true. Unlike a for loop, you don't need to know the count upfront — just define the rule that will eventually break the cycle."),
                      e("Guard patrol", "int energy = 3;\nwhile (energy > 0) {\n    System.out.println(\"Patrolling...\");\n    energy--;\n}")),
                "<p>Print numbers from 10 down to 1 using a while loop.</p>",
                "int n = 10;\n// TODO: write a while loop that prints n and counts down until n reaches 1\n",
                tests(test("Countdown", "null", "10\n9\n8\n7\n6\n5\n4\n3\n2\n1")),
                "Explain the difference between a for loop and a while loop. When would you choose one over the other?");

        mcQuestion("C2", QuestionTier.RECALL, "When does a while loop stop?",
                new String[]{"When its condition becomes false", "After a fixed number of iterations", "When it runs out of memory", "When you press stop"},
                "When its condition becomes false", "A while loop checks its condition before each iteration and stops when the condition is false.");

        subChunk("C3", "C", "Loop Control: Break & Continue", 3, 50, "LoopControl.java",
                "<p>What if you want to exit a loop early, or skip just one iteration?</p>",
                "<p><code>break</code> exits the loop entirely. <code>continue</code> skips the rest of the current iteration and moves to the next one.</p><pre><code>for (int i = 0; i < 10; i++) {\n    if (i == 5) break;\n    System.out.println(i);\n}</code></pre>",
                story(
                      n("Eldrin traces a glowing sigil in the air — a loop humming with repetition — then snaps his fingers. The spell <em>stops</em>. He begins again, this time merely flicking one iteration away and letting the rest continue. <em>Two different powers</em>, and both are yours to command."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "<code>break</code> <strong>shatters</strong> the entire loop immediately — the spell ends. <code>continue</code> merely <strong>skips the current beat</strong>, jumping straight to the next iteration. Choose wisely: one ends the song, the other skips a note."),
                      e("Skip and shatter", "for (int i = 1; i <= 10; i++) {\n    if (i == 7) break;       // stop entirely at 7\n    if (i % 2 != 0) continue; // skip odd numbers\n    System.out.println(i);\n}")),
                "<p>Print only the even numbers from 1 to 10 using continue to skip odd numbers.</p>",
                "for (int i = 1; i <= 10; i++) {\n    // TODO: use continue to skip odd numbers\n    System.out.println(i);\n}",
                tests(test("Even numbers", "null", "2\n4\n6\n8\n10")),
                "Explain break and continue in your own words. Give an example of when each would be useful.");

        mcQuestion("C3", QuestionTier.APPLICATION, "What does <code>break</code> do inside a loop?",
                new String[]{"Exits the loop entirely", "Skips the current iteration", "Restarts the loop", "Pauses the loop"},
                "Exits the loop entirely", "break immediately terminates the loop and continues with the code after the loop.");
    }

    private void seedChunkD() {
        chunk("D", "Methods", "\uD83D\uDDDD", 4, "B");

        subChunk("D1", "D", "Defining Methods", 1, 50, "Methods.java",
                "<p>If you write the same code in 5 places, what happens when you need to change it?</p>",
                "<p>A <strong>method</strong> is a reusable block of code with a name. Instead of repeating code, you call the method.</p><pre><code>static void greet() {\n    System.out.println(\"Hello!\");\n}\n// Call it:\ngreet();</code></pre>",
                story(
                      n("Eldrin gestures to a vast grimoire filled with labelled spells, each one written once but invoked a hundred times. <em>Every</em> great wizard learns early: never write the same incantation twice. That is why we have methods."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "A <strong>method</strong> is a named, reusable spell. You define it once with a <strong>return type</strong>, a <strong>name</strong>, and a body — then invoke it by name whenever you need it. No duplication, no chaos."),
                      e("A simple incantation", "static void greet() {\n    System.out.println(\"Greetings, apprentice!\");\n}\n\n// Call it:\ngreet();")),
                "<p>Create a method called <code>greet</code> that prints \"Welcome to the Academy!\" then call it from main.</p>",
                "import java.util.*;\n\npublic class Methods {\n    static void greet() {\n        // TODO: print \"Welcome to the Academy!\"\n    }\n    public static void main(String[] args) {\n        // TODO: call greet()\n    }\n}",
                tests(test("Greet", "null", "Welcome to the Academy!")),
                "Explain what a method is and why methods are useful. Use a real-world analogy.");

        mcQuestion("D1", QuestionTier.RECALL, "What keyword means a method returns nothing?",
                new String[]{"void", "null", "empty", "none"},
                "void", "The void keyword indicates a method does not return a value.");

        subChunk("D2", "D", "Parameters & Return Values", 2, 50, "MethodParams.java",
                "<p>What if your method needs different inputs each time it runs?</p>",
                "<p><strong>Parameters</strong> let you pass data into a method. <strong>Return values</strong> let a method send data back.</p><pre><code>static int add(int a, int b) {\n    return a + b;\n}\nint result = add(3, 4); // result is 7</code></pre>",
                story(
                      n("Eldrin casts a fireball — then pauses and casts it again, larger, hotter, aimed differently. <em>Same spell</em>, Eldrin says with a grin, <em>different ingredients</em>. That is what parameters give you: a spell that bends to your will."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "<strong>Parameters</strong> are the ingredients you hand to a method when you invoke it. A <strong>return value</strong> is what the method gives back — like a potion brewed from those ingredients. Declare the return type before the method name, and use <code>return</code> to send the result."),
                      e("Brew and return", "static int add(int a, int b) {\n    return a + b;\n}\n\nint total = add(3, 7); // total is 10")),
                "<p>Create a method <code>multiply</code> that takes two ints and returns their product. Print the result of multiplying 6 and 7.</p>",
                "import java.util.*;\n\npublic class MethodParams {\n    static int multiply(int a, int b) {\n        // TODO: return the product of a and b\n        return 0;\n    }\n    public static void main(String[] args) {\n        // TODO: print the result of calling multiply(6, 7)\n    }\n}",
                tests(test("Multiply", "null", "42")),
                "Explain the difference between a parameter and an argument in your own words.");

        mcQuestion("D2", QuestionTier.APPLICATION, "What does <code>return</code> do in a method?",
                new String[]{"Sends a value back to the caller", "Prints output to the console", "Ends the program", "Declares a variable"},
                "Sends a value back to the caller", "return exits the method and provides the result to whoever called it.");

        subChunk("D3", "D", "Method Overloading", 3, 50, "Overloading.java",
                "<p>Can two methods have the same name?</p>",
                "<p>Yes! <strong>Method overloading</strong> means having multiple methods with the same name but different parameter lists.</p><pre><code>static int add(int a, int b) { return a + b; }\nstatic double add(double a, double b) { return a + b; }</code></pre>",
                story(
                      n("A master alchemist has three versions of the same potion recipe: one for a single herb, one for two, one for a liquid base. The recipe is called <em>Brew</em> in all three cases. Eldrin taps the grimoire: this is overloading."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "<strong>Method overloading</strong> lets you define multiple methods with the <strong>same name</strong> but <strong>different parameter lists</strong>. Java chooses the right version at compile time based on what you pass in. Same incantation name — different ingredients, different power."),
                      e("Same name, different form", "static int add(int a, int b) { return a + b; }\nstatic double add(double a, double b) { return a + b; }\n\nadd(2, 3);       // calls int version\nadd(2.5, 1.5);   // calls double version")),
                "<p>Create two <code>describe</code> methods: one that takes a String (name) and one that takes a String and an int (name and age). Call both.</p>",
                "import java.util.*;\n\npublic class Overloading {\n    static void describe(String name) {\n        // TODO: print \"Name: \" + name\n    }\n    static void describe(String name, int age) {\n        // TODO: print \"Name: \" + name + \", Age: \" + age\n    }\n    public static void main(String[] args) {\n        // TODO: call describe with just a name\n        // TODO: call describe with a name and age\n    }\n}",
                tests(test("Overload 1", "null", "Name: Eldrin"), test("Overload 2", "null", "Age: 200")),
                "Explain method overloading. How does Java know which version of a method to call?");

        mcQuestion("D3", QuestionTier.RECALL, "What makes overloaded methods different from each other?",
                new String[]{"Their parameter lists", "Their names", "Their return types only", "Their access modifiers"},
                "Their parameter lists", "Overloaded methods must have different parameter types or counts. The name stays the same.");
    }

    private void seedChunkE() {
        chunk("E", "Arrays & Collections", "\uD83D\uDCDA", 5, "C", "D");

        subChunk("E1", "E", "Arrays", 1, 50, "Arrays.java",
                "<p>How do you store 100 student grades without creating 100 separate variables?</p>",
                "<p>An <strong>array</strong> holds a fixed number of values of the same type, accessed by index (starting at 0).</p><pre><code>int[] scores = {85, 92, 78, 95};\nSystem.out.println(scores[0]); // 85\nSystem.out.println(scores.length); // 4</code></pre>",
                story(
                      n("The Arcane Library's east wing holds hundreds of numbered cubbyholes, each containing exactly one scroll. Eldrin runs a finger along the row: cubby zero, cubby one, cubby two. <em>Fixed</em> in number, <em>precise</em> in access — that is the nature of an array."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "An <strong>array</strong> stores a <strong>fixed number</strong> of values of the <em>same type</em>, side by side in memory. Each slot has an <strong>index</strong> starting at <code>0</code>. Declare the size upfront — the shelves cannot grow."),
                      e("Shelf of power levels", "int[] power = {10, 40, 75, 90};\nSystem.out.println(power[0]); // 10\nSystem.out.println(power.length); // 4")),
                "<p>Create an array of 3 names and print each one on a new line using a for loop.</p>",
                "String[] names = {\"Eldrin\", \"Thara\", \"Korben\"};\n// TODO: write a for loop that iterates over names and prints each one\n",
                tests(test("Print names", "null", "Eldrin\nThara\nKorben")),
                "Explain what an array is and why array indices start at 0 in Java.");

        mcQuestion("E1", QuestionTier.RECALL, "What is the index of the first element in a Java array?",
                new String[]{"0", "1", "-1", "It depends on the array"},
                "0", "Java arrays are zero-indexed. The first element is at index 0.");

        subChunk("E2", "E", "ArrayList", 2, 50, "ArrayLists.java",
                "<p>What if you don't know how many items you'll need to store?</p>",
                "<p>An <strong>ArrayList</strong> is a resizable list. Unlike arrays, you can add and remove elements dynamically.</p><pre><code>ArrayList&lt;String&gt; list = new ArrayList&lt;&gt;();\nlist.add(\"apple\");\nlist.add(\"banana\");\nSystem.out.println(list.size()); // 2</code></pre>",
                story(
                      n("Eldrin produces an enchanted satchel from his robes. He drops in a potion, then a scroll, then three more potions. The bag simply <em>expands</em> each time. No pre-declared size, no overflow — it just grows. <em>This</em>, he says, is your ArrayList."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "An <strong>ArrayList</strong> is a <strong>resizable</strong> collection — unlike an array, it can grow and shrink at will. Use <code>add()</code> to insert, <code>remove()</code> to discard, and <code>size()</code> to check how many items it holds. Declare it with a type in angle brackets."),
                      e("The enchanted satchel", "ArrayList<String> spells = new ArrayList<>();\nspells.add(\"Fireball\");\nspells.add(\"Ice Lance\");\nSystem.out.println(spells.size()); // 2")),
                "<p>Create an ArrayList of integers, add the numbers 10, 20, 30, then print each using a for-each loop.</p>",
                "import java.util.ArrayList;\n\npublic class ArrayLists {\n    public static void main(String[] args) {\n        ArrayList<Integer> nums = new ArrayList<>();\n        // TODO: add 10, 20, and 30 to nums\n        for (int n : nums) {\n            // TODO: print each element\n        }\n    }\n}",
                tests(test("Print list", "null", "10\n20\n30")),
                "Explain the difference between an array and an ArrayList. When would you use each?");

        mcQuestion("E2", QuestionTier.RECALL, "How do you add an element to an ArrayList?",
                new String[]{".add()", ".push()", ".insert()", ".append()"},
                ".add()", "ArrayList uses the add() method to append elements to the end of the list.");

        subChunk("E3", "E", "Iterating Collections", 3, 50, "Iteration.java",
                "<p>You have a list of items — how do you process each one?</p>",
                "<p>The <strong>for-each loop</strong> (enhanced for) iterates over every element without needing an index.</p><pre><code>for (String name : names) {\n    System.out.println(name);\n}</code></pre>",
                story(
                      n("Eldrin opens a thick tome and reads every page — one after another, without skipping, without counting. He doesn't care which page number he is on; he only cares about what each page <em>contains</em>. This is the spirit of the for-each loop."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "The <strong>enhanced for loop</strong> — or <em>for-each</em> — gives you each element in turn without needing an index. Write <code>for (Type item : collection)</code> and Java hands you each item one by one. Cleaner, safer, and impossible to go out of bounds."),
                      e("Reading the tome", "String[] runes = {\"Fire\", \"Ice\", \"Storm\"};\nfor (String rune : runes) {\n    System.out.println(\"Rune: \" + rune);\n}")),
                "<p>Given an array of integers, use a for-each loop to print only the values greater than 5.</p>",
                "int[] values = {3, 7, 1, 9, 4, 6};\nfor (int v : values) {\n    // TODO: check if v is greater than 5, and if so print it\n}",
                tests(test("Filter > 5", "null", "7\n9\n6")),
                "Explain the for-each loop and compare it to a regular for loop. When is each more appropriate?");

        mcQuestion("E3", QuestionTier.APPLICATION, "What is the syntax for a for-each loop over an int array called <code>nums</code>?",
                new String[]{"for (int n : nums)", "for (int i = 0; i < nums; i++)", "foreach (nums as n)", "for n in nums"},
                "for (int n : nums)", "The enhanced for loop syntax is: for (type variable : collection)");
    }

    private void seedChunkF() {
        chunk("F", "Classes & Objects", "\uD83D\uDCD6", 6, "E");

        subChunk("F1", "F", "Classes and Objects", 1, 60, "ClassesIntro.java",
                "<p>You create a BankAccount but forget to set the balance. How do you guarantee every account starts valid?</p>",
                "<p>A <strong>class</strong> is a blueprint for creating objects. An <strong>object</strong> is an instance of a class with its own data.</p><pre><code>class Dog {\n    String name;\n    int age;\n}\nDog myDog = new Dog();\nmyDog.name = \"Rex\";</code></pre>",
                story(
                      n("On the workbench sits a blank scroll — a perfect <em>template</em> for a fireball spell. Beside it, three glowing orbs flicker, each one conjured from that very template but burning differently. The scroll is the class; the orbs are objects."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "A <strong>class</strong> is a <em>blueprint</em> — it describes the <strong>fields</strong> (what an object knows) and <strong>methods</strong> (what it can do), but it isn't a real thing yet. An <strong>object</strong> is the living instance created from that blueprint using <code>new</code>."),
                      e("Blueprint and instance", "class Wizard {\n    String name;\n    int level;\n}\n\nWizard w = new Wizard();\nw.name = \"Eldrin\";\nw.level = 99;")),
                "<p>Create a <code>Student</code> class with fields <code>name</code> and <code>grade</code>. In main, create a Student, set the fields, and print them.</p>",
                "import java.util.*;\n\npublic class ClassesIntro {\n    static String name;\n    static int grade;\n    public static void main(String[] args) {\n        // TODO: set name to \"Alice\" and grade to 95\n        // TODO: print name + \": \" + grade\n    }\n}",
                tests(test("Student", "null", "Alice: 95")),
                "Explain the difference between a class and an object. Use an analogy.");

        mcQuestion("F1", QuestionTier.RECALL, "What keyword creates a new object from a class?",
                new String[]{"new", "create", "make", "build"},
                "new", "The new keyword creates a new instance (object) of a class.");

        subChunk("F2", "F", "Constructors", 2, 60, "Constructors.java",
                "<p>You create a BankAccount but forget to set the balance. How do you guarantee every account starts valid?</p>",
                "<p>A <strong>constructor</strong> is a special method that runs when you create an object. It initialises the object's fields.</p><pre><code>class Dog {\n    String name;\n    Dog(String name) {\n        this.name = name;\n    }\n}\nDog d = new Dog(\"Rex\");</code></pre>",
                story(
                      n("When a golem is first shaped from clay, the wizard speaks a <em>birth incantation</em> — binding the creature's name, purpose, and power into its very form in that single moment. You cannot change these founding words later. That is what a constructor does."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "A <strong>constructor</strong> is a special method that runs <em>the moment</em> an object is created with <code>new</code>. It has the same name as the class and no return type. Use <code>this.field = parameter</code> to bind the initial values into the object."),
                      e("Binding the golem", "class Golem {\n    String name;\n    Golem(String name) {\n        this.name = name;\n    }\n}\n\nGolem g = new Golem(\"Stonefist\");\nSystem.out.println(g.name); // Stonefist")),
                "<p>Create a <code>Book</code> class with a constructor that takes title and author. Print both fields.</p>",
                "import java.util.*;\n\npublic class Constructors {\n    String title;\n    String author;\n    Constructors(String title, String author) {\n        // TODO: assign title and author to their fields using this.\n    }\n    public static void main(String[] args) {\n        Constructors b = new Constructors(\"The Arcane Arts\", \"Eldrin\");\n        // TODO: print b.title + \" by \" + b.author\n    }\n}",
                tests(test("Book", "null", "The Arcane Arts by Eldrin")),
                "Explain what a constructor does and why it's useful. What happens if you don't write one?");

        mcQuestion("F2", QuestionTier.RECALL, "What is the <code>this</code> keyword in Java?",
                new String[]{"A reference to the current object instance", "A reference to the current class", "A keyword that creates a new object", "A reference to the parent class"},
                "A reference to the current object instance", "this refers to the current object instance. It's used to distinguish fields from parameters with the same name.");

        subChunk("F3", "F", "Methods Inside Classes", 3, 60, "ClassMethods.java",
                "<p>Data without behaviour is just dead storage. How do objects <em>do</em> things?</p>",
                "<p>Objects have <strong>methods</strong> — functions that belong to the class and can access its fields.</p><pre><code>class Circle {\n    double radius;\n    double area() {\n        return Math.PI * radius * radius;\n    }\n}</code></pre>",
                story(
                      n("A spellbook is not just a list of ingredients — it also contains <em>instructions</em> for what to do with them. Eldrin holds up two fingers: a class has <em>fields</em> for what an object knows, and <em>methods</em> for what it can do. Data and behaviour, bound together."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "Methods <em>inside</em> a class have direct access to the object's <strong>fields</strong> — no need to pass them in. The method and the data belong to the same entity. This binding of state and behaviour is the very heart of object-oriented magic."),
                      e("A spell with memory", "class Wand {\n    String wood;\n    int charges;\n    void castSpell() {\n        charges--;\n        System.out.println(wood + \" wand fires! Charges left: \" + charges);\n    }\n}")),
                "<p>Create a <code>Rectangle</code> class with width and height fields, and an <code>area()</code> method. Print the area of a 5x3 rectangle.</p>",
                "import java.util.*;\n\npublic class ClassMethods {\n    int width;\n    int height;\n    ClassMethods(int w, int h) { this.width = w; this.height = h; }\n    int area() {\n        // TODO: return width * height\n        return 0;\n    }\n    public static void main(String[] args) {\n        ClassMethods r = new ClassMethods(5, 3);\n        // TODO: print the area of r\n    }\n}",
                tests(test("Area", "null", "15")),
                "Explain why methods belong inside classes rather than being standalone functions.");

        mcQuestion("F3", QuestionTier.APPLICATION, "What can a method inside a class access that a standalone method cannot?",
                new String[]{"The object's fields (instance variables)", "Other classes", "The Java runtime", "System files"},
                "The object's fields (instance variables)", "Methods inside a class can access the instance variables (fields) of the object they belong to.");
    }

    private void seedChunkG() {
        chunk("G", "Encapsulation", "\uD83D\uDD12", 7, "F");

        subChunk("G1", "G", "Access Modifiers", 1, 60, "AccessModifiers.java",
                "<p>What stops someone from setting a bank account balance to negative?</p>",
                "<p><strong>Access modifiers</strong> control who can see and change fields. <code>private</code> hides them; <code>public</code> exposes them.</p><pre><code>class Account {\n    private double balance;\n    public double getBalance() { return balance; }\n}</code></pre>",
                story(
                      n("The academy vault holds the most powerful artefacts — but apprentices cannot simply reach in and grab them. They must request them through the proper channels. Eldrin smiles: <em>that</em> is encapsulation — controlled access to protected internals."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "<strong>Access modifiers</strong> control who can see your fields. Mark fields <code>private</code> to hide them from the outside world — then expose them only through <code>public</code> methods you control. This prevents anyone from corrupting your object's inner state."),
                      e("Protected vault", "class Vault {\n    private int gold;\n    public int getGold() { return gold; }\n    public void deposit(int amount) {\n        if (amount > 0) gold += amount;\n    }\n}")),
                "<p>Create a class with a private field and public getter/setter methods.</p>",
                "import java.util.*;\n\npublic class AccessModifiers {\n    private String secret = \"hidden\";\n    public String getSecret() {\n        // TODO: return the secret field\n        return \"\";\n    }\n    public void setSecret(String s) {\n        // TODO: assign s to the secret field\n    }\n    public static void main(String[] args) {\n        AccessModifiers a = new AccessModifiers();\n        a.setSecret(\"revealed\");\n        System.out.println(a.getSecret());\n    }\n}",
                tests(test("Getter/Setter", "null", "revealed")),
                "Explain encapsulation and why fields should be private. Give an example of what could go wrong without it.");

        mcQuestion("G1", QuestionTier.RECALL, "What access modifier hides a field from other classes?",
                new String[]{"private", "public", "protected", "hidden"},
                "private", "The private modifier restricts access to within the same class only.");

        subChunk("G2", "G", "Getters, Setters & Validation", 2, 60, "Validation.java",
                "<p>A setter doesn't just set — it can <em>validate</em>.</p>",
                "<p>Setters can include logic to enforce rules on data.</p><pre><code>public void setAge(int age) {\n    if (age < 0) throw new IllegalArgumentException();\n    this.age = age;\n}</code></pre>",
                story(
                      n("At the academy gate stands a stern sentinel who demands proof before letting anyone pass. No proof, no entry. Eldrin gestures to the gate: <em>your setters must do the same</em> — question every value before allowing it to change the object's state."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "A <strong>setter</strong> is not just an assignment — it is a <em>gatekeeper</em>. Before storing a value, add <code>if</code> logic to <strong>validate</strong> it. Reject bad data early, loudly, and clearly. A getter is the trusted courier that safely reveals private state."),
                      e("Sentinel setter", "public void setLevel(int level) {\n    if (level < 1) {\n        System.out.println(\"Invalid level!\");\n        return;\n    }\n    this.level = level;\n}")),
                "<p>Create a <code>Temperature</code> class where the setter rejects values below absolute zero (-273).</p>",
                "import java.util.*;\n\npublic class Validation {\n    private int temp;\n    public void setTemp(int t) {\n        // TODO: if t is below -273, print \"Invalid\" and return\n        // TODO: otherwise assign t to this.temp\n    }\n    public int getTemp() {\n        // TODO: return the temp field\n        return 0;\n    }\n    public static void main(String[] args) {\n        Validation v = new Validation();\n        v.setTemp(25);\n        System.out.println(v.getTemp());\n        v.setTemp(-300);\n    }\n}",
                tests(test("Valid temp", "null", "25"), test("Invalid temp", "null", "Invalid")),
                "Explain why validation in setters is important. Give a real-world example.");

        mcQuestion("G2", QuestionTier.APPLICATION, "Why should setters include validation?",
                new String[]{"To prevent invalid data from being stored", "To make code run faster", "Because Java requires it", "To avoid using constructors"},
                "To prevent invalid data from being stored", "Validation in setters ensures objects always remain in a valid state.");
    }

    private void seedChunkH() {
        chunk("H", "Inheritance", "\uD83C\uDF33", 8, "F");

        subChunk("H1", "H", "Extending Classes", 1, 60, "Inheritance.java",
                "<p>Dogs, cats, and birds are all animals. They share common traits but also have unique ones. How do you model this?</p>",
                "<p><strong>Inheritance</strong> lets one class extend another, inheriting its fields and methods.</p><pre><code>class Animal {\n    String name;\n    void speak() { System.out.println(\"...\"); }\n}\nclass Dog extends Animal {\n    void speak() { System.out.println(\"Woof!\"); }\n}</code></pre>",
                story(
                      n("When Eldrin took on his first apprentice, he did not re-teach fire, water, and wind from scratch. He <em>passed them down</em> — the apprentice inherited every spell in the master's grimoire and then forged her own. That is inheritance."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "<strong>Inheritance</strong> lets one class <strong>extend</strong> another with the <code>extends</code> keyword. The child class automatically gains all the parent's fields and methods, and can <strong>override</strong> them to provide specialised behaviour. Write once, refine forever."),
                      e("Apprentice inherits power", "class Wizard {\n    void castSpell() { System.out.println(\"Casting...\"); }\n}\nclass FireMage extends Wizard {\n    void castSpell() { System.out.println(\"Fireball!\"); }\n}")),
                "<p>Create an <code>Animal</code> class with a <code>speak()</code> method, and a <code>Cat</code> that overrides it to print \"Meow\". Call it.</p>",
                "import java.util.*;\n\npublic class Inheritance {\n    public static void main(String[] args) {\n        Cat c = new Cat();\n        c.speak();\n    }\n}\nclass Animal {\n    void speak() { System.out.println(\"...\"); }\n}\nclass Cat extends Animal {\n    // TODO: override speak() to print \"Meow\"\n}",
                tests(test("Cat speaks", "null", "Meow")),
                "Explain inheritance in your own words. Why is it useful?");

        mcQuestion("H1", QuestionTier.RECALL, "What keyword is used to inherit from another class?",
                new String[]{"extends", "inherits", "implements", "derives"},
                "extends", "In Java, a class uses the extends keyword to inherit from another class.");

        subChunk("H2", "H", "Super Keyword & Constructor Chaining", 2, 60, "SuperKeyword.java",
                "<p>When a child class creates an object, does the parent's constructor run too?</p>",
                "<p>The <code>super</code> keyword calls the parent's constructor or methods.</p><pre><code>class Animal {\n    String name;\n    Animal(String name) { this.name = name; }\n}\nclass Dog extends Animal {\n    Dog(String name) { super(name); }\n}</code></pre>",
                story(
                      n("Before the apprentice could add her own sigils to the grand ritual, the master's foundational incantation had to be spoken first. The power flowed downward — master to apprentice — each layer building on the last. That first call to the master's power is <code>super</code>."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "The <code>super</code> keyword reaches up to the <strong>parent class</strong>. Call <code>super(args)</code> in a child constructor to run the parent's constructor first — Java actually <em>requires</em> this if the parent has no no-arg constructor. You may also call <code>super.method()</code> to invoke an overridden parent method."),
                      e("Chaining the ritual", "class Creature {\n    String name;\n    Creature(String name) { this.name = name; }\n}\nclass Dragon extends Creature {\n    int wings;\n    Dragon(String name, int wings) {\n        super(name); // must come first\n        this.wings = wings;\n    }\n}")),
                "<p>Create a <code>Vehicle</code> with a constructor and a <code>Car</code> that calls super. Print the vehicle's type.</p>",
                "import java.util.*;\n\npublic class SuperKeyword {\n    public static void main(String[] args) {\n        Car c = new Car(\"Sedan\");\n        System.out.println(c.type);\n    }\n}\nclass Vehicle {\n    String type;\n    Vehicle(String type) { this.type = type; }\n}\nclass Car extends Vehicle {\n    Car(String type) {\n        // TODO: call the parent constructor with super()\n    }\n}",
                tests(test("Super", "null", "Sedan")),
                "Explain the super keyword and when you need to use it.");

        mcQuestion("H2", QuestionTier.RECALL, "What does the <code>super</code> keyword refer to?",
                new String[]{"The parent class", "The current class", "A static method", "The main method"},
                "The parent class", "super refers to the parent (superclass) and is used to call its constructor or methods.");
    }

    private void seedChunkI() {
        chunk("I", "Polymorphism & Abstraction", "\uD83C\uDF00", 9, "G", "H");

        subChunk("I1", "I", "Polymorphism", 1, 70, "Polymorphism.java",
                "<p>If a method accepts an Animal, can you pass it a Dog?</p>",
                "<p><strong>Polymorphism</strong> means one interface, many implementations. A parent reference can hold a child object.</p><pre><code>Animal a = new Dog();\na.speak(); // calls Dog's speak()</code></pre>",
                story(
                      n("Eldrin lines up three creatures — a wolf, a raven, and a serpent — and calls out \"<em>Speak!</em>\" in a single voice. Each creature answers in its own tongue. Same command, three utterly different responses. This is polymorphism: one incantation, many truths."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "<strong>Polymorphism</strong> means a parent-type reference can hold any child object, and calling a method on it invokes the <em>child's</em> version at runtime — not the parent's. This is called <strong>dynamic dispatch</strong>, and it lets you write flexible code that works with any subtype."),
                      e("Many shapes, one call", "Animal a1 = new Dog();\nAnimal a2 = new Cat();\na1.speak(); // prints Woof\na2.speak(); // prints Meow")),
                "<p>Create an Animal with speak(), Dog and Cat that override it. Store both in Animal variables and call speak().</p>",
                "import java.util.*;\n\npublic class Polymorphism {\n    public static void main(String[] args) {\n        Animal dog = new Dog();\n        Animal cat = new Cat();\n        // TODO: call speak() on both dog and cat\n    }\n}\nclass Animal { void speak() { System.out.println(\"...\"); } }\nclass Dog extends Animal {\n    // TODO: override speak() to print \"Woof\"\n}\nclass Cat extends Animal {\n    // TODO: override speak() to print \"Meow\"\n}",
                tests(test("Poly", "null", "Woof\nMeow")),
                "Explain polymorphism to a beginner. Why is it powerful?");

        mcQuestion("I1", QuestionTier.APPLICATION, "What prints when you call speak() on an Animal variable holding a Dog?",
                new String[]{"Dog's version of speak()", "Animal's version of speak()", "A compile error", "Nothing"},
                "Dog's version of speak()", "At runtime, Java calls the actual object's method (Dog), not the reference type's (Animal). This is dynamic dispatch.");

        subChunk("I2", "I", "Abstract Classes & Interfaces", 2, 70, "AbstractClasses.java",
                "<p>What if a class should never be instantiated directly — only extended?</p>",
                "<p>An <strong>abstract class</strong> can't be instantiated. It can have abstract methods (no body) that subclasses must implement. An <strong>interface</strong> defines a contract of methods a class must provide.</p><pre><code>abstract class Shape {\n    abstract double area();\n}\nclass Circle extends Shape {\n    double radius;\n    double area() { return Math.PI * radius * radius; }\n}</code></pre>",
                story(
                      n("Eldrin slides a half-finished scroll across the desk. The outer ritual is written, the energy channels drawn — but the <em>core incantation</em> is blank. <em>That</em> is yours to fill in, he says. Every shape must define its own area; I merely insist the method exists."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "An <strong>abstract class</strong> is marked with the <code>abstract</code> keyword and cannot be instantiated directly. It may declare <code>abstract</code> methods — stubs with no body that subclasses <em>must</em> implement. An <strong>interface</strong> takes this further: it is a pure contract of method signatures with no state."),
                      e("The incomplete formula", "abstract class Shape {\n    abstract double area(); // subclass must fill this in\n}\nclass Circle extends Shape {\n    double r;\n    Circle(double r) { this.r = r; }\n    double area() { return Math.PI * r * r; }\n}")),
                "<p>Create an abstract <code>Shape</code> with an abstract <code>area()</code> method. Implement it in <code>Square</code>. Print the area of a 4x4 square.</p>",
                "import java.util.*;\n\npublic class AbstractClasses {\n    public static void main(String[] args) {\n        Square s = new Square(4);\n        // TODO: print the area of s\n    }\n}\nabstract class Shape {\n    abstract int area();\n}\nclass Square extends Shape {\n    int side;\n    Square(int side) { this.side = side; }\n    int area() {\n        // TODO: return side * side\n        return 0;\n    }\n}",
                tests(test("Abstract area", "null", "16")),
                "Explain the difference between abstract classes and interfaces. When would you use each?");

        mcQuestion("I2", QuestionTier.RECALL, "Can you create an instance of an abstract class?",
                new String[]{"No", "Yes", "Only with a constructor", "Only inside its package"},
                "No", "Abstract classes cannot be instantiated directly. You must create a subclass that implements all abstract methods.");
    }

    private void seedChunkJ() {
        chunk("J", "Exception Handling", "\u26A0", 10, "B");

        subChunk("J1", "J", "Try/Catch Blocks", 1, 50, "TryCatch.java",
                "<p>What happens when your program divides by zero? Does it crash?</p>",
                "<p>A <strong>try/catch</strong> block lets you handle errors gracefully instead of crashing.</p><pre><code>try {\n    int result = 10 / 0;\n} catch (ArithmeticException e) {\n    System.out.println(\"Can't divide by zero!\");\n}</code></pre>",
                story(
                      n("Mid-incantation, Eldrin's spell sputters and a jet of smoke fills the room. He calmly steps aside — he had already drawn a <em>containment circle</em> around himself before beginning. The spell failed; he did not. That containment circle is your try/catch."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "Wrap risky code in a <code>try</code> block and place your recovery logic in the <code>catch</code> block. If anything throws an <strong>exception</strong> inside <code>try</code>, Java leaps straight to <code>catch</code> — your program survives rather than crashing to the floor."),
                      e("Containment circle", "try {\n    int result = 10 / 0;\n    System.out.println(result);\n} catch (ArithmeticException e) {\n    System.out.println(\"Spell failed: \" + e.getMessage());\n}")),
                "<p>Write code that tries to divide 10 by 0 and catches the exception, printing an error message.</p>",
                "try {\n    int result = 10 / 0;\n    System.out.println(result);\n} catch (ArithmeticException e) {\n    // TODO: print \"Error: Division by zero\"\n}",
                tests(test("Catch division", "null", "Error: Division by zero")),
                "Explain try/catch to a beginner. How is it different from using if/else to check for errors?");

        mcQuestion("J1", QuestionTier.RECALL, "What block of code executes when an exception occurs?",
                new String[]{"catch", "try", "finally", "throw"},
                "catch", "The catch block runs when the specified exception type is thrown in the try block.");

        subChunk("J2", "J", "Throwing Exceptions", 2, 50, "ThrowExceptions.java",
                "<p>Sometimes your code detects an invalid state. How do you signal the problem?</p>",
                "<p>Use <code>throw</code> to create an exception and <code>throws</code> to declare that a method might throw one.</p><pre><code>static void checkAge(int age) {\n    if (age < 0) throw new IllegalArgumentException(\"Negative age!\");\n}</code></pre>",
                story(
                      n("The academy's alarm bell only rings when a guard <em>pulls the rope</em> — it does not ring itself. Eldrin nods: your code must be that guard. When something is wrong, you must actively <code>throw</code> the alarm and let the caller deal with it."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "Use <code>throw new ExceptionType(\"message\")</code> to raise an exception from your own code. The <code>throws</code> keyword on a method signature declares that callers should be ready to catch it. This is how you communicate contract violations clearly and loudly."),
                      e("Pulling the alarm rope", "static void validate(int age) {\n    if (age < 0) {\n        throw new IllegalArgumentException(\"Age cannot be negative!\");\n    }\n    System.out.println(\"Age accepted: \" + age);\n}")),
                "<p>Write a method that throws an exception if the input is negative. Catch it in main.</p>",
                "import java.util.*;\n\npublic class ThrowExceptions {\n    static void validate(int n) {\n        // TODO: if n is negative, throw new IllegalArgumentException(\"Negative!\")\n        System.out.println(\"Valid: \" + n);\n    }\n    public static void main(String[] args) {\n        try {\n            validate(-5);\n        } catch (IllegalArgumentException e) {\n            // TODO: print the exception message\n        }\n    }\n}",
                tests(test("Throw", "null", "Negative!")),
                "Explain the difference between throw and throws in Java.");

        mcQuestion("J2", QuestionTier.APPLICATION, "What does <code>throw new IllegalArgumentException(\"bad\")</code> do?",
                new String[]{"Creates and throws an exception with the message 'bad'", "Prints 'bad' to the console", "Returns 'bad' from the method", "Catches an exception called 'bad'"},
                "Creates and throws an exception with the message 'bad'", "throw creates a new exception object and immediately throws it, interrupting normal flow.");
    }

    private void seedChunkK() {
        chunk("K", "Common APIs & Utils", "\uD83D\uDEE0", 11, "B");

        subChunk("K1", "K", "String Methods", 1, 50, "StringMethods.java",
                "<p>You have a user's email in all caps. How do you clean it up?</p>",
                "<p>Java's <code>String</code> class has many built-in methods: <code>length()</code>, <code>toUpperCase()</code>, <code>substring()</code>, <code>contains()</code>, and more.</p><pre><code>String s = \"Hello World\";\nSystem.out.println(s.length()); // 11\nSystem.out.println(s.toLowerCase()); // hello world</code></pre>",
                story(
                      n("Every message in the academy is carried by a <em>String</em> — names on envelopes, spells in scrolls, warnings on dungeon walls. Eldrin traces the letters of a word with his wand: <em>know these built-in methods</em>, he says, <em>and Strings will bend to your will</em>."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "<code>String</code> objects come pre-loaded with powerful methods. <code>length()</code> counts the characters, <code>toUpperCase()</code> transforms the text, <code>substring()</code> carves out a portion, and <code>contains()</code> searches within. Learn these runes and you will never struggle with text again."),
                      e("Inspecting the scroll", "String msg = \"Arcane Academy\";\nSystem.out.println(msg.length());       // 14\nSystem.out.println(msg.toUpperCase());  // ARCANE ACADEMY\nSystem.out.println(msg.contains(\"Arc\")); // true")),
                "<p>Given a string, print its length, uppercase version, and whether it contains \"Java\".</p>",
                "String text = \"I love Java programming\";\n// TODO: print the length of text\n// TODO: print the uppercase version of text\n// TODO: print whether text contains \"Java\"\n",
                tests(test("Length", "null", "23"), test("Upper", "null", "I LOVE JAVA PROGRAMMING"), test("Contains", "null", "true")),
                "Explain 3 useful String methods and when you would use each one.");

        mcQuestion("K1", QuestionTier.RECALL, "What does <code>\"hello\".length()</code> return?",
                new String[]{"5", "4", "6", "\"hello\""},
                "5", "length() returns the number of characters in the string. \"hello\" has 5 characters.");

        subChunk("K2", "K", "Math Class & Utility Methods", 2, 50, "MathUtils.java",
                "<p>How do you calculate a square root or generate a random number in Java?</p>",
                "<p>The <code>Math</code> class provides static methods for common operations: <code>Math.max()</code>, <code>Math.min()</code>, <code>Math.sqrt()</code>, <code>Math.random()</code>.</p><pre><code>System.out.println(Math.max(10, 20)); // 20\nSystem.out.println(Math.sqrt(16)); // 4.0</code></pre>",
                story(
                      n("Eldrin snaps his fingers and an abacus the size of a bookshelf materialises, covered in glowing symbols. <em>The academy has already built you every mathematical tool you could need</em>, he says. <em>The Math class is that abacus — and it never makes mistakes.</em>"),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "The <code>Math</code> class provides <strong>static</strong> utility methods — no object needed, just call them directly. <code>Math.max()</code> picks the larger value, <code>Math.sqrt()</code> finds the root, <code>Math.abs()</code> strips the sign, and <code>Math.random()</code> generates a number between 0 and 1."),
                      e("The academy's abacus", "System.out.println(Math.max(42, 17));   // 42\nSystem.out.println(Math.sqrt(81));      // 9.0\nSystem.out.println(Math.abs(-7));       // 7")),
                "<p>Print the max of 15 and 23, the square root of 49, and Math.PI.</p>",
                "// TODO: print Math.max(15, 23)\n// TODO: print Math.sqrt(49)\n// TODO: print Math.PI\n",
                tests(test("Max", "null", "23"), test("Sqrt", "null", "7.0"), test("PI", "null", "3.14")),
                "Explain 3 methods from the Math class and give a practical example for each.");

        mcQuestion("K2", QuestionTier.RECALL, "What does <code>Math.sqrt(25)</code> return?",
                new String[]{"5.0", "25", "12.5", "625"},
                "5.0", "Math.sqrt() returns the square root as a double. The square root of 25 is 5.0.");
    }
}
