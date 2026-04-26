package com.Graxa_API.Graxa_API.dto.credencialUsuarioDto;

import com.Graxa_API.Graxa_API.Enums.TipoUsuario;
import com.Graxa_API.Graxa_API.Security.RoleMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public record CredencialUsuarioDetailsDto(
        String nomeUsuario,
        Long usuarioId,
        String email,
        String senha,
        TipoUsuario tipoUsuario
) implements UserDetails {

    // Necessários por causa da interface UserDetails —
    // os nomes não batem com os campos do record, então precisam ser explícitos
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return RoleMapper.toAuthorities(tipoUsuario); }
    @Override public String getPassword() { return senha; }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}