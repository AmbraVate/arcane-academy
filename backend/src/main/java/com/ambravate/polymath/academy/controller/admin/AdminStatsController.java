package com.ambravate.polymath.academy.controller.admin;

import com.ambravate.polymath.academy.dto.admin.AdminStatsDto;
import com.ambravate.polymath.academy.service.AdminStatsService;
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
