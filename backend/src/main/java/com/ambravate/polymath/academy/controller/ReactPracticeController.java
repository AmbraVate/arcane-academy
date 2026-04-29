package com.ambravate.polymath.academy.controller;

import com.ambravate.polymath.academy.dto.ReactSubmitRequest;
import com.ambravate.polymath.academy.dto.SubmitResponse;
import com.ambravate.polymath.academy.security.UserPrincipal;
import com.ambravate.polymath.academy.service.ReactPracticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/react")
@RequiredArgsConstructor
public class ReactPracticeController {

    private final ReactPracticeService reactPracticeService;

    /**
     * Validates a React JSX submission against client-reported in-iframe test results.
     *
     * <p>POST /api/react/{subChunkId}/submit
     * <br>Body: {@link ReactSubmitRequest}
     */
    @PostMapping("/{subChunkId}/submit")
    public ResponseEntity<SubmitResponse> submit(
            @PathVariable String subChunkId,
            @RequestBody ReactSubmitRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                reactPracticeService.submit(user.getId(), subChunkId, request));
    }

    /**
     * Solo practice submission — same validation as guided practice.
     * The service has idempotency built-in (won't re-award XP if already passed).
     *
     * <p>POST /api/react/{subChunkId}/solo-practice/submit
     */
    @PostMapping("/{subChunkId}/solo-practice/submit")
    public ResponseEntity<SubmitResponse> submitSoloPractice(
            @PathVariable String subChunkId,
            @RequestBody ReactSubmitRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                reactPracticeService.submit(user.getId(), subChunkId, request));
    }
}
