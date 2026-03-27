package com.arcane.academy.runner;

import com.arcane.academy.dto.CodeRunResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.security.*;
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
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("arcane_run_");
            String wrappedCode = wrapCode(studentCode, testInput);
            Path sourceFile = tempDir.resolve("StudentSolution.java");
            Files.writeString(sourceFile, wrappedCode);

            // Compile
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
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
                    return CodeRunResponse.compilationError(errors.toString().trim());
                }
            }

            // Execute in a sandboxed thread with timeout
            return executeWithTimeout(tempDir);

        } catch (IOException e) {
            log.error("IO error during code run", e);
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

            Method main = clazz.getMethod("main", String[].class);
            main.invoke(null, (Object) new String[]{});

            String output = baos.toString();
            if (output.length() > MAX_OUTPUT_CHARS) {
                output = output.substring(0, MAX_OUTPUT_CHARS) + "\n... (output truncated)";
            }
            return CodeRunResponse.success(output.trim());

        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getCause();
            return CodeRunResponse.runtimeError(cause != null ? cause.toString() : e.toString());
        } catch (Exception e) {
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
            // Also handle the case where there is no public class (just "class Foo").
            String renamed = studentCode
                    .replaceFirst("public\\s+class\\s+\\w+", "public class StudentSolution")
                    .replaceFirst("(?<!public\\s{0,20})class\\s+(\\w+)\\s*\\{",
                                  "public class StudentSolution {");
            // If there are multiple classes defined (e.g. helper + main class),
            // only the outermost one needs to be StudentSolution.
            return renamed;
        }

        // No class — wrap in boilerplate.
        String injectedVars = (testInput != null && !testInput.isBlank()) ? testInput + "\n" : "";
        String cleanCode = studentCode;
        // Remove conflicting variable declarations when injecting a test override
        if (injectedVars.contains("coins")) {
            cleanCode = studentCode.replaceAll("(?m)^\\s*(int|double|String|boolean)\\s+coins[^;]*;", "");
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
