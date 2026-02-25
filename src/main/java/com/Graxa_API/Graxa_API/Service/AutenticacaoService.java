package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.CredenciaisUsuarioEntity;
import com.Graxa_API.Graxa_API.Repository.CredenciaisUsuarioRepository;
import com.Graxa_API.Graxa_API.dto.credencialUsuarioDto.CredencialUsuarioDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private CredenciaisUsuarioRepository repository;

    @Cacheable(value = "users", key = "#identificador")
    @Override
    public UserDetails loadUserByUsername(String identificador) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername chamado para: " + identificador);
        Optional<CredenciaisUsuarioEntity> credencial = repository.findByEmail(identificador);

        if (credencial.isEmpty()) {
            credencial = repository.findByNomeUsuario(identificador);
        }

        return credencial
                .map(c -> new CredencialUsuarioDetailsDto(
                        c.getEmail(),  // ← USA EMAIL em vez de nomeUsuario
                        c.getUsuario().getId(),
                        c.getEmail(),
                        c.getSenha(),
                        c.getRoles()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + identificador));
    }
}
