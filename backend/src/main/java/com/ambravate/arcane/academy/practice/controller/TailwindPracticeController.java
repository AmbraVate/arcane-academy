package com.ambravate.arcane.academy.practice.controller;

import com.ambravate.arcane.academy.common.dto.SubmitResponse;
import com.ambravate.arcane.academy.common.dto.TailwindSubmitRequest;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.practice.service.TailwindPracticeService;
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

    /**
     * Solo practice submission — same validation as guided practice.
     * TailwindPracticeService.submit() has idempotency built-in (won't re-award XP
     * if the learner already passed guided practice).
     *
     * POST /api/tailwind/{subChunkId}/solo-practice/submit
     * Body: { "html": "<div class=\"bg-white p-4\">...</div>" }
     */
    @PostMapping("/{subChunkId}/solo-practice/submit")
    public ResponseEntity<SubmitResponse> submitSoloPractice(
            @PathVariable String subChunkId,
            @RequestBody TailwindSubmitRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                tailwindPracticeService.submit(user.getId(), subChunkId, request.getHtml()));
    }
}
