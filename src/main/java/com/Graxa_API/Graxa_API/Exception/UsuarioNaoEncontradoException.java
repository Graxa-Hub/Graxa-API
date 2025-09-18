package com.Graxa_API.Graxa_API.Exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(Long id) {

        super("Usuário com ID: "+id+ " Não encontrado!");
    }
}
