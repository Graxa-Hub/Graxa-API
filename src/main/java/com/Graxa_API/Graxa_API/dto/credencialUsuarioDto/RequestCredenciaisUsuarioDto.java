package com.Graxa_API.Graxa_API.dto.credencialUsuarioDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para criação de credenciais de acesso do usuário")
public record RequestCredenciaisUsuarioDto(

        @Schema(description = "Nome de usuário para login", example = "gabriel.sousa")
        @NotBlank(message = "O nome de usuario é obrigatorio")
        String nomeUsuario,

        @Schema(description = "ID do usuário associado às credenciais", example = "123")
        @NotNull(message = "O usuario é obrigatório")
        Long usuarioId,

        @Schema(description = "Email do usuário", example = "gabriel.sousa@example.com")
        @NotBlank(message = "O Email é obrigatório")
        String email,

        @Schema(description = "Senha de acesso", example = "senhaSegura123")
        @NotBlank(message = "A senha é obrigatória")
        String senha

) {}
