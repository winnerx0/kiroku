package com.winnerx0.kiroku.services;

import com.winnerx0.kiroku.dto.LoginDTO;
import com.winnerx0.kiroku.dto.RegisterDTO;
import com.winnerx0.kiroku.exceptions.NoDataFoundException;
import com.winnerx0.kiroku.models.User;
import com.winnerx0.kiroku.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationProvider authenticationProvider;

    public AuthService(PasswordEncoder passwordEncoder, JwtService jwtService,
                       UserRepository userRepository, AuthenticationProvider authenticationProvider){
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.authenticationProvider = authenticationProvider;
    }

    public String register(RegisterDTO registerDTO){

        if(userRepository.findByUsername(registerDTO.username()).isPresent()){
            throw new RuntimeException("User exists");
        }
        User user = new User();
        user.setUsername(registerDTO.username());
        user.setPassword(passwordEncoder.encode(registerDTO.password()));
        userRepository.save(user);

        return jwtService.generateToken(user.getUsername());
    }

    public String login(LoginDTO loginDTO) throws NoDataFoundException {
        try {
            authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password())).getName();

            return jwtService.generateToken(loginDTO.username());
        } catch (AuthenticationException e){
            throw new NoDataFoundException("No User Found");
        }
    }

}
