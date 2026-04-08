package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.Boss;
import com.ambravate.polymath.academy.model.Quest;
import com.ambravate.polymath.academy.model.User;
import com.ambravate.polymath.academy.model.UserProgress;
import com.ambravate.polymath.academy.repository.BossRepository;
import com.ambravate.polymath.academy.repository.QuestRepository;
import com.ambravate.polymath.academy.repository.UserProgressRepository;
import com.ambravate.polymath.academy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

// ══════════════════════════════════════════════════════════════════════════════
// TEST USER SEEDER
// Creates test accounts at various stages for QA / development testing.
//
// Accounts (all password: Test1234!)
//   novice@polymath.test       — fresh start, 0 XP
//   apprentice@polymath.test   — Chapter I complete
//   adept@polymath.test        — Chapters I–II complete
//   mage@polymath.test         — Chapters I–IV complete
// ══════════════════════════════════════════════════════════════════════════════
@Component
@RequiredArgsConstructor
@Slf4j
public class TestUserSeeder {

    private final UserRepository userRepository;
    private final UserProgressRepository progressRepository;
    private final QuestRepository questRepository;
    private final BossRepository bossRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String TEST_PASSWORD = "Test1234!";

    public void seed() {
        createUser("novice@polymath.test",      "test_novice",      List.of(),          List.of());
        createUser("apprentice@polymath.test",  "test_apprentice",  List.of(1),         List.of(1));
        createUser("adept@polymath.test",       "test_adept",       List.of(1, 2),      List.of(1, 2));
        createUser("mage@polymath.test",        "test_mage",        List.of(1, 2, 3, 4), List.of(1, 2, 3, 4));
    }

    private void createUser(String email, String username,
                            List<Integer> questChapters,
                            List<Integer> bossChapters) {
        if (userRepository.existsByEmail(email)) {
            log.debug("[TestUserSeeder] {} already exists — skipping", email);
            return;
        }

        List<Quest> quests = questChapters.isEmpty() ? List.of() :
            questRepository.findAll().stream()
                .filter(q -> questChapters.contains(q.getChapterNumber()) && !q.isSideQuest())
                .toList();

        List<Boss> bosses = bossChapters.isEmpty() ? List.of() :
            bossRepository.findAll().stream()
                .filter(b -> bossChapters.contains(b.getChapterNumber()))
                .toList();

        int totalXp = quests.stream().mapToInt(Quest::getXpReward).sum()
                    + bosses.stream().mapToInt(Boss::getXpReward).sum();

        User user = userRepository.save(User.builder()
            .username(username)
            .email(email)
            .passwordHash(passwordEncoder.encode(TEST_PASSWORD))
            .totalXp(totalXp)
            .rank(calculateRank(totalXp))
            .build());

        for (Quest q : quests) {
            progressRepository.save(UserProgress.builder()
                .userId(user.getId())
                .itemId(q.getId())
                .itemType(UserProgress.ItemType.QUEST)
                .completed(true)
                .xpEarned(q.getXpReward())
                .build());
        }

        for (Boss b : bosses) {
            progressRepository.save(UserProgress.builder()
                .userId(user.getId())
                .itemId(b.getId())
                .itemType(UserProgress.ItemType.BOSS)
                .completed(true)
                .xpEarned(b.getXpReward())
                .build());
        }

        log.info("[TestUserSeeder] Created {} ({}) — {} XP, rank {}",
            username, email, totalXp, calculateRank(totalXp));
    }

    private String calculateRank(int xp) {
        if (xp >= 4000) return "Archmage";
        if (xp >= 2000) return "Mage";
        if (xp >= 1000) return "Adept";
        if (xp >= 400)  return "Apprentice";
        return "Novice";
    }
}
