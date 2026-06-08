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
                .tagline("Build reliable systems — computational thinking, design, and architecture.")
                .accentColor("#2dd4bf")
                .sortOrder(1)
                .active(true)
                .build(),
            Domain.builder()
                .id("frontend-engineering")
                .name("Frontend Engineering")
                .glyph("🖥️")
                .tagline("HTML, CSS, JavaScript, React — build polished, accessible, production-quality UIs.")
                .accentColor("#f59e0b")
                .sortOrder(2)
                .active(true)
                .build(),
            Domain.builder()
                .id("data-engineering")
                .name("Data Engineering")
                .glyph("🗄️")
                .tagline("SQL, databases, pipelines, and data architecture — from a single table to enterprise scale.")
                .accentColor("#6366f1")
                .sortOrder(3)
                .active(true)
                .build()
        );

        for (Domain d : defaults) {
            if (!domainRepository.existsById(d.getId())) {
                domainRepository.save(d);
                log.info("[DomainSeeder] Created domain: {}", d.getId());
            }
        }
    }
}
