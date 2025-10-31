package com.Graxa_API.Graxa_API.dto.UsuarioDto;

import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Enums.TipoUsuario;
import com.Graxa_API.Graxa_API.dto.EnderecoDto.ResponseEnderecoDto;

import java.time.LocalDate;
import java.util.List;

public record ResponseDetalhesUsuarioDto(
        Long id,
        String nome,
        LocalDate dataNascimento,
        String cpf,
        TipoUsuario tipoUsuario,
        Boolean ativo,
        String email,
        ResponseEnderecoDto endereco,
        List<String> telefones,
        List<String> acoes
) {
    public static ResponseDetalhesUsuarioDto toResponse(ColaboradorEntity usuario) {
        ResponseEnderecoDto enderecoDto = usuario.getEndereco() != null
                ? ResponseEnderecoDto.toResponse(usuario.getEndereco())
                : null;

        List<String> telefonesDto = usuario.getTelefones() != null
                ? usuario.getTelefones().stream().map(t -> t.getNumeroTelefone()).toList()
                : List.of();

        List<String> acoesDto = usuario.getAcoes() != null
                ? usuario.getAcoes().stream().map(a -> a.getTipoAcao().name()).toList()
                : List.of();

        return new ResponseDetalhesUsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getDataNascimento(),
                usuario.getCpf(),
                usuario.getTipoUsuario(),
                usuario.getAtivo(),
                usuario.getCredenciais() != null ? usuario.getCredenciais().getEmail() : null,
                enderecoDto,
                telefonesDto,
                acoesDto
        );
    }

    public static List<ResponseDetalhesUsuarioDto> toResponse(List<ColaboradorEntity> usuarios) {
        return usuarios.stream()
                .map(ResponseDetalhesUsuarioDto::toResponse)
                .toList();
    }

}
