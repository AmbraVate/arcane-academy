package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.repository.ChunkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final ChunkRepository chunkRepository;
    private final ChunkASeeder chunkASeeder;
    private final ChunkBSeeder chunkBSeeder;
    private final ChunkCtoKSeeder chunkCtoKSeeder;
    private final TestUserSeeder testUserSeeder;

    @Bean
    public ApplicationRunner seedData() {
        return args -> {
            if (chunkRepository.count() == 0) {
                log.info("Seeding chunks...");
                chunkASeeder.seed();
                chunkBSeeder.seed();
                chunkCtoKSeeder.seed();
                log.info("Seeded {} chunks.", chunkRepository.count());
            }

            testUserSeeder.seed();
        };
    }
}
