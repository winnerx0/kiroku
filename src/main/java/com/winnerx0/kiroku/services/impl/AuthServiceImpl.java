package com.winnerx0.kiroku.services.impl;

import com.winnerx0.kiroku.dto.LoginRequestDTO;
import com.winnerx0.kiroku.dto.RegisterRequestDTO;
import com.winnerx0.kiroku.exceptions.NoDataFoundException;
import com.winnerx0.kiroku.models.User;
import com.winnerx0.kiroku.repositories.UserRepository;
import com.winnerx0.kiroku.services.AuthService;
import com.winnerx0.kiroku.services.JwtService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationProvider authenticationProvider;

    public AuthServiceImpl(PasswordEncoder passwordEncoder, JwtService jwtService,
                       UserRepository userRepository, AuthenticationProvider authenticationProvider){
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public String register(RegisterRequestDTO registerDTO){

        if(userRepository.findByEmail(registerDTO.email()).isPresent()){
            throw new EntityExistsException("User with email exists");
        }
        User user = new User();
        user.setEmail(registerDTO.email());
        user.setName(registerDTO.name());
        user.setPassword(passwordEncoder.encode(registerDTO.password()));
        userRepository.save(user);

        return jwtService.generateToken(user.getUsername());
    }

    @Override
    public String login(LoginRequestDTO loginDTO) {
        try {
            authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));

            return jwtService.generateToken(loginDTO.email());
        } catch (AuthenticationException e){
            throw new EntityNotFoundException("No User Found");
        }
    }

}
