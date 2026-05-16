package com.ambravate.arcane.academy.auth.oauth2;

import com.ambravate.arcane.academy.common.domain.AuthProvider;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.auth.service.AuthService;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;
    private final JwtService jwtService;

    @Value("${app.oauth2.redirect-uri}")
    private String frontendRedirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        String googleId = oidcUser.getSubject();

        log.info("[OAuth2] Google login success | email={} name={}", email, name);

        User user = authService.processOAuth2Login(email, name, googleId, AuthProvider.GOOGLE);
        String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getRole().name(), user.isBlocked());

        String redirectUrl = UriComponentsBuilder.fromUriString(frontendRedirectUri)
                .queryParam("token", token)
                .queryParam("refreshToken", user.getRefreshToken())
                .queryParam("userId", user.getId())
                .queryParam("username", URLEncoder.encode(user.getUsername(), StandardCharsets.UTF_8))
                .queryParam("totalXp", user.getTotalXp())
                .queryParam("rank", user.getRank())
                .queryParam("streakDays", user.getStreakDays())
                .queryParam("role", user.getRole().name())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
