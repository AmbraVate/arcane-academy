package com.ambravate.arcane.academy.practice.dto;

import com.ambravate.arcane.academy.common.dto.QuestionDto;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SubChunkEncodingDto {
    private String subChunkId;
    private String chunkId;
    private String topicId;
    private String title;
    private String phase;
    private String status;

    // Phase content — only populated for the current phase
    private String hookHtml;
    private String explanationHtml;
    private Object storyBeats;
    private String guidedPracticeHtml;
    private String soloPracticeHtml;
    private String starterCode;
    private Object testCaseLabels;
    private String filename;
    private List<QuestionDto> retrievalQuestions;
    private String feynmanPrompt;
    private int xpReward;
    /** "JAVA" | "TAILWIND" | "NONE" */
    private String practiceType;

    /** Terms to highlight as rabbit-hole anchors in story beats: [{ term, description }] */
    private Object rabbitHoleTerms;

    /**
     * Exemplar answer revealed after solo practice is complete (NONE practice type only).
     * Only populated during the SOLO_PRACTICE phase once the learner has submitted.
     * Null for all other phases and for non-written practice types.
     */
    private String modelAnswer;

    /**
     * Exemplar answer revealed after guided practice is passed (NONE practice type only).
     * Populated during the GUIDED_PRACTICE phase once the learner has passed.
     * Null for all other phases and for non-written practice types.
     */
    private String guidedPracticeModelAnswer;
}
