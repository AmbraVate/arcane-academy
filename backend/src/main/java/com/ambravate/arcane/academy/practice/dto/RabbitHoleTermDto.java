package com.ambravate.arcane.academy.practice.dto;

import com.ambravate.arcane.academy.common.domain.UserRabbitHoleTerm;
import lombok.Data;

import java.time.Instant;

@Data
public class RabbitHoleTermDto {
    private String id;
    private String term;
    private String description;
    private String lessonId;
    private String domainId;
    private Instant savedAt;

    public static RabbitHoleTermDto from(UserRabbitHoleTerm e) {
        RabbitHoleTermDto dto = new RabbitHoleTermDto();
        dto.id = e.getId();
        dto.term = e.getTerm();
        dto.description = e.getDescription();
        dto.lessonId = e.getLessonId();
        dto.domainId = e.getDomainId();
        dto.savedAt = e.getSavedAt();
        return dto;
    }
}
