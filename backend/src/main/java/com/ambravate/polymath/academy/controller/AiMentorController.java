package com.ambravate.polymath.academy.controller;

import com.ambravate.polymath.academy.dto.MentorFeedbackRequest;
import com.ambravate.polymath.academy.dto.MentorFeedbackResponse;
import com.ambravate.polymath.academy.service.AiMentorService;
import com.ambravate.polymath.academy.security.UserPrincipal;
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
