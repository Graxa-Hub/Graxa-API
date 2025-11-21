package com.Graxa_API.Graxa_API.dto.ShowDto;

import com.Graxa_API.Graxa_API.Entity.Evento.ShowEntity;
import com.Graxa_API.Graxa_API.dto.BandaDto.ResponseBandaDto;
import com.Graxa_API.Graxa_API.dto.LocalDto.ResponseLocalDto;
import com.Graxa_API.Graxa_API.dto.TurneDto.ResponseTurneDto;
import com.Graxa_API.Graxa_API.dto.UsuarioDto.ResponseUsuarioDto;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.ResponseAlocacaoDto;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseShowDto(
        Long id,
        String nomeEvento,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        String descricao,
        ResponseTurneDto turne,
        ResponseLocalDto local,
        ResponseUsuarioDto responsavelEvento,
        List<ResponseBandaDto> bandas,
        List<ResponseAlocacaoDto> alocacoes // ✅ novo campo
) {
    public static ResponseShowDto toResponse(ShowEntity entity) {
        return new ResponseShowDto(
                entity.getId(),
                entity.getNomeEvento(),
                entity.getDataInicio(),
                entity.getDataFim(),
                entity.getDescricao(),
                ResponseTurneDto.toResponse(entity.getTurne()),
                ResponseLocalDto.toResponse(entity.getLocal()),
                ResponseUsuarioDto.toResponse(entity.getResponsavelEvento()),
                ResponseBandaDto.toResponse(entity.getBandas()),
                ResponseAlocacaoDto.toResponse(entity.getAlocacoes()) // ✅ converte lista
        );
    }

    public static List<ResponseShowDto> toResponse(List<ShowEntity> entities) {
        return entities.stream()
                .map(ResponseShowDto::toResponse)
                .toList();
    }
}
