package com.Graxa_API.Graxa_API.handler;

import com.Graxa_API.Graxa_API.Entity.CredenciaisUsuarioEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.UsuarioEntity;
import com.Graxa_API.Graxa_API.Repository.CredenciaisUsuarioRepository;
import com.Graxa_API.Graxa_API.Utils.AuditRequestContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuditLogInterceptor implements HandlerInterceptor {

    private final CredenciaisUsuarioRepository credenciaisRepo;

    public AuditLogInterceptor(CredenciaisUsuarioRepository credenciaisRepo) {
        this.credenciaisRepo = credenciaisRepo;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // Captura usuário logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ColaboradorEntity usuario = null;

        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();

            // Se o principal for String (username)
            if (principal instanceof String) {
                String email = (String) principal;
                CredenciaisUsuarioEntity credenciais = credenciaisRepo.findByEmail(email).orElse(null);
                if (credenciais != null) {
                    usuario = credenciais.getUsuario();
                }
            }
            // Se for UserDetails
            else if (principal instanceof UserDetails) {
                String email = ((UserDetails) principal).getUsername();
                CredenciaisUsuarioEntity credenciais = credenciaisRepo.findByEmail(email).orElse(null);
                if (credenciais != null) {
                    usuario = credenciais.getUsuario();
                }
            }
        }

        // Captura IP
        String ip = getClientIp(request);

        // Captura endpoint e método
        String endpoint = request.getRequestURI();
        String method = request.getMethod();

        // Só cria contexto em operações críticas
        if (method.equals("POST") || method.equals("PUT") || method.equals("DELETE")) {
            AuditRequestContext ctx = new AuditRequestContext();
            ctx.setIp(ip);
            ctx.setEndpoint(endpoint);
            ctx.setMethod(method);
            ctx.setUsuario(usuario);
            AuditRequestContext.set(ctx);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        AuditRequestContext.clear();
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return (ip == null || ip.isEmpty()) ? request.getRemoteAddr() : ip;
    }
}

