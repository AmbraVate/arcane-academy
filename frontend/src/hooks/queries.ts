import { useQuery, useQueries, useQueryClient } from '@tanstack/react-query'
import { dashboardApi, moduleApi } from '@/shared/api/services'

export const QUERY_KEYS = {
  dashboard: (domainId: string) => ['dashboard', domainId] as const,
  reviewsDue: () => ['dashboard', 'reviews-due'] as const,
  chunkDetail: (moduleId: string) => ['chunk', moduleId] as const,
}

export function useDashboard(domainId: string) {
  return useQuery({
    queryKey: QUERY_KEYS.dashboard(domainId),
    queryFn: () => dashboardApi.get(domainId),
    enabled: !!domainId,
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

export function useModuleDetail(moduleId: string | undefined) {
  return useQuery({
    queryKey: QUERY_KEYS.chunkDetail(moduleId ?? ''),
    queryFn: () => moduleApi.getDetail(moduleId!),
    enabled: !!moduleId,
  })
}

/** Fetch dashboard data for multiple topics in parallel (used by DomainsPage). */
export function useTopicsDashboard(domainIds: string[]) {
  const results = useQueries({
    queries: domainIds.map(id => ({
      queryKey: QUERY_KEYS.dashboard(id),
      queryFn: () => dashboardApi.get(id),
      staleTime: 30_000,
    })),
  })
  return Object.fromEntries(domainIds.map((id, i) => [id, results[i].data]))
}

/** Call after any action that should refresh dashboard counts (XP, badges, reviews). */
export function useInvalidateDashboard() {
  const qc = useQueryClient()
  return (domainId?: string) => {
    if (domainId) {
      qc.invalidateQueries({ queryKey: QUERY_KEYS.dashboard(domainId) })
    } else {
      qc.invalidateQueries({ queryKey: ['dashboard'] })
    }
  }
}
