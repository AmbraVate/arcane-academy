import React, { createContext, useContext, useEffect, useState } from 'react'

export type Theme = 'default' | 'blizzard'
export type Palette = 'frostmourne' | 'fel' | 'bloodelf' | 'arcane' | 'bronze' | 'shadowlands' | 'naga'
export type SceneKind = 'castle' | 'aurora' | 'void'
export type FontKind = 'cinzel' | 'rune'

export interface BlizzardPrefs {
  palette: Palette
  scene: SceneKind
  font: FontKind
  snow: boolean
}

interface ThemeContextValue {
  theme: Theme
  blizzardPrefs: BlizzardPrefs
  toggleTheme: () => void
  setBlizzardPref: <K extends keyof BlizzardPrefs>(key: K, value: BlizzardPrefs[K]) => void
}

const DEFAULT_PREFS: BlizzardPrefs = { palette: 'frostmourne', scene: 'castle', font: 'cinzel', snow: true }

const ThemeContext = createContext<ThemeContextValue>({
  theme: 'default', blizzardPrefs: DEFAULT_PREFS, toggleTheme: () => {}, setBlizzardPref: () => {},
})

export function ThemeProvider({ children }: { children: React.ReactNode }) {
  const [theme, setTheme] = useState<Theme>(() => (localStorage.getItem('arcane-theme') as Theme) || 'default')
  const [blizzardPrefs, setPrefs] = useState<BlizzardPrefs>(() => {
    try { return { ...DEFAULT_PREFS, ...JSON.parse(localStorage.getItem('arcane-blizzard-prefs') || '{}') } }
    catch { return DEFAULT_PREFS }
  })

  useEffect(() => {
    const html = document.documentElement
    if (theme === 'blizzard') {
      html.setAttribute('data-theme', 'blizzard')
      html.setAttribute('data-palette', blizzardPrefs.palette)
      html.setAttribute('data-scene', blizzardPrefs.scene)
      html.setAttribute('data-font', blizzardPrefs.font)
    } else {
      html.removeAttribute('data-theme')
      html.removeAttribute('data-palette')
      html.removeAttribute('data-scene')
      html.removeAttribute('data-font')
    }
    localStorage.setItem('arcane-theme', theme)
  }, [theme, blizzardPrefs])

  const toggleTheme = () => setTheme(t => t === 'default' ? 'blizzard' : 'default')
  const setBlizzardPref = <K extends keyof BlizzardPrefs>(key: K, value: BlizzardPrefs[K]) => {
    setPrefs(prev => {
      const next = { ...prev, [key]: value }
      localStorage.setItem('arcane-blizzard-prefs', JSON.stringify(next))
      return next
    })
  }

  return <ThemeContext.Provider value={{ theme, blizzardPrefs, toggleTheme, setBlizzardPref }}>{children}</ThemeContext.Provider>
}

export function useTheme() { return useContext(ThemeContext) }
