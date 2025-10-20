package com.Graxa_API.Graxa_API.dto.credencialUsuarioDto;

import jakarta.validation.constraints.NotBlank;

public record RequestLoginDto(
        @NotBlank(message = "O identificador (nome de usuário ou e-mail) é obrigatório")
        String identificador,

        @NotBlank(message = "A senha é obrigatória")
        String senha
) { }
