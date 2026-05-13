package com.ambravate.arcane.academy.admin.controller;

import com.ambravate.arcane.academy.admin.dto.AdminStatsDto;
import com.ambravate.arcane.academy.admin.service.AdminStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class AdminStatsController {

    private final AdminStatsService statsService;

    @GetMapping
    public ResponseEntity<AdminStatsDto> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }
}
