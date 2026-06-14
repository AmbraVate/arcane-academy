package com.ambravate.arcane.academy.content.seeder;

import com.ambravate.arcane.academy.common.domain.Track;
import com.ambravate.arcane.academy.content.repository.DomainRepository;
import com.ambravate.arcane.academy.content.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Ensures every domain has a corresponding track.
 *
 * <p>Flyway migrations (V27, V35) create tracks at migration time, but domains
 * inserted by {@link DomainSeeder} at runtime may arrive after those migrations
 * have already run, leaving them without a track.  This seeder runs after
 * {@link DomainSeeder} and before any content seeding, so the FK constraint on
 * {@code modules.track_id → tracks.id} is always satisfied.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TrackSeeder {

    private final DomainRepository domainRepository;
    private final TrackRepository  trackRepository;

    public void seed() {
        domainRepository.findAll().forEach(domain -> {
            if (!trackRepository.existsById(domain.getId())) {
                Track track = Track.builder()
                        .id(domain.getId())
                        .domainId(domain.getId())
                        .name(domain.getName())
                        .description("Core curriculum for " + domain.getName())
                        .sortOrder(1)
                        .build();
                trackRepository.save(track);
                log.info("[TrackSeeder] Created missing track: {}", domain.getId());
            }
        });
    }
}
