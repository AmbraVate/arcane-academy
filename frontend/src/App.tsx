import React, { lazy, Suspense } from 'react'
import { Routes, Route, Navigate, Outlet, useNavigate, useLocation, useParams } from 'react-router-dom'
import ErrorBoundary from './features/errors/components/ErrorBoundary'
import { useAuth } from './shared/hooks/useAuth'
import { useReviewsDue } from './hooks/queries'
import Nav from './components/layout/Nav'
import { TutorialProvider } from './features/tutorial/context/TutorialContext'
import TutorialOverlay from './features/tutorial/components/TutorialOverlay'
import { Library, RotateCcw, Trophy, User } from 'lucide-react'

const DomainsPage          = lazy(() => import('./features/domains/pages/DomainsPage'))
const LoginPage            = lazy(() => import('./features/auth/pages/LoginPage'))
const RegisterPage         = lazy(() => import('./features/auth/pages/RegisterPage'))
const ResetPasswordPage    = lazy(() => import('./features/auth/pages/ResetPasswordPage'))
const OAuthCallbackPage    = lazy(() => import('./features/auth/pages/OAuthCallbackPage'))
const ModuleMapPage        = lazy(() => import('./features/domains/pages/ModuleMapPage'))
const EncodingPage         = lazy(() => import('./features/learning/pages/EncodingPage'))
const ReviewPage           = lazy(() => import('./features/review/pages/ReviewPage'))
const RabbitHolePage       = lazy(() => import('./features/exploration/pages/RabbitHolePage'))
const CuriosityQueuePage   = lazy(() => import('./features/exploration/pages/CuriosityQueuePage'))
const ProfilePage          = lazy(() => import('./features/profile/pages/ProfilePage'))
const DomainPage           = lazy(() => import('./features/domains/pages/DomainPage'))
const LeaderboardPage      = lazy(() => import('./features/leaderboard/pages/LeaderboardPage'))
const PublicProfilePage    = lazy(() => import('./features/profile/pages/PublicProfilePage'))
const LandingPage          = lazy(() => import('./features/auth/pages/LandingPage'))
const HomePage             = lazy(() => import('./features/home/pages/HomePage'))
const AdminLayout          = lazy(() => import('./features/admin/pages/AdminLayout'))
const AdminDashboardPage   = lazy(() => import('./features/admin/pages/AdminDashboardPage'))
const AdminChunksPage      = lazy(() => import('./features/admin/pages/AdminChunksPage'))
const AdminLessonsPage     = lazy(() => import('./features/admin/pages/AdminLessonsPage'))
const AdminLessonEditorPage = lazy(() => import('./features/admin/pages/AdminLessonEditorPage'))
const AdminQuestionsPage   = lazy(() => import('./features/admin/pages/AdminQuestionsPage'))
const AdminUsersPage       = lazy(() => import('./features/admin/pages/AdminUsersPage'))
const AdminImportExportPage = lazy(() => import('./features/admin/pages/AdminImportExportPage'))
const AdminDomainsPage     = lazy(() => import('./features/admin/pages/AdminDomainsPage'))
const AdminStuckReportsPage = lazy(() => import('./features/admin/pages/AdminStuckReportsPage'))
const AdminCapstonesPage   = lazy(() => import('./features/admin/pages/AdminCapstonesPage'))
const TutorialLessonPage   = lazy(() => import('./features/tutorial/pages/TutorialLessonPage'))
const SettingsPage         = lazy(() => import('./features/settings/pages/SettingsPage'))
const TopicLessonsPage     = lazy(() => import('./features/domains/pages/TopicLessonsPage'))
const NotFoundPage         = lazy(() => import('./features/errors/pages/NotFoundPage'))
const ErrorPage            = lazy(() => import('./features/errors/pages/ErrorPage'))

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

// ── Legacy redirect helpers ───────────────────────────────────────────────────

/** /domain/:id/onboarding|diagnostic|etc → /pathway/:id */
function LegacyDomainSubpathRedirect() {
  const { domainId } = useParams<{ domainId: string }>()
  return <Navigate to={`/pathway/${domainId}`} replace />
}

/** /domain/:id → /pathway/:id */
function LegacyDomainRedirect() {
  const { domainId } = useParams<{ domainId: string }>()
  return <Navigate to={`/pathway/${domainId}`} replace />
}

/** /chunk/:moduleId → /module/:moduleId */
function LegacyChunkRedirect() {
  const { moduleId } = useParams<{ moduleId: string }>()
  return <Navigate to={`/module/${moduleId}`} replace />
}

/** /chunk/:moduleId/topic/:topicId → /module/:moduleId/topic/:topicId */
function LegacyChunkTopicRedirect() {
  const { moduleId, topicId } = useParams<{ moduleId: string; topicId: string }>()
  return <Navigate to={`/module/${moduleId}/topic/${topicId}`} replace />
}

/** /topic/:id → /pathway/:id (original pre-V24 URLs) */
function LegacyTopicRedirect() {
  const location = useLocation()
  const to = location.pathname.replace(/^\/topic\b/, '/pathway') + location.search
  return <Navigate to={to} replace />
}

function AppRoutes() {
  return (
    <Suspense fallback={<PageFallback />}>
      <Routes>
        <Route path="/login"          element={<LoginPageGuard />} />
        <Route path="/register"       element={<RegisterPageGuard />} />
        <Route path="/reset-password" element={<ResetPasswordPage />} />
        <Route path="/oauth2/callback" element={<OAuthCallbackPage />} />
<Route path="/profile"  element={<PrivateRoute><ProfilePage /></PrivateRoute>} />
        <Route path="/settings" element={<PrivateRoute><SettingsPage /></PrivateRoute>} />
        <Route path="/" element={<HomeRedirect />} />
        {/* Module + topic pages — publicly browsable (structure only; content requires login) */}
        <Route path="/module/:moduleId" element={<ModuleMapPage />} />
        <Route path="/module/:moduleId/topic/:topicId" element={<TopicLessonsPage />} />
        <Route path="/learn/:lessonId" element={<PrivateRoute><EncodingPage /></PrivateRoute>} />
        <Route path="/review"   element={<PrivateRoute><ReviewPage /></PrivateRoute>} />
        <Route path="/rabbit-hole/:id" element={<PrivateRoute><RabbitHolePage /></PrivateRoute>} />
        <Route path="/curiosity-queue" element={<PrivateRoute><CuriosityQueuePage /></PrivateRoute>} />
        {/* Schools listing — publicly browsable */}
        <Route path="/schools"  element={<DomainsPage />} />
        {/* Pathway page publicly browsable; module content requires login */}
        <Route path="/pathway/:domainId" element={<DomainPage />} />
        {/* Legacy URL redirects (permanent backward compat) */}
        <Route path="/domains"  element={<Navigate to="/schools" replace />} />
        <Route path="/topics"   element={<Navigate to="/schools" replace />} />
        <Route path="/topic/*"  element={<LegacyTopicRedirect />} />
        <Route path="/domain/:domainId" element={<LegacyDomainRedirect />} />
        <Route path="/domain/:domainId/onboarding"   element={<LegacyDomainSubpathRedirect />} />
        <Route path="/domain/:domainId/diagnostic"   element={<LegacyDomainSubpathRedirect />} />
        <Route path="/domain/:domainId/prereq-check" element={<LegacyDomainSubpathRedirect />} />
        <Route path="/domain/:domainId/css-primer"   element={<LegacyDomainSubpathRedirect />} />
        <Route path="/chunk/:moduleId" element={<LegacyChunkRedirect />} />
        <Route path="/chunk/:moduleId/topic/:topicId" element={<LegacyChunkTopicRedirect />} />
        <Route path="/tutorial/lesson" element={<PrivateRoute><TutorialLessonPage /></PrivateRoute>} />
        <Route path="/leaderboard" element={<PrivateRoute><LeaderboardPage /></PrivateRoute>} />
        <Route path="/u/:username" element={<PrivateRoute><PublicProfilePage /></PrivateRoute>} />

        <Route element={<AdminRoute />}>
          <Route path="/admin" element={<AdminLayout />}>
            <Route index element={<AdminDashboardPage />} />
            <Route path="domains" element={<AdminDomainsPage />} />
            <Route path="topics"  element={<Navigate to="/admin/domains" replace />} />
            <Route path="chunks" element={<AdminChunksPage />} />
            <Route path="chunks/:moduleId/subchunks" element={<AdminLessonsPage />} />
            <Route path="subchunks/:lessonId/edit" element={<AdminLessonEditorPage />} />
            <Route path="subchunks/:lessonId/questions" element={<AdminQuestionsPage />} />
            <Route path="users" element={<AdminUsersPage />} />
            <Route path="import-export" element={<AdminImportExportPage />} />
            <Route path="stuck-reports" element={<AdminStuckReportsPage />} />
            <Route path="capstones" element={<AdminCapstonesPage />} />
          </Route>
        </Route>

        <Route path="/error" element={<ErrorPage type="server" />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </Suspense>
  )
}

function LoginPageGuard() {
  const { user } = useAuth()
  return user ? <Navigate to="/" replace /> : <LoginPage />
}

function RegisterPageGuard() {
  const { user } = useAuth()
  return user ? <Navigate to="/" replace /> : <RegisterPage />
}

function HomeRedirect() {
  const { user } = useAuth()
  if (!user) return <LandingPage />
  // Redirect to the path the user intended before being sent to login
  const intended = sessionStorage.getItem('arcane-intended-path')
  if (intended) {
    sessionStorage.removeItem('arcane-intended-path')
    return <Navigate to={intended} replace />
  }
  return <HomePage />
}

export default function App() {
  const { user } = useAuth()
  const location = useLocation()

  return (
    <ErrorBoundary>
      <TutorialProvider>
        <div style={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
          {user && !location.pathname.startsWith('/admin') && <Nav />}
          <div style={{ flex: 1, overflowY: 'auto', display: 'flex', flexDirection: 'column' }}>
            <AppRoutes />
          </div>
        </div>
        {/* Tutorial overlay rendered above everything */}
        <TutorialOverlay />
      </TutorialProvider>
    </ErrorBoundary>
  )
}
