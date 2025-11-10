package com.Graxa_API.Graxa_API.dto.RepresentanteDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record RequestRepresentanteDto(
        @Schema(description = "Nome completo do usuário", example = "Michelle Marcelino")
        @NotBlank(message = "Preencha o nome")
        String nome,

        @Schema(description = "Email do usuário", example = "michelle.marcelino@example.com")
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Email inválido")
        String email
) {
}
