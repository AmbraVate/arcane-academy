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
                .id("java")
                .name("Java")
                .glyph("â˜•")
                .tagline("From zero to job-ready â€” master the language that powers the enterprise.")
                .accentColor("#f89820")
                .sortOrder(1)
                .active(true)
                .build(),
            Domain.builder()
                .id("tailwind")
                .name("Tailwind CSS")
                .glyph("ðŸŽ¨")
                .tagline("Utility-first CSS â€” build polished, responsive UIs without leaving your HTML.")
                .accentColor("#38bdf8")
                .sortOrder(2)
                .active(true)
                .build(),
            Domain.builder()
                .id("react")
                .name("React")
                .glyph("âš›ï¸")
                .tagline("Component-driven UIs. Hooks, state, and the modern frontend â€” all the way to deployment.")
                .accentColor("#61dafb")
                .sortOrder(3)
                .active(true)
                .build(),
            Domain.builder()
                .id("sql")
                .name("SQL")
                .glyph("ðŸ—ƒï¸")
                .tagline("The language of data. Read, filter, summarise â€” the queries every backend dev writes daily.")
                .accentColor("#7dd3fc")
                .sortOrder(4)
                .active(true)
                .build(),
            Domain.builder()
                .id("psychology")
                .name("Psychology")
                .glyph("ðŸ§ ")
                .tagline("Cognition, behaviour, and the architecture of mind.")
                .accentColor("#a78bfa")
                .sortOrder(5)
                .active(true)
                .build(),
            Domain.builder()
                .id("genealogy")
                .name("Genealogy")
                .glyph("ðŸŒ³")
                .tagline("Records, lineages, and DNA â€” the methods of family history.")
                .accentColor("#d4a73f")
                .sortOrder(6)
                .active(true)
                .build(),
            Domain.builder()
                .id("sciences")
                .name("Natural Sciences")
                .glyph("ðŸ”¬")
                .tagline("Scientific method, physics, biology â€” the laws of the world.")
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
