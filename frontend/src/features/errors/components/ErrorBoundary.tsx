import React from 'react'
import ErrorPage from '../pages/ErrorPage'

interface Props {
  children: React.ReactNode
}

interface State {
  hasError: boolean
}

/**
 * Top-level error boundary. Catches any unhandled React render error and
 * shows the arcane error page rather than a blank screen.
 */
export default class ErrorBoundary extends React.Component<Props, State> {
  state: State = { hasError: false }

  static getDerivedStateFromError(): State {
    return { hasError: true }
  }

  componentDidCatch(error: Error, info: React.ErrorInfo) {
    console.error('[Arcane Academy] An arcane anomaly occurred:', error, info.componentStack)
  }

  render() {
    if (this.state.hasError) {
      return (
        <ErrorPage
          type="crash"
          onRetry={() => this.setState({ hasError: false })}
        />
      )
    }
    return this.props.children
  }
}
