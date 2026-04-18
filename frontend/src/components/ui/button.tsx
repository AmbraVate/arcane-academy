import * as React from 'react'
import { Slot } from '@radix-ui/react-slot'
import { cva, type VariantProps } from 'class-variance-authority'
import { cn } from '@/lib/utils'

const buttonVariants = cva(
  'inline-flex items-center justify-center rounded transition-all duration-200 active:scale-[0.97] disabled:opacity-50 disabled:cursor-not-allowed select-none',
  {
    variants: {
      variant: {
        primary: [
          'bg-purple-dim border border-purple text-purple-light',
          'font-cinzel text-[13px] tracking-[1px]',
          'hover:bg-purple',
        ],
        ghost: [
          'bg-transparent border border-border text-muted',
          'text-[13px]',
          'hover:border-purple hover:text-text',
        ],
        success: [
          'bg-[#061a0c] border border-green text-green',
          'font-cinzel text-[13px]',
          'hover:bg-[#0a2d14]',
        ],
        gold: [
          'bg-gold-dim border border-gold text-gold',
          'font-cinzel text-[13px] tracking-[1px]',
          'hover:bg-[#6a4c0e]',
        ],
        danger: [
          'bg-[#2d0808] border border-red text-red',
          'text-[13px]',
          'hover:bg-[#4a1010]',
        ],
        unstyled: '',
      },
      size: {
        default: 'px-[18px] py-[7px] text-[13px]',
        sm:      'px-[12px] py-[4px] text-[12px]',
        lg:      'px-[40px] py-[14px] text-[16px]',
        icon:    'p-[6px]',
      },
    },
    defaultVariants: {
      variant: 'primary',
      size: 'default',
    },
  }
)

export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {
  asChild?: boolean
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant, size, asChild = false, ...props }, ref) => {
    const Comp = asChild ? Slot : 'button'
    return (
      <Comp
        className={cn(buttonVariants({ variant, size, className }))}
        ref={ref}
        {...props}
      />
    )
  }
)
Button.displayName = 'Button'

export { Button, buttonVariants }
