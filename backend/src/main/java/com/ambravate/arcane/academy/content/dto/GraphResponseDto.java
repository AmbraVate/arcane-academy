package com.ambravate.arcane.academy.content.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * Full curriculum knowledge-graph payload returned by {@code GET /api/graph}.
 *
 * <p>The frontend consumes this to render an interactive React Flow graph
 * (Phase 7) with HIERARCHY, PREREQUISITE, and INTEGRATION edges.
 */
@Value
@Builder
public class GraphResponseDto {

    /** All nodes in the graph (DOMAIN, MODULE, LESSON). */
    List<GraphNodeDto> nodes;

    /** All directed edges between nodes. */
    List<GraphEdgeDto> edges;
}
