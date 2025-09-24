package com.Graxa_API.Graxa_API.Exception;

public class CpfDuplicadoException extends RuntimeException {
    public CpfDuplicadoException() {
        super("CPF Já cadastrado");
    }
}

