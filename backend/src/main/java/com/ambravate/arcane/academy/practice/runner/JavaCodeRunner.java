package com.ambravate.arcane.academy.practice.runner;

import com.ambravate.arcane.academy.practice.dto.CodeRunResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * In-process Java code runner — the default (£0) binding for {@link CodeExecutionPort}.
 * <p>
 * Active whenever {@code sandbox.mode} is not set to {@code docker}.
 * Compilation uses the JVM's {@code javax.tools.JavaCompiler}; execution runs in a
 * thread-isolated {@link SandboxedClassLoader} with a 5-second wall-clock timeout.
 */
@Service
@ConditionalOnProperty(name = "sandbox.mode", havingValue = "inprocess", matchIfMissing = true)
@Slf4j
public class JavaCodeRunner implements CodeExecutionPort {

    private static final int TIMEOUT_SECONDS = 5;
    private static final int MAX_OUTPUT_CHARS = 2000;

    // ── Thread-safe output capture ────────────────────────────────────────────
    //
    // System.setOut/setErr are JVM-global. Calling them per-request means
    // concurrent submissions corrupt each other's output.  Instead we install
    // a ThreadLocalPrintStream once at startup and each run just calls
    // capture()/release() on the current thread.

    private static ThreadLocalPrintStream threadOut;
    private static ThreadLocalPrintStream threadErr;

    @PostConstruct
    void installThreadLocalStreams() {
        // Guard against double-installation if Spring ever creates a second bean.
        if (threadOut == null) {
            threadOut = new ThreadLocalPrintStream(System.out);
            threadErr = new ThreadLocalPrintStream(System.err);
            System.setOut(threadOut);
            System.setErr(threadErr);
            log.debug("[CodeRunner] ThreadLocal output streams installed");
        }
    }

    // ── Forbidden source patterns (pre-compile scan) ──────────────────────────
    //
    // System.exit() would kill the server process; SecurityManager is gone in
    // Java 21 so we cannot intercept it at runtime.  A source scan is the
    // pragmatic backstop for a learning platform.

    private record ForbiddenPattern(String token, String reason) {}

    private static final List<ForbiddenPattern> FORBIDDEN_PATTERNS = List.of(
            new ForbiddenPattern("System.exit",      "System.exit() is not permitted in the sandbox."),
            new ForbiddenPattern("Runtime.getRuntime","Runtime.getRuntime() is not permitted."),
            new ForbiddenPattern("new ProcessBuilder","ProcessBuilder is not permitted."),
            new ForbiddenPattern("System.setOut",    "Replacing System.out is not permitted."),
            new ForbiddenPattern("System.setErr",    "Replacing System.err is not permitted."),
            new ForbiddenPattern("System.setIn",     "Replacing System.in is not permitted.")
    );

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Wraps student code in a class, compiles it, and executes it in a
     * sandboxed thread with a strict timeout.
     */
    public CodeRunResponse run(String studentCode, String testInput) {
        log.debug("[CodeRunner] Starting run | codeLength={} hasTestInput={}",
                studentCode != null ? studentCode.length() : 0, testInput != null && !testInput.isBlank());

        // Pre-compile source scan — catches System.exit() before JVM has a chance to run it.
        if (studentCode != null) {
            for (ForbiddenPattern fp : FORBIDDEN_PATTERNS) {
                if (studentCode.contains(fp.token())) {
                    log.info("[CodeRunner] Forbidden pattern detected: {}", fp.token());
                    return CodeRunResponse.compilationError("Sandbox violation: " + fp.reason());
                }
            }
        }

        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("arcane_run_");
            log.debug("[CodeRunner] Temp dir created: {}", tempDir);

            String wrappedCode = wrapCode(studentCode, testInput);
            boolean hasClass = studentCode != null && studentCode.matches("(?s).*\\bclass\\s+\\w+\\s*\\{.*");
            int lineOffset = computeLineOffset(hasClass, testInput);
            log.debug("[CodeRunner] Code wrapping | hasClassDeclaration={} wrappedLength={} lineOffset={}",
                    hasClass, wrappedCode.length(), lineOffset);

            Path sourceFile = tempDir.resolve("StudentSolution.java");
            Files.writeString(sourceFile, wrappedCode);

            // Compile
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                log.error("[CodeRunner] JavaCompiler not available — JDK required, not JRE");
                return CodeRunResponse.error("Java compiler not available on this server.");
            }

            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null)) {
                Iterable<? extends JavaFileObject> units = fileManager.getJavaFileObjects(sourceFile.toFile());
                JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, units);
                boolean success = task.call();

                if (!success) {
                    StringBuilder errors = new StringBuilder();
                    for (Diagnostic<?> d : diagnostics.getDiagnostics()) {
                        if (d.getKind() == Diagnostic.Kind.ERROR) {
                            long line = Math.max(1, d.getLineNumber() - lineOffset);
                            String friendly = friendlyError(d.getMessage(null), line);
                            errors.append(friendly).append("\n");
                        }
                    }
                    String errorMsg = errors.toString().trim();
                    log.info("[CodeRunner] COMPILE FAILED | errors='{}'", errorMsg);
                    return CodeRunResponse.compilationError(errorMsg);
                }
                log.debug("[CodeRunner] Compilation succeeded");
            }

            log.debug("[CodeRunner] Executing compiled class with {}s timeout", TIMEOUT_SECONDS);
            return executeWithTimeout(tempDir);

        } catch (IOException e) {
            log.error("[CodeRunner] IO error during code run", e);
            return CodeRunResponse.error("Internal error preparing code execution.");
        } finally {
            if (tempDir != null) cleanupDir(tempDir);
        }
    }

    private CodeRunResponse executeWithTimeout(Path classDir) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<CodeRunResponse> future = executor.submit(() -> executeClass(classDir));
        try {
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            return CodeRunResponse.error("Time limit exceeded (" + TIMEOUT_SECONDS + "s). Check for infinite loops.");
        } catch (Exception e) {
            return CodeRunResponse.error("Execution error: " + e.getMessage());
        } finally {
            executor.shutdownNow();
        }
    }

    private CodeRunResponse executeClass(Path classDir) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream capturedOut = new PrintStream(baos);

        // Route this thread's System.out/err to our capture buffer.
        // InheritableThreadLocal ensures any threads student code spawns
        // also write here rather than to the server log.
        threadOut.capture(capturedOut);
        threadErr.capture(capturedOut);

        try (SandboxedClassLoader loader = new SandboxedClassLoader(
                new java.net.URL[]{classDir.toUri().toURL()})) {

            // Try StudentSolution first, then scan for any class with a main method.
            Class<?> clazz = null;
            try {
                clazz = loader.loadClass("StudentSolution");
            } catch (ClassNotFoundException ex) {
                try (var files = java.nio.file.Files.list(classDir)) {
                    for (var cf : files.filter(p -> p.toString().endsWith(".class")).toList()) {
                        String name = cf.getFileName().toString().replace(".class", "");
                        try {
                            Class<?> c = loader.loadClass(name);
                            c.getMethod("main", String[].class);
                            clazz = c;
                            break;
                        } catch (NoSuchMethodException | ClassNotFoundException ignored) {}
                    }
                }
            }
            if (clazz == null) return CodeRunResponse.error("No class with a main method found.");

            log.debug("[CodeRunner] Invoking main on class: {}", clazz.getName());
            Method main = clazz.getMethod("main", String[].class);
            main.invoke(null, (Object) new String[]{});

            String output = baos.toString();
            boolean truncated = output.length() > MAX_OUTPUT_CHARS;
            if (truncated) {
                output = output.substring(0, MAX_OUTPUT_CHARS) + "\n... (output truncated)";
            }
            log.debug("[CodeRunner] Execution SUCCESS | outputLength={} truncated={}",
                    output.trim().length(), truncated);
            return CodeRunResponse.success(output.trim());

        } catch (SecurityException e) {
            log.info("[CodeRunner] SANDBOX VIOLATION | {}", e.getMessage());
            return CodeRunResponse.error("Sandbox violation: " + e.getMessage()
                    + "\nHint: File, network, and reflection APIs are not available in the sandbox.");
        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getCause();
            String errMsg = cause != null ? cause.toString() : e.toString();
            log.info("[CodeRunner] RUNTIME ERROR | {}", errMsg);
            return CodeRunResponse.runtimeError(friendlyRuntimeError(cause != null ? cause : e));
        } catch (Exception e) {
            log.error("[CodeRunner] Unexpected execution error", e);
            return CodeRunResponse.error("Execution error: " + e.getMessage());
        } finally {
            // Always restore — leaves fallback (server log) active for this thread.
            threadOut.release();
            threadErr.release();
        }
    }

    /**
     * Returns how many compiler lines precede the first line of student code
     * in the wrapped source file, so error line numbers can be mapped back to
     * what the student sees in the editor.
     *
     * <p>When we wrap code we always emit:
     * <ol>
     *   <li>{@code public class StudentSolution {}}</li>
     *   <li>{@code public static void main(String[] args) {}}</li>
     *   <li>The injected-vars placeholder line (blank when there are no test vars)</li>
     * </ol>
     * That's 3 fixed preamble lines.  Each newline in the injected test-input
     * string adds one additional line before the student code begins.
     * When the student supplies a full class declaration we do not wrap at all
     * and the offset is zero.
     */
    private int computeLineOffset(boolean studentHasClass, String testInput) {
        if (studentHasClass) return 0;
        if (testInput == null || testInput.isBlank() || "null".equalsIgnoreCase(testInput)) {
            return 3;
        }
        long injectedNewlines = (testInput + "\n").chars().filter(c -> c == '\n').count();
        return (int) (3 + injectedNewlines);
    }

    /**
     * Wraps student code in a valid Java class with a main method,
     * unless the student has already written a full class.
     *
     * Detection: if the code contains a class declaration with a body
     * (i.e. "class Foo {") we use it directly, renaming the public class
     * to StudentSolution so the classloader can find it.
     *
     * Injects any test input override at the top of main when wrapping.
     */
    private String wrapCode(String studentCode, String testInput) {
        boolean hasClassDeclaration = studentCode.matches("(?s).*\\bclass\\s+\\w+\\s*\\{.*");

        if (hasClassDeclaration) {
            // Rename the first public class to StudentSolution so classloader finds it.
            String renamed = studentCode.replaceFirst(
                    "public\\s+class\\s+\\w+", "public class StudentSolution");

            // If no public class existed, fall back to renaming the first non-public class.
            // Also rename its constructor declarations so the constructor name stays in sync
            // with the new class name — otherwise the compiler sees a method with no return type.
            // (Do NOT chain both replacements — that would rename auxiliary classes too,
            //  producing duplicate-class compile errors in multi-class files.)
            if (renamed.equals(studentCode)) {
                java.util.regex.Matcher classMatcher = java.util.regex.Pattern
                        .compile("\\bclass\\s+(\\w+)\\s*\\{")
                        .matcher(studentCode);
                if (classMatcher.find()) {
                    String oldName = classMatcher.group(1);
                    renamed = classMatcher.replaceFirst("public class StudentSolution {");
                    // Rename constructor declarations that carry the old class name.
                    // Match access modifier + old name + '(' to avoid touching 'new OldName()' calls.
                    for (String mod : new String[]{"public ", "private ", "protected "}) {
                        renamed = renamed.replace(mod + oldName + "(", mod + "StudentSolution(");
                    }
                    // Also handle package-private constructors (indented, at start of line)
                    renamed = renamed.replaceAll(
                            "(?m)^([ \\t]+)" + java.util.regex.Pattern.quote(oldName) + "\\s*\\(",
                            "$1StudentSolution(");
                }
            }
            return renamed;
        }

        // No class — wrap in boilerplate.
        // Treat the sentinel string "null" (from JSON test specs) the same as Java null.
        String injectedVars = (testInput != null && !testInput.isBlank() && !"null".equalsIgnoreCase(testInput))
                ? testInput + "\n" : "";
        String cleanCode = studentCode;
        // Remove conflicting variable declarations for any variable that is re-declared by the test input
        if (!injectedVars.isBlank()) {
            java.util.regex.Matcher m = java.util.regex.Pattern
                    .compile("(?:int|double|String|boolean|char|long|float)\\s+(\\w+)\\s*=")
                    .matcher(injectedVars);
            while (m.find()) {
                String varName = java.util.regex.Pattern.quote(m.group(1));
                cleanCode = cleanCode.replaceAll(
                        "(?m)^[ \\t]*(?:int|double|String|boolean|char|long|float)\\s+" + varName + "[^;]*;[ \\t]*(?:\r?\n)?",
                        "");
            }
        }
        return """
            public class StudentSolution {
                public static void main(String[] args) {
                    %s
                    %s
                }
            }
            """.formatted(injectedVars, cleanCode);
    }

    /**
     * Converts a Java runtime exception into a learner-friendly message.
     * Strips package prefixes and stack traces, and maps the most common
     * exception types to plain-English explanations with actionable hints.
     */
    private String friendlyRuntimeError(Throwable t) {
        String type = t.getClass().getSimpleName();
        String detail = t.getMessage() != null ? t.getMessage() : "";

        // ── NullPointerException ──────────────────────────────────────────────
        if (t instanceof NullPointerException) {
            if (!detail.isBlank()) {
                // Java 14+ NPE messages are already quite descriptive — keep them but simplify
                String clean = detail.replace("Cannot invoke \"", "Tried to call a method on '")
                                     .replace("\" because \"", "' but it was null (")
                                     .replace("\" is null", " is null)");
                return "NullPointerException: " + clean
                        + "\nHint: Make sure the object is created (using 'new') before you call methods on it.";
            }
            return "NullPointerException: You tried to use an object that hasn't been created yet (it's null)."
                    + "\nHint: Check that all your objects are initialised with 'new' before use.";
        }

        // ── ArrayIndexOutOfBoundsException ────────────────────────────────────
        if (t instanceof ArrayIndexOutOfBoundsException) {
            java.util.regex.Matcher m = java.util.regex.Pattern
                    .compile("Index (\\d+) out of bounds for length (\\d+)").matcher(detail);
            if (m.find()) {
                int idx = Integer.parseInt(m.group(1));
                int len = Integer.parseInt(m.group(2));
                return "ArrayIndexOutOfBoundsException: Tried to access index " + idx
                        + " but the array only has " + len + " element" + (len == 1 ? "" : "s")
                        + " (valid indices: 0 to " + (len - 1) + ")."
                        + "\nHint: Check your loop bounds and array access expressions.";
            }
            return "ArrayIndexOutOfBoundsException: Tried to access an array position that doesn't exist."
                    + "\nHint: Check your loop bounds — array indices start at 0, not 1.";
        }

        // ── StringIndexOutOfBoundsException ───────────────────────────────────
        if (t instanceof StringIndexOutOfBoundsException) {
            return "StringIndexOutOfBoundsException: Tried to access a character position outside the string's length."
                    + "\nHint: Check the index you're passing to charAt() or substring() — it must be less than the string's length.";
        }

        // ── IndexOutOfBoundsException (List etc.) ─────────────────────────────
        if (t instanceof IndexOutOfBoundsException) {
            return "IndexOutOfBoundsException: " + (detail.isBlank() ? "Tried to access an index outside the valid range." : detail)
                    + "\nHint: Check that your index is between 0 and size()-1.";
        }

        // ── StackOverflowError ────────────────────────────────────────────────
        if (t instanceof StackOverflowError) {
            return "StackOverflowError: Too many nested method calls — this usually means infinite recursion."
                    + "\nHint: Check that your recursive method has a base case that stops the recursion.";
        }

        // ── ArithmeticException ───────────────────────────────────────────────
        if (t instanceof ArithmeticException) {
            if (detail.contains("/ by zero") || detail.contains("divide by zero")) {
                return "ArithmeticException: Division by zero — you can't divide a number by zero."
                        + "\nHint: Add a check (e.g. if (divisor != 0)) before performing the division.";
            }
            return "ArithmeticException: " + detail;
        }

        // ── NumberFormatException ─────────────────────────────────────────────
        if (t instanceof NumberFormatException) {
            java.util.regex.Matcher m = java.util.regex.Pattern
                    .compile("For input string: \"(.+?)\"").matcher(detail);
            String val = m.find() ? "\"" + m.group(1) + "\"" : "the value";
            return "NumberFormatException: Cannot convert " + val + " to a number."
                    + "\nHint: Make sure the string contains only digits before calling parseInt() or similar.";
        }

        // ── ClassCastException ────────────────────────────────────────────────
        if (t instanceof ClassCastException) {
            return "ClassCastException: Tried to cast an object to an incompatible type."
                    + "\nHint: Use 'instanceof' to check the type before casting.";
        }

        // ── OutOfMemoryError ──────────────────────────────────────────────────
        if (t instanceof OutOfMemoryError) {
            return "OutOfMemoryError: Your program ran out of memory."
                    + "\nHint: Check for infinite loops, or collections that grow without bound.";
        }

        // ── NoSuchElementException ────────────────────────────────────────────
        if (t.getClass().getSimpleName().equals("NoSuchElementException")) {
            return "NoSuchElementException: Tried to retrieve an element that doesn't exist."
                    + "\nHint: Check that the collection isn't empty before calling next() or similar.";
        }

        // ── ConcurrentModificationException ───────────────────────────────────
        if (t.getClass().getSimpleName().equals("ConcurrentModificationException")) {
            return "ConcurrentModificationException: A collection was modified while it was being iterated."
                    + "\nHint: Don't add or remove elements from a list inside a for-each loop over it.";
        }

        // ── Generic fallback — strip package names and stack trace ────────────
        String clean = detail.isBlank() ? "" : " " + detail;
        return type + ":" + clean + "\nHint: Check the logic around the line causing this error.";
    }

    /**
     * Converts a raw javac diagnostic message into a learner-friendly hint.
     * Strips internal implementation details (e.g. "class StudentSolution") and
     * maps the most common compiler errors to plain-English explanations.
     */
    private String friendlyError(String raw, long line) {
        // Strip internal class name that leaks from our renaming
        String msg = raw.replace("class StudentSolution", "your class")
                        .replace("StudentSolution", "your class");

        String prefix = "Line " + line + ": ";

        // ── cannot find symbol ────────────────────────────────────────────────
        if (msg.contains("cannot find symbol")) {
            java.util.regex.Matcher varM = java.util.regex.Pattern
                    .compile("symbol:\\s*variable (\\w+)").matcher(msg);
            if (varM.find()) {
                String name = varM.group(1);
                return prefix + "'" + name + "' is not defined. "
                        + "Did you declare it? Check the spelling and make sure it's created before this line.";
            }
            java.util.regex.Matcher methodM = java.util.regex.Pattern
                    .compile("symbol:\\s*method (\\w+)").matcher(msg);
            if (methodM.find()) {
                String name = methodM.group(1);
                return prefix + "Method '" + name + "' cannot be found. "
                        + "Check the spelling and make sure the method exists with the right parameter types.";
            }
            java.util.regex.Matcher classM = java.util.regex.Pattern
                    .compile("symbol:\\s*class (\\w+)").matcher(msg);
            if (classM.find()) {
                String name = classM.group(1);
                return prefix + "Class '" + name + "' cannot be found. "
                        + "Check the spelling and make sure the class is defined in your code.";
            }
            return prefix + "Something here isn't defined or can't be found — check the spelling.";
        }

        // ── missing semicolon ─────────────────────────────────────────────────
        if (msg.contains("';' expected")) {
            return prefix + "Missing a semicolon ';' — every statement in Java must end with one.";
        }

        // ── missing brace ─────────────────────────────────────────────────────
        if (msg.contains("reached end of file while parsing")) {
            return "Missing a closing brace '}' somewhere — every '{' needs a matching '}'.";
        }
        if (msg.contains("'{' expected")) {
            return prefix + "Expected an opening brace '{' here.";
        }
        if (msg.contains("'}' expected")) {
            return prefix + "Expected a closing brace '}' here — check your block is closed.";
        }

        // ── return type / constructor name mismatch ───────────────────────────
        if (msg.contains("invalid method declaration") && msg.contains("return type required")) {
            return prefix + "This looks like a method missing a return type — or a constructor "
                    + "whose name doesn't match the class name. Every method needs a return type (or 'void').";
        }

        // ── code outside class ────────────────────────────────────────────────
        if (msg.contains("class, interface, enum, or record expected")) {
            return prefix + "Code found outside of any class. "
                    + "Make sure all your code is inside a class body.";
        }

        // ── incompatible types ────────────────────────────────────────────────
        if (msg.contains("incompatible types")) {
            java.util.regex.Matcher typM = java.util.regex.Pattern
                    .compile("required: (\\S+).*found:\\s*(\\S+)", java.util.regex.Pattern.DOTALL).matcher(msg);
            if (typM.find()) {
                return prefix + "Type mismatch — expected '" + typM.group(1)
                        + "' but got '" + typM.group(2) + "'. Check the value you're assigning or returning.";
            }
            return prefix + "Type mismatch — the value on the right doesn't match the expected type on the left.";
        }

        // ── method not applicable ─────────────────────────────────────────────
        if (msg.contains("cannot be applied to given types") || msg.contains("no suitable method found")) {
            return prefix + "Wrong arguments for this method call — check the number and types of values you're passing.";
        }

        // ── variable not initialised ──────────────────────────────────────────
        if (msg.contains("might not have been initialized")) {
            java.util.regex.Matcher varM = java.util.regex.Pattern
                    .compile("variable (\\w+) might").matcher(msg);
            String name = varM.find() ? "'" + varM.group(1) + "'" : "A variable";
            return prefix + name + " is used before it has been given a value. Assign a value to it first.";
        }

        // ── non-static from static context ───────────────────────────────────
        if (msg.contains("non-static") && msg.contains("static context")) {
            return prefix + "You're calling a non-static method or field from a static method. "
                    + "Create an object first (e.g. 'MyClass obj = new MyClass();'), then call obj.method().";
        }

        // ── return statement ──────────────────────────────────────────────────
        if (msg.contains("missing return statement")) {
            return prefix + "This method is missing a return statement. "
                    + "Every code path in a non-void method must return a value.";
        }
        if (msg.contains("return outside method")) {
            return prefix + "'return' found outside of a method — move it inside the method body.";
        }

        // ── duplicate class ───────────────────────────────────────────────────
        if (msg.contains("duplicate class")) {
            java.util.regex.Matcher dupM = java.util.regex.Pattern
                    .compile("duplicate class (\\S+)").matcher(msg);
            String name = dupM.find() ? "'" + dupM.group(1) + "'" : "a class";
            return prefix + "Duplicate class — " + name + " is defined more than once. Each class name must be unique.";
        }

        // ── unreachable statement ─────────────────────────────────────────────
        if (msg.contains("unreachable statement")) {
            return prefix + "Unreachable code — this line can never be executed (there's a return or break before it).";
        }

        // ── array index / null ────────────────────────────────────────────────
        if (msg.contains("array required") && msg.contains("found")) {
            return prefix + "You're using '[]' on something that isn't an array.";
        }

        // ── else without if ───────────────────────────────────────────────────
        if (msg.contains("'else' without 'if'")) {
            return prefix + "'else' without a matching 'if' — check that the 'if' block is closed before the 'else'.";
        }

        // ── fallback — clean up internal name but keep original message ───────
        return prefix + msg.replaceAll("\\(compiler\\.err\\..*?\\)", "").trim();
    }

    private void cleanupDir(Path dir) {
        try {
            try (var walk = Files.walk(dir)) {
                walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            }
        } catch (IOException e) {
            log.warn("Could not clean temp dir: {}", dir);
        }
    }
}
