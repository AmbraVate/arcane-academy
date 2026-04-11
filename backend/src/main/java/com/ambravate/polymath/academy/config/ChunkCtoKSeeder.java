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
                story(n("The ancient scrolls speak of <em>repetition spells</em> — incantations that echo themselves until their power is spent."),
                      d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "npc", "A for loop is like setting a spell to cast itself a certain number of times. You define when it starts, when it stops, and how it progresses.")),
                "<p>Print the numbers 1 through 5, each on a new line.</p>",
                "for (int i = 1; i <= 5; i++) {\n    System.out.println(i);\n}",
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
                story(n("While loops are like guard spells — they keep running as long as a condition holds true.")),
                "<p>Print numbers from 10 down to 1 using a while loop.</p>",
                "int n = 10;\nwhile (n >= 1) {\n    System.out.println(n);\n    n--;\n}",
                tests(test("Countdown", "null", "10\n9\n8\n7\n6\n5\n4\n3\n2\n1")),
                "Explain the difference between a for loop and a while loop. When would you choose one over the other?");

        mcQuestion("C2", QuestionTier.RECALL, "When does a while loop stop?",
                new String[]{"When its condition becomes false", "After a fixed number of iterations", "When it runs out of memory", "When you press stop"},
                "When its condition becomes false", "A while loop checks its condition before each iteration and stops when the condition is false.");

        subChunk("C3", "C", "Loop Control: Break & Continue", 3, 50, "LoopControl.java",
                "<p>What if you want to exit a loop early, or skip just one iteration?</p>",
                "<p><code>break</code> exits the loop entirely. <code>continue</code> skips the rest of the current iteration and moves to the next one.</p><pre><code>for (int i = 0; i < 10; i++) {\n    if (i == 5) break;\n    System.out.println(i);\n}</code></pre>",
                story(n("Sometimes a spell must be interrupted — <code>break</code> shatters the loop, while <code>continue</code> merely skips a beat.")),
                "<p>Print only the even numbers from 1 to 10 using continue to skip odd numbers.</p>",
                "for (int i = 1; i <= 10; i++) {\n    if (i % 2 != 0) continue;\n    System.out.println(i);\n}",
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
                story(n("Methods are named spells — define them once, invoke them whenever you need their power.")),
                "<p>Create a method called <code>greet</code> that prints \"Welcome to the Academy!\" then call it from main.</p>",
                "import java.util.*;\n\npublic class Methods {\n    static void greet() {\n        System.out.println(\"Welcome to the Academy!\");\n    }\n    public static void main(String[] args) {\n        greet();\n    }\n}",
                tests(test("Greet", "null", "Welcome to the Academy!")),
                "Explain what a method is and why methods are useful. Use a real-world analogy.");

        mcQuestion("D1", QuestionTier.RECALL, "What keyword means a method returns nothing?",
                new String[]{"void", "null", "empty", "none"},
                "void", "The void keyword indicates a method does not return a value.");

        subChunk("D2", "D", "Parameters & Return Values", 2, 50, "MethodParams.java",
                "<p>What if your method needs different inputs each time it runs?</p>",
                "<p><strong>Parameters</strong> let you pass data into a method. <strong>Return values</strong> let a method send data back.</p><pre><code>static int add(int a, int b) {\n    return a + b;\n}\nint result = add(3, 4); // result is 7</code></pre>",
                story(n("A spell with parameters can be customised each time it is cast.")),
                "<p>Create a method <code>multiply</code> that takes two ints and returns their product. Print the result of multiplying 6 and 7.</p>",
                "import java.util.*;\n\npublic class MethodParams {\n    static int multiply(int a, int b) {\n        return a * b;\n    }\n    public static void main(String[] args) {\n        System.out.println(multiply(6, 7));\n    }\n}",
                tests(test("Multiply", "null", "42")),
                "Explain the difference between a parameter and an argument in your own words.");

        mcQuestion("D2", QuestionTier.APPLICATION, "What does <code>return</code> do in a method?",
                new String[]{"Sends a value back to the caller", "Prints output to the console", "Ends the program", "Declares a variable"},
                "Sends a value back to the caller", "return exits the method and provides the result to whoever called it.");

        subChunk("D3", "D", "Method Overloading", 3, 50, "Overloading.java",
                "<p>Can two methods have the same name?</p>",
                "<p>Yes! <strong>Method overloading</strong> means having multiple methods with the same name but different parameter lists.</p><pre><code>static int add(int a, int b) { return a + b; }\nstatic double add(double a, double b) { return a + b; }</code></pre>",
                story(n("A spell can have variations — same name, different ingredients.")),
                "<p>Create two <code>describe</code> methods: one that takes a String (name) and one that takes a String and an int (name and age). Call both.</p>",
                "import java.util.*;\n\npublic class Overloading {\n    static void describe(String name) {\n        System.out.println(\"Name: \" + name);\n    }\n    static void describe(String name, int age) {\n        System.out.println(\"Name: \" + name + \", Age: \" + age);\n    }\n    public static void main(String[] args) {\n        describe(\"Eldrin\");\n        describe(\"Eldrin\", 200);\n    }\n}",
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
                story(n("Arrays are like shelves in a library — each slot holds one item, and you find it by its position number.")),
                "<p>Create an array of 3 names and print each one on a new line using a for loop.</p>",
                "String[] names = {\"Eldrin\", \"Thara\", \"Korben\"};\nfor (int i = 0; i < names.length; i++) {\n    System.out.println(names[i]);\n}",
                tests(test("Print names", "null", "Eldrin\nThara\nKorben")),
                "Explain what an array is and why array indices start at 0 in Java.");

        mcQuestion("E1", QuestionTier.RECALL, "What is the index of the first element in a Java array?",
                new String[]{"0", "1", "-1", "It depends on the array"},
                "0", "Java arrays are zero-indexed. The first element is at index 0.");

        subChunk("E2", "E", "ArrayList", 2, 50, "ArrayLists.java",
                "<p>What if you don't know how many items you'll need to store?</p>",
                "<p>An <strong>ArrayList</strong> is a resizable list. Unlike arrays, you can add and remove elements dynamically.</p><pre><code>ArrayList&lt;String&gt; list = new ArrayList&lt;&gt;();\nlist.add(\"apple\");\nlist.add(\"banana\");\nSystem.out.println(list.size()); // 2</code></pre>",
                story(n("An ArrayList is a magical bag that grows as you put more items in.")),
                "<p>Create an ArrayList of integers, add the numbers 10, 20, 30, then print each using a for-each loop.</p>",
                "import java.util.ArrayList;\n\npublic class ArrayLists {\n    public static void main(String[] args) {\n        ArrayList<Integer> nums = new ArrayList<>();\n        nums.add(10);\n        nums.add(20);\n        nums.add(30);\n        for (int n : nums) {\n            System.out.println(n);\n        }\n    }\n}",
                tests(test("Print list", "null", "10\n20\n30")),
                "Explain the difference between an array and an ArrayList. When would you use each?");

        mcQuestion("E2", QuestionTier.RECALL, "How do you add an element to an ArrayList?",
                new String[]{".add()", ".push()", ".insert()", ".append()"},
                ".add()", "ArrayList uses the add() method to append elements to the end of the list.");

        subChunk("E3", "E", "Iterating Collections", 3, 50, "Iteration.java",
                "<p>You have a list of items — how do you process each one?</p>",
                "<p>The <strong>for-each loop</strong> (enhanced for) iterates over every element without needing an index.</p><pre><code>for (String name : names) {\n    System.out.println(name);\n}</code></pre>",
                story(n("The for-each loop walks through every element like a wizard reading every page of a tome.")),
                "<p>Given an array of integers, use a for-each loop to print only the values greater than 5.</p>",
                "int[] values = {3, 7, 1, 9, 4, 6};\nfor (int v : values) {\n    if (v > 5) {\n        System.out.println(v);\n    }\n}",
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
                story(n("A class is like a spell template — it defines what an object <em>can be</em>. An object is the spell made real.")),
                "<p>Create a <code>Student</code> class with fields <code>name</code> and <code>grade</code>. In main, create a Student, set the fields, and print them.</p>",
                "import java.util.*;\n\npublic class ClassesIntro {\n    static String name;\n    static int grade;\n    public static void main(String[] args) {\n        name = \"Alice\";\n        grade = 95;\n        System.out.println(name + \": \" + grade);\n    }\n}",
                tests(test("Student", "null", "Alice: 95")),
                "Explain the difference between a class and an object. Use an analogy.");

        mcQuestion("F1", QuestionTier.RECALL, "What keyword creates a new object from a class?",
                new String[]{"new", "create", "make", "build"},
                "new", "The new keyword creates a new instance (object) of a class.");

        subChunk("F2", "F", "Constructors", 2, 60, "Constructors.java",
                "<p>You create a BankAccount but forget to set the balance. How do you guarantee every account starts valid?</p>",
                "<p>A <strong>constructor</strong> is a special method that runs when you create an object. It initialises the object's fields.</p><pre><code>class Dog {\n    String name;\n    Dog(String name) {\n        this.name = name;\n    }\n}\nDog d = new Dog(\"Rex\");</code></pre>",
                story(n("Constructors are the birth incantation of an object — they define how it comes into existence.")),
                "<p>Create a <code>Book</code> class with a constructor that takes title and author. Print both fields.</p>",
                "import java.util.*;\n\npublic class Constructors {\n    String title;\n    String author;\n    Constructors(String title, String author) {\n        this.title = title;\n        this.author = author;\n    }\n    public static void main(String[] args) {\n        Constructors b = new Constructors(\"The Arcane Arts\", \"Eldrin\");\n        System.out.println(b.title + \" by \" + b.author);\n    }\n}",
                tests(test("Book", "null", "The Arcane Arts by Eldrin")),
                "Explain what a constructor does and why it's useful. What happens if you don't write one?");

        mcQuestion("F2", QuestionTier.RECALL, "What is the <code>this</code> keyword in Java?",
                new String[]{"A reference to the current object instance", "A reference to the current class", "A keyword that creates a new object", "A reference to the parent class"},
                "A reference to the current object instance", "this refers to the current object instance. It's used to distinguish fields from parameters with the same name.");

        subChunk("F3", "F", "Methods Inside Classes", 3, 60, "ClassMethods.java",
                "<p>Data without behaviour is just dead storage. How do objects <em>do</em> things?</p>",
                "<p>Objects have <strong>methods</strong> — functions that belong to the class and can access its fields.</p><pre><code>class Circle {\n    double radius;\n    double area() {\n        return Math.PI * radius * radius;\n    }\n}</code></pre>",
                story(n("A class defines both what an object <em>knows</em> (fields) and what it can <em>do</em> (methods).")),
                "<p>Create a <code>Rectangle</code> class with width and height fields, and an <code>area()</code> method. Print the area of a 5x3 rectangle.</p>",
                "import java.util.*;\n\npublic class ClassMethods {\n    int width;\n    int height;\n    ClassMethods(int w, int h) { this.width = w; this.height = h; }\n    int area() { return width * height; }\n    public static void main(String[] args) {\n        ClassMethods r = new ClassMethods(5, 3);\n        System.out.println(r.area());\n    }\n}",
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
                story(n("Encapsulation is the art of protecting an object's inner secrets — letting others use it without seeing how it works inside.")),
                "<p>Create a class with a private field and public getter/setter methods.</p>",
                "import java.util.*;\n\npublic class AccessModifiers {\n    private String secret = \"hidden\";\n    public String getSecret() { return secret; }\n    public void setSecret(String s) { this.secret = s; }\n    public static void main(String[] args) {\n        AccessModifiers a = new AccessModifiers();\n        a.setSecret(\"revealed\");\n        System.out.println(a.getSecret());\n    }\n}",
                tests(test("Getter/Setter", "null", "revealed")),
                "Explain encapsulation and why fields should be private. Give an example of what could go wrong without it.");

        mcQuestion("G1", QuestionTier.RECALL, "What access modifier hides a field from other classes?",
                new String[]{"private", "public", "protected", "hidden"},
                "private", "The private modifier restricts access to within the same class only.");

        subChunk("G2", "G", "Getters, Setters & Validation", 2, 60, "Validation.java",
                "<p>A setter doesn't just set — it can <em>validate</em>.</p>",
                "<p>Setters can include logic to enforce rules on data.</p><pre><code>public void setAge(int age) {\n    if (age < 0) throw new IllegalArgumentException();\n    this.age = age;\n}</code></pre>",
                story(n("A good setter is a gatekeeper — it checks credentials before letting data through.")),
                "<p>Create a <code>Temperature</code> class where the setter rejects values below absolute zero (-273).</p>",
                "import java.util.*;\n\npublic class Validation {\n    private int temp;\n    public void setTemp(int t) {\n        if (t < -273) { System.out.println(\"Invalid\"); return; }\n        this.temp = t;\n    }\n    public int getTemp() { return temp; }\n    public static void main(String[] args) {\n        Validation v = new Validation();\n        v.setTemp(25);\n        System.out.println(v.getTemp());\n        v.setTemp(-300);\n    }\n}",
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
                story(n("Inheritance is the passing of power from master to apprentice — the apprentice gains everything the master knew, and adds their own abilities.")),
                "<p>Create an <code>Animal</code> class with a <code>speak()</code> method, and a <code>Cat</code> that overrides it to print \"Meow\". Call it.</p>",
                "import java.util.*;\n\npublic class Inheritance {\n    public static void main(String[] args) {\n        Cat c = new Cat();\n        c.speak();\n    }\n}\nclass Animal {\n    void speak() { System.out.println(\"...\"); }\n}\nclass Cat extends Animal {\n    void speak() { System.out.println(\"Meow\"); }\n}",
                tests(test("Cat speaks", "null", "Meow")),
                "Explain inheritance in your own words. Why is it useful?");

        mcQuestion("H1", QuestionTier.RECALL, "What keyword is used to inherit from another class?",
                new String[]{"extends", "inherits", "implements", "derives"},
                "extends", "In Java, a class uses the extends keyword to inherit from another class.");

        subChunk("H2", "H", "Super Keyword & Constructor Chaining", 2, 60, "SuperKeyword.java",
                "<p>When a child class creates an object, does the parent's constructor run too?</p>",
                "<p>The <code>super</code> keyword calls the parent's constructor or methods.</p><pre><code>class Animal {\n    String name;\n    Animal(String name) { this.name = name; }\n}\nclass Dog extends Animal {\n    Dog(String name) { super(name); }\n}</code></pre>",
                story(n("super calls upon the power of the master class — ensuring the foundation is laid before the apprentice builds upon it.")),
                "<p>Create a <code>Vehicle</code> with a constructor and a <code>Car</code> that calls super. Print the vehicle's type.</p>",
                "import java.util.*;\n\npublic class SuperKeyword {\n    public static void main(String[] args) {\n        Car c = new Car(\"Sedan\");\n        System.out.println(c.type);\n    }\n}\nclass Vehicle {\n    String type;\n    Vehicle(String type) { this.type = type; }\n}\nclass Car extends Vehicle {\n    Car(String type) { super(type); }\n}",
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
                story(n("Polymorphism is the shape-shifting art — the same invocation produces different results depending on the true nature of the object.")),
                "<p>Create an Animal with speak(), Dog and Cat that override it. Store both in Animal variables and call speak().</p>",
                "import java.util.*;\n\npublic class Polymorphism {\n    public static void main(String[] args) {\n        Animal dog = new Dog();\n        Animal cat = new Cat();\n        dog.speak();\n        cat.speak();\n    }\n}\nclass Animal { void speak() { System.out.println(\"...\"); } }\nclass Dog extends Animal { void speak() { System.out.println(\"Woof\"); } }\nclass Cat extends Animal { void speak() { System.out.println(\"Meow\"); } }",
                tests(test("Poly", "null", "Woof\nMeow")),
                "Explain polymorphism to a beginner. Why is it powerful?");

        mcQuestion("I1", QuestionTier.APPLICATION, "What prints when you call speak() on an Animal variable holding a Dog?",
                new String[]{"Dog's version of speak()", "Animal's version of speak()", "A compile error", "Nothing"},
                "Dog's version of speak()", "At runtime, Java calls the actual object's method (Dog), not the reference type's (Animal). This is dynamic dispatch.");

        subChunk("I2", "I", "Abstract Classes & Interfaces", 2, 70, "AbstractClasses.java",
                "<p>What if a class should never be instantiated directly — only extended?</p>",
                "<p>An <strong>abstract class</strong> can't be instantiated. It can have abstract methods (no body) that subclasses must implement. An <strong>interface</strong> defines a contract of methods a class must provide.</p><pre><code>abstract class Shape {\n    abstract double area();\n}\nclass Circle extends Shape {\n    double radius;\n    double area() { return Math.PI * radius * radius; }\n}</code></pre>",
                story(n("Abstract classes are incomplete spell formulas — they outline the structure but leave key incantations for the apprentice to complete.")),
                "<p>Create an abstract <code>Shape</code> with an abstract <code>area()</code> method. Implement it in <code>Square</code>. Print the area of a 4x4 square.</p>",
                "import java.util.*;\n\npublic class AbstractClasses {\n    public static void main(String[] args) {\n        Square s = new Square(4);\n        System.out.println(s.area());\n    }\n}\nabstract class Shape {\n    abstract int area();\n}\nclass Square extends Shape {\n    int side;\n    Square(int side) { this.side = side; }\n    int area() { return side * side; }\n}",
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
                story(n("Exceptions are unexpected failures in spells. A try/catch is your magical safety net.")),
                "<p>Write code that tries to divide 10 by 0 and catches the exception, printing an error message.</p>",
                "try {\n    int result = 10 / 0;\n    System.out.println(result);\n} catch (ArithmeticException e) {\n    System.out.println(\"Error: Division by zero\");\n}",
                tests(test("Catch division", "null", "Error: Division by zero")),
                "Explain try/catch to a beginner. How is it different from using if/else to check for errors?");

        mcQuestion("J1", QuestionTier.RECALL, "What block of code executes when an exception occurs?",
                new String[]{"catch", "try", "finally", "throw"},
                "catch", "The catch block runs when the specified exception type is thrown in the try block.");

        subChunk("J2", "J", "Throwing Exceptions", 2, 50, "ThrowExceptions.java",
                "<p>Sometimes your code detects an invalid state. How do you signal the problem?</p>",
                "<p>Use <code>throw</code> to create an exception and <code>throws</code> to declare that a method might throw one.</p><pre><code>static void checkAge(int age) {\n    if (age < 0) throw new IllegalArgumentException(\"Negative age!\");\n}</code></pre>",
                story(n("Throwing an exception is like raising an alarm — it signals that something has gone wrong and must be dealt with.")),
                "<p>Write a method that throws an exception if the input is negative. Catch it in main.</p>",
                "import java.util.*;\n\npublic class ThrowExceptions {\n    static void validate(int n) {\n        if (n < 0) throw new IllegalArgumentException(\"Negative!\");\n        System.out.println(\"Valid: \" + n);\n    }\n    public static void main(String[] args) {\n        try {\n            validate(-5);\n        } catch (IllegalArgumentException e) {\n            System.out.println(e.getMessage());\n        }\n    }\n}",
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
                story(n("Strings are among the most common data types. Mastering their built-in methods will save you countless lines of code.")),
                "<p>Given a string, print its length, uppercase version, and whether it contains \"Java\".</p>",
                "String text = \"I love Java programming\";\nSystem.out.println(text.length());\nSystem.out.println(text.toUpperCase());\nSystem.out.println(text.contains(\"Java\"));",
                tests(test("Length", "null", "23"), test("Upper", "null", "I LOVE JAVA PROGRAMMING"), test("Contains", "null", "true")),
                "Explain 3 useful String methods and when you would use each one.");

        mcQuestion("K1", QuestionTier.RECALL, "What does <code>\"hello\".length()</code> return?",
                new String[]{"5", "4", "6", "\"hello\""},
                "5", "length() returns the number of characters in the string. \"hello\" has 5 characters.");

        subChunk("K2", "K", "Math Class & Utility Methods", 2, 50, "MathUtils.java",
                "<p>How do you calculate a square root or generate a random number in Java?</p>",
                "<p>The <code>Math</code> class provides static methods for common operations: <code>Math.max()</code>, <code>Math.min()</code>, <code>Math.sqrt()</code>, <code>Math.random()</code>.</p><pre><code>System.out.println(Math.max(10, 20)); // 20\nSystem.out.println(Math.sqrt(16)); // 4.0</code></pre>",
                story(n("The Math class is your built-in calculator — it handles the mathematical heavy lifting so you don't have to.")),
                "<p>Print the max of 15 and 23, the square root of 49, and Math.PI.</p>",
                "System.out.println(Math.max(15, 23));\nSystem.out.println(Math.sqrt(49));\nSystem.out.println(Math.PI);",
                tests(test("Max", "null", "23"), test("Sqrt", "null", "7.0"), test("PI", "null", "3.14")),
                "Explain 3 methods from the Math class and give a practical example for each.");

        mcQuestion("K2", QuestionTier.RECALL, "What does <code>Math.sqrt(25)</code> return?",
                new String[]{"5.0", "25", "12.5", "625"},
                "5.0", "Math.sqrt() returns the square root as a double. The square root of 25 is 5.0.");
    }
}
