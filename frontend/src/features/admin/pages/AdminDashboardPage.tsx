import { useEffect, useState } from 'react'
import { AlertTriangle, CheckCircle2, CircleHelp, FileText, Flame, Map, Package, Users } from 'lucide-react'
import { adminStatsApi, type AdminStats } from '@/shared/api/adminServices'
import React from 'react'

interface StatCardProps {
  label: string
  value: number | string
  Icon: React.ElementType
  color: string
}

function StatCard({ label, value, Icon, color }: StatCardProps) {
  return (
    <div style={{
      background: '#16132b',
      border: `1px solid ${color}22`,
      borderTop: `2px solid ${color}`,
      borderRadius: 10,
      padding: '18px 20px',
      display: 'flex',
      alignItems: 'center',
      gap: 14,
    }}>
      <div style={{
        width: 40, height: 40, borderRadius: 10, flexShrink: 0,
        background: `${color}18`,
        display: 'flex', alignItems: 'center', justifyContent: 'center',
      }}>
        <Icon size={20} color={color} strokeWidth={1.75} />
      </div>
      <div>
        <div style={{ fontSize: 22, fontWeight: 700, color, fontFamily: 'Cinzel, serif' }}>{value}</div>
        <div style={{ fontSize: 11, color: '#8b7fa0', marginTop: 2 }}>{label}</div>
      </div>
    </div>
  )
}

export default function AdminDashboardPage() {
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

  return (
    <div>
      <h1 style={{ fontFamily: 'Cinzel, serif', fontSize: 22, color: '#c9a227', marginBottom: 6 }}>
        Dashboard
      </h1>
      <p style={{ color: '#8b7fa0', fontSize: 13, marginBottom: 28 }}>Platform health at a glance</p>

      {/* Stat cards */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(190px, 1fr))', gap: 16, marginBottom: 36 }}>
        <StatCard label="Total Users"    value={stats.totalUsers}     Icon={Users}        color="#8b5cf6" />
        <StatCard label="Active (7d)"    value={stats.activeUsers7d}  Icon={Flame}        color="#fb923c" />
        <StatCard label="Topics"         value={stats.totalTopics}    Icon={Map}          color="#2dd4bf" />
        <StatCard label="Modules"         value={stats.totalChunks}    Icon={Package}      color="#c9a227" />
        <StatCard label="Lessons"        value={stats.totalSubChunks} Icon={FileText}     color="#8b5cf6" />
        <StatCard label="Questions"      value={stats.totalQuestions} Icon={CircleHelp}   color="#4ade80" />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">

        {/* Recent signups */}
        <div style={{ background: '#16132b', border: '1px solid #2e2850', borderRadius: 10, padding: 20 }}>
          <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 14, color: '#c4b5fd', marginBottom: 14 }}>
            Recent Sign-ups
          </h2>
          {stats.recentSignups.length === 0 ? (
            <p style={{ color: '#8b7fa0', fontSize: 13 }}>No recent sign-ups.</p>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
              {stats.recentSignups.map(u => (
                <div key={u.id} style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', fontSize: 12 }}>
                  <div>
                    <span style={{ color: '#e8e0f0', fontWeight: 600 }}>{u.username}</span>
                    <span style={{ color: '#8b7fa0', marginLeft: 8 }}>{u.email}</span>
                  </div>
                  <span style={{ color: '#8b7fa0' }}>{new Date(u.createdAt).toLocaleDateString()}</span>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Content health */}
        <div style={{ background: '#16132b', border: '1px solid #2e2850', borderRadius: 10, padding: 20 }}>
          <h2 style={{ fontFamily: 'Cinzel, serif', fontSize: 14, color: '#c4b5fd', marginBottom: 14 }}>
            Content Health
          </h2>
          {stats.contentHealth.length === 0 ? (
            <div style={{ display: 'flex', alignItems: 'center', gap: 8, color: '#4ade80', fontSize: 13 }}>
              <CheckCircle2 size={16} color="#4ade80" strokeWidth={1.75} />
              All lessons look healthy
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
              {stats.contentHealth.map(item => (
                <div key={item.subChunkId} style={{
                  background: 'rgba(248,113,113,.07)', border: '1px solid rgba(248,113,113,.25)',
                  borderRadius: 7, padding: '8px 12px',
                }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: 5, marginBottom: 2 }}>
                    <AlertTriangle size={11} color="#f87171" />
                    <span style={{ fontSize: 12, color: '#fca5a5', fontWeight: 600 }}>{item.title}</span>
                  </div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: 6, marginBottom: 4 }}>
                    <span style={{ fontSize: 11, color: '#8b7fa0' }}>{item.chunkTitle}</span>
                    {item.topicId && (
                      <span style={{
                        fontSize: 10, padding: '1px 6px', borderRadius: 4,
                        background: 'rgba(139,92,246,.18)', color: '#c4b5fd', fontWeight: 600,
                      }}>{item.topicId}</span>
                    )}
                    {item.tier && (
                      <span style={{
                        fontSize: 10, padding: '1px 6px', borderRadius: 4,
                        background: 'rgba(201,162,39,.15)', color: '#c9a227', fontWeight: 600,
                      }}>{item.tier}</span>
                    )}
                  </div>
                  {item.issues.map((issue, i) => (
                    <div key={i} style={{ fontSize: 11, color: '#f87171' }}>• {issue}</div>
                  ))}
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
