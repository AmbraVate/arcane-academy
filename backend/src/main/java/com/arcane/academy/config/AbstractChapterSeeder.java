package com.arcane.academy.config;

import com.arcane.academy.model.Quest;
import com.arcane.academy.repository.QuestRepository;

public abstract class AbstractChapterSeeder {

    protected final QuestRepository questRepository;

    protected AbstractChapterSeeder(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }

    public abstract void seed();

    // ── Helpers ───────────────────────────────────────────────────────────────

    protected void q(String id, String title, String eyebrow, String topic,
                     int chapter, int order, int xp, String file,
                     String story, String problem, String hint, String starter,
                     String win, String tests) {
        questRepository.save(Quest.builder()
            .id(id).title(title).eyebrow(eyebrow).topic(topic)
            .chapterNumber(chapter).orderInChapter(order).xpReward(xp).filename(file)
            .storyJson(story).problemHtml(problem).hint(hint)
            .starterCode(starter).winStory(win).testCasesJson(tests)
            .build());
    }

    /** Side quest — same as q() but marks the quest as a side quest (bonus enrichment content). */
    protected void sq(String id, String title, String eyebrow, String topic,
                      int chapter, int order, int xp, String file,
                      String story, String problem, String hint, String starter,
                      String win, String tests) {
        questRepository.save(Quest.builder()
            .id(id).title(title).eyebrow(eyebrow).topic(topic)
            .chapterNumber(chapter).orderInChapter(order).xpReward(xp).filename(file)
            .storyJson(story).problemHtml(problem).hint(hint)
            .starterCode(starter).winStory(win).testCasesJson(tests)
            .sideQuest(true)
            .build());
    }

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

    protected String tests(String... ts) { return "[" + String.join(",", ts) + "]"; }

    protected String test(String label, String input, String expected) {
        return "{\"label\":\"" + label + "\",\"input\":"
                + ("null".equals(input) ? "null" : "\"" + esc(input) + "\"")
                + ",\"expected\":\"" + esc(expected) + "\"}";
    }

    protected String esc(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\t", "\\t");
    }
}
