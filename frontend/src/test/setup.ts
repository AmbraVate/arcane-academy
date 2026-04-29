import '@testing-library/jest-dom/vitest'
import { afterEach } from 'vitest'
import { cleanup } from '@testing-library/react'

// Reset DOM between tests
afterEach(() => {
  cleanup()
})

// Stable localStorage mock per test (jsdom provides one, but make sure it's clear)
afterEach(() => {
  localStorage.clear()
})
