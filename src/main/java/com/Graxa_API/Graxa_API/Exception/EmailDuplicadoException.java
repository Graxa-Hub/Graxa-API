package com.Graxa_API.Graxa_API.Exception;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException(@NotBlank(message = "O email é obrigatório") @Email(message = "Email inválido") String email) {
        super(
                "Email já cadastrado"
        );
    }
}
