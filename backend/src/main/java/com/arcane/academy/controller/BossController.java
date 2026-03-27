package com.arcane.academy.controller;

import com.arcane.academy.dto.*;
import com.arcane.academy.service.BossService;
import com.arcane.academy.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bosses")
@RequiredArgsConstructor
public class BossController {

    private final BossService bossService;

    @GetMapping
    public ResponseEntity<List<BossDto>> getAllBosses(
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(bossService.getAllWithProgress(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BossDto> getBoss(
            @PathVariable String id,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(bossService.getById(id, user.getId()));
    }

    @PostMapping("/{id}/answer")
    public ResponseEntity<BossAnswerResponse> checkAnswer(
            @PathVariable String id,
            @RequestBody BossAnswerRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(bossService.checkAnswer(id, request));
    }

    @PostMapping("/{id}/defeat")
    public ResponseEntity<ProgressResponse> defeatBoss(
            @PathVariable String id,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(bossService.defeatBoss(id, user.getId()));
    }
}
