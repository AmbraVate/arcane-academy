package com.ambravate.arcane.academy.content.seeder;

import com.ambravate.arcane.academy.common.domain.Domain;
import com.ambravate.arcane.academy.content.repository.DomainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DomainSeeder {

    private final DomainRepository domainRepository;

    public void seed() {
        List<Domain> defaults = List.of(
            Domain.builder()
                .id("software-engineering")
                .name("Software Engineering")
                .glyph("⚙️")
                .tagline("Build reliable systems - computational thinking, design, and architecture.")
                .accentColor("#2dd4bf")
                .sortOrder(1)
                .active(true)
                .build(),
            Domain.builder()
                .id("tailwind")
                .name("Tailwind CSS")
                .glyph("🎨")
                .tagline("Utility-first CSS - build polished, responsive UIs without leaving your HTML.")
                .accentColor("#38bdf8")
                .sortOrder(2)
                .active(true)
                .build(),
            Domain.builder()
                .id("react")
                .name("React")
                .glyph("⚛️")
                .tagline("Component-driven UIs. Hooks, state, and the modern frontend - all the way to deployment.")
                .accentColor("#61dafb")
                .sortOrder(3)
                .active(true)
                .build(),
            Domain.builder()
                .id("sql")
                .name("SQL")
                .glyph("🗃️")
                .tagline("The language of data. Read, filter, summarise - the queries every backend dev writes daily.")
                .accentColor("#7dd3fc")
                .sortOrder(4)
                .active(true)
                .build(),
            Domain.builder()
                .id("psychology")
                .name("Psychology")
                .glyph("🧠")
                .tagline("Cognition, behaviour, and the architecture of mind.")
                .accentColor("#a78bfa")
                .sortOrder(5)
                .active(true)
                .build(),
            Domain.builder()
                .id("genealogy")
                .name("Genealogy")
                .glyph("🌳")
                .tagline("Records, lineages, and DNA - the methods of family history.")
                .accentColor("#d4a73f")
                .sortOrder(6)
                .active(true)
                .build(),
            Domain.builder()
                .id("sciences")
                .name("Natural Sciences")
                .glyph("🔬")
                .tagline("Scientific method, physics, biology - the laws of the world.")
                .accentColor("#5dd5b5")
                .sortOrder(7)
                .active(true)
                .build()
        );

        for (Domain t : defaults) {
            if (!domainRepository.existsById(t.getId())) {
                domainRepository.save(t);
                log.info("[DomainSeeder] Created domain: {}", t.getId());
            }
        }
    }
}
