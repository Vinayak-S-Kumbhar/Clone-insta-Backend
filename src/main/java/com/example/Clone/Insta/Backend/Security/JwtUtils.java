package com.example.Clone.Insta.Backend.Security;

import com.example.Clone.Insta.Backend.Entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    private final String SECRET = "suasge5vq6s5e4ys77ne6271b45489bvsr456g84v575541g6123ty5uev5e4wer657e55tvr54566";

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String genrateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userid", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24))
                .signWith(key)
                .compact();
    }

    public SecretKey getKey() {
        return key;
    }
}
