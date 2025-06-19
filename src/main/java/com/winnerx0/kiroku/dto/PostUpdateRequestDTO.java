package com.winnerx0.kiroku.dto;

import com.winnerx0.kiroku.models.Status;
import org.springframework.web.multipart.MultipartFile;

public record PostUpdateRequestDTO(
        String id,
        String title,
        MultipartFile file,
        Status status
) {
}
