/**
 * CurriculumGraph — interactive knowledge map using React Flow.
 *
 * Default view: Domain + Module nodes only, arranged in vertical columns
 * (one column per domain, modules stacked beneath it).  Lesson nodes are
 * hidden until the user enables "Show Lessons", which then drills into a
 * single selected domain.
 *
 * Controls:
 *  - Domain filter chips — focus on one domain or view all
 *  - "Show Lessons" toggle — expand lesson nodes under the focused domain
 */

import React, { useMemo, useState } from 'react'
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
import { useNavigate } from 'react-router-dom'
import type { GraphNode, GraphEdge } from '@/shared/types'

// ── Layout constants ──────────────────────────────────────────────────────────

const COL_WIDTH        = 220   // horizontal centre-to-centre of domain columns
const MODULE_ROW_H     = 78    // vertical gap between module rows
const LESSON_ROW_H     = 52    // vertical gap between lesson rows
const DOMAIN_Y         = 0
const MODULE_Y_START   = 110   // first module below domain
const LESSON_Y_START   = 80    // first lesson below module (relative to module)

// Node dimensions
const DOMAIN_W  = 180; const DOMAIN_H  = 60
const MODULE_W  = 160; const MODULE_H  = 56
const LESSON_W  = 130; const LESSON_H  = 40

// ── Colour helpers ────────────────────────────────────────────────────────────

function strengthColor(strength: number): string {
  if (strength <= 0)   return '#475569'   // slate — not started
  if (strength < 0.4)  return '#dc2626'   // red — fading
  if (strength < 0.7)  return '#d97706'   // amber — needs review
  return '#16a34a'                        // green — healthy
}

function statusColor(status: string | undefined): string {
  switch (status) {
    case 'COMPLETE':    return '#16a34a'
    case 'IN_PROGRESS': return '#7c3aed'
    case 'UNLOCKED':    return '#0369a1'
    default:            return '#334155'
  }
}

const DOMAIN_COLORS = [
  '#c9a227', '#8b5cf6', '#0ea5e9', '#16a34a',
  '#e11d48', '#d97706', '#0891b2',
]

// ── Layout algorithm ──────────────────────────────────────────────────────────

function computePositions(
  graphNodes: GraphNode[],
  graphEdges: GraphEdge[],
  activeDomain: string | null,
  showLessons: boolean,
): Map<string, { x: number; y: number }> {
  const positions = new Map<string, { x: number; y: number }>()

  const parentOf = new Map<string, string>()
  for (const e of graphEdges) {
    if (e.edgeType === 'HIERARCHY') parentOf.set(e.target, e.source)
  }

  const domains = graphNodes.filter(n => n.type === 'DOMAIN')
  const modules = graphNodes.filter(n => n.type === 'MODULE')
  const lessons = graphNodes.filter(n => n.type === 'LESSON')

  const visibleDomains = activeDomain
    ? domains.filter(d => d.id === activeDomain)
    : domains

  // Group modules by domain
  const modulesByDomain = new Map<string, GraphNode[]>()
  for (const m of modules) {
    const domainId = parentOf.get(m.id) ?? m.domainId ?? '__unknown__'
    if (!modulesByDomain.has(domainId)) modulesByDomain.set(domainId, [])
    modulesByDomain.get(domainId)!.push(m)
  }

  // Group lessons by module
  const lessonsByModule = new Map<string, GraphNode[]>()
  for (const l of lessons) {
    const moduleId = parentOf.get(l.id) ?? '__unknown__'
    if (!lessonsByModule.has(moduleId)) lessonsByModule.set(moduleId, [])
    lessonsByModule.get(moduleId)!.push(l)
  }

  // Place domain columns
  visibleDomains.forEach((d, colIdx) => {
    const colX = colIdx * COL_WIDTH
    positions.set(d.id, { x: colX, y: DOMAIN_Y })

    const mods = modulesByDomain.get(d.id) ?? []

    mods.forEach((m, rowIdx) => {
      const modY = MODULE_Y_START + rowIdx * MODULE_ROW_H
      positions.set(m.id, { x: colX, y: modY })

      if (showLessons) {
        const lsns = lessonsByModule.get(m.id) ?? []
        const lessonColSpan = LESSON_W + 8
        const totalLsnWidth = lsns.length * lessonColSpan
        const startX = colX - totalLsnWidth / 2 + lessonColSpan / 2

        lsns.forEach((l, lIdx) => {
          positions.set(l.id, {
            x: startX + lIdx * lessonColSpan,
            y: modY + MODULE_H + LESSON_Y_START + lIdx * 0,  // same row per module
          })
        })
      }
    })
  })

  return positions
}

// ── Node / edge builders ──────────────────────────────────────────────────────

function buildRFNodes(
  graphNodes: GraphNode[],
  positions: Map<string, { x: number; y: number }>,
  activeDomain: string | null,
  showLessons: boolean,
  domainColorMap: Map<string, string>,
  onNodeClick: (n: GraphNode) => void,
): Node[] {
  return graphNodes
    .filter(n => {
      if (!positions.has(n.id)) return false
      if (n.type === 'LESSON' && !showLessons) return false
      if (activeDomain && n.type === 'MODULE' && n.domainId !== activeDomain) return false
      if (activeDomain && n.type === 'LESSON') {
        // keep only lessons whose domain matches
        if (n.domainId !== activeDomain) return false
      }
      return true
    })
    .map(n => {
      const pos = positions.get(n.id)!
      const domainColor = domainColorMap.get(n.domainId ?? n.id) ?? '#c9a227'

      let style: React.CSSProperties
      let width: number
      let height: number

      if (n.type === 'DOMAIN') {
        style = {
          background: 'rgba(26, 16, 37, 0.95)',
          border: `2px solid ${domainColor}`,
          color: domainColor,
          borderRadius: '12px',
          fontSize: '13px',
          fontWeight: 700,
          fontFamily: "'Cinzel', serif",
          padding: '0 14px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          textAlign: 'center',
          cursor: 'pointer',
          boxShadow: `0 0 18px ${domainColor}22`,
        }
        width = DOMAIN_W
        height = DOMAIN_H
      } else if (n.type === 'MODULE') {
        const sc = statusColor(n.status)
        style = {
          background: '#0f0c1a',
          border: `1.5px solid ${sc}`,
          borderLeft: `3px solid ${domainColor}`,
          color: '#e2e8f0',
          borderRadius: '8px',
          fontSize: '11px',
          padding: '0 10px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          textAlign: 'center',
          cursor: 'pointer',
        }
        width = MODULE_W
        height = MODULE_H
      } else {
        const sc = strengthColor(n.memoryStrength ?? 0)
        style = {
          background: '#07050f',
          border: `1px solid ${sc}`,
          color: '#94a3b8',
          borderRadius: '6px',
          fontSize: '10px',
          padding: '0 6px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          textAlign: 'center',
          cursor: 'pointer',
        }
        width = LESSON_W
        height = LESSON_H
      }

      const label = n.glyph ? `${n.glyph} ${n.label}` : n.label

      return {
        id: n.id,
        position: pos,
        data: {
          label: (
            <div
              style={{ wordBreak: 'break-word', lineHeight: 1.3, width: '100%' }}
              onClick={() => onNodeClick(n)}
            >
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

function buildRFEdges(
  graphEdges: GraphEdge[],
  visibleNodeIds: Set<string>,
): Edge[] {
  return graphEdges
    .filter(e => visibleNodeIds.has(e.source) && visibleNodeIds.has(e.target))
    .map(e => {
      let style: React.CSSProperties
      let markerEnd: Edge['markerEnd']
      const animated = e.edgeType === 'INTEGRATION'

      switch (e.edgeType) {
        case 'HIERARCHY':
          style = { stroke: '#334155', strokeWidth: 1.5 }
          markerEnd = { type: MarkerType.ArrowClosed, color: '#334155', width: 12, height: 12 }
          break
        case 'PREREQUISITE':
          style = { stroke: '#d97706', strokeWidth: 1.5, strokeDasharray: '5 4' }
          markerEnd = { type: MarkerType.ArrowClosed, color: '#d97706', width: 12, height: 12 }
          break
        case 'INTEGRATION':
          style = { stroke: '#0ea5e9', strokeWidth: 1, strokeDasharray: '3 3', opacity: 0.5 }
          markerEnd = { type: MarkerType.ArrowClosed, color: '#0ea5e9', width: 10, height: 10 }
          break
        default:
          style = { stroke: '#334155', strokeWidth: 1 }
          markerEnd = { type: MarkerType.ArrowClosed, color: '#334155' }
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

export function CurriculumGraph({ nodes: graphNodes, edges: graphEdges }: CurriculumGraphProps) {
  const navigate = useNavigate()
  const domains = useMemo(() => graphNodes.filter(n => n.type === 'DOMAIN'), [graphNodes])

  const [activeDomain, setActiveDomain] = useState<string | null>(null)
  const [showLessons, setShowLessons] = useState(false)

  // Assign a stable colour to each domain
  const domainColorMap = useMemo(() => {
    const map = new Map<string, string>()
    domains.forEach((d, i) => map.set(d.id, DOMAIN_COLORS[i % DOMAIN_COLORS.length]))
    return map
  }, [domains])

  function handleNodeClick(n: GraphNode) {
    if (n.type === 'DOMAIN') {
      setActiveDomain(prev => prev === n.id ? null : n.id)
      setShowLessons(false)
    } else if (n.type === 'MODULE') {
      navigate(`/domain/${n.domainId}`)
    } else if (n.type === 'LESSON') {
      navigate(`/domain/${n.domainId}`)
    }
  }

  const positions = useMemo(
    () => computePositions(graphNodes, graphEdges, activeDomain, showLessons),
    [graphNodes, graphEdges, activeDomain, showLessons],
  )

  const rfNodes = useMemo(
    () => buildRFNodes(graphNodes, positions, activeDomain, showLessons, domainColorMap, handleNodeClick),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [graphNodes, positions, activeDomain, showLessons, domainColorMap],
  )

  const visibleNodeIds = useMemo(() => new Set(rfNodes.map(n => n.id)), [rfNodes])

  const rfEdges = useMemo(
    () => buildRFEdges(graphEdges, visibleNodeIds),
    [graphEdges, visibleNodeIds],
  )

  const activeModuleCount = activeDomain
    ? graphNodes.filter(n => n.type === 'MODULE' && n.domainId === activeDomain).length
    : graphNodes.filter(n => n.type === 'MODULE').length

  const activeLessonCount = activeDomain
    ? graphNodes.filter(n => n.type === 'LESSON' && n.domainId === activeDomain).length
    : 0

  return (
    <div className="flex flex-col h-full">
      {/* Controls bar */}
      <div className="flex-shrink-0 flex items-center gap-3 px-4 py-2.5 border-b border-border flex-wrap">
        {/* Domain filter chips */}
        <div className="flex items-center gap-2 flex-wrap flex-1">
          <button
            onClick={() => { setActiveDomain(null); setShowLessons(false) }}
            className="text-[11px] font-cinzel px-3 py-1 rounded-full border transition-all"
            style={{
              borderColor: !activeDomain ? 'rgba(201,162,39,0.7)' : 'var(--border)',
              background: !activeDomain ? 'rgba(201,162,39,0.12)' : 'transparent',
              color: !activeDomain ? '#c9a227' : 'var(--muted)',
            }}
          >
            All domains
          </button>
          {domains.map((d, i) => {
            const color = DOMAIN_COLORS[i % DOMAIN_COLORS.length]
            const active = activeDomain === d.id
            return (
              <button
                key={d.id}
                onClick={() => { setActiveDomain(prev => prev === d.id ? null : d.id); setShowLessons(false) }}
                className="text-[11px] font-cinzel px-3 py-1 rounded-full border transition-all"
                style={{
                  borderColor: active ? `${color}90` : 'var(--border)',
                  background: active ? `${color}18` : 'transparent',
                  color: active ? color : 'var(--muted)',
                }}
              >
                {d.glyph ? `${d.glyph} ` : ''}{d.label}
              </button>
            )
          })}
        </div>

        {/* Show lessons toggle — only available when a single domain is selected */}
        {activeDomain && (
          <button
            onClick={() => setShowLessons(p => !p)}
            className="flex-shrink-0 text-[11px] font-cinzel px-3 py-1 rounded-full border transition-all"
            style={{
              borderColor: showLessons ? 'rgba(139,92,246,0.6)' : 'var(--border)',
              background: showLessons ? 'rgba(139,92,246,0.12)' : 'transparent',
              color: showLessons ? '#a78bfa' : 'var(--muted)',
            }}
          >
            {showLessons
              ? `Hide lessons (${activeLessonCount})`
              : `Show lessons (${activeLessonCount})`}
          </button>
        )}

        <span className="flex-shrink-0 text-[11px] text-muted">
          {rfNodes.length} nodes
        </span>

        {/* Info hint */}
        {!activeDomain && (
          <span className="flex-shrink-0 text-[10px] text-muted opacity-60 hidden md:block">
            Click a domain to focus · Click a module to open it
          </span>
        )}
      </div>

      {/* Graph */}
      <div className="flex-1 min-h-0" style={{ background: '#050311' }}>
        <ReactFlow
          key={`${activeDomain}-${showLessons}`}
          nodes={rfNodes}
          edges={rfEdges}
          fitView
          fitViewOptions={{ padding: 0.08 }}
          nodesDraggable={false}
          nodesConnectable={false}
          elementsSelectable={false}
          colorMode="dark"
          proOptions={{ hideAttribution: true }}
          minZoom={0.1}
          maxZoom={2}
        >
          <Background variant={BackgroundVariant.Dots} gap={24} size={1} color="#1e1b4b" />
          <Controls showInteractive={false} style={{ background: '#0f0c1a', border: '1px solid #1e1b4b' }} />
          <MiniMap
            nodeColor={node => {
              const style = node.style as React.CSSProperties | undefined
              return (style?.borderColor as string | undefined)?.replace('1.5px solid ', '') ?? '#334155'
            }}
            maskColor="rgba(5, 3, 17, 0.75)"
            style={{ background: '#0f0c1a', border: '1px solid #1e1b4b', borderRadius: 6 }}
            zoomable
            pannable
          />
        </ReactFlow>
      </div>
    </div>
  )
}

// ── Legend ────────────────────────────────────────────────────────────────────

export function GraphLegend() {
  const items = [
    { label: 'Domain',       color: '#c9a227', dash: ''    },
    { label: 'Module',       color: '#0369a1', dash: ''    },
    { label: 'Lesson',       color: '#475569', dash: ''    },
    { label: 'Prerequisite', color: '#d97706', dash: '5 4' },
    { label: 'Integration',  color: '#0ea5e9', dash: '3 3' },
  ]
  return (
    <div className="flex flex-wrap gap-4 text-xs text-muted px-4 py-2">
      {items.map(({ label, color, dash }) => (
        <div key={label} className="flex items-center gap-1.5">
          <svg width="24" height="12">
            <line x1="0" y1="6" x2="24" y2="6" stroke={color} strokeWidth="2" strokeDasharray={dash || undefined} />
          </svg>
          <span style={{ color }}>{label}</span>
        </div>
      ))}
    </div>
  )
}

export default CurriculumGraph
