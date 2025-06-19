package com.winnerx0.kiroku.services;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

public interface JwtService {

    String generateToken(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    Date extractExpiration(String token);

    String extractUsername(String token);

}
