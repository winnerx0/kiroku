package com.winnerx0.kiroku.services;

import com.winnerx0.kiroku.dto.LoginRequestDTO;
import com.winnerx0.kiroku.dto.RegisterRequestDTO;

public interface AuthService {

    String register(RegisterRequestDTO registerRequestDTO);

    String login(LoginRequestDTO loginRequestDTO);

}
