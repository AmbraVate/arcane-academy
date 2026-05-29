package com.ambravate.arcane.academy.common.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTOs for deserialising chunk content from JSON resource files
 * (src/main/resources/content/{domainId}/{chunkId}.json).
 *
 * Fields are public so Jackson can populate them without annotations.
 * All fields are optional / nullable unless noted.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChunkContentDto {

    /** Required. E.g. "tw-a" */
    public String id;
    /** Required. Display title. */
    public String title;
    /** Emoji glyph shown next to the title. */
    public String glyph;
    /** Sort position within the topic. */
    public int sortOrder;
    /** FOUNDATION | PRACTITIONER | EXPERT â€" also accepted as "learnerPath" */
    @JsonAlias("learnerPath")
    public String tier;
    /** E.g. "java" or "psychology" — accepts both "topicId" (legacy) and "domainId" (new) */
    @JsonAlias({"topicId", "domainId"})
    public String domainId;
    /** IDs of modules that must be completed first. Also accepted as "prerequisiteIds". */
    @JsonAlias("prerequisiteIds")
    public List<String> prerequisites;

    /** Also accepted as "lessons" (blueprint terminology). */
    @JsonAlias("lessons")
    public List<SubChunkDto> subChunks;
    public List<RabbitHoleDto> rabbitHoles;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubChunkDto {
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public SubChunkDto(String feynmanPrompt) {
            this.feynmanPrompt = feynmanPrompt;
        }

        public SubChunkDto() {}

        /** Required. E.g. "tw-a1" */
        public String id;
        public String title;
        public int sortOrder;
        public int xpReward;
        /** File shown in the editor header. E.g. "index.html" â€" also accepted as "practiceFileName" */
        @JsonAlias("practiceFileName")
        public String filename;
        /** JAVA | TAILWIND | NONE */
        public String practiceType;

        /** Short teaser shown before the lesson begins. HTML string. */
        public String hookHtml;
        /**
         * Main lesson body. HTML string.
         * Rendered after the story beats in the EXPLANATION phase.
         */
        public String explanationHtml;
        /**
         * Story beats rendered before explanationHtml.
         * Each object must have a "type" key: "narration" | "dialogue" | "example".
         * Dialogue objects also carry: av, cls, speaker, sCls, text.
         * Example objects carry: speaker (label), text (code).
         */
        public List<Map<String, Object>> storyBeats;

        /** Task description for the GUIDED_PRACTICE phase. HTML string. */
        public String guidedPracticeHtml;
        /** Starter code placed in the editor. */
        public String guidedPracticeStarterCode;
        /**
         * Test specifications.
         * Java tests:     { "label": "...", "input": "...", "expected": "..." }
         * Tailwind tests: { "label": "...", "selector": "...", "requiredClass": "..." }
         */
        public List<Map<String, Object>> guidedPracticeTests;

        /**
         * Task description for the SOLO_PRACTICE phase (scratch rebuild, no starter code).
         * If null or blank, the SOLO_PRACTICE phase is skipped automatically.
         * HTML string.
         */
        public String soloPracticeHtml;

        /** Optional Feynman-technique prompt shown on the COMPLETE phase. */
        public String feynmanPrompt;

        /**
         * Optional model answer / exemplar response revealed after solo practice is complete.
         * Use for written-response (practiceType: NONE) sub-chunks to give the learner a
         * benchmark after their independent attempt. Should be concise (150â€"300 words) and
         * demonstrate the vocabulary, structure, and critical evaluation expected at this tier.
         */
        public String modelAnswer;

        /**
         * Optional exemplar response revealed after guided practice is passed (NONE practice type).
         * Shown before solo practice so the student can benchmark their guided attempt.
         */
        public String guidedPracticeModelAnswer;

        public List<QuestionDto> questions;

        /**
         * Terms to highlight as rabbit-hole anchors in story beats.
         * Each entry: { "term": "autoboxing", "description": "Java automatically converts..." }
         */
        public List<Map<String, Object>> rabbitHoleTerms;

        // â"€â"€ Structured lesson metadata (V15) â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€

        /**
         * Learning objectives shown before story beats begin.
         * Plain-text strings: "Understand X", "Apply Y correctly".
         */
        public List<String> learningObjectives;

        /**
         * A harder challenge exercise available after solo practice.
         * Uses the same sandbox (practiceType) as the guided/solo exercises.
         */
        public ChallengeDto challenge;

        /**
         * HTML description of an open-ended mini project.
         * No sandbox â€" conceptual/design prompt. Shown after the challenge.
         */
        public String miniProject;

        /**
         * Common pitfalls and misconceptions about this topic.
         * Plain-text strings shown at the end of the sub-chunk.
         */
        public List<String> commonMistakes;

        /**
         * Assessment criteria ("Can X", "Writes Y correctly").
         * Plain-text checklist shown at the end of the sub-chunk.
         */
        public List<String> assessmentCriteria;

        /**
         * Downloadable resources shown as chips at the start of the EXPLANATION phase.
         * Each entry: { "title": "...", "type": "pdf", "url": "/downloads/filename.pdf" }
         */
        public List<Map<String, Object>> downloadables;

        /**
         * Cross-domain connection prompt shown in the INTEGRATION phase (between retrieval and complete).
         * Asks the learner to link this concept to other domains they know.
         * If null or blank, the INTEGRATION phase is skipped automatically.
         */
        public String integrationPrompt;

        /**
         * Blueprint quest classification: KNOWLEDGE | GUIDED | PRACTICE | INVESTIGATION | SYNTHESIS | MASTERY.
         * Defaults to null (unclassified) if omitted.
         */
        public String questType;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ChallengeDto {
            /** Task description. HTML string. */
            public String html;
            /** Starter code placed in the editor. */
            public String starterCode;
            /** Same shape as guidedPracticeTests. */
            public List<Map<String, Object>> tests;
        }

        /**
         * Accepts the nested "guidedPractice" object used in legacy JSON files:
         * { "problemHtml": "...", "hint": "...", "starterCode": "...", "tests": [...] }
         * Maps into the flat guidedPractice* fields if they haven't been set directly.
         */
        @JsonSetter("guidedPractice")
        public void setGuidedPracticeFromNested(GuidedPracticeDto gp) {
            if (gp == null) return;
            if (guidedPracticeHtml == null)         guidedPracticeHtml = gp.problemHtml;
            if (guidedPracticeStarterCode == null)  guidedPracticeStarterCode = gp.starterCode;
            if (guidedPracticeTests == null)        guidedPracticeTests = gp.tests;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class GuidedPracticeDto {
            public String problemHtml;
            public String hint;
            public String starterCode;
            public List<Map<String, Object>> tests;
        }
    }

    // â"€â"€ Question â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QuestionDto {
        /**
         * Required. One of:
         * MULTIPLE_CHOICE | TRUE_FALSE | FILL_BLANK |
         * CODE_COMPLETION | WHATS_THE_OUTPUT | DEBUGGING | SCENARIO | COMPARE_CONTRAST | DISCRIMINATION
         */
        public String type;
        /** RECALL | APPLICATION | DISCRIMINATION â€" also accepted as "difficulty". */
        @JsonAlias("difficulty")
        public String tier;
        /** Question body. HTML string â€" also accepted as "prompt". */
        @JsonAlias({"prompt", "question"})
        public String questionHtml;
        /** Optional code block shown alongside the question. */
        public String codeSnippet;
        /** Answer choices. For TRUE_FALSE this can be omitted â€" loader fills ["True","False"]. */
        public List<String> options;
        /** The correct answer text â€" also accepted as "answer". Mutually exclusive with correctIndex. */
        @JsonAlias("answer")
        public String correctAnswer;
        /**
         * Zero-based index into options[] of the correct answer.
         * Alternative to correctAnswer for MULTIPLE_CHOICE questions.
         * The seeder resolves this to the option text before persisting.
         */
        public Integer correctIndex;
        /** Shown after the learner answers. HTML string â€" also accepted as "explanation". */
        @JsonAlias("explanation")
        public String explanationHtml;
        /** IDs of other sub-chunks that relate to this question (SCENARIO only). */
        public List<String> crossChunkIds;
    }

    // â"€â"€ Rabbit hole â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€â"€

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RabbitHoleDto {
        /** Required. Globally unique. E.g. "tw-a-rh1" */
        public String id;
        public String title;
        public int sortOrder;
        public String filename;
        /** Deep-dive content. HTML string. */
        public String contentHtml;
        /** Same shape as SubChunkDto.storyBeats. */
        public List<Map<String, Object>> storyBeats;
        public String starterCode;
        /** Same shape as SubChunkDto.guidedPracticeTests. */
        public List<Map<String, Object>> tests;
    }
}
