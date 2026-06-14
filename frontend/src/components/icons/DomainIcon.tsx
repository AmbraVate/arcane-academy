/**
 * Branded topic icons.
 * Tech languages use Simple Icons (via react-icons/si) — same source as techicons.dev.
 * Non-tech disciplines fall back to lucide-react.
 */
import {
  SiTailwindcss, SiHtml5, SiCss,
  SiJavascript, SiPython, SiTypescript, SiReact,
} from 'react-icons/si'
import { Database, Brain, GitBranch, Microscope, Coffee } from 'lucide-react'
import type { FC, SVGProps } from 'react'

// Brand colours matching each technology's official palette
const TOPIC_COLORS: Record<string, string> = {
  java:       '#e76f00',
  tailwind:   '#38bdf8',
  html:       '#e34c26',
  css:        '#1572b6',
  javascript: '#e8c900',
  python:     '#3572a5',
  sql:        'var(--teal)',
  typescript: '#3178c6',
  react:      '#61dafb',
  psychology: 'var(--purple-light)',
  genealogy:  'var(--gold)',
  sciences:   'var(--teal)',
}

type IconComponent = FC<SVGProps<SVGSVGElement> & { size?: number; color?: string }>

const TOPIC_ICONS: Record<string, IconComponent> = {
  java:       Coffee       as IconComponent,
  tailwind:   SiTailwindcss as IconComponent,
  html:       SiHtml5      as IconComponent,
  css:        SiCss        as IconComponent,
  javascript: SiJavascript as IconComponent,
  python:     SiPython     as IconComponent,
  sql:        Database     as IconComponent,
  typescript: SiTypescript as IconComponent,
  react:      SiReact      as IconComponent,
  psychology: Brain        as IconComponent,
  genealogy:  GitBranch    as IconComponent,
  sciences:   Microscope   as IconComponent,
}

interface DomainIconProps {
  domainId: string
  size?: number
  /** Override the default brand colour */
  color?: string
  className?: string
}

export function DomainIcon({ domainId, size = 28, color, className }: DomainIconProps) {
  const Icon = TOPIC_ICONS[domainId]
  if (!Icon) return null
  return (
    <Icon
      size={size}
      color={color ?? TOPIC_COLORS[domainId] ?? 'currentColor'}
      className={className}
    />
  )
}
