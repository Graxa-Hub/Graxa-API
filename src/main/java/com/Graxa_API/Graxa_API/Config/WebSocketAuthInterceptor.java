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
                logger.info("🔐 Token extraído do WebSocket");

                String username = jwtTokenManager.getUsernameFromToken(token);

                // ✅ LOG CRÍTICO - Mostra qual username foi decodificado
                logger.info("👤 Username decodificado do JWT: '{}'", username);

                if (username != null && jwtTokenManager.validaTokenSomente(token, username)) {
                    // Armazena o username na sessão WebSocket
                    attributes.put("username", username);
                    attributes.put("token", token);
                    logger.info("✅ WebSocket connection authenticated for user: {}", username);
                    return true;
                } else {
                    logger.warn("⚠️ Username NULL ou token inválido");
                }
            } catch (Exception e) {
                logger.error("❌ JWT token validation failed: {}", e.getMessage(), e);
            }
        } else {
            logger.warn("⚠️ Token não encontrado na requisição WebSocket");
        }

        logger.warn("🚫 WebSocket connection rejected - invalid or missing token");
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
                    String token = param.substring(6); // Remove "token="
                    logger.debug("🔑 Token extraído do query parameter");
                    return token;
                }
            }
        }

        // Se não encontrou no query, tenta no header
        List<String> authHeaders = request.getHeaders().get("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String authHeader = authHeaders.get(0);
            if (authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                logger.debug("🔑 Token extraído do header Authorization");
                return token;
            }
        }

        return null;
    }
}