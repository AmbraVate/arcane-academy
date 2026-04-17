package com.ambravate.polymath.academy.controller;

import com.ambravate.polymath.academy.dto.SubmitResponse;
import com.ambravate.polymath.academy.dto.TailwindSubmitRequest;
import com.ambravate.polymath.academy.security.UserPrincipal;
import com.ambravate.polymath.academy.service.TailwindPracticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tailwind")
@RequiredArgsConstructor
public class TailwindPracticeController {

    private final TailwindPracticeService tailwindPracticeService;

    /**
     * Validates a student's Tailwind HTML submission.
     *
     * POST /api/tailwind/{subChunkId}/submit
     * Body: { "html": "<div class=\"bg-white p-4\">...</div>" }
     */
    @PostMapping("/{subChunkId}/submit")
    public ResponseEntity<SubmitResponse> submit(
            @PathVariable String subChunkId,
            @RequestBody TailwindSubmitRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                tailwindPracticeService.submit(user.getId(), subChunkId, request.getHtml()));
    }
}
