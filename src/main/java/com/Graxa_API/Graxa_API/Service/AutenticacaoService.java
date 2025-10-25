package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.CredenciaisUsuarioEntity;
import com.Graxa_API.Graxa_API.Repository.CredenciaisUsuarioRepository;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.CredencialUsuarioDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private CredenciaisUsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String identificador) throws UsernameNotFoundException {
        Optional<CredenciaisUsuarioEntity> credencial = repository.findByEmail(identificador);

        if (credencial.isEmpty()) {
            credencial = repository.findByNomeUsuario(identificador);
        }

        return credencial
                .map(c -> new CredencialUsuarioDetailsDto(
                        c.getNomeUsuario(),
                        c.getUsuario().getId(),
                        c.getEmail(),
                        c.getSenha()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + identificador));
    }
}
