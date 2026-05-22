package com.ambravate.arcane.academy.content.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    private final JsonContentSeeder jsonContentSeeder;
    private final TestUserSeeder testUserSeeder;
    private final TopicSeeder topicSeeder;

    @Bean
    public ApplicationRunner seedData() {
        return args -> {
            seedQuietly("JSON content", this::seedJsonContent);
            seedQuietly("topics",       topicSeeder::seed);
            seedQuietly("test users",   testUserSeeder::seed);
        };
    }

    /**
     * Loads JSON content, retrying a few times to ride out a transient database
     * blip (e.g. a recycled serverless connection). {@link JsonContentSeeder#seed()}
     * is idempotent, so re-running is safe.
     */
    private void seedJsonContent() throws Exception {
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                int chunkFiles = jsonContentSeeder.seed();
                log.info("[DataSeeder] JSON content seed complete ({} chunk file(s)).", chunkFiles);
                return;
            } catch (Exception e) {
                if (attempt == maxAttempts) {
                    throw e;
                }
                long delayMs = 2000L * attempt;
                log.warn("[DataSeeder] JSON seed attempt {}/{} failed: {} — retrying in {} ms.",
                        attempt, maxAttempts, e.getMessage(), delayMs);
                Thread.sleep(delayMs);
            }
        }
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
