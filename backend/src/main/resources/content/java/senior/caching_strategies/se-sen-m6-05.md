---
id: se-sen-m6-05
school: engineering
domainId: java
tier: SENIOR
moduleId: se-sen-m6
moduleTitle: "Module 6: Performance Engineering"
moduleGlyph: "🚀"
moduleSortOrder: 6
topicSlug: caching_strategies
topicTitle: "Caching Strategies"
topicSortOrder: 5
lesson: caching_strategies
title: "Caching Strategies"
sortOrder: 5
difficulty: 4
estimatedMinutes: 30
xpReward: 60
practiceType: NONE
questType: GUIDED
retrievalWeight: high
questTypes: [guided, solo, retrieval]
prerequisites: [database_optimisation]
integrationDomains: [mathematics, economics]
soloAssessment:
  type: PATTERN_MATCH
  rubricItems:
    - "Distinguishes cache-aside, write-through, write-behind, and read-through patterns and identifies appropriate use cases"
    - "Explains TTL and eviction policies (LRU, LFU) and the trade-offs between them"
    - "Describes the cache stampede problem and at least two mitigation strategies"
    - "Explains how @Cacheable works in Spring and what it abstracts"
    - "Articulates the stale data trade-off and how TTL decisions reflect business tolerance for staleness"
  keywords: [cache-aside, write-through, write-behind, TTL, LRU, LFU, cache stampede, thundering herd, Redis, @Cacheable, stale data, eviction, hit rate]
  modelAnswer: |
    Caching strategies define how the application interacts with the cache relative to the primary data store. Cache-aside (lazy loading) is the most common pattern: the application first checks the cache; on a miss, reads from the database, populates the cache, and returns the result. The application is responsible for cache population and invalidation. It tolerates missing cache entries gracefully and avoids caching data that is never read. Write-through updates the cache synchronously on every write, ensuring the cache is always fresh. Write latency increases (cache and database write), but reads are always consistent with the database. Write-behind (write-back) writes to the cache immediately and asynchronously persists to the database, improving write latency at the cost of data durability risk. Read-through delegates cache population to the cache layer itself (the cache client calls the database on a miss), removing cache logic from the application.

    TTL (Time To Live) controls how long an entry lives before expiry. Short TTLs reduce stale data but increase cache misses and database load. Long TTLs improve hit rates but risk serving outdated data. The correct TTL reflects the business's tolerance for staleness: product prices may tolerate 30-second staleness; user authentication tokens may tolerate none. Eviction policies control what happens when the cache is full. LRU (Least Recently Used) evicts the entry that was not accessed for the longest time — good for workloads with temporal locality (recently accessed data is likely to be accessed again). LFU (Least Frequently Used) evicts the entry accessed the fewest times — good for stable "hot" data that should never be evicted regardless of recency. LFU is more complex to implement but better for skewed access patterns (80/20 rule).

    Cache stampede (thundering herd) occurs when a popular cache entry expires and many concurrent requests all miss the cache simultaneously, all querying the database at once. Solutions: (1) Probabilistic early expiry — some requests re-cache just before TTL expires; (2) Mutex/lock on first miss — first thread populates, others wait; (3) Stale-while-revalidate — serve stale data while refreshing asynchronously; (4) Jitter on TTL — randomise expiry time to stagger misses across the fleet.

    Spring's @Cacheable annotation adds result caching to methods declaratively. @Cacheable(value = "products", key = "#productId") caches the return value by productId. On subsequent calls, Spring checks the cache before invoking the method. @CachePut forces cache update. @CacheEvict removes entries. Spring abstracts the cache implementation (in-memory Caffeine, Redis, Hazelcast) behind CacheManager, allowing implementation swap without code changes.

    Stale data is an inevitable consequence of caching. Every cache is a consistency trade-off: accepting data that may be seconds or minutes out of date in exchange for reduced latency and database load. The senior engineer's responsibility is to quantify the staleness tolerance for each cached entity and design TTLs accordingly, then communicate these trade-offs to product stakeholders who may expect cache to be transparent.
guidedSteps:
  - id: cach-step-1
    sortOrder: 1
    inputType: MULTIPLE_CHOICE
    instruction: |
      A product catalogue has 50,000 products. Data changes infrequently (a few updates per day). Reads are 100,000 per minute. Which caching pattern best fits this workload?
    inputConfig:
      options:
        - "A. Write-through — updates the cache on every product change"
        - "B. Cache-aside with long TTL (hours) — lazy-populate on miss, serve stale data for infrequent updates"
        - "C. Write-behind — write to cache first, persist asynchronously"
        - "D. No cache — product data changes too frequently"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["B"]
      rejectedFeedback: "A product catalogue with infrequent updates and high read volume is the ideal cache-aside scenario. Long TTL (hours) matches the business staleness tolerance (product changes are announced, not real-time). Write-through adds write latency for every admin update. Write-behind risks data loss. No cache would collapse the database under 100K reads/minute."
    hint: "Match the pattern to the update frequency and read/write ratio."
    reflectionPrompt: "Caching is most valuable when reads vastly outnumber writes and tolerance for stale data is high."
  - id: cach-step-2
    sortOrder: 2
    inputType: FILL_BLANK
    instruction: |
      When a popular cache entry expires and hundreds of concurrent requests all miss the cache simultaneously and query the database at once, this is called a cache ___.
    inputConfig:
      placeholder: "event name"
    markingRule:
      matchMode: NORMALIZED
      accepted: ["stampede", "cache stampede", "thundering herd"]
      rejectedFeedback: "Cache stampede (or thundering herd) occurs when a high-traffic cache entry expires. Every concurrent request misses the cache and hits the database simultaneously, potentially overwhelming it. Mitigations: mutex on first miss, probabilistic early refresh, stale-while-revalidate, or TTL jitter."
    hint: "Think about what happens when many requests arrive at an empty cache simultaneously."
    reflectionPrompt: "A highly popular cache key has disproportionate stampede risk — it needs special handling."
  - id: cach-step-3
    sortOrder: 3
    inputType: SHORT_TEXT
    instruction: |
      A Spring Boot service uses @Cacheable to cache user profiles with a 5-minute TTL. A user updates their profile and the UI immediately re-loads, showing the old profile. Explain what happened and describe two ways to fix it.
    inputConfig:
      minWords: 25
    markingRule:
      matchMode: CONTAINS
      accepted: [CacheEvict, TTL, stale, invalidation, CachePut, update, evict, cache-aside]
      rejectedFeedback: "@Cacheable caches the old profile for up to 5 minutes. The update does not invalidate the cache. Fix 1: add @CacheEvict on the update method to remove the cached entry when the profile is updated. Fix 2: add @CachePut on the update method to immediately replace the cached entry with the new value. Fix 3: shorten TTL to an acceptable staleness window."
    hint: "The profile was cached before the update — what Spring annotation removes or replaces a cache entry?"
    reflectionPrompt: "Cache invalidation is one of the famously hard problems in computer science. @CacheEvict makes it explicit and manageable."
microCheckpoint:
  - type: MULTIPLE_CHOICE
    question: "LRU eviction policy removes which entry when the cache is full?"
    options:
      - "A. The entry with the lowest access frequency"
      - "B. The entry that has not been accessed for the longest time"
      - "C. A randomly selected entry"
      - "D. The entry with the earliest creation time"
    correctIndex: 1
    feedback: "LRU (Least Recently Used) evicts the entry with the oldest last-access time. It suits workloads with temporal locality — recently accessed data is more likely to be accessed again. LFU evicts by access frequency, not recency."
  - type: MULTIPLE_CHOICE
    question: "Which Spring Cache annotation forces a cache update regardless of whether an entry exists, without skipping the method invocation?"
    options:
      - "A. @Cacheable"
      - "B. @CacheEvict"
      - "C. @CachePut"
      - "D. @Caching"
    correctIndex: 2
    feedback: "@CachePut always executes the method and updates the cache with the result. Use it on write/update methods to keep the cache current. @Cacheable skips the method if a cache entry exists. @CacheEvict removes the entry without updating."
retrieval:
  recall: "Name four caching strategies and describe the primary trade-off each makes between read performance, write performance, and data consistency."
  explain: "Explain to a junior developer why a cache TTL of 0 (no caching) and a cache TTL of infinity (never expire) are both problematic."
  mistakeId:
    code: |
      @Service
      public class ProductService {
          @Cacheable("products")
          public Product getProduct(Long id) {
              return productRepository.findById(id).orElseThrow();
          }

          public Product updateProduct(Long id, ProductDto dto) {
              Product product = productRepository.findById(id).orElseThrow();
              product.setPrice(dto.getPrice());
              return productRepository.save(product);
              // Cache NOT invalidated
          }
      }
    answer: "The updateProduct method modifies the database but does not invalidate or update the cache. Subsequent calls to getProduct(id) return the stale cached version. Fix: add @CacheEvict(value = 'products', key = '#id') to updateProduct, or @CachePut(value = 'products', key = '#id') to replace the cached entry with the updated product. Without cache invalidation, getProduct returns stale data until TTL expires."
---

# Hook

Your service handles 50,000 product page views per minute. The database handles it — barely, at 95% utilisation. You add a cache. The database drops to 12% utilisation. Response times fall from 450ms to 8ms. For the first time in months, the system feels responsive. Three hours later, a popular product entry expires from the cache. In the five seconds it takes to repopulate from the database, 2,000 concurrent requests all miss the cache, slam the database simultaneously, and you watch the database utilisation spike to 400% before the cache repopulates. Welcome to the cache stampede. Caching is powerful — and it requires understanding its failure modes as deeply as its benefits.

# Lore Introduction

The Academy's masters call caching "borrowing time from the future." Every cache hit is time borrowed from the last cache miss — the work was done once and its result is served many times over. But borrowed time must be repaid: when entries expire, the debt comes due. When data changes, the cache must be told. When the cache is full, the least valuable entries must leave. The senior mage who wields caching wields a tool of extraordinary leverage — and like all powerful tools, its misuse creates problems worse than the ones it solved. The art is knowing what to cache, for how long, and how to invalidate it without leaving a ghost of stale data haunting the system.

# Core Learning

## Concept Introduction

**Cache Patterns**

| Pattern | Application Role | Read Behaviour | Write Behaviour | Use Case |
|---------|-----------------|----------------|-----------------|----------|
| Cache-aside | Manages cache explicitly | Read cache → miss → DB → populate | Write to DB; optionally evict/update cache | Most common; lazy population |
| Write-through | Manages cache explicitly | Read from cache | Write to cache AND DB synchronously | Consistent reads; higher write latency |
| Write-behind | Manages cache explicitly | Read from cache | Write to cache immediately; async DB write | Low write latency; durability risk |
| Read-through | Cache layer manages DB access | Cache fetches from DB on miss | Application writes to DB; cache tracks DB | Simplified application; less control |

**TTL and Eviction**

- **TTL** (Time To Live): entry expires after N seconds regardless of access. Controls maximum staleness.
- **LRU** (Least Recently Used): evicts entry not accessed for longest time. Good for temporal locality.
- **LFU** (Least Frequently Used): evicts entry with fewest accesses. Good for hot data that should persist.
- **Size-based**: evict when cache reaches max bytes/entries.

**Cache Stampede Mitigation**

```java
// Option 1: Probabilistic early expiry
// Before TTL expires, some requests re-populate proactively

// Option 2: Mutex on first miss (simplified)
String value = cache.get(key);
if (value == null) {
    if (lock.tryLock(key)) {
        try {
            value = database.get(key);
            cache.set(key, value, TTL);
        } finally {
            lock.unlock(key);
        }
    } else {
        value = waitForRepopulation(key); // or return stale
    }
}
```

**Redis Basics**

Redis is an in-memory data structure server. Common cache operations:
```bash
SET product:42 '{"id":42,"name":"Widget"}' EX 3600  # Set with 60-min TTL
GET product:42                                         # Read (null if expired)
DEL product:42                                         # Explicit invalidation
EXPIRE product:42 1800                                 # Reset TTL to 30 min
```

## Why It Matters

Caching is the most impactful single technique for read-heavy service performance. A 99% cache hit rate on database reads reduces database load by 99x, allowing the same database to serve 100x more read traffic. However, caching introduces consistency complexity: the cache is a separate copy of truth, and keeping it aligned with the database requires deliberate design. Senior engineers choose caching strategies based on business requirements (staleness tolerance), failure modes (stampede, inconsistency), and operational characteristics (cache size, eviction behaviour).

## Worked Examples

**Example 1: Spring @Cacheable with Redis**

```yaml
# application.yml
spring:
  cache:
    type: redis
  redis:
    host: localhost
    port: 6379
```

```java
@Service
public class ProductService {
    
    @Cacheable(value = "products", key = "#productId")
    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow();
        // Only called on cache miss; cached result used on hit
    }
    
    @CachePut(value = "products", key = "#product.id")
    public Product updateProduct(Product product) {
        return productRepository.save(product); // Updates cache with new value
    }
    
    @CacheEvict(value = "products", key = "#productId")
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId); // Removes cache entry
    }
    
    @CacheEvict(value = "products", allEntries = true)
    @Scheduled(fixedDelay = 3600000) // Every hour
    public void evictProductCache() {} // Periodic full eviction
}
```

**Example 2: Cache-Aside in Service Layer**

```java
@Service
public class UserProfileService {
    private final RedisTemplate<String, UserProfile> redis;
    private final UserRepository userRepository;
    
    public UserProfile getProfile(Long userId) {
        String key = "profile:" + userId;
        UserProfile profile = redis.opsForValue().get(key);
        
        if (profile == null) {                           // Cache miss
            profile = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
            
            // Jitter: randomise TTL by ±10% to prevent stampede
            long ttl = 3600 + ThreadLocalRandom.current().nextLong(-360, 360);
            redis.opsForValue().set(key, profile, ttl, TimeUnit.SECONDS);
        }
        
        return profile;
    }
}
```

**Example 3: Caffeine In-Process Cache with Write-Through**

```java
@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(10_000)          // Max entries
            .expireAfterWrite(5, MINUTES) // TTL
            .recordStats());              // Hit/miss metrics
        return manager;
    }
}
```

## Common Mistakes

- **Caching mutable data without invalidation**: the most common caching bug. Every update path must have a corresponding cache eviction or update.
- **Caching negative results (null)**: a cache that returns null on a DB miss and does not cache that null will re-query the DB on every request for a non-existent entity. Cache negative results with a short TTL.
- **Over-caching**: caching data that changes every request provides no benefit and adds complexity. Cache data that is read much more frequently than it changes.
- **Not monitoring hit rates**: a cache with a 20% hit rate provides little benefit. Monitor `cache_hit_ratio`; a healthy application cache should be >80%.
- **Ignoring cache-as-a-single-point-of-failure**: if Redis is unavailable, the cache-aside pattern falls back to the database. Write the fallback explicitly; do not let Redis unavailability cause service unavailability.

## Mental Model

Think of a cache as a buffer between your application and a slow source of truth. The faster the buffer and the higher its hit rate, the less often the slow source is consulted. TTL determines how long entries sit in the buffer before being refreshed from the source. Eviction policy determines which entries leave when the buffer is full. The cache stampede is the buffer running empty simultaneously for many requests — like a checkout queue suddenly losing its cashier and everyone piling in at once. Managing caching well means managing the buffer's size, freshness, and failure modes as carefully as any other infrastructure concern.

## Mini Summary

- ✔ Cache-aside is the most common pattern: check cache, miss → load from DB, populate cache; application manages population and eviction
- ✔ TTL should reflect business staleness tolerance; LRU eviction suits temporal locality; LFU suits stable hot data
- ✔ Cache stampede occurs when popular entries expire simultaneously; mitigate with mutex-on-miss, TTL jitter, or stale-while-revalidate
- ✔ @Cacheable, @CachePut, and @CacheEvict provide Spring declarative cache management over any CacheManager implementation
- ✔ Cache hit rate must be monitored; a cache with <50% hit rate may not be worth the consistency complexity it introduces

# Guided Practice Quest

Work through the guided steps above. For the @CacheEvict question, think carefully about the timing: the update must persist to the database AND the cache must be invalidated — in what order, and why does order matter for consistency?

# Solo Practice Quest

A Spring Boot API serves customer order history. The `getOrderHistory(customerId)` method is called 200,000 times per day per customer for top customers. Data changes when a new order is placed (several times per day). The service is deployed across 4 instances with a shared Redis cluster.

Design the caching strategy:
1. Which caching pattern (cache-aside, write-through, etc.) and why?
2. What TTL would you set and what is the staleness trade-off?
3. How would you handle cache invalidation when a new order is placed?
4. How would you prevent a cache stampede for a top customer's cache entry expiring?
5. What metrics would you monitor to validate the cache is effective?

# Integration

**Connecting to Mathematics — Hit Rate and the Geometric Series**

Cache effectiveness is governed by the hit rate h (fraction of requests served from cache). If database query time is D and cache access time is C, average response time is `h × C + (1 - h) × (C + D)`. For C = 1ms, D = 50ms, at h = 0.9: average = 0.9×1 + 0.1×51 = 6ms — an 8x improvement over 51ms without caching. At h = 0.99: 1.5ms — a 34x improvement. The relationship between hit rate and improvement is non-linear: increasing hit rate from 0.9 to 0.99 doubles the improvement despite only a 9-percentage-point change. This explains why cache engineers obsess over the final percentage points of hit rate. LRU eviction policy's effectiveness can be modelled by the stack distance distribution of the access sequence — the fraction of accesses with stack distance ≤ cache size equals the hit rate. How does understanding this model change how you would size a cache?

**Connecting to Economics — Caching as Inventory Management**

Caching is economically equivalent to inventory management: the cache is a store of pre-computed results (inventory) held to avoid the cost of re-computation (production). Holding too much inventory (cache too large) wastes memory (storage cost). Holding too little inventory (cache too small, high miss rate) forces frequent re-computation (production cost). TTL is the shelf life: set it too short (high spoilage rate), inventory turns over frequently; set it too long (low spoilage), stale goods reach the customer. The Economic Order Quantity model from inventory management has a direct analogue in cache sizing: the optimal cache size minimises total cost (memory cost + miss cost). How does the economics of your application's infrastructure cost (Redis cluster pricing vs database query cost per request) inform your TTL and cache size decisions?

# Lore Conclusion

The cache is a promise: "this answer was true when I last checked." The TTL is the expiry on that promise. The eviction policy is the decision about which promises to retire when space runs low. The senior engineer who caches wisely makes only promises they can keep — with TTLs that reflect genuine staleness tolerance, invalidation that fires on every update, and stampede protection that ensures the database is not overwhelmed when the promises expire all at once. Cache what is expensive to compute and cheap to serve stale. Know when to serve the old answer and when to demand the new one. That judgment is what separates a cached system from a consistently inconsistent one.
