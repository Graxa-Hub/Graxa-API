package com.Graxa_API.Graxa_API.dto.credencialUsuarioDto;

import com.Graxa_API.Graxa_API.Entity.CredenciaisUsuarioEntity;

import java.util.List;

public record ResponseCredenciaisUsuarioDto(
        Long id,
        String nomeUsuario,
        String email,
        String senha,
        Long usuarioId
) {
    public static ResponseCredenciaisUsuarioDto toResponse(CredenciaisUsuarioEntity credenciais) {
        return new ResponseCredenciaisUsuarioDto(
                credenciais.getId(),
                credenciais.getNomeUsuario(),
                credenciais.getEmail(),
                credenciais.getSenha(),
                credenciais.getUsuario() != null ? credenciais.getUsuario().getId() : null
        );
    }

    public static List<ResponseCredenciaisUsuarioDto> toResponse(List<CredenciaisUsuarioEntity> lista) {
        return lista.stream()
                .map(ResponseCredenciaisUsuarioDto::toResponse)
                .toList();
    }
}
