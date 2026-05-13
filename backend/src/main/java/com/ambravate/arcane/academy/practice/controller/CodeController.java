package com.ambravate.arcane.academy.practice.controller;

import com.ambravate.arcane.academy.common.dto.CodeRunRequest;
import com.ambravate.arcane.academy.common.dto.CodeRunResponse;
import com.ambravate.arcane.academy.practice.runner.JavaCodeRunner;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
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

    @PostMapping("/run")
    public ResponseEntity<CodeRunResponse> runCode(
            @Valid @RequestBody CodeRunRequest request,
            @AuthenticationPrincipal UserPrincipal user) {

        CodeRunResponse result = codeRunner.run(request.getCode(), request.getTestInput());
        return ResponseEntity.ok(result);
    }
}
