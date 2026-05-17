package com.ambravate.arcane.academy.practice.runner;

import com.ambravate.arcane.academy.practice.dto.CodeRunResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

@Service
@Slf4j
public class JavaCodeRunner {

    private static final int TIMEOUT_SECONDS = 5;
    private static final int MAX_OUTPUT_CHARS = 2000;

    /**
     * Wraps student code in a class, compiles it, and executes it in a
     * sandboxed thread with a strict timeout.
     */
    public CodeRunResponse run(String studentCode, String testInput) {
        log.debug("[CodeRunner] Starting run | codeLength={} hasTestInput={}",
                studentCode != null ? studentCode.length() : 0, testInput != null && !testInput.isBlank());
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("arcane_run_");
            log.debug("[CodeRunner] Temp dir created: {}", tempDir);

            String wrappedCode = wrapCode(studentCode, testInput);
            boolean hasClass = studentCode != null && studentCode.matches("(?s).*\\bclass\\s+\\w+\\s*\\{.*");
            log.debug("[CodeRunner] Code wrapping | hasClassDeclaration={} wrappedLength={}",
                    hasClass, wrappedCode.length());

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
                            // Report user-friendly line number (subtract wrapper lines)
                            long line = d.getLineNumber() - 5;
                            errors.append("Line ").append(Math.max(1, line))
                                  .append(": ").append(d.getMessage(null)).append("\n");
                        }
                    }
                    String errorMsg = errors.toString().trim();
                    log.info("[CodeRunner] COMPILE FAILED | errors='{}'", errorMsg);
                    return CodeRunResponse.compilationError(errorMsg);
                }
                log.debug("[CodeRunner] Compilation succeeded");
            }

            // Execute in a sandboxed thread with timeout
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
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;

        try (URLClassLoader loader = new URLClassLoader(
                new java.net.URL[]{classDir.toUri().toURL()},
                this.getClass().getClassLoader())) {

            System.setOut(capturedOut);
            System.setErr(capturedOut);

            // Try StudentSolution first, then scan for any class with a main method
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

        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getCause();
            String errMsg = cause != null ? cause.toString() : e.toString();
            log.info("[CodeRunner] RUNTIME ERROR | {}", errMsg);
            return CodeRunResponse.runtimeError(errMsg);
        } catch (Exception e) {
            log.error("[CodeRunner] Unexpected execution error", e);
            return CodeRunResponse.error("Execution error: " + e.getMessage());
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
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
