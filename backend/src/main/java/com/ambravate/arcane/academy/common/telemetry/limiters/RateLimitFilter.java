package com.ambravate.arcane.academy.common.telemetry.limiters;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token-bucket rate limiting via Bucket4j.
 *
 * <p>Three buckets:
 * <ul>
 *   <li><b>auth</b> — keyed on client IP. Covers /api/auth/login + /api/auth/register
 *       to slow down credential-stuffing and signup-spam.</li>
 *   <li><b>ai-mentor</b> — keyed on userId (or IP if anon). Tight cap because
 *       LLM calls are expensive — see CLAUDE.md §14a.</li>
 *   <li><b>code-runner</b> — keyed on userId. Looser cap because the runner
 *       has its own 5s timeout.</li>
 * </ul>
 *
 * <p>Buckets live in an in-memory {@link ConcurrentHashMap}. Sufficient for a
 * single-node deployment; horizontal scaling needs a shared backend
 * (bucket4j ships Hazelcast/Redis adapters).
 *
 * <p>Trust domain: {@link HttpServletRequest#getRemoteAddr()} is the direct
 * peer's IP. When this app is behind a proxy/CDN you must enable
 * {@code server.forward-headers-strategy=framework} or this filter will see
 * the proxy's IP and rate-limit the wrong key.
 *
 * <p>Returns HTTP 429 with a {@code Retry-After} header (seconds) on cap hit.
 * Registered explicitly by {@code SecurityConfig.filterChain} — NOT a
 * {@code @Component} so the servlet container does not auto-register it
 * twice.
 */
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitProperties props;

    private final Map<String, Bucket> authBuckets       = new ConcurrentHashMap<>();
    private final Map<String, Bucket> aiMentorBuckets   = new ConcurrentHashMap<>();
    private final Map<String, Bucket> codeRunnerBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> adminBuckets      = new ConcurrentHashMap<>();

    public RateLimitFilter(RateLimitProperties props) {
        this.props = props;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws ServletException, IOException {

        if (!props.isEnabled()) {
            chain.doFilter(req, resp);
            return;
        }

        BucketSpec spec = pickSpec(req.getRequestURI());
        if (spec == null || spec.capacity() == 0) {
            // No matching route, or capacity 0 disables a particular bucket.
            chain.doFilter(req, resp);
            return;
        }

        String key = spec.identityFromRequest(req);
        Bucket bucket = spec.cache().computeIfAbsent(key, k -> newBucket(spec));

        if (bucket.tryConsume(1)) {
            chain.doFilter(req, resp);
            return;
        }

        // Cap hit. 429 with conservative Retry-After (full refill window).
        // jakarta.servlet doesn't define SC_TOO_MANY_REQUESTS — hard-code 429.
        long retryAfterSeconds = Math.max(1, spec.refillPeriodSeconds());
        resp.setStatus(429);
        resp.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(
                "{\"message\":\"Rate limit exceeded. Try again in " + retryAfterSeconds + "s.\"}"
        );
        log.info("[RateLimit] 429 | route={} bucket={} key={}",
                req.getRequestURI(), spec.bucketName(), redact(key));
    }

    /** Map a request to its bucket — first-match wins; null = no limiting. */
    private BucketSpec pickSpec(String path) {
        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register")) {
            return new BucketSpec("auth", authBuckets,
                    props.getAuth().capacity(),
                    props.getAuth().refillTokens(),
                    props.getAuth().refillPeriodSeconds(),
                    RateLimitFilter::clientIp);
        }
        if (path.startsWith("/api/ai-mentor")) {
            return new BucketSpec("ai-mentor", aiMentorBuckets,
                    props.getAiMentor().capacity(),
                    props.getAiMentor().refillTokens(),
                    props.getAiMentor().refillPeriodSeconds(),
                    RateLimitFilter::userIdOrIp);
        }
        if (path.startsWith("/api/code/run")) {
            return new BucketSpec("code-runner", codeRunnerBuckets,
                    props.getCodeRunner().capacity(),
                    props.getCodeRunner().refillTokens(),
                    props.getCodeRunner().refillPeriodSeconds(),
                    RateLimitFilter::userIdOrIp);
        }
        if (path.startsWith("/api/admin")) {
            return new BucketSpec("admin", adminBuckets,
                    props.getAdmin().capacity(),
                    props.getAdmin().refillTokens(),
                    props.getAdmin().refillPeriodSeconds(),
                    RateLimitFilter::userIdOrIp);
        }
        return null;
    }

    private Bucket newBucket(BucketSpec spec) {
        Bandwidth limit = Bandwidth.builder()
                .capacity(spec.capacity())
                .refillIntervally(spec.refillTokens(), Duration.ofSeconds(spec.refillPeriodSeconds()))
                .build();
        return Bucket.builder().addLimit(limit).build();
    }

    /** Skip everything else — actuator, OAuth, static assets — to keep hot-path cost minimal. */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        String path = req.getRequestURI();
        return !(path.startsWith("/api/auth/login")
                || path.startsWith("/api/auth/register")
                || path.startsWith("/api/ai-mentor")
                || path.startsWith("/api/code/run")
                || path.startsWith("/api/admin"));
    }

    /** Direct peer IP. Behind a proxy this is the proxy — see class javadoc. */
    private static String clientIp(HttpServletRequest req) {
        String addr = req.getRemoteAddr();
        return addr == null ? "unknown" : addr;
    }

    /** Logged-in user → userId; anonymous → IP. */
    private static String userIdOrIp(HttpServletRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return "u:" + auth.getName();
        }
        return "ip:" + clientIp(req);
    }

    /** Pseudonymise bucket keys in log lines so we never write raw IPs/userIds. */
    private static String redact(String key) {
        if (key == null || key.length() <= 4) return "?";
        return key.substring(0, Math.min(4, key.length())) + "***";
    }

}
