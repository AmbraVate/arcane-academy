package com.ambravate.arcane.academy.onboarding;

import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.dto.BadgeDto;
import com.ambravate.arcane.academy.common.exception.UserNotFoundException;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.gamification.api.GamificationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
public class OnboardingController {

    private final UserRepository userRepository;
    private final GamificationFacade gamificationFacade;

    /**
     * Marks the onboarding walkthrough as complete and evaluates badge conditions.
     * Safe to call multiple times — idempotent after the first call.
     * Returns the ARCANE_INITIATE badge if it was just earned, or null if already held.
     */
    @PostMapping("/complete")
    @Transactional
    public ResponseEntity<Map<String, Object>> complete(
            @AuthenticationPrincipal UserPrincipal principal) {

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.isOnboardingCompleted()) {
            user.setOnboardingCompleted(true);
            userRepository.save(user);
        }

        // Evaluate with empty progress/sessions — ARCANE_INITIATE fires on onboardingCompleted flag.
        List<BadgeDto> newBadges = gamificationFacade.evaluateAndAwardBadges(
                principal.getId(), Collections.emptyList(), Collections.emptyList());

        BadgeDto badge = newBadges.stream()
                .filter(b -> "ARCANE_INITIATE".equals(b.getId()))
                .findFirst()
                .orElse(null);

        return ResponseEntity.ok(Map.of(
                "badge", badge != null ? badge : Map.of()
        ));
    }
}
