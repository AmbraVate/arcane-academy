package com.ambravate.arcane.academy.content.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * Runs the data seeders once, as an {@link ApplicationRunner}, after the
 * application context has started.
 *
 * <p>Seeding never blocks startup and never crash-loops the service: every step
 * runs through {@link #seedQuietly}, which logs and swallows failures. A running
 * API with missing content can still serve health checks and be diagnosed; a
 * container that exits on a seed failure cannot.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final MarkdownContentSeeder    markdownContentSeeder;
    private final Optional<TestUserSeeder> testUserSeeder;   // absent in prod (@Profile("!prod"))
    private final DomainSeeder             domainSeeder;
    private final TrackSeeder              trackSeeder;
    private final AdminSeeder              adminSeeder;

    @Bean
    public ApplicationRunner seedData() {
        return args -> {
            seedQuietly("admin account",    adminSeeder::seed);
            seedQuietly("domains",          domainSeeder::seed);
            seedQuietly("tracks",           trackSeeder::seed);
            seedQuietly("Markdown content", markdownContentSeeder::seed);
            testUserSeeder.ifPresent(s -> seedQuietly("test users", s::seed));
        };
    }

    /**
     * Runs a single seeding step, swallowing any failure so a seeding problem can
     * never stop the application from starting.
     */
    private void seedQuietly(String label, SeedStep step) {
        try {
            step.run();
        } catch (Exception e) {
            log.error("[DataSeeder] Seeding '{}' failed — application will start without it.", label, e);
        }
    }

    /** A seeding step that may fail with a checked exception. */
    @FunctionalInterface
    private interface SeedStep {
        void run() throws Exception;
    }
}
