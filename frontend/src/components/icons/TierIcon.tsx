/**
 * Icons for the four universal learning tiers.
 * Uses lucide-react so no extra dependency is needed.
 */
import { Sprout, Hammer, Zap, Crown } from 'lucide-react'
import type { LucideProps } from 'lucide-react'

const TIER_ICONS: Record<string, React.FC<LucideProps>> = {
  APPRENTICE: Sprout,
  JUNIOR:     Hammer,
  SENIOR:     Zap,
  LEAD:       Crown,
}

const TIER_COLORS: Record<string, string> = {
  APPRENTICE: 'var(--teal)',
  JUNIOR:     'var(--accent)',
  SENIOR:     'var(--gold)',
  LEAD:       'var(--purple-light)',
}

interface TierIconProps {
  tier: string
  size?: number
  color?: string
  className?: string
}

export function TierIcon({ tier, size = 18, color, className }: TierIconProps) {
  const Icon = TIER_ICONS[tier]
  if (!Icon) return null
  return (
    <Icon
      size={size}
      color={color ?? TIER_COLORS[tier] ?? 'currentColor'}
      className={className}
      strokeWidth={1.75}
    />
  )
}
