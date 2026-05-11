package com.ambravate.arcane.academy.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.Map;

/**
 * Provides structured, deterministic mentor feedback without any external API calls.
 *
 * Feedback is keyed on:
 *   - Error type (compile, runtime, test failure)
 *   - Common error patterns detected in the error message or code
 *   - Topic of the current quest
 *
 * This replaces the previous Anthropic API integration which was hitting usage limits.
 */
@Service
@Slf4j
public class AiMentorService {

    @Value("${groq.api.key:}")
    private String groqApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private String callGroqApi(String systemPrompt, String userPrompt) {
        if (groqApiKey == null || groqApiKey.trim().isEmpty()) {
            log.info("[Mentor] No Groq API key set, falling back to static mentor.");
            return null;
        }
        try {
            ObjectNode root = objectMapper.createObjectNode();
            root.put("domain", "llama3-8b-8192");
            ArrayNode messages = root.putArray("messages");
            
            ObjectNode systemMsg = objectMapper.createObjectNode();
            systemMsg.put("role", "system");
            systemMsg.put("content", systemPrompt);
            messages.add(systemMsg);
            
            ObjectNode userMsg = objectMapper.createObjectNode();
            userMsg.put("role", "user");
            userMsg.put("content", userPrompt);
            messages.add(userMsg);

            String requestBody = objectMapper.writeValueAsString(root);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                    .header("Authorization", "Bearer " + groqApiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                JsonNode responseJson = objectMapper.readTree(response.body());
                String content = responseJson.path("choices").get(0).path("message").path("content").asText();
                return "Master Velan: \"" + content.replace("\"", "") + "\"";
            } else {
                log.error("[Mentor] Groq API error ({}): {}", response.statusCode(), response.body());
                return null;
            }
        } catch (Exception e) {
            log.error("[Mentor] Failed to call Groq API", e);
            return null;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // COMPILE ERROR FEEDBACK
    // ─────────────────────────────────────────────────────────────────────────

    public String explainCompileError(String questTitle, String topic,
                                       String code, String compilerError) {
        log.info("[Mentor] Compile error for quest='{}' topic='{}' — analysing pattern",
                questTitle, topic);
        log.debug("[Mentor] Compiler error text: {}", compilerError);

        String groqFeedback = callGroqApi(
            "You are Master Velan, a Socratic wizard mentor teaching Java at the Arcane Academy. The student has encountered a compiler error. Give a short, 2-3 sentence magical hint that guides them to the answer without writing code for them.",
            "Quest: " + questTitle + "\nCode:\n" + code + "\nError:\n" + compilerError
        );
        if (groqFeedback != null) return groqFeedback;

        String lower = compilerError == null ? "" : compilerError.toLowerCase();
        String codeLower = code == null ? "" : code.toLowerCase();

        // --- Semicolon missing ---
        if (lower.contains("';' expected") || lower.contains("illegal start of expression")) {
            log.debug("[Mentor] Pattern matched: missing semicolon");
            return "Master Velan examines your spell and sighs gently. " +
                   "\"Every statement in Java must end with a semicolon — that small ';' is the full stop of the language. " +
                   "Check each line of your code carefully: find the one that is missing its semicolon and add it. " +
                   "The compiler cannot proceed without it.\"";
        }

        // --- Missing closing brace ---
        if (lower.contains("reached end of file") || lower.contains("'}' expected")) {
            log.debug("[Mentor] Pattern matched: missing closing brace");
            return "Master Velan peers at your code and counts the curly braces. " +
                   "\"Every opening brace '{' must have a matching closing brace '}'. " +
                   "Count your opening braces and count your closing braces — one of them is missing its partner. " +
                   "Make sure every method body and class body is properly closed.\"";
        }

        // --- Cannot find symbol (variable not declared) ---
        if (lower.contains("cannot find symbol") || lower.contains("symbol:   variable")) {
            log.debug("[Mentor] Pattern matched: cannot find symbol / undeclared variable");
            // Extract the symbol name if possible
            String hint = "";
            if (lower.contains("symbol:")) {
                int idx = lower.indexOf("symbol:");
                String after = compilerError.substring(idx).lines().findFirst().orElse("");
                hint = " I can see the problem involves: " + after.trim() + ".";
            }
            return "Master Velan nods knowingly. " +
                   "\"The compiler cannot find a variable or method you are trying to use." + hint + " " +
                   "This usually means you have either forgotten to declare the variable before using it, " +
                   "or you have spelled its name differently (remember: Java is case-sensitive — 'level' and 'Level' are different things). " +
                   "Check your declarations carefully.\"";
        }

        // --- Type mismatch ---
        if (lower.contains("incompatible types") || lower.contains("cannot convert")) {
            log.debug("[Mentor] Pattern matched: type mismatch");
            return "Master Velan raises an eyebrow. " +
                   "\"You are trying to put something into a container that cannot hold it — a type mismatch. " +
                   "For example, storing a decimal number in an 'int', or text in a numeric variable. " +
                   "Check what type each variable was declared as, and make sure you are assigning a compatible value. " +
                   "A cast like '(int)' or '(double)' may be needed if you are converting between numeric types.\"";
        }

        // --- Class not found / not defined ---
        if (lower.contains("class, interface") || lower.contains("class expected")) {
            log.debug("[Mentor] Pattern matched: class structure error");
            return "Master Velan studies your scroll carefully. " +
                   "\"The compiler expects your code to be inside a proper class structure. " +
                   "Make sure your code is inside a 'public class ClassName { }' block, " +
                   "and that your main method is declared as 'public static void main(String[] args)'. " +
                   "Check for mismatched braces that might be breaking the structure.\"";
        }

        // --- Method not found ---
        if (lower.contains("cannot find symbol") && lower.contains("method")) {
            log.debug("[Mentor] Pattern matched: method not found");
            return "Master Velan strokes his beard. " +
                   "\"You are calling a method that the compiler cannot locate. " +
                   "Check the spelling of the method name — capitalisation matters. " +
                   "If it is your own method, make sure it is declared in the correct class and has the 'static' keyword if you are calling it from main. " +
                   "If it is a built-in method, check you are calling it on the correct type.\"";
        }

        // --- Wrong number of arguments ---
        if (lower.contains("wrong number of arguments") || lower.contains("actual and formal argument")) {
            log.debug("[Mentor] Pattern matched: wrong argument count");
            return "Master Velan counts on his fingers. " +
                   "\"You are calling a method with the wrong number of arguments. " +
                   "Check how many parameters the method expects and make sure you are providing exactly that many values when calling it. " +
                   "Count the commas in your method call and compare with the method definition.\"";
        }

        // --- Generic fallback for compile errors ---
        log.debug("[Mentor] No specific pattern matched — using generic compile error feedback");
        String lineHint = "";
        if (lower.contains("line") || compilerError.contains("Line")) {
            lineHint = " The error message above tells you which line to look at — start there.";
        }
        return "Master Velan looks up from his tome with a patient expression. " +
               "\"Your spell could not be compiled — this means there is a syntax error in your code." + lineHint + " " +
               "Read the error message carefully: it usually names the exact problem. " +
               "Common culprits are: a missing semicolon ';', a missing closing brace '}', " +
               "a variable used before it was declared, or a misspelled keyword. " +
               "Fix one error at a time and try again.\"";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // RUNTIME ERROR FEEDBACK
    // ─────────────────────────────────────────────────────────────────────────

    public String explainRuntimeError(String questTitle, String topic,
                                       String code, String runtimeError) {
        log.info("[Mentor] Runtime error for quest='{}' topic='{}' — analysing pattern",
                questTitle, topic);
        log.debug("[Mentor] Runtime error text: {}", runtimeError);

        String groqFeedback = callGroqApi(
            "You are Master Velan, a Socratic wizard mentor teaching Java at the Arcane Academy. The student has encountered a runtime error. Give a short, 2-3 sentence magical hint that guides them to the answer without writing code for them.",
            "Quest: " + questTitle + "\nCode:\n" + code + "\nError:\n" + runtimeError
        );
        if (groqFeedback != null) return groqFeedback;

        String lower = runtimeError == null ? "" : runtimeError.toLowerCase();

        // --- Division by zero ---
        if (lower.contains("arithmeticexception") || lower.contains("/ by zero")) {
            log.debug("[Mentor] Pattern matched: division by zero");
            return "Master Velan winces. " +
                   "\"Your spell compiled perfectly but collapsed when it ran — you are dividing by zero. " +
                   "In Java, dividing an integer by zero is illegal and causes an immediate crash. " +
                   "Check any division operations in your code and make sure the denominator cannot be zero. " +
                   "If it can be zero, add a check: 'if (divisor != 0)' before dividing.\"";
        }

        // --- Array index out of bounds ---
        if (lower.contains("arrayindexoutofbounds") || lower.contains("index out of bounds")) {
            log.debug("[Mentor] Pattern matched: array index out of bounds");
            return "Master Velan nods — he has seen this one many times. " +
                   "\"You are trying to access an array slot that does not exist. " +
                   "Remember: if an array has 5 elements, valid indices are 0, 1, 2, 3, and 4 — not 5. " +
                   "Check your loop condition: it should use '<' not '<=' when comparing against the array's length. " +
                   "Also check any direct array accesses like arr[i] to make sure i is within bounds.\"";
        }

        // --- Null pointer ---
        if (lower.contains("nullpointerexception") || lower.contains("null pointer")) {
            log.debug("[Mentor] Pattern matched: null pointer exception");
            return "Master Velan raises a cautioning hand. " +
                   "\"A NullPointerException means you are trying to use a variable that has no value — it is null. " +
                   "Check that every variable is properly initialised before you use it. " +
                   "If you declared a variable but never assigned it a value, it starts as null. " +
                   "Make sure objects are created with 'new' before calling methods on them.\"";
        }

        // --- Stack overflow (infinite recursion) ---
        if (lower.contains("stackoverflowerror") || lower.contains("stack overflow")) {
            log.debug("[Mentor] Pattern matched: stack overflow / infinite recursion");
            return "Master Velan looks at your recursive spell with a knowing smile. " +
                   "\"A StackOverflowError means your recursive method called itself so many times it ran out of memory. " +
                   "Every recursive method needs a base case — a condition that stops the recursion before it goes too deep. " +
                   "Check your base case: does it correctly return without making another recursive call? " +
                   "Also check that the recursive call is always moving towards the base case, not away from it.\"";
        }

        // --- Timeout ---
        if (lower.contains("timeout") || lower.contains("infinite loop") || lower.contains("time limit")) {
            log.debug("[Mentor] Pattern matched: timeout / infinite loop");
            return "Master Velan gently stops the clock. " +
                   "\"Your spell ran but never stopped — this usually means an infinite loop. " +
                   "Check any while loops: is the condition variable being changed inside the loop? " +
                   "For a while loop to end, something inside it must eventually make the condition false. " +
                   "Also check for loops: make sure the counter is moving towards the exit condition, not away from it.\"";
        }

        // --- Class cast ---
        if (lower.contains("classcastexception")) {
            log.debug("[Mentor] Pattern matched: class cast exception");
            return "Master Velan examines your transformation spell. " +
                   "\"A ClassCastException means you are trying to convert an object to a type it is not. " +
                   "When casting objects, the object must actually be an instance of the target type. " +
                   "Check your cast operations and make sure the types are compatible.\"";
        }

        // --- Generic runtime fallback ---
        log.debug("[Mentor] No specific runtime pattern matched — using generic feedback");
        return "Master Velan studies the crash report carefully. " +
               "\"Your code compiled successfully but something went wrong at runtime. " +
               "The error message above describes what happened — read it carefully for clues. " +
               "Common runtime problems are: dividing by zero, accessing an array out of its bounds, " +
               "calling a method on a null variable, or an infinite loop or recursion. " +
               "Add a print statement before the crash point to see what values your variables hold.\"";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST FAILURE FEEDBACK  (keyed on topic + which tests failed)
    // ─────────────────────────────────────────────────────────────────────────

    public String getFeedback(String questTitle, String topic, String problem,
                               String code, String failedTests) {
        log.info("[Mentor] Test failure for quest='{}' topic='{}' failedTests='{}'",
                questTitle, topic, failedTests);
        log.debug("[Mentor] Student code length: {} chars", code == null ? 0 : code.length());

        String groqFeedback = callGroqApi(
            "You are Master Velan, a Socratic wizard mentor teaching Java at the Arcane Academy. The student's code failed some tests. Give a short, 2-3 sentence magical hint that guides them to fix the logic, without writing the code for them.",
            "Quest: " + questTitle + "\nTopic: " + topic + "\nCode:\n" + code + "\nFailed Tests:\n" + failedTests
        );
        if (groqFeedback != null) return groqFeedback;

        String topicLower = topic == null ? "" : topic.toLowerCase();
        String failedLower = failedTests == null ? "" : failedTests.toLowerCase();
        String codeLower = code == null ? "" : code.toLowerCase();

        // ── Hello World ──────────────────────────────────────────────────────
        if (topicLower.contains("hello world") || questTitle.toLowerCase().contains("first spell")) {
            log.debug("[Mentor] Topic: Hello World");
            return "Master Velan leans over to look. " +
                   "\"The output does not quite match what is expected. " +
                   "Check the exact text inside your println — every character must match, including capital letters and punctuation. " +
                   "The text 'Welcome to Polymath Academy!' must appear exactly as written, including the exclamation mark.\"";
        }

        // ── Variables ────────────────────────────────────────────────────────
        if (topicLower.contains("variable")) {
            log.debug("[Mentor] Topic: Variables");
            if (failedLower.contains("string") || failedLower.contains("name")) {
                return "Master Velan points to the String declaration. " +
                       "\"Remember that String starts with a capital S, and text values must be wrapped in double quotes. " +
                       "Check your String variable — is it declared as 'String name = \"value\"'?\"";
            }
            if (failedLower.contains("double") || failedLower.contains("mana")) {
                return "Master Velan taps the decimal line. " +
                       "\"A double value should include a decimal point — write '100.0' not '100' if the expected output is '100.0'. " +
                       "Check your double declaration and make sure the value matches exactly.\"";
            }
            return "Master Velan surveys your variables. " +
                   "\"Check each variable is declared with the correct type, the correct name (case matters!), " +
                   "and the correct value. Then make sure you are printing each one with System.out.println().\"";
        }

        // ── Arithmetic ───────────────────────────────────────────────────────
        if (topicLower.contains("arithmetic")) {
            log.debug("[Mentor] Topic: Arithmetic");
            return "Master Velan counts on his fingers. " +
                   "\"The calculation is not producing the right result. " +
                   "Work through it step by step: apply each operation in order and check the intermediate values. " +
                   "Remember the shortcut operators — '+= 5' adds 5, '*= 2' doubles, '-= 3' subtracts 3. " +
                   "Try printing the value after each step to find where it goes wrong.\"";
        }

        // ── Strings / concatenation ──────────────────────────────────────────
        if (topicLower.contains("string")) {
            log.debug("[Mentor] Topic: Strings");
            if (failedLower.contains("length")) {
                return "Master Velan counts carefully. " +
                       "\"The length output is wrong. Remember: name.length() counts every character including spaces. " +
                       "'Aria Voss' has 9 characters (4 + 1 space + 4). Make sure you are calling length() on the full name, not just part of it.\"";
            }
            if (failedLower.contains("upper")) {
                return "Master Velan raises an eyebrow. " +
                       "\"Use toUpperCase() — not toUpper() or upper(). Java method names are case-sensitive and must be spelled exactly.\"";
            }
            return "Master Velan points to the concatenation. " +
                   "\"Check how you are joining the strings. To combine first and last name with a space between: " +
                   "firstName + \" \" + lastName. Make sure the space is inside its own set of quotes.\"";
        }

        // ── Booleans / comparisons ───────────────────────────────────────────
        if (topicLower.contains("boolean") || topicLower.contains("comparison")) {
            log.debug("[Mentor] Topic: Booleans/Comparisons");
            if (codeLower.contains("= true") && !codeLower.contains("== true")) {
                return "Master Velan spots the issue immediately. " +
                       "\"You may be confusing '=' with '=='. A single equals sign assigns a value. " +
                       "Two equals signs '==' check equality. For example: 'boolean a = (10 > 5)' not 'boolean a = 10 > 5 = true'.\"";
            }
            return "Master Velan lights up the truth crystals. " +
                   "\"Check your comparison operators carefully. '>' is greater than, '>=' is greater than or equal, '==' is equal to (two equals signs!), '!=' is not equal. " +
                   "For AND use '&&', for OR use '||'. Make sure each boolean is declared and printed separately.\"";
        }

        // ── Casting / doubles ────────────────────────────────────────────────
        if (topicLower.contains("cast") || topicLower.contains("double")) {
            log.debug("[Mentor] Topic: Casting");
            if (failedLower.contains("3.5") || failedLower.contains("decimal")) {
                return "Master Velan measures precisely. " +
                       "\"To get a decimal result from integer division, cast one value to double first: (double) total / portions. " +
                       "If both numbers are ints and you divide them, Java drops the decimal entirely.\"";
            }
            if (failedLower.contains("3.1") || failedLower.contains("printf")) {
                return "Master Velan reaches for the printf scroll. " +
                       "\"Use System.out.printf(\\\"%.1f%n\\\", value) for one decimal place. " +
                       "The '%.1f' means decimal format with 1 digit after the point, and '%n' adds a newline.\"";
            }
            return "Master Velan balances the scales. " +
                   "\"Check your casting. To convert an int to double: (double) value. To convert a double to int: (int) value — this drops the decimal, it does not round. " +
                   "For printf formatting: System.out.printf(\\\"%.1f%n\\\", value) gives one decimal place.\"";
        }

        // ── If/Else ──────────────────────────────────────────────────────────
        if (topicLower.contains("if") || topicLower.contains("else")) {
            log.debug("[Mentor] Topic: If/Else");
            return "Master Velan checks the bridge conditions. " +
                   "\"One of your conditions is not matching correctly. " +
                   "Check the order: the 'coins > 50' check must come first. If you check '>= 10' first, " +
                   "it will catch values above 50 as well. Also check your comparison operators — '>' not '>=', '>=' not '>'.\"";
        }

        // ── Switch ───────────────────────────────────────────────────────────
        if (topicLower.contains("switch")) {
            log.debug("[Mentor] Topic: Switch");
            if (!codeLower.contains("break")) {
                return "Master Velan points firmly at the switch. " +
                       "\"You are missing 'break;' statements. Without a break at the end of each case, " +
                       "Java falls through into the next case and executes that too. Add 'break;' after each println.\"";
            }
            return "Master Velan studies the sigil. " +
                   "\"Check your case values match exactly — String comparisons are case-sensitive. " +
                   "Make sure you have a 'default:' case at the end, and that every case has a 'break;' statement.\"";
        }

        // ── While loops ──────────────────────────────────────────────────────
        if (topicLower.contains("while")) {
            log.debug("[Mentor] Topic: While loop");
            if (!codeLower.contains("count++") && !codeLower.contains("count +=") && !codeLower.contains("count=count")) {
                return "Master Velan checks the clock mechanism. " +
                       "\"Your loop may never stop. Inside a while loop, the condition variable must change — " +
                       "otherwise the condition stays true forever (infinite loop). " +
                       "Add 'count++' inside your loop to increment the counter each time.\"";
            }
            return "Master Velan listens to the ticks. " +
                   "\"Check your while condition: 'while (count <= 5)' runs for 1, 2, 3, 4, 5. " +
                   "Make sure you start at 1, not 0, and that you print 'Clock resting.' after the loop ends (outside the braces).\"";
        }

        // ── For loops ────────────────────────────────────────────────────────
        if (topicLower.contains("for loop") || topicLower.contains("for loops")) {
            log.debug("[Mentor] Topic: For loop");
            return "Master Velan counts the echoes. " +
                   "\"Check your for loop: 'for (int i = 1; i <= 5; i++)' runs for i = 1, 2, 3, 4, 5. " +
                   "Inside the loop, 'Echo ' + i gives you the numbered output. " +
                   "The 'Tower unlocked.' line must be after the closing brace of the loop, not inside it.\"";
        }

        // ── Nested loops ─────────────────────────────────────────────────────
        if (topicLower.contains("nested")) {
            log.debug("[Mentor] Topic: Nested loops");
            return "Master Velan maps the labyrinth. " +
                   "\"In nested loops, the inner loop must run completely for each iteration of the outer loop. " +
                   "Structure: outer loop over i (rows 1-3), inner loop over j (columns 1-3), " +
                   "print 'Room ' + i + '-' + j. Make sure the inner loop's braces are inside the outer loop's braces.\"";
        }

        // ── Arrays ───────────────────────────────────────────────────────────
        if (topicLower.contains("array") && !topicLower.contains("arraylist")) {
            log.debug("[Mentor] Topic: Arrays");
            if (failedLower.contains("total")) {
                return "Master Velan counts the vials. " +
                       "\"The 'Total: 5' line is not appearing. This should be printed after the loop ends — outside the loop's closing brace. " +
                       "Check that your print statement is not accidentally inside the loop.\"";
            }
            return "Master Velan examines the shelf. " +
                   "\"Check your array declaration syntax: String[] ingredients = {\\\"Moonpetal\\\", ...}. " +
                   "Then loop from i = 0 to i < ingredients.length (not <=), printing ingredients[i] each time.\"";
        }

        // ── ArrayList ────────────────────────────────────────────────────────
        if (topicLower.contains("arraylist") || topicLower.contains("list")) {
            log.debug("[Mentor] Topic: ArrayList");
            if (!codeLower.contains("import java.util.arraylist")) {
                return "Master Velan checks the import scrolls. " +
                       "\"ArrayList requires an import at the top of your file: import java.util.ArrayList; " +
                       "Without this, Java does not know what ArrayList is.\"";
            }
            return "Master Velan unrolls the scroll. " +
                   "\"Check: declare with 'ArrayList<String> graduates = new ArrayList<>();', " +
                   "add items with '.add()', get the size with '.size()', and access items with '.get(i)'. " +
                   "Make sure you print the size before looping through the names.\"";
        }

        // ── Methods ──────────────────────────────────────────────────────────
        if (topicLower.contains("method")) {
            log.debug("[Mentor] Topic: Methods");
            if (!codeLower.contains("static")) {
                return "Master Velan checks the method signatures. " +
                       "\"Methods called from main need the 'static' keyword. " +
                       "Declare them as: 'static void greetWizard(...)' and 'static int add(...)'.\"";
            }
            if (failedLower.contains("sum") || failedLower.contains("add")) {
                return "Master Velan examines the add method. " +
                       "\"The add method must return a value — use 'return a + b;' as the last line. " +
                       "The return type must be 'int', not 'void'. Then print the result: System.out.println(add(12, 8)).\"";
            }
            return "Master Velan reviews the Codex entry. " +
                   "\"Check two things: the greeting method should print the exact format 'Welcome, Kael! Level 7.' " +
                   "and the add method should have 'return a + b;' and return type 'int'.\"";
        }

        // ── Recursion ────────────────────────────────────────────────────────
        if (topicLower.contains("recursion")) {
            log.debug("[Mentor] Topic: Recursion");
            return "Master Velan consults the Obelisk. " +
                   "\"Every recursive method needs two parts: a base case that stops the recursion, " +
                   "and a recursive case that calls the method again with a smaller value. " +
                   "For factorial: 'if (n <= 1) return 1;' is the base case, " +
                   "and 'return n * factorial(n - 1);' is the recursive case.\"";
        }

        // ── OOP — Classes ────────────────────────────────────────────────────
        if (topicLower.contains("class") || topicLower.contains("object")) {
            log.debug("[Mentor] Topic: Classes/Objects");
            return "Master Velan inspects the blueprint. " +
                   "\"Check your class structure: fields declared at the top, constructor using 'this.name = name' to set them, " +
                   "and the describe() method printing the correct format. " +
                   "Then create objects with 'new Wizard(\\\"Aldric\\\", 5)' and call describe() on each.\"";
        }

        // ── Encapsulation ────────────────────────────────────────────────────
        if (topicLower.contains("encapsulation")) {
            log.debug("[Mentor] Topic: Encapsulation");
            return "Master Velan seals the vault. " +
                   "\"Check: fields must be 'private'. Getters return the field value. " +
                   "The setLevel setter must check 'if (level > 0)' before setting — negative values should be silently ignored. " +
                   "After calling setLevel(-1), the level should remain at 5.\"";
        }

        // ── Inheritance ──────────────────────────────────────────────────────
        if (topicLower.contains("inheritance")) {
            log.debug("[Mentor] Topic: Inheritance");
            return "Master Velan reads the lineage scroll. " +
                   "\"Check: 'class BattleMage extends Wizard', constructor calls 'super(name, level)' first, " +
                   "then sets 'this.weapon = weapon'. The @Override describe() should print: " +
                   "'BattleMage ' + name + ' wields ' + weapon + '.'.\"";
        }

        // ── Polymorphism ─────────────────────────────────────────────────────
        if (topicLower.contains("polymorphism")) {
            log.debug("[Mentor] Topic: Polymorphism");
            return "Master Velan surveys the mirrors. " +
                   "\"Declare the array as 'Wizard[] gallery = { new Wizard(...), new BattleMage(...), new Healer(...) }'. " +
                   "Loop with 'gallery[i].describe()' — Java will call the correct version automatically based on the actual object type.\"";
        }

        // ── Abstract classes ─────────────────────────────────────────────────
        if (topicLower.contains("abstract")) {
            log.debug("[Mentor] Topic: Abstract classes");
            return "Master Velan consults the Sanctum blueprints. " +
                   "\"Check: 'abstract class Shape { abstract double area(); }', " +
                   "Circle overrides area() with 'return Math.PI * radius * radius', " +
                   "Rectangle with 'return width * height'. " +
                   "Round the circle result: Math.round(circle.area() * 10) / 10.0.\"";
        }

        // ── Exceptions ───────────────────────────────────────────────────────
        if (topicLower.contains("exception")) {
            log.debug("[Mentor] Topic: Exception handling");
            return "Master Velan monitors the ward. " +
                   "\"Structure: try { System.out.println(10/0); } catch (ArithmeticException e) { System.out.println(\\\"Caught: \\\" + e.getMessage()); } finally { System.out.println(\\\"Ward stable.\\\"); }. " +
                   "The finally block runs regardless of whether an exception occurred.\"";
        }

        // ── Generics ─────────────────────────────────────────────────────────
        if (topicLower.contains("generics") || topicLower.contains("generic")) {
            log.debug("[Mentor] Topic: Generics");
            return "Master Velan examines the forge. " +
                   "\"Your Box<T> class needs: 'private T item', 'public void put(T item) { this.item = item; }', " +
                   "and 'public T get() { return item; }'. " +
                   "Use it as: 'Box<String> box = new Box<>(); box.put(\\\"value\\\"); System.out.println(box.get());'.\"";
        }

        // ── Lambdas ──────────────────────────────────────────────────────────
        if (topicLower.contains("lambda")) {
            log.debug("[Mentor] Topic: Lambdas");
            if (failedLower.contains("predicate") || failedLower.contains("true")) {
                return "Master Velan threads the loom. " +
                       "\"For the Predicate: 'Predicate<Integer> isEven = n -> n % 2 == 0;' then call 'isEven.test(4)'. " +
                       "The modulo operator % gives the remainder — if n % 2 == 0, the number is even.\"";
            }
            return "Master Velan weaves three threads. " +
                   "\"Check each lambda: Runnable r = () -> System.out.println(\\\"Loom activated.\\\"); r.run(). " +
                   "Predicate<Integer> isEven = n -> n % 2 == 0; isEven.test(4). " +
                   "Function<Integer,Integer> doubler = n -> n * 2; doubler.apply(7).\"";
        }

        // ── Streams ──────────────────────────────────────────────────────────
        if (topicLower.contains("stream")) {
            log.debug("[Mentor] Topic: Streams");
            if (failedLower.contains("count")) {
                return "Master Velan traces the conduit. " +
                       "\"For the count, use a separate stream pipeline with a terminal .count() operation: " +
                       "numbers.stream().filter(n -> n % 2 == 0).count(). " +
                       "Then print the result with System.out.println().\"";
            }
            return "Master Velan opens the conduit. " +
                   "\"Chain operations: numbers.stream().filter(n -> n % 2 == 0).map(n -> n * 2).forEach(System.out::println). " +
                   "This filters even numbers, doubles each one, and prints them all.\"";
        }

        // ── Design Patterns ──────────────────────────────────────────────────
        if (topicLower.contains("pattern") || topicLower.contains("singleton") || topicLower.contains("builder")) {
            log.debug("[Mentor] Topic: Design patterns");
            if (failedLower.contains("same instance") || failedLower.contains("singleton")) {
                return "Master Velan unseals the Archive. " +
                       "\"For Singleton: private constructor, private static Registry instance, " +
                       "public static Registry getInstance() { if (instance == null) instance = new Registry(); return instance; }. " +
                       "Calling it twice must return the same object (r1 == r2 is true).\"";
            }
            return "Master Velan consults both pattern entries. " +
                   "\"For Builder: inner static class Builder with 'name', 'level' fields, " +
                   "chainable methods that return 'this', and a build() method that creates a new Wizard. " +
                   "Then: new Wizard.Builder().name(\\\"Aldric\\\").level(7).build().\"";
        }

        // ── Capstone / Project ───────────────────────────────────────────────
        if (topicLower.contains("capstone") || topicLower.contains("task")) {
            log.debug("[Mentor] Topic: Capstone");
            if (failedLower.contains("[x]") || failedLower.contains("complete")) {
                return "Master Velan reviews the completed tasks. " +
                       "\"Make sure completeTask() finds the task by title using .equals() (not ==) and calls setDone(true). " +
                       "String comparison with == checks memory address, not content — always use .equals() for Strings.\"";
            }
            if (failedLower.contains("progress") || failedLower.contains("percent")) {
                return "Master Velan calculates the percentage. " +
                       "\"For the percentage: (double) getCompletedCount() / tasks.size() * 100. " +
                       "Cast to double first, otherwise integer division will cut off the decimal. " +
                       "Use System.out.printf(\\\"Completed: %d | Pending: %d | Progress: %.1f%%%n\\\", c, p, pct).\"";
            }
            return "Master Velan checks the integration. " +
                   "\"Make sure your Task class has a proper display() method, " +
                   "TaskManager uses .equals() for string comparison in completeTask(), " +
                   "and printStats() uses printf with %.1f for the percentage.\"";
        }

        // ── Interview / FizzBuzz ─────────────────────────────────────────────
        if (topicLower.contains("fizzbuzz") || topicLower.contains("interview")) {
            log.debug("[Mentor] Topic: FizzBuzz/Interview");
            if (failedLower.contains("fizzbuzz") && codeLower.contains("% 3") && !codeLower.contains("&&")) {
                return "Assessor Vorn taps the paper. " +
                       "\"The combined FizzBuzz check must come first. Check '% 3 == 0 && % 5 == 0' before the individual checks — " +
                       "if you check % 3 alone first, 15 will print 'Fizz' and never reach the FizzBuzz branch.\"";
            }
            return "Assessor Vorn checks the output carefully. " +
                   "\"Order matters: check FizzBuzz first (i % 3 == 0 && i % 5 == 0), then Fizz (i % 3 == 0), " +
                   "then Buzz (i % 5 == 0), then print i as the else case. " +
                   "Loop from i = 1 to i <= 20.\"";
        }

        // ── Palindrome ───────────────────────────────────────────────────────
        if (topicLower.contains("palindrome")) {
            log.debug("[Mentor] Topic: Palindrome");
            return "Assessor Vorn examines the method. " +
                   "\"Reverse the string and compare: new StringBuilder(s).reverse().toString(). " +
                   "Then return s.equals(reversed) — use .equals() not == for String comparison.\"";
        }

        // ── HashMap / Word count ─────────────────────────────────────────────
        if (topicLower.contains("hashmap") || topicLower.contains("word")) {
            log.debug("[Mentor] Topic: HashMap");
            return "Assessor Vorn points to the map. " +
                   "\"Split the sentence: String[] words = sentence.split(\\\" \\\"). " +
                   "For each word: counts.put(word, counts.getOrDefault(word, 0) + 1). " +
                   "getOrDefault returns 0 if the word has not been seen yet. " +
                   "Then print with: counts.forEach((w, c) -> System.out.println(w + \\\": \\\" + c)).\"";
        }

        // ── Generic fallback ─────────────────────────────────────────────────
        log.debug("[Mentor] No topic-specific pattern matched — using generic test failure feedback");
        return "Master Velan studies the failed tests. " +
               "\"One or more test cases did not produce the expected output. " +
               "Compare your output carefully against what is expected — check for extra spaces, wrong capitalisation, or a missing character. " +
               "Add a print statement to show each variable's value and trace through your logic step by step.\"";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TOPIC HINT  (optional — not used for error feedback, but can be called
    // from a future "hint" button)
    // ─────────────────────────────────────────────────────────────────────────

    public String getTopicHint(String topic) {
        log.info("[Mentor] Topic hint requested for: {}", topic);

        Map<String, String> hints = Map.ofEntries(
            Map.entry("hello world",     "Use System.out.println(\"your message\"); — don't forget the semicolon."),
            Map.entry("variables",       "Declare as: type name = value;  e.g. int level = 5;"),
            Map.entry("arithmetic",      "Shortcuts: x += 5 adds 5, x *= 2 doubles, x++ adds 1."),
            Map.entry("string",          "Join strings with +.  Call methods with a dot: name.length(), name.toUpperCase()."),
            Map.entry("boolean",         "== checks equality (two equals). = assigns a value (one equals). Use && for AND, || for OR."),
            Map.entry("casting",         "(double) value converts to decimal. (int) value truncates the decimal."),
            Map.entry("if",              "if (condition) { } else if (condition) { } else { }"),
            Map.entry("switch",          "switch(var) { case \"value\": ... break; default: ... }"),
            Map.entry("while",           "while (condition) { ... update condition ... }"),
            Map.entry("for",             "for (int i = 0; i < n; i++) { ... }"),
            Map.entry("array",           "String[] arr = {\"a\",\"b\"}; Access with arr[0]. Length: arr.length"),
            Map.entry("arraylist",       "ArrayList<String> list = new ArrayList<>();  list.add(x); list.get(i); list.size()"),
            Map.entry("method",          "static returnType methodName(params) { ... return value; }"),
            Map.entry("recursion",       "Base case stops it. Recursive case calls itself with a smaller value.")
        );

        String topicLower = topic == null ? "" : topic.toLowerCase();
        return hints.entrySet().stream()
                .filter(e -> topicLower.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("Break the problem down step by step. Read the worked example again carefully.");
    }
}
