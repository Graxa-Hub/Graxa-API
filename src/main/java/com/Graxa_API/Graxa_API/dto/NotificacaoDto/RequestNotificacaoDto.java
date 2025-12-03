package com.Graxa_API.Graxa_API.dto.NotificacaoDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestNotificacaoDto(
        @NotNull(message = "ID do colaborador é obrigatório")
        Long colaboradorId,

        @NotBlank(message = "Mensagem é obrigatória")
        @Size(max = 500, message = "Mensagem deve ter no máximo 500 caracteres")
        String mensagem,

        @NotBlank(message = "Tipo é obrigatório")
        @Size(max = 100, message = "Tipo deve ter no máximo 100 caracteres")
        String tipo,

        Long alocacaoId
) {
}