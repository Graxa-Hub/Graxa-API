package com.Graxa_API.Graxa_API.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.IOException;

@Component
public class AutenticacaoEntryPoint implements AuthenticationEntryPoint {

    @Lazy
    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // Aplica CORS headers manualmente — sendError() pode bypassar o filtro CORS
        // causando respostas com wildcard '*' em origens não autorizadas
        String origin = request.getHeader("Origin");
        if (origin != null) {
            CorsConfiguration cors = corsConfigurationSource.getCorsConfiguration(request);
            if (cors != null && cors.checkOrigin(origin) != null) {
                // Só adiciona o header se a origem estiver na lista de permitidas
                response.setHeader("Access-Control-Allow-Origin", origin);
                response.setHeader("Access-Control-Allow-Credentials", "true");
            }
            // Origem não permitida → nenhum header adicionado (bloqueada implicitamente)
        }

        response.setContentType("application/json;charset=UTF-8");

        if (authException.getClass().equals(BadCredentialsException.class) ||
                authException.getClass().equals(InsufficientAuthenticationException.class)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"erro\": \"Não autenticado\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"erro\": \"Acesso negado\"}");
        }
    }
}