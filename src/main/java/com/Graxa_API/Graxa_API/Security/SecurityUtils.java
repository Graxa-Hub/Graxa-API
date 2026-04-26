package com.Graxa_API.Graxa_API.Security;

import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Repository.CredenciaisUsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final CredenciaisUsuarioRepository credenciaisRepository;

    public SecurityUtils(CredenciaisUsuarioRepository credenciaisRepository) {
        this.credenciaisRepository = credenciaisRepository;
    }

    public ColaboradorEntity getUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return credenciaisRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"))
                .getUsuario();
    }
}