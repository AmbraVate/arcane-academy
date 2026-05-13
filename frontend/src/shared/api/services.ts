import api from './client'
import type {
  User, Badge, CodeRunResponse,
  ChunkSummary, ChunkDetail, SubChunkEncoding, PracticeResult,
  RetrievalResultDto, ReviewSessionDto, ReviewResultDto,
  DashboardDto, DiagnosticResultDto, FeynmanResultDto,
  RabbitHoleModule, CuriosityQueueItem, AnswerEntry,
} from '@/shared/types'

// ── Auth ─────────────────────────────────────────────────────────────────────
export const authApi = {
  register: async (username: string, email: string, password: string): Promise<User> => {
    const { data } = await api.post('/api/auth/register', { username, email, password })
    return data
  },
  login: async (email: string, password: string): Promise<User> => {
    const { data } = await api.post('/api/auth/login', { email, password })
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

// ── Rabbit Holes ─────────────────────────────────────────────────────────────
export const rabbitHoleApi = {
  getForChunk: async (chunkId: string): Promise<RabbitHoleModule[]> => {
    const { data } = await api.get(`/api/rabbit-holes/${chunkId}`)
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
