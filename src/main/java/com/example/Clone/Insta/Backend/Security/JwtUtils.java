package com.example.Clone.Insta.Backend.Security;

import com.example.Clone.Insta.Backend.Entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    private final SecretKey key;

    private static final long ACCESS_TOKEN_VALIDITY =
            15 * 60 * 1000L; // 15 minutes

    public JwtUtils(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateAccessToken(User user) {

        Date now = new Date();

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userid", user.getId())
                .claim("type", "access")
                .setIssuedAt(now)
                .setExpiration(
                        new Date(now.getTime() + ACCESS_TOKEN_VALIDITY)
                )
                .signWith(key)
                .compact();
    }

    public SecretKey getKey() {
        return key;
    }
}