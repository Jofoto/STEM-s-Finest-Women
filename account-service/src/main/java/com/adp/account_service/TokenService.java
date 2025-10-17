package com.adp.account_service;

import java.sql.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
//import io.jsonwebtoken.*;


@Component
public class TokenService{

    private final SecretKey key;

    public TokenService(@Value("${jwt.secret}") String secret) {
        
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    public String GenerateToken(String subject) {
        return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(new Date(0))
        .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) //1hr
        .signWith(key)
        .compact();
    }

    public String validateToken(String token) {
        return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
    }
}
