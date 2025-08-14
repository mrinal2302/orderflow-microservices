package com.order.flow.api_gateway.jwt;

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
/*
    @Test
    void testExpiredToken() {
        String token = Jwts.builder()
                .setSubject("testuser")
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // expired
                .signWith(testKey)
                .compact();

        assertFalse(jwtUtil.validateToken(token), "Expired token should be invalid");
    }

    @Test
    void testExtractExpiration() {
        Date expectedExpiration = new Date(System.currentTimeMillis() + 3600000); // +1 hour
        String token = Jwts.builder()
                .setSubject("testuser")
                .setExpiration(expectedExpiration)
                .signWith(testKey)
                .compact();

        assertEquals(expectedExpiration, jwtUtil.extractExpiration(token),
                "Extracted expiration should match the expected date");
    }*/

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
}
