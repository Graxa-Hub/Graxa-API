package com.Graxa_API.Graxa_API.dto.credencialUsuarioDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record CredencialUsuarioDetailsDto(

        @NotBlank(message = "O nome de usuário é obrigatório")
        String nomeUsuario,

        @NotNull(message = "O ID do usuário é obrigatório")
        Long usuarioId,

        @NotBlank(message = "O email é obrigatório")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        String senha

) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // você pode retornar roles futuramente
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;  // ✅ CORRIGIDO: Retorna email em vez de nomeUsuario
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}