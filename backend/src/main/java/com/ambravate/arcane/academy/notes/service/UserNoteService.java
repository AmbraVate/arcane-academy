package com.ambravate.arcane.academy.notes.service;

import com.ambravate.arcane.academy.notes.domain.UserNote;
import com.ambravate.arcane.academy.notes.domain.UserNoteRepository;
import com.ambravate.arcane.academy.notes.dto.NoteDto;
import com.ambravate.arcane.academy.notes.dto.SaveNoteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserNoteService {

    private final UserNoteRepository noteRepo;

    /** Returns all notes for the given user, newest first. */
    @Transactional(readOnly = true)
    public List<NoteDto> listNotes(String userId) {
        return noteRepo.findByUserIdOrderByUpdatedAtDesc(userId)
                .stream()
                .map(NoteDto::from)
                .toList();
    }

    /**
     * Creates or updates the note for a given sub-chunk.
     * Only one note per user per sub-chunk is kept (upsert semantics).
     */
    @Transactional
    public NoteDto save(String userId, SaveNoteRequest req) {
        UserNote note = noteRepo.findByUserIdAndLessonId(userId, req.lessonId())
                .orElseGet(() -> UserNote.builder()
                        .userId(userId)
                        .lessonId(req.lessonId())
                        .moduleId(req.moduleId())
                        .build());

        note.setTitle(req.title());
        note.setContent(req.content());
        note.setModuleId(req.moduleId());

        return NoteDto.from(noteRepo.save(note));
    }

    /** Deletes a note owned by the given user. Throws 404 if not found or not owned. */
    @Transactional
    public void delete(String userId, String noteId) {
        UserNote note = noteRepo.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
        noteRepo.delete(note);
    }

    /** Returns note count for a user (used by admin aggregate stats). */
    @Transactional(readOnly = true)
    public long countByUser(String userId) {
        return noteRepo.countByUserId(userId);
    }
}
