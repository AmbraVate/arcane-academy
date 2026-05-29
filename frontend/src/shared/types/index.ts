// â”€â”€ Auth â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export type SubscriptionStatus = 'FREE' | 'MONTHLY' | 'ANNUAL' | 'LIFETIME' | 'CANCELLED'

export interface User {
  userId: string; username: string; totalXp: number; rank: string; streakDays: number; token: string
  role?: 'USER' | 'ADMIN'
  /** Admin-granted flag allowing enrolment in multiple topics without a subscription. */
  bypassPaywall?: boolean
  /** Current billing status. FREE = one topic allowed; MONTHLY/ANNUAL/LIFETIME = full access. */
  subscriptionStatus?: SubscriptionStatus
  /** False until the user completes or skips the onboarding walkthrough. */
  onboardingCompleted?: boolean
}

/** Returns true when the user has full, active access to all topics. */
export function hasActiveSubscription(user?: User | null): boolean {
  if (!user) return false
  if (user.role === 'ADMIN' || user.bypassPaywall) return true
  return (
    user.subscriptionStatus === 'MONTHLY' ||
    user.subscriptionStatus === 'ANNUAL' ||
    user.subscriptionStatus === 'LIFETIME'
  )
}

// â”€â”€ Shared (kept from old system) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface StoryBeat {
  type: 'narration' | 'dialogue' | 'example'; text: string
  av?: string; cls?: string; speaker?: string; sCls?: string
}
export interface TestCaseLabel { label: string }
export interface TestResult { label: string; passed: boolean; actualOutput: string; expectedOutput: string }
export interface Badge { id: string; displayName: string; description: string; glyph: string; category: string; earned: boolean; earnedAt: string | null }
export interface CodeRunResponse { output: string | null; error: string | null; status: 'SUCCESS'|'COMPILE_ERROR'|'RUNTIME_ERROR'|'TIMEOUT'|'ERROR' }

// â”€â”€ Chunks â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface ModuleSummary {
  id: string; title: string; glyph: string; status: string
  totalLessons: number; completedLessons: number
  memoryStrength: number; healthColor: string; prerequisiteIds: string[]
}

export interface LessonSummary {
  id: string; title: string; sortOrder: number; status: string
  currentPhase: string; memoryStrength: number; healthColor: string
  feynmanCompleted: boolean; xpReward: number
  // Sprint 1 â€” chip metadata
  practiceType: string
  learningObjectiveCount: number
  hasChallenge: boolean
  hasMiniProject: boolean
}

export interface ModuleDetail {
  id: string; domainId: string; title: string; glyph: string; status: string
  lessons: LessonSummary[]
}

// â”€â”€ Encoding â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export type EncodingPhase = 'HOOK' | 'EXPLANATION' | 'GUIDED_PRACTICE' | 'SOLO_PRACTICE' | 'RETRIEVAL_CHECK' | 'INTEGRATION' | 'COMPLETE'

export type QuestType = 'KNOWLEDGE' | 'GUIDED' | 'PRACTICE' | 'INVESTIGATION' | 'SYNTHESIS' | 'MASTERY'

export interface StoryRabbitHoleTerm { term: string; description: string }

export interface LessonEncoding {
  lessonId: string; moduleId: string; domainId: string; title: string
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
  // Sprint 1 â€” structured lesson metadata
  learningObjectives: string[] | null
  challenge: { html: string; starterCode: string | null; tests: Record<string, unknown>[] | null } | null
  miniProject: string | null
  commonMistakes: string[] | null
  assessmentCriteria: string[] | null
  // Sprint 7 â€” downloadable resources
  downloadables: Downloadable[] | null
  integrationPrompt: string | null
  questType: QuestType | null
}

export interface Downloadable {
  title: string
  type: 'pdf' | 'md' | 'txt' | string
  url: string
}

export interface PracticeResult {
  allPassed: boolean; testResults: TestResult[]
  xpEarned: number; mentorFeedback: string | null
  errorType?: string | null; newBadges?: Badge[]
}

// â”€â”€ Questions â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
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

// â”€â”€ Retrieval â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface RetrievalResultDto {
  score: number; correct: number; total: number
  results: QuestionResultDto[]; passed: boolean
  xpEarned: number; newBadges: Badge[]
  recommendation: string
}

// â”€â”€ Reviews â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface ReviewSessionDto {
  sessionId: string; sessionType: string
  questions: QuestionDto[]; estimatedMinutes: number
}

export interface ReviewResultDto {
  score: number; correct: number; total: number
  results: QuestionResultDto[]; newBadges: Badge[]
}

// â”€â”€ Diagnostic â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface DiagnosticResultDto {
  recommendedPath: string
  chunkRecommendations: Record<string, string>
  overallScore: number
}

// â”€â”€ Feynman â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface FeynmanResultDto {
  accuracy: number; completeness: number; simplicity: number; connection: number
  overallScore: number; feedback: string; xpEarned: number
}

// â”€â”€ Dashboard â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface ModuleHealthDto {
  moduleId: string; title: string; glyph: string
  status: string; memoryStrength: number; healthColor: string
  totalLessons: number; completedLessons: number
  tier: string
}

export interface DashboardDto {
  totalXp: number; rank: string; streakDays: number; streakAtRisk: boolean
  currentPath: string; diagnosticCompleted: boolean; diagnosticCompletedAt: string | null
  reviewsDue: number; dailyGoalMinutes: number
  overallProgress: number; chunkHealth: ModuleHealthDto[]
}

// â”€â”€ Rabbit Holes â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface RabbitHoleModule {
  id: string; moduleId: string; title: string
  contentHtml: string; storyBeats: StoryBeat[]
  starterCode: string; testCaseLabels: { label: string }[]; filename: string; sortOrder: number
}

// â”€â”€ Curiosity Queue â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface CuriosityQueueItem {
  id: string; userId: string; lessonId: string; savedAt: string
}

// â”€â”€ Rabbit Hole Terms â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
export interface RabbitHoleTerm {
  id: string; term: string; description: string | null
  lessonId: string | null; domainId: string | null; savedAt: string
}
