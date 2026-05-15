import { useEffect, useState, useCallback } from 'react'
import { adminUserApi, type AdminUser } from '@/shared/api/adminServices'

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
