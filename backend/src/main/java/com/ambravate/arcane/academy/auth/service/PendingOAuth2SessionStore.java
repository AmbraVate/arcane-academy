package com.ambravate.arcane.academy.auth.service;

import com.ambravate.arcane.academy.auth.dto.AuthResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Short-lived server-side store for OAuth2 post-login session data.
 *
 * <p>Prevents tokens from appearing in OAuth2 callback URLs (which would expose them in
 * browser history, server logs, and referrer headers). The flow is:
 * <ol>
 *   <li>OAuth2 success handler stores the full {@link AuthResponse} here and gets back an
 *       opaque one-time {@code code}.</li>
 *   <li>The backend redirects the browser to the frontend with only {@code ?code=<code>}.</li>
 *   <li>The frontend immediately POSTs the code to {@code /api/auth/exchange}, receives the
 *       {@link AuthResponse} as a JSON body, then discards the code.</li>
 * </ol>
 *
 * <p>Codes expire after {@value #TTL_MS} ms and are single-use (consumed on first read).
 */
@Service
public class PendingOAuth2SessionStore {

    static final long TTL_MS = 30_000; // 30 seconds — enough for a browser redirect

    private record Entry(AuthResponse auth, long createdAt) {}

    private final ConcurrentHashMap<String, Entry> store = new ConcurrentHashMap<>();

    /**
     * Stores an {@link AuthResponse} and returns a single-use opaque code.
     */
    public String store(AuthResponse auth) {
        // Evict stale entries on every store to keep the map small.
        long cutoff = System.currentTimeMillis() - TTL_MS;
        store.entrySet().removeIf(e -> e.getValue().createdAt() < cutoff);

        String code = UUID.randomUUID().toString();
        store.put(code, new Entry(auth, System.currentTimeMillis()));
        return code;
    }

    /**
     * Consumes a code (single-use) and returns the associated {@link AuthResponse},
     * or empty if the code is unknown or has expired.
     */
    public Optional<AuthResponse> consume(String code) {
        if (code == null || code.isBlank()) return Optional.empty();
        Entry entry = store.remove(code);
        if (entry == null) return Optional.empty();
        if (System.currentTimeMillis() - entry.createdAt() > TTL_MS) return Optional.empty();
        return Optional.of(entry.auth());
    }
}
