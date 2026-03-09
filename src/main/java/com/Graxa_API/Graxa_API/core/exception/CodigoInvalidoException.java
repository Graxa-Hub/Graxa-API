package com.Graxa_API.Graxa_API.core.exception;

public class CodigoInvalidoException extends RuntimeException {
    public CodigoInvalidoException() {
        super("Código inválido.");
    }
}