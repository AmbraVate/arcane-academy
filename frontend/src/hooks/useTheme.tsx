import React, { createContext, useContext, useEffect, useState } from 'react'

export type Theme = 'default' | 'blizzard'

interface ThemeContextValue {
  theme: Theme
  toggleTheme: () => void
}

const ThemeContext = createContext<ThemeContextValue>({
  theme: 'default',
  toggleTheme: () => {},
})

export function ThemeProvider({ children }: { children: React.ReactNode }) {
  const [theme, setTheme] = useState<Theme>(() => {
    return (localStorage.getItem('arcane-theme') as Theme) || 'default'
  })

  useEffect(() => {
    const html = document.documentElement
    if (theme === 'blizzard') {
      html.setAttribute('data-theme', 'blizzard')
    } else {
      html.removeAttribute('data-theme')
    }
    localStorage.setItem('arcane-theme', theme)
  }, [theme])

  const toggleTheme = () => setTheme(t => (t === 'default' ? 'blizzard' : 'default'))

  return (
    <ThemeContext.Provider value={{ theme, toggleTheme }}>
      {children}
    </ThemeContext.Provider>
  )
}

export function useTheme() {
  return useContext(ThemeContext)
}
