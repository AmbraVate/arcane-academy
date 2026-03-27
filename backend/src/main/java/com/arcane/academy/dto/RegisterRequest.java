// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.arcane.academy.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
    @NotBlank @Size(min = 3, max = 30)
    private String username;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 8)
    private String password;
}
