import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { AlertTriangle, CheckCircle2, CircleHelp, FileText, Flag, Flame, Map, Package, StickyNote, Trophy, Users } from 'lucide-react'
import { adminStatsApi, type AdminStats, type DailyCount, type DomainEngagementItem, type XpBucket } from '@/shared/api/adminServices'
import React from 'react'

// â”€â”€ Palette helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

const RANK_ORDER = ['Novice', 'Apprentice', 'Adept', 'Mage', 'Archmage', 'Magus', 'Lord Magus']
const RANK_COLOR: Record<string, string> = {
  Novice: '#6b7280', Apprentice: '#8b5cf6', Adept: '#3b82f6',
  Mage: '#2dd4bf', Archmage: '#c9a227', Magus: '#fb923c', 'Lord Magus': '#f87171',
}
const SUB_COLOR: Record<string, string> = {
  FREE: '#6b7280', MONTHLY: '#3b82f6', ANNUAL: '#8b5cf6', LIFETIME: '#c9a227', CANCELLED: '#f87171',
}
const SUB_LABEL: Record<string, string> = {
  FREE: 'Free', MONTHLY: 'Monthly', ANNUAL: 'Annual', LIFETIME: 'Lifetime', CANCELLED: 'Cancelled',
}

// â”€â”€ Sub-components â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

function StatCard({ label, value, Icon, color }: { label: string; value: number | string; Icon: React.ElementType; color: string }) {
  return (
    <div style={{
      background: '#16132b', border: `1px solid ${color}22`,
      borderTop: `2px solid ${color}`, borderRadius: 10,
      padding: '14px 16px', display: 'flex', alignItems: 'center', gap: 12,
    }}>
      <div style={{
        width: 36, height: 36, borderRadius: 8, flexShrink: 0,
        background: `${color}18`,
        display: 'flex', alignItems: 'center', justifyContent: 'center',
      }}>
        <Icon size={18} color={color} strokeWidth={1.75} />
      </div>
      <div style={{ minWidth: 0 }}>
        <div style={{ fontSize: 20, fontWeight: 700, color, fontFamily: 'Cinzel, serif', lineHeight: 1.1 }}>{value}</div>
        <div style={{ fontSize: 11, color: '#8b7fa0', marginTop: 2, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{label}</div>
      </div>
    </div>
  )
}

function Panel({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div style={{ background: '#16132b', border: '1px solid #2e2850', borderRadius: 10, padding: '16px 18px' }}>
      <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 13, color: '#c4b5fd', marginBottom: 14 }}>{title}</h2>
      {children}
    </div>
  )
}

function SignupTrend({ data }: { data: DailyCount[] }) {
  const max = Math.max(...data.map(d => d.count), 1)
  const BAR_H = 56

  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'flex-end', gap: 3, height: BAR_H + 20, paddingBottom: 20, position: 'relative' }}>
        {data.map((d, i) => {
          const barH = Math.max(4, Math.round((d.count / max) * BAR_H))
          const isWeekend = [0, 6].includes(new Date(d.date + 'T12:00:00Z').getDay())
          return (
            <div
              key={d.date}
              title={`${d.date}: ${d.count} sign-up${d.count !== 1 ? 's' : ''}`}
              style={{
                flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center',
                justifyContent: 'flex-end', height: '100%', cursor: 'default',
              }}
            >
              <div style={{
                width: '100%', height: barH,
                background: isWeekend ? '#4c1d95' : '#8b5cf6',
                borderRadius: '3px 3px 0 0',
                opacity: d.count === 0 ? 0.3 : 1,
                transition: 'opacity .15s',
              }} />
              {/* Date label: show every 7th to avoid clutter on mobile */}
              {(i === 0 || i === 6 || i === 13) && (
                <div style={{
                  position: 'absolute', bottom: 0,
                  fontSize: 9, color: '#8b7fa0', whiteSpace: 'nowrap',
                  transform: 'translateX(-50%)',
                  left: `${((i + 0.5) / data.length) * 100}%`,
                }}>
                  {d.date.slice(5)}
                </div>
              )}
            </div>
          )
        })}
      </div>
      <div style={{ fontSize: 11, color: '#8b7fa0', marginTop: 2 }}>
        Total past 14 days: <span style={{ color: '#c4b5fd', fontWeight: 600 }}>{data.reduce((s, d) => s + d.count, 0)}</span>
      </div>
    </div>
  )
}

function XpDistribution({ data }: { data: XpBucket[] }) {
  const sorted = [...data].sort((a, b) => RANK_ORDER.indexOf(a.rank) - RANK_ORDER.indexOf(b.rank))
  const max = Math.max(...sorted.map(d => d.count), 1)
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
      {sorted.map(({ rank, count }) => {
        const color = RANK_COLOR[rank] ?? '#8b5cf6'
        const pct = Math.max(4, (count / max) * 100)
        return (
          <div key={rank} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <div style={{ width: 80, fontSize: 11, color: '#c4b5fd', textAlign: 'right', flexShrink: 0 }}>{rank}</div>
            <div style={{ flex: 1, background: '#1e1a35', borderRadius: 4, height: 14, overflow: 'hidden' }}>
              <div style={{ width: `${pct}%`, height: '100%', background: color, borderRadius: 4, transition: 'width .4s ease' }} />
            </div>
            <div style={{ width: 32, fontSize: 11, color, fontWeight: 600, textAlign: 'right', flexShrink: 0 }}>{count}</div>
          </div>
        )
      })}
    </div>
  )
}

function DomainEngagement({ data }: { data: DomainEngagementItem[] }) {
  return (
    <div style={{ overflowX: 'auto', WebkitOverflowScrolling: 'touch' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 12, minWidth: 420 }}>
        <thead>
          <tr>
            {['Topic', 'Lessons', 'Completions', 'Learners', 'Rate'].map(h => (
              <th key={h} style={{
                textAlign: h === 'Topic' ? 'left' : 'right', padding: '6px 8px',
                color: '#8b7fa0', fontWeight: 600, fontSize: 11, borderBottom: '1px solid #2e2850',
                fontFamily: 'Cinzel, serif', whiteSpace: 'nowrap',
              }}>{h}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map(row => {
            const rate = row.totalLessons > 0
              ? Math.round((row.totalCompletions / (row.totalLessons * Math.max(row.uniqueLearners, 1))) * 100)
              : 0
            const cappedRate = Math.min(rate, 100)
            return (
              <tr key={row.domainId} style={{ borderBottom: '1px solid #1e1a35' }}>
                <td style={{ padding: '8px 8px', color: '#e8e0f0' }}>
                  <span style={{ marginRight: 6 }}>{row.glyph}</span>{row.domainName}
                </td>
                <td style={{ padding: '8px 8px', color: '#c4b5fd', textAlign: 'right' }}>{row.totalLessons}</td>
                <td style={{ padding: '8px 8px', color: '#4ade80', textAlign: 'right' }}>{row.totalCompletions}</td>
                <td style={{ padding: '8px 8px', color: '#fb923c', textAlign: 'right' }}>{row.uniqueLearners}</td>
                <td style={{ padding: '8px 8px', textAlign: 'right' }}>
                  <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end', gap: 6 }}>
                    <div style={{ width: 48, background: '#1e1a35', borderRadius: 3, height: 6, overflow: 'hidden' }}>
                      <div style={{ width: `${cappedRate}%`, height: '100%', background: '#8b5cf6', borderRadius: 3 }} />
                    </div>
                    <span style={{ color: cappedRate > 50 ? '#8b5cf6' : '#6b7280', width: 30, textAlign: 'right' }}>
                      {cappedRate}%
                    </span>
                  </div>
                </td>
              </tr>
            )
          })}
        </tbody>
      </table>
    </div>
  )
}

// â”€â”€ Main page â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

export default function AdminDashboardPage() {
  const navigate = useNavigate()
  const [stats, setStats] = useState<AdminStats | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    adminStatsApi.get()
      .then(setStats)
      .catch(() => setError('Failed to load stats'))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <div style={{ color: '#8b7fa0', fontSize: 14 }}>Loading...</div>
  if (error || !stats) return <div style={{ color: '#f87171', fontSize: 14 }}>{error ?? 'Error'}</div>

  const subBreakdown = stats.subscriptionBreakdown ?? {}
  const subOrder = ['FREE', 'MONTHLY', 'ANNUAL', 'LIFETIME', 'CANCELLED']

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>

      {/* Header */}
      <div>
        <h1 style={{ fontFamily: 'Cinzel, serif', fontSize: 22, color: '#c9a227', marginBottom: 4 }}>Dashboard</h1>
        <p style={{ color: '#8b7fa0', fontSize: 13 }}>Platform health at a glance</p>
      </div>

      {/* Alert row — only shown when action is needed */}
      {(stats.openStuckReports > 0 || stats.pendingCapstones > 0) && (
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 10 }}>
          {stats.openStuckReports > 0 && (
            <button
              onClick={() => navigate('/admin/stuck-reports')}
              style={{
                display: 'flex', alignItems: 'center', gap: 8,
                background: 'rgba(251,146,60,.1)', border: '1px solid rgba(251,146,60,.35)',
                borderRadius: 8, padding: '8px 14px', cursor: 'pointer',
                color: '#fb923c', fontSize: 13, fontFamily: 'Cinzel, serif',
              }}
            >
              <Flag size={14} strokeWidth={2} />
              {stats.openStuckReports} open stuck report{stats.openStuckReports !== 1 ? 's' : ''}
            </button>
          )}
          {stats.pendingCapstones > 0 && (
            <button
              onClick={() => navigate('/admin/capstones')}
              style={{
                display: 'flex', alignItems: 'center', gap: 8,
                background: 'rgba(139,92,246,.1)', border: '1px solid rgba(139,92,246,.35)',
                borderRadius: 8, padding: '8px 14px', cursor: 'pointer',
                color: '#c4b5fd', fontSize: 13, fontFamily: 'Cinzel, serif',
              }}
            >
              <Trophy size={14} strokeWidth={2} />
              {stats.pendingCapstones} capstone{stats.pendingCapstones !== 1 ? 's' : ''} awaiting review
            </button>
          )}
        </div>
      )}

      {/* Stat cards */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(155px, 1fr))', gap: 12 }}>
        <StatCard label="Total Users"   value={stats.totalUsers}     Icon={Users}      color="#8b5cf6" />
        <StatCard label="Active (7d)"   value={stats.activeUsers7d}  Icon={Flame}      color="#fb923c" />
        <StatCard label="Pathways"      value={stats.totalDomains}   Icon={Map}        color="#2dd4bf" />
        <StatCard label="Modules"       value={stats.totalChunks}    Icon={Package}    color="#c9a227" />
        <StatCard label="Lessons"       value={stats.totalLessons} Icon={FileText}   color="#8b5cf6" />
        <StatCard label="Questions"     value={stats.totalQuestions} Icon={CircleHelp} color="#4ade80" />
        <StatCard label="Notes"         value={stats.totalNotes ?? 0}     Icon={StickyNote} color="#2dd4bf" />
        <StatCard label="Capstones"     value={stats.totalCapstones ?? 0} Icon={Trophy}     color="#c9a227" />
      </div>

      {/* Subscription breakdown */}
      {Object.keys(subBreakdown).length > 0 && (
        <div style={{ background: '#16132b', border: '1px solid #2e2850', borderRadius: 10, padding: '14px 18px' }}>
          <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 13, color: '#c4b5fd', marginBottom: 12 }}>Subscriptions</h2>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }}>
            {subOrder.filter(k => subBreakdown[k] != null).map(key => {
              const color = SUB_COLOR[key] ?? '#8b7fa0'
              const count = subBreakdown[key]
              return (
                <div key={key} style={{
                  display: 'flex', alignItems: 'center', gap: 7,
                  background: `${color}14`, border: `1px solid ${color}33`,
                  borderRadius: 20, padding: '5px 12px',
                }}>
                  <div style={{ width: 7, height: 7, borderRadius: '50%', background: color, flexShrink: 0 }} />
                  <span style={{ fontSize: 12, color: '#c4b5fd' }}>{SUB_LABEL[key] ?? key}</span>
                  <span style={{ fontSize: 13, fontWeight: 700, color, fontFamily: 'Cinzel, serif' }}>{count}</span>
                </div>
              )
            })}
          </div>
        </div>
      )}

      {/* Signup trend + XP distribution */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Panel title="Sign-ups — Last 14 Days">
          {stats.signupTrend?.length ? (
            <SignupTrend data={stats.signupTrend} />
          ) : (
            <p style={{ color: '#8b7fa0', fontSize: 13 }}>No data.</p>
          )}
        </Panel>
        <Panel title="XP Distribution by Rank">
          {stats.xpDistribution?.length ? (
            <XpDistribution data={stats.xpDistribution} />
          ) : (
            <p style={{ color: '#8b7fa0', fontSize: 13 }}>No data.</p>
          )}
        </Panel>
      </div>

      {/* Topic engagement */}
      <Panel title="Topic Engagement">
        {stats.domainEngagement?.length ? (
          <DomainEngagement data={stats.domainEngagement} />
        ) : (
          <p style={{ color: '#8b7fa0', fontSize: 13 }}>No topics found.</p>
        )}
      </Panel>

      {/* Recent signups + Content health */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">

        <Panel title="Recent Sign-ups">
          {stats.recentSignups.length === 0 ? (
            <p style={{ color: '#8b7fa0', fontSize: 13 }}>No recent sign-ups.</p>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
              {stats.recentSignups.map(u => (
                <div key={u.id} style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', fontSize: 12, gap: 8 }}>
                  <div style={{ minWidth: 0 }}>
                    <span style={{ color: '#e8e0f0', fontWeight: 600 }}>{u.username}</span>
                    <span style={{ color: '#8b7fa0', marginLeft: 8, display: 'inline-block', overflow: 'hidden', textOverflow: 'ellipsis', maxWidth: 120, verticalAlign: 'bottom' }}>{u.email}</span>
                  </div>
                  <span style={{ color: '#8b7fa0', flexShrink: 0 }}>{new Date(u.createdAt).toLocaleDateString()}</span>
                </div>
              ))}
            </div>
          )}
        </Panel>

        <Panel title="Content Health">
          {stats.contentHealth.length === 0 ? (
            <div style={{ display: 'flex', alignItems: 'center', gap: 8, color: '#4ade80', fontSize: 13 }}>
              <CheckCircle2 size={16} color="#4ade80" strokeWidth={1.75} />
              All lessons look healthy
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 10, maxHeight: 320, overflowY: 'auto' }}>
              {stats.contentHealth.map(item => (
                <div key={item.lessonId} style={{
                  background: 'rgba(248,113,113,.07)', border: '1px solid rgba(248,113,113,.25)',
                  borderRadius: 7, padding: '8px 12px',
                }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: 5, marginBottom: 2 }}>
                    <AlertTriangle size={11} color="#f87171" />
                    <span style={{ fontSize: 12, color: '#fca5a5', fontWeight: 600 }}>{item.title}</span>
                  </div>
                  <div style={{ display: 'flex', flexWrap: 'wrap', alignItems: 'center', gap: 6, marginBottom: 4 }}>
                    <span style={{ fontSize: 11, color: '#8b7fa0' }}>{item.chunkTitle}</span>
                    {item.domainId && (
                      <span style={{ fontSize: 10, padding: '1px 6px', borderRadius: 4, background: 'rgba(139,92,246,.18)', color: '#c4b5fd', fontWeight: 600 }}>{item.domainId}</span>
                    )}
                    {item.tier && (
                      <span style={{ fontSize: 10, padding: '1px 6px', borderRadius: 4, background: 'rgba(201,162,39,.15)', color: '#c9a227', fontWeight: 600 }}>{item.tier}</span>
                    )}
                  </div>
                  {item.issues.map((issue, i) => (
                    <div key={i} style={{ fontSize: 11, color: '#f87171' }}>• {issue}</div>
                  ))}
                </div>
              ))}
            </div>
          )}
        </Panel>

      </div>
    </div>
  )
}
