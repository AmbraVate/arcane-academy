package com.ambravate.arcane.academy.retention.controller;

import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.retention.dto.ReviewQueueItem;
import com.ambravate.arcane.academy.retention.dto.ReviewSubmitRequest;
import com.ambravate.arcane.academy.retention.dto.ReviewSubmitResponse;
import com.ambravate.arcane.academy.retention.service.RetentionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/retention")
@RequiredArgsConstructor
public class RetentionController {

    private final RetentionService retentionService;

    /** Returns lessons due for spaced-repetition review today (capped at 8 questions total). */
    @GetMapping("/queue")
    public ResponseEntity<List<ReviewQueueItem>> getQueue(
            @AuthenticationPrincipal UserPrincipal user) {
        if (user == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(retentionService.getReviewQueue(user.getId()));
    }

    /** Submits answers for a daily review session. Returns per-question correct/incorrect feedback. */
    @PostMapping("/review")
    public ResponseEntity<ReviewSubmitResponse> submitReview(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody ReviewSubmitRequest request) {
        if (user == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(retentionService.recordReview(user.getId(), request));
    }
}
