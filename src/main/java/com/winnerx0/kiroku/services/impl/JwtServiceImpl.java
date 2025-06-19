package com.winnerx0.kiroku.services.impl;

import com.winnerx0.kiroku.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {


    private final String SECRET_KEY = "e1aeeebb61f38877e007589e55e5ed4abf91eb2b02e1195420f7320e7ba22e2f";

    private final long EXPIRATION = 1000 * 60 * 60 * 24 * 7;

    private Key getSigningKey(){
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(String username){
        return buildToken(username);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails){
        return userDetails.getUsername().equals(extractUsername(token)) &&  isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).after(new Date());
    }

    private String buildToken(String username){
        return Jwts
                .builder()
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .setIssuedAt(new Date())
                .setSubject(username)
                .compact();
    }

    @Override
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
