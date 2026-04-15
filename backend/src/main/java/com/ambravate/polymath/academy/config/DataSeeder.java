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
    private static final int EXPECTED_CHUNK_COUNT = 38; // 14 Foundation + 14 Practitioner + 10 Expert

    // ── Repositories (for clean reseed) ───────────────────────────────────
    private final ChunkRepository chunkRepository;
    private final SubChunkRepository subChunkRepository;
    private final QuestionRepository questionRepository;
    private final RabbitHoleModuleRepository rabbitHoleRepository;
    private final UserChunkProgressRepository userChunkProgressRepository;
    private final CuriosityQueueItemRepository curiosityQueueItemRepository;

    // ── Foundation seeders ─────────────────────────────────────────────────
    private final ChunkASeeder chunkASeeder;         // A: Variables & Primitives
    private final ChunkBSeeder chunkBSeeder;         // B: Operators & Control Flow
    private final ChunkCtoFSeeder chunkCtoFSeeder;   // C: Loops, D: Methods, E: Strings, F: Console I/O
    private final ChunkGtoLSeeder chunkGtoLSeeder;   // G: Arrays, H: Collections, I-L: OOP
    private final ChunkMtoNSeeder chunkMtoNSeeder;   // M: Exceptions, N: Core APIs

    // ── Practitioner seeders ───────────────────────────────────────────────
    private final PractitionerSeeder1 practitionerSeeder1; // PA-PG
    private final PractitionerSeeder2 practitionerSeeder2; // PH-PN

    // ── Expert seeders ─────────────────────────────────────────────────────
    private final ExpertSeeder1 expertSeeder1; // XA-XE
    private final ExpertSeeder2 expertSeeder2; // XF-XJ

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

                // ── Seed Foundation ────────────────────────────────────────
                chunkASeeder.seed();     // A
                chunkBSeeder.seed();     // B
                chunkCtoFSeeder.seed();  // C, D, E, F
                chunkGtoLSeeder.seed();  // G, H, I, J, K, L
                chunkMtoNSeeder.seed();  // M, N

                // ── Seed Practitioner ──────────────────────────────────────
                practitionerSeeder1.seed(); // PA-PG
                practitionerSeeder2.seed(); // PH-PN

                // ── Seed Expert ────────────────────────────────────────────
                expertSeeder1.seed();    // XA-XE
                expertSeeder2.seed();    // XF-XJ

                log.info("Seeded {} chunks.", chunkRepository.count());
            } else {
                log.info("Chunk data already seeded ({} chunks).", currentCount);
            }

            testUserSeeder.seed();
        };
    }
}
