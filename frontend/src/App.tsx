import React, {lazy, Suspense} from 'react'
import {Routes, Route, Navigate, Outlet, useNavigate, useLocation} from 'react-router-dom'
import ErrorBoundary from './features/errors/components/ErrorBoundary'
import {useAuth} from './shared/hooks/useAuth'
import {useTheme} from './hooks/useTheme'
import {useReviewsDue} from './hooks/queries'
import Nav from './components/layout/Nav'
import BlizzardFrame from './components/layout/BlizzardFrame'
import {BlizzardBackground} from './components/layout/BlizzardScene'
import {Library, RotateCcw, Trophy, User, Network} from 'lucide-react'

const DomainsPage = lazy(() => import('./features/domains/pages/DomainsPage'))
const LoginPage = lazy(() => import('./features/auth/pages/LoginPage'))
const RegisterPage = lazy(() => import('./features/auth/pages/RegisterPage'))
const OAuthCallbackPage = lazy(() => import('./features/auth/pages/OAuthCallbackPage'))
const ModuleMapPage = lazy(() => import('./features/domains/pages/ModuleMapPage'))
const EncodingPage = lazy(() => import('./features/learning/pages/EncodingPage'))
const ReviewPage = lazy(() => import('./features/review/pages/ReviewPage'))
const DiagnosticPage = lazy(() => import('./features/diagnostic/pages/DiagnosticPage'))
const OnboardingPage = lazy(() => import('./features/auth/pages/OnboardingPage'))
const RabbitHolePage = lazy(() => import('./features/exploration/pages/RabbitHolePage'))
const CuriosityQueuePage = lazy(() => import('./features/exploration/pages/CuriosityQueuePage'))
const ProfilePage = lazy(() => import('./features/profile/pages/ProfilePage'))
const DomainPage = lazy(() => import('./features/domains/pages/DomainPage'))
const DomainOnboardingPage = lazy(() => import('./features/onboarding/pages/DomainOnboardingPage'))
const DomainDiagnosticPage = lazy(() => import('./features/diagnostic/pages/DomainDiagnosticPage'))
const PrerequisiteCheckPage = lazy(() => import('./features/onboarding/pages/PrerequisiteCheckPage'))
const CssPrimerPage = lazy(() => import('./features/onboarding/pages/CssPrimerPage'))
const LeaderboardPage = lazy(() => import('./features/leaderboard/pages/LeaderboardPage'))
const PublicProfilePage = lazy(() => import('./features/profile/pages/PublicProfilePage'))
const LandingPage = lazy(() => import('./features/auth/pages/LandingPage'))
const HomePage = lazy(() => import('./features/home/pages/HomePage'))
const AdminLayout = lazy(() => import('./features/admin/pages/AdminLayout'))
const AdminDashboardPage = lazy(() => import('./features/admin/pages/AdminDashboardPage'))
const AdminChunksPage = lazy(() => import('./features/admin/pages/AdminChunksPage'))
const AdminLessonsPage = lazy(() => import('./features/admin/pages/AdminLessonsPage'))
const AdminLessonEditorPage = lazy(() => import('./features/admin/pages/AdminLessonEditorPage'))
const AdminQuestionsPage = lazy(() => import('./features/admin/pages/AdminQuestionsPage'))
const AdminUsersPage = lazy(() => import('./features/admin/pages/AdminUsersPage'))
const AdminImportExportPage = lazy(() => import('./features/admin/pages/AdminImportExportPage'))
const AdminDomainsPage = lazy(() => import('./features/admin/pages/AdminDomainsPage'))
const AdminStuckReportsPage = lazy(() => import('./features/admin/pages/AdminStuckReportsPage'))
const AdminCapstonesPage = lazy(() => import('./features/admin/pages/AdminCapstonesPage'))
const KnowledgeMapPage = lazy(() => import('./features/knowledge-map/pages/KnowledgeMapPage'))
const NotFoundPage = lazy(() => import('./features/errors/pages/NotFoundPage'))
const ErrorPage = lazy(() => import('./features/errors/pages/ErrorPage'))

function PageFallback() {
  return (
      <div className="flex items-center justify-center h-[60vh] text-muted">
        <div className="w-8 h-8 animate-spin rounded-full border-2 border-border border-t-purple"/>
      </div>
  )
}

function PrivateRoute({children}: { children: React.ReactNode }) {
  const {user} = useAuth()
  return user ? <>{children}</> : <Navigate to="/login" replace/>
}

function AdminRoute() {
  const {user} = useAuth()
  if (!user) return <Navigate to="/login" replace/>
  if (user.role !== 'ADMIN') return <Navigate to="/" replace/>
  return <Outlet/>
}

const RANK_FLOORS = [0, 800, 2000, 4000, 6500, 8000, 11000]

function BlizzardNav() {
  const {user, logout} = useAuth()
  const {theme} = useTheme()
  const navigate = useNavigate()
  const location = useLocation()
  const {data: reviewsDue = 0} = useReviewsDue()

  if (!user || theme !== 'blizzard') return null

  const rankIdx = RANK_FLOORS.reduce((acc, floor, i) => user.totalXp >= floor ? i : acc, 0)
  const isMaxRank = rankIdx === RANK_FLOORS.length - 1
  const floor = RANK_FLOORS[rankIdx]
  const ceiling = isMaxRank ? null : RANK_FLOORS[rankIdx + 1]
  const xpInRank = user.totalXp - floor
  const xpForRank = ceiling !== null ? ceiling - floor : xpInRank || 1
  const xpPct = ceiling !== null ? Math.min(100, (xpInRank / xpForRank) * 100) : 100
  const streak = user.streakDays ?? 0
  const streakHot = streak >= 3

  const NAV_ITEMS = [
    {label: 'Domains', Icon: Library, path: '/domains'},
    {label: 'Map', Icon: Network, path: '/knowledge-map', badge: null},
    {label: 'Review', Icon: RotateCcw, path: '/review', badge: reviewsDue > 0 ? reviewsDue : null},
    {label: 'Ranks', Icon: Trophy, path: '/leaderboard'},
    {label: 'Profile', Icon: User, path: '/profile'},
  ];

  return (
      <nav className="blizzard-nav">
        <div className="nav-brand" onClick={() => navigate('/')}>⌂ Arcane Academy</div>
        <div className="nav-spacer"/>
        <div className="nav-right">
          {/* Streak */}
          <div className={`streak${streakHot ? ' hot' : ''}`}>
            <span className="flame">ðŸ”¥</span>
            <span className="num">{streak}</span>
          </div>

          {/* XP bar */}
          <div className="xpwrap">
            <span className="xp-lbl">XP</span>
            <div className="xp-bar">
              <div className="xp-fill" style={{width: xpPct + '%'}}/>
            </div>
            <span className="xp-num">{user.totalXp} xp</span>
          </div>

          {/* Rank */}
          <div className="rank-pill">{user.rank}</div>

          {/* Nav buttons */}
          {NAV_ITEMS.map(({label, Icon, path, badge}) => {
            const active = location.pathname === path || location.pathname.startsWith(path + '/')
            return (
                <button
                    key={path}
                    className={`nav-btn${active ? ' active' : ''}`}
                    onClick={() => navigate(path)}
                    title={label}
                >
                  <span className="icon"><Icon size={16}/></span>
                  <span className="lbl">{label}</span>
                  {badge != null && <span className="badge">{badge}</span>}
                </button>
            )
          })}

          {/* Logout */}
          <button className="nav-btn" onClick={logout} title="Logout">
            <span className="icon">ðŸšª</span>
            <span className="lbl">Logout</span>
          </button>
        </div>
      </nav>
  )
}

/** Redirects legacy /topic/:id(/...) URLs to the new /domain/:id(/...) scheme, preserving the path tail. */
function LegacyTopicRedirect() {
  const location = useLocation()
  const to = location.pathname.replace(/^\/topic\b/, '/domain') + location.search
  return <Navigate to={to} replace/>
}

function AppRoutes() {
  return (
      <Suspense fallback={<PageFallback/>}>
        <Routes>
          <Route path="/login" element={<LoginPageGuard/>}/>
          <Route path="/register" element={<RegisterPageGuard/>}/>
          <Route path="/oauth2/callback" element={<OAuthCallbackPage/>}/>
          <Route path="/onboarding" element={<PrivateRoute><OnboardingPage/></PrivateRoute>}/>
          <Route path="/diagnostic" element={<PrivateRoute><DiagnosticPage/></PrivateRoute>}/>
          <Route path="/profile" element={<PrivateRoute><ProfilePage/></PrivateRoute>}/>
          <Route path="/" element={<HomeRedirect/>}/>
          <Route path="/chunk/:moduleId" element={<PrivateRoute><ModuleMapPage/></PrivateRoute>}/>
          <Route path="/learn/:lessonId" element={<PrivateRoute><EncodingPage/></PrivateRoute>}/>
          <Route path="/review" element={<PrivateRoute><ReviewPage/></PrivateRoute>}/>
          <Route path="/rabbit-hole/:id" element={<PrivateRoute><RabbitHolePage/></PrivateRoute>}/>
          <Route path="/curiosity-queue"
                 element={<PrivateRoute><CuriosityQueuePage/></PrivateRoute>}/>
          <Route path="/domains" element={<PrivateRoute><DomainsPage/></PrivateRoute>}/>
          <Route path="/domain/:domainId" element={<PrivateRoute><DomainPage/></PrivateRoute>}/>
          <Route path="/domain/:domainId/onboarding"
                 element={<PrivateRoute><DomainOnboardingPage/></PrivateRoute>}/>
          <Route path="/domain/:domainId/diagnostic"
                 element={<PrivateRoute><DomainDiagnosticPage/></PrivateRoute>}/>
          <Route path="/domain/:domainId/prereq-check"
                 element={<PrivateRoute><PrerequisiteCheckPage/></PrivateRoute>}/>
          <Route path="/domain/:domainId/css-primer"
                 element={<PrivateRoute><CssPrimerPage/></PrivateRoute>}/>
          {/* Legacy /topics + /topic/* URLs → redirect to the new /domains scheme (bookmarks, old links). */}
          <Route path="/topics" element={<Navigate to="/domains" replace/>}/>
          <Route path="/topic/*" element={<LegacyTopicRedirect/>}/>
          <Route path="/knowledge-map" element={<PrivateRoute><KnowledgeMapPage/></PrivateRoute>}/>
          <Route path="/leaderboard" element={<PrivateRoute><LeaderboardPage/></PrivateRoute>}/>
          <Route path="/u/:username" element={<PrivateRoute><PublicProfilePage/></PrivateRoute>}/>

          <Route element={<AdminRoute/>}>
            <Route path="/admin" element={<AdminLayout/>}>
              <Route index element={<AdminDashboardPage/>}/>
              <Route path="domains" element={<AdminDomainsPage/>}/>
              <Route path="topics" element={<Navigate to="/admin/domains" replace/>}/>
              <Route path="chunks" element={<AdminChunksPage/>}/>
              <Route path="chunks/:moduleId/subchunks" element={<AdminLessonsPage/>}/>
              <Route path="subchunks/:lessonId/edit" element={<AdminLessonEditorPage/>}/>
              <Route path="subchunks/:lessonId/questions" element={<AdminQuestionsPage/>}/>
              <Route path="users" element={<AdminUsersPage/>}/>
              <Route path="import-export" element={<AdminImportExportPage/>}/>
              <Route path="stuck-reports" element={<AdminStuckReportsPage/>}/>
              <Route path="capstones" element={<AdminCapstonesPage/>}/>
            </Route>
          </Route>

          <Route path="/error" element={<ErrorPage type="server"/>}/>
          <Route path="*" element={<NotFoundPage/>}/>
        </Routes>
      </Suspense>
  )
}

function LoginPageGuard() {
  const {user} = useAuth()
  return user ? <Navigate to="/" replace/> : <LoginPage/>
}

function RegisterPageGuard() {
  const {user} = useAuth()
  return user ? <Navigate to="/" replace/> : <RegisterPage/>
}

function HomeRedirect() {
  const {user} = useAuth()
  return user ? <HomePage/> : <LandingPage/>
}

export default function App() {
  const {user} = useAuth()
  const {theme, blizzardPrefs, blizzardAvailable} = useTheme()
  const location = useLocation()

  // Blizzard theme is only rendered when it is both selected and available (not on mobile).
  // blizzardAvailable = false on mobile, so the default layout renders instead.
  if (theme === 'blizzard' && blizzardAvailable) {
    return (
        <ErrorBoundary>
          <div className="stage">
            <BlizzardBackground scene={blizzardPrefs.scene} snow={blizzardPrefs.snow}/>
            <BlizzardFrame/>
            {user && !location.pathname.startsWith('/admin') && <BlizzardNav/>}
            <div className="viewport">
              <div className="page">
                <AppRoutes/>
              </div>
            </div>
          </div>
        </ErrorBoundary>
    )
  }

  return (
      <ErrorBoundary>
        <div style={{height: '100%', display: 'flex', flexDirection: 'column'}}>
          {user && !location.pathname.startsWith('/admin') && <Nav/>}
          <div style={{flex: 1, overflowY: 'auto', display: 'flex', flexDirection: 'column'}}>
            <AppRoutes/>
          </div>
        </div>
      </ErrorBoundary>
  )
}
