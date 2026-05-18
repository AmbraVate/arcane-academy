// ── Auth ──────────────────────────────────────────────────────────────────────
export interface User {
  userId: string; username: string; totalXp: number; rank: string; streakDays: number; token: string
  role?: 'USER' | 'ADMIN'
}

// ── Shared (kept from old system) ────────────────────────────────────────────
export interface StoryBeat {
  type: 'narration' | 'dialogue' | 'example'; text: string
  av?: string; cls?: string; speaker?: string; sCls?: string
}
export interface TestCaseLabel { label: string }
export interface TestResult { label: string; passed: boolean; actualOutput: string; expectedOutput: string }
export interface Badge { id: string; displayName: string; description: string; glyph: string; category: string; earned: boolean; earnedAt: string | null }
export interface CodeRunResponse { output: string | null; error: string | null; status: 'SUCCESS'|'COMPILE_ERROR'|'RUNTIME_ERROR'|'TIMEOUT'|'ERROR' }

// ── Chunks ───────────────────────────────────────────────────────────────────
export interface ChunkSummary {
  id: string; title: string; glyph: string; status: string
  totalSubChunks: number; completedSubChunks: number
  memoryStrength: number; healthColor: string; prerequisiteIds: string[]
}

export interface SubChunkSummary {
  id: string; title: string; sortOrder: number; status: string
  currentPhase: string; memoryStrength: number; healthColor: string
  feynmanCompleted: boolean; xpReward: number
}

export interface ChunkDetail {
  id: string; topicId: string; title: string; glyph: string; status: string
  subChunks: SubChunkSummary[]
}

// ── Encoding ─────────────────────────────────────────────────────────────────
export type EncodingPhase = 'HOOK' | 'EXPLANATION' | 'GUIDED_PRACTICE' | 'SOLO_PRACTICE' | 'RETRIEVAL_CHECK' | 'COMPLETE'

export interface StoryRabbitHoleTerm { term: string; description: string }

export interface SubChunkEncoding {
  subChunkId: string; chunkId: string; topicId: string; title: string
  phase: EncodingPhase; status: string
  hookHtml: string | null; explanationHtml: string | null
  storyBeats: StoryBeat[] | null
  guidedPracticeHtml: string | null; starterCode: string | null
  soloPracticeHtml: string | null
  testCaseLabels: TestCaseLabel[] | null; filename: string | null
  retrievalQuestions: QuestionDto[] | null
  feynmanPrompt: string | null; xpReward: number
  /** "JAVA" | "TAILWIND" | "NONE" */
  practiceType: string
  rabbitHoleTerms: StoryRabbitHoleTerm[] | null
  /** Exemplar answer revealed after solo practice is complete (written-response sub-chunks only). */
  modelAnswer: string | null
  /** Exemplar answer revealed after guided practice is passed (written-response sub-chunks only). */
  guidedPracticeModelAnswer: string | null
}

export interface PracticeResult {
  allPassed: boolean; testResults: TestResult[]
  xpEarned: number; mentorFeedback: string | null
  errorType?: string | null; newBadges?: Badge[]
}

// ── Questions ────────────────────────────────────────────────────────────────
export interface QuestionDto {
  id: string; tier: string; type: string
  questionHtml: string; codeSnippet: string | null
  options: string[] | null
}

export interface AnswerEntry { questionId: string; answer: string }

export interface QuestionResultDto {
  questionId: string; correct: boolean
  userAnswer: string; correctAnswer: string; explanationHtml: string
}

// ── Retrieval ────────────────────────────────────────────────────────────────
export interface RetrievalResultDto {
  score: number; correct: number; total: number
  results: QuestionResultDto[]; passed: boolean
  xpEarned: number; newBadges: Badge[]
  recommendation: string
}

// ── Reviews ──────────────────────────────────────────────────────────────────
export interface ReviewSessionDto {
  sessionId: string; sessionType: string
  questions: QuestionDto[]; estimatedMinutes: number
}

export interface ReviewResultDto {
  score: number; correct: number; total: number
  results: QuestionResultDto[]; newBadges: Badge[]
}

// ── Diagnostic ───────────────────────────────────────────────────────────────
export interface DiagnosticResultDto {
  recommendedPath: string
  chunkRecommendations: Record<string, string>
  overallScore: number
}

// ── Feynman ──────────────────────────────────────────────────────────────────
export interface FeynmanResultDto {
  accuracy: number; completeness: number; simplicity: number; connection: number
  overallScore: number; feedback: string; xpEarned: number
}

// ── Dashboard ────────────────────────────────────────────────────────────────
export interface ChunkHealthDto {
  chunkId: string; title: string; glyph: string
  status: string; memoryStrength: number; healthColor: string
  totalSubChunks: number; completedSubChunks: number
  tier: string
}

export interface DashboardDto {
  totalXp: number; rank: string; streakDays: number; streakAtRisk: boolean
  currentPath: string; diagnosticCompleted: boolean; diagnosticCompletedAt: string | null
  reviewsDue: number; dailyGoalMinutes: number
  overallProgress: number; chunkHealth: ChunkHealthDto[]
}

// ── Rabbit Holes ─────────────────────────────────────────────────────────────
export interface RabbitHoleModule {
  id: string; chunkId: string; title: string
  contentHtml: string; storyBeats: StoryBeat[]
  starterCode: string; testCaseLabels: { label: string }[]; filename: string; sortOrder: number
}

// ── Curiosity Queue ──────────────────────────────────────────────────────────
export interface CuriosityQueueItem {
  id: string; userId: string; subChunkId: string; savedAt: string
}

// ── Rabbit Hole Terms ────────────────────────────────────────────────────────
export interface RabbitHoleTerm {
  id: string; term: string; description: string | null
  subChunkId: string | null; topicId: string | null; savedAt: string
}
