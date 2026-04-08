package com.ambravate.polymath.academy.controller;

import com.ambravate.polymath.academy.dto.ProgressResponse;
import com.ambravate.polymath.academy.dto.QuestDetailDto;
import com.ambravate.polymath.academy.dto.QuestSummaryDto;

import com.ambravate.polymath.academy.service.QuestService;
import com.ambravate.polymath.academy.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;

    @GetMapping
    public ResponseEntity<List<QuestSummaryDto>> getAllQuests(
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(questService.getAllWithProgress(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestDetailDto> getQuest(
            @PathVariable String id,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(questService.getQuestDetail(id, user.getId()));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<ProgressResponse> completeQuest(
            @PathVariable String id,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(questService.markComplete(id, user.getId()));
    }
}
