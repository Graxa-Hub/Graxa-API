package com.Graxa_API.Graxa_API.core.exception;

public class EmailNaoEncontradoException extends RuntimeException {
    public EmailNaoEncontradoException() {
        super("E-mail inválido ou não cadastrado.");
    }
}