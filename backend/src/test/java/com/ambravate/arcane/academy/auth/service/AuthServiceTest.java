package com.ambravate.arcane.academy.auth.service;

import com.ambravate.arcane.academy.auth.dto.AuthResponse;
import com.ambravate.arcane.academy.auth.dto.LoginRequest;
import com.ambravate.arcane.academy.auth.dto.RegisterRequest;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.common.domain.AuthProvider;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.events.UserEngagedEvent;
import com.ambravate.arcane.academy.common.exception.UserNotFoundException;
import com.ambravate.arcane.academy.common.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link AuthService} — verifies registration, login, OAuth2 linking,
 * token refresh, and the username-availability helpers.
 * <p>
 * All persistence and infrastructure dependencies are mocked; no Spring context
 * is started (pure Mockito unit tests).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService")
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks private AuthService service;

    // ── Helpers ─────────────────────────────────────────────────────────────────

    private User localUser(String id, String username, String email, String passwordHash) {
        return User.aUser()
                .withId(id)
                .withUsername(username)
                .withEmail(email)
                .withPasswordHash(passwordHash)
                .withAuthProvider(AuthProvider.LOCAL)
                .build();
    }

    private User blockedUser(String id, String email) {
        return User.aUser()
                .withId(id)
                .withUsername("blocked-user")
                .withEmail(email)
                .withPasswordHash("hash")
                .withBlocked(true)
                .build();
    }

    /** save() in tests doesn't call Hibernate — assign a synthetic ID via answer. */
    private void stubSaveWithId(String id) {
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(id);
            u.setRefreshToken(u.getRefreshToken() != null ? u.getRefreshToken() : "rt-stub");
            return u;
        });
    }

    // ── register ────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("register()")
    class Register {

        @Test
        @DisplayName("Happy path — saves user and returns populated AuthResponse")
        void happyPath() {
            when(userRepository.existsByEmail("alice@test")).thenReturn(false);
            when(userRepository.existsByUsername("alice")).thenReturn(false);
            when(passwordEncoder.encode("secret123")).thenReturn("hashed");
            stubSaveWithId("u-alice");
            when(jwtService.generateToken(eq("u-alice"), eq("alice"), anyString(), eq(false)))
                    .thenReturn("jwt-token");

            AuthResponse resp = service.register(new RegisterRequest("alice", "alice@test", "secret123"));

            assertThat(resp.getToken()).isEqualTo("jwt-token");
            assertThat(resp.getUsername()).isEqualTo("alice");
            assertThat(resp.getUserId()).isEqualTo("u-alice");
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Throws when email already registered")
        void emailTaken() {
            when(userRepository.existsByEmail("dup@test")).thenReturn(true);

            assertThatThrownBy(() -> service.register(new RegisterRequest("newuser", "dup@test", "pass1234")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Email already registered");

            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Throws when username already taken")
        void usernameTaken() {
            when(userRepository.existsByEmail("new@test")).thenReturn(false);
            when(userRepository.existsByUsername("taken")).thenReturn(true);

            assertThatThrownBy(() -> service.register(new RegisterRequest("taken", "new@test", "pass1234")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Username already taken");

            verify(userRepository, never()).save(any());
        }
    }

    // ── login ───────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("login()")
    class Login {

        @Test
        @DisplayName("Happy path — valid credentials return AuthResponse and publish UserEngagedEvent")
        void happyPath() {
            User alice = localUser("u-alice", "alice", "alice@test", "hashed");
            when(userRepository.findByEmail("alice@test")).thenReturn(Optional.of(alice));
            when(passwordEncoder.matches("secret123", "hashed")).thenReturn(true);
            when(userRepository.save(any())).thenReturn(alice);
            when(jwtService.generateToken(anyString(), anyString(), anyString(), eq(false)))
                    .thenReturn("jwt-token");

            AuthResponse resp = service.login(new LoginRequest("alice@test", "secret123"));

            assertThat(resp.getToken()).isEqualTo("jwt-token");
            assertThat(resp.getUsername()).isEqualTo("alice");

            ArgumentCaptor<UserEngagedEvent> eventCaptor = ArgumentCaptor.forClass(UserEngagedEvent.class);
            verify(eventPublisher).publishEvent(eventCaptor.capture());
            assertThat(eventCaptor.getValue().userId()).isEqualTo("u-alice");
        }

        @Test
        @DisplayName("Throws UserNotFoundException when email is not registered")
        void emailNotFound() {
            when(userRepository.findByEmail("nobody@test")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.login(new LoginRequest("nobody@test", "pass")))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        @DisplayName("Throws BadCredentialsException for OAuth-only account (no password hash)")
        void oauthAccountHasNoPassword() {
            User oauthUser = User.aUser()
                    .withId("u-oauth")
                    .withUsername("googleuser")
                    .withEmail("g@test")
                    .withAuthProvider(AuthProvider.GOOGLE)
                    .build(); // passwordHash is null
            when(userRepository.findByEmail("g@test")).thenReturn(Optional.of(oauthUser));

            assertThatThrownBy(() -> service.login(new LoginRequest("g@test", "pass")))
                    .isInstanceOf(BadCredentialsException.class)
                    .hasMessageContaining("Google sign-in");
        }

        @Test
        @DisplayName("Throws BadCredentialsException when password does not match")
        void wrongPassword() {
            User alice = localUser("u-alice", "alice", "alice@test", "hashed");
            when(userRepository.findByEmail("alice@test")).thenReturn(Optional.of(alice));
            when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

            assertThatThrownBy(() -> service.login(new LoginRequest("alice@test", "wrong")))
                    .isInstanceOf(BadCredentialsException.class)
                    .hasMessageContaining("Invalid credentials");
        }

        @Test
        @DisplayName("Throws IllegalStateException when account is blocked")
        void blockedUserCannotLogin() {
            User blocked = blockedUser("u-b", "blocked@test");
            when(userRepository.findByEmail("blocked@test")).thenReturn(Optional.of(blocked));
            when(passwordEncoder.matches("pass", "hash")).thenReturn(true);

            assertThatThrownBy(() -> service.login(new LoginRequest("blocked@test", "pass")))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("blocked");

            verify(eventPublisher, never()).publishEvent(any());
        }
    }

    // ── processOAuth2Login ───────────────────────────────────────────────────────

    @Nested
    @DisplayName("processOAuth2Login()")
    class ProcessOAuth2Login {

        @Test
        @DisplayName("Returns existing user when matched by provider + providerId")
        void returningUser() {
            User existing = User.aUser()
                    .withId("u-g")
                    .withUsername("googleuser")
                    .withEmail("g@test")
                    .withAuthProvider(AuthProvider.GOOGLE)
                    .withProviderId("google-123")
                    .build();
            when(userRepository.findByAuthProviderAndProviderId(AuthProvider.GOOGLE, "google-123"))
                    .thenReturn(Optional.of(existing));
            when(userRepository.save(any())).thenReturn(existing);

            User result = service.processOAuth2Login("g@test", "Google User", "google-123", AuthProvider.GOOGLE);

            assertThat(result.getId()).isEqualTo("u-g");
            verify(eventPublisher).publishEvent(any(UserEngagedEvent.class));
        }

        @Test
        @DisplayName("Throws when returning OAuth2 user is blocked")
        void returningUserBlocked() {
            User blocked = User.aUser()
                    .withId("u-b")
                    .withUsername("blocked")
                    .withEmail("b@test")
                    .withAuthProvider(AuthProvider.GOOGLE)
                    .withProviderId("google-blocked")
                    .withBlocked(true)
                    .build();
            when(userRepository.findByAuthProviderAndProviderId(AuthProvider.GOOGLE, "google-blocked"))
                    .thenReturn(Optional.of(blocked));

            assertThatThrownBy(() -> service.processOAuth2Login("b@test", "Blocked", "google-blocked", AuthProvider.GOOGLE))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("blocked");

            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        @DisplayName("Refuses to link when email matches a local-password account (account-takeover guard)")
        void accountLinking() {
            User localAlice = localUser("u-alice", "alice", "alice@test", "hashed");
            when(userRepository.findByAuthProviderAndProviderId(AuthProvider.GOOGLE, "google-abc"))
                    .thenReturn(Optional.empty());
            when(userRepository.findByEmail("alice@test")).thenReturn(Optional.of(localAlice));

            assertThatThrownBy(() -> service.processOAuth2Login("alice@test", "Alice", "google-abc", AuthProvider.GOOGLE))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("already exists");

            assertThat(localAlice.getAuthProvider()).isEqualTo(AuthProvider.LOCAL);
            verify(userRepository, never()).save(any());
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        @DisplayName("Links OAuth2 to email-matched account that has no local password")
        void accountLinkingWithoutPassword() {
            User passwordless = localUser("u-alice", "alice", "alice@test", null);
            when(userRepository.findByAuthProviderAndProviderId(AuthProvider.GOOGLE, "google-abc"))
                    .thenReturn(Optional.empty());
            when(userRepository.findByEmail("alice@test")).thenReturn(Optional.of(passwordless));
            when(userRepository.save(any())).thenReturn(passwordless);

            User result = service.processOAuth2Login("alice@test", "Alice", "google-abc", AuthProvider.GOOGLE);

            assertThat(result.getId()).isEqualTo("u-alice");
            assertThat(passwordless.getAuthProvider()).isEqualTo(AuthProvider.GOOGLE);
            assertThat(passwordless.getProviderId()).isEqualTo("google-abc");
            verify(eventPublisher).publishEvent(any(UserEngagedEvent.class));
        }

        @Test
        @DisplayName("Throws when email-matched account is blocked")
        void linkedAccountBlocked() {
            User blocked = blockedUser("u-b", "b@test");
            when(userRepository.findByAuthProviderAndProviderId(AuthProvider.GOOGLE, "gid"))
                    .thenReturn(Optional.empty());
            when(userRepository.findByEmail("b@test")).thenReturn(Optional.of(blocked));

            assertThatThrownBy(() -> service.processOAuth2Login("b@test", "Blocked", "gid", AuthProvider.GOOGLE))
                    .isInstanceOf(IllegalStateException.class);

            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        @DisplayName("Creates a new user when no existing account is found")
        void newUser() {
            when(userRepository.findByAuthProviderAndProviderId(AuthProvider.GOOGLE, "gid-new"))
                    .thenReturn(Optional.empty());
            when(userRepository.findByEmail("new@test")).thenReturn(Optional.empty());
            when(userRepository.existsByUsername("newuser")).thenReturn(false);
            when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            User result = service.processOAuth2Login("new@test", "New User", "gid-new", AuthProvider.GOOGLE);

            assertThat(result.getEmail()).isEqualTo("new@test");
            assertThat(result.getAuthProvider()).isEqualTo(AuthProvider.GOOGLE);
            verify(userRepository).save(any(User.class));
            // New OAuth2 user registration does not fire UserEngagedEvent — only logins do
            verify(eventPublisher, never()).publishEvent(any());
        }

        @Test
        @DisplayName("Appends numeric suffix to resolve username collision")
        void usernameCollisionSuffix() {
            when(userRepository.findByAuthProviderAndProviderId(any(), anyString()))
                    .thenReturn(Optional.empty());
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
            // "alice" is taken; "alice1" is free
            when(userRepository.existsByUsername("alice")).thenReturn(true);
            when(userRepository.existsByUsername("alice1")).thenReturn(false);
            when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            User result = service.processOAuth2Login("alice2@test", "Alice!", "gid-collision", AuthProvider.GOOGLE);

            assertThat(result.getUsername()).isEqualTo("alice1");
        }
    }

    // ── refreshToken ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("refreshToken()")
    class RefreshToken {

        @Test
        @DisplayName("Happy path — rotates token and returns new AuthResponse")
        void happyPath() {
            User alice = localUser("u-alice", "alice", "alice@test", "hashed");
            alice.setRefreshToken("old-rt");
            when(userRepository.findByRefreshToken("old-rt")).thenReturn(Optional.of(alice));
            when(userRepository.save(any())).thenReturn(alice);
            when(jwtService.generateToken(anyString(), anyString(), anyString(), eq(false)))
                    .thenReturn("new-jwt");

            AuthResponse resp = service.refreshToken("old-rt");

            assertThat(resp.getToken()).isEqualTo("new-jwt");
            // Refresh token must be rotated (not the same value)
            assertThat(alice.getRefreshToken()).isNotEqualTo("old-rt");
        }

        @Test
        @DisplayName("Throws BadCredentialsException for unknown refresh token")
        void tokenNotFound() {
            when(userRepository.findByRefreshToken("bogus")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.refreshToken("bogus"))
                    .isInstanceOf(BadCredentialsException.class)
                    .hasMessageContaining("Invalid refresh token");
        }

        @Test
        @DisplayName("Throws IllegalStateException when account is blocked")
        void blockedAccountCannotRefresh() {
            User blocked = blockedUser("u-b", "b@test");
            blocked.setRefreshToken("rt-b");
            when(userRepository.findByRefreshToken("rt-b")).thenReturn(Optional.of(blocked));

            assertThatThrownBy(() -> service.refreshToken("rt-b"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("blocked");
        }
    }

    // ── availability helpers ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("availability helpers")
    class Availability {

        @Test
        @DisplayName("isUsernameAvailable returns true when username is free")
        void usernameFree() {
            when(userRepository.existsByUsername("free")).thenReturn(false);
            assertThat(service.isUsernameAvailable("free")).isTrue();
        }

        @Test
        @DisplayName("isUsernameAvailable returns false when username is taken")
        void usernameTaken() {
            when(userRepository.existsByUsername("taken")).thenReturn(true);
            assertThat(service.isUsernameAvailable("taken")).isFalse();
        }

        @Test
        @DisplayName("isEmailAvailable returns true when email is free")
        void emailFree() {
            when(userRepository.existsByEmail("free@test")).thenReturn(false);
            assertThat(service.isEmailAvailable("free@test")).isTrue();
        }

        @Test
        @DisplayName("isEmailAvailable returns false when email is taken")
        void emailTaken() {
            when(userRepository.existsByEmail("dup@test")).thenReturn(true);
            assertThat(service.isEmailAvailable("dup@test")).isFalse();
        }
    }
}
