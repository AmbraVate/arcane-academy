package com.ambravate.arcane.academy.content.seeder;

import com.ambravate.arcane.academy.common.repository.ChunkRepository;
import com.ambravate.arcane.academy.content.repository.CuriosityQueueItemRepository;
import com.ambravate.arcane.academy.common.repository.QuestionRepository;
import com.ambravate.arcane.academy.content.repository.RabbitHoleModuleRepository;
import com.ambravate.arcane.academy.common.repository.SubChunkRepository;
import com.ambravate.arcane.academy.common.repository.UserChunkProgressRepository;

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
            log.info("[DataSeeder] Syncing JSON content ({} chunks currently present)...", currentCount);
            int loaded = jsonContentSeeder.seed();
            log.info("[DataSeeder] Synced {} JSON chunk files; database now has {} chunks.", loaded, chunkRepository.count());

            topicSeeder.seed();
            testUserSeeder.seed();
        };
    }
}
