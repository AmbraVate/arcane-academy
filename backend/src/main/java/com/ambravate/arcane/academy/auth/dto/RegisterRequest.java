package com.ambravate.arcane.academy.auth.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
    @NotBlank @Size(min = 3, max = 30)
    private String username;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 20, message = "Password must be at least 20 characters — try a passphrase")
    private String password;
}
