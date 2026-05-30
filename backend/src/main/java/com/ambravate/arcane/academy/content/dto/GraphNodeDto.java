package com.ambravate.arcane.academy.content.dto;

import lombok.Builder;
import lombok.Value;

/**
 * A single node in the curriculum knowledge graph.
 *
 * <p>Node types:
 * <ul>
 *   <li>{@code DOMAIN} — a top-level domain (e.g. "java", "psychology")</li>
 *   <li>{@code MODULE} — a learning module within a domain</li>
 *   <li>{@code LESSON} — an individual lesson within a module</li>
 * </ul>
 *
 * <p>Used by {@link GraphResponseDto} to build the payload for {@code GET /api/graph}.
 */
@Value
@Builder
public class GraphNodeDto {

    /** Unique identifier — matches the entity id (domain slug, module id, lesson id). */
    String id;

    /** {@code DOMAIN | MODULE | LESSON} */
    String type;

    /** Display label for the node. */
    String label;

    /** Emoji glyph for the node (may be null for lessons). */
    String glyph;

    /**
     * The domain this node belongs to. For DOMAIN nodes this equals {@code id};
     * for MODULE and LESSON nodes it tracks which domain they live under.
     */
    String domainId;

    /** {@code APPRENTICE | JUNIOR | SENIOR | LEAD} — null for DOMAIN nodes. */
    String tier;

    /**
     * Progress status for the authenticated user:
     * {@code LOCKED | UNLOCKED | IN_PROGRESS | COMPLETE | NOT_STARTED}.
     * Null for DOMAIN nodes (domains themselves are not locked/unlocked).
     */
    String status;

    /** FSRS-based decayed memory strength in [0, 1]. 0.0 for unstarted nodes. */
    @Builder.Default
    double memoryStrength = 0.0;
}
