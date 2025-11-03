package com.Graxa_API.Graxa_API.dto.credencialUsuarioDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para login de usuário")
public record RequestLoginDto(

        @NotBlank(message = "O identificador (nome de usuário ou e-mail) é obrigatório")
        @Schema(description = "Identificador do usuário (e-mail ou nome de usuário)", example = "usuario@email.com")
        String identificador,

        @NotBlank(message = "A senha é obrigatória")
        @Schema(description = "Senha do usuário", example = "senhaSegura123")
        String senha
) { }
