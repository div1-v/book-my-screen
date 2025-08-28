package com.springboot.bookmyscreen.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtService {

    private static final SecretKey KEY = Keys.hmacShaKeyFor(
            System.getenv().getOrDefault("JWT_SECRET",
                    "this-is-a-very-long-and-secure-secret-key-that-is-64-bytes-minimum-123456").getBytes());

    private final ConcurrentHashMap<String, Boolean> blacklist = new ConcurrentHashMap<>();

    // ✅ Default token TTL = 1 hour (3600 seconds)
    private static final long DEFAULT_TTL = 3600;

    // Token TTL (in seconds), can be overridden from application.properties
    private final long ttlSeconds;

    public JwtService(@Value("${jwt.ttl:" + DEFAULT_TTL + "}") long ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
    }

    public String issue(String subject) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(subject)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(ttlSeconds)))
                .signWith(KEY, Jwts.SIG.HS512)
                .compact();
    }

    public JwtPayload validate(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(KEY)
                    .clockSkewSeconds(60)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            if (isRevoked(claims.getId()))
                throw new JwtException("Token revoked");

            return new JwtPayload(
                    claims.getId(),
                    claims.getSubject(),
                    claims.getExpiration().toInstant()
            );
        } catch (JwtException e) {
            throw new JwtAuthenticationException("Invalid JWT", e);
        }
    }

    public void revoke(String jti) { blacklist.put(jti, true); }
    public boolean isRevoked(String jti) { return blacklist.getOrDefault(jti, false); }

    public record JwtPayload(String jti, String sub, Instant exp) {}

    public static class JwtAuthenticationException extends RuntimeException {
        public JwtAuthenticationException(String msg, Throwable t) { super(msg, t); }
    }
}
