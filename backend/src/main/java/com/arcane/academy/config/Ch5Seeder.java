package com.arcane.academy.config;

import com.arcane.academy.repository.QuestRepository;
import org.springframework.stereotype.Component;

// ══════════════════════════════════════════════════════════════════════════════
// CHAPTER V — THE MASTER'S PATH (Advanced Java)
// ══════════════════════════════════════════════════════════════════════════════
@Component
public class Ch5Seeder extends AbstractChapterSeeder {

    public Ch5Seeder(QuestRepository questRepository) {
        super(questRepository);
    }

    @Override
    public void seed() {

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
}
