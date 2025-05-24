package com.winnerx0.kiroku.responses;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthResponse {
    private final String message;
    private final String token;
    private LocalTime timestamp = LocalTime.now();
}
