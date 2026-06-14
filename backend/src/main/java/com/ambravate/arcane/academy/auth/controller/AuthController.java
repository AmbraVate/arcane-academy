package com.ambravate.arcane.academy.auth.controller;

import com.ambravate.arcane.academy.auth.dto.AuthResponse;
import com.ambravate.arcane.academy.auth.dto.LoginRequest;
import com.ambravate.arcane.academy.auth.dto.RegisterRequest;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.auth.service.AuthService;
import com.ambravate.arcane.academy.auth.service.PasswordResetService;
import com.ambravate.arcane.academy.auth.service.PendingOAuth2SessionStore;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService              authService;
    private final PasswordResetService     passwordResetService;
    private final PendingOAuth2SessionStore pendingOAuth2SessionStore;
    private final UserRepository           userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> me(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(authService.getMe(principal.getId()));
    }

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
        return ResponseEntity.ok(Map.of("available", authService.isUsernameAvailable(username)));
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(Map.of("available", authService.isEmailAvailable(email)));
    }

    /**
     * Exchanges a short-lived OAuth2 code (issued by {@link com.ambravate.arcane.academy.auth.oauth2.OAuth2LoginSuccessHandler})
     * for a full {@link AuthResponse}. The code is single-use and expires after 30 seconds.
     * Tokens are never sent as URL query parameters — only via this JSON response body.
     */
    @PostMapping("/exchange")
    public ResponseEntity<AuthResponse> exchange(@RequestParam String code) {
        return pendingOAuth2SessionStore.consume(code)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    /**
     * Initiates a self-service password reset.
     * Always returns 200 regardless of whether the email is registered (prevents enumeration).
     * A reset link is emailed to the address if an account exists and email is configured.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.getOrDefault("email", "").trim();
        if (!email.isBlank()) {
            userRepository.findByEmail(email).ifPresent(user -> {
                try {
                    passwordResetService.sendReset(user.getId());
                } catch (IllegalStateException e) {
                    log.warn("[Auth] forgot-password: email service not configured — reset link not sent");
                }
            });
        }
        // Always 200 — do not reveal whether the email is registered
        return ResponseEntity.ok().build();
    }

    /**
     * Completes a password reset.
     * Returns 204 on success, 410 if the token is invalid or expired, 400 if inputs are invalid.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody Map<String, String> body) {
        String token       = body.get("token");
        String newPassword = body.get("newPassword");
        if (token == null || token.isBlank() || newPassword == null || newPassword.length() < 20) {
            return ResponseEntity.badRequest().build();
        }
        try {
            passwordResetService.completeReset(token, newPassword);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.GONE).build(); // 410 = token invalid/expired
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody Map<String, String> body) {
        String token = body.get("refreshToken");
        if (token == null || token.isBlank()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        try {
            return ResponseEntity.ok(authService.refreshToken(token));
        } catch (BadCredentialsException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
