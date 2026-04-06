package com.arcane.academy.config;

import com.arcane.academy.repository.QuestRepository;
import org.springframework.stereotype.Component;

// ══════════════════════════════════════════════════════════════════════════════
// CHAPTER VIII — THE TESTING SANCTUM (JUnit & Test-Driven Development)
// ══════════════════════════════════════════════════════════════════════════════
@Component
public class Ch8Seeder extends AbstractChapterSeeder {

    public Ch8Seeder(QuestRepository questRepository) {
        super(questRepository);
    }

    @Override
    public void seed() {

        q("ch8-q1","The First Assertion","Chapter VIII · Quest 1","Unit Testing",8,1,220,"FirstAssertion.java",
          story(
            n("The Testing Sanctum. A new wing of the Academy, added after the Great Bug of Year Three left the entire enchantment registry corrupted for a week. The first lesson is simple: always verify your code does what you think it does."),
            d("🧙","mentor","Master Velan","s-mentor","A <em>unit test</em> is a small piece of code that checks one specific behaviour of your production code. In professional Java, tests are written with <em>JUnit</em> — a framework where you annotate methods with <em>@Test</em> and use <em>assertEquals(expected, actual)</em> to verify results."),
            e("JUnit test structure",
              "<span class='cm'>// In a real JUnit test class:</span>\n<span class='cm'>// @Test</span>\n<span class='cm'>// void addsTwoNumbers() {</span>\n<span class='cm'>//     assertEquals(5, Calculator.add(2, 3));</span>\n<span class='cm'>// }</span>\n\n<span class='cm'>// The pattern: assertEquals(EXPECTED, ACTUAL)</span>\n<span class='cm'>// If they don't match, the test fails with a clear message.</span>"),
            d("🔬","npc","Tester Nara","s-npc","In this Sanctum, you'll build a simple test harness by hand — the same logic that JUnit uses internally. You'll write a check() helper that compares expected and actual, and then write test methods that use it. The concepts are identical to JUnit."),
            e("Worked Example — Manual test harness",
              "<span class='kw'>static void</span> check(<span class='type'>String</span> label, <span class='type'>Object</span> expected, <span class='type'>Object</span> actual) {\n    <span class='kw'>if</span> (expected.equals(actual)) {\n        System.out.println(<span class='str'>\"PASS: \"</span> + label);\n    } <span class='kw'>else</span> {\n        System.out.println(<span class='str'>\"FAIL: \"</span> + label +\n            <span class='str'>\" | expected: \"</span> + expected +\n            <span class='str'>\" | got: \"</span> + actual);\n    }\n}")
          ),
          "A <code>Calculator</code> class with <code>add(int a, int b)</code> and <code>multiply(int a, int b)</code> is provided.<br><br>Complete the <code>check()</code> method and write test calls that print:<br>• <strong>PASS: add 2+3</strong><br>• <strong>PASS: add negative</strong><br>• <strong>PASS: multiply 4x5</strong>",
          "check(label, expected, actual) — if expected.equals(actual) print PASS, else FAIL. Call: check(\"add 2+3\", 5, Calculator.add(2, 3));",
          "class Calculator {\n    static int add(int a, int b)      { return a + b; }\n    static int multiply(int a, int b)  { return a * b; }\n}\n\npublic class FirstAssertion {\n\n    static void check(String label, Object expected, Object actual) {\n        // Print \"PASS: label\" if equal, \"FAIL: ...\" if not\n\n    }\n\n    public static void main(String[] args) {\n        // Test add(2, 3) == 5\n        // Test add(-1, 1) == 0\n        // Test multiply(4, 5) == 20\n\n    }\n}\n",
          "Three green ticks appear. \"PASS. PASS. PASS. This is what confidence looks like,\" Nara says.",
          tests(test("add 2+3","null","PASS: add 2+3"),test("add negative","null","PASS: add negative"),test("multiply 4x5","null","PASS: multiply 4x5")));

        q("ch8-q2","The Edge Case Ward","Chapter VIII · Quest 2","Edge Case Testing",8,2,230,"EdgeCaseWard.java",
          story(
            n("The Edge Case Ward. Most bugs don't live in the happy path — they lurk at the edges. Empty input. Zero. Null. The maximum value. The cases users aren't supposed to hit, but always do."),
            d("🔬","npc","Tester Nara","s-npc","After testing the normal case, always ask: what happens with empty input? What happens with zero? With negative numbers? With null? A test suite that only covers the happy path is not a test suite — it's wishful thinking."),
            e("Edge cases to always consider",
              "<span class='cm'>// For a String method:</span>\n<span class='cm'>// - empty string \"\"</span>\n<span class='cm'>// - single character</span>\n<span class='cm'>// - already in desired form</span>\n\n<span class='cm'>// For a numeric method:</span>\n<span class='cm'>// - zero</span>\n<span class='cm'>// - negative numbers</span>\n<span class='cm'>// - very large numbers</span>\n\n<span class='cm'>// For a collection method:</span>\n<span class='cm'>// - empty collection</span>\n<span class='cm'>// - single element</span>\n<span class='cm'>// - duplicate elements</span>"),
            d("🧙","mentor","Master Velan","s-mentor","The isPalindrome method you wrote in Chapter VII — did you test the empty string? A single character? An even-length palindrome like 'abba'? These are the cases that reveal whether your algorithm is truly correct.")
          ),
          "A <code>StringUtils</code> class with <code>isPalindrome(String s)</code> and <code>countVowels(String s)</code> is provided.<br><br>Write tests covering edge cases. Print these exact lines:<br>• <strong>PASS: empty is palindrome</strong><br>• <strong>PASS: single char</strong><br>• <strong>PASS: even palindrome</strong><br>• <strong>PASS: zero vowels</strong>",
          "Empty string \"\" is a palindrome (no characters to mismatch). Single char is always a palindrome. \"abba\" is an even-length palindrome. \"rhythm\" has zero vowels.",
          "class StringUtils {\n    static boolean isPalindrome(String s) {\n        int left = 0, right = s.length() - 1;\n        while (left < right) {\n            if (s.charAt(left) != s.charAt(right)) return false;\n            left++; right--;\n        }\n        return true;\n    }\n    static int countVowels(String s) {\n        int count = 0;\n        for (char c : s.toCharArray())\n            if (\"aeiouAEIOU\".indexOf(c) >= 0) count++;\n        return count;\n    }\n}\n\npublic class EdgeCaseWard {\n    static void check(String label, Object expected, Object actual) {\n        System.out.println(expected.equals(actual) ? \"PASS: \" + label : \"FAIL: \" + label);\n    }\n    public static void main(String[] args) {\n        // Test isPalindrome(\"\")     == true\n        // Test isPalindrome(\"a\")    == true\n        // Test isPalindrome(\"abba\") == true\n        // Test countVowels(\"rhythm\") == 0\n    }\n}\n",
          "\"Four edge cases. Four passes,\" Nara marks the board. \"You have learned to distrust the happy path.\"",
          tests(test("empty palindrome","null","PASS: empty is palindrome"),test("single char","null","PASS: single char"),test("even palindrome","null","PASS: even palindrome"),test("zero vowels","null","PASS: zero vowels")));

        q("ch8-q3","The Before Ritual","Chapter VIII · Quest 3","Test Setup",8,3,240,"BeforeRitual.java",
          story(
            n("The Before Ritual. In the Sanctum, before each test begins, the room resets. Every artefact returns to its starting position. This is what @BeforeEach does in JUnit — it runs a setup method before every test to ensure a clean slate."),
            d("🧙","mentor","Master Velan","s-mentor","Tests must be <em>independent</em>. If test 2 relies on state left by test 1, you don't have two tests — you have one fragile sequence. Each test should set up its own fresh state. In JUnit, methods annotated with <em>@BeforeEach</em> reset shared state before every single test runs."),
            e("JUnit @BeforeEach",
              "<span class='cm'>// In a real JUnit class:</span>\n<span class='cm'>// private Calculator calc;</span>\n<span class='cm'>//</span>\n<span class='cm'>// @BeforeEach</span>\n<span class='cm'>// void setUp() {</span>\n<span class='cm'>//     calc = new Calculator(); // fresh object before each test</span>\n<span class='cm'>// }</span>\n<span class='cm'>//</span>\n<span class='cm'>// @Test</span>\n<span class='cm'>// void testAdd() {</span>\n<span class='cm'>//     assertEquals(5, calc.add(2, 3));</span>\n<span class='cm'>// }</span>"),
            d("🔬","npc","Tester Nara","s-npc","We'll simulate setup by creating a fresh Counter object at the start of each test method. The key lesson: each test method starts from zero. Never let one test's side effects contaminate another.")
          ),
          "A <code>Counter</code> class with <code>increment()</code> and <code>getCount()</code> is provided.<br><br>Write three independent tests, each creating a fresh Counter. Print:<br>• <strong>PASS: fresh counter starts at 0</strong><br>• <strong>PASS: one increment</strong><br>• <strong>PASS: three increments</strong>",
          "Each test method creates its own new Counter(). Do not share state between tests. fresh.getCount() should be 0 before any increments.",
          "class Counter {\n    private int count = 0;\n    void increment() { count++; }\n    int getCount()   { return count; }\n}\n\npublic class BeforeRitual {\n    static void check(String label, Object expected, Object actual) {\n        System.out.println(expected.equals(actual) ? \"PASS: \" + label : \"FAIL: \" + label);\n    }\n    static void testFreshCounter() {\n        // Create new Counter, check count == 0\n\n    }\n    static void testOneIncrement() {\n        // Create new Counter, increment once, check count == 1\n\n    }\n    static void testThreeIncrements() {\n        // Create new Counter, increment 3 times, check count == 3\n\n    }\n    public static void main(String[] args) {\n        testFreshCounter();\n        testOneIncrement();\n        testThreeIncrements();\n    }\n}\n",
          "\"Three independent tests. Three passes. No shared state,\" Nara confirms. \"This is a proper test suite.\"",
          tests(test("fresh at 0","null","PASS: fresh counter starts at 0"),test("one increment","null","PASS: one increment"),test("three increments","null","PASS: three increments")));

        q("ch8-q4","The Exception Trap","Chapter VIII · Quest 4","Testing Exceptions",8,4,250,"ExceptionTrap.java",
          story(
            n("The Exception Trap. Some behaviours should throw exceptions — invalid input, illegal state. Testing that exceptions are thrown is just as important as testing that methods return the right value."),
            d("🧙","mentor","Master Velan","s-mentor","In JUnit you use <em>assertThrows(ExceptionClass.class, () -&gt; codeToTest())</em>. If the exception is thrown, the test passes. If not, it fails. Manually, you wrap the call in try/catch — if the catch block runs, the test passes; if execution reaches the end without throwing, the test fails."),
            e("Testing exceptions manually",
              "<span class='kw'>static void</span> testThrows(<span class='type'>String</span> label, <span class='type'>Runnable</span> action) {\n    <span class='kw'>try</span> {\n        action.run();\n        System.out.println(<span class='str'>\"FAIL: \"</span> + label + <span class='str'>\" (no exception thrown)\"</span>);\n    } <span class='kw'>catch</span> (<span class='type'>Exception</span> e) {\n        System.out.println(<span class='str'>\"PASS: \"</span> + label);\n    }\n}\n\n<span class='cm'>// Usage:</span>\ntestThrows(<span class='str'>\"divide by zero\"</span>, () -> { <span class='type'>int</span> x = <span class='num'>1</span> / <span class='num'>0</span>; });"),
            d("🔬","npc","Tester Nara","s-npc","A BankAccount class is provided. Withdrawing more than the balance should throw an IllegalArgumentException. Depositing a negative amount should also throw. Write tests that verify both.")
          ),
          "A <code>BankAccount</code> class is provided. Write tests using the <code>testThrows()</code> helper to verify exceptions. Print:<br>• <strong>PASS: overdraft throws</strong><br>• <strong>PASS: negative deposit throws</strong><br>• <strong>PASS: valid deposit works</strong> (using check())",
          "testThrows(\"overdraft throws\", () -> account.withdraw(9999)); — passes if IllegalArgumentException is thrown. For the valid deposit, use check() not testThrows().",
          "class BankAccount {\n    private double balance;\n    BankAccount(double initial) { this.balance = initial; }\n    void deposit(double amount) {\n        if (amount <= 0) throw new IllegalArgumentException(\"Deposit must be positive\");\n        balance += amount;\n    }\n    void withdraw(double amount) {\n        if (amount > balance) throw new IllegalArgumentException(\"Insufficient funds\");\n        balance -= amount;\n    }\n    double getBalance() { return balance; }\n}\n\npublic class ExceptionTrap {\n    static void check(String label, Object expected, Object actual) {\n        System.out.println(expected.equals(actual) ? \"PASS: \" + label : \"FAIL: \" + label);\n    }\n    static void testThrows(String label, Runnable action) {\n        try {\n            action.run();\n            System.out.println(\"FAIL: \" + label + \" (no exception thrown)\");\n        } catch (Exception e) {\n            System.out.println(\"PASS: \" + label);\n        }\n    }\n    public static void main(String[] args) {\n        BankAccount account = new BankAccount(100.0);\n        // Test: withdraw(9999) throws\n        // Test: deposit(-50) throws\n        // Test: deposit(50) succeeds — check balance is 150.0\n    }\n}\n",
          "\"Two exceptions caught. One valid deposit confirmed,\" Nara reads. \"You test for failure as rigorously as success.\"",
          tests(test("overdraft","null","PASS: overdraft throws"),test("negative deposit","null","PASS: negative deposit throws"),test("valid deposit","null","PASS: valid deposit works")));

        q("ch8-q5","The Grand Verification","Chapter VIII · Quest 5","Full Test Suite",8,5,280,"GrandVerification.java",
          story(
            n("The Grand Verification. The final trial of the Testing Sanctum. Write a complete, professional test suite for a class from scratch. Every edge case covered. Every method tested. No excuses."),
            d("🧙","mentor","Master Velan","s-mentor","A good test suite follows three rules. First: one assertion per test method, with a descriptive name that explains what is being verified. Second: tests cover the normal case, edge cases, and error cases. Third: tests are independent — running them in any order gives the same result."),
            d("🔬","npc","Tester Nara","s-npc","A Stack class is provided — a last-in, first-out structure. push() adds an element. pop() removes and returns the top. isEmpty() checks if it has elements. peek() reads the top without removing it. Test every method, including error cases."),
            e("What a complete test suite covers",
              "<span class='cm'>// For Stack:</span>\n<span class='cm'>// - push then peek → correct value on top</span>\n<span class='cm'>// - push then pop → removes and returns top</span>\n<span class='cm'>// - isEmpty on new stack → true</span>\n<span class='cm'>// - isEmpty after push → false</span>\n<span class='cm'>// - pop on empty stack → throws exception</span>")
          ),
          "A <code>Stack&lt;T&gt;</code> class is provided. Write a complete test suite. Print these five lines in order:<br>• <strong>PASS: new stack is empty</strong><br>• <strong>PASS: push then peek</strong><br>• <strong>PASS: push then pop</strong><br>• <strong>PASS: not empty after push</strong><br>• <strong>PASS: pop empty throws</strong>",
          "Each test creates a fresh Stack. peek() and pop() return the last-pushed item. pop() on empty stack throws IllegalStateException. Use testThrows() for the last one.",
          "import java.util.ArrayList;\n\nclass Stack<T> {\n    private ArrayList<T> items = new ArrayList<>();\n    void push(T item) { items.add(item); }\n    T pop() {\n        if (items.isEmpty()) throw new IllegalStateException(\"Stack is empty\");\n        return items.remove(items.size() - 1);\n    }\n    T peek() {\n        if (items.isEmpty()) throw new IllegalStateException(\"Stack is empty\");\n        return items.get(items.size() - 1);\n    }\n    boolean isEmpty() { return items.isEmpty(); }\n}\n\npublic class GrandVerification {\n    static void check(String label, Object expected, Object actual) {\n        System.out.println(expected.equals(actual) ? \"PASS: \" + label : \"FAIL: \" + label);\n    }\n    static void testThrows(String label, Runnable action) {\n        try { action.run(); System.out.println(\"FAIL: \" + label); }\n        catch (Exception e) { System.out.println(\"PASS: \" + label); }\n    }\n    public static void main(String[] args) {\n        // Test: new stack is empty\n        // Test: push \"A\" then peek == \"A\"\n        // Test: push \"B\" then pop == \"B\"\n        // Test: after push, not empty\n        // Test: pop on empty stack throws\n    }\n}\n",
          "Five green lines. Nara closes the book. \"A complete test suite. Clean. Independent. Thorough. The Testing Sanctum accepts you.\" The door to the next chapter opens.",
          tests(test("is empty","null","PASS: new stack is empty"),test("peek","null","PASS: push then peek"),test("pop","null","PASS: push then pop"),test("not empty","null","PASS: not empty after push"),test("pop throws","null","PASS: pop empty throws")));

        // ── Side quests ───────────────────────────────────────────────────────

        sq("ch8-sq1","The TDD Ritual","Chapter VIII · Side Quest","Test-Driven Development",8,90,90,"TddRitual.java",
          story(
            n("The Testing Sanctum's inner chamber. On the altar: a blank scroll and a quill. Nara speaks quietly: \"Most developers write code, then tests. We do the opposite.\""),
            d("📋","npc","Nara the Tester","s-npc","<em>Test-Driven Development (TDD)</em> is a discipline: you write the test <em>before</em> the implementation. The test fails first (Red). Then you write just enough code to make it pass (Green). Then you clean up without breaking tests (Refactor)."),
            e("The Red-Green-Refactor Cycle","// RED: write a failing test\ncheck(\"add(2,3) == 5\", 5, add(2,3)); // fails: add doesn't exist yet\n\n// GREEN: implement the minimum to pass\nstatic int add(int a, int b) { return a + b; }\n\n// REFACTOR: improve without breaking\n// (nothing to refactor here — it's already clean)"),
            d("🧙","mentor","Master Velan","s-mentor","TDD's real value isn't the tests themselves — it's that writing tests first forces you to think about the <em>interface</em> and <em>expected behaviour</em> before getting lost in implementation details. You end up with cleaner APIs and better-thought-out logic."),
            d("📋","npc","Nara the Tester","s-npc","TDD also prevents over-engineering. If you only write code to make the tests pass, you don't add features nobody asked for. And when your tests are green, you have immediate confidence that the feature works.")
          ),
          "Follow the TDD ritual: the <code>isPalindrome</code> method signature is given — implement it so all tests pass.<br><br>A palindrome reads the same forwards and backwards: <em>racecar</em> → true, <em>hello</em> → false, <em>level</em> → true.<br><br>Hint: <code>new StringBuilder(s).reverse().toString()</code> gives you the reversed string.",
          "Reverse the string and compare with the original: return s.equals(new StringBuilder(s).reverse().toString());",
          "public class TddRitual {\n    // The tests were written first — now implement the method:\n    static boolean isPalindrome(String s) {\n        // Your implementation here\n        \n    }\n    public static void main(String[] args) {\n        // These \"tests\" defined the behaviour before the code existed\n        System.out.println(\"racecar: \" + isPalindrome(\"racecar\"));\n        System.out.println(\"hello: \" + isPalindrome(\"hello\"));\n        System.out.println(\"level: \" + isPalindrome(\"level\"));\n        System.out.println(\"java: \" + isPalindrome(\"java\"));\n    }\n}\n",
          "Four results. All correct. Nara nods. \"Tests defined the contract. Implementation fulfilled it. That is TDD.\"",
          tests(test("racecar=true","null","racecar: true"),test("hello=false","null","hello: false"),test("level=true","null","level: true"),test("java=false","null","java: false")));
    }
}
