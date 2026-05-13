package com.ambravate.arcane.academy.profile.controller;

import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.profile.domain.VisibilityRequest;
import com.ambravate.arcane.academy.profile.service.PublicProfileService;
import com.ambravate.arcane.academy.profile.domain.PublicProfile;
import com.ambravate.arcane.academy.common.repository.UserRepository;
import com.ambravate.arcane.academy.common.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Public profile + visibility-toggle endpoints.
 *
 * <ul>
 *   <li>{@code GET /api/profile/public/{username}} — read-only profile;
 *       returns 404 if the user is not opted in</li>
 *   <li>{@code GET /api/profile/visibility} — returns the caller's current setting</li>
 *   <li>{@code POST /api/profile/visibility} — toggle the caller's setting,
 *       body {@code {"enabled": true|false}}</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class PublicProfileController {

    private final PublicProfileService publicProfileService;
    private final UserRepository userRepository;

    @GetMapping("/public/{username}")
    public ResponseEntity<PublicProfile> getPublicProfile(@PathVariable String username) {
        return publicProfileService.findByUsername(username)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/visibility")
    public ResponseEntity<Map<String, Boolean>> getVisibility(@AuthenticationPrincipal UserPrincipal principal) {
        User user = userRepository.findById(principal.getId()).orElseThrow();
        return ResponseEntity.ok(Map.of("enabled", user.isPublicProfileEnabled()));
    }

    @PostMapping("/visibility")
    public ResponseEntity<Map<String, Boolean>> setVisibility(
        @AuthenticationPrincipal UserPrincipal principal,
        @RequestBody VisibilityRequest body
    ) {
        User user = userRepository.findById(principal.getId()).orElseThrow();
        user.setPublicProfileEnabled(body.enabled());
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("enabled", user.isPublicProfileEnabled()));
    }

}
