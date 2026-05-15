package com.ambravate.polymath.academy.controller;

import com.ambravate.polymath.academy.dto.RabbitHoleTermDto;
import com.ambravate.polymath.academy.security.UserPrincipal;
import com.ambravate.polymath.academy.service.UserRabbitHoleTermService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rabbit-hole-terms")
@RequiredArgsConstructor
public class UserRabbitHoleTermController {

    private final UserRabbitHoleTermService service;

    @GetMapping
    public ResponseEntity<List<RabbitHoleTermDto>> getAll(
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(service.getAll(user.getId()));
    }

    @PostMapping
    public ResponseEntity<RabbitHoleTermDto> save(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserPrincipal user) {
        RabbitHoleTermDto dto = service.save(
                user.getId(),
                body.get("term"),
                body.get("description"),
                body.get("subChunkId"),
                body.get("topicId"));
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{term}")
    public ResponseEntity<Void> remove(
            @PathVariable String term,
            @AuthenticationPrincipal UserPrincipal user) {
        service.remove(user.getId(), term);
        return ResponseEntity.ok().build();
    }
}
