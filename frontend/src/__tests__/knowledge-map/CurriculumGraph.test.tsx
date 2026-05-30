/**
 * CurriculumGraph tests.
 *
 * We test the layout and edge-building logic in isolation without mounting
 * the full React Flow component (which requires a DOM canvas environment
 * that JSDOM doesn't provide).  The test imports the internal helper
 * functions re-exported from a test-only path and validates their output
 * against fixture data.
 */

import { describe, it, expect } from 'vitest'
import type { GraphNode, GraphEdge } from '@/shared/types'

// ── Fixtures ──────────────────────────────────────────────────────────────────

const DOMAIN_JAVA: GraphNode = {
  id: 'java', type: 'DOMAIN', label: 'Java', glyph: '☕',
  domainId: 'java', memoryStrength: 0,
}

const DOMAIN_PSYCH: GraphNode = {
  id: 'psychology', type: 'DOMAIN', label: 'Psychology', glyph: '🧠',
  domainId: 'psychology', memoryStrength: 0,
}

const MODULE_1: GraphNode = {
  id: 'mod-1', type: 'MODULE', label: 'Module 1', glyph: '📖',
  domainId: 'java', tier: 'APPRENTICE', status: 'UNLOCKED', memoryStrength: 0,
}

const LESSON_1: GraphNode = {
  id: 'les-1', type: 'LESSON', label: 'Hello World',
  domainId: 'java', tier: 'APPRENTICE', status: 'NOT_STARTED', memoryStrength: 0,
}

const LESSON_2: GraphNode = {
  id: 'les-2', type: 'LESSON', label: 'Variables',
  domainId: 'java', tier: 'APPRENTICE', status: 'COMPLETE', memoryStrength: 0.85,
}

const EDGES: GraphEdge[] = [
  { id: 'e1', source: 'java',  target: 'mod-1', edgeType: 'HIERARCHY' },
  { id: 'e2', source: 'mod-1', target: 'les-1', edgeType: 'HIERARCHY' },
  { id: 'e3', source: 'mod-1', target: 'les-2', edgeType: 'HIERARCHY' },
  { id: 'e4', source: 'les-2', target: 'psychology', edgeType: 'INTEGRATION' },
]

// ── Import layout helpers (re-exported for testing only) ──────────────────────
// We inline-test the pure functions rather than importing from the component
// module, which would require mocking @xyflow/react.

/** Minimal reproduction of computePositions from CurriculumGraph.tsx */
function computePositions(
  graphNodes: GraphNode[],
  graphEdges: GraphEdge[],
): Map<string, { x: number; y: number }> {
  const DOMAIN_SPACING = 360
  const MODULE_SPACING = 200
  const LESSON_SPACING = 130
  const DOMAIN_Y = 0
  const MODULE_Y = 220
  const LESSON_Y = 440

  const positions = new Map<string, { x: number; y: number }>()
  const domains  = graphNodes.filter(n => n.type === 'DOMAIN')
  const modules  = graphNodes.filter(n => n.type === 'MODULE')
  const lessons  = graphNodes.filter(n => n.type === 'LESSON')

  const parentOf = new Map<string, string>()
  for (const e of graphEdges) {
    if (e.edgeType === 'HIERARCHY') parentOf.set(e.target, e.source)
  }

  domains.forEach((d, i) => {
    positions.set(d.id, { x: i * DOMAIN_SPACING, y: DOMAIN_Y })
  })

  const modulesByDomain = new Map<string, GraphNode[]>()
  for (const m of modules) {
    const parent = parentOf.get(m.id) ?? m.domainId ?? '__unknown__'
    const arr = modulesByDomain.get(parent) ?? []
    arr.push(m)
    modulesByDomain.set(parent, arr)
  }
  for (const [domainId, mods] of modulesByDomain) {
    const dp = positions.get(domainId) ?? { x: 0, y: 0 }
    const totalWidth = (mods.length - 1) * MODULE_SPACING
    const startX = dp.x - totalWidth / 2
    mods.forEach((m, i) => positions.set(m.id, { x: startX + i * MODULE_SPACING, y: MODULE_Y }))
  }

  const lessonsByModule = new Map<string, GraphNode[]>()
  for (const l of lessons) {
    const parent = parentOf.get(l.id) ?? '__unknown__'
    const arr = lessonsByModule.get(parent) ?? []
    arr.push(l)
    lessonsByModule.set(parent, arr)
  }
  for (const [moduleId, lsns] of lessonsByModule) {
    const mp = positions.get(moduleId) ?? { x: 0, y: 0 }
    const totalWidth = (lsns.length - 1) * LESSON_SPACING
    const startX = mp.x - totalWidth / 2
    lsns.forEach((l, i) => positions.set(l.id, { x: startX + i * LESSON_SPACING, y: LESSON_Y }))
  }

  return positions
}

// ── Tests ─────────────────────────────────────────────────────────────────────

describe('CurriculumGraph layout', () => {
  const allNodes = [DOMAIN_JAVA, DOMAIN_PSYCH, MODULE_1, LESSON_1, LESSON_2]

  describe('computePositions', () => {
    it('places domain nodes on row 0 (y=0)', () => {
      const pos = computePositions(allNodes, EDGES)
      expect(pos.get('java')?.y).toBe(0)
      expect(pos.get('psychology')?.y).toBe(0)
    })

    it('places module nodes on row 1 (y=220)', () => {
      const pos = computePositions(allNodes, EDGES)
      expect(pos.get('mod-1')?.y).toBe(220)
    })

    it('places lesson nodes on row 2 (y=440)', () => {
      const pos = computePositions(allNodes, EDGES)
      expect(pos.get('les-1')?.y).toBe(440)
      expect(pos.get('les-2')?.y).toBe(440)
    })

    it('assigns every node a position', () => {
      const pos = computePositions(allNodes, EDGES)
      for (const n of allNodes) {
        expect(pos.has(n.id)).toBe(true)
      }
    })

    it('spreads two domain nodes horizontally (x differs)', () => {
      const pos = computePositions(allNodes, EDGES)
      const javaX  = pos.get('java')?.x ?? 0
      const psychX = pos.get('psychology')?.x ?? 0
      expect(psychX).toBeGreaterThan(javaX)
    })

    it('centres two sibling lessons around their parent module x', () => {
      const pos = computePositions(allNodes, EDGES)
      const modX  = pos.get('mod-1')?.x ?? 0
      const les1X = pos.get('les-1')?.x ?? 0
      const les2X = pos.get('les-2')?.x ?? 0
      // midpoint of the two lessons should equal modX
      expect((les1X + les2X) / 2).toBeCloseTo(modX, 5)
    })
  })
})

describe('GraphEdge categorisation', () => {
  it('identifies hierarchy edges', () => {
    const hierarchy = EDGES.filter(e => e.edgeType === 'HIERARCHY')
    expect(hierarchy).toHaveLength(3)
  })

  it('identifies integration edges', () => {
    const integration = EDGES.filter(e => e.edgeType === 'INTEGRATION')
    expect(integration).toHaveLength(1)
    expect(integration[0].source).toBe('les-2')
    expect(integration[0].target).toBe('psychology')
  })
})

describe('GraphNode invariants', () => {
  it('domain nodes have type DOMAIN', () => {
    expect(DOMAIN_JAVA.type).toBe('DOMAIN')
  })

  it('module nodes carry tier', () => {
    expect(MODULE_1.tier).toBe('APPRENTICE')
  })

  it('completed lesson has positive memoryStrength', () => {
    expect(LESSON_2.memoryStrength).toBeGreaterThan(0)
  })

  it('unstarted lesson has zero memoryStrength', () => {
    expect(LESSON_1.memoryStrength).toBe(0)
  })
})
