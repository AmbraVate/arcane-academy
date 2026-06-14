package com.ambravate.arcane.academy.practice.runner;

import com.ambravate.arcane.academy.practice.dto.CodeRunResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Docker-isolated Java code runner — the secure adapter for {@link CodeExecutionPort}.
 *
 * <p><strong>Activation:</strong> set {@code sandbox.mode=docker} in application properties.
 * The {@code @Primary} annotation ensures this bean is preferred over
 * {@link JavaCodeRunner} when both are on the classpath and this bean is created.
 *
 * <p><strong>£0 deployment strategy:</strong> this runner is <em>not</em> bound in the
 * default Cloud Run deployment (stays in-process).  Switch by setting the property on
 * a dedicated Always-Free VM (e.g. Oracle Cloud ARM or GCP e2-micro) running
 * {@code java -Dsandbox.mode=docker -jar arcane-academy.jar}.
 *
 * <p><strong>Security posture per container:</strong>
 * <ul>
 *   <li>{@code --network=none} — no outbound network</li>
 *   <li>{@code --memory=256m} — hard memory ceiling</li>
 *   <li>{@code --cpus=0.5} — CPU throttle</li>
 *   <li>{@code --pids-limit=64} — fork bomb prevention</li>
 *   <li>{@code --read-only --tmpfs /tmp} — immutable root filesystem</li>
 *   <li>Wall-clock timeout: {@value TIMEOUT_SECONDS} seconds</li>
 * </ul>
 *
 * <p>The runner compiles student code locally (reusing the JVM's {@code javac}),
 * then mounts the resulting {@code .class} files read-only into the container for
 * isolated execution only — no JDK required inside the image.
 */
@Service
@ConditionalOnProperty(name = "sandbox.mode", havingValue = "docker")
@Slf4j
public class DockerSandboxRunner implements CodeExecutionPort {

    private static final int TIMEOUT_SECONDS = 10;
    private static final int MAX_OUTPUT_CHARS = 2000;
    private static final String DOCKER_IMAGE = "eclipse-temurin:21-jre-alpine";

    // ── Forbidden source patterns (pre-compile scan, same as JavaCodeRunner) ──
    private record ForbiddenPattern(String token, String reason) {}

    private static final List<ForbiddenPattern> FORBIDDEN_PATTERNS = List.of(
            new ForbiddenPattern("System.exit",       "System.exit() is not permitted in the sandbox."),
            new ForbiddenPattern("Runtime.getRuntime","Runtime.getRuntime() is not permitted."),
            new ForbiddenPattern("new ProcessBuilder","ProcessBuilder is not permitted."),
            new ForbiddenPattern("System.setOut",     "Replacing System.out is not permitted."),
            new ForbiddenPattern("System.setErr",     "Replacing System.err is not permitted."),
            new ForbiddenPattern("System.setIn",      "Replacing System.in is not permitted.")
    );

    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public CodeRunResponse run(String studentCode, String testInput) {
        log.debug("[DockerSandbox] Starting run | codeLength={}", studentCode != null ? studentCode.length() : 0);

        // 1. Pre-compile source scan
        if (studentCode != null) {
            for (ForbiddenPattern fp : FORBIDDEN_PATTERNS) {
                if (studentCode.contains(fp.token())) {
                    log.info("[DockerSandbox] Forbidden pattern: {}", fp.token());
                    return CodeRunResponse.compilationError("Sandbox violation: " + fp.reason());
                }
            }
        }

        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("arcane_docker_");

            // 2. Wrap and compile locally
            String wrappedCode = wrapCode(studentCode, testInput);
            Path sourceFile = tempDir.resolve("StudentSolution.java");
            Files.writeString(sourceFile, wrappedCode);

            javax.tools.JavaCompiler compiler = javax.tools.ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                return CodeRunResponse.error("Java compiler not available on this server.");
            }

            javax.tools.DiagnosticCollector<javax.tools.JavaFileObject> diagnostics = new javax.tools.DiagnosticCollector<>();
            try (javax.tools.StandardJavaFileManager fm = compiler.getStandardFileManager(diagnostics, null, null)) {
                Iterable<? extends javax.tools.JavaFileObject> units = fm.getJavaFileObjects(sourceFile.toFile());
                javax.tools.JavaCompiler.CompilationTask task = compiler.getTask(null, fm, diagnostics, null, null, units);
                if (!task.call()) {
                    StringBuilder errors = new StringBuilder();
                    for (javax.tools.Diagnostic<?> d : diagnostics.getDiagnostics()) {
                        if (d.getKind() == javax.tools.Diagnostic.Kind.ERROR) {
                            errors.append("Line ").append(Math.max(1, d.getLineNumber() - 3))
                                  .append(": ").append(d.getMessage(null)).append("\n");
                        }
                    }
                    return CodeRunResponse.compilationError(errors.toString().trim());
                }
            }

            // 3. Execute in Docker container
            return executeInDocker(tempDir);

        } catch (IOException e) {
            log.error("[DockerSandbox] IO error", e);
            return CodeRunResponse.error("Internal error preparing Docker execution.");
        } finally {
            if (tempDir != null) cleanupDir(tempDir);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────

    private CodeRunResponse executeInDocker(Path classDir) {
        List<String> cmd = List.of(
                "docker", "run",
                "--rm",
                "--network=none",
                "--memory=256m",
                "--cpus=0.5",
                "--pids-limit=64",
                "--read-only",
                "--tmpfs", "/tmp:rw,size=64m,mode=755",
                "-v", classDir.toAbsolutePath() + ":/sandbox:ro",
                DOCKER_IMAGE,
                "java", "-cp", "/sandbox", "StudentSolution"
        );

        log.debug("[DockerSandbox] Launching: {}", String.join(" ", cmd));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<CodeRunResponse> future = executor.submit(() -> runProcess(cmd));
        try {
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            log.info("[DockerSandbox] Execution timed out");
            return CodeRunResponse.error("Time limit exceeded (" + TIMEOUT_SECONDS + "s). Check for infinite loops.");
        } catch (Exception e) {
            log.error("[DockerSandbox] Execution error", e);
            return CodeRunResponse.error("Docker execution error: " + e.getMessage());
        } finally {
            executor.shutdownNow();
        }
    }

    private CodeRunResponse runProcess(List<String> cmd) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true); // merge stderr into stdout
        Process process = pb.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                if (output.length() > MAX_OUTPUT_CHARS) {
                    output.setLength(MAX_OUTPUT_CHARS);
                    output.append("\n... (output truncated)");
                    break;
                }
            }
        }

        int exitCode = process.waitFor();
        String result = output.toString().trim();

        if (exitCode == 0) {
            log.debug("[DockerSandbox] Execution SUCCESS | outputLength={}", result.length());
            return CodeRunResponse.success(result);
        } else {
            log.info("[DockerSandbox] Execution FAILED | exitCode={}", exitCode);
            return CodeRunResponse.runtimeError(result.isEmpty() ? "Program exited with code " + exitCode : result);
        }
    }

    /**
     * Wraps student code in a class/main method if needed.
     * Mirrors the same logic as {@link JavaCodeRunner#wrapCode} to ensure
     * identical behaviour across execution strategies.
     */
    private String wrapCode(String studentCode, String testInput) {
        if (studentCode == null) studentCode = "";
        boolean hasClass = studentCode.matches("(?s).*\\bclass\\s+\\w+\\s*\\{.*");

        if (hasClass) {
            String renamed = studentCode.replaceFirst(
                    "public\\s+class\\s+\\w+", "public class StudentSolution");
            if (renamed.equals(studentCode)) {
                java.util.regex.Matcher m = java.util.regex.Pattern
                        .compile("\\bclass\\s+(\\w+)\\s*\\{").matcher(studentCode);
                if (m.find()) {
                    String oldName = m.group(1);
                    renamed = m.replaceFirst("public class StudentSolution {");
                    for (String mod : new String[]{"public ", "private ", "protected "}) {
                        renamed = renamed.replace(mod + oldName + "(", mod + "StudentSolution(");
                    }
                }
            }
            return renamed;
        }

        String injectedVars = (testInput != null && !testInput.isBlank() && !"null".equalsIgnoreCase(testInput))
                ? testInput + "\n" : "";
        return """
                public class StudentSolution {
                    public static void main(String[] args) {
                        %s
                        %s
                    }
                }
                """.formatted(injectedVars, studentCode);
    }

    private void cleanupDir(Path dir) {
        try (var walk = Files.walk(dir)) {
            walk.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (IOException e) {
            log.warn("[DockerSandbox] Could not clean temp dir: {}", dir);
        }
    }
}
