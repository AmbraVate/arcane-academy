package com.ambravate.arcane.academy.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Thin wrapper around Spring Mail.
 *
 * <p>{@link JavaMailSender} is injected as optional — when {@code MAIL_HOST} is not
 * configured the auto-configuration skips it and {@code mailSender} stays null.
 * Callers should check {@link #isConfigured()} before calling send methods.
 */
@Service
@Slf4j
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.from:noreply@arcane-academy.com}")
    private String fromAddress;

    /** Returns true when SMTP is fully configured and ready to send. */
    public boolean isConfigured() {
        return mailSender != null;
    }

    /**
     * Sends a password-reset link email to the specified address.
     *
     * @param toEmail   recipient address
     * @param username  display name used in the greeting
     * @param resetLink full URL of the reset page (includes the token)
     * @throws RuntimeException if sending fails at the SMTP layer
     */
    public void sendPasswordReset(String toEmail, String username, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("Arcane Academy - Password Reset");
        message.setText(
                "Greetings, " + username + ",\n\n" +
                "An administrator has requested a password reset for your Arcane Academy account.\n\n" +
                "Click the link below to set a new password. This link is valid for 24 hours:\n\n" +
                resetLink + "\n\n" +
                "If you did not expect this email, you can safely ignore it - your password will not change unless you follow the link above.\n\n" +
                "-- The Arcane Academy"
        );
        try {
            mailSender.send(message);
            log.info("[Email] Password reset sent to {}", toEmail);
        } catch (Exception e) {
            log.error("[Email] Failed to send to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send password reset email.", e);
        }
    }
}
