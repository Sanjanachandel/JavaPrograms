package com.capg.rechargenova.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.capg.rechargenova.util.JwtUtil;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    @Autowired
    private JwtUtil jwtUtil;

    public JwtAuthenticationFilter() {
        super(Config.class); // ✅ Correct (no setName)
    }

    public static class Config {
        // future config if needed
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String path = exchange.getRequest().getURI().getPath();

            // ✅ Allow public endpoints (only for GET if it's a sensitive path)
            if (isPublicPath(path, exchange.getRequest().getMethod().name())) {
                return chain.filter(exchange);
            }

            // ✅ Get Authorization header
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            try {
                // ✅ Validate token
                if (!jwtUtil.validateToken(token)) {
                    return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
                }

                // ✅ Extract user details
                Long userId = jwtUtil.extractUserId(token);
                String email = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                // ✅ Add headers for downstream services
                ServerWebExchange modifiedExchange = exchange.mutate()
                        .request(exchange.getRequest().mutate()
                                .header("X-User-Id", String.valueOf(userId))
                                .header("X-User-Email", email)
                                .header("X-User-Role", role)
                                .build())
                        .build();

                return chain.filter(modifiedExchange);

            } catch (Exception e) {
                return onError(exchange, "Token validation failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        };
    }

    /**
     * Public endpoints (no authentication required)
     */
    private boolean isPublicPath(String path, String method) {
        // Only GET requests are public for operators and plans
        if (path.equals("/operators") || 
            (path.startsWith("/operators/") && path.endsWith("/plans")) ||
            path.equals("/operators/count") || 
            path.equals("/operators/plans/count")) {
            return "GET".equalsIgnoreCase(method);
        }

        return path.equals("/users/login") ||
               path.equals("/users/register") ||
               path.equals("/users/forgot-password") ||
               path.equals("/users/reset-password") ||
               path.equals("/users/count") ||
               path.equals("/recharges/count") ||
               path.contains("/swagger-ui") ||
               path.contains("/v3/api-docs") ||
               path.contains("/swagger-resources") ||
               path.startsWith("/actuator");
    }

    /**
     * Error response handler
     */
    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"error\": \"%s\", \"status\": %d}", message, status.value());

        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(body.getBytes()))
        );
    }
}