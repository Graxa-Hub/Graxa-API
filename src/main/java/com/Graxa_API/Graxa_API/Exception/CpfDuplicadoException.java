package com.Graxa_API.Graxa_API.Exception;

import org.hibernate.validator.constraints.br.CPF;

public class CpfDuplicadoException extends RuntimeException {
    public CpfDuplicadoException(@CPF(message = "CPF inválido") String cpf) {
        super("CPF Já cadastrado");
    }
}

