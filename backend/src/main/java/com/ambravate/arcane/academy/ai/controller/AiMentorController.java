package com.ambravate.arcane.academy.ai.controller;

import com.ambravate.arcane.academy.ai.dto.MentorFeedbackRequest;
import com.ambravate.arcane.academy.ai.dto.MentorFeedbackResponse;
import com.ambravate.arcane.academy.ai.service.AiMentorService;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mentor")
@RequiredArgsConstructor
public class AiMentorController {

    private final AiMentorService aiMentorService;

    @PostMapping("/feedback")
    public ResponseEntity<MentorFeedbackResponse> getFeedback(
            @Valid @RequestBody MentorFeedbackRequest request,
            @AuthenticationPrincipal UserPrincipal user) {

        String feedback = aiMentorService.getFeedback(
            request.getQuestTitle(),
            request.getTopic(),
            request.getProblemDescription(),
            request.getCode(),
            request.getFailedTests()
        );
        return ResponseEntity.ok(new MentorFeedbackResponse(feedback));
    }
}
