package com.ambravate.polymath.academy.controller;

import com.ambravate.polymath.academy.dto.CodeRunRequest;
import com.ambravate.polymath.academy.dto.CodeRunResponse;
import com.ambravate.polymath.academy.dto.CodeSubmitRequest;
import com.ambravate.polymath.academy.dto.SubmitResponse;

import com.ambravate.polymath.academy.runner.JavaCodeRunner;
import com.ambravate.polymath.academy.service.QuestService;
import com.ambravate.polymath.academy.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/code")
@RequiredArgsConstructor
public class CodeController {

    private final JavaCodeRunner codeRunner;
    private final QuestService questService;

    @PostMapping("/run")
    public ResponseEntity<CodeRunResponse> runCode(
            @Valid @RequestBody CodeRunRequest request,
            @AuthenticationPrincipal UserPrincipal user) {

        CodeRunResponse result = codeRunner.run(request.getCode(), request.getTestInput());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/submit/{questId}")
    public ResponseEntity<SubmitResponse> submitQuest(
            @PathVariable String questId,
            @Valid @RequestBody CodeSubmitRequest request,
            @AuthenticationPrincipal UserPrincipal user) {

        SubmitResponse result = questService.evaluateSubmission(questId, request.getCode(), user.getId());
        return ResponseEntity.ok(result);
    }
}
