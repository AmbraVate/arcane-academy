/**
 * axe-core smoke test — UI primitives.
 *
 * Runs jest-axe inside jsdom (Vitest's default env). jsdom doesn't compute
 * layout or styles, so visual rules — colour-contrast in particular — are
 * skipped here. What this catches:
 *   • missing alt text / aria-label
 *   • role mismatches
 *   • bad heading order
 *   • form controls without labels
 *   • focusable elements without accessible names
 *
 * For full visual a11y (contrast, focus indicators, layout overlap) you'd
 * pair this with @axe-core/playwright once Playwright is wired up — see
 * CLAUDE.md §13. Until then, this catches ~60-70% of WCAG AA semantic
 * failures and runs in <2s under Vitest, so it's cheap enough to gate every
 * PR on.
 *
 * Test scope: the design-system primitives (Button, Card). They're the
 * leaves every page composes from — fix them once and a whole class of
 * issues never reaches a feature page. Add page-level smoke tests as the
 * scaffolding stabilises; for now, primitives are the highest-leverage
 * starting point.
 */

import { describe, expect, it } from 'vitest'
import { render } from '@testing-library/react'
import { axe, toHaveNoViolations } from 'jest-axe'
import { Button } from '../../components/ui/button'
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
  CardFooter,
} from '../../components/ui/card'

expect.extend(toHaveNoViolations)

describe('a11y: UI primitives', () => {
  it('Button — every variant has an accessible name', async () => {
    const { container } = render(
      <div>
        <Button>Default primary</Button>
        <Button variant="ghost">Ghost button</Button>
        <Button variant="success">Success</Button>
        <Button variant="gold">Accept Quest</Button>
        <Button variant="danger">Delete</Button>
        <Button disabled>Disabled</Button>
        <Button aria-label="Close dialog" size="icon">×</Button>
      </div>,
    )
    const results = await axe(container)
    expect(results).toHaveNoViolations()
  })

  it('Card — composed semantic structure passes axe', async () => {
    const { container } = render(
      <Card>
        <CardHeader>
          <CardTitle>Daily Review</CardTitle>
          <CardDescription>15 items due across 3 topics.</CardDescription>
        </CardHeader>
        <CardContent>
          <p>Review now to maintain your streak.</p>
        </CardContent>
        <CardFooter>
          <Button>Start review</Button>
        </CardFooter>
      </Card>,
    )
    const results = await axe(container)
    expect(results).toHaveNoViolations()
  })
})
