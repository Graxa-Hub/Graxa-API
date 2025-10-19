package com.Graxa_API.Graxa_API.Exception;

public class SenhaInvalidaException extends RuntimeException {
    public SenhaInvalidaException() {
        super("Senha inválida.");
    }
}
