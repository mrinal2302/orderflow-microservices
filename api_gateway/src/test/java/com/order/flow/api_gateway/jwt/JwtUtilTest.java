package com.order.flow.api_gateway.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private SecretKey testKey;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        testKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        ReflectionTestUtils.setField(jwtUtil, "key", testKey);
    }

    @Test
    void testValidToken() {
        String token = Jwts.builder()
                .setSubject("validuser")
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(testKey)
                .compact();

        assertTrue(jwtUtil.validateToken(token), "Valid token should be accepted");
    }

    @Test
    void testExtractUsername() {
        String token = Jwts.builder()
                .setSubject("myuser")
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(testKey)
                .compact();

        assertEquals("myuser", jwtUtil.extractUsername(token), "Username should match");
    }

    @Test
    void testExtractExpiration() {
        Date expiration = new Date(System.currentTimeMillis() + 5000);
        String token = Jwts.builder()
                .setSubject("testuser")
                .setExpiration(expiration)
                .signWith(testKey)
                .compact();

        Date extracted = jwtUtil.extractExpiration(token);

        assertTrue(Math.abs(extracted.getTime() - expiration.getTime()) < 1000,
                "Expiration date should be close (within 1 second)");
    }

    @Test
    void testExpiredToken() {
        Date pastDate = new Date(System.currentTimeMillis() - 3600000);
        String token = Jwts.builder()
                .setSubject("expireduser")
                .setExpiration(pastDate)
                .signWith(testKey)
                .compact();

        assertThrows(ExpiredJwtException.class, () -> {
            Jwts.parserBuilder().setSigningKey(testKey).build().parseClaimsJws(token);
        }, "Expired token should throw ExpiredJwtException");
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken("generatedUser");

        assertNotNull(token, "Generated token should not be null");
        assertEquals("generatedUser", jwtUtil.extractUsername(token), "Generated token should contain correct username");
        assertTrue(jwtUtil.validateToken(token), "Generated token should be valid");
    }

    @Test
    void testInvalidSignatureToken() {
        SecretKey anotherKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String token = Jwts.builder()
                .setSubject("hacker")
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(anotherKey)
                .compact();

        assertThrows(io.jsonwebtoken.security.SecurityException.class,
                () -> jwtUtil.extractUsername(token),
                "Token with invalid signature should throw exception");
    }
}
