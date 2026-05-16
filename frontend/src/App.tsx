import React, { lazy, Suspense } from 'react'
import { Routes, Route, Navigate, Outlet } from 'react-router-dom'
import { useAuth } from './shared/hooks/useAuth'
import { useTheme } from './hooks/useTheme'
import Nav from './components/layout/Nav'
import BlizzardFrame from './components/layout/BlizzardFrame'

const TopicsPage           = lazy(() => import('./features/topics/pages/TopicsPage'))
const LoginPage            = lazy(() => import('./features/auth/pages/LoginPage'))
const RegisterPage         = lazy(() => import('./features/auth/pages/RegisterPage'))
const OAuthCallbackPage    = lazy(() => import('./features/auth/pages/OAuthCallbackPage'))
const ChunkMapPage         = lazy(() => import('./features/topics/pages/ChunkMapPage'))
const EncodingPage         = lazy(() => import('./features/learning/pages/EncodingPage'))
const ReviewPage           = lazy(() => import('./features/review/pages/ReviewPage'))
const DiagnosticPage       = lazy(() => import('./features/diagnostic/pages/DiagnosticPage'))
const OnboardingPage       = lazy(() => import('./features/auth/pages/OnboardingPage'))
const RabbitHolePage       = lazy(() => import('./features/exploration/pages/RabbitHolePage'))
const CuriosityQueuePage   = lazy(() => import('./features/exploration/pages/CuriosityQueuePage'))
const ProfilePage          = lazy(() => import('./features/profile/pages/ProfilePage'))
const TopicPage            = lazy(() => import('./features/topics/pages/TopicPage'))
const TopicOnboardingPage  = lazy(() => import('./features/onboarding/pages/TopicOnboardingPage'))
const TopicDiagnosticPage  = lazy(() => import('./features/diagnostic/pages/TopicDiagnosticPage'))
const PrerequisiteCheckPage = lazy(() => import('./features/onboarding/pages/PrerequisiteCheckPage'))
const CssPrimerPage        = lazy(() => import('./features/onboarding/pages/CssPrimerPage'))
const LeaderboardPage      = lazy(() => import('./features/leaderboard/pages/LeaderboardPage'))
const PublicProfilePage    = lazy(() => import('./features/profile/pages/PublicProfilePage'))
const LandingPage          = lazy(() => import('./features/auth/pages/LandingPage'))
const AdminLayout          = lazy(() => import('./features/admin/pages/AdminLayout'))
const AdminDashboardPage   = lazy(() => import('./features/admin/pages/AdminDashboardPage'))
const AdminChunksPage      = lazy(() => import('./features/admin/pages/AdminChunksPage'))
const AdminSubChunksPage   = lazy(() => import('./features/admin/pages/AdminSubChunksPage'))
const AdminSubChunkEditorPage = lazy(() => import('./features/admin/pages/AdminSubChunkEditorPage'))
const AdminQuestionsPage   = lazy(() => import('./features/admin/pages/AdminQuestionsPage'))
const AdminUsersPage       = lazy(() => import('./features/admin/pages/AdminUsersPage'))
const AdminImportExportPage = lazy(() => import('./features/admin/pages/AdminImportExportPage'))
const AdminTopicsPage      = lazy(() => import('./features/admin/pages/AdminTopicsPage'))

function PageFallback() {
  return (
    <div className="flex items-center justify-center h-[60vh] text-muted">
      <div className="w-8 h-8 animate-spin rounded-full border-2 border-border border-t-purple" />
    </div>
  )
}

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
  const { theme } = useTheme()
  return (
    <div style={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      {theme === 'blizzard' && <BlizzardFrame />}
      {user && !location.pathname.startsWith('/admin') && <Nav />}
      <div style={{ flex: 1, overflowY: 'auto', display: 'flex', flexDirection: 'column' }}>
      <Suspense fallback={<PageFallback />}>
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
            <Route path="topics" element={<AdminTopicsPage />} />
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
      </Suspense>
      </div>
    </div>
  )
}
