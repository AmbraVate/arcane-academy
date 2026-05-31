import { useGraph } from '@/hooks/queries'
import { CurriculumGraph } from '../components/CurriculumGraph'
import { Network } from 'lucide-react'

export default function KnowledgeMapPage() {
  const { data, isLoading, isError, error } = useGraph()

  if (isLoading) {
    return (
      <div className="flex flex-col items-center justify-center h-[60vh] gap-4 text-muted">
        <div className="w-10 h-10 animate-spin rounded-full border-2 border-border border-t-purple" />
        <p className="font-cinzel text-sm tracking-wide">Conjuring knowledge map…</p>
      </div>
    )
  }

  if (isError || !data) {
    return (
      <div className="flex flex-col items-center justify-center h-[60vh] gap-3 text-muted">
        <Network size={32} className="opacity-40" />
        <p className="text-sm">
          {isError && error instanceof Error ? error.message : 'Could not load the knowledge map.'}
        </p>
      </div>
    )
  }

  return (
    <div className="flex flex-col h-[calc(100vh-50px)]">
      <div className="flex-shrink-0 px-4 py-3 border-b border-border flex items-center gap-3">
        <Network size={18} className="text-purple" />
        <div>
          <h1 className="font-cinzel text-[15px] text-gold tracking-wide">Knowledge Map</h1>
          <p className="text-[11px] text-muted mt-0.5">
            {data.nodes.filter(n => n.type === 'DOMAIN').length} domains ·{' '}
            {data.nodes.filter(n => n.type === 'MODULE').length} modules ·{' '}
            {data.nodes.filter(n => n.type === 'LESSON').length} lessons
          </p>
        </div>
      </div>

      <div className="flex-1 min-h-0">
        <CurriculumGraph nodes={data.nodes} edges={data.edges} />
      </div>
    </div>
  )
}
