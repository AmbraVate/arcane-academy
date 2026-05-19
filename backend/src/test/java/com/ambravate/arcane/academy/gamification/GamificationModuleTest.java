package com.ambravate.arcane.academy.gamification;

import com.ambravate.arcane.academy.ArcaneAcademyApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Verifies the gamification module's dependency boundaries in isolation.
 *
 * <p>Declared allowed dependencies: {@code common, content, auth}
 *
 * <p>gamification must NOT depend on: {@code practice, admin, profile, ai}
 * <br>Other modules access gamification exclusively via the {@code api} named interface
 * ({@link com.ambravate.arcane.academy.gamification.api.GamificationFacade}).
 * Direct imports of {@code gamification.service.*} or {@code gamification.repository.*}
 * from outside the module are prohibited by the {@code gamification::api} dependency qualifier.
 */
@DisplayName("Gamification module")
class GamificationModuleTest {

    private static final ApplicationModules MODULES =
            ApplicationModules.of(ArcaneAcademyApplication.class);

    @Test
    @DisplayName("is recognised by Spring Modulith scan")
    void moduleIsRecognised() {
        assertThat(MODULES.getModuleByName("gamification"))
                .as("'gamification' module must be present in the ApplicationModules scan")
                .isPresent();
    }

    @Test
    @DisplayName("all dependencies are within declared bounds")
    void dependenciesAreWithinDeclaredBounds() {
        var gamification = MODULES.getModuleByName("gamification").orElseThrow();
        assertThatCode(() -> gamification.verifyDependencies(MODULES))
                .as("gamification module must not violate its declared allowedDependencies")
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("does not depend on the practice module")
    void doesNotDependOnPracticeModule() {
        // Practice fires SubChunkCompletedEvent / UserEngagedEvent which gamification listens to.
        // Gamification must never import from practice — that would create a circular dependency
        // and collapse the event-driven decoupling introduced in Phase 2a.
        var gamification = MODULES.getModuleByName("gamification").orElseThrow();
        var deps = gamification.getDependencies(MODULES);
        assertThat(deps.containsModuleNamed("practice"))
                .as("gamification must not depend on practice — dependency flows practice → gamification via events, not the reverse")
                .isFalse();
    }
}
