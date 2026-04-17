package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.stereotype.Component;

@Component
public class PractitionerSeeder1 extends AbstractChunkSeeder {

    public PractitionerSeeder1(ChunkRepository chunkRepository, SubChunkRepository subChunkRepository,
                               QuestionRepository questionRepository, RabbitHoleModuleRepository rabbitHoleRepository) {
        super(chunkRepository, subChunkRepository, questionRepository, rabbitHoleRepository);
    }

    @Override
    public void seed() {
        seedChunkPA();
        seedChunkPB();
        seedChunkPC();
        seedChunkPD();
        seedChunkPE();
        seedChunkPF();
        seedChunkPG();
    }

    // =========================================================================
    // CHUNK PA — Generics
    // =========================================================================
    private void seedChunkPA() {
        chunk("PA", "Generics", "\uD83D\uDD37", 15, LearnerPath.PRACTITIONER, "L", "H");

        subChunk("PA1", "PA", "Why Generics & Generic Classes", 1, 70, "GenericsIntro.java",

                "<p>Before generics, storing objects in a collection meant casting from <code>Object</code> — and a <code>ClassCastException</code> could blow up at runtime with no warning at compile time.</p>",

                "<h3>Type-Safe Containers with Generics</h3>"
                + "<p>Generics let you parameterise types — so <code>Box&lt;T&gt;</code> works for any type <code>T</code>, checked at compile time:</p>"
                + "<pre><code>// Generic class\npublic class Box&lt;T&gt; {\n    private T value;\n    public Box(T value) { this.value = value; }\n    public T get() { return value; }\n}\n\nBox&lt;String&gt; strBox = new Box&lt;&gt;(\"Magic\");\nString s = strBox.get(); // no cast needed\n\nBox&lt;Integer&gt; intBox = new Box&lt;&gt;(42);\nint n = intBox.get();</code></pre>"
                + "<p>Benefits: compile-time type safety, elimination of casts, better IDE support, self-documenting code.</p>"
                + "<p><strong>Multiple type parameters:</strong></p>"
                + "<pre><code>public class Pair&lt;A, B&gt; {\n    public final A first;\n    public final B second;\n    public Pair(A first, B second) { this.first=first; this.second=second; }\n}\nPair&lt;String, Integer&gt; p = new Pair&lt;&gt;(\"Score\", 100);</code></pre>",

                story(
                    n("Eldrin shows you a pre-generics cauldron labelled <em>Object cauldron</em>. He drops in a fireball, a potion, and a boot — all accepted. When retrieving, he must guess what each item is and risks pulling out something unexpected."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Before generics, every container accepted anything. The chaos was spectacular and the crashes, catastrophic. Generics seal the cauldron: <code>Cauldron&lt;Potion&gt;</code> — potions only. The compiler enforces the contract."),
                    e("Generic Box", "class Box<T> {\n    private T value;\n    Box(T v) { value = v; }\n    T get() { return value; }\n}\nBox<String> name = new Box<>(\"Eldrin\");\nBox<Integer> level = new Box<>(99);\nSystem.out.println(name.get() + \" Lv.\" + level.get());"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The <code>T</code> is a placeholder — a type variable. When you create <code>Box&lt;String&gt;</code>, every <code>T</code> inside becomes <code>String</code>. The compiler checks all usages against that contract.")
                ),

                "<p>Create a generic class <code>Pair&lt;A, B&gt;</code> with fields <code>A first</code> and <code>B second</code>, "
                + "a constructor, and getters. Create a <code>Pair&lt;String, Integer&gt;</code> representing a wizard name and level. Print both.</p>",

                "public class GenericsIntro {\n    static class Pair<A, B> {\n        // Add fields, constructor, getters\n\n    }\n    public static void main(String[] args) {\n        Pair<String, Integer> wizard = new Pair<>(\"Eldrin\", 99);\n        System.out.println(wizard.first);\n        System.out.println(wizard.second);\n    }\n}\n",

                tests(
                    test("Name", "null", "Eldrin"),
                    test("Level", "null", "99")
                ),
                "Explain why generics were introduced in Java. What problem do they solve compared to using Object references?"
        );

        mcQuestion("PA1", QuestionTier.RECALL,
                "<p>What does the <code>T</code> represent in <code>class Box&lt;T&gt;</code>?</p>",
                new String[]{"A type parameter — a placeholder for any type", "The letter T as a String", "The return type only", "A type that must extend Object"},
                "A type parameter — a placeholder for any type",
                "<p><code>T</code> is a type parameter — a placeholder replaced by an actual type when the class is instantiated, e.g. <code>Box&lt;String&gt;</code>.</p>");

        tfQuestion("PA1", QuestionTier.RECALL,
                "<p>You can use a primitive type like <code>int</code> directly as a generic type parameter (e.g. <code>Box&lt;int&gt;</code>).</p>",
                "False",
                "<p>Generic type parameters require reference types. Use <code>Box&lt;Integer&gt;</code> (the wrapper class). Java auto-boxes <code>int</code> ↔ <code>Integer</code> automatically.</p>");

        subChunk("PA2", "PA", "Bounded Types & Wildcards", 2, 70, "BoundedTypes.java",

                "<p>What if you want a generic method that works only on types that can be compared — not just any type?</p>",

                "<h3>Bounded Type Parameters</h3>"
                + "<pre><code>// T must implement Comparable\npublic static &lt;T extends Comparable&lt;T&gt;&gt; T max(T a, T b) {\n    return a.compareTo(b) >= 0 ? a : b;\n}\nSystem.out.println(max(10, 25));      // 25\nSystem.out.println(max(\"ant\", \"zebra\")); // zebra</code></pre>"
                + "<h3>Wildcards</h3>"
                + "<p>Use <code>?</code> when the type does not matter to your method:</p>"
                + "<pre><code>// Read from any list of Number or its subtype\nvoid printAll(List&lt;? extends Number&gt; list) {\n    for (Number n : list) System.out.println(n);\n}\n\n// Write to a list of Number or its supertype\nvoid addNumber(List&lt;? super Integer&gt; list) {\n    list.add(42);\n}</code></pre>"
                + "<p><strong>PECS</strong>: Producer Extends, Consumer Super — a mnemonic for when to use each wildcard.</p>",

                story(
                    n("Eldrin wants a spell that finds the more powerful of any two artefacts — but it must work for swords, potions, and scrolls alike."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A bounded type parameter says: I will work with any <code>T</code>, but only if <code>T</code> promises to be Comparable. It narrows the universe of accepted types to only those that can be meaningfully ordered."),
                    e("Generic max", "static <T extends Comparable<T>> T max(T a, T b) {\n    return a.compareTo(b) >= 0 ? a : b;\n}\nSystem.out.println(max(3, 7));       // 7\nSystem.out.println(max(\"apple\", \"mango\")); // mango"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Wildcards — the <code>?</code> — are for when the caller dictates the type and you merely need to read or write. PECS: if a structure <em>produces</em> values for you, use <code>extends</code>; if you <em>consume</em> (add to) it, use <code>super</code>.")
                ),

                "<p>Write a generic static method <code>max(T a, T b)</code> where <code>T extends Comparable&lt;T&gt;</code> "
                + "that returns the larger of the two. Test with two integers and two strings.</p>",

                "public class BoundedTypes {\n    static <T extends Comparable<T>> T max(T a, T b) {\n        // Return the larger of a and b\n\n    }\n    public static void main(String[] args) {\n        System.out.println(max(15, 9));         // 15\n        System.out.println(max(\"zebra\", \"ant\")); // zebra\n    }\n}\n",

                tests(
                    test("Integer max", "null", "15"),
                    test("String max", "null", "zebra")
                ),
                "Explain bounded type parameters in Java generics. What does <T extends Comparable<T>> mean and why is it useful?"
        );

        codeQuestion("PA2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this print?</p>",
                "static <T extends Comparable<T>> T min(T a, T b) {\n    return a.compareTo(b) <= 0 ? a : b;\n}\nSystem.out.println(min(\"banana\", \"apple\"));",
                "apple",
                "<p><code>\"banana\".compareTo(\"apple\")</code> is positive (b > a alphabetically), so <code>a</code> is not ≤ 0, and <code>b</code> (\"apple\") is returned.</p>");

        mcQuestion("PA2", QuestionTier.DISCRIMINATION,
                "<p>You have a method that reads elements from a list and adds them to a total. Which wildcard is appropriate?</p>",
                new String[]{"List<? extends Number>", "List<? super Number>", "List<Number>", "List<?>"},
                "List<? extends Number>",
                "<p>When reading (producing values) from a list, use <code>? extends</code>. This allows <code>List&lt;Integer&gt;</code>, <code>List&lt;Double&gt;</code> etc. — PECS: Producer Extends.</p>");
    }

    // =========================================================================
    // CHUNK PB — Lambdas & Functional Interfaces
    // =========================================================================
    private void seedChunkPB() {
        chunk("PB", "Lambdas & Functional Interfaces", "\u03BB", 16, LearnerPath.PRACTITIONER, "L");

        subChunk("PB1", "PB", "Functional Interfaces & Lambdas", 1, 70, "LambdaBasics.java",

                "<p>A Comparator used to require an anonymous class with 7 lines of boilerplate. A lambda does it in one.</p>",

                "<h3>Functional Interfaces and Lambda Syntax</h3>"
                + "<p>A <strong>functional interface</strong> has exactly one abstract method. The <code>@FunctionalInterface</code> annotation enforces this.</p>"
                + "<pre><code>@FunctionalInterface\ninterface Greeter {\n    void greet(String name);\n}</code></pre>"
                + "<p>A <strong>lambda</strong> is a compact anonymous function that implements a functional interface:</p>"
                + "<pre><code>// Anonymous class (old way)\nGreeter g1 = new Greeter() {\n    public void greet(String name) {\n        System.out.println(\"Hello, \" + name);\n    }\n};\n\n// Lambda (new way)\nGreeter g2 = name -> System.out.println(\"Hello, \" + name);\ng2.greet(\"Eldrin\"); // Hello, Eldrin</code></pre>"
                + "<p>Lambda syntax: <code>(params) -> expression</code> or <code>(params) -> { statements; }</code></p>"
                + "<h3>Built-in Functional Interfaces (java.util.function)</h3>"
                + "<ul>"
                + "<li><code>Predicate&lt;T&gt;</code> — <code>boolean test(T t)</code> — tests a condition</li>"
                + "<li><code>Function&lt;T,R&gt;</code> — <code>R apply(T t)</code> — transforms T to R</li>"
                + "<li><code>Consumer&lt;T&gt;</code> — <code>void accept(T t)</code> — consumes, returns nothing</li>"
                + "<li><code>Supplier&lt;T&gt;</code> — <code>T get()</code> — provides a value</li>"
                + "</ul>",

                story(
                    n("Eldrin holds up an ancient scroll — 20 lines of incantation for one trivial effect. Then he holds up a modern rune: a single line that does the same thing."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A lambda is condensed arcane notation. Where once you wrote an entire anonymous invocation ritual, you now write the essence: the parameters and what to do with them. The compiler infers the rest."),
                    e("Lambda basics", "import java.util.function.*;\n\nPredicate<Integer> isEven = n -> n % 2 == 0;\nFunction<String, Integer> length = String::length;\nConsumer<String> print = System.out::println;\nSupplier<String> greeting = () -> \"Hello!\";\n\nSystem.out.println(isEven.test(4));     // true\nSystem.out.println(length.apply(\"Java\")); // 4\nprint.accept(\"Arcane\");                  // Arcane\nSystem.out.println(greeting.get());     // Hello!"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Note the <code>::</code> — a method reference. <code>String::length</code> is shorthand for the lambda <code>s -> s.length()</code>. Where a lambda merely calls an existing method, a method reference is cleaner.")
                ),

                "<p>Create a <code>Predicate&lt;String&gt;</code> that tests if a string starts with <code>\"A\"</code>. "
                + "Create a <code>Function&lt;String, String&gt;</code> that converts to uppercase. "
                + "Test both on <code>\"Arcane\"</code> and <code>\"magic\"</code>.</p>",

                "import java.util.function.*;\n\npublic class LambdaBasics {\n    public static void main(String[] args) {\n        Predicate<String> startsWithA = s -> s.startsWith(\"A\");\n        Function<String, String> toUpper = s -> s.toUpperCase();\n\n        System.out.println(startsWithA.test(\"Arcane\")); // true\n        System.out.println(startsWithA.test(\"magic\"));  // false\n        System.out.println(toUpper.apply(\"arcane\"));    // ARCANE\n    }\n}\n",

                tests(
                    test("Starts with A", "null", "true"),
                    test("Does not start with A", "null", "false"),
                    test("Uppercase", "null", "ARCANE")
                ),
                "What is a functional interface? Describe the four main functional interfaces in java.util.function and give an example of each."
        );

        mcQuestion("PB1", QuestionTier.RECALL,
                "<p>How many abstract methods can a functional interface have?</p>",
                new String[]{"Exactly one", "Any number", "Zero", "At most two"},
                "Exactly one",
                "<p>A functional interface has exactly one abstract method — this is what allows it to be implemented by a lambda or method reference.</p>");

        tfQuestion("PB1", QuestionTier.RECALL,
                "<p>A lambda expression can be used anywhere a functional interface is expected.</p>",
                "True",
                "<p>Lambdas implement functional interfaces. Wherever you need a functional interface instance, you can supply a lambda that matches the single abstract method's signature.</p>");

        subChunk("PB2", "PB", "Method References & Composing Functions", 2, 70, "MethodReferences.java",

                "<p>When a lambda does nothing but call an existing method, a method reference is even cleaner.</p>",

                "<h3>The Four Types of Method Reference</h3>"
                + "<pre><code>// 1. Static method:   ClassName::staticMethod\nFunction&lt;String,Integer&gt; parse = Integer::parseInt;\n\n// 2. Instance method of a particular object:   instance::method\nString prefix = \"Hello \";\nFunction&lt;String,String&gt; greet = prefix::concat;\n\n// 3. Instance method of an arbitrary object:   ClassName::method\nFunction&lt;String,String&gt; upper = String::toUpperCase;\n\n// 4. Constructor:   ClassName::new\nSupplier&lt;StringBuilder&gt; sbFactory = StringBuilder::new;</code></pre>"
                + "<h3>Composing Functions</h3>"
                + "<pre><code>Function&lt;Integer,Integer&gt; doubleIt = x -> x * 2;\nFunction&lt;Integer,Integer&gt; addTen  = x -> x + 10;\n\n// andThen: apply doubleIt first, then addTen\nFunction&lt;Integer,Integer&gt; combined = doubleIt.andThen(addTen);\nSystem.out.println(combined.apply(5)); // (5*2)+10 = 20\n\n// Predicate composition\nPredicate&lt;Integer&gt; gt5   = n -> n > 5;\nPredicate&lt;Integer&gt; even  = n -> n % 2 == 0;\nPredicate&lt;Integer&gt; both  = gt5.and(even);\nSystem.out.println(both.test(8));  // true\nSystem.out.println(both.test(4));  // false</code></pre>",

                story(
                    n("Eldrin points to a spell book entry that says 'cast upperCase'. 'Why write a whole new incantation for that?' he asks. 'Just reference the one that already exists.'"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Method references are the lazy wizard's best friend — in the complimentary sense. When your lambda body is simply <em>call this method</em>, a method reference makes it unmistakably clear."),
                    e("Method references", "import java.util.*;\nimport java.util.function.*;\n\nList<String> names = Arrays.asList(\"charlie\", \"alice\", \"bob\");\nnames.forEach(System.out::println); // 3. instance of arbitrary\n\nFunction<String,String> up = String::toUpperCase;\nSystem.out.println(up.apply(\"magic\")); // MAGIC"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Composing functions is building spell chains. <code>andThen</code> links them sequentially: the output of one flows into the next. <code>Predicate.and()</code> and <code>.or()</code> combine boolean tests without nested ifs.")
                ),

                "<p>Use method references to: (1) parse \"42\" to int using <code>Integer::parseInt</code>, "
                + "(2) convert \"hello\" to uppercase using <code>String::toUpperCase</code>. Print both results.</p>",

                "import java.util.function.*;\n\npublic class MethodReferences {\n    public static void main(String[] args) {\n        Function<String, Integer> parse = Integer::parseInt;\n        Function<String, String> upper = String::toUpperCase;\n\n        System.out.println(parse.apply(\"42\"));\n        System.out.println(upper.apply(\"hello\"));\n    }\n}\n",

                tests(
                    test("Parse int", "null", "42"),
                    test("Uppercase", "null", "HELLO")
                ),
                "Describe the four types of method reference in Java. Give an example of each and explain when you would use a method reference instead of a lambda."
        );

        mcQuestion("PB2", QuestionTier.APPLICATION,
                "<p><code>names.forEach(System.out::println)</code> — which type of method reference is this?</p>",
                new String[]{"Instance method of a particular object", "Static method", "Instance method of an arbitrary object", "Constructor reference"},
                "Instance method of a particular object",
                "<p><code>System.out</code> is a specific <code>PrintStream</code> instance. <code>System.out::println</code> is an instance method reference on that specific object.</p>");

        codeQuestion("PB2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this print?</p>",
                "import java.util.function.*;\nFunction<Integer,Integer> triple = x -> x * 3;\nFunction<Integer,Integer> minusFive = x -> x - 5;\nSystem.out.println(triple.andThen(minusFive).apply(4));",
                "7",
                "<p>Apply <code>triple</code> first: 4 × 3 = 12. Then <code>minusFive</code>: 12 − 5 = 7.</p>");
    }

    // =========================================================================
    // CHUNK PC — Streams API
    // =========================================================================
    private void seedChunkPC() {
        chunk("PC", "Streams API", "\uD83C\uDF0A", 17, LearnerPath.PRACTITIONER, "PB");

        subChunk("PC1", "PC", "Stream Pipelines & Intermediate Operations", 1, 80, "StreamPipeline.java",

                "<p>What if you could express 'filter the even numbers, double them, and collect the results' as one readable sentence instead of a loop with nested ifs?</p>",

                "<h3>Stream Pipelines</h3>"
                + "<p>A <code>Stream</code> is a sequence of elements supporting pipeline operations. A pipeline has three parts:</p>"
                + "<ol>"
                + "<li><strong>Source</strong> — <code>list.stream()</code>, <code>Stream.of()</code>, <code>IntStream.range()</code></li>"
                + "<li><strong>Intermediate operations</strong> — lazy, return another stream</li>"
                + "<li><strong>Terminal operation</strong> — triggers execution, produces a result</li>"
                + "</ol>"
                + "<pre><code>List&lt;Integer&gt; nums = Arrays.asList(1,2,3,4,5,6,7,8,9,10);\n\nList&lt;Integer&gt; result = nums.stream()\n    .filter(n -> n % 2 == 0)   // keep even\n    .map(n -> n * 3)           // multiply by 3\n    .collect(Collectors.toList());\n\nSystem.out.println(result); // [6, 12, 18, 24, 30]</code></pre>"
                + "<p>Key intermediate operations:</p>"
                + "<ul>"
                + "<li><code>filter(Predicate)</code> — keep matching elements</li>"
                + "<li><code>map(Function)</code> — transform each element</li>"
                + "<li><code>flatMap(Function)</code> — flatten nested streams</li>"
                + "<li><code>sorted()</code> / <code>sorted(Comparator)</code> — sort</li>"
                + "<li><code>distinct()</code> — remove duplicates</li>"
                + "<li><code>limit(n)</code> / <code>skip(n)</code> — slice the stream</li>"
                + "</ul>",

                story(
                    n("Eldrin constructs an elaborate enchanted pipeline: a sieve of magical filters and transformation chambers through which raw ore flows, emerging as refined artefacts at the other end."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A stream pipeline is a declaration of <em>what</em> you want, not <em>how</em> to get it. You tell Java: filter this, transform that, collect the results. Java figures out the most efficient path through the pipeline."),
                    e("Pipeline example", "import java.util.*;\nimport java.util.stream.*;\n\nList<String> words = Arrays.asList(\"arcane\",\"fire\",\"ice\",\"thunder\",\"earth\");\nList<String> result = words.stream()\n    .filter(w -> w.length() > 3)\n    .map(String::toUpperCase)\n    .sorted()\n    .collect(Collectors.toList());\nSystem.out.println(result);"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "One critical rule: streams are <em>lazy</em>. The filter and map operations do nothing until a terminal operation demands the result. And streams are single-use — once consumed, you must create a new one.")
                ),

                "<p>Given <code>Arrays.asList(1,2,3,4,5,6,7,8,9,10)</code>, use a stream to: "
                + "filter to even numbers only, multiply each by 3, collect to a list, and print it.</p>",

                "import java.util.*;\nimport java.util.stream.*;\n\npublic class StreamPipeline {\n    public static void main(String[] args) {\n        List<Integer> nums = Arrays.asList(1,2,3,4,5,6,7,8,9,10);\n        List<Integer> result = nums.stream()\n            // filter evens, multiply by 3, collect\n            .collect(Collectors.toList());\n        System.out.println(result);\n    }\n}\n",

                tests(
                    test("Even × 3", "null", "[6, 12, 18, 24, 30]")
                ),
                "What are the three parts of a Java stream pipeline? Explain the difference between intermediate and terminal operations."
        );

        mcQuestion("PC1", QuestionTier.RECALL,
                "<p>Which of the following is a terminal operation on a Java Stream?</p>",
                new String[]{"collect()", "filter()", "map()", "sorted()"},
                "collect()",
                "<p><code>collect()</code> is a terminal operation — it triggers the pipeline and materialises the result. <code>filter()</code>, <code>map()</code>, and <code>sorted()</code> are intermediate (lazy) operations.</p>");

        tfQuestion("PC1", QuestionTier.RECALL,
                "<p>A Java Stream can be consumed (iterated) multiple times.</p>",
                "False",
                "<p>Streams are single-use. Once a terminal operation has been called, the stream is consumed and cannot be reused. Call <code>.stream()</code> again to create a new one.</p>");

        subChunk("PC2", "PC", "Terminal Operations & Collectors", 2, 80, "TerminalOps.java",

                "<p>Filtering and mapping are only half the story — what you do with the results is just as important.</p>",

                "<h3>Terminal Operations</h3>"
                + "<pre><code>List&lt;Integer&gt; nums = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6);\n\nlong count   = nums.stream().filter(n->n>3).count();       // 4\nOptional&lt;Integer&gt; max = nums.stream().max(Integer::compareTo); // 9\nint sum      = nums.stream().mapToInt(Integer::intValue).sum(); // 31\nboolean any  = nums.stream().anyMatch(n -> n > 8);         // true\nboolean all  = nums.stream().allMatch(n -> n > 0);         // true\nOptional&lt;Integer&gt; first = nums.stream().filter(n->n>4).findFirst(); // 5</code></pre>"
                + "<h3>Collectors</h3>"
                + "<pre><code>// Collect to list\nList&lt;String&gt; list = stream.collect(Collectors.toList());\n\n// Join strings\nString joined = Stream.of(\"A\",\"B\",\"C\")\n    .collect(Collectors.joining(\", \")); // \"A, B, C\"\n\n// Group by\nMap&lt;Integer,List&lt;String&gt;&gt; byLength = words.stream()\n    .collect(Collectors.groupingBy(String::length));\n\n// Count by group\nMap&lt;Integer,Long&gt; countByLength = words.stream()\n    .collect(Collectors.groupingBy(String::length, Collectors.counting()));</code></pre>",

                story(
                    n("Eldrin's pipeline now reaches its output vessel — the terminal operation. The refined ore pours out and is collected, counted, or joined into a final form."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Collectors are the moulds into which your stream pours itself. <code>toList()</code> is a bag. <code>joining()</code> is a scroll that binds strings together. <code>groupingBy()</code> is a sorting hat — it places each element into the right category."),
                    e("Collectors in action", "import java.util.stream.*;\nimport java.util.*;\n\nList<String> spells = Arrays.asList(\"Fire\",\"Ice\",\"Fog\",\"Flash\",\"Force\");\n\n// Join\nString joined = spells.stream().collect(Collectors.joining(\", \"));\nSystem.out.println(joined); // Fire, Ice, Fog, Flash, Force\n\n// Group by first letter\nMap<Character,List<String>> groups = spells.stream()\n    .collect(Collectors.groupingBy(s -> s.charAt(0)));\nSystem.out.println(groups.get('F')); // [Fire, Fog, Flash, Force]"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The <code>reduce()</code> operation is the most powerful terminal: it folds all elements into a single result using an accumulator function. It is the foundation that <code>sum()</code> and <code>count()</code> are built on.")
                ),

                "<p>Given a list of words <code>[\"fire\", \"ice\", \"storm\", \"fog\", \"thunder\"]</code>: "
                + "(1) count how many have length > 3 and print it, "
                + "(2) join all with <code>\" | \"</code> and print.</p>",

                "import java.util.*;\nimport java.util.stream.*;\n\npublic class TerminalOps {\n    public static void main(String[] args) {\n        List<String> words = Arrays.asList(\"fire\",\"ice\",\"storm\",\"fog\",\"thunder\");\n        // Count words with length > 3\n\n        // Join with \" | \"\n\n    }\n}\n",

                tests(
                    test("Count > 3 chars", "null", "3"),
                    test("Joined", "null", "fire | ice | storm | fog | thunder")
                ),
                "Describe three Collectors available in Java's Collectors class. What does groupingBy() produce?"
        );

        codeQuestion("PC2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this print?</p>",
                "import java.util.*; import java.util.stream.*;\nList<Integer> nums = Arrays.asList(2, 4, 6, 8);\nint sum = nums.stream().reduce(0, Integer::sum);\nSystem.out.println(sum);",
                "20",
                "<p><code>reduce(0, Integer::sum)</code> starts with identity 0 and adds each element: 0+2+4+6+8 = 20.</p>");

        mcQuestion("PC2", QuestionTier.DISCRIMINATION,
                "<p>You want to group a list of orders by their status (PENDING, SHIPPED, DELIVERED) and count how many are in each group. Which collector achieves this?</p>",
                new String[]{
                    "Collectors.groupingBy(Order::getStatus, Collectors.counting())",
                    "Collectors.partitioningBy(Order::getStatus)",
                    "Collectors.toMap(Order::getStatus, Order::count)",
                    "Collectors.joining(Order::getStatus)"
                },
                "Collectors.groupingBy(Order::getStatus, Collectors.counting())",
                "<p><code>groupingBy</code> with a downstream <code>counting()</code> collector groups elements and counts each group — exactly what is needed.</p>");

        subChunk("PC3", "PC", "Optional & Primitive Streams", 3, 70, "StreamOptional.java",

                "<p>Stream operations like <code>findFirst()</code> may not find anything — they return <code>Optional</code> to force you to handle the absent case.</p>",

                "<h3>Optional</h3>"
                + "<pre><code>Optional&lt;String&gt; found = names.stream()\n    .filter(n -> n.startsWith(\"Z\"))\n    .findFirst();\n\n// Safe handling\nfound.ifPresent(n -> System.out.println(\"Found: \" + n));\nString result = found.orElse(\"Not found\");\nSystem.out.println(result);\n\n// Transform if present\nOptional&lt;Integer&gt; length = found.map(String::length);</code></pre>"
                + "<h3>Primitive Streams</h3>"
                + "<p><code>IntStream</code>, <code>LongStream</code>, <code>DoubleStream</code> avoid boxing overhead:</p>"
                + "<pre><code>IntStream.range(1, 6).forEach(System.out::println); // 1-5\nIntStream.rangeClosed(1, 5).sum(); // 15\n\n// Stream to IntStream\nint total = words.stream()\n    .mapToInt(String::length)\n    .sum();</code></pre>",

                story(
                    n("Eldrin searches the archive for a scroll matching 'Dragon Lore'. He may find it — or may not. He holds up an Optional crystal: 'If something's inside, it glows.'"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "<code>Optional</code> is a container that either holds a value or is empty. It forces you to acknowledge the possibility of absence — no more silent null surprises. Call <code>orElse()</code> for a fallback; call <code>ifPresent()</code> to act only when something is there."),
                    e("Optional in practice", "import java.util.*;\nList<String> tomes = Arrays.asList(\"Fire Magic\", \"Ice Arts\");\nOptional<String> tome = tomes.stream()\n    .filter(t -> t.contains(\"Dragon\"))\n    .findFirst();\nSystem.out.println(tome.orElse(\"No dragon tome found\"));"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Primitive streams — IntStream, LongStream, DoubleStream — exist because boxing integers to Integer just to sum them is wasteful. When doing arithmetic on streams, always use primitive stream variants for performance.")
                ),

                "<p>Given <code>Arrays.asList(\"apple\", \"banana\", \"cherry\", \"date\")</code>, "
                + "find the first fruit with more than 5 letters. Print it if found, else print <code>\"None\"</code>. "
                + "Then use <code>IntStream.rangeClosed(1,5)</code> to print the sum of 1-5.</p>",

                "import java.util.*;\nimport java.util.stream.*;\n\npublic class StreamOptional {\n    public static void main(String[] args) {\n        List<String> fruits = Arrays.asList(\"apple\",\"banana\",\"cherry\",\"date\");\n        // Find first with length > 5, print or \"None\"\n\n        // Sum 1 to 5 using IntStream.rangeClosed\n\n    }\n}\n",

                tests(
                    test("First long fruit", "null", "banana"),
                    test("Sum 1-5", "null", "15")
                ),
                "What is Optional in Java? Give two examples of methods on Optional and explain how it prevents NullPointerException."
        );

        mcQuestion("PC3", QuestionTier.RECALL,
                "<p>What does <code>Optional.orElse(\"default\")</code> return?</p>",
                new String[]{"The value if present, otherwise \"default\"", "Always \"default\"", "null if empty", "Throws an exception if empty"},
                "The value if present, otherwise \"default\"",
                "<p><code>orElse(fallback)</code> returns the contained value if present, or the fallback value if the Optional is empty.</p>");
    }

    // =========================================================================
    // CHUNK PD — Advanced Collections
    // =========================================================================
    private void seedChunkPD() {
        chunk("PD", "Advanced Collections", "\uD83D\uDDC3\uFE0F", 18, LearnerPath.PRACTITIONER, "PA", "PC");

        subChunk("PD1", "PD", "Set Implementations", 1, 60, "SetTypes.java",

                "<p>What if you need a collection that automatically rejects duplicates? Or one that always stays sorted?</p>",

                "<h3>Set: No Duplicates Allowed</h3>"
                + "<p>The <code>Set</code> interface guarantees uniqueness. Three key implementations:</p>"
                + "<ul>"
                + "<li><code>HashSet&lt;T&gt;</code> — fastest (O(1) operations), no order guarantee</li>"
                + "<li><code>LinkedHashSet&lt;T&gt;</code> — maintains insertion order</li>"
                + "<li><code>TreeSet&lt;T&gt;</code> — keeps elements sorted (O(log n) operations)</li>"
                + "</ul>"
                + "<pre><code>Set&lt;String&gt; set = new HashSet&lt;&gt;();\nset.add(\"Fire\"); set.add(\"Ice\"); set.add(\"Fire\"); // duplicate ignored\nSystem.out.println(set.size()); // 2\n\nSet&lt;Integer&gt; sorted = new TreeSet&lt;&gt;(Arrays.asList(5,2,8,1));\nSystem.out.println(sorted); // [1, 2, 5, 8]</code></pre>"
                + "<p>Set operations:</p>"
                + "<pre><code>Set&lt;String&gt; a = new HashSet&lt;&gt;(Arrays.asList(\"A\",\"B\",\"C\"));\nSet&lt;String&gt; b = new HashSet&lt;&gt;(Arrays.asList(\"B\",\"C\",\"D\"));\n\n// Intersection\na.retainAll(b); // a = {B, C}\n\n// Union\na.addAll(b); // a = {A, B, C, D}</code></pre>",

                story(
                    n("Eldrin empties a bag of magical crystals onto the table — many are identical. He slides them all into a glowing ring that rejects any duplicate that enters."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A Set is the uncompromising collector — it will not hold two identical items. When uniqueness is a requirement, a Set enforces it automatically, with no effort on your part. A HashSet does it fastest; a TreeSet keeps them sorted."),
                    e("HashSet and TreeSet", "import java.util.*;\n\nSet<String> unique = new HashSet<>(Arrays.asList(\"Fire\",\"Ice\",\"Fire\",\"Storm\"));\nSystem.out.println(unique.size()); // 3 (Fire deduplicated)\n\nSet<Integer> sorted = new TreeSet<>(Arrays.asList(9,3,7,1,5));\nSystem.out.println(sorted); // [1, 3, 5, 7, 9]"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "For <code>equals()</code> and <code>hashCode()</code> to work correctly in a HashSet, your objects must implement both. This is why we overrode them earlier — the collection depends on those contracts.")
                ),

                "<p>Create a <code>HashSet&lt;String&gt;</code> from the list <code>[\"alpha\", \"beta\", \"alpha\", \"gamma\", \"beta\"]</code>. "
                + "Print the size (should be 3). Then create a <code>TreeSet</code> from the same list and print it (sorted).</p>",

                "import java.util.*;\n\npublic class SetTypes {\n    public static void main(String[] args) {\n        List<String> items = Arrays.asList(\"alpha\",\"beta\",\"alpha\",\"gamma\",\"beta\");\n        Set<String> hashSet = new HashSet<>(items);\n        System.out.println(hashSet.size());\n\n        Set<String> treeSet = new TreeSet<>(items);\n        System.out.println(treeSet);\n    }\n}\n",

                tests(
                    test("HashSet size (deduped)", "null", "3"),
                    test("TreeSet sorted", "null", "[alpha, beta, gamma]")
                ),
                "Explain the difference between HashSet, LinkedHashSet, and TreeSet. When would you choose each?"
        );

        mcQuestion("PD1", QuestionTier.APPLICATION,
                "<p>You need to track unique visitor IP addresses and quickly check if an IP has been seen before. Which collection is best?</p>",
                new String[]{"HashSet<String>", "ArrayList<String>", "TreeSet<String>", "LinkedList<String>"},
                "HashSet<String>",
                "<p><code>HashSet</code> provides O(1) <code>contains()</code> and automatically rejects duplicates — ideal for unique membership tracking.</p>");

        codeQuestion("PD1", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this print?</p>",
                "import java.util.*;\nSet<Integer> s = new TreeSet<>(Arrays.asList(5,2,8,2,1,8));\nSystem.out.println(s.size() + \" \" + s.first());",
                "4 1",
                "<p>TreeSet deduplicates (5 → 4 unique values) and sorts. <code>first()</code> returns the smallest: 1.</p>");

        subChunk("PD2", "PD", "Queue, Deque & Map Variants", 2, 70, "QueueMap.java",

                "<p>Not every collection problem is about lists. Sometimes you need FIFO ordering, priority-based retrieval, or insertion-ordered maps.</p>",

                "<h3>Queue & Deque</h3>"
                + "<pre><code>// Queue — FIFO (first in, first out)\nQueue&lt;String&gt; queue = new LinkedList&lt;&gt;();\nqueue.offer(\"Alice\"); queue.offer(\"Bob\"); queue.offer(\"Carol\");\nSystem.out.println(queue.poll()); // Alice (removed)\n\n// PriorityQueue — smallest element first\nPriorityQueue&lt;Integer&gt; pq = new PriorityQueue&lt;&gt;();\npq.addAll(Arrays.asList(5, 1, 3, 2, 4));\nwhile (!pq.isEmpty()) System.out.print(pq.poll() + \" \"); // 1 2 3 4 5\n\n// Deque — double-ended (stack or queue)\nDeque&lt;String&gt; deque = new ArrayDeque&lt;&gt;();\ndeque.push(\"first\"); // LIFO stack usage\ndeque.push(\"second\");\nSystem.out.println(deque.pop()); // second</code></pre>"
                + "<h3>Map Variants</h3>"
                + "<pre><code>// Insertion-ordered map\nMap&lt;String,Integer&gt; linked = new LinkedHashMap&lt;&gt;();\n// Key-sorted map\nMap&lt;String,Integer&gt; sorted = new TreeMap&lt;&gt;();\n// Useful Map methods (Java 8+)\nmap.getOrDefault(\"key\", 0);\nmap.putIfAbsent(\"key\", 1);\nmap.computeIfAbsent(\"key\", k -> expensiveCompute(k));\nmap.merge(\"key\", 1, Integer::sum); // increment counter</code></pre>",

                story(
                    n("Eldrin directs the Academy queue of apprentices: 'First in, first seen by the Sorting Hat.' Then he demonstrates the priority queue — the highest-priority tasks are always handled first."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Different shapes of data demand different shapes of container. A queue for task processing; a priority queue when some tasks matter more; a deque when you need both ends; a TreeMap when your keys must always be sorted."),
                    e("PriorityQueue demo", "import java.util.*;\nPriorityQueue<Integer> pq = new PriorityQueue<>();\npq.addAll(Arrays.asList(5,1,3));\nwhile (!pq.isEmpty())\n    System.out.print(pq.poll() + \" \"); // 1 3 5"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The <code>merge()</code> method on Map is one of the most underrated additions in Java 8. For building frequency maps — counting how often each key appears — it replaces a four-line pattern with one line.")
                ),

                "<p>Create a <code>PriorityQueue&lt;Integer&gt;</code> with values <code>[4, 1, 3, 2, 5]</code>. "
                + "Poll all elements and print each on a new line (they should come out in ascending order).</p>",

                "import java.util.*;\n\npublic class QueueMap {\n    public static void main(String[] args) {\n        PriorityQueue<Integer> pq = new PriorityQueue<>(Arrays.asList(4,1,3,2,5));\n        while (!pq.isEmpty()) {\n            System.out.println(pq.poll());\n        }\n    }\n}\n",

                tests(
                    test("Priority order", "null", "1\n2\n3\n4\n5")
                ),
                "What is the difference between Queue and Deque? When would you use a PriorityQueue?"
        );

        mcQuestion("PD2", QuestionTier.RECALL,
                "<p>Which method adds to a Queue and returns false on failure (instead of throwing)?</p>",
                new String[]{"offer()", "add()", "push()", "insert()"},
                "offer()",
                "<p><code>offer()</code> is the safe Queue method — returns false if full. <code>add()</code> throws an exception. Use <code>offer()</code>/<code>poll()</code>/<code>peek()</code> for Queue operations.</p>");

        subChunk("PD3", "PD", "Collections Utility Class & Immutable Collections", 3, 60, "CollectionsUtil.java",

                "<p>Java's <code>Collections</code> class and modern factory methods give you powerful utilities — and sometimes you need collections nobody can tamper with.</p>",

                "<h3>java.util.Collections</h3>"
                + "<pre><code>List&lt;Integer&gt; nums = new ArrayList&lt;&gt;(Arrays.asList(3,1,4,1,5));\nCollections.sort(nums);           // [1, 1, 3, 4, 5]\nCollections.reverse(nums);        // [5, 4, 3, 1, 1]\nCollections.shuffle(nums);        // random order\nSystem.out.println(Collections.min(nums)); // 1\nSystem.out.println(Collections.frequency(nums, 1)); // 2</code></pre>"
                + "<h3>Immutable Collections (Java 9+)</h3>"
                + "<pre><code>// Cannot be modified — add/remove throws UnsupportedOperationException\nList&lt;String&gt; fixed = List.of(\"A\", \"B\", \"C\");\nMap&lt;String,Integer&gt; config = Map.of(\"timeout\", 30, \"retries\", 3);\nSet&lt;String&gt; roles = Set.of(\"ADMIN\", \"USER\", \"GUEST\");\n\n// Unmodifiable wrapper around existing collection\nList&lt;String&gt; readOnly = Collections.unmodifiableList(mutableList);</code></pre>"
                + "<p>Immutable collections are thread-safe and communicate intent — they tell readers: this data is fixed.</p>",

                story(
                    n("Eldrin opens the Collections grimoire — a compendium of algorithms that no wizard should ever rewrite."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The <code>Collections</code> utility class is your shortcut to dozens of common algorithms. Sort, reverse, shuffle, min, max, frequency — all written by engineers far more patient than either of us. Use them."),
                    e("Collections utilities", "import java.util.*;\nList<Integer> vals = new ArrayList<>(Arrays.asList(5,2,8,1,9));\nCollections.sort(vals);\nSystem.out.println(vals);           // [1, 2, 5, 8, 9]\nSystem.out.println(Collections.max(vals)); // 9"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "And immutable collections — <code>List.of()</code>, <code>Map.of()</code> — are the sealed scrolls of the data world. Once created, they cannot be altered. They cost less memory, are thread-safe, and announce loudly: this data is fixed by design.")
                ),

                "<p>Create an <code>ArrayList</code> of <code>[5, 2, 8, 1, 9]</code>. "
                + "Sort it using <code>Collections.sort()</code> and print. "
                + "Then print the maximum using <code>Collections.max()</code>.</p>",

                "import java.util.*;\n\npublic class CollectionsUtil {\n    public static void main(String[] args) {\n        List<Integer> nums = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9));\n        Collections.sort(nums);\n        System.out.println(nums);\n        System.out.println(Collections.max(nums));\n    }\n}\n",

                tests(
                    test("Sorted list", "null", "[1, 2, 5, 8, 9]"),
                    test("Maximum", "null", "9")
                ),
                "What is the difference between List.of() and new ArrayList()? Why would you choose an immutable collection?"
        );

        mcQuestion("PD3", QuestionTier.RECALL,
                "<p>What happens when you call <code>add()</code> on a list created with <code>List.of()</code>?</p>",
                new String[]{"Throws UnsupportedOperationException", "The element is added normally", "Returns false", "Creates a new list with the element"},
                "Throws UnsupportedOperationException",
                "<p><code>List.of()</code> creates an immutable list. Any attempt to modify it — add, remove, or set — throws <code>UnsupportedOperationException</code>.</p>");
    }

    // =========================================================================
    // CHUNK PE — File I/O
    // =========================================================================
    private void seedChunkPE() {
        chunk("PE", "File I/O", "\uD83D\uDCC1", 19, LearnerPath.PRACTITIONER, "M");

        subChunk("PE1", "PE", "Reading & Writing Files (NIO.2)", 1, 70, "FileReadWrite.java",

                "<p>Your application needs to persist data between runs, read configuration files, or process logs. Java's modern NIO.2 API makes this clean and concise.</p>",

                "<h3>java.nio.file: The Modern Way</h3>"
                + "<pre><code>import java.nio.file.*;\nimport java.util.*;\n\n// Read all lines at once\nPath path = Path.of(\"spells.txt\");\nList&lt;String&gt; lines = Files.readAllLines(path);\n\n// Read entire file as String\nString content = Files.readString(path);\n\n// Write lines to file (overwrites)\nList&lt;String&gt; spells = List.of(\"Fireball\", \"Ice Lance\", \"Thunder\");\nFiles.write(path, spells);\n\n// Append to existing file\nFiles.writeString(path, \"\\nNew Spell\", StandardOpenOption.APPEND);</code></pre>"
                + "<h3>try-with-resources</h3>"
                + "<p>For streaming large files, use <code>BufferedReader</code> in a try-with-resources block — it closes the file automatically:</p>"
                + "<pre><code>try (BufferedReader reader = Files.newBufferedReader(path)) {\n    String line;\n    while ((line = reader.readLine()) != null) {\n        System.out.println(line);\n    }\n} // reader closed automatically</code></pre>",

                story(
                    n("Eldrin opens the Academy's scroll archive and begins reading from a magical tome that connects to the physical scroll rack on the wall."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "File I/O is your application's memory beyond the session. Without it, every run starts fresh. With it, spells persist, logs accumulate, configurations endure. The NIO.2 API — <code>Files</code> and <code>Path</code> — is the modern way."),
                    e("Read and write", "import java.nio.file.*;\nimport java.util.*;\n\nPath p = Path.of(\"test.txt\");\nFiles.write(p, List.of(\"line1\", \"line2\", \"line3\"));\nList<String> lines = Files.readAllLines(p);\nSystem.out.println(lines.size()); // 3\nlines.forEach(System.out::println);"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Always use try-with-resources when working with streams or readers. The file handle closes automatically when the block exits — even if an exception is thrown. A forgotten open file handle is a resource leak.")
                ),

                "<p>Write a list of three lines to a temp file, read them back, and print the line count then each line.</p>",

                "import java.nio.file.*;\nimport java.util.*;\nimport java.io.IOException;\n\npublic class FileReadWrite {\n    public static void main(String[] args) throws IOException {\n        Path path = Files.createTempFile(\"arcane\", \".txt\");\n        List<String> content = List.of(\"Fireball\", \"Ice Lance\", \"Thunder\");\n        Files.write(path, content);\n\n        List<String> lines = Files.readAllLines(path);\n        System.out.println(lines.size());\n        lines.forEach(System.out::println);\n        Files.delete(path);\n    }\n}\n",

                tests(
                    test("Line count", "null", "3"),
                    test("Lines content", "null", "Fireball\nIce Lance\nThunder")
                ),
                "Explain the difference between Files.readAllLines() and using a BufferedReader. When would you prefer each?"
        );

        mcQuestion("PE1", QuestionTier.RECALL,
                "<p>What is the main advantage of try-with-resources when doing file I/O?</p>",
                new String[]{"Resources are automatically closed even if an exception occurs", "It reads files faster", "It prevents IOException", "It works without imports"},
                "Resources are automatically closed even if an exception occurs",
                "<p>Try-with-resources calls <code>close()</code> automatically on any <code>AutoCloseable</code> resource, ensuring file handles are never leaked — even when exceptions are thrown.</p>");

        subChunk("PE2", "PE", "Paths, File Operations & Serialisation", 2, 70, "FilePaths.java",

                "<p>Beyond reading and writing, you often need to check if files exist, create directories, copy files, or persist entire Java objects.</p>",

                "<h3>File Operations with Files and Path</h3>"
                + "<pre><code>Path p = Path.of(\"data\", \"config.txt\"); // data/config.txt\nSystem.out.println(p.getFileName()); // config.txt\nSystem.out.println(p.getParent());   // data\n\nFiles.exists(p);                         // boolean\nFiles.createDirectories(p.getParent());  // create dir tree\nFiles.copy(source, dest);               // copy file\nFiles.move(source, dest);               // move/rename\nFiles.delete(p);                        // delete\n\n// Walk a directory tree\nFiles.walk(Path.of(\".\"))\n    .filter(Files::isRegularFile)\n    .forEach(System.out::println);</code></pre>"
                + "<h3>Object Serialisation</h3>"
                + "<pre><code>// Write object to file\ntry (ObjectOutputStream oos =\n        new ObjectOutputStream(new FileOutputStream(\"wizard.ser\"))) {\n    oos.writeObject(wizard);\n}\n\n// Read object back\ntry (ObjectInputStream ois =\n        new ObjectInputStream(new FileInputStream(\"wizard.ser\"))) {\n    Wizard loaded = (Wizard) ois.readObject();\n}</code></pre>"
                + "<p>The class must implement <code>Serializable</code>. Declare <code>serialVersionUID</code> to avoid incompatibilities between versions.</p>",

                story(
                    n("Eldrin seals an entire Wizard object inside a magical crystal for transport to another academy — the serialised form persists the object's complete state."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Serialisation is object teleportation — you freeze an object's state to a byte stream, ship it across a network or to disk, and reconstruct it identically on the other end. It requires the <code>Serializable</code> marker contract."),
                    e("Serialise and deserialise", "import java.io.*;\n\nclass Spell implements Serializable {\n    String name; int power;\n    Spell(String n, int p) { name=n; power=p; }\n}\n\n// Write\ntry (ObjectOutputStream oos = new ObjectOutputStream(\n        new FileOutputStream(\"spell.ser\"))) {\n    oos.writeObject(new Spell(\"Fireball\", 80));\n}\n\n// Read\ntry (ObjectInputStream ois = new ObjectInputStream(\n        new FileInputStream(\"spell.ser\"))) {\n    Spell s = (Spell) ois.readObject();\n    System.out.println(s.name + \": \" + s.power);\n}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "In modern applications, JSON has largely replaced native serialisation for data exchange — it's human-readable, language-agnostic, and more robust to class changes. But Java serialisation remains useful for internal caching and RMI.")
                ),

                "<p>Serialise a simple <code>Wizard</code> object (name, level) to a temp file, then deserialise and print its fields.</p>",

                "import java.io.*;\nimport java.nio.file.*;\n\npublic class FilePaths {\n    static class Wizard implements Serializable {\n        String name; int level;\n        Wizard(String n, int l) { name=n; level=l; }\n    }\n    public static void main(String[] args) throws Exception {\n        Path temp = Files.createTempFile(\"wizard\", \".ser\");\n        try (ObjectOutputStream oos = new ObjectOutputStream(\n                new FileOutputStream(temp.toFile()))) {\n            oos.writeObject(new Wizard(\"Merlin\", 99));\n        }\n        try (ObjectInputStream ois = new ObjectInputStream(\n                new FileInputStream(temp.toFile()))) {\n            Wizard w = (Wizard) ois.readObject();\n            System.out.println(w.name);\n            System.out.println(w.level);\n        }\n        Files.delete(temp);\n    }\n}\n",

                tests(
                    test("Wizard name", "null", "Merlin"),
                    test("Wizard level", "null", "99")
                ),
                "What is Java object serialisation? What interface must a class implement to be serialised, and what is serialVersionUID for?"
        );

        mcQuestion("PE2", QuestionTier.RECALL,
                "<p>What interface must a Java class implement to be serialisable?</p>",
                new String[]{"java.io.Serializable", "java.io.Writable", "java.lang.Cloneable", "java.io.ObjectStream"},
                "java.io.Serializable",
                "<p><code>Serializable</code> is a marker interface (no methods) that signals to the JVM that a class's instances can be serialised to a byte stream.</p>");

        tfQuestion("PE2", QuestionTier.DISCRIMINATION,
                "<p>Fields marked <code>transient</code> in a serialisable class are saved to the byte stream.</p>",
                "False",
                "<p><code>transient</code> fields are explicitly excluded from serialisation — useful for sensitive data (passwords) or fields that cannot be serialised (threads, streams).</p>");
    }

    // =========================================================================
    // CHUNK PF — Logging
    // =========================================================================
    private void seedChunkPF() {
        chunk("PF", "Logging", "\uD83D\uDCCB", 20, LearnerPath.PRACTITIONER, "M");

        subChunk("PF1", "PF", "Why Log & Log Levels", 1, 60, "LoggingBasics.java",

                "<p>You deploy your application and something goes wrong. There's no debugger, no console — just your logs. What do they say?</p>",

                "<h3>Logging vs System.out.println</h3>"
                + "<p>Problems with <code>System.out.println</code> in production:</p>"
                + "<ul>"
                + "<li>No timestamp — when did this happen?</li>"
                + "<li>No level — is this info or a critical error?</li>"
                + "<li>No context — which class, which thread?</li>"
                + "<li>Cannot be turned off without redeployment</li>"
                + "<li>Performance: always executes, even in debug-only paths</li>"
                + "</ul>"
                + "<p>Proper logging with <code>java.util.logging</code>:</p>"
                + "<pre><code>import java.util.logging.*;\n\npublic class SpellService {\n    private static final Logger log =\n        Logger.getLogger(SpellService.class.getName());\n\n    public void castSpell(String name) {\n        log.info(\"Casting spell: \" + name);\n        log.warning(\"Spell power critically low!\");\n        log.severe(\"Spell failed: NullPointerException\");\n    }\n}</code></pre>"
                + "<p><strong>Log levels</strong> (lowest to highest): FINEST → FINER → FINE → CONFIG → INFO → WARNING → SEVERE. "
                + "SLF4J/Logback levels: TRACE → DEBUG → INFO → WARN → ERROR.</p>",

                story(
                    n("Eldrin reviews the incident report from last night's spell failure. The log is a jumble of println statements with no timestamps, no severity markers, no class names."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A log without structure is noise. A structured log is a story: <em>when</em> this happened, <em>where</em> in the codebase, <em>how severe</em> it was. That is the difference between a System.out.println and a proper logging statement."),
                    e("Structured logging", "import java.util.logging.*;\nLogger log = Logger.getLogger(\"AcademyApp\");\nlog.info(\"Application started\");\nlog.warning(\"Low mana detected\");\nlog.severe(\"Spell exploded — critical failure\");"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "In production you use SLF4J with a Logback implementation. In that world: TRACE for the minutest detail, DEBUG for developer diagnostics, INFO for normal operation milestones, WARN for unexpected-but-recoverable events, ERROR for failures that need attention.")
                ),

                "<p>Create a <code>Logger</code> named <code>\"SpellLogger\"</code>. "
                + "Log one INFO message <code>\"Spell cast: Fireball\"</code> and one WARNING <code>\"Low mana!\"</code>. "
                + "Configure it to log to console using a <code>ConsoleHandler</code> set to <code>Level.ALL</code>.</p>",

                "import java.util.logging.*;\n\npublic class LoggingBasics {\n    public static void main(String[] args) {\n        Logger logger = Logger.getLogger(\"SpellLogger\");\n        logger.setUseParentHandlers(false);\n        ConsoleHandler handler = new ConsoleHandler();\n        handler.setLevel(Level.ALL);\n        logger.addHandler(handler);\n        logger.setLevel(Level.ALL);\n\n        logger.info(\"Spell cast: Fireball\");\n        logger.warning(\"Low mana!\");\n    }\n}\n",

                tests(
                    test("INFO logged", "null", "Spell cast: Fireball"),
                    test("WARNING logged", "null", "Low mana!")
                ),
                "Why is System.out.println inadequate for production logging? Describe the five main log levels and when you would use each."
        );

        mcQuestion("PF1", QuestionTier.RECALL,
                "<p>Which log level indicates a condition that might cause problems but from which the application recovered?</p>",
                new String[]{"WARN / WARNING", "DEBUG", "INFO", "ERROR / SEVERE"},
                "WARN / WARNING",
                "<p>WARN indicates unexpected situations that the application handled but that deserve attention. ERROR/SEVERE means a failure requiring immediate action.</p>");

        subChunk("PF2", "PF", "SLF4J, Logback & Best Practices", 2, 70, "SLF4JPattern.java",

                "<p>In real Spring Boot applications, the logging framework is SLF4J (facade) backed by Logback (implementation). Understanding the pattern prepares you for professional development.</p>",

                "<h3>SLF4J Pattern</h3>"
                + "<pre><code>// In a Spring Boot / Maven project, just add the dependency\n// SLF4J is included with spring-boot-starter\nimport org.slf4j.Logger;\nimport org.slf4j.LoggerFactory;\n\npublic class WizardService {\n    private static final Logger log =\n        LoggerFactory.getLogger(WizardService.class);\n\n    public void enrolWizard(String name) {\n        log.debug(\"Enrolling wizard: {}\", name);   // parameterised!\n        log.info(\"Wizard {} enrolled successfully\", name);\n    }\n}</code></pre>"
                + "<p><strong>Parameterised logging</strong> — use <code>{}</code> placeholders instead of string concatenation. The string is only built if the log level is active:</p>"
                + "<pre><code>// Bad — concatenation always happens\nlog.debug(\"Processing \" + expensiveCall() + \" items\");\n\n// Good — expensiveCall() not called if DEBUG is off\nlog.debug(\"Processing {} items\", expensiveCall());</code></pre>"
                + "<h3>Logback Configuration (logback.xml)</h3>"
                + "<pre><code>&lt;configuration&gt;\n  &lt;appender name=\"CONSOLE\" class=\"ch.qos.logback.core.ConsoleAppender\"&gt;\n    &lt;encoder&gt;\n      &lt;pattern&gt;%d{HH:mm:ss} %-5level %logger{36} - %msg%n&lt;/pattern&gt;\n    &lt;/encoder&gt;\n  &lt;/appender&gt;\n  &lt;root level=\"INFO\"&gt;\n    &lt;appender-ref ref=\"CONSOLE\"/&gt;\n  &lt;/root&gt;\n&lt;/configuration&gt;</code></pre>",

                story(
                    n("Eldrin reveals the Academy's professional logging grimoire — SLF4J, the façade that speaks to any logging library underneath."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "SLF4J is a layer of abstraction. Your code only knows SLF4J. Logback, Log4j, JUL — the actual implementation — can be swapped without changing a single line of your application code. This is the bridge pattern in practice."),
                    e("SLF4J pattern", "// import org.slf4j.*; — with SLF4J on classpath\nLogger log = LoggerFactory.getLogger(MyClass.class);\nlog.info(\"User {} logged in from {}\", username, ip);\nlog.error(\"Failed to process request\", exception);"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The curly-brace placeholders are critical. String concatenation in a debug statement runs even when the debug level is disabled. With <code>{}</code> placeholders, the message is only formatted if the level is active — sometimes a significant performance win.")
                ),

                "<p>Demonstrate the logging pattern using <code>java.util.logging</code> (since SLF4J requires a dependency). "
                + "Show parameterised-style logging (manual string format), log at INFO and WARNING level, "
                + "and demonstrate that a finer-level message doesn't appear when the level is set to INFO.</p>",

                "import java.util.logging.*;\n\npublic class SLF4JPattern {\n    static final Logger log = Logger.getLogger(\"Demo\");\n    static {\n        log.setUseParentHandlers(false);\n        ConsoleHandler h = new ConsoleHandler();\n        h.setLevel(Level.INFO);\n        log.addHandler(h);\n        log.setLevel(Level.INFO);\n    }\n    public static void main(String[] args) {\n        String user = \"Eldrin\";\n        log.info(String.format(\"User %s logged in\", user));  // appears\n        log.fine(\"Debug detail — should not appear\");          // filtered\n        log.warning(\"Low mana warning\");                        // appears\n        System.out.println(\"Logging demo complete\");\n    }\n}\n",

                tests(
                    test("Info logged", "null", "User Eldrin logged in"),
                    test("Warning logged", "null", "Low mana warning"),
                    test("Demo complete", "null", "Logging demo complete")
                ),
                "What is the advantage of using {} placeholders in SLF4J logging over string concatenation?"
        );

        mcQuestion("PF2", QuestionTier.DISCRIMINATION,
                "<p>Why should you use <code>log.debug(\"Value: {}\", computeHeavy())</code> instead of <code>log.debug(\"Value: \" + computeHeavy())</code>?</p>",
                new String[]{"computeHeavy() is only called if DEBUG level is active", "The first version logs faster always", "String concatenation doesn't work with loggers", "The second version always throws an exception"},
                "computeHeavy() is only called if DEBUG level is active",
                "<p>With string concatenation, <code>computeHeavy()</code> always executes even if DEBUG logging is disabled. With <code>{}</code> placeholders, the argument is only evaluated when the message is actually going to be logged.</p>");

        tfQuestion("PF2", QuestionTier.RECALL,
                "<p>SLF4J is a logging implementation — it writes log statements to files and the console.</p>",
                "False",
                "<p>SLF4J is a <em>facade</em> (API only). The actual writing is done by a backing implementation (Logback, Log4j2, JUL). You program against SLF4J so you can swap implementations without changing your code.</p>");
    }

    // =========================================================================
    // CHUNK PG — Multithreading
    // =========================================================================
    private void seedChunkPG() {
        chunk("PG", "Multithreading", "\u2699\uFE0F", 21, LearnerPath.PRACTITIONER, "D");

        subChunk("PG1", "PG", "Threads & the Thread Lifecycle", 1, 70, "ThreadBasics.java",

                "<p>Your download manager shows a progress bar while downloading — the UI and the download run simultaneously. How?</p>",

                "<h3>Threads: Concurrent Execution</h3>"
                + "<p>A <strong>thread</strong> is an independent unit of execution. The JVM starts with one thread (main); you can create more:</p>"
                + "<pre><code>// Option 1: Implement Runnable\nRunnable task = () -> {\n    for (int i = 0; i < 3; i++)\n        System.out.println(Thread.currentThread().getName() + \": \" + i);\n};\nThread t = new Thread(task, \"WorkerThread\");\nt.start(); // starts new thread\n\n// Option 2: Extend Thread\nclass MyThread extends Thread {\n    @Override public void run() {\n        System.out.println(\"Running in \" + getName());\n    }\n}\nnew MyThread().start();</code></pre>"
                + "<p><strong>Thread lifecycle:</strong> NEW → RUNNABLE → (BLOCKED/WAITING/TIMED_WAITING) → TERMINATED</p>"
                + "<p><code>t.join()</code> — wait for thread to finish before continuing. <code>Thread.sleep(ms)</code> — pause the current thread.</p>",

                story(
                    n("Eldrin summons three apprentice golems simultaneously — each begins a separate task at the same instant. The spell chamber echoes with three different incantations being cast in parallel."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A thread is a parallel stream of execution. Your program's main method runs on the main thread. Every new <code>Thread</code> you create and <code>start()</code> runs concurrently. Note: <em>start()</em>, not <em>run()</em>. Calling <code>run()</code> directly does not start a new thread."),
                    e("Two threads", "Runnable r = () -> {\n    for (int i = 0; i < 3; i++)\n        System.out.println(Thread.currentThread().getName()+\":\" + i);\n};\nThread t1 = new Thread(r, \"T1\");\nThread t2 = new Thread(r, \"T2\");\nt1.start(); t2.start();\nt1.join(); t2.join();"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "<code>join()</code> is the waiting ward — it blocks the current thread until the target thread finishes. Without it, your main method might exit before your worker threads complete their work.")
                ),

                "<p>Create two threads using lambda Runnables. Thread 1 prints <code>\"T1: 0\"</code> through <code>\"T1: 2\"</code>. "
                + "Thread 2 prints <code>\"T2: 0\"</code> through <code>\"T2: 2\"</code>. "
                + "Start both and join both so the main thread waits for completion.</p>",

                "public class ThreadBasics {\n    public static void main(String[] args) throws InterruptedException {\n        Thread t1 = new Thread(() -> {\n            for (int i = 0; i < 3; i++)\n                System.out.println(\"T1: \" + i);\n        });\n        Thread t2 = new Thread(() -> {\n            for (int i = 0; i < 3; i++)\n                System.out.println(\"T2: \" + i);\n        });\n        t1.start();\n        t2.start();\n        t1.join();\n        t2.join();\n        System.out.println(\"Done\");\n    }\n}\n",

                tests(
                    test("T1 output present", "null", "T1: 0"),
                    test("T2 output present", "null", "T2: 0"),
                    test("Completion", "null", "Done")
                ),
                "Explain the Thread lifecycle in Java. What is the difference between calling start() and run() on a Thread?"
        );

        mcQuestion("PG1", QuestionTier.RECALL,
                "<p>What is the correct way to start a new thread of execution in Java?</p>",
                new String[]{"Call t.start()", "Call t.run()", "Call t.execute()", "Call t.begin()"},
                "Call t.start()",
                "<p><code>start()</code> creates a new OS thread and calls <code>run()</code> inside it. Calling <code>run()</code> directly executes on the current thread — no new thread is created.</p>");

        tfQuestion("PG1", QuestionTier.RECALL,
                "<p>The order in which threads print output is always the same between runs.</p>",
                "False",
                "<p>Thread scheduling is non-deterministic. The OS decides which thread runs when — output order can vary between runs, which is exactly why concurrent programming is challenging.</p>");

        subChunk("PG2", "PG", "Synchronisation & Race Conditions", 2, 80, "Synchronization.java",

                "<p>Two threads both increment a counter. You expect 2000 — you get 1843. Welcome to race conditions.</p>",

                "<h3>Race Conditions and Synchronisation</h3>"
                + "<p>A <strong>race condition</strong> occurs when multiple threads access shared mutable state without coordination. The increment operation (<code>count++</code>) is not atomic — it's actually three steps:</p>"
                + "<ol><li>Read <code>count</code></li><li>Add 1</li><li>Write back</li></ol>"
                + "<p>Two threads can interleave these steps and lose updates. The fix: <code>synchronized</code>:</p>"
                + "<pre><code>class SafeCounter {\n    private int count = 0;\n\n    // Only one thread can execute this at a time\n    public synchronized void increment() {\n        count++;\n    }\n    public synchronized int getCount() { return count; }\n}\n\n// Or use synchronized block (finer control)\nsynchronized (lockObject) {\n    // critical section\n}</code></pre>"
                + "<p><strong>AtomicInteger</strong> from <code>java.util.concurrent.atomic</code> is often better — lock-free and faster than synchronized for single-variable operations:</p>"
                + "<pre><code>AtomicInteger counter = new AtomicInteger(0);\ncounter.incrementAndGet(); // atomic, no locks</code></pre>",

                story(
                    n("Two golems simultaneously reach for the same mana crystal, each trying to record how much power they've drawn. Their simultaneous reads and writes collide — the total is wrong."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "That is a race condition. The golems compete for a shared resource without coordination. <code>synchronized</code> places a ward around the critical section — only one golem may enter at a time. The others wait."),
                    e("Synchronized counter", "import java.util.concurrent.atomic.*;\n\nAtomicInteger counter = new AtomicInteger(0);\nThread t1 = new Thread(() -> { for(int i=0;i<1000;i++) counter.incrementAndGet(); });\nThread t2 = new Thread(() -> { for(int i=0;i<1000;i++) counter.incrementAndGet(); });\nt1.start(); t2.start(); t1.join(); t2.join();\nSystem.out.println(counter.get()); // always 2000"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "<code>AtomicInteger</code> uses CPU-level compare-and-swap operations — faster than a <code>synchronized</code> lock for a single counter. For more complex invariants spanning multiple variables, <code>synchronized</code> is still necessary.")
                ),

                "<p>Create an <code>AtomicInteger</code> counter. Start 10 threads, each incrementing it 100 times. "
                + "After joining all threads, print the final count (should always be 1000).</p>",

                "import java.util.concurrent.atomic.*;\n\npublic class Synchronization {\n    public static void main(String[] args) throws InterruptedException {\n        AtomicInteger counter = new AtomicInteger(0);\n        Thread[] threads = new Thread[10];\n        for (int i = 0; i < 10; i++) {\n            threads[i] = new Thread(() -> {\n                for (int j = 0; j < 100; j++)\n                    counter.incrementAndGet();\n            });\n            threads[i].start();\n        }\n        for (Thread t : threads) t.join();\n        System.out.println(counter.get());\n    }\n}\n",

                tests(
                    test("Final count", "null", "1000")
                ),
                "What is a race condition? Explain how synchronized and AtomicInteger each prevent data corruption in multi-threaded code."
        );

        mcQuestion("PG2", QuestionTier.RECALL,
                "<p>What does the <code>synchronized</code> keyword guarantee?</p>",
                new String[]{"Only one thread executes the synchronized code at a time", "The code runs faster", "All threads run at the same speed", "The code is thread-local"},
                "Only one thread executes the synchronized code at a time",
                "<p><code>synchronized</code> acquires the object's intrinsic lock. Other threads attempting to enter a synchronized method/block on the same object block until the lock is released.</p>");

        codeQuestion("PG2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>Why might this code print a value less than 2000?</p>",
                "class Counter { int n = 0; void inc() { n++; } }\nCounter c = new Counter();\n// 2 threads each call c.inc() 1000 times concurrently",
                "n++ is not atomic — read, increment, write can interleave between threads losing updates",
                "<p><code>n++</code> is three operations (read, add, write). Two threads can both read the same value, both increment, and both write back — losing one update. Use <code>synchronized</code> or <code>AtomicInteger</code>.</p>");

        subChunk("PG3", "PG", "volatile, ThreadLocal & Deadlock", 3, 70, "ThreadSafety.java",

                "<p>Not all concurrency problems require full synchronisation — sometimes you just need visibility. And sometimes synchronisation itself causes the bug.</p>",

                "<h3>volatile</h3>"
                + "<p>The <code>volatile</code> keyword guarantees that a variable's value is always read from and written to main memory — not a CPU cache. Use it for simple flags:</p>"
                + "<pre><code>volatile boolean running = true;\n\n// Thread 1\nnew Thread(() -> {\n    while (running) { /* work */ }\n    System.out.println(\"Stopped\");\n}).start();\n\n// Thread 2 (after a while)\nrunning = false; // visible immediately to Thread 1</code></pre>"
                + "<h3>ThreadLocal</h3>"
                + "<pre><code>ThreadLocal&lt;String&gt; userId = new ThreadLocal&lt;&gt;();\nuserId.set(\"wizard_42\");\nSystem.out.println(userId.get()); // \"wizard_42\" for THIS thread only</code></pre>"
                + "<h3>Deadlock</h3>"
                + "<p>Deadlock: Thread A holds lock 1 and waits for lock 2; Thread B holds lock 2 and waits for lock 1 — neither can proceed. Prevention: always acquire locks in the same order, or use <code>tryLock()</code> with timeouts.</p>",

                story(
                    n("Two golems carry treasure — Golem A holds the gold key and wants the silver key; Golem B holds the silver key and wants the gold key. They stare at each other forever."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Deadlock is the eternal standoff. Neither thread can proceed because each holds what the other needs. Prevention is simple in principle: always acquire multiple locks in a consistent order. In practice, it requires discipline."),
                    e("ThreadLocal example", "ThreadLocal<String> context = new ThreadLocal<>();\nThread t1 = new Thread(() -> {\n    context.set(\"Thread-1-context\");\n    System.out.println(context.get());\n});\nThread t2 = new Thread(() -> {\n    context.set(\"Thread-2-context\");\n    System.out.println(context.get());\n});\nt1.start(); t2.start();"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "<code>ThreadLocal</code> gives each thread its own isolated copy of a variable — a per-thread rune that no other thread can read or write. Used extensively in frameworks to carry request-scoped context like user sessions.")
                ),

                "<p>Demonstrate <code>ThreadLocal</code>: create a <code>ThreadLocal&lt;String&gt;</code>, "
                + "set it to <code>\"Thread-A\"</code> in one thread and <code>\"Thread-B\"</code> in another. "
                + "Each thread prints its own value. Both values should appear (order may vary).</p>",

                "public class ThreadSafety {\n    static ThreadLocal<String> local = new ThreadLocal<>();\n    public static void main(String[] args) throws InterruptedException {\n        Thread a = new Thread(() -> { local.set(\"Thread-A\"); System.out.println(local.get()); });\n        Thread b = new Thread(() -> { local.set(\"Thread-B\"); System.out.println(local.get()); });\n        a.start(); b.start(); a.join(); b.join();\n    }\n}\n",

                tests(
                    test("Thread A value", "null", "Thread-A"),
                    test("Thread B value", "null", "Thread-B")
                ),
                "What is the difference between volatile and synchronized? Explain what a deadlock is and how to prevent it."
        );

        mcQuestion("PG3", QuestionTier.RECALL,
                "<p>What does <code>volatile</code> guarantee in Java?</p>",
                new String[]{"Reads and writes go directly to main memory (visibility), not CPU caches", "Only one thread can write the variable at a time (atomicity)", "The variable cannot be changed", "The variable is initialised to zero"},
                "Reads and writes go directly to main memory (visibility), not CPU caches",
                "<p><code>volatile</code> guarantees visibility — threads always see the latest value. It does NOT guarantee atomicity — compound operations like <code>n++</code> still need synchronisation.</p>");

        tfQuestion("PG3", QuestionTier.DISCRIMINATION,
                "<p>ThreadLocal variables are shared between all threads that access them.</p>",
                "False",
                "<p><code>ThreadLocal</code> gives each thread its own independent copy of the variable. One thread's value cannot be seen by another thread — that is the entire purpose of ThreadLocal.</p>");
    }
}
