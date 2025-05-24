package com.winnerx0.kiroku.responses;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String message;
    private LocalTime timestamp = LocalTime.now();
}
