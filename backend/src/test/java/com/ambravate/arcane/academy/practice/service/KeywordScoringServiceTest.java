package com.ambravate.arcane.academy.practice.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class KeywordScoringServiceTest {

    private final KeywordScoringService svc = new KeywordScoringService();

    // ── band thresholds ───────────────────────────────────────────────────────

    @Test
    void zeroMatches_isWeak() {
        var result = svc.score("hello world", List.of("variable", "type", "value"));
        assertThat(result.band()).isEqualTo(KeywordScoringService.Band.WEAK);
        assertThat(result.isPassing()).isFalse();
    }

    @Test
    void oneOfThreeMatches_isGood() {
        // 1/3 = 33.3 % which meets the >= 0.33 GOOD threshold
        var result = svc.score("I declare a variable here", List.of("variable", "type", "value"));
        assertThat(result.matched()).isEqualTo(1);
        assertThat(result.band()).isEqualTo(KeywordScoringService.Band.GOOD);
        assertThat(result.isPassing()).isTrue();
    }

    @Test
    void twoOfThreeMatches_isGood() {
        var result = svc.score("A variable stores a value", List.of("variable", "type", "value"));
        assertThat(result.matched()).isEqualTo(2);
        assertThat(result.band()).isEqualTo(KeywordScoringService.Band.GOOD);
        assertThat(result.isPassing()).isTrue();
    }

    @Test
    void allMatches_isExcellent() {
        var result = svc.score("A variable has a type and a value assigned", List.of("variable", "type", "value"));
        assertThat(result.matched()).isEqualTo(3);
        assertThat(result.band()).isEqualTo(KeywordScoringService.Band.EXCELLENT);
        assertThat(result.isPassing()).isTrue();
    }

    @Test
    void zeroOfThreeMatches_isWeak() {
        // 0/3 = 0.0 % — clearly below the GOOD threshold
        var result = svc.score("the code is written", List.of("variable", "type", "value"));
        assertThat(result.band()).isEqualTo(KeywordScoringService.Band.WEAK);
        assertThat(result.isPassing()).isFalse();
    }

    @Test
    void matchingIsCaseInsensitive() {
        var result = svc.score("VARIABLE holds a VALUE", List.of("variable", "value"));
        assertThat(result.matched()).isEqualTo(2);
        assertThat(result.band()).isEqualTo(KeywordScoringService.Band.EXCELLENT);
    }

    @Test
    void matchedKeywordsListIsPopulated() {
        var result = svc.score("a variable has a type", List.of("variable", "type", "value"));
        assertThat(result.matchedKeywords()).containsExactlyInAnyOrder("variable", "type");
    }

    // ── edge cases ────────────────────────────────────────────────────────────

    @Test
    void nullAnswer_returnsWeak() {
        var result = svc.score(null, List.of("variable"));
        assertThat(result.band()).isEqualTo(KeywordScoringService.Band.WEAK);
        assertThat(result.matched()).isEqualTo(0);
    }

    @Test
    void emptyAnswer_returnsWeak() {
        var result = svc.score("   ", List.of("variable"));
        assertThat(result.band()).isEqualTo(KeywordScoringService.Band.WEAK);
    }

    @Test
    void nullKeywords_returnsWeak() {
        var result = svc.score("a variable", null);
        assertThat(result.band()).isEqualTo(KeywordScoringService.Band.WEAK);
        assertThat(result.total()).isEqualTo(0);
    }

    @Test
    void emptyKeywords_returnsWeak() {
        var result = svc.score("a variable", List.of());
        assertThat(result.band()).isEqualTo(KeywordScoringService.Band.WEAK);
    }

    @Test
    void nullKeywordsInList_areIgnored() {
        // List.of() rejects nulls; use Arrays.asList which allows them
        var keywords = new java.util.ArrayList<String>();
        keywords.add("variable");
        keywords.add(null);
        keywords.add("value");
        var result = svc.score("variable value", keywords);
        assertThat(result.total()).isEqualTo(2); // null entry not counted
        assertThat(result.matched()).isEqualTo(2);
        assertThat(result.band()).isEqualTo(KeywordScoringService.Band.EXCELLENT);
    }

    @Test
    void substringMatch_isAllowed() {
        // "variables" contains "variable"
        var result = svc.score("variables are important", List.of("variable"));
        assertThat(result.matched()).isEqualTo(1);
    }
}
