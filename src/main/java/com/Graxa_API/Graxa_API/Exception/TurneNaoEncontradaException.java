package com.Graxa_API.Graxa_API.Exception;

public class TurneNaoEncontradaException extends RuntimeException {
    public TurneNaoEncontradaException(Object identificador) {
        super("Turnê não encontrada: " + identificador);
    }
}
