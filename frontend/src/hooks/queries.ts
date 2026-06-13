import { useQuery, useQueries, useQueryClient } from '@tanstack/react-query'
import { dashboardApi, moduleApi } from '@/shared/api/services'

export const QUERY_KEYS = {
  dashboard: (domainId: string) => ['dashboard', domainId] as const,
  reviewsDue: () => ['dashboard', 'reviews-due'] as const,
  moduleDetail: (moduleId: string) => ['module', moduleId] as const,
}

/** Fetch dashboard for a domain.
 *  Pass `isPublic = true` when no user is logged in - calls the public endpoint
 *  which returns structure only (all modules LOCKED, no progress). */
export function useDashboard(domainId: string, isPublic = false) {
  return useQuery({
    queryKey: isPublic ? ['dashboard', 'public', domainId] : QUERY_KEYS.dashboard(domainId),
    queryFn: () => isPublic ? dashboardApi.getPublic(domainId) : dashboardApi.get(domainId),
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
    queryKey: QUERY_KEYS.moduleDetail(moduleId ?? ''),
    queryFn: () => moduleApi.getDetail(moduleId!),
    enabled: !!moduleId,
  })
}

/** Fetch dashboard data for multiple domains in parallel (used by DomainsPage).
 *  Pass `enabled = false` when the user is not authenticated to skip all fetches. */
export function useDomainsDashboard(domainIds: string[], enabled = true) {
  const results = useQueries({
    queries: domainIds.map(id => ({
      queryKey: QUERY_KEYS.dashboard(id),
      queryFn: () => dashboardApi.get(id),
      staleTime: 30_000,
      enabled: enabled && !!id,
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
