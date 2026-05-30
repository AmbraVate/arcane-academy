package com.ambravate.arcane.academy.content.dto;

import lombok.Builder;
import lombok.Value;

/**
 * A directed edge in the curriculum knowledge graph.
 *
 * <p>Edge types:
 * <ul>
 *   <li>{@code HIERARCHY} — structural containment: DOMAIN→MODULE or MODULE→LESSON</li>
 *   <li>{@code PREREQUISITE} — a module must be completed before its target</li>
 *   <li>{@code INTEGRATION} — a lesson bridges to a cross-domain concept</li>
 * </ul>
 */
@Value
@Builder
public class GraphEdgeDto {

    /** Unique edge identifier. */
    String id;

    /** Source node id. */
    String source;

    /** Target node id. */
    String target;

    /** {@code HIERARCHY | PREREQUISITE | INTEGRATION} */
    String edgeType;
}
