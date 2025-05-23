package com.winnerx0.kiroku.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.Authentication;

public record LoginDTO(
        @NotBlank(message = "Username cannot be blank") String username,
        @NotBlank(message = "Password cannot be blank") String password
) {
}
