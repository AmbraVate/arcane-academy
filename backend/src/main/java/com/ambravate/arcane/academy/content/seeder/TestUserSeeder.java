package com.ambravate.arcane.academy.content.seeder;

import com.ambravate.arcane.academy.common.domain.EncodingPhase;
import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.SubChunkStatus;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.content.repository.SubChunkRepository;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@Profile("!prod")   // never create test accounts in production
@RequiredArgsConstructor
@Slf4j
public class TestUserSeeder {

  private final UserRepository userRepository;
  private final UserChunkProgressRepository progressRepository;
  private final SubChunkRepository subChunkRepository;
  private final PasswordEncoder passwordEncoder;

  private static final String TEST_PASSWORD = "Test1234!";

  public void seed() {
    createUser("novice@polymath.test", "test_novice", List.of(), 0);
    createUser("apprentice@polymath.test", "test_apprentice", List.of("A"), 200);
    createUser("adept@polymath.test", "test_adept", List.of("A", "B"), 500);
    createUser(
        "mage@polymath.test",
        "test_mage",
        List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"),
        5000
    );
  }

  private void createUser(
      String email, String username,
      List<String> completedChunkIds, int xp
  ) {
    if (userRepository.existsByEmail(email)) {
      log.debug("[TestUserSeeder] {} already exists — skipping", email);
      return;
    }

    User user = userRepository.save(
        User.aUser()
            .withUsername(username)
            .withEmail(email)
            .withPasswordHash(passwordEncoder.encode(TEST_PASSWORD))
            .withTotalXp(xp)
            .withRank(calculateRank(xp))
            .build()
    );

    for (String chunkId : completedChunkIds) {
      List<SubChunk> subChunks = subChunkRepository.findByChunkIdOrderBySortOrderAsc(chunkId);
      for (SubChunk sc : subChunks) {
        progressRepository.save(
            UserChunkProgress.builder()
                .userId(user.getId())
                .subChunkId(sc.getId())
                .currentPhase(EncodingPhase.COMPLETE)
                .status(SubChunkStatus.COMPLETE)
                .memoryStrength(0.9)
                .easeFactor(2.5)
                .repetitionCount(1)
                .intervalDays(3)
                .lastReviewedAt(Instant.now())
                .nextReviewAt(Instant.now().plusSeconds(3 * 86400))
                .lastScore(1.0)
                .completedAt(Instant.now())
                .build()
        );
      }
    }

    log.info(
        "[TestUserSeeder] Created {} ({}) — {} XP, rank {}",
        username, email, xp, calculateRank(xp)
    );
  }

  private String calculateRank(int xp) {
    if (xp >= 11000) {
      return "Lord Magus";
    }
    if (xp >= 8000) {
      return "Magus";
    }
    if (xp >= 6500) {
      return "Archmage";
    }
    if (xp >= 4000) {
      return "Mage";
    }
    if (xp >= 2000) {
      return "Adept";
    }
    if (xp >= 800) {
      return "Apprentice";
    }
    return "Novice";
  }
}
