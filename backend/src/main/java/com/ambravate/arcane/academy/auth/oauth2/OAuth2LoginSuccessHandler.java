package com.ambravate.arcane.academy.auth.oauth2;

import com.ambravate.arcane.academy.common.domain.AuthProvider;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.auth.dto.AuthResponse;
import com.ambravate.arcane.academy.auth.service.AuthService;
import com.ambravate.arcane.academy.auth.service.PendingOAuth2SessionStore;
import com.ambravate.arcane.academy.common.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Handles successful Google OAuth2 logins.
 *
 * <p>Tokens are NOT placed in the redirect URL (which would expose them in browser history,
 * logs, and referrer headers). Instead a short-lived opaque code is stored server-side via
 * {@link PendingOAuth2SessionStore} and only the code is sent to the frontend. The frontend
 * immediately exchanges it at {@code POST /api/auth/exchange} to receive the actual tokens
 * as a JSON body.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;
    private final JwtService jwtService;
    private final PendingOAuth2SessionStore pendingStore;

    @Value("${app.oauth2.redirect-uri}")
    private String frontendRedirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        String email    = oidcUser.getEmail();
        String name     = oidcUser.getFullName();
        String googleId = oidcUser.getSubject();

        log.info("[OAuth2] Google login | email={}***", email.substring(0, Math.min(3, email.length())));

        User user;
        try {
            user = authService.processOAuth2Login(email, name, googleId, AuthProvider.GOOGLE);
        } catch (IllegalStateException e) {
            // Account with this email already exists as a local-password account.
            // Redirect to login with a clear error rather than silently linking.
            log.warn("[OAuth2] Account conflict for email={}*** — {}", email.substring(0, Math.min(3, email.length())), e.getMessage());
            String errorRedirect = buildBaseRedirectUri() + "/login?error=" +
                    URLEncoder.encode("account_conflict", StandardCharsets.UTF_8);
            getRedirectStrategy().sendRedirect(request, response, errorRedirect);
            return;
        }

        String token = jwtService.generateToken(
                user.getId(), user.getUsername(), user.getRole().name(), user.isBlocked());

        String subStatus = user.getSubscriptionStatus() != null
                ? user.getSubscriptionStatus().name() : "FREE";

        AuthResponse authResponse = AuthResponse.anAuthResponse()
                .withToken(token)
                .withRefreshToken(user.getRefreshToken())
                .withUserId(user.getId())
                .withUsername(user.getUsername())
                .withTotalXp(user.getTotalXp())
                .withRank(user.getRank())
                .withStreakDays(user.getStreakDays())
                .withRole(user.getRole().name())
                .withBypassPaywall(user.isBypassPaywall())
                .withSubscriptionStatus(subStatus)
                .withOnboardingCompleted(user.isOnboardingCompleted())
                .build();

        // Store session server-side; redirect with only the opaque code.
        String code = pendingStore.store(authResponse);
        String redirectUrl = frontendRedirectUri + "?code=" + code;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    /** Derives the frontend base URL from the configured redirect URI. */
    private String buildBaseRedirectUri() {
        // frontendRedirectUri is typically "https://example.com/oauth2/callback"
        int idx = frontendRedirectUri.indexOf("/oauth2/callback");
        return idx > 0 ? frontendRedirectUri.substring(0, idx) : frontendRedirectUri;
    }
}
