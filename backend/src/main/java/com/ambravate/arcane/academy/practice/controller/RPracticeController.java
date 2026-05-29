package com.ambravate.arcane.academy.practice.controller;

import com.ambravate.arcane.academy.practice.dto.RSubmitRequest;
import com.ambravate.arcane.academy.practice.dto.SubmitResponse;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.practice.service.RPracticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/r")
@RequiredArgsConstructor
public class RPracticeController {

    private final RPracticeService rPracticeService;

    /**
     * Validates an R submission against client-reported in-iframe test results.
     *
     * <p>POST /api/r/{lessonId}/submit
     * <br>Body: {@link RSubmitRequest}
     */
    @PostMapping("/{lessonId}/submit")
    public ResponseEntity<SubmitResponse> submit(
            @PathVariable String lessonId,
            @RequestBody RSubmitRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                rPracticeService.submit(user.getId(), lessonId, request));
    }

    /**
     * Solo practice submission â€” same validation as guided practice.
     * The service has idempotency built-in (won't re-award XP if already passed).
     *
     * <p>POST /api/r/{lessonId}/solo-practice/submit
     */
    @PostMapping("/{lessonId}/solo-practice/submit")
    public ResponseEntity<SubmitResponse> submitSoloPractice(
            @PathVariable String lessonId,
            @RequestBody RSubmitRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                rPracticeService.submit(user.getId(), lessonId, request));
    }
}
