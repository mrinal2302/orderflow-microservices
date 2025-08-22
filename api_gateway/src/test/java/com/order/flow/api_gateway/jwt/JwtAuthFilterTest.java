package com.order.flow.api_gateway.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtAuthFilterTest {
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private GatewayFilterChain chain;
    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginPathBypassesJwtValidation() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/auth/login").build()
        );
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        Mono<Void> result = jwtAuthFilter.filter(exchange, chain);

        verify(chain, times(1)).filter(exchange);

    }

    @Test
    void testMissingAuthorizationHeaderReturnsUnauthorized() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/orders").build()
        );
        Mono<Void> result = jwtAuthFilter.filter(exchange, chain);
        result.block();
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void testValidTokenProceedsFilterChain() {
        String token = "valid.jwt.token";
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build()
        );

        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        Mono<Void> result = jwtAuthFilter.filter(exchange, chain);

        assertDoesNotThrow(() -> result.block());
        verify(chain, times(1)).filter(exchange);
        verify(jwtUtil, times(1)).validateToken(token);
    }

    @Test
    void testInvalidTokenReturnsUnauthorized() {
        String token = "invalid.jwt.token";
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build()
        );

        when(jwtUtil.validateToken(token)).thenReturn(false);

        Mono<Void> result = jwtAuthFilter.filter(exchange, chain);
        result.block();
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        verify(jwtUtil, times(1)).validateToken(token);
    }
}
