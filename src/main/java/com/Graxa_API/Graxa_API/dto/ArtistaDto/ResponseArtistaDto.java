package com.Graxa_API.Graxa_API.dto.ArtistaDto;

import com.Graxa_API.Graxa_API.Entity.Usuario.ArtistaEntity;
import com.Graxa_API.Graxa_API.Enums.TipoUsuario;

import java.time.LocalDate;
import java.util.List;

public record ResponseArtistaDto(
        Long id,
        String nome,
        String cpf,
        Boolean ativo
) {
    public static ResponseArtistaDto toResponse(ArtistaEntity artista) {
        return new ResponseArtistaDto(
                artista.getId(),
                artista.getNome(),
                artista.getCpf(),
                artista.getAtivo()
        );
    }

    public static List<ResponseArtistaDto> toResponse(List<ArtistaEntity> artistas) {
        return artistas.stream()
                .map(ResponseArtistaDto::toResponse)
                .toList();
    }
}
