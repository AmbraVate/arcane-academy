package com.ambravate.polymath.academy.config;

import com.ambravate.polymath.academy.model.Topic;
import com.ambravate.polymath.academy.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TopicSeeder {

    private final TopicRepository topicRepository;

    public void seed() {
        List<Topic> defaults = List.of(
            Topic.builder()
                .id("java")
                .name("Java")
                .glyph("☕")
                .tagline("From zero to job-ready — master the language that powers the enterprise.")
                .accentColor("#f89820")
                .sortOrder(1)
                .active(true)
                .build(),
            Topic.builder()
                .id("tailwind")
                .name("Tailwind CSS")
                .glyph("🎨")
                .tagline("Utility-first CSS — build polished, responsive UIs without leaving your HTML.")
                .accentColor("#38bdf8")
                .sortOrder(2)
                .active(true)
                .build()
        );

        for (Topic t : defaults) {
            if (!topicRepository.existsById(t.getId())) {
                topicRepository.save(t);
                log.info("[TopicSeeder] Created topic: {}", t.getId());
            }
        }
    }
}
