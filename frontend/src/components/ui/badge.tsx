import * as React from 'react'
import { cva, type VariantProps } from 'class-variance-authority'
import { cn } from '@/lib/utils'

const badgeVariants = cva(
  'inline-flex items-center font-cinzel text-[10px] px-[10px] py-[2px] rounded-[10px] whitespace-nowrap',
  {
    variants: {
      variant: {
        purple: 'bg-purple-dim text-purple-light border border-purple-dim',
        green:  'bg-[#182d10] text-green border border-[#1f4016]',
        teal:   'bg-teal-dim text-teal border border-teal',
        red:    'bg-[#2d0808] text-red border border-[#7f1d1d]',
        gray:   'bg-border text-muted',
        gold:   'bg-gold-dim text-gold border border-[#7a5c10]',
        // Tier badges for QuestionCard
        recall:         'bg-[rgba(45,212,191,0.15)] text-teal border border-[rgba(45,212,191,0.3)]',
        application:    'bg-[rgba(139,92,246,0.15)] text-purple-light border border-[rgba(139,92,246,0.3)]',
        discrimination: 'bg-[rgba(201,162,39,0.15)] text-gold border border-[rgba(201,162,39,0.3)]',
        // Topic status
        active: 'bg-[rgba(45,212,191,0.15)] text-teal border border-[rgba(45,212,191,0.3)]',
        soon:   'bg-border text-muted border border-border',
      },
    },
    defaultVariants: { variant: 'purple' },
  }
)

export interface BadgeProps
  extends React.HTMLAttributes<HTMLDivElement>,
    VariantProps<typeof badgeVariants> {}

function Badge({ className, variant, ...props }: BadgeProps) {
  return <div className={cn(badgeVariants({ variant }), className)} {...props} />
}

export { Badge, badgeVariants }
