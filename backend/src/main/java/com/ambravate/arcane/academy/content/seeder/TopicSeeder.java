package com.ambravate.arcane.academy.content.seeder;

import com.ambravate.arcane.academy.common.domain.Topic;
import com.ambravate.arcane.academy.common.repository.TopicRepository;
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
                .build(),
            Topic.builder()
                .id("react")
                .name("React")
                .glyph("⚛️")
                .tagline("Component-driven UIs. Hooks, state, and the modern frontend — all the way to deployment.")
                .accentColor("#61dafb")
                .sortOrder(3)
                .active(true)
                .build(),
            Topic.builder()
                .id("sql")
                .name("SQL")
                .glyph("🗃️")
                .tagline("The language of data. Read, filter, summarise — the queries every backend dev writes daily.")
                .accentColor("#7dd3fc")
                .sortOrder(4)
                .active(true)
                .build(),
            Topic.builder()
                .id("psychology")
                .name("Psychology")
                .glyph("🧠")
                .tagline("Cognition, behaviour, and the architecture of mind.")
                .accentColor("#a78bfa")
                .sortOrder(5)
                .active(true)
                .build(),
            Topic.builder()
                .id("genealogy")
                .name("Genealogy")
                .glyph("🌳")
                .tagline("Records, lineages, and DNA — the methods of family history.")
                .accentColor("#d4a73f")
                .sortOrder(6)
                .active(true)
                .build(),
            Topic.builder()
                .id("sciences")
                .name("Natural Sciences")
                .glyph("🔬")
                .tagline("Scientific method, physics, biology — the laws of the world.")
                .accentColor("#5dd5b5")
                .sortOrder(7)
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
