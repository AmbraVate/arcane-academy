export interface User {
  userId: string; username: string; totalXp: number; rank: string; streakDays: number; token: string
}
export interface QuestSummary {
  id: string; title: string; eyebrow: string; topic: string
  chapterNumber: number; orderInChapter: number; xpReward: number
  completed: boolean; locked: boolean
}
export interface StoryBeat {
  type: 'narration' | 'dialogue' | 'example'; text: string
  av?: string; cls?: string; speaker?: string; sCls?: string
}
export interface TestCaseLabel { label: string }
export interface QuestDetail {
  id: string; title: string; eyebrow: string; topic: string; xpReward: number
  filename: string; problemHtml: string; hint: string; starterCode: string
  winStory: string; story: StoryBeat[]; testCaseLabels: TestCaseLabel[]
  completed: boolean
}
export interface TestResult { label: string; passed: boolean; actualOutput: string; expectedOutput: string }
export interface SubmitResponse { allPassed: boolean; testResults: TestResult[]; xpEarned: number; mentorFeedback: string | null; errorType?: string | null }
export interface CodeRunResponse { output: string | null; error: string | null; status: 'SUCCESS'|'COMPILE_ERROR'|'RUNTIME_ERROR'|'TIMEOUT'|'ERROR' }
export type BossQuestionType = 'multiple_choice' | 'be_the_compiler' | 'fill_blank'
export interface BossQuestion { id: string; type: BossQuestionType; question: string; code?: string; options?: string[] }
export interface BossData { id: string; name: string; glyph: string; chapterNumber: number; xpReward: number; intro: string; questions: BossQuestion[]; completed: boolean; locked: boolean }
export interface BossAnswerResponse { correct: boolean; explanation: string; correctAnswer: string }
export interface Chapter { number: number; title: string; subtitle: string; icon: string; quests: QuestSummary[]; boss: BossData | null; status: 'done'|'active'|'locked' }
