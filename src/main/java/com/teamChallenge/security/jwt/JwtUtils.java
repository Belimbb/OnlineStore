package com.teamChallenge.security.jwt;

import com.teamChallenge.entity.Users.UserEntity;
import com.teamChallenge.exception.exceptions.generalExceptions.BadJWTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;


    public String generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        String roles = user.getRole().toString();
        claims.put("id", user.getId());
        claims.put("roles", roles);
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());

        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + jwtLifetime.toMillis());
        return Jwts.builder()
                .claims(claims)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getClaims(String token) throws BadJWTException {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new BadJWTException();
        }
        return claims;
    }

    public Long getUserId(String token) throws BadJWTException {
        return Long.parseLong(getClaims(token).get("id", String.class));
    }

    public String getRole(String token) throws BadJWTException {
        return getClaims(token).get("roles", String.class);
    }

    public String getUsername(String token) throws BadJWTException {
        return getClaims(token).get("username", String.class);
    }

    public String getEmail(String token) throws BadJWTException {
        return getClaims(token).get("email", String.class);
    }
}
