/**
 * KnowledgeMapPage — the /knowledge-map route.
 *
 * Shows the full curriculum as an interactive React Flow knowledge graph.
 * Nodes are coloured by the learner's progress and FSRS memory strength.
 * Integration edges (dotted teal) visualise cross-domain connections from
 * lesson frontmatter.
 */

import React from 'react'
import { useGraph } from '@/hooks/queries'
import { CurriculumGraph, GraphLegend } from '../components/CurriculumGraph'
import { Network } from 'lucide-react'

export default function KnowledgeMapPage() {
  const { data, isLoading, isError, error } = useGraph()

  // ── Loading ────────────────────────────────────────────────────────────────
  if (isLoading) {
    return (
      <div className="flex flex-col items-center justify-center h-[60vh] gap-4 text-muted">
        <div className="w-10 h-10 animate-spin rounded-full border-2 border-border border-t-purple" />
        <p className="font-cinzel text-sm tracking-wide">Conjuring knowledge map…</p>
      </div>
    )
  }

  // ── Error ──────────────────────────────────────────────────────────────────
  if (isError || !data) {
    return (
      <div className="flex flex-col items-center justify-center h-[60vh] gap-3 text-muted">
        <Network size={32} className="opacity-40" />
        <p className="text-sm">
          {isError && error instanceof Error
            ? error.message
            : 'Could not load the knowledge map.'}
        </p>
      </div>
    )
  }

  const nodeCount  = data.nodes.length
  const edgeCount  = data.edges.length
  const integCount = data.edges.filter(e => e.edgeType === 'INTEGRATION').length

  // ── Render ─────────────────────────────────────────────────────────────────
  return (
    <div className="flex flex-col h-[calc(100vh-50px)]">
      {/* Header */}
      <div className="flex-shrink-0 px-4 py-3 border-b border-border flex items-center gap-3">
        <Network size={18} className="text-purple" />
        <div>
          <h1 className="font-cinzel text-[15px] text-gold tracking-wide">
            Knowledge Map
          </h1>
          <p className="text-[11px] text-muted mt-0.5">
            {nodeCount} nodes · {edgeCount} edges
            {integCount > 0 && ` · ${integCount} cross-domain connections`}
          </p>
        </div>
      </div>

      {/* Legend */}
      <div className="flex-shrink-0 border-b border-border">
        <GraphLegend />
      </div>

      {/* Graph canvas — fills remaining height */}
      <div className="flex-1 min-h-0">
        <CurriculumGraph nodes={data.nodes} edges={data.edges} />
      </div>
    </div>
  )
}
