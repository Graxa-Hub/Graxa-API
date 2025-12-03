package com.Graxa_API.Graxa_API.dto.NotificacaoDto;

import com.Graxa_API.Graxa_API.Entity.NotificacaoEntity;
import com.Graxa_API.Graxa_API.dto.AlocacaoDto.ResponseAlocacaoDto;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseNotificacaoDto(
        Long id,
        String mensagem,
        String tipo,
        Boolean lida,
        LocalDateTime dataHoraCriacao,
        ResponseAlocacaoDto alocacao
) {
    public static ResponseNotificacaoDto toResponse(NotificacaoEntity entity) {
        if (entity == null) return null;

        return new ResponseNotificacaoDto(
                entity.getId(),
                entity.getMensagem(),
                entity.getTipo(),
                entity.isLida(),
                entity.getDataCriacao(),
                entity.getAlocacao() != null ? ResponseAlocacaoDto.toResponse(entity.getAlocacao()) : null
        );
    }

    public static List<ResponseNotificacaoDto> toResponse(List<NotificacaoEntity> entities) {
        return entities.stream()
                .map(ResponseNotificacaoDto::toResponse)
                .toList();
    }
}