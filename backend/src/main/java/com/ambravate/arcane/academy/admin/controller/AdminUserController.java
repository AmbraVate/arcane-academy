package com.ambravate.arcane.academy.admin.controller;

import com.ambravate.arcane.academy.admin.dto.AdminUserDto;
import com.ambravate.arcane.academy.admin.dto.UserStatsDto;
import com.ambravate.arcane.academy.admin.service.AdminStatsService;
import com.ambravate.arcane.academy.auth.service.PasswordResetService;
import com.ambravate.arcane.academy.common.domain.LessonStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserRole;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.common.security.UserPrincipal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class AdminUserController {

    private final UserRepository              userRepository;
    private final UserChunkProgressRepository progressRepository;
    private final AdminStatsService           statsService;
    private final PasswordResetService        passwordResetService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false)    String search) {

        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> users = (search == null || search.isBlank())
                ? userRepository.findAll(pageable)
                : userRepository.findBySearchTerm(search, pageable);

        return ResponseEntity.ok(Map.of(
                "content", users.getContent().stream().map(u -> {
                    long completed = progressRepository.countByUserIdAndStatus(u.getId(), LessonStatus.COMPLETE);
                    return statsService.toUserDto(u, completed);
                }).toList(),
                "totalElements", users.getTotalElements(),
                "totalPages", users.getTotalPages(),
                "page", page
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDto> get(@PathVariable String id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
        long completed = progressRepository.countByUserIdAndStatus(id, LessonStatus.COMPLETE);
        return ResponseEntity.ok(statsService.toUserDto(u, completed));
    }

    @DeleteMapping("/{id}/progress")
    public ResponseEntity<Void> resetProgress(@PathVariable String id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found: " + id);
        }
        var progress = progressRepository.findByUserId(id);
        progressRepository.deleteAll(progress);
        return ResponseEntity.noContent().build();
    }

    /**
     * Change a user's role (ADMIN / USER).
     * An admin cannot demote their own account.
     */
    @PutMapping("/{userId}/role")
    public ResponseEntity<AdminUserDto> changeRole(
            @PathVariable String userId,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserPrincipal caller) {

        if (caller.getId().equals(userId)) {
            return ResponseEntity.badRequest().build();
        }

        String roleStr = body.get("role");
        if (roleStr == null) {
            return ResponseEntity.badRequest().build();
        }

        UserRole newRole;
        try {
            newRole = UserRole.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
        user.setRole(newRole);
        userRepository.save(user);

        long completed = progressRepository.countByUserIdAndStatus(userId, LessonStatus.COMPLETE);
        return ResponseEntity.ok(statsService.toUserDto(user, completed));
    }

    /**
     * Block or unblock a user.
     * An admin cannot block their own account.
     */
    @PutMapping("/{userId}/blocked")
    public ResponseEntity<AdminUserDto> setBlocked(
            @PathVariable String userId,
            @RequestBody Map<String, Boolean> body,
            @AuthenticationPrincipal UserPrincipal caller) {

        if (caller.getId().equals(userId)) {
            return ResponseEntity.badRequest().build();
        }

        Boolean blocked = body.get("blocked");
        if (blocked == null) {
            return ResponseEntity.badRequest().build();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
        user.setBlocked(blocked);
        userRepository.save(user);

        long completed = progressRepository.countByUserIdAndStatus(userId, LessonStatus.COMPLETE);
        return ResponseEntity.ok(statsService.toUserDto(user, completed));
    }

    /**
     * Grant or revoke the paywall bypass for a user.
     * Admins cannot modify their own bypass flag (they bypass implicitly).
     */
    @PatchMapping("/{userId}/bypass-paywall")
    public ResponseEntity<AdminUserDto> setBypassPaywall(
            @PathVariable String userId,
            @RequestBody Map<String, Boolean> body,
            @AuthenticationPrincipal UserPrincipal caller) {

        if (caller.getId().equals(userId)) {
            return ResponseEntity.badRequest().build();
        }

        Boolean bypass = body.get("bypassPaywall");
        if (bypass == null) {
            return ResponseEntity.badRequest().build();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
        user.setBypassPaywall(bypass);
        userRepository.save(user);

        long completed = progressRepository.countByUserIdAndStatus(userId, LessonStatus.COMPLETE);
        return ResponseEntity.ok(statsService.toUserDto(user, completed));
    }

    /**
     * Detailed stats for a single user - for the admin user-detail panel.
     */
    @GetMapping("/{userId}/stats")
    public ResponseEntity<UserStatsDto> getUserStats(@PathVariable String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
        return ResponseEntity.ok(statsService.toUserStatsDto(user));
    }

    /**
     * Triggers a password-reset email to the user's registered address.
     * The user receives a link valid for 24 hours to set their own new password.
     * Returns 503 if SMTP is not configured on this deployment.
     */
    @PostMapping("/{userId}/send-password-reset")
    public ResponseEntity<Map<String, String>> sendPasswordReset(@PathVariable String userId) {
        try {
            passwordResetService.sendReset(userId);
            return ResponseEntity.ok(Map.of("message", "Password reset email sent."));
        } catch (IllegalStateException e) {
            // SMTP not configured
            return ResponseEntity.status(503).body(Map.of("error", e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("[Admin] Failed to send password reset for userId={}: {}", userId, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to send email. Check server logs."));
        }
    }
}
