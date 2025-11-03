package com.Graxa_API.Graxa_API.Exception;

public class TurneJaExistenteException extends RuntimeException {
    public TurneJaExistenteException(String message) {
        super("Turnê, "+message+" Já existe.");
    }
}
