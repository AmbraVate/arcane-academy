package com.ambravate.arcane.academy.practice.runner;

import com.ambravate.arcane.academy.practice.dto.CodeRunResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link DockerSandboxRunner}.
 *
 * <p>Gated on the {@code DOCKER_SANDBOX_TESTS=true} environment variable so that
 * CI pipelines without Docker sidecar support skip these tests automatically.
 * Run locally with:
 * <pre>DOCKER_SANDBOX_TESTS=true ./mvnw test -Dtest=DockerSandboxRunnerIntegrationTest</pre>
 *
 * <p>Tests verify the security constraints that the in-process runner cannot enforce:
 * <ul>
 *   <li>Network access is blocked ({@code --network=none})</li>
 *   <li>Execution timeout is enforced</li>
 *   <li>Normal code produces correct output</li>
 * </ul>
 */
@EnabledIfEnvironmentVariable(named = "DOCKER_SANDBOX_TESTS", matches = "true")
class DockerSandboxRunnerIntegrationTest {

    private final DockerSandboxRunner runner = new DockerSandboxRunner();

    @Test
    void helloWorld_producesCorrectOutput() {
        CodeRunResponse result = runner.run(
                "System.out.println(\"Hello, Arcane Academy!\");", null);

        assertThat(result.getStatus()).isEqualTo(CodeRunResponse.RunStatus.SUCCESS);
        assertThat(result.getOutput()).contains("Hello, Arcane Academy!");
    }

    @Test
    void infiniteLoop_timesOut() {
        CodeRunResponse result = runner.run("while(true) {}", null);

        assertThat(result.getStatus()).isNotEqualTo(CodeRunResponse.RunStatus.SUCCESS);
        assertThat(result.getError()).containsIgnoringCase("time limit");
    }

    @Test
    void networkAccess_isBlocked() {
        // Attempting a socket connection should fail (network=none)
        String code = """
                try {
                    new java.net.Socket("google.com", 80);
                    System.out.println("CONNECTED");
                } catch (Exception e) {
                    System.out.println("BLOCKED: " + e.getClass().getSimpleName());
                }
                """;
        CodeRunResponse result = runner.run(code, null);

        // Either a compile error or a runtime exception — but NOT "CONNECTED"
        String combined = (result.getOutput() != null ? result.getOutput() : "")
                        + (result.getError() != null ? result.getError() : "");
        assertThat(combined).doesNotContain("CONNECTED");
    }

    @Test
    void forbiddenPattern_isRejectedBeforeDocker() {
        CodeRunResponse result = runner.run("System.exit(0);", null);

        assertThat(result.getStatus()).isEqualTo(CodeRunResponse.RunStatus.COMPILE_ERROR);
        assertThat(result.getError()).containsIgnoringCase("sandbox violation");
    }

    @Test
    void compilationError_isReturnedBeforeDocker() {
        CodeRunResponse result = runner.run("this is not java !!!!", null);

        assertThat(result.getStatus()).isEqualTo(CodeRunResponse.RunStatus.COMPILE_ERROR);
        assertThat(result.getError()).isNotBlank();
    }
}
