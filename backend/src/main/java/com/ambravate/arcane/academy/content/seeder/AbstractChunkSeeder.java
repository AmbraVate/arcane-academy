package com.ambravate.arcane.academy.content.seeder;

import com.ambravate.arcane.academy.common.domain.Chunk;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.common.domain.QuestionTier;
import com.ambravate.arcane.academy.common.domain.QuestionType;
import com.ambravate.arcane.academy.content.domain.RabbitHoleModule;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkPracticeType;
import com.ambravate.arcane.academy.content.repository.ChunkRepository;
import com.ambravate.arcane.academy.content.repository.QuestionRepository;
import com.ambravate.arcane.academy.content.repository.RabbitHoleModuleRepository;
import com.ambravate.arcane.academy.content.repository.SubChunkRepository;


public abstract class AbstractChunkSeeder {

    protected final ChunkRepository chunkRepository;
    protected final SubChunkRepository subChunkRepository;
    protected final QuestionRepository questionRepository;
    protected final RabbitHoleModuleRepository rabbitHoleRepository;

    protected AbstractChunkSeeder(ChunkRepository chunkRepository,
                                   SubChunkRepository subChunkRepository,
                                   QuestionRepository questionRepository,
                                   RabbitHoleModuleRepository rabbitHoleRepository) {
        this.chunkRepository = chunkRepository;
        this.subChunkRepository = subChunkRepository;
        this.questionRepository = questionRepository;
        this.rabbitHoleRepository = rabbitHoleRepository;
    }

    public abstract void seed();

    // ── Chunk helpers ─────────────────────────────────────────────────────

    protected Chunk chunk(String id, String title, String glyph, int order, String... prereqs) {
        return chunk(id, title, glyph, order, LearnerPath.FOUNDATION, prereqs);
    }

    protected Chunk chunk(String id, String title, String glyph, int order, LearnerPath tier, String... prereqs) {
        String prereqJson = prereqs.length == 0 ? "[]" :
                "[" + String.join(",", java.util.Arrays.stream(prereqs).map(p -> "\"" + p + "\"").toArray(String[]::new)) + "]";
        Chunk c = Chunk.builder().id(id).title(title).glyph(glyph)
                .sortOrder(order).prerequisiteIds(prereqJson).tier(tier).build();
        return chunkRepository.save(c);
    }

    // ── SubChunk helpers ──────────────────────────────────────────────────

    protected SubChunk subChunk(String id, String chunkId, String title, int order, int xp,
                                 String filename, String hook, String explanation,
                                 String story, String gpHtml, String gpStarter, String gpTests,
                                 String feynmanPrompt) {
        return subChunk(id, chunkId, title, order, xp, filename, hook, explanation,
                story, gpHtml, gpStarter, gpTests, feynmanPrompt, SubChunkPracticeType.JAVA);
    }

    protected SubChunk subChunk(String id, String chunkId, String title, int order, int xp,
                                 String filename, String hook, String explanation,
                                 String story, String gpHtml, String gpStarter, String gpTests,
                                 String feynmanPrompt, SubChunkPracticeType practiceType) {
        SubChunk sc = SubChunk.builder()
                .id(id).chunkId(chunkId).title(title).sortOrder(order).xpReward(xp)
                .filename(filename).hookHtml(hook).explanationHtml(explanation)
                .storyJson(story).guidedPracticeHtml(gpHtml)
                .guidedPracticeStarterCode(gpStarter).guidedPracticeTestsJson(gpTests)
                .feynmanPrompt(feynmanPrompt)
                .practiceType(practiceType)
                .build();
        return subChunkRepository.save(sc);
    }

    // ── Question helpers ──────────────────────────────────────────────────

    protected Question mcQuestion(String subChunkId, QuestionTier tier, String html,
                                   String[] options, String correct, String explanation) {
        return questionRepository.save(Question.builder()
                .subChunkId(subChunkId).tier(tier).type(QuestionType.MULTIPLE_CHOICE)
                .questionHtml(html)
                .optionsJson("[" + String.join(",", java.util.Arrays.stream(options).map(o -> "\"" + esc(o) + "\"").toArray(String[]::new)) + "]")
                .correctAnswer(correct).explanationHtml(explanation)
                .minPath(LearnerPath.FOUNDATION).build());
    }

    protected Question tfQuestion(String subChunkId, QuestionTier tier, String html,
                                   String correct, String explanation) {
        return questionRepository.save(Question.builder()
                .subChunkId(subChunkId).tier(tier).type(QuestionType.TRUE_FALSE)
                .questionHtml(html)
                .optionsJson("[\"True\",\"False\"]")
                .correctAnswer(correct).explanationHtml(explanation)
                .minPath(LearnerPath.FOUNDATION).build());
    }

    protected Question fillQuestion(String subChunkId, QuestionTier tier, String html,
                                     String correct, String explanation) {
        return questionRepository.save(Question.builder()
                .subChunkId(subChunkId).tier(tier).type(QuestionType.FILL_BLANK)
                .questionHtml(html).correctAnswer(correct).explanationHtml(explanation)
                .minPath(LearnerPath.FOUNDATION).build());
    }

    protected Question codeQuestion(String subChunkId, QuestionTier tier, QuestionType type,
                                     String html, String code, String correct, String explanation) {
        return questionRepository.save(Question.builder()
                .subChunkId(subChunkId).tier(tier).type(type)
                .questionHtml(html).codeSnippet(code)
                .correctAnswer(correct).explanationHtml(explanation)
                .minPath(LearnerPath.FOUNDATION).build());
    }

    protected Question scenarioQuestion(String subChunkId, String html, String correct,
                                          String explanation, String... crossChunkIds) {
        String cross = crossChunkIds.length == 0 ? null :
                "[" + String.join(",", java.util.Arrays.stream(crossChunkIds).map(c -> "\"" + c + "\"").toArray(String[]::new)) + "]";
        return questionRepository.save(Question.builder()
                .subChunkId(subChunkId).tier(QuestionTier.DISCRIMINATION).type(QuestionType.SCENARIO)
                .questionHtml(html).correctAnswer(correct).explanationHtml(explanation)
                .crossChunkIds(cross).minPath(LearnerPath.PRACTITIONER).build());
    }

    // ── Story beat helpers (same format as original) ──────────────────────

    protected String story(String... beats) { return "[" + String.join(",", beats) + "]"; }

    protected String n(String t) {
        return "{\"type\":\"narration\",\"text\":\"" + esc(t) + "\"}";
    }

    protected String d(String av, String cls, String sp, String sCls, String t) {
        return "{\"type\":\"dialogue\",\"av\":\"" + av + "\",\"cls\":\"" + cls + "\",\"speaker\":\""
                + sp + "\",\"sCls\":\"" + sCls + "\",\"text\":\"" + esc(t) + "\"}";
    }

    protected String e(String label, String code) {
        return "{\"type\":\"example\",\"speaker\":\"" + esc(label) + "\",\"text\":\"" + esc(code) + "\"}";
    }

    // ── Test case helpers ─────────────────────────────────────────────────

    protected String tests(String... ts) { return "[" + String.join(",", ts) + "]"; }

    protected String test(String label, String input, String expected) {
        return "{\"label\":\"" + label + "\",\"input\":"
                + ("null".equals(input) ? "null" : "\"" + esc(input) + "\"")
                + ",\"expected\":\"" + esc(expected) + "\"}";
    }

    // ── Rabbit hole helper ────────────────────────────────────────────────

    protected RabbitHoleModule rabbitHole(String id, String chunkId, String title, int order,
                                           String contentHtml, String story, String starter,
                                           String testCases, String filename) {
        return rabbitHoleRepository.save(RabbitHoleModule.builder()
                .id(id).chunkId(chunkId).title(title).sortOrder(order)
                .contentHtml(contentHtml).storyJson(story)
                .starterCode(starter).testCasesJson(testCases).filename(filename)
                .build());
    }

    protected String esc(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\t", "\\t");
    }
}
