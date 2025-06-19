package com.winnerx0.kiroku.dto;

import com.winnerx0.kiroku.models.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record PostCreateRequestDTO(@NotBlank(message = "Title cannot be blank") String title,
                                   @NotNull(message = "File required") MultipartFile file,
                                   @NotNull(message = "Status required") Status status) {
}
