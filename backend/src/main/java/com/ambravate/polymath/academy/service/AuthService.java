package com.ambravate.polymath.academy.service;

import com.ambravate.polymath.academy.dto.AuthResponse;
import com.ambravate.polymath.academy.dto.LoginRequest;
import com.ambravate.polymath.academy.dto.RegisterRequest;
import com.arcane.academy.dto.*;
import com.ambravate.polymath.academy.model.User;
import com.ambravate.polymath.academy.repository.UserRepository;
import com.ambravate.polymath.academy.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final StreakService streakService;
    private final BadgeService badgeService;

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

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        log.info("[Auth] Registered new user | userId={} username={}", user.getId(), user.getUsername());
        String token = jwtService.generateToken(user.getId(), user.getUsername());
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

        streakService.updateStreak(user.getId());
        badgeService.evaluateAndAward(user.getId());

        log.info("[Auth] Login success | userId={} username={} streak={} totalXp={}",
                user.getId(), user.getUsername(), user.getStreakDays(), user.getTotalXp());
        String token = jwtService.generateToken(user.getId(), user.getUsername());
        return buildResponse(user, token);
    }

    public User processOAuth2Login(String email, String name, String providerId,
                                    User.AuthProvider provider) {
        // Try to find by provider + providerId first
        var existing = userRepository.findByAuthProviderAndProviderId(provider, providerId);
        if (existing.isPresent()) {
            User user = existing.get();
            streakService.updateStreak(user.getId());
            badgeService.evaluateAndAward(user.getId());
            log.info("[Auth] OAuth2 returning user | userId={} provider={}", user.getId(), provider);
            return userRepository.findById(user.getId()).orElse(user);
        }

        // Try to find by email (account linking)
        var byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            user.setAuthProvider(provider);
            user.setProviderId(providerId);
            userRepository.save(user);
            streakService.updateStreak(user.getId());
            badgeService.evaluateAndAward(user.getId());
            log.info("[Auth] OAuth2 linked existing account | userId={} provider={}", user.getId(), provider);
            return userRepository.findById(user.getId()).orElse(user);
        }

        // Create new user
        String username = generateUniqueUsername(name);
        User user = User.builder()
                .username(username)
                .email(email)
                .authProvider(provider)
                .providerId(providerId)
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

    private AuthResponse buildResponse(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .totalXp(user.getTotalXp())
                .rank(user.getRank())
                .streakDays(user.getStreakDays())
                .build();
    }
}
