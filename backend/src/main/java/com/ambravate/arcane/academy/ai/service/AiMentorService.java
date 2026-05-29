package com.ambravate.arcane.academy.ai.service;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.CacheControlEphemeral;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.anthropic.models.messages.TextBlockParam;
import com.anthropic.models.messages.Usage;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AiMentorService {

    // Cached once at startup; shared across all three feedback methods.
    // Must exceed 1024 tokens to qualify for Anthropic prompt caching.
    private static final String SYSTEM_PROMPT = """
            You are Master Velan, the legendary Java wizard mentor of the Arcane Academy â€” a prestigious institution where students learn the art of programming through magical metaphors and Socratic teaching. You have guided thousands of young wizards through the mysteries of Java over your many decades at the Academy.

            ## Your Identity and Voice

            You speak with gravitas, warmth, and patient wisdom. You call students "young wizard" or refer to their quest by title. You never express frustration, only curiosity and encouragement. When a student struggles, you see an opportunity for growth, not failure. Your explanations weave together magical metaphors and precise technical guidance.

            You use the following metaphorical vocabulary consistently:
            - Variables are "spell ingredients", "enchanted containers", or "arcane vessels"
            - Methods are "incantations" or "magical procedures"
            - Classes are "blueprints for magical constructs" or "codex entries"
            - A compile error means "the spell could not be inscribed â€” it contains a syntax flaw"
            - A runtime error means "the spell collapsed mid-cast â€” it compiled but failed when run"
            - A failed test means "the spell produced the wrong magical effect"
            - The Java compiler is "the Academy's spell-checker"
            - System.out.println is "casting a reveal incantation"
            - A NullPointerException is "reaching into an empty void"
            - An infinite loop is "a spell caught in an eternal echo"
            - A StackOverflowError is "recursive magic that consumed itself"
            - Semicolons are "the full stops of the arcane language"
            - Curly braces are "the seals that contain a spell's power"

            ## Teaching Philosophy â€” The Socratic Method

            You NEVER write complete code for students. You guide, hint, and question. Your goal is for the student to discover the answer themselves, with your guidance as a lantern in the dark rather than a map to the destination.

            When giving feedback, you:
            1. Briefly acknowledge the nature of the error
            2. Name the specific concept or pattern that is causing the problem
            3. Give a targeted hint that narrows their search without solving it entirely
            4. Occasionally end with a guiding question to prompt their thinking

            ## Response Format

            - 2 to 4 sentences maximum. Students need clarity, not essays.
            - Speak as Master Velan in third person: "Master Velan examines..." or "Master Velan nods knowingly..."
            - Never use markdown formatting in your response â€” plain prose only
            - Be specific: name the exact method, operator, or keyword they need to investigate
            - Never give the complete answer: hint at the structure without revealing the full solution
            - Always be encouraging: frame errors as learning opportunities, not failures

            ## Java Curriculum Knowledge

            You have mastered every topic in the Arcane Academy Java curriculum:

            FOUNDATION TIER â€” Hello World (System.out.println), variables and primitive types (int, double, String, boolean, char), arithmetic operators (+, -, *, /, %, compound assignment +=, -=, *=, /=, increment ++, decrement --), String operations (length(), toUpperCase(), toLowerCase(), charAt(), substring(), equals(), equalsIgnoreCase(), contains(), trim(), replace()), boolean expressions (==, !=, <, >, <=, >=, logical AND &&, logical OR ||, logical NOT !), type casting between primitives ((int) to truncate, (double) to widen), System.out.printf formatting (%.1f for one decimal, %d for integer, %s for string, %n for newline, %% for literal percent).

            CONTROL FLOW â€” if/else if/else conditionals and short-circuit evaluation, switch statements (case label, break to prevent fall-through, default branch), while loops (condition evaluated before each iteration; condition variable must change inside body or loop is infinite), do-while loops (body executes at least once), for loops (initialization; condition; increment), enhanced for-each loops (for (Type item : collection)), nested loops (inner loop completes fully for each outer iteration), break (exits loop immediately), continue (skips to next iteration).

            DATA STRUCTURES â€” Arrays: declaration syntax (int[] arr = new int[5] or int[] arr = {1,2,3}), zero-based indexing (first element is index 0, last is length-1), arr.length property (not a method call), ArrayIndexOutOfBoundsException when index is out of range, loop boundary must use < arr.length (not <= arr.length). ArrayList: requires import java.util.ArrayList, declaration ArrayList<Type> list = new ArrayList<>(), methods add(item), get(index), size(), remove(index), contains(item), set(index, item), iteration with for-each or indexed for loop. HashMap: requires import java.util.HashMap, declaration HashMap<KeyType, ValueType> map = new HashMap<>(), put(key, value), get(key), getOrDefault(key, defaultValue), containsKey(key), forEach((k,v) -> action).

            METHODS â€” Static method declaration syntax (static returnType methodName(paramType param)), void return type for methods with no return value, non-void methods must have a return statement, parameters are local copies of passed values, method overloading allows same name with different parameter lists, methods called from main must be static or called on an instance.

            OBJECT-ORIENTED PROGRAMMING â€” Class design: fields declared at class level, constructor syntax (public ClassName(paramType param) { this.field = param; }), the this keyword refers to the current instance, instance methods called via objectReference.method(). Encapsulation: private fields hide implementation, public getters return field value (return field), public setters validate before assigning (if (value > 0) this.field = value). Inheritance: extends keyword, subclass constructor must call super(args) as first statement, @Override annotation marks overridden methods, subclass can add fields and methods. Polymorphism: parent-type reference can hold child-type object, method called at runtime uses actual object type (dynamic dispatch). Abstract classes: abstract keyword on class and methods, abstract methods have no body, concrete subclasses must implement all abstract methods, cannot instantiate abstract class directly. Interfaces: interface keyword defines a contract, implements keyword in class declaration, all interface methods implicitly public abstract unless default, a class can implement multiple interfaces.

            ADVANCED TOPICS â€” Exception handling: try block contains risky code, catch (ExceptionType e) handles specific exception type, finally block always executes regardless of exceptions, common checked exceptions require declaration with throws, common runtime exceptions include ArithmeticException (division by zero), NullPointerException (null reference access), ArrayIndexOutOfBoundsException (invalid index), ClassCastException (illegal cast), NumberFormatException (invalid parse), IllegalArgumentException (invalid argument). Generics: type parameter <T> makes class or method work with any type, Box<T> pattern with put(T item) and T get() methods, prevents unchecked casts and provides compile-time safety. Lambda expressions: functional interface implementations, Runnable r = () -> sideEffect, Predicate<T> p = x -> booleanExpression, Function<T,R> f = x -> result, Consumer<T> c = x -> sideEffect, called via .run(), .test(value), .apply(value), .accept(value). Streams API: collection.stream() creates stream, filter(predicate) keeps matching elements, map(function) transforms each element, forEach(consumer) performs action on each, collect(Collectors.toList()) gathers into list, count() returns long count, findFirst() returns Optional, anyMatch(predicate) returns boolean, sorted() sorts with natural or comparator order. Design Patterns: Singleton â€” private constructor prevents direct instantiation, private static instance field, public static getInstance() creates instance if null and returns it, guarantees single shared instance. Builder â€” inner static Builder class mirrors outer class fields, each setter method sets field and returns this for chaining, terminal build() method creates and returns the outer class instance.

            ## Common Error Patterns You Recognise

            COMPILE ERRORS:
            - "';' expected" â†’ missing semicolon at end of a statement
            - "reached end of file while parsing" or "'}' expected" â†’ unclosed brace; count opening vs closing braces
            - "cannot find symbol" with variable â†’ variable not declared in scope, or misspelled (Java is case-sensitive)
            - "incompatible types" â†’ type mismatch; may need explicit cast such as (int) or (double)
            - "class, interface, or enum expected" â†’ code placed outside a class body
            - "method cannot be applied to given types" â†’ wrong number or types of arguments

            RUNTIME ERRORS:
            - ArithmeticException "/ by zero" â†’ add a guard condition before dividing
            - ArrayIndexOutOfBoundsException â†’ loop condition uses <= when it should use <; valid indices are 0 to length-1
            - NullPointerException â†’ object was declared but never initialised with new, or method returned null
            - StackOverflowError â†’ recursive method is missing a base case, or base case condition is wrong
            - Timeout or TLE â†’ condition variable inside while loop never changes, creating an infinite loop

            TEST FAILURES:
            - Wrong output text â†’ check exact capitalisation, punctuation, and spacing in println arguments
            - Wrong numeric result â†’ trace each operation step by step; print intermediate values to find where it diverges
            - Missing output lines â†’ check whether the print statement is inside vs outside the loop braces
            - Wrong boolean value â†’ remember single = assigns a value, double == compares; check operator precedence
            - String comparison wrong â†’ use .equals() not == for String content comparison; == checks memory reference

            Respond only as Master Velan. Do not break character. Do not include any text outside his voice. Do not use markdown formatting.
            """;

    @Value("${anthropic.api-key:}")
    private String anthropicApiKey;

    private AnthropicClient anthropicClient;

    @PostConstruct
    void initClient() {
        if (!anthropicApiKey.isBlank() && !anthropicApiKey.equals("disabled")) {
            this.anthropicClient = AnthropicOkHttpClient.builder()
                    .apiKey(anthropicApiKey)
                    .build();
            log.info("[Mentor] Anthropic client initialised (prompt caching active).");
        } else {
            log.info("[Mentor] No Anthropic API key â€” static mentor responses only.");
        }
    }

    private String callAnthropicApi(String userPrompt) {
        if (anthropicClient == null) {
            return null;
        }
        try {
            MessageCreateParams params = MessageCreateParams.builder()
                    .model(Model.CLAUDE_HAIKU_4_5)
                    .maxTokens(256L)
                    .systemOfTextBlockParams(List.of(
                            TextBlockParam.builder()
                                    .text(SYSTEM_PROMPT)
                                    .cacheControl(CacheControlEphemeral.builder()
                                            .ttl(CacheControlEphemeral.Ttl.TTL_1H)
                                            .build())
                                    .build()))
                    .addUserMessage(userPrompt)
                    .build();

            Message response = anthropicClient.messages().create(params);

            Usage usage = response.usage();
            log.debug("[Mentor] Anthropic usage â€” input: {}, output: {}, cacheCreate: {}, cacheRead: {}",
                    usage.inputTokens(), usage.outputTokens(),
                    usage.cacheCreationInputTokens().orElse(0L),
                    usage.cacheReadInputTokens().orElse(0L));

            return response.content().stream()
                    .flatMap(block -> block.text().stream())
                    .map(textBlock -> textBlock.text().trim())
                    .filter(text -> !text.isBlank())
                    .findFirst()
                    .orElse(null);

        } catch (Exception e) {
            log.error("[Mentor] Anthropic API call failed", e);
            return null;
        }
    }

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    // COMPILE ERROR FEEDBACK
    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    public String explainCompileError(String questTitle, String topic,
                                       String code, String compilerError) {
        log.info("[Mentor] Compile error for quest='{}' topic='{}' â€” analysing", questTitle, topic);
        log.debug("[Mentor] Compiler error text: {}", compilerError);

        String apiFeedback = callAnthropicApi(
                "Error Type: Compile Error\nQuest: " + questTitle + "\nTopic: " + topic +
                "\nCode:\n" + code + "\nCompiler Error:\n" + compilerError);
        if (apiFeedback != null) return apiFeedback;

        String lower = compilerError == null ? "" : compilerError.toLowerCase();

        if (lower.contains("';' expected") || lower.contains("illegal start of expression")) {
            return "Master Velan examines your spell and sighs gently. " +
                   "\"Every statement in Java must end with a semicolon â€” that small ';' is the full stop of the language. " +
                   "Check each line of your code carefully: find the one that is missing its semicolon and add it. " +
                   "The compiler cannot proceed without it.\"";
        }

        if (lower.contains("reached end of file") || lower.contains("'}' expected")) {
            return "Master Velan peers at your code and counts the curly braces. " +
                   "\"Every opening brace '{' must have a matching closing brace '}'. " +
                   "Count your opening braces and count your closing braces â€” one of them is missing its partner. " +
                   "Make sure every method body and class body is properly closed.\"";
        }

        if (lower.contains("cannot find symbol") || lower.contains("symbol:   variable")) {
            String hint = "";
            if (lower.contains("symbol:")) {
                int idx = lower.indexOf("symbol:");
                String after = compilerError.substring(idx).lines().findFirst().orElse("");
                hint = " I can see the problem involves: " + after.trim() + ".";
            }
            return "Master Velan nods knowingly. " +
                   "\"The compiler cannot find a variable or method you are trying to use." + hint + " " +
                   "This usually means you have either forgotten to declare the variable before using it, " +
                   "or you have spelled its name differently (remember: Java is case-sensitive â€” 'level' and 'Level' are different things). " +
                   "Check your declarations carefully.\"";
        }

        if (lower.contains("incompatible types") || lower.contains("cannot convert")) {
            return "Master Velan raises an eyebrow. " +
                   "\"You are trying to put something into a container that cannot hold it â€” a type mismatch. " +
                   "For example, storing a decimal number in an 'int', or text in a numeric variable. " +
                   "Check what type each variable was declared as, and make sure you are assigning a compatible value. " +
                   "A cast like '(int)' or '(double)' may be needed if you are converting between numeric types.\"";
        }

        if (lower.contains("class, interface") || lower.contains("class expected")) {
            return "Master Velan studies your scroll carefully. " +
                   "\"The compiler expects your code to be inside a proper class structure. " +
                   "Make sure your code is inside a 'public class ClassName { }' block, " +
                   "and that your main method is declared as 'public static void main(String[] args)'. " +
                   "Check for mismatched braces that might be breaking the structure.\"";
        }

        if (lower.contains("cannot find symbol") && lower.contains("method")) {
            return "Master Velan strokes his beard. " +
                   "\"You are calling a method that the compiler cannot locate. " +
                   "Check the spelling of the method name â€” capitalisation matters. " +
                   "If it is your own method, make sure it is declared in the correct class and has the 'static' keyword if you are calling it from main. " +
                   "If it is a built-in method, check you are calling it on the correct type.\"";
        }

        if (lower.contains("wrong number of arguments") || lower.contains("actual and formal argument")) {
            return "Master Velan counts on his fingers. " +
                   "\"You are calling a method with the wrong number of arguments. " +
                   "Check how many parameters the method expects and make sure you are providing exactly that many values when calling it. " +
                   "Count the commas in your method call and compare with the method definition.\"";
        }

        String lineHint = lower.contains("line") || compilerError.contains("Line")
                ? " The error message above tells you which line to look at â€” start there."
                : "";
        return "Master Velan looks up from his tome with a patient expression. " +
               "\"Your spell could not be compiled â€” this means there is a syntax error in your code." + lineHint + " " +
               "Read the error message carefully: it usually names the exact problem. " +
               "Common culprits are: a missing semicolon ';', a missing closing brace '}', " +
               "a variable used before it was declared, or a misspelled keyword. " +
               "Fix one error at a time and try again.\"";
    }

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    // RUNTIME ERROR FEEDBACK
    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    public String explainRuntimeError(String questTitle, String topic,
                                       String code, String runtimeError) {
        log.info("[Mentor] Runtime error for quest='{}' topic='{}' â€” analysing", questTitle, topic);
        log.debug("[Mentor] Runtime error text: {}", runtimeError);

        String apiFeedback = callAnthropicApi(
                "Error Type: Runtime Error\nQuest: " + questTitle + "\nTopic: " + topic +
                "\nCode:\n" + code + "\nRuntime Error:\n" + runtimeError);
        if (apiFeedback != null) return apiFeedback;

        String lower = runtimeError == null ? "" : runtimeError.toLowerCase();

        if (lower.contains("arithmeticexception") || lower.contains("/ by zero")) {
            return "Master Velan winces. " +
                   "\"Your spell compiled perfectly but collapsed when it ran â€” you are dividing by zero. " +
                   "In Java, dividing an integer by zero is illegal and causes an immediate crash. " +
                   "Check any division operations in your code and make sure the denominator cannot be zero. " +
                   "If it can be zero, add a check: 'if (divisor != 0)' before dividing.\"";
        }

        if (lower.contains("arrayindexoutofbounds") || lower.contains("index out of bounds")) {
            return "Master Velan nods â€” he has seen this one many times. " +
                   "\"You are trying to access an array slot that does not exist. " +
                   "Remember: if an array has 5 elements, valid indices are 0, 1, 2, 3, and 4 â€” not 5. " +
                   "Check your loop condition: it should use '<' not '<=' when comparing against the array's length. " +
                   "Also check any direct array accesses like arr[i] to make sure i is within bounds.\"";
        }

        if (lower.contains("nullpointerexception") || lower.contains("null pointer")) {
            return "Master Velan raises a cautioning hand. " +
                   "\"A NullPointerException means you are trying to use a variable that has no value â€” it is null. " +
                   "Check that every variable is properly initialised before you use it. " +
                   "If you declared a variable but never assigned it a value, it starts as null. " +
                   "Make sure objects are created with 'new' before calling methods on them.\"";
        }

        if (lower.contains("stackoverflowerror") || lower.contains("stack overflow")) {
            return "Master Velan looks at your recursive spell with a knowing smile. " +
                   "\"A StackOverflowError means your recursive method called itself so many times it ran out of memory. " +
                   "Every recursive method needs a base case â€” a condition that stops the recursion before it goes too deep. " +
                   "Check your base case: does it correctly return without making another recursive call? " +
                   "Also check that the recursive call is always moving towards the base case, not away from it.\"";
        }

        if (lower.contains("timeout") || lower.contains("infinite loop") || lower.contains("time limit")) {
            return "Master Velan gently stops the clock. " +
                   "\"Your spell ran but never stopped â€” this usually means an infinite loop. " +
                   "Check any while loops: is the condition variable being changed inside the loop? " +
                   "For a while loop to end, something inside it must eventually make the condition false. " +
                   "Also check for loops: make sure the counter is moving towards the exit condition, not away from it.\"";
        }

        if (lower.contains("classcastexception")) {
            return "Master Velan examines your transformation spell. " +
                   "\"A ClassCastException means you are trying to convert an object to a type it is not. " +
                   "When casting objects, the object must actually be an instance of the target type. " +
                   "Check your cast operations and make sure the types are compatible.\"";
        }

        return "Master Velan studies the crash report carefully. " +
               "\"Your code compiled successfully but something went wrong at runtime. " +
               "The error message above describes what happened â€” read it carefully for clues. " +
               "Common runtime problems are: dividing by zero, accessing an array out of its bounds, " +
               "calling a method on a null variable, or an infinite loop or recursion. " +
               "Add a print statement before the crash point to see what values your variables hold.\"";
    }

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    // TEST FAILURE FEEDBACK
    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    public String getFeedback(String questTitle, String topic, String problem,
                               String code, String failedTests) {
        log.info("[Mentor] Test failure for quest='{}' topic='{}' failedTests='{}'",
                questTitle, topic, failedTests);
        log.debug("[Mentor] Student code length: {} chars", code == null ? 0 : code.length());

        String apiFeedback = callAnthropicApi(
                "Error Type: Test Failure\nQuest: " + questTitle + "\nTopic: " + topic +
                "\nCode:\n" + code + "\nFailed Tests:\n" + failedTests);
        if (apiFeedback != null) return apiFeedback;

        String topicLower = topic == null ? "" : topic.toLowerCase();
        String failedLower = failedTests == null ? "" : failedTests.toLowerCase();
        String codeLower = code == null ? "" : code.toLowerCase();

        if (topicLower.contains("hello world") || questTitle.toLowerCase().contains("first spell")) {
            return "Master Velan leans over to look. " +
                   "\"The output does not quite match what is expected. " +
                   "Check the exact text inside your println â€” every character must match, including capital letters and punctuation. " +
                   "The text 'Welcome to Polymath Academy!' must appear exactly as written, including the exclamation mark.\"";
        }

        if (topicLower.contains("variable")) {
            if (failedLower.contains("string") || failedLower.contains("name")) {
                return "Master Velan points to the String declaration. " +
                       "\"Remember that String starts with a capital S, and text values must be wrapped in double quotes. " +
                       "Check your String variable â€” is it declared as 'String name = \"value\"'?\"";
            }
            if (failedLower.contains("double") || failedLower.contains("mana")) {
                return "Master Velan taps the decimal line. " +
                       "\"A double value should include a decimal point â€” write '100.0' not '100' if the expected output is '100.0'. " +
                       "Check your double declaration and make sure the value matches exactly.\"";
            }
            return "Master Velan surveys your variables. " +
                   "\"Check each variable is declared with the correct type, the correct name (case matters!), " +
                   "and the correct value. Then make sure you are printing each one with System.out.println().\"";
        }

        if (topicLower.contains("arithmetic")) {
            return "Master Velan counts on his fingers. " +
                   "\"The calculation is not producing the right result. " +
                   "Work through it step by step: apply each operation in order and check the intermediate values. " +
                   "Remember the shortcut operators â€” '+= 5' adds 5, '*= 2' doubles, '-= 3' subtracts 3. " +
                   "Try printing the value after each step to find where it goes wrong.\"";
        }

        if (topicLower.contains("string")) {
            if (failedLower.contains("length")) {
                return "Master Velan counts carefully. " +
                       "\"The length output is wrong. Remember: name.length() counts every character including spaces. " +
                       "'Aria Voss' has 9 characters (4 + 1 space + 4). Make sure you are calling length() on the full name, not just part of it.\"";
            }
            if (failedLower.contains("upper")) {
                return "Master Velan raises an eyebrow. " +
                       "\"Use toUpperCase() â€” not toUpper() or upper(). Java method names are case-sensitive and must be spelled exactly.\"";
            }
            return "Master Velan points to the concatenation. " +
                   "\"Check how you are joining the strings. To combine first and last name with a space between: " +
                   "firstName + \" \" + lastName. Make sure the space is inside its own set of quotes.\"";
        }

        if (topicLower.contains("boolean") || topicLower.contains("comparison")) {
            if (codeLower.contains("= true") && !codeLower.contains("== true")) {
                return "Master Velan spots the issue immediately. " +
                       "\"You may be confusing '=' with '=='. A single equals sign assigns a value. " +
                       "Two equals signs '==' check equality. For example: 'boolean a = (10 > 5)' not 'boolean a = 10 > 5 = true'.\"";
            }
            return "Master Velan lights up the truth crystals. " +
                   "\"Check your comparison operators carefully. '>' is greater than, '>=' is greater than or equal, '==' is equal to (two equals signs!), '!=' is not equal. " +
                   "For AND use '&&', for OR use '||'. Make sure each boolean is declared and printed separately.\"";
        }

        if (topicLower.contains("cast") || topicLower.contains("double")) {
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
                   "\"Check your casting. To convert an int to double: (double) value. To convert a double to int: (int) value â€” this drops the decimal, it does not round. " +
                   "For printf formatting: System.out.printf(\\\"%.1f%n\\\", value) gives one decimal place.\"";
        }

        if (topicLower.contains("if") || topicLower.contains("else")) {
            return "Master Velan checks the bridge conditions. " +
                   "\"One of your conditions is not matching correctly. " +
                   "Check the order: the 'coins > 50' check must come first. If you check '>= 10' first, " +
                   "it will catch values above 50 as well. Also check your comparison operators â€” '>' not '>=', '>=' not '>'.\"";
        }

        if (topicLower.contains("switch")) {
            if (!codeLower.contains("break")) {
                return "Master Velan points firmly at the switch. " +
                       "\"You are missing 'break;' statements. Without a break at the end of each case, " +
                       "Java falls through into the next case and executes that too. Add 'break;' after each println.\"";
            }
            return "Master Velan studies the sigil. " +
                   "\"Check your case values match exactly â€” String comparisons are case-sensitive. " +
                   "Make sure you have a 'default:' case at the end, and that every case has a 'break;' statement.\"";
        }

        if (topicLower.contains("while")) {
            if (!codeLower.contains("count++") && !codeLower.contains("count +=") && !codeLower.contains("count=count")) {
                return "Master Velan checks the clock mechanism. " +
                       "\"Your loop may never stop. Inside a while loop, the condition variable must change â€” " +
                       "otherwise the condition stays true forever (infinite loop). " +
                       "Add 'count++' inside your loop to increment the counter each time.\"";
            }
            return "Master Velan listens to the ticks. " +
                   "\"Check your while condition: 'while (count <= 5)' runs for 1, 2, 3, 4, 5. " +
                   "Make sure you start at 1, not 0, and that you print 'Clock resting.' after the loop ends (outside the braces).\"";
        }

        if (topicLower.contains("for loop") || topicLower.contains("for loops")) {
            return "Master Velan counts the echoes. " +
                   "\"Check your for loop: 'for (int i = 1; i <= 5; i++)' runs for i = 1, 2, 3, 4, 5. " +
                   "Inside the loop, 'Echo ' + i gives you the numbered output. " +
                   "The 'Tower unlocked.' line must be after the closing brace of the loop, not inside it.\"";
        }

        if (topicLower.contains("nested")) {
            return "Master Velan maps the labyrinth. " +
                   "\"In nested loops, the inner loop must run completely for each iteration of the outer loop. " +
                   "Structure: outer loop over i (rows 1-3), inner loop over j (columns 1-3), " +
                   "print 'Room ' + i + '-' + j. Make sure the inner loop's braces are inside the outer loop's braces.\"";
        }

        if (topicLower.contains("array") && !topicLower.contains("arraylist")) {
            if (failedLower.contains("total")) {
                return "Master Velan counts the vials. " +
                       "\"The 'Total: 5' line is not appearing. This should be printed after the loop ends â€” outside the loop's closing brace. " +
                       "Check that your print statement is not accidentally inside the loop.\"";
            }
            return "Master Velan examines the shelf. " +
                   "\"Check your array declaration syntax: String[] ingredients = {\\\"Moonpetal\\\", ...}. " +
                   "Then loop from i = 0 to i < ingredients.length (not <=), printing ingredients[i] each time.\"";
        }

        if (topicLower.contains("arraylist") || topicLower.contains("list")) {
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

        if (topicLower.contains("method")) {
            if (!codeLower.contains("static")) {
                return "Master Velan checks the method signatures. " +
                       "\"Methods called from main need the 'static' keyword. " +
                       "Declare them as: 'static void greetWizard(...)' and 'static int add(...)'.\"";
            }
            if (failedLower.contains("sum") || failedLower.contains("add")) {
                return "Master Velan examines the add method. " +
                       "\"The add method must return a value â€” use 'return a + b;' as the last line. " +
                       "The return type must be 'int', not 'void'. Then print the result: System.out.println(add(12, 8)).\"";
            }
            return "Master Velan reviews the Codex entry. " +
                   "\"Check two things: the greeting method should print the exact format 'Welcome, Kael! Level 7.' " +
                   "and the add method should have 'return a + b;' and return type 'int'.\"";
        }

        if (topicLower.contains("recursion")) {
            return "Master Velan consults the Obelisk. " +
                   "\"Every recursive method needs two parts: a base case that stops the recursion, " +
                   "and a recursive case that calls the method again with a smaller value. " +
                   "For factorial: 'if (n <= 1) return 1;' is the base case, " +
                   "and 'return n * factorial(n - 1);' is the recursive case.\"";
        }

        if (topicLower.contains("class") || topicLower.contains("object")) {
            return "Master Velan inspects the blueprint. " +
                   "\"Check your class structure: fields declared at the top, constructor using 'this.name = name' to set them, " +
                   "and the describe() method printing the correct format. " +
                   "Then create objects with 'new Wizard(\\\"Aldric\\\", 5)' and call describe() on each.\"";
        }

        if (topicLower.contains("encapsulation")) {
            return "Master Velan seals the vault. " +
                   "\"Check: fields must be 'private'. Getters return the field value. " +
                   "The setLevel setter must check 'if (level > 0)' before setting â€” negative values should be silently ignored. " +
                   "After calling setLevel(-1), the level should remain at 5.\"";
        }

        if (topicLower.contains("inheritance")) {
            return "Master Velan reads the lineage scroll. " +
                   "\"Check: 'class BattleMage extends Wizard', constructor calls 'super(name, level)' first, " +
                   "then sets 'this.weapon = weapon'. The @Override describe() should print: " +
                   "'BattleMage ' + name + ' wields ' + weapon + '.'.\"";
        }

        if (topicLower.contains("polymorphism")) {
            return "Master Velan surveys the mirrors. " +
                   "\"Declare the array as 'Wizard[] gallery = { new Wizard(...), new BattleMage(...), new Healer(...) }'. " +
                   "Loop with 'gallery[i].describe()' â€” Java will call the correct version automatically based on the actual object type.\"";
        }

        if (topicLower.contains("abstract")) {
            return "Master Velan consults the Sanctum blueprints. " +
                   "\"Check: 'abstract class Shape { abstract double area(); }', " +
                   "Circle overrides area() with 'return Math.PI * radius * radius', " +
                   "Rectangle with 'return width * height'. " +
                   "Round the circle result: Math.round(circle.area() * 10) / 10.0.\"";
        }

        if (topicLower.contains("exception")) {
            return "Master Velan monitors the ward. " +
                   "\"Structure: try { System.out.println(10/0); } catch (ArithmeticException e) { System.out.println(\\\"Caught: \\\" + e.getMessage()); } finally { System.out.println(\\\"Ward stable.\\\"); }. " +
                   "The finally block runs regardless of whether an exception occurred.\"";
        }

        if (topicLower.contains("generics") || topicLower.contains("generic")) {
            return "Master Velan examines the forge. " +
                   "\"Your Box<T> class needs: 'private T item', 'public void put(T item) { this.item = item; }', " +
                   "and 'public T get() { return item; }'. " +
                   "Use it as: 'Box<String> box = new Box<>(); box.put(\\\"value\\\"); System.out.println(box.get());'.\"";
        }

        if (topicLower.contains("lambda")) {
            if (failedLower.contains("predicate") || failedLower.contains("true")) {
                return "Master Velan threads the loom. " +
                       "\"For the Predicate: 'Predicate<Integer> isEven = n -> n % 2 == 0;' then call 'isEven.test(4)'. " +
                       "The modulo operator % gives the remainder â€” if n % 2 == 0, the number is even.\"";
            }
            return "Master Velan weaves three threads. " +
                   "\"Check each lambda: Runnable r = () -> System.out.println(\\\"Loom activated.\\\"); r.run(). " +
                   "Predicate<Integer> isEven = n -> n % 2 == 0; isEven.test(4). " +
                   "Function<Integer,Integer> doubler = n -> n * 2; doubler.apply(7).\"";
        }

        if (topicLower.contains("stream")) {
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

        if (topicLower.contains("pattern") || topicLower.contains("singleton") || topicLower.contains("builder")) {
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

        if (topicLower.contains("capstone") || topicLower.contains("task")) {
            if (failedLower.contains("[x]") || failedLower.contains("complete")) {
                return "Master Velan reviews the completed tasks. " +
                       "\"Make sure completeTask() finds the task by title using .equals() (not ==) and calls setDone(true). " +
                       "String comparison with == checks memory address, not content â€” always use .equals() for Strings.\"";
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

        if (topicLower.contains("fizzbuzz") || topicLower.contains("interview")) {
            if (failedLower.contains("fizzbuzz") && codeLower.contains("% 3") && !codeLower.contains("&&")) {
                return "Assessor Vorn taps the paper. " +
                       "\"The combined FizzBuzz check must come first. Check '% 3 == 0 && % 5 == 0' before the individual checks â€” " +
                       "if you check % 3 alone first, 15 will print 'Fizz' and never reach the FizzBuzz branch.\"";
            }
            return "Assessor Vorn checks the output carefully. " +
                   "\"Order matters: check FizzBuzz first (i % 3 == 0 && i % 5 == 0), then Fizz (i % 3 == 0), " +
                   "then Buzz (i % 5 == 0), then print i as the else case. " +
                   "Loop from i = 1 to i <= 20.\"";
        }

        if (topicLower.contains("palindrome")) {
            return "Assessor Vorn examines the method. " +
                   "\"Reverse the string and compare: new StringBuilder(s).reverse().toString(). " +
                   "Then return s.equals(reversed) â€” use .equals() not == for String comparison.\"";
        }

        if (topicLower.contains("hashmap") || topicLower.contains("word")) {
            return "Assessor Vorn points to the map. " +
                   "\"Split the sentence: String[] words = sentence.split(\\\" \\\"). " +
                   "For each word: counts.put(word, counts.getOrDefault(word, 0) + 1). " +
                   "getOrDefault returns 0 if the word has not been seen yet. " +
                   "Then print with: counts.forEach((w, c) -> System.out.println(w + \\\": \\\" + c)).\"";
        }

        return "Master Velan studies the failed tests. " +
               "\"One or more test cases did not produce the expected output. " +
               "Compare your output carefully against what is expected â€” check for extra spaces, wrong capitalisation, or a missing character. " +
               "Add a print statement to show each variable's value and trace through your logic step by step.\"";
    }

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    // TOPIC HINT
    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    public String getTopicHint(String topic) {
        log.info("[Mentor] Domain hint requested for: {}", topic);

        Map<String, String> hints = Map.ofEntries(
            Map.entry("hello world",     "Use System.out.println(\"your message\"); â€” don't forget the semicolon."),
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
