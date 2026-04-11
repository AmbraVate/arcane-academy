package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.stereotype.Component;

@Component
public class ChunkASeeder extends AbstractChunkSeeder {

    public ChunkASeeder(ChunkRepository chunkRepository, SubChunkRepository subChunkRepository,
                        QuestionRepository questionRepository, RabbitHoleModuleRepository rabbitHoleRepository) {
        super(chunkRepository, subChunkRepository, questionRepository, rabbitHoleRepository);
    }

    @Override
    public void seed() {

        // ── Chunk A: Variables & Data Types (no prerequisites) ──────────────
        chunk("chunk-a", "Variables & Data Types", "\uD83D\uDCDC", 1);

        // =====================================================================
        // A1 — What is a Variable?
        // =====================================================================
        subChunk("a1", "chunk-a", "What is a Variable?", 1, 50, "VariableBasics.java",

                // hook
                "<p>How does your phone remember your name, your high score, or your favourite colour? "
                + "It stores each piece of information in a tiny labelled box. In Java, that box is called a <strong>variable</strong>.</p>",

                // explanation
                "<h3>Variables: Labelled Boxes for Data</h3>"
                + "<p>A variable is a named container that holds a value. You <em>declare</em> a variable by "
                + "telling Java two things: what <strong>type</strong> of data it will hold, and what <strong>name</strong> to give it.</p>"
                + "<pre><code>int age = 25;\n"
                + "String name = \"Eldrin\";\n"
                + "double height = 1.82;</code></pre>"
                + "<p>Here <code>int</code> means whole number, <code>String</code> means text, and <code>double</code> means a decimal number. "
                + "The <code>=</code> sign assigns a value to the variable.</p>"
                + "<p>You can change a variable's value later:</p>"
                + "<pre><code>age = 26; // birthday!</code></pre>"
                + "<p>Variable names should be descriptive. Use <strong>camelCase</strong> in Java: "
                + "<code>playerScore</code>, <code>firstName</code>, <code>totalCost</code>.</p>"
                + "<p>Think of a variable like a labelled jar on a shelf. The label is the name, "
                + "the jar's shape is the type, and whatever is inside is the current value.</p>",

                // story
                story(
                    n("You step into the Arcane Library for the first time. Dust motes drift through beams of golden light. "
                      + "An old wizard looks up from an enormous tome."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Welcome, young apprentice! Before you can cast even the simplest spell, you must learn to store magical energy. "
                      + "We call these storage vessels <strong>runes</strong> — the rest of the world calls them <em>variables</em>."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A rune has three parts: a <strong>type</strong> that determines what kind of energy it can hold, "
                      + "a <strong>name</strong> so you can recall it, and a <strong>value</strong> — the energy itself."),
                    e("Your first rune", "int manaPoints = 100;\nString wizardName = \"Apprentice\";\ndouble spellPower = 7.5;"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Excellent! You have inscribed three runes. <code>int</code> holds whole numbers, "
                      + "<code>String</code> holds text, and <code>double</code> holds decimal numbers. "
                      + "Now try it yourself!")
                ),

                // guided practice HTML
                "<p>Declare a <code>String</code> variable called <code>name</code> and set it to <code>\"Merlin\"</code>. "
                + "Then declare an <code>int</code> variable called <code>age</code> and set it to <code>30</code>. "
                + "Print them on separate lines using <code>System.out.println()</code>.</p>",

                // starter code
                "// Declare your variables below\n\n\n// Print them\n",

                // tests
                tests(
                    test("Prints name", "null", "Merlin"),
                    test("Prints age",  "null", "30")
                ),

                // feynman prompt
                "Explain what a variable is in Java as if you were describing it to a friend who has never programmed."
        );

        // A1 questions
        mcQuestion("a1", QuestionTier.RECALL,
                "<p>Which keyword declares a whole-number variable in Java?</p>",
                new String[]{"int", "String", "double", "number"},
                "int",
                "<p><code>int</code> is the Java type for whole numbers (integers). "
                + "<code>String</code> is for text, <code>double</code> is for decimals, "
                + "and <code>number</code> is not a Java keyword.</p>");

        tfQuestion("a1", QuestionTier.RECALL,
                "<p>In Java, you must specify the type of a variable when you declare it.</p>",
                "True",
                "<p>Java is a <em>statically typed</em> language. You must always declare the type "
                + "(e.g., <code>int</code>, <code>String</code>) before the variable name.</p>");

        codeQuestion("a1", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "int x = 10;\nx = 25;\nSystem.out.println(x);",
                "25",
                "<p>The variable <code>x</code> is first set to 10, then reassigned to 25. "
                + "The final value is 25, which is what gets printed.</p>");

        fillQuestion("a1", QuestionTier.APPLICATION,
                "<p>Fill in the blank to declare a text variable:<br><code>______ greeting = \"Hello\";</code></p>",
                "String",
                "<p><code>String</code> is the type used for text values in Java.</p>");

        // =====================================================================
        // A2 — Data Types
        // =====================================================================
        subChunk("a2", "chunk-a", "Data Types", 2, 50, "DataTypes.java",

                // hook
                "<p>What if you tried to store your age as a word instead of a number? "
                + "Java is very particular about the <em>kind</em> of data you put in each variable.</p>",

                // explanation
                "<h3>Java's Core Data Types</h3>"
                + "<p>Every variable in Java must have a type. Here are the most common ones:</p>"
                + "<ul>"
                + "<li><code>int</code> — whole numbers: <code>42</code>, <code>-7</code>, <code>0</code></li>"
                + "<li><code>double</code> — decimal numbers: <code>3.14</code>, <code>-0.5</code></li>"
                + "<li><code>boolean</code> — true or false: <code>true</code>, <code>false</code></li>"
                + "<li><code>char</code> — a single character: <code>'A'</code>, <code>'z'</code>, <code>'7'</code></li>"
                + "<li><code>String</code> — text (sequence of characters): <code>\"Hello\"</code></li>"
                + "</ul>"
                + "<p>Notice that <code>char</code> uses single quotes <code>'A'</code> while <code>String</code> uses "
                + "double quotes <code>\"Hello\"</code>. This is an important distinction!</p>"
                + "<pre><code>boolean isWizard = true;\n"
                + "char grade = 'A';\n"
                + "int score = 95;\n"
                + "double gpa = 3.8;\n"
                + "String school = \"Arcane Academy\";</code></pre>"
                + "<p>Choosing the right type matters. You cannot do maths with a <code>String</code>, "
                + "and you cannot store a sentence in a <code>char</code>.</p>",

                // story
                story(
                    n("Archmage Eldrin pulls five differently shaped jars from the shelf, each glowing a different colour."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Each jar can only hold a specific kind of magical essence. Put fire essence in a water jar and — <em>boom</em>! "
                      + "The same rule applies to Java types. You must match the data to its correct type."),
                    e("Five types of essence", "int potions = 3;\n"
                      + "double strength = 9.5;\n"
                      + "boolean enchanted = true;\n"
                      + "char rank = 'S';\n"
                      + "String spellName = \"Fireball\";"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Notice: <code>char</code> uses single quotes for one character, "
                      + "while <code>String</code> uses double quotes for text. "
                      + "Mix them up, and the spell fizzles!")
                ),

                // guided practice
                "<p>Declare one variable of each type and print them all, each on its own line:</p>"
                + "<ul>"
                + "<li><code>int lives = 3;</code></li>"
                + "<li><code>double temperature = 36.6;</code></li>"
                + "<li><code>boolean isRaining = false;</code></li>"
                + "<li><code>char initial = 'J';</code></li>"
                + "<li><code>String city = \"London\";</code></li>"
                + "</ul>",

                // starter code
                "// Declare all five variables\n\n\n// Print each one\n",

                // tests
                tests(
                    test("Prints lives",       "null", "3"),
                    test("Prints temperature", "null", "36.6"),
                    test("Prints isRaining",   "null", "false"),
                    test("Prints initial",     "null", "J"),
                    test("Prints city",        "null", "London")
                ),

                // feynman prompt
                "Explain the difference between int, double, boolean, char, and String to someone who has never coded."
        );

        // A2 questions
        mcQuestion("a2", QuestionTier.RECALL,
                "<p>Which data type stores a single character?</p>",
                new String[]{"String", "char", "boolean", "int"},
                "char",
                "<p><code>char</code> holds exactly one character, wrapped in single quotes like <code>'A'</code>. "
                + "<code>String</code> holds a sequence of characters in double quotes.</p>");

        mcQuestion("a2", QuestionTier.APPLICATION,
                "<p>Which declaration is correct for storing the value <code>true</code>?</p>",
                new String[]{
                    "boolean flag = true;",
                    "String flag = true;",
                    "int flag = true;",
                    "boolean flag = \"true\";"
                },
                "boolean flag = true;",
                "<p><code>boolean</code> stores <code>true</code> or <code>false</code> without quotes. "
                + "Wrapping it in quotes would make it a <code>String</code>.</p>");

        codeQuestion("a2", QuestionTier.DISCRIMINATION, QuestionType.DEBUGGING,
                "<p>This code has an error. What needs to change?</p>",
                "char greeting = \"Hi\";",
                "Change char to String (or change the value to a single character like 'H')",
                "<p><code>char</code> can only hold a single character in single quotes. "
                + "<code>\"Hi\"</code> is two characters in double quotes, which makes it a <code>String</code>.</p>");

        // =====================================================================
        // A3 — Variable Operations
        // =====================================================================
        subChunk("a3", "chunk-a", "Variable Operations", 3, 50, "VariableOps.java",

                // hook
                "<p>A shopkeeper needs to calculate change for a customer. "
                + "How do you get Java to do the maths for you?</p>",

                // explanation
                "<h3>Arithmetic and String Operations</h3>"
                + "<p>Java supports the standard arithmetic operators:</p>"
                + "<pre><code>int a = 10;\n"
                + "int b = 3;\n"
                + "System.out.println(a + b);  // 13\n"
                + "System.out.println(a - b);  // 7\n"
                + "System.out.println(a * b);  // 30\n"
                + "System.out.println(a / b);  // 3 (integer division!)\n"
                + "System.out.println(a % b);  // 1 (remainder)</code></pre>"
                + "<p><strong>Important:</strong> dividing two <code>int</code> values gives an <code>int</code> result — "
                + "the decimal part is dropped, not rounded. <code>10 / 3</code> gives <code>3</code>, not <code>3.33</code>.</p>"
                + "<p>The <code>+</code> operator also works with <code>String</code> to join text together — "
                + "this is called <strong>concatenation</strong>:</p>"
                + "<pre><code>String first = \"Harry\";\n"
                + "String last = \"Potter\";\n"
                + "String full = first + \" \" + last;\n"
                + "System.out.println(full); // Harry Potter</code></pre>"
                + "<p>If you add a number to a <code>String</code>, Java converts the number to text automatically:</p>"
                + "<pre><code>System.out.println(\"Score: \" + 95); // Score: 95</code></pre>",

                // story
                story(
                    n("Archmage Eldrin sets two bags of gold coins on the table."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Magic is not just about storing energy — it is about <em>combining</em> it. "
                      + "Add two runes together, subtract, multiply... your spells become far more powerful."),
                    e("Combining runes", "int bag1 = 50;\nint bag2 = 30;\n"
                      + "int total = bag1 + bag2;\nSystem.out.println(\"Gold: \" + total);"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Notice how the <code>+</code> sign can add numbers <em>or</em> join text. "
                      + "Context is everything, young apprentice!"),
                    e("String joining", "String title = \"Archmage\";\nString name = \"Eldrin\";\n"
                      + "System.out.println(title + \" \" + name);")
                ),

                // guided practice
                "<p>A customer buys 3 potions at 15 gold each and 2 scrolls at 8 gold each. "
                + "Calculate and print the total cost.</p>"
                + "<p>Expected output: <code>61</code></p>",

                // starter code
                "int potionPrice = 15;\nint potionQty = 3;\nint scrollPrice = 8;\nint scrollQty = 2;\n\n// Calculate total and print it\n",

                // tests
                tests(
                    test("Total cost", "null", "61")
                ),

                // feynman prompt
                "Explain how arithmetic operators and string concatenation work in Java, including the difference between + for numbers and + for strings."
        );

        // A3 questions
        codeQuestion("a3", QuestionTier.RECALL, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "int a = 10;\nint b = 3;\nSystem.out.println(a / b);",
                "3",
                "<p>Integer division in Java truncates the decimal part. <code>10 / 3</code> is <code>3</code>, not <code>3.33</code>.</p>");

        codeQuestion("a3", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "String word = \"Java\";\nint num = 21;\nSystem.out.println(word + num);",
                "Java21",
                "<p>When you use <code>+</code> with a <code>String</code> and a number, "
                + "Java converts the number to text and concatenates them.</p>");

        mcQuestion("a3", QuestionTier.APPLICATION,
                "<p>What is the result of <code>17 % 5</code>?</p>",
                new String[]{"2", "3", "3.4", "5"},
                "2",
                "<p>The <code>%</code> (modulo) operator returns the remainder of division. "
                + "<code>17 / 5 = 3</code> remainder <code>2</code>.</p>");

        fillQuestion("a3", QuestionTier.DISCRIMINATION,
                "<p>Fill in the blank so the code prints <code>Hello World</code>:<br>"
                + "<code>System.out.println(\"Hello\" ______ \" World\");</code></p>",
                "+",
                "<p>The <code>+</code> operator concatenates (joins) strings in Java.</p>");

        // =====================================================================
        // A4 — Type Casting & Conversion
        // =====================================================================
        subChunk("a4", "chunk-a", "Type Casting & Conversion", 4, 50, "TypeCasting.java",

                // hook
                "<p>What happens when you divide 7 by 2 in Java? You might expect 3.5, but Java has other plans. "
                + "Understanding type casting lets you control what happens.</p>",

                // explanation
                "<h3>Implicit and Explicit Casting</h3>"
                + "<p><strong>Implicit casting</strong> (widening) happens automatically when Java converts a smaller "
                + "type to a larger one with no data loss:</p>"
                + "<pre><code>int whole = 7;\n"
                + "double decimal = whole; // 7.0 — automatic\n"
                + "System.out.println(decimal);</code></pre>"
                + "<p><strong>Explicit casting</strong> (narrowing) is needed when converting a larger type to a smaller one, "
                + "because data could be lost:</p>"
                + "<pre><code>double pi = 3.14;\n"
                + "int rounded = (int) pi; // 3 — decimal part is chopped\n"
                + "System.out.println(rounded);</code></pre>"
                + "<p>The <code>(int)</code> before the value is called a <strong>cast operator</strong>. "
                + "It tells Java: \"I know data might be lost — do it anyway.\"</p>"
                + "<p>A common trap: <code>7 / 2</code> gives <code>3</code> because both operands are <code>int</code>. "
                + "To get <code>3.5</code>, cast one operand first:</p>"
                + "<pre><code>int a = 7;\n"
                + "int b = 2;\n"
                + "double result = (double) a / b; // 3.5\n"
                + "System.out.println(result);</code></pre>",

                // story
                story(
                    n("Archmage Eldrin holds up two vials — one large, one small."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Pouring essence from a small vial into a large one is safe — nothing spills. "
                      + "This is <strong>implicit casting</strong>. But pouring from a large vial into a small one? "
                      + "You will lose some essence. That is <strong>explicit casting</strong>."),
                    e("Widening (safe)", "int gold = 100;\ndouble preciseGold = gold;\n"
                      + "System.out.println(preciseGold); // 100.0"),
                    e("Narrowing (needs cast)", "double temperature = 36.7;\nint rounded = (int) temperature;\n"
                      + "System.out.println(rounded); // 36"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "And here is the classic trap. Watch what happens when two integers divide:")
                    ,
                    e("Integer division fix", "int a = 7;\nint b = 2;\n"
                      + "System.out.println(a / b);           // 3\n"
                      + "System.out.println((double) a / b);  // 3.5")
                ),

                // guided practice
                "<p>Given <code>int a = 7</code> and <code>int b = 2</code>, print the result of dividing "
                + "<code>a</code> by <code>b</code> as a <code>double</code> (should print <code>3.5</code>). "
                + "Then cast <code>9.99</code> to an <code>int</code> and print the result.</p>",

                // starter code
                "int a = 7;\nint b = 2;\n\n// Print a / b as a double\n\n\n// Cast 9.99 to int and print\n",

                // tests
                tests(
                    test("Division result", "null", "3.5"),
                    test("Cast result",     "null", "9")
                ),

                // feynman prompt
                "Explain implicit and explicit type casting in Java as if you were teaching a classmate, including why integer division can be surprising."
        );

        // A4 questions
        codeQuestion("a4", QuestionTier.RECALL, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "double x = 9.8;\nint y = (int) x;\nSystem.out.println(y);",
                "9",
                "<p>Casting a <code>double</code> to <code>int</code> truncates (chops off) the decimal part. "
                + "<code>9.8</code> becomes <code>9</code>.</p>");

        tfQuestion("a4", QuestionTier.RECALL,
                "<p>Java automatically converts an <code>int</code> to a <code>double</code> without a cast.</p>",
                "True",
                "<p>This is called <em>implicit casting</em> or widening. Going from <code>int</code> to <code>double</code> "
                + "is safe because no data is lost, so Java does it automatically.</p>");

        codeQuestion("a4", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "int a = 5;\nint b = 2;\nSystem.out.println((double) a / b);",
                "2.5",
                "<p>Casting <code>a</code> to <code>double</code> before dividing forces Java to do floating-point division. "
                + "<code>5.0 / 2</code> gives <code>2.5</code>.</p>");

        mcQuestion("a4", QuestionTier.DISCRIMINATION,
                "<p>Which of the following requires an explicit cast?</p>",
                new String[]{
                    "int to double",
                    "double to int",
                    "int to long",
                    "char to int"
                },
                "double to int",
                "<p>Converting from <code>double</code> to <code>int</code> is narrowing — you lose the decimal part. "
                + "Java requires you to write <code>(int)</code> to confirm you accept the data loss. "
                + "The other conversions are all widening and happen automatically.</p>");
    }
}
