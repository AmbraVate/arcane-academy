import { Routes, Route, Navigate, Outlet } from 'react-router-dom'
import { useAuth } from '@/shared/hooks/useAuth'
import TopicsPage from '@/features/topics/pages/TopicsPage'
import LoginPage from '@/features/auth/pages/LoginPage'
import RegisterPage from '@/features/auth/pages/RegisterPage'
import OAuthCallbackPage from '@/features/auth/pages/OAuthCallbackPage'
import ChunkMapPage from '@/features/topics/pages/ChunkMapPage'
import EncodingPage from '@/features/learning/pages/EncodingPage'
import ReviewPage from '@/features/review/pages/ReviewPage'
import DiagnosticPage from '@/features/diagnostic/pages/DiagnosticPage'
import OnboardingPage from '@/features/auth/pages/OnboardingPage'
import RabbitHolePage from '@/features/exploration/pages/RabbitHolePage'
import CuriosityQueuePage from '@/features/exploration/pages/CuriosityQueuePage'
import ProfilePage from '@/features/profile/pages/ProfilePage'
import TopicPage from '@/features/topics/pages/TopicPage'
import TopicOnboardingPage from '@/features/onboarding/pages/TopicOnboardingPage'
import TopicDiagnosticPage from '@/features/diagnostic/pages/TopicDiagnosticPage'
import PrerequisiteCheckPage from '@/features/onboarding/pages/PrerequisiteCheckPage'
import CssPrimerPage from '@/features/onboarding/pages/CssPrimerPage'
import LeaderboardPage from '@/features/leaderboard/pages/LeaderboardPage'
import PublicProfilePage from '@/features/profile/pages/PublicProfilePage'
import Nav from '@/shared/components/layout/Nav'
import AdminLayout from '@/features/admin/pages/AdminLayout'
import AdminDashboardPage from '@/features/admin/pages/AdminDashboardPage'
import AdminChunksPage from '@/features/admin/pages/AdminChunksPage'
import AdminSubChunksPage from '@/features/admin/pages/AdminSubChunksPage'
import AdminSubChunkEditorPage from '@/features/admin/pages/AdminSubChunkEditorPage'
import AdminQuestionsPage from '@/features/admin/pages/AdminQuestionsPage'
import AdminUsersPage from '@/features/admin/pages/AdminUsersPage'
import AdminImportExportPage from '@/features/admin/pages/AdminImportExportPage'
import React from 'react'

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
        <Route path="/login"    element={user ? <Navigate to="/" replace /> : <LoginPage />} />
        <Route path="/register" element={user ? <Navigate to="/" replace /> : <RegisterPage />} />
        <Route path="/oauth2/callback" element={<OAuthCallbackPage />} />
        <Route path="/onboarding" element={<PrivateRoute><OnboardingPage /></PrivateRoute>} />
        <Route path="/diagnostic" element={<PrivateRoute><DiagnosticPage /></PrivateRoute>} />
        <Route path="/profile"  element={<PrivateRoute><ProfilePage /></PrivateRoute>} />
        <Route path="/"         element={<LoginPage />} />
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
