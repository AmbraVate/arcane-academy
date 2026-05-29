package com.ambravate.arcane.academy.content.controller;

import com.ambravate.arcane.academy.content.domain.CuriosityQueueItem;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.content.service.CuriosityQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/curiosity-queue")
@RequiredArgsConstructor
public class CuriosityQueueController {

    private final CuriosityQueueService queueService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getQueue(
            @AuthenticationPrincipal UserPrincipal user) {
        List<CuriosityQueueItem> items = queueService.getQueue(user.getId());
        List<Map<String, Object>> result = items.stream().map(i -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", i.getId());
            map.put("userId", i.getUserId());
            map.put("lessonId", i.getLessonId());
            map.put("savedAt", i.getSavedAt().toString());
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{lessonId}")
    public ResponseEntity<Void> saveForLater(
            @PathVariable String lessonId,
            @AuthenticationPrincipal UserPrincipal user) {
        queueService.saveForLater(user.getId(), lessonId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> removeFromQueue(
            @PathVariable String lessonId,
            @AuthenticationPrincipal UserPrincipal user) {
        queueService.removeFromQueue(user.getId(), lessonId);
        return ResponseEntity.ok().build();
    }
}
