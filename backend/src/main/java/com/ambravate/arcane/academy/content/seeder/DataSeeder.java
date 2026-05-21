package com.ambravate.arcane.academy.content.seeder;

import com.ambravate.arcane.academy.content.repository.ChunkRepository;
import com.ambravate.arcane.academy.content.repository.CuriosityQueueItemRepository;
import com.ambravate.arcane.academy.content.repository.QuestionRepository;
import com.ambravate.arcane.academy.content.repository.RabbitHoleModuleRepository;
import com.ambravate.arcane.academy.content.repository.SubChunkRepository;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    // ── Repositories ──────────────────────────────────────────────────────
    private final ChunkRepository chunkRepository;
    private final SubChunkRepository subChunkRepository;
    private final QuestionRepository questionRepository;
    private final RabbitHoleModuleRepository rabbitHoleRepository;
    private final UserChunkProgressRepository userChunkProgressRepository;
    private final CuriosityQueueItemRepository curiosityQueueItemRepository;

    // ── Seeders ────────────────────────────────────────────────────────────
    private final JsonContentSeeder jsonContentSeeder;
    private final TestUserSeeder testUserSeeder;
    private final TopicSeeder topicSeeder;

    @Bean
    public ApplicationRunner seedData() {
        return args -> {
            long currentCount = chunkRepository.count();

            if (currentCount > 0) {
                // Content already seeded — skip the expensive JSON sync on cold starts.
                // To reseed from scratch: clear the chunks table and redeploy.
                log.info("[DataSeeder] {} chunks already present — skipping JSON seed.", currentCount);
            } else {
                log.info("[DataSeeder] No chunks found — running initial seed...");
                int maxAttempts = 5;
                for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                    try {
                        int loaded = jsonContentSeeder.seed();
                        log.info("[DataSeeder] Seeded {} JSON chunk files; database now has {} chunks.",
                                loaded, chunkRepository.count());
                        break;
                    } catch (Exception e) {
                        log.warn("[DataSeeder] Seed attempt {}/{} failed: {}", attempt, maxAttempts, e.getMessage());
                        if (attempt == maxAttempts) {
                            log.error("[DataSeeder] All {} seed attempts failed. App will start with no content.", maxAttempts, e);
                        } else {
                            long delay = 5000L * attempt; // 5 s, 10 s, 15 s, 20 s
                            log.info("[DataSeeder] Retrying in {} ms (fresh DB connection will be used)...", delay);
                            Thread.sleep(delay);
                        }
                    }
                }
            }

            // Always run these — they are fast and idempotent
            topicSeeder.seed();
            testUserSeeder.seed();
        };
    }
}
