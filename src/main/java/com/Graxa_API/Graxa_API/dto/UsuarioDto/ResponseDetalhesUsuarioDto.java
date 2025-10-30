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
        List<String> bandas,
        List<String> acoes
) {
    public static ResponseDetalhesUsuarioDto toResponse(ColaboradorEntity usuario) {
        ResponseEnderecoDto enderecoDto = usuario.getEndereco() != null
                ? ResponseEnderecoDto.toResponse(usuario.getEndereco())
                : null;


        return new ResponseDetalhesUsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getDataNascimento(),
                usuario.getCpf(),
                usuario.getTipoUsuario(),
                usuario.getAtivo(),
                usuario.getCredenciais() != null ? usuario.getCredenciais().getEmail() : null,
                enderecoDto,
                usuario.getTelefones() != null
                        ? usuario.getTelefones().stream().map(t -> t.getNumeroTelefone()).toList()
                        : null,
                usuario.getBandas() != null
                        ? usuario.getBandas().stream().map(b -> b.getNome()).toList()
                        : null,
                usuario.getAcoes() != null
                        ? usuario.getAcoes().stream().map(a -> a.getTipoAcao().name()).toList()
                        : null
        );
    }
}
