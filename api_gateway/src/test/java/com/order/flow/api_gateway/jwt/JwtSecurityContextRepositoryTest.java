package com.order.flow.api_gateway.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.adapter.DefaultServerWebExchange;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtSecurityContextRepositoryTest {

    @Mock
    private JwtAuthenticationManager authenticationManager;

    private JwtSecurityContextRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new JwtSecurityContextRepository();
        repository.authenticationManager = authenticationManager;
    }

    @Test
    void load_WithValidBearerToken_ShouldReturnSecurityContext() {
        String token = "validToken";
        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        Authentication authentication = new UsernamePasswordAuthenticationToken("user", null);
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(Mono.just(authentication));

        StepVerifier.create(repository.load(exchange))
                .expectNextMatches(context -> context.getAuthentication().getPrincipal().equals("user"))
                .verifyComplete();

        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
    }

    @Test
    void load_WithNoAuthorizationHeader_ShouldReturnEmpty() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(repository.load(exchange))
                .verifyComplete();

        verify(authenticationManager, never()).authenticate(any(Authentication.class));
    }

    @Test
    void load_WithInvalidBearerPrefix_ShouldReturnEmpty() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/")
                .header(HttpHeaders.AUTHORIZATION, "Token something")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(repository.load(exchange))
                .verifyComplete();

        verify(authenticationManager, never()).authenticate(any(Authentication.class));
    }
}
