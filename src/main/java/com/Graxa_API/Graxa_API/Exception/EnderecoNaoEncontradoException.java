package com.Graxa_API.Graxa_API.Exception;

public class EnderecoNaoEncontradoException extends RuntimeException {
    public EnderecoNaoEncontradoException(Long id) {
        super("Endereço não encontrado com ID: " + id);
    }
}
