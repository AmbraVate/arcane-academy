import { Routes, Route, Navigate, Outlet } from 'react-router-dom'
import { useAuth } from './hooks/useAuth'
import TopicsPage from './pages/TopicsPage'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import OAuthCallbackPage from './pages/OAuthCallbackPage'
import ChunkMapPage from './pages/ChunkMapPage'
import EncodingPage from './pages/EncodingPage'
import ReviewPage from './pages/ReviewPage'
import DiagnosticPage from './pages/DiagnosticPage'
import OnboardingPage from './pages/OnboardingPage'
import RabbitHolePage from './pages/RabbitHolePage'
import CuriosityQueuePage from './pages/CuriosityQueuePage'
import ProfilePage from './pages/ProfilePage'
import TopicPage from './pages/TopicPage'
import TopicOnboardingPage from './pages/TopicOnboardingPage'
import TopicDiagnosticPage from './pages/TopicDiagnosticPage'
import PrerequisiteCheckPage from './pages/PrerequisiteCheckPage'
import CssPrimerPage from './pages/CssPrimerPage'
import LeaderboardPage from './pages/LeaderboardPage'
import PublicProfilePage from './pages/PublicProfilePage'
import Nav from './components/layout/Nav'
import AdminLayout from './pages/admin/AdminLayout'
import AdminDashboardPage from './pages/admin/AdminDashboardPage'
import AdminChunksPage from './pages/admin/AdminChunksPage'
import AdminSubChunksPage from './pages/admin/AdminSubChunksPage'
import AdminSubChunkEditorPage from './pages/admin/AdminSubChunkEditorPage'
import AdminQuestionsPage from './pages/admin/AdminQuestionsPage'
import AdminUsersPage from './pages/admin/AdminUsersPage'
import AdminImportExportPage from './pages/admin/AdminImportExportPage'
import LandingPage from './pages/LandingPage'
import React from "react";

function PrivateRoute({ children }: { children: React.ReactNode }) {
  const { user } = useAuth()
  return user ? <>{children}</> : <Navigate to="/login" replace />
}

function AdminRoute() {
  const { user } = useAuth()
  if (!user) return <Navigate to="/login" replace />
  if (user.role !== 'ADMIN') return <Navigate to="/" replace />
  return <Outlet />
}

export default function App() {
  const { user } = useAuth()
  return (
    <div style={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      {user && !location.pathname.startsWith('/admin') && <Nav />}
      <div style={{ flex: 1, overflowY: 'auto', display: 'flex', flexDirection: 'column' }}>
      <Routes>
        <Route path="/login"    element={user ? <Navigate to="/topics" replace /> : <LoginPage />} />
        <Route path="/register" element={user ? <Navigate to="/topics" replace /> : <RegisterPage />} />
        <Route path="/oauth2/callback" element={<OAuthCallbackPage />} />
        <Route path="/onboarding" element={<PrivateRoute><OnboardingPage /></PrivateRoute>} />
        <Route path="/diagnostic" element={<PrivateRoute><DiagnosticPage /></PrivateRoute>} />
        <Route path="/profile"  element={<PrivateRoute><ProfilePage /></PrivateRoute>} />
        <Route path="/"         element={user ? <Navigate to="/topics" replace /> : <LandingPage />} />
        {/* All topics (including java) go through the unified /topic/:topicId → TopicPage flow.
            DashboardPage is reserved for an aggregate, multi-topic view — not a Java special case. */}
        <Route path="/chunk/:chunkId" element={<PrivateRoute><ChunkMapPage /></PrivateRoute>} />
        <Route path="/learn/:subChunkId" element={<PrivateRoute><EncodingPage /></PrivateRoute>} />
        <Route path="/review"   element={<PrivateRoute><ReviewPage /></PrivateRoute>} />
        <Route path="/rabbit-hole/:id" element={<PrivateRoute><RabbitHolePage /></PrivateRoute>} />
        <Route path="/curiosity-queue" element={<PrivateRoute><CuriosityQueuePage /></PrivateRoute>} />
        <Route path="/topics" element={<PrivateRoute><TopicsPage /></PrivateRoute>} />
        <Route path="/topic/:topicId" element={<PrivateRoute><TopicPage /></PrivateRoute>} />
        <Route path="/topic/:topicId/onboarding"   element={<PrivateRoute><TopicOnboardingPage /></PrivateRoute>} />
        <Route path="/topic/:topicId/diagnostic"   element={<PrivateRoute><TopicDiagnosticPage /></PrivateRoute>} />
        <Route path="/topic/:topicId/prereq-check" element={<PrivateRoute><PrerequisiteCheckPage /></PrivateRoute>} />
        <Route path="/topic/:topicId/css-primer"   element={<PrivateRoute><CssPrimerPage /></PrivateRoute>} />
        <Route path="/leaderboard"   element={<PrivateRoute><LeaderboardPage /></PrivateRoute>} />
        <Route path="/u/:username"   element={<PrivateRoute><PublicProfilePage /></PrivateRoute>} />

        {/* Admin — completely separate layout, no learner Nav */}
        <Route element={<AdminRoute />}>
          <Route path="/admin" element={<AdminLayout />}>
            <Route index element={<AdminDashboardPage />} />
            <Route path="chunks" element={<AdminChunksPage />} />
            <Route path="chunks/:chunkId/subchunks" element={<AdminSubChunksPage />} />
            <Route path="subchunks/:subChunkId/edit" element={<AdminSubChunkEditorPage />} />
            <Route path="subchunks/:subChunkId/questions" element={<AdminQuestionsPage />} />
            <Route path="users" element={<AdminUsersPage />} />
            <Route path="import-export" element={<AdminImportExportPage />} />
          </Route>
        </Route>

        <Route path="*"         element={<Navigate to="/" replace />} />
      </Routes>
      </div>
    </div>
  )
}
