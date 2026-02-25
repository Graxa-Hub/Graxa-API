package com.Graxa_API.Graxa_API.dto.credencialUsuarioDto;

import com.Graxa_API.Graxa_API.Entity.CredenciaisUsuarioEntity;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.ResponseUsuarioDto;

import java.util.Set;

public record ResponseLoginDto(
        String token,
        ResponseUsuarioDto usuario,
        Set<String> roles
) {
    public static ResponseLoginDto toResponse(CredenciaisUsuarioEntity credenciais, String token) {
        ResponseUsuarioDto usuarioDto = ResponseUsuarioDto.toResponse(credenciais.getUsuario());
        return new ResponseLoginDto(token, usuarioDto, credenciais.getRoles());
    }
}
