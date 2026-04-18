import * as React from 'react'
import { cn } from '@/lib/utils'

export interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {}

const Input = React.forwardRef<HTMLInputElement, InputProps>(
  ({ className, type, ...props }, ref) => (
    <input
      type={type}
      className={cn(
        'w-full bg-surface border border-border rounded-[6px]',
        'px-[14px] py-[10px]',
        'text-text text-[15px] font-crimson',
        'placeholder:text-muted',
        'outline-none transition-colors duration-200',
        'focus:border-purple',
        'disabled:opacity-50 disabled:cursor-not-allowed',
        className
      )}
      ref={ref}
      {...props}
    />
  )
)
Input.displayName = 'Input'

export { Input }
