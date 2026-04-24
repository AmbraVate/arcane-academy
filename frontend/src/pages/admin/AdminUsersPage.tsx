import { useEffect, useState, useCallback } from 'react'
import { adminUserApi, type AdminUser } from '../../api/adminServices'

export default function AdminUsersPage() {
  const [users, setUsers] = useState<AdminUser[]>([])
  const [total, setTotal] = useState(0)
  const [page, setPage] = useState(0)
  const [search, setSearch] = useState('')
  const [searchInput, setSearchInput] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [selectedUser, setSelectedUser] = useState<AdminUser | null>(null)
  const [resetting, setResetting] = useState<string | null>(null)

  const PAGE_SIZE = 20

  const load = useCallback(() => {
    setLoading(true)
    adminUserApi.list(page, PAGE_SIZE, search || undefined)
      .then(resp => { setUsers(resp.content); setTotal(resp.totalElements) })
      .catch(() => setError('Failed to load users'))
      .finally(() => setLoading(false))
  }, [page, search])

  useEffect(() => { load() }, [load])

  const handleSearch = () => {
    setSearch(searchInput)
    setPage(0)
  }

  const handleResetProgress = async (userId: string) => {
    if (!confirm('Reset ALL progress for this user? This cannot be undone.')) return
    setResetting(userId)
    try {
      await adminUserApi.resetProgress(userId)
      load()
      if (selectedUser?.id === userId) setSelectedUser(null)
    } catch {
      setError('Failed to reset progress')
    } finally {
      setResetting(null)
    }
  }

  const totalPages = Math.ceil(total / PAGE_SIZE)

  const ROLE_COLOR: Record<string, string> = { ADMIN: '#c9a227', USER: '#8b7fa0' }

  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 24 }}>
        <div>
          <h1 style={{ fontFamily: 'Cinzel, serif', fontSize: 22, color: '#c9a227' }}>Users</h1>
          <p style={{ color: '#8b7fa0', fontSize: 13, marginTop: 4 }}>{total} registered learners</p>
        </div>
      </div>

      {error && <div style={{ color: '#f87171', fontSize: 13, marginBottom: 12, padding: '8px 12px', background: 'rgba(248,113,113,.1)', borderRadius: 6 }}>{error}</div>}

      {/* Search */}
      <div style={{ display: 'flex', gap: 10, marginBottom: 20 }}>
        <input
          style={{ ...inputStyle, flex: 1, maxWidth: 360 }}
          placeholder="Search by username or email…"
          value={searchInput}
          onChange={e => setSearchInput(e.target.value)}
          onKeyDown={e => e.key === 'Enter' && handleSearch()}
        />
        <button className="btn btn-ghost" style={{ fontSize: 12, padding: '7px 16px' }} onClick={handleSearch}>
          Search
        </button>
        {search && (
          <button className="btn btn-ghost" style={{ fontSize: 12, padding: '7px 12px' }} onClick={() => { setSearch(''); setSearchInput(''); setPage(0) }}>
            Clear
          </button>
        )}
      </div>

      <div style={{ display: 'flex', gap: 20 }}>

        {/* Table */}
        <div style={{ flex: 1, minWidth: 0 }}>
          {loading ? (
            <div style={{ color: '#8b7fa0', fontSize: 14 }}>Loading…</div>
          ) : (
            <>
              <div style={{ background: '#16132b', border: '1px solid #2e2850', borderRadius: 10, overflow: 'hidden' }}>
                {/* Header */}
                <div style={{ display: 'grid', gridTemplateColumns: '2fr 2fr 1fr 1fr 1fr', gap: 0, padding: '10px 16px', borderBottom: '1px solid #2e2850', fontSize: 11, fontFamily: 'Cinzel, serif', color: '#8b7fa0' }}>
                  <span>Username</span>
                  <span>Email</span>
                  <span>Role</span>
                  <span>XP</span>
                  <span>Joined</span>
                </div>
                {users.length === 0 && (
                  <div style={{ padding: 20, color: '#8b7fa0', fontSize: 13 }}>No users found.</div>
                )}
                {users.map(u => (
                  <div
                    key={u.id}
                    onClick={() => setSelectedUser(u.id === selectedUser?.id ? null : u)}
                    style={{
                      display: 'grid',
                      gridTemplateColumns: '2fr 2fr 1fr 1fr 1fr',
                      gap: 0,
                      padding: '12px 16px',
                      borderBottom: '1px solid #1e1a35',
                      cursor: 'pointer',
                      transition: 'background .15s',
                      background: selectedUser?.id === u.id ? 'rgba(139,92,246,.1)' : 'transparent',
                      fontSize: 13,
                    }}
                    onMouseEnter={e => { if (selectedUser?.id !== u.id) (e.currentTarget as HTMLDivElement).style.background = 'rgba(255,255,255,.02)' }}
                    onMouseLeave={e => { if (selectedUser?.id !== u.id) (e.currentTarget as HTMLDivElement).style.background = 'transparent' }}
                  >
                    <span style={{ color: '#e8e0f0', fontWeight: 600, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{u.username}</span>
                    <span style={{ color: '#8b7fa0', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', fontSize: 12 }}>{u.email}</span>
                    <span style={{ color: ROLE_COLOR[u.role] ?? '#8b7fa0', fontSize: 11, fontFamily: 'Cinzel, serif' }}>{u.role}</span>
                    <span style={{ color: '#c9a227', fontSize: 12 }}>{u.totalXp.toLocaleString()}</span>
                    <span style={{ color: '#8b7fa0', fontSize: 11 }}>{new Date(u.createdAt).toLocaleDateString()}</span>
                  </div>
                ))}
              </div>

              {/* Pagination */}
              {totalPages > 1 && (
                <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginTop: 14, justifyContent: 'center' }}>
                  <button className="btn btn-ghost" style={{ fontSize: 11, padding: '5px 12px' }} disabled={page === 0} onClick={() => setPage(p => p - 1)}>
                    ← Prev
                  </button>
                  <span style={{ color: '#8b7fa0', fontSize: 12, fontFamily: 'Cinzel, serif' }}>
                    {page + 1} / {totalPages}
                  </span>
                  <button className="btn btn-ghost" style={{ fontSize: 11, padding: '5px 12px' }} disabled={page >= totalPages - 1} onClick={() => setPage(p => p + 1)}>
                    Next →
                  </button>
                </div>
              )}
            </>
          )}
        </div>

        {/* Detail panel */}
        {selectedUser && (
          <div style={{
            width: 260,
            flexShrink: 0,
            background: '#16132b',
            border: '1px solid #2e2850',
            borderRadius: 10,
            padding: 20,
            alignSelf: 'flex-start',
          }}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 14 }}>
              <h3 style={{ fontFamily: 'Cinzel, serif', fontSize: 14, color: '#c4b5fd' }}>User Detail</h3>
              <button style={{ background: 'transparent', border: 'none', color: '#8b7fa0', cursor: 'pointer', fontSize: 14 }} onClick={() => setSelectedUser(null)}>✕</button>
            </div>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
              {[
                { label: 'Username',   val: selectedUser.username },
                { label: 'Email',      val: selectedUser.email },
                { label: 'Role',       val: selectedUser.role },
                { label: 'Auth',       val: selectedUser.authProvider },
                { label: 'Rank',       val: selectedUser.rank },
                { label: 'XP',         val: selectedUser.totalXp.toLocaleString() },
                { label: 'Streak',     val: `${selectedUser.streakDays}d` },
                { label: 'Completed',  val: `${selectedUser.completedSubChunks} sub-chunks` },
                { label: 'Joined',     val: new Date(selectedUser.createdAt).toLocaleDateString() },
                { label: 'Last login', val: selectedUser.lastLoginAt ? new Date(selectedUser.lastLoginAt).toLocaleDateString() : 'Never' },
              ].map(row => (
                <div key={row.label} style={{ display: 'flex', justifyContent: 'space-between', gap: 8 }}>
                  <span style={{ fontSize: 11, color: '#8b7fa0', fontFamily: 'Cinzel, serif' }}>{row.label}</span>
                  <span style={{ fontSize: 12, color: '#e8e0f0', textAlign: 'right' }}>{row.val}</span>
                </div>
              ))}
            </div>

            <div style={{ marginTop: 20, paddingTop: 16, borderTop: '1px solid #2e2850' }}>
              <button
                className="btn btn-ghost"
                style={{ fontSize: 11, padding: '6px 14px', borderColor: 'rgba(248,113,113,.3)', color: '#f87171', width: '100%', justifyContent: 'center' }}
                disabled={resetting === selectedUser.id}
                onClick={() => handleResetProgress(selectedUser.id)}
              >
                {resetting === selectedUser.id ? 'Resetting…' : '⚠️ Reset Progress'}
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

const inputStyle: React.CSSProperties = {
  background: '#16132b', border: '1px solid #2e2850', borderRadius: 6,
  color: '#e8e0f0', fontSize: 13, padding: '7px 10px', outline: 'none',
}
