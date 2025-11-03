package com.Graxa_API.Graxa_API.Exception;

public class CredencialNaoEncontradaException extends RuntimeException {
    public CredencialNaoEncontradaException(Long id) {
        super("Credencial com ID " + id + " não encontrada.");
    }
}
