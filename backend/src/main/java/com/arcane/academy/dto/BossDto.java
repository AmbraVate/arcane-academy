package com.arcane.academy.dto;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BossDto {
    private String id;
    private String name;
    private String glyph;
    private int chapterNumber;
    private int xpReward;
    private String intro;
    private List<Object> questions;
    private boolean completed;
    private boolean locked;
}
