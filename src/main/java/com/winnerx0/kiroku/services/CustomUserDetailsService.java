package com.winnerx0.kiroku.services;

import com.winnerx0.kiroku.exceptions.NoUserFoundException;
import com.winnerx0.kiroku.models.User;
import com.winnerx0.kiroku.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return userRepository.findByUsername(username).orElseThrow(() -> new NoUserFoundException("No User Found"));
        } catch (NoUserFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
