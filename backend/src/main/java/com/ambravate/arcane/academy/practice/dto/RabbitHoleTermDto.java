package com.ambravate.arcane.academy.practice.dto;

import com.ambravate.arcane.academy.common.domain.UserRabbitHoleTerm;
import lombok.Data;

import java.time.Instant;

@Data
public class RabbitHoleTermDto {
    private String id;
    private String term;
    private String description;
    private String subChunkId;
    private String topicId;
    private Instant savedAt;

    public static RabbitHoleTermDto from(UserRabbitHoleTerm e) {
        RabbitHoleTermDto dto = new RabbitHoleTermDto();
        dto.id = e.getId();
        dto.term = e.getTerm();
        dto.description = e.getDescription();
        dto.subChunkId = e.getSubChunkId();
        dto.topicId = e.getTopicId();
        dto.savedAt = e.getSavedAt();
        return dto;
    }
}
