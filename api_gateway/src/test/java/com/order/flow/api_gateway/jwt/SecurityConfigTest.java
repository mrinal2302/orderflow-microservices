package com.order.flow.api_gateway.jwt;

import com.order.flow.api_gateway.controller.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = AuthController.class)
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockitoBean
    private JwtAuthenticationManager jwtAuthenticationManager;
    @MockitoBean
    private JwtSecurityContextRepository jwtSecurityContextRepository;
    @MockitoBean
    private JwtUtil jwtUtil;

    @BeforeEach
    void stubSecurityContextRepository() {
        when(jwtSecurityContextRepository.load(any())).thenReturn(Mono.empty());
    }

    @Test
    void testLoginEndpoint_PermitAll_POST() {
        when(jwtUtil.generateToken("admin")).thenReturn("fake-jwt");

        webTestClient.post()
                .uri("/auth/login")
                .bodyValue(Map.of("username", "admin", "password", "password"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").isEqualTo("fake-jwt");
    }

    @Test
    void testSecuredEndpoint_UnauthorizedWithoutToken() {
        webTestClient.get()
                .uri("/auth/secured")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testSecuredEndpoint_AuthorizedWithValidToken() {
        when(jwtSecurityContextRepository.load(any()))
                .thenReturn(Mono.just(new SecurityContextImpl(
                        new UsernamePasswordAuthenticationToken(
                                "admin", null,
                                java.util.List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        )
                )));

        webTestClient.get()
                .uri("/auth/secured")
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid-token")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Access granted! Token is valid. Working properly :)");
    }
}
