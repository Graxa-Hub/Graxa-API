package com.Graxa_API.Graxa_API.Exception;

public class LocalNaoEncontradoException extends RuntimeException {
    public LocalNaoEncontradoException(Long id) {
        super("Local não encontrado com ID: " + id);
    }
}
