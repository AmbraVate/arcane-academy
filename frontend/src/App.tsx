// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
import { Routes, Route, Navigate } from 'react-router-dom'
import { useAuth } from './hooks/useAuth'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import TopicSelectPage from './pages/TopicSelectPage'
import HomePage from './pages/HomePage'
import QuestPage from './pages/QuestPage'
import BossPage from './pages/BossPage'
import Nav from './components/layout/Nav'

function PrivateRoute({ children }: { children: React.ReactNode }) {
  const { user } = useAuth()
  return user ? <>{children}</> : <Navigate to="/login" replace />
}

function TopicGuard({ children }: { children: React.ReactNode }) {
  const topic = localStorage.getItem('arcane_topic')
  return topic ? <>{children}</> : <Navigate to="/topics" replace />
}

export default function App() {
  const { user } = useAuth()
  return (
    <div style={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      {user && <Nav />}
      <Routes>
        <Route path="/login"    element={user ? <Navigate to="/topics" replace /> : <LoginPage />} />
        <Route path="/register" element={user ? <Navigate to="/topics" replace /> : <RegisterPage />} />
        <Route path="/topics"   element={<PrivateRoute><TopicSelectPage /></PrivateRoute>} />
        <Route path="/"         element={<PrivateRoute><TopicGuard><HomePage /></TopicGuard></PrivateRoute>} />
        <Route path="/quest/:id" element={<PrivateRoute><TopicGuard><QuestPage /></TopicGuard></PrivateRoute>} />
        <Route path="/boss/:id"  element={<PrivateRoute><TopicGuard><BossPage /></TopicGuard></PrivateRoute>} />
        <Route path="*"         element={<Navigate to="/" replace />} />
      </Routes>
    </div>
  )
}
