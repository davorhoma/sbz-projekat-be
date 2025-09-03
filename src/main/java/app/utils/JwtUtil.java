package app.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private final Key SECRET_KEY = Keys.hmacShaKeyFor("mySuperSecretKeyForJWTs1234567890".getBytes());
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    public String generateToken(UUID userId, String email, String role) {
        return Jwts.builder()
        		.claim("userId", userId.toString())
                .claim("email", email)
                .claim("role", role)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) throws Exception {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new Exception("Invalid or expired token");
        }
    }

    public String extractEmail(String token) throws Exception {
        return extractClaims(token).get("email", String.class);
    }

    public String extractRole(String token) throws Exception {
        return extractClaims(token).get("role", String.class);
    }
    
    public UUID extractUserId(String token) throws Exception {
        String userIdString = extractClaims(token).get("userId", String.class);
        return UUID.fromString(userIdString);
    }

    public boolean isTokenExpired(String token) throws Exception {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
