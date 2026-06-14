import { useState, useCallback } from 'react'

// ── Storage keys ───────────────────────────────────────────────────────────────
const LORE_KEY     = 'arcane-lore-enabled'
const TUTORIAL_KEY = 'arcane-tutorial-completed'

function readBool(key: string, defaultValue: boolean): boolean {
  const raw = localStorage.getItem(key)
  if (raw === null) return defaultValue
  return raw === 'true'
}

// ── Hook ───────────────────────────────────────────────────────────────────────

export function usePreferences() {
  const [loreEnabled, setLoreEnabledState] = useState<boolean>(
    () => readBool(LORE_KEY, true),
  )
  const [tutorialCompleted, setTutorialCompletedState] = useState<boolean>(
    () => readBool(TUTORIAL_KEY, false),
  )

  const setLoreEnabled = useCallback((enabled: boolean) => {
    localStorage.setItem(LORE_KEY, String(enabled))
    setLoreEnabledState(enabled)
  }, [])

  const completeTutorial = useCallback(() => {
    localStorage.setItem(TUTORIAL_KEY, 'true')
    setTutorialCompletedState(true)
  }, [])

  const restartTutorial = useCallback(() => {
    localStorage.setItem(TUTORIAL_KEY, 'false')
    setTutorialCompletedState(false)
  }, [])

  return { loreEnabled, setLoreEnabled, tutorialCompleted, completeTutorial, restartTutorial }
}
