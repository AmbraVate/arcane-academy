import { useEffect, useState } from 'react'

/**
 * Returns true when the viewport width is below the given breakpoint.
 * Defaults to 768px (tablet breakpoint).
 * Re-evaluates on window resize.
 */
export function useIsMobile(breakpoint = 768): boolean {
  const [mobile, setMobile] = useState(() => window.innerWidth < breakpoint)

  useEffect(() => {
    const handler = () => setMobile(window.innerWidth < breakpoint)
    window.addEventListener('resize', handler)
    return () => window.removeEventListener('resize', handler)
  }, [breakpoint])

  return mobile
}
