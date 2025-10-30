package com.Graxa_API.Graxa_API.dto.UsuarioDto;

import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Enums.TipoUsuario;
import com.Graxa_API.Graxa_API.dto.EnderecoDto.ResponseEnderecoDto;


import java.time.LocalDate;
import java.util.List;

public record ResponseUsuarioDto(
        Long id,
        String nome,
        LocalDate dataNascimento,
        String cpf,
        TipoUsuario tipoUsuario,
        Boolean ativo
) {
    public static ResponseUsuarioDto toResponse(ColaboradorEntity usuario) {
        ResponseEnderecoDto enderecoDto = usuario.getEndereco() != null
                ? ResponseEnderecoDto.toResponse(usuario.getEndereco())
                : null;


        return new ResponseUsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getDataNascimento(),
                usuario.getCpf(),
                usuario.getTipoUsuario(),
                usuario.getAtivo()
        );
    }

    public static List<ResponseUsuarioDto> toResponse(List<ColaboradorEntity> usuarios) {
        return usuarios.stream()
                .map(ResponseUsuarioDto::toResponse)
                .toList();
    }
}
