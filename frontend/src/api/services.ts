import api from './client'
import type { QuestSummary, QuestDetail, SubmitResponse, CodeRunResponse, User, BossData, BossAnswerResponse } from '../types'

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

export const questApi = {
  getAll: async (): Promise<QuestSummary[]> => {
    const { data } = await api.get('/api/quests')
    return data
  },
  getDetail: async (id: string): Promise<QuestDetail> => {
    const { data } = await api.get(`/api/quests/${id}`)
    return data
  },
}

export const bossApi = {
  getAll: async (): Promise<BossData[]> => {
    const { data } = await api.get('/api/bosses')
    return data
  },
  getById: async (id: string): Promise<BossData> => {
    const { data } = await api.get(`/api/bosses/${id}`)
    return data
  },
  checkAnswer: async (bossId: string, questionId: string, answer: string): Promise<BossAnswerResponse> => {
    const { data } = await api.post(`/api/bosses/${bossId}/answer`, { questionId, answer })
    return data
  },
  defeat: async (bossId: string): Promise<{ xpEarned: number; totalXp: number; rank: string }> => {
    const { data } = await api.post(`/api/bosses/${bossId}/defeat`)
    return data
  },
}

export const codeApi = {
  run: async (code: string, testInput?: string): Promise<CodeRunResponse> => {
    const { data } = await api.post('/api/code/run', { code, testInput })
    return data
  },
  submit: async (questId: string, code: string): Promise<SubmitResponse> => {
    const { data } = await api.post(`/api/code/submit/${questId}`, { code })
    return data
  },
}
