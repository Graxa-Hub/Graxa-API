package com.Graxa_API.Graxa_API.core.exception;

public class CodigoNaoSolicitadoException extends RuntimeException {
    public CodigoNaoSolicitadoException() {
        super("Nenhum código de recuperação foi solicitado para este e-mail.");
    }
}