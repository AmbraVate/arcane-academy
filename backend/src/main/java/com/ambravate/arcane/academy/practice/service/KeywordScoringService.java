package com.ambravate.arcane.academy.practice.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * Scores a free-text answer against a list of expected keywords.
 *
 * <h2>Band thresholds</h2>
 * <ul>
 *   <li>{@code WEAK}      — fewer than 33 % of keywords matched</li>
 *   <li>{@code GOOD}      — 33 – 66 % matched</li>
 *   <li>{@code EXCELLENT} — 67 % or more matched</li>
 * </ul>
 *
 * <p>Matching is case-insensitive substring search.
 * GOOD and EXCELLENT bands count as a passing submission.
 */
@Service
public class KeywordScoringService {

    /** Monthly AI-review quota per user per domain. */
    public static final int AI_REVIEW_MONTHLY_QUOTA = 3;

    public enum Band { WEAK, GOOD, EXCELLENT }

    /**
     * Holds the scoring result.
     *
     * @param matched         number of keywords found
     * @param total           total keywords evaluated
     * @param band            scoring band
     * @param matchedKeywords the subset of keywords that were found
     */
    public record KeywordScore(int matched, int total, Band band, List<String> matchedKeywords) {
        /** True when the band is GOOD or EXCELLENT. */
        public boolean isPassing() {
            return band == Band.GOOD || band == Band.EXCELLENT;
        }
    }

    /**
     * Scores {@code answer} against {@code keywords}.
     *
     * @param answer   the learner's free-text response (may be null/blank)
     * @param keywords expected keywords (nulls / blanks are skipped)
     * @return a {@link KeywordScore} — never null
     */
    public KeywordScore score(String answer, List<String> keywords) {
        if (answer == null || answer.isBlank()
                || keywords == null || keywords.isEmpty()) {
            return new KeywordScore(0, keywords == null ? 0 : keywords.size(),
                    Band.WEAK, List.of());
        }

        String lower = answer.toLowerCase(Locale.ROOT);

        List<String> matched = keywords.stream()
                .filter(k -> k != null && !k.isBlank())
                .filter(k -> lower.contains(k.toLowerCase(Locale.ROOT)))
                .toList();

        int total = (int) keywords.stream()
                .filter(k -> k != null && !k.isBlank())
                .count();

        Band band = computeBand(matched.size(), total);
        return new KeywordScore(matched.size(), total, band, matched);
    }

    // ── private ───────────────────────────────────────────────────────────────

    private static Band computeBand(int matched, int total) {
        if (total == 0) return Band.WEAK;
        double ratio = (double) matched / total;
        if (ratio >= 0.67) return Band.EXCELLENT;
        if (ratio >= 0.33) return Band.GOOD;
        return Band.WEAK;
    }
}
