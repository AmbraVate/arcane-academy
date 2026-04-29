import { describe, it, expect } from 'vitest'
import {
  CSS_PRIMER_SECTIONS,
  PREREQ_QUIZ_QUESTIONS,
  CSS_PREREQ_TOPICS,
  prereqStorageKey,
} from './cssPrimer'

/**
 * Tests for the CSS primer content + helpers.
 *
 * The primer is the gate that prevents Tailwind/React learners from hitting a
 * cliff of unfamiliar HTML/CSS. Bugs here (a wrong correctIndex, a stray topic
 * registered with no content) silently corrupt the onboarding experience.
 */

describe('CSS Primer — section structure', () => {
  it('has three sections (HTML, CSS basics, layout)', () => {
    expect(CSS_PRIMER_SECTIONS).toHaveLength(3)
    expect(CSS_PRIMER_SECTIONS.map(s => s.id)).toEqual([
      'html',
      'css-basics',
      'css-layout',
    ])
  })

  it('every section has 4 questions (consistent depth)', () => {
    CSS_PRIMER_SECTIONS.forEach(section => {
      expect(section.questions, `section ${section.id}`).toHaveLength(4)
    })
  })

  it('every question has a valid correctIndex pointing into options', () => {
    CSS_PRIMER_SECTIONS.forEach(section => {
      section.questions.forEach(q => {
        expect(q.correctIndex, `${section.id}/${q.id}`)
          .toBeGreaterThanOrEqual(0)
        expect(q.correctIndex, `${section.id}/${q.id}`)
          .toBeLessThan(q.options.length)
      })
    })
  })

  it('every question has a non-empty explanation', () => {
    CSS_PRIMER_SECTIONS.forEach(section => {
      section.questions.forEach(q => {
        expect(q.explanation.trim().length, `${section.id}/${q.id}`)
          .toBeGreaterThan(20) // arbitrary but catches empty stubs
      })
    })
  })

  it('every section has explanation HTML (not empty)', () => {
    CSS_PRIMER_SECTIONS.forEach(section => {
      expect(section.explanationHtml.trim().length, section.id)
        .toBeGreaterThan(100)
    })
  })

  it('question IDs are unique within the primer', () => {
    const allIds = CSS_PRIMER_SECTIONS.flatMap(s => s.questions.map(q => q.id))
    expect(new Set(allIds).size).toBe(allIds.length)
  })
})

describe('CSS Primer — quick quiz', () => {
  it('has exactly 5 questions (≥3 = pass)', () => {
    expect(PREREQ_QUIZ_QUESTIONS).toHaveLength(5)
  })

  it('every quiz question has a valid correctIndex', () => {
    PREREQ_QUIZ_QUESTIONS.forEach(q => {
      expect(q.correctIndex).toBeGreaterThanOrEqual(0)
      expect(q.correctIndex).toBeLessThan(q.options.length)
    })
  })

  it('passing score (3/5) requires getting at least 3 questions right', () => {
    // Simulate a learner who gets the first 3 correct
    const answers = PREREQ_QUIZ_QUESTIONS.map((q, i) =>
      i < 3 ? q.correctIndex : (q.correctIndex + 1) % q.options.length
    )
    const score = PREREQ_QUIZ_QUESTIONS.filter(
      (q, i) => answers[i] === q.correctIndex
    ).length
    expect(score).toBe(3)
    expect(score >= 3).toBe(true) // pass threshold
  })

  it('failing score (2/5) requires the primer route', () => {
    const answers = PREREQ_QUIZ_QUESTIONS.map((q, i) =>
      i < 2 ? q.correctIndex : (q.correctIndex + 1) % q.options.length
    )
    const score = PREREQ_QUIZ_QUESTIONS.filter(
      (q, i) => answers[i] === q.correctIndex
    ).length
    expect(score).toBe(2)
    expect(score >= 3).toBe(false) // fail threshold → primer
  })
})

describe('CSS Primer — prereq topic registry', () => {
  it('includes both Tailwind and React (CSS-dependent topics)', () => {
    expect(CSS_PREREQ_TOPICS.has('tailwind')).toBe(true)
    expect(CSS_PREREQ_TOPICS.has('react')).toBe(true)
  })

  it('does NOT include Java (no CSS dependency)', () => {
    expect(CSS_PREREQ_TOPICS.has('java')).toBe(false)
  })
})

describe('CSS Primer — storage key helper', () => {
  it('produces a stable, scoped key per user+topic', () => {
    expect(prereqStorageKey('user-123', 'tailwind'))
      .toBe('arcane-prereq-user-123-tailwind')
  })

  it('produces different keys for different users on same topic', () => {
    const a = prereqStorageKey('alice', 'react')
    const b = prereqStorageKey('bob', 'react')
    expect(a).not.toBe(b)
  })

  it('produces different keys for same user on different topics', () => {
    const tw = prereqStorageKey('alice', 'tailwind')
    const rx = prereqStorageKey('alice', 'react')
    expect(tw).not.toBe(rx)
  })
})
