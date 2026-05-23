package com.ambravate.arcane.academy.notes.controller;

import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.notes.dto.NoteDto;
import com.ambravate.arcane.academy.notes.dto.SaveNoteRequest;
import com.ambravate.arcane.academy.notes.service.UserNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class UserNoteController {

    private final UserNoteService noteService;

    @GetMapping
    public ResponseEntity<List<NoteDto>> listNotes(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok(noteService.listNotes(user.getId()));
    }

    @PostMapping
    public ResponseEntity<NoteDto> saveNote(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody @Valid SaveNoteRequest req
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.save(user.getId(), req));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable String noteId
    ) {
        noteService.delete(user.getId(), noteId);
        return ResponseEntity.noContent().build();
    }
}
