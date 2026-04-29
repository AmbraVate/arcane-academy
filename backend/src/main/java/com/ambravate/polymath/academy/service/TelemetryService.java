package com.ambravate.polymath.academy.service;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

/**
 * Engagement event telemetry — emits two channels per event:
 *
 * <ul>
 *   <li><b>Micrometer counters</b> (low-cardinality labels only — topic, tier,
 *       cadence) → scraped by Prometheus via {@code /actuator/prometheus}.
 *       Drives Grafana dashboards: rates, totals, retention by topic.</li>
 *   <li><b>Structured logs</b> (named logger {@code TELEMETRY}) — every event
 *       includes a HMAC-SHA256-hashed user id. Suitable for log aggregation
 *       (Loki/Datadog/ELK) and per-user behavioural analysis without leaking
 *       the underlying UUID.</li>
 * </ul>
 *
 * <h3>Why hash user IDs?</h3>
 * Putting raw user UUIDs into log streams is a compliance liability. HMAC-SHA256
 * with a per-instance salt produces a stable pseudonym — the same user always
 * hashes to the same string, so cohort analysis works, but the hash cannot be
 * reversed without the salt.
 *
 * <h3>Why not put user_id as a Prometheus label?</h3>
 * Prometheus is a time-series database; each unique label combination is a
 * separate series. Adding {@code user_id} as a label would create one series
 * per user → tens of thousands of series, killing query performance. User-level
 * analysis belongs in the log channel.
 *
 * <h3>Event catalog</h3>
 * <table>
 *   <tr><th>Method</th><th>Counter name</th><th>Trigger</th></tr>
 *   <tr><td>questStarted</td><td>arcane.quest.started</td><td>Sub-chunk encoding session begins</td></tr>
 *   <tr><td>questCompleted</td><td>arcane.quest.completed</td><td>Sub-chunk reaches COMPLETE phase</td></tr>
 *   <tr><td>reviewGradeGiven</td><td>arcane.review.grade_given</td><td>Single review answer submitted</td></tr>
 *   <tr><td>reviewSessionCompleted</td><td>arcane.review.session_completed</td><td>Review session submission</td></tr>
 *   <tr><td>streakExtended</td><td>arcane.streak.extended</td><td>Daily activity continues an existing streak</td></tr>
 *   <tr><td>streakBroken</td><td>arcane.streak.broken</td><td>User returns after &gt;1 day idle</td></tr>
 *   <tr><td>badgeEarned</td><td>arcane.badge.earned</td><td>BadgeService awards a new badge</td></tr>
 *   <tr><td>diagnosticCompleted</td><td>arcane.diagnostic.completed</td><td>User completes the placement diagnostic</td></tr>
 * </table>
 */
@Service
@RequiredArgsConstructor
public class TelemetryService {

    private static final Logger TELEMETRY = LoggerFactory.getLogger("TELEMETRY");

    private final MeterRegistry meterRegistry;

    @Value("${telemetry.hash-salt:dev-salt-rotate-in-production}")
    private String hashSalt;

    private SecretKeySpec hmacKey;

    @PostConstruct
    void init() {
        hmacKey = new SecretKeySpec(hashSalt.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        if ("dev-salt-rotate-in-production".equals(hashSalt)) {
            TELEMETRY.warn("TELEMETRY_HASH_SALT is using its default value. " +
                    "Set a strong random value in production so log pseudonyms are not guessable.");
        }
    }

    // ── Quest lifecycle ─────────────────────────────────────────────────────────

    /**
     * @param chunkId   The parent chunk id — used to derive the topic label
     *                  ({@code chunk-*} → java, {@code tw-*} → tailwind, {@code rx-*} → react).
     */
    public void questStarted(String userId, String subChunkId, String chunkId) {
        String topic = topicFromChunkId(chunkId);
        meterRegistry.counter("arcane.quest.started", "topic", topic).increment();
        TELEMETRY.info("event=quest_started user={} sub_chunk={} chunk={} topic={}",
                hash(userId), subChunkId, chunkId, topic);
    }

    public void questCompleted(String userId, String subChunkId, String chunkId, int xpEarned) {
        String topic = topicFromChunkId(chunkId);
        meterRegistry.counter("arcane.quest.completed", "topic", topic).increment();
        TELEMETRY.info("event=quest_completed user={} sub_chunk={} chunk={} topic={} xp_earned={}",
                hash(userId), subChunkId, chunkId, topic, xpEarned);
    }

    // ── Reviews ─────────────────────────────────────────────────────────────────

    /**
     * @param cadence "DAILY" | "WEEKLY" | "MONTHLY" | "RETRIEVAL"
     * @param score   0.0 – 1.0
     */
    public void reviewGradeGiven(String userId, String cadence, double score) {
        meterRegistry.counter("arcane.review.grade_given",
                "cadence", safeCadence(cadence),
                "passed", score >= 0.6 ? "true" : "false").increment();
        TELEMETRY.info("event=review_grade_given user={} cadence={} score={}",
                hash(userId), cadence, String.format("%.2f", score));
    }

    public void reviewSessionCompleted(String userId, String cadence, int correct, int total) {
        meterRegistry.counter("arcane.review.session_completed",
                "cadence", safeCadence(cadence)).increment();
        double pct = total == 0 ? 0.0 : (double) correct / total;
        TELEMETRY.info("event=review_session_completed user={} cadence={} score={}/{} pct={}",
                hash(userId), cadence, correct, total, String.format("%.2f", pct));
    }

    // ── Streaks ────────────────────────────────────────────────────────────────

    public void streakExtended(String userId, int newLength) {
        meterRegistry.counter("arcane.streak.extended").increment();
        TELEMETRY.info("event=streak_extended user={} new_length={}", hash(userId), newLength);
    }

    public void streakBroken(String userId, int previousLength, long daysSinceActive) {
        meterRegistry.counter("arcane.streak.broken").increment();
        TELEMETRY.info("event=streak_broken user={} previous_length={} days_idle={}",
                hash(userId), previousLength, daysSinceActive);
    }

    // ── Badges ──────────────────────────────────────────────────────────────────

    public void badgeEarned(String userId, String badgeCode, String category) {
        meterRegistry.counter("arcane.badge.earned",
                "category", safeLabel(category)).increment();
        TELEMETRY.info("event=badge_earned user={} badge={} category={}",
                hash(userId), badgeCode, category);
    }

    // ── Diagnostic ──────────────────────────────────────────────────────────────

    /**
     * @param placedTier the tier the diagnostic placed the user into
     *                   (FOUNDATION / PRACTITIONER / EXPERT / CAPSTONE / SKIPPED)
     */
    public void diagnosticCompleted(String userId, String topicId, String placedTier, double score) {
        meterRegistry.counter("arcane.diagnostic.completed",
                "topic", safeTopic(topicId),
                "tier", safeLabel(placedTier)).increment();
        TELEMETRY.info("event=diagnostic_completed user={} topic={} tier={} score={}",
                hash(userId), topicId, placedTier, String.format("%.2f", score));
    }

    // ── Helpers ─────────────────────────────────────────────────────────────────

    /**
     * HMAC-SHA256 of the user id with the configured salt, truncated to 16 hex
     * chars (64 bits — collision probability is negligible at platform scale and
     * keeps log lines compact).
     */
    String hash(String userId) {
        if (userId == null) return "null";
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(hmacKey);
            byte[] sig = mac.doFinal(userId.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(sig).substring(0, 16);
        } catch (Exception e) {
            return "hash-error";
        }
    }

    /**
     * Bound a label to a known low-cardinality set. Anything unrecognised falls
     * to "unknown" so a typo can't blow up Prometheus series count.
     */
    private static String safeTopic(String topicId) {
        if (topicId == null) return "unknown";
        return switch (topicId) {
            case "java", "tailwind", "react" -> topicId;
            default -> "unknown";
        };
    }

    /**
     * Map a chunk id to its parent topic via the seeder naming convention:
     * <ul>
     *   <li>{@code chunk-*} or {@code chunk-cap} → java</li>
     *   <li>{@code tw-*} → tailwind</li>
     *   <li>{@code rx-*} → react</li>
     * </ul>
     * Anything else returns {@code unknown}.
     */
    static String topicFromChunkId(String chunkId) {
        if (chunkId == null) return "unknown";
        if (chunkId.startsWith("chunk-")) return "java";
        if (chunkId.startsWith("tw-"))    return "tailwind";
        if (chunkId.startsWith("rx-"))    return "react";
        return "unknown";
    }

    private static String safeCadence(String cadence) {
        if (cadence == null) return "unknown";
        return switch (cadence.toUpperCase()) {
            case "DAILY", "WEEKLY", "MONTHLY", "RETRIEVAL" -> cadence.toUpperCase();
            default -> "unknown";
        };
    }

    /** Defensive fallback for arbitrary string labels (badge category, tier). */
    private static String safeLabel(String value) {
        if (value == null || value.isBlank()) return "unknown";
        // Strip anything that could blow up Prometheus parsing
        return value.replaceAll("[^A-Za-z0-9_]", "_");
    }
}
