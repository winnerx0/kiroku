package com.winnerx0.kiroku.controllers;

import com.winnerx0.kiroku.dto.LoginRequestDTO;
import com.winnerx0.kiroku.dto.RegisterRequestDTO;
import com.winnerx0.kiroku.exceptions.NoDataFoundException;
import com.winnerx0.kiroku.responses.AuthResponse;
import com.winnerx0.kiroku.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequestDTO registerDTO){
        String token = authService.register(registerDTO);
        AuthResponse response = new AuthResponse("Success", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequestDTO loginDTO) {
        String token = authService.login(loginDTO);
        AuthResponse response = new AuthResponse("Success", token);
        return ResponseEntity.ok(response);
    }
}
