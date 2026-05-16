import { useQuery, useQueries, useQueryClient } from '@tanstack/react-query'
import { dashboardApi, chunkApi } from '@/shared/api/services'

export const QUERY_KEYS = {
  dashboard: (topicId: string) => ['dashboard', topicId] as const,
  reviewsDue: () => ['dashboard', 'reviews-due'] as const,
  chunkDetail: (chunkId: string) => ['chunk', chunkId] as const,
}

export function useDashboard(topicId: string) {
  return useQuery({
    queryKey: QUERY_KEYS.dashboard(topicId),
    queryFn: () => dashboardApi.get(topicId),
    enabled: !!topicId,
    staleTime: 30_000,
  })
}

export function useReviewsDue() {
  return useQuery({
    queryKey: QUERY_KEYS.reviewsDue(),
    queryFn: () => dashboardApi.getReviewsDue(),
    staleTime: 60_000,
  })
}

export function useChunkDetail(chunkId: string | undefined) {
  return useQuery({
    queryKey: QUERY_KEYS.chunkDetail(chunkId ?? ''),
    queryFn: () => chunkApi.getDetail(chunkId!),
    enabled: !!chunkId,
  })
}

/** Fetch dashboard data for multiple topics in parallel (used by TopicsPage). */
export function useTopicsDashboard(topicIds: string[]) {
  const results = useQueries({
    queries: topicIds.map(id => ({
      queryKey: QUERY_KEYS.dashboard(id),
      queryFn: () => dashboardApi.get(id),
      staleTime: 30_000,
    })),
  })
  return Object.fromEntries(topicIds.map((id, i) => [id, results[i].data]))
}

/** Call after any action that should refresh dashboard counts (XP, badges, reviews). */
export function useInvalidateDashboard() {
  const qc = useQueryClient()
  return (topicId?: string) => {
    if (topicId) {
      qc.invalidateQueries({ queryKey: QUERY_KEYS.dashboard(topicId) })
    } else {
      qc.invalidateQueries({ queryKey: ['dashboard'] })
    }
  }
}
