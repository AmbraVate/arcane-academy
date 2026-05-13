package com.ambravate.arcane.academy.common.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "curiosity_queue",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "sub_chunk_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CuriosityQueueItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "sub_chunk_id", nullable = false)
    private String subChunkId;

    @Builder.Default
    private Instant savedAt = Instant.now();
}
