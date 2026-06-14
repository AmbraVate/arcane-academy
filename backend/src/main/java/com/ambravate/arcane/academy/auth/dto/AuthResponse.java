package com.ambravate.arcane.academy.auth.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder (
    setterPrefix = "with",
    builderMethodName = "anAuthResponse"
)
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String userId;
    private String username;
    private int totalXp;
    private String rank;
    private int streakDays;
    private String role;
    private boolean bypassPaywall;
    private String subscriptionStatus;
    private boolean onboardingCompleted;
}
