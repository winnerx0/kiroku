package com.winnerx0.kiroku.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record PostDTO(@NotBlank(message = "Title cannot be blank") String title, @NotNull(message = "File required") MultipartFile file) {
}
