/**
 * Icons for learning tier levels — Foundation through Capstone.
 * Uses lucide-react so no extra dependency is needed.
 */
import { Sprout, Zap, Gem, Trophy, Crown } from 'lucide-react'
import type { LucideProps } from 'lucide-react'

const TIER_ICONS: Record<string, React.FC<LucideProps>> = {
  FOUNDATION:   Sprout,
  ADVANCED:     Zap,
  PRACTITIONER: Gem,
  EXPERT:       Trophy,
  CAPSTONE:     Crown,
}

const TIER_COLORS: Record<string, string> = {
  FOUNDATION:   'var(--teal)',
  ADVANCED:     'var(--gold)',
  PRACTITIONER: 'var(--purple-light)',
  EXPERT:       'var(--gold)',
  CAPSTONE:     'var(--gold)',
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
