package com.arcane.academy.controller;

import com.arcane.academy.dto.BadgeDto;
import com.arcane.academy.security.UserPrincipal;
import com.arcane.academy.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    @GetMapping
    public ResponseEntity<List<BadgeDto>> getAll(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(badgeService.getAllForUser(principal.getId()));
    }

    @GetMapping("/earned")
    public ResponseEntity<List<BadgeDto>> getEarned(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(badgeService.getEarnedForUser(principal.getId()));
    }
}
