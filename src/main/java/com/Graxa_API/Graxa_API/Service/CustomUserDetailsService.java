package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.CredenciaisUsuarioEntity;
import com.Graxa_API.Graxa_API.Repository.CredenciaisUsuarioRepository;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.CredencialUsuarioDetailsDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


public class CustomUserDetailsService implements UserDetailsService {

    private final CredenciaisUsuarioRepository repository;

    public CustomUserDetailsService(CredenciaisUsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String identificador) throws UsernameNotFoundException {

        CredenciaisUsuarioEntity usuario =
                repository.findByNomeUsuarioOrEmail(identificador, identificador)
                        .orElseThrow(() ->
                                new UsernameNotFoundException("Usuário não encontrado: " + identificador));

        return new CredencialUsuarioDetailsDto(
                usuario.getNomeUsuario(),
                usuario.getUsuario().getId(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getUsuario().getTipoUsuario()  // ← adiciona esse
        );
    }
}
