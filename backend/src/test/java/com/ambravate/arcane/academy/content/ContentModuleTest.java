package com.ambravate.arcane.academy.content;

import com.ambravate.arcane.academy.ArcaneAcademyApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Verifies the content module's dependency boundaries in isolation.
 *
 * <p>Declared allowed dependencies:
 * {@code common, ai, practice::repository, practice::dto, practice::domain, practice::runner, auth}
 * (OPEN module — all its sub-packages are accessible to other modules)
 *
 * <p>content must NOT depend on: {@code gamification, admin, profile}
 * <br>Content is responsible for curriculum structure (chunks, sub-chunks, questions,
 * rabbit holes). It has no business knowing about scores, badges, or admin tooling.
 */
@DisplayName("Content module")
class ContentModuleTest {

    private static final ApplicationModules MODULES =
            ApplicationModules.of(ArcaneAcademyApplication.class);

    @Test
    @DisplayName("is recognised by Spring Modulith scan")
    void moduleIsRecognised() {
        assertThat(MODULES.getModuleByName("content"))
                .as("'content' module must be present in the ApplicationModules scan")
                .isPresent();
    }

    @Test
    @DisplayName("all dependencies are within declared bounds")
    void dependenciesAreWithinDeclaredBounds() {
        var content = MODULES.getModuleByName("content").orElseThrow();
        assertThatCode(() -> content.verifyDependencies(MODULES))
                .as("content module must not violate its declared allowedDependencies")
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("does not depend on the gamification module")
    void doesNotDependOnGamificationModule() {
        // Content defines the curriculum; it must remain agnostic of points, badges, and streaks.
        // Coupling content to gamification would make it impossible to swap gamification rules
        // without touching curriculum code.
        var content = MODULES.getModuleByName("content").orElseThrow();
        var deps = content.getDependencies(MODULES);
        assertThat(deps.containsModuleNamed("gamification"))
                .as("content must not depend on gamification — curriculum should be gamification-agnostic")
                .isFalse();
    }
}
