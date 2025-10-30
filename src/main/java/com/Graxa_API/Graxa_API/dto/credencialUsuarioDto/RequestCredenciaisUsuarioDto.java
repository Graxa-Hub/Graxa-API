package com.Graxa_API.Graxa_API.dto.credencialUsuarioDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestCredenciaisUsuarioDto (
        @NotBlank(message = "O nome de usuario é obrigatorio")
        String nomeUsuario,

        @NotNull(message = "O usuario é obrigatório")
        Long usuarioId,
        @NotBlank(message = "O Email é obrigatório")
        String email,
        @NotBlank(message = "A senha é obrigatória")
        String senha
){ }
