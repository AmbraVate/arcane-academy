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

    // ── Expected totals ────────────────────────────────────────────────────
    // Java: 14 Foundation (A-N) + 14 Practitioner (PA-PN) + 10 Expert (XA-XJ)
    // Tailwind: tw-a (Foundation) + tw-b (Practitioner) + tw-c (Expert)
    private static final int EXPECTED_CHUNK_COUNT = 41;

    // ── Repositories (for clean reseed) ───────────────────────────────────
    private final ChunkRepository chunkRepository;
    private final SubChunkRepository subChunkRepository;
    private final QuestionRepository questionRepository;
    private final RabbitHoleModuleRepository rabbitHoleRepository;
    private final UserChunkProgressRepository userChunkProgressRepository;
    private final CuriosityQueueItemRepository curiosityQueueItemRepository;

    // ── JSON content seeder — loads all content from resources/content/**/*.json
    private final JsonContentSeeder jsonContentSeeder;

    // ── Test users ─────────────────────────────────────────────────────────
    private final TestUserSeeder testUserSeeder;

    @Bean
    public ApplicationRunner seedData() {
        return args -> {
            long currentCount = chunkRepository.count();
            if (currentCount != EXPECTED_CHUNK_COUNT) {
                log.info("Reseeding: found {} chunks, expected {}. Clearing and rebuilding...",
                        currentCount, EXPECTED_CHUNK_COUNT);

                // Clear in FK-safe order
                userChunkProgressRepository.deleteAll();
                curiosityQueueItemRepository.deleteAll();
                questionRepository.deleteAll();
                rabbitHoleRepository.deleteAll();
                subChunkRepository.deleteAll();
                chunkRepository.deleteAll();

                // Seed all content from JSON resource files
                jsonContentSeeder.seed();

                log.info("Seeded {} chunks.", chunkRepository.count());
            } else {
                log.info("Chunk data already seeded ({} chunks).", currentCount);
            }

            testUserSeeder.seed();
        };
    }
}
