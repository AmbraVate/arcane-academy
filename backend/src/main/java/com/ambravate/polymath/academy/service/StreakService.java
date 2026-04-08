package com.ambravate.polymath.academy.service;

import com.ambravate.polymath.academy.model.User;
import com.ambravate.polymath.academy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class StreakService {

    private final UserRepository userRepository;

    /**
     * Called on login and on quest/boss completion.
     * - If the user was last active yesterday → increment streak
     * - If the user was last active today → no change (already counted today)
     * - If the user was last active 2+ days ago → streak resets to 1
     */
    @Transactional
    public int updateStreak(String userId) {
        User user = userRepository.findById(userId).orElseThrow();
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        int previousStreak = user.getStreakDays();

        if (user.getLastLoginAt() == null) {
            log.info("[Streak] First activity | userId={} → streak=1", userId);
            user.setStreakDays(1);
        } else {
            LocalDate lastActive = user.getLastLoginAt()
                    .atZone(ZoneOffset.UTC).toLocalDate();

            long daysSince = today.toEpochDay() - lastActive.toEpochDay();
            log.debug("[Streak] userId={} lastActive={} today={} daysSince={}",
                    userId, lastActive, today, daysSince);

            if (daysSince == 0) {
                log.debug("[Streak] Already active today | userId={} streak unchanged at {}",
                        userId, previousStreak);
            } else if (daysSince == 1) {
                user.setStreakDays(user.getStreakDays() + 1);
                log.info("[Streak] Extended | userId={} {} → {}", userId, previousStreak, user.getStreakDays());
            } else {
                user.setStreakDays(1);
                log.info("[Streak] RESET (missed {} days) | userId={} {} → 1", daysSince, userId, previousStreak);
            }
        }

        user.setLastLoginAt(Instant.now());
        userRepository.save(user);
        return user.getStreakDays();
    }

    public boolean isStreakAtRisk(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getLastLoginAt() == null || user.getStreakDays() == 0) return false;

        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        LocalDate lastActive = user.getLastLoginAt().atZone(ZoneOffset.UTC).toLocalDate();
        long daysSince = today.toEpochDay() - lastActive.toEpochDay();

        // Streak is at risk if last active was yesterday (haven't done anything today yet)
        return daysSince == 1;
    }
}
