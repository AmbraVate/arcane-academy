package com.ambravate.polymath.academy.dto;
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
    private String userId;
    private String username;
    private int totalXp;
    private String rank;
    private int streakDays;
}
