package com.ambravate.arcane.academy.practice.controller;

import com.ambravate.arcane.academy.practice.dto.ReactSubmitRequest;
import com.ambravate.arcane.academy.practice.dto.SubmitResponse;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.practice.service.ReactPracticeService;
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
     * <p>POST /api/react/{lessonId}/submit
     * <br>Body: {@link ReactSubmitRequest}
     */
    @PostMapping("/{lessonId}/submit")
    public ResponseEntity<SubmitResponse> submit(
            @PathVariable String lessonId,
            @RequestBody ReactSubmitRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                reactPracticeService.submit(user.getId(), lessonId, request));
    }

    /**
     * Solo practice submission â€” same validation as guided practice.
     * The service has idempotency built-in (won't re-award XP if already passed).
     *
     * <p>POST /api/react/{lessonId}/solo-practice/submit
     */
    @PostMapping("/{lessonId}/solo-practice/submit")
    public ResponseEntity<SubmitResponse> submitSoloPractice(
            @PathVariable String lessonId,
            @RequestBody ReactSubmitRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                reactPracticeService.submit(user.getId(), lessonId, request));
    }
}
