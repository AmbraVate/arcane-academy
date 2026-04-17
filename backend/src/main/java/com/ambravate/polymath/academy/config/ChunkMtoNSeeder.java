package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import org.springframework.stereotype.Component;

@Component
public class ChunkMtoNSeeder extends AbstractChunkSeeder {

    public ChunkMtoNSeeder(ChunkRepository chunkRepository, SubChunkRepository subChunkRepository,
                           QuestionRepository questionRepository, RabbitHoleModuleRepository rabbitHoleRepository) {
        super(chunkRepository, subChunkRepository, questionRepository, rabbitHoleRepository);
    }

    @Override
    public void seed() {
        seedChunkM();
        seedChunkN();
    }

    // =========================================================================
    // CHUNK M — Exception Handling
    // =========================================================================
    private void seedChunkM() {
        chunk("M", "Exception Handling", "\u26A1", 13, LearnerPath.FOUNDATION, "D");

        seedM1();
        seedM2();
        seedM3();
        seedM4();
    }

    // -------------------------------------------------------------------------
    // M1 — What are Exceptions?
    // -------------------------------------------------------------------------
    private void seedM1() {
        subChunk("M1", "M", "What are Exceptions?", 1, 50, "ExceptionBasics.java",

                // hook
                "<p>What happens when your program tries to divide a number by zero? "
                + "Or access the 100th element of an array that only has 5 elements? "
                + "Java doesn't silently soldier on — it <strong>throws an exception</strong>, "
                + "bringing your program to a screeching halt. Understanding what that means is the first step "
                + "to writing code that doesn't explode.</p>",

                // explanation
                "<h3>Exceptions: When Spells Go Wrong</h3>"
                + "<p>An <strong>exception</strong> is an event that disrupts the normal flow of a program at runtime. "
                + "When Java encounters a problem it can't handle, it creates an exception object describing what went wrong "
                + "and <em>throws</em> it up the call stack.</p>"
                + "<p>If nothing catches it, the program prints a <strong>stack trace</strong> and terminates:</p>"
                + "<pre><code>Exception in thread \"main\" java.lang.ArithmeticException: / by zero\n"
                + "    at Main.calculate(Main.java:8)\n"
                + "    at Main.main(Main.java:3)</code></pre>"
                + "<p>The stack trace tells you the <em>type</em> of exception, the <em>message</em>, "
                + "and the <em>exact line</em> where it happened. It's an invaluable debugging tool.</p>"
                + "<h4>Common Exceptions</h4>"
                + "<ul>"
                + "<li><code>NullPointerException</code> — you used a variable that is <code>null</code></li>"
                + "<li><code>ArrayIndexOutOfBoundsException</code> — you accessed an array at an invalid index</li>"
                + "<li><code>NumberFormatException</code> — you tried to parse a non-numeric String as a number</li>"
                + "<li><code>ClassCastException</code> — you tried to cast an object to a type it isn't</li>"
                + "<li><code>ArithmeticException</code> — an illegal arithmetic operation, like dividing by zero</li>"
                + "</ul>"
                + "<h4>Checked vs Unchecked (Preview)</h4>"
                + "<p>Java exceptions fall into two categories. <strong>Unchecked</strong> exceptions "
                + "(subclasses of <code>RuntimeException</code>) don't require you to handle them in advance — "
                + "they represent bugs in your code. <strong>Checked</strong> exceptions must be either caught "
                + "or declared — they represent expected error conditions like a missing file. "
                + "We'll explore this fully in M3.</p>"
                + "<h4>Catching Exceptions with try/catch</h4>"
                + "<p>You wrap risky code in a <code>try</code> block. If an exception is thrown, "
                + "the matching <code>catch</code> block handles it:</p>"
                + "<pre><code>try {\n"
                + "    int result = Integer.parseInt(\"abc\");\n"
                + "} catch (NumberFormatException e) {\n"
                + "    System.out.println(\"Invalid number\");\n"
                + "}</code></pre>",

                // story
                story(
                    n("The Grand Casting Hall fills with the smell of singed parchment. "
                      + "Apprentice Lira attempts a summoning spell — but she forgot to check "
                      + "whether the summoning ingredients were on the shelf."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Ah — a <strong>SpellBackfireException</strong>. She tried to use an ingredient "
                      + "that was not there. In Java terms, she accessed a null reference. "
                      + "The spell did not just fail quietly — it threw an exception, "
                      + "and the entire ritual collapsed."),
                    n("Smoke curls from the ruined summoning circle. Eldrin steps forward, unruffled."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "This is why we use <strong>protective wards</strong> — <code>try</code> and <code>catch</code>. "
                      + "Wrap any risky incantation in a <code>try</code> block. "
                      + "If something goes wrong, the <code>catch</code> block intercepts the backfire "
                      + "and handles it gracefully, leaving the rest of the academy intact."),
                    e("A basic protective ward",
                        "try {\n"
                        + "    int result = Integer.parseInt(\"abc\"); // this throws!\n"
                        + "} catch (NumberFormatException e) {\n"
                        + "    System.out.println(\"Invalid number\");\n"
                        + "}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Notice: without the ward, the program would crash. "
                      + "With the ward, we print a friendly message and carry on. "
                      + "Now — your turn. Set up a ward around a risky parse.")
                ),

                // guided practice
                "<p>Use <code>try/catch</code> to safely handle a <code>NumberFormatException</code>. "
                + "Inside the <code>try</code> block, call <code>Integer.parseInt(\"notANumber\")</code>. "
                + "In the <code>catch</code> block, print exactly: <code>Invalid number</code></p>"
                + "<p><strong>Expected output:</strong> <code>Invalid number</code></p>",

                // starter code
                "public class ExceptionBasics {\n"
                + "    public static void main(String[] args) {\n"
                + "        // Wrap the risky call in try/catch\n"
                + "        try {\n"
                + "            // TODO: call Integer.parseInt(\"notANumber\")\n"
                + "        } catch (NumberFormatException e) {\n"
                + "            // TODO: print \"Invalid number\"\n"
                + "        }\n"
                + "    }\n"
                + "}\n",

                // tests
                tests(
                    test("Catches NumberFormatException", "null", "Invalid number")
                ),

                // feynman prompt
                "What is an exception in Java and why does Java use exceptions instead of returning error codes from methods?"
        );

        mcQuestion("M1", QuestionTier.RECALL,
                "<p>Which exception is thrown when you try to access <code>arr[10]</code> on an array with only 5 elements?</p>",
                new String[]{
                    "NullPointerException",
                    "ArrayIndexOutOfBoundsException",
                    "NumberFormatException",
                    "ClassCastException"
                },
                "ArrayIndexOutOfBoundsException",
                "<p><code>ArrayIndexOutOfBoundsException</code> is thrown whenever you use an index that is "
                + "negative or greater than or equal to the array's length. "
                + "<code>NullPointerException</code> is for null references; "
                + "<code>NumberFormatException</code> is for bad number parsing.</p>");

        mcQuestion("M1", QuestionTier.RECALL,
                "<p>What information does a Java stack trace provide?</p>",
                new String[]{
                    "The type of exception, the error message, and the line numbers where it propagated",
                    "Only the line number where the exception occurred",
                    "The exception type and a list of all variables in the program",
                    "Nothing useful — stack traces should be ignored"
                },
                "The type of exception, the error message, and the line numbers where it propagated",
                "<p>A stack trace shows the exception class name, the detail message, "
                + "and a sequence of method calls (the call stack) with file names and line numbers, "
                + "letting you trace exactly how the program reached the failure point.</p>");

        codeQuestion("M1", QuestionTier.APPLICATION, QuestionType.DEBUGGING,
                "<p>A student wrote this code but it crashes. What exception does it throw and why?</p>",
                "String text = null;\nSystem.out.println(text.length());",
                "NullPointerException — calling a method on a null reference",
                "<p><code>text</code> is <code>null</code>, meaning it references nothing. "
                + "Calling <code>.length()</code> on it throws a <code>NullPointerException</code> "
                + "because there is no object to invoke the method on.</p>");

        tfQuestion("M1", QuestionTier.DISCRIMINATION,
                "<p><code>NullPointerException</code> is a checked exception that must be declared or caught.</p>",
                "False",
                "<p><code>NullPointerException</code> extends <code>RuntimeException</code>, making it an "
                + "<em>unchecked</em> exception. The compiler does not require you to handle it. "
                + "Only checked exceptions (subclasses of <code>Exception</code> that are not "
                + "<code>RuntimeException</code>) must be declared or caught.</p>");
    }

    // -------------------------------------------------------------------------
    // M2 — try / catch / finally
    // -------------------------------------------------------------------------
    private void seedM2() {
        subChunk("M2", "M", "try / catch / finally", 2, 50, "TryCatch.java",

                // hook
                "<p>You know how to catch exceptions — but what if you need some code to run "
                + "<em>no matter what</em>, whether an exception happened or not? "
                + "And what do you do when a single method could throw several different types of exceptions? "
                + "That's where <code>finally</code> and multiple catch blocks come in.</p>",

                // explanation
                "<h3>The Full try/catch/finally Structure</h3>"
                + "<p>The complete form has three parts:</p>"
                + "<pre><code>try {\n"
                + "    // risky code here\n"
                + "} catch (SomeException e) {\n"
                + "    // handle the problem\n"
                + "} finally {\n"
                + "    // ALWAYS runs — cleanup goes here\n"
                + "}</code></pre>"
                + "<p>The <code>finally</code> block executes whether an exception was thrown or not. "
                + "It's the right place to release resources like database connections or file handles.</p>"
                + "<h4>Multiple catch Blocks</h4>"
                + "<p>A single <code>try</code> can have many <code>catch</code> blocks — "
                + "one for each exception type you want to handle differently:</p>"
                + "<pre><code>try {\n"
                + "    int[] arr = new int[3];\n"
                + "    arr[5] = Integer.parseInt(input);\n"
                + "} catch (NumberFormatException e) {\n"
                + "    System.out.println(\"Not a number\");\n"
                + "} catch (ArrayIndexOutOfBoundsException e) {\n"
                + "    System.out.println(\"Index too large\");\n"
                + "} finally {\n"
                + "    System.out.println(\"Done\");\n"
                + "}</code></pre>"
                + "<h4>Catch Ordering — Specific Before General</h4>"
                + "<p>Always put more <em>specific</em> exception types before more <em>general</em> ones. "
                + "If you put <code>catch (Exception e)</code> first, it swallows everything "
                + "and your specific handlers below are unreachable (the compiler will warn you).</p>"
                + "<h4>Exception Hierarchy Basics</h4>"
                + "<p><code>Throwable</code> → <code>Exception</code> → <code>RuntimeException</code> → "
                + "NullPointerException, etc. The general rule: catch the most specific subclass first.</p>",

                // story
                story(
                    n("Archmage Eldrin constructs a three-layered ward around the Academy's most dangerous ritual chamber."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The <code>try</code> block is the ritual itself — potentially hazardous. "
                      + "The first <code>catch</code> ward intercepts fire backfires. "
                      + "The second catch ward intercepts energy overflows. "
                      + "And the <code>finally</code> ward? That one always triggers. "
                      + "It cleans up the ritual space whether the casting succeeded or exploded."),
                    e("Three-part ward",
                        "try {\n"
                        + "    performRitual(); // might throw\n"
                        + "} catch (FireBackfireException e) {\n"
                        + "    System.out.println(\"Doused the fire backfire\");\n"
                        + "} catch (EnergyOverflowException e) {\n"
                        + "    System.out.println(\"Grounded the overflow\");\n"
                        + "} finally {\n"
                        + "    System.out.println(\"Ritual chamber cleaned up\");\n"
                        + "}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Remember: the cleanup ward — <code>finally</code> — always fires. "
                      + "Even if you add a <code>return</code> statement inside a catch block, "
                      + "the finally block still executes before the method returns."),
                    n("Eldrin taps the wall where a motto is carved: "
                      + "<em>\"Specific before general — catch the narrow ward first, lest the broad one swallow all.\"</em>")
                ),

                // guided practice
                "<p>Write a method (or inline code in <code>main</code>) that divides two numbers. "
                + "Use these steps:</p>"
                + "<ol>"
                + "<li>Inside <code>try</code>: perform integer division <code>10 / 0</code></li>"
                + "<li>In <code>catch (ArithmeticException e)</code>: print <code>Cannot divide by zero</code></li>"
                + "<li>In <code>finally</code>: print <code>Operation complete</code></li>"
                + "</ol>"
                + "<p><strong>Expected output (two lines):</strong></p>"
                + "<pre><code>Cannot divide by zero\nOperation complete</code></pre>",

                // starter code
                "public class TryCatch {\n"
                + "    public static void main(String[] args) {\n"
                + "        try {\n"
                + "            int result = 10 / 0; // triggers ArithmeticException\n"
                + "            System.out.println(result);\n"
                + "        } catch (ArithmeticException e) {\n"
                + "            // TODO: print \"Cannot divide by zero\"\n"
                + "        } finally {\n"
                + "            // TODO: print \"Operation complete\"\n"
                + "        }\n"
                + "    }\n"
                + "}\n",

                // tests
                tests(
                    test("Division by zero", "null", "Cannot divide by zero\nOperation complete")
                ),

                // feynman prompt
                "Explain the role of each block in a try/catch/finally statement. When does each part execute?"
        );

        mcQuestion("M2", QuestionTier.RECALL,
                "<p>When does a <code>finally</code> block execute?</p>",
                new String[]{
                    "Only if no exception is thrown",
                    "Only if an exception is thrown",
                    "Always — whether or not an exception was thrown",
                    "Only if a catch block handles the exception"
                },
                "Always — whether or not an exception was thrown",
                "<p>The <code>finally</code> block is guaranteed to execute after the <code>try</code> "
                + "(and any matching <code>catch</code>), regardless of whether an exception occurred. "
                + "It is the correct place for cleanup code such as closing resources.</p>");

        codeQuestion("M2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "try {\n"
                + "    System.out.println(\"try\");\n"
                + "    int x = 1 / 0;\n"
                + "} catch (ArithmeticException e) {\n"
                + "    System.out.println(\"catch\");\n"
                + "} finally {\n"
                + "    System.out.println(\"finally\");\n"
                + "}",
                "try\ncatch\nfinally",
                "<p>\"try\" prints before the exception. The exception is thrown on division by zero, "
                + "so execution jumps to catch, printing \"catch\". "
                + "The finally block always runs last, printing \"finally\".</p>");

        mcQuestion("M2", QuestionTier.DISCRIMINATION,
                "<p>A method might throw both <code>IllegalArgumentException</code> and <code>RuntimeException</code>. "
                + "Which catch order is correct?</p>",
                new String[]{
                    "catch (IllegalArgumentException e) first, then catch (RuntimeException e)",
                    "catch (RuntimeException e) first, then catch (IllegalArgumentException e)",
                    "Either order is fine",
                    "You cannot catch both in the same try block"
                },
                "catch (IllegalArgumentException e) first, then catch (RuntimeException e)",
                "<p><code>IllegalArgumentException</code> is a subclass of <code>RuntimeException</code>. "
                + "More specific subclasses must come first. If <code>RuntimeException</code> came first, "
                + "it would catch <code>IllegalArgumentException</code> as well, "
                + "making the second catch unreachable — the compiler reports this as a compile error.</p>");

        tfQuestion("M2", QuestionTier.RECALL,
                "<p>A <code>try</code> block can have more than one <code>catch</code> block.</p>",
                "True",
                "<p>You can attach multiple <code>catch</code> blocks to a single <code>try</code>, "
                + "each handling a different exception type. Java checks them from top to bottom "
                + "and executes the first one whose type matches the thrown exception.</p>");
    }

    // -------------------------------------------------------------------------
    // M3 — Checked vs Unchecked Exceptions
    // -------------------------------------------------------------------------
    private void seedM3() {
        subChunk("M3", "M", "Checked vs Unchecked Exceptions", 3, 60, "CheckedUnchecked.java",

                // hook
                "<p>Why does Java force you to write <code>try/catch</code> around some method calls "
                + "but not others? The answer lies in a distinction that shapes how Java APIs are designed: "
                + "<strong>checked</strong> versus <strong>unchecked</strong> exceptions.</p>",

                // explanation
                "<h3>Checked Exceptions</h3>"
                + "<p>A <strong>checked exception</strong> is one that the compiler requires you to handle — "
                + "either with a <code>try/catch</code>, or by declaring it with <code>throws</code> "
                + "in the method signature. They represent conditions that a well-written program "
                + "should anticipate, such as a missing file or a failed network connection.</p>"
                + "<p>Common checked exceptions: <code>IOException</code>, <code>SQLException</code>, "
                + "<code>ClassNotFoundException</code>.</p>"
                + "<pre><code>// Must catch or declare:\npublic void readFile(String path) throws IOException {\n"
                + "    String content = Files.readString(Path.of(path));\n"
                + "}</code></pre>"
                + "<h3>Unchecked Exceptions</h3>"
                + "<p>An <strong>unchecked exception</strong> extends <code>RuntimeException</code>. "
                + "The compiler does not require you to handle them — they typically represent "
                + "programming mistakes (bugs) rather than expected error conditions.</p>"
                + "<p>Common unchecked exceptions: <code>NullPointerException</code>, "
                + "<code>ArrayIndexOutOfBoundsException</code>, <code>NumberFormatException</code>, "
                + "<code>IllegalArgumentException</code>.</p>"
                + "<h3>Multi-catch (Java 7+)</h3>"
                + "<p>When two exceptions need identical handling, you can combine them in one catch block "
                + "using the pipe symbol <code>|</code>:</p>"
                + "<pre><code>try {\n"
                + "    process(input);\n"
                + "} catch (NumberFormatException | NullPointerException e) {\n"
                + "    System.out.println(\"Caught: invalid input\");\n"
                + "}</code></pre>"
                + "<h3>The throws Declaration</h3>"
                + "<p>If your method does not catch a checked exception, it must declare it:</p>"
                + "<pre><code>public void loadSpell(String file) throws IOException {\n"
                + "    // ... may throw IOException\n"
                + "}</code></pre>"
                + "<p>This tells callers: \"I might throw this — you must handle it.\"</p>",

                // story
                story(
                    n("Archmage Eldrin leads the class into the ingredient storeroom."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Some ingredients are inherently dangerous — <em>volatile moonshards</em>, say. "
                      + "Whenever you requisition them, the storeroom <em>demands</em> you sign a hazard form. "
                      + "That is a <strong>checked exception</strong> — the system forces you to acknowledge the risk."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Other mishaps — dropping a flask, mispronouncing an incantation — "
                      + "only happen if you're careless. The storeroom does not make you sign anything for those. "
                      + "Those are <strong>unchecked exceptions</strong>: bugs you should simply fix."),
                    e("Checked — must acknowledge",
                        "try {\n"
                        + "    String data = Files.readString(Path.of(\"spells.txt\"));\n"
                        + "} catch (IOException e) {\n"
                        + "    System.out.println(\"Spell book not found\");\n"
                        + "}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "And for multi-catch — when two ingredients cause the same kind of havoc, "
                      + "one ward can handle both, using the pipe symbol between their names.")
                ),

                // guided practice
                "<p>Use <strong>multi-catch</strong> to handle two exceptions with one catch block.</p>"
                + "<p>Inside <code>try</code>, call <code>Integer.parseInt(null)</code> "
                + "(this throws <code>NumberFormatException</code> or <code>NullPointerException</code> depending on JVM). "
                + "Catch <code>NumberFormatException | NullPointerException</code> and print exactly: "
                + "<code>Caught: invalid input</code></p>"
                + "<p><strong>Expected output:</strong> <code>Caught: invalid input</code></p>",

                // starter code
                "import java.io.*;\n"
                + "import java.nio.file.*;\n"
                + "\n"
                + "public class CheckedUnchecked {\n"
                + "    public static void main(String[] args) {\n"
                + "        try {\n"
                + "            // TODO: call Integer.parseInt(null)\n"
                + "        } catch (NumberFormatException | NullPointerException e) {\n"
                + "            // TODO: print \"Caught: invalid input\"\n"
                + "        }\n"
                + "    }\n"
                + "}\n",

                // tests
                tests(
                    test("Multi-catch", "null", "Caught: invalid input")
                ),

                // feynman prompt
                "What is the difference between a checked and an unchecked exception in Java? Give an example of each."
        );

        mcQuestion("M3", QuestionTier.RECALL,
                "<p>Which of the following is a <em>checked</em> exception?</p>",
                new String[]{"NullPointerException", "IOException", "ArrayIndexOutOfBoundsException", "IllegalArgumentException"},
                "IOException",
                "<p><code>IOException</code> extends <code>Exception</code> directly (not via <code>RuntimeException</code>), "
                + "making it a checked exception that the compiler requires you to handle. "
                + "The other options all extend <code>RuntimeException</code> and are unchecked.</p>");

        tfQuestion("M3", QuestionTier.RECALL,
                "<p>The <code>throws</code> keyword in a method signature declares that the method might throw a checked exception.</p>",
                "True",
                "<p><code>throws</code> in a method signature signals to callers that a checked exception may propagate "
                + "out of the method. Callers must then either catch it or declare it themselves.</p>");

        codeQuestion("M3", QuestionTier.APPLICATION, QuestionType.DEBUGGING,
                "<p>This code does not compile. Why not?</p>",
                "public void readData() {\n"
                + "    String content = Files.readString(Path.of(\"data.txt\"));\n"
                + "}",
                "IOException is a checked exception — the method must either catch it or declare 'throws IOException'",
                "<p><code>Files.readString</code> throws <code>IOException</code>, a checked exception. "
                + "The compiler requires the calling method to either wrap it in <code>try/catch</code> "
                + "or add <code>throws IOException</code> to the method signature.</p>");

        mcQuestion("M3", QuestionTier.DISCRIMINATION,
                "<p>What is the advantage of Java 7's multi-catch syntax <code>catch (A | B e)</code>?</p>",
                new String[]{
                    "It reduces code duplication when two exceptions need identical handling",
                    "It catches exceptions in a specific order",
                    "It allows catching checked and unchecked exceptions together",
                    "It automatically logs both exception messages"
                },
                "It reduces code duplication when two exceptions need identical handling",
                "<p>Multi-catch lets you handle two exception types with the same logic in a single block, "
                + "eliminating copy-pasted catch bodies. Note: the two types must not be in the same "
                + "inheritance hierarchy.</p>");
    }

    // -------------------------------------------------------------------------
    // M4 — Custom Exceptions
    // -------------------------------------------------------------------------
    private void seedM4() {
        subChunk("M4", "M", "Custom Exceptions", 4, 60, "CustomExceptions.java",

                // hook
                "<p>Java's built-in exceptions cover common programming errors, "
                + "but what about domain-specific problems? "
                + "What if your spell-casting system needs to signal that the caster has run out of mana, "
                + "or that a potion recipe is invalid? That's where <strong>custom exceptions</strong> come in.</p>",

                // explanation
                "<h3>Creating Custom Exception Classes</h3>"
                + "<p>Custom exceptions are just regular classes that extend either "
                + "<code>Exception</code> (checked) or <code>RuntimeException</code> (unchecked). "
                + "Most custom exceptions provide at least a constructor that accepts a message:</p>"
                + "<pre><code>public class InsufficientManaException extends RuntimeException {\n"
                + "    public InsufficientManaException(String message) {\n"
                + "        super(message);\n"
                + "    }\n"
                + "}</code></pre>"
                + "<p>Throw it with <code>throw new InsufficientManaException(\"Not enough mana\");</code></p>"
                + "<h4>Checked or Unchecked?</h4>"
                + "<ul>"
                + "<li>Extend <code>RuntimeException</code> when the failure represents a programming error "
                + "or a condition the caller is not expected to recover from.</li>"
                + "<li>Extend <code>Exception</code> when the caller <em>should</em> handle the failure "
                + "(e.g., invalid user input, missing configuration).</li>"
                + "</ul>"
                + "<p>Modern Java practice leans toward unchecked exceptions for most custom domain errors.</p>"
                + "<h4>Exception Chaining</h4>"
                + "<p>You can wrap a low-level exception in a higher-level one using the <code>cause</code> constructor:</p>"
                + "<pre><code>try {\n"
                + "    Integer.parseInt(badInput);\n"
                + "} catch (NumberFormatException e) {\n"
                + "    throw new SpellConfigException(\"Bad configuration\", e);\n"
                + "}</code></pre>"
                + "<h4>Best Practice</h4>"
                + "<p>Prefer standard exceptions where they fit: <code>IllegalArgumentException</code>, "
                + "<code>IllegalStateException</code>, <code>UnsupportedOperationException</code>. "
                + "Create custom exceptions only when a standard one doesn't convey the right meaning.</p>",

                // story
                story(
                    n("Archmage Eldrin unfurls a blank scroll and writes a single line at the top: "
                      + "<em>SpellMisfireException</em>."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "A <code>NullPointerException</code> tells you nothing about <em>why</em> "
                      + "your spell misfired. But a <code>SpellMisfireException</code> carrying the message "
                      + "<em>'The reagent ratio was off by 3%'</em> — now that's useful. "
                      + "Custom exceptions give failures a name and a voice."),
                    e("SpellMisfireException",
                        "public class SpellMisfireException extends RuntimeException {\n"
                        + "    public SpellMisfireException(String message) {\n"
                        + "        super(message);\n"
                        + "    }\n"
                        + "}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "I extend <code>RuntimeException</code> because misfire is the caster's fault — "
                      + "a programming error. I don't want every caller forced to wrap their code "
                      + "in try/catch for something that shouldn't happen in correct code. "
                      + "Now go — create your own domain exception."),
                    n("You take the scroll and begin writing an "
                      + "<code>InsufficientManaException</code> for the Academy's combat spellcasting module.")
                ),

                // guided practice
                "<p>Create a custom exception and use it:</p>"
                + "<ol>"
                + "<li>Define <code>InsufficientManaException extends RuntimeException</code> with a message constructor.</li>"
                + "<li>Write a method <code>castSpell(int manaCost, int availableMana)</code> that throws "
                + "<code>InsufficientManaException</code> if <code>availableMana &lt; manaCost</code>.</li>"
                + "<li>In <code>main</code>, call <code>castSpell(50, 30)</code> inside a <code>try/catch</code> "
                + "and print the exception message.</li>"
                + "</ol>"
                + "<p><strong>Expected output:</strong> <code>Cannot cast: not enough mana</code></p>",

                // starter code
                "public class CustomExceptions {\n"
                + "\n"
                + "    // TODO: define InsufficientManaException extending RuntimeException\n"
                + "\n"
                + "    static void castSpell(int manaCost, int availableMana) {\n"
                + "        if (availableMana < manaCost) {\n"
                + "            // TODO: throw new InsufficientManaException(\"Cannot cast: not enough mana\")\n"
                + "        }\n"
                + "        System.out.println(\"Spell cast successfully!\");\n"
                + "    }\n"
                + "\n"
                + "    public static void main(String[] args) {\n"
                + "        try {\n"
                + "            castSpell(50, 30);\n"
                + "        } catch (InsufficientManaException e) {\n"
                + "            System.out.println(e.getMessage());\n"
                + "        }\n"
                + "    }\n"
                + "}\n",

                // tests
                tests(
                    test("Custom exception message", "null", "Cannot cast: not enough mana")
                ),

                // feynman prompt
                "When should you create a custom exception class in Java, and should it extend Exception or RuntimeException?"
        );

        mcQuestion("M4", QuestionTier.RECALL,
                "<p>To create a custom <em>unchecked</em> exception, your class should extend:</p>",
                new String[]{"Exception", "RuntimeException", "Throwable", "Error"},
                "RuntimeException",
                "<p><code>RuntimeException</code> is the root of the unchecked exception hierarchy. "
                + "Extending it means callers are not forced to handle the exception at compile time. "
                + "Extending <code>Exception</code> creates a checked exception.</p>");

        codeQuestion("M4", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>Given a correctly implemented <code>InsufficientManaException</code>, what does this print?</p>",
                "try {\n"
                + "    throw new InsufficientManaException(\"Not enough mana\");\n"
                + "} catch (InsufficientManaException e) {\n"
                + "    System.out.println(\"Caught: \" + e.getMessage());\n"
                + "}",
                "Caught: Not enough mana",
                "<p><code>e.getMessage()</code> returns the string passed to the constructor, "
                + "which is then concatenated with <code>\"Caught: \"</code> and printed.</p>");

        tfQuestion("M4", QuestionTier.DISCRIMINATION,
                "<p>It is always better to create a custom exception class than to use a standard Java exception.</p>",
                "False",
                "<p>Best practice is to prefer standard exceptions like <code>IllegalArgumentException</code> "
                + "or <code>IllegalStateException</code> when they accurately convey the error. "
                + "Custom exceptions add value when they provide domain-specific meaning "
                + "that no standard exception expresses clearly.</p>");

        mcQuestion("M4", QuestionTier.DISCRIMINATION,
                "<p>What is exception chaining in Java?</p>",
                new String[]{
                    "Wrapping a caught low-level exception inside a higher-level custom exception, preserving the original cause",
                    "Catching multiple exceptions in sequence using chained catch blocks",
                    "Throwing the same exception from multiple methods",
                    "Using multi-catch to combine two exception types"
                },
                "Wrapping a caught low-level exception inside a higher-level custom exception, preserving the original cause",
                "<p>Exception chaining preserves the original cause by passing it to the new exception's constructor "
                + "via the <code>cause</code> parameter (e.g., <code>new MyException(\"msg\", originalException)</code>). "
                + "This keeps the full diagnostic chain intact while providing a higher-level description.</p>");
    }

    // =========================================================================
    // CHUNK N — Core Java APIs
    // =========================================================================
    private void seedChunkN() {
        chunk("N", "Core Java APIs", "\uD83D\uDD27", 14, LearnerPath.FOUNDATION, "H", "M");

        seedN1();
        seedN2();
        seedN3();
        seedN4();
    }

    // -------------------------------------------------------------------------
    // N1 — Math & Random
    // -------------------------------------------------------------------------
    private void seedN1() {
        subChunk("N1", "N", "Math & Random", 1, 50, "MathRandom.java",

                // hook
                "<p>Should you implement your own square-root algorithm? "
                + "Your own function for rounding numbers? Of course not — "
                + "Java's <code>Math</code> class ships with dozens of battle-tested mathematical operations "
                + "ready to use without importing anything. You also get a <code>Random</code> class "
                + "for generating pseudorandom numbers. Stop reinventing the wheel.</p>",

                // explanation
                "<h3>The Math Class</h3>"
                + "<p>All methods in <code>java.lang.Math</code> are <code>static</code>, "
                + "so you call them directly on the class name — no object needed.</p>"
                + "<pre><code>Math.abs(-7)        // 7    — absolute value\n"
                + "Math.max(3, 9)      // 9    — larger of two values\n"
                + "Math.min(3, 9)      // 3    — smaller of two values\n"
                + "Math.pow(2, 10)     // 1024.0 — 2 to the power of 10\n"
                + "Math.sqrt(144.0)    // 12.0 — square root\n"
                + "Math.round(3.7)     // 4    — rounds to nearest long/int\n"
                + "Math.floor(3.9)     // 3.0  — rounds down\n"
                + "Math.ceil(3.1)      // 4.0  — rounds up\n"
                + "Math.PI             // 3.141592653589793</code></pre>"
                + "<h3>Random Numbers</h3>"
                + "<p><code>Math.random()</code> returns a <code>double</code> in the range [0.0, 1.0). "
                + "For more control, use <code>java.util.Random</code>:</p>"
                + "<pre><code>import java.util.Random;\n"
                + "\n"
                + "Random rng = new Random();\n"
                + "int die = rng.nextInt(6) + 1;  // 1 to 6 inclusive\n"
                + "double chance = rng.nextDouble(); // 0.0 to &lt;1.0</code></pre>"
                + "<p><code>nextInt(bound)</code> returns a value from 0 (inclusive) to bound (exclusive), "
                + "so <code>nextInt(6) + 1</code> gives 1–6.</p>"
                + "<p>For <strong>reproducible tests</strong>, create the Random with a fixed seed: "
                + "<code>new Random(42)</code>.</p>",

                // story
                story(
                    n("Archmage Eldrin leads you to the Great Mathematical Grimoire — "
                      + "a vast tome bound in bronze, its pages shimmering with pre-written incantations."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Every wizard who ever lived has needed to take a square root, "
                      + "find the larger of two numbers, or roll a magical die. "
                      + "Do not waste your arcane energy writing these from scratch. "
                      + "The Grimoire — Java's <code>Math</code> class — contains them all."),
                    e("Consulting the Grimoire",
                        "System.out.println(Math.pow(2, 10));   // 1024.0\n"
                        + "System.out.println(Math.sqrt(144.0));  // 12.0\n"
                        + "System.out.println(Math.round(3.7));   // 4"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "And for randomness — every great ritual needs an element of the unknown. "
                      + "The <code>Random</code> class is your magical die. "
                      + "Give it a seed and the sequence becomes perfectly repeatable — "
                      + "essential for testing your spells reliably."),
                    e("Rolling the magical die",
                        "import java.util.Random;\n"
                        + "Random rng = new Random();\n"
                        + "int roll = rng.nextInt(6) + 1;\n"
                        + "System.out.println(\"Rolled: \" + roll);")
                ),

                // guided practice
                "<p>Print the following, each on its own line:</p>"
                + "<ol>"
                + "<li><code>Math.pow(2, 10)</code> — should print <code>1024.0</code></li>"
                + "<li><code>Math.sqrt(144.0)</code> — should print <code>12.0</code></li>"
                + "<li><code>Math.round(3.7)</code> — should print <code>4</code></li>"
                + "<li>The string <code>Rolling dice...</code> (simulate rolling a die — print the label only for a deterministic test)</li>"
                + "</ol>"
                + "<p><strong>Expected output:</strong></p>"
                + "<pre><code>1024.0\n12.0\n4\nRolling dice...</code></pre>",

                // starter code
                "public class MathRandom {\n"
                + "    public static void main(String[] args) {\n"
                + "        // TODO: print Math.pow(2, 10)\n"
                + "        // TODO: print Math.sqrt(144.0)\n"
                + "        // TODO: print Math.round(3.7)\n"
                + "        System.out.println(\"Rolling dice...\");\n"
                + "    }\n"
                + "}\n",

                // tests
                tests(
                    test("Math results and dice label", "null", "1024.0\n12.0\n4\nRolling dice...")
                ),

                // feynman prompt
                "Name five Math class methods and explain what each one does, including its parameter types and return type."
        );

        mcQuestion("N1", QuestionTier.RECALL,
                "<p>What does <code>Math.round(3.5)</code> return in Java?</p>",
                new String[]{"3", "4", "3.5", "4.0"},
                "4",
                "<p><code>Math.round</code> rounds to the nearest integer. "
                + "When the fractional part is exactly 0.5, Java rounds up (towards positive infinity), "
                + "so <code>3.5</code> becomes <code>4</code>. The return type for a <code>double</code> argument is <code>long</code>.</p>");

        codeQuestion("N1", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "System.out.println(Math.max(15, Math.min(20, 10)));",
                "15",
                "<p>Inner call: <code>Math.min(20, 10)</code> = <code>10</code>. "
                + "Outer call: <code>Math.max(15, 10)</code> = <code>15</code>.</p>");

        mcQuestion("N1", QuestionTier.APPLICATION,
                "<p>How do you generate a random integer between 1 and 10 inclusive using <code>Random</code>?</p>",
                new String[]{
                    "rng.nextInt(10) + 1",
                    "rng.nextInt(11)",
                    "rng.nextInt(10)",
                    "rng.nextInt(1, 10)"
                },
                "rng.nextInt(10) + 1",
                "<p><code>nextInt(10)</code> returns 0–9. Adding 1 shifts the range to 1–10. "
                + "<code>nextInt(11)</code> gives 0–10. <code>nextInt(1, 10)</code> is a newer overload "
                + "but gives 1–9, not 1–10.</p>");

        tfQuestion("N1", QuestionTier.DISCRIMINATION,
                "<p><code>Math.floor(3.9)</code> returns <code>3</code> (as an int).</p>",
                "False",
                "<p><code>Math.floor</code> always returns a <code>double</code>. "
                + "<code>Math.floor(3.9)</code> returns <code>3.0</code>, not <code>3</code>. "
                + "If you need an int, you must cast: <code>(int) Math.floor(3.9)</code>.</p>");
    }

    // -------------------------------------------------------------------------
    // N2 — Date & Time (java.time)
    // -------------------------------------------------------------------------
    private void seedN2() {
        subChunk("N2", "N", "Date & Time (java.time)", 2, 60, "DateTime.java",

                // hook
                "<p>Dates and times seem simple until you actually work with them — time zones, "
                + "leap years, varying month lengths, daylight saving time... "
                + "Java's original <code>Date</code> and <code>Calendar</code> classes were famously painful to use. "
                + "Java 8 introduced the <code>java.time</code> package, which finally got this right.</p>",

                // explanation
                "<h3>Core Classes</h3>"
                + "<ul>"
                + "<li><code>LocalDate</code> — date only (year, month, day); no time, no timezone</li>"
                + "<li><code>LocalTime</code> — time only (hours, minutes, seconds, nanoseconds)</li>"
                + "<li><code>LocalDateTime</code> — date + time combined</li>"
                + "</ul>"
                + "<h3>Creating Instances</h3>"
                + "<pre><code>import java.time.*;\n"
                + "import java.time.format.*;\n"
                + "\n"
                + "LocalDate today = LocalDate.now();\n"
                + "LocalDate birthday = LocalDate.of(1990, 6, 15);\n"
                + "LocalTime now = LocalTime.now();\n"
                + "LocalDateTime stamp = LocalDateTime.now();</code></pre>"
                + "<h3>Arithmetic</h3>"
                + "<p>All <code>java.time</code> objects are <strong>immutable</strong>. "
                + "Arithmetic returns a <em>new</em> object — the original is unchanged:</p>"
                + "<pre><code>LocalDate start = LocalDate.of(2025, 1, 15);\n"
                + "LocalDate end = start.plusDays(30);    // 2025-02-14\n"
                + "LocalDate earlier = start.minusMonths(1); // 2024-12-15</code></pre>"
                + "<h3>Formatting</h3>"
                + "<pre><code>DateTimeFormatter fmt = DateTimeFormatter.ofPattern(\"dd/MM/yyyy\");\n"
                + "String formatted = LocalDate.now().format(fmt);\n"
                + "System.out.println(formatted); // e.g. 13/04/2026</code></pre>"
                + "<h3>Period and Duration</h3>"
                + "<p><code>Period</code> represents a date-based amount (years, months, days). "
                + "<code>Duration</code> represents a time-based amount (hours, minutes, seconds).</p>"
                + "<pre><code>Period gap = Period.between(start, end);\n"
                + "System.out.println(gap.getDays()); // 30</code></pre>"
                + "<h3>Why Not java.util.Date?</h3>"
                + "<p>The old <code>Date</code> class is mutable, poorly named (months are 0-indexed!), "
                + "and lacks timezone support. <code>java.time</code> is immutable, consistent, "
                + "and timezone-aware via <code>ZonedDateTime</code>.</p>",

                // story
                story(
                    n("Archmage Eldrin enchants a glowing calendar on the wall of his study. "
                      + "Dates ripple and shift as he gestures."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The old Calendar grimoire was a disaster — months started at zero, "
                      + "nothing was immutable, and time zones were a nightmare. "
                      + "In Java 8, we rewrote the entire chapter. Meet <code>java.time</code>: "
                      + "clean, immutable, and sensible."),
                    e("A magical contract date",
                        "import java.time.*;\n"
                        + "LocalDate signed = LocalDate.of(2025, 1, 15);\n"
                        + "LocalDate expires = signed.plusDays(30);\n"
                        + "System.out.println(expires); // 2025-02-14"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Notice: calling <code>plusDays</code> does not change <code>signed</code>. "
                      + "It creates a brand new date object. All <code>java.time</code> objects are immutable — "
                      + "a deliberate design choice that prevents many subtle bugs."),
                    n("The calendar pulses softly, the contract date highlighted in gold — "
                      + "exactly thirty days after signing, to the second.")
                ),

                // guided practice
                "<p>Perform the following operations:</p>"
                + "<ol>"
                + "<li>Create a <code>LocalDate</code> for <strong>2025-01-15</strong> using <code>LocalDate.of()</code>.</li>"
                + "<li>Add 30 days to get the expiry date.</li>"
                + "<li>Print the expiry date (default <code>toString</code> format is <code>yyyy-MM-dd</code>).</li>"
                + "</ol>"
                + "<p><strong>Expected output:</strong> <code>2025-02-14</code></p>",

                // starter code
                "import java.time.*;\n"
                + "import java.time.format.*;\n"
                + "\n"
                + "public class DateTime {\n"
                + "    public static void main(String[] args) {\n"
                + "        // TODO: create LocalDate for 2025-01-15\n"
                + "        // TODO: add 30 days\n"
                + "        // TODO: print the result\n"
                + "    }\n"
                + "}\n",

                // tests
                tests(
                    test("Date plus 30 days", "null", "2025-02-14")
                ),

                // feynman prompt
                "Explain the java.time package. Why was it introduced in Java 8? What are the key differences from java.util.Date?"
        );

        mcQuestion("N2", QuestionTier.RECALL,
                "<p>Which class would you use to represent a date without a time or timezone?</p>",
                new String[]{"LocalDateTime", "LocalDate", "LocalTime", "ZonedDateTime"},
                "LocalDate",
                "<p><code>LocalDate</code> holds only a year, month, and day — no time, no timezone. "
                + "<code>LocalDateTime</code> adds time; <code>ZonedDateTime</code> adds a timezone.</p>");

        tfQuestion("N2", QuestionTier.RECALL,
                "<p>Calling <code>plusDays(10)</code> on a <code>LocalDate</code> modifies the original object.</p>",
                "False",
                "<p>All <code>java.time</code> objects are <strong>immutable</strong>. "
                + "<code>plusDays</code> returns a new <code>LocalDate</code> object; "
                + "the original is unchanged. You must capture the return value.</p>");

        codeQuestion("N2", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "LocalDate d = LocalDate.of(2024, 2, 28);\nSystem.out.println(d.plusDays(1));",
                "2024-02-29",
                "<p>2024 is a leap year, so February has 29 days. "
                + "<code>plusDays(1)</code> from Feb 28 gives Feb 29. "
                + "<code>LocalDate.toString()</code> uses ISO format: <code>yyyy-MM-dd</code>.</p>");

        mcQuestion("N2", QuestionTier.DISCRIMINATION,
                "<p>You need to calculate the number of days between two <code>LocalDate</code> values. Which class do you use?</p>",
                new String[]{"Duration", "Period", "DateTimeFormatter", "ChronoUnit"},
                "Period",
                "<p><code>Period</code> represents a date-based amount (years, months, days) "
                + "and is used with <code>LocalDate</code>. "
                + "<code>Duration</code> is for time-based amounts (nanoseconds to hours) "
                + "and works with <code>LocalTime</code> or <code>Instant</code>. "
                + "(<code>ChronoUnit.DAYS.between(d1, d2)</code> also works but returns a <code>long</code>.)</p>");
    }

    // -------------------------------------------------------------------------
    // N3 — Enums
    // -------------------------------------------------------------------------
    private void seedN3() {
        subChunk("N3", "N", "Enums", 3, 50, "EnumsEx.java",

                // hook
                "<p>Your dungeon game has four directions: NORTH, SOUTH, EAST, WEST. "
                + "You could represent them as integers (0, 1, 2, 3) or strings (\"north\", \"south\"…), "
                + "but both approaches are fragile — what stops someone passing 99 or \"sideways\"? "
                + "<strong>Enums</strong> give you a fixed, type-safe set of named constants.</p>",

                // explanation
                "<h3>Declaring an Enum</h3>"
                + "<pre><code>public enum Season {\n"
                + "    SPRING, SUMMER, AUTUMN, WINTER\n"
                + "}</code></pre>"
                + "<p>Each value is a constant of type <code>Season</code>. "
                + "You reference them as <code>Season.SUMMER</code>.</p>"
                + "<h3>Enums in switch</h3>"
                + "<pre><code>Season s = Season.SUMMER;\n"
                + "switch (s) {\n"
                + "    case SPRING: System.out.println(\"Blossoming\"); break;\n"
                + "    case SUMMER: System.out.println(\"Warm and sunny\"); break;\n"
                + "    case AUTUMN: System.out.println(\"Falling leaves\"); break;\n"
                + "    case WINTER: System.out.println(\"Cold and dark\"); break;\n"
                + "}</code></pre>"
                + "<h3>Built-in Methods</h3>"
                + "<pre><code>Season.WINTER.name()    // \"WINTER\" — the constant's name as a String\n"
                + "Season.WINTER.ordinal() // 3 — zero-based position\n"
                + "Season.valueOf(\"SPRING\") // Season.SPRING</code></pre>"
                + "<h3>Enums with Fields and Methods</h3>"
                + "<p>Enums are full classes — they can have fields, constructors, and methods:</p>"
                + "<pre><code>public enum Planet {\n"
                + "    MERCURY(3.303e+23, 2.4397e6),\n"
                + "    VENUS  (4.869e+24, 6.0518e6);\n"
                + "\n"
                + "    private final double mass;\n"
                + "    private final double radius;\n"
                + "\n"
                + "    Planet(double mass, double radius) {\n"
                + "        this.mass = mass;\n"
                + "        this.radius = radius;\n"
                + "    }\n"
                + "}</code></pre>"
                + "<p><code>EnumSet</code> and <code>EnumMap</code> provide highly efficient collection "
                + "implementations specialised for enum keys.</p>",

                // story
                story(
                    n("Archmage Eldrin stands before four crystalline orbs, each radiating a different energy."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "The four schools of magic: FIRE, WATER, EARTH, AIR. "
                      + "These are not arbitrary strings. They are a <em>fixed, known set</em>. "
                      + "No apprentice can invent a fifth school by typo. "
                      + "This is what an <code>enum</code> gives you — a closed, type-safe catalogue."),
                    e("The four schools",
                        "public enum MagicSchool {\n"
                        + "    FIRE, WATER, EARTH, AIR\n"
                        + "}"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "Notice how cleanly this reads in a switch statement. "
                      + "And because the compiler knows all possible values, "
                      + "it can warn you if you forget to handle one in a switch expression. "
                      + "Try representing that with integers."),
                    n("One of the apprentices raises a hand. \"Can an enum have methods?\" "
                      + "Eldrin smiles. \"An enum is a full class. It can have fields, constructors, and methods. "
                      + "But today, keep it simple — master the basics first.\"")
                ),

                // guided practice
                "<p>Define a <code>Season</code> enum with values <code>SPRING</code>, "
                + "<code>SUMMER</code>, <code>AUTUMN</code>, <code>WINTER</code>.</p>"
                + "<p>Write a method <code>getDescription(Season s)</code> that returns a short phrase for each season "
                + "using a switch statement.</p>"
                + "<p>In <code>main</code>, print the descriptions for <code>SUMMER</code> and <code>WINTER</code> "
                + "(each on its own line).</p>"
                + "<p><strong>Expected output:</strong></p>"
                + "<pre><code>Warm and sunny\nCold and dark</code></pre>",

                // starter code
                "public class EnumsEx {\n"
                + "\n"
                + "    enum Season {\n"
                + "        SPRING, SUMMER, AUTUMN, WINTER\n"
                + "    }\n"
                + "\n"
                + "    static String getDescription(Season s) {\n"
                + "        switch (s) {\n"
                + "            case SPRING: return \"Fresh and rainy\";\n"
                + "            case SUMMER: return \"Warm and sunny\";\n"
                + "            case AUTUMN: return \"Cool and crisp\";\n"
                + "            case WINTER: return \"Cold and dark\";\n"
                + "            default: return \"Unknown\";\n"
                + "        }\n"
                + "    }\n"
                + "\n"
                + "    public static void main(String[] args) {\n"
                + "        // TODO: print getDescription for SUMMER and WINTER\n"
                + "    }\n"
                + "}\n",

                // tests
                tests(
                    test("Season descriptions", "null", "Warm and sunny\nCold and dark")
                ),

                // feynman prompt
                "What is an enum in Java? How is it different from using int constants or String constants for a fixed set of values?"
        );

        mcQuestion("N3", QuestionTier.RECALL,
                "<p>What does <code>Season.WINTER.ordinal()</code> return if the enum is declared as "
                + "<code>SPRING, SUMMER, AUTUMN, WINTER</code>?</p>",
                new String[]{"0", "1", "3", "4"},
                "3",
                "<p><code>ordinal()</code> returns the zero-based position of the constant in the enum declaration. "
                + "SPRING=0, SUMMER=1, AUTUMN=2, WINTER=3.</p>");

        codeQuestion("N3", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>Given <code>enum Color { RED, GREEN, BLUE }</code>, what does this print?</p>",
                "System.out.println(Color.GREEN.name());\nSystem.out.println(Color.GREEN.ordinal());",
                "GREEN\n1",
                "<p><code>name()</code> returns the constant's name as a String: <code>\"GREEN\"</code>. "
                + "<code>ordinal()</code> returns its zero-based position: RED=0, GREEN=1, BLUE=2.</p>");

        tfQuestion("N3", QuestionTier.RECALL,
                "<p>An enum in Java can have fields and methods.</p>",
                "True",
                "<p>Enums are full class types in Java. You can add fields, constructors, and methods. "
                + "Each constant can also override methods individually, making enums very powerful "
                + "beyond simple constants.</p>");

        mcQuestion("N3", QuestionTier.DISCRIMINATION,
                "<p>Why is using an enum better than using <code>int</code> constants for a fixed set of values?</p>",
                new String[]{
                    "Enums are type-safe — the compiler rejects invalid values; int constants allow any integer",
                    "Enums are faster at runtime than ints",
                    "Enums can only hold four values",
                    "Int constants cannot be used in switch statements"
                },
                "Enums are type-safe — the compiler rejects invalid values; int constants allow any integer",
                "<p>With int constants, nothing stops a caller from passing <code>99</code> as a 'direction'. "
                + "Enums constrain the type to only the declared values, catching mistakes at compile time. "
                + "They also provide built-in <code>name()</code>, <code>ordinal()</code>, "
                + "and meaningful <code>toString()</code>.</p>");
    }

    // -------------------------------------------------------------------------
    // N4 — Optional & Varargs
    // -------------------------------------------------------------------------
    private void seedN4() {
        subChunk("N4", "N", "Optional & Varargs", 4, 60, "OptionalVarargs.java",

                // hook
                "<p>Two common problems: (1) a method might or might not have a result — "
                + "returning <code>null</code> to mean \"nothing\" causes endless <code>NullPointerException</code>s. "
                + "(2) a method should accept any number of arguments — do you really want fifteen overloads? "
                + "<code>Optional</code> and <strong>varargs</strong> solve these cleanly.</p>",

                // explanation
                "<h3>Optional&lt;T&gt;</h3>"
                + "<p><code>Optional</code> is a container that either holds a value or holds nothing. "
                + "It makes the possibility of absence explicit in the type signature:</p>"
                + "<pre><code>Optional&lt;String&gt; found = Optional.of(\"Fireball\");\n"
                + "Optional&lt;String&gt; missing = Optional.empty();\n"
                + "\n"
                + "// Safe access:\n"
                + "System.out.println(found.isPresent());   // true\n"
                + "System.out.println(found.get());         // Fireball\n"
                + "System.out.println(missing.orElse(\"Spell not found\")); // Spell not found</code></pre>"
                + "<p>Key factory methods:</p>"
                + "<ul>"
                + "<li><code>Optional.of(value)</code> — value must not be null</li>"
                + "<li><code>Optional.ofNullable(value)</code> — wraps null as empty</li>"
                + "<li><code>Optional.empty()</code> — an empty Optional</li>"
                + "</ul>"
                + "<p>Useful terminal methods: <code>isPresent()</code>, <code>isEmpty()</code>, "
                + "<code>get()</code>, <code>orElse(default)</code>, <code>orElseGet(supplier)</code>, "
                + "<code>ifPresent(consumer)</code>.</p>"
                + "<h3>Varargs</h3>"
                + "<p>Varargs (variable-length arguments) let a method accept zero or more arguments of a type:</p>"
                + "<pre><code>static int sum(int... numbers) {\n"
                + "    int total = 0;\n"
                + "    for (int n : numbers) total += n;\n"
                + "    return total;\n"
                + "}</code></pre>"
                + "<p>Callers can pass any number of arguments — or even an array:</p>"
                + "<pre><code>sum(1, 2, 3);           // 6\n"
                + "sum(10, 20, 30, 40);    // 100\n"
                + "sum();                  // 0</code></pre>"
                + "<p>Internally, Java treats <code>int... numbers</code> as an <code>int[]</code>. "
                + "Varargs parameter must be <strong>last</strong> in the parameter list.</p>"
                + "<h3>Combining Both</h3>"
                + "<pre><code>static Optional&lt;Integer&gt; safeDivide(int a, int b) {\n"
                + "    if (b == 0) return Optional.empty();\n"
                + "    return Optional.of(a / b);\n"
                + "}</code></pre>",

                // story
                story(
                    n("Archmage Eldrin opens the Academy's spell catalogue — a book where some entries exist and some do not."),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "When a student looks up a spell that doesn't exist, the old system returned <code>null</code>. "
                      + "Null is a lie — it means 'nothing', but the type says 'a thing'. "
                      + "<code>Optional</code> tells the truth: 'this method might return a Spell, or it might not.' "
                      + "The absence is part of the contract."),
                    e("Spell lookup with Optional",
                        "static Optional<String> findSpell(String name) {\n"
                        + "    if (name.equals(\"Fireball\")) return Optional.of(\"Fireball\");\n"
                        + "    return Optional.empty();\n"
                        + "}\n"
                        + "\n"
                        + "System.out.println(findSpell(\"Fireball\").orElse(\"Spell not found\"));\n"
                        + "System.out.println(findSpell(\"Unknown\").orElse(\"Spell not found\"));"),
                    d("\uD83E\uDDD9", "mentor", "Archmage Eldrin", "mentor",
                      "And varargs — some spells accept one ingredient, some accept ten. "
                      + "You don't write ten overloaded methods. "
                      + "You write one method with <code>String... ingredients</code> "
                      + "and let the caller pass as many as needed."),
                    e("Varargs ingredient list",
                        "static int sum(int... numbers) {\n"
                        + "    int total = 0;\n"
                        + "    for (int n : numbers) total += n;\n"
                        + "    return total;\n"
                        + "}\n"
                        + "System.out.println(sum(1, 2, 3, 4, 5)); // 15")
                ),

                // guided practice
                "<p>Complete both tasks:</p>"
                + "<p><strong>Part 1 — Optional:</strong></p>"
                + "<ol>"
                + "<li>Write <code>findSpell(String name)</code> that returns <code>Optional.of(name)</code> "
                + "if <code>name.equals(\"Fireball\")</code>, otherwise <code>Optional.empty()</code>.</li>"
                + "<li>Print <code>findSpell(\"Fireball\").orElse(\"Spell not found\")</code></li>"
                + "<li>Print <code>findSpell(\"Unknown\").orElse(\"Spell not found\")</code></li>"
                + "</ol>"
                + "<p><strong>Part 2 — Varargs:</strong></p>"
                + "<ol>"
                + "<li>Write <code>sum(int... numbers)</code> that totals all arguments.</li>"
                + "<li>Print <code>sum(1, 2, 3, 4, 5)</code></li>"
                + "</ol>"
                + "<p><strong>Expected output (three lines):</strong></p>"
                + "<pre><code>Fireball\nSpell not found\n15</code></pre>",

                // starter code
                "import java.util.Optional;\n"
                + "\n"
                + "public class OptionalVarargs {\n"
                + "\n"
                + "    static Optional<String> findSpell(String name) {\n"
                + "        // TODO: return Optional.of(name) if name equals \"Fireball\", else Optional.empty()\n"
                + "        return Optional.empty();\n"
                + "    }\n"
                + "\n"
                + "    static int sum(int... numbers) {\n"
                + "        // TODO: total and return all numbers\n"
                + "        return 0;\n"
                + "    }\n"
                + "\n"
                + "    public static void main(String[] args) {\n"
                + "        System.out.println(findSpell(\"Fireball\").orElse(\"Spell not found\"));\n"
                + "        System.out.println(findSpell(\"Unknown\").orElse(\"Spell not found\"));\n"
                + "        System.out.println(sum(1, 2, 3, 4, 5));\n"
                + "    }\n"
                + "}\n",

                // tests
                tests(
                    test("Optional and varargs output", "null", "Fireball\nSpell not found\n15")
                ),

                // feynman prompt
                "What problem does Optional solve in Java? How is it better than returning null from a method?"
        );

        mcQuestion("N4", QuestionTier.RECALL,
                "<p>What does <code>Optional.ofNullable(null).orElse(\"default\")</code> return?</p>",
                new String[]{"null", "default", "Optional.empty()", "throws NullPointerException"},
                "default",
                "<p><code>Optional.ofNullable(null)</code> creates an empty Optional. "
                + "<code>orElse(\"default\")</code> returns the fallback value when the Optional is empty. "
                + "This is precisely why <code>orElse</code> exists — safe extraction without NPE.</p>");

        codeQuestion("N4", QuestionTier.APPLICATION, QuestionType.WHATS_THE_OUTPUT,
                "<p>What does this code print?</p>",
                "static int sum(int... nums) {\n"
                + "    int t = 0;\n"
                + "    for (int n : nums) t += n;\n"
                + "    return t;\n"
                + "}\n"
                + "// in main:\n"
                + "System.out.println(sum(2, 4, 6));",
                "12",
                "<p>The varargs method iterates over the three arguments: 2 + 4 + 6 = 12.</p>");

        tfQuestion("N4", QuestionTier.RECALL,
                "<p>Varargs parameters must be the last parameter in a method's parameter list.</p>",
                "True",
                "<p>Java requires varargs (<code>type... name</code>) to be the final parameter. "
                + "This is because the compiler converts it to an array, "
                + "and there can be only one such variable-length parameter per method.</p>");

        mcQuestion("N4", QuestionTier.DISCRIMINATION,
                "<p>You have an <code>Optional&lt;String&gt; opt</code> that may or may not hold a value. "
                + "Which approach is safest?</p>",
                new String[]{
                    "opt.get()",
                    "opt.orElse(\"fallback\")",
                    "if (opt != null) opt.get()",
                    "opt.toString()"
                },
                "opt.orElse(\"fallback\")",
                "<p><code>opt.get()</code> throws <code>NoSuchElementException</code> if the Optional is empty. "
                + "<code>orElse</code> safely returns a fallback. "
                + "An Optional is never <code>null</code> itself (it's either present or empty), "
                + "so null-checking the Optional is wrong.</p>");
    }
}
