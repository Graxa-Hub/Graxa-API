package com.Graxa_API.Graxa_API.Exception;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException(@NotBlank @Email String email) {
        super(
                "Email já cadastrado"
        );
    }
}
