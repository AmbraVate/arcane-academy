package com.ambravate.polymath.academy.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuestionDto {
    private String id;
    private String tier;
    private String type;
    private String questionHtml;
    private String codeSnippet;
    private List<String> options;
}
