package com.winnerx0.kiroku.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") String email,
        @NotBlank(message = "Password cannot be blank") @Size(min = 8, message = "Password must be at least 8 characters") String password,
        @NotBlank(message = "Name cannot be blank") String name
) {
}
