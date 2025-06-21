package com.winnerx0.kiroku.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String message;
}
