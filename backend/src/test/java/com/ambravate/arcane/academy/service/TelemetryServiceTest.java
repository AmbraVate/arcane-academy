package com.ambravate.arcane.academy.service;

import com.ambravate.arcane.academy.common.telemetry.service.TelemetryService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TelemetryService} — verifies that:
 * <ul>
 *   <li>Each event increments the right Prometheus counter</li>
 *   <li>Tags use the bounded label set (no cardinality blow-ups)</li>
 *   <li>User IDs are hashed deterministically (same id → same hash)</li>
 *   <li>The chunk-id → topic mapping is correct</li>
 * </ul>
 */
@DisplayName("TelemetryService — engagement event emission")
class TelemetryServiceTest {

    private MeterRegistry registry;
    private TelemetryService telemetry;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
        telemetry = new TelemetryService(registry);
        // @PostConstruct doesn't fire in plain unit tests; inject the salt + key manually
        ReflectionTestUtils.setField(telemetry, "hashSalt", "test-salt-deterministic");
        ReflectionTestUtils.invokeMethod(telemetry, "init");
    }

    // ── Quest events ────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Quest lifecycle")
    class Quest {
        @Test
        @DisplayName("questStarted increments counter with derived topic label")
        void questStartedTagsTopic() {
            telemetry.questStarted("user-1", "rx-a1", "rx-a");

            Counter c = registry.find("arcane.quest.started").tag("topic", "react").counter();
            assertThat(c).isNotNull();
            assertThat(c.count()).isEqualTo(1.0);
        }

        @Test
        @DisplayName("questCompleted records xp and topic")
        void questCompletedRecordsXp() {
            telemetry.questCompleted("user-1", "chunk-a1", "chunk-a", 75);

            Counter c = registry.find("arcane.quest.completed").tag("topic", "java").counter();
            assertThat(c).isNotNull();
            assertThat(c.count()).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Tailwind chunk id maps to tailwind topic")
        void tailwindChunkMapsCorrectly() {
            telemetry.questStarted("u", "tw-a1", "tw-a");
            assertThat(registry.find("arcane.quest.started").tag("topic", "tailwind").counter().count())
                    .isEqualTo(1.0);
        }

        @Test
        @DisplayName("Unknown chunk id maps to 'unknown' (defends Prometheus cardinality)")
        void unknownChunkFallsBackToUnknown() {
            telemetry.questStarted("u", "weird-x1", "weird-x");
            assertThat(registry.find("arcane.quest.started").tag("topic", "unknown").counter().count())
                    .isEqualTo(1.0);
        }
    }

    // ── Reviews ─────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Reviews")
    class Reviews {
        @Test
        @DisplayName("reviewGradeGiven tags pass/fail by 0.6 threshold")
        void gradeTagsPassed() {
            telemetry.reviewGradeGiven("u", "DAILY", 0.8);
            telemetry.reviewGradeGiven("u", "DAILY", 0.4);

            Counter passed = registry.find("arcane.review.grade_given")
                    .tag("cadence", "DAILY").tag("passed", "true").counter();
            Counter failed = registry.find("arcane.review.grade_given")
                    .tag("cadence", "DAILY").tag("passed", "false").counter();

            assertThat(passed).isNotNull();
            assertThat(failed).isNotNull();
            assertThat(passed.count()).isEqualTo(1.0);
            assertThat(failed.count()).isEqualTo(1.0);
        }

        @Test
        @DisplayName("session_completed counter ticks once per session")
        void sessionCompletedTicks() {
            telemetry.reviewSessionCompleted("u", "WEEKLY", 8, 10);

            Counter c = registry.find("arcane.review.session_completed")
                    .tag("cadence", "WEEKLY").counter();
            assertThat(c.count()).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Unknown cadence falls back to 'unknown'")
        void unknownCadence() {
            telemetry.reviewSessionCompleted("u", "QUARTERLY", 1, 1);
            assertThat(registry.find("arcane.review.session_completed")
                    .tag("cadence", "unknown").counter().count())
                    .isEqualTo(1.0);
        }
    }

    // ── Streaks ─────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Streaks")
    class Streaks {
        @Test
        @DisplayName("extension and break each have their own counter")
        void streakCounters() {
            telemetry.streakExtended("u", 5);
            telemetry.streakExtended("u", 6);
            telemetry.streakBroken("u", 6, 3);

            assertThat(registry.find("arcane.streak.extended").counter().count()).isEqualTo(2.0);
            assertThat(registry.find("arcane.streak.broken").counter().count()).isEqualTo(1.0);
        }
    }

    // ── Badges ──────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Badges")
    class Badges {
        @Test
        @DisplayName("badgeEarned counter is tagged by category")
        void badgeByCategory() {
            telemetry.badgeEarned("u", "FIRST_CONCEPT", "LEARNING");
            telemetry.badgeEarned("u", "STREAK_7", "STREAK");

            assertThat(registry.find("arcane.badge.earned")
                    .tag("category", "LEARNING").counter().count()).isEqualTo(1.0);
            assertThat(registry.find("arcane.badge.earned")
                    .tag("category", "STREAK").counter().count()).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Category strings are sanitised against label-injection")
        void sanitisesCategory() {
            telemetry.badgeEarned("u", "X", "BAD CATEGORY {hack}");
            // Non-alphanumerics replaced with _
            Counter c = registry.find("arcane.badge.earned")
                    .tag("category", "BAD_CATEGORY__hack_").counter();
            assertThat(c).isNotNull();
        }
    }

    // ── Diagnostic ──────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Diagnostic")
    class Diagnostic {
        @Test
        @DisplayName("diagnosticCompleted tags topic and tier")
        void diagnosticTags() {
            telemetry.diagnosticCompleted("u", "java", "PRACTITIONER", 0.78);

            Counter c = registry.find("arcane.diagnostic.completed")
                    .tag("topic", "java")
                    .tag("tier", "PRACTITIONER")
                    .counter();
            assertThat(c).isNotNull();
            assertThat(c.count()).isEqualTo(1.0);
        }
    }

    // ── User-id hashing (privacy) ───────────────────────────────────────────────

    @Nested
    @DisplayName("User ID hashing")
    class Hashing {
        @Test
        @DisplayName("Same user id always hashes to the same value (deterministic)")
        void deterministic() {
            String h1 = telemetry.hash("user-abc-123");
            String h2 = telemetry.hash("user-abc-123");
            assertThat(h1).isEqualTo(h2);
        }

        @Test
        @DisplayName("Different user ids produce different hashes")
        void distinct() {
            assertThat(telemetry.hash("alice")).isNotEqualTo(telemetry.hash("bob"));
        }

        @Test
        @DisplayName("Hash is 16 hex chars (truncated SHA-256)")
        void hashLength() {
            String h = telemetry.hash("any");
            assertThat(h).hasSize(16);
            assertThat(h).matches("[0-9a-f]{16}");
        }

        @Test
        @DisplayName("null user id hashes to the literal 'null' (no NPE in logs)")
        void nullSafe() {
            assertThat(telemetry.hash(null)).isEqualTo("null");
        }

        @Test
        @DisplayName("Different salts produce different hashes for the same id (forward secrecy)")
        void saltedHash() {
            String hashWithSalt1 = telemetry.hash("u");
            ReflectionTestUtils.setField(telemetry, "hashSalt", "different-salt");
            ReflectionTestUtils.invokeMethod(telemetry, "init");
            String hashWithSalt2 = telemetry.hash("u");
            assertThat(hashWithSalt1).isNotEqualTo(hashWithSalt2);
        }
    }

    // ── Topic derivation helper ─────────────────────────────────────────────────

    @Nested
    @DisplayName("topicFromChunkId helper")
    class TopicMapping {
        @Test
        void javaPrefix() { assertThat(TelemetryService.topicFromChunkId("chunk-a")).isEqualTo("java"); }
        @Test
        void capstonePrefix() { assertThat(TelemetryService.topicFromChunkId("chunk-cap")).isEqualTo("java"); }
        @Test
        void tailwindPrefix() { assertThat(TelemetryService.topicFromChunkId("tw-c")).isEqualTo("tailwind"); }
        @Test
        void reactPrefix() { assertThat(TelemetryService.topicFromChunkId("rx-d")).isEqualTo("react"); }
        @Test
        void sqlPrefix() { assertThat(TelemetryService.topicFromChunkId("sql-a")).isEqualTo("sql"); }
        @Test
        void unknown() { assertThat(TelemetryService.topicFromChunkId("xyz-9")).isEqualTo("unknown"); }
        @Test
        void nullInput() { assertThat(TelemetryService.topicFromChunkId(null)).isEqualTo("unknown"); }
    }
}
