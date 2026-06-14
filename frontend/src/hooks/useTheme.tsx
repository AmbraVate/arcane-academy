import React from 'react'

// Blizzard theme removed. ThemeProvider is kept as a transparent wrapper
// so existing import sites in main.tsx don't need updating.
export function ThemeProvider({ children }: { children: React.ReactNode }) {
  return <>{children}</>
}
