package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.User;
import com.ambravate.polymath.academy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserPromoter {

    private static final String ADMIN_EMAIL = "archergilly84@gmail.com";

    private final UserRepository userRepository;

    /**
     * Ensures the admin email address has ROLE_ADMIN.
     * Also backfills any legacy rows that have NULL in the role column.
     * Idempotent — safe to call on every startup.
     */
    public void promote() {
        int fixed = userRepository.backfillNullRoles();
        if (fixed > 0) {
            log.info("[AdminUserPromoter] Backfilled role=USER for {} legacy user(s).", fixed);
        }

        userRepository.findByEmail(ADMIN_EMAIL).ifPresentOrElse(user -> {
            if (user.getRole() != User.UserRole.ADMIN) {
                user.setRole(User.UserRole.ADMIN);
                userRepository.save(user);
                log.info("[AdminUserPromoter] Promoted {} to ADMIN.", ADMIN_EMAIL);
            } else {
                log.debug("[AdminUserPromoter] {} is already ADMIN.", ADMIN_EMAIL);
            }
        }, () -> log.debug("[AdminUserPromoter] Admin email {} not yet registered.", ADMIN_EMAIL));
    }
}
