package com.ambravate.arcane.academy.content.seeder;

import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Ensures a designated admin account exists on every startup.
 *
 * <p>Credentials come from environment variables — never hard-coded:
 * <ul>
 *   <li>{@code ADMIN_EMAIL}    — required; seeder is skipped when blank.</li>
 *   <li>{@code ADMIN_PASSWORD} — required; seeder is skipped when blank.</li>
 *   <li>{@code ADMIN_USERNAME} — optional, defaults to {@code "admin"}.</li>
 * </ul>
 *
 * <p>The seeder is fully idempotent:
 * <ul>
 *   <li>If no account with that e-mail exists it is created with role ADMIN.</li>
 *   <li>If the account already exists the role, password hash, and onboarding
 *       flag are synchronised with the current env values. This allows password
 *       rotation by updating the env var and redeploying — no manual DB access.</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email:}")
    private String adminEmail;

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.password:}")
    private String adminPassword;

    public void seed() {
        // Backfill any legacy rows with a null role — safe to run repeatedly.
        int fixed = userRepository.backfillNullRoles();
        if (fixed > 0) {
            log.info("[AdminSeeder] Backfilled role=USER for {} legacy user(s).", fixed);
        }

        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            log.info("[AdminSeeder] ADMIN_EMAIL or ADMIN_PASSWORD not configured — skipping admin bootstrap.");
            return;
        }

        userRepository.findByEmail(adminEmail)
                .ifPresentOrElse(this::syncAdmin, this::createAdmin);
    }

    // ── Internal ──────────────────────────────────────────────────────────────

    private void syncAdmin(User admin) {
        boolean changed = false;

        if (admin.getRole() != UserRole.ADMIN) {
            admin.setRole(UserRole.ADMIN);
            changed = true;
        }

        // Re-hash if the env password differs from the stored hash.
        // Allows password rotation by changing the env var and redeploying.
        String currentHash = admin.getPasswordHash() != null ? admin.getPasswordHash() : "";
        if (!passwordEncoder.matches(adminPassword, currentHash)) {
            admin.setPasswordHash(passwordEncoder.encode(adminPassword));
            changed = true;
        }

        if (!admin.isOnboardingCompleted()) {
            admin.setOnboardingCompleted(true);
            changed = true;
        }

        if (changed) {
            userRepository.save(admin);
            log.info("[AdminSeeder] Admin account synced (role/password/onboarding updated): {}", adminEmail);
        } else {
            log.info("[AdminSeeder] Admin account already current: {}", adminEmail);
        }
    }

    private void createAdmin() {
        String username = resolveUsername();
        User admin = User.aUser()
                .withEmail(adminEmail)
                .withUsername(username)
                .withPasswordHash(passwordEncoder.encode(adminPassword))
                .withRole(UserRole.ADMIN)
                .withOnboardingCompleted(true)
                .withBypassPaywall(true)
                .build();
        admin.setRefreshToken(UUID.randomUUID().toString());
        userRepository.save(admin);
        log.info("[AdminSeeder] Admin account created: {} ({})", username, adminEmail);
    }

    /** Returns {@code adminUsername}, appending a numeric suffix if already taken. */
    private String resolveUsername() {
        if (!userRepository.existsByUsername(adminUsername)) {
            return adminUsername;
        }
        int suffix = 1;
        while (userRepository.existsByUsername(adminUsername + suffix)) {
            suffix++;
        }
        return adminUsername + suffix;
    }
}
