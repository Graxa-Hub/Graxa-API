package com.Graxa_API.Graxa_API.Exception;

public class TelefoneNaoEncontradoException extends RuntimeException {
    public TelefoneNaoEncontradoException(Long id) {

        super("Telefone de ID: "+id+ " Não encontrado");
    }
}
