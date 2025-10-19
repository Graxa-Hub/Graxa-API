package com.Graxa_API.Graxa_API.Exception;

public class NomeUsuarioDuplicadoException extends RuntimeException {
    public NomeUsuarioDuplicadoException(String nomeUsuario) {
        super("Nome de usuário '" + nomeUsuario + "' já está em uso.");
    }
}
