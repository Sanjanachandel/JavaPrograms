package com.capg.rechargenova.filter;

import com.capg.rechargenova.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private GatewayFilterChain chain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPublicPath() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/users/login").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        
        when(chain.filter(exchange)).thenReturn(Mono.empty());
        
        GatewayFilter gatewayFilter = filter.apply(new JwtAuthenticationFilter.Config());
        Mono<Void> result = gatewayFilter.filter(exchange, chain);
        
        StepVerifier.create(result).verifyComplete();
        verify(chain, times(1)).filter(exchange);
    }

    @Test
    void testMissingHeader() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/users/profile").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        
        GatewayFilter gatewayFilter = filter.apply(new JwtAuthenticationFilter.Config());
        Mono<Void> result = gatewayFilter.filter(exchange, chain);
        
        StepVerifier.create(result).verifyComplete();
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void testValidToken() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/users/profile")
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid-token")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        
        when(jwtUtil.validateToken("valid-token")).thenReturn(true);
        when(jwtUtil.extractUserId("valid-token")).thenReturn(1L);
        when(jwtUtil.extractUsername("valid-token")).thenReturn("user@test.com");
        when(jwtUtil.extractRole("valid-token")).thenReturn("ROLE_USER");
        when(chain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());
        
        GatewayFilter gatewayFilter = filter.apply(new JwtAuthenticationFilter.Config());
        Mono<Void> result = gatewayFilter.filter(exchange, chain);
        
        StepVerifier.create(result).verifyComplete();
        verify(chain, times(1)).filter(any(ServerWebExchange.class));
    }

    @Test
    void testInvalidToken() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/users/profile")
                .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        
        when(jwtUtil.validateToken("invalid-token")).thenReturn(false);
        
        GatewayFilter gatewayFilter = filter.apply(new JwtAuthenticationFilter.Config());
        Mono<Void> result = gatewayFilter.filter(exchange, chain);
        
        StepVerifier.create(result).verifyComplete();
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }
}
