package com.ambravate.arcane.academy.practice;

import com.ambravate.arcane.academy.ArcaneAcademyApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Verifies the practice module's dependency boundaries in isolation.
 *
 * <p>Declared allowed dependencies:
 * {@code common, ai, content, auth, gamification::api}
 *
 * <p>practice must NOT depend on: {@code admin}
 * <br>practice may only access gamification via its public {@code api} named interface —
 * never via gamification.service.* or gamification.repository.*.
 */
@DisplayName("Practice module")
class PracticeModuleTest {

    private static final ApplicationModules MODULES =
            ApplicationModules.of(ArcaneAcademyApplication.class);

    @Test
    @DisplayName("is recognised by Spring Modulith scan")
    void moduleIsRecognised() {
        assertThat(MODULES.getModuleByName("practice"))
                .as("'practice' module must be present in the ApplicationModules scan")
                .isPresent();
    }

    @Test
    @DisplayName("all dependencies are within declared bounds")
    void dependenciesAreWithinDeclaredBounds() {
        // Per-module verification — failures are attributed specifically to practice,
        // not lumped together with the global ModuleStructureTest.
        var practice = MODULES.getModuleByName("practice").orElseThrow();
        assertThatCode(() -> practice.verifyDependencies(MODULES))
                .as("practice module must not violate its declared allowedDependencies")
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("does not depend on the admin module")
    void doesNotDependOnAdminModule() {
        // Practice handles learner flows and must never pull in admin functionality.
        // Mixing the two would expose admin concerns to the learner path.
        var practice = MODULES.getModuleByName("practice").orElseThrow();
        var deps = practice.getDependencies(MODULES);
        assertThat(deps.containsModuleNamed("admin"))
                .as("practice must not depend on admin — learner flows must stay separate from admin concerns")
                .isFalse();
    }
}
