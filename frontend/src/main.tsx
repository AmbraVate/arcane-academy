import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import * as Sentry from '@sentry/react'
import App from './App'
import { AuthProvider } from '@/shared/hooks/useAuth'
import './index.css'

// Sentry — gated on VITE_SENTRY_DSN. When empty (the dev default), init() is
// skipped entirely so there is zero network noise and no runtime overhead.
// PII contract: we deliberately do NOT call Sentry.setUser(), and the default
// PII flags are off. The only thing leaking out is exception messages and
// stack traces. Audit any call to Sentry.captureException with user-derived
// data before adding it.
const SENTRY_DSN = import.meta.env.VITE_SENTRY_DSN as string | undefined
if (SENTRY_DSN) {
  Sentry.init({
    dsn: SENTRY_DSN,
    environment: (import.meta.env.VITE_SENTRY_ENVIRONMENT as string) || 'dev',
    sendDefaultPii: false,
    // Browser tracing is opt-in via VITE_SENTRY_TRACES_SAMPLE_RATE; keep at 0
    // unless you need it. The default integrations cover global errors and
    // unhandled promise rejections.
    tracesSampleRate: Number(import.meta.env.VITE_SENTRY_TRACES_SAMPLE_RATE) || 0,
  })
}

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <BrowserRouter>
      <AuthProvider>
        <App />
      </AuthProvider>
    </BrowserRouter>
  </React.StrictMode>
)
