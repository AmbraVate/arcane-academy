package com.ambravate.arcane.academy.admin.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminQuestionDto {
    private String id;
    private String subChunkId;
    private String type;
    private String tier;
    private String questionHtml;
    private String codeSnippet;
    private List<String> options;
    private String correctAnswer;
    private String explanationHtml;
}
