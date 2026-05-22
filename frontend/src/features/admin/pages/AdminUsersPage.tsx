import { useEffect, useState, useCallback } from 'react'
import { adminUserApi, type AdminUser, type UserStats } from '@/shared/api/adminServices'
import { useAuth } from '@/shared/hooks/useAuth'
import {
  Search, X, ChevronLeft, ChevronRight, Shield, ShieldOff,
  UserCheck, UserX, RotateCcw, BarChart2, Loader2,
  Star, Zap, Flame, BookOpen, Trophy, Calendar, Clock, Unlock, Lock,
} from 'lucide-react'
import React from 'react'

const s: Record<string, React.CSSProperties> = {
  card: { background: '#16132b', border: '1px solid #2e2850', borderRadius: 10 },
  label: { fontSize: 10, fontFamily: 'Cinzel, serif', color: '#8b7fa0', textTransform: 'uppercase' as const, letterSpacing: 1 },
  value: { fontSize: 13, color: '#e8e0f0' },
}

const inputStyle: React.CSSProperties = {
  background: '#16132b', border: '1px solid #2e2850', borderRadius: 6,
  color: '#e8e0f0', fontSize: 13, padding: '7px 10px', outline: 'none', width: '100%',
}

const ROLE_COLOR: Record<string, string> = { ADMIN: '#c9a227', USER: '#8b7fa0' }

function StatMini({ icon: Icon, label, value, color = '#8b5cf6' }: {
  icon: React.ElementType; label: string; value: string | number; color?: string
}) {
  return (
    <div style={{ background: '#0f0d1e', border: `1px solid ${color}22`, borderRadius: 8, padding: '10px 12px' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 6, marginBottom: 4 }}>
        <Icon size={12} color={color} />
        <span style={{ ...s.label, letterSpacing: 0.5 }}>{label}</span>
      </div>
      <div style={{ fontSize: 18, fontWeight: 700, color, fontFamily: 'Cinzel, serif' }}>{value}</div>
    </div>
  )
}

function UserDetailPanel({ user, onClose, onUpdate }: {
  user: AdminUser
  onClose: () => void
  onUpdate: (updated: AdminUser) => void
}) {
  const { user: caller } = useAuth()
  const [stats, setStats] = useState<UserStats | null>(null)
  const [statsLoading, setStatsLoading] = useState(true)
  const [blocking, setBlocking] = useState(false)
  const [roleSaving, setRoleSaving] = useState(false)
  const [resetting, setResetting] = useState(false)
  const [bypassSaving, setBypassSaving] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const isSelf = caller?.userId === user.id

  useEffect(() => {
    setStats(null)
    setStatsLoading(true)
    setError(null)
    adminUserApi.getStats(user.id)
      .then(setStats)
      .catch(() => setError('Failed to load stats'))
      .finally(() => setStatsLoading(false))
  }, [user.id])

  const handleBlock = async () => {
    if (isSelf) return
    setBlocking(true)
    try {
      const updated = await adminUserApi.setBlocked(user.id, !user.blocked)
      onUpdate(updated)
    } catch {
      setError('Failed to update block status')
    } finally {
      setBlocking(false)
    }
  }

  const handleRoleChange = async (newRole: string) => {
    if (isSelf || newRole === user.role) return
    setRoleSaving(true)
    try {
      const updated = await adminUserApi.setRole(user.id, newRole)
      onUpdate(updated)
    } catch {
      setError('Failed to change role')
    } finally {
      setRoleSaving(false)
    }
  }

  const handleBypassPaywall = async () => {
    setBypassSaving(true)
    try {
      const updated = await adminUserApi.setBypassPaywall(user.id, !user.bypassPaywall)
      onUpdate(updated)
    } catch {
      setError('Failed to update paywall bypass')
    } finally {
      setBypassSaving(false)
    }
  }

  const handleReset = async () => {
    if (!confirm(`Reset ALL learning progress for ${user.username}? This cannot be undone.`)) return
    setResetting(true)
    try {
      await adminUserApi.resetProgress(user.id)
      setStats(s => s ? { ...s, subChunksCompleted: 0, chunksCompleted: 0, reviewSessionsCompleted: 0 } : s)
    } catch {
      setError('Failed to reset progress')
    } finally {
      setResetting(false)
    }
  }

  return (
    <div style={{ ...s.card, width: 280, flexShrink: 0, padding: 20, alignSelf: 'flex-start', position: 'sticky', top: 0 }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 16 }}>
        <div>
          <div style={{ fontFamily: 'Cinzel, serif', fontSize: 14, color: '#c4b5fd', fontWeight: 700 }}>{user.username}</div>
          <div style={{ fontSize: 11, color: '#8b7fa0', marginTop: 2 }}>{user.email}</div>
        </div>
        <button style={{ background: 'transparent', border: 'none', color: '#8b7fa0', cursor: 'pointer' }} onClick={onClose}>
          <X size={16} />
        </button>
      </div>
      <div style={{ display: 'flex', gap: 6, marginBottom: 16, flexWrap: 'wrap' }}>
        <span style={{ fontSize: 10, fontFamily: 'Cinzel, serif', padding: '3px 8px', borderRadius: 5, background: ROLE_COLOR[user.role] + '22', border: `1px solid ${ROLE_COLOR[user.role]}44`, color: ROLE_COLOR[user.role] }}>{user.role}</span>
        {user.blocked && <span style={{ fontSize: 10, fontFamily: 'Cinzel, serif', padding: '3px 8px', borderRadius: 5, background: 'rgba(248,113,113,.12)', border: '1px solid rgba(248,113,113,.3)', color: '#f87171' }}>BLOCKED</span>}
        {user.bypassPaywall && <span style={{ fontSize: 10, fontFamily: 'Cinzel, serif', padding: '3px 8px', borderRadius: 5, background: 'rgba(201,162,39,.12)', border: '1px solid rgba(201,162,39,.3)', color: '#c9a227' }}>BYPASS</span>}
        <span style={{ fontSize: 10, padding: '3px 8px', borderRadius: 5, background: 'rgba(139,92,246,.1)', border: '1px solid rgba(139,92,246,.25)', color: '#c4b5fd' }}>{user.authProvider}</span>
      </div>
      {statsLoading ? (
        <div style={{ display: 'flex', alignItems: 'center', gap: 6, color: '#8b7fa0', fontSize: 12, margin: '8px 0 16px' }}>
          <Loader2 size={13} className="animate-spin" /> Loading stats…
        </div>
      ) : stats ? (
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 8, marginBottom: 16 }}>
          <StatMini icon={Zap}      label="XP"       value={stats.totalXp.toLocaleString()} color="#c9a227" />
          <StatMini icon={Star}     label="Rank"     value={stats.rank}                     color="#8b5cf6" />
          <StatMini icon={Flame}    label="Streak"   value={`${stats.streakDays}d`}          color="#fb923c" />
          <StatMini icon={Trophy}   label="Badges"   value={stats.badgesEarned}             color="#4ade80" />
          <StatMini icon={BookOpen} label="Concepts" value={stats.subChunksCompleted}       color="#2dd4bf" />
          <StatMini icon={BarChart2}label="Reviews"  value={stats.reviewSessionsCompleted}  color="#60a5fa" />
        </div>
      ) : null}
      <div style={{ display: 'flex', flexDirection: 'column', gap: 6, marginBottom: 16 }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <span style={{ display: 'flex', alignItems: 'center', gap: 5, ...s.label }}><Calendar size={10} /> Joined</span>
          <span style={{ fontSize: 11, color: '#c4b5fd' }}>{new Date(user.createdAt).toLocaleDateString()}</span>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <span style={{ display: 'flex', alignItems: 'center', gap: 5, ...s.label }}><Clock size={10} /> Last login</span>
          <span style={{ fontSize: 11, color: '#8b7fa0' }}>{user.lastLoginAt ? new Date(user.lastLoginAt).toLocaleDateString() : 'Never'}</span>
        </div>
      </div>
      {error && <div style={{ fontSize: 11, color: '#f87171', background: 'rgba(248,113,113,.1)', border: '1px solid rgba(248,113,113,.25)', borderRadius: 6, padding: '6px 10px', marginBottom: 12 }}>{error}</div>}
      <div style={{ display: 'flex', flexDirection: 'column', gap: 8, paddingTop: 12, borderTop: '1px solid #2e2850' }}>
        <div>
          <div style={{ ...s.label, marginBottom: 5 }}>Role</div>
          <div style={{ display: 'flex', gap: 6 }}>
            {(['USER', 'ADMIN'] as const).map(role => (
              <button key={role} disabled={isSelf || roleSaving || user.role === role} onClick={() => handleRoleChange(role)}
                style={{ flex: 1, fontSize: 11, fontFamily: 'Cinzel, serif', padding: '6px 8px', borderRadius: 6, cursor: isSelf ? 'not-allowed' : 'pointer', background: user.role === role ? ROLE_COLOR[role] + '22' : 'transparent', border: `1px solid ${ROLE_COLOR[role] + (user.role === role ? '66' : '33')}`, color: user.role === role ? ROLE_COLOR[role] : '#8b7fa0', transition: 'all .15s', opacity: isSelf ? 0.5 : 1 }}>
                {roleSaving && user.role !== role ? <Loader2 size={10} /> : role}
              </button>
            ))}
          </div>
        </div>
        <button disabled={isSelf || blocking} onClick={handleBlock}
          style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 6, fontSize: 11, padding: '7px 12px', borderRadius: 6, cursor: isSelf ? 'not-allowed' : 'pointer', background: user.blocked ? 'rgba(74,222,128,.08)' : 'rgba(248,113,113,.08)', border: `1px solid ${user.blocked ? 'rgba(74,222,128,.3)' : 'rgba(248,113,113,.3)'}`, color: user.blocked ? '#4ade80' : '#f87171', transition: 'all .15s', opacity: isSelf ? 0.5 : 1 }}>
          {blocking ? <Loader2 size={13} className="animate-spin" /> : user.blocked ? <UserCheck size={13} /> : <UserX size={13} />}
          {user.blocked ? 'Unblock User' : 'Block User'}
        </button>
        <button disabled={bypassSaving} onClick={handleBypassPaywall}
          style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 6, fontSize: 11, padding: '7px 12px', borderRadius: 6, cursor: 'pointer', background: user.bypassPaywall ? 'rgba(201,162,39,.08)' : 'rgba(201,162,39,.04)', border: `1px solid ${user.bypassPaywall ? 'rgba(201,162,39,.4)' : 'rgba(201,162,39,.2)'}`, color: '#c9a227', transition: 'all .15s' }}>
          {bypassSaving ? <Loader2 size={13} className="animate-spin" /> : user.bypassPaywall ? <Unlock size={13} /> : <Lock size={13} />}
          {user.bypassPaywall ? 'Revoke Paywall Bypass' : 'Grant Paywall Bypass'}
        </button>
        <button disabled={resetting} onClick={handleReset}
          style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 6, fontSize: 11, padding: '7px 12px', borderRadius: 6, cursor: 'pointer', background: 'transparent', border: '1px solid rgba(248,113,113,.2)', color: '#f87171', transition: 'all .15s' }}>
          {resetting ? <Loader2 size={13} className="animate-spin" /> : <RotateCcw size={13} />}
          {resetting ? 'Resetting…' : 'Reset Progress'}
        </button>
      </div>
    </div>
  )
}

export default function AdminUsersPage() {
  const [users, setUsers] = useState<AdminUser[]>([])
  const [total, setTotal] = useState(0)
  const [page, setPage] = useState(0)
  const [search, setSearch] = useState('')
  const [searchInput, setSearchInput] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [selectedUser, setSelectedUser] = useState<AdminUser | null>(null)
  const [filter, setFilter] = useState<'ALL' | 'ADMIN' | 'BLOCKED'>('ALL')

  const PAGE_SIZE = 20

  const load = useCallback(() => {
    setLoading(true)
    adminUserApi.list(page, PAGE_SIZE, search || undefined)
      .then(resp => { setUsers(resp.content); setTotal(resp.totalElements) })
      .catch(() => setError('Failed to load users'))
      .finally(() => setLoading(false))
  }, [page, search])

  useEffect(() => { load() }, [load])

  const handleSearch = () => { setSearch(searchInput); setPage(0) }

  const handleUpdate = (updated: AdminUser) => {
    setUsers(prev => prev.map(u => u.id === updated.id ? updated : u))
    setSelectedUser(updated)
  }

  const totalPages = Math.ceil(total / PAGE_SIZE)

  const visibleUsers = users.filter(u => {
    if (filter === 'ADMIN')   return u.role === 'ADMIN'
    if (filter === 'BLOCKED') return u.blocked
    return true
  })

  return (
    <div>
      {/* Page header */}
      <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', marginBottom: 20 }}>
        <div>
          <h1 style={{ fontFamily: 'Cinzel, serif', fontSize: 22, color: '#c9a227', margin: 0 }}>Users</h1>
          <p style={{ color: '#8b7fa0', fontSize: 13, marginTop: 4, marginBottom: 0 }}>{total.toLocaleString()} registered learners</p>
        </div>
      </div>

      {error && (
        <div style={{ color: '#f87171', fontSize: 13, marginBottom: 12, padding: '8px 12px', background: 'rgba(248,113,113,.1)', borderRadius: 6 }}>
          {error}
        </div>
      )}

      {/* Search + filter row */}
      <div style={{ display: 'flex', gap: 10, marginBottom: 16, alignItems: 'center', flexWrap: 'wrap' }}>
        <div style={{ position: 'relative', flex: 1, maxWidth: 360 }}>
          <Search size={13} color="#8b7fa0" style={{ position: 'absolute', left: 10, top: '50%', transform: 'translateY(-50%)', pointerEvents: 'none' }} />
          <input
            style={{ ...inputStyle, paddingLeft: 32 }}
            placeholder="Search by username or email…"
            value={searchInput}
            onChange={e => setSearchInput(e.target.value)}
            onKeyDown={e => e.key === 'Enter' && handleSearch()}
          />
        </div>
        <button className="btn btn-ghost" style={{ fontSize: 12, padding: '7px 14px' }} onClick={handleSearch}>
          Search
        </button>
        {search && (
          <button className="btn btn-ghost" style={{ fontSize: 12, padding: '7px 10px' }} onClick={() => { setSearch(''); setSearchInput(''); setPage(0) }}>
            <X size={13} />
          </button>
        )}

        {/* Filter chips */}
        <div style={{ display: 'flex', gap: 6, marginLeft: 'auto' }}>
          {(['ALL', 'ADMIN', 'BLOCKED'] as const).map(f => (
            <button
              key={f}
              onClick={() => setFilter(f)}
              style={{
                fontSize: 10, fontFamily: 'Cinzel, serif', padding: '5px 10px', borderRadius: 20, cursor: 'pointer',
                background: filter === f ? 'rgba(139,92,246,.2)' : 'transparent',
                border: `1px solid ${filter === f ? 'rgba(139,92,246,.5)' : '#2e2850'}`,
                color: filter === f ? '#c4b5fd' : '#8b7fa0',
                transition: 'all .15s',
              }}
            >
              {f}
            </button>
          ))}
        </div>
      </div>

      <div style={{ display: 'flex', gap: 16, alignItems: 'flex-start' }}>

        {/* Table */}
        <div style={{ flex: 1, minWidth: 0 }}>
          {loading ? (
            <div style={{ display: 'flex', alignItems: 'center', gap: 8, color: '#8b7fa0', fontSize: 14 }}>
              <Loader2 size={16} className="animate-spin" /> Loading…
            </div>
          ) : (
            <>
              <div style={{ ...s.card, overflow: 'hidden' }}>
                {/* Table header */}
                <div style={{
                  display: 'grid', gridTemplateColumns: '2fr 2fr 80px 80px 90px 28px',
                  padding: '10px 16px', borderBottom: '1px solid #2e2850',
                  fontSize: 10, fontFamily: 'Cinzel, serif', color: '#8b7fa0',
                }}>
                  <span>Username</span>
                  <span>Email</span>
                  <span>Role</span>
                  <span>XP</span>
                  <span>Joined</span>
                  <span />
                </div>

                {visibleUsers.length === 0 && (
                  <div style={{ padding: '20px 16px', color: '#8b7fa0', fontSize: 13 }}>No users found.</div>
                )}

                {visibleUsers.map(u => {
                  const isSelected = selectedUser?.id === u.id
                  return (
                    <div
                      key={u.id}
                      onClick={() => setSelectedUser(isSelected ? null : u)}
                      style={{
                        display: 'grid', gridTemplateColumns: '2fr 2fr 80px 80px 90px 28px',
                        padding: '11px 16px', borderBottom: '1px solid #1e1a35',
                        cursor: 'pointer', transition: 'background .12s',
                        background: isSelected ? 'rgba(139,92,246,.1)' : 'transparent',
                        opacity: u.blocked ? 0.6 : 1,
                      }}
                      onMouseEnter={e => { if (!isSelected) (e.currentTarget as HTMLDivElement).style.background = 'rgba(255,255,255,.02)' }}
                      onMouseLeave={e => { if (!isSelected) (e.currentTarget as HTMLDivElement).style.background = 'transparent' }}
                    >
                      <span style={{ display: 'flex', alignItems: 'center', gap: 6, overflow: 'hidden' }}>
                        {u.blocked && <ShieldOff size={11} color="#f87171" />}
                        {u.role === 'ADMIN' && !u.blocked && <Shield size={11} color="#c9a227" />}
                        {u.bypassPaywall && !u.blocked && u.role !== 'ADMIN' && <span title="Paywall bypassed"><Unlock size={11} color="#c9a227" /></span>}
                        <span style={{ color: '#e8e0f0', fontWeight: 600, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', fontSize: 13 }}>
                          {u.username}
                        </span>
                      </span>
                      <span style={{ color: '#8b7fa0', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', fontSize: 12, alignSelf: 'center' }}>
                        {u.email}
                      </span>
                      <span style={{ color: ROLE_COLOR[u.role] ?? '#8b7fa0', fontSize: 10, fontFamily: 'Cinzel, serif', alignSelf: 'center' }}>
                        {u.role}
                      </span>
                      <span style={{ color: '#c9a227', fontSize: 12, alignSelf: 'center' }}>{u.totalXp.toLocaleString()}</span>
                      <span style={{ color: '#8b7fa0', fontSize: 11, alignSelf: 'center' }}>{new Date(u.createdAt).toLocaleDateString()}</span>
                      <span style={{ alignSelf: 'center', color: '#8b7fa0' }}>
                        <BarChart2 size={13} />
                      </span>
                    </div>
                  )
                })}
              </div>

              {/* Pagination */}
              {totalPages > 1 && (
                <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginTop: 12, justifyContent: 'center' }}>
                  <button className="btn btn-ghost" style={{ fontSize: 11, padding: '5px 10px' }} disabled={page === 0} onClick={() => setPage(p => p - 1)}>
                    <ChevronLeft size={14} />
                  </button>
                  <span style={{ color: '#8b7fa0', fontSize: 12, fontFamily: 'Cinzel, serif' }}>
                    {page + 1} / {totalPages}
                  </span>
                  <button className="btn btn-ghost" style={{ fontSize: 11, padding: '5px 10px' }} disabled={page >= totalPages - 1} onClick={() => setPage(p => p + 1)}>
                    <ChevronRight size={14} />
                  </button>
                </div>
              )}
            </>
          )}
        </div>

        {/* Detail panel */}
        {selectedUser && (
          <UserDetailPanel
            user={selectedUser}
            onClose={() => setSelectedUser(null)}
            onUpdate={handleUpdate}
          />
        )}
      </div>
    </div>
  )
}
