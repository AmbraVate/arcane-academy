package com.ambravate.arcane.academy.auth.service;

import com.ambravate.arcane.academy.common.domain.AuthProvider;
import com.ambravate.arcane.academy.auth.dto.AuthResponse;
import com.ambravate.arcane.academy.auth.dto.LoginRequest;
import com.ambravate.arcane.academy.auth.dto.RegisterRequest;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.events.UserEngagedEvent;
import com.ambravate.arcane.academy.common.repository.UserRepository;
import com.ambravate.arcane.academy.common.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ApplicationEventPublisher eventPublisher;

    public AuthResponse register(RegisterRequest request) {
        log.info("[Auth] Register attempt | username={} email={}",
                request.getUsername(), request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("[Auth] Register failed — email already exists: {}", request.getEmail());
            throw new IllegalArgumentException("Email already registered.");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("[Auth] Register failed — username already taken: {}", request.getUsername());
            throw new IllegalArgumentException("Username already taken.");
        }

        User user = User.aUser()
                .withUsername(request.getUsername())
                .withEmail(request.getEmail())
                .withPasswordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        user.setRefreshToken(UUID.randomUUID().toString());
        userRepository.save(user);
        log.info("[Auth] Registered new user | userId={} username={}", user.getId(), user.getUsername());
        String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getRole().name(), false);
        return buildResponse(user, token);
    }

    public AuthResponse login(LoginRequest request) {
        log.info("[Auth] Login attempt | email={}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("[Auth] Login failed — email not found: {}", request.getEmail());
                    return new BadCredentialsException("Invalid credentials.");
                });

        if (user.getPasswordHash() == null) {
            log.warn("[Auth] Login failed — user has no password (OAuth account) | userId={}", user.getId());
            throw new BadCredentialsException("This account uses Google sign-in. Please log in with Google.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            log.warn("[Auth] Login failed — wrong password | userId={}", user.getId());
            throw new BadCredentialsException("Invalid credentials.");
        }

        if (user.isBlocked()) {
            log.warn("[Auth] Login rejected — account blocked | userId={}", user.getId());
            throw new IllegalStateException("Account is blocked. Please contact support.");
        }

        user.setRefreshToken(UUID.randomUUID().toString());
        userRepository.save(user);
        eventPublisher.publishEvent(new UserEngagedEvent(user.getId()));

        log.info("[Auth] Login success | userId={} username={} streak={} totalXp={}",
                user.getId(), user.getUsername(), user.getStreakDays(), user.getTotalXp());
        String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getRole().name(), false);
        return buildResponse(user, token);
    }

    public User processOAuth2Login(
        String email,
        String name,
        String providerId,
        AuthProvider provider
    ) {
        // Try to find by provider + providerId first
        var existing = userRepository.findByAuthProviderAndProviderId(provider, providerId);
        if (existing.isPresent()) {
            User user = existing.get();
            if (user.isBlocked()) {
                log.warn("[Auth] OAuth2 rejected — account blocked | userId={}", user.getId());
                throw new IllegalStateException("Account is blocked. Please contact support.");
            }
            user.setRefreshToken(UUID.randomUUID().toString());
            userRepository.save(user);
            eventPublisher.publishEvent(new UserEngagedEvent(user.getId()));
            log.info("[Auth] OAuth2 returning user | userId={} provider={}", user.getId(), provider);
            return user;
        }

        // Try to find by email (account linking)
        var byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            if (user.isBlocked()) {
                log.warn("[Auth] OAuth2 rejected — account blocked (email match) | userId={}", user.getId());
                throw new IllegalStateException("Account is blocked. Please contact support.");
            }
            user.setAuthProvider(provider);
            user.setProviderId(providerId);
            user.setRefreshToken(UUID.randomUUID().toString());
            userRepository.save(user);
            eventPublisher.publishEvent(new UserEngagedEvent(user.getId()));
            log.info("[Auth] OAuth2 linked existing account | userId={} provider={}", user.getId(), provider);
            return user;
        }

        // Create new user
        String username = generateUniqueUsername(name);
        User user = User.aUser()
                .withUsername(username)
                .withEmail(email)
                .withAuthProvider(provider)
                .withProviderId(providerId)
                .withRefreshToken(UUID.randomUUID().toString())
                .build();
        userRepository.save(user);
        log.info("[Auth] OAuth2 new user created | userId={} username={} provider={}", user.getId(), username, provider);
        return user;
    }

    private String generateUniqueUsername(String name) {
        String base = name.replaceAll("[^a-zA-Z0-9_]", "").toLowerCase();
        if (base.isEmpty()) base = "wizard";
        if (base.length() > 20) base = base.substring(0, 20);

        String candidate = base;
        int suffix = 1;
        while (userRepository.existsByUsername(candidate)) {
            candidate = base + suffix++;
        }
        return candidate;
    }

    public AuthResponse refreshToken(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
        if (user.isBlocked()) throw new IllegalStateException("Account is blocked");
        // Rotate the refresh token on every use
        user.setRefreshToken(UUID.randomUUID().toString());
        userRepository.save(user);
        String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getRole().name(), false);
        return buildResponse(user, token);
    }

    private AuthResponse buildResponse(User user, String token) {
        return AuthResponse.anAuthResponse()
                .withToken(token)
                .withRefreshToken(user.getRefreshToken())
                .withUserId(user.getId())
                .withUsername(user.getUsername())
                .withTotalXp(user.getTotalXp())
                .withRank(user.getRank())
                .withStreakDays(user.getStreakDays())
                .withRole(user.getRole().name())
                .build();
    }
}
