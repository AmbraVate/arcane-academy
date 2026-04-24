package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.repository.*;
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
    private final AdminUserPromoter adminUserPromoter;
    private final TopicSeeder topicSeeder;

    @Bean
    public ApplicationRunner seedData() {
        return args -> {
            long currentCount = chunkRepository.count();
            if (currentCount == 0) {
                log.info("[DataSeeder] No chunks found — running initial seed...");

                // Clear dependents in FK-safe order (in case of partial seed)
                userChunkProgressRepository.deleteAll();
                curiosityQueueItemRepository.deleteAll();
                questionRepository.deleteAll();
                rabbitHoleRepository.deleteAll();
                subChunkRepository.deleteAll();
                chunkRepository.deleteAll();

                jsonContentSeeder.seed();
                log.info("[DataSeeder] Seeded {} chunks.", chunkRepository.count());
            } else {
                log.info("[DataSeeder] Content already present ({} chunks) — skipping seed.", currentCount);
            }

            topicSeeder.seed();
            testUserSeeder.seed();
            adminUserPromoter.promote();
        };
    }
}
