import { Routes, Route, Navigate } from 'react-router-dom'
import { useAuth } from './hooks/useAuth'
import LandingPage from './pages/LandingPage'
import TopicsPage from './pages/TopicsPage'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import OAuthCallbackPage from './pages/OAuthCallbackPage'
import DashboardPage from './pages/DashboardPage'
import ChunkMapPage from './pages/ChunkMapPage'
import EncodingPage from './pages/EncodingPage'
import ReviewPage from './pages/ReviewPage'
import DiagnosticPage from './pages/DiagnosticPage'
import OnboardingPage from './pages/OnboardingPage'
import RabbitHolePage from './pages/RabbitHolePage'
import CuriosityQueuePage from './pages/CuriosityQueuePage'
import ProfilePage from './pages/ProfilePage'
import Nav from './components/layout/Nav'

function PrivateRoute({ children }: { children: React.ReactNode }) {
  const { user } = useAuth()
  return user ? <>{children}</> : <Navigate to="/login" replace />
}

export default function App() {
  const { user } = useAuth()
  return (
    <div style={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      {user && <Nav />}
      <div style={{ flex: 1, overflowY: 'auto', display: 'flex', flexDirection: 'column' }}>
      <Routes>
        <Route path="/login"    element={user ? <Navigate to="/" replace /> : <LoginPage />} />
        <Route path="/register" element={user ? <Navigate to="/" replace /> : <RegisterPage />} />
        <Route path="/oauth2/callback" element={<OAuthCallbackPage />} />
        <Route path="/onboarding" element={<PrivateRoute><OnboardingPage /></PrivateRoute>} />
        <Route path="/diagnostic" element={<PrivateRoute><DiagnosticPage /></PrivateRoute>} />
        <Route path="/profile"  element={<PrivateRoute><ProfilePage /></PrivateRoute>} />
        <Route path="/"         element={user ? <DashboardPage /> : <LandingPage />} />
        <Route path="/chunk/:chunkId" element={<PrivateRoute><ChunkMapPage /></PrivateRoute>} />
        <Route path="/learn/:subChunkId" element={<PrivateRoute><EncodingPage /></PrivateRoute>} />
        <Route path="/review"   element={<PrivateRoute><ReviewPage /></PrivateRoute>} />
        <Route path="/rabbit-hole/:id" element={<PrivateRoute><RabbitHolePage /></PrivateRoute>} />
        <Route path="/curiosity-queue" element={<PrivateRoute><CuriosityQueuePage /></PrivateRoute>} />
        <Route path="/topics" element={<PrivateRoute><TopicsPage /></PrivateRoute>} />
        <Route path="*"         element={<Navigate to="/" replace />} />
      </Routes>
      </div>
    </div>
  )
}
