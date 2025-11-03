package com.Graxa_API.Graxa_API.dto.TelefoneDto;

import com.Graxa_API.Graxa_API.Enums.TipoTelefone;

public record ResponseTelefoneDto(
        Long id,
        TipoTelefone tipoTelefone,
        String numeroTelefone,
        Long usuarioId
) {
    public static ResponseTelefoneDto toResponse(com.Graxa_API.Graxa_API.Entity.TelefoneEntity entity) {
        return new ResponseTelefoneDto(
                entity.getId(),
                entity.getTipoTelefone(),
                entity.getNumeroTelefone(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null
        );
    }
}
