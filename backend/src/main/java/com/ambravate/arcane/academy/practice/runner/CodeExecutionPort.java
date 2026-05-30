package com.ambravate.arcane.academy.practice.runner;

import com.ambravate.arcane.academy.practice.dto.CodeRunResponse;

/**
 * Port (Dependency-Inversion) over Java code execution strategies.
 *
 * <p>Two adapters are provided:
 * <ul>
 *   <li>{@link JavaCodeRunner} — in-process JVM execution with a SandboxedClassLoader.
 *       This is the <strong>default and production binding</strong> (£0 — no extra infra).</li>
 *   <li>{@link DockerSandboxRunner} — docker-per-submission with network isolation,
 *       memory + CPU caps, and a read-only container.  Activated by setting
 *       {@code sandbox.mode=docker} in application properties.  Recommended for
 *       production once a suitable Always-Free VM is available.</li>
 * </ul>
 *
 * <p>All callers inject {@code CodeExecutionPort}; switching strategies is a
 * config-only flip with no code changes required.
 */
public interface CodeExecutionPort {

    /**
     * Compile and execute {@code studentCode}, injecting {@code testInput} as
     * pre-declared variables when non-blank.
     *
     * @param studentCode raw Java code from the learner
     * @param testInput   optional test-harness variable declarations (may be null)
     * @return the execution result (output, error, or compilation failure)
     */
    CodeRunResponse run(String studentCode, String testInput);
}
