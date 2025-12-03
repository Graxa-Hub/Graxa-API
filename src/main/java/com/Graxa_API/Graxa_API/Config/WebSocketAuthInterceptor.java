package com.Graxa_API.Graxa_API.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketAuthInterceptor.class);

    @Autowired
    private GerenciadorTokenJwt jwtTokenManager;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        // Pegar token do query parameter ou header
        String token = extractToken(request);

        if (token != null) {
            try {
                String username = jwtTokenManager.getUsernameFromToken(token);

                if (username != null && jwtTokenManager.validaTokenSomente(token, username)) {
                    // Armazena o username na sessão WebSocket
                    attributes.put("username", username);
                    attributes.put("token", token);
                    logger.info("WebSocket connection authenticated for user: {}", username);
                    return true;
                }
            } catch (Exception e) {
                logger.error("JWT token validation failed: {}", e.getMessage());
            }
        }

        logger.warn("WebSocket connection rejected - invalid or missing token");
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // Implementação opcional
    }

    private String extractToken(ServerHttpRequest request) {
        // Primeiro tenta pegar do query parameter
        String query = request.getURI().getQuery();
        if (query != null && query.contains("token=")) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("token=")) {
                    return param.substring(6); // Remove "token="
                }
            }
        }

        // Se não encontrou no query, tenta no header
        List<String> authHeaders = request.getHeaders().get("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String authHeader = authHeaders.get(0);
            if (authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
        }

        return null;
    }
}