package com.arcane.academy.config;

import com.arcane.academy.repository.BossRepository;
import com.arcane.academy.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final QuestRepository questRepository;
    private final BossRepository bossRepository;

    private final Ch1Seeder ch1Seeder;
    private final Ch2Seeder ch2Seeder;
    private final Ch3Seeder ch3Seeder;
    private final Ch4Seeder ch4Seeder;
    private final Ch5Seeder ch5Seeder;
    private final Ch6Seeder ch6Seeder;
    private final Ch7Seeder ch7Seeder;
    private final Ch8Seeder ch8Seeder;
    private final BossSeeder bossSeeder;
    private final TestUserSeeder testUserSeeder;

    @Bean
    public ApplicationRunner seedData() {
        return args -> {
            if (questRepository.count() == 0) {
                log.info("Seeding quests...");
                ch1Seeder.seed();
                ch2Seeder.seed();
                ch3Seeder.seed();
                ch4Seeder.seed();
                ch5Seeder.seed();
                ch6Seeder.seed();
                ch7Seeder.seed();
                ch8Seeder.seed();
                log.info("Seeded {} quests.", questRepository.count());
            }

            if (bossRepository.count() == 0) {
                bossSeeder.seed();
            }

            testUserSeeder.seed();
        };
    }
}
