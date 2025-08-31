package com.order.flow.api_gateway.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class JwtAuthenticationManagerTest {
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private JwtAuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_ValidToken_ReturnsAuthentication_Success() {
        String token = "valid-token";
        String username = "testUser";

        Authentication auth = new UsernamePasswordAuthenticationToken(null, token);

        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(jwtUtil.validateToken(token)).thenReturn(true);

        Mono<Authentication> result = authenticationManager.authenticate(auth);

        verify(jwtUtil).extractUsername(token);
        verify(jwtUtil).validateToken(token);
    }

    @Test
    void authenticate_InvalidToken_ReturnsEmptyMono_Failure() {
        String token = "invalid-token";
        Authentication auth = new UsernamePasswordAuthenticationToken(null, token);

        when(jwtUtil.extractUsername(token)).thenReturn(null);

        Mono<Authentication> result = authenticationManager.authenticate(auth);

        verify(jwtUtil).extractUsername(token);
        verify(jwtUtil, never()).validateToken(token);
    }

    @Test
    void authenticate_ExceptionThrown() {
        String token = "bad-token";
        Authentication authRequest = new UsernamePasswordAuthenticationToken(null, token);

        when(jwtUtil.extractUsername(token)).thenThrow(new RuntimeException("JWT parsing failed"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authenticationManager.authenticate(authRequest));

        assertEquals("JWT parsing failed", exception.getMessage());
        verify(jwtUtil).extractUsername(token);
    }
}
