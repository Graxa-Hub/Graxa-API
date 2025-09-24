package com.Graxa_API.Graxa_API.Exception;

public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException() {
        super(
                "Email já cadastrado"
        );
    }
}
