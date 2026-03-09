package com.Graxa_API.Graxa_API.core.exception;

public class CodigoExpiradoException extends RuntimeException {
    public CodigoExpiradoException() {
        super("Código expirado. Solicite um novo.");
    }
}