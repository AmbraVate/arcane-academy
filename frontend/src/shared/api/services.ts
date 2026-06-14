import api, { REFRESH_TOKEN_KEY } from './client'
import type {
  User, Badge, CodeRunResponse,
  ChunkSummary, ChunkDetail, SubChunkEncoding, PracticeResult,
  RetrievalResultDto, ReviewSessionDto, ReviewResultDto,
  DashboardDto, DiagnosticResultDto, FeynmanResultDto,
  CuriosityQueueItem, AnswerEntry, RabbitHoleTerm,
} from '@/shared/types'

// ── Auth ─────────────────────────────────────────────────────────────────────
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
}

// ── Chunks ───────────────────────────────────────────────────────────────────
export const chunkApi = {
  getAll: async (): Promise<ChunkSummary[]> => {
    const { data } = await api.get('/api/chunks')
    return data
  },
  getDetail: async (chunkId: string): Promise<ChunkDetail> => {
    const { data } = await api.get(`/api/chunks/${chunkId}`)
    return data
  },
}

// ── Encoding ─────────────────────────────────────────────────────────────────
export const encodingApi = {
  start: async (subChunkId: string): Promise<SubChunkEncoding> => {
    const { data } = await api.post(`/api/encoding/${subChunkId}/start`)
    return data
  },
  advance: async (subChunkId: string): Promise<SubChunkEncoding> => {
    const { data } = await api.post(`/api/encoding/${subChunkId}/advance`)
    return data
  },
  submitPractice: async (subChunkId: string, code: string): Promise<PracticeResult> => {
    const { data } = await api.post(`/api/encoding/${subChunkId}/guided-practice/submit`, { code })
    return data
  },
  submitSoloPractice: async (subChunkId: string, code: string): Promise<PracticeResult> => {
    const { data } = await api.post(`/api/encoding/${subChunkId}/solo-practice/submit`, { code })
    return data
  },
  submitRetrieval: async (subChunkId: string, answers: AnswerEntry[]): Promise<RetrievalResultDto> => {
    const { data } = await api.post(`/api/encoding/${subChunkId}/retrieval-check/submit`, { answers })
    return data
  },
  submitFeynman: async (subChunkId: string, explanation: string): Promise<FeynmanResultDto> => {
    const { data } = await api.post(`/api/encoding/${subChunkId}/feynman/submit`, { explanation })
    return data
  },
  getFeynmanPrompt: async (subChunkId: string): Promise<string> => {
    const { data } = await api.get(`/api/encoding/${subChunkId}/feynman/prompt`)
    return data
  },
}

// ── Reviews ──────────────────────────────────────────────────────────────────
export const reviewApi = {
  getDaily: async (): Promise<ReviewSessionDto> => {
    const { data } = await api.get('/api/reviews/daily')
    return data
  },
  getInterleaved: async (subChunkId: string): Promise<ReviewSessionDto> => {
    const { data } = await api.get(`/api/reviews/interleaved/${subChunkId}`)
    return data
  },
  submit: async (sessionId: string, answers: AnswerEntry[]): Promise<ReviewResultDto> => {
    const { data } = await api.post(`/api/reviews/${sessionId}/submit`, { answers })
    return data
  },
}

// ── Diagnostic ───────────────────────────────────────────────────────────────
export const diagnosticApi = {
  start: async (topicId = 'java'): Promise<ReviewSessionDto> => {
    const { data } = await api.post(`/api/diagnostic/start?topicId=${topicId}`)
    return data
  },
  submit: async (answers: AnswerEntry[], topicId = 'java'): Promise<DiagnosticResultDto> => {
    const { data } = await api.post(`/api/diagnostic/submit?topicId=${topicId}`, { answers })
    return data
  },
  skip: async (topicId = 'java'): Promise<void> => {
    await api.post(`/api/diagnostic/skip?topicId=${topicId}`)
  },
  getResults: async (): Promise<DiagnosticResultDto> => {
    const { data } = await api.get('/api/diagnostic/results')
    return data
  },
}

// ── Dashboard ────────────────────────────────────────────────────────────────
export const dashboardApi = {
  get: async (topicId = 'java'): Promise<DashboardDto> => {
    const { data } = await api.get(`/api/dashboard?topicId=${topicId}`)
    return data
  },
  getReviewsDue: async (): Promise<number> => {
    const { data } = await api.get('/api/dashboard/reviews-due')
    return typeof data === 'number' ? data : (data?.count ?? 0)
  },
}

// ── Curiosity Queue ──────────────────────────────────────────────────────────
export const curiosityApi = {
  getAll: async (): Promise<CuriosityQueueItem[]> => {
    const { data } = await api.get('/api/curiosity-queue')
    return data
  },
  save: async (subChunkId: string): Promise<void> => {
    await api.post(`/api/curiosity-queue/${subChunkId}`)
  },
  remove: async (subChunkId: string): Promise<void> => {
    await api.delete(`/api/curiosity-queue/${subChunkId}`)
  },
}

// ── Tailwind Practice ─────────────────────────────────────────────────────────
export const tailwindApi = {
  submit: async (subChunkId: string, html: string): Promise<PracticeResult> => {
    const { data } = await api.post(`/api/tailwind/${subChunkId}/submit`, { html })
    return data
  },
  submitSoloPractice: async (subChunkId: string, html: string): Promise<PracticeResult> => {
    const { data } = await api.post(`/api/tailwind/${subChunkId}/solo-practice/submit`, { html })
    return data
  },
}

// ── React Practice ────────────────────────────────────────────────────────────
// Tests run in the iframe sandbox client-side (see ReactEditor); the per-test
// pass/fail is sent here for XP awarding. Backend does a structural sanity
// check on the JSX source — see ReactPracticeService for rationale.
export interface ReactClientTestResult {
  label: string
  passed: boolean
  actual: string
}

export const reactApi = {
  submit: async (
    subChunkId: string,
    code: string,
    clientTestResults: ReactClientTestResult[],
  ): Promise<PracticeResult> => {
    const { data } = await api.post(`/api/react/${subChunkId}/submit`, { code, clientTestResults })
    return data
  },
  submitSoloPractice: async (
    subChunkId: string,
    code: string,
    clientTestResults: ReactClientTestResult[],
  ): Promise<PracticeResult> => {
    const { data } = await api.post(`/api/react/${subChunkId}/solo-practice/submit`, { code, clientTestResults })
    return data
  },
}

// ── SQL Practice ──────────────────────────────────────────────────────────────
// sql.js (SQLite-WASM) runs inside the iframe; the harness compares the user's
// query result to expected rows or to a reference query and reports per-test
// pass/fail. Backend does a structural sanity check on the SQL source before
// awarding XP — see SqlPracticeService.
export interface SqlClientTestResult {
  label: string
  passed: boolean
  actual: string
}

export const sqlApi = {
  submit: async (
    subChunkId: string,
    code: string,
    clientTestResults: SqlClientTestResult[],
  ): Promise<PracticeResult> => {
    const { data } = await api.post(`/api/sql/${subChunkId}/submit`, { code, clientTestResults })
    return data
  },
  submitSoloPractice: async (
    subChunkId: string,
    code: string,
    clientTestResults: SqlClientTestResult[],
  ): Promise<PracticeResult> => {
    const { data } = await api.post(`/api/sql/${subChunkId}/solo-practice/submit`, { code, clientTestResults })
    return data
  },
}

// ── R (statistics) practice ────────────────────────────────────────────────
export interface RClientTestResult {
  label: string
  passed: boolean
  actual: string
}

export const rApi = {
  submit: async (
    subChunkId: string,
    code: string,
    clientTestResults: RClientTestResult[],
  ): Promise<PracticeResult> => {
    const { data } = await api.post(`/api/r/${subChunkId}/submit`, { code, clientTestResults })
    return data
  },
  submitSoloPractice: async (
    subChunkId: string,
    code: string,
    clientTestResults: RClientTestResult[],
  ): Promise<PracticeResult> => {
    const { data } = await api.post(`/api/r/${subChunkId}/solo-practice/submit`, { code, clientTestResults })
    return data
  },
}

// ── Code (kept) ──────────────────────────────────────────────────────────────
export const codeApi = {
  run: async (code: string, testInput?: string): Promise<CodeRunResponse> => {
    const { data } = await api.post('/api/code/run', { code, testInput })
    return data
  },
}

// ── Badges (kept) ────────────────────────────────────────────────────────────
export const badgeApi = {
  getAll: async (): Promise<Badge[]> => {
    const { data } = await api.get('/api/badges')
    return data
  },
  getEarned: async (): Promise<Badge[]> => {
    const { data } = await api.get('/api/badges/earned')
    return data
  },
}

// ── Leaderboards ─────────────────────────────────────────────────────────────
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
  topicWeekly: async (topicId: string, limit = 20): Promise<LeaderboardEntry[]> => {
    const { data } = await api.get(`/api/leaderboard/topic/${topicId}/weekly?limit=${limit}`)
    return data
  },
  topicAllTime: async (topicId: string, limit = 20): Promise<LeaderboardEntry[]> => {
    const { data } = await api.get(`/api/leaderboard/topic/${topicId}/all-time?limit=${limit}`)
    return data
  },
  polymath: async (limit = 20): Promise<LeaderboardEntry[]> => {
    const { data } = await api.get(`/api/leaderboard/polymath?limit=${limit}`)
    return data
  },
}

// ── Rabbit Hole Terms ─────────────────────────────────────────────────────────
export const rabbitHoleTermApi = {
  getAll: async (): Promise<RabbitHoleTerm[]> => {
    const { data } = await api.get('/api/rabbit-hole-terms')
    return data
  },
  save: async (term: string, description: string, subChunkId: string, topicId: string): Promise<RabbitHoleTerm> => {
    const { data } = await api.post('/api/rabbit-hole-terms', { term, description, subChunkId, topicId })
    return data
  },
  remove: async (term: string): Promise<void> => {
    await api.delete(`/api/rabbit-hole-terms/${encodeURIComponent(term)}`)
  },
}

// ── Public profile ───────────────────────────────────────────────────────────
export interface PublicProfileTopic {
  topicId: string
  name: string
  glyph: string
  accentColor: string | null
  xpEarned: number
  subChunksCompleted: number
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
  topics: PublicProfileTopic[]
  badges: PublicProfileBadge[]
}

export interface MyStuckReport {
  id: string
  topicId: string | null
  subChunkId: string | null
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
    topicId?: string
    subChunkId?: string
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

// ── Notes ────────────────────────────────────────────────────────────────────

export interface UserNote {
  id: string
  subChunkId: string
  chunkId: string
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
    subChunkId: string
    chunkId: string
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

// ── Capstones ────────────────────────────────────────────────────────────────

export interface UserCapstone {
  id: string
  chunkId: string
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
    chunkId: string
    title: string
    description?: string
    codeContent?: string
    githubUrl?: string
  }): Promise<UserCapstone> => {
    const { data } = await api.post('/api/capstones', payload)
    return data
  },
  update: async (capstoneId: string, payload: {
    chunkId: string
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
