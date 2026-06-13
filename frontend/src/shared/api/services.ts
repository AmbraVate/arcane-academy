import api, { REFRESH_TOKEN_KEY } from './client'
import type {
  User, Badge, CodeRunResponse,
  ModuleDetail, LessonEncoding, PracticeResult,
  RetrievalResultDto, ReviewSessionDto, ReviewResultDto,
  DashboardDto, DiagnosticResultDto, FeynmanResultDto,
  RabbitHoleModule, CuriosityQueueItem, AnswerEntry, RabbitHoleTerm,
  SubscriptionStatus, GuidedStepDto, GuidedStepCheckResponse,
  SoloAssessmentResult,
  ReviewQueueItem, ReviewSubmitRequest, ReviewSubmitResponse,
} from '@/shared/types'

// â”€â”€ Auth â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
function storeRefreshToken(data: { refreshToken?: string }) {
  if (data.refreshToken) localStorage.setItem(REFRESH_TOKEN_KEY, data.refreshToken)
}

export const authApi = {
  register: async (username: string, email: string, password: string): Promise<User> => {
    const { data } = await api.post('/api/auth/register', { username, email, password })
    storeRefreshToken(data)
    return data
  },
  login: async (email: string, password: string): Promise<User> => {
    const { data } = await api.post('/api/auth/login', { email, password })
    storeRefreshToken(data)
    return data
  },
  /** Fetch fresh user data from the server (picks up admin changes to bypassPaywall etc.) */
  me: async (): Promise<User> => {
    const { data } = await api.get('/api/auth/me')
    storeRefreshToken(data)
    return data
  },
  /** Mark onboarding complete and collect the ARCANE_INITIATE badge if not yet earned. */
  completeOnboarding: async (): Promise<{ badge: Badge | null }> => {
    const { data } = await api.post('/api/onboarding/complete')
    const badge = data.badge && data.badge.id ? data.badge as Badge : null
    return { badge }
  },
}

// ── Modules ──────────────────────────────────────────────────────────────────
export const moduleApi = {
  getDetail: async (moduleId: string): Promise<ModuleDetail> => {
    const { data } = await api.get(`/api/modules/${moduleId}`)
    return data
  },
}

// â”€â”€ Encoding â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export const encodingApi = {
  start: async (lessonId: string): Promise<LessonEncoding> => {
    const { data } = await api.post(`/api/encoding/${lessonId}/start`)
    return data
  },
  advance: async (lessonId: string): Promise<LessonEncoding> => {
    const { data } = await api.post(`/api/encoding/${lessonId}/advance`)
    return data
  },
  submitPractice: async (lessonId: string, code: string): Promise<PracticeResult> => {
    const { data } = await api.post(`/api/encoding/${lessonId}/guided-practice/submit`, { code })
    return data
  },
  submitSoloPractice: async (lessonId: string, code: string): Promise<SoloAssessmentResult> => {
    const { data } = await api.post(`/api/encoding/${lessonId}/solo-practice/submit`, { code })
    return data
  },
  /** Submit a non-code solo assessment (RUBRIC_REFLECTION, PATTERN_MATCH, AI_REVIEW). */
  submitSoloAssessment: async (
    lessonId: string,
    payload: {
      answer?: string
      checkedRubricItems?: string[]
      confidence?: string
    }
  ): Promise<SoloAssessmentResult> => {
    const { data } = await api.post(`/api/encoding/${lessonId}/solo-practice/submit`, payload)
    return data
  },
  submitRetrieval: async (lessonId: string, answers: AnswerEntry[]): Promise<RetrievalResultDto> => {
    const { data } = await api.post(`/api/encoding/${lessonId}/retrieval-check/submit`, { answers })
    return data
  },
  submitFeynman: async (lessonId: string, explanation: string): Promise<FeynmanResultDto> => {
    const { data } = await api.post(`/api/encoding/${lessonId}/feynman/submit`, { explanation })
    return data
  },
  getFeynmanPrompt: async (lessonId: string): Promise<string> => {
    const { data } = await api.get(`/api/encoding/${lessonId}/feynman/prompt`)
    return data
  },
}

// ── Guided steps (Phase 3) ────────────────────────────────────────────────────
export const guidedStepApi = {
  getSteps: async (lessonId: string): Promise<GuidedStepDto[]> => {
    const { data } = await api.get(`/api/encoding/${lessonId}/guided/steps`)
    return data
  },
  checkStep: async (
    lessonId: string,
    stepId: string,
    answer: string,
  ): Promise<GuidedStepCheckResponse> => {
    const { data } = await api.post(
      `/api/encoding/${lessonId}/guided/steps/${stepId}/check`,
      { answer },
    )
    return data
  },
}

// â”€â”€ Reviews â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export const reviewApi = {
  getDaily: async (): Promise<ReviewSessionDto> => {
    const { data } = await api.get('/api/reviews/daily')
    return data
  },
  getInterleaved: async (lessonId: string): Promise<ReviewSessionDto> => {
    const { data } = await api.get(`/api/reviews/interleaved/${lessonId}`)
    return data
  },
  submit: async (sessionId: string, answers: AnswerEntry[]): Promise<ReviewResultDto> => {
    const { data } = await api.post(`/api/reviews/${sessionId}/submit`, { answers })
    return data
  },
}

// ── Diagnostic ────────────────────────────────────────────────────────────────
export const diagnosticApi = {
  start: async (domainId = 'java'): Promise<ReviewSessionDto> => {
    const { data } = await api.post(`/api/diagnostic/start?domainId=${domainId}`)
    return data
  },
  submit: async (answers: AnswerEntry[], domainId = 'java'): Promise<DiagnosticResultDto> => {
    const { data } = await api.post(`/api/diagnostic/submit?domainId=${domainId}`, { answers })
    return data
  },
  skip: async (domainId = 'java'): Promise<void> => {
    await api.post(`/api/diagnostic/skip?domainId=${domainId}`)
  },
}

// ── Dashboard ────────────────────────────────────────────────────────────────
export const dashboardApi = {
  get: async (domainId = 'java'): Promise<DashboardDto> => {
    const { data } = await api.get(`/api/dashboard?domainId=${domainId}`)
    return data
  },
  /** Public (unauthenticated) - returns module structure with all statuses LOCKED. */
  getPublic: async (domainId = 'java'): Promise<DashboardDto> => {
    const { data } = await api.get(`/api/dashboard/public?domainId=${domainId}`)
    return data
  },
  getReviewsDue: async (): Promise<number> => {
    const { data } = await api.get('/api/dashboard/reviews-due')
    return typeof data === 'number' ? data : (data?.count ?? 0)
  },
}

// â”€â”€ Rabbit Holes â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export const rabbitHoleApi = {
  getForChunk: async (moduleId: string): Promise<RabbitHoleModule[]> => {
    const { data } = await api.get(`/api/rabbit-holes/${moduleId}`)
    return data
  },
  getModule: async (moduleId: string): Promise<RabbitHoleModule> => {
    const { data } = await api.get(`/api/rabbit-holes/module/${moduleId}`)
    return data
  },
  submit: async (moduleId: string, code: string): Promise<PracticeResult> => {
    const { data } = await api.post(`/api/rabbit-holes/module/${moduleId}/submit`, { code })
    return data
  },
}

// â”€â”€ Curiosity Queue â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export const curiosityApi = {
  getAll: async (): Promise<CuriosityQueueItem[]> => {
    const { data } = await api.get('/api/curiosity-queue')
    return data
  },
  save: async (lessonId: string): Promise<void> => {
    await api.post(`/api/curiosity-queue/${lessonId}`)
  },
  remove: async (lessonId: string): Promise<void> => {
    await api.delete(`/api/curiosity-queue/${lessonId}`)
  },
}


// â”€â”€ Code (kept) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export const codeApi = {
  run: async (code: string, testInput?: string): Promise<CodeRunResponse> => {
    const { data } = await api.post('/api/code/run', { code, testInput })
    return data
  },
}

// ── Retention (spaced repetition) ────────────────────────────────────────────
export const retentionApi = {
  getQueue: async (): Promise<ReviewQueueItem[]> => {
    const { data } = await api.get('/api/retention/queue')
    return data
  },
  submitReview: async (payload: ReviewSubmitRequest): Promise<ReviewSubmitResponse> => {
    const { data } = await api.post('/api/retention/review', payload)
    return data
  },
}

// ── Badges ────────────────────────────────────────────────────────────────────
export const badgeApi = {
  getAll: async (): Promise<Badge[]> => {
    const { data } = await api.get('/api/badges')
    return data
  },
}

// â”€â”€ Leaderboards â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface LeaderboardEntry {
  rank: number
  username: string
  xpEarned: number
  globalXp: number
  streakDays: number
  rankTitle: string
  topicCount: number   // -1 on topic boards (only set for /polymath)
  badgeCount: number
}

export const leaderboardApi = {
  topicWeekly: async (domainId: string, limit = 20): Promise<LeaderboardEntry[]> => {
    const { data } = await api.get(`/api/leaderboard/topic/${domainId}/weekly?limit=${limit}`)
    return data
  },
  topicAllTime: async (domainId: string, limit = 20): Promise<LeaderboardEntry[]> => {
    const { data } = await api.get(`/api/leaderboard/topic/${domainId}/all-time?limit=${limit}`)
    return data
  },
  polymath: async (limit = 20): Promise<LeaderboardEntry[]> => {
    const { data } = await api.get(`/api/leaderboard/polymath?limit=${limit}`)
    return data
  },
}

// â”€â”€ Rabbit Hole Terms â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export const rabbitHoleTermApi = {
  getAll: async (): Promise<RabbitHoleTerm[]> => {
    const { data } = await api.get('/api/rabbit-hole-terms')
    return data
  },
  save: async (term: string, description: string, lessonId: string, domainId: string): Promise<RabbitHoleTerm> => {
    const { data } = await api.post('/api/rabbit-hole-terms', { term, description, lessonId, domainId })
    return data
  },
  remove: async (term: string): Promise<void> => {
    await api.delete(`/api/rabbit-hole-terms/${encodeURIComponent(term)}`)
  },
}

// â”€â”€ Public profile â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface PublicProfileDomain {
  domainId: string
  name: string
  glyph: string
  accentColor: string | null
  xpEarned: number
  lessonsCompleted: number
}

export interface PublicProfileBadge {
  id: string
  displayName: string
  glyph: string
  category: string
  earnedAt: string
}

export interface PublicProfile {
  username: string
  memberSince: string
  rank: string
  totalXp: number
  streakDays: number
  domains: PublicProfileDomain[]
  badges: PublicProfileBadge[]
}

export interface MyStuckReport {
  id: string
  domainId: string | null
  lessonId: string | null
  currentPhase: string | null
  currentUrl: string | null
  userMessage: string | null
  status: 'NEW' | 'REVIEWED' | 'RESOLVED'
  adminNotes: string | null
  createdAt: string
  updatedAt: string | null
}

export const stuckReportApi = {
  submit: async (payload: {
    domainId?: string
    lessonId?: string
    currentPhase?: string
    currentUrl: string
    userMessage?: string
    screenshotData?: string
  }): Promise<void> => {
    await api.post('/api/stuck-reports', payload)
  },
  mine: async (): Promise<MyStuckReport[]> => {
    const { data } = await api.get('/api/stuck-reports/mine')
    return data
  },
}

export const profileApi = {
  getPublic: async (username: string): Promise<PublicProfile | null> => {
    try {
      const { data } = await api.get(`/api/profile/public/${encodeURIComponent(username)}`)
      return data
    } catch (err: unknown) {
      // 404 = user doesn't exist OR is opted out
      const status = (err as { response?: { status?: number } })?.response?.status
      if (status === 404) return null
      throw err
    }
  },
  getVisibility: async (): Promise<boolean> => {
    const { data } = await api.get('/api/profile/visibility')
    return Boolean(data?.enabled)
  },
  setVisibility: async (enabled: boolean): Promise<boolean> => {
    const { data } = await api.post('/api/profile/visibility', { enabled })
    return Boolean(data?.enabled)
  },
}

// â”€â”€ Notes â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export interface UserNote {
  id: string
  lessonId: string
  moduleId: string
  title: string
  content: string
  createdAt: string
  updatedAt: string
}

export const notesApi = {
  list: async (): Promise<UserNote[]> => {
    const { data } = await api.get('/api/notes')
    return data
  },
  save: async (payload: {
    lessonId: string
    moduleId: string
    title: string
    content: string
  }): Promise<UserNote> => {
    const { data } = await api.post('/api/notes', payload)
    return data
  },
  delete: async (noteId: string): Promise<void> => {
    await api.delete(`/api/notes/${noteId}`)
  },
}

// â”€â”€ Payments â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export type PlanType = 'MONTHLY' | 'ANNUAL' | 'LIFETIME'

export interface SubscriptionStatusResponse {
  /** FREE | MONTHLY | ANNUAL | LIFETIME | CANCELLED */
  status: SubscriptionStatus
  /** True when the user currently has full access. */
  active: boolean
  /** ISO string - when the current billing period ends. Null for FREE / LIFETIME. */
  periodEnd: string | null
  /** Whether the user has a Stripe customer record (has ever paid). */
  hasStripeCustomer: boolean
}

export const paymentsApi = {
  /**
   * Creates a Stripe Checkout Session for the given plan.
   * Returns the Stripe-hosted checkout URL to redirect the user to.
   */
  createCheckout: async (planType: PlanType): Promise<string> => {
    const { data } = await api.post('/api/payments/checkout', { planType })
    return data.url as string
  },

  /**
   * Creates a Stripe Customer Portal session.
   * Returns the Stripe-hosted portal URL to redirect the user to.
   */
  createPortal: async (): Promise<string> => {
    const { data } = await api.post('/api/payments/portal')
    return data.url as string
  },

  /** Returns the current user's subscription status. */
  getStatus: async (): Promise<SubscriptionStatusResponse> => {
    const { data } = await api.get('/api/payments/status')
    return data
  },
}

// ── Capstones â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export interface UserCapstone {
  id: string
  moduleId: string
  title: string
  description: string | null
  codeContent: string | null
  githubUrl: string | null
  adminFeedback: string | null
  reviewedAt: string | null
  createdAt: string
  updatedAt: string
}

export const capstoneApi = {
  list: async (): Promise<UserCapstone[]> => {
    const { data } = await api.get('/api/capstones')
    return data
  },
  create: async (payload: {
    moduleId: string
    title: string
    description?: string
    codeContent?: string
    githubUrl?: string
  }): Promise<UserCapstone> => {
    const { data } = await api.post('/api/capstones', payload)
    return data
  },
  update: async (capstoneId: string, payload: {
    moduleId: string
    title: string
    description?: string
    codeContent?: string
    githubUrl?: string
  }): Promise<UserCapstone> => {
    const { data } = await api.put(`/api/capstones/${capstoneId}`, payload)
    return data
  },
  delete: async (capstoneId: string): Promise<void> => {
    await api.delete(`/api/capstones/${capstoneId}`)
  },
}
