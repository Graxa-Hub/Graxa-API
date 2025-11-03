package com.Graxa_API.Graxa_API.dto.EnderecoDto;

import com.Graxa_API.Graxa_API.Entity.EnderecoEntity;
import com.Graxa_API.Graxa_API.Enums.TipoEndereco;

import java.util.List;

public record ResponseEnderecoDto(
        Long id,
        TipoEndereco tipoEndereco,
        String cep,
        String logradouro,
        String bairro,
        Integer numero,
        String complemento,
        String cidade,
        String estado,
        String pais
) {
    public static ResponseEnderecoDto toResponse(EnderecoEntity endereco) {
        return new ResponseEnderecoDto(
                endereco.getId(),
                endereco.getTipoEndereco(),
                endereco.getCep(),
                endereco.getLogradouro(),
                endereco.getBairro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getPais()
        );
    }

    public static List<ResponseEnderecoDto> toResponse(List<EnderecoEntity> entities) {
        return entities.stream()
                .map(ResponseEnderecoDto::toResponse)
                .toList();
    }

}
