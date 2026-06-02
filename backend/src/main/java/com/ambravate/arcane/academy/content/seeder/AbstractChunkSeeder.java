package com.ambravate.arcane.academy.content.seeder;

import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.common.domain.Question;
import com.ambravate.arcane.academy.common.domain.QuestionTier;
import com.ambravate.arcane.academy.common.domain.QuestionType;
import com.ambravate.arcane.academy.content.domain.RabbitHoleModule;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonPracticeType;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.QuestionRepository;
import com.ambravate.arcane.academy.content.repository.RabbitHoleModuleRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;


public abstract class AbstractChunkSeeder {

    protected final LearningModuleRepository moduleRepository;
    protected final LessonRepository lessonRepository;
    protected final QuestionRepository questionRepository;
    protected final RabbitHoleModuleRepository rabbitHoleRepository;

    protected AbstractChunkSeeder(LearningModuleRepository moduleRepository,
                                   LessonRepository lessonRepository,
                                   QuestionRepository questionRepository,
                                   RabbitHoleModuleRepository rabbitHoleRepository) {
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
        this.questionRepository = questionRepository;
        this.rabbitHoleRepository = rabbitHoleRepository;
    }

    public abstract void seed();

    // â”€â”€ LearningModule helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    protected LearningModule chunk(String id, String title, String glyph, int order, String... prereqs) {
        return chunk(id, title, glyph, order, LearnerPath.APPRENTICE, prereqs);
    }

    protected LearningModule chunk(String id, String title, String glyph, int order, LearnerPath tier, String... prereqs) {
        java.util.List<LearningModule> prereqChunks = java.util.Arrays.stream(prereqs)
                .map(pId -> moduleRepository.findById(pId)
                        .orElseThrow(() -> new IllegalStateException("Prerequisite chunk not found: " + pId)))
                .collect(java.util.stream.Collectors.toList());
        LearningModule c = LearningModule.builder().id(id).title(title).glyph(glyph)
                .sortOrder(order).tier(tier).prerequisites(prereqChunks).build();
        return moduleRepository.save(c);
    }

    // â”€â”€ Lesson helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    protected Lesson subChunk(String id, String chunkId, String title, int order, int xp,
                                 String filename, String hook, String explanation,
                                 String story, String gpHtml, String gpStarter, String gpTests,
                                 String feynmanPrompt) {
        return subChunk(id, chunkId, title, order, xp, filename, hook, explanation,
                story, gpHtml, gpStarter, gpTests, feynmanPrompt, LessonPracticeType.JAVA);
    }

    protected Lesson subChunk(String id, String chunkId, String title, int order, int xp,
                                 String filename, String hook, String explanation,
                                 String story, String gpHtml, String gpStarter, String gpTests,
                                 String feynmanPrompt, LessonPracticeType practiceType) {
        Lesson sc = Lesson.builder()
                .id(id).moduleId(chunkId).title(title).sortOrder(order).xpReward(xp)
                .filename(filename).hookHtml(hook).explanationHtml(explanation)
                .storyJson(story).guidedPracticeHtml(gpHtml)
                .guidedPracticeStarterCode(gpStarter).guidedPracticeTestsJson(gpTests)
                .feynmanPrompt(feynmanPrompt)
                .practiceType(practiceType)
                .build();
        return lessonRepository.save(sc);
    }

    // â”€â”€ Question helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    protected Question mcQuestion(String lessonId, QuestionTier tier, String html,
                                   String[] options, String correct, String explanation) {
        return questionRepository.save(Question.builder()
                .lessonId(lessonId).tier(tier).type(QuestionType.MULTIPLE_CHOICE)
                .questionHtml(html)
                .optionsJson("[" + String.join(",", java.util.Arrays.stream(options).map(o -> "\"" + esc(o) + "\"").toArray(String[]::new)) + "]")
                .correctAnswer(correct).explanationHtml(explanation)
                .minPath(LearnerPath.APPRENTICE).build());
    }

    protected Question tfQuestion(String lessonId, QuestionTier tier, String html,
                                   String correct, String explanation) {
        return questionRepository.save(Question.builder()
                .lessonId(lessonId).tier(tier).type(QuestionType.TRUE_FALSE)
                .questionHtml(html)
                .optionsJson("[\"True\",\"False\"]")
                .correctAnswer(correct).explanationHtml(explanation)
                .minPath(LearnerPath.APPRENTICE).build());
    }

    protected Question fillQuestion(String lessonId, QuestionTier tier, String html,
                                     String correct, String explanation) {
        return questionRepository.save(Question.builder()
                .lessonId(lessonId).tier(tier).type(QuestionType.FILL_BLANK)
                .questionHtml(html).correctAnswer(correct).explanationHtml(explanation)
                .minPath(LearnerPath.APPRENTICE).build());
    }

    protected Question codeQuestion(String lessonId, QuestionTier tier, QuestionType type,
                                     String html, String code, String correct, String explanation) {
        return questionRepository.save(Question.builder()
                .lessonId(lessonId).tier(tier).type(type)
                .questionHtml(html).codeSnippet(code)
                .correctAnswer(correct).explanationHtml(explanation)
                .minPath(LearnerPath.APPRENTICE).build());
    }

    protected Question scenarioQuestion(String lessonId, String html, String correct,
                                          String explanation, String... crossChunkIds) {
        String cross = crossChunkIds.length == 0 ? null :
                "[" + String.join(",", java.util.Arrays.stream(crossChunkIds).map(c -> "\"" + c + "\"").toArray(String[]::new)) + "]";
        return questionRepository.save(Question.builder()
                .lessonId(lessonId).tier(QuestionTier.DISCRIMINATION).type(QuestionType.SCENARIO)
                .questionHtml(html).correctAnswer(correct).explanationHtml(explanation)
                .crossChunkIds(cross).minPath(LearnerPath.JUNIOR).build());
    }

    // â”€â”€ Story beat helpers (same format as original) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

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

    // â”€â”€ Test case helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    protected String tests(String... ts) { return "[" + String.join(",", ts) + "]"; }

    protected String test(String label, String input, String expected) {
        return "{\"label\":\"" + label + "\",\"input\":"
                + ("null".equals(input) ? "null" : "\"" + esc(input) + "\"")
                + ",\"expected\":\"" + esc(expected) + "\"}";
    }

    // â”€â”€ Rabbit hole helper â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    protected RabbitHoleModule rabbitHole(String id, String chunkId, String title, int order,
                                           String contentHtml, String story, String starter,
                                           String testCases, String filename) {
        return rabbitHoleRepository.save(RabbitHoleModule.builder()
                .id(id).moduleId(chunkId).title(title).sortOrder(order)
                .contentHtml(contentHtml).storyJson(story)
                .starterCode(starter).testCasesJson(testCases).filename(filename)
                .build());
    }

    protected String esc(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\t", "\\t");
    }
}
