/**
 * CurriculumGraph — interactive knowledge-map using React Flow (@xyflow/react).
 *
 * Renders three node types:
 *  - DOMAIN   (large, gold) — top-level domain e.g. "Java", "Psychology"
 *  - MODULE   (medium, tier-coloured) — learning module within a domain
 *  - LESSON   (small, strength-coloured) — individual lesson
 *
 * Three edge types:
 *  - HIERARCHY    — structural containment (solid, gray)
 *  - PREREQUISITE — must-complete-before (dashed, amber)
 *  - INTEGRATION  — cross-domain bridge (dotted, teal)
 *
 * Layout: simple three-row DAG computed by `layoutNodes()`:
 *   Row 0 (y=0)    — Domains, spread horizontally
 *   Row 1 (y=220)  — Modules, grouped under parent domain
 *   Row 2 (y=440)  — Lessons, grouped under parent module
 */

import React, { useMemo } from 'react'
import {
  ReactFlow,
  Background,
  Controls,
  MiniMap,
  Node,
  Edge,
  MarkerType,
  BackgroundVariant,
} from '@xyflow/react'
import '@xyflow/react/dist/style.css'
import type { GraphNode, GraphEdge } from '@/shared/types'

// ── Layout constants ──────────────────────────────────────────────────────────

const DOMAIN_SPACING   = 360   // horizontal gap between domain columns
const MODULE_SPACING   = 200   // horizontal gap between modules under a domain
const LESSON_SPACING   = 130   // horizontal gap between lessons under a module
const DOMAIN_Y         = 0
const MODULE_Y         = 220
const LESSON_Y         = 440

// ── Colour helpers ────────────────────────────────────────────────────────────

/** Maps a memory-strength value [0, 1] → a health-signal CSS colour. */
function strengthColor(strength: number): string {
  if (strength <= 0) return '#64748b'   // slate — not started
  if (strength < 0.4) return '#dc2626'  // red — fading fast
  if (strength < 0.7) return '#d97706'  // amber — needs review
  return '#16a34a'                      // green — healthy
}

/** Maps a module status string → a badge colour. */
function statusColor(status: string | undefined): string {
  switch (status) {
    case 'COMPLETE':    return '#16a34a'
    case 'IN_PROGRESS': return '#7c3aed'
    case 'UNLOCKED':    return '#0369a1'
    case 'LOCKED':      return '#374151'
    default:            return '#475569'
  }
}

// ── Layout algorithm ──────────────────────────────────────────────────────────

/**
 * Computes positions for all nodes in a three-row hierarchical layout.
 * Returns a map from node id → `{ x, y }`.
 */
function computePositions(
  graphNodes: GraphNode[],
  graphEdges: GraphEdge[],
): Map<string, { x: number; y: number }> {
  const positions = new Map<string, { x: number; y: number }>()

  const domains  = graphNodes.filter(n => n.type === 'DOMAIN')
  const modules  = graphNodes.filter(n => n.type === 'MODULE')
  const lessons  = graphNodes.filter(n => n.type === 'LESSON')

  // hierarchy edges: source = parent, target = child
  const parentOf = new Map<string, string>()
  for (const e of graphEdges) {
    if (e.edgeType === 'HIERARCHY') {
      parentOf.set(e.target, e.source)
    }
  }

  // ── Row 0: Domains ───────────────────────────────────────────────────────────
  domains.forEach((d, i) => {
    positions.set(d.id, { x: i * DOMAIN_SPACING, y: DOMAIN_Y })
  })

  // ── Row 1: Modules grouped by domain ─────────────────────────────────────────
  const modulesByDomain = new Map<string, GraphNode[]>()
  for (const m of modules) {
    const parent = parentOf.get(m.id) ?? m.domainId ?? '__unknown__'
    const arr = modulesByDomain.get(parent) ?? []
    arr.push(m)
    modulesByDomain.set(parent, arr)
  }

  for (const [domainId, mods] of modulesByDomain) {
    const domainPos = positions.get(domainId) ?? { x: 0, y: 0 }
    const totalWidth = (mods.length - 1) * MODULE_SPACING
    const startX = domainPos.x - totalWidth / 2
    mods.forEach((m, i) => {
      positions.set(m.id, { x: startX + i * MODULE_SPACING, y: MODULE_Y })
    })
  }

  // ── Row 2: Lessons grouped by module ─────────────────────────────────────────
  const lessonsByModule = new Map<string, GraphNode[]>()
  for (const l of lessons) {
    const parent = parentOf.get(l.id) ?? '__unknown__'
    const arr = lessonsByModule.get(parent) ?? []
    arr.push(l)
    lessonsByModule.set(parent, arr)
  }

  for (const [moduleId, lsns] of lessonsByModule) {
    const modulePos = positions.get(moduleId) ?? { x: 0, y: 0 }
    const totalWidth = (lsns.length - 1) * LESSON_SPACING
    const startX = modulePos.x - totalWidth / 2
    lsns.forEach((l, i) => {
      positions.set(l.id, { x: startX + i * LESSON_SPACING, y: LESSON_Y })
    })
  }

  return positions
}

// ── Node builder ──────────────────────────────────────────────────────────────

function buildRFNodes(
  graphNodes: GraphNode[],
  positions: Map<string, { x: number; y: number }>,
): Node[] {
  return graphNodes.map(n => {
    const pos = positions.get(n.id) ?? { x: 0, y: 0 }

    let style: React.CSSProperties
    let width: number
    let height: number

    if (n.type === 'DOMAIN') {
      style = {
        background: '#1a1025',
        border: '2px solid #c9a227',
        color: '#c9a227',
        borderRadius: '12px',
        fontSize: '14px',
        fontWeight: 700,
        padding: '10px 16px',
        textAlign: 'center',
        minWidth: '140px',
      }
      width = 160
      height = 60
    } else if (n.type === 'MODULE') {
      const sc = statusColor(n.status)
      style = {
        background: '#0f0c1a',
        border: `1.5px solid ${sc}`,
        color: '#e2e8f0',
        borderRadius: '8px',
        fontSize: '11px',
        padding: '8px 10px',
        textAlign: 'center',
        minWidth: '120px',
      }
      width = 140
      height = 52
    } else {
      // LESSON
      const sc = strengthColor(n.memoryStrength ?? 0)
      style = {
        background: '#0a0715',
        border: `1px solid ${sc}`,
        color: '#94a3b8',
        borderRadius: '6px',
        fontSize: '10px',
        padding: '5px 8px',
        textAlign: 'center',
        minWidth: '90px',
      }
      width = 110
      height = 40
    }

    const glyphPrefix = n.glyph ? `${n.glyph} ` : ''
    const label = `${glyphPrefix}${n.label}`

    return {
      id: n.id,
      position: pos,
      data: {
        label: (
          <div style={{ wordBreak: 'break-word', lineHeight: 1.3 }}>
            {label}
          </div>
        ),
      },
      style,
      width,
      height,
    }
  })
}

// ── Edge builder ──────────────────────────────────────────────────────────────

function buildRFEdges(graphEdges: GraphEdge[]): Edge[] {
  return graphEdges.map(e => {
    let style: React.CSSProperties
    let markerEnd: Edge['markerEnd']
    let animated = false

    switch (e.edgeType) {
      case 'HIERARCHY':
        style = { stroke: '#475569', strokeWidth: 1 }
        markerEnd = { type: MarkerType.ArrowClosed, color: '#475569' }
        break
      case 'PREREQUISITE':
        style = { stroke: '#d97706', strokeWidth: 1.5, strokeDasharray: '5 4' }
        markerEnd = { type: MarkerType.ArrowClosed, color: '#d97706' }
        break
      case 'INTEGRATION':
        style = { stroke: '#0ea5e9', strokeWidth: 1, strokeDasharray: '3 3', opacity: 0.6 }
        markerEnd = { type: MarkerType.ArrowClosed, color: '#0ea5e9' }
        animated = true
        break
      default:
        style = { stroke: '#374151', strokeWidth: 1 }
        markerEnd = { type: MarkerType.ArrowClosed, color: '#374151' }
    }

    return {
      id: e.id,
      source: e.source,
      target: e.target,
      style,
      markerEnd,
      animated,
      type: 'smoothstep',
    }
  })
}

// ── Component ─────────────────────────────────────────────────────────────────

export interface CurriculumGraphProps {
  nodes: GraphNode[]
  edges: GraphEdge[]
}

/**
 * Interactive curriculum knowledge map.
 *
 * @param nodes  Graph nodes from `GET /api/graph`
 * @param edges  Graph edges from `GET /api/graph`
 */
export function CurriculumGraph({ nodes: graphNodes, edges: graphEdges }: CurriculumGraphProps) {
  const rfNodes = useMemo(() => {
    const positions = computePositions(graphNodes, graphEdges)
    return buildRFNodes(graphNodes, positions)
  }, [graphNodes, graphEdges])

  const rfEdges = useMemo(() => buildRFEdges(graphEdges), [graphEdges])

  return (
    <div style={{ width: '100%', height: '100%', background: '#050311' }}>
      <ReactFlow
        nodes={rfNodes}
        edges={rfEdges}
        fitView
        fitViewOptions={{ padding: 0.1 }}
        nodesDraggable={false}
        nodesConnectable={false}
        elementsSelectable
        colorMode="dark"
        proOptions={{ hideAttribution: true }}
      >
        <Background variant={BackgroundVariant.Dots} gap={20} size={1} color="#1e1b4b" />
        <Controls showInteractive={false} />
        <MiniMap
          nodeColor={node => {
            const style = node.style as React.CSSProperties | undefined
            return (style?.borderColor as string | undefined) ?? '#475569'
          }}
          maskColor="rgba(5, 3, 17, 0.7)"
          style={{ background: '#0f0c1a', border: '1px solid #1e1b4b' }}
        />
      </ReactFlow>
    </div>
  )
}

// ── Legend ────────────────────────────────────────────────────────────────────

export function GraphLegend() {
  const items = [
    { label: 'Domain',      color: '#c9a227', dash: ''       },
    { label: 'Module',      color: '#0369a1', dash: ''       },
    { label: 'Lesson',      color: '#64748b', dash: ''       },
    { label: 'Prerequisite', color: '#d97706', dash: '5 4'  },
    { label: 'Integration', color: '#0ea5e9', dash: '3 3'   },
  ]

  return (
    <div className="flex flex-wrap gap-4 text-xs text-muted px-4 py-2">
      {items.map(({ label, color, dash }) => (
        <div key={label} className="flex items-center gap-1.5">
          <svg width="24" height="12">
            <line
              x1="0" y1="6" x2="24" y2="6"
              stroke={color}
              strokeWidth="2"
              strokeDasharray={dash || undefined}
            />
          </svg>
          <span style={{ color }}>{label}</span>
        </div>
      ))}
    </div>
  )
}

export default CurriculumGraph
