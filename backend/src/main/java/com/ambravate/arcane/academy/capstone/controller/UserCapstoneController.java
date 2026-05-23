package com.ambravate.arcane.academy.capstone.controller;

import com.ambravate.arcane.academy.capstone.dto.CapstoneDto;
import com.ambravate.arcane.academy.capstone.dto.SaveCapstoneRequest;
import com.ambravate.arcane.academy.capstone.service.UserCapstoneService;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/capstones")
@RequiredArgsConstructor
public class UserCapstoneController {

    private final UserCapstoneService capstoneService;

    @GetMapping
    public ResponseEntity<List<CapstoneDto>> list(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return ResponseEntity.ok(capstoneService.listForUser(user.getId()));
    }

    @PostMapping
    public ResponseEntity<CapstoneDto> create(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody @Valid SaveCapstoneRequest req
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(capstoneService.save(user.getId(), req));
    }

    @PutMapping("/{capstoneId}")
    public ResponseEntity<CapstoneDto> update(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable String capstoneId,
            @RequestBody @Valid SaveCapstoneRequest req
    ) {
        return ResponseEntity.ok(capstoneService.update(user.getId(), capstoneId, req));
    }

    @DeleteMapping("/{capstoneId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable String capstoneId
    ) {
        capstoneService.delete(user.getId(), capstoneId);
        return ResponseEntity.noContent().build();
    }
}
