package com.ambravate.arcane.academy.capstone.controller;

import com.ambravate.arcane.academy.capstone.dto.AdminCapstoneDto;
import com.ambravate.arcane.academy.capstone.service.UserCapstoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/capstones")
@RequiredArgsConstructor
public class AdminCapstoneController {

    private final UserCapstoneService capstoneService;

    @GetMapping
    public ResponseEntity<Page<AdminCapstoneDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(capstoneService.listAll(pageable));
    }

    @PutMapping("/{capstoneId}/feedback")
    public ResponseEntity<AdminCapstoneDto> addFeedback(
            @PathVariable String capstoneId,
            @RequestBody Map<String, String> body
    ) {
        String feedback = body.getOrDefault("adminFeedback", "");
        return ResponseEntity.ok(capstoneService.addFeedback(capstoneId, feedback));
    }
}
