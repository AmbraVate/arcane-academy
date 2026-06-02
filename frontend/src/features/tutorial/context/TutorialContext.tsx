import React, { createContext, useContext, useState, useCallback, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { TUTORIAL_STEPS, type TutorialStep } from '../data/tutorialSteps'
import { useAuth } from '@/shared/hooks/useAuth'
import { authApi } from '@/shared/api/services'

const STORAGE_KEY = 'arcane-tutorial-completed'

// ── Context value ──────────────────────────────────────────────────────────────

interface TutorialContextValue {
  isActive: boolean
  currentStep: TutorialStep | null
  stepIndex: number
  totalSteps: number
  next: () => void
  skip: () => void
  complete: () => void
  restart: () => void
  /** Call once, from HomePage, to auto-start on first login */
  maybeStart: () => void
}

const TutorialContext = createContext<TutorialContextValue>({
  isActive: false, currentStep: null, stepIndex: 0, totalSteps: 0,
  next: () => {}, skip: () => {}, complete: () => {}, restart: () => {}, maybeStart: () => {},
})

// ── Provider ───────────────────────────────────────────────────────────────────

export function TutorialProvider({ children }: { children: React.ReactNode }) {
  const navigate = useNavigate()
  const { markOnboardingDone } = useAuth()

  const [isActive, setIsActive] = useState(false)
  const [stepIndex, setStepIndex] = useState(0)

  const activateStep = useCallback((index: number) => {
    const step = TUTORIAL_STEPS[index]
    if (!step) return
    if (step.navigateTo) {
      navigate(step.navigateTo)
    }
  }, [navigate])

  const next = useCallback(() => {
    const nextIndex = stepIndex + 1
    if (nextIndex >= TUTORIAL_STEPS.length) {
      // Last pre-lesson step navigated by activateStep; lesson page calls complete()
      setStepIndex(nextIndex)
      const step = TUTORIAL_STEPS[nextIndex - 1]
      if (step?.navigateTo) {
        navigate(step.navigateTo)
      }
      setIsActive(false)
      return
    }
    activateStep(nextIndex)
    setStepIndex(nextIndex)
  }, [stepIndex, activateStep, navigate])

  // Separate effect: navigate when step changes
  useEffect(() => {
    if (!isActive) return
    const step = TUTORIAL_STEPS[stepIndex]
    if (step?.navigateTo) {
      navigate(step.navigateTo)
    }
  }, [isActive, stepIndex, navigate])

  const skip = useCallback(() => {
    setIsActive(false)
    localStorage.setItem(STORAGE_KEY, 'skipped')
    markOnboardingDone()
    authApi.completeOnboarding().catch(() => {})
  }, [markOnboardingDone])

  const complete = useCallback(() => {
    setIsActive(false)
    localStorage.setItem(STORAGE_KEY, 'true')
    markOnboardingDone()
    authApi.completeOnboarding().catch(() => {})
  }, [markOnboardingDone])

  const restart = useCallback(() => {
    localStorage.removeItem(STORAGE_KEY)
    setStepIndex(0)
    setIsActive(true)
    navigate('/')
  }, [navigate])

  const maybeStart = useCallback(() => {
    const stored = localStorage.getItem(STORAGE_KEY)
    if (!stored) {
      setStepIndex(0)
      setIsActive(true)
    }
  }, [])

  const currentStep = isActive ? (TUTORIAL_STEPS[stepIndex] ?? null) : null

  return (
    <TutorialContext.Provider value={{
      isActive, currentStep, stepIndex,
      totalSteps: TUTORIAL_STEPS.length,
      next, skip, complete, restart, maybeStart,
    }}>
      {children}
    </TutorialContext.Provider>
  )
}

export function useTutorial() { return useContext(TutorialContext) }
