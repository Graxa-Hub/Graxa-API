package com.Graxa_API.Graxa_API.Exception;

public class UsuariosNaoEncontradosException extends RuntimeException {
    public UsuariosNaoEncontradosException() {
        super("Não há usuários cadastrados");
    }
}
