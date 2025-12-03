package com.Graxa_API.Graxa_API.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketAuthInterceptor webSocketAuthInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilita um broker simples para enviar mensagens
        config.enableSimpleBroker("/topic", "/queue");
        // Define o prefixo para mensagens destinadas ao servidor
        config.setApplicationDestinationPrefixes("/app");
        // Prefixo para mensagens pessoais
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // ✅ CORRIGIR: Permitir origens específicas do frontend
        registry.addEndpoint("/ws/notificacoes")
                .setAllowedOriginPatterns("*") // ✅ Permite todas as origens
                // Ou específico: .setAllowedOrigins("http://localhost:3000", "http://localhost:5173")
                .addInterceptors(webSocketAuthInterceptor)
                .withSockJS();

        // WebSocket nativo sem SockJS
        registry.addEndpoint("/ws/notificacoes")
                .setAllowedOriginPatterns("*") // ✅ Permite todas as origens
                .addInterceptors(webSocketAuthInterceptor);
    }
}