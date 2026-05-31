package com.ambravate.arcane.academy.content.repository;

import com.ambravate.arcane.academy.common.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends JpaRepository<Track, String> {
}
