package com.ambravate.arcane.academy.practice.controller;

import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.practice.dto.GuidedStepCheckRequest;
import com.ambravate.arcane.academy.practice.dto.GuidedStepCheckResponse;
import com.ambravate.arcane.academy.practice.dto.GuidedStepDto;
import com.ambravate.arcane.academy.practice.service.GuidedStepService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints for the Guided Practice step workbook.
 *
 * <ul>
 *   <li>{@code GET  /api/encoding/{lessonId}/guided/steps}
 *       — ordered step list with per-user completion flags</li>
 *   <li>{@code POST /api/encoding/{lessonId}/guided/steps/{stepId}/check}
 *       — deterministic marking; records completion on pass</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/encoding/{lessonId}/guided")
@RequiredArgsConstructor
public class GuidedStepController {

    private final GuidedStepService guidedStepService;

    @GetMapping("/steps")
    public ResponseEntity<List<GuidedStepDto>> getSteps(
            @PathVariable String lessonId,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(guidedStepService.getSteps(user.getId(), lessonId));
    }

    @PostMapping("/steps/{stepId}/check")
    public ResponseEntity<GuidedStepCheckResponse> checkStep(
            @PathVariable String lessonId,
            @PathVariable String stepId,
            @Valid @RequestBody GuidedStepCheckRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                guidedStepService.checkStep(user.getId(), lessonId, stepId, request.getAnswer()));
    }
}
