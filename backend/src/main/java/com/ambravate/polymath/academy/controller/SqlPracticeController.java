package com.ambravate.polymath.academy.controller;

import com.ambravate.polymath.academy.dto.SqlSubmitRequest;
import com.ambravate.polymath.academy.dto.SubmitResponse;
import com.ambravate.polymath.academy.security.UserPrincipal;
import com.ambravate.polymath.academy.service.SqlPracticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sql")
@RequiredArgsConstructor
public class SqlPracticeController {

    private final SqlPracticeService sqlPracticeService;

    /**
     * Validates a SQL submission against client-reported in-iframe test results.
     *
     * <p>POST /api/sql/{subChunkId}/submit
     * <br>Body: {@link SqlSubmitRequest}
     */
    @PostMapping("/{subChunkId}/submit")
    public ResponseEntity<SubmitResponse> submit(
            @PathVariable String subChunkId,
            @RequestBody SqlSubmitRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                sqlPracticeService.submit(user.getId(), subChunkId, request));
    }

    /**
     * Solo practice submission — same validation as guided practice.
     * The service has idempotency built-in (won't re-award XP if already passed).
     *
     * <p>POST /api/sql/{subChunkId}/solo-practice/submit
     */
    @PostMapping("/{subChunkId}/solo-practice/submit")
    public ResponseEntity<SubmitResponse> submitSoloPractice(
            @PathVariable String subChunkId,
            @RequestBody SqlSubmitRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                sqlPracticeService.submit(user.getId(), subChunkId, request));
    }
}
