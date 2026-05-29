package com.ambravate.arcane.academy.notes.dto;

import com.ambravate.arcane.academy.notes.domain.UserNote;

import java.time.Instant;

public record NoteDto(
        String id,
        String lessonId,
        String moduleId,
        String title,
        String content,
        Instant createdAt,
        Instant updatedAt
) {
    public static NoteDto from(UserNote note) {
        return new NoteDto(
                note.getId(),
                note.getLessonId(),
                note.getModuleId(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }
}
