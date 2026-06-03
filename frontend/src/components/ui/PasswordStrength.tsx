type Tier = { label: string; color: string; width: string }

function getTier(len: number): Tier | null {
  if (len === 0)  return null
  if (len < 20)   return { label: 'Too short',     color: '#f87171', width: `${Math.max(8, (len / 20) * 33)}%` }
  if (len < 30)   return { label: 'Good',          color: '#c9a227', width: '55%' }
  if (len < 40)   return { label: 'Strong',        color: '#2dd4bf', width: '78%' }
  return           { label: 'Very strong',         color: '#4ade80', width: '100%' }
}

export function PasswordStrength({ password }: { password: string }) {
  const tier = getTier(password.length)
  if (!tier) return null

  return (
    <div className="mt-1.5 flex flex-col gap-1">
      <div className="h-1 w-full rounded-full bg-border overflow-hidden">
        <div
          style={{ width: tier.width, background: tier.color, height: '100%', borderRadius: 9999, transition: 'width 0.3s, background 0.3s' }}
        />
      </div>
      <p className="text-[11px]" style={{ color: tier.color }}>{tier.label}</p>
    </div>
  )
}
