package com.ambravate.arcane.academy.ai.service;

import com.ambravate.arcane.academy.ai.domain.FeynmanResult;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;

import com.ambravate.arcane.academy.common.repository.SubChunkRepository;
import com.ambravate.arcane.academy.common.repository.UserChunkProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeynmanService {

    private final SubChunkRepository subChunkRepository;
    private final UserChunkProgressRepository progressRepository;

    // Key terms per chunk topic for pattern-matching evaluation
    private static final Map<String, List<String>> KEY_TERMS = Map.ofEntries(
            Map.entry("A", List.of("variable", "type", "int", "string", "declare", "value", "assign", "store", "data")),
            Map.entry("B", List.of("if", "else", "condition", "boolean", "operator", "compare", "switch", "true", "false")),
            Map.entry("C", List.of("loop", "for", "while", "iterate", "repeat", "counter", "break", "continue")),
            Map.entry("D", List.of("method", "function", "parameter", "return", "call", "argument", "void", "overload")),
            Map.entry("E", List.of("array", "list", "collection", "index", "element", "size", "add", "iterate")),
            Map.entry("F", List.of("class", "object", "instance", "constructor", "field", "new", "blueprint", "this")),
            Map.entry("G", List.of("encapsulation", "private", "public", "getter", "setter", "access", "hide", "protect")),
            Map.entry("H", List.of("inheritance", "extends", "super", "parent", "child", "override", "subclass")),
            Map.entry("I", List.of("polymorphism", "abstract", "interface", "implement", "override", "dynamic", "shape")),
            Map.entry("J", List.of("exception", "try", "catch", "throw", "error", "handle", "finally", "checked")),
            Map.entry("K", List.of("api", "utility", "collections", "math", "string", "library", "method", "static"))
    );

    /**
     * Get the Feynman prompt for a sub-chunk.
     */
    public String getPrompt(String subChunkId) {
        SubChunk sc = subChunkRepository.findById(subChunkId)
                .orElseThrow(() -> new NoSuchElementException("SubChunk not found: " + subChunkId));
        if (sc.getFeynmanPrompt() != null && !sc.getFeynmanPrompt().isBlank()) {
            return sc.getFeynmanPrompt();
        }
        return "Explain " + sc.getTitle() + " in your own words as if teaching someone who has never programmed. " +
               "Use at least one analogy. Write 3-5 sentences.";
    }

    /**
     * Evaluate a Feynman explanation using pattern-matching scoring.
     * Scores on: accuracy (key terms present), completeness (coverage),
     * simplicity (jargon-free readability), connection (analogies/examples).
     */
    @Transactional
    public FeynmanResult evaluateExplanation(String userId, String subChunkId, String explanation) {
        SubChunk sc = subChunkRepository.findById(subChunkId).orElseThrow();
        String chunkId = sc.getChunkId();

        String lower = explanation.toLowerCase();
        String[] words = lower.split("\\s+");
        int wordCount = words.length;

        // 1. Accuracy: What percentage of key terms are mentioned?
        List<String> terms = KEY_TERMS.getOrDefault(chunkId, List.of());
        long termsFound = terms.stream().filter(lower::contains).count();
        double accuracy = terms.isEmpty() ? 0.5 : Math.min(1.0, (double) termsFound / (terms.size() * 0.6));

        // 2. Completeness: Is it long enough? (50-200 words is ideal)
        double completeness;
        if (wordCount < 20) completeness = 0.2;
        else if (wordCount < 50) completeness = 0.5;
        else if (wordCount <= 200) completeness = 1.0;
        else completeness = 0.8; // Too verbose

        // 3. Simplicity: Does it avoid excessive jargon? Lower jargon density = better
        List<String> jargon = List.of("polymorphism", "encapsulation", "abstraction", "inheritance",
                "instantiate", "dereference", "heap", "stack frame", "bytecode", "JVM");
        long jargonCount = jargon.stream().filter(j -> lower.contains(j.toLowerCase())).count();
        double simplicity = Math.max(0.3, 1.0 - (jargonCount * 0.15));

        // 4. Connection: Does it use analogies or examples?
        boolean hasAnalogy = Pattern.compile("\\b(like|similar to|imagine|think of|analogy|compared to|just as|picture)\\b")
                .matcher(lower).find();
        boolean hasExample = Pattern.compile("\\b(for example|e\\.g\\.|such as|consider|let's say|suppose)\\b")
                .matcher(lower).find();
        boolean hasCodeExample = explanation.contains("```") || explanation.contains("System.out")
                || Pattern.compile("\\b(int|String|class|void)\\s+\\w+").matcher(explanation).find();
        double connection = 0.3;
        if (hasAnalogy) connection += 0.3;
        if (hasExample) connection += 0.2;
        if (hasCodeExample) connection += 0.2;
        connection = Math.min(1.0, connection);

        double overall = (accuracy * 0.3 + completeness * 0.2 + simplicity * 0.2 + connection * 0.3);

        // Generate feedback
        StringBuilder feedback = new StringBuilder();
        if (accuracy < 0.5) feedback.append("Try mentioning more key concepts related to this topic. ");
        if (completeness < 0.5) feedback.append("Your explanation could use more detail. ");
        if (simplicity < 0.5) feedback.append("Great technical depth, but try simplifying for a beginner. ");
        if (connection < 0.5) feedback.append("Adding an analogy or real-world example would strengthen your explanation. ");
        if (overall >= 0.7) feedback.append("Well done! Your explanation shows solid understanding.");

        // Update progress
        UserChunkProgress progress = progressRepository.findByUserIdAndSubChunkId(userId, subChunkId).orElse(null);
        if (progress != null) {
            progress.setFeynmanCompleted(true);
            progress.setFeynmanScore(overall);
            progressRepository.save(progress);
        }

        int xpEarned = overall >= 0.5 ? 15 : 5;

        log.info("[Feynman] Evaluated | user={} subChunk={} overall={} accuracy={} completeness={} simplicity={} connection={}",
                userId, subChunkId, overall, accuracy, completeness, simplicity, connection);

        return new FeynmanResult(accuracy, completeness, simplicity, connection, overall,
                feedback.toString().trim(), xpEarned);
    }

}
