package com.order.flow.api_gateway.controller;

import com.order.flow.api_gateway.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ValidCredentials_Success() {
        String username = "admin";
        String password = "password";
        String mockToken = "mock-jwt-token";

        when(jwtUtil.generateToken(username)).thenReturn(mockToken);

        ResponseEntity<?> response = authController.login(Map.of(
                "username", username,
                "password", password
        ));

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(mockToken, body.get("token"));

        verify(jwtUtil, times(1)).generateToken(username);
    }

    @Test
    void login_InvalidCredentials_Fail() {

        ResponseEntity<?> response = authController.login(Map.of(
                "username", "wrongUser",
                "password", "wrongPass"
        ));
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }
}
