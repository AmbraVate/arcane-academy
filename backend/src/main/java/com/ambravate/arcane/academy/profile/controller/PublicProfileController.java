package com.ambravate.arcane.academy.profile.controller;

import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.profile.domain.VisibilityRequest;
import com.ambravate.arcane.academy.profile.service.PublicProfileService;
import com.ambravate.arcane.academy.profile.domain.PublicProfile;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.common.domain.User;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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
@Validated
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

    /** Returns the caller's current location string (may be null if not set). */
    @GetMapping("/location")
    public ResponseEntity<Map<String, String>> getLocation(@AuthenticationPrincipal UserPrincipal principal) {
        User user = userRepository.findById(principal.getId()).orElseThrow();
        return ResponseEntity.ok(Map.of("location", user.getLocation() != null ? user.getLocation() : ""));
    }

    /**
     * Updates the caller's location. Send {@code {"location": ""}} to clear it.
     * Maximum 100 characters; validated server-side.
     */
    @PatchMapping("/location")
    public ResponseEntity<Map<String, String>> setLocation(
        @AuthenticationPrincipal UserPrincipal principal,
        @RequestBody Map<String, @Size(max = 100, message = "Location must be 100 characters or fewer") String> body
    ) {
        User user = userRepository.findById(principal.getId()).orElseThrow();
        String loc = body.getOrDefault("location", "").trim();
        user.setLocation(loc.isEmpty() ? null : loc);
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("location", user.getLocation() != null ? user.getLocation() : ""));
    }

}
