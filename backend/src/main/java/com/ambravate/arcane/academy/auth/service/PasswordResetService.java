package com.ambravate.arcane.academy.auth.service;

import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.common.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Handles the two-step password reset flow:
 *
 * <ol>
 *   <li>Admin triggers {@link #sendReset(String)} for a user — a one-time token is
 *       stored on the user record and an email with the reset link is sent.</li>
 *   <li>User clicks the link and submits a new password via {@link #completeReset(String, String)} —
 *       the token is validated, the password is updated, and the token is cleared.</li>
 * </ol>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private static final long TOKEN_EXPIRY_HOURS = 24;

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService     emailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    /**
     * Generates a reset token, persists it, and sends the reset email.
     * Throws {@link IllegalStateException} if the email service is not configured.
     */
    public void sendReset(String userId) {
        if (!emailService.isConfigured()) {
            throw new IllegalStateException(
                "Email service not configured. Set MAIL_HOST (and MAIL_USERNAME / MAIL_PASSWORD) " +
                "environment variables to enable password reset emails.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        String token = UUID.randomUUID().toString();
        user.setPasswordResetToken(token);
        user.setPasswordResetExpiresAt(Instant.now().plusSeconds(TOKEN_EXPIRY_HOURS * 3600));
        userRepository.save(user);

        String link = frontendUrl + "/reset-password#token=" + token;
        emailService.sendPasswordReset(user.getEmail(), user.getUsername(), link);
        log.info("[PasswordReset] Reset email dispatched for userId={}", userId);
    }

    /**
     * Validates {@code token} and sets the new password.
     * Throws {@link IllegalArgumentException} if the token is invalid or expired.
     */
    public void completeReset(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset link."));

        if (user.getPasswordResetExpiresAt() == null
                || Instant.now().isAfter(user.getPasswordResetExpiresAt())) {
            throw new IllegalArgumentException("This reset link has expired. Please request a new one.");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiresAt(null);
        user.setRefreshToken(null); // invalidate all existing sessions after password reset
        userRepository.save(user);
        log.info("[PasswordReset] Password updated for userId={}", user.getId());
    }
}
