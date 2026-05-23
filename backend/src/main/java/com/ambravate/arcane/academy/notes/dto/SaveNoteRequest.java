package com.ambravate.arcane.academy.notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SaveNoteRequest(
        @NotBlank String subChunkId,
        @NotBlank String chunkId,
        @NotBlank @Size(max = 500) String title,
        @NotBlank String content
) {}
