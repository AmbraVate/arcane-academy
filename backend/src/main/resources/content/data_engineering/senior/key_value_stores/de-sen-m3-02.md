---
id: de-sen-m3-02
school: DATA_ENGINEERING
domainId: data_engineering
tier: SENIOR
moduleId: de-sen-m3
moduleTitle: "Module 3: NoSQL Systems"
moduleGlyph: "🗂️"
moduleSortOrder: 3
topicSlug: key_value_stores
topicTitle: "Key-Value Stores"
topicSortOrder: 2
lesson: 2
title: "Key-Value Stores: Speed at the Cost of Simplicity"
sortOrder: 2
difficulty: 4
estimatedMinutes: 35
xpReward: 75
practiceType: GUIDED_AND_SOLO
questType: RETRIEVAL_CHALLENGE
retrievalWeight: 0.6
questTypes:
  - guided
  - solo
  - retrieval
prerequisites:
  - de-sen-m3-01
integrationDomains:
  - mathematics
  - sciences
soloAssessment:
  type: RUBRIC_REFLECTION
  rubricItems:
    - "Explains what a key-value store trades away to achieve its speed"
    - "Describes TTL-based expiry and its use in caching and session management"
    - "Identifies cache eviction policies and when each is appropriate"
    - "Names common misuses of key-value stores and their consequences"
  keywords:
    - key-value
    - TTL
    - cache eviction
    - LRU
    - cache stampede
    - Redis
    - atomic operations
  modelAnswer: |
    Key-value stores map a string key to an opaque value, accessed only by key. They trade query flexibility (no range scans, no filtering by value, no JOINs) for extreme speed — O(1) get/set operations, sub-millisecond latency, and high throughput.
    TTL (time-to-live) sets an expiry on a key. When TTL expires, the key is automatically deleted. This enables caching (serve stale data from memory rather than querying the database) and session management (sessions expire automatically without a cleanup job).
    Cache eviction policies determine what to remove when memory fills: LRU (Least Recently Used) removes the key not accessed for the longest time — appropriate for caches where recent data is more likely to be needed. LFU (Least Frequently Used) removes the key accessed least often — good for stable popular data. TTL-based eviction is deterministic — good for time-bounded data like session tokens.
    Common misuses: using a key-value store as a primary database (data is often ephemeral, persistence guarantees are weaker), storing large blobs (network transfer overhead cancels the speed benefit), or failing to handle cache stampedes (thundering herd when a popular key expires and hundreds of requests simultaneously miss the cache and hit the database).
guidedSteps:
  - type: MULTIPLE_CHOICE
    question: "A cached product page expires at exactly midnight. At 00:00:01, 10,000 concurrent users request the page, all miss the cache, and all simultaneously query the database. What is this called?"
    options:
      - "Cache poisoning — malicious injection of incorrect cached data"
      - "Cold start — the cache is warming up after a restart"
      - "Cache stampede (thundering herd) — concurrent cache misses overwhelming the database"
      - "Eviction cascade — LRU eviction triggering a chain of misses"
    correctIndex: 2
    explanation: "A cache stampede (or thundering herd) occurs when a popular key expires and many concurrent requests simultaneously miss the cache and hit the backing store. Mitigation: probabilistic early expiry (refresh before expiry), mutex lock on cache miss (only one request fetches; others wait), or jittered TTLs to stagger expiry."
  - type: FILL_BLANK
    question: "Redis's INCR command is guaranteed to be ___, meaning two concurrent INCR calls on the same key will never produce the same result."
    answer: "atomic"
    explanation: "Redis is single-threaded for command execution. INCR is an atomic read-modify-write operation. Even under high concurrency, each INCR produces the next integer. This makes Redis suitable for distributed counters, rate limiters, and sequence generators without the need for database-level locks."
  - type: SHORT_TEXT
    question: "An application caches database query results in Redis with a 5-minute TTL. After a database schema migration, cached results contain old field names that no longer exist. How do you handle this?"
    modelAnswer: "Use cache key versioning: include a version number or schema hash in the cache key (e.g. user:profile:v3:{userId}). When the schema changes, bump the version — all old keys become unreachable and expire naturally. Alternatively, perform a targeted cache flush on deploy (FLUSHDB or delete keys matching a pattern). The versioning approach is safer as it avoids the brief inconsistency window during a flush."
microCheckpoint:
  question: "What does a key-value store give up to achieve its speed?"
  answer: "Query flexibility. You can only retrieve data by exact key — no range scans, no filtering by value, no JOINs. All query logic must be handled by the application, which must know the exact key to look up."
retrieval:
  recall: "What is TTL in a key-value store and what two patterns does it enable?"
  explain: "Explain the cache stampede problem and one mitigation strategy."
  mistakeId: "kv-store-as-primary-database"
---

# The Response Time Problem

The Consortium's API was responding in 450ms. Every request hit the database. "Half your queries are for data that doesn't change more than once a day," the Lead Data Engineer said. "You're reading the same data thousands of times per minute and re-computing it each time." The Senior Engineer knew the solution. "Redis."

# The Key-Value Model

A key-value store has the simplest possible API: **GET(key) → value** and **SET(key, value)**. Nothing else. No query language, no schema, no JOINs.

```
SET user:4891:profile → "{name: 'Aria', tier: 'senior', ...}"
GET user:4891:profile → "{name: 'Aria', tier: 'senior', ...}"
DEL user:4891:profile → (deleted)
```

This simplicity enables **sub-millisecond latency** — data lives in memory, access is O(1) hash lookup. Redis benchmarks consistently show 100,000+ operations per second per node.

## Redis Data Structures

Redis is not a pure key-value store — it supports rich value types:

```bash
# String (most common — arbitrary bytes, JSON, serialised objects)
SET cache:product:123 '{"id":123,"name":"Arcane Tome","price":39.99}'
GET cache:product:123

# Hash (field-value pairs — like a small document)
HSET user:4891 name "Aria" tier "senior" loginCount 42
HGET user:4891 tier           # → "senior"
HINCRBY user:4891 loginCount 1

# Sorted Set (leaderboard, priority queue)
ZADD leaderboard 1500 "player:aria"
ZADD leaderboard 2200 "player:marcus"
ZRANGE leaderboard 0 9 WITHSCORES REV  # top 10

# List (message queue, activity feed)
LPUSH feed:user:4891 "event:login"
LRANGE feed:user:4891 0 19  # last 20 events

# Set (unique membership, tags)
SADD course:python:enrolled "user:001" "user:002"
SISMEMBER course:python:enrolled "user:001"  # → 1

# Pub/Sub (event broadcasting)
PUBLISH notifications:user:4891 '{"type":"badge","name":"First Commit"}'
SUBSCRIBE notifications:user:4891
```

## TTL-Based Expiry

Every key can have a time-to-live in seconds. When TTL expires, the key is automatically deleted — no cleanup job required.

```bash
# Set with TTL
SET session:token:abc123 '{"userId":"4891","role":"senior"}' EX 3600  # 1 hour

# Set TTL separately
EXPIRE cache:product:123 300  # 5 minutes

# Check remaining TTL
TTL session:token:abc123  # → 3543 (seconds remaining)

# Remove TTL (make permanent)
PERSIST cache:product:123
```

**TTL use cases:**
- **Caching**: serve stale data from memory; TTL forces refresh from database
- **Session tokens**: sessions expire automatically — no `DELETE FROM sessions WHERE expires_at < NOW()` job
- **Rate limiting**: `INCR rate:user:4891; EXPIRE rate:user:4891 60` — auto-resets every minute
- **Distributed locks**: lock expires if the holder crashes — no deadlock

## Caching Patterns

### Cache-Aside (Lazy Loading)
```java
public Product getProduct(String id) {
    String cached = redis.get("product:" + id);
    if (cached != null) return deserialize(cached);

    Product product = database.findById(id);
    redis.setex("product:" + id, 300, serialize(product));
    return product;
}
```

The application is responsible for populating the cache. Cache is only populated on demand. Handles cache miss gracefully.

### Write-Through
```java
public void updateProduct(Product product) {
    database.save(product);
    redis.setex("product:" + product.getId(), 300, serialize(product));
}
```

Write to database and cache together. Cache is always warm. Slightly slower writes; risk of inconsistency if one fails.

### Cache Eviction Policies
When Redis memory fills, eviction policy determines what to remove:

| Policy | Behaviour | Use Case |
|---|---|---|
| `allkeys-lru` | Remove least recently used key | General caching |
| `allkeys-lfu` | Remove least frequently used key | Stable popular data |
| `volatile-lru` | LRU among keys with TTL only | Mixed persistent + cache data |
| `volatile-ttl` | Remove key with shortest TTL | Aggressive cache management |
| `noeviction` | Return error when full | Sessions (don't lose them) |

## Atomic Operations

Redis executes commands atomically (single-threaded). This enables patterns impossible in a multi-threaded system without locking:

```bash
# Distributed rate limiter — atomic increment + conditional expire
MULTI                            # begin transaction
INCR rate:user:4891
EXPIRE rate:user:4891 60
EXEC                             # execute atomically

# Check-and-set with Lua script (fully atomic)
local current = redis.call('GET', KEYS[1])
if current == false then
  redis.call('SET', KEYS[1], ARGV[1], 'EX', ARGV[2])
  return 1
else
  return 0
end
```

## Common Mistakes

> **Using Redis as Primary Database**
> Redis persistence (RDB snapshots, AOF logging) exists but is weaker than PostgreSQL's WAL. Redis is designed for data you can afford to lose (cached query results) or regenerate (session can re-authenticate). Never store the only copy of important data in Redis.

> **Ignoring Cache Stampede**
> When a high-traffic key expires, hundreds of simultaneous misses can overwhelm the database. Mitigate with: probabilistic early expiry (PER), a short mutex lock (only one process fetches while others wait), or jittered TTL (each instance adds random ±10% to TTL).

> **Large Values**
> Storing 1MB JSON blobs in Redis for 100k cache entries consumes 100GB RAM. Profile cache value sizes. For large objects, cache only the hot fields or use a tiered cache (Redis for small hot data, object storage for large cold data).

> **No Key Namespacing**
> `SET product:123 ...` and `SET user:123 ...` are different keys. Always namespace with prefix. Avoid collisions across microservices sharing a Redis instance.

## Mental Model

Think of a key-value store as a **cloakroom** — you hand in a coat and get a numbered token. To retrieve your coat, you need the exact token. The cloakroom attendant cannot say "find me all coats owned by someone named Aria" — there's no index on owner. Blazingly fast if you know the number; useless if you don't.

**Mini Summary**: Key-value stores trade query flexibility for O(1) speed. Redis supports rich value types — strings, hashes, lists, sets, sorted sets. TTL enables caching and session management. Eviction policies manage memory limits. Primary use cases: caching, sessions, rate limiting, distributed locks. Never use as a primary database.

---

# Guided Practice Quest

Follow the steps above in the **Guided Steps** section.

---

# Solo Practice Quest

The Consortium's leaderboard feature shows the top 100 students by XP. Currently it runs a `SELECT user_id, SUM(xp) FROM xp_events GROUP BY user_id ORDER BY xp DESC LIMIT 100` query on every page load — 200ms, and it's called 500 times per second.

Reflect on:
1. Propose a Redis-based caching strategy. What key would you use, what would you store, and what TTL?
2. The XP leaderboard is also updated in real-time when a student earns XP. Would you use a Redis Sorted Set directly (rather than caching the SQL result) to maintain the leaderboard? What are the trade-offs?
3. How would you handle a cache stampede if the leaderboard key expires during peak traffic?

---

# Integration

**Mathematics**: LRU cache eviction implements the **optimal page replacement approximation**. The theoretical optimal algorithm (Bélády's algorithm) evicts the page that will be accessed furthest in the future — but this requires knowing the future. LRU approximates this by evicting the page unused for the longest time, exploiting **temporal locality**: recently accessed data is likely to be accessed again soon. LRU's approximation ratio (how often it performs optimally) is provably close to 1 for workloads with temporal locality — which is why it's the default for general caching.

**Sciences**: Caching mirrors **ATP storage in cellular biology**. Cells don't synthesise ATP on demand for every reaction — they maintain an ATP reservoir (cache) that provides instant energy. When ATP is depleted, metabolic pathways (database queries) regenerate it. The cache size (ATP pool) is bounded by cell volume (memory). The cell also has second-tier energy stores (ADP, creatine phosphate) analogous to tiered caches. The body's energy system is a highly optimised multi-tier cache hierarchy — exactly what Redis implements for application data.

---

# The First Cache Miss

The Senior Engineer deployed the Redis cache. Response times dropped from 450ms to 12ms for cached requests. The database load dropped by 87%. "Now the expensive queries are only running when the cache expires," the Lead Data Engineer said. "And when they do run, the database isn't drowning — it has headroom." The Senior Engineer was already planning the rate limiter. Redis had just become their favourite tool.
