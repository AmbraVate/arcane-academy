package com.arcane.academy.service;

import com.arcane.academy.dto.*;
import com.arcane.academy.model.User;
import com.arcane.academy.repository.UserRepository;
import com.arcane.academy.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final StreakService streakService;

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

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            log.warn("[Auth] Login failed — wrong password | userId={}", user.getId());
            throw new BadCredentialsException("Invalid credentials.");
        }

        streakService.updateStreak(user.getId());

        log.info("[Auth] Login success | userId={} username={} streak={} totalXp={}",
                user.getId(), user.getUsername(), user.getStreakDays(), user.getTotalXp());
        String token = jwtService.generateToken(user.getId(), user.getUsername());
        return buildResponse(user, token);
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
